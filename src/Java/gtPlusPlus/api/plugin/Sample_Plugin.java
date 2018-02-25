package gtPlusPlus.api.plugin;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.plugin.manager.Core_Manager;

public class Sample_Plugin implements IPlugin {

	private Sample_Plugin() {
		Core_Manager.registerPlugin(this); //This must be called, else it won't load.
	}
	
	@Override
	public boolean preInit() {
		return true;
	}

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public boolean postInit() {
		return true;
	}

	@Override
	public String getPluginName() {
		return "Sample Plugin";
	}

}
