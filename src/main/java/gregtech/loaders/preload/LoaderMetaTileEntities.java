package gregtech.loaders.preload;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.chain;
import static gregtech.api.enums.MetaTileEntityIDs.*;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.amplifierRecipes;
import static gregtech.api.recipe.RecipeMaps.arcFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fermentingRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.furnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.microwaveRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.plasmaArcFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.polarizerRecipes;
import static gregtech.api.recipe.RecipeMaps.recyclerRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.MTEBasicBatteryBuffer;
import gregtech.api.metatileentity.implementations.MTEBasicHull;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.metatileentity.implementations.MTEHatchBulkCatalystHousing;
import gregtech.api.metatileentity.implementations.MTEHatchCokeOven;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchEnergyDebug;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchInputBusDebug;
import gregtech.api.metatileentity.implementations.MTEHatchInputDebug;
import gregtech.api.metatileentity.implementations.MTEHatchMagnet;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.metatileentity.implementations.MTEHatchNanite;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.metatileentity.implementations.MTEHatchQuadrupleHumongous;
import gregtech.api.metatileentity.implementations.MTEHatchVoid;
import gregtech.api.metatileentity.implementations.MTEHatchVoidBus;
import gregtech.api.metatileentity.implementations.MTETransformer;
import gregtech.api.metatileentity.implementations.MTEWetTransformer;
import gregtech.api.metatileentity.implementations.MTEWirelessEnergy;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTLog;
import gregtech.common.tileentities.automation.MTEChestBuffer;
import gregtech.common.tileentities.automation.MTEFilter;
import gregtech.common.tileentities.automation.MTEItemDistributor;
import gregtech.common.tileentities.automation.MTERecipeFilter;
import gregtech.common.tileentities.automation.MTERegulator;
import gregtech.common.tileentities.automation.MTESuperBuffer;
import gregtech.common.tileentities.automation.MTETypeFilter;
import gregtech.common.tileentities.boilers.MTEBoilerBronze;
import gregtech.common.tileentities.boilers.MTEBoilerLava;
import gregtech.common.tileentities.boilers.MTEBoilerSolar;
import gregtech.common.tileentities.boilers.MTEBoilerSolarSteel;
import gregtech.common.tileentities.boilers.MTEBoilerSteel;
import gregtech.common.tileentities.debug.MTEAdvDebugStructureWriter;
import gregtech.common.tileentities.generators.MTEDieselGenerator;
import gregtech.common.tileentities.generators.MTEGasTurbine;
import gregtech.common.tileentities.generators.MTELightningRod;
import gregtech.common.tileentities.generators.MTEMagicEnergyConverter;
import gregtech.common.tileentities.generators.MTEMagicalEnergyAbsorber;
import gregtech.common.tileentities.generators.MTENaquadahReactor;
import gregtech.common.tileentities.generators.MTEPlasmaGenerator;
import gregtech.common.tileentities.generators.MTESolarGenerator;
import gregtech.common.tileentities.generators.MTESteamTurbine;
import gregtech.common.tileentities.machines.MTEBasicHullBronze;
import gregtech.common.tileentities.machines.MTEBasicHullBronzeBricks;
import gregtech.common.tileentities.machines.MTEBasicHullSteel;
import gregtech.common.tileentities.machines.MTEBasicHullSteelBricks;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gregtech.common.tileentities.machines.MTEHatchOutputBusME;
import gregtech.common.tileentities.machines.MTEHatchOutputME;
import gregtech.common.tileentities.machines.basic.MTEAdvSeismicProspector;
import gregtech.common.tileentities.machines.basic.MTEBetterJukebox;
import gregtech.common.tileentities.machines.basic.MTEBoxinator;
import gregtech.common.tileentities.machines.basic.MTECharger;
import gregtech.common.tileentities.machines.basic.MTEIndustrialApiary;
import gregtech.common.tileentities.machines.basic.MTEMagLevPylon;
import gregtech.common.tileentities.machines.basic.MTEMassfabricator;
import gregtech.common.tileentities.machines.basic.MTEMicrowaveEnergyTransmitter;
import gregtech.common.tileentities.machines.basic.MTEMiner;
import gregtech.common.tileentities.machines.basic.MTEMonsterRepellent;
import gregtech.common.tileentities.machines.basic.MTENameRemover;
import gregtech.common.tileentities.machines.basic.MTEPotionBrewer;
import gregtech.common.tileentities.machines.basic.MTEPump;
import gregtech.common.tileentities.machines.basic.MTEReplicator;
import gregtech.common.tileentities.machines.basic.MTERockBreaker;
import gregtech.common.tileentities.machines.basic.MTEScanner;
import gregtech.common.tileentities.machines.basic.MTETeleporter;
import gregtech.common.tileentities.machines.basic.MTETurboCharger;
import gregtech.common.tileentities.machines.basic.MTEWorldAccelerator;
import gregtech.common.tileentities.machines.long_distance.MTELongDistancePipelineFluid;
import gregtech.common.tileentities.machines.long_distance.MTELongDistancePipelineItem;
import gregtech.common.tileentities.machines.multi.MTEAirFilter1;
import gregtech.common.tileentities.machines.multi.MTEAirFilter2;
import gregtech.common.tileentities.machines.multi.MTEAirFilter3;
import gregtech.common.tileentities.machines.multi.MTEAssemblyLine;
import gregtech.common.tileentities.machines.multi.MTEBrickedBlastFurnace;
import gregtech.common.tileentities.machines.multi.MTECharcoalPit;
import gregtech.common.tileentities.machines.multi.MTECleanroom;
import gregtech.common.tileentities.machines.multi.MTECokeOven;
import gregtech.common.tileentities.machines.multi.MTEConcreteBackfiller1;
import gregtech.common.tileentities.machines.multi.MTEConcreteBackfiller2;
import gregtech.common.tileentities.machines.multi.MTEDecayWarehouse;
import gregtech.common.tileentities.machines.multi.MTEDieselEngine;
import gregtech.common.tileentities.machines.multi.MTEDistillationTower;
import gregtech.common.tileentities.machines.multi.MTEElectricBlastFurnace;
import gregtech.common.tileentities.machines.multi.MTEEntropicProcessor;
import gregtech.common.tileentities.machines.multi.MTEExtremeDieselEngine;
import gregtech.common.tileentities.machines.multi.MTEFluidShaper;
import gregtech.common.tileentities.machines.multi.MTEFusionComputer1;
import gregtech.common.tileentities.machines.multi.MTEFusionComputer2;
import gregtech.common.tileentities.machines.multi.MTEFusionComputer3;
import gregtech.common.tileentities.machines.multi.MTEHeatExchanger;
import gregtech.common.tileentities.machines.multi.MTEImplosionCompressor;
import gregtech.common.tileentities.machines.multi.MTEIndustrialBrewery;
import gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator;
import gregtech.common.tileentities.machines.multi.MTEIndustrialExtractor;
import gregtech.common.tileentities.machines.multi.MTEIndustrialLaserEngraver;
import gregtech.common.tileentities.machines.multi.MTEIntegratedOreFactory;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerBronze;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerSteel;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerTitanium;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerTungstenSteel;
import gregtech.common.tileentities.machines.multi.MTELargeChemicalReactor;
import gregtech.common.tileentities.machines.multi.MTELargeFluidExtractor;
import gregtech.common.tileentities.machines.multi.MTELargeMolecularAssembler;
import gregtech.common.tileentities.machines.multi.MTELargeTurbineGas;
import gregtech.common.tileentities.machines.multi.MTELargeTurbineHPSteam;
import gregtech.common.tileentities.machines.multi.MTELargeTurbinePlasma;
import gregtech.common.tileentities.machines.multi.MTELargeTurbineSteam;
import gregtech.common.tileentities.machines.multi.MTELatex;
import gregtech.common.tileentities.machines.multi.MTEMassSolidifier;
import gregtech.common.tileentities.machines.multi.MTEMultiAutoclave;
import gregtech.common.tileentities.machines.multi.MTEMultiCanner;
import gregtech.common.tileentities.machines.multi.MTEMultiFurnace;
import gregtech.common.tileentities.machines.multi.MTEMultiLathe;
import gregtech.common.tileentities.machines.multi.MTENanoForge;
import gregtech.common.tileentities.machines.multi.MTEOilCracker;
import gregtech.common.tileentities.machines.multi.MTEOilDrill1;
import gregtech.common.tileentities.machines.multi.MTEOilDrill2;
import gregtech.common.tileentities.machines.multi.MTEOilDrill3;
import gregtech.common.tileentities.machines.multi.MTEOilDrill4;
import gregtech.common.tileentities.machines.multi.MTEOilDrillInfinite;
import gregtech.common.tileentities.machines.multi.MTEOreDrillingPlant1;
import gregtech.common.tileentities.machines.multi.MTEOreDrillingPlant2;
import gregtech.common.tileentities.machines.multi.MTEOreDrillingPlant3;
import gregtech.common.tileentities.machines.multi.MTEOreDrillingPlant4;
import gregtech.common.tileentities.machines.multi.MTEPlasmaForge;
import gregtech.common.tileentities.machines.multi.MTEPyrolyseOven;
import gregtech.common.tileentities.machines.multi.MTEResearchCompleter;
import gregtech.common.tileentities.machines.multi.MTESolarFactory;
import gregtech.common.tileentities.machines.multi.MTESpinmatron;
import gregtech.common.tileentities.machines.multi.MTETranscendentPlasmaMixer;
import gregtech.common.tileentities.machines.multi.MTEVacuumFreezer;
import gregtech.common.tileentities.machines.multi.MTEWormholeGenerator;
import gregtech.common.tileentities.machines.multi.compressor.MTEBlackHoleCompressor;
import gregtech.common.tileentities.machines.multi.compressor.MTEBlackHoleUtility;
import gregtech.common.tileentities.machines.multi.compressor.MTEHIPCompressor;
import gregtech.common.tileentities.machines.multi.compressor.MTEHeatSensor;
import gregtech.common.tileentities.machines.multi.compressor.MTEIndustrialCompressor;
import gregtech.common.tileentities.machines.multi.compressor.MTENeutroniumCompressor;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;
import gregtech.common.tileentities.machines.multi.drone.MTEHatchDroneDownLink;
import gregtech.common.tileentities.machines.multi.foundry.MTEExoFoundry;
import gregtech.common.tileentities.machines.multi.pcb.MTEPCBBioChamber;
import gregtech.common.tileentities.machines.multi.pcb.MTEPCBCoolingTower;
import gregtech.common.tileentities.machines.multi.pcb.MTEPCBFactory;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gregtech.common.tileentities.machines.multi.nanochip.MTEVacuumConveyorPipe;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.modules.*;
import gregtech.common.tileentities.machines.multi.purification.MTEHatchDegasifierControl;
import gregtech.common.tileentities.machines.multi.purification.MTEHatchLensHousing;
import gregtech.common.tileentities.machines.multi.purification.MTEHatchLensIndicator;
import gregtech.common.tileentities.machines.multi.purification.MTEHatchPHSensor;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBaryonicPerfection;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitClarifier;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitDegasser;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitFlocculation;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitOzonation;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitPhAdjustment;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitPlasmaHeater;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitUVTreatment;
import gregtech.common.tileentities.machines.steam.MTESteamAlloySmelterBronze;
import gregtech.common.tileentities.machines.steam.MTESteamAlloySmelterSteel;
import gregtech.common.tileentities.machines.steam.MTESteamCompressorBronze;
import gregtech.common.tileentities.machines.steam.MTESteamCompressorSteel;
import gregtech.common.tileentities.machines.steam.MTESteamExtractorBronze;
import gregtech.common.tileentities.machines.steam.MTESteamExtractorSteel;
import gregtech.common.tileentities.machines.steam.MTESteamForgeHammerBronze;
import gregtech.common.tileentities.machines.steam.MTESteamForgeHammerSteel;
import gregtech.common.tileentities.machines.steam.MTESteamFurnaceBronze;
import gregtech.common.tileentities.machines.steam.MTESteamFurnaceSteel;
import gregtech.common.tileentities.machines.steam.MTESteamMaceratorBronze;
import gregtech.common.tileentities.machines.steam.MTESteamMaceratorSteel;
import gregtech.common.tileentities.storage.MTEDebugTank;
import gregtech.common.tileentities.storage.MTELocker;
import gregtech.common.tileentities.storage.MTEQuantumChest;
import gregtech.common.tileentities.storage.MTEQuantumTank;
import gregtech.common.tileentities.storage.MTESuperChest;
import gregtech.common.tileentities.storage.MTESuperTank;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTETransformerHiAmp;

// TODO Some GT MetaTileEntity registrations are done in load/GT_Loader_MetaTileEntities_Recipes.java due to joint
// registration+recipe methods, they should be split and brought here to register all in preload.

public class LoaderMetaTileEntities implements Runnable { // TODO CHECK CIRCUIT RECIPES AND USAGES

    public static final String imagination = EnumChatFormatting.RESET + "You just need "
        + EnumChatFormatting.DARK_PURPLE
        + "I"
        + EnumChatFormatting.LIGHT_PURPLE
        + "m"
        + EnumChatFormatting.DARK_RED
        + "a"
        + EnumChatFormatting.RED
        + "g"
        + EnumChatFormatting.YELLOW
        + "i"
        + EnumChatFormatting.GREEN
        + "n"
        + EnumChatFormatting.AQUA
        + "a"
        + EnumChatFormatting.DARK_AQUA
        + "t"
        + EnumChatFormatting.BLUE
        + "i"
        + EnumChatFormatting.DARK_BLUE
        + "o"
        + EnumChatFormatting.DARK_PURPLE
        + "n"
        + EnumChatFormatting.RESET
        + " to use this.";

