package com.connect.backend.domian.seprofile;

import com.connect.backend.domian.utilities.queries.ConnectServiceQuery;
import com.connect.backend.domian.utilities.queries.GeneralQueryUtils;
import com.connect.backend.forms.ServiceOfferedForm;
import com.google.api.client.util.Preconditions;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class ServiceOffered{
	@Id
	private long ID;
	
	private String serviceName;  //Service offered  details
	
	private double estTime;    //duration to carry out job
	
	
	private String serviceCategory;  //
	
	
	@Index
	private double servicePrice;
	
	
	private String serviceDescription; /// should be manadatory as customer needs to know what exactly this service offers
	
	@Parent
	private Key<Profile> profileKey;
	
	
	public ServiceOffered(){}
	
	public ServiceOffered(long Id,ServiceOfferedForm serviceOfferedForm, final String webSafeProfileKey)   //essentials that need to be initialized when a new product clas is created
	{
	  
		this.ID=Id;   // assign allocated ID
		
		Preconditions.checkNotNull(serviceOfferedForm.getServiceName());
		Preconditions.checkNotNull(serviceOfferedForm.getServicePrice());                        //check fields not null for important service info needed to create a new object 
        Preconditions.checkNotNull(serviceOfferedForm.getEstTime());
		
		this.serviceName=serviceOfferedForm.getServiceName();
		this.servicePrice=serviceOfferedForm.getServicePrice();
        this.serviceCategory=serviceOfferedForm.getServiceCategory();
        this.serviceDescription=serviceOfferedForm.getServiceDescription();
	    this.estTime=serviceOfferedForm.getEstTime();
	    
		profileKey=Key.create(webSafeProfileKey);
		
		
	}
	
	public void updateServiceOffered(ServiceOfferedForm serviceOfferedForm)
	{   
		Preconditions.checkNotNull(serviceOfferedForm.getServiceName());
		Preconditions.checkNotNull(serviceOfferedForm.getServicePrice());                        //check fields not null for important service info needed to create a new object 
        Preconditions.checkNotNull(serviceOfferedForm.getEstTime());

		
		this.serviceName=serviceOfferedForm.getServiceName();                                    //update service offered  details
		this.servicePrice=serviceOfferedForm.getServicePrice();
		this.serviceCategory=serviceOfferedForm.getServiceCategory();
	    this.serviceDescription=serviceOfferedForm.getServiceDescription();                     
	    this.estTime=serviceOfferedForm.getEstTime();

	}
	
    
	@ApiResourceProperty(ignored= AnnotationBoolean.TRUE)
    public Key<Profile> getParentKey(){return this.profileKey;}
	
	public String getServiceName(){return this.serviceName;}
	public String getServiceCategory(){return this.serviceCategory;}                              //get member variables 
	public double getServicePrice(){return this.servicePrice;}
	public double getEstTime(){return this.estTime;}
	public String getServiceDescription(){return this.serviceDescription==null ? "Service Offered needs a description":this.serviceDescription;}
	public String getWebSafeServiceOfferedKey(){return Key.create(this.profileKey,ServiceOffered.class,this.ID).getString();}
	
	
	@Override
	public boolean equals(Object obj){
		
		if(!(obj instanceof ServiceOffered)) return false;                                          //comparing to services by their websafeconferenceKey
		
		ServiceOffered serviceOffered=(ServiceOffered) obj;
		return this.getWebSafeServiceOfferedKey().equals(serviceOffered.getWebSafeServiceOfferedKey());
		
	}
	
}