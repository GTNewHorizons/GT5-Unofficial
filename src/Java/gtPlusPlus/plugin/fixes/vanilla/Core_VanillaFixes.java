package gtPlusPlus.plugin.fixes.vanilla;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.manager.Core_Manager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Core_VanillaFixes implements IPlugin {

	final static Core_VanillaFixes mInstance;
	final static VanillaBedHeightFix mBedFixInstance;
	final static VanillaBackgroundMusicFix mMusicFixInstance;

	static {
		mInstance = new Core_VanillaFixes();
		mBedFixInstance = new VanillaBedHeightFix(mInstance);
		mMusicFixInstance = new VanillaBackgroundMusicFix(mInstance);
		mInstance.log("Preparing "+mInstance.getPluginName()+" for use.");
	}
	
	Core_VanillaFixes() {
		Core_Manager.registerPlugin(this);
	}
	
	@Override
	public boolean preInit() {		
		return fixVanillaOD();
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
		mMusicFixInstance.manage();
		return true;
	}

	@Override
	public boolean serverStop() {
		return true;
	}

	@Override
	public String getPluginName() {
		return "GT++ Vanilla Fixes Module";
	}

	@Override
	public String getPluginAbbreviation() {
		return "VFIX";
	}
	
	private boolean fixVanillaOD() {		
		registerToOreDict(ItemUtils.getSimpleStack(Items.nether_wart), "cropNetherWart");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.reeds), "sugarcane");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.paper), "paper");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.ender_pearl), "enderpearl");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.bone), "bone");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.gunpowder), "gunpowder");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.string), "string");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.nether_star), "netherStar");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.leather), "leather");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.feather), "feather");	
		registerToOreDict(ItemUtils.getSimpleStack(Items.egg), "egg");
		registerToOreDict(ItemUtils.getSimpleStack(Blocks.end_stone), "endstone");	
		registerToOreDict(ItemUtils.getSimpleStack(Blocks.vine), "vine");	
		registerToOreDict(ItemUtils.getSimpleStack(Blocks.cactus), "blockCactus");	
		registerToOreDict(ItemUtils.getSimpleStack(Blocks.grass), "grass");	
		registerToOreDict(ItemUtils.getSimpleStack(Blocks.obsidian), "obsidian");	
		registerToOreDict(ItemUtils.getSimpleStack(Blocks.crafting_table), "workbench");		
		return true;
	}
	
	private void registerToOreDict(ItemStack aStack, String aString) {
		mInstance.log("Registering "+aStack.getDisplayName()+" to OreDictionary under the tag '"+aString+"'. (Added to Forge in 1.8.9)");
		ItemUtils.addItemToOreDictionary(aStack, aString);		
	}

}
