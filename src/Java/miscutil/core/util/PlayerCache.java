package miscutil.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

import miscutil.core.lib.CORE;

public class PlayerCache {

	private static final File cache = new File("PlayerCache.dat");

	public static void createPropertiesFile(String playerName, String playerUUIDasString) {
		try {
			Properties props = new Properties();
			props.setProperty(playerName, playerUUIDasString);
			OutputStream out = new FileOutputStream(cache);
			props.store(out, "Player Cache.");
		}
		catch (Exception e ) {
			e.printStackTrace();
		}
	}

	public static void appendParamChanges(String playerName, String playerUUIDasString) {
		try {

			Properties properties = new Properties();
			try {
				Utils.LOG_WARNING("Attempting to load "+cache.getName());
				properties.load(new FileInputStream(cache));
			} catch (IOException e) {
				Utils.LOG_WARNING("No PlayerCache file found, creating one.");
				createPropertiesFile(playerName, playerUUIDasString);
			}

			properties.setProperty(playerName, playerUUIDasString);
			FileOutputStream fr=new FileOutputStream(cache);
			properties.store(fr, "Player Cache.");
			fr.close();
		}
		catch (Exception e ) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a "properties" file, and returns it as a Map 
	 * (a collection of key/value pairs).
	 * 
	 * Credit due to Alvin Alexander - http://alvinalexander.com/java/java-properties-file-map-example?nocache=1#comment-8215
	 * Changed slightly as the filename and delimiter are constant in my case.
	 * 
	 * @param filename  The properties filename to read.
	 * @param delimiter The string (or character) that separates the key 
	 *                  from the value in the properties file.
	 * @return The Map that contains the key/value pairs.
	 * @throws Exception
	 */
	private static Map<String, String> readPropertiesFileAsMap() throws Exception {
		String filename = cache.getName();
		String delimiter = ":";
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Map<String, String> map = new HashMap();
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = reader.readLine()) != null)
		{
			if (line.trim().length()==0) continue;
			if (line.charAt(0)=='#') continue;
			// assumption here is that proper lines are like "String : <a href="http://xxx.yyy.zzz/foo/bar"" title="http://xxx.yyy.zzz/foo/bar"">http://xxx.yyy.zzz/foo/bar"</a>,
			// and the ":" is the delimiter
			int delimPosition = line.indexOf(delimiter);
			String key = line.substring(0, delimPosition-1).trim();
			String value = line.substring(delimPosition+1).trim();
			map.put(key, value);
		}
		reader.close();
		CORE.PlayerCache = map;
		return map;
	}

	public static String lookupPlayerByUUID(String UUID){
		Map<String, String> map = null;
		try {
			map = readPropertiesFileAsMap();
		} catch (Exception e) {
			Utils.LOG_ERROR("Caught Exception"+e.toString());
			e.printStackTrace();
		}
		for (Entry<String, String> entry : map.entrySet()) {
			if (Objects.equals(UUID, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
}
