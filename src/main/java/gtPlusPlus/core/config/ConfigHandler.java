package gtPlusPlus.core.config;

import static gregtech.api.enums.Mods.GregTech;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.MACHINE_INFO;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.baseMaxPollutionPerSecondRocketFuelGenerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.baseMinPollutionPerSecondRocketFuelGenerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.basePollutionPerSecondBoiler;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.basePollutionPerSecondGeothermalGenerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.basePollutionPerSecondSemiFluidGenerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.boilerSteamPerSecond;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.chanceToDropDrainedShard;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.chanceToDropFluoriteOre;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.disableEnderIOIngotTooltips;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.disableEnderIOIntegration;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.disableIC2Recipes;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.dumpItemAndBlockData;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableAlternativeBatteryAlloy;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableAlternativeDivisionSigilRecipe;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableAnimatedTextures;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableCustomCapes;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableCustomCircuits;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableCustom_Cables;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableCustom_Pipes;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_Dehydrators;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_FluidTanks;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_GeothermalEngines;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_Pollution;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_RF_Convetor;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_RocketEngines;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_SimpleWasher;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_SolarGenerators;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_SteamConverter;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMachine_Tesseracts;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiSizeTools;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_AlloyBlastSmelter;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_Cyclotron;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialCentrifuge;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialCokeOven;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialCuttingMachine;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialElectrolyzer;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialExtrudingMachine;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialFishingPort;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialMacerationStack;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialMultiMachine;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialPlatePress;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialSifter;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialThermalCentrifuge;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialWashPlant;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_IndustrialWireMill;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_LargeAutoCrafter;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_LiquidFluorideThoriumReactor;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_MatterFabricator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_MultiTank;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_NuclearFuelRefinery;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_NuclearSaltProcessingPlant;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_PowerSubstation;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableMultiblock_ThermalBoiler;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableOldGTcircuits;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableSulfuricAcidFix;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableThaumcraftShardUnification;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableWatchdogBGM;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.hideUniversalCells;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiABS;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiAdvDistillationTower_ModeDT;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiAdvDistillationTower_ModeDistillery;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiAdvEBF;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiAdvImplosion;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiAlgaePond;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiAutoCrafter;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiCyclotron;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiFrothFlotationCell;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialAlloySmelter;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialArcFurnace;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialCentrifuge;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialChisel;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialCokeOven;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialCuttingMachine;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialDehydrator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialElectrolyzer;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialExtruder;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialFishingPond;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialForgeHammer;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMacerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMixer;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialPlatePress_ModeBending;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialPlatePress_ModeForming;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialRockBreaker;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialSifter;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialThermalCentrifuge;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialVacuumFreezer;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialWashPlant_ModeWasher;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialWireMill;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiIsaMill;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiLargeSemiFluidGenerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiMassFabricator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiMolecularTransformer;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiPackager;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiRefinery;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiThermalBoiler;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionPerSecondMultiTreeFarm;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionReleasedByTierBoiler;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionReleasedByTierGeothermalGenerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionReleasedByTierRocketFuelGenerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.pollutionReleasedByTierSemiFluidGenerator;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.showHiddenNEIItems;
import static gtPlusPlus.core.lib.CORE.EVERGLADESBIOME_ID;
import static gtPlusPlus.core.lib.CORE.EVERGLADES_ID;
import static gtPlusPlus.core.lib.CORE.turbineCutoffBase;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {

    public static void handleConfigFile(final FMLPreInitializationEvent event) {
        final Configuration config = new Configuration(
                new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
        config.load();

        // Debug
        /*
         * DEBUG = config.getBoolean("debugMode", "debug", false,
         * "Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)");
         */
        disableEnderIOIntegration = config
                .getBoolean("disableEnderIO", "debug", false, "Disables EnderIO Integration.");
        disableEnderIOIngotTooltips = config.getBoolean(
                "disableEnderIOIngotTooltips",
                "debug",
                false,
                "Disables EnderIO Ingot Tooltips. These apparently may cause issues for a very small number of users.");
        MACHINE_INFO = config.getBoolean(
                "enableMachineInfoLogging",
                "debug",
                false,
                "Makes many machines display lots of debug logging.");
        showHiddenNEIItems = config
                .getBoolean("showHiddenNEIItems", "debug", false, "Makes all items hidden from NEI display.");
        dumpItemAndBlockData = config.getBoolean(
                "dumpItemAndBlockData",
                "debug",
                false,
                "Dumps all GT++ and Toxic Everglade Data to en_US.lang in the config folder. This config option can be used by foreign players to generate blank .lang files, which they can populate with their language of choice.");

        // Machines
        enableThaumcraftShardUnification = config.getBoolean(
                "enableThaumcraftShardUnification",
                "machines",
                false,
                "Allows the use of TC shards across many recipes by oreDicting them into a common group.");
        enableAlternativeBatteryAlloy = config.getBoolean(
                "enableAlternativeBatteryAlloy",
                "machines",
                false,
                "Adds a non-Antimony using Battery Alloy. Not Balanced at all..");
        disableIC2Recipes = config.getBoolean(
                "disableIC2Recipes",
                "machines",
                false,
                "Alkaluscraft Related - Removes IC2 Cables Except glass fibre. Few other Misc Tweaks.");
        enableAlternativeDivisionSigilRecipe = config
                .getBoolean("enableAlternativeDivisionSigilRecipe", "machines", false, "Utilizes Neutronium instead.");
        boilerSteamPerSecond = config.getInt(
                "boilerSteamPerSecond",
                "machines",
                750,
                0,
                10000,
                "Sets the steam per second value in LV,MV,HV boilers (respectively 1x,2x,3x this number for the tiers)");

        // requireControlCores
        boolean temp = config.getBoolean("requireControlCores", "machines", true, "Multiblocks Require Control Cores");

        // Circuits
        enableCustomCircuits = config.getBoolean(
                "enableCustomCircuits",
                GregTech.ID,
                false,
                "Adds custom circuits to expand past the Master Tier. Only really recommended to enable if enableOldGTcircuits is enabled.");
        enableOldGTcircuits = config.getBoolean(
                "enableOldGTcircuits",
                GregTech.ID,
                false,
                "Restores circuits and their recipes from Pre-5.09.28 times.");

        // Tools
        enableMultiSizeTools = config.getBoolean(
                "enableMultiSizeTools",
                GregTech.ID,
                true,
                "Adds Custom GT Shovels and Pickaxes which mine in a 3x3 style. One of each whill be generated for each Gregtech Material which has Dense Plates and Long Rods available.");

        // GT-Fixes
        enableSulfuricAcidFix = config.getBoolean(
                "enableSulfuricAcidFix",
                GregTech.ID,
                false,
                "Adds GT6 recipes for Sulfuric Acid. Should remove all pre-existing recipes.");
        turbineCutoffBase = config.getInt(
                "turbineCutoffBase",
                GregTech.ID,
                75000,
                0,
                Integer.MAX_VALUE,
                "Rotors below this durability will be removed, prevents NEI clutter. Minimum Durability is N * x, where N is the new value set and x is the turbine size, where 1 is Tiny and 4 is Huge. Set to 0 to disable.");

        // Pipes & Cables
        enableCustom_Pipes = config.getBoolean("enableCustom_Pipes", GregTech.ID, true, "Adds Custom GT Fluid Pipes.");
        enableCustom_Cables = config.getBoolean("enableCustom_Cables", GregTech.ID, true, "Adds Custom GT Cables.");

        // Block Drops
        chanceToDropDrainedShard = config.getInt(
                "chanceToDropDrainedShard",
                "blockdrops",
                196,
                0,
                10000,
                "Drained shards have a 1 in X chance to drop.");
        chanceToDropFluoriteOre = config.getInt(
                "chanceToDropFluoriteOre",
                "blockdrops",
                32,
                0,
                10000,
                "Fluorite Ore has a 1 in X chance to drop from Limestone and a 1 in X*20 from Sandstone..");

        // Single machines
        enableMachine_SolarGenerators = config.getBoolean(
                "enableSolarGenerators",
                GregTech.ID,
                false,
                "These may be overpowered, Consult a local electrician.");

        enableMachine_Dehydrators = config
                .getBoolean("enableMachineDehydrators", GregTech.ID, true, "These dehydrate stuff.");
        enableMachine_SteamConverter = config
                .getBoolean("enableMachineSteamConverter", GregTech.ID, true, "Converts IC2 steam -> Railcraft steam.");
        enableMachine_FluidTanks = config
                .getBoolean("enableMachineFluidTanks", GregTech.ID, true, "Portable fluid tanks.");
        enableMachine_RocketEngines = config.getBoolean(
                "enableMachineRocketEngines",
                GregTech.ID,
                true,
                "Diesel egines with different internals, they consume less fuel overall.");
        enableMachine_GeothermalEngines = config.getBoolean(
                "enableMachineGeothermalEngines",
                GregTech.ID,
                true,
                "These may be overpowered, Consult a local geologist.");
        enableMachine_Tesseracts = config.getBoolean(
                "enableMachineTesseracts",
                GregTech.ID,
                true,
                "Tesseracts for wireless item/fluid movement.");
        enableMachine_SimpleWasher = config.getBoolean(
                "enableMachineSimpleWasher",
                GregTech.ID,
                true,
                "Very basic automated cauldron for dust washing.");
        enableMachine_Pollution = config
                .getBoolean("enableMachinePollution", GregTech.ID, true, "Pollution Detector & Scrubbers.");
        enableMachine_RF_Convetor = config.getBoolean(
                "enableMachineRFConvetor",
                GregTech.ID,
                true,
                "Converts RF to GTEU. Requires COFH-Core to be installed.");

        // Multi machines
        enableMultiblock_AlloyBlastSmelter = config.getBoolean(
                "enableMultiblockAlloyBlastSmelter",
                GregTech.ID,
                true,
                "Required to smelt most high tier materials from GT++. Also smelts everything else to molten metal.");
        enableMultiblock_IndustrialCentrifuge = config
                .getBoolean("enableMultiblockIndustrialCentrifuge", GregTech.ID, true, "Spin, Spin, Spiiiin.");
        enableMultiblock_IndustrialCokeOven = config.getBoolean(
                "enableMultiblockIndustrialCokeOven",
                GregTech.ID,
                true,
                "Pyro Oven Alternative, older, more realistic, better.");
        enableMultiblock_IndustrialElectrolyzer = config.getBoolean(
                "enableMultiblockIndustrialElectrolyzer",
                GregTech.ID,
                true,
                "Electrolyzes things with extra bling factor.");
        enableMultiblock_IndustrialMacerationStack = config.getBoolean(
                "enableMultiblockIndustrialMacerationStack",
                GregTech.ID,
                true,
                "A hyper efficient maceration tower, nets more bonus outputs.");
        enableMultiblock_IndustrialPlatePress = config.getBoolean(
                "enableMultiblockIndustrialPlatePress",
                GregTech.ID,
                true,
                "Industrial bendering machine thingo.");
        enableMultiblock_IndustrialWireMill = config.getBoolean(
                "enableMultiblockIndustrialWireMill",
                GregTech.ID,
                true,
                "Produces fine wire and exotic cables.");
        enableMultiblock_MatterFabricator = config
                .getBoolean("enableMultiblockMatterFabricator", GregTech.ID, true, "?FAB?RIC?ATE MA?TT?ER.");
        enableMultiblock_MultiTank = config.getBoolean(
                "enableMultiblockMultiTank",
                GregTech.ID,
                true,
                "Tall tanks, each layer adds extra fluid storage.");
        enableMultiblock_PowerSubstation = config
                .getBoolean("enableMultiblockPowerSubstation", GregTech.ID, true, "For managing large power grids.");
        enableMultiblock_LiquidFluorideThoriumReactor = config.getBoolean(
                "enableMultiblockLiquidFluorideThoriumReactor",
                GregTech.ID,
                true,
                "For supplying large power grids.");
        enableMultiblock_NuclearFuelRefinery = config.getBoolean(
                "enableMultiblock_NuclearFuelRefinery",
                GregTech.ID,
                true,
                "Refines molten chemicals into nuclear fuels.");
        enableMultiblock_NuclearSaltProcessingPlant = config.getBoolean(
                "enableMultiblockNuclearSaltProcessingPlant",
                GregTech.ID,
                true,
                "Reprocesses depleted nuclear salts into useful chemicals.");
        enableMultiblock_IndustrialSifter = config
                .getBoolean("enableMultiblock_IndustrialSifter", GregTech.ID, true, "Large scale sifting.");
        enableMultiblock_LargeAutoCrafter = config.getBoolean(
                "enableMultiblock_LargeAutoCrafter",
                GregTech.ID,
                true,
                "Can Assemble, Disassemble and Craft Project data from Data Sticks.");
        enableMultiblock_IndustrialThermalCentrifuge = config.getBoolean(
                "enableMultiblock_IndustrialThermalCentrifuge",
                GregTech.ID,
                true,
                "Your warm spin for the ore thing.");
        enableMultiblock_IndustrialWashPlant = config.getBoolean(
                "enableMultiblock_IndustrialWashPlant",
                GregTech.ID,
                true,
                "Used to wash the dirt, riiiiight offff..");
        enableMultiblock_ThermalBoiler = config.getBoolean(
                "enableMachineThermalBoiler",
                GregTech.ID,
                true,
                "Thermal Boiler from GT4. Can Filter Lava for resources.");
        enableMultiblock_IndustrialCuttingMachine = config.getBoolean(
                "enableMultiblock_IndustrialCuttingMachine",
                GregTech.ID,
                true,
                "Very fast and efficient Cutting Machine.");
        enableMultiblock_IndustrialFishingPort = config.getBoolean(
                "enableMultiblock_IndustrialFishingPort",
                GregTech.ID,
                true,
                "Fish the seas, except on land.");
        enableMultiblock_IndustrialExtrudingMachine = config.getBoolean(
                "enableMultiblock_IndustrialExtrudingMachine",
                GregTech.ID,
                true,
                "Very fast and efficient Extruding Machine.");
        enableMultiblock_IndustrialMultiMachine = config.getBoolean(
                "enableMultiblock_IndustrialMultiMachine",
                GregTech.ID,
                true,
                "Can run recipes for 9 different types of machines.");
        enableMultiblock_Cyclotron = config
                .getBoolean("enableMultiblock_Cyclotron", GregTech.ID, true, "COMET - Scientific Cyclotron.");

        // Features
        enableCustomCapes = config.getBoolean("enableSupporterCape", "features", true, "Enables Custom GT++ Cape.");

        enableWatchdogBGM = config.getInt(
                "enableWatchdogBGM",
                "features",
                0,
                0,
                Short.MAX_VALUE,
                "Set to a value greater than 0 to reduce the ticks taken to delay between BGM tracks. Acceptable Values are 1-32767, where 0 is disabled. Vanilla Uses 12,000 & 24,000. 200 is 10s.");
        hideUniversalCells = config
                .getBoolean("hideUniversalCells", "features", true, "Hides every filled IC2 Universal Cell from NEI.");

        // Biomes
        EVERGLADES_ID = config.getInt("darkworld_ID", "worldgen", 227, 1, 254, "The ID of the Dark Dimension.");
        EVERGLADESBIOME_ID = config
                .getInt("darkbiome_ID", "worldgen", 238, 1, 254, "The biome within the Dark Dimension.");

        // Pollution
        pollutionPerSecondMultiPackager = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiPackager",
                        pollutionPerSecondMultiPackager,
                        "pollution rate in gibbl/s for the Amazon warehousing depot")
                .getInt(pollutionPerSecondMultiPackager);
        pollutionPerSecondMultiIndustrialAlloySmelter = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialAlloySmelter",
                        pollutionPerSecondMultiIndustrialAlloySmelter,
                        "pollution rate in gibbl/s for the Alloy blast smelter")
                .getInt(pollutionPerSecondMultiIndustrialAlloySmelter);
        pollutionPerSecondMultiIndustrialArcFurnace = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialArcFurnace",
                        pollutionPerSecondMultiIndustrialArcFurnace,
                        "pollution rate in gibbl/s for the High current arc furnace")
                .getInt(pollutionPerSecondMultiIndustrialArcFurnace);
        pollutionPerSecondMultiIndustrialCentrifuge = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialCentrifuge",
                        pollutionPerSecondMultiIndustrialCentrifuge,
                        "pollution rate in gibbl/s for the Industrial centrifuge")
                .getInt(pollutionPerSecondMultiIndustrialCentrifuge);
        pollutionPerSecondMultiIndustrialCokeOven = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialCokeOven",
                        pollutionPerSecondMultiIndustrialCokeOven,
                        "pollution rate in gibbl/s for the Industrial coke oven")
                .getInt(pollutionPerSecondMultiIndustrialCokeOven);
        pollutionPerSecondMultiIndustrialCuttingMachine = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialCuttingMachine",
                        pollutionPerSecondMultiIndustrialCuttingMachine,
                        "pollution rate in gibbl/s for the Cutting factory")
                .getInt(pollutionPerSecondMultiIndustrialCuttingMachine);
        pollutionPerSecondMultiIndustrialDehydrator = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialDehydrator",
                        pollutionPerSecondMultiIndustrialDehydrator,
                        "pollution rate in gibbl/s for the Utupu-Tanuri")
                .getInt(pollutionPerSecondMultiIndustrialDehydrator);
        pollutionPerSecondMultiIndustrialElectrolyzer = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialElectrolyzer",
                        pollutionPerSecondMultiIndustrialElectrolyzer,
                        "pollution rate in gibbl/s for the Industrial electrolyzer")
                .getInt(pollutionPerSecondMultiIndustrialElectrolyzer);
        pollutionPerSecondMultiIndustrialExtruder = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialExtruder",
                        pollutionPerSecondMultiIndustrialExtruder,
                        "pollution rate in gibbl/s for the Industrial extrusion machine")
                .getInt(pollutionPerSecondMultiIndustrialExtruder);
        pollutionPerSecondMultiIndustrialMacerator = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialMacerator",
                        pollutionPerSecondMultiIndustrialMacerator,
                        "pollution rate in gibbl/s for the Maceration stack")
                .getInt(pollutionPerSecondMultiIndustrialMacerator);
        pollutionPerSecondMultiIndustrialMixer = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialMixer",
                        pollutionPerSecondMultiIndustrialMixer,
                        "pollution rate in gibbl/s for the Industrial mixing machine")
                .getInt(pollutionPerSecondMultiIndustrialMixer);
        pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal",
                        pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal,
                        "pollution rate in gibbl/s for the Large processing factory in metal mode")
                .getInt(pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal);
        pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid",
                        pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid,
                        "pollution rate in gibbl/s for the Large processing factory in fluid mode")
                .getInt(pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid);
        pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc",
                        pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc,
                        "pollution rate in gibbl/s for the Large processing factory in misc mode")
                .getInt(pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc);
        pollutionPerSecondMultiIndustrialPlatePress_ModeForming = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialPlatePress_ModeForming",
                        pollutionPerSecondMultiIndustrialPlatePress_ModeForming,
                        "pollution rate in gibbl/s for the Industrial material press in forming mode")
                .getInt(pollutionPerSecondMultiIndustrialPlatePress_ModeForming);
        pollutionPerSecondMultiIndustrialPlatePress_ModeBending = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialPlatePress_ModeBending",
                        pollutionPerSecondMultiIndustrialPlatePress_ModeBending,
                        "pollution rate in gibbl/s for the Industrial material press in bending mode")
                .getInt(pollutionPerSecondMultiIndustrialPlatePress_ModeBending);
        pollutionPerSecondMultiIndustrialForgeHammer = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialForgeHammer",
                        pollutionPerSecondMultiIndustrialForgeHammer,
                        "pollution rate in gibbl/s for the Industrial Forge Hammer")
                .getInt(pollutionPerSecondMultiIndustrialForgeHammer);
        pollutionPerSecondMultiIndustrialSifter = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialSifter",
                        pollutionPerSecondMultiIndustrialSifter,
                        "pollution rate in gibbl/s for the Large Sifter")
                .getInt(pollutionPerSecondMultiIndustrialSifter);
        pollutionPerSecondMultiIndustrialThermalCentrifuge = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialThermalCentrifuge",
                        pollutionPerSecondMultiIndustrialThermalCentrifuge,
                        "pollution rate in gibbl/s for the Large thermal refinery")
                .getInt(pollutionPerSecondMultiIndustrialThermalCentrifuge);
        pollutionPerSecondMultiIndustrialVacuumFreezer = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialVacuumFreezer",
                        pollutionPerSecondMultiIndustrialVacuumFreezer,
                        "pollution rate in gibbl/s for the Cryogenic freezer")
                .getInt(pollutionPerSecondMultiIndustrialVacuumFreezer);
        pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath",
                        pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath,
                        "pollution rate in gibbl/s for the Ore washing plant in chemical bath mode")
                .getInt(pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath);
        pollutionPerSecondMultiIndustrialWashPlant_ModeWasher = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialWashPlant_ModeWasher",
                        pollutionPerSecondMultiIndustrialWashPlant_ModeWasher,
                        "pollution rate in gibbl/s for the Ore washing plant in ore washer mode")
                .getInt(pollutionPerSecondMultiIndustrialWashPlant_ModeWasher);
        pollutionPerSecondMultiIndustrialWireMill = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialWireMill",
                        pollutionPerSecondMultiIndustrialWireMill,
                        "pollution rate in gibbl/s for the Wire factory")
                .getInt(pollutionPerSecondMultiIndustrialWireMill);
        pollutionPerSecondMultiIsaMill = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIsaMill",
                        pollutionPerSecondMultiIsaMill,
                        "pollution rate in gibbl/s for the IsaMill grinding machine")
                .getInt(pollutionPerSecondMultiIsaMill);
        pollutionPerSecondMultiAdvDistillationTower_ModeDistillery = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiAdvDistillationTower_ModeDistillery",
                        pollutionPerSecondMultiAdvDistillationTower_ModeDistillery,
                        "pollution rate in gibbl/s for the Dangote distillus in distillery mode")
                .getInt(pollutionPerSecondMultiAdvDistillationTower_ModeDistillery);
        pollutionPerSecondMultiAdvDistillationTower_ModeDT = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiAdvDistillationTower_ModeDT",
                        pollutionPerSecondMultiAdvDistillationTower_ModeDT,
                        "pollution rate in gibbl/s for the Dangote distillus in distillation tower mode")
                .getInt(pollutionPerSecondMultiAdvDistillationTower_ModeDT);
        pollutionPerSecondMultiAdvEBF = config.get(
                "pollution",
                "pollutionPerSecondMultiAdvEBF",
                pollutionPerSecondMultiAdvEBF,
                "pollution rate in gibbl/s for the Volcanus").getInt(pollutionPerSecondMultiAdvEBF);
        pollutionPerSecondMultiAdvImplosion = config.get(
                "pollution",
                "pollutionPerSecondMultiAdvImplosion",
                pollutionPerSecondMultiAdvImplosion,
                "pollution rate in gibbl/s for the Density^2").getInt(pollutionPerSecondMultiAdvImplosion);
        pollutionPerSecondMultiABS = config.get(
                "pollution",
                "pollutionPerSecondMultiABS",
                pollutionPerSecondMultiABS,
                "pollution rate in gibbl/s for the Alloy blast furnace").getInt(pollutionPerSecondMultiABS);
        pollutionPerSecondMultiCyclotron = config.get(
                "pollution",
                "pollutionPerSecondMultiCyclotron",
                pollutionPerSecondMultiCyclotron,
                "pollution rate in gibbl/s for the Cyclotron").getInt(pollutionPerSecondMultiCyclotron);
        pollutionPerSecondMultiIndustrialFishingPond = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialFishingPond",
                        pollutionPerSecondMultiIndustrialFishingPond,
                        "pollution rate in gibbl/s for the Zuhai - fishing port")
                .getInt(pollutionPerSecondMultiIndustrialFishingPond);
        // pollutionPerSecondMultiLargeRocketEngine;
        pollutionPerSecondMultiLargeSemiFluidGenerator = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiLargeSemiFluidGenerator",
                        pollutionPerSecondMultiLargeSemiFluidGenerator,
                        "pollution rate in gibbl/s for the Large semifluid burner")
                .getInt(pollutionPerSecondMultiLargeSemiFluidGenerator);
        pollutionPerSecondMultiMassFabricator = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiMassFabricator",
                        pollutionPerSecondMultiMassFabricator,
                        "pollution rate in gibbl/s for the Matter fabrication CPU")
                .getInt(pollutionPerSecondMultiMassFabricator);
        pollutionPerSecondMultiRefinery = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiRefinery",
                        pollutionPerSecondMultiRefinery,
                        "pollution rate in gibbl/s for the Reactor fuel processing plant")
                .getInt(pollutionPerSecondMultiRefinery);
        // pollutionPerSecondMultiGeneratorArray;
        pollutionPerSecondMultiIndustrialRockBreaker = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialRockBreaker",
                        pollutionPerSecondMultiIndustrialRockBreaker,
                        "pollution rate in gibbl/s for the Industrial Rock Breaker")
                .getInt(pollutionPerSecondMultiIndustrialRockBreaker);
        pollutionPerSecondMultiIndustrialChisel = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiIndustrialChisel",
                        pollutionPerSecondMultiIndustrialChisel,
                        "pollution rate in gibbl/s for the Industrial Chisel")
                .getInt(pollutionPerSecondMultiIndustrialChisel);
        pollutionPerSecondMultiTreeFarm = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiTreeFarm",
                        pollutionPerSecondMultiTreeFarm,
                        "pollution rate in gibbl/s for the Tree growth simulator")
                .getInt(pollutionPerSecondMultiTreeFarm);
        pollutionPerSecondMultiFrothFlotationCell = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiFrothFlotationCell",
                        pollutionPerSecondMultiFrothFlotationCell,
                        "pollution rate in gibbl/s for the Flotation cell regulator")
                .getInt(pollutionPerSecondMultiFrothFlotationCell);
        pollutionPerSecondMultiAutoCrafter = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiAutoCrafter",
                        pollutionPerSecondMultiAutoCrafter,
                        "pollution rate in gibbl/s for the Large-Scale auto assembler v1.01")
                .getInt(pollutionPerSecondMultiAutoCrafter);
        pollutionPerSecondMultiMolecularTransformer = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiMolecularTransformer",
                        pollutionPerSecondMultiMolecularTransformer,
                        "pollution rate in gibbl/s for the Multiblock Molecular Transformer")
                .getInt(pollutionPerSecondMultiMolecularTransformer);
        pollutionPerSecondMultiThermalBoiler = config
                .get(
                        "pollution",
                        "pollutionPerSecondMultiThermalBoiler",
                        pollutionPerSecondMultiThermalBoiler,
                        "pollution rate in gibbl/s for the Thermal boiler")
                .getInt(pollutionPerSecondMultiThermalBoiler);
        pollutionPerSecondMultiAlgaePond = config.get(
                "pollution",
                "pollutionPerSecondMultiAlgaePond",
                pollutionPerSecondMultiAlgaePond,
                "pollution rate in gibbl/s for the Algae farm").getInt(pollutionPerSecondMultiAlgaePond);
        basePollutionPerSecondSemiFluidGenerator = config
                .get(
                        "pollution",
                        "basePollutionPerSecondSemiFluidGenerator",
                        basePollutionPerSecondSemiFluidGenerator,
                        "base pollution rate in gibbl/s for the single block semi fluid generators")
                .getInt(basePollutionPerSecondSemiFluidGenerator);
        pollutionReleasedByTierSemiFluidGenerator = config.get(
                "pollution",
                "pollutionReleasedByTierSemiFluidGenerator",
                pollutionReleasedByTierSemiFluidGenerator,
                "coefficient applied to the base rate of the single block semi fluid generators based on its tier (first is tier 0 aka ULV)")
                .getDoubleList();
        basePollutionPerSecondBoiler = config
                .get(
                        "pollution",
                        "basePollutionPerSecondBoiler",
                        basePollutionPerSecondBoiler,
                        "base pollution rate in gibbl/s for the single block boilers")
                .getInt(basePollutionPerSecondBoiler);
        pollutionReleasedByTierBoiler = config.get(
                "pollution",
                "pollutionReleasedByTierBoiler",
                pollutionReleasedByTierBoiler,
                "coefficient applied to the base rate of the single block semi fluid generators based on its tier (first is tier 0 aka ULV)")
                .getDoubleList();
        baseMinPollutionPerSecondRocketFuelGenerator = config
                .get(
                        "pollution",
                        "baseMinPollutionPerSecondRocketFuelGenerator",
                        baseMinPollutionPerSecondRocketFuelGenerator,
                        "minimum base pollution rate in gibbl/s for the single block rocket engines")
                .getInt(baseMinPollutionPerSecondRocketFuelGenerator);
        baseMaxPollutionPerSecondRocketFuelGenerator = config
                .get(
                        "pollution",
                        "baseMaxPollutionPerSecondRocketFuelGenerator",
                        baseMaxPollutionPerSecondRocketFuelGenerator,
                        "maximum base pollution rate in gibbl/s for the single block rocket engines")
                .getInt(baseMaxPollutionPerSecondRocketFuelGenerator);
        pollutionReleasedByTierRocketFuelGenerator = config.get(
                "pollution",
                "pollutionReleasedByTierRocketFuelGenerator",
                pollutionReleasedByTierRocketFuelGenerator,
                "coefficient applied to the base rate of the single block rocket engines based on its tier (first is tier 0 aka ULV)")
                .getDoubleList();
        basePollutionPerSecondGeothermalGenerator = config
                .get(
                        "pollution",
                        "basePollutionPerSecondGeothermalGenerator",
                        basePollutionPerSecondGeothermalGenerator,
                        "base pollution rate in gibbl/s for the geothermal engines")
                .getInt(basePollutionPerSecondGeothermalGenerator);
        pollutionReleasedByTierGeothermalGenerator = config.get(
                "pollution",
                "pollutionReleasedByTierGeothermalGenerator",
                pollutionReleasedByTierGeothermalGenerator,
                "coefficient applied to the base rate of the single block geothermal engines based on its tier (first is tier 0 aka ULV)")
                .getDoubleList();

        // Visual
        enableAnimatedTextures = config.getBoolean(
                "enableAnimatedTextures",
                "visual",
                true,
                "Enables Animated GT++ Textures, Requires Restart");
        config.save();
    }
}
