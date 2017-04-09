package gtPlusPlus.core.util.networking;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class NetworkUtils {

	public static String getContentFromURL(final String args) {
		try {
			if (hasValidNetworkInterface()){
				if (netIsAvailableGithub() || netIsAvailableOther() || netIsAvailableBaidu() || netIsAvailableGoogle()){
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
					catch (final MalformedURLException e) {}
					catch (final IOException e) {}
				}
			}
		} catch (final SocketException e) {}
		return "offline";
	}

	public static boolean checkNetworkIsAvailableWithValidInterface(){
		try {
			if (hasValidNetworkInterface()){
				if (netIsAvailableGithub() || netIsAvailableOther() || netIsAvailableBaidu() || netIsAvailableGoogle()){
					return true;
				}
			}
		}
		catch (SocketException e) {}
		return false;
	}

	private static boolean netIsAvailableGoogle() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			return false;
		}
	}

	private static boolean netIsAvailableBaidu() {
		try {
			final URL url = new URL("http://www.baidu.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			return false;
		}
	}

	private static boolean netIsAvailableGithub() {
		try {
			final URL url = new URL("https://github.com/draknyte1/GTplusplus");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			return false;
		}
	}

	private static boolean netIsAvailableOther() {
		try {
			final int timeout = 200;
			final InetAddress[] addresses = InetAddress.getAllByName("www.yahoo.com");
			for (final InetAddress address : addresses) {
				if (address.isReachable(timeout)) {
					return true;
				}
				return false;
			}
		} catch (final Exception e) {
			return false;
		}
		return false;
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
