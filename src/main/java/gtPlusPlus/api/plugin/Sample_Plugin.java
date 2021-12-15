package gtPlusPlus.api.plugin;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.plugin.manager.Core_Manager;

public final class Sample_Plugin implements IPlugin {

	public Sample_Plugin() {
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
	public boolean serverStart() {
		return true;
	}

	@Override
	public boolean serverStop() {
		return true;
	}

	@Override
	public String getPluginName() {
		return "Sample Plugin";
	}

	@Override
	public String getPluginAbbreviation() {
		return "Test";
	}

}
