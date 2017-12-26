package com.connect.backend.domian.customerprofile;
import com.connect.backend.domian.appusers.ConnectUser;
import com.connect.backend.domian.utilities.Financials;
import com.connect.backend.forms.ClientForms;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Client{
	
	@Id
	private long id;
	
	@Parent
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<ConnectUser> connectUserKey;  //connect User key 
	
	private String name;
	
	private String email;
	
	private String phoneNumber;
	
	private String literalAddress;
	
	private String postCode;
	
	private Financials financials;
	
	private String webSafeClientKey;
	
	
	public Client(){}
	
    public Client( long id,ClientForms clientForms,ConnectUser connectUser )
    {
        
    	this.id=id;
    	this.connectUserKey=Key.create(ConnectUser.class, connectUser.getId());
    	
    	Preconditions.checkNotNull(clientForms.getPostCode(), "Clients postcode can't be empty");      //ensure that the phone number and the postcode is entered 
    	Preconditions.checkNotNull(clientForms.getPhoneNumber(), "Clients phone number can't be empty");
    	
    	
    	this.name=connectUser.getName();        //get name and email from the connect user profile 
    	this.email=connectUser.getEmail();
    	
    	this.postCode=clientForms.getPostCode();            
    	this.literalAddress=clientForms.getLiteralAddress();
        this.phoneNumber=clientForms.getPhoneNumber();
    
        
      
        				
        this.webSafeClientKey=Key.create(connectUserKey,Client.class,this.id).getString();
    
    } 
	
	
	
public void updateClient(ClientForms clientForms)
{

	Preconditions.checkNotNull(clientForms.getPostCode(), "Clients postcode can't be empty");      //ensure that the phone number and the postcode is entered 
	Preconditions.checkNotNull(clientForms.getPhoneNumber(), "Clients phone number can't be empty");
	
	this.name=clientForms.getName();
	this.email=clientForms.getEmail();
	this.postCode=clientForms.getPostCode();
	this.literalAddress=clientForms.getLiteralAddress();
    this.phoneNumber=clientForms.getPhoneNumber();

	
	
	
	
}
	

public String getName(){return name;}
public String getLiteralAddress(){return literalAddress;}
public String getpostCode(){return postCode;}
public String getEmail(){return email;}
public String getPhoneNumber(){return phoneNumber;}
public String getWebSafeClientKey(){return webSafeClientKey;}	
	
	
	
	
	
	
}