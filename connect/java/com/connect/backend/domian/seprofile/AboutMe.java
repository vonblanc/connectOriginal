package com.connect.backend.domian.seprofile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.connect.backend.domian.utilities.queries.ConnectServiceQuery;
import com.connect.backend.domian.utilities.queries.GeneralQueryUtils;
import com.connect.backend.forms.AboutMeForms;
import com.google.appengine.repackaged.com.google.api.client.util.Preconditions;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;



public class AboutMe{
	public static final Logger logger=Logger.getLogger(AboutMe.class.getName());
      
	private String profileName;  //profile name 
	
	
    private double serviceTravelDistance;   //distance SE is wiling to travel mandatory
	
	private List <String> serviceType;            //service type/types of the SE, this should be a list feature****
	

	private List<String> SERVICETYPE;      //stores the normalized version
	
	private String phoneNumber;             //SE phone number mandatory
	
	
	private String literalAddress;           //SE literal base address without postcode mandatory
	
	private String postCode;                 //SE postcode used for location purposes mandatory
	
	//maybe pin point location coordinates in future
	
	private String profileDescription;       //profile description 
	
	private String startTime;                  //start time
	
	private String finishTime;                 //finish time 
	
	private String profilePicUrl;            //url
	
	
		
	public AboutMe(){};
	
	public AboutMe(AboutMeForms aboutMeForms) throws ParseException
	{
		//logger.log(Level.SEVERE, "This is the list displayed: "+aboutMeForms.getServiceType().toString());
		
     	Preconditions.checkNotNull(aboutMeForms.getProfileName(), "Enter Profile Name");
     	Preconditions.checkNotNull(aboutMeForms.getServiceType(), "Enter service type");
     	Preconditions.checkNotNull(aboutMeForms.getPhoneNumber(), "you have to give a phone number");
     	Preconditions.checkNotNull(aboutMeForms.getLiteralAddress(), "you have to give a litreal address");  //essentials required to create aboutme class
		Preconditions.checkNotNull(aboutMeForms.getPostCode(), "you have to give a postcode");
	    
		this.profileName=aboutMeForms.getProfileName();
		this.phoneNumber=aboutMeForms.getPhoneNumber();
		this.literalAddress=aboutMeForms.getLiteralAddress();
		this.postCode=aboutMeForms.getPostCode();
        this.serviceType=aboutMeForms.getServiceType();
        
        this.profileDescription=aboutMeForms.getProfileDescription();
		
        String strtTime=aboutMeForms.getStartTime();
	  	String finshTime=aboutMeForms.getFinishTime();         //get starttime and finishtime strings from forms 
	  	
	  	if(strtTime!=null&&finshTime!=null)
	  	{
	  	 Date strtTimeDate=new SimpleDateFormat("HH:mm").parse(strtTime);  	  	  //if both start time and finish time not null parse string and create date instance 
		
	     Date finishTimeDate=new SimpleDateFormat("HH:mm").parse(finshTime);
	  	//}
	  	
	  	//catch(Exception e) {logger.log(Level.SEVERE, e.toString(), e);}
	  	
        startTime=new SimpleDateFormat("HH:mm").format(strtTimeDate);
        finishTime=new SimpleDateFormat("HH:mm").format(finishTimeDate);
        
        
	}  
	  	
	  	else
	  	{
	  		startTime="09:00";
	        finishTime="17:00";
	  	}
	 	
	  	
		
	}
	
	
	
	public void update(AboutMeForms aboutmeforms) throws ParseException
	{ 
     	Preconditions.checkNotNull(aboutmeforms.getProfileName(), "Enter Profile Name");
     	Preconditions.checkNotNull(aboutmeforms.getServiceType(), "Enter service type");
        Preconditions.checkNotNull(aboutmeforms.getPhoneNumber(), "you have to give a phone number");
        Preconditions.checkNotNull(aboutmeforms.getLiteralAddress(), "you have to give a litreal address");  //essentials required to create aboutme class	
        Preconditions.checkNotNull(aboutmeforms.getPostCode(), "you have to give a postcode");	
		

        
		this.phoneNumber=aboutmeforms.getPhoneNumber();
		this.literalAddress=aboutmeforms.getLiteralAddress();
		this.postCode=aboutmeforms.getPostCode();
        this.serviceType=aboutmeforms.getServiceType();
        this.profileName=aboutmeforms.getProfileName();
        
        
        this.profileDescription=aboutmeforms.getProfileDescription();


       // try{
	  	
        String strtTime=aboutmeforms.getStartTime();
	  	String finshTime=aboutmeforms.getFinishTime();         //get starttime and finishtime strings from forms 
	  	
        
		if(strtTime!=null&&finshTime!=null)
	  	{
	  	 Date strtTimeDate=new SimpleDateFormat("HH:mm").parse(strtTime);  	  	  //if both start time and finish time not null parse string and create date instance 
		
	     Date finishTimeDate=new SimpleDateFormat("HH:mm").parse(finshTime);
	  	//}
	  	
	  	//catch(Exception e) {logger.log(Level.SEVERE, e.toString(), e);}
	  	
        startTime=new SimpleDateFormat("HH:mm").format(strtTimeDate);
        finishTime=new SimpleDateFormat("HH:mm").format(finishTimeDate);
        
        
	}  
	  	
	  	else
	  	{
	  		startTime="09:00";
	        finishTime="17:00";
	  	}
	 	
	  	
	}
	
	
	
	public String getPhoneNumber(){return phoneNumber;}
	
	public List<String> getServiceType(){return serviceType;}
	
	public String getLiteralAddress(){return literalAddress;}
	
	public String getPostCode(){return postCode;}
	
	public String getStartTime() throws ParseException{return startTime;}
	                                                                                                                                            //return dates  if null return generic start and finish times
	public String getfinishTime() throws ParseException{return finishTime;}
	
	public String getProfileDescription(){return this.profileDescription;}

	public void setProfilePicUrl(String profilePicUrl){this.profilePicUrl=profilePicUrl;}   //profile pic url should not be set with constructor or update method should be set through a queue or cron job
	
	public String getProfilePicUrl(){return this.profilePicUrl;}
	
	public String getName(){return this.profileName;}
	
	
	

	
}