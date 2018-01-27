package com.square.mail2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;

import org.jsoup.Jsoup;

import android.util.Log;

import com.sun.mail.imap.IMAPStore;
import com.sun.mail.pop3.POP3Store;

public class ReceiveMail{

 public static List<String> receiveEmail(String pop3Host, String storeType,
  String user, String password) {
  try {
	  
	  List<String> result = new ArrayList<String>();
   //1) get the session object
   Properties properties = new Properties();
   properties.put("mail.pop3.host", pop3Host);
   Session emailSession = Session.getDefaultInstance(properties);
   
   //2) create the POP3 store object and connect with the pop server
  
   
  
   if(storeType=="pop3"){
   POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
   emailStore.connect(pop3Host,user, password);
   
   //3) create the folder object and open it
   Folder emailFolder = emailStore.getFolder("INBOX");
   emailFolder.open(Folder.READ_ONLY);

   //4) retrieve the messages from the folder in an array and print it
   Message[] messages = emailFolder.getMessages();
   
   for (int i = 0; i < messages.length; i++) {
	Message message = messages[i];
	System.out.println("---------------------------------");
	System.out.println("Email Number " + (i + 1));
	System.out.println("Subject: " + message.getSubject());
	//System.out.println("From: " + message.getFrom()[0]);
	//System.out.println("Text: " + message.getContent().toString());
	
	Object msgContent = messages[i].getContent();

    String content = "";             

     /* Check if content is pure text/html or in parts */                     
     if (msgContent instanceof Multipart) {

         Multipart multipart = (Multipart) msgContent;

         Log.e("BodyPart", "MultiPartCount: "+multipart.getCount());

         for (int j = 0; j < multipart.getCount(); j++) {

          BodyPart bodyPart = multipart.getBodyPart(j);

          String disposition = bodyPart.getDisposition();

          if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { 
              System.out.println("Mail have some attachment");

              DataHandler handler = bodyPart.getDataHandler();
              System.out.println("file name : " + handler.getName());  
              
            }
          else { 
        	  try {	content = getText(bodyPart);} catch (Exception e) {		e.printStackTrace();	}
            }
        }
     }
     else                
         content= messages[i].getContent().toString();
     System.out.println(Jsoup.parse(content).text());
     result.add(Jsoup.parse(content).text());
   }
   
   //5) close the store and folder objects
   emailFolder.close(false);
   emailStore.close();}
   else if (storeType=="imap"){
	   IMAPStore emailStoreImap = (IMAPStore) emailSession.getStore(storeType);
	   emailStoreImap.connect(pop3Host,user, password);
	   
	   //3) create the folder object and open it
	   Folder emailFolder = emailStoreImap.getFolder("INBOX");
	   emailFolder.open(Folder.READ_ONLY);

	   //4) retrieve the messages from the folder in an array and print it
	   Message[] messages = emailFolder.getMessages();
	   
	   for (int i = 0; i < messages.length; i++) {
		Message message = messages[i];
		System.out.println("---------------------------------");
		System.out.println("Email Number " + (i + 1));
		System.out.println("Subject: " + message.getSubject());
		//System.out.println("From: " + message.getFrom()[0]);
		//System.out.println("Text: " + message.getContent().toString());
		
		Object msgContent = messages[i].getContent();

	    String content = "";             

	     /* Check if content is pure text/html or in parts */                     
	     if (msgContent instanceof Multipart) {

	         Multipart multipart = (Multipart) msgContent;

	         Log.e("BodyPart", "MultiPartCount: "+multipart.getCount());

	         for (int j = 0; j < multipart.getCount(); j++) {

	          BodyPart bodyPart = multipart.getBodyPart(j);

	          String disposition = bodyPart.getDisposition();

	          if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { 
	              System.out.println("Mail have some attachment");

	              DataHandler handler = bodyPart.getDataHandler();
	              System.out.println("file name : " + handler.getName()); 
	          }
	          else { 
	        	  Log.e("vv",bodyPart.getContentType()+" type");
	              //content = bodyPart.getContent().toString();  // the changed code
	        	  try {	content = getText(bodyPart);} catch (Exception e) {		e.printStackTrace();	}
	            }
	        }	     
	    } else                
	         content= messages[i].getContent().toString();
	     Log.e("vv","content "+content);
	     System.out.println(Jsoup.parse(content).text());
	     result.add(Jsoup.parse(content).text());
	   }
	   
	   //5) close the store and folder objects
	   emailFolder.close(false);
	   emailStoreImap.close();}	   
   
   return result;
  } catch (NoSuchProviderException e) {e.printStackTrace();} 
  catch (MessagingException e) {e.printStackTrace();}
  catch (IOException e) {e.printStackTrace();}
  return null;
 }
 public static String getText(Part p) throws Exception {

	
	/*
	 * Using isMimeType to determine the content type avoids
	 * fetching the actual content data until we need it.
	 */
	if (p.isMimeType("text/plain")) {
		
		return((String)p.getContent());
	} else if (p.isMimeType("multipart/*")) {
	    Multipart mp = (Multipart)p.getContent();
	    int count = mp.getCount();
	    for (int i = 0; i < count; i++)
		getText(mp.getBodyPart(i));
	    
	} else if (p.isMimeType("message/rfc822")) {
	    getText((Part)p.getContent());
	} else {
	    /*
		 * If we actually want to see the data, and it's not a
		 * MIME type we know, fetch it and check its Java type.
		 */
		Object o = p.getContent();
		if (o instanceof String) {
		    return((String)o);
		} else if (o instanceof InputStream) {
		    InputStream is = (InputStream)o;
		    int c;
		    String tmpSt="";
		    while ((c = is.read()) != -1)
			tmpSt=tmpSt+(c);
		    is.close();
		    return tmpSt;
		} 
	    }
	return ""; 
	}

 public static void main(String[] args) {
 }
 
}
