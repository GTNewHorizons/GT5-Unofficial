package gtPlusPlus.plugin.fixes.vanilla;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.plugin.manager.Core_Manager;

public class Core_VanillaFixes implements IPlugin {

	final static Core_VanillaFixes mInstance;

	static {
		mInstance = new Core_VanillaFixes();
		mInstance.log("Preparing "+mInstance.getPluginName()+" for use.");
	}
	
	Core_VanillaFixes() {
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
	public String getPluginName() {
		return "GT++ Vanilla Fixes Module";
	}

	@Override
	public String getPluginAbbreviation() {
		return "VFIX";
	}

}
