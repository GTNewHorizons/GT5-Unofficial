package gtPlusPlus;

import static gtPlusPlus.core.lib.CORE.DEBUG;
import static gtPlusPlus.core.lib.CORE.configSwitches.*;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_WorldAccelerator.BlacklistedTileEntiyClassNames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.reflect.FieldUtils;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.EmptyRecipeMap;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import gtPlusPlus.core.commands.CommandMath;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.handler.events.LoginEventHandler;
import gtPlusPlus.core.item.general.RF2EU_Battery;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.gregtech.CustomGTMaterials;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.geo.GeoUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.networking.NetworkUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

@MCVersion(value = "1.7.10")
@Mod(modid = CORE.MODID, name = CORE.name, version = CORE.VERSION, dependencies = "required-after:Forge; after:PlayerAPI; after:dreamcraft; after:IC2; after:ihl; after:psychedelicraft; after:gregtech; after:Forestry; after:MagicBees; after:CoFHCore; after:Growthcraft; after:Railcraft; after:CompactWindmills; after:ForbiddenMagic; after:MorePlanet; after:PneumaticCraft; after:ExtraUtilities; after:Thaumcraft; after:rftools; after:simplyjetpacks; after:BigReactors; after:EnderIO;")
public class GTplusplus implements ActionListener {

	@Mod.Instance(CORE.MODID)
	public static GTplusplus instance;

	protected static Meta_GT_Proxy gregtechproxy;

