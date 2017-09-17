package com.connect.backend.spi;
import static com.connect.backend.service.OfyService.ofy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.connect.backend.authenticators.FireAuth;
import com.connect.backend.domian.seprofile.AboutMe;
import com.connect.backend.domian.seprofile.Profile;
import com.connect.backend.domian.seprofile.ServiceOffered;
import com.connect.backend.domian.utilities.queries.ConnectProfileQuery;
import com.connect.backend.domian.utilities.queries.ConnectServiceQuery;
import com.connect.backend.forms.AboutMeForms;
import com.connect.backend.forms.ClientForms;
import com.connect.backend.forms.ServiceOfferedForm;
import com.connect.backend.forms.ServiceReqForms;
import com.connect.backend.users.AuthUser;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.connect.backend.Constants;
import com.connect.backend.domian.appusers.ConnectUser;
import com.connect.backend.domian.customerprofile.Client;
import com.connect.backend.domian.customerprofile.ServiceReq;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.maps.model.GeocodingResult;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;


@Api(
		name="connectapi",
		version="v1",
		authenticators={FireAuth.class},
		clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID },
		description="Test REST api for the connect app"
		)


public class connectapi{

	
	public static final Logger logger=Logger.getLogger(FireAuth.class.getName());
	
	public ConnectUser getConnectUserFromUser(User user) throws UnauthorizedException{
		
    if(user==null) throw new UnauthorizedException("There is no user returned");
		
		//String name=user.getName();                                            ///creates a connect user class from the injected authorization user class 
	
		//Hello hello=new Hello(Message);
		 String uName=((AuthUser)user).getName();
		 String uId=((AuthUser)user).getId();
		 String uEmail=((AuthUser)user).getEmail();
		
		 ConnectUser connectUser=new ConnectUser(uName,uEmail, uId);
		 
		return connectUser;
	}
	
	
	public Profile getProfileFromConnectUser(ConnectUser user) throws NotFoundException
	{
		Key<ConnectUser> connectUserKey=Key.create(ConnectUser.class,user.getId());       
		                                                                                   //get profile from ConnectUser only one profile allowed per user 
		Query<Profile> query=ofy().load().type(Profile.class).ancestor(connectUserKey);
		
		
		
		List<Profile> profileList=query.list();
		
		Profile profile=profileList.get(0);
		
		if(profile==null) throw new NotFoundException("Profile does not  exist!");
	
		return profile;
	}
	
	
	
	
	@ApiMethod(name="createProfile",path="createProfile",httpMethod=HttpMethod.POST)                              //method to be used just once!!!!
	public Profile createProfile(User user,AboutMeForms aboutmeforms) throws ParseException, UnauthorizedException
	{
		
		
		
		 ConnectUser connectUser=getConnectUserFromUser(user);  //method that returns Connect user from authorized user 
		 
		 String userId=connectUser.getId();
		 
		 
		 
		 Key<ConnectUser> userKey=Key.create(ConnectUser.class,userId); //create user key 
		 
		 Query<Profile> query=ofy().load().type(Profile.class).ancestor(userKey);  //query for all the Profiles associated with a particular User(Parent Relationship)
		 
		 List<Profile> profilesForOneUser=query.list();  //Reference to list of profiles
		
		 
		 
		 if(profilesForOneUser!=null && (!profilesForOneUser.isEmpty()))
		 { 
			 
			 
				 
		   int listSize=profilesForOneUser.size();          //store number of profiles 
		 
		   if(listSize>=1) return profilesForOneUser.get(0); //return the first profile if there's already a profile created for a particular user 
		 
		 }

		 
		 
		 final Key<Profile> profileKey=ObjectifyService.factory().allocateId(userKey,Profile.class);  //creates key from the user key and profile class(id also created)
		 
		 
		 
		 final long profileId=profileKey.getId();   //reference to the created profile ID
		 
		 
		 AboutMe aboutMe=new AboutMe(aboutmeforms); //create about me embeded class from details above
		 		 
		 
		 Profile profile=new Profile(profileId,userId,aboutMe);
		 
		 ofy().save().entities(connectUser,profile);
		 
		 
		
		return profile;
	}
	
	
	
	@ApiMethod(name="updateProfile",path="updateProfile",httpMethod=HttpMethod.POST)
	
