package miscutil.core.common;

import static miscutil.core.lib.CORE.DEBUG;
import static miscutil.core.lib.LoadedMods.Gregtech;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.block.ModBlocks;
import miscutil.core.common.compat.COMPAT_HANDLER;
import miscutil.core.gui.ModGUI;
import miscutil.core.handler.registration.RegistrationHandler;
import miscutil.core.item.ModItems;
import miscutil.core.lib.CORE;
import miscutil.core.tileentities.ModTileEntities;
import miscutil.core.util.Utils;
import miscutil.core.util.UtilsItems;
import miscutil.gregtech.api.init.InitGregtech;
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

		RegistrationHandler.run();

	}

	public void postInit(FMLPostInitializationEvent e) {
		registerOreDict();
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

	@SuppressWarnings("static-method")
	private void registerOreDict(){

		Utils.LOG_INFO("Registering Materials with OreDict.");
		//In-house

		//tools
		GT_OreDictUnificator.registerOre("craftingToolSandHammer", new ItemStack(ModItems.itemSandstoneHammer));
		GT_OreDictUnificator.registerOre("ingotBloodSteel", new ItemStack(ModItems.itemIngotBloodSteel));
		GT_OreDictUnificator.registerOre("ingotStaballoy", new ItemStack(ModItems.itemIngotStaballoy));

		//Plates
		GT_OreDictUnificator.registerOre("plateBloodSteel", new ItemStack(ModItems.itemPlateBloodSteel));
		GT_OreDictUnificator.registerOre("plateStaballoy", new ItemStack(ModItems.itemPlateStaballoy));

		//Blocks
		GT_OreDictUnificator.registerOre("blockStaballoy", new ItemStack(Item.getItemFromBlock(ModBlocks.blockStaballoy)));
		OreDictionary.registerOre("blockBloodSteel", new ItemStack(ModBlocks.blockBloodSteel));


		for(int i=1; i<=10; i++){
			GT_OreDictUnificator.registerOre("bufferCore_"+CORE.VOLTAGES[i-1], new ItemStack(UtilsItems.getItem("miscutils:item.itemBufferCore"+i)));
		}

		//Do Inter-Mod Compatibility
		COMPAT_HANDLER.run();
	}

	@SuppressWarnings("static-method")
	public int addArmor(String armor) {
		return 0;
	}

}