	@SidedProxy(clientSide = "gtPlusPlus.core.proxy.ClientProxy", serverSide = "gtPlusPlus.core.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static void handleConfigFile(final FMLPreInitializationEvent event) {
		final Configuration config = new Configuration(
				new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
		config.load();

		// Debug
		DEBUG = config.getBoolean("debugMode", "debug", false,
				"Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)");
		disableEnderIOIntegration = config.getBoolean("disableEnderIO", "debug", false,
				"Disables EnderIO Integration.");

		// Machines
		enableThaumcraftShardUnification = config.getBoolean("enableThaumcraftShardUnification", "machines", false,
				"Allows the use of TC shards across many recipes by oreDicting them into a common group.");
		enableAlternativeBatteryAlloy = config.getBoolean("enableAlternativeBatteryAlloy", "machines", false,
				"Adds a non-Antimony using Battery Alloy. Not Balanced at all..");
		disableIC2Recipes = config.getBoolean("disableIC2Recipes", "machines", false,
				"Alkaluscraft Related - Removes IC2 Cables Except glass fibre. Few other Misc Tweaks.");
		enableAlternativeDivisionSigilRecipe = config.getBoolean("enableAlternativeDivisionSigilRecipe", "machines",
				false, "Utilizes Neutronium instead.");

		//Circuits
		CORE.configSwitches.enableCustomCircuits = config.getBoolean("enableCustomCircuits", "gregtech", true,
				"Adds custom circuits to expand past the Master Tier.");
		CORE.configSwitches.enableOldGTcircuits = config.getBoolean("enableOldGTcircuits", "gregtech", false,
				"Restores circuits and their recipes from Pre-5.09.28 times.");

		// Tools
		CORE.configSwitches.enableSkookumChoochers = config.getBoolean("enableSkookumChoochers", "gregtech", true,
				"Adds Custom GT Tools, called Skookum Choochers, functioning as a hard hammer and a wrench.");
		CORE.configSwitches.enableMultiSizeTools = config.getBoolean("enableMultiSizeTools", "gregtech", true,
				"Adds Custom GT Shovels and Pickaxes which mine in a 3x3 style. One of each whill be generated for each Gregtech Material which has Dense Plates and Long Rods available.");

		// Pipes & Cables
		CORE.configSwitches.enableCustom_Pipes = config.getBoolean("enableCustom_Pipes", "gregtech", true,
				"Adds Custom GT Fluid Pipes.");
		CORE.configSwitches.enableCustom_Cables = config.getBoolean("enableCustom_Cables", "gregtech", true,
				"Adds Custom GT Cables.");

		// Block Drops
		CORE.configSwitches.chanceToDropDrainedShard = config.getInt("chanceToDropDrainedShard", "blockdrops", 196, 0,
				10000, "Drained shards have a 1 in X chance to drop.");
		CORE.configSwitches.chanceToDropFluoriteOre = config.getInt("chanceToDropFluoriteOre", "blockdrops", 32, 0,
				10000, "Fluorite Ore has a 1 in X chance to drop from Limestone and a 1 in X*20 from Sandstone..");

		// Single machines
		CORE.configSwitches.enableMachine_SolarGenerators = config.getBoolean("enableSolarGenerators", "gregtech",
				false, "These may be overpowered, Consult a local electrician.");
		CORE.configSwitches.enableMachine_Safes = config.getBoolean("enableMachineSafes", "gregtech", true,
				"These protect your goodies/rare stuff.");
		CORE.configSwitches.enableMachine_Dehydrators = config.getBoolean("enableMachineDehydrators", "gregtech", true,
				"These dehydrate stuff.");
		CORE.configSwitches.enableMachine_SteamConverter = config.getBoolean("enableMachineSteamConverter", "gregtech",
				true, "Converts IC2 steam -> Railcraft steam.");
		CORE.configSwitches.enableMachine_FluidTanks = config.getBoolean("enableMachineFluidTanks", "gregtech", true,
				"Portable fluid tanks.");
		CORE.configSwitches.enableMachine_RocketEngines = config.getBoolean("enableMachineRocketEngines", "gregtech",
				true, "Diesel egines with different internals, they consume less fuel overall.");
		CORE.configSwitches.enableMachine_GeothermalEngines = config.getBoolean("enableMachineGeothermalEngines",
				"gregtech", true, "These may be overpowered, Consult a local geologist.");
		CORE.configSwitches.enableMachine_WorldAccelerators = config.getBoolean("enableMachineWorldAccelerators",
				"gregtech", true, "These allow boosting Block/TileEntity Tick times [OP].");
		CORE.configSwitches.enableMachine_Tesseracts = config.getBoolean("enableMachineTesseracts",
				"gregtech", true, "Tesseracts for wireless item/fluid movement.");
		CORE.configSwitches.enableMachine_SimpleWasher = config.getBoolean("enableMachineSimpleWasher",
				"gregtech", true, "Very basic automated cauldron for dust washing.");
		CORE.configSwitches.enableMachine_Pollution = config.getBoolean("enableMachinePollution",
				"gregtech", true, "Pollution Detector & Scrubbers.");

		// Multi machines
		CORE.configSwitches.enableMultiblock_AlloyBlastSmelter = config.getBoolean("enableMultiblockAlloyBlastSmelter",
				"gregtech", true,
				"Required to smelt most high tier materials from GT++. Also smelts everything else to molten metal.");
		CORE.configSwitches.enableMultiblock_IndustrialCentrifuge = config
				.getBoolean("enableMultiblockIndustrialCentrifuge", "gregtech", true, "Spin, Spin, Spiiiin.");
		CORE.configSwitches.enableMultiblock_IndustrialCokeOven = config.getBoolean(
				"enableMultiblockIndustrialCokeOven", "gregtech", true,
				"Pyro Oven Alternative, older, more realistic, better.");
		CORE.configSwitches.enableMultiblock_IndustrialElectrolyzer = config.getBoolean(
				"enableMultiblockIndustrialElectrolyzer", "gregtech", true,
				"Electrolyzes things with extra bling factor.");
		CORE.configSwitches.enableMultiblock_IndustrialMacerationStack = config.getBoolean(
				"enableMultiblockIndustrialMacerationStack", "gregtech", true,
				"A hyper efficient maceration tower, nets more bonus outputs.");
		CORE.configSwitches.enableMultiblock_IndustrialPlatePress = config.getBoolean(
				"enableMultiblockIndustrialPlatePress", "gregtech", true, "Industrial bendering machine thingo.");
		CORE.configSwitches.enableMultiblock_IndustrialWireMill = config.getBoolean(
				"enableMultiblockIndustrialWireMill", "gregtech", true, "Produces fine wire and exotic cables.");
		CORE.configSwitches.enableMultiblock_IronBlastFurnace = config.getBoolean("enableMultiblockIronBlastFurnace",
				"gregtech", true, "Skip the Bronze age, very slowly.");
		CORE.configSwitches.enableMultiblock_MatterFabricator = config.getBoolean("enableMultiblockMatterFabricator",
				"gregtech", true, "?FAB?RIC?ATE MA?TT?ER.");
		CORE.configSwitches.enableMultiblock_MultiTank = config.getBoolean("enableMultiblockMultiTank", "gregtech",
				true, "Tall tanks, each layer adds extra fluid storage.");
		CORE.configSwitches.enableMultiblock_PowerSubstation = config.getBoolean("enableMultiblockPowerSubstation",
				"gregtech", true, "For managing large power grids.");
		CORE.configSwitches.enableMultiblock_LiquidFluorideThoriumReactor = config.getBoolean(
				"enableMultiblockLiquidFluorideThoriumReactor", "gregtech", true, "For supplying large power grids.");
		CORE.configSwitches.enableMultiblock_NuclearFuelRefinery = config.getBoolean(
				"enableMultiblock_NuclearFuelRefinery", "gregtech", true,
				"Refines molten chemicals into nuclear fuels.");
		CORE.configSwitches.enableMultiblock_IndustrialSifter = config.getBoolean("enableMultiblock_IndustrialSifter",
				"gregtech", true, "Large scale sifting.");
		CORE.configSwitches.enableMachine_ThermalBoiler = config.getBoolean("enableMachineThermalBoiler",
				"gregtech", true, "Thermal Boiler from GT4. Can Filter Lava for resources.");
		
		// Options
		RF2EU_Battery.rfPerEU = config.getInt("rfUsedPerEUForUniversalBatteries", "configurables", 4, 1, 1000,
				"How much RF is a single unit of EU worth? (Most mods use 4:1 ratio)");

		// Features
		enableCustomAlvearyBlocks = config.getBoolean("enableCustomAlvearyBlocks", "features", false,
				"Enables Custom Alveary Blocks.");

		//Biomes
		CORE.DARKBIOME_ID = config.getInt("darkbiome_ID", "worldgen", 238, 1, 254, "The biome within the Dark Dimension.");

		//Blacklisted Accelerator TileEntities
		BlacklistedTileEntiyClassNames = new String[] { "com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderQuarry" };
		BlacklistedTileEntiyClassNames = config.getStringList(
				"BlacklistedTileEntiyClassNames", "gregtech",
				BlacklistedTileEntiyClassNames,
				"The Canonical Class-Names of TileEntities that should be ignored by the WorldAccelerator");
		
		
		config.save();
	}

	// Loads Textures
	@SideOnly(value = Side.CLIENT)
	public static void loadTextures() {
		Utils.LOG_INFO("Loading some textures on the client.");
		// Tools
		Utils.LOG_WARNING("Processing texture: " + TexturesGtTools.SKOOKUM_CHOOCHER.getTextureFile().getResourcePath());

		// Blocks
		Utils.LOG_WARNING("Processing texture: " + TexturesGtBlock.Casing_Machine_Dimensional.getTextureFile().getResourcePath());
	}

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Utils.LOG_INFO("Loading " + CORE.name + " V" + CORE.VERSION);
		
		//HTTP Requests
		CORE.MASTER_VERSION = NetworkUtils.getContentFromURL("https://raw.githubusercontent.com/draknyte1/GTplusplus/master/Recommended.txt").toLowerCase();
		CORE.USER_COUNTRY = GeoUtils.determineUsersCountry();
		
		// Handle GT++ Config
		handleConfigFile(event);
		
		CORE.DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		Utils.LOG_INFO("Latest is " + CORE.MASTER_VERSION + ". Updated? " + Utils.isModUpToDate());
		Utils.LOG_INFO("User's Country: " + CORE.USER_COUNTRY);

		// FirstCall();
		FMLCommonHandler.instance().bus().register(new LoginEventHandler());
		Utils.LOG_INFO("Login Handler Initialized");

		//Early load materials
		try {
			CustomGTMaterials.run();
		} catch (Throwable t){}
		
		if (CORE.configSwitches.enableOldGTcircuits && CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			removeCircuitRecipeMap(); //Bye shitty recipes.			
		}
		
		// HANDLER_GT.mMaterialProperties = new GT_Config(new Configuration(new
		// File(new File(event.getModConfigurationDirectory(), "GTplusplus"),
		// "MaterialProperties.cfg")));
		proxy.preInit(event);
	}

