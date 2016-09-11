package gtPlusPlus.core.util.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkUtils {

	public static String getContentFromURL(String args) {

		URL url;

		try {
			// get URL content
			url = new URL(args);
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));

			String inputLine;
			String tempLine = null;
			

			

			while ((inputLine = br.readLine()) != null) {
				tempLine = inputLine;
			}

			br.close();
			return tempLine;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
