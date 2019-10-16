package gtPlusPlus.plugin.agrichem;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.plugin.agrichem.fluids.FluidLoader;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAgrichemBase;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAlgaeBase;
import gtPlusPlus.plugin.manager.Core_Manager;

public class Core_Agrichem implements IPlugin {

	final static Core_Agrichem mInstance;

	static {
		mInstance = new Core_Agrichem();
		Core_Manager.registerPlugin(mInstance);
		mInstance.log("Preparing "+mInstance.getPluginName()+" for use.");
	}

	@Override
	public boolean preInit() {		
		FluidLoader.generate();
		new ItemAlgaeBase();
		new ItemAgrichemBase();
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
		return "GT++ Agrichemistry Module";
	}

	@Override
	public String getPluginAbbreviation() {
		return "FARM";
	}

}
