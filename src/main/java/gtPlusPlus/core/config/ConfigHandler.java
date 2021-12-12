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
	
		// Debug
		/*		DEBUG = config.getBoolean("debugMode", "debug", false,
						"Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)");*/
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
		
		//requireControlCores
		boolean temp = config.getBoolean("requireControlCores", "machines", true, "Multiblocks Require Control Cores");
		
		
		
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
		   
		enableHarderRecipesForHighTierCasings = config.getBoolean("enableHarderRecipesForHighTierCasings", "gregtech", false,
                "Makes LuV+ Casings and Hulls more difficult to craft.");		
		
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
		enableMachine_RF_Convetor = config.getBoolean("enableMachineRFConvetor", "gregtech",
				true, "Converts RF to GTEU. Requires COFH-Core to be installed.");
		
	
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
		enableWatchdogBGM = config.getInt("enableWatchdogBGM", "features", 0, 0, Short.MAX_VALUE, "Set to a value greater than 0 to reduce the ticks taken to delay between BGM tracks. Acceptable Values are 1-32767, where 0 is disabled. Vanilla Uses 12,000 & 24,000. 200 is 10s.");
	
		//Biomes
		EVERGLADES_ID = config.getInt("darkworld_ID", "worldgen", 227, 1, 254, "The ID of the Dark Dimension.");
		EVERGLADESBIOME_ID = config.getInt("darkbiome_ID", "worldgen", 238, 1, 254, "The biome within the Dark Dimension.");
	
		//Blacklisted Accelerator TileEntities
		BlacklistedTileEntiyClassNames = new String[] { "com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderQuarry" };
		BlacklistedTileEntiyClassNames = config.getStringList(
				"BlacklistedTileEntiyClassNames", "gregtech",
				BlacklistedTileEntiyClassNames,
				"The Canonical Class-Names of TileEntities that should be ignored by the WorldAccelerator");

		//Pollution
		pollutionPerSecondMultiPackager = config.get("pollution", "pollutionPerSecondMultiPackager", pollutionPerSecondMultiPackager,"pollution rate in gibbl/s for the Amazon warehousing depot").getInt(pollutionPerSecondMultiPackager);
		pollutionPerSecondMultiIndustrialAlloySmelter = config.get("pollution", "pollutionPerSecondMultiIndustrialAlloySmelter", pollutionPerSecondMultiIndustrialAlloySmelter,"pollution rate in gibbl/s for the Alloy blast smelter").getInt(pollutionPerSecondMultiIndustrialAlloySmelter);
		pollutionPerSecondMultiIndustrialArcFurnace = config.get("pollution", "pollutionPerSecondMultiIndustrialArcFurnace", pollutionPerSecondMultiIndustrialArcFurnace,"pollution rate in gibbl/s for the High current arc furnace").getInt(pollutionPerSecondMultiIndustrialArcFurnace);
		pollutionPerSecondMultiIndustrialCentrifuge = config.get("pollution", "pollutionPerSecondMultiIndustrialCentrifuge", pollutionPerSecondMultiIndustrialCentrifuge,"pollution rate in gibbl/s for the Industrial centrifuge").getInt(pollutionPerSecondMultiIndustrialCentrifuge);
		pollutionPerSecondMultiIndustrialCokeOven = config.get("pollution", "pollutionPerSecondMultiIndustrialCokeOven", pollutionPerSecondMultiIndustrialCokeOven,"pollution rate in gibbl/s for the Industrial coke oven").getInt(pollutionPerSecondMultiIndustrialCokeOven);
		pollutionPerSecondMultiIndustrialCuttingMachine = config.get("pollution", "pollutionPerSecondMultiIndustrialCuttingMachine", pollutionPerSecondMultiIndustrialCuttingMachine,"pollution rate in gibbl/s for the Cutting factory").getInt(pollutionPerSecondMultiIndustrialCuttingMachine);
		pollutionPerSecondMultiIndustrialDehydrator = config.get("pollution", "pollutionPerSecondMultiIndustrialDehydrator", pollutionPerSecondMultiIndustrialDehydrator,"pollution rate in gibbl/s for the Utupu-Tanuri").getInt(pollutionPerSecondMultiIndustrialDehydrator);
		pollutionPerSecondMultiIndustrialElectrolyzer = config.get("pollution", "pollutionPerSecondMultiIndustrialElectrolyzer", pollutionPerSecondMultiIndustrialElectrolyzer,"pollution rate in gibbl/s for the Industrial electrolyzer").getInt(pollutionPerSecondMultiIndustrialElectrolyzer);
		pollutionPerSecondMultiIndustrialExtruder = config.get("pollution", "pollutionPerSecondMultiIndustrialExtruder", pollutionPerSecondMultiIndustrialExtruder,"pollution rate in gibbl/s for the Industrial extrusion machine").getInt(pollutionPerSecondMultiIndustrialExtruder);
		pollutionPerSecondMultiIndustrialMacerator = config.get("pollution", "pollutionPerSecondMultiIndustrialMacerator", pollutionPerSecondMultiIndustrialMacerator,"pollution rate in gibbl/s for the Maceration stack").getInt(pollutionPerSecondMultiIndustrialMacerator);
		pollutionPerSecondMultiIndustrialMixer = config.get("pollution", "pollutionPerSecondMultiIndustrialMixer", pollutionPerSecondMultiIndustrialMixer,"pollution rate in gibbl/s for the Industrial mixing machine").getInt(pollutionPerSecondMultiIndustrialMixer);
		pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal = config.get("pollution", "pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal", pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal,"pollution rate in gibbl/s for the Large processing factory in metal mode").getInt(pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal);
		pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid = config.get("pollution", "pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid", pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid,"pollution rate in gibbl/s for the Large processing factory in fluid mode").getInt(pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid);
		pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc = config.get("pollution", "pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc", pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc,"pollution rate in gibbl/s for the Large processing factory in misc mode").getInt(pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc);
		pollutionPerSecondMultiIndustrialPlatePress_ModeForming = config.get("pollution", "pollutionPerSecondMultiIndustrialPlatePress_ModeForming", pollutionPerSecondMultiIndustrialPlatePress_ModeForming,"pollution rate in gibbl/s for the Industrial material press in forming mode").getInt(pollutionPerSecondMultiIndustrialPlatePress_ModeForming);
		pollutionPerSecondMultiIndustrialPlatePress_ModeBending = config.get("pollution", "pollutionPerSecondMultiIndustrialPlatePress_ModeBending", pollutionPerSecondMultiIndustrialPlatePress_ModeBending,"pollution rate in gibbl/s for the Industrial material press in bending mode").getInt(pollutionPerSecondMultiIndustrialPlatePress_ModeBending);
		pollutionPerSecondMultiIndustrialSifter = config.get("pollution", "pollutionPerSecondMultiIndustrialSifter", pollutionPerSecondMultiIndustrialSifter,"pollution rate in gibbl/s for the Large Sifter").getInt(pollutionPerSecondMultiIndustrialSifter);
		pollutionPerSecondMultiIndustrialThermalCentrifuge = config.get("pollution", "pollutionPerSecondMultiIndustrialThermalCentrifuge", pollutionPerSecondMultiIndustrialThermalCentrifuge,"pollution rate in gibbl/s for the Large thermal refinery").getInt(pollutionPerSecondMultiIndustrialThermalCentrifuge);
		pollutionPerSecondMultiIndustrialVacuumFreezer = config.get("pollution", "pollutionPerSecondMultiIndustrialVacuumFreezer", pollutionPerSecondMultiIndustrialVacuumFreezer,"pollution rate in gibbl/s for the Cryogenic freezer").getInt(pollutionPerSecondMultiIndustrialVacuumFreezer);
		pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath = config.get("pollution", "pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath", pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath,"pollution rate in gibbl/s for the Ore washing plant in chemical bath mode").getInt(pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath);
		pollutionPerSecondMultiIndustrialWashPlant_ModeWasher = config.get("pollution", "pollutionPerSecondMultiIndustrialWashPlant_ModeWasher", pollutionPerSecondMultiIndustrialWashPlant_ModeWasher,"pollution rate in gibbl/s for the Ore washing plant in ore washer mode").getInt(pollutionPerSecondMultiIndustrialWashPlant_ModeWasher);
		pollutionPerSecondMultiIndustrialWireMill = config.get("pollution", "pollutionPerSecondMultiIndustrialWireMill", pollutionPerSecondMultiIndustrialWireMill,"pollution rate in gibbl/s for the Wire factory").getInt(pollutionPerSecondMultiIndustrialWireMill);
		pollutionPerSecondMultiIsaMill = config.get("pollution", "pollutionPerSecondMultiIsaMill", pollutionPerSecondMultiIsaMill,"pollution rate in gibbl/s for the IsaMill grinding machine").getInt(pollutionPerSecondMultiIsaMill);
		pollutionPerSecondMultiAdvDistillationTower_ModeDistillery = config.get("pollution", "pollutionPerSecondMultiAdvDistillationTower_ModeDistillery", pollutionPerSecondMultiAdvDistillationTower_ModeDistillery,"pollution rate in gibbl/s for the Dangote distillus in distillery mode").getInt(pollutionPerSecondMultiAdvDistillationTower_ModeDistillery);
		pollutionPerSecondMultiAdvDistillationTower_ModeDT = config.get("pollution", "pollutionPerSecondMultiAdvDistillationTower_ModeDT", pollutionPerSecondMultiAdvDistillationTower_ModeDT,"pollution rate in gibbl/s for the Dangote distillus in distillation tower mode").getInt(pollutionPerSecondMultiAdvDistillationTower_ModeDT);
		pollutionPerSecondMultiAdvEBF = config.get("pollution", "pollutionPerSecondMultiAdvEBF", pollutionPerSecondMultiAdvEBF,"pollution rate in gibbl/s for the Volcanus").getInt(pollutionPerSecondMultiAdvEBF);
		pollutionPerSecondMultiAdvImplosion = config.get("pollution", "pollutionPerSecondMultiAdvImplosion", pollutionPerSecondMultiAdvImplosion,"pollution rate in gibbl/s for the Density^2").getInt(pollutionPerSecondMultiAdvImplosion);
		pollutionPerSecondMultiABS = config.get("pollution", "pollutionPerSecondMultiABS", pollutionPerSecondMultiABS,"pollution rate in gibbl/s for the Alloy blast furnace").getInt(pollutionPerSecondMultiABS);
		pollutionPerSecondMultiCyclotron = config.get("pollution", "pollutionPerSecondMultiCyclotron", pollutionPerSecondMultiCyclotron,"pollution rate in gibbl/s for the Cyclotron").getInt(pollutionPerSecondMultiCyclotron);
		pollutionPerSecondMultiIndustrialFishingPond = config.get("pollution", "pollutionPerSecondMultiIndustrialFishingPond", pollutionPerSecondMultiIndustrialFishingPond,"pollution rate in gibbl/s for the Zuhai - fishing port").getInt(pollutionPerSecondMultiIndustrialFishingPond);
		//pollutionPerSecondMultiLargeRocketEngine;
		pollutionPerSecondMultiLargeSemiFluidGenerator = config.get("pollution", "pollutionPerSecondMultiLargeSemiFluidGenerator", pollutionPerSecondMultiLargeSemiFluidGenerator,"pollution rate in gibbl/s for the Large semifluid burner").getInt(pollutionPerSecondMultiLargeSemiFluidGenerator);
		pollutionPerSecondMultiMassFabricator = config.get("pollution", "pollutionPerSecondMultiMassFabricator", pollutionPerSecondMultiMassFabricator,"pollution rate in gibbl/s for the Matter fabrication CPU").getInt(pollutionPerSecondMultiMassFabricator);
		pollutionPerSecondMultiRefinery = config.get("pollution", "pollutionPerSecondMultiRefinery", pollutionPerSecondMultiRefinery,"pollution rate in gibbl/s for the Reactor fuel processing plant").getInt(pollutionPerSecondMultiRefinery);
		//pollutionPerSecondMultiGeneratorArray;
		pollutionPerSecondMultiTreeFarm = config.get("pollution", "pollutionPerSecondMultiTreeFarm", pollutionPerSecondMultiTreeFarm,"pollution rate in gibbl/s for the Tree growth simulator").getInt(pollutionPerSecondMultiTreeFarm);
		pollutionPerSecondMultiFrothFlotationCell = config.get("pollution", "pollutionPerSecondMultiFrothFlotationCell", pollutionPerSecondMultiFrothFlotationCell,"pollution rate in gibbl/s for the Flotation cell regulator").getInt(pollutionPerSecondMultiFrothFlotationCell);
		pollutionPerSecondMultiAutoCrafter = config.get("pollution", "pollutionPerSecondMultiAutoCrafter", pollutionPerSecondMultiAutoCrafter,"pollution rate in gibbl/s for the Large-Scale auto assembler v1.01").getInt(pollutionPerSecondMultiAutoCrafter);
		pollutionPerSecondMultiThermalBoiler = config.get("pollution", "pollutionPerSecondMultiThermalBoiler", pollutionPerSecondMultiThermalBoiler,"pollution rate in gibbl/s for the Thermal boiler").getInt(pollutionPerSecondMultiThermalBoiler);
		pollutionPerSecondMultiAlgaePond = config.get("pollution", "pollutionPerSecondMultiAlgaePond", pollutionPerSecondMultiAlgaePond,"pollution rate in gibbl/s for the Algae farm").getInt(pollutionPerSecondMultiAlgaePond);
		basePollutionPerSecondSemiFluidGenerator = config.get("pollution", "basePollutionPerSecondSemiFluidGenerator", basePollutionPerSecondSemiFluidGenerator, "base pollution rate in gibbl/s for the single block semi fluid generators").getInt(basePollutionPerSecondSemiFluidGenerator);
		pollutionReleasedByTierSemiFluidGenerator = config.get("pollution", "pollutionReleasedByTierSemiFluidGenerator", pollutionReleasedByTierSemiFluidGenerator, "coefficient applied to the base rate of the single block semi fluid generators based on its tier (first is tier 0 aka ULV)").getDoubleList();
		basePollutionPerSecondBoiler = config.get("pollution", "basePollutionPerSecondBoiler", basePollutionPerSecondBoiler,"base pollution rate in gibbl/s for the single block boilers").getInt(basePollutionPerSecondBoiler);
		pollutionReleasedByTierBoiler = config.get("pollution", "pollutionReleasedByTierBoiler", pollutionReleasedByTierBoiler, "coefficient applied to the base rate of the single block semi fluid generators based on its tier (first is tier 0 aka ULV)").getDoubleList();
		baseMinPollutionPerSecondRocketFuelGenerator = config.get("pollution", "baseMinPollutionPerSecondRocketFuelGenerator", baseMinPollutionPerSecondRocketFuelGenerator, "minimum base pollution rate in gibbl/s for the single block rocket engines").getInt(baseMinPollutionPerSecondRocketFuelGenerator);
		baseMaxPollutionPerSecondRocketFuelGenerator = config.get("pollution", "baseMaxPollutionPerSecondRocketFuelGenerator", baseMaxPollutionPerSecondRocketFuelGenerator, "maximum base pollution rate in gibbl/s for the single block rocket engines").getInt(baseMaxPollutionPerSecondRocketFuelGenerator);
		pollutionReleasedByTierRocketFuelGenerator = config.get("pollution", "pollutionReleasedByTierRocketFuelGenerator", pollutionReleasedByTierRocketFuelGenerator, "coefficient applied to the base rate of the single block rocket engines based on its tier (first is tier 0 aka ULV)").getDoubleList();
		basePollutionPerSecondGeothermalGenerator = config.get("pollution", "basePollutionPerSecondGeothermalGenerator", basePollutionPerSecondGeothermalGenerator,"base pollution rate in gibbl/s for the geothermal engines").getInt(basePollutionPerSecondGeothermalGenerator);
		pollutionReleasedByTierGeothermalGenerator = config.get("pollution", "pollutionReleasedByTierGeothermalGenerator", pollutionReleasedByTierGeothermalGenerator, "coefficient applied to the base rate of the single block geothermal engines based on its tier (first is tier 0 aka ULV)").getDoubleList();
		config.save();
	}

}
