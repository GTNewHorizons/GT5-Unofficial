package gtPlusPlus.core.config;

import static gregtech.api.enums.Mods.GregTech;
import static gtPlusPlus.core.lib.GTPPCore.ConfigSwitches.*;
import static gtPlusPlus.core.lib.GTPPCore.EVERGLADESBIOME_ID;
import static gtPlusPlus.core.lib.GTPPCore.EVERGLADES_ID;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {

    public static void handleConfigFile(final FMLPreInitializationEvent event) {
        final Configuration config = new Configuration(
            new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
        config.load();

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
        pollutionPerSecondMultiAdvEBF = config
            .get(
                "pollution",
                "pollutionPerSecondMultiAdvEBF",
                pollutionPerSecondMultiAdvEBF,
                "pollution rate in gibbl/s for the Volcanus")
            .getInt(pollutionPerSecondMultiAdvEBF);
        pollutionPerSecondMultiAdvImplosion = config
            .get(
                "pollution",
                "pollutionPerSecondMultiAdvImplosion",
                pollutionPerSecondMultiAdvImplosion,
                "pollution rate in gibbl/s for the Density^2")
            .getInt(pollutionPerSecondMultiAdvImplosion);
        pollutionPerSecondMultiABS = config
            .get(
                "pollution",
                "pollutionPerSecondMultiABS",
                pollutionPerSecondMultiABS,
                "pollution rate in gibbl/s for the Alloy blast furnace")
            .getInt(pollutionPerSecondMultiABS);
        pollutionPerSecondMultiCyclotron = config
            .get(
                "pollution",
                "pollutionPerSecondMultiCyclotron",
                pollutionPerSecondMultiCyclotron,
                "pollution rate in gibbl/s for the Cyclotron")
            .getInt(pollutionPerSecondMultiCyclotron);
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
        pollutionPerSecondMultiAlgaePond = config
            .get(
                "pollution",
                "pollutionPerSecondMultiAlgaePond",
                pollutionPerSecondMultiAlgaePond,
                "pollution rate in gibbl/s for the Algae farm")
            .getInt(pollutionPerSecondMultiAlgaePond);
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
        enableAnimatedTextures = config
            .getBoolean("enableAnimatedTextures", "visual", true, "Enables Animated GT++ Textures, Requires Restart");
        config.save();
    }
}
