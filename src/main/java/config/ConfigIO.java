package config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigIO {
	
	private static final String CONFIG_PATH = "main/java/config/config.properties";
	private static final int CONFIG_SIZE = 0;
	
	private static Properties config;
	
	private ConfigIO() {
		
	}
	
	public static void load() {
		config = new Properties();
		try {
			config.load(new FileInputStream(CONFIG_PATH));
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load KekzTech config!");
		}
		if(config.size() != CONFIG_SIZE) {
			throw new IllegalStateException("KekzTech config is not expected size!");
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T valueType) {
		if(config.size() != CONFIG_SIZE) {
			throw new IllegalStateException("Tried to access config without loading it first");
		}
		return (T) config.get((Object) key);
	}
	
	public static void saveConfig() {
		try {
			config = (config == null) ? new Properties() : config;
			config.setProperty("key", "value");
			config.store(new FileOutputStream(CONFIG_PATH), "Welcome to KekzTech's config file :)");
		} catch (IOException e) {
			System.err.println("Failed to save changes to KekzTech config. Settings may be lost.");
		}
	}
	
}
