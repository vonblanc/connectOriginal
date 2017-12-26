package com.connect.backend.domian.utilities;


import com.connect.backend.domian.utilities.queries.GeneralQueryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import java.io.IOException;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
@MultipartConfig
public class CloudStorageHelper extends HttpServlet
{
	
	
	 private static Storage storage = null;
	static {
		  storage = StorageOptions.getDefaultInstance().getService();
		  		  
		  	
		}
	
	private void checkFileExtension(String fileName) throws ServletException {
	    if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
	      String[] allowedExt = { ".jpg", ".jpeg", ".png", ".gif" };
	      for (String ext : allowedExt) {
	        if (fileName.endsWith(ext)) {
	          return;
	        }
	      
	      }
	      
	      throw new ServletException("file must be an image");
	    }
	  }
	  // [END checkFileExtension]
	
	
	
	private String createFile(Part filePart, final String bucketName,final String webSafeProfileKey) throws ServletException, IOException {
		String completeFileName=null;
		Date date= new Date();
		final String sDate=GeneralQueryUtils.TimeUtils.simpleDateFormat.format(date);
		
		final String fileName=filePart.getSubmittedFileName();
		this.checkFileExtension(fileName);
		
		if(sDate!=null) {completeFileName=fileName+"-"+sDate+"-"+webSafeProfileKey;}
		
		@SuppressWarnings("deprecation")
		BlobInfo blobInfo =
		        storage.create(
		            BlobInfo
		                .newBuilder(bucketName, completeFileName)
		                // Modify access list to allow all users with link to read file
		                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
		                .build(),
		            filePart.getInputStream());
		    // return the public download link
		    return blobInfo.getMediaLink();
		}
		
		
	
	
	public void doPost (HttpServletRequest req, HttpServletResponse resp ) throws IOException, ServletException
	{
       
		final String webSafeProfileKey=req.getParameter("webSafeProfileKey");
		
		
		
		final Part filePart=req.getPart("File");
		
		String bucketName=(String)this.getServletContext().getInitParameter("BucketName");
		
		String blobUrl=createFile(filePart,bucketName,webSafeProfileKey);
		
		
		
		
		
	}
	
}