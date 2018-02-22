package gtPlusPlus.core.util.sys;

import java.io.*;
import java.net.*;

import org.apache.http.client.utils.URIBuilder;

import gtPlusPlus.api.objects.Logger;

public class GeoUtils {

	public static String determineUsersCountry(){
		try {
		if (NetworkUtils.checkNetworkIsAvailableWithValidInterface()){
			return getUsersCountry();			
		}
		else {
			return "Offline.";
		}
		} catch (Throwable T){
			Logger.INFO("Failed to initialise GeoUtils.");
			return "Failed.";
		}
	}

	private static String getUsersIPAddress() {
		try {
			String webPage = "http://checkip.amazonaws.com/";
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			isr.close();
			String result = sb.toString();
			return result;
		} catch (IOException e) {}
		return "Error getting users IP.";
	}

	private static String getUsersCountry() {
		
		//Get the IP
		String ipAddress = getUsersIPAddress();
		
		//Build a URL
		URIBuilder builder = new URIBuilder()
	            .setScheme("http")
	            .setHost("ipinfo.io")
	            .setPath("/"+ipAddress+"/country/");

	    URI uri;
		try {
			//Convert the URI Builder to a URI, then to a URL
			uri = builder.build();
		    URL url = uri.toURL();
		    
		    //Main Check method
		    try {
				URLConnection urlConnection = url.openConnection();
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String temp = sb.toString();
				String result = temp.replaceAll("(\\r|\\n)", "");
				isr.close();
				return result;
				//Catch block for bad connection
			} catch (IOException e) {
				Logger.INFO("Method 1 - Failed.");
			}  
		    
		    //Secondary method
		    try (java.util.Scanner s = new java.util.Scanner(url.openStream(), "UTF-8").useDelimiter("\\A")) {
				String r = s.next();
				return r.replaceAll("(\\r|\\n)", "");
				//Catch block for bad connection
			} catch (java.io.IOException e) {
				Logger.INFO("Method 2 - Failed.");			
			}
		    
		}
		//Catch block for all the Bad URI/URL building
		catch (URISyntaxException | MalformedURLException e1) {
			if (e1 instanceof URISyntaxException){
				Logger.INFO("Bad URI Syntax for builder.");
			}
			else {
				Logger.INFO("Malformed URL.");				
			}
			Logger.INFO("Country Check - Failed.");
		}
		return "Error getting users Country. "+ipAddress;
	}

}
