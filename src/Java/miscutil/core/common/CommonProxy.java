package miscutil.core.common;

import static miscutil.core.lib.CORE.DEBUG;
import miscutil.core.block.ModBlocks;
import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.gui.ModGUI;
import miscutil.core.handler.COMPAT_HANDLER;
import miscutil.core.handler.events.PickaxeBlockBreakEventHandler;
import miscutil.core.item.ModItems;
import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.tileentities.ModTileEntities;
import miscutil.core.util.PlayerCache;
import miscutil.core.util.Utils;
import miscutil.core.util.debug.DEBUG_INIT;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		Utils.LOG_INFO("Doing some house cleaning.");		
		LoadedMods.checkLoaded();
		Utils.LOG_INFO("Making sure we're ready to party!");
		if (!DEBUG){
			Utils.LOG_WARNING("Development mode not enabled.");
		}
		else if (DEBUG){
			Utils.LOG_INFO("Development mode enabled.");
		}
		else {
			Utils.LOG_WARNING("Development mode not set.");
		}		
		AddToCreativeTab.initialiseTabs();
	}

	public void init(FMLInitializationEvent e) {
		//Debug Loading
		if (CORE.DEBUG){
					DEBUG_INIT.registerHandlers();
		}
		ModItems.init();
		ModBlocks.init();
		MinecraftForge.EVENT_BUS.register(new PickaxeBlockBreakEventHandler());
		
		//Compat Handling		
		COMPAT_HANDLER.registerMyModsOreDictEntries();
		COMPAT_HANDLER.registerGregtechMachines();
		COMPAT_HANDLER.intermodOreDictionarySupport();
	}

	public void postInit(FMLPostInitializationEvent e) {
		Utils.LOG_INFO("Cleaning up, doing postInit.");
		PlayerCache.initCache();		
		//Compat Handling
		COMPAT_HANDLER.InitialiseHandlerThenAddRecipes();
		COMPAT_HANDLER.RemoveRecipesFromOtherMods();
		COMPAT_HANDLER.InitialiseLateHandlerThenAddRecipes();
		COMPAT_HANDLER.startLoadingGregAPIBasedRecipes();
	}

	public void registerNetworkStuff(){
		ModGUI.init();
	}

	public void registerTileEntities(){
		ModTileEntities.init();
	}

	public void registerRenderThings() {

	}

	@SuppressWarnings("static-method")
	public int addArmor(String armor) {
		return 0;
	}

}
