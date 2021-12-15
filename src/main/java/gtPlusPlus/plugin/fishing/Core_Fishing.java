package gtPlusPlus.plugin.fishing;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.plugin.manager.Core_Manager;

public class Core_Fishing implements IPlugin {

	final static Core_Fishing mInstance;

	static {
		mInstance = new Core_Fishing();
		mInstance.log("Preparing "+mInstance.getPluginName()+" for use.");
	}
	
	Core_Fishing() {
		Core_Manager.registerPlugin(this);
	}
	
	@Override
	public boolean preInit() {
		return false;
	}

	@Override
	public boolean init() {
		return false;
	}

	@Override
	public boolean postInit() {
		return false;
	}

	@Override
	public boolean serverStart() {
		return false;
	}

	@Override
	public boolean serverStop() {
		return false;
	}

	@Override
	public String getPluginName() {
		return "GT++ Fishing Module";
	}

	@Override
	public String getPluginAbbreviation() {
		return "Fish";
	}

}
