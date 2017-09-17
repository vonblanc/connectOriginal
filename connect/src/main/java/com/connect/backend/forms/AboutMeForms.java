package com.connect.backend.forms;

import java.util.List;

  public class AboutMeForms{
	  
   private String profileName;	  
	
   private String phoneNumber;
   
   public List<String> serviceType;
  
   	
    private String literalAddress;
	
	private String postCode;
		
	private String profileDescription;
	
	private String startTime;
	
	private String finishTime;
	
	
   public AboutMeForms(){};
	
	public String getPhoneNumber(){return phoneNumber;}
	public String getLiteralAddress(){return literalAddress;}
	public String getPostCode(){return  postCode;}
	public String getProfileDescription(){return profileDescription;}
	public String getStartTime(){return startTime;}
	public String getFinishTime(){return finishTime;}
	public List<String> getServiceType(){return serviceType;}
	public String getProfileName(){return profileName;}
	
	

}
 