    private static void registerMultiblockControllers() {
        ItemList.CokeOvenController
            .set(new MTECokeOven(COKE_OVEN_CONTROLLER.ID, "multimachine.cokeoven", "Coke Oven").getStackForm(1L));

        ItemList.Machine_Bricked_BlastFurnace.set(
            new MTEBrickedBlastFurnace(
                BRICKED_BLAST_FURNACE_CONTROLLER.ID,
                "multimachine.brickedblastfurnace",
                "Bricked Blast Furnace").getStackForm(1L));

        ItemList.Machine_Multi_BlastFurnace.set(
            new MTEElectricBlastFurnace(EBF_CONTROLLER.ID, "multimachine.blastfurnace", "Electric Blast Furnace")
                .getStackForm(1L));
        ItemList.Machine_Multi_ImplosionCompressor.set(
            new MTEImplosionCompressor(
                IMPLOSION_COMPRESSOR_CONTROLLER.ID,
                "multimachine.implosioncompressor",
                "Implosion Compressor").getStackForm(1L));
        ItemList.Machine_Multi_VacuumFreezer.set(
            new MTEVacuumFreezer(VACUUM_FREEZER_CONTROLLER.ID, "multimachine.vacuumfreezer", "Vacuum Freezer")
                .getStackForm(1L));
        ItemList.Machine_Multi_Furnace.set(
            new MTEMultiFurnace(MULTI_SMELTER_CONTROLLER.ID, "multimachine.multifurnace", "Multi Smelter")
                .getStackForm(1L));
        ItemList.Machine_Multi_PlasmaForge.set(
            new MTEPlasmaForge(
                DTPF_CONTROLLER.ID,
                "multimachine.plasmaforge",
                "Dimensionally Transcendent Plasma Forge").getStackForm(1L));
        ItemList.Machine_Multi_PurificationPlant.set(
            new MTEPurificationPlant(
                PURIFICATION_PLANT_CONTROLLER.ID,
                "multimachine.purificationplant",
                "Water Purification Plant").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitClarifier.set(
            new MTEPurificationUnitClarifier(
                PURIFICATION_UNIT_CLARIFIER.ID,
                "multimachine.purificationunitclarifier",
                "Clarifier Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitFlocculator.set(
            new MTEPurificationUnitFlocculation(
                PURIFICATION_UNIT_FLOCCULATOR.ID,
                "multimachine.purificationunitflocculator",
                "Flocculation Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitPhAdjustment.set(
            new MTEPurificationUnitPhAdjustment(
                PURIFICATION_UNIT_PH_ADJUSTMENT.ID,
                "multimachine.purificationunitphadjustment",
                "pH Neutralization Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitOzonation.set(
            new MTEPurificationUnitOzonation(
                PURIFICATION_UNIT_OZONATION.ID,
                "multimachine.purificationunitozonation",
                "Ozonation Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitPlasmaHeater.set(
            new MTEPurificationUnitPlasmaHeater(
                PURIFICATION_UNIT_PLASMA_HEATER.ID,
                "multimachine.purificationunitplasmaheater",
                "Extreme Temperature Fluctuation Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitUVTreatment.set(
            new MTEPurificationUnitUVTreatment(
                PURIFICATION_UNIT_UV_TREATMENT.ID,
                "multimachine.purificationunituvtreatment",
                "High Energy Laser Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitDegasifier.set(
            new MTEPurificationUnitDegasser(
                PURIFICATION_UNIT_DEGASIFIER.ID,
                "multimachine.purificationunitdegasifier",
                "Residual Decontaminant Degasser Purification Unit").getStackForm(1L));
        ItemList.Machine_Multi_PurificationUnitParticleExtractor.set(
            new MTEPurificationUnitBaryonicPerfection(
                PURIFICATION_UNIT_PARTICLE_EXTRACTOR.ID,
                "multimachine.purificationunitextractor",
                "Absolute Baryonic Perfection Purification Unit").getStackForm(1L));
        ItemList.Hatch_DegasifierControl.set(
            new MTEHatchDegasifierControl(
                HATCH_DEGASIFIER_CONTROL.ID,
                "hatch.degasifiercontrol",
                "Degasser Control Hatch",
                8).getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Bronze.set(
            new MTELargeBoilerBronze(
                LARGE_BRONZE_BOILER_CONTROLLER.ID,
                "multimachine.boiler.bronze",
                "Large Bronze Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Steel.set(
            new MTELargeBoilerSteel(LARGE_STEEL_BOILER_CONTROLLER.ID, "multimachine.boiler.steel", "Large Steel Boiler")
                .getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Titanium.set(
            new MTELargeBoilerTitanium(
                LARGE_TITANIUM_BOILER_CONTROLLER.ID,
                "multimachine.boiler.titanium",
                "Large Titanium Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_TungstenSteel.set(
            new MTELargeBoilerTungstenSteel(
                LARGE_TUNGSTENSTEEL_BOILER_CONTROLLER.ID,
                "multimachine.boiler.tungstensteel",
                "Large Tungstensteel Boiler").getStackForm(1L));
        ItemList.FusionComputer_LuV.set(
            new MTEFusionComputer1(FUSION_CONTROLLER_MKI.ID, "fusioncomputer.tier.06", "Fusion Control Computer Mark I")
                .getStackForm(1L));
        ItemList.FusionComputer_ZPMV.set(
            new MTEFusionComputer2(
                FUSION_CONTROLLER_MKII.ID,
                "fusioncomputer.tier.07",
                "Fusion Control Computer Mark II").getStackForm(1L));
        ItemList.FusionComputer_UV.set(
            new MTEFusionComputer3(
                FUSION_CONTROLLER_MKIII.ID,
                "fusioncomputer.tier.08",
                "Fusion Control Computer Mark III").getStackForm(1L));

        ItemList.Distillation_Tower.set(
            new MTEDistillationTower(
                DISTILLATION_TOWER_CONTROLLER.ID,
                "multimachine.distillationtower",
                "Distillation Tower").getStackForm(1L));
        ItemList.Ore_Processor.set(
            new MTEIntegratedOreFactory(
                INTEGRATED_ORE_FACTORY_CONTROLLER.ID,
                "multimachine.oreprocessor",
                "Integrated Ore Factory").getStackForm(1L));

        ItemList.LargeSteamTurbine.set(
            new MTELargeTurbineSteam(
                LARGE_STEAM_TURBINE_CONTROLLER.ID,
                "multimachine.largeturbine",
                "Large Steam Turbine").getStackForm(1L));
        ItemList.LargeGasTurbine.set(
            new MTELargeTurbineGas(LARGE_GAS_TURBINE_CONTROLLER.ID, "multimachine.largegasturbine", "Large Gas Turbine")
                .getStackForm(1L));
        ItemList.LargeHPSteamTurbine.set(
            new MTELargeTurbineHPSteam(
                LARGE_HP_STEAM_TURBINE_CONTROLLER.ID,
                "multimachine.largehpturbine",
                "Large HP Steam Turbine").getStackForm(1L));
        ItemList.Machine_Multi_TranscendentPlasmaMixer.set(
            new MTETranscendentPlasmaMixer(
                TRANSCENDENT_PLASMA_MIXER_CONTROLLER.ID,
                "multimachine.transcendentplasmamixer",
                "Transcendent Plasma Mixer").getStackForm(1));

        ItemList.LargePlasmaTurbine.set(
            new MTELargeTurbinePlasma(
                LARGE_PLASMA_TURBINE_CONTROLLER.ID,
                "multimachine.largeplasmaturbine",
                "Large Plasma Turbine").getStackForm(1L));
        ItemList.Machine_Multi_HeatExchanger.set(
            new MTEHeatExchanger(
                LARGE_HEAT_EXCHANGER_CONTROLLER.ID,
                "multimachine.heatexchanger",
                "Large Heat Exchanger").getStackForm(1L));
        ItemList.Charcoal_Pile.set(
            new MTECharcoalPit(
                CHARCOAL_PILE_IGNITER_CONTROLLER.ID,
                "multimachine.charcoalpile",
                "Charcoal Pile Igniter").getStackForm(1));

        // Converter recipes in case you had old one lying around
        ItemList.OilDrill1.set(
            new MTEOilDrill1(MULTIBLOCK_PUMP_MKI_CONTROLLER.ID, "multimachine.oildrill1", "Fluid Drilling Rig")
                .getStackForm(1));
        ItemList.OilDrill2.set(
            new MTEOilDrill2(MULTILOCK_PUMP_MKII_CONTROLLER.ID, "multimachine.oildrill2", "Fluid Drilling Rig II")
                .getStackForm(1));
        ItemList.OilDrill3.set(
            new MTEOilDrill3(MULTILOCK_PUMP_MKIII_CONTROLLER.ID, "multimachine.oildrill3", "Fluid Drilling Rig III")
                .getStackForm(1));
        ItemList.OilDrill4.set(
            new MTEOilDrill4(MULTILOCK_PUMP_MKIV_CONTROLLER.ID, "multimachine.oildrill4", "Fluid Drilling Rig IV")
                .getStackForm(1));
        ItemList.OilDrillInfinite.set(
            new MTEOilDrillInfinite(
                MULTIBLOCK_PUMP_INFINITE_CONTROLLER.ID,
                "multimachine.oildrillinfinite",
                "Infinite Fluid Drilling Rig").getStackForm(1));

        ItemList.ConcreteBackfiller1.set(
            new MTEConcreteBackfiller1(
                CONCRETE_BACKFILLER_I_CONTROLLER.ID,
                "multimachine.concretebackfiller1",
                "Concrete Backfiller").getStackForm(1));
        ItemList.ConcreteBackfiller2.set(
            new MTEConcreteBackfiller2(
                CONCRETE_BACKFILLER_II_CONTROLLER.ID,
                "multimachine.concretebackfiller3",
                "Advanced Concrete Backfiller").getStackForm(1));
        ItemList.OreDrill1.set(
            new MTEOreDrillingPlant1(ORE_DRILL_MKI_CONTROLLER.ID, "multimachine.oredrill1", "Ore Drilling Plant")
                .getStackForm(1));
        ItemList.OreDrill2.set(
            new MTEOreDrillingPlant2(ORE_DRILL_MKII_CONTROLLER.ID, "multimachine.oredrill2", "Ore Drilling Plant II")
                .getStackForm(1));
        ItemList.OreDrill3.set(
            new MTEOreDrillingPlant3(ORE_DRILL_MKIII_CONTROLLER.ID, "multimachine.oredrill3", "Ore Drilling Plant III")
                .getStackForm(1));
        ItemList.OreDrill4.set(
            new MTEOreDrillingPlant4(ORE_DRILL_MKIV_CONTROLLER.ID, "multimachine.oredrill4", "Ore Drilling Plant IV")
                .getStackForm(1));

        ItemList.PyrolyseOven.set(
            new MTEPyrolyseOven(PYROLYSE_OVEN_CONTROLLER.ID, "multimachine.pyro", "Pyrolyse Oven").getStackForm(1));
        ItemList.OilCracker.set(
            new MTEOilCracker(OIL_CRACKER_CONTROLLER.ID, "multimachine.cracker", "Oil Cracking Unit").getStackForm(1));

        ItemList.SolarFactory.set(
            new MTESolarFactory(SOLAR_FACTORY_CONTROLLER.ID, "multimachine.solarfactory", "Solar Factory")
                .getStackForm(1));

        ItemList.Machine_Multi_Assemblyline.set(
            new MTEAssemblyLine(ASSEMBLING_LINE_CONTROLLER.ID, "multimachine.assemblyline", "Assembly Line")
                .getStackForm(1L));
        ItemList.Machine_Multi_DieselEngine.set(
            new MTEDieselEngine(COMBUSTION_ENGINE_CONTROLLER.ID, "multimachine.dieselengine", "Large Combustion Engine")
                .getStackForm(1L));
        ItemList.Machine_Multi_ExtremeDieselEngine.set(
            new MTEExtremeDieselEngine(
                EXTREME_COMBUSTION_ENGINE_CONTROLLER.ID,
                "multimachine.extremedieselengine",
                "Extreme Combustion Engine").getStackForm(1L));
        ItemList.Machine_Multi_Cleanroom.set(
            new MTECleanroom(CLEANROOM_CONTROLLER.ID, "multimachine.cleanroom", "Cleanroom Controller")
                .getStackForm(1));

        ItemList.Machine_Multi_LargeChemicalReactor.set(
            new MTELargeChemicalReactor(LCR_CONTROLLER.ID, "multimachine.chemicalreactor", "Large Chemical Reactor")
                .getStackForm(1));
        ItemList.PCBFactory.set(
            new MTEPCBFactory(PCB_FACTORY_CONTROLLER.ID, "multimachine.pcbfactory", "PCB Factory").getStackForm(1));
        ItemList.PCBBioChamber.set(
            new MTEPCBBioChamber(PCB_BIO_CHAMBER_CONTROLLER.ID, "multimachine.pcbbiochamber", "Bio Chamber")
                .getStackForm(1));
        ItemList.PCBCoolingTower.set(
            new MTEPCBCoolingTower(PCB_COOLING_TOWER_CONTROLLER.ID, "multimachine.pcbcoolingtower", "Cooling Tower")
                .getStackForm(1));
        ItemList.NanoForge
            .set(new MTENanoForge(NANO_FORGE_CONTROLLER.ID, "multimachine.nanoforge", "Nano Forge").getStackForm(1));
        ItemList.Machine_Multi_DroneCentre
            .set(new MTEDroneCentre(Drone_Centre.ID, "multimachine_DroneCentre", "Drone Centre").getStackForm(1));

        ItemList.Machine_Multi_IndustrialElectromagneticSeparator.set(
            new MTEIndustrialElectromagneticSeparator(
                INDUSTRIAL_ELECTROMAGNETIC_SEPARATOR_CONTROLLER.ID,
                "multimachine.electromagneticseparator",
                "Magnetic Flux Exhibitor").getStackForm(1));

        ItemList.Machine_Multi_Canner
            .set(new MTEMultiCanner(MULTI_CANNER_CONTROLLER.ID, "multimachine.canner", "TurboCan Pro").getStackForm(1));

        ItemList.Machine_Fluid_Shaper.set(
            new MTEFluidShaper(MULTI_FLUID_SHAPER_CONTROLLER.ID, "multimachine.solidifier", "Fluid Shaper")
                .getStackForm(1));
        ItemList.Machine_Mass_Solidifier.set(
            new MTEMassSolidifier(
                MULTI_MASS_SOLIDIFIER_CONTROLLER.ID,
                "multimachine.mass_solidifier",
                "Mass Solidifier").getStackForm(1));
        ItemList.Machine_Multi_ExoFoundry.set(
            new MTEExoFoundry(MultiExoFoundryController.ID, "multimachine.exofoundry", "Exo-Foundry").getStackForm(1));
        addItemTooltip(
            ItemList.Machine_Multi_ExoFoundry.get(1),
            chain(
                GTValues.AUTHORS_SUPPLIER,
                GTValues.fancyAuthorChrom,
                GTValues.AND_SUPPLIER,
                GTValues.AuthorAuynonymous));

        ItemList.WormholeGenerator.set(
            new MTEWormholeGenerator(
                WORMHOLE_GENERATOR_CONTROLLER.ID,
                "multimachine.wormhole",
                "Miniature Wormhole Generator").getStackForm(1));

        ItemList.Machine_Multi_IndustrialLaserEngraver.set(
            new MTEIndustrialLaserEngraver(
                INDUSTRIAL_LASER_ENGRAVER_CONTROLLER.ID,
                "multimachine.engraver",
                "Hyper-Intensity Laser Engraver").getStackForm(1));

        ItemList.Machine_Multi_IndustrialExtractor.set(
            new MTEIndustrialExtractor(
                INDUSTRIAL_EXTRACTOR_CONTROLLER.ID,
                "multimachine.extractor",
                "Dissection Apparatus").getStackForm(1));

        ItemList.Machine_Multi_Lathe.set(
            new MTEMultiLathe(MULTI_LATHE_CONTROLLER.ID, "multimachine.lathe", "Industrial Precision Lathe")
                .getStackForm(1));

        ItemList.Machine_Multi_IndustrialCompressor.set(
            new MTEIndustrialCompressor(
                INDUSTRIAL_COMPRESSOR_CONTROLLER.ID,
                "multimachine.basiccompressor",
                "Large Electric Compressor").getStackForm(1));
        ItemList.Machine_Multi_HIPCompressor.set(
            new MTEHIPCompressor(
                HIP_COMPRESSOR_CONTROLLER.ID,
                "multimachine.hipcompressor",
                "Hot Isostatic Pressurization Unit").getStackForm(1));
        ItemList.Machine_Multi_NeutroniumCompressor.set(
            new MTENeutroniumCompressor(
                NEUTRONIUM_COMPRESSOR_CONTROLLER.ID,
                "multimachine.neutroniumcompressor",
                "Neutronium Compressor").getStackForm(1));
        ItemList.Machine_Multi_BlackHoleCompressor.set(
            new MTEBlackHoleCompressor(
                BLACKHOLE_COMPRESSOR_CONTROLLER.ID,
                "multimachine.blackholecompressor",
                "Pseudostable Black Hole Containment Field").getStackForm(1));

        ItemList.Machine_Multi_IndustrialBrewery.set(
            new MTEIndustrialBrewery(INDUSTRIAL_BREWERY_CONTROLLER.ID, "multimachine.brewery", "Big Barrel Brewery")
                .getStackForm(1));

        ItemList.Machine_Multi_Spinmatron.set(
            new MTESpinmatron(SPINMATRON_CONTROLLER.ID, "multimachine.spinmatron", "Spinmatron-2737").getStackForm(1));
        addItemTooltip(
            ItemList.Machine_Multi_Spinmatron.get(1),
            chain(GTValues.AUTHORS_SUPPLIER, GTValues.fancyAuthorChrom, GTValues.AND_SUPPLIER, GTValues.AuthorNoc));

        ItemList.Machine_Multi_Autoclave.set(
            new MTEMultiAutoclave(MULTI_AUTOCLAVE_CONTROLLER.ID, "multimachine.autoclave", "Industrial Autoclave")
                .getStackForm(1));

        ItemList.LargeFluidExtractor.set(
            new MTELargeFluidExtractor(LARGE_FLUID_EXTRACTOR.ID, "multimachine.fluidextractor", "Large Fluid Extractor")
                .getStackForm(1));

        ItemList.EntropicProcessor.set(
            new MTEEntropicProcessor(ENTROPIC_PROCESSOR.ID, "multimachine.entropic-processor", "Entropic Processor")
                .getStackForm(1));

        ItemList.DecayWarehouse.set(
            new MTEDecayWarehouse(DECAY_WAREHOUSE.ID, "multimachine.decay-warehouse", "Decay Warehouse")
                .getStackForm(1));

        ItemList.LATEX.set(new MTELatex(LATEX.ID, "multimachine.latex", "L.A.T.E.X.").getStackForm(1));
        addItemTooltip(ItemList.LATEX.get(1), chain(() -> "Author: ", GTValues.AuthorThree));

        ItemList.Machine_Multi_NanochipAssemblyComplex.set(
            new MTENanochipAssemblyComplex(
                NANOCHIP_ASSEMBLY_CONTROLLER.ID,
                "multimachine.nanochipassemblycomplex",
                "Nanochip Assembly Complex").getStackForm(1));
        ItemList.NanoChipModule_AssemblyMatrix.set(
            new AssemblyMatrix(
                NANOCHIP_MODULE_ASSEMBLY_MATRIX.ID,
                "multimachine.nanochipmodule.assemblymatrix",
                "Nanochip Assembly Matrix").getStackForm(1));
        ItemList.NanoChipModule_SMDProcessor.set(
            new SMDProcessor(
                NANOCHIP_MODULE_SMD_PROCESSOR.ID,
                "multimachine.nanochipmodule.smdprocessor",
                "Part Preparation Apparatus").getStackForm(1));
        ItemList.NanoChipModule_BoardProcessor.set(
            new BoardProcessor(
                NANOCHIP_MODULE_BOARD_PROCESSOR.ID,
                "multimachine.nanochipmodule.boadprocessor",
                "Full-Board Immersion Device").getStackForm(1));
        ItemList.NanoChipModule_EtchingArray.set(
            new EtchingArray(
                NANOCHIP_MODULE_ETCHING_ARRAY.ID,
                "multimachine.nanochipmodule.etchingarray",
                "Ultra-high Energy Etching Array").getStackForm(1));
        ItemList.NanoChipModule_CuttingChamber.set(
            new CuttingChamber(
                NANOCHIP_MODULE_CUTTING_CHAMBER.ID,
                "multimachine.nanochipmodule.cuttingchamber",
                "Nanoprecision Cutting Chamber").getStackForm(1));
        ItemList.NanoChipModule_WireTracer.set(
            new WireTracer(
                NANOCHIP_MODULE_WIRE_TRACER.ID,
                "multimachine.nanochipmodule.wiretracer",
                "Nanoprecision Wire Tracer").getStackForm(1));
        ItemList.NanoChipModule_SuperconductorSplitter.set(
            new SuperconductorSplitter(
                NANOCHIP_MODULE_SUPERCONDUCTOR_SPLITTER.ID,
                "multimachine.nanochipmodule.superconductorsplitter",
                "Superconductive Strand Splitter").getStackForm(1));
        ItemList.NanoChipModule_Splitter.set(
            new Splitter(NANOCHIP_MODULE_SPLITTER.ID, "multimachine.nanochipmodule.splitter", "Nanopart Splitter")
                .getStackForm(1));
        ItemList.NanoChipModule_OpticalOrganizer.set(
            new OpticalOrganizer(NANOCHIP_MODULE_OPTICAL_ORGANIZER.ID, "multimachine.nanochipmodule.opticalorganizer", "Optically Optimized Organizer")
                .getStackForm(1));

        if (Thaumcraft.isModLoaded()) {
            ItemList.ResearchCompleter.set(
                new MTEResearchCompleter(ResearchCompleter.ID, "Research Completer", "Research Completer")
                    .getStackForm(1));
        }
    }

    private static void registerSteamMachines() {
        ItemList.Machine_Bronze_Furnace.set(
            new MTESteamFurnaceBronze(STEAM_FURNACE.ID, "bronzemachine.furnace", "Steam Furnace").getStackForm(1L));
        ItemList.Machine_Bronze_Macerator.set(
            new MTESteamMaceratorBronze(STEAM_MACERATOR.ID, "bronzemachine.macerator", "Steam Macerator")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Extractor.set(
            new MTESteamExtractorBronze(STEAM_EXTRACTOR.ID, "bronzemachine.extractor", "Steam Extractor")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Hammer.set(
            new MTESteamForgeHammerBronze(STEAM_FORGE_HAMMER.ID, "bronzemachine.hammer", "Steam Forge Hammer")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Compressor.set(
            new MTESteamCompressorBronze(STEAM_COMPRESSOR.ID, "bronzemachine.compressor", "Steam Compressor")
                .getStackForm(1L));
        ItemList.Machine_Bronze_AlloySmelter.set(
            new MTESteamAlloySmelterBronze(STEAM_ALLOY_SMELTER.ID, "bronzemachine.alloysmelter", "Steam Alloy Smelter")
                .getStackForm(1L));

    }

    private static void registerUnpackager() {
        ItemList.Machine_LV_Unboxinator.set(
            new MTEBasicMachineWithRecipe(
                UNPACKAGER_LV.ID,
                "basicmachine.unboxinator.tier.01",
                "Basic Unpackager",
                1,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "UNBOXINATOR").getStackForm(1L));

        ItemList.Machine_MV_Unboxinator.set(
            new MTEBasicMachineWithRecipe(
                UNPACKAGER_MV.ID,
                "basicmachine.unboxinator.tier.02",
                "Advanced Unpackager",
                2,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "UNBOXINATOR").getStackForm(1L));

        ItemList.Machine_HV_Unboxinator.set(
            new MTEBasicMachineWithRecipe(
                UNPACKAGER_HV.ID,
                "basicmachine.unboxinator.tier.03",
                "Advanced Unpackager II",
                3,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "UNBOXINATOR").getStackForm(1L));

        ItemList.Machine_EV_Unboxinator.set(
            new MTEBasicMachineWithRecipe(
                UNPACKAGER_EV.ID,
                "basicmachine.unboxinator.tier.04",
                "Advanced Unpackager III",
                4,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "UNBOXINATOR").getStackForm(1L));

        ItemList.Machine_IV_Unboxinator.set(
            new MTEBasicMachineWithRecipe(
                UNPACKAGER_IV.ID,
                "basicmachine.unboxinator.tier.05",
                "Unboxinator",
                5,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "UNBOXINATOR").getStackForm(1L));

        ItemList.Machine_LuV_Unboxinator.set(
            new MTEBasicMachineWithRecipe(
                UNPACKAGER_LuV.ID,
                "basicmachine.unboxinator.tier.06",
                "Unboxinator",
                6,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "UNBOXINATOR").getStackForm(1L));

        ItemList.Machine_ZPM_Unboxinator.set(
            new MTEBasicMachineWithRecipe(
                UNPACKAGER_ZPM.ID,
                "basicmachine.unboxinator.tier.07",
                "Unboxinator",
                7,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "UNBOXINATOR").getStackForm(1L));

        ItemList.Machine_UV_Unboxinator.set(
            new MTEBasicMachineWithRecipe(
                UNPACKAGER_UV.ID,
                "basicmachine.unboxinator.tier.08",
                "Unboxinator",
                8,
                MachineType.UNPACKAGER.tooltipDescription(),
                RecipeMaps.unpackagerRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "UNBOXINATOR").getStackForm(1L));
    }

    private static void registerAssemblingMachine() {

        ItemList.Machine_LV_Assembler.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLER_LV.ID,
                "basicmachine.assembler.tier.01",
                "Basic Assembling Machine",
                1,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.Machine_MV_Assembler.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLER_MV.ID,
                "basicmachine.assembler.tier.02",
                "Advanced Assembling Machine",
                2,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.Machine_HV_Assembler.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLER_HV.ID,
                "basicmachine.assembler.tier.03",
                "Advanced Assembling Machine II",
                3,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.Machine_EV_Assembler.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLER_EV.ID,
                "basicmachine.assembler.tier.04",
                "Advanced Assembling Machine III",
                4,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.Machine_IV_Assembler.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLER_IV.ID,
                "basicmachine.assembler.tier.05",
                "Advanced Assembling Machine IV",
                5,
                MachineType.ASSEMBLER.tooltipDescription(),
                RecipeMaps.assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.AssemblingMachineLuV.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLING_MACHINE_LuV.ID,
                "basicmachine.assembler.tier.06",
                "Elite Assembling Machine",
                6,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.AssemblingMachineZPM.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLING_MACHINE_ZPM.ID,
                "basicmachine.assembler.tier.07",
                "Elite Assembling Machine II",
                7,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.AssemblingMachineUV.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLING_MACHINE_UV.ID,
                "basicmachine.assembler.tier.08",
                "Ultimate Assembly Constructor",
                8,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.AssemblingMachineUHV.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLING_MACHINE_UHV.ID,
                "basicmachine.assembler.tier.09",
                "Epic Assembly Constructor",
                9,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.AssemblingMachineUEV.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLING_MACHINE_UEV.ID,
                "basicmachine.assembler.tier.10",
                "Epic Assembly Constructor II",
                10,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.AssemblingMachineUIV.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLING_MACHINE_UIV.ID,
                "basicmachine.assembler.tier.11",
                "Epic Assembly Constructor III",
                11,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));

        ItemList.AssemblingMachineUMV.set(
            new MTEBasicMachineWithRecipe(
                ASSEMBLING_MACHINE_UMV.ID,
                "basicmachine.assembler.tier.12",
                "Epic Assembly Constructor IV",
                12,
                MachineType.ASSEMBLER.tooltipDescription(),
                assemblerRecipes,
                9,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ASSEMBLER").getStackForm(1L));
    }

    private static void registerMatterAmplifier() {
        ItemList.Machine_LV_Amplifab.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_LV.ID,
                "basicmachine.amplifab.tier.01",
                "Basic Amplifabricator",
                1,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.Machine_MV_Amplifab.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_MV.ID,
                "basicmachine.amplifab.tier.02",
                "Advanced Amplifabricator",
                2,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.Machine_HV_Amplifab.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_HV.ID,
                "basicmachine.amplifab.tier.03",
                "Advanced Amplifabricator II",
                3,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.Machine_EV_Amplifab.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_EV.ID,
                "basicmachine.amplifab.tier.04",
                "Advanced Amplifabricator III",
                4,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.Machine_IV_Amplifab.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_IV.ID,
                "basicmachine.amplifab.tier.05",
                "Advanced Amplifabricator IV",
                5,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                RecipeMaps.amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.AmplifabricatorLuV.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_LuV.ID,
                "basicmachine.amplifab.tier.06",
                "Elite Amplifabricator",
                6,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.AmplifabricatorZPM.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_ZPM.ID,
                "basicmachine.amplifab.tier.07",
                "Elite Amplifabricator II",
                7,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.AmplifabricatorUV.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_UV.ID,
                "basicmachine.amplifab.tier.08",
                "Ultimate Amplicreator",
                8,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.AmplifabricatorUHV.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_UHV.ID,
                "basicmachine.amplifab.tier.09",
                "Epic Amplicreator",
                9,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.AmplifabricatorUEV.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_UEV.ID,
                "basicmachine.amplifab.tier.10",
                "Epic Amplicreator II",
                10,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.AmplifabricatorUIV.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_UIV.ID,
                "basicmachine.amplifab.tier.11",
                "Epic Amplicreator III",
                11,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));

        ItemList.AmplifabricatorUMV.set(
            new MTEBasicMachineWithRecipe(
                MATTER_AMPLIFIER_UMV.ID,
                "basicmachine.amplifab.tier.12",
                "Epic Amplicreator IV",
                12,
                MachineType.MATTER_AMPLIFIER.tooltipDescription(),
                amplifierRecipes,
                1,
                1,
                1000,
                SoundResource.GTCEU_LOOP_REPLICATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AMPLIFAB").getStackForm(1L));
    }

    private static void registerAlloySmelter() {
        ItemList.Machine_LV_AlloySmelter.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_LV.ID,
                "basicmachine.alloysmelter.tier.01",
                "Basic Alloy Smelter",
                1,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.Machine_MV_AlloySmelter.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_MV.ID,
                "basicmachine.alloysmelter.tier.02",
                "Advanced Alloy Smelter",
                2,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.Machine_HV_AlloySmelter.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_HV.ID,
                "basicmachine.alloysmelter.tier.03",
                "Advanced Alloy Smelter II",
                3,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.Machine_EV_AlloySmelter.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_EV.ID,
                "basicmachine.alloysmelter.tier.04",
                "Advanced Alloy Smelter III",
                4,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.Machine_IV_AlloySmelter.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_IV.ID,
                "basicmachine.alloysmelter.tier.05",
                "Advanced Alloy Smelter IV",
                5,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                RecipeMaps.alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.AlloySmelterLuV.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_LuV.ID,
                "basicmachine.alloysmelter.tier.06",
                "Elite Alloy Smelter",
                6,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.AlloySmelterZPM.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_ZPM.ID,
                "basicmachine.alloysmelter.tier.07",
                "Elite Alloy Smelter II",
                7,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.AlloySmelterUV.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_UV.ID,
                "basicmachine.alloysmelter.tier.08",
                "Ultimate Alloy Integrator",
                8,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.AlloySmelterUHV.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_UHV.ID,
                "basicmachine.alloysmelter.tier.09",
                "Epic Alloy Integrator",
                9,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.AlloySmelterUEV.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_UEV.ID,
                "basicmachine.alloysmelter.tier.10",
                "Epic Alloy Integrator II",
                10,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.AlloySmelterUIV.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_UIV.ID,
                "basicmachine.alloysmelter.tier.11",
                "Epic Alloy Integrator III",
                11,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));

        ItemList.AlloySmelterUMV.set(
            new MTEBasicMachineWithRecipe(
                ALLOY_SMELTER_UMV.ID,
                "basicmachine.alloysmelter.tier.12",
                "Epic Alloy Integrator IV",
                12,
                MachineType.ALLOY_SMELTER.tooltipDescription(),
                alloySmelterRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ALLOY_SMELTER").getStackForm(1L));
    }

    private static void registerHPSteamMachines() {
        ItemList.Machine_HP_Extractor.set(
            new MTESteamExtractorSteel(HP_STEAM_EXTRACTOR.ID, "hpmachine.extractor", "High Pressure Steam Extractor")
                .getStackForm(1L));
        ItemList.Machine_HP_Furnace.set(
            new MTESteamFurnaceSteel(HP_STEAM_FURNACE.ID, "hpmachine.furnace", "High Pressure Steam Furnace")
                .getStackForm(1L));
        ItemList.Machine_HP_Macerator.set(
            new MTESteamMaceratorSteel(HP_STEAM_MACERATOR.ID, "hpmachine.macerator", "High Pressure Steam Macerator")
                .getStackForm(1L));
        ItemList.Machine_HP_Hammer.set(
            new MTESteamForgeHammerSteel(
                HP_STEAM_FORGE_HAMMER.ID,
                "hpmachine.hammer",
                "High Pressure Steam Forge Hammer").getStackForm(1L));
        ItemList.Machine_HP_Compressor.set(
            new MTESteamCompressorSteel(
                HP_STEAM_COMPRESSOR.ID,
                "hpmachine.compressor",
                "High Pressure Steam Compressor").getStackForm(1L));
        ItemList.Machine_HP_AlloySmelter.set(
            new MTESteamAlloySmelterSteel(
                HP_STEAM_ALLOY_SMELTER.ID,
                "hpmachine.alloysmelter",
                "High Pressure Alloy Smelter").getStackForm(1L));
    }

    private static void registerLocker() {
        ItemList.Locker_ULV
            .set(new MTELocker(LOCKER_ULV.ID, "locker.tier.00", "Ultra Low Voltage Locker", 0).getStackForm(1L));
        ItemList.Locker_LV.set(new MTELocker(LOCKER_LV.ID, "locker.tier.01", "Low Voltage Locker", 1).getStackForm(1L));
        ItemList.Locker_MV
            .set(new MTELocker(LOCKER_MV.ID, "locker.tier.02", "Medium Voltage Locker", 2).getStackForm(1L));
        ItemList.Locker_HV
            .set(new MTELocker(LOCKER_HV.ID, "locker.tier.03", "High Voltage Locker", 3).getStackForm(1L));
        ItemList.Locker_EV
            .set(new MTELocker(LOCKER_EV.ID, "locker.tier.04", "Extreme Voltage Locker", 4).getStackForm(1L));
        ItemList.Locker_IV
            .set(new MTELocker(LOCKER_IV.ID, "locker.tier.05", "Insane Voltage Locker", 5).getStackForm(1L));
        ItemList.Locker_LuV
            .set(new MTELocker(LOCKER_LuV.ID, "locker.tier.06", "Ludicrous Voltage Locker", 6).getStackForm(1L));
        ItemList.Locker_ZPM
            .set(new MTELocker(LOCKER_ZPM.ID, "locker.tier.07", "ZPM Voltage Locker", 7).getStackForm(1L));
        ItemList.Locker_UV
            .set(new MTELocker(LOCKER_UV.ID, "locker.tier.08", "Ultimate Voltage Locker", 8).getStackForm(1L));
        ItemList.Locker_MAX
            .set(new MTELocker(LOCKER_UHV.ID, "locker.tier.09", "Highly Ultimate Voltage Locker", 9).getStackForm(1L));
    }

    private static void registerScanner() {
        ItemList.Machine_LV_Scanner
            .set(new MTEScanner(SCANNER_LV.ID, "basicmachine.scanner.tier.01", "Basic Scanner", 1).getStackForm(1L));
        ItemList.Machine_MV_Scanner
            .set(new MTEScanner(SCANNER_MV.ID, "basicmachine.scanner.tier.02", "Advanced Scanner", 2).getStackForm(1L));
        ItemList.Machine_HV_Scanner.set(
            new MTEScanner(SCANNER_HV.ID, "basicmachine.scanner.tier.03", "Advanced Scanner II", 3).getStackForm(1L));
        ItemList.Machine_EV_Scanner.set(
            new MTEScanner(SCANNER_EV.ID, "basicmachine.scanner.tier.04", "Advanced Scanner III", 4).getStackForm(1L));
        ItemList.Machine_IV_Scanner.set(
            new MTEScanner(SCANNER_IV.ID, "basicmachine.scanner.tier.05", "Advanced Scanner IV", 5).getStackForm(1L));
        ItemList.ScannerLuV
            .set(new MTEScanner(SCANNER_LuV.ID, "basicmachine.scanner.tier.06", "Elite Scanner", 6).getStackForm(1L));
        ItemList.ScannerZPM.set(
            new MTEScanner(SCANNER_ZPM.ID, "basicmachine.scanner.tier.07", "Elite Scanner II", 7).getStackForm(1L));
        ItemList.ScannerUV.set(
            new MTEScanner(SCANNER_UV.ID, "basicmachine.scanner.tier.08", "Ultimate Electron Microscope", 8)
                .getStackForm(1L));
        ItemList.ScannerUHV.set(
            new MTEScanner(SCANNER_UHV.ID, "basicmachine.scanner.tier.09", "Epic Electron Microscope", 9)
                .getStackForm(1L));
        ItemList.ScannerUEV.set(
            new MTEScanner(SCANNER_UEV.ID, "basicmachine.scanner.tier.10", "Epic Electron Microscope II", 10)
                .getStackForm(1L));
        ItemList.ScannerUIV.set(
            new MTEScanner(SCANNER_UIV.ID, "basicmachine.scanner.tier.11", "Epic Electron Microscope III", 11)
                .getStackForm(1L));
        ItemList.ScannerUMV.set(
            new MTEScanner(SCANNER_UMV.ID, "basicmachine.scanner.tier.12", "Epic Electron Microscope IV", 12)
                .getStackForm(1L));
    }

    private static void registerPackager() {
        ItemList.Machine_LV_Boxinator.set(
            new MTEBoxinator(PACKAGER_LV.ID, "basicmachine.boxinator.tier.01", "Basic Packager", 1).getStackForm(1L));
        ItemList.Machine_MV_Boxinator.set(
            new MTEBoxinator(PACKAGER_MV.ID, "basicmachine.boxinator.tier.02", "Advanced Packager", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Boxinator.set(
            new MTEBoxinator(PACKAGER_HV.ID, "basicmachine.boxinator.tier.03", "Advanced Packager II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Boxinator.set(
            new MTEBoxinator(PACKAGER_EV.ID, "basicmachine.boxinator.tier.04", "Advanced Packager III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Boxinator
            .set(new MTEBoxinator(PACKAGER_IV.ID, "basicmachine.boxinator.tier.05", "Boxinator", 5).getStackForm(1L));
        ItemList.Machine_LuV_Boxinator
            .set(new MTEBoxinator(PACKAGER_LuV.ID, "basicmachine.boxinator.tier.06", "Boxinator", 6).getStackForm(1L));
        ItemList.Machine_ZPM_Boxinator
            .set(new MTEBoxinator(PACKAGER_ZPM.ID, "basicmachine.boxinator.tier.07", "Boxinator", 7).getStackForm(1L));
        ItemList.Machine_UV_Boxinator
            .set(new MTEBoxinator(PACKAGER_UV.ID, "basicmachine.boxinator.tier.08", "Boxinator", 8).getStackForm(1L));
    }

    private static void registerRockBreaker() {
        ItemList.Machine_LV_RockBreaker.set(
            new MTERockBreaker(ROCK_BREAKER_LV.ID, "basicmachine.rockbreaker.tier.01", "Basic Rock Breaker", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_RockBreaker.set(
            new MTERockBreaker(ROCK_BREAKER_MV.ID, "basicmachine.rockbreaker.tier.02", "Advanced Rock Breaker", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_RockBreaker.set(
            new MTERockBreaker(ROCK_BREAKER_HV.ID, "basicmachine.rockbreaker.tier.03", "Advanced Rock Breaker II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_RockBreaker.set(
            new MTERockBreaker(ROCK_BREAKER_EV.ID, "basicmachine.rockbreaker.tier.04", "Advanced Rock Breaker III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_RockBreaker.set(
            new MTERockBreaker(
                ROCK_BREAKER_IV.ID,
                "basicmachine.rockbreaker.tier.05",
                "Cryogenic Magma Solidifier R-8200",
                5).getStackForm(1L));
        ItemList.RockBreakerLuV.set(
            new MTERockBreaker(ROCK_BREAKER_LuV.ID, "rockbreaker.tier.06", "Cryogenic Magma Solidifier R-9200", 6)
                .getStackForm(1L));

        ItemList.RockBreakerZPM.set(
            new MTERockBreaker(ROCK_BREAKER_ZPM.ID, "rockbreaker.tier.07", "Cryogenic Magma Solidifier R-10200", 7)
                .getStackForm(1L));

        ItemList.RockBreakerUV.set(
            new MTERockBreaker(ROCK_BREAKER_UV.ID, "rockbreaker.tier.08", "Cryogenic Magma Solidifier R-11200", 8)
                .getStackForm(1L));

        ItemList.RockBreakerUHV.set(
            new MTERockBreaker(ROCK_BREAKER_UHV.ID, "rockbreaker.tier.09", "Cryogenic Magma Solidifier R-12200", 9)
                .getStackForm(1L));

        ItemList.RockBreakerUEV.set(
            new MTERockBreaker(ROCK_BREAKER_UEV.ID, "rockbreaker.tier.10", "Cryogenic Magma Solidifier R-13200", 10)
                .getStackForm(1L));

        ItemList.RockBreakerUIV.set(
            new MTERockBreaker(ROCK_BREAKER_UIV.ID, "rockbreaker.tier.11", "Cryogenic Magma Solidifier R-14200", 11)
                .getStackForm(1L));

        ItemList.RockBreakerUMV.set(
            new MTERockBreaker(ROCK_BREAKER_UMV.ID, "rockbreaker.tier.12", "Cryogenic Magma Solidifier R-15200", 12)
                .getStackForm(1L));
    }

    private static void registerIndustrialApiary() {
        if (Forestry.isModLoaded()) {
            ItemList.Machine_IndustrialApiary.set(
                new MTEIndustrialApiary(INDUSTRIAL_APIARY.ID, "basicmachine.industrialapiary", "Industrial Apiary", 8)
                    .getStackForm(1L));
        }
    }

    private static void registerMassFab() {
        ItemList.Machine_LV_Massfab.set(
            new MTEMassfabricator(MASS_FABRICATOR_LV.ID, "basicmachine.massfab.tier.01", "Basic Mass Fabricator", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Massfab.set(
            new MTEMassfabricator(MASS_FABRICATOR_MV.ID, "basicmachine.massfab.tier.02", "Advanced Mass Fabricator", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Massfab.set(
            new MTEMassfabricator(
                MASS_FABRICATOR_HV.ID,
                "basicmachine.massfab.tier.03",
                "Advanced Mass Fabricator II",
                3).getStackForm(1L));
        ItemList.Machine_EV_Massfab.set(
            new MTEMassfabricator(
                MASS_FABRICATOR_EV.ID,
                "basicmachine.massfab.tier.04",
                "Advanced Mass Fabricator III",
                4).getStackForm(1L));
        ItemList.Machine_IV_Massfab.set(
            new MTEMassfabricator(
                MASS_FABRICATOR_IV.ID,
                "basicmachine.massfab.tier.05",
                "Advanced Mass Fabricator IV",
                5).getStackForm(1L));
    }

    private static void registerReplicator() {
        ItemList.Machine_LV_Replicator.set(
            new MTEReplicator(REPLICATOR_LV.ID, "basicmachine.replicator.tier.01", "Basic Replicator", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Replicator.set(
            new MTEReplicator(REPLICATOR_MV.ID, "basicmachine.replicator.tier.02", "Advanced Replicator", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Replicator.set(
            new MTEReplicator(REPLICATOR_HV.ID, "basicmachine.replicator.tier.03", "Advanced Replicator II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Replicator.set(
            new MTEReplicator(REPLICATOR_EV.ID, "basicmachine.replicator.tier.04", "Advanced Replicator III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Replicator.set(
            new MTEReplicator(REPLICATOR_IV.ID, "basicmachine.replicator.tier.05", "Advanced Replicator IV", 5)
                .getStackForm(1L));

        ItemList.ReplicatorLuV.set(
            new MTEReplicator(MATTER_REPLICATOR_LuV.ID, "basicmachine.replicator.tier.06", "Elite Replicator", 6)
                .getStackForm(1L));
        ItemList.ReplicatorZPM.set(
            new MTEReplicator(MATTER_REPLICATOR_ZPM.ID, "basicmachine.replicator.tier.07", "Elite Replicator II", 7)
                .getStackForm(1L));
        ItemList.ReplicatorUV.set(
            new MTEReplicator(
                MATTER_REPLICATOR_UV.ID,
                "basicmachine.replicator.tier.08",
                "Ultimate Elemental Composer",
                8).getStackForm(1L));
        ItemList.ReplicatorUHV.set(
            new MTEReplicator(MATTER_REPLICATOR_UHV.ID, "basicmachine.replicator.tier.09", "Epic Elemental Composer", 9)
                .getStackForm(1L));
        ItemList.ReplicatorUEV.set(
            new MTEReplicator(
                MATTER_REPLICATOR_UEV.ID,
                "basicmachine.replicator.tier.10",
                "Epic Elemental Composer II",
                10).getStackForm(1L));
        ItemList.ReplicatorUIV.set(
            new MTEReplicator(
                MATTER_REPLICATOR_UIV.ID,
                "basicmachine.replicator.tier.11",
                "Epic Elemental Composer III",
                11).getStackForm(1L));
        ItemList.ReplicatorUMV.set(
            new MTEReplicator(
                MATTER_REPLICATOR_UMV.ID,
                "basicmachine.replicator.tier.12",
                "Epic Elemental Composer IV",
                12).getStackForm(1L));
    }

    private static void registerBrewery() {
        ItemList.Machine_LV_Brewery.set(
            new MTEPotionBrewer(BREWERY_LV.ID, "basicmachine.brewery.tier.01", "Basic Brewery", 1).getStackForm(1L));
        ItemList.Machine_MV_Brewery.set(
            new MTEPotionBrewer(BREWERY_MV.ID, "basicmachine.brewery.tier.02", "Advanced Brewery", 2).getStackForm(1L));
        ItemList.Machine_HV_Brewery.set(
            new MTEPotionBrewer(BREWERY_HV.ID, "basicmachine.brewery.tier.03", "Advanced Brewery II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Brewery.set(
            new MTEPotionBrewer(BREWERY_EV.ID, "basicmachine.brewery.tier.04", "Advanced Brewery III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Brewery.set(
            new MTEPotionBrewer(BREWERY_IV.ID, "basicmachine.brewery.tier.05", "Advanced Brewery IV", 5)
                .getStackForm(1L));

        ItemList.BreweryLuV.set(
            new MTEPotionBrewer(BREWERY_LuV.ID, "basicmachine.brewery.tier.06", "Elite Brewery", 6).getStackForm(1L));
        ItemList.BreweryZPM.set(
            new MTEPotionBrewer(BREWERY_ZPM.ID, "basicmachine.brewery.tier.07", "Elite Brewery II", 7)
                .getStackForm(1L));
        ItemList.BreweryUV.set(
            new MTEPotionBrewer(BREWERY_UV.ID, "basicmachine.brewery.tier.08", "Ultimate Brew Rusher", 8)
                .getStackForm(1L));
        ItemList.BreweryUHV.set(
            new MTEPotionBrewer(BREWERY_UHV.ID, "basicmachine.brewery.tier.09", "Epic Brew Rusher", 9)
                .getStackForm(1L));
        ItemList.BreweryUEV.set(
            new MTEPotionBrewer(BREWERY_UEV.ID, "basicmachine.brewery.tier.10", "Epic Brew Rusher II", 10)
                .getStackForm(1L));
        ItemList.BreweryUIV.set(
            new MTEPotionBrewer(BREWERY_UIV.ID, "basicmachine.brewery.tier.11", "Epic Brew Rusher III", 11)
                .getStackForm(1L));
        ItemList.BreweryUMV.set(
            new MTEPotionBrewer(BREWERY_UMV.ID, "basicmachine.brewery.tier.12", "Epic Brew Rusher IV", 12)
                .getStackForm(1L));
    }

    private static void registerMiner() {
        ItemList.Machine_LV_Miner
            .set(new MTEMiner(MINER_LV.ID, "basicmachine.miner.tier.01", "Basic Miner", 1).getStackForm(1L));
        ItemList.Machine_MV_Miner
            .set(new MTEMiner(MINER_MV.ID, "basicmachine.miner.tier.02", "Good Miner", 2).getStackForm(1L));
        ItemList.Machine_HV_Miner
            .set(new MTEMiner(MINER_HV.ID, "basicmachine.miner.tier.03", "Advanced Miner", 3).getStackForm(1L));
    }

    private static void registerPump() {
        ItemList.Pump_LV.set(new MTEPump(PUMP_LV.ID, "basicmachine.pump.tier.01", "Basic Pump", 1).getStackForm(1L));
        ItemList.Pump_MV.set(new MTEPump(PUMP_MV.ID, "basicmachine.pump.tier.02", "Good Pump", 2).getStackForm(1L));
        ItemList.Pump_HV.set(new MTEPump(PUMP_HV.ID, "basicmachine.pump.tier.03", "Advanced Pump", 3).getStackForm(1L));
    }

    private static void registerTeleporter() {
        ItemList.Teleporter
            .set(new MTETeleporter(TELEPORTER.ID, "basicmachine.teleporter", "Teleporter", 9).getStackForm(1L));
    }

    private static void registerMonsterRepellator() {
        ItemList.MobRep_LV.set(
            new MTEMonsterRepellent(
                MONSTER_REPELLATOR_LV.ID,
                "basicmachine.mobrep.tier.01",
                "Basic Monster Repellator",
                1).getStackForm(1L));
        ItemList.MobRep_MV.set(
            new MTEMonsterRepellent(
                MONSTER_REPELLATOR_MV.ID,
                "basicmachine.mobrep.tier.02",
                "Advanced Monster Repellator",
                2).getStackForm(1L));
        ItemList.MobRep_HV.set(
            new MTEMonsterRepellent(
                MONSTER_REPELLATOR_HV.ID,
                "basicmachine.mobrep.tier.03",
                "Advanced Monster Repellator II",
                3).getStackForm(1L));
        ItemList.MobRep_EV.set(
            new MTEMonsterRepellent(
                MONSTER_REPELLATOR_EV.ID,
                "basicmachine.mobrep.tier.04",
                "Advanced Monster Repellator III",
                4).getStackForm(1L));
        ItemList.MobRep_IV.set(
            new MTEMonsterRepellent(
                MONSTER_REPELLATOR_IV.ID,
                "basicmachine.mobrep.tier.05",
                "Advanced Monster Repellator IV",
                5).getStackForm(1L));
        ItemList.MobRep_LuV.set(
            new MTEMonsterRepellent(
                MONSTER_REPELLATOR_LuV.ID,
                "basicmachine.mobrep.tier.06",
                "Advanced Monster Repellator V",
                6).getStackForm(1L));
        ItemList.MobRep_ZPM.set(
            new MTEMonsterRepellent(
                MONSTER_REPELLATOR_ZPM.ID,
                "basicmachine.mobrep.tier.07",
                "Advanced Monster Repellator VI",
                7).getStackForm(1L));
        ItemList.MobRep_UV.set(
            new MTEMonsterRepellent(
                MONSTER_REPELLATOR_UV.ID,
                "basicmachine.mobrep.tier.08",
                "Advanced Monster Repellator VII",
                8).getStackForm(1L));
    }

    private static void registerMagLevPylon() {
        ItemList.MagLevPython_MV.set(
            new MTEMagLevPylon(MAGLEV_PYLON_MV.ID, "basicmachine.maglev.tier.02", "Advanced MagLev Pylon", 2)
                .getStackForm(1L));
        ItemList.MagLevPython_HV.set(
            new MTEMagLevPylon(MAGLEV_PYLON_HV.ID, "basicmachine.maglev.tier.03", "Advanced MagLev Pylon II", 3)
                .getStackForm(1L));
        ItemList.MagLevPython_EV.set(
            new MTEMagLevPylon(MAGLEV_PYLON_EV.ID, "basicmachine.maglev.tier.04", "Advanced MagLev Pylon III", 4)
                .getStackForm(1L));
    }

    private void registerWorldAccelerator() {
        ItemList.AcceleratorLV.set(
            new MTEWorldAccelerator(
                WORLD_ACCELERATOR_LV.ID,
                "basicmachine.accelerator.tier.01",
                "Basic World Accelerator",
                1).getStackForm(1L));
        ItemList.AcceleratorMV.set(
            new MTEWorldAccelerator(
                WORLD_ACCELERATOR_MV.ID,
                "basicmachine.accelerator.tier.02",
                "Advanced World Accelerator",
                2).getStackForm(1L));
        ItemList.AcceleratorHV.set(
            new MTEWorldAccelerator(
                WORLD_ACCELERATOR_HV.ID,
                "basicmachine.accelerator.tier.03",
                "Advanced World Accelerator II",
                3).getStackForm(1L));
        ItemList.AcceleratorEV.set(
            new MTEWorldAccelerator(
                WORLD_ACCELERATOR_EV.ID,
                "basicmachine.accelerator.tier.04",
                "Advanced World Accelerator III",
                4).getStackForm(1L));
        ItemList.AcceleratorIV.set(
            new MTEWorldAccelerator(
                WORLD_ACCELERATOR_IV.ID,
                "basicmachine.accelerator.tier.05",
                "Advanced World Accelerator IV",
                5).getStackForm(1L));
        ItemList.AcceleratorLuV.set(
            new MTEWorldAccelerator(
                WORLD_ACCELERATOR_LuV.ID,
                "basicmachine.accelerator.tier.06",
                "Elite World Accelerator",
                6).getStackForm(1L));
        ItemList.AcceleratorZPM.set(
            new MTEWorldAccelerator(
                WORLD_ACCELERATOR_ZPM.ID,
                "basicmachine.accelerator.tier.07",
                "Elite World Accelerator II",
                7).getStackForm(1L));
        ItemList.AcceleratorUV.set(
            new MTEWorldAccelerator(
                WORLD_ACCELERATOR_UV.ID,
                "basicmachine.accelerator.tier.08",
                "Ultimate Time Anomaly",
                8).getStackForm(1L));

    }

    private static void registerAdvancedSeismicProspector() {
        ItemList.Seismic_Prospector_Adv_LV.set(
            new MTEAdvSeismicProspector(
                ADVANCED_SEISMIC_PROSPECTOR_LV.ID,
                "basicmachine.seismicprospector.07",
                "Advanced Seismic Prospector LV",
                1,
                5 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_MV.set(
            new MTEAdvSeismicProspector(
                ADVANCED_SEISMIC_PROSPECTOR_MV.ID,
                "basicmachine.seismicprospector.06",
                "Advanced Seismic Prospector MV",
                2,
                7 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_HV.set(
            new MTEAdvSeismicProspector(
                ADVANCED_SEISMIC_PROSPECTOR_HV.ID,
                "basicmachine.seismicprospector.05",
                "Advanced Seismic Prospector HV",
                3,
                9 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_EV.set(
            new MTEAdvSeismicProspector(
                ADVANCED_SEISMIC_PROSPECTOR_EV.ID,
                "basicmachine.seismicprospector.04",
                "Advanced Seismic Prospector EV",
                4,
                11 * 16 / 2,
                2).getStackForm(1));
    }

    private static void registerMicrowaveEnergyTransmitter() {
        ItemList.MicroTransmitter_HV.set(
            new MTEMicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_HV.ID,
                "basicmachine.microtransmitter.03",
                "HV Microwave Energy Transmitter",
                3).getStackForm(1L));
        ItemList.MicroTransmitter_EV.set(
            new MTEMicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_EV.ID,
                "basicmachine.microtransmitter.04",
                "EV Microwave Energy Transmitter",
                4).getStackForm(1L));
        ItemList.MicroTransmitter_IV.set(
            new MTEMicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_IV.ID,
                "basicmachine.microtransmitter.05",
                "IV Microwave Energy Transmitter",
                5).getStackForm(1L));
        ItemList.MicroTransmitter_LUV.set(
            new MTEMicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_LuV.ID,
                "basicmachine.microtransmitter.06",
                "LuV Microwave Energy Transmitter",
                6).getStackForm(1L));
        ItemList.MicroTransmitter_ZPM.set(
            new MTEMicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_ZPM.ID,
                "basicmachine.microtransmitter.07",
                "ZPM Microwave Energy Transmitter",
                7).getStackForm(1L));
        ItemList.MicroTransmitter_UV.set(
            new MTEMicrowaveEnergyTransmitter(
                MICROWAVE_ENERGY_TRANSMITTER_UV.ID,
                "basicmachine.microtransmitter.08",
                "UV Microwave Energy Transmitter",
                8).getStackForm(1L));
    }

    private static void registerBetterJukebox() {
        ItemList.BetterJukebox_LV.set(
            new MTEBetterJukebox(
                BETTER_JUKEBOX_LV.ID,
                "basicmachine.betterjukebox.tier.01",
                "Basic Electric Jukebox",
                1).getStackForm(1L));
        ItemList.BetterJukebox_MV.set(
            new MTEBetterJukebox(
                BETTER_JUKEBOX_MV.ID,
                "basicmachine.betterjukebox.tier.02",
                "Advanced Electric Jukebox",
                2).getStackForm(1L));
        ItemList.BetterJukebox_HV.set(
            new MTEBetterJukebox(
                BETTER_JUKEBOX_HV.ID,
                "basicmachine.betterjukebox.tier.03",
                "Advanced Electric Jukebox II",
                3).getStackForm(1L));
        ItemList.BetterJukebox_EV.set(
            new MTEBetterJukebox(BETTER_JUKEBOX_EV.ID, "basicmachine.betterjukebox.tier.04", "Extreme Music Mixer", 4)
                .getStackForm(1L));
        ItemList.BetterJukebox_IV.set(
            new MTEBetterJukebox(BETTER_JUKEBOX_IV.ID, "basicmachine.betterjukebox.tier.05", "Duke Mix'em 3D", 5)
                .getStackForm(1L));
    }

    private static void registerChestBuffer() {
        ItemList.Automation_ChestBuffer_ULV.set(
            new MTEChestBuffer(
                CHEST_BUFFER_ULV.ID,
                "automation.chestbuffer.tier.00",
                "Ultra Low Voltage Chest Buffer",
                0).getStackForm(1L));
        ItemList.Automation_ChestBuffer_LV.set(
            new MTEChestBuffer(CHEST_BUFFER_LV.ID, "automation.chestbuffer.tier.01", "Low Voltage Chest Buffer", 1)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_MV.set(
            new MTEChestBuffer(CHEST_BUFFER_MV.ID, "automation.chestbuffer.tier.02", "Medium Voltage Chest Buffer", 2)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_HV.set(
            new MTEChestBuffer(CHEST_BUFFER_HV.ID, "automation.chestbuffer.tier.03", "High Voltage Chest Buffer", 3)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_EV.set(
            new MTEChestBuffer(CHEST_BUFFER_EV.ID, "automation.chestbuffer.tier.04", "Extreme Voltage Chest Buffer", 4)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_IV.set(
            new MTEChestBuffer(CHEST_BUFFER_IV.ID, "automation.chestbuffer.tier.05", "Insane Voltage Chest Buffer", 5)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_LuV.set(
            new MTEChestBuffer(
                CHEST_BUFFER_LuV.ID,
                "automation.chestbuffer.tier.06",
                "Ludicrous Voltage Chest Buffer",
                6).getStackForm(1L));
        ItemList.Automation_ChestBuffer_ZPM.set(
            new MTEChestBuffer(CHEST_BUFFER_ZPM.ID, "automation.chestbuffer.tier.07", "ZPM Voltage Chest Buffer", 7)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_UV.set(
            new MTEChestBuffer(CHEST_BUFFER_UV.ID, "automation.chestbuffer.tier.08", "Ultimate Voltage Chest Buffer", 8)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_UHV.set(
            new MTEChestBuffer(
                CHEST_BUFFER_UHV.ID,
                "automation.chestbuffer.tier.09",
                "Highly Ultimate Voltage Chest Buffer",
                9).getStackForm(1L));

        ItemList.Automation_ChestBuffer_UEV.set(
            new MTEChestBuffer(
                CHEST_BUFFER_UEV.ID,
                "automation.chestbuffer.tier.10",
                "Ultra High Voltage Chest Buffer",
                10).getStackForm(1L));

        ItemList.Automation_ChestBuffer_UIV.set(
            new MTEChestBuffer(CHEST_BUFFER_UIV.ID, "automation.chestbuffer.tier.11", "UIV Voltage Chest Buffer", 11)
                .getStackForm(1L));

        ItemList.Automation_ChestBuffer_UMV.set(
            new MTEChestBuffer(CHEST_BUFFER_UMV.ID, "automation.chestbuffer.tier.12", "UMV Voltage Chest Buffer", 12)
                .getStackForm(1L));
    }

    private static void registerItemFilter() {
        ItemList.Automation_Filter_ULV.set(
            new MTEFilter(ITEM_FILTER_ULV.ID, "automation.filter.tier.00", "Ultra Low Voltage Item Filter", 0)
                .getStackForm(1L));
        ItemList.Automation_Filter_LV.set(
            new MTEFilter(ITEM_FILTER_LV.ID, "automation.filter.tier.01", "Low Voltage Item Filter", 1)
                .getStackForm(1L));
        ItemList.Automation_Filter_MV.set(
            new MTEFilter(ITEM_FILTER_MV.ID, "automation.filter.tier.02", "Medium Voltage Item Filter", 2)
                .getStackForm(1L));
        ItemList.Automation_Filter_HV.set(
            new MTEFilter(ITEM_FILTER_HV.ID, "automation.filter.tier.03", "High Voltage Item Filter", 3)
                .getStackForm(1L));
        ItemList.Automation_Filter_EV.set(
            new MTEFilter(ITEM_FILTER_EV.ID, "automation.filter.tier.04", "Extreme Voltage Item Filter", 4)
                .getStackForm(1L));
        ItemList.Automation_Filter_IV.set(
            new MTEFilter(ITEM_FILTER_IV.ID, "automation.filter.tier.05", "Insane Voltage Item Filter", 5)
                .getStackForm(1L));
        ItemList.Automation_Filter_LuV.set(
            new MTEFilter(ITEM_FILTER_LuV.ID, "automation.filter.tier.06", "Ludicrous Voltage Item Filter", 6)
                .getStackForm(1L));
        ItemList.Automation_Filter_ZPM.set(
            new MTEFilter(ITEM_FILTER_ZPM.ID, "automation.filter.tier.07", "ZPM Voltage Item Filter", 7)
                .getStackForm(1L));
        ItemList.Automation_Filter_UV.set(
            new MTEFilter(ITEM_FILTER_UV.ID, "automation.filter.tier.08", "Ultimate Voltage Item Filter", 8)
                .getStackForm(1L));
        ItemList.Automation_Filter_MAX.set(
            new MTEFilter(ITEM_FILTER_UHV.ID, "automation.filter.tier.09", "Highly Ultimate Voltage Item Filter", 9)
                .getStackForm(1L));
    }

    private static void registerTypeFilter() {
        ItemList.Automation_TypeFilter_ULV.set(
            new MTETypeFilter(TYPE_FILTER_ULV.ID, "automation.typefilter.tier.00", "Ultra Low Voltage Type Filter", 0)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_LV.set(
            new MTETypeFilter(TYPE_FILTER_LV.ID, "automation.typefilter.tier.01", "Low Voltage Type Filter", 1)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_MV.set(
            new MTETypeFilter(TYPE_FILTER_MV.ID, "automation.typefilter.tier.02", "Medium Voltage Type Filter", 2)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_HV.set(
            new MTETypeFilter(TYPE_FILTER_HV.ID, "automation.typefilter.tier.03", "High Voltage Type Filter", 3)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_EV.set(
            new MTETypeFilter(TYPE_FILTER_EV.ID, "automation.typefilter.tier.04", "Extreme Voltage Type Filter", 4)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_IV.set(
            new MTETypeFilter(TYPE_FILTER_IV.ID, "automation.typefilter.tier.05", "Insane Voltage Type Filter", 5)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_LuV.set(
            new MTETypeFilter(TYPE_FILTER_LuV.ID, "automation.typefilter.tier.06", "Ludicrous Voltage Type Filter", 6)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_ZPM.set(
            new MTETypeFilter(TYPE_FILTER_ZPM.ID, "automation.typefilter.tier.07", "ZPM Voltage Type Filter", 7)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_UV.set(
            new MTETypeFilter(TYPE_FILTER_UV.ID, "automation.typefilter.tier.08", "Ultimate Voltage Type Filter", 8)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_MAX.set(
            new MTETypeFilter(
                TYPE_FILTER_UHV.ID,
                "automation.typefilter.tier.09",
                "Highly Ultimate Voltage Type Filter",
                9).getStackForm(1L));
    }

    private static void registerRegulator() {
        ItemList.Automation_Regulator_ULV.set(
            new MTERegulator(VOLTAGE_REGULATOR_ULV.ID, "automation.regulator.tier.00", "Ultra Low Voltage Regulator", 0)
                .getStackForm(1L));
        ItemList.Automation_Regulator_LV.set(
            new MTERegulator(VOLTAGE_REGULATOR_LV.ID, "automation.regulator.tier.01", "Low Voltage Regulator", 1)
                .getStackForm(1L));
        ItemList.Automation_Regulator_MV.set(
            new MTERegulator(VOLTAGE_REGULATOR_MV.ID, "automation.regulator.tier.02", "Medium Voltage Regulator", 2)
                .getStackForm(1L));
        ItemList.Automation_Regulator_HV.set(
            new MTERegulator(VOLTAGE_REGULATOR_HV.ID, "automation.regulator.tier.03", "High Voltage Regulator", 3)
                .getStackForm(1L));
        ItemList.Automation_Regulator_EV.set(
            new MTERegulator(VOLTAGE_REGULATOR_EV.ID, "automation.regulator.tier.04", "Extreme Voltage Regulator", 4)
                .getStackForm(1L));
        ItemList.Automation_Regulator_IV.set(
            new MTERegulator(VOLTAGE_REGULATOR_IV.ID, "automation.regulator.tier.05", "Insane Voltage Regulator", 5)
                .getStackForm(1L));
        ItemList.Automation_Regulator_LuV.set(
            new MTERegulator(VOLTAGE_REGULATOR_LuV.ID, "automation.regulator.tier.06", "Ludicrous Voltage Regulator", 6)
                .getStackForm(1L));
        ItemList.Automation_Regulator_ZPM.set(
            new MTERegulator(VOLTAGE_REGULATOR_ZPM.ID, "automation.regulator.tier.07", "ZPM Voltage Regulator", 7)
                .getStackForm(1L));
        ItemList.Automation_Regulator_UV.set(
            new MTERegulator(VOLTAGE_REGULATOR_UV.ID, "automation.regulator.tier.08", "Ultimate Voltage Regulator", 8)
                .getStackForm(1L));
        ItemList.Automation_Regulator_MAX.set(
            new MTERegulator(
                VOLTAGE_REGULATOR_UHV.ID,
                "automation.regulator.tier.09",
                "Highly Ultimate Voltage Regulator",
                9).getStackForm(1L));
    }

    private static void registerSuperBuffer() {
        ItemList.Automation_SuperBuffer_ULV.set(
            new MTESuperBuffer(
                SUPER_BUFFER_ULV.ID,
                "automation.superbuffer.tier.00",
                "Ultra Low Voltage Super Buffer",
                0).getStackForm(1L));
        ItemList.Automation_SuperBuffer_LV.set(
            new MTESuperBuffer(SUPER_BUFFER_LV.ID, "automation.superbuffer.tier.01", "Low Voltage Super Buffer", 1)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_MV.set(
            new MTESuperBuffer(SUPER_BUFFER_MV.ID, "automation.superbuffer.tier.02", "Medium Voltage Super Buffer", 2)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_HV.set(
            new MTESuperBuffer(SUPER_BUFFER_HV.ID, "automation.superbuffer.tier.03", "High Voltage Super Buffer", 3)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_EV.set(
            new MTESuperBuffer(SUPER_BUFFER_EV.ID, "automation.superbuffer.tier.04", "Extreme Voltage Super Buffer", 4)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_IV.set(
            new MTESuperBuffer(SUPER_BUFFER_IV.ID, "automation.superbuffer.tier.05", "Insane Voltage Super Buffer", 5)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_LuV.set(
            new MTESuperBuffer(
                SUPER_BUFFER_LuV.ID,
                "automation.superbuffer.tier.06",
                "Ludicrous Voltage Super Buffer",
                6).getStackForm(1L));
        ItemList.Automation_SuperBuffer_ZPM.set(
            new MTESuperBuffer(SUPER_BUFFER_ZPM.ID, "automation.superbuffer.tier.07", "ZPM Voltage Super Buffer", 7)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_UV.set(
            new MTESuperBuffer(SUPER_BUFFER_UV.ID, "automation.superbuffer.tier.08", "Ultimate Voltage Super Buffer", 8)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_MAX.set(
            new MTESuperBuffer(
                SUPER_BUFFER_UHV.ID,
                "automation.superbuffer.tier.09",
                "Highly Ultimate Voltage Super Buffer",
                9).getStackForm(1L));
    }

    private static void registerItemDistributor() {
        ItemList.Automation_ItemDistributor_ULV.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_ULV.ID,
                "automation.itemdistributor.tier.00",
                "Ultra Low Voltage Item Distributor",
                0).getStackForm(1L));
        ItemList.Automation_ItemDistributor_LV.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_LV.ID,
                "automation.itemdistributor.tier.01",
                "Low Voltage Item Distributor",
                1).getStackForm(1L));
        ItemList.Automation_ItemDistributor_MV.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_MV.ID,
                "automation.itemdistributor.tier.02",
                "Medium Voltage Item Distributor",
                2).getStackForm(1L));
        ItemList.Automation_ItemDistributor_HV.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_HV.ID,
                "automation.itemdistributor.tier.03",
                "High Voltage Item Distributor",
                3).getStackForm(1L));
        ItemList.Automation_ItemDistributor_EV.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_EV.ID,
                "automation.itemdistributor.tier.04",
                "Extreme Voltage Item Distributor",
                4).getStackForm(1L));
        ItemList.Automation_ItemDistributor_IV.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_IV.ID,
                "automation.itemdistributor.tier.05",
                "Insane Voltage Item Distributor",
                5).getStackForm(1L));
        ItemList.Automation_ItemDistributor_LuV.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_LuV.ID,
                "automation.itemdistributor.tier.06",
                "Ludicrous Voltage Item Distributor",
                6).getStackForm(1L));
        ItemList.Automation_ItemDistributor_ZPM.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_ZPM.ID,
                "automation.itemdistributor.tier.07",
                "ZPM Voltage Item Distributor",
                7).getStackForm(1L));
        ItemList.Automation_ItemDistributor_UV.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_UV.ID,
                "automation.itemdistributor.tier.08",
                "Ultimate Voltage Item Distributor",
                8).getStackForm(1L));
        ItemList.Automation_ItemDistributor_MAX.set(
            new MTEItemDistributor(
                ITEM_DISTRIBUTOR_UHV.ID,
                "automation.itemdistributor.tier.09",
                "MAX Voltage Item Distributor",
                9).getStackForm(1L));
    }

    private static void registerRecipeFilter() {
        ItemList.Automation_RecipeFilter_ULV.set(
            new MTERecipeFilter(
                RECIPE_FILTER_ULV.ID,
                "automation.recipefilter.tier.00",
                "Ultra Low Voltage Recipe Filter",
                0).getStackForm(1L));
        ItemList.Automation_RecipeFilter_LV.set(
            new MTERecipeFilter(RECIPE_FILTER_LV.ID, "automation.recipefilter.tier.01", "Low Voltage Recipe Filter", 1)
                .getStackForm(1L));
        ItemList.Automation_RecipeFilter_MV.set(
            new MTERecipeFilter(
                RECIPE_FILTER_MV.ID,
                "automation.recipefilter.tier.02",
                "Medium Voltage Recipe Filter",
                2).getStackForm(1L));
        ItemList.Automation_RecipeFilter_HV.set(
            new MTERecipeFilter(RECIPE_FILTER_HV.ID, "automation.recipefilter.tier.03", "High Voltage Recipe Filter", 3)
                .getStackForm(1L));
        ItemList.Automation_RecipeFilter_EV.set(
            new MTERecipeFilter(
                RECIPE_FILTER_EV.ID,
                "automation.recipefilter.tier.04",
                "Extreme Voltage Recipe Filter",
                4).getStackForm(1L));
        ItemList.Automation_RecipeFilter_IV.set(
            new MTERecipeFilter(
                RECIPE_FILTER_IV.ID,
                "automation.recipefilter.tier.05",
                "Insane Voltage Recipe Filter",
                5).getStackForm(1L));
        ItemList.Automation_RecipeFilter_LuV.set(
            new MTERecipeFilter(
                RECIPE_FILTER_LuV.ID,
                "automation.recipefilter.tier.06",
                "Ludicrous Voltage Recipe Filter",
                6).getStackForm(1L));
        ItemList.Automation_RecipeFilter_ZPM.set(
            new MTERecipeFilter(RECIPE_FILTER_ZPM.ID, "automation.recipefilter.tier.07", "ZPM Voltage Recipe Filter", 7)
                .getStackForm(1L));
        ItemList.Automation_RecipeFilter_UV.set(
            new MTERecipeFilter(
                RECIPE_FILTER_UV.ID,
                "automation.recipefilter.tier.08",
                "Ultimate Voltage Recipe Filter",
                8).getStackForm(1L));
        ItemList.Automation_RecipeFilter_MAX.set(
            new MTERecipeFilter(
                RECIPE_FILTER_UHV.ID,
                "automation.recipefilter.tier.09",
                "Highly Ultimate Voltage Recipe Filter",
                9).getStackForm(1L));
    }

    private static void registerMachineHull() {
        ItemList.Hull_Bronze.set(
            new MTEBasicHullBronze(HULL_BRONZE.ID, "hull.bronze", "Bronze Hull", 0, "For your first Steam Machines")
                .getStackForm(1L));
        ItemList.Hull_Bronze_Bricks.set(
            new MTEBasicHullBronzeBricks(
                HULL_BRICKED_BRONZE.ID,
                "hull.bronze_bricked",
                "Bricked Bronze Hull",
                0,
                "For your first Steam Machines").getStackForm(1L));
        ItemList.Hull_HP.set(
            new MTEBasicHullSteel(HULL_STEEL.ID, "hull.steel", "Steel Hull", 0, "For improved Steam Machines")
                .getStackForm(1L));
        ItemList.Hull_HP_Bricks.set(
            new MTEBasicHullSteelBricks(
                HULL_WROUGHT_IRON.ID,
                "hull.steel_bricked",
                "Bricked Wrought Iron Hull",
                0,
                "For improved Steam Machines").getStackForm(1L));

        ItemList.Hull_ULV
            .set(new MTEBasicHull(HULL_ULV.ID, "hull.tier.00", "ULV Machine Hull", 0, imagination).getStackForm(1L));
        ItemList.Hull_LV
            .set(new MTEBasicHull(HULL_LV.ID, "hull.tier.01", "LV Machine Hull", 1, imagination).getStackForm(1L));
        ItemList.Hull_MV
            .set(new MTEBasicHull(HULL_MV.ID, "hull.tier.02", "MV Machine Hull", 2, imagination).getStackForm(1L));
        ItemList.Hull_HV
            .set(new MTEBasicHull(HULL_HV.ID, "hull.tier.03", "HV Machine Hull", 3, imagination).getStackForm(1L));
        ItemList.Hull_EV
            .set(new MTEBasicHull(HULL_EV.ID, "hull.tier.04", "EV Machine Hull", 4, imagination).getStackForm(1L));
        ItemList.Hull_IV
            .set(new MTEBasicHull(HULL_IV.ID, "hull.tier.05", "IV Machine Hull", 5, imagination).getStackForm(1L));
        ItemList.Hull_LuV
            .set(new MTEBasicHull(HULL_LuV.ID, "hull.tier.06", "LuV Machine Hull", 6, imagination).getStackForm(1L));
        ItemList.Hull_ZPM
            .set(new MTEBasicHull(HULL_ZPM.ID, "hull.tier.07", "ZPM Machine Hull", 7, imagination).getStackForm(1L));
        ItemList.Hull_UV
            .set(new MTEBasicHull(HULL_UV.ID, "hull.tier.08", "UV Machine Hull", 8, imagination).getStackForm(1L));
        ItemList.Hull_MAX
            .set(new MTEBasicHull(HULL_UHV.ID, "hull.tier.09", "UHV Machine Hull", 9, imagination).getStackForm(1L));

        ItemList.Hull_UEV.set(
            new MTEBasicHull(HULL_UEV.ID, "hull.tier.10", "UEV Machine Hull", 10, LoaderMetaTileEntities.imagination)
                .getStackForm(1L));

        ItemList.Hull_UIV.set(
            new MTEBasicHull(HULL_UIV.ID, "hull.tier.11", "UIV Machine Hull", 11, LoaderMetaTileEntities.imagination)
                .getStackForm(1L));

        ItemList.Hull_UMV.set(
            new MTEBasicHull(HULL_UMV.ID, "hull.tier.12", "UMV Machine Hull", 12, LoaderMetaTileEntities.imagination)
                .getStackForm(1L));

        ItemList.Hull_UXV.set(
            new MTEBasicHull(HULL_UXV.ID, "hull.tier.13", "UXV Machine Hull", 13, LoaderMetaTileEntities.imagination)
                .getStackForm(1L));

        ItemList.Hull_MAXV.set(
            new MTEBasicHull(HULL_MAX.ID, "hull.tier.14", "MAX Machine Hull", 14, LoaderMetaTileEntities.imagination)
                .getStackForm(1L));
    }

    private static void registerTransformer() {
        ItemList.Transformer_LV_ULV.set(
            new MTETransformer(
                transformer_LV_ULV.ID,
                "transformer.tier.00",
                "Ultra Low Voltage Transformer",
                0,
                "LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_MV_LV.set(
            new MTETransformer(
                transformer_MV_LV.ID,
                "transformer.tier.01",
                "Low Voltage Transformer",
                1,
                "MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HV_MV.set(
            new MTETransformer(
                transformer_HV_MV.ID,
                "transformer.tier.02",
                "Medium Voltage Transformer",
                2,
                "HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_EV_HV.set(
            new MTETransformer(
                transformer_EV_HV.ID,
                "transformer.tier.03",
                "High Voltage Transformer",
                3,
                "EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_IV_EV.set(
            new MTETransformer(
                transformer_IV_EV.ID,
                "transformer.tier.04",
                "Extreme Transformer",
                4,
                "IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_LuV_IV.set(
            new MTETransformer(
                transformer_LuV_IV.ID,
                "transformer.tier.05",
                "Insane Transformer",
                5,
                "LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_ZPM_LuV.set(
            new MTETransformer(
                transformer_ZPM_LuV.ID,
                "transformer.tier.06",
                "Ludicrous Transformer",
                6,
                "ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_UV_ZPM.set(
            new MTETransformer(
                transformer_UV_ZPM.ID,
                "transformer.tier.07",
                "ZPM Voltage Transformer",
                7,
                "UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_MAX_UV.set(
            new MTETransformer(
                transformer_UHV_UV.ID,
                "transformer.tier.08",
                "Ultimate Transformer",
                8,
                "UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_UEV_UHV.set(
            new MTETransformer(
                TRANSFORMER_UEV_UHV.ID,
                "transformer.tier.09",
                "Highly Ultimate Transformer",
                9,
                "UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Transformer_UIV_UEV.set(
            new MTETransformer(
                TRANSFORMER_UIV_UEV.ID,
                "transformer.tier.10",
                "Extremely Ultimate Transformer",
                10,
                "UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Transformer_UMV_UIV.set(
            new MTETransformer(
                TRANSFORMER_UMV_UIV.ID,
                "transformer.tier.11",
                "Insanely Ultimate Transformer",
                11,
                "UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Transformer_UXV_UMV.set(
            new MTETransformer(
                TRANSFORMER_UXV_UMV.ID,
                "transformer.tier.12",
                "Mega Ultimate Transformer",
                12,
                "UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Transformer_MAX_UXV.set(
            new MTETransformer(
                TRANSFORMER_MAX_UXV.ID,
                "transformer.tier.13",
                "Extended Mega Ultimate Transformer",
                13,
                "MAX -> UXV (Use Soft Mallet to invert)").getStackForm(1L));
    }

    private void registerChemicalBath() {
        ItemList.Machine_LV_ChemicalBath.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_LV.ID,
                "basicmachine.chemicalbath.tier.01",
                "Basic Chemical Bath",
                1,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.Machine_MV_ChemicalBath.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_MV.ID,
                "basicmachine.chemicalbath.tier.02",
                "Advanced Chemical Bath",
                2,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.Machine_HV_ChemicalBath.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_HV.ID,
                "basicmachine.chemicalbath.tier.03",
                "Advanced Chemical Bath II",
                3,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.Machine_EV_ChemicalBath.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_EV.ID,
                "basicmachine.chemicalbath.tier.04",
                "Advanced Chemical Bath III",
                4,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.Machine_IV_ChemicalBath.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_IV.ID,
                "basicmachine.chemicalbath.tier.05",
                "Advanced Chemical Bath IV",
                5,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                RecipeMaps.chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.ChemicalBathLuV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_LuV.ID,
                "basicmachine.chemicalbath.tier.06",
                "Elite Chemical Bath",
                6,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.ChemicalBathZPM.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_ZPM.ID,
                "basicmachine.chemicalbath.tier.07",
                "Elite Chemical Bath II",
                7,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.ChemicalBathUV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_UV.ID,
                "basicmachine.chemicalbath.tier.08",
                "Ultimate Chemical Dunktron",
                8,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.ChemicalBathUHV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_UHV.ID,
                "basicmachine.chemicalbath.tier.09",
                "Epic Chemical Dunktron",
                9,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.ChemicalBathUEV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_UEV.ID,
                "basicmachine.chemicalbath.tier.10",
                "Epic Chemical Dunktron II",
                10,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.ChemicalBathUIV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_UIV.ID,
                "basicmachine.chemicalbath.tier.11",
                "Epic Chemical Dunktron III",
                11,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));

        ItemList.ChemicalBathUMV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_BATH_UMV.ID,
                "basicmachine.chemicalbath.tier.12",
                "Epic Chemical Dunktron IV",
                12,
                MachineType.CHEMICAL_BATH.tooltipDescription(),
                chemicalBathRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_BATH").getStackForm(1L));
    }

    private void registerChemicalReactor() {
        ItemList.Machine_LV_ChemicalReactor.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_LV.ID,
                "basicmachine.chemicalreactor.tier.01",
                "Basic Chemical Reactor",
                1,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.Machine_MV_ChemicalReactor.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_MV.ID,
                "basicmachine.chemicalreactor.tier.02",
                "Advanced Chemical Reactor",
                2,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.Machine_HV_ChemicalReactor.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_HV.ID,
                "basicmachine.chemicalreactor.tier.03",
                "Advanced Chemical Reactor II",
                3,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.Machine_EV_ChemicalReactor.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_EV.ID,
                "basicmachine.chemicalreactor.tier.04",
                "Advanced Chemical Reactor III",
                4,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.Machine_IV_ChemicalReactor.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_IV.ID,
                "basicmachine.chemicalreactor.tier.05",
                "Advanced Chemical Reactor IV",
                5,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                RecipeMaps.chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.ChemicalReactorLuV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_LuV.ID,
                "basicmachine.chemicalreactor.tier.06",
                "Elite Chemical Reactor",
                6,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.ChemicalReactorZPM.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_ZPM.ID,
                "basicmachine.chemicalreactor.tier.07",
                "Elite Chemical Reactor II",
                7,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.ChemicalReactorUV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_UV.ID,
                "basicmachine.chemicalreactor.tier.08",
                "Ultimate Chemical Perforer",
                8,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.ChemicalReactorUHV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_UHV.ID,
                "basicmachine.chemicalreactor.tier.09",
                "Epic Chemical Performer",
                9,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.ChemicalReactorUEV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_UEV.ID,
                "basicmachine.chemicalreactor.tier.10",
                "Epic Chemical Performer II",
                10,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.ChemicalReactorUIV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_UIV.ID,
                "basicmachine.chemicalreactor.tier.11",
                "Epic Chemical Performer III",
                11,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

        ItemList.ChemicalReactorUMV.set(
            new MTEBasicMachineWithRecipe(
                CHEMICAL_REACTOR_UMV.ID,
                "basicmachine.chemicalreactor.tier.12",
                "Epic Chemical Performer IV",
                12,
                MachineType.CHEMICAL_REACTOR.tooltipDescription(),
                chemicalReactorRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CHEMICAL_REACTOR").getStackForm(1L));

    }

    private void registerFermenter() {
        ItemList.Machine_LV_Fermenter.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_LV.ID,
                "basicmachine.fermenter.tier.01",
                "Basic Fermenter",
                1,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.Machine_MV_Fermenter.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_MV.ID,
                "basicmachine.fermenter.tier.02",
                "Advanced Fermenter",
                2,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.Machine_HV_Fermenter.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_HV.ID,
                "basicmachine.fermenter.tier.03",
                "Advanced Fermenter II",
                3,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.Machine_EV_Fermenter.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_EV.ID,
                "basicmachine.fermenter.tier.04",
                "Advanced Fermenter III",
                4,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.Machine_IV_Fermenter.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_IV.ID,
                "basicmachine.fermenter.tier.05",
                "Advanced Fermenter IV",
                5,
                MachineType.FERMENTER.tooltipDescription(),
                RecipeMaps.fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.FermenterLuV.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_LuV.ID,
                "basicmachine.fermenter.tier.06",
                "Elite Fermenter",
                6,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.FermenterZPM.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_ZPM.ID,
                "basicmachine.fermenter.tier.07",
                "Elite Fermenter II",
                7,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.FermenterUV.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_UV.ID,
                "basicmachine.fermenter.tier.08",
                "Ultimate Fermentation Hastener",
                8,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.FermenterUHV.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_UHV.ID,
                "basicmachine.fermenter.tier.09",
                "Epic Fermentation Hastener",
                9,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.FermenterUEV.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_UEV.ID,
                "basicmachine.fermenter.tier.10",
                "Epic Fermentation Hastener II",
                10,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.FermenterUIV.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_UIV.ID,
                "basicmachine.fermenter.tier.11",
                "Epic Fermentation Hastener III",
                11,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));

        ItemList.FermenterUMV.set(
            new MTEBasicMachineWithRecipe(
                FERMENTER_UMV.ID,
                "basicmachine.fermenter.tier.12",
                "Epic Fermentation Hastener IV",
                12,
                MachineType.FERMENTER.tooltipDescription(),
                fermentingRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_CHEMICAL,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FERMENTER").getStackForm(1L));
    }

    private void registerFluidCanner() {
        ItemList.Machine_LV_FluidCanner.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_LV.ID,
                "basicmachine.fluidcanner.tier.01",
                "Basic Fluid Canner",
                1,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.Machine_MV_FluidCanner.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_MV.ID,
                "basicmachine.fluidcanner.tier.02",
                "Advanced Fluid Canner",
                2,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.Machine_HV_FluidCanner.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_HV.ID,
                "basicmachine.fluidcanner.tier.03",
                "Quick Fluid Canner",
                3,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.Machine_EV_FluidCanner.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_EV.ID,
                "basicmachine.fluidcanner.tier.04",
                "Turbo Fluid Canner",
                4,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.Machine_IV_FluidCanner.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_IV.ID,
                "basicmachine.fluidcanner.tier.05",
                "Instant Fluid Canner",
                5,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.FluidCannerLuV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_LuV.ID,
                "basicmachine.fluidcanner.tier.06",
                "Elite Fluid Canner",
                6,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.FluidCannerZPM.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_ZPM.ID,
                "basicmachine.fluidcanner.tier.07",
                "Elite Fluid Canner II",
                7,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.FluidCannerUV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_UV.ID,
                "basicmachine.fluidcanner.tier.08",
                "Ultimate Liquid Can Actuator",
                8,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.FluidCannerUHV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_UHV.ID,
                "basicmachine.fluidcanner.tier.09",
                "Epic Liquid Can Actuator",
                9,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.FluidCannerUEV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_UEV.ID,
                "basicmachine.fluidcanner.tier.10",
                "Epic Liquid Can Actuator II",
                10,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.FluidCannerUIV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_UIV.ID,
                "basicmachine.fluidcanner.tier.11",
                "Epic Liquid Can Actuator III",
                11,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));

        ItemList.FluidCannerUMV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_CANNER_UMV.ID,
                "basicmachine.fluidcanner.tier.12",
                "Epic Liquid Can Actuator IV",
                12,
                MachineType.FLUID_CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_CANNER").getStackForm(1L));
    }

    private void registerFluidExtractor() {
        ItemList.Machine_LV_FluidExtractor.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_LV.ID,
                "basicmachine.fluidextractor.tier.01",
                "Basic Fluid Extractor",
                1,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.Machine_MV_FluidExtractor.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_MV.ID,
                "basicmachine.fluidextractor.tier.02",
                "Advanced Fluid Extractor",
                2,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.Machine_HV_FluidExtractor.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_HV.ID,
                "basicmachine.fluidextractor.tier.03",
                "Advanced Fluid Extractor II",
                3,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.Machine_EV_FluidExtractor.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_EV.ID,
                "basicmachine.fluidextractor.tier.04",
                "Advanced Fluid Extractor III",
                4,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.Machine_IV_FluidExtractor.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_IV.ID,
                "basicmachine.fluidextractor.tier.05",
                "Advanced Fluid Extractor IV",
                5,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                RecipeMaps.fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.FluidExtractorLuV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_LuV.ID,
                "basicmachine.fluidextractor.tier.06",
                "Elite Fluid Extractor",
                6,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.FluidExtractorZPM.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_ZPM.ID,
                "basicmachine.fluidextractor.tier.07",
                "Elite Fluid Extractor II",
                7,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.FluidExtractorUV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_UV.ID,
                "basicmachine.fluidextractor.tier.08",
                "Ultimate Liquefying Sucker",
                8,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.FluidExtractorUHV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_UHV.ID,
                "basicmachine.fluidextractor.tier.09",
                "Epic Liquefying Sucker",
                9,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.FluidExtractorUEV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_UEV.ID,
                "basicmachine.fluidextractor.tier.10",
                "Epic Liquefying Sucker II",
                10,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.FluidExtractorUIV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_UIV.ID,
                "basicmachine.fluidextractor.tier.11",
                "Epic Liquefying Sucker III",
                11,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));

        ItemList.FluidExtractorUMV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_EXTRACTOR_UMV.ID,
                "basicmachine.fluidextractor.tier.12",
                "Epic Liquefying Sucker IV",
                12,
                MachineType.FLUID_EXTRACTOR.tooltipDescription(),
                fluidExtractionRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_EXTRACTOR").getStackForm(1L));
    }

    private void registerFluidHeater() {
        ItemList.Machine_LV_FluidHeater.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_LV.ID,
                "basicmachine.fluidheater.tier.01",
                "Basic Fluid Heater",
                1,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.Machine_MV_FluidHeater.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_MV.ID,
                "basicmachine.fluidheater.tier.02",
                "Advanced Fluid Heater",
                2,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.Machine_HV_FluidHeater.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_HV.ID,
                "basicmachine.fluidheater.tier.03",
                "Advanced Fluid Heater II",
                3,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.Machine_EV_FluidHeater.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_EV.ID,
                "basicmachine.fluidheater.tier.04",
                "Advanced Fluid Heater III",
                4,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.Machine_IV_FluidHeater.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_IV.ID,
                "basicmachine.fluidheater.tier.05",
                "Advanced Fluid Heater IV",
                5,
                MachineType.FLUID_HEATER.tooltipDescription(),
                RecipeMaps.fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.FluidHeaterLuV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_LuV.ID,
                "basicmachine.fluidheater.tier.06",
                "Elite Fluid Heater",
                6,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.FluidHeaterZPM.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_ZPM.ID,
                "basicmachine.fluidheater.tier.07",
                "Elite Fluid Heater II",
                7,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.FluidHeaterUV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_UV.ID,
                "basicmachine.fluidheater.tier.08",
                "Ultimate Heat Infuser",
                8,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.FluidHeaterUHV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_UHV.ID,
                "basicmachine.fluidheater.tier.09",
                "Epic Heat Infuser",
                9,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.FluidHeaterUEV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_UEV.ID,
                "basicmachine.fluidheater.tier.10",
                "Epic Heat Infuser II",
                10,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.FluidHeaterUIV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_UIV.ID,
                "basicmachine.fluidheater.tier.11",
                "Epic Heat Infuser III",
                11,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));

        ItemList.FluidHeaterUMV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_HEATER_UMV.ID,
                "basicmachine.fluidheater.tier.12",
                "Epic Heat Infuser IV",
                12,
                MachineType.FLUID_HEATER.tooltipDescription(),
                fluidHeaterRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_BOILER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_HEATER").getStackForm(1L));
    }

    private void registerMixer() {
        ItemList.Machine_LV_Mixer.set(
            new MTEBasicMachineWithRecipe(
                MIXER_LV.ID,
                "basicmachine.mixer.tier.01",
                "Basic Mixer",
                1,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.Machine_MV_Mixer.set(
            new MTEBasicMachineWithRecipe(
                MIXER_MV.ID,
                "basicmachine.mixer.tier.02",
                "Advanced Mixer",
                2,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.Machine_HV_Mixer.set(
            new MTEBasicMachineWithRecipe(
                MIXER_HV.ID,
                "basicmachine.mixer.tier.03",
                "Advanced Mixer II",
                3,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                6,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.Machine_EV_Mixer.set(
            new MTEBasicMachineWithRecipe(
                MIXER_EV.ID,
                "basicmachine.mixer.tier.04",
                "Advanced Mixer III",
                4,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.Machine_IV_Mixer.set(
            new MTEBasicMachineWithRecipe(
                MIXER_IV.ID,
                "basicmachine.mixer.tier.05",
                "Advanced Mixer IV",
                5,
                MachineType.MIXER.tooltipDescription(),
                RecipeMaps.mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.MixerLuV.set(
            new MTEBasicMachineWithRecipe(
                MIXER_LuV.ID,
                "basicmachine.mixer.tier.06",
                "Elite Mixer",
                6,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.MixerZPM.set(
            new MTEBasicMachineWithRecipe(
                MIXER_ZPM.ID,
                "basicmachine.mixer.tier.07",
                "Elite Mixer II",
                7,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.MixerUV.set(
            new MTEBasicMachineWithRecipe(
                MIXER_UV.ID,
                "basicmachine.mixer.tier.08",
                "Ultimate Matter Organizer",
                8,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.MixerUHV.set(
            new MTEBasicMachineWithRecipe(
                MIXER_UHV.ID,
                "basicmachine.mixer.tier.09",
                "Epic Matter Organizer",
                9,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.MixerUEV.set(
            new MTEBasicMachineWithRecipe(
                MIXER_UEV.ID,
                "basicmachine.mixer.tier.10",
                "Epic Matter Organizer II",
                10,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.MixerUIV.set(
            new MTEBasicMachineWithRecipe(
                MIXER_UIV.ID,
                "basicmachine.mixer.tier.11",
                "Epic Matter Organizer III",
                11,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));

        ItemList.MixerUMV.set(
            new MTEBasicMachineWithRecipe(
                MIXER_UMV.ID,
                "basicmachine.mixer.tier.12",
                "Epic Matter Organizer IV",
                12,
                MachineType.MIXER.tooltipDescription(),
                mixerRecipes,
                9,
                4,
                true,
                SoundResource.GTCEU_LOOP_MIXER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MIXER").getStackForm(1L));
    }

    private void registerAutoclave() {
        ItemList.Machine_LV_Autoclave.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_LV.ID,
                "basicmachine.autoclave.tier.01",
                "Basic Autoclave",
                1,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.Machine_MV_Autoclave.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_MV.ID,
                "basicmachine.autoclave.tier.02",
                "Advanced Autoclave",
                2,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.Machine_HV_Autoclave.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_HV.ID,
                "basicmachine.autoclave.tier.03",
                "Advanced Autoclave II",
                3,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                3,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.Machine_EV_Autoclave.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_EV.ID,
                "basicmachine.autoclave.tier.04",
                "Advanced Autoclave III",
                4,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.Machine_IV_Autoclave.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_IV.ID,
                "basicmachine.autoclave.tier.05",
                "Advanced Autoclave IV",
                5,
                MachineType.AUTOCLAVE.tooltipDescription(),
                RecipeMaps.autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.AutoclaveLuV.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_LuV.ID,
                "basicmachine.autoclave.tier.06",
                "Elite Autoclave",
                6,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.AutoclaveZPM.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_ZPM.ID,
                "basicmachine.autoclave.tier.07",
                "Elite Autoclave II",
                7,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.AutoclaveUV.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_UV.ID,
                "basicmachine.autoclave.tier.08",
                "Ultimate Pressure Cooker",
                8,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.AutoclaveUHV.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_UHV.ID,
                "basicmachine.autoclave.tier.09",
                "Epic Pressure Cooker",
                9,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.AutoclaveUEV.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_UEV.ID,
                "basicmachine.autoclave.tier.10",
                "Epic Pressure Cooker II",
                10,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.AutoclaveUIV.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_UIV.ID,
                "basicmachine.autoclave.tier.11",
                "Epic Pressure Cooker III",
                11,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

        ItemList.AutoclaveUMV.set(
            new MTEBasicMachineWithRecipe(
                AUTOCLAVE_UMV.ID,
                "basicmachine.autoclave.tier.12",
                "Epic Pressure Cooker IV",
                12,
                MachineType.AUTOCLAVE.tooltipDescription(),
                autoclaveRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "AUTOCLAVE").getStackForm(1L));

    }

    private void registerBendingMachine() {
        ItemList.Machine_LV_Bender.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_LV.ID,
                "basicmachine.bender.tier.01",
                "Basic Bending Machine",
                1,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.Machine_MV_Bender.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_MV.ID,
                "basicmachine.bender.tier.02",
                "Advanced Bending Machine",
                2,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.Machine_HV_Bender.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_HV.ID,
                "basicmachine.bender.tier.03",
                "Advanced Bending Machine II",
                3,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.Machine_EV_Bender.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_EV.ID,
                "basicmachine.bender.tier.04",
                "Advanced Bending Machine III",
                4,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.Machine_IV_Bender.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_IV.ID,
                "basicmachine.bender.tier.05",
                "Advanced Bending Machine IV",
                5,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                RecipeMaps.benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.BendingMachineLuV.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_LuV.ID,
                "basicmachine.bender.tier.06",
                "Elite Bending Machine",
                6,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.BendingMachineZPM.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_ZPM.ID,
                "basicmachine.bender.tier.07",
                "Elite Bending Machine II",
                7,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.BendingMachineUV.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_UV.ID,
                "basicmachine.bender.tier.08",
                "Ultimate Bending Unit",
                8,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.BendingMachineUHV.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_UHV.ID,
                "basicmachine.bender.tier.09",
                "Epic Bending Unit",
                9,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.BendingMachineUEV.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_UEV.ID,
                "basicmachine.bender.tier.10",
                "Epic Bending Unit II",
                10,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.BendingMachineUIV.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_UIV.ID,
                "basicmachine.bender.tier.11",
                "Epic Bending Unit III",
                11,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));

        ItemList.BendingMachineUMV.set(
            new MTEBasicMachineWithRecipe(
                BENDING_MACHINE_UMV.ID,
                "basicmachine.bender.tier.12",
                "Epic Bending Unit IV",
                12,
                MachineType.BENDING_MACHINE.tooltipDescription(),
                benderRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "BENDER").getStackForm(1L));
    }

    private void registerCompressor() {
        ItemList.Machine_LV_Compressor.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_LV.ID,
                "basicmachine.compressor.tier.01",
                "Basic Compressor",
                1,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.Machine_MV_Compressor.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_MV.ID,
                "basicmachine.compressor.tier.02",
                "Advanced Compressor",
                2,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.Machine_HV_Compressor.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_HV.ID,
                "basicmachine.compressor.tier.03",
                "Advanced Compressor II",
                3,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.Machine_EV_Compressor.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_EV.ID,
                "basicmachine.compressor.tier.04",
                "Advanced Compressor III",
                4,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.Machine_IV_Compressor.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_IV.ID,
                "basicmachine.compressor.tier.05",
                "Singularity Compressor",
                5,
                MachineType.COMPRESSOR.tooltipDescription(),
                RecipeMaps.compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.CompressorLuV.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_LuV.ID,
                "basicmachine.compressor.tier.06",
                "Elite Compressor",
                6,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.CompressorZPM.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_ZPM.ID,
                "basicmachine.compressor.tier.07",
                "Elite Compressor II",
                7,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.CompressorUV.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_UV.ID,
                "basicmachine.compressor.tier.08",
                "Ultimate Matter Constrictor",
                8,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.CompressorUHV.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_UHV.ID,
                "basicmachine.compressor.tier.09",
                "Epic Matter Constrictor",
                9,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.CompressorUEV.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_UEV.ID,
                "basicmachine.compressor.tier.10",
                "Epic Matter Constrictor II",
                10,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.CompressorUIV.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_UIV.ID,
                "basicmachine.compressor.tier.11",
                "Epic Matter Constrictor III",
                11,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

        ItemList.CompressorUMV.set(
            new MTEBasicMachineWithRecipe(
                COMPRESSOR_UMV.ID,
                "basicmachine.compressor.tier.12",
                "Epic Matter Constrictor IV",
                12,
                MachineType.COMPRESSOR.tooltipDescription(),
                compressorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_COMPRESSOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "COMPRESSOR").getStackForm(1L));

    }

    private void registerCuttingMachine() {
        ItemList.Machine_LV_Cutter.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_LV.ID,
                "basicmachine.cutter.tier.01",
                "Basic Cutting Machine",
                1,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.Machine_MV_Cutter.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_MV.ID,
                "basicmachine.cutter.tier.02",
                "Advanced Cutting Machine",
                2,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.Machine_HV_Cutter.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_HV.ID,
                "basicmachine.cutter.tier.03",
                "Advanced Cutting Machine II",
                3,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.Machine_EV_Cutter.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_EV.ID,
                "basicmachine.cutter.tier.04",
                "Advanced Cutting Machine III",
                4,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.Machine_IV_Cutter.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_IV.ID,
                "basicmachine.cutter.tier.05",
                "Advanced Cutting Machine IV",
                5,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                RecipeMaps.cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.CuttingMachineLuV.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_LuV.ID,
                "basicmachine.cutter.tier.06",
                "Elite Cutting Machine",
                6,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.CuttingMachineZPM.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_ZPM.ID,
                "basicmachine.cutter.tier.07",
                "Elite Cutting Machine II",
                7,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.CuttingMachineUV.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_UV.ID,
                "basicmachine.cutter.tier.08",
                "Ultimate Object Divider",
                8,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.CuttingMachineUHV.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_UHV.ID,
                "basicmachine.cutter.tier.09",
                "Epic Object Divider",
                9,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.CuttingMachineUEV.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_UEV.ID,
                "basicmachine.cutter.tier.10",
                "Epic Object Divider II",
                10,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.CuttingMachineUIV.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_UIV.ID,
                "basicmachine.cutter.tier.11",
                "Epic Object Divider III",
                11,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

        ItemList.CuttingMachineUMV.set(
            new MTEBasicMachineWithRecipe(
                CUTTING_MACHINE_UMV.ID,
                "basicmachine.cutter.tier.12",
                "Epic Object Divider IV",
                12,
                MachineType.CUTTING_MACHINE.tooltipDescription(),
                cutterRecipes,
                2,
                4,
                true,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CUTTER").getStackForm(1L));

    }

    private void registerDistillery() {
        ItemList.Machine_LV_Distillery.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_LV.ID,
                "basicmachine.distillery.tier.01",
                "Basic Distillery",
                1,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.Machine_MV_Distillery.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_MV.ID,
                "basicmachine.distillery.tier.02",
                "Advanced Distillery",
                2,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.Machine_HV_Distillery.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_HV.ID,
                "basicmachine.distillery.tier.03",
                "Advanced Distillery II",
                3,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.Machine_EV_Distillery.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_EV.ID,
                "basicmachine.distillery.tier.04",
                "Advanced Distillery III",
                4,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.Machine_IV_Distillery.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_IV.ID,
                "basicmachine.distillery.tier.05",
                "Advanced Distillery IV",
                5,
                MachineType.DISTILLERY.tooltipDescription(),
                RecipeMaps.distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.DistilleryLuV.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_LuV.ID,
                "basicmachine.distillery.tier.06",
                "Elite Distillery",
                6,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.DistilleryZPM.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_ZPM.ID,
                "basicmachine.distillery.tier.07",
                "Elite Distillery II",
                7,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.DistilleryUV.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_UV.ID,
                "basicmachine.distillery.tier.08",
                "Ultimate Fraction Splitter",
                8,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.DistilleryUHV.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_UHV.ID,
                "basicmachine.distillery.tier.09",
                "Epic Fraction Splitter",
                9,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.DistilleryUEV.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_UEV.ID,
                "basicmachine.distillery.tier.10",
                "Epic Fraction Splitter II",
                10,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.DistilleryUIV.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_UIV.ID,
                "basicmachine.distillery.tier.11",
                "Epic Fraction Splitter III",
                11,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

        ItemList.DistilleryUMV.set(
            new MTEBasicMachineWithRecipe(
                DISTILLERY_UMV.ID,
                "basicmachine.distillery.tier.12",
                "Epic Fraction Splitter IV",
                12,
                MachineType.DISTILLERY.tooltipDescription(),
                distilleryRecipes,
                1,
                1,
                true,
                SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "DISTILLERY").getStackForm(1L));

    }

    private void registerElectricFurnace() {
        ItemList.Machine_LV_E_Furnace.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_LV.ID,
                "basicmachine.e_furnace.tier.01",
                "Basic Electric Furnace",
                1,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.Machine_MV_E_Furnace.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_MV.ID,
                "basicmachine.e_furnace.tier.02",
                "Advanced Electric Furnace",
                2,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.Machine_HV_E_Furnace.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_HV.ID,
                "basicmachine.e_furnace.tier.03",
                "Advanced Electric Furnace II",
                3,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.Machine_EV_E_Furnace.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_EV.ID,
                "basicmachine.e_furnace.tier.04",
                "Advanced Electric Furnace III",
                4,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.Machine_IV_E_Furnace.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_IV.ID,
                "basicmachine.e_furnace.tier.05",
                "Electron Excitement Processor",
                5,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").setProgressBarTextureName("E_Furnace")
                    .getStackForm(1L));

        ItemList.ElectricFurnaceLuV.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_LuV.ID,
                "basicmachine.e_furnace.tier.06",
                "Elite Electric Furnace",
                6,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").getStackForm(1L));

        ItemList.ElectricFurnaceZPM.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_ZPM.ID,
                "basicmachine.e_furnace.tier.07",
                "Elite Electric Furnace II",
                7,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").getStackForm(1L));

        ItemList.ElectricFurnaceUV.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_UV.ID,
                "basicmachine.e_furnace.tier.08",
                "Ultimate Atom Stimulator",
                8,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").getStackForm(1L));

        ItemList.ElectricFurnaceUHV.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_UHV.ID,
                "basicmachine.e_furnace.tier.09",
                "Epic Atom Stimulator",
                9,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").getStackForm(1L));

        ItemList.ElectricFurnaceUEV.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_UEV.ID,
                "basicmachine.e_furnace.tier.10",
                "Epic Atom Stimulator II",
                10,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").getStackForm(1L));

        ItemList.ElectricFurnaceUIV.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_UIV.ID,
                "basicmachine.e_furnace.tier.11",
                "Epic Atom Stimulator III",
                11,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").getStackForm(1L));

        ItemList.ElectricFurnaceUMV.set(
            new MTEBasicMachineWithRecipe(
                ELECTRIC_FURNACE_UMV.ID,
                "basicmachine.e_furnace.tier.12",
                "Epic Atom Stimulator IV",
                12,
                MachineType.ELECTRIC_FURNACE.tooltipDescription(),
                furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_FURNACE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_FURNACE").getStackForm(1L));
    }

    private void registerElectrolyzer() {
        ItemList.Machine_LV_Electrolyzer.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYSER_LV.ID,
                "basicmachine.electrolyzer.tier.01",
                "Basic Electrolyzer",
                1,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.Machine_MV_Electrolyzer.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYSER_MV.ID,
                "basicmachine.electrolyzer.tier.02",
                "Advanced Electrolyzer",
                2,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.Machine_HV_Electrolyzer.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYSER_HV.ID,
                "basicmachine.electrolyzer.tier.03",
                "Advanced Electrolyzer II",
                3,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.Machine_EV_Electrolyzer.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYSER_EV.ID,
                "basicmachine.electrolyzer.tier.04",
                "Advanced Electrolyzer III",
                4,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.Machine_IV_Electrolyzer.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYSER_IV.ID,
                "basicmachine.electrolyzer.tier.05",
                "Molecular Disintegrator E-4908",
                5,
                MachineType.ELECTROLYZER.tooltipDescription(),
                RecipeMaps.electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.ElectrolyzerLuV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYZER_LuV.ID,
                "basicmachine.electrolyzer.tier.06",
                "Elite Electrolyzer",
                6,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.ElectrolyzerZPM.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYZER_ZPM.ID,
                "basicmachine.electrolyzer.tier.07",
                "Elite Electrolyzer II",
                7,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.ElectrolyzerUV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYZER_UV.ID,
                "basicmachine.electrolyzer.tier.08",
                "Ultimate Ionizer",
                8,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.ElectrolyzerUHV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYZER_UHV.ID,
                "basicmachine.electrolyzer.tier.09",
                "Epic Ionizer",
                9,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.ElectrolyzerUEV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYZER_UEV.ID,
                "basicmachine.electrolyzer.tier.10",
                "Epic Ionizer II",
                10,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.ElectrolyzerUIV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYZER_UIV.ID,
                "basicmachine.electrolyzer.tier.11",
                "Epic Ionizer III",
                11,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

        ItemList.ElectrolyzerUMV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROLYZER_UMV.ID,
                "basicmachine.electrolyzer.tier.12",
                "Epic Ionizer IV",
                12,
                MachineType.ELECTROLYZER.tooltipDescription(),
                electrolyzerRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROLYZER").getStackForm(1L));

    }

    private void registerElectromagneticSeparator() {
        ItemList.Machine_LV_ElectromagneticSeparator.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_LV.ID,
                "basicmachine.electromagneticseparator.tier.01",
                "Basic Electromagnetic Separator",
                1,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.Machine_MV_ElectromagneticSeparator.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_MV.ID,
                "basicmachine.electromagneticseparator.tier.02",
                "Advanced Electromagnetic Separator",
                2,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.Machine_HV_ElectromagneticSeparator.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_HV.ID,
                "basicmachine.electromagneticseparator.tier.03",
                "Advanced Electromagnetic Separator II",
                3,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.Machine_EV_ElectromagneticSeparator.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_EV.ID,
                "basicmachine.electromagneticseparator.tier.04",
                "Advanced Electromagnetic Separator III",
                4,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.Machine_IV_ElectromagneticSeparator.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_IV.ID,
                "basicmachine.electromagneticseparator.tier.05",
                "Advanced Electromagnetic Separator IV",
                5,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                RecipeMaps.electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.ElectromagneticSeparatorLuV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_LuV.ID,
                "basicmachine.electromagneticseparator.tier.06",
                "Elite Electromagnetic Separator",
                6,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.ElectromagneticSeparatorZPM.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_ZPM.ID,
                "basicmachine.electromagneticseparator.tier.07",
                "Elite Electromagnetic Separator II",
                7,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.ElectromagneticSeparatorUV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_UV.ID,
                "basicmachine.electromagneticseparator.tier.08",
                "Ultimate Magnetar Separator",
                8,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.ElectromagneticSeparatorUHV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_UHV.ID,
                "basicmachine.electromagneticseparator.tier.09",
                "Epic Magnetar Separator",
                9,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.ElectromagneticSeparatorUEV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_UEV.ID,
                "basicmachine.electromagneticseparator.tier.10",
                "Epic Magnetar Separator II",
                10,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.ElectromagneticSeparatorUIV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_UIV.ID,
                "basicmachine.electromagneticseparator.tier.11",
                "Epic Magnetar Separator III",
                11,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

        ItemList.ElectromagneticSeparatorUMV.set(
            new MTEBasicMachineWithRecipe(
                ELECTROMAGNETIC_SEPARATOR_UMV.ID,
                "basicmachine.electromagneticseparator.tier.12",
                "Epic Magnetar Separator IV",
                12,
                MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription(),
                electroMagneticSeparatorRecipes,
                1,
                3,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTROMAGNETIC_SEPARATOR").getStackForm(1L));

    }

    private void registerExtractor() {
        ItemList.Machine_LV_Extractor.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_LV.ID,
                "basicmachine.extractor.tier.01",
                "Basic Extractor",
                1,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.Machine_MV_Extractor.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_MV.ID,
                "basicmachine.extractor.tier.02",
                "Advanced Extractor",
                2,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.Machine_HV_Extractor.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_HV.ID,
                "basicmachine.extractor.tier.03",
                "Advanced Extractor II",
                3,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.Machine_EV_Extractor.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_EV.ID,
                "basicmachine.extractor.tier.04",
                "Advanced Extractor III",
                4,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.Machine_IV_Extractor.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_IV.ID,
                "basicmachine.extractor.tier.05",
                "Vacuum Extractor",
                5,
                MachineType.EXTRACTOR.tooltipDescription(),
                RecipeMaps.extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.ExtractorLuV.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_LuV.ID,
                "basicmachine.extractor.tier.06",
                "Elite Extractor",
                6,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.ExtractorZPM.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_ZPM.ID,
                "basicmachine.extractor.tier.07",
                "Elite Extractor II",
                7,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.ExtractorUV.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_UV.ID,
                "basicmachine.extractor.tier.08",
                "Ultimate Extractinator",
                8,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.ExtractorUHV.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_UHV.ID,
                "basicmachine.extractor.tier.09",
                "Epic Extractinator",
                9,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.ExtractorUEV.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_UEV.ID,
                "basicmachine.extractor.tier.10",
                "Epic Extractinator II",
                10,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.ExtractorUIV.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_UIV.ID,
                "basicmachine.extractor.tier.11",
                "Epic Extractinator III",
                11,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

        ItemList.ExtractorUMV.set(
            new MTEBasicMachineWithRecipe(
                EXTRACTOR_UMV.ID,
                "basicmachine.extractor.tier.12",
                "Epic Extractinator IV",
                12,
                MachineType.EXTRACTOR.tooltipDescription(),
                extractorRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRACTOR").getStackForm(1L));

    }

    private void registerExtruder() {
        ItemList.Machine_LV_Extruder.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_LV.ID,
                "basicmachine.extruder.tier.01",
                "Basic Extruder",
                1,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.Machine_MV_Extruder.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_MV.ID,
                "basicmachine.extruder.tier.02",
                "Advanced Extruder",
                2,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.Machine_HV_Extruder.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_HV.ID,
                "basicmachine.extruder.tier.03",
                "Advanced Extruder II",
                3,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.Machine_EV_Extruder.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_EV.ID,
                "basicmachine.extruder.tier.04",
                "Advanced Extruder III",
                4,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.Machine_IV_Extruder.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_IV.ID,
                "basicmachine.extruder.tier.05",
                "Advanced Extruder IV",
                5,
                MachineType.EXTRUDER.tooltipDescription(),
                RecipeMaps.extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.ExtruderLuV.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_LuV.ID,
                "basicmachine.extruder.tier.06",
                "Elite Extruder",
                6,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.ExtruderZPM.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_ZPM.ID,
                "basicmachine.extruder.tier.07",
                "Elite Extruder II",
                7,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.ExtruderUV.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_UV.ID,
                "basicmachine.extruder.tier.08",
                "Ultimate Shape Driver",
                8,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.ExtruderUHV.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_UHV.ID,
                "basicmachine.extruder.tier.09",
                "Epic Shape Driver",
                9,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.ExtruderUEV.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_UEV.ID,
                "basicmachine.extruder.tier.10",
                "Epic Shape Driver II",
                10,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.ExtruderUIV.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_UIV.ID,
                "basicmachine.extruder.tier.11",
                "Epic Shape Driver III",
                11,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

        ItemList.ExtruderUMV.set(
            new MTEBasicMachineWithRecipe(
                EXTRUDER_UMV.ID,
                "basicmachine.extruder.tier.12",
                "Epic Shape Driver IV",
                12,
                MachineType.EXTRUDER.tooltipDescription(),
                extruderRecipes,
                2,
                1,
                false,
                SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "EXTRUDER").getStackForm(1L));

    }

    private void registerFluidSolidifier() {
        ItemList.Machine_LV_FluidSolidifier.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_LV.ID,
                "basicmachine.fluidsolidifier.tier.01",
                "Basic Fluid Solidifier",
                1,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.Machine_MV_FluidSolidifier.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_MV.ID,
                "basicmachine.fluidsolidifier.tier.02",
                "Advanced Fluid Solidifier",
                2,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.Machine_HV_FluidSolidifier.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_HV.ID,
                "basicmachine.fluidsolidifier.tier.03",
                "Advanced Fluid Solidifier II",
                3,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.Machine_EV_FluidSolidifier.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_EV.ID,
                "basicmachine.fluidsolidifier.tier.04",
                "Advanced Fluid Solidifier III",
                4,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.Machine_IV_FluidSolidifier.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_IV.ID,
                "basicmachine.fluidsolidifier.tier.05",
                "Advanced Fluid Solidifier IV",
                5,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                RecipeMaps.fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.FluidSolidifierLuV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_LuV.ID,
                "basicmachine.fluidsolidifier.tier.06",
                "Elite Fluid Solidifier",
                6,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.FluidSolidifierZPM.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_ZPM.ID,
                "basicmachine.fluidsolidifier.tier.07",
                "Elite Fluid Solidifier II",
                7,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.FluidSolidifierUV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_UV.ID,
                "basicmachine.fluidsolidifier.tier.08",
                "Ultimate Fluid Petrificator",
                8,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.FluidSolidifierUHV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_UHV.ID,
                "basicmachine.fluidsolidifier.tier.09",
                "Epic Fluid Petrificator",
                9,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.FluidSolidifierUEV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_UEV.ID,
                "basicmachine.fluidsolidifier.tier.10",
                "Epic Fluid Petrificator II",
                10,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.FluidSolidifierUIV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_UIV.ID,
                "basicmachine.fluidsolidifier.tier.11",
                "Epic Fluid Petrificator III",
                11,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

        ItemList.FluidSolidifierUMV.set(
            new MTEBasicMachineWithRecipe(
                FLUID_SOLIDIFIER_UMV.ID,
                "basicmachine.fluidsolidifier.tier.12",
                "Epic Fluid Petrificator IV",
                12,
                MachineType.FLUID_SOLIDIFIER.tooltipDescription(),
                fluidSolidifierRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_COOLING,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "FLUID_SOLIDIFIER").getStackForm(1L));

    }

    private void registerFormingPress() {
        ItemList.Machine_LV_Press.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_LV.ID,
                "basicmachine.press.tier.01",
                "Basic Forming Press",
                1,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                2,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.Machine_MV_Press.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_MV.ID,
                "basicmachine.press.tier.02",
                "Advanced Forming Press",
                2,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                2,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.Machine_HV_Press.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_HV.ID,
                "basicmachine.press.tier.03",
                "Advanced Forming Press II",
                3,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.Machine_EV_Press.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_EV.ID,
                "basicmachine.press.tier.04",
                "Advanced Forming Press III",
                4,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.Machine_IV_Press.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_IV.ID,
                "basicmachine.press.tier.05",
                "Advanced Forming Press IV",
                5,
                MachineType.FORMING_PRESS.tooltipDescription(),
                RecipeMaps.formingPressRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.FormingPressLuV.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_LuV.ID,
                "basicmachine.press.tier.06",
                "Elite Forming Press",
                6,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.FormingPressZPM.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_ZPM.ID,
                "basicmachine.press.tier.07",
                "Elite Forming Press II",
                7,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.FormingPressUV.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_UV.ID,
                "basicmachine.press.tier.08",
                "Ultimate Surface Shifter",
                8,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.FormingPressUHV.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_UHV.ID,
                "basicmachine.press.tier.09",
                "Epic Surface Shifter",
                9,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.FormingPressUEV.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_UEV.ID,
                "basicmachine.press.tier.10",
                "Epic Surface Shifter II",
                10,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.FormingPressUIV.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_UIV.ID,
                "basicmachine.press.tier.11",
                "Epic Surface Shifter III",
                11,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

        ItemList.FormingPressUMV.set(
            new MTEBasicMachineWithRecipe(
                FORMING_PRESS_UMV.ID,
                "basicmachine.press.tier.12",
                "Epic Surface Shifter IV",
                12,
                MachineType.FORMING_PRESS.tooltipDescription(),
                formingPressRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PRESS").getStackForm(1L));

    }

    private void registerForgeHammer() {
        ItemList.Machine_LV_Hammer.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_LV.ID,
                "basicmachine.hammer.tier.01",
                "Basic Forge Hammer",
                1,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER").getStackForm(1L));

        ItemList.Machine_MV_Hammer.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_MV.ID,
                "basicmachine.hammer.tier.02",
                "Advanced Forge Hammer",
                2,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER").getStackForm(1L));

        ItemList.Machine_HV_Hammer.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_HV.ID,
                "basicmachine.hammer.tier.03",
                "Advanced Forge Hammer II",
                3,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER").getStackForm(1L));

        ItemList.Machine_EV_Hammer.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_EV.ID,
                "basicmachine.hammer.tier.04",
                "Advanced Forge Hammer III",
                4,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER").getStackForm(1L));

        ItemList.Machine_IV_Hammer.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_IV.ID,
                "basicmachine.hammer.tier.05",
                "Advanced Forge Hammer IV",
                5,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                RecipeMaps.hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                "HAMMER").getStackForm(1L));

        ItemList.ForgeHammerLuV.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_LuV.ID,
                "basicmachine.hammer.tier.06",
                "Elite Forge Hammer",
                6,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "HAMMER").getStackForm(1L));

        ItemList.ForgeHammerZPM.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_ZPM.ID,
                "basicmachine.hammer.tier.07",
                "Elite Forge Hammer II",
                7,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "HAMMER").getStackForm(1L));

        ItemList.ForgeHammerUV.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_UV.ID,
                "basicmachine.hammer.tier.08",
                "Ultimate Impact Modulator",
                8,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "HAMMER").getStackForm(1L));

        ItemList.ForgeHammerUHV.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_UHV.ID,
                "basicmachine.hammer.tier.09",
                "Epic Impact Modulator",
                9,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "HAMMER").getStackForm(1L));

        ItemList.ForgeHammerUEV.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_UEV.ID,
                "basicmachine.hammer.tier.10",
                "Epic Impact Modulator II",
                10,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "HAMMER").getStackForm(1L));

        ItemList.ForgeHammerUIV.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_UIV.ID,
                "basicmachine.hammer.tier.11",
                "Epic Impact Modulator III",
                11,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "HAMMER").getStackForm(1L));

        ItemList.ForgeHammerUMV.set(
            new MTEBasicMachineWithRecipe(
                FORGE_HAMMER_UMV.ID,
                "basicmachine.hammer.tier.12",
                "Epic Impact Modulator IV",
                12,
                MachineType.FORGE_HAMMER.tooltipDescription(),
                hammerRecipes,
                1,
                1,
                true,
                SoundResource.GTCEU_LOOP_FORGE_HAMMER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "HAMMER").getStackForm(1L));

    }

    private void registerLathe() {
        ItemList.Machine_LV_Lathe.set(
            new MTEBasicMachineWithRecipe(
                LATHE_LV.ID,
                "basicmachine.lathe.tier.01",
                "Basic Lathe",
                1,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.Machine_MV_Lathe.set(
            new MTEBasicMachineWithRecipe(
                LATHE_MV.ID,
                "basicmachine.lathe.tier.02",
                "Advanced Lathe",
                2,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.Machine_HV_Lathe.set(
            new MTEBasicMachineWithRecipe(
                LATHE_HV.ID,
                "basicmachine.lathe.tier.03",
                "Advanced Lathe II",
                3,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.Machine_EV_Lathe.set(
            new MTEBasicMachineWithRecipe(
                LATHE_EV.ID,
                "basicmachine.lathe.tier.04",
                "Advanced Lathe III",
                4,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.Machine_IV_Lathe.set(
            new MTEBasicMachineWithRecipe(
                LATHE_IV.ID,
                "basicmachine.lathe.tier.05",
                "Advanced Lathe IV",
                5,
                MachineType.LATHE.tooltipDescription(),
                RecipeMaps.latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.LatheLuV.set(
            new MTEBasicMachineWithRecipe(
                LATHE_LuV.ID,
                "basicmachine.lathe.tier.06",
                "Elite Lathe",
                6,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.LatheZPM.set(
            new MTEBasicMachineWithRecipe(
                LATHE_ZPM.ID,
                "basicmachine.lathe.tier.07",
                "Elite Lathe II",
                7,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.LatheUV.set(
            new MTEBasicMachineWithRecipe(
                LATHE_UV.ID,
                "basicmachine.lathe.tier.08",
                "Ultimate Turn-O-Matic",
                8,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.LatheUHV.set(
            new MTEBasicMachineWithRecipe(
                LATHE_UHV.ID,
                "basicmachine.lathe.tier.09",
                "Epic Turn-O-Matic",
                9,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.LatheUEV.set(
            new MTEBasicMachineWithRecipe(
                LATHE_UEV.ID,
                "basicmachine.lathe.tier.10",
                "Epic Turn-O-Matic II",
                10,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.LatheUIV.set(
            new MTEBasicMachineWithRecipe(
                LATHE_UIV.ID,
                "basicmachine.lathe.tier.11",
                "Epic Turn-O-Matic III",
                11,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

        ItemList.LatheUMV.set(
            new MTEBasicMachineWithRecipe(
                LATHE_UMV.ID,
                "basicmachine.lathe.tier.12",
                "Epic Turn-O-Matic IV",
                12,
                MachineType.LATHE.tooltipDescription(),
                latheRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_CUT,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LATHE").getStackForm(1L));

    }

    private void registerPrecisionLaserEngraver() {
        ItemList.Machine_LV_LaserEngraver.set(
            new MTEBasicMachineWithRecipe(
                LASER_ENGRAVER_LV.ID,
                "basicmachine.laserengraver.tier.01",
                "Basic Precision Laser Engraver",
                1,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                2,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.Machine_MV_LaserEngraver.set(
            new MTEBasicMachineWithRecipe(
                LASER_ENGRAVER_MV.ID,
                "basicmachine.laserengraver.tier.02",
                "Advanced Precision Laser Engraver",
                2,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                2,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.Machine_HV_LaserEngraver.set(
            new MTEBasicMachineWithRecipe(
                LASER_ENGRAVER_HV.ID,
                "basicmachine.laserengraver.tier.03",
                "Advanced Precision Laser Engraver II",
                3,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                2,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.Machine_EV_LaserEngraver.set(
            new MTEBasicMachineWithRecipe(
                LASER_ENGRAVER_EV.ID,
                "basicmachine.laserengraver.tier.04",
                "Advanced Precision Laser Engraver III",
                4,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.Machine_IV_LaserEngraver.set(
            new MTEBasicMachineWithRecipe(
                LASER_ENGRAVER_IV.ID,
                "basicmachine.laserengraver.tier.05",
                "Advanced Precision Laser Engraver IV",
                5,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                RecipeMaps.laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.PrecisionLaserEngraverLuV.set(
            new MTEBasicMachineWithRecipe(
                PRECISION_LASER_ENGRAVER_LuV.ID,
                "basicmachine.laserengraver.tier.06",
                "Elite Precision Laser Engraver",
                6,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.PrecisionLaserEngraverZPM.set(
            new MTEBasicMachineWithRecipe(
                PRECISION_LASER_ENGRAVER_ZPM.ID,
                "basicmachine.laserengraver.tier.07",
                "Elite Precision Laser Engraver II",
                7,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.PrecisionLaserEngraverUV.set(
            new MTEBasicMachineWithRecipe(
                PRECISION_LASER_ENGRAVER_UV.ID,
                "basicmachine.laserengraver.tier.08",
                "Ultimate Exact Photon Cannon",
                8,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.PrecisionLaserEngraverUHV.set(
            new MTEBasicMachineWithRecipe(
                PRECISION_LASER_ENGRAVER_UHV.ID,
                "basicmachine.laserengraver.tier.09",
                "Epic Exact Photon Cannon",
                9,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.PrecisionLaserEngraverUEV.set(
            new MTEBasicMachineWithRecipe(
                PRECISION_LASER_ENGRAVER_UEV.ID,
                "basicmachine.laserengraver.tier.10",
                "Epic Exact Photon Cannon II",
                10,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.PrecisionLaserEngraverUIV.set(
            new MTEBasicMachineWithRecipe(
                PRECISION_LASER_ENGRAVER_UIV.ID,
                "basicmachine.laserengraver.tier.11",
                "Epic Exact Photon Cannon III",
                11,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

        ItemList.PrecisionLaserEngraverUMV.set(
            new MTEBasicMachineWithRecipe(
                PRECISION_LASER_ENGRAVER_UMV.ID,
                "basicmachine.laserengraver.tier.12",
                "Epic Exact Photon Cannon IV",
                12,
                MachineType.LASER_ENGRAVER.tooltipDescription(),
                laserEngraverRecipes,
                4,
                1,
                true,
                SoundResource.GTCEU_LOOP_ELECTROLYZER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "LASER_ENGRAVER").getStackForm(1L));

    }

    private void registerMacerator() {
        ItemList.Machine_LV_Macerator.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_LV.ID,
                "basicmachine.macerator.tier.01",
                "Basic Macerator",
                1,
                MachineType.MACERATOR.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "MACERATOR").getStackForm(1L));

        ItemList.Machine_MV_Macerator.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_MV.ID,
                "basicmachine.macerator.tier.02",
                "Advanced Macerator",
                2,
                MachineType.MACERATOR.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "MACERATOR").getStackForm(1L));

        ItemList.Machine_HV_Macerator.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_HV.ID,
                "basicmachine.macerator.tier.03",
                "Universal Macerator",
                3,
                MachineType.MACERATOR.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                2,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.Machine_EV_Macerator.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_EV.ID,
                "basicmachine.macerator.tier.04",
                "Universal Pulverizer",
                4,
                MachineType.MACERATOR.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.Machine_IV_Macerator.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_IV.ID,
                "basicmachine.macerator.tier.05",
                "Blend-O-Matic 9001",
                5,
                MachineType.MACERATOR.tooltipDescription(),
                RecipeMaps.maceratorRecipes,
                1,
                4,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.MaceratorLuV.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_LuV.ID,
                "basicmachine.macerator.tier.06",
                "Elite Pulverizer",
                6,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.MaceratorZPM.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_ZPM.ID,
                "basicmachine.macerator.tier.07",
                "Elite Pulverizer II",
                7,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.MaceratorUV.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_UV.ID,
                "basicmachine.macerator.tier.08",
                "Ultimate Shape Eliminator",
                8,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.MaceratorUHV.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_UHV.ID,
                "basicmachine.macerator.tier.09",
                "Epic Shape Eliminator",
                9,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.MaceratorUEV.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_UEV.ID,
                "basicmachine.macerator.tier.10",
                "Epic Shape Eliminator II",
                10,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.MaceratorUIV.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_UIV.ID,
                "basicmachine.macerator.tier.11",
                "Epic Shape Eliminator III",
                11,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

        ItemList.MaceratorUMV.set(
            new MTEBasicMachineWithRecipe(
                MACERATOR_UMV.ID,
                "basicmachine.macerator.tier.12",
                "Epic Shape Eliminator IV",
                12,
                MachineType.MACERATOR.tooltipDescription(),
                maceratorRecipes,
                1,
                4,
                false,
                SoundResource.GTCEU_LOOP_MACERATOR,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PULVERIZER").getStackForm(1L));

    }

    private void registerMatterFabricator() {

        ItemList.MassFabricatorLuV.set(
            new MTEMassfabricator(MATTER_FABRICATOR_LuV.ID, "basicmachine.massfab.tier.06", "Elite Mass Fabricator", 6)
                .getStackForm(1L));
        ItemList.MassFabricatorZPM.set(
            new MTEMassfabricator(
                MATTER_FABRICATOR_ZPM.ID,
                "basicmachine.massfab.tier.07",
                "Elite Mass Fabricator II",
                7).getStackForm(1L));
        ItemList.MassFabricatorUV.set(
            new MTEMassfabricator(
                MATTER_FABRICATOR_UV.ID,
                "basicmachine.massfab.tier.08",
                "Ultimate Existence Initiator",
                8).getStackForm(1L));
        ItemList.MassFabricatorUHV.set(
            new MTEMassfabricator(
                MATTER_FABRICATOR_UHV.ID,
                "basicmachine.massfab.tier.09",
                "Epic Existence Initiator",
                9).getStackForm(1L));
        ItemList.MassFabricatorUEV.set(
            new MTEMassfabricator(
                MATTER_FABRICATOR_UEV.ID,
                "basicmachine.massfab.tier.10",
                "Epic Existence Initiator II",
                10).getStackForm(1L));
        ItemList.MassFabricatorUIV.set(
            new MTEMassfabricator(
                MATTER_FABRICATOR_UIV.ID,
                "basicmachine.massfab.tier.11",
                "Epic Existence Initiator III",
                11).getStackForm(1L));
        ItemList.MassFabricatorUMV.set(
            new MTEMassfabricator(
                MATTER_FABRICATOR_UMV.ID,
                "basicmachine.massfab.tier.12",
                "Epic Existence Initiator IV",
                12).getStackForm(1L));
    }

    private void registerMicrowave() {
        ItemList.Machine_LV_Microwave.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_OVEN_LV.ID,
                "basicmachine.microwave.tier.01",
                "Basic Microwave",
                1,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.Machine_MV_Microwave.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_OVEN_MV.ID,
                "basicmachine.microwave.tier.02",
                "Advanced Microwave",
                2,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.Machine_HV_Microwave.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_OVEN_HV.ID,
                "basicmachine.microwave.tier.03",
                "Advanced Microwave II",
                3,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.Machine_EV_Microwave.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_OVEN_EV.ID,
                "basicmachine.microwave.tier.04",
                "Advanced Microwave III",
                4,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.Machine_IV_Microwave.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_OVEN_IV.ID,
                "basicmachine.microwave.tier.05",
                "Advanced Microwave IV",
                5,
                MachineType.MICROWAVE.tooltipDescription(),
                RecipeMaps.microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.MicrowaveLuV.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_LuV.ID,
                "basicmachine.microwave.tier.06",
                "Elite Microwave",
                6,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.MicrowaveZPM.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_ZPM.ID,
                "basicmachine.microwave.tier.07",
                "Elite Microwave II",
                7,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.MicrowaveUV.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_UV.ID,
                "basicmachine.microwave.tier.08",
                "Ultimate UFO Engine",
                8,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.MicrowaveUHV.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_UHV.ID,
                "basicmachine.microwave.tier.09",
                "Epic UFO Engine",
                9,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.MicrowaveUEV.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_UEV.ID,
                "basicmachine.microwave.tier.10",
                "Epic UFO Engine II",
                10,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.MicrowaveUIV.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_UIV.ID,
                "basicmachine.microwave.tier.11",
                "Epic UFO Engine III",
                11,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

        ItemList.MicrowaveUMV.set(
            new MTEBasicMachineWithRecipe(
                MICROWAVE_UMV.ID,
                "basicmachine.microwave.tier.12",
                "Epic UFO Engine IV",
                12,
                MachineType.MICROWAVE.tooltipDescription(),
                microwaveRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "MICROWAVE").getStackForm(1L));

    }

    private static void registerOven() {
        ItemList.Machine_LV_Oven.set(
            new MTEBasicMachineWithRecipe(
                OVEN_LV.ID,
                "basicmachine.e_oven.tier.01",
                "Basic Electric Oven",
                1,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN").setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));

        ItemList.Machine_MV_Oven.set(
            new MTEBasicMachineWithRecipe(
                OVEN_MV.ID,
                "basicmachine.e_oven.tier.02",
                "Advanced Electric Oven",
                2,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN").setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));

        ItemList.Machine_HV_Oven.set(
            new MTEBasicMachineWithRecipe(
                OVEN_HV.ID,
                "basicmachine.e_oven.tier.03",
                "Advanced Electric Oven II",
                3,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN").setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));

        ItemList.Machine_EV_Oven.set(
            new MTEBasicMachineWithRecipe(
                OVEN_EV.ID,
                "basicmachine.e_oven.tier.04",
                "Advanced Electric Oven III",
                4,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN").setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));

        ItemList.Machine_IV_Oven.set(
            new MTEBasicMachineWithRecipe(
                OVEN_IV.ID,
                "basicmachine.e_oven.tier.05",
                "Advanced Electric Oven IV",
                5,
                MachineType.OVEN.tooltipDescription(),
                RecipeMaps.furnaceRecipes,
                1,
                1,
                false,
                SoundResource.GTCEU_LOOP_HUM,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ELECTRIC_OVEN").setProgressBarTextureName("E_Oven")
                    .getStackForm(1L));
    }

    private void registerOreWashingPlant() {
        ItemList.Machine_LV_OreWasher.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHER_LV.ID,
                "basicmachine.orewasher.tier.01",
                "Basic Ore Washing Plant",
                1,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.Machine_MV_OreWasher.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHER_MV.ID,
                "basicmachine.orewasher.tier.02",
                "Advanced Ore Washing Plant",
                2,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.Machine_HV_OreWasher.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHER_HV.ID,
                "basicmachine.orewasher.tier.03",
                "Advanced Ore Washing Plant II",
                3,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.Machine_EV_OreWasher.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHER_EV.ID,
                "basicmachine.orewasher.tier.04",
                "Advanced Ore Washing Plant III",
                4,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.Machine_IV_OreWasher.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHER_IV.ID,
                "basicmachine.orewasher.tier.05",
                "Repurposed Laundry-Washer I-360",
                5,
                MachineType.ORE_WASHER.tooltipDescription(),
                RecipeMaps.oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.OreWashingPlantLuV.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHING_PLANT_LuV.ID,
                "basicmachine.orewasher.tier.06",
                "Elite Ore Washing Plant",
                6,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.OreWashingPlantZPM.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHING_PLANT_ZPM.ID,
                "basicmachine.orewasher.tier.07",
                "Elite Ore Washing Plant II",
                7,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.OreWashingPlantUV.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHING_PLANT_UV.ID,
                "basicmachine.orewasher.tier.08",
                "Ultimate Ore Washing Machine",
                8,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.OreWashingPlantUHV.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHING_PLANT_UHV.ID,
                "basicmachine.orewasher.tier.09",
                "Epic Ore Washing Machine",
                9,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.OreWashingPlantUEV.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHING_PLANT_UEV.ID,
                "basicmachine.orewasher.tier.10",
                "Epic Ore Washing Machine II",
                10,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.OreWashingPlantUIV.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHING_PLANT_UIV.ID,
                "basicmachine.orewasher.tier.11",
                "Epic Ore Washing Machine III",
                11,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

        ItemList.OreWashingPlantUMV.set(
            new MTEBasicMachineWithRecipe(
                ORE_WASHING_PLANT_UMV.ID,
                "basicmachine.orewasher.tier.12",
                "Epic Ore Washing Machine IV",
                12,
                MachineType.ORE_WASHER.tooltipDescription(),
                oreWasherRecipes,
                1,
                3,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ORE_WASHER").getStackForm(1L));

    }

    private void registerPolarizer() {
        ItemList.Machine_LV_Polarizer.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_LV.ID,
                "basicmachine.polarizer.tier.01",
                "Basic Polarizer",
                1,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.Machine_MV_Polarizer.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_MV.ID,
                "basicmachine.polarizer.tier.02",
                "Advanced Polarizer",
                2,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.Machine_HV_Polarizer.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_HV.ID,
                "basicmachine.polarizer.tier.03",
                "Advanced Polarizer II",
                3,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.Machine_EV_Polarizer.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_EV.ID,
                "basicmachine.polarizer.tier.04",
                "Advanced Polarizer III",
                4,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.Machine_IV_Polarizer.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_IV.ID,
                "basicmachine.polarizer.tier.05",
                "Advanced Polarizer IV",
                5,
                MachineType.POLARIZER.tooltipDescription(),
                RecipeMaps.polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.PolarizerLuV.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_LuV.ID,
                "basicmachine.polarizer.tier.06",
                "Elite Polarizer",
                6,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.PolarizerZPM.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_ZPM.ID,
                "basicmachine.polarizer.tier.07",
                "Elite Polarizer II",
                7,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.PolarizerUV.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_UV.ID,
                "basicmachine.polarizer.tier.08",
                "Ultimate Magnetism Inducer",
                8,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.PolarizerUHV.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_UHV.ID,
                "basicmachine.polarizer.tier.09",
                "Epic Magnetism Inducer",
                9,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.PolarizerUEV.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_UEV.ID,
                "basicmachine.polarizer.tier.10",
                "Epic Magnetism Inducer II",
                10,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.PolarizerUIV.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_UIV.ID,
                "basicmachine.polarizer.tier.11",
                "Epic Magnetism Inducer III",
                11,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

        ItemList.PolarizerUMV.set(
            new MTEBasicMachineWithRecipe(
                POLARIZER_UMV.ID,
                "basicmachine.polarizer.tier.12",
                "Epic Magnetism Inducer IV",
                12,
                MachineType.POLARIZER.tooltipDescription(),
                polarizerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "POLARIZER").getStackForm(1L));

    }

    private static void registerPrinter() {
        ItemList.Machine_LV_Printer.set(
            new MTEBasicMachineWithRecipe(
                PRINTER_LV.ID,
                "basicmachine.printer.tier.01",
                "Basic Printer",
                1,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PRINTER").getStackForm(1L));

        ItemList.Machine_MV_Printer.set(
            new MTEBasicMachineWithRecipe(
                PRINTER_MV.ID,
                "basicmachine.printer.tier.02",
                "Advanced Printer",
                2,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PRINTER").getStackForm(1L));

        ItemList.Machine_HV_Printer.set(
            new MTEBasicMachineWithRecipe(
                PRINTER_HV.ID,
                "basicmachine.printer.tier.03",
                "Advanced Printer II",
                3,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PRINTER").getStackForm(1L));

        ItemList.Machine_EV_Printer.set(
            new MTEBasicMachineWithRecipe(
                PRINTER_EV.ID,
                "basicmachine.printer.tier.04",
                "Advanced Printer III",
                4,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PRINTER").getStackForm(1L));

        ItemList.Machine_IV_Printer.set(
            new MTEBasicMachineWithRecipe(
                PRINTER_IV.ID,
                "basicmachine.printer.tier.05",
                "Advanced Printer IV",
                5,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PRINTER").getStackForm(1L));

        ItemList.Machine_LuV_Printer.set(
            new MTEBasicMachineWithRecipe(
                PRINTER_LuV.ID,
                "basicmachine.printer.tier.06",
                "Advanced Printer V",
                6,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PRINTER").getStackForm(1L));

        ItemList.Machine_ZPM_Printer.set(
            new MTEBasicMachineWithRecipe(
                PRINTER_ZPM.ID,
                "basicmachine.printer.tier.07",
                "Advanced Printer VI",
                7,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PRINTER").getStackForm(1L));

        ItemList.Machine_UV_Printer.set(
            new MTEBasicMachineWithRecipe(
                PRINTER_UV.ID,
                "basicmachine.printer.tier.08",
                "Advanced Printer VII",
                8,
                MachineType.PRINTER.tooltipDescription(),
                RecipeMaps.printerRecipes,
                1,
                1,
                true,
                SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE,
                "PRINTER").getStackForm(1L));
    }

    private void registerRecycler() {
        ItemList.Machine_LV_Recycler.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_LV.ID,
                "basicmachine.recycler.tier.01",
                "Basic Recycler",
                1,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.Machine_MV_Recycler.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_MV.ID,
                "basicmachine.recycler.tier.02",
                "Advanced Recycler",
                2,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.Machine_HV_Recycler.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_HV.ID,
                "basicmachine.recycler.tier.03",
                "Advanced Recycler II",
                3,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.Machine_EV_Recycler.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_EV.ID,
                "basicmachine.recycler.tier.04",
                "Advanced Recycler III",
                4,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.Machine_IV_Recycler.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_IV.ID,
                "basicmachine.recycler.tier.05",
                "The Oblitterator",
                5,
                MachineType.RECYCLER.tooltipDescription(),
                RecipeMaps.recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.RecyclerLuV.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_LuV.ID,
                "basicmachine.recycler.tier.06",
                "Elite Recycler",
                6,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.RecyclerZPM.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_ZPM.ID,
                "basicmachine.recycler.tier.07",
                "Elite Recycler II",
                7,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.RecyclerUV.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_UV.ID,
                "basicmachine.recycler.tier.08",
                "Ultimate Scrap-O-Matic",
                8,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.RecyclerUHV.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_UHV.ID,
                "basicmachine.recycler.tier.09",
                "Epic Scrap-O-Matic",
                9,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.RecyclerUEV.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_UEV.ID,
                "basicmachine.recycler.tier.10",
                "Epic Scrap-O-Matic II",
                10,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.RecyclerUIV.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_UIV.ID,
                "basicmachine.recycler.tier.11",
                "Epic Scrap-O-Matic III",
                11,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

        ItemList.RecyclerUMV.set(
            new MTEBasicMachineWithRecipe(
                RECYCLER_UMV.ID,
                "basicmachine.recycler.tier.12",
                "Epic Scrap-O-Matic IV",
                12,
                MachineType.RECYCLER.tooltipDescription(),
                recyclerRecipes,
                1,
                1,
                false,
                SoundResource.IC2_MACHINES_RECYCLER_OP,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "RECYCLER").getStackForm(1L));

    }

    private void registerSiftingMachine() {
        ItemList.Machine_LV_Sifter.set(
            new MTEBasicMachineWithRecipe(
                SIFTER_LV.ID,
                "basicmachine.sifter.tier.01",
                "Basic Sifting Machine",
                1,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.Machine_MV_Sifter.set(
            new MTEBasicMachineWithRecipe(
                SIFTER_MV.ID,
                "basicmachine.sifter.tier.02",
                "Advanced Sifting Machine",
                2,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.Machine_HV_Sifter.set(
            new MTEBasicMachineWithRecipe(
                SIFTER_HV.ID,
                "basicmachine.sifter.tier.03",
                "Advanced Sifting Machine II",
                3,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.Machine_EV_Sifter.set(
            new MTEBasicMachineWithRecipe(
                SIFTER_EV.ID,
                "basicmachine.sifter.tier.04",
                "Advanced Sifting Machine III",
                4,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.Machine_IV_Sifter.set(
            new MTEBasicMachineWithRecipe(
                SIFTER_IV.ID,
                "basicmachine.sifter.tier.05",
                "Advanced Sifting Machine IV",
                5,
                MachineType.SIFTER.tooltipDescription(),
                RecipeMaps.sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.SiftingMachineLuV.set(
            new MTEBasicMachineWithRecipe(
                SIFTING_MACHINE_LuV.ID,
                "basicmachine.sifter.tier.06",
                "Elite Sifting Machine",
                6,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.SiftingMachineZPM.set(
            new MTEBasicMachineWithRecipe(
                SIFTING_MACHINE_ZPM.ID,
                "basicmachine.sifter.tier.07",
                "Elite Sifting Machine II",
                7,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.SiftingMachineUV.set(
            new MTEBasicMachineWithRecipe(
                SIFTING_MACHINE_UV.ID,
                "basicmachine.sifter.tier.08",
                "Ultimate Pulsation Filter",
                8,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.SiftingMachineUHV.set(
            new MTEBasicMachineWithRecipe(
                SIFTING_MACHINE_UHV.ID,
                "basicmachine.sifter.tier.09",
                "Epic Pulsation Filter",
                9,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.SiftingMachineUEV.set(
            new MTEBasicMachineWithRecipe(
                SIFTING_MACHINE_UEV.ID,
                "basicmachine.sifter.tier.10",
                "Epic Pulsation Filter II",
                10,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.SiftingMachineUIV.set(
            new MTEBasicMachineWithRecipe(
                SIFTING_MACHINE_UIV.ID,
                "basicmachine.sifter.tier.11",
                "Epic Pulsation Filter III",
                11,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

        ItemList.SiftingMachineUMV.set(
            new MTEBasicMachineWithRecipe(
                SIFTING_MACHINE_UMV.ID,
                "basicmachine.sifter.tier.12",
                "Epic Pulsation Filter IV",
                12,
                MachineType.SIFTER.tooltipDescription(),
                sifterRecipes,
                1,
                9,
                true,
                SoundResource.NONE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "SIFTER").getStackForm(1L));

    }

    private void registerThermalCentrifuge() {
        ItemList.Machine_LV_ThermalCentrifuge.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_LV.ID,
                "basicmachine.thermalcentrifuge.tier.01",
                "Basic Thermal Centrifuge",
                1,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.Machine_MV_ThermalCentrifuge.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_MV.ID,
                "basicmachine.thermalcentrifuge.tier.02",
                "Advanced Thermal Centrifuge",
                2,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.Machine_HV_ThermalCentrifuge.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_HV.ID,
                "basicmachine.thermalcentrifuge.tier.03",
                "Advanced Thermal Centrifuge II",
                3,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.Machine_EV_ThermalCentrifuge.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_EV.ID,
                "basicmachine.thermalcentrifuge.tier.04",
                "Advanced Thermal Centrifuge III",
                4,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.Machine_IV_ThermalCentrifuge.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_IV.ID,
                "basicmachine.thermalcentrifuge.tier.05",
                "Blaze Sweatshop T-6350",
                5,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                RecipeMaps.thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.ThermalCentrifugeLuV.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_LuV.ID,
                "basicmachine.thermalcentrifuge.tier.06",
                "Elite Thermal Centrifuge",
                6,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.ThermalCentrifugeZPM.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_ZPM.ID,
                "basicmachine.thermalcentrifuge.tier.07",
                "Elite Thermal Centrifuge II",
                7,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.ThermalCentrifugeUV.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_UV.ID,
                "basicmachine.thermalcentrifuge.tier.08",
                "Ultimate Fire Cyclone",
                8,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.ThermalCentrifugeUHV.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_UHV.ID,
                "basicmachine.thermalcentrifuge.tier.09",
                "Epic Fire Cyclone",
                9,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.ThermalCentrifugeUEV.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_UEV.ID,
                "basicmachine.thermalcentrifuge.tier.10",
                "Epic Fire Cyclone II",
                10,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.ThermalCentrifugeUIV.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_UIV.ID,
                "basicmachine.thermalcentrifuge.tier.11",
                "Epic Fire Cyclone III",
                11,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));

        ItemList.ThermalCentrifugeUMV.set(
            new MTEBasicMachineWithRecipe(
                THERMAL_CENTRIFUGE_UMV.ID,
                "basicmachine.thermalcentrifuge.tier.12",
                "Epic Fire Cyclone IV",
                12,
                MachineType.THERMAL_CENTRIFUGE.tooltipDescription(),
                thermalCentrifugeRecipes,
                1,
                3,
                false,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "THERMAL_CENTRIFUGE").getStackForm(1L));
    }

    private void registerWiremill() {
        ItemList.Machine_LV_Wiremill.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_LV.ID,
                "basicmachine.wiremill.tier.01",
                "Basic Wiremill",
                1,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.Machine_MV_Wiremill.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_MV.ID,
                "basicmachine.wiremill.tier.02",
                "Advanced Wiremill",
                2,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.Machine_HV_Wiremill.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_HV.ID,
                "basicmachine.wiremill.tier.03",
                "Advanced Wiremill II",
                3,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.Machine_EV_Wiremill.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_EV.ID,
                "basicmachine.wiremill.tier.04",
                "Advanced Wiremill III",
                4,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.Machine_IV_Wiremill.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_IV.ID,
                "basicmachine.wiremill.tier.05",
                "Advanced Wiremill IV",
                5,
                MachineType.WIREMILL.tooltipDescription(),
                RecipeMaps.wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.WiremillLuV.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_LuV.ID,
                "basicmachine.wiremill.tier.06",
                "Elite Wiremill",
                6,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.WiremillZPM.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_ZPM.ID,
                "basicmachine.wiremill.tier.07",
                "Elite Wiremill II",
                7,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.WiremillUV.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_UV.ID,
                "basicmachine.wiremill.tier.08",
                "Ultimate Wire Transfigurator",
                8,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.WiremillUHV.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_UHV.ID,
                "basicmachine.wiremill.tier.09",
                "Epic Wire Transfigurator",
                9,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.WiremillUEV.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_UEV.ID,
                "basicmachine.wiremill.tier.10",
                "Epic Wire Transfigurator II",
                10,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.WiremillUIV.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_UIV.ID,
                "basicmachine.wiremill.tier.11",
                "Epic Wire Transfigurator III",
                11,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

        ItemList.WiremillUMV.set(
            new MTEBasicMachineWithRecipe(
                WIREMILL_UMV.ID,
                "basicmachine.wiremill.tier.12",
                "Epic Wire Transfigurator IV",
                12,
                MachineType.WIREMILL.tooltipDescription(),
                wiremillRecipes,
                2,
                1,
                false,
                SoundResource.GTCEU_LOOP_MOTOR,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "WIREMILL").getStackForm(1L));

    }

    private void registerArcFurnace() {
        ItemList.Machine_LV_ArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_LV.ID,
                "basicmachine.arcfurnace.tier.01",
                "Basic Arc Furnace",
                1,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.Machine_MV_ArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_MV.ID,
                "basicmachine.arcfurnace.tier.02",
                "Advanced Arc Furnace",
                2,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.Machine_HV_ArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_HV.ID,
                "basicmachine.arcfurnace.tier.03",
                "Advanced Arc Furnace II",
                3,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.Machine_EV_ArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_EV.ID,
                "basicmachine.arcfurnace.tier.04",
                "Advanced Arc Furnace III",
                4,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.Machine_IV_ArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_IV.ID,
                "basicmachine.arcfurnace.tier.05",
                "Advanced Arc Furnace IV",
                5,
                MachineType.ARC_FURNACE.tooltipDescription(),
                RecipeMaps.arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.ArcFurnaceLuV.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_LuV.ID,
                "basicmachine.arcfurnace.tier.06",
                "Elite Arc Furnace",
                6,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.ArcFurnaceZPM.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_ZPM.ID,
                "basicmachine.arcfurnace.tier.07",
                "Elite Arc Furnace II",
                7,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.ArcFurnaceUV.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_UV.ID,
                "basicmachine.arcfurnace.tier.08",
                "Ultimate Short Circuit Heater",
                8,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.ArcFurnaceUHV.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_UHV.ID,
                "basicmachine.arcfurnace.tier.09",
                "Epic Short Circuit Heater",
                9,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.ArcFurnaceUEV.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_UEV.ID,
                "basicmachine.arcfurnace.tier.10",
                "Epic Short Circuit Heater II",
                10,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.ArcFurnaceUIV.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_UIV.ID,
                "basicmachine.arcfurnace.tier.11",
                "Epic Short Circuit Heater III",
                11,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

        ItemList.ArcFurnaceUMV.set(
            new MTEBasicMachineWithRecipe(
                ARC_FURNACE_UMV.ID,
                "basicmachine.arcfurnace.tier.12",
                "Epic Short Circuit Heater IV",
                12,
                MachineType.ARC_FURNACE.tooltipDescription(),
                arcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "ARC_FURNACE").getStackForm(1L));

    }

    private void registerCentrifuge() {
        ItemList.Machine_LV_Centrifuge.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_LV.ID,
                "basicmachine.centrifuge.tier.01",
                "Basic Centrifuge",
                1,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.Machine_MV_Centrifuge.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_MV.ID,
                "basicmachine.centrifuge.tier.02",
                "Advanced Centrifuge",
                2,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.Machine_HV_Centrifuge.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_HV.ID,
                "basicmachine.centrifuge.tier.03",
                "Turbo Centrifuge",
                3,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.Machine_EV_Centrifuge.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_EV.ID,
                "basicmachine.centrifuge.tier.04",
                "Molecular Separator",
                4,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.Machine_IV_Centrifuge.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_IV.ID,
                "basicmachine.centrifuge.tier.05",
                "Molecular Cyclone",
                5,
                MachineType.CENTRIFUGE.tooltipDescription(),
                RecipeMaps.centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.CentrifugeLuV.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_LuV.ID,
                "basicmachine.centrifuge.tier.06",
                "Elite Centrifuge",
                6,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.CentrifugeZPM.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_ZPM.ID,
                "basicmachine.centrifuge.tier.07",
                "Elite Centrifuge II",
                7,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.CentrifugeUV.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_UV.ID,
                "basicmachine.centrifuge.tier.08",
                "Ultimate Molecular Tornado",
                8,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.CentrifugeUHV.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_UHV.ID,
                "basicmachine.centrifuge.tier.09",
                "Epic Molecular Tornado",
                9,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.CentrifugeUEV.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_UEV.ID,
                "basicmachine.centrifuge.tier.10",
                "Epic Molecular Tornado II",
                10,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.CentrifugeUIV.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_UIV.ID,
                "basicmachine.centrifuge.tier.11",
                "Epic Molecular Tornado III",
                11,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

        ItemList.CentrifugeUMV.set(
            new MTEBasicMachineWithRecipe(
                CENTRIFUGE_UMV.ID,
                "basicmachine.centrifuge.tier.12",
                "Epic Molecular Tornado IV",
                12,
                MachineType.CENTRIFUGE.tooltipDescription(),
                centrifugeRecipes,
                2,
                6,
                true,
                SoundResource.GTCEU_LOOP_CENTRIFUGE,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CENTRIFUGE").getStackForm(1L));

    }

    private void registerPlasmaArcFurnace() {
        ItemList.Machine_LV_PlasmaArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_LV.ID,
                "basicmachine.plasmaarcfurnace.tier.01",
                "Basic Plasma Arc Furnace",
                1,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.Machine_MV_PlasmaArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_MV.ID,
                "basicmachine.plasmaarcfurnace.tier.02",
                "Advanced Plasma Arc Furnace",
                2,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.Machine_HV_PlasmaArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_HV.ID,
                "basicmachine.plasmaarcfurnace.tier.03",
                "Advanced Plasma Arc Furnace II",
                3,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                4,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.Machine_EV_PlasmaArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_EV.ID,
                "basicmachine.plasmaarcfurnace.tier.04",
                "Advanced Plasma Arc Furnace III",
                4,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.Machine_IV_PlasmaArcFurnace.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_IV.ID,
                "basicmachine.plasmaarcfurnace.tier.05",
                "Advanced Plasma Arc Furnace IV",
                5,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                RecipeMaps.plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.PlasmaArcFurnaceLuV.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_LuV.ID,
                "basicmachine.plasmaarcfurnace.tier.06",
                "Elite Plasma Arc Furnace",
                6,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.PlasmaArcFurnaceZPM.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_ZPM.ID,
                "basicmachine.plasmaarcfurnace.tier.07",
                "Elite Plasma Arc Furnace II",
                7,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.PlasmaArcFurnaceUV.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_UV.ID,
                "basicmachine.plasmaarcfurnace.tier.08",
                "Ultimate Plasma Discharge Heater",
                8,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.PlasmaArcFurnaceUHV.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_UHV.ID,
                "basicmachine.plasmaarcfurnace.tier.09",
                "Epic Plasma Discharge Heater",
                9,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.PlasmaArcFurnaceUEV.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_UEV.ID,
                "basicmachine.plasmaarcfurnace.tier.10",
                "Epic Plasma Discharge Heater II",
                10,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.PlasmaArcFurnaceUIV.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_UIV.ID,
                "basicmachine.plasmaarcfurnace.tier.11",
                "Epic Plasma Discharge Heater III",
                11,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));

        ItemList.PlasmaArcFurnaceUMV.set(
            new MTEBasicMachineWithRecipe(
                PLASMA_ARC_FURNACE_UMV.ID,
                "basicmachine.plasmaarcfurnace.tier.12",
                "Epic Plasma Discharge Heater IV",
                12,
                MachineType.PLASMA_ARC_FURNACE.tooltipDescription(),
                plasmaArcFurnaceRecipes,
                1,
                9,
                true,
                SoundResource.GTCEU_LOOP_ARC,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "PLASMA_ARC_FURNACE").getStackForm(1L));
    }

    private void registerCanningMachine() {
        ItemList.Machine_LV_Canner.set(
            new MTEBasicMachineWithRecipe(
                CANNER_LV.ID,
                "basicmachine.canner.tier.01",
                "Basic Canning Machine",
                1,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.Machine_MV_Canner.set(
            new MTEBasicMachineWithRecipe(
                CANNER_MV.ID,
                "basicmachine.canner.tier.02",
                "Advanced Canning Machine",
                2,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.Machine_HV_Canner.set(
            new MTEBasicMachineWithRecipe(
                CANNER_HV.ID,
                "basicmachine.canner.tier.03",
                "Advanced Canning Machine II",
                3,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.Machine_EV_Canner.set(
            new MTEBasicMachineWithRecipe(
                CANNER_EV.ID,
                "basicmachine.canner.tier.04",
                "Advanced Canning Machine III",
                4,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.Machine_IV_Canner.set(
            new MTEBasicMachineWithRecipe(
                CANNER_IV.ID,
                "basicmachine.canner.tier.05",
                "Advanced Canning Machine IV",
                5,
                MachineType.CANNER.tooltipDescription(),
                RecipeMaps.cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.CanningMachineLuV.set(
            new MTEBasicMachineWithRecipe(
                CANNING_MACHINE_LuV.ID,
                "basicmachine.canner.tier.06",
                "Elite Canning Machine",
                6,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.CanningMachineZPM.set(
            new MTEBasicMachineWithRecipe(
                CANNING_MACHINE_ZPM.ID,
                "basicmachine.canner.tier.07",
                "Elite Canning Machine II",
                7,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.CanningMachineUV.set(
            new MTEBasicMachineWithRecipe(
                CANNING_MACHINE_UV.ID,
                "basicmachine.canner.tier.08",
                "Ultimate Can Operator",
                8,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.CanningMachineUHV.set(
            new MTEBasicMachineWithRecipe(
                CANNING_MACHINE_UHV.ID,
                "basicmachine.canner.tier.09",
                "Epic Can Operator",
                9,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.CanningMachineUEV.set(
            new MTEBasicMachineWithRecipe(
                CANNING_MACHINE_UEV.ID,
                "basicmachine.canner.tier.10",
                "Epic Can Operator II",
                10,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.CanningMachineUIV.set(
            new MTEBasicMachineWithRecipe(
                CANNING_MACHINE_UIV.ID,
                "basicmachine.canner.tier.11",
                "Epic Can Operator III",
                11,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));

        ItemList.CanningMachineUMV.set(
            new MTEBasicMachineWithRecipe(
                CANNING_MACHINE_UMV.ID,
                "basicmachine.canner.tier.12",
                "Epic Can Operator IV",
                12,
                MachineType.CANNER.tooltipDescription(),
                cannerRecipes,
                2,
                2,
                true,
                SoundResource.GTCEU_LOOP_BATH,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CANNER").getStackForm(1L));
    }

    private static void registerDynamoHatch() {
        ItemList.Hatch_Dynamo_ULV.set(
            new MTEHatchDynamo(DYNAMO_HATCH_ULV.ID, "hatch.dynamo.tier.00", "ULV Dynamo Hatch", 0).getStackForm(1L));
        ItemList.Hatch_Dynamo_LV
            .set(new MTEHatchDynamo(DYNAMO_HATCH_LV.ID, "hatch.dynamo.tier.01", "LV Dynamo Hatch", 1).getStackForm(1L));
        ItemList.Hatch_Dynamo_MV
            .set(new MTEHatchDynamo(DYNAMO_HATCH_MV.ID, "hatch.dynamo.tier.02", "MV Dynamo Hatch", 2).getStackForm(1L));
        ItemList.Hatch_Dynamo_HV
            .set(new MTEHatchDynamo(DYNAMO_HATCH_HV.ID, "hatch.dynamo.tier.03", "HV Dynamo Hatch", 3).getStackForm(1L));
        ItemList.Hatch_Dynamo_EV
            .set(new MTEHatchDynamo(DYNAMO_HATCH_EV.ID, "hatch.dynamo.tier.04", "EV Dynamo Hatch", 4).getStackForm(1L));
        ItemList.Hatch_Dynamo_IV
            .set(new MTEHatchDynamo(DYNAMO_HATCH_IV.ID, "hatch.dynamo.tier.05", "IV Dynamo Hatch", 5).getStackForm(1L));
        ItemList.Hatch_Dynamo_LuV.set(
            new MTEHatchDynamo(DYNAMO_HATCH_LuV.ID, "hatch.dynamo.tier.06", "LuV Dynamo Hatch", 6).getStackForm(1L));
        ItemList.Hatch_Dynamo_ZPM.set(
            new MTEHatchDynamo(DYNAMO_HATCH_ZPM.ID, "hatch.dynamo.tier.07", "ZPM Dynamo Hatch", 7).getStackForm(1L));
        ItemList.Hatch_Dynamo_UV
            .set(new MTEHatchDynamo(DYNAMO_HATCH_UV.ID, "hatch.dynamo.tier.08", "UV Dynamo Hatch", 8).getStackForm(1L));
        ItemList.Hatch_Dynamo_UHV.set(
            new MTEHatchDynamo(DYNAMO_HATCH_UHV.ID, "hatch.dynamo.tier.09", "UHV Dynamo Hatch", 9).getStackForm(1L));
        ItemList.Hatch_Dynamo_UEV.set(
            new MTEHatchDynamo(DYNAMO_HATCH_UEV.ID, "hatch.dynamo.tier.10", "UEV Dynamo Hatch", 10).getStackForm(1L));

        ItemList.Hatch_Dynamo_UIV.set(
            new MTEHatchDynamo(DYNAMO_HATCH_UIV.ID, "hatch.dynamo.tier.11", "UIV Dynamo Hatch", 11).getStackForm(1L));

        ItemList.Hatch_Dynamo_UMV.set(
            new MTEHatchDynamo(DYNAMO_HATCH_UMV.ID, "hatch.dynamo.tier.12", "UMV Dynamo Hatch", 12).getStackForm(1L));

        ItemList.Hatch_Dynamo_UXV.set(
            new MTEHatchDynamo(DYNAMO_HATCH_UXV.ID, "hatch.dynamo.tier.13", "UXV Dynamo Hatch", 13).getStackForm(1L));
    }

    private static void registerEnergyHatch() {
        ItemList.Hatch_Energy_ULV.set(
            new MTEHatchEnergy(ENERGY_HATCH_ULV.ID, "hatch.energy.tier.00", "ULV Energy Hatch", 0).getStackForm(1L));
        ItemList.Hatch_Energy_LV
            .set(new MTEHatchEnergy(ENERGY_HATCH_LV.ID, "hatch.energy.tier.01", "LV Energy Hatch", 1).getStackForm(1L));
        ItemList.Hatch_Energy_MV
            .set(new MTEHatchEnergy(ENERGY_HATCH_MV.ID, "hatch.energy.tier.02", "MV Energy Hatch", 2).getStackForm(1L));
        ItemList.Hatch_Energy_HV
            .set(new MTEHatchEnergy(ENERGY_HATCH_HV.ID, "hatch.energy.tier.03", "HV Energy Hatch", 3).getStackForm(1L));
        ItemList.Hatch_Energy_EV
            .set(new MTEHatchEnergy(ENERGY_HATCH_EV.ID, "hatch.energy.tier.04", "EV Energy Hatch", 4).getStackForm(1L));
        ItemList.Hatch_Energy_IV
            .set(new MTEHatchEnergy(ENERGY_HATCH_IV.ID, "hatch.energy.tier.05", "IV Energy Hatch", 5).getStackForm(1L));
        ItemList.Hatch_Energy_LuV.set(
            new MTEHatchEnergy(ENERGY_HATCH_LuV.ID, "hatch.energy.tier.06", "LuV Energy Hatch", 6).getStackForm(1L));
        ItemList.Hatch_Energy_ZPM.set(
            new MTEHatchEnergy(ENERGY_HATCH_ZPM.ID, "hatch.energy.tier.07", "ZPM Energy Hatch", 7).getStackForm(1L));
        ItemList.Hatch_Energy_UV
            .set(new MTEHatchEnergy(ENERGY_HATCH_UV.ID, "hatch.energy.tier.08", "UV Energy Hatch", 8).getStackForm(1L));
        ItemList.Hatch_Energy_UHV.set(
            new MTEHatchEnergy(ENERGY_HATCH_UHV.ID, "hatch.energy.tier.09", "UHV Energy Hatch", 9).getStackForm(1L));
        ItemList.Hatch_Energy_UEV.set(
            new MTEHatchEnergy(ENERGY_HATCH_UEV.ID, "hatch.energy.tier.10", "UEV Energy Hatch", 10).getStackForm(1L));

        ItemList.Hatch_Energy_UIV.set(
            new MTEHatchEnergy(ENERGY_HATCH_UIV.ID, "hatch.energy.tier.11", "UIV Energy Hatch", 11).getStackForm(1L));

        ItemList.Hatch_Energy_UMV.set(
            new MTEHatchEnergy(ENERGY_HATCH_UMV.ID, "hatch.energy.tier.12", "UMV Energy Hatch", 12).getStackForm(1L));

        ItemList.Hatch_Energy_UXV.set(
            new MTEHatchEnergy(ENERGY_HATCH_UXV.ID, "hatch.energy.tier.13", "UXV Energy Hatch", 13).getStackForm(1L));

        ItemList.DebugEnergyHatch.set(
            new MTEHatchEnergyDebug(ENERGY_HATCH_DEBUG.ID, "hatch.energy.debug", "Debug Energy Hatch", 14)
                .getStackForm(1L));
    }

    private static void registerInputHatch() {
        ItemList.Hatch_Input_ULV
            .set(new MTEHatchInput(INPUT_HATCH_ULV.ID, "hatch.input.tier.00", "Input Hatch (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Input_LV
            .set(new MTEHatchInput(INPUT_HATCH_LV.ID, "hatch.input.tier.01", "Input Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Input_MV
            .set(new MTEHatchInput(INPUT_HATCH_MV.ID, "hatch.input.tier.02", "Input Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Input_HV
            .set(new MTEHatchInput(INPUT_HATCH_HV.ID, "hatch.input.tier.03", "Input Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Input_EV
            .set(new MTEHatchInput(INPUT_HATCH_EV.ID, "hatch.input.tier.04", "Input Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Input_IV
            .set(new MTEHatchInput(INPUT_HATCH_IV.ID, "hatch.input.tier.05", "Input Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Input_LuV
            .set(new MTEHatchInput(INPUT_HATCH_LuV.ID, "hatch.input.tier.06", "Input Hatch (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Input_ZPM
            .set(new MTEHatchInput(INPUT_HATCH_ZPM.ID, "hatch.input.tier.07", "Input Hatch (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Input_UV
            .set(new MTEHatchInput(INPUT_HATCH_UV.ID, "hatch.input.tier.08", "Input Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Input_UHV
            .set(new MTEHatchInput(INPUT_HATCH_UHV.ID, "hatch.input.tier.09", "Input Hatch (UHV)", 9).getStackForm(1L));
        ItemList.Hatch_Input_UEV.set(
            new MTEHatchInput(INPUT_HATCH_UEV.ID, "hatch.input.tier.10", "Input Hatch (UEV)", 10).getStackForm(1L));
        ItemList.Hatch_Input_UIV.set(
            new MTEHatchInput(INPUT_HATCH_UIV.ID, "hatch.input.tier.11", "Input Hatch (UIV)", 11).getStackForm(1L));
        ItemList.Hatch_Input_UMV.set(
            new MTEHatchInput(INPUT_HATCH_UMV.ID, "hatch.input.tier.12", "Input Hatch (UMV)", 12).getStackForm(1L));
        ItemList.Hatch_Input_UXV.set(
            new MTEHatchInput(INPUT_HATCH_UXV.ID, "hatch.input.tier.13", "Input Hatch (UXV)", 13).getStackForm(1L));
        ItemList.Hatch_Input_MAX.set(
            new MTEHatchInput(INPUT_HATCH_MAX.ID, "hatch.input.tier.14", "Input Hatch (MAX)", 14).getStackForm(1L));
        ItemList.Hatch_Input_Debug.set(
            new MTEHatchInputDebug(INPUT_HATCH_DEBUG.ID, "hatch.input.debug", "Debug Input Hatch", 14)
                .getStackForm(1L));
    }

    private static void registerQuadrupleInputHatch() {
        ItemList.Hatch_Input_Multi_2x2_EV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_EV.ID,
                4,
                "hatch.multi.input.tier.01",
                "Quadruple Input Hatch (EV)",
                4).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_IV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_IV.ID,
                4,
                "hatch.multi.input.tier.02",
                "Quadruple Input Hatch (IV)",
                5).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_LuV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_LuV.ID,
                4,
                "hatch.multi.input.tier.03",
                "Quadruple Input Hatch (LuV)",
                6).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_ZPM.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_ZPM.ID,
                4,
                "hatch.multi.input.tier.04",
                "Quadruple Input Hatch (ZPM)",
                7).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_UV.ID,
                4,
                "hatch.multi.input.tier.05",
                "Quadruple Input Hatch (UV)",
                8).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UHV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_UHV.ID,
                4,
                "hatch.multi.input.tier.06",
                "Quadruple Input Hatch (UHV)",
                9).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UEV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_UEV.ID,
                4,
                "hatch.multi.input.tier.07",
                "Quadruple Input Hatch (UEV)",
                10).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UIV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_UIV.ID,
                4,
                "hatch.multi.input.tier.08",
                "Quadruple Input Hatch (UIV)",
                11).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UMV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_UMV.ID,
                4,
                "hatch.multi.input.tier.09",
                "Quadruple Input Hatch (UMV)",
                12).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UXV.set(
            new MTEHatchMultiInput(
                QUADRUPLE_INPUT_HATCHES_UXV.ID,
                4,
                "hatch.multi.input.tier.10",
                "Quadruple Input Hatch (UXV)",
                13).getStackForm(1L));

        ItemList.Hatch_Input_Multi_2x2_Humongous.set(
            new MTEHatchQuadrupleHumongous(
                QUADRUPLE_INPUT_HATCHES_MAX.ID,
                4,
                "hatch.multi.input.tier.11",
                "Humongous Quadruple Input Hatch").getStackForm(1L));
    }

    private static void registerOutputHatch() {
        ItemList.Hatch_Output_ULV.set(
            new MTEHatchOutput(OUTPUT_HATCH_ULV.ID, "hatch.output.tier.00", "Output Hatch (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Output_LV.set(
            new MTEHatchOutput(OUTPUT_HATCH_LV.ID, "hatch.output.tier.01", "Output Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Output_MV.set(
            new MTEHatchOutput(OUTPUT_HATCH_MV.ID, "hatch.output.tier.02", "Output Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Output_HV.set(
            new MTEHatchOutput(OUTPUT_HATCH_HV.ID, "hatch.output.tier.03", "Output Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Output_EV.set(
            new MTEHatchOutput(OUTPUT_HATCH_EV.ID, "hatch.output.tier.04", "Output Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Output_IV.set(
            new MTEHatchOutput(OUTPUT_HATCH_IV.ID, "hatch.output.tier.05", "Output Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Output_LuV.set(
            new MTEHatchOutput(OUTPUT_HATCH_LuV.ID, "hatch.output.tier.06", "Output Hatch (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Output_ZPM.set(
            new MTEHatchOutput(OUTPUT_HATCH_ZPM.ID, "hatch.output.tier.07", "Output Hatch (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Output_UV.set(
            new MTEHatchOutput(OUTPUT_HATCH_UV.ID, "hatch.output.tier.08", "Output Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Output_UHV.set(
            new MTEHatchOutput(OUTPUT_HATCH_UHV.ID, "hatch.output.tier.09", "Output Hatch (UHV)", 9).getStackForm(1L));
        ItemList.Hatch_Output_UEV.set(
            new MTEHatchOutput(OUTPUT_HATCH_UEV.ID, "hatch.output.tier.10", "Output Hatch (UEV)", 10).getStackForm(1L));
        ItemList.Hatch_Output_UIV.set(
            new MTEHatchOutput(OUTPUT_HATCH_UIV.ID, "hatch.output.tier.11", "Output Hatch (UIV)", 11).getStackForm(1L));
        ItemList.Hatch_Output_UMV.set(
            new MTEHatchOutput(OUTPUT_HATCH_UMV.ID, "hatch.output.tier.12", "Output Hatch (UMV)", 12).getStackForm(1L));
        ItemList.Hatch_Output_UXV.set(
            new MTEHatchOutput(OUTPUT_HATCH_UXV.ID, "hatch.output.tier.13", "Output Hatch (UXV)", 13).getStackForm(1L));
        ItemList.Hatch_Output_MAX.set(
            new MTEHatchOutput(OUTPUT_HATCH_MAX.ID, "hatch.output.tier.14", "Output Hatch (MAX)", 14).getStackForm(1L));
    }

    private static void registerVoidHatch() {
        ItemList.Hatch_Void.set(new MTEHatchVoid(VOID_HATCH.ID, "hatch.void.tier.00", "Void Hatch").getStackForm(1L));
    }

    private static void registerVoidBus() {
        ItemList.Hatch_Void_Bus
            .set(new MTEHatchVoidBus(VOID_BUS.ID, "hatch.void_bus.tier.00", "Void Bus").getStackForm(1L));
    }

    private static void registerCokeOvenHatch() {
        ItemList.CokeOvenHatch
            .set(new MTEHatchCokeOven(COKE_OVEN_HATCH.ID, "hatch.cokeoven", "Coke Oven Hatch").getStackForm(1L));
    }

    private static void registerQuantumTank() {
        ItemList.Quantum_Tank_LV
            .set(new MTEQuantumTank(QUANTUM_TANK_LV.ID, "quantum.tank.tier.06", "Quantum Tank I", 6).getStackForm(1L));
        ItemList.Quantum_Tank_MV
            .set(new MTEQuantumTank(QUANTUM_TANK_MV.ID, "quantum.tank.tier.07", "Quantum Tank II", 7).getStackForm(1L));
        ItemList.Quantum_Tank_HV.set(
            new MTEQuantumTank(QUANTUM_TANK_HV.ID, "quantum.tank.tier.08", "Quantum Tank III", 8).getStackForm(1L));
        ItemList.Quantum_Tank_EV
            .set(new MTEQuantumTank(QUANTUM_TANK_EV.ID, "quantum.tank.tier.09", "Quantum Tank IV", 9).getStackForm(1L));
        ItemList.Quantum_Tank_IV
            .set(new MTEQuantumTank(QUANTUM_TANK_IV.ID, "quantum.tank.tier.10", "Quantum Tank V", 10).getStackForm(1L));
        ItemList.Debug_Fluid_Tank
            .set(new MTEDebugTank(DEBUG_FLUID_TANK.ID, "quantum.tank.debug", "Debug Fluid Tank", 10).getStackForm(1L));
    }

    private static void registerQuantumChest() {
        ItemList.Quantum_Chest_LV.set(
            new MTEQuantumChest(QUANTUM_CHEST_LV.ID, "quantum.chest.tier.06", "Quantum Chest I", 6).getStackForm(1L));
        ItemList.Quantum_Chest_MV.set(
            new MTEQuantumChest(QUANTUM_CHEST_MV.ID, "quantum.chest.tier.07", "Quantum Chest II", 7).getStackForm(1L));
        ItemList.Quantum_Chest_HV.set(
            new MTEQuantumChest(QUANTUM_CHEST_HV.ID, "quantum.chest.tier.08", "Quantum Chest III", 8).getStackForm(1L));
        ItemList.Quantum_Chest_EV.set(
            new MTEQuantumChest(QUANTUM_CHEST_EV.ID, "quantum.chest.tier.09", "Quantum Chest IV", 9).getStackForm(1L));
        ItemList.Quantum_Chest_IV.set(
            new MTEQuantumChest(QUANTUM_CHEST_IV.ID, "quantum.chest.tier.10", "Quantum Chest V", 10).getStackForm(1L));
    }

    private static void registerSuperTank() {
        ItemList.Super_Tank_LV
            .set(new MTESuperTank(SUPER_TANK_LV.ID, "super.tank.tier.01", "Super Tank I", 1).getStackForm(1L));
        ItemList.Super_Tank_MV
            .set(new MTESuperTank(SUPER_TANK_MV.ID, "super.tank.tier.02", "Super Tank II", 2).getStackForm(1L));
        ItemList.Super_Tank_HV
            .set(new MTESuperTank(SUPER_TANK_HV.ID, "super.tank.tier.03", "Super Tank III", 3).getStackForm(1L));
        ItemList.Super_Tank_EV
            .set(new MTESuperTank(SUPER_TANK_EV.ID, "super.tank.tier.04", "Super Tank IV", 4).getStackForm(1L));
        ItemList.Super_Tank_IV
            .set(new MTESuperTank(SUPER_TANK_IV.ID, "super.tank.tier.05", "Super Tank V", 5).getStackForm(1L));
    }

    private static void registerSuperChest() {
        ItemList.Super_Chest_LV
            .set(new MTESuperChest(SUPER_CHEST_LV.ID, "super.chest.tier.01", "Super Chest I", 1).getStackForm(1L));
        ItemList.Super_Chest_MV
            .set(new MTESuperChest(SUPER_CHEST_MV.ID, "super.chest.tier.02", "Super Chest II", 2).getStackForm(1L));
        ItemList.Super_Chest_HV
            .set(new MTESuperChest(SUPER_CHEST_HV.ID, "super.chest.tier.03", "Super Chest III", 3).getStackForm(1L));
        ItemList.Super_Chest_EV
            .set(new MTESuperChest(SUPER_CHEST_EV.ID, "super.chest.tier.04", "Super Chest IV", 4).getStackForm(1L));
        ItemList.Super_Chest_IV
            .set(new MTESuperChest(SUPER_CHEST_IV.ID, "super.chest.tier.05", "Super Chest V", 5).getStackForm(1L));
    }

    private static void registerLongDistancePipe() {
        ItemList.Long_Distance_Pipeline_Fluid.set(
            new MTELongDistancePipelineFluid(
                LONG_DISTANCE_PIPELINE_FLUID.ID,
                "long.distance.pipeline.fluid",
                "Long Distance Fluid Pipeline",
                1).getStackForm(1L));
        ItemList.Long_Distance_Pipeline_Item.set(
            new MTELongDistancePipelineItem(
                LONG_DISTANCE_PIPELINE_ITEM.ID,
                "long.distance.pipeline.item",
                "Long Distance Item Pipeline",
                1).getStackForm(1L));
    }

    private static void registerAE2Hatches() {
        ItemList.Hatch_Output_Bus_ME
            .set(new MTEHatchOutputBusME(OUTPUT_BUS_ME.ID, "hatch.output_bus.me", "Output Bus (ME)").getStackForm(1L));
        ItemList.Hatch_Input_Bus_ME.set(
            new MTEHatchInputBusME(INPUT_BUS_ME.ID, false, "hatch.input_bus.me.basic", "Stocking Input Bus (ME)")
                .getStackForm(1L));
        ItemList.Hatch_Input_Bus_ME_Advanced.set(
            new MTEHatchInputBusME(
                INPUT_BUS_ME_ADVANCED.ID,
                true,
                "hatch.input_bus.me",
                "Advanced Stocking Input Bus (ME)").getStackForm(1L));
        ItemList.Hatch_Input_ME.set(
            new MTEHatchInputME(INPUT_HATCH_ME.ID, false, "hatch.input.me.basic", "Stocking Input Hatch (ME)")
                .getStackForm(1L));
        ItemList.Hatch_Input_ME_Advanced.set(
            new MTEHatchInputME(
                INPUT_HATCH_ME_ADVANCED.ID,
                true,
                "hatch.input.me",
                "Advanced Stocking Input Hatch (ME)").getStackForm(1L));
        ItemList.Hatch_Output_ME
            .set(new MTEHatchOutputME(OUTPUT_HATCH_ME.ID, "hatch.output.me", "Output Hatch (ME)").getStackForm(1L));
        ItemList.Hatch_CraftingInput_Bus_ME.set(
            new MTEHatchCraftingInputME(
                CRAFTING_INPUT_ME.ID,
                "hatch.crafting_input.me",
                "Crafting Input Buffer (ME)",
                true).getStackForm(1L));
        ItemList.Hatch_CraftingInput_Bus_ME_ItemOnly.set(
            new MTEHatchCraftingInputME(
                CRAFTING_INPUT_ME_BUS.ID,
                "hatch.crafting_input.me.item_only",
                "Crafting Input Bus (ME)",
                false).getStackForm(1L));
        ItemList.Hatch_CraftingInput_Bus_Slave.set(
            new MTEHatchCraftingInputSlave(
                CRAFTING_INPUT_SLAVE.ID,
                "hatch.crafting_input.proxy",
                "Crafting Input Proxy").getStackForm(1L));
    }

    private static void registerMagHatch() {
        ItemList.Hatch_Electromagnet
            .set(new MTEHatchMagnet(MAG_HATCH.ID, "hatch.mag_hatch", "Electromagnet Housing").getStackForm(1L));
    }

    private static void registerInputBus() {
        ItemList.Hatch_Input_Bus_ULV.set(
            new MTEHatchInputBus(INPUT_BUS_ULV.ID, "hatch.input_bus.tier.00", "Input Bus (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Input_Bus_LV.set(
            new MTEHatchInputBus(INPUT_BUS_LV.ID, "hatch.input_bus.tier.01", "Input Bus (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Input_Bus_MV.set(
            new MTEHatchInputBus(INPUT_BUS_MV.ID, "hatch.input_bus.tier.02", "Input Bus (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Input_Bus_HV.set(
            new MTEHatchInputBus(INPUT_BUS_HV.ID, "hatch.input_bus.tier.03", "Input Bus (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Input_Bus_EV.set(
            new MTEHatchInputBus(INPUT_BUS_EV.ID, "hatch.input_bus.tier.04", "Input Bus (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Input_Bus_IV.set(
            new MTEHatchInputBus(INPUT_BUS_IV.ID, "hatch.input_bus.tier.05", "Input Bus (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Input_Bus_LuV.set(
            new MTEHatchInputBus(INPUT_BUS_LuV.ID, "hatch.input_bus.tier.06", "Input Bus (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Input_Bus_ZPM.set(
            new MTEHatchInputBus(INPUT_BUS_ZPM.ID, "hatch.input_bus.tier.07", "Input Bus (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Input_Bus_UV.set(
            new MTEHatchInputBus(INPUT_BUS_UV.ID, "hatch.input_bus.tier.08", "Input Bus (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Input_Bus_MAX.set(
            new MTEHatchInputBus(INPUT_BUS_UHV.ID, "hatch.input_bus.tier.09", "Input Bus (UHV)", 9).getStackForm(1L));

        ItemList.Hatch_Input_Bus_Debug.set(
            new MTEHatchInputBusDebug(INPUT_BUS_DEBUG.ID, "hatch.input_bus.debug", "Debug Input Bus", 9)
                .getStackForm(1l));
    }

    private static void registerOutputBus() {
        ItemList.Hatch_Output_Bus_ULV.set(
            new MTEHatchOutputBus(OUTPUT_BUS_ULV.ID, "hatch.output_bus.tier.00", "Output Bus (ULV)", 0)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_LV.set(
            new MTEHatchOutputBus(OUTPUT_BUS_LV.ID, "hatch.output_bus.tier.01", "Output Bus (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Output_Bus_MV.set(
            new MTEHatchOutputBus(OUTPUT_BUS_MV.ID, "hatch.output_bus.tier.02", "Output Bus (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Output_Bus_HV.set(
            new MTEHatchOutputBus(OUTPUT_BUS_HV.ID, "hatch.output_bus.tier.03", "Output Bus (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Output_Bus_EV.set(
            new MTEHatchOutputBus(OUTPUT_BUS_EV.ID, "hatch.output_bus.tier.04", "Output Bus (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Output_Bus_IV.set(
            new MTEHatchOutputBus(OUTPUT_BUS_IV.ID, "hatch.output_bus.tier.05", "Output Bus (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Output_Bus_LuV.set(
            new MTEHatchOutputBus(OUTPUT_BUS_LuV.ID, "hatch.output_bus.tier.06", "Output Bus (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_ZPM.set(
            new MTEHatchOutputBus(OUTPUT_BUS_ZPM.ID, "hatch.output_bus.tier.07", "Output Bus (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_UV.set(
            new MTEHatchOutputBus(OUTPUT_BUS_UV.ID, "hatch.output_bus.tier.08", "Output Bus (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Output_Bus_MAX.set(
            new MTEHatchOutputBus(OUTPUT_BUS_UHV.ID, "hatch.output_bus.tier.09", "Output Bus (UHV)", 9)
                .getStackForm(1L));
    }

    private static void registerMufflerHatch() {
        ItemList.Hatch_Muffler_LV.set(
            new MTEHatchMuffler(MUFFLER_HATCH_LV.ID, "hatch.muffler.tier.01", "Muffler Hatch (LV)", 1)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_MV.set(
            new MTEHatchMuffler(MUFFLER_HATCH_MV.ID, "hatch.muffler.tier.02", "Muffler Hatch (MV)", 2)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_HV.set(
            new MTEHatchMuffler(MUFFLER_HATCH_HV.ID, "hatch.muffler.tier.03", "Muffler Hatch (HV)", 3)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_EV.set(
            new MTEHatchMuffler(MUFFLER_HATCH_EV.ID, "hatch.muffler.tier.04", "Muffler Hatch (EV)", 4)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_IV.set(
            new MTEHatchMuffler(MUFFLER_HATCH_IV.ID, "hatch.muffler.tier.05", "Muffler Hatch (IV)", 5)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_LuV.set(
            new MTEHatchMuffler(MUFFLER_HATCH_LuV.ID, "hatch.muffler.tier.06", "Muffler Hatch (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_ZPM.set(
            new MTEHatchMuffler(MUFFLER_HATCH_ZPM.ID, "hatch.muffler.tier.07", "Muffler Hatch (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_UV.set(
            new MTEHatchMuffler(MUFFLER_HATCH_UV.ID, "hatch.muffler.tier.08", "Muffler Hatch (UV)", 8)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_MAX.set(
            new MTEHatchMuffler(MUFFLER_HATCH_UHV.ID, "hatch.muffler.tier.09", "Muffler Hatch (UHV)", 9)
                .getStackForm(1L));
    }

    private static void registerBoiler() {
        ItemList.Machine_Bronze_Boiler
            .set(new MTEBoilerBronze(SMALL_COAL_BOILER.ID, "boiler.bronze", "Small Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler.set(
            new MTEBoilerSteel(HIGH_PRESSURE_COAL_BOILER.ID, "boiler.steel", "Large Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler_Lava.set(
            new MTEBoilerLava(HIGH_PRESSURE_LAVA_BOILER.ID, "boiler.lava", "Reinforced Lava Boiler").getStackForm(1L));
        ItemList.Machine_Bronze_Boiler_Solar
            .set(new MTEBoilerSolar(SIMPLE_SOLAR_BOILER.ID, "boiler.solar", "Simple Solar Boiler").getStackForm(1L));
        ItemList.Machine_HP_Solar.set(
            new MTEBoilerSolarSteel(HIGH_PRESSURE_SOLAR_BOILER.ID, "boiler.steel.solar", "Advanced Solar Boiler")
                .getStackForm(1L));
    }

    private static void registerBatteryBuffer1x1() {
        ItemList.Battery_Buffer_1by1_ULV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_ULV.ID,
                "batterybuffer.01.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_LV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_LV.ID,
                "batterybuffer.01.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_MV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_MV.ID,
                "batterybuffer.01.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_HV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_HV.ID,
                "batterybuffer.01.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_EV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_EV.ID,
                "batterybuffer.01.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_IV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_IV.ID,
                "batterybuffer.01.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_LuV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_LuV.ID,
                "batterybuffer.01.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_ZPM.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_ZPM.ID,
                "batterybuffer.01.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_UV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UV.ID,
                "batterybuffer.01.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_UHV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UHV.ID,
                "batterybuffer.01.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_UEV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UEV.ID,
                "batterybuffer.01.tier.10",
                "Extremely Ultimate Battery Buffer",
                10,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_UIV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UIV.ID,
                "batterybuffer.01.tier.11",
                "Insanely Ultimate Battery Buffer",
                11,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_UMV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UMV.ID,
                "batterybuffer.01.tier.12",
                "Mega Ultimate Battery Buffer",
                12,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_UXV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_UXV.ID,
                "batterybuffer.01.tier.13",
                "Extended Mega Ultimate Battery Buffer",
                13,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_MAXV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_1_BY_1_MAX.ID,
                "batterybuffer.01.tier.14",
                "Maximum Battery Buffer",
                14,
                "",
                1).getStackForm(1L));
    }

    private static void registerBatteryBuffer2x2() {
        ItemList.Battery_Buffer_2by2_ULV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_ULV.ID,
                "batterybuffer.04.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_LV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_LV.ID,
                "batterybuffer.04.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_MV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_MV.ID,
                "batterybuffer.04.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_HV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_HV.ID,
                "batterybuffer.04.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_EV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_EV.ID,
                "batterybuffer.04.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_IV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_IV.ID,
                "batterybuffer.04.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_LuV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_LuV.ID,
                "batterybuffer.04.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_ZPM.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_ZPM.ID,
                "batterybuffer.04.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_UV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UV.ID,
                "batterybuffer.04.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_UHV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UHV.ID,
                "batterybuffer.04.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_UEV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UEV.ID,
                "batterybuffer.04.tier.10",
                "Extremely Ultimate Battery Buffer",
                10,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_UIV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UIV.ID,
                "batterybuffer.04.tier.11",
                "Insanely Ultimate Battery Buffer",
                11,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_UMV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UMV.ID,
                "batterybuffer.04.tier.12",
                "Mega Ultimate Battery Buffer",
                12,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_UXV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_UXV.ID,
                "batterybuffer.04.tier.13",
                "Extended Mega Ultimate Battery Buffer",
                13,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_MAXV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_2_BY_2_MAX.ID,
                "batterybuffer.04.tier.14",
                "Maximum Battery Buffer",
                14,
                "",
                4).getStackForm(1L));
    }

    private static void registerBatteryBuffer3x3() {
        ItemList.Battery_Buffer_3by3_ULV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_ULV.ID,
                "batterybuffer.09.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_LV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_LV.ID,
                "batterybuffer.09.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_MV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_MV.ID,
                "batterybuffer.09.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_HV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_HV.ID,
                "batterybuffer.09.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_EV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_EV.ID,
                "batterybuffer.09.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_IV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_IV.ID,
                "batterybuffer.09.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_LuV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_LuV.ID,
                "batterybuffer.09.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_ZPM.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_ZPM.ID,
                "batterybuffer.09.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_UV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UV.ID,
                "batterybuffer.09.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_UHV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UHV.ID,
                "batterybuffer.09.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_UEV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UEV.ID,
                "batterybuffer.09.tier.10",
                "Extremely Ultimate Battery Buffer",
                10,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_UIV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UIV.ID,
                "batterybuffer.09.tier.11",
                "Insanely Ultimate Battery Buffer",
                11,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_UMV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UMV.ID,
                "batterybuffer.09.tier.12",
                "Mega Ultimate Battery Buffer",
                12,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_UXV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_UXV.ID,
                "batterybuffer.09.tier.13",
                "Extended Mega Ultimate Battery Buffer",
                13,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_MAXV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_3_BY_3_MAX.ID,
                "batterybuffer.09.tier.14",
                "Maximum Battery Buffer",
                14,
                "",
                9).getStackForm(1L));
    }

    private static void registerBatteryBuffer4x4() {
        ItemList.Battery_Buffer_4by4_ULV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_ULV.ID,
                "batterybuffer.16.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_LV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_LV.ID,
                "batterybuffer.16.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_MV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_MV.ID,
                "batterybuffer.16.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_HV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_HV.ID,
                "batterybuffer.16.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_EV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_EV.ID,
                "batterybuffer.16.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_IV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_IV.ID,
                "batterybuffer.16.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_LuV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_LuV.ID,
                "batterybuffer.16.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_ZPM.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_ZPM.ID,
                "batterybuffer.16.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UV.ID,
                "batterybuffer.16.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UHV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UHV.ID,
                "batterybuffer.16.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UEV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UEV.ID,
                "batterybuffer.16.tier.10",
                "Extremely Ultimate Battery Buffer",
                10,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_UIV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UIV.ID,
                "batterybuffer.16.tier.11",
                "Insanely Ultimate Battery Buffer",
                11,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_UMV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UMV.ID,
                "batterybuffer.16.tier.12",
                "Mega Ultimate Battery Buffer",
                12,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_UXV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_UXV.ID,
                "batterybuffer.16.tier.13",
                "Extended Mega Ultimate Battery Buffer",
                13,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_MAXV.set(
            new MTEBasicBatteryBuffer(
                BATTERY_BUFFER_4_BY_4_MAX.ID,
                "batterybuffer.16.tier.14",
                "Maximum Battery Buffer",
                14,
                "",
                16).getStackForm(1L));
    }

    private static void registerCharger4x4() {
        ItemList.Battery_Charger_4by4_ULV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_ULV.ID,
                "batterycharger.16.tier.00",
                "Ultra Low Voltage Battery Charger",
                0,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_LV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_LV.ID,
                "batterycharger.16.tier.01",
                "Low Voltage Battery Charger",
                1,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_MV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_MV.ID,
                "batterycharger.16.tier.02",
                "Medium Voltage Battery Charger",
                2,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_HV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_HV.ID,
                "batterycharger.16.tier.03",
                "High Voltage Battery Charger",
                3,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_EV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_EV.ID,
                "batterycharger.16.tier.04",
                "Extreme Voltage Battery Charger",
                4,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_IV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_IV.ID,
                "batterycharger.16.tier.05",
                "Insane Voltage Battery Charger",
                5,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_LuV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_LuV.ID,
                "batterycharger.16.tier.06",
                "Ludicrous Voltage Battery Charger",
                6,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_ZPM.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_ZPM.ID,
                "batterycharger.16.tier.07",
                "ZPM Voltage Battery Charger",
                7,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_UV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_UV.ID,
                "batterycharger.16.tier.08",
                "Ultimate Voltage Battery Charger",
                8,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_UHV.set(
            new MTECharger(
                BATTERY_CHARGER_4_BY_4_UHV.ID,
                "batterycharger.16.tier.09",
                "Highly Ultimate Voltage Battery Charger",
                9,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));

        ItemList.Battery_Charger_4by4_UEV.set(
            new MTECharger(
                BATTERY_CHARGER_4_4_UEV.ID,
                "batterycharger.16.tier.10",
                "Extremely Ultimate Battery Charger",
                10,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Charger_4by4_UIV.set(
            new MTECharger(
                BATTERY_CHARGER_4_4_UIV.ID,
                "batterycharger.16.tier.11",
                "Insanely Ultimate Battery Charger",
                11,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Charger_4by4_UMV.set(
            new MTECharger(
                BATTERY_CHARGER_4_4_UMV.ID,
                "batterycharger.16.tier.12",
                "Mega Ultimate Battery Charger",
                12,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Charger_4by4_UXV.set(
            new MTECharger(
                BATTERY_CHARGER_4_4_UXV.ID,
                "batterycharger.16.tier.13",
                "Extended Mega Ultimate Battery Charger",
                13,
                "",
                4).getStackForm(1L));
    }

    private void registerCircuitAssembler() {
        ItemList.Machine_LV_CircuitAssembler.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_LV.ID,
                "basicmachine.circuitassembler.tier.01",
                "Basic Circuit Assembler",
                1,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.Machine_MV_CircuitAssembler.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_MV.ID,
                "basicmachine.circuitassembler.tier.02",
                "Advanced Circuit Assembler",
                2,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.Machine_HV_CircuitAssembler.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_HV.ID,
                "basicmachine.circuitassembler.tier.03",
                "Advanced Circuit Assembler II",
                3,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.Machine_EV_CircuitAssembler.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_EV.ID,
                "basicmachine.circuitassembler.tier.04",
                "Advanced Circuit Assembler III",
                4,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.Machine_IV_CircuitAssembler.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_IV.ID,
                "basicmachine.circuitassembler.tier.05",
                "Advanced Circuit Assembler IV",
                5,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.Machine_LuV_CircuitAssembler.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_LuV.ID,
                "basicmachine.circuitassembler.tier.06",
                "Advanced Circuit Assembler V",
                6,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.Machine_ZPM_CircuitAssembler.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_ZPM.ID,
                "basicmachine.circuitassembler.tier.07",
                "Advanced Circuit Assembler VI",
                7,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.Machine_UV_CircuitAssembler.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_UV.ID,
                "basicmachine.circuitassembler.tier.08",
                "Advanced Circuit Assembler VII",
                8,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                RecipeMaps.circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.CircuitAssemblerUHV.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_UHV.ID,
                "basicmachine.circuitassembler.tier.09",
                "Ultimate Circuit Assembling Machine",
                9,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.CircuitAssemblerUEV.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_UEV.ID,
                "basicmachine.circuitassembler.tier.10",
                "Ultimate Circuit Assembling Machine II",
                10,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.CircuitAssemblerUIV.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_UIV.ID,
                "basicmachine.circuitassembler.tier.11",
                "Ultimate Circuit Assembling Machine III",
                11,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.CircuitAssemblerUMV.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_UMV.ID,
                "basicmachine.circuitassembler.tier.12",
                "Ultimate Circuit Assembling Machine IV",
                12,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.CircuitAssemblerUXV.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_UXV.ID,
                "basicmachine.circuitassembler.tier.13",
                "Ultimate Circuit Assembling Machine V",
                13,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));

        ItemList.CircuitAssemblerMAX.set(
            new MTEBasicMachineWithRecipe(
                CIRCUIT_ASSEMBLER_MAX.ID,
                "basicmachine.circuitassembler.tier.14",
                "MAX Circuit Assembling Machine",
                14,
                MachineType.CIRCUIT_ASSEMBLER.tooltipDescription(),
                circuitAssemblerRecipes,
                6,
                1,
                true,
                SoundResource.GTCEU_LOOP_ASSEMBLER,
                MTEBasicMachineWithRecipe.SpecialEffects.NONE,
                "CIRCUITASSEMBLER").getStackForm(1L));
    }

    private void registerWetTransformer() {
        ItemList.WetTransformer_LV_ULV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_LV_ULV.ID,
                "wettransformer.tier.00",
                "Ultra Low Voltage Power Transformer",
                0,
                "LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_MV_LV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_MV_LV.ID,
                "wetransformer.tier.01",
                "Low Voltage Power Transformer",
                1,
                "MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_HV_MV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_HV_MV.ID,
                "wettransformer.tier.02",
                "Medium Voltage Power Transformer",
                2,
                "HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_EV_HV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_EV_HV.ID,
                "wettransformer.tier.03",
                "High Voltage Power Transformer",
                3,
                "EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_IV_EV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_IV_EV.ID,
                "wettransformer.tier.04",
                "Extreme Power Transformer",
                4,
                "IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_LuV_IV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_LuV_IV.ID,
                "wettransformer.tier.05",
                "Insane Power Transformer",
                5,
                "LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_ZPM_LuV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_ZPM_LuV.ID,
                "wettransformer.tier.06",
                "Ludicrous Power Transformer",
                6,
                "ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UV_ZPM.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_UV_ZPM.ID,
                "wettransformer.tier.07",
                "ZPM Voltage Power Transformer",
                7,
                "UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UHV_UV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_UHV_UV.ID,
                "wettransformer.tier.08",
                "Ultimate Power Transformer",
                8,
                "UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UEV_UHV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_UEV_UHV.ID,
                "wettransformer.tier.09",
                "Highly Ultimate Power Transformer",
                9,
                "UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UIV_UEV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_UIV_UEV.ID,
                "wettransformer.tier.10",
                "Extremely Ultimate Power Transformer",
                10,
                "UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UMV_UIV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_UMV_UIV.ID,
                "wettransformer.tier.11",
                "Insanely Ultimate Power Transformer",
                11,
                "UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_UXV_UMV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_UXV_UMV.ID,
                "wettransformer.tier.12",
                "Mega Ultimate Power Transformer",
                12,
                "UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.WetTransformer_MAX_UXV.set(
            new MTEWetTransformer(
                WET_TRANSFORMER_MAX_UXV.ID,
                "wettransformer.tier.13",
                "Extended Mega Ultimate Power Transformer",
                13,
                "MAX -> UXV (Use Soft Mallet to invert)").getStackForm(1L));
    }

    private void registerHighAmpTransformer() {
        ItemList.Transformer_HA_UEV_UHV.set(
            new MTETransformerHiAmp(
                HIGH_AMP_TRANSFORMER_UEV_UHV.ID,
                "transformer.ha.tier.09",
                "Highly Ultimate Hi-Amp Transformer",
                9,
                "UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HA_UIV_UEV.set(
            new MTETransformerHiAmp(
                HIGH_AMP_TRANSFORMER_UIV_UEV.ID,
                "transformer.ha.tier.10",
                "Extremely Ultimate Hi-Amp Transformer",
                10,
                "UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HA_UMV_UIV.set(
            new MTETransformerHiAmp(
                HIGH_AMP_TRANSFORMER_UMV_UIV.ID,
                "transformer.ha.tier.11",
                "Insanely Ultimate Hi-Amp Transformer",
                11,
                "UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HA_UXV_UMV.set(
            new MTETransformerHiAmp(
                HIGH_AMP_TRANSFORMER_UXV_UMV.ID,
                "transformer.ha.tier.12",
                "Mega Ultimate Hi-Amp Transformer",
                12,
                "UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HA_MAX_UXV.set(
            new MTETransformerHiAmp(
                HIGH_AMP_TRANSFORMER_MAX_UXV.ID,
                "transformer.ha.tier.13",
                "Extended Mega Ultimate Hi-Amp Transformer",
                13,
                "MAX -> UXV (Use Soft Mallet to invert)").getStackForm(1L));

    }

    private void registerTurboCharger4By4() {
        ItemList.Battery_TurboCharger_4by4_ULV.set(
            new MTETurboCharger(
                TURBO_CHARGER_ULV.ID,
                "batteryturbocharger.16.tier.00",
                "Ultra Low Voltage Turbo Charger",
                0,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_LV.set(
            new MTETurboCharger(
                TURBO_CHARGER_LV.ID,
                "batteryturbocharger.16.tier.01",
                "Low Voltage Turbo Charger",
                1,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_MV.set(
            new MTETurboCharger(
                TURBO_CHARGER_MV.ID,
                "batteryturbocharger.16.tier.02",
                "Medium Voltage Turbo Charger",
                2,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_HV.set(
            new MTETurboCharger(
                TURBO_CHARGER_HV.ID,
                "batteryturbocharger.16.tier.03",
                "High Voltage Turbo Charger",
                3,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_EV.set(
            new MTETurboCharger(
                TURBO_CHARGER_EV.ID,
                "batteryturbocharger.16.tier.04",
                "Extreme Voltage Turbo Charger",
                4,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_IV.set(
            new MTETurboCharger(
                TURBO_CHARGER_IV.ID,
                "batteryturbocharger.16.tier.05",
                "Insane Voltage Turbo Charger",
                5,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_LuV.set(
            new MTETurboCharger(
                TURBO_CHARGER_LuV.ID,
                "batteryturbocharger.16.tier.06",
                "Ludicrous Voltage Turbo Charger",
                6,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_ZPM.set(
            new MTETurboCharger(
                TURBO_CHARGER_ZPM.ID,
                "batteryturbocharger.16.tier.07",
                "ZPM Voltage Turbo Charger",
                7,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_UV.set(
            new MTETurboCharger(
                TURBO_CHARGER_UV.ID,
                "batteryturbocharger.16.tier.08",
                "Ultimate Voltage Turbo Charger",
                8,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

        ItemList.Battery_TurboCharger_4by4_UHV.set(
            new MTETurboCharger(
                TURBO_CHARGER_UHV.ID,
                "batteryturbocharger.16.tier.09",
                "Highly Ultimate Voltage Turbo Charger",
                9,
                "64A in /16A out, 120A/item, Disable to force Charge",
                4).getStackForm(1L));

    }

    private static void registerWirelessEnergyHatch() {
        ItemList.Wireless_Hatch_Energy_ULV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_ULV.ID,
                "hatch.wireless.receiver.tier.00",
                "ULV Wireless Energy Hatch",
                0).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_LV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_LV.ID,
                "hatch.wireless.receiver.tier.01",
                "LV Wireless Energy Hatch",
                1).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_MV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_MV.ID,
                "hatch.wireless.receiver.tier.02",
                "MV Wireless Energy Hatch",
                2).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_HV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_HV.ID,
                "hatch.wireless.receiver.tier.03",
                "HV Wireless Energy Hatch",
                3).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_EV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_EV.ID,
                "hatch.wireless.receiver.tier.04",
                "EV Wireless Energy Hatch",
                4).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_IV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_IV.ID,
                "hatch.wireless.receiver.tier.05",
                "IV Wireless Energy Hatch",
                5).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_LuV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_LuV.ID,
                "hatch.wireless.receiver.tier.06",
                "LuV Wireless Energy Hatch",
                6).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_ZPM.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_ZPM.ID,
                "hatch.wireless.receiver.tier.07",
                "ZPM Wireless Energy Hatch",
                7).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_UV.ID,
                "hatch.wireless.receiver.tier.08",
                "UV Wireless Energy Hatch",
                8).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UHV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_UHV.ID,
                "hatch.wireless.receiver.tier.09",
                "UHV Wireless Energy Hatch",
                9).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UEV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_UEV.ID,
                "hatch.wireless.receiver.tier.10",
                "UEV Wireless Energy Hatch",
                10).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UIV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_UIV.ID,
                "hatch.wireless.receiver.tier.11",
                "UIV Wireless Energy Hatch",
                11).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UMV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_UMV.ID,
                "hatch.wireless.receiver.tier.12",
                "UMV Wireless Energy Hatch",
                12).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_UXV.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_UXV.ID,
                "hatch.wireless.receiver.tier.13",
                "UXV Wireless Energy Hatch",
                13).getStackForm(1L));
        ItemList.Wireless_Hatch_Energy_MAX.set(
            new MTEWirelessEnergy(
                WIRELESS_HATCH_ENERGY_MAX.ID,
                "hatch.wireless.receiver.tier.14",
                "MAX Wireless Energy Hatch",
                14).getStackForm(1L));
    }

    private static void registerLightningRods() {
        ItemList.Machine_HV_LightningRod.set(
            new MTELightningRod(LIGHTNING_ROD_HV.ID, "basicgenerator.lightningrod.03", "Lightning Rod", 3)
                .getStackForm(1));
        ItemList.Machine_EV_LightningRod.set(
            new MTELightningRod(LIGHTNING_ROD_EV.ID, "basicgenerator.lightningrod.04", "Lightning Rod II", 4)
                .getStackForm(1));
        ItemList.Machine_IV_LightningRod.set(
            new MTELightningRod(LIGHTNING_ROD_IV.ID, "basicgenerator.lightningrod.05", "Lightning Rod III", 5)
                .getStackForm(1));
    }

    private static void registerSolarPanels() {
        ItemList.Machine_LV_SolarPanel.set(
            new MTESolarGenerator(SOLAR_PANEL_LV.ID, "basicgenerator.solarpanel.01", "Basic Solar Panel", 1)
                .getStackForm(1));
        ItemList.Machine_MV_SolarPanel.set(
            new MTESolarGenerator(SOLAR_PANEL_MV.ID, "basicgenerator.solarpanel.02", "Advanced Solar Panel", 2)
                .getStackForm(1));
        ItemList.Machine_HV_SolarPanel.set(
            new MTESolarGenerator(SOLAR_PANEL_HV.ID, "basicgenerator.solarpanel.03", "Advanced Solar Panel II", 3)
                .getStackForm(1));
        ItemList.Machine_EV_SolarPanel.set(
            new MTESolarGenerator(SOLAR_PANEL_EV.ID, "basicgenerator.solarpanel.04", "Advanced Solar Panel III", 4)
                .getStackForm(1));
        ItemList.Machine_IV_SolarPanel.set(
            new MTESolarGenerator(SOLAR_PANEL_IV.ID, "basicgenerator.solarpanel.05", "Advanced Solar Panel IV", 5)
                .getStackForm(1));
        ItemList.Machine_LuV_SolarPanel.set(
            new MTESolarGenerator(SOLAR_PANEL_LuV.ID, "basicgenerator.solarpanel.06", "Elite Solar Panel", 6)
                .getStackForm(1));
        ItemList.Machine_ZPM_SolarPanel.set(
            new MTESolarGenerator(SOLAR_PANEL_ZPM.ID, "basicgenerator.solarpanel.07", "Elite Solar Panel II", 7)
                .getStackForm(1));
        ItemList.Machine_UV_SolarPanel.set(
            new MTESolarGenerator(SOLAR_PANEL_UV.ID, "basicgenerator.solarpanel.08", "Ultimate Solar Panel", 8)
                .getStackForm(1));
    }

    private static void registerCombustionGenerators() {
        ItemList.Generator_Diesel_LV.set(
            new MTEDieselGenerator(
                COMBUSTION_GENERATOR_LV.ID,
                "basicgenerator.diesel.tier.01",
                "Basic Combustion Generator",
                1,
                95).getStackForm(1L));
        ItemList.Generator_Diesel_MV.set(
            new MTEDieselGenerator(
                COMBUSTION_GENERATOR_MV.ID,
                "basicgenerator.diesel.tier.02",
                "Advanced Combustion Generator",
                2,
                90).getStackForm(1L));
        ItemList.Generator_Diesel_HV.set(
            new MTEDieselGenerator(
                COMBUSTION_GENERATOR_HV.ID,
                "basicgenerator.diesel.tier.03",
                "Turbo Combustion Generator",
                3,
                85).getStackForm(1L));
    }

    private static void registerGasTurbines() {
        ItemList.Generator_Gas_Turbine_LV.set(
            new MTEGasTurbine(GAS_TURBINE_LV.ID, "basicgenerator.gasturbine.tier.01", "Basic Gas Turbine", 1, 95)
                .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_MV.set(
            new MTEGasTurbine(GAS_TURBINE_MV.ID, "basicgenerator.gasturbine.tier.02", "Advanced Gas Turbine", 2, 90)
                .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_HV.set(
            new MTEGasTurbine(GAS_TURBINE_HV.ID, "basicgenerator.gasturbine.tier.03", "Turbo Gas Turbine", 3, 85)
                .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_EV.set(
            new MTEGasTurbine(GAS_TURBINE_EV.ID, "basicgenerator.gasturbine.tier.04", "Turbo Gas Turbine II", 4, 60)
                .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_IV.set(
            new MTEGasTurbine(GAS_TURBINE_IV.ID, "basicgenerator.gasturbine.tier.05", "Turbo Gas Turbine III", 5, 50)
                .getStackForm(1L));
    }

    private static void registerSteamTurbines() {
        ItemList.Generator_Steam_Turbine_LV.set(
            new MTESteamTurbine(STEAM_TURBINE_LV.ID, "basicgenerator.steamturbine.tier.01", "Basic Steam Turbine", 1)
                .getStackForm(1L));
        ItemList.Generator_Steam_Turbine_MV.set(
            new MTESteamTurbine(STEAM_TURBINE_MV.ID, "basicgenerator.steamturbine.tier.02", "Advanced Steam Turbine", 2)
                .getStackForm(1L));
        ItemList.Generator_Steam_Turbine_HV.set(
            new MTESteamTurbine(STEAM_TURBINE_HV.ID, "basicgenerator.steamturbine.tier.03", "Turbo Steam Turbine", 3)
                .getStackForm(1L));
    }

    private static void registerNaquadahReactors() {
        ItemList.Generator_Naquadah_Mark_I.set(
            new MTENaquadahReactor(
                NAQUADAH_REACTOR_EV.ID,
                "basicgenerator.naquadah.tier.04",
                new String[] { "Requires Enriched Naquadah Bolts" },
                "Naquadah Reactor Mark I",
                4).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_II.set(
            new MTENaquadahReactor(
                NAQUADAH_REACTOR_IV.ID,
                "basicgenerator.naquadah.tier.05",
                new String[] { "Requires Enriched Naquadah Rods" },
                "Naquadah Reactor Mark II",
                5).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_III.set(
            new MTENaquadahReactor(
                NAQUADAH_REACTOR_LuV.ID,
                "basicgenerator.naquadah.tier.06",
                new String[] { "Requires Enriched Naquadah Long Rods" },
                "Naquadah Reactor Mark III",
                6).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_IV.set(
            new MTENaquadahReactor(
                NAQUADAH_REACTOR_ZPM.ID,
                "basicgenerator.naquadah.tier.07",
                new String[] { "Requires Naquadria Bolts" },
                "Naquadah Reactor Mark IV",
                7).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_V.set(
            new MTENaquadahReactor(
                NAQUADAH_REACTOR_UV.ID,
                "basicgenerator.naquadah.tier.08",
                new String[] { "Requires Naquadria Rods" },
                "Naquadah Reactor Mark V",
                8).getStackForm(1L));
    }

    private static void registerMagicEnergyConverters() {
        ItemList.MagicEnergyConverter_LV.set(
            new MTEMagicEnergyConverter(
                MAGIC_ENERGY_CONVERTER_LV.ID,
                "basicgenerator.magicenergyconverter.tier.01",
                "Novice Magic Energy Converter",
                1).getStackForm(1L));
        ItemList.MagicEnergyConverter_MV.set(
            new MTEMagicEnergyConverter(
                MAGIC_ENERGY_CONVERTER_MV.ID,
                "basicgenerator.magicenergyconverter.tier.02",
                "Adept Magic Energy Converter",
                2).getStackForm(1L));
        ItemList.MagicEnergyConverter_HV.set(
            new MTEMagicEnergyConverter(
                MAGIC_ENERGY_CONVERTER_HV.ID,
                "basicgenerator.magicenergyconverter.tier.03",
                "Master Magic Energy Converter",
                3).getStackForm(1L));
    }

    private static void registerMagicEnergyAbsorbers() {
        ItemList.MagicEnergyAbsorber_LV.set(
            new MTEMagicalEnergyAbsorber(
                MAGIC_ENERGY_ABSORBER_LV.ID,
                "basicgenerator.magicenergyabsorber.tier.01",
                "Novice Magic Energy Absorber",
                1).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_MV.set(
            new MTEMagicalEnergyAbsorber(
                MAGIC_ENERGY_ABSORBER_MV.ID,
                "basicgenerator.magicenergyabsorber.tier.02",
                "Adept Magic Energy Absorber",
                2).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_HV.set(
            new MTEMagicalEnergyAbsorber(
                MAGIC_ENERGY_ABSORBER_HV.ID,
                "basicgenerator.magicenergyabsorber.tier.03",
                "Master Magic Energy Absorber",
                3).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_EV.set(
            new MTEMagicalEnergyAbsorber(
                MAGIC_ENERGY_ABSORBER_EV.ID,
                "basicgenerator.magicenergyabsorber.tier.04",
                "Grandmaster Magic Energy Absorber",
                4).getStackForm(1L));
    }

    private static void registerPlasmaGenerators() {
        ItemList.Generator_Plasma_EV.set(
            new MTEPlasmaGenerator(
                PLASMA_GENERATOR_EV.ID,
                "basicgenerator.plasmagenerator.tier.05",
                "Plasma Generator Mark I",
                4).getStackForm(1L));
        ItemList.Generator_Plasma_IV.set(
            new MTEPlasmaGenerator(
                PLASMA_GENERATOR_IV.ID,
                "basicgenerator.plasmagenerator.tier.06",
                "Plasma Generator Mark II",
                5).getStackForm(1L));
        ItemList.Generator_Plasma_LuV.set(
            new MTEPlasmaGenerator(
                PLASMA_GENERATOR_LuV.ID,
                "basicgenerator.plasmagenerator.tier.07",
                "Plasma Generator Mark III",
                6).getStackForm(1L));

        ItemList.Generator_Plasma_ZPMV.set(
            new MTEPlasmaGenerator(
                PLASMA_GENERATOR_ZPM.ID,
                "basicgenerator.plasmagenerator.tier.08",
                "Plasma Generator Mark IV",
                7).getStackForm(1L));

        ItemList.Generator_Plasma_UV.set(
            new MTEPlasmaGenerator(
                PLASMA_GENERATOR_UV.ID,
                "basicgenerator.plasmagenerator.tier.09",
                "Ultimate Pocket Sun",
                8).getStackForm(1L));
    }

    private static void registerNameRemover() {
        ItemList.NameRemover
            .set(new MTENameRemover(NAME_REMOVER.ID, "fix.name.remover", "Name Remover", 0).getStackForm(1L));
    }

    private static void registerAirFilters() {
        ItemList.Machine_Multi_AirFilterT1.set(
            new MTEAirFilter1(AIR_FILTER_CONTROLLER_T1.ID, "multimachine.airfilter.01", "Electric Air Filter T1")
                .getStackForm(1L));
        ItemList.Machine_Multi_AirFilterT2.set(
            new MTEAirFilter2(AIR_FILTER_CONTROLLER_T2.ID, "multimachine.airfilter.02", "Electric Air Filter T2")
                .getStackForm(1L));
        ItemList.Machine_Multi_AirFilterT3.set(
            new MTEAirFilter3(AIR_FILTER_CONTROLLER_T3.ID, "multimachine.airfilter.03", "Electric Air Filter T3")
                .getStackForm(1L));
    }

    private static void registerVacuumComponents() {
        ItemList.Hatch_VacuumConveyor_Input.set(
            new MTEHatchVacuumConveyorInput(
                HATCH_VACUUM_CONVEYOR_INPUT.ID,
                "vacuum.hatch.input",
                "Vacuum Conveyor Input",
                1).getStackForm(1L));
        ItemList.Hatch_VacuumConveyor_Output.set(
            new MTEHatchVacuumConveyorOutput(
                HATCH_VACUUM_CONVEYOR_OUTPUT.ID,
                "vacuum.hatch.output",
                "Vacuum Conveyor Output",
                1).getStackForm(1L));
        ItemList.VacuumConveyorPipe.set(
            new MTEVacuumConveyorPipe(VACUUM_CONVEYOR_PIPE.ID, "vacuum.pipe", "Vacuum Conveyor Pipe").getStackForm(1L));
    }

    @Override
    public void run() {
        GTLog.out.println("GTMod: Registering MetaTileEntities.");
        registerMachineHull();
        registerTransformer();
        registerDynamoHatch();
        registerEnergyHatch();
        registerInputHatch();
        registerQuadrupleInputHatch();
        registerOutputHatch();
        registerVoidHatch();
        registerQuantumTank();
        registerQuantumChest();
        registerSuperTank();
        registerSuperChest();
        registerLongDistancePipe();
        registerAE2Hatches();
        registerMagHatch();
        registerInputBus();
        registerOutputBus();
        registerVoidBus();
        registerCokeOvenHatch();
        registerMufflerHatch();
        registerBoiler();
        registerBatteryBuffer1x1();
        registerBatteryBuffer2x2();
        registerBatteryBuffer3x3();
        registerBatteryBuffer4x4();
        registerCharger4x4();
        registerWirelessEnergyHatch();
        registerSteamMachines();
        registerHPSteamMachines();
        registerLocker();
        registerScanner();
        registerPackager();
        registerRockBreaker();
        registerIndustrialApiary();
        registerMassFab();
        registerReplicator();
        registerBrewery();
        registerMiner();
        registerPump();
        registerTeleporter();
        registerMonsterRepellator();
        registerMagLevPylon();
        registerAdvancedSeismicProspector();
        registerMicrowaveEnergyTransmitter();
        registerChestBuffer();
        registerItemFilter();
        registerTypeFilter();
        registerRegulator();
        registerSuperBuffer();
        registerItemDistributor();
        registerRecipeFilter();
        registerLightningRods();
        registerSolarPanels();
        registerCombustionGenerators();
        registerGasTurbines();
        registerSteamTurbines();
        registerNaquadahReactors();
        registerMagicEnergyAbsorbers();
        registerMagicEnergyConverters();
        registerPlasmaGenerators();
        registerMultiblockControllers();
        registerWorldAccelerator();
        registerAlloySmelter();
        registerMatterAmplifier();
        registerAssemblingMachine();
        registerWetTransformer();
        registerHighAmpTransformer();
        registerChemicalBath();
        registerChemicalReactor();
        registerFermenter();
        registerFluidCanner();
        registerFluidExtractor();
        registerFluidHeater();
        registerMixer();
        registerAutoclave();
        registerBendingMachine();
        registerCompressor();
        registerCuttingMachine();
        registerDistillery();
        registerElectricFurnace();
        registerElectromagneticSeparator();
        registerExtractor();
        registerExtruder();
        registerFluidSolidifier();
        registerFormingPress();
        registerForgeHammer();
        registerLathe();
        registerPrecisionLaserEngraver();
        registerMacerator();
        registerMatterFabricator();
        registerMicrowave();
        registerOreWashingPlant();
        registerPolarizer();
        registerRecycler();
        registerSiftingMachine();
        registerThermalCentrifuge();
        registerWiremill();
        registerArcFurnace();
        registerCentrifuge();
        registerPlasmaArcFurnace();
        registerCanningMachine();
        registerElectrolyzer();
        registerCircuitAssembler();
        registerTurboCharger4By4();
        registerBetterJukebox();
        registerUnpackager();
        registerPrinter();
        registerOven();
        registerNameRemover();
        registerAirFilters();
        registerVacuumComponents();

        ItemList.AdvDebugStructureWriter.set(
            new MTEAdvDebugStructureWriter(
                ADVANCED_DEBUG_STRUCTURE_WRITTER.ID,
                "advdebugstructurewriter",
                "Advanced Debug Structure Writer",
                5).getStackForm(1L));
        ItemList.Hatch_Maintenance.set(
            new MTEHatchMaintenance(MAINTENANCE_HATCH.ID, "hatch.maintenance", "Maintenance Hatch", 1)
                .getStackForm(1L));
        ItemList.Hatch_AutoMaintenance.set(
            new MTEHatchMaintenance(
                AUTO_MAINTENANCE_HATCH.ID,
                "hatch.maintenance.auto",
                "Auto Maintenance Hatch",
                6,
                true).getStackForm(1L));
        ItemList.Hatch_DroneDownLink.set(
            new MTEHatchDroneDownLink(DroneDownLink.ID, "hatch.dronedownlink", "Drone DownLink Module", 5)
                .getStackForm(1));
        ItemList.Hatch_DataAccess_EV.set(
            new MTEHatchDataAccess(DATA_ACCESS_HATCH.ID, "hatch.dataaccess", "Data Access Hatch", 4).getStackForm(1L));
        ItemList.Hatch_DataAccess_LuV.set(
            new MTEHatchDataAccess(
                ADVANCED_DATA_ACCESS_HATCH.ID,
                "hatch.dataaccess.adv",
                "Advanced Data Access Hatch",
                6).getStackForm(1L));
        ItemList.Hatch_DataAccess_UV.set(
            new MTEHatchDataAccess(
                AUTOMATABLE_DATA_ACCESS_HATCH.ID,
                "hatch.dataaccess.auto",
                "Automatable Data Access Hatch",
                8).getStackForm(1L));
        ItemList.Hatch_HeatSensor
            .set(new MTEHeatSensor(HEAT_DETECTOR_HATCH.ID, "hatch.heatsensor", "Heat Sensor Hatch", 7).getStackForm(1));
        ItemList.Hatch_BlackHoleUtility.set(
            new MTEBlackHoleUtility(
                HATCH_BLACK_HOLE_UTILITY.ID,
                "hatch.blackholeutility",
                "Black Hole Utility Hatch",
                11).getStackForm(1));
        ItemList.Hatch_pHSensor
            .set(new MTEHatchPHSensor(HATCH_PH_SENSOR.ID, "hatch.phsensor", "pH Sensor Hatch", 7).getStackForm(1));
        ItemList.Hatch_LensHousing
            .set(new MTEHatchLensHousing(HATCH_LENS_HOUSING.ID, "hatch.lenshousing", "Lens Housing").getStackForm(1L));
        ItemList.Hatch_LensIndicator.set(
            new MTEHatchLensIndicator(HATCH_LENS_INDICATOR.ID, "hatch.lensindicator", "Lens Indicator Hatch", 8)
                .getStackForm(1L));
        ItemList.Hatch_Nanite.set(
            new MTEHatchNanite(HATCH_NANITE.ID, "hatch.nanite", "Nanite Containment Bus", 9, 2048).getStackForm(1));
        ItemList.Hatch_Catalyst_Bulk.set(
            new MTEHatchBulkCatalystHousing(
                HATCH_CATALYST_BULK.ID,
                "hatch.catalystbulk",
                "Bulk Catalyst Housing",
                10,
                Integer.MAX_VALUE).getStackForm(1));

        ItemList.LargeMolecularAssembler.set(
            new MTELargeMolecularAssembler(
                LargeMolecularAssembler.ID,
                "largemolecularassembler",
                "Large Molecular Assembler").getStackForm(1));
    }

}
