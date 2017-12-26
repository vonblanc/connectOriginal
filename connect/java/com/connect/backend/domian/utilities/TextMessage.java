package com.connect.backend.domian.utilities;

import java.util.Date;

import com.connect.backend.domian.utilities.queries.GeneralQueryUtils;
import com.google.appengine.repackaged.com.google.common.base.Preconditions;


public class TextMessage 
{
	private final String messageDate;
	
	private final String textMessage;
	
	
	public TextMessage(String textMessage)
	{  
		Preconditions.checkNotNull(textMessage,"Please provide message with test string");
		this.textMessage=textMessage;
		Date date=new Date();
		messageDate=GeneralQueryUtils.TimeUtils.simpleDateFormat.format(date);
		
	}
	
	public String getMessage(){
		
		return textMessage;
	
	}
	
	public String getMessageTime()
	{
		return messageDate;
		
	}
	
	
	
}