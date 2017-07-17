package gtPlusPlus.core.common;

import static gtPlusPlus.core.lib.CORE.DEBUG;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.InternalEntityRegistry;
import gtPlusPlus.core.handler.*;
import gtPlusPlus.core.handler.events.BlockEventHandler;
import gtPlusPlus.core.handler.events.PickaxeBlockBreakEventHandler;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.RECIPES_Old_Circuits;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.recipe.LOADER_Machine_Components;
import gtPlusPlus.core.tileentities.ModTileEntities;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.debug.DEBUG_INIT;
import gtPlusPlus.core.util.player.PlayerCache;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public static Meta_GT_Proxy GtProxy;

	public CommonProxy(){
		//Should Register Gregtech Materials I've Made
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		if (LoadedMods.Gregtech){
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				Utils.LOG_INFO("We're using Gregtech 5.09 Experimental.");
			}
			else {
				Utils.LOG_INFO("We're using Gregtech 5.08 or an equivalent fork.");
			}
			Utils.LOG_INFO("Setting up our own GT_Proxy.");
			GtProxy = new Meta_GT_Proxy();
		}
		else {
			GtProxy = null;
		}
	}

	public void preInit(final FMLPreInitializationEvent e) {
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
		COMPAT_IntermodStaging.preInit();
		//Apparently I should do this here. Might put it in Init for a test.
		//Growthcraft_Handler.run();
	}

	public void init(final FMLInitializationEvent e) {
		//Debug Loading
		if (CORE.DEBUG){
			DEBUG_INIT.registerHandlers();
		}
		ModItems.init();
		ModBlocks.init();
		CI.Init();
		//Prevents my Safes being destroyed.
		MinecraftForge.EVENT_BUS.register(new PickaxeBlockBreakEventHandler());
		//Block Handler for all events.
		MinecraftForge.EVENT_BUS.register(new BlockEventHandler());

		//Compat Handling
		COMPAT_HANDLER.registerMyModsOreDictEntries();
		COMPAT_HANDLER.intermodOreDictionarySupport();
		COMPAT_IntermodStaging.init();
	}

	public void postInit(final FMLPostInitializationEvent e) {
		Utils.LOG_INFO("Cleaning up, doing postInit.");
		PlayerCache.initCache();

		//Circuits
		if (CORE.configSwitches.enableOldGTcircuits){
			RECIPES_Old_Circuits.handleCircuits();
			new RECIPES_Old_Circuits();
		}

		//Make Burnables burnable
		if (!CORE.burnables.isEmpty()){
			BurnableFuelHandler fuelHandler = new BurnableFuelHandler();
			GameRegistry.registerFuelHandler(fuelHandler);
			Utils.LOG_INFO("[Fuel Handler] Registering "+fuelHandler.getClass().getName());
		}
		
		//Compat Handling
		COMPAT_HANDLER.InitialiseHandlerThenAddRecipes();
		COMPAT_HANDLER.RemoveRecipesFromOtherMods();
		COMPAT_HANDLER.startLoadingGregAPIBasedRecipes();
		COMPAT_IntermodStaging.postInit();
	}


	public void serverStarting(final FMLServerStartingEvent e)
	{
		COMPAT_HANDLER.InitialiseLateHandlerThenAddRecipes();
	}

	public void registerNetworkStuff(){
		GuiHandler.init();
	}
	
	public void registerEntities(){
		InternalEntityRegistry.registerEntities();
	}

	public void registerTileEntities(){
		ModTileEntities.init();
	}

	public void registerRenderThings() {

	}

	public int addArmor(final String armor) {
		return 0;
	}

	public void generateMysteriousParticles(final Entity entity) {
	}

}
