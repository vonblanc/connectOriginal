package com.connect.backend.domian.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.connect.backend.domian.utilities.queries.GeneralQueryUtils;

public class Inbox{
	
	private Map<Date,TextMessage> inbox= new TreeMap<>();    //map containing the list odf messages sorted
	
	
	
	public Inbox(){}       //default constructor 
	
	public void addMessage(TextMessage message)          //add message feature 
	{   
		Date date= GeneralQueryUtils.TimeUtils.dateFormat(message.getMessageTime());   ///create date from date String
		
		inbox.put(date, message);
		
	}
	
	
	public List<TextMessage>getInbox()
	{
		List <TextMessage> inboxList=new ArrayList<>(inbox.values());     //convert TreeMapp inbox to a List and return
		
		return inboxList;
		
	}
}