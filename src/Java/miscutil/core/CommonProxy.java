package miscutil.core;

import miscutil.core.block.ModBlocks;
import miscutil.core.gui.ModGUI;
import miscutil.core.item.ModItems;
import miscutil.core.lib.Strings;
import miscutil.core.tileentities.ModTileEntities;
import miscutil.core.util.Utils;
import miscutil.gregtech.init.InitGregtech;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		ModItems.init();
		ModBlocks.init();
		

		//Register Gregtech related items
		if (Loader.isModLoaded("gregtech") == true) {
			Utils.LOG_INFO("Gregtech Found - Loading Resources.");
			Strings.GREGTECH = true;
			Utils.LOG_INFO("Begining registration & initialization of Gregtech related content.");
			// Init Gregtech
			InitGregtech.run();

		}
		else { 
			Utils.LOG_WARNING("Gregtech not Found - Skipping Resources.");
			Strings.GREGTECH = false;
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

	public int addArmor(String armor) {
		return 0;
	}

}
