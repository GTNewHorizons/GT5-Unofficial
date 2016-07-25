package miscutil.core.common;

import static miscutil.core.lib.CORE.DEBUG;

import java.util.Iterator;

import miscutil.core.block.ModBlocks;
import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.handler.COMPAT_HANDLER;
import miscutil.core.handler.COMPAT_IntermodStaging;
import miscutil.core.handler.GuiHandler;
import miscutil.core.handler.events.PickaxeBlockBreakEventHandler;
import miscutil.core.item.ModItems;
import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.recipe.RECIPE_CONSTANTS;
import miscutil.core.tileentities.ModTileEntities;
import miscutil.core.util.Utils;
import miscutil.core.util.debug.DEBUG_INIT;
import miscutil.core.util.player.PlayerCache;
import miscutil.core.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {
	
	public CommonProxy(){
		//Should Register Gregtech Materials I've Made
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		if (LoadedMods.Gregtech){
			for (String tOreName : OreDictionary.getOreNames()) {
				ItemStack tOreStack;
				for (Iterator i$ = OreDictionary.getOres(tOreName).iterator(); i$.hasNext(); Meta_GT_Proxy.registerOre(new OreDictionary.OreRegisterEvent(tOreName, tOreStack))) {
					Utils.LOG_INFO("Iterating Material");
					tOreStack = (ItemStack) i$.next();
				}
			}
			new Meta_GT_Proxy();
		}
	}

	public void preInit(FMLPreInitializationEvent e) {
		Utils.LOG_INFO("Doing some house cleaning.");		
		LoadedMods.checkLoaded();
		Utils.LOG_INFO("Making sure we're ready to party!");
		
		if (LoadedMods.Gregtech){
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				Utils.LOG_INFO("We're using Gregtech 5.09 Experimental.");
			}
			else {
				Utils.LOG_INFO("We're using Gregtech 5.08 or an equivalent fork.");
			}
		}
		
		
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
		MinecraftForge.EVENT_BUS.register(new PickaxeBlockBreakEventHandler());
		
		//Compat Handling		
		COMPAT_HANDLER.registerMyModsOreDictEntries();
		COMPAT_HANDLER.registerGregtechMachines();
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
