package gtPlusPlus.core.config;

import static gtPlusPlus.core.item.general.RF2EU_Battery.rfPerEU;
import static gtPlusPlus.core.lib.CORE.*;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.*;
import static gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_WorldAccelerator.BlacklistedTileEntiyClassNames;

import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static void handleConfigFile(final FMLPreInitializationEvent event) {
		final Configuration config = new Configuration(
				new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
		config.load();
	
		enableUpdateChecker = config.getBoolean("enableUpdateChecker", "debug", true,
				"Stops mod checking for updates.");
	
		// Debug
		DEBUG = config.getBoolean("debugMode", "debug", false,
				"Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)");
		disableEnderIOIntegration = config.getBoolean("disableEnderIO", "debug", false,
				"Disables EnderIO Integration.");
		disableEnderIOIngotTooltips = config.getBoolean("disableEnderIOIngotTooltips", "debug", false,
				"Disables EnderIO Ingot Tooltips. These apparently may cause issues for a very small number of users.");
		MACHINE_INFO = config.getBoolean("enableMachineInfoLogging", "debug", false,
				"Makes many machines display lots of debug logging.");
		showHiddenNEIItems = config.getBoolean("showHiddenNEIItems", "debug", false,
				"Makes all items hidden from NEI display.");
		dumpItemAndBlockData = config.getBoolean("dumpItemAndBlockData", "debug", false,
				"Dumps all GT++ and Toxic Everglade Data to en_US.lang in the config folder. This config option can be used by foreign players to generate blank .lang files, which they can populate with their language of choice.");
	
	
		// Machines
		enableThaumcraftShardUnification = config.getBoolean("enableThaumcraftShardUnification", "machines", false,
				"Allows the use of TC shards across many recipes by oreDicting them into a common group.");
		enableAlternativeBatteryAlloy = config.getBoolean("enableAlternativeBatteryAlloy", "machines", false,
				"Adds a non-Antimony using Battery Alloy. Not Balanced at all..");
		disableIC2Recipes = config.getBoolean("disableIC2Recipes", "machines", false,
				"Alkaluscraft Related - Removes IC2 Cables Except glass fibre. Few other Misc Tweaks.");
		enableAlternativeDivisionSigilRecipe = config.getBoolean("enableAlternativeDivisionSigilRecipe", "machines",
				false, "Utilizes Neutronium instead.");
		boilerSteamPerSecond = config.getInt("boilerSteamPerSecond", "machines", 750, 0, 10000, "Sets the steam per second value in LV,MV,HV boilers (respectively 1x,2x,3x this number for the tiers)");
	
		//Circuits
		enableCustomCircuits = config.getBoolean("enableCustomCircuits", "gregtech", false,
				"Adds custom circuits to expand past the Master Tier. Only really recommended to enable if enableOldGTcircuits is enabled.");
		enableOldGTcircuits = config.getBoolean("enableOldGTcircuits", "gregtech", false,
				"Restores circuits and their recipes from Pre-5.09.28 times.");
	
		// Tools
		enableSkookumChoochers = config.getBoolean("enableSkookumChoochers", "gregtech", true,
				"Adds Custom GT Tools, called Skookum Choochers, functioning as a hard hammer and a wrench.");
		enableMultiSizeTools = config.getBoolean("enableMultiSizeTools", "gregtech", true,
				"Adds Custom GT Shovels and Pickaxes which mine in a 3x3 style. One of each whill be generated for each Gregtech Material which has Dense Plates and Long Rods available.");
	
		// GT-Fixes
		enableNitroFix = config.getBoolean("enableNitroFix", "gregtech", false,
				"Restores the old Nitro-Diesel recipes.");		
		enableSulfuricAcidFix = config.getBoolean("enableSulfuricAcidFix", "gregtech", false,
				"Adds GT6 recipes for Sulfuric Acid. Should remove all pre-existing recipes.");		
		enableAnimatedTurbines = config.getBoolean("enableAnimatedTurbines", "gregtech", true,
				"Gives GT Gas/Steam turbines animated textures while running.");
		turbineCutoffBase = config.getInt("turbineCutoffBase", "gregtech", 75000, 0, Integer.MAX_VALUE, "Rotors below this durability will be removed, prevents NEI clutter. Minimum Durability is N * x, where N is the new value set and x is the turbine size, where 1 is Tiny and 4 is Huge. Set to 0 to disable.");
	
		// Pipes & Cables
		enableCustom_Pipes = config.getBoolean("enableCustom_Pipes", "gregtech", true,
				"Adds Custom GT Fluid Pipes.");
		enableCustom_Cables = config.getBoolean("enableCustom_Cables", "gregtech", true,
				"Adds Custom GT Cables.");
	
		// Block Drops
		chanceToDropDrainedShard = config.getInt("chanceToDropDrainedShard", "blockdrops", 196, 0,
				10000, "Drained shards have a 1 in X chance to drop.");
		chanceToDropFluoriteOre = config.getInt("chanceToDropFluoriteOre", "blockdrops", 32, 0,
				10000, "Fluorite Ore has a 1 in X chance to drop from Limestone and a 1 in X*20 from Sandstone..");
	
		// Single machines
		enableMachine_SolarGenerators = config.getBoolean("enableSolarGenerators", "gregtech",
				false, "These may be overpowered, Consult a local electrician.");		
		enableMachine_ComponentAssemblers = config.getBoolean("enableComponentAssemblers", "gregtech",
				true, "These construct machine components.");		
		enableMachine_Safes = config.getBoolean("enableMachineSafes", "gregtech", true,
				"These protect your goodies/rare stuff.");
		enableMachine_Dehydrators = config.getBoolean("enableMachineDehydrators", "gregtech", true,
				"These dehydrate stuff.");
		enableMachine_SteamConverter = config.getBoolean("enableMachineSteamConverter", "gregtech",
				true, "Converts IC2 steam -> Railcraft steam.");
		enableMachine_FluidTanks = config.getBoolean("enableMachineFluidTanks", "gregtech", true,
				"Portable fluid tanks.");
		enableMachine_RocketEngines = config.getBoolean("enableMachineRocketEngines", "gregtech",
				true, "Diesel egines with different internals, they consume less fuel overall.");
		enableMachine_GeothermalEngines = config.getBoolean("enableMachineGeothermalEngines",
				"gregtech", true, "These may be overpowered, Consult a local geologist.");
		enableMachine_WorldAccelerators = config.getBoolean("enableMachineWorldAccelerators",
				"gregtech", true, "These allow boosting Block/TileEntity Tick times [OP].");
		enableMachine_Tesseracts = config.getBoolean("enableMachineTesseracts",
				"gregtech", true, "Tesseracts for wireless item/fluid movement.");
		enableMachine_SimpleWasher = config.getBoolean("enableMachineSimpleWasher",
				"gregtech", true, "Very basic automated cauldron for dust washing.");
		enableMachine_Pollution = config.getBoolean("enableMachinePollution",
				"gregtech", true, "Pollution Detector & Scrubbers.");
	
		// Multi machines
		enableMultiblock_AlloyBlastSmelter = config.getBoolean("enableMultiblockAlloyBlastSmelter",
				"gregtech", true,
				"Required to smelt most high tier materials from GT++. Also smelts everything else to molten metal.");
		enableMultiblock_IndustrialCentrifuge = config
				.getBoolean("enableMultiblockIndustrialCentrifuge", "gregtech", true, "Spin, Spin, Spiiiin.");
		enableMultiblock_IndustrialCokeOven = config.getBoolean(
				"enableMultiblockIndustrialCokeOven", "gregtech", true,
				"Pyro Oven Alternative, older, more realistic, better.");
		enableMultiblock_IndustrialElectrolyzer = config.getBoolean(
				"enableMultiblockIndustrialElectrolyzer", "gregtech", true,
				"Electrolyzes things with extra bling factor.");
		enableMultiblock_IndustrialMacerationStack = config.getBoolean(
				"enableMultiblockIndustrialMacerationStack", "gregtech", true,
				"A hyper efficient maceration tower, nets more bonus outputs.");
		enableMultiblock_IndustrialPlatePress = config.getBoolean(
				"enableMultiblockIndustrialPlatePress", "gregtech", true, "Industrial bendering machine thingo.");
		enableMultiblock_IndustrialWireMill = config.getBoolean(
				"enableMultiblockIndustrialWireMill", "gregtech", true, "Produces fine wire and exotic cables.");
		enableMultiblock_IronBlastFurnace = config.getBoolean("enableMultiblockIronBlastFurnace",
				"gregtech", true, "Skip the Bronze age, very slowly.");
		enableMultiblock_MatterFabricator = config.getBoolean("enableMultiblockMatterFabricator",
				"gregtech", true, "?FAB?RIC?ATE MA?TT?ER.");
		enableMultiblock_MultiTank = config.getBoolean("enableMultiblockMultiTank", "gregtech",
				true, "Tall tanks, each layer adds extra fluid storage.");
		enableMultiblock_PowerSubstation = config.getBoolean("enableMultiblockPowerSubstation",
				"gregtech", true, "For managing large power grids.");
		enableMultiblock_LiquidFluorideThoriumReactor = config.getBoolean(
				"enableMultiblockLiquidFluorideThoriumReactor", "gregtech", true, "For supplying large power grids.");
		enableMultiblock_NuclearFuelRefinery = config.getBoolean(
				"enableMultiblock_NuclearFuelRefinery", "gregtech", true,
				"Refines molten chemicals into nuclear fuels.");
		enableMultiblock_IndustrialSifter = config.getBoolean("enableMultiblock_IndustrialSifter",
				"gregtech", true, "Large scale sifting.");
		enableMultiblock_LargeAutoCrafter = config.getBoolean("enableMultiblock_LargeAutoCrafter",
				"gregtech", true, "Can Assemble, Disassemble and Craft Project data from Data Sticks.");
		enableMultiblock_IndustrialThermalCentrifuge = config.getBoolean("enableMultiblock_IndustrialThermalCentrifuge",
				"gregtech", true, "Your warm spin for the ore thing.");
		enableMultiblock_IndustrialWashPlant = config.getBoolean("enableMultiblock_IndustrialWashPlant",
				"gregtech", true, "Used to wash the dirt, riiiiight offff..");
		enableMultiblock_ThermalBoiler = config.getBoolean("enableMachineThermalBoiler",
				"gregtech", true, "Thermal Boiler from GT4. Can Filter Lava for resources.");
		enableMultiblock_IndustrialCuttingMachine = config.getBoolean("enableMultiblock_IndustrialCuttingMachine",
				"gregtech", true, "Very fast and efficient Cutting Machine.");
		enableMultiblock_IndustrialFishingPort = config.getBoolean("enableMultiblock_IndustrialFishingPort",
				"gregtech", true, "Fish the seas, except on land.");		
		enableMultiblock_IndustrialExtrudingMachine = config.getBoolean("enableMultiblock_IndustrialExtrudingMachine",
				"gregtech", true, "Very fast and efficient Extruding Machine.");		
		enableMultiblock_IndustrialMultiMachine = config.getBoolean("enableMultiblock_IndustrialMultiMachine",
				"gregtech", true, "Can run recipes for 9 different types of machines.");
		enableMultiblock_Cyclotron = config.getBoolean("enableMultiblock_Cyclotron",
				"gregtech", true, "COMET - Scientific Cyclotron."); 
	
		// Options
		rfPerEU = config.getInt("rfUsedPerEUForUniversalBatteries", "configurables", 4, 1, 1000,
				"How much RF is a single unit of EU worth? (Most mods use 4:1 ratio)");
	
		// Features
		enableCustomCapes = config.getBoolean("enableSupporterCape", "features", true,
				"Enables Custom GT++ Cape.");		
		disableZombieReinforcement = config.getBoolean("disableZombieReinforcement", "features", false,
				"Disables Zombie Reinforcement on hard difficutly.");
	
		//Biomes
		EVERGLADES_ID = config.getInt("darkworld_ID", "worldgen", 227, 1, 254, "The ID of the Dark Dimension.");
		EVERGLADESBIOME_ID = config.getInt("darkbiome_ID", "worldgen", 238, 1, 254, "The biome within the Dark Dimension.");
	
		//Blacklisted Accelerator TileEntities
		BlacklistedTileEntiyClassNames = new String[] { "com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderQuarry" };
		BlacklistedTileEntiyClassNames = config.getStringList(
				"BlacklistedTileEntiyClassNames", "gregtech",
				BlacklistedTileEntiyClassNames,
				"The Canonical Class-Names of TileEntities that should be ignored by the WorldAccelerator");
	
		config.save();
	}

}
