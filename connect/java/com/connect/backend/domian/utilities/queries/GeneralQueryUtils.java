package com.connect.backend.domian.utilities.queries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class GeneralQueryUtils
{
	public static class WordUtils{
		
		private static int minDistance(String word1, String word2) {
			int len1 = word1.length();
			int len2 = word2.length();
		 
			// len1+1, len2+1, because finally return dp[len1][len2]
			int[][] dp = new int[len1 + 1][len2 + 1];
		 
			for (int i = 0; i <= len1; i++) {
				dp[i][0] = i;
			}
		 
			for (int j = 0; j <= len2; j++) {
				dp[0][j] = j;
			}
		 
			//iterate though, and check last char
			for (int i = 0; i < len1; i++) {
				char c1 = word1.charAt(i);
				for (int j = 0; j < len2; j++) {
					char c2 = word2.charAt(j);
		 
					//if last two chars equal
					if (c1 == c2) {
						//update dp value for +1 length
						dp[i + 1][j + 1] = dp[i][j];
					} else {
						int replace = dp[i][j] + 1;
						int insert = dp[i][j + 1] + 1;
						int delete = dp[i + 1][j] + 1;
		 
						int min = replace > insert ? insert : replace;
						min = delete > min ? min : delete;
						dp[i + 1][j + 1] = min;
					}
				}
			}
		 
			return dp[len1][len2];
		}
		
		
		
		public static String normalizeWhiteSpaces(String value)
		{   
			String valueAfter=value.trim().replaceAll(" +", " ");             //implementation of the normalizer function first remove any beginning and end white spaces then remove all inbetween excess whitespace
			
			valueAfter=valueAfter.toUpperCase();                              //set all characters to Upper case
			
			return valueAfter;
			
		}
		
		
		public static List<String> normalizeWhiteSpaces(List<String> value)
		{   List<String> normalizedResults=new ArrayList<>();
		  if(value!=null)
		  {
		   for(String iterate:value)
			{ iterate=iterate.trim().replaceAll(" +", " ");             //implementation of the normalizer function for lists first remove any beginning and end white spaces then remove all inbetween excess whitespace
			
			iterate=iterate.toUpperCase();                              //set all characters to Upper case
			
			normalizedResults.add(iterate);
			}
		  }
			return normalizedResults;
		}
		
		public static StringBuilder singlefyStringList(StringBuilder stringBuilder, String value)
		{
			
			stringBuilder.append(" "+value+" ");
			
			return stringBuilder;
		}
		
		public static double searchInputForSimilarities(String original, String compare)
		{
			original=normalizeWhiteSpaces(original);
			compare=normalizeWhiteSpaces(compare);                      //convert search string to ALL CAPS and NO EXCESS whitespaces format used to store data  in datastore 

			
	        double largestLength=0;  //initialize largest length
	        
	        if(original.length()>compare.length()) largestLength=original.length();  //set length to the largest string out of both being compared  
	        if(original.length()<compare.length()) largestLength=compare.length();
	        if(original.length()==compare.length())largestLength=original.length();
	        
	        
	        double distance=minDistance(original,compare);    //use the edit distance algorithim to get the amount changed
	        
	        
	        
	       double diff=largestLength-distance;
	        
	       
	        double percentageSimilarity=(diff/largestLength)*100;
	         
	        
	        
			return percentageSimilarity;
			
		}
		
		
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
	public static class TimeUtils{
		
		
		public final static SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
		public static String checkDateStrings(String date)
		{
			 String isCorrectDate=null;
			try{
			 Date checkDate=simpleDateFormat.parse(date);           //to check if dates inputed to the app are in the correct format 
			 isCorrectDate=simpleDateFormat.format(checkDate);
			 
			 
			}
			
			catch(ParseException e)
			{
				e.printStackTrace();                                 //so if string date is not in the right format return null
			}
			
		
			return isCorrectDate;

			
		}
		
		public static Date dateFormat(String sDate)
		{   
			Date date=null;
		    try{
		    	
		    	date=simpleDateFormat.parse(sDate);                 //return date object from string check date formatter
		    }
			catch(ParseException e)
		    {
				e.printStackTrace();
				
		    }
		   
		    
		    
			return date;
		}
		
		
		public static int dateInterval(String fromDate, String toDate)
		{
			
			
			Date objFromDate=dateFormat(fromDate);
			Date objToDate=dateFormat(toDate);
			
			DateTime currentDate=new DateTime(objFromDate);
			DateTime futureDate=new DateTime(objToDate);
			
		   return Seconds.secondsBetween(currentDate, futureDate).getSeconds();
			
			}
		
		
		
		public static String dateFowardEst(String reqDate,int interval)
		{   
			
			Date objStartDate=dateFormat(reqDate);      //get date object from string 
						
			Calendar cal= Calendar.getInstance();          //create calendar instance using the interface method
			
			cal.setTime(objStartDate);                      //set interface time using date instance
			
			cal.add(Calendar.SECOND, interval);           //add to cal interface 
			
			
			
			
			return simpleDateFormat.format(cal.getTime());
		}
		
		
		public static boolean isLessThan(String firstDate, String secondDate )
		
		
		{
			Date first=dateFormat(firstDate);
			
			Date second=dateFormat(secondDate);
			
			
			if(first.compareTo(second)<=0) return true;
			
			
			else return false;
			
			
			
		}
		
		
	}
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
	 public static <T> List<T> sortArray(List<Double> arrComp, List<T> arrObjVals)
	    {
	        if(arrComp.size()!= arrObjVals.size()) return null;
	        
	        for(int i=0;i<arrComp.size();i++)
	        {                                                            //sort a list according to how similar the search is percentage similarity vs values  
	            
	            for(int j=0;j<arrComp.size()-1;j++) 
	            { 
	                Double currentValue=arrComp.get(j);
	                Double nextValue=arrComp.get(j+1);
	                
	                T currentObjVal= arrObjVals.get(j);
	                T nextObjVal= arrObjVals.get(j+1);
	                
	                if(nextValue>currentValue) 
	                {
	                     arrComp.set(j,nextValue);
	                     arrComp.set(j+1,currentValue);
	                    
	                     arrObjVals.set(j,nextObjVal);
	                     arrObjVals.set(j+1,currentObjVal);
	                }
	                
	            }

	            
	        }
	        
	        return  arrObjVals;
	    };
	
	
	
	
	
	
	
	
	
}