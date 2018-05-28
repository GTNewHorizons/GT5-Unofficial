package gtPlusPlus.core.common;

import static gtPlusPlus.core.lib.CORE.DEBUG;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.Entity;

import gregtech.api.enums.ItemList;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.ChunkManager;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.InternalEntityRegistry;
import gtPlusPlus.core.handler.*;
import gtPlusPlus.core.handler.events.*;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.RECIPES_Old_Circuits;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.tileentities.ModTileEntities;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.debug.DEBUG_INIT;
import gtPlusPlus.core.util.player.PlayerCache;
import gtPlusPlus.xmod.eio.handler.HandlerTooltip_EIO;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraftforge.common.ForgeChunkManager;

public class CommonProxy {

	public static Meta_GT_Proxy GtProxy;
	private boolean mFluidsGenerated = false;

	public CommonProxy(){
		//Should Register Gregtech Materials I've Made
		Utils.registerEvent(this);
		if (LoadedMods.Gregtech){
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				Logger.INFO("We're using Gregtech 5.09 Experimental.");
			}
			else {
				Logger.INFO("We're using Gregtech 5.08 or an earlier fork.");
			}
			Logger.INFO("Setting up our own GT_Proxy.");
			GtProxy = new Meta_GT_Proxy();
		}
		else {
			GtProxy = null;
		}
	}

	public void preInit(final FMLPreInitializationEvent e) {
		Logger.INFO("Doing some house cleaning.");
		LoadedMods.checkLoaded();
		Logger.INFO("Making sure we're ready to party!");


		if (!DEBUG){
			Logger.WARNING("Development mode not enabled.");
		}
		else if (DEBUG){
			Logger.INFO("Development mode enabled.");
		}
		else {
			Logger.WARNING("Development mode not set.");
		}
		
		//Moved from Init after Debug Loading.
		//29/01/18 - Alkalus
		//Moved earlier into PreInit, so that Items exist before they're called upon in recipes.
		//20/03/18 - Alkalus
		ModItems.init();
		ModBlocks.init();
		CI.preInit();
		
		AddToCreativeTab.initialiseTabs();
		
		
		COMPAT_IntermodStaging.preInit();
		BookHandler.run();
		//Registration of entities and renderers
		Logger.INFO("[Proxy] Calling Entity registrator.");
		registerEntities();
		Logger.INFO("[Proxy] Calling Tile Entity registrator.");
		registerTileEntities();
		
		
		Logger.INFO("[Proxy] Calling Render registrator.");
		registerRenderThings();

		if (!mFluidsGenerated && ItemList.valueOf("Cell_Empty").hasBeenSet()) {
			Material.generateQueuedFluids();
			mFluidsGenerated = true;
		}
	}

	public void init(final FMLInitializationEvent e) {
		//Debug Loading
		if (CORE.DEBUG){
			DEBUG_INIT.registerHandlers();
		}
		if (!mFluidsGenerated && ItemList.valueOf("Cell_Empty").hasBeenSet()) {
			Material.generateQueuedFluids();
			mFluidsGenerated = true;
		}
		else {
			Logger.INFO("[ERROR] Did not generate fluids at all.");
			Logger.WARNING("[ERROR] Did not generate fluids at all.");
			Logger.ERROR("[ERROR] Did not generate fluids at all.");
		}
		CI.init();

		/**
		 * Register the Event Handlers.
		 */

		//Prevents my Safes being destroyed.
		Utils.registerEvent(new PickaxeBlockBreakEventHandler());
		//Block Handler for all events.
		Utils.registerEvent(new BlockEventHandler());
		Utils.registerEvent(new GeneralTooltipEventHandler());
		//Handles Custom tooltips for EIO.
		Utils.registerEvent(new HandlerTooltip_EIO());
		
		//Register Chunkloader
		ForgeChunkManager.setForcedChunkLoadingCallback(GTplusplus.instance, ChunkManager.getInstance());
		Utils.registerEvent(ChunkManager.getInstance());

		if (ConfigSwitches.disableZombieReinforcement){
			//Make Zombie reinforcements fuck off.
			Utils.registerEvent(new ZombieBackupSpawnEventHandler());
		}

		/**
		 * End of Subscribe Event registration.
		 */

		//Compat Handling
		COMPAT_HANDLER.registerMyModsOreDictEntries();
		COMPAT_HANDLER.intermodOreDictionarySupport();
		COMPAT_IntermodStaging.init();
	}

	public void postInit(final FMLPostInitializationEvent e) {
		Logger.INFO("Cleaning up, doing postInit.");
		PlayerCache.initCache();

		//Make Burnables burnable
		if (!CORE.burnables.isEmpty()){
			BurnableFuelHandler fuelHandler = new BurnableFuelHandler();
			GameRegistry.registerFuelHandler(fuelHandler);
			Logger.INFO("[Fuel Handler] Registering "+fuelHandler.getClass().getName());
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
