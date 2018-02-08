package gtPlusPlus.api.interfaces;

import gtPlusPlus.plugin.manager.Core_Manager;

public interface IPlugin {

	public default void register() {
		Core_Manager.registerPlugin(this);
	}	
	
	public String getPluginName();

	public boolean preInit();
	public boolean init();
	public boolean postInit();
	
}
