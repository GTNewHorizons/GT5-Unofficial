package gtPlusPlus.core.util.networking;

import java.io.*;
import java.net.*;

public class NetworkUtils {

	public static String getContentFromURL(final String args) {
		if (NetworkUtils.netIsAvailable()) {
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
				e.printStackTrace();
			}
			catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static boolean netIsAvailable() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		}
		catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		}
		catch (final IOException e) {
			return false;
		}
	}

}
