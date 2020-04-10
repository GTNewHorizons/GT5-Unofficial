package gtPlusPlus.core.util.sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

import gtPlusPlus.api.objects.Logger;

public class NetworkUtils {

	public static String getContentFromURL(final String args) {
		if (checkNetworkIsAvailableWithValidInterface()){
			try {
				URL url;
				// get URL content
				url = new URL(args);
				final URLConnection conn = url.openConnection();
				// open the stream and put it into BufferedReader
				final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				String tempLine = null;
				while ((inputLine = br.readLine()) != null) {
					tempLine = inputLine;
				}
				br.close();
				return tempLine;
			}
			catch (final MalformedURLException e) {
				Logger.INFO("Bad URL for Version Check.");
			}
			catch (final IOException e) {
				Logger.INFO("IOException during Version Check.");
			}				
		}
		Logger.INFO("Network Not Available during Version Check.");
		return "offline";
	}

	public static boolean checkNetworkIsAvailableWithValidInterface(){
		try {
			if (hasValidNetworkInterface()){
				if (checkAddressWithTimeout("http://www.google.com", 100) ||
						checkAddressWithTimeout("http://www.baidu.com", 100) ||
						checkAddressWithTimeout("http://www.github.com/alkcorp/GTplusplus", 100) ||
						checkAddressWithTimeout("http://www.yahoo.com", 100)/* ||
						netIsAvailableGoogle() ||
						netIsAvailableBaidu() ||
						netIsAvailableGithub() ||
						netIsAvailableOther()*/){
					return true;
				}
				else {
					Logger.INFO("No sites responded to network connectivity test.");
				}
			}
			else {
				Logger.INFO("Network Adapter was not valid.");
			}
		}
		catch (SocketException e) {}
		return false;
	}

	private static boolean checkAddressWithTimeout(String URL, int timeout) {

		try {
			InetAddress.getByName(URL).isReachable(timeout); //Replace with your name
			return true;
		} catch (Exception e) {
			boolean result = false;
			try {
				URL urlObj = new URL(URL);
				HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
				con.setRequestMethod("GET");
				con.setConnectTimeout(timeout);
				con.connect();		 
				int code = con.getResponseCode();
				if (code == 200) {
					result = true;
				}
			} catch (Exception e2) {}
			return result;
		}
	}

	private static boolean hasValidNetworkInterface() throws SocketException{
		final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			final NetworkInterface interf = interfaces.nextElement();
			if (interf.isUp() && !interf.isLoopback()) {
				return true;
			}
		}
		return false;
	}

}
