package com.connect.backend.domian.seprofile;

import java.util.ArrayList;
import java.util.List;

import com.connect.backend.domian.appusers.ConnectUser;
import com.connect.backend.domian.customerprofile.ServiceReq;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Profile{
	

@Id
private long id;               //id for  datastore purposes to be allocated automatically



@Parent
@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
private Key<ConnectUser> connectUserKey;    //the parent key of profile which is connectUser


private AboutMe aboutMe;      //add About Me as an Embeded Entity class x1  //remember to index!!!!!

@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
private List<Key<ServiceReq>> requests=new ArrayList<>();  //service requests made to the SEE

private String webSafeProfileKey;


public Profile(){}
	
public Profile(long id,String connectUserId, AboutMe aboutMe){       //construct profile from about me class
	
	this.id=id;     //assign get allocated ID from allocated key and store
	
	this.connectUserKey=Key.create(ConnectUser.class,connectUserId);  //create parentkey and store
	
	this.aboutMe=aboutMe;  //set about me
	
	this.webSafeProfileKey=Key.create(connectUserKey,Profile.class,this.id).getString(); 
}
	


public AboutMe getAboutMe()
{
	                            //return the about me class for on the fly info requests
	return aboutMe;
	
}

public void updateAboutMe(AboutMe aboutMe)
{                                            //update aboutme class
	this.aboutMe=aboutMe;
}


@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
public Key<ConnectUser> getConnectUserKey()
{                                                 //get the key for the parent User
	return connectUserKey;
}

@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
public boolean addServiceRequest(Key<ServiceReq> request)
{   boolean status=false;
	
	requests.add(request);
	if(requests.contains(request)) status=true;
		
		
	return status;
}

public String getWebSafeProfileKey()
{
	   return webSafeProfileKey; //return web safe version of key
}


public long getId(){return id;}
	
	
public List<String>	getWebSafeRequestKeys(){
	
	List<String> webSafeRequestKeys=new ArrayList<>();
	
	if(requests!=null)
	{
		
	for(Key<ServiceReq> iterator:requests) {webSafeRequestKeys.add(iterator.getString());}
	}
	
	return webSafeRequestKeys;
	
}
	
	
}