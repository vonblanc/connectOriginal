package com.connect.backend.domian.seprofile;

import java.util.Date;
import java.util.List;

import com.connect.backend.domian.customerprofile.ServiceReq;
import com.connect.backend.domian.utilities.queries.GeneralQueryUtils;
import com.connect.backend.forms.ServiceRespForms;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.repackaged.com.google.api.client.util.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import static com.connect.backend.service.OfyService.ofy;

@Entity
public class ServiceResp{
	@Id
	long id;
	
	@Parent
	@ApiResourceProperty(ignored= AnnotationBoolean.TRUE)
	private Key<ServiceReq> parentKey;   //service request parent key 
	
	private List<String> suggestedServices;  //names of suggested services 
	
	private Key <Profile> senderKey;     //key of the profile sending response
	
	private String offerDescription;    //description of services and how they match customers unique request
	
	private double duration;              //appointment duration in hours 
	
	private String serviceResponseDate;    //time of response
	
	private String webSafeServiceRespKey;    //string version of key
	
	public ServiceResp(long id, long reqId,ServiceRespForms serviceRespForms) throws NotFoundException, ForbiddenException{
		
	this.id=id;
	this.parentKey=Key.create(ServiceReq.class, reqId);
	
	ServiceReq serviceReq=ofy().load().key(parentKey).now();                               //load
	
	if(serviceReq==null) {throw new NotFoundException("No such service request");}                  //check if serice request exist 
	
	
	List<String> recievers=serviceReq.getWebSafeServicePeopleKeys();                              //get list of profiles that the service request got sent to 
	
	if(!recievers.contains(senderKey.getString())) throw new NotFoundException("This Profile doesn't exist in the service request list"); //check if the profile was part of the original request send list
	
	
	this.serviceResponseDate=GeneralQueryUtils.TimeUtils.simpleDateFormat.format(new Date());
	
	if(!GeneralQueryUtils.TimeUtils.isLessThan(serviceResponseDate, serviceReq.getResponseDeadlineTime()))  throw new ForbiddenException("The response time is greater than the request deadline time");
	
	Preconditions.checkNotNull(serviceRespForms.getSuggestedServices(),"There are no suggested services in the response");
	Preconditions.checkNotNull(serviceRespForms.getOfferDescription(),"The description about how proposed services will meet unique needs not present");
	Preconditions.checkNotNull(serviceRespForms.getDuration(),"The duration of custom job not set");
	
	this.suggestedServices=serviceRespForms.getSuggestedServices();
	this.offerDescription=serviceRespForms.getOfferDescription();
	this.duration=serviceRespForms.getDuration();
	
	}
	
	
	
	
	
}