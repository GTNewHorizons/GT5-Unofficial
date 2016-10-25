package gtPlusPlus;

import static gtPlusPlus.core.lib.CORE.DEBUG;
import static gtPlusPlus.core.lib.CORE.configSwitches.disableEnderIOIntegration;
import static gtPlusPlus.core.lib.CORE.configSwitches.disableIC2Recipes;
import static gtPlusPlus.core.lib.CORE.configSwitches.disableStaballoyBlastFurnaceRecipe;
import static gtPlusPlus.core.lib.CORE.configSwitches.enableAlternativeBatteryAlloy;
import static gtPlusPlus.core.lib.CORE.configSwitches.enableAlternativeDivisionSigilRecipe;
import static gtPlusPlus.core.lib.CORE.configSwitches.enableCustomAlvearyBlocks;
import static gtPlusPlus.core.lib.CORE.configSwitches.enableSolarGenerators;
import static gtPlusPlus.core.lib.CORE.configSwitches.enableThaumcraftShardUnification;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import gtPlusPlus.core.commands.CommandMath;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.handler.events.LoginEventHandler;
import gtPlusPlus.core.item.general.RF2EU_Battery;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;

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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid=CORE.MODID, name=CORE.name, version=CORE.VERSION, dependencies="required-after:Forge; after:IC2; after:ihl; after:psychedelicraft; after:gregtech; after:Forestry; after:MagicBees; after:CoFHCore; after:Growthcraft; after:Railcraft; after:CompactWindmills; after:ForbiddenMagic; after:MorePlanet; after:PneumaticCraft; after:ExtraUtilities; after:Thaumcraft; after:rftools; after:simplyjetpacks; after:BigReactors; after:EnderIO;")
public class GTplusplus
implements ActionListener
{ 

	@Mod.Instance(CORE.MODID)
	public static GTplusplus instance;

	protected static Meta_GT_Proxy gregtechproxy;

	@SidedProxy(clientSide="gtPlusPlus.core.proxy.ClientProxy", serverSide="gtPlusPlus.core.proxy.ServerProxy")
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
		Utils.LOG_INFO("Found the aspect of "+infusedDusts[a]+" to embody into energy crystals.");
		Utils.LOG_INFO("Found the aspect of "+infusedDusts[b]+" to embody into eshonergy crystals.");
		Utils.LOG_INFO("Found the aspect of "+infusedDusts[c]+" to embody into energy crystals.");
		Utils.LOG_INFO("Found the aspect of "+infusedDusts[d]+" to embody into energy crystals.");
		randomDust_A = infusedDust1;
		randomDust_B = infusedDust2;
		randomDust_C = infusedDust3;
		randomDust_D = infusedDust4;
		//ItemStack a1 = UtilsItems.getItemStackOfAmountFromOreDict("dustInfused"+infusedDusts[a], 8);
		//ItemStack b1 = UtilsItems.getItemStackOfAmountFromOreDict("dustInfused"+infusedDusts[b], 8);
		//ItemStack c1 = UtilsItems.getItemStackOfAmountFromOreDict("dustInfused"+infusedDusts[c], 8);
		//ItemStack d1 = UtilsItems.getItemStackOfAmountFromOreDict("dustInfused"+infusedDusts[d], 8);	


	}

	@SideOnly(value=Side.CLIENT)
	public static void loadTextures(){
		Utils.LOG_INFO("Loading some textures on the client.");
		//Tools
		Utils.LOG_WARNING("Processing texture: "+TexturesGtTools.SKOOKUM_CHOOCHER.getTextureFile().getResourcePath());

		//Blocks
		Utils.LOG_WARNING("Processing texture: "+TexturesGtBlocks.Casing_Machine_Dimensional.getTextureFile().getResourcePath());
	}




	//Pre-Init
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Utils.LOG_INFO("Loading "+CORE.name+" V"+CORE.VERSION);
		Utils.LOG_INFO("Latest is V"+CORE.MASTER_VERSION+". Updated? "+Utils.isModUpToDate());
		FirstCall();
		FMLCommonHandler.instance().bus().register(new LoginEventHandler());        
		Utils.LOG_INFO("Login Handler Initialized");

		handleConfigFile(event);
		proxy.registerTileEntities();
		proxy.registerRenderThings();
		HANDLER_GT.mMaterialProperties = new GT_Config(new Configuration(new File(new File(event.getModConfigurationDirectory(), "GTplusplus"), "MaterialProperties.cfg")));
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

		if (DEBUG){
			dumpGtRecipeMap(Gregtech_Recipe_Map.sChemicalDehydratorRecipes);
			dumpGtRecipeMap(Gregtech_Recipe_Map.sCokeOvenRecipes);
			dumpGtRecipeMap(Gregtech_Recipe_Map.sMatterFab2Recipes);
		}
		dumpGtRecipeMap(Gregtech_Recipe_Map.sAlloyBlastSmelterRecipes);

		//~
		ReflectionUtils.becauseIWorkHard();

		Utils.LOG_INFO("Activating GT OreDictionary Handler, this can take some time.");
		Utils.LOG_INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Utils.LOG_INFO("| Recipes succesfully Loaded: "+RegistrationHandler.recipesSuccess+" | Failed: "+RegistrationHandler.recipesFailed + " |");
		Utils.LOG_INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Meta_GT_Proxy.activateOreDictHandler();
		Utils.LOG_INFO("Finally, we are finished. Have some cripsy bacon as a reward.");
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

	protected void dumpGtRecipeMap(GT_Recipe_Map r){	
		Collection<GT_Recipe> x = r.mRecipeList;
		Utils.LOG_INFO("Dumping "+r.mUnlocalizedName+" Recipes for Debug.");
		for(GT_Recipe newBo : x){
			Utils.LOG_INFO("========================");
			Utils.LOG_INFO("Dumping Input: "+ItemUtils.getArrayStackNames(newBo.mInputs));
			Utils.LOG_INFO("Dumping Inputs "+ItemUtils.getFluidArrayStackNames(newBo.mFluidInputs));
			Utils.LOG_INFO("Dumping Duration: "+newBo.mDuration);
			Utils.LOG_INFO("Dumping EU/t: "+newBo.mEUt);
			Utils.LOG_INFO("Dumping Output: "+ItemUtils.getArrayStackNames(newBo.mOutputs));
			Utils.LOG_INFO("Dumping Output: "+ItemUtils.getFluidArrayStackNames(newBo.mFluidOutputs));
			Utils.LOG_INFO("========================");
		}
	}

}
