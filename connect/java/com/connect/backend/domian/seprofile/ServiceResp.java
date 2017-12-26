package com.connect.backend.domian.seprofile;

import java.util.Date;
import java.util.List;

import com.connect.backend.domian.customerprofile.ServiceReq;
import com.connect.backend.domian.utilities.queries.GeneralQueryUtils;
import com.connect.backend.forms.ServiceRespForms;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.Response;
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
	
	private String senderStringKey;     //key of the profile sending response
	
	private String offerDescription;    //description of scopes covered by selected services and how they match customers unique request
	
	private double duration;              //appointment duration in hours 
	
	private String serviceResponseDate;    //time of response
	
	private String serviceStartDate;       //service start date from service request  
	
	private String webSafeServiceRespKey;    //string version of key
	
	private String estEndDate;
	
	public ServiceResp(){}
	
	public ServiceResp(long id, ServiceReq serviceReq ,String webSafeProfileKey,ServiceRespForms serviceRespForms) throws NotFoundException, ForbiddenException{
		
	this.id=id;
	this.parentKey=Key.create(serviceReq.getWebSafeServiceReqKey());                      //create the serviceRequest parent Key
	this.serviceStartDate=serviceReq.getServiceStarDay();                //start day for service noted from serviceRequest
	this.senderStringKey=Key.create(webSafeProfileKey).getString();      //create the sender(Profile) string key 
	
	System.out.println("The key of the profile's "+senderStringKey);

	
	
	
	List<String> recievers=serviceReq.getWebSafeServicePeopleKeys();                              //get list of profiles that the service request got sent to 
	
	System.out.println("The list of people request was sent to  "+recievers.toString());

	
	
	if(!recievers.contains(this.senderStringKey)) throw new NotFoundException("This Profile doesn't exist in the service request list"); //check if the profile was part of the original request send list
	
	
	this.serviceResponseDate=GeneralQueryUtils.TimeUtils.simpleDateFormat.format(new Date()); //set the response date 
	
	if(!GeneralQueryUtils.TimeUtils.isLessThan(serviceResponseDate, serviceReq.getResponseDeadlineTime()))  throw new ForbiddenException("The response time is greater than the request deadline time");
	
	
	
	Preconditions.checkNotNull(serviceRespForms.getSuggestedServices(),"There are no suggested services in the response");
	Preconditions.checkNotNull(serviceRespForms.getOfferDescription(),"The description about how proposed services will meet unique needs not present");
	Preconditions.checkNotNull(serviceRespForms.getDuration(),"The duration of custom job not set");
	Preconditions.checkNotNull(serviceRespForms.getEstEndDate(), "Please provide the estimated end date of this service");
	
	
	this.suggestedServices=serviceRespForms.getSuggestedServices();
	this.offerDescription=serviceRespForms.getOfferDescription();     //set class variables 
	this.duration=serviceRespForms.getDuration();
	this.webSafeServiceRespKey=Key.create(parentKey,Response.class,this.id).getString();
	this.estEndDate=serviceRespForms.getEstEndDate();
	}
	
	public List<String> getSuggestedServices(){return suggestedServices;}
	public String getSenderKey(){return senderStringKey;}
	public String getOfferDescription(){return offerDescription;}
	public double getDuration(){return duration;}
	public String getServiceResponseDate(){return serviceResponseDate;}
	public String getWebSafeServiceRespKey(){return webSafeServiceRespKey;}
	public String getServiceStartDate(){return serviceStartDate;}
	public String getEstEndDate(){return estEndDate;}
}