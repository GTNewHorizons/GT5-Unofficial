package miscutil;

import static miscutil.core.lib.CORE.DEBUG;
import static miscutil.core.lib.CORE.configSwitches.disableEnderIOIntegration;
import static miscutil.core.lib.CORE.configSwitches.disableIC2Recipes;
import static miscutil.core.lib.CORE.configSwitches.disableStaballoyBlastFurnaceRecipe;
import static miscutil.core.lib.CORE.configSwitches.enableAlternativeBatteryAlloy;
import static miscutil.core.lib.CORE.configSwitches.enableAlternativeDivisionSigilRecipe;
import static miscutil.core.lib.CORE.configSwitches.enableCustomAlvearyBlocks;
import static miscutil.core.lib.CORE.configSwitches.enableSolarGenerators;
import static miscutil.core.lib.CORE.configSwitches.enableThaumcraftShardUnification;
import gregtech.api.util.GT_Config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import miscutil.core.commands.CommandMath;
import miscutil.core.common.CommonProxy;
import miscutil.core.handler.events.LoginEventHandler;
import miscutil.core.item.general.RF2EU_Battery;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.math.MathUtils;
import miscutil.xmod.gregtech.HANDLER_GT;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid=CORE.MODID, name=CORE.name, version=CORE.VERSION, dependencies="required-after:Forge; after:IC2; after:ihl; after:psychedelicraft; after:gregtech; after:Forestry; after:MagicBees; after:CoFHCore; after:Growthcraft; after:Railcraft; after:CompactWindmills; after:ForbiddenMagic; after:MorePlanet; after:PneumaticCraft; after:ExtraUtilities; after:Thaumcraft; after:rftools; after:simplyjetpacks; after:BigReactors; after:EnderIO;")
public class MiscUtils
implements ActionListener
{ 

	@Mod.Instance(CORE.MODID)
	public static MiscUtils instance;

	@SidedProxy(clientSide="miscutil.core.proxy.ClientProxy", serverSide="miscutil.core.proxy.ServerProxy")
	public static CommonProxy proxy;



	public static void handleConfigFile(FMLPreInitializationEvent event) { 
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		//Debug
		DEBUG = config.getBoolean("debugMode", "debug", false, "Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)");
		disableEnderIOIntegration = config.getBoolean("disableEnderIO", "debug", false, "Disables EnderIO Integration.");
		disableStaballoyBlastFurnaceRecipe = config.getBoolean("disableStaballoyBlastFurnaceRecipe", "debug", false, "Disables Staballoy Blast Furnace Recipe.");
		//disableCentrifugeFormation = config.getBoolean("disableCentrifuge", "debug", false, "Keeps the Items around, just stops the multiblock forming. (It's broken currently, needs in depth testing)");
		
		//Machines
		enableSolarGenerators = config.getBoolean("enableSolarGenerators", "machines", false, "These may be overpowered, Consult a local electrician.");
		enableThaumcraftShardUnification = config.getBoolean("enableThaumcraftShardUnification", "machines", false, "Allows the use of TC shards across many recipes by oreDicting them into a common group.");
		enableAlternativeBatteryAlloy = config.getBoolean("enableAlternativeBatteryAlloy", "machines", false, "Adds a non-Antimony using Battery Alloy. Not Balanced at all..");
		disableIC2Recipes = config.getBoolean("disableIC2Recipes", "machines", false, "Alkaluscraft Related - Removes IC2 Cables Except glass fibre. Few other Misc Tweaks.");
		enableAlternativeDivisionSigilRecipe = config.getBoolean("enableAlternativeDivisionSigilRecipe", "machines", false, "Utilizes Neutronium instead.");
		
		//Options
		RF2EU_Battery.rfPerEU = config.getInt("rfUsedPerEUForUniversalBatteries", "configurables", 4, 1, 1000, "How much RF is a single unit of EU worth? (Most mods use 4:1 ratio)");
		
		//Features
		enableCustomAlvearyBlocks = config.getBoolean("enableCustomAlvearyBlocks", "features", false, "Enables Custom Alveary Blocks.");
		
		config.save(); 
	}
	
	public static String randomDust_A;
	public static String randomDust_B;
	public static String randomDust_C;
	public static String randomDust_D;
	
	protected void FirstCall(){
		Utils.LOG_WARNING("Summoning up mystic powers.");
		String[] infusedDusts = {"Fire", "Water", "Earth", "Air", "Order", "Entropy"};
		int a = MathUtils.randInt(0, 5);
		int b = MathUtils.randInt(0, 5);
		int c = MathUtils.randInt(0, 5);
		int d = MathUtils.randInt(0, 5);
		String infusedDust1 = "dustInfused"+infusedDusts[a];
		String infusedDust2 = "dustInfused"+infusedDusts[b];
		String infusedDust3 = "dustInfused"+infusedDusts[c];
		String infusedDust4 = "dustInfused"+infusedDusts[d];
		Utils.LOG_INFO("Found the aspect of "+infusedDust1+" to embody into energy crystals.");
		Utils.LOG_INFO("Found the aspect of "+infusedDust2+" to embody into energy crystals.");
		Utils.LOG_INFO("Found the aspect of "+infusedDust3+" to embody into energy crystals.");
		Utils.LOG_INFO("Found the aspect of "+infusedDust4+" to embody into energy crystals.");
		randomDust_A = infusedDust1;
		randomDust_B = infusedDust2;
		randomDust_C = infusedDust3;
		randomDust_D = infusedDust4;
		//ItemStack a1 = UtilsItems.getItemStackOfAmountFromOreDict("dustInfused"+infusedDusts[a], 8);
		//ItemStack b1 = UtilsItems.getItemStackOfAmountFromOreDict("dustInfused"+infusedDusts[b], 8);
		//ItemStack c1 = UtilsItems.getItemStackOfAmountFromOreDict("dustInfused"+infusedDusts[c], 8);
		//ItemStack d1 = UtilsItems.getItemStackOfAmountFromOreDict("dustInfused"+infusedDusts[d], 8);
		
	}




	//Pre-Init
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Utils.LOG_INFO("Loading "+CORE.name+" V"+CORE.VERSION);
		FirstCall();
		FMLCommonHandler.instance().bus().register(new LoginEventHandler());        
		Utils.LOG_INFO("Login Handler Initialized");

		handleConfigFile(event);
		proxy.registerTileEntities();
		proxy.registerRenderThings();
		HANDLER_GT.mMaterialProperties = new GT_Config(new Configuration(new File(new File(event.getModConfigurationDirectory(), "MiscUtils"), "MaterialProperties.cfg")));
		proxy.preInit(event);
	}

	//Init
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);				
		//MinecraftForge.EVENT_BUS.register(this);
		//FMLCommonHandler.instance().bus().register(this);
		proxy.registerNetworkStuff();
	}

	//Post-Init
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);		
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandMath());
	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

}
