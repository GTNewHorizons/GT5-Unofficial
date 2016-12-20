package gtPlusPlus.core.common;

import static gtPlusPlus.core.lib.CORE.DEBUG;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.*;
import gtPlusPlus.core.handler.events.BlockEventHandler;
import gtPlusPlus.core.handler.events.PickaxeBlockBreakEventHandler;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.RECIPE_CONSTANTS;
import gtPlusPlus.core.tileentities.ModTileEntities;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.debug.DEBUG_INIT;
import gtPlusPlus.core.util.player.PlayerCache;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;

public class CommonProxy {
	
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
			handleGtIntegrationInit();
		}
	}
	private static void handleGtIntegrationInit(){
		Utils.LOG_INFO("Setting up our own GT_Proxy.");
		Meta_GT_Proxy GtProxy = new Meta_GT_Proxy();	
		for (String tOreName : OreDictionary.getOreNames()) {
			ItemStack tOreStack;
			Utils.LOG_INFO("Iterating list of GT materials for custom tool parts.");
			for (Iterator i$ = OreDictionary.getOres(tOreName).iterator(); i$.hasNext(); GtProxy.registerOre(new OreDictionary.OreRegisterEvent(tOreName, tOreStack))) {
				Utils.LOG_INFO("Iterating Material");
				tOreStack = (ItemStack) i$.next();
			}
		}
	}

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
		COMPAT_IntermodStaging.preInit();
		//Apparently I should do this here. Might put it in Init for a test.
		//Growthcraft_Handler.run();
	}

	public void init(FMLInitializationEvent e) {
		//Debug Loading
		if (CORE.DEBUG){
					DEBUG_INIT.registerHandlers();
		}		
		ModItems.init();
		ModBlocks.init();
		RECIPE_CONSTANTS.initialise();
		//Prevents my Safes being destroyed.
		MinecraftForge.EVENT_BUS.register(new PickaxeBlockBreakEventHandler());
		//Block Handler for all events.
		MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
		
		//Compat Handling		
		COMPAT_HANDLER.registerMyModsOreDictEntries();
		COMPAT_HANDLER.intermodOreDictionarySupport();
		COMPAT_IntermodStaging.init();
	}

	public void postInit(FMLPostInitializationEvent e) {
		Utils.LOG_INFO("Cleaning up, doing postInit.");
		PlayerCache.initCache();		
		//Compat Handling
		COMPAT_HANDLER.InitialiseHandlerThenAddRecipes();
		COMPAT_HANDLER.RemoveRecipesFromOtherMods();
		COMPAT_HANDLER.startLoadingGregAPIBasedRecipes();
		COMPAT_IntermodStaging.postInit();
	}
	
	
	public void serverStarting(FMLServerStartingEvent e)
	{
		COMPAT_HANDLER.InitialiseLateHandlerThenAddRecipes();
	}

	public void registerNetworkStuff(){
		GuiHandler.init();
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
	
	public void generateMysteriousParticles(Entity theEntity) { }


}
