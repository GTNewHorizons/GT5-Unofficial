package gtPlusPlus.plugin.manager;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.array.AutoMap;

public class Core_Manager {

	public static AutoMap<IPlugin> mPlugins = new AutoMap<IPlugin>();
	public static void registerPlugin(IPlugin plug) {
		mPlugins.put(plug);
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
