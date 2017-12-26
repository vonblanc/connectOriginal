package com.connect.backend.domian.utilities.queries;

import java.util.ArrayList;
import java.util.Arrays.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.connect.backend.domian.seprofile.Profile;
import com.google.maps.GaeRequestHandler;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.googlecode.objectify.cmd.Query;

import static com.connect.backend.service.OfyService.ofy;

public class ConnectProfileQuery
{
	public static final Logger logger=Logger.getLogger(ConnectServiceQuery.class.getName());
	private static final double R = 6372.8; // In kilometers for harvsshine formula
	private static GeoApiContext context = new GeoApiContext(new GaeRequestHandler()).setApiKey("AIzaSyCkpSy184xeQ5eQEkgfNIb6QegWdqIMuH4");
	
	private static double haversine(double lat1, double lon1, double lat2, double lon2) {
		

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);                       //find the distance in km
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
       double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

	
	
	private static GeocodingResult[] geoCodeAddress(String address) throws Exception 
	{    
		 GeocodingResult[] result=null;
		try
		{
			result = GeocodingApi.geocode(context,address).await();
            
		}
		
		catch(Exception e)
		{
			
			logger.log(Level.SEVERE,e.getMessage(),e);

			//throw e;

			
		}
	   
		return result;
	
	}
	
	
	
	
	public static List<Profile> queryProfilesFromProximity(String customerAddress, double proximity) throws Exception
	{  
		double customerLong;
		double customerLat;
		
		double seLong;
		double seLat;
		
		double calculatedProximity;
		
		List<Profile>hold=new ArrayList<>();
		List<Profile>filtered=new ArrayList<>();
		
		GeocodingResult[] customerResult=geoCodeAddress(customerAddress);    //use google geocode to get the longtitude and latitude of the address then 
		                                                                      //use the harvshine algorithim to calculate the distance between the geocoded customer address and the eocoded se address  
		GeocodingResult[] seResult;
		
        customerLat=customerResult[0].geometry.location.lat;    
        customerLong=customerResult[0].geometry.location.lng;            //get customer lon/lat from geocode object 
        
        hold=ofy().load().type(Profile.class).list();
		
        for(Profile iterate:hold)
        {
        	seResult=geoCodeAddress(iterate.getAboutMe().getPostCode());  //geocode SE address to long/lat 
        	seLat=seResult[0].geometry.location.lat;
        	seLong=seResult[0].geometry.location.lng;
             
        	calculatedProximity=haversine(customerLat,customerLong,seLat,seLong); //calculate proximity in km using harveshine formula
        	
        	if(calculatedProximity<=proximity)
        	{
        		filtered.add(iterate);     //check if calculated proximity is less than given proximity 
        		                   
        	}
        }
		
		
		
		return filtered;
	}
	

	
	
	
	

    public static String searchJobTags(String search, List<String> jobTags)   //search and return which tag most matches the search word

    {     //check if the service tags are null, service person should have at least one service

        double percentage=60.0;  //percentage similarity allowed 

        String mostMatchedJob=null; //default string iif match !>= set percentage then return null

        if(jobTags.isEmpty()) return mostMatchedJob; //return default string if empty job tags list 

     //get list of service tags for a particular SE then returning the highest matching tag as long as it's greater than equals 70%

     for(String iterate:jobTags)  //run through job tags

     {

        

         if(GeneralQueryUtils.WordUtils.searchInputForSimilarities(search, iterate)>=percentage) //if any tags are greater than percentage value

         {     mostMatchedJob=iterate;
            
               return mostMatchedJob;

           //percentage=GeneralQueryUtils.searchInputForSimilarities(search, iterate);  //latest percentage becomes benchmark

         }

        

     }

    

     return mostMatchedJob;

       

    }



public static List<Profile> searchForProfiles(String search)
{
	List<Profile> holdList;
	List<Profile> resultList=new ArrayList<>();
	List<Double> matchList=new ArrayList<>();
	
	
	
	Query<Profile> query=ofy().load().type(Profile.class);
	
	holdList=query.list();
	
	
	
	
	for(Profile iterate:holdList)
	{
		String matchedJobTag=searchJobTags(search,iterate.getAboutMe().getServiceType());   //search jobtags and return best fit 
		
		//logger.log(Level.WARNING,"Name/ID of Profile: "+iterate.getId()  +" and Matched job string:"+ matchedJobTag);
		
		if(matchedJobTag!=null)
		{
			  Double percentSimilarity=GeneralQueryUtils.WordUtils.searchInputForSimilarities(search, matchedJobTag); 
			
			  resultList.add(iterate);
			  matchList.add(percentSimilarity);
			  
			  
			
		}
		
		
		
	}
	
	 resultList=GeneralQueryUtils.sortArray(matchList, resultList);

     return resultList;
	
}



/*  
    static public List<String> sortArray(List<Double> arrComp, List<String> arrStringVals)
    {
        if(arrComp.size()!=arrStringVals.size()) return null;
        
        for(int i=0;i<arrComp.size();i++)
        {
            
            for(int j=0;j<arrComp.size()-1;j++) 
            { 
                Double currentValue=arrComp.get(j);
                Double nextValue=arrComp.get(j+1);
                
                String currentString=arrStringVals.get(j);
                String nextString=arrStringVals.get(j+1);
                
                if(nextValue>currentValue) 
                {
                     arrComp.set(j,nextValue);
                     arrComp.set(j+1,currentValue);
                    
                     arrStringVals.set(j,nextString);
                     arrStringVals.set(j+1,currentString);
                }
                
            }

            
        }
        
        return arrStringVals;
    };
*/


}