	public Profile updateProfile(User user, @Named("webSafeProfileKey")String webSafeProfileKey, AboutMeForms aboutmeforms) throws UnauthorizedException, NotFoundException, ParseException, ForbiddenException
	{
		
		
		
		
		ConnectUser connectUser=getConnectUserFromUser(user);
		
		
		Key<ConnectUser> connectUserKey=Key.create(ConnectUser.class,connectUser.getId());
		
					
		 Key<Profile> profileKey=Key.create(webSafeProfileKey);
		
		 if(!(profileKey.getParent().equals(connectUserKey))) throw new ForbiddenException("this profile that you tried to load does not belong to the User");
		
		 
		 
		 Profile profile=ofy().load().key(profileKey).now();
		 
		
		 if(profile==null) throw new NotFoundException("Profile you are trying to load not found"); 
		
	     AboutMe aboutMe=profile.getAboutMe();
	     
	     aboutMe.update(aboutmeforms);
		
		 profile.updateAboutMe(aboutMe);
		 
		 ofy().save().entity(profile);
		 
		 
		
		return profile;
	}
	
	@ApiMethod(name="createService",path="createService",httpMethod=HttpMethod.POST)
	public ServiceOffered createService(User user, ServiceOfferedForm serviceOfferedForm) throws UnauthorizedException, NotFoundException
	{  
		ConnectUser connectUser=getConnectUserFromUser(user);
		
		
		Profile profile=getProfileFromConnectUser(connectUser);  //get profile from Key  
		
	   Key<Profile> profileKey=Key.create(profile.getWebSafeProfileKey());  //create profile Key
		 
	   Key<ServiceOffered> serviceOfferedKey= ObjectifyService.factory().allocateId(profileKey,ServiceOffered.class);  //creates key from the user key and profile class(id also created)
       
	   long serviceOfferedId=serviceOfferedKey.getId();
	   
	   ServiceOffered serviceOffered= new ServiceOffered(serviceOfferedId,serviceOfferedForm,profileKey.getString());  //create new ServiceOffered
		
	   ofy().save().entities(profile,serviceOffered);
		
		
		return serviceOffered;
	}
	
	
	@ApiMethod(name="updateService",path="updateService",httpMethod=HttpMethod.POST)
	public ServiceOffered updateService(ServiceOfferedForm serviceOfferedForm,@Named("webSafeServiceOfferedKey")String webSafeServiceOfferedKey) throws UnauthorizedException, NotFoundException
	{
		
		Key<ServiceOffered> serviceOfferedKey=Key.create(webSafeServiceOfferedKey);
		
		ServiceOffered serviceOffered=ofy().load().key(serviceOfferedKey).now();
		
		if(serviceOffered==null) throw new NotFoundException("Service is not found");
		
		serviceOffered.updateServiceOffered(serviceOfferedForm);
		
		ofy().save().entity(serviceOffered);
		
		return serviceOffered;
		
		
	}
	
	@ApiMethod(name="createClient",path="createClient", httpMethod=HttpMethod.POST)
	public Client createClient(User user,  ClientForms clientForms) throws UnauthorizedException, NotFoundException
	{
		ConnectUser connectUser=getConnectUserFromUser(user);
		
		if(connectUser==null) throw new NotFoundException("Can't find connect user");
		
		Key<ConnectUser> connectUserKey=Key.create(ConnectUser.class,connectUser.getId());

		
		Query<Client> query=ofy().load().type(Client.class).ancestor(connectUserKey);  //query for all the Profiles associated with a particular User(Parent Relationship)
		
		List<Client>clientList=query.list();
		
		 
		 if(clientList!=null && (!clientList.isEmpty()))
		 { 
			 
			 
				 
		   int listSize=clientList.size();          //store number of profiles 
		 
		   if(listSize>=1) return clientList.get(0); //return the first profile if there's already a profile created for a particular user 
		 
		 }


			Key<Client>clientKey= ObjectifyService.factory().allocateId(connectUserKey,Client.class);  //creates key from the user key and client class(id also created)
		       
			long clientId=clientKey.getId();
			
			Client client=new Client(clientId,clientForms,connectUser);    //create client class
			
			ofy().save().entities(connectUser,client).now();
		
		 
		return client;
	}
	
	
	@ApiMethod(name="updateClient", path="updateClient", httpMethod=HttpMethod.POST)
	public Client updateClient(ClientForms clientForms, @Named("updateClient")final String webSafeClientKey) throws NotFoundException
	{
		
		Key<Client> clientKey=Key.create(webSafeClientKey);
		
		Client client=ofy().load().key(clientKey).now();
		
		if(client==null) throw new NotFoundException("Client not found");
		
		client.updateClient(clientForms);
		
		ofy().save().entity(client);
		
		return client;
		
		
	}
	
