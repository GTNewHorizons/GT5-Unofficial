package miscutil.core.common;

import static miscutil.core.lib.LoadedMods.Gregtech;
import static miscutil.core.lib.Strings.DEBUG;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.block.ModBlocks;
import miscutil.core.gui.ModGUI;
import miscutil.core.item.ModItems;
import miscutil.core.lib.LoadedMods;
import miscutil.core.lib.Strings;
import miscutil.core.tileentities.ModTileEntities;
import miscutil.core.util.Utils;
import miscutil.enderio.init.InitEnderIO;
import miscutil.gregtech.init.InitGregtech;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		/*
		 * 
		 * Strings.DEBUG Parameters area
		 * 
		 */
		//Logs
		if (!DEBUG){
			Utils.LOG_WARNING("Development mode not enabled.");
		}
		else if (DEBUG){
			Utils.LOG_INFO("Development mode enabled.");
		}
		else {
			Utils.LOG_WARNING("Development mode not set.");
		}
		/*
		 * End Strings.DEBUG
		 */		
		ModItems.init();
		ModBlocks.init();

		/**
		 * Enable Dev mode related content
		 */
		if (Strings.DEBUG){
			InitEnderIO.run();
		}

		//Register Gregtech related items
		if (Gregtech) {
			Utils.LOG_INFO("Gregtech Found - Loading Resources.");
			//Utils.LOG_INFO("Begining initialization of Gregtech related content.");
			// Init Gregtech
			InitGregtech.run();

		}
		else { 
			Utils.LOG_WARNING("Gregtech not Found - Skipping Resources.");
		}

	}

	public void init(FMLInitializationEvent e) {



	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public void registerNetworkStuff(){
		ModGUI.init();
		//NetworkRegistry.INSTANCE.registerGuiHandler(MiscUtils.instance, new BloodSteelFurnaceGuiHandler());

	}

	public void registerTileEntities(){
		ModTileEntities.init();
		//GameRegistry.registerTileEntity(TileEntityBloodSteelChest.class, "tileEntityBloodSteelChest");
		//GameRegistry.registerTileEntity(TileEntityBloodSteelFurnace.class, "tileEntityBloodSteelFurnace");
		//GameRegistry.registerTileEntity(TileEntityBloodSteelChest.class, Strings.MODID);
		//GameRegistry.registerTileEntity(TileEntityArcaneInfuser.class, "TileEntityArcaneInfuser");
	}

	public void registerRenderThings() {

	}

	public void registerOreDict(){
		
		Utils.LOG_INFO("Registering Ingots & Plates with OreDict.");
		//Ingots
		//OreDictionary.registerOre("ingotBloodSteel", new ItemStack(ModItems.itemIngotBloodSteel));
		GT_OreDictUnificator.registerOre("ingotBloodSteel", new ItemStack(ModItems.itemIngotBloodSteel));
		//OreDictionary.registerOre("ingotStaballoy", new ItemStack(ModItems.itemIngotStaballoy));
		GT_OreDictUnificator.registerOre("ingotStaballoy", new ItemStack(ModItems.itemIngotStaballoy));

		//Plates
		//InterMod
		if (LoadedMods.Big_Reactors){
			OreDictionary.registerOre("plateBlutonium", new ItemStack(ModItems.itemPlateBlutonium));
			OreDictionary.registerOre("plateCyanite", new ItemStack(ModItems.itemPlateCyanite));
			OreDictionary.registerOre("plateLudicrite", new ItemStack(ModItems.itemPlateLudicrite));
		}
		if (LoadedMods.EnderIO){
			OreDictionary.registerOre("plateConductiveIron", new ItemStack(ModItems.itemPlateConductiveIron));
			OreDictionary.registerOre("plateDarkSteel", new ItemStack(ModItems.itemPlateDarkSteel));
			OreDictionary.registerOre("plateElectricalSteel", new ItemStack(ModItems.itemPlateElectricalSteel));
			OreDictionary.registerOre("plateEnergeticAlloy", new ItemStack(ModItems.itemPlateEnergeticAlloy));
			OreDictionary.registerOre("platePulsatingIron", new ItemStack(ModItems.itemPlatePulsatingIron));
			OreDictionary.registerOre("plateRedstoneAlloy", new ItemStack(ModItems.itemPlateRedstoneAlloy));
			OreDictionary.registerOre("plateSoularium", new ItemStack(ModItems.itemPlateSoularium));
			OreDictionary.registerOre("plateVibrantAlloy", new ItemStack(ModItems.itemPlateVibrantAlloy));
		}
		if (LoadedMods.Simply_Jetpacks){
			OreDictionary.registerOre("plateEnrichedSoularium", new ItemStack(ModItems.itemPlateEnrichedSoularium));

		}
		if (LoadedMods.RFTools){
			OreDictionary.registerOre("plateDimensionShard", new ItemStack(ModItems.itemPlateDimensionShard));

		}
		if (LoadedMods.Thaumcraft){
			OreDictionary.registerOre("plateVoidMetal", new ItemStack(ModItems.itemPlateVoidMetal));

		}
		if (LoadedMods.Extra_Utils){
			OreDictionary.registerOre("plateBedrockium", new ItemStack(ModItems.itemPlateBedrockium));
		}
		if (LoadedMods.PneumaticCraft){
			OreDictionary.registerOre("plateCompressedIron", new ItemStack(ModItems.itemPlateCompressedIron));
		}
		//In-house
		OreDictionary.registerOre("plateBloodSteel", new ItemStack(ModItems.itemPlateBloodSteel));
		OreDictionary.registerOre("plateStaballoy", new ItemStack(ModItems.itemPlateStaballoy));


		//Blocks
		//OreDictionary.registerOre("blockBloodSteel", new ItemStack(ModBlocks.blockBloodSteel.ge)));


		//Misc
	}

	public int addArmor(String armor) {
		return 0;
	}

}
