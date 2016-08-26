package miscutil.core.util.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class PlayerCache {

	private static final File cache = new File("PlayerCache.dat");

	public static final void initCache() {
		if (CORE.PlayerCache == null || CORE.PlayerCache.equals(null)){
			try {

				if (cache != null){
					CORE.PlayerCache = PlayerCache.readPropertiesFileAsMap();
					Utils.LOG_INFO("Loaded PlayerCache.dat");	
				}


			} catch (Exception e) {
				Utils.LOG_INFO("Failed to initialise PlayerCache.dat");
				PlayerCache.createPropertiesFile("PLAYER_", "DATA");
				//e.printStackTrace();
			}
		}
	}

	public static void createPropertiesFile(String playerName, String playerUUIDasString) {
		try {
			Properties props = new Properties();
			props.setProperty(playerName+" ", playerUUIDasString);
			OutputStream out = new FileOutputStream(cache);
			props.store(out, "Player Cache.");
			Utils.LOG_INFO("PlayerCache.dat created for future use.");
		}
		catch (Exception e ) {
			e.printStackTrace();
		}
	}

	public static void appendParamChanges(String playerName, String playerUUIDasString) {
		HashMap<String, UUID> playerInfo = new HashMap<String, UUID>();
		playerInfo.put(playerName, UUID.fromString(playerUUIDasString));
		
		/*try {
			Utils.LOG_INFO("Attempting to load "+cache.getName());
			properties.load(new FileInputStream(cache));
			if (properties == null || properties.equals(null)){
				Utils.LOG_INFO("Please wait.");
			}
			else {
				Utils.LOG_INFO("Loaded PlayerCache.dat");
				properties.setProperty(playerName+"_", playerUUIDasString);
				FileOutputStream fr=new FileOutputStream(cache);
				properties.store(fr, "Player Cache.");
				fr.close();
			}

		} */
		
		try
        {
               FileOutputStream fos = new FileOutputStream("PlayerCache.dat");
               ObjectOutputStream oos = new ObjectOutputStream(fos);
               oos.writeObject(playerInfo);
               oos.close();
               fos.close();
               Utils.LOG_INFO("Serialized Player data saved in PlayerCache.dat");
        }
		
		catch (IOException e) {
			Utils.LOG_INFO("No PlayerCache file found, creating one.");
			createPropertiesFile(playerName, playerUUIDasString);
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
	@Deprecated
	public static Map<String, String> readPropertiesFileAsMapOld() throws Exception {
		String delimiter = "=";
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new FileReader(cache));
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

	public static HashMap<String, UUID> readPropertiesFileAsMap() {
		HashMap<String, UUID> map = null;
	      try
	      {
	         FileInputStream fis = new FileInputStream(cache);
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         map = (HashMap<String, UUID>) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(IOException ioe)
	      {
	         ioe.printStackTrace();	
	         return null;
	      }catch(ClassNotFoundException c)
	      {
	    	  Utils.LOG_INFO("Class not found");
	         c.printStackTrace();
	         return null;
	      }
	      Utils.LOG_WARNING("Deserialized PlayerCache..");
	     return map;
	}

	public static String lookupPlayerByUUID(UUID UUID){
		
		try {
			World worldw = Minecraft.getMinecraft().thePlayer.worldObj;
			//if (!worldw.isRemote){
				
				
				try {
					Map<String, UUID> map = null;
					try {
						map = readPropertiesFileAsMap();
					} catch (Exception e) {
						Utils.LOG_INFO("With "+e.getCause()+" as cause, Caught Exception: "+e.toString());
						//e.printStackTrace();
					}
					for (Entry<String, UUID> entry : map.entrySet()) {
						if (Objects.equals(UUID, entry.getValue())) {
							return entry.getKey();
						}
					}
					return null;
				} catch (NullPointerException e) {
					Utils.LOG_INFO("With "+e.getCause()+" as cause, Caught Exception: "+e.toString());
					//e.printStackTrace();
				}			
			
			
			//}
				
				
		} catch (Throwable r){
			Utils.LOG_INFO("With "+r.getCause()+" as cause, Caught Exception: "+r.toString());			
		}		
		return null;
	}
}