	@ApiMethod(name="createServiceReq", path="createServiceReq", httpMethod=HttpMethod.POST)
	public ServiceReq createServiceReq(ServiceReqForms serviceReqForms, @Named("webSafeServicePeopleKeys")List<String> webSafeServicePeopleKeys, @Named("webSafeClientKey") String webSafeClientKey) throws NotFoundException
	{
		Key<Client>clientKey=Key.create(webSafeClientKey);
		
		Client client=ofy().load().key(clientKey).now();
		
		if(client==null) throw new NotFoundException("Client not Found");
		
		Key<ServiceReq>ServiceReqKey= ObjectifyService.factory().allocateId(clientKey,ServiceReq.class);  //creates key from the user key and client class(id also created)

		
		
		ServiceReq serviceReq= new ServiceReq(ServiceReqKey.getId(),clientKey.getId(),serviceReqForms,webSafeServicePeopleKeys);
		
		ofy().save().entity(serviceReq);
		
		return serviceReq;
		
		
	}
	
	@ApiMethod(name="getSpecificService",path="getSpecificService",httpMethod=HttpMethod.GET)
	public List<ServiceOffered> getSpecificService(@Named("serviceName")String serviceName)
	{
		return ConnectServiceQuery.queryServicesUsingClosestMatch(serviceName);
		
		
	}

	@ApiMethod(name="queryProfilesWithinProximity",path="queryProfilesWithinProximity",httpMethod=HttpMethod.GET)
	public List<Profile> queryProfilesWithinProximity(@Named("address")String address, @Named("proximity") double proximity) throws Exception
	{
		return ConnectProfileQuery.queryProfilesFromProximity(address,proximity);
		
		
	}
	
	@ApiMethod(name="queryProfilesByJobTag",path="queryProfilesByJobTag",httpMethod=HttpMethod.GET)
	public List<Profile> queryProfilesByJobTag(@Named("address")String search)
	{
		List<Profile> results=ConnectProfileQuery.searchForProfiles(search);
		
		return results;
		
	}

	
	
	@ApiMethod(name="testqueries",path="testqueries",httpMethod=HttpMethod.GET)
	public List<Profile> testqueries(@Named("search") String search)
	{
		
		
		return ofy().load().type(Profile.class).list();
		
		//return ConnectProfileQuery.searchForProfiles(search);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@ApiMethod(name="createTestProfiles",path="CreateTestProfiles",httpMethod=HttpMethod.POST)
    
	public Profile createTestProfiles(AboutMeForms aboutmeforms, @Named("webSafeConnectUserKey")String webSafeConnectUserKey) throws ParseException, NotFoundException
	{
		//AboutMe aboutme=new AboutMe(aboutmeforms);
		
		Key<ConnectUser> userKey=Key.create(webSafeConnectUserKey);
		
		ConnectUser connectUser=ofy().load().key(userKey).now();
		
		if (connectUser==null) throw new NotFoundException("Connect User not found");
		
		String userId=connectUser.getId();
		
		
		final Key<Profile> profileKey=ObjectifyService.factory().allocateId(userKey,Profile.class);  //creates key from the user key and profile class(id also created)
		 
		 
		 
		 final long profileId=profileKey.getId();   //reference to the created profile ID
		 
		 
		 AboutMe aboutMe=new AboutMe(aboutmeforms); //create about me embeded class from details above
		 		 
		 
		 Profile profile=new Profile(profileId,userId,aboutMe);
		 
		 ofy().save().entity(profile).now();
		 
		
		return profile;
		
	}
	
	
	@ApiMethod(name="createTestServices",path="createTestServices",httpMethod=HttpMethod.POST)
	
	public ServiceOffered createTestServices(ServiceOfferedForm serviceOfferedForm, @Named("webSafeProfileKey")String webSafeProfileKey) throws NotFoundException{
	
		Key<Profile> profileKey=Key.create(webSafeProfileKey);
		
		Profile profile=ofy().load().key(profileKey).now();
		
		if(profile==null) throw new NotFoundException("Profile not found");
		
		final Key <ServiceOffered> serviceOfferedKey=ObjectifyService.factory().allocateId(profileKey,ServiceOffered.class);
		
		final long serviceOfferedId=serviceOfferedKey.getId();
		
		ServiceOffered service=new ServiceOffered(serviceOfferedId,serviceOfferedForm,serviceOfferedKey.getString());
		
	    ofy().save().entity(service);
		
		
		return service;
	
	}
	
	@ApiMethod(name="createTestClient", path="createTestClient", httpMethod=HttpMethod.POST)
	public Client createTestClient(ClientForms clientForms, @Named("webSafeConnectUserKey") String webSafeConnectUserKey) throws NotFoundException{
		
		Key<ConnectUser> connectUserKey=Key.create(webSafeConnectUserKey);
		
        ConnectUser connectUser=ofy().load().key(connectUserKey).now();
        
        if(connectUser==null) throw new NotFoundException("User not found");
        
		final Key <Client> ClientKey=ObjectifyService.factory().allocateId(connectUserKey,Client.class);
		
		final long clientKey=ClientKey.getId();
		
		Client client= new Client(clientKey,clientForms,connectUser);
		
		ofy().save().entity(client);

		
		
		
		return client;
	}
	
}