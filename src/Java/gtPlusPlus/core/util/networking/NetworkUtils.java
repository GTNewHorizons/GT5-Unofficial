package gtPlusPlus.core.util.networking;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class NetworkUtils {

	public static String getContentFromURL(String args) {
		try {
			if (hasValidNetworkInterface()){
				if (netIsAvailableGithub() || netIsAvailableOther() || netIsAvailableBaidu() || netIsAvailableGoogle()){
					try {
						URL url;
						// get URL content
						url = new URL(args);
						URLConnection conn = url.openConnection();
						// open the stream and put it into BufferedReader
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String inputLine;
						String tempLine = null;
						while ((inputLine = br.readLine()) != null) {
							tempLine = inputLine;
						}
						br.close();
						return tempLine;
					} 
					catch (MalformedURLException e) {}
					catch (IOException e) {}
				}		
			}
		} catch (SocketException e) {}
		return "offline";
	}

	private static boolean netIsAvailableGoogle() {                                                                                                                                                                                                 
		try {                                                                                                                                                                                                                                 
			final URL url = new URL("http://www.google.com");                                                                                                                                                                                 
			final URLConnection conn = url.openConnection();                                                                                                                                                                                  
			conn.connect();                                                                                                                                                                                                                   
			return true;                                                                                                                                                                                                                      
		} catch (MalformedURLException e) {                                                                                                                                                                                                   
			throw new RuntimeException(e);                                                                                                                                                                                                    
		} catch (IOException e) {                                                                                                                                                                                                             
			return false;                                                                                                                                                                                                                     
		}                                                                                                                                                                                                                                     
	}  

	private static boolean netIsAvailableBaidu() {                                                                                                                                                                                                 
		try {                                                                                                                                                                                                                                 
			final URL url = new URL("http://www.baidu.com");                                                                                                                                                                                 
			final URLConnection conn = url.openConnection();                                                                                                                                                                                  
			conn.connect();                                                                                                                                                                                                                   
			return true;                                                                                                                                                                                                                      
		} catch (MalformedURLException e) {                                                                                                                                                                                                   
			throw new RuntimeException(e);                                                                                                                                                                                                    
		} catch (IOException e) {                                                                                                                                                                                                             
			return false;                                                                                                                                                                                                                     
		}                                                                                                                                                                                                                                     
	}

	private static boolean netIsAvailableGithub() {                                                                                                                                                                                                 
		try {                                                                                                                                                                                                                                 
			final URL url = new URL("https://github.com/draknyte1/GTplusplus");                                                                                                                                                                                 
			final URLConnection conn = url.openConnection();                                                                                                                                                                                  
			conn.connect();                                                                                                                                                                                                                   
			return true;                                                                                                                                                                                                                      
		} catch (MalformedURLException e) {                                                                                                                                                                                                   
			throw new RuntimeException(e);                                                                                                                                                                                                    
		} catch (IOException e) {                                                                                                                                                                                                             
			return false;                                                                                                                                                                                                                     
		}                                                                                                                                                                                                                                     
	}	

	private static boolean netIsAvailableOther() {  
	try {
	      int timeout = 2000;
	      InetAddress[] addresses = InetAddress.getAllByName("www.yahoo.com");
	      for (InetAddress address : addresses) {
	        if (address.isReachable(timeout))
	          return true;
			return false;
	      }
	    } catch (Exception e) {
	    	return false;
	    }
	return false;
	}

	private static boolean hasValidNetworkInterface() throws SocketException{
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface interf = interfaces.nextElement();
			if (interf.isUp() && !interf.isLoopback())
				return true;
		}
		return false;
	}

}
