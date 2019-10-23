package gtPlusPlus.plugin.agrichem;

import java.util.List;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.agrichem.block.AgrichemFluids;
import gtPlusPlus.plugin.agrichem.fluids.FluidLoader;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAgrichemBase;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAlgaeBase;
import gtPlusPlus.plugin.agrichem.item.algae.ItemBioChip;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Core_Agrichem implements IPlugin {

	final static Core_Agrichem mInstance;
	

	static {
		mInstance = new Core_Agrichem();
		Core_Manager.registerPlugin(mInstance);
		mInstance.log("Preparing "+mInstance.getPluginName()+" for use.");
	}

	@Override
	public boolean preInit() {
		mInstance.log("Generating Fluids");		
		FluidLoader.generate();
		AgrichemFluids.init();
		mInstance.log("Generating Items");
		return true;
	}

	@Override
	public boolean init() {
		mInstance.log("Setting Items");	
		return true;
	}

	@Override
	public boolean postInit() {
		mInstance.log("Generating Recipes");
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
