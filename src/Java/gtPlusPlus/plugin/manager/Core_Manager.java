package gtPlusPlus.plugin.manager;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class Core_Manager {

	public static AutoMap<IPlugin> mPlugins = new AutoMap<IPlugin>();
	
	/**
	 * @param plugin - Dynamically registers the plugin for loading.
	 */
	public static void registerPlugin(IPlugin plugin) {
		Logger.INFO("[Plugin] " + "Registered "+plugin.getPluginName()+".");
		mPlugins.put(plugin);
	}	
	
	/**
	 * Dynamically loads all class objects within the "gtPlusPlus.plugin" package.
	 */
	public static void veryEarlyInit() {
		if (ReflectionUtils.dynamicallyLoadClassesInPackage("gtPlusPlus.plugin")) {
			Logger.INFO("[Plugin] Plugin System loaded.");
		}
	}
	
	public static boolean preInit() {
		try {			
			for (IPlugin h : mPlugins) {
				if (h.preInit()) {
					Logger.INFO("[Plugin] Completed Pre-Init Phase for "+h.getPluginName()+".");
				}
				else {
					Logger.INFO("[Plugin] Failed during Pre-Init Phase for "+h.getPluginName()+".");					
				}
			}			
			return true;
		}
		catch (Throwable t) {}
		return false;		
	}
	
	public static boolean init() {
		try {			
			for (IPlugin h : mPlugins) {
				if (h.init()) {
					Logger.INFO("[Plugin] Completed Init Phase for "+h.getPluginName()+".");
				}
				else {
					Logger.INFO("[Plugin] Failed during Init Phase for "+h.getPluginName()+".");					
				}
			}			
			return true;
		}
		catch (Throwable t) {}
		return false;		
	}
	
	public static boolean postInit() {
		try {			
			for (IPlugin h : mPlugins) {
				if (h.postInit()) {
					Logger.INFO("[Plugin] Completed Post-Init Phase for "+h.getPluginName()+".");
				}
				else {
					Logger.INFO("[Plugin] Failed during Post-Init Phase for "+h.getPluginName()+".");					
				}
			}			
			return true;
		}
		catch (Throwable t) {}
		return false;		
	}
	
	
}
