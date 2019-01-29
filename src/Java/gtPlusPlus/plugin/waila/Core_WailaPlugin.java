package gtPlusPlus.plugin.waila;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.plugin.manager.Core_Manager;

public class Core_WailaPlugin implements IPlugin {

	final static Core_WailaPlugin mInstance;
	static boolean mActive = false;

	static {
		mInstance = new Core_WailaPlugin();
		mInstance.log("Preparing "+mInstance.getPluginName()+" for use.");
	}
	
	Core_WailaPlugin() {
		Core_Manager.registerPlugin(this);
	}
	
	@Override
	public boolean preInit() {
		if (LoadedMods.Waila) {
			mActive = true;
		}		
		return mActive;
	}

	@Override
	public boolean init() {
		return mActive;
	}

	@Override
	public boolean postInit() {
		return mActive;
	}

	@Override
	public String getPluginName() {
		return "GT++ WAILA module";
	}

	@Override
	public String getPluginAbbreviation() {
		return "Look";
	}

}
