package com.connect.backend.domian.customerprofile;

import com.connect.backend.domian.seprofile.ServiceResp;
import com.googlecode.objectify.Key;

public class ServiceOrder
{
	
	private enum Status{
		
		REQ,RESP
		
	};
	
	private long id;
	
	private Key<Client> clientParentKey;
	
	private Status status;
	
	private String requestKey;
	
	private String responseKey;
	
	private double duration;         //duration of the service to be gotten from the response or request 
	
	private String servicePerson;     //service person key to be gotten from request or response 
	
	private String startDate;         //job start date to be gotten from request or response
	
	private String estEndDate;        //estimated end date to be gotten from response or request
	
	//private List<Appointment> appointments  //list of appointments 
	//private Inbox inbox;
	
	
	public ServiceOrder()
	{
		
		
	}
	
    public ServiceOrder(long id, ServiceResp serviceResp)
    {  
    	if(serviceResp!=null) this.status=Status.RESP;
    	
    	this.id=id;
    	
    	this.responseKey=serviceResp.getWebSafeServiceRespKey();  //job string service response key
    	this.duration=serviceResp.getDuration();                 //job duration 
    	this.servicePerson=serviceResp.getSenderKey();             //service person sending the response
    	this.startDate=serviceResp.getServiceStartDate();         //get the service start date
    	this.estEndDate=serviceResp.getEstEndDate();              //get estimated end date 
    }
    
	
	public ServiceOrder(long id, ServiceReq serviceReq)
	{
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
}