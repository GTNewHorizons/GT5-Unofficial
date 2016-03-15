package miscutil.core.common;

import static miscutil.core.lib.CORE.DEBUG;
import static miscutil.core.lib.LoadedMods.Gregtech;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.block.ModBlocks;
import miscutil.core.gui.ModGUI;
import miscutil.core.item.ModItems;
import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.tileentities.ModTileEntities;
import miscutil.core.util.Utils;
import miscutil.gregtech.init.InitGregtech;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		/*
		 * 
		 * CORE.DEBUG Parameters area
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
		 * End CORE.DEBUG
		 */		
		ModItems.init();
		ModBlocks.init();

		/**
		 * Enable Dev mode related content
		 */
		if (CORE.DEBUG){
			//InitEnderIO.run();
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
		//GameRegistry.registerTileEntity(TileEntityBloodSteelChest.class, CORE.MODID);
		//GameRegistry.registerTileEntity(TileEntityArcaneInfuser.class, "TileEntityArcaneInfuser");
	}

	public void registerRenderThings() {

	}

	public void registerOreDict(){

		Utils.LOG_INFO("Registering Ingots & Plates with OreDict.");
		//In-house
		//Ingots
		//OreDictionary.registerOre("ingotBloodSteel", new ItemStack(ModItems.itemIngotBloodSteel));
		GT_OreDictUnificator.registerOre("ingotBloodSteel", new ItemStack(ModItems.itemIngotBloodSteel));
		//OreDictionary.registerOre("ingotStaballoy", new ItemStack(ModItems.itemIngotStaballoy));
		GT_OreDictUnificator.registerOre("ingotStaballoy", new ItemStack(ModItems.itemIngotStaballoy));

		//Plates
		GT_OreDictUnificator.registerOre("plateBloodSteel", new ItemStack(ModItems.itemPlateBloodSteel));
		GT_OreDictUnificator.registerOre("plateStaballoy", new ItemStack(ModItems.itemPlateStaballoy));

		//Blocks
		GT_OreDictUnificator.registerOre("blockStaballoy", new ItemStack(Item.getItemFromBlock(ModBlocks.blockStaballoy)));
		OreDictionary.registerOre("blockBloodSteel", new ItemStack(ModBlocks.blockBloodSteel));


		//InterMod
		if (LoadedMods.Big_Reactors){
			GT_OreDictUnificator.registerOre("plateBlutonium", new ItemStack(ModItems.itemPlateBlutonium));
			GT_OreDictUnificator.registerOre("plateCyanite", new ItemStack(ModItems.itemPlateCyanite));
			GT_OreDictUnificator.registerOre("plateLudicrite", new ItemStack(ModItems.itemPlateLudicrite));
		}
		if (LoadedMods.EnderIO){
			GT_OreDictUnificator.registerOre("plateConductiveIron", new ItemStack(ModItems.itemPlateConductiveIron));
			GT_OreDictUnificator.registerOre("plateDarkSteel", new ItemStack(ModItems.itemPlateDarkSteel));
			GT_OreDictUnificator.registerOre("plateElectricalSteel", new ItemStack(ModItems.itemPlateElectricalSteel));
			GT_OreDictUnificator.registerOre("plateEnergeticAlloy", new ItemStack(ModItems.itemPlateEnergeticAlloy));
			GT_OreDictUnificator.registerOre("platePulsatingIron", new ItemStack(ModItems.itemPlatePulsatingIron));
			GT_OreDictUnificator.registerOre("plateRedstoneAlloy", new ItemStack(ModItems.itemPlateRedstoneAlloy));
			GT_OreDictUnificator.registerOre("plateSoularium", new ItemStack(ModItems.itemPlateSoularium));
			GT_OreDictUnificator.registerOre("plateVibrantAlloy", new ItemStack(ModItems.itemPlateVibrantAlloy));
		}
		if (LoadedMods.Simply_Jetpacks){
			GT_OreDictUnificator.registerOre("plateEnrichedSoularium", new ItemStack(ModItems.itemPlateEnrichedSoularium));

		}
		if (LoadedMods.RFTools){
			GT_OreDictUnificator.registerOre("plateDimensionShard", new ItemStack(ModItems.itemPlateDimensionShard));

		}
		if (LoadedMods.Thaumcraft){
			try{
			Item em = null;
			//Item em1 = GameRegistry.findItem("ThaumCraft", "ItemResource:16");
			//Item em2 = GameRegistry.findItem("ThaumCraft", "<ItemResource:16>");
			
			Item em1 = Utils.getItem("Thaumcraft:ItemResource:16");
			Utils.LOG_WARNING("Found: "+em1.toString());
			if (!em1.equals(null)){
				em = em1;
			}			
			else {
				em = null;
			}			
			if (!em.equals(null)){
				ItemStack voidIngot = new ItemStack(em,1,16);
				//GT_OreDictUnificator.registerOre("ingotVoidMetal", new ItemStack(em));
				//GameRegistry.registerCustomItemStack("ingotVoidMetal", voidIngot);
				GT_OreDictUnificator.registerOre("ingotVoidMetal", voidIngot);
			}
			else {
				Utils.LOG_WARNING("Void Metal Ingot not found.");
			}
			} catch (NullPointerException e) {
				Utils.LOG_ERROR("Void Metal Ingot not found. [NULL]");
	        }
			GT_OreDictUnificator.registerOre("plateVoidMetal", new ItemStack(ModItems.itemPlateVoidMetal));
			//System.exit(0);
			
		}
		if (LoadedMods.Extra_Utils){
			try {
			Item em = null;			
			Item em1 = Utils.getItem("ExtraUtilities:bedrockiumIngot");
			Utils.LOG_WARNING("Found: "+em1.toString());
			if (!em1.equals(null)){
				em = em1;
			}
			if (!em.equals(null)){
				GT_OreDictUnificator.registerOre("ingotBedrockium", new ItemStack(em));
			}
			else {
				Utils.LOG_WARNING("Bedrockium Ingot not found.");
			}
			} catch (NullPointerException e) {
				Utils.LOG_ERROR("Bedrockium Ingot not found. [NULL]");
	        }
			
			GT_OreDictUnificator.registerOre("plateBedrockium", new ItemStack(ModItems.itemPlateBedrockium));
		}
		if (LoadedMods.PneumaticCraft){
			GT_OreDictUnificator.registerOre("plateCompressedIron", new ItemStack(ModItems.itemPlateCompressedIron));
		}
		//Misc
	}

	public int addArmor(String armor) {
		return 0;
	}

}