	// Init
	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		proxy.init(event);

		Utils.LOG_INFO("[Proxy] Calling Entity registrator.");
		proxy.registerEntities();
		Utils.LOG_INFO("[Proxy] Calling Tile Entity registrator.");
		proxy.registerTileEntities();
		Utils.LOG_INFO("[Proxy] Calling Render registrator.");
		proxy.registerRenderThings();

		proxy.registerNetworkStuff();

	}

	// Post-Init
	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		proxy.postInit(event);

		if (DEBUG) {
			this.dumpGtRecipeMap(Gregtech_Recipe_Map.sChemicalDehydratorRecipes);
			this.dumpGtRecipeMap(Gregtech_Recipe_Map.sCokeOvenRecipes);
			this.dumpGtRecipeMap(Gregtech_Recipe_Map.sMatterFab2Recipes);
			this.dumpGtRecipeMap(Gregtech_Recipe_Map.sAlloyBlastSmelterRecipes);
		}
		
		for (Materials s : gtPlusPlus.core.material.gregtech.CustomGTMaterials.Custom_GT_Materials){
			Utils.LOG_INFO("Verification for New Material: "+s.mName);
		}

		// ~
		//ReflectionUtils.becauseIWorkHard();
		// Utils.LOG_INFO("Activating GT OreDictionary Handler, this can take
		// some time.");
		Utils.LOG_INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Utils.LOG_INFO("| Recipes succesfully Loaded: " + RegistrationHandler.recipesSuccess + " | Failed: "
				+ RegistrationHandler.recipesFailed + " |");
		Utils.LOG_INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// Meta_GT_Proxy.activateOreDictHandler();
		Utils.LOG_INFO("Finally, we are finished. Have some cripsy bacon as a reward.");
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {

	}

	@EventHandler
	public void serverStarting(final FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandMath());
	}

	@Mod.EventHandler
	public void serverStopping(final FMLServerStoppingEvent event) {

	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {

	}

	protected void dumpGtRecipeMap(final GT_Recipe_Map r) {
		final Collection<GT_Recipe> x = r.mRecipeList;
		Utils.LOG_INFO("Dumping " + r.mUnlocalizedName + " Recipes for Debug.");
		for (final GT_Recipe newBo : x) {
			Utils.LOG_INFO("========================");
			Utils.LOG_INFO("Dumping Input: " + ItemUtils.getArrayStackNames(newBo.mInputs));
			Utils.LOG_INFO("Dumping Inputs " + ItemUtils.getFluidArrayStackNames(newBo.mFluidInputs));
			Utils.LOG_INFO("Dumping Duration: " + newBo.mDuration);
			Utils.LOG_INFO("Dumping EU/t: " + newBo.mEUt);
			Utils.LOG_INFO("Dumping Output: " + ItemUtils.getArrayStackNames(newBo.mOutputs));
			Utils.LOG_INFO("Dumping Output: " + ItemUtils.getFluidArrayStackNames(newBo.mFluidOutputs));
			Utils.LOG_INFO("========================");
		}
	}


	private static boolean removeCircuitRecipeMap(){
		try {			
			Utils.LOG_INFO("[Old Feature - Circuits] Trying to override the Circuit Assembler Recipe map, so that no recipes for new circuits get added.");
			ReflectionUtils.setFinalStatic(GT_Recipe_Map.class.getDeclaredField("sCircuitAssemblerRecipes"), new EmptyRecipeMap(new HashSet<GT_Recipe>(0), "gt.recipe.removed", "Removed", null, GT_Values.RES_PATH_GUI + "basicmachines/Default", 0, 0, 0, 0, 0, GT_Values.E, 0, GT_Values.E, true, false));		
			Field jaffar = GT_Recipe_Map.class.getDeclaredField("sCircuitAssemblerRecipes");
			FieldUtils.removeFinalModifier(jaffar, true);
			jaffar.set(null, new EmptyRecipeMap(new HashSet<GT_Recipe>(0), "gt.recipe.removed", "Removed", null, GT_Values.RES_PATH_GUI + "basicmachines/Default", 0, 0, 0, 0, 0, GT_Values.E, 0, GT_Values.E, true, false));
			Utils.LOG_INFO("[Old Feature - Circuits] Successfully replaced circuit assembler recipe map with one that cannot hold recipes.");
		}
		catch (Exception e) {
			Utils.LOG_INFO("[Old Feature - Circuits] Failed removing circuit assembler recipe map.");
			return false;
		}
		return true;
	}
}
