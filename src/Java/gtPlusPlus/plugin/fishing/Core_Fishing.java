package gtPlusPlus.plugin.fishing;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.plugin.manager.Core_Manager;

public class Core_Fishing implements IPlugin {

	Core_Fishing() {
		Core_Manager.registerPlugin(this);
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
		return "GT++ Fishing Module";
	}

	@Override
	public String getPluginAbbreviation() {
		return "FISH";
	}

}
