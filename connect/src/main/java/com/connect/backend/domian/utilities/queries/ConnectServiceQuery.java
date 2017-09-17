package com.connect.backend.domian.utilities.queries;

import com.connect.backend.domian.seprofile.ServiceOffered;
import com.googlecode.objectify.cmd.Query;
import static com.connect.backend.service.OfyService.ofy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ConnectServiceQuery{
	
	public static final Logger logger=Logger.getLogger(ConnectServiceQuery.class.getName());
	
	public ConnectServiceQuery(){}
	
     
	
	
	
	
	
	
	
	public static List<ServiceOffered> queryService(String serviceName)   //product search algorithim
	{
		serviceName=GeneralQueryUtils.WordUtils.normalizeWhiteSpaces(serviceName);                      //convert search string to ALL CAPS and NO EXCESS whitespaces format used to store data  in datastore 
		
		logger.log(Level.INFO,"This is the value you are about querying for "+ serviceName);
		
		Query<ServiceOffered> query=ofy().load().type(ServiceOffered.class);  //query all products-------test phase complete phase should have a list of profiles to query products from 
		
		List<ServiceOffered> allServices= query.list(); // give reference to query list 
		
     	List<ServiceOffered> filteredServices=new ArrayList<ServiceOffered>();    //createlist to hold filtered results 
		
		for(ServiceOffered iterate:allServices)                //iterate through queried results 
		{
		String comparedName=GeneralQueryUtils.WordUtils.normalizeWhiteSpaces(iterate.getServiceName());        //normalize name and category to compare 
		String comparedCategory=GeneralQueryUtils.WordUtils.normalizeWhiteSpaces(iterate.getServiceCategory());
		if(serviceName.indexOf(comparedName)!=-1||serviceName.indexOf(comparedCategory)!=-1) filteredServices.add(iterate); //check if service name is contained in the SERVICENAME field and SERVICECATEGORY field 
			
		else logger.log(Level.INFO,"Did  Not work the querying ");

		}
		
		
		return filteredServices;    //return filtered services
		
	}
	
	
	/* public static boolean queryServicesForInputPattern(String serviceString, String searchInput)
     {   
		 searchInput=normalizeWhiteSpaces(searchInput);
		 serviceString=normalizeWhiteSpaces(serviceString);     //normalize both inputs 
		 
		 
         String [] array;
         array=searchInput.split(" ");        //split input 
         boolean result=true;                 //return value always true set to false if there is no match 
         
         for(int i=0;i<array.length;i++)
            {
                int length=array[i].length();                            //get length of each word 
                int queryLength=(int) Math.round(0.1*length);                                          //get 60% of the length of each word then roundup
                String smallerString=array[i].substring(0,queryLength);                                //get the substring of new smaller length calculated 
                
                if(serviceString.indexOf(smallerString)!=-1){}                           //check if that smaller string lies in the service word
                
                
                
                else {System.out.println("not contained!!!");
                    result=false;                                                                       //return false if not contained
                    return false;
                }
                
            }
         
         
         return result;
     }
	 */
	 
	
	 
	 public static List<ServiceOffered> queryServicesUsingClosestMatch(String findService)
	 {   
		 String serviceName;   //to store service name string from service
		 
		 String serviceCategory;   //to store service category gotten from service object
		 List<ServiceOffered> results;    //to store generic query results 
		 
		 List<ServiceOffered> finalList= new ArrayList<>();   //to hold list that have a similar words to search of up to 40%
		 
		 List<Double> similarList= new ArrayList<>();      //to hold percentage similarity
		 
		 Query<ServiceOffered> query=ofy().load().type(ServiceOffered.class);  //****query all products-------test phase complete phase should have a list of profiles to query products from 
		
		 
		 results=query.list();  
		 
		 
		 for(ServiceOffered iterate:results)                         
		 {
			 serviceName=iterate.getServiceName();
			 serviceCategory=iterate.getServiceCategory();
			 
			 Double similarName=GeneralQueryUtils.WordUtils.searchInputForSimilarities(serviceName,findService); //get percentage similarity between ServiceOffereds name and the searched for service
			 
			 Double similarCategory=GeneralQueryUtils.WordUtils.searchInputForSimilarities(serviceCategory,findService);  //as bove but for ServiceOffered's  category 
			 
			 Double similarMax= (similarName>similarCategory)? similarName:similarCategory;  //check which percentage similarity is greater then equate it to similarMax
			 
			 
			 
			 if(similarName>40||similarCategory>40)          //check if the percentage similarity of goods offered and searched string is greater than 40%  
			 {
				 finalList.add(iterate);
				 
				 
				 
				 
				 similarList.add(similarMax);  //store percentage value for each serviceOffered 

			 }
			 
		 }
		 

		 finalList=GeneralQueryUtils.sortArray(similarList, finalList);
		 

		
		 
		return finalList; 
		 
	 }
	
	
}