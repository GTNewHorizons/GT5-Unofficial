package gregtech.loaders.preload;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.chain;
import static gregtech.api.enums.MetaTileEntityIDs.*;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.StorageDrawers;
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
import gregtech.api.enums.VoltageIndex;
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
import gregtech.api.metatileentity.implementations.MTEHatchInputBusCompressed;
import gregtech.api.metatileentity.implementations.MTEHatchInputBusDebug;
import gregtech.api.metatileentity.implementations.MTEHatchInputDebug;
import gregtech.api.metatileentity.implementations.MTEHatchMagnet;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.metatileentity.implementations.MTEHatchNanite;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBusCompressed;
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
import gregtech.common.tileentities.machines.basic.MTEAdvSeismicProspector;
import gregtech.common.tileentities.machines.basic.MTEBasicMachineWithRecipeBuilder;
import gregtech.common.tileentities.machines.basic.MTEBetterJukebox;
import gregtech.common.tileentities.machines.basic.MTEBoxinator;
import gregtech.common.tileentities.machines.basic.MTECharger;
import gregtech.common.tileentities.machines.basic.MTEDrawerFramer;
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
import gregtech.common.tileentities.machines.multi.MTEIndustrialCentrifuge;
import gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator;
import gregtech.common.tileentities.machines.multi.MTEIndustrialExtractor;
import gregtech.common.tileentities.machines.multi.MTEIndustrialLaserEngraver;
import gregtech.common.tileentities.machines.multi.MTEIndustrialPackager;
import gregtech.common.tileentities.machines.multi.MTEIndustrialWireMill;
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
import gregtech.common.tileentities.machines.multi.MTEMegaChemicalReactor;
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
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gregtech.common.tileentities.machines.multi.nanochip.MTEVacuumConveyorPipe;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchSplitterRedstone;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorOutput;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEAssemblyMatrixModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEBiologicalCoordinationModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEBoardProcessorModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTECuttingChamberModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEEncasementWrapperModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEEtchingArrayModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEOpticalOrganizerModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTESMDProcessorModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTESplitterModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTESuperconductorSplitterModule;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEWireTracerModule;
import gregtech.common.tileentities.machines.multi.pcb.MTEPCBBioChamber;
import gregtech.common.tileentities.machines.multi.pcb.MTEPCBCoolingTower;
import gregtech.common.tileentities.machines.multi.pcb.MTEPCBFactory;
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
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;
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
        addItemTooltip(
            ItemList.Machine_Multi_PurificationPlant.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
        ItemList.Machine_Multi_PurificationUnitClarifier.set(
            new MTEPurificationUnitClarifier(
                PURIFICATION_UNIT_CLARIFIER.ID,
                "multimachine.purificationunitclarifier",
                "Clarifier Purification Unit").getStackForm(1L));
        addItemTooltip(
            ItemList.Machine_Multi_PurificationUnitClarifier.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
        ItemList.Machine_Multi_PurificationUnitFlocculator.set(
            new MTEPurificationUnitFlocculation(
                PURIFICATION_UNIT_FLOCCULATOR.ID,
                "multimachine.purificationunitflocculator",
                "Flocculation Purification Unit").getStackForm(1L));
        addItemTooltip(
            ItemList.Machine_Multi_PurificationUnitFlocculator.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
        ItemList.Machine_Multi_PurificationUnitPhAdjustment.set(
            new MTEPurificationUnitPhAdjustment(
                PURIFICATION_UNIT_PH_ADJUSTMENT.ID,
                "multimachine.purificationunitphadjustment",
                "pH Neutralization Purification Unit").getStackForm(1L));
        addItemTooltip(
            ItemList.Machine_Multi_PurificationUnitPhAdjustment.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
        ItemList.Machine_Multi_PurificationUnitOzonation.set(
            new MTEPurificationUnitOzonation(
                PURIFICATION_UNIT_OZONATION.ID,
                "multimachine.purificationunitozonation",
                "Ozonation Purification Unit").getStackForm(1L));
        addItemTooltip(
            ItemList.Machine_Multi_PurificationUnitOzonation.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
        ItemList.Machine_Multi_PurificationUnitPlasmaHeater.set(
            new MTEPurificationUnitPlasmaHeater(
                PURIFICATION_UNIT_PLASMA_HEATER.ID,
                "multimachine.purificationunitplasmaheater",
                "Extreme Temperature Fluctuation Purification Unit").getStackForm(1L));
        addItemTooltip(
            ItemList.Machine_Multi_PurificationUnitPlasmaHeater.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
        ItemList.Machine_Multi_PurificationUnitUVTreatment.set(
            new MTEPurificationUnitUVTreatment(
                PURIFICATION_UNIT_UV_TREATMENT.ID,
                "multimachine.purificationunituvtreatment",
                "High Energy Laser Purification Unit").getStackForm(1L));
        addItemTooltip(
            ItemList.Machine_Multi_PurificationUnitUVTreatment.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
        ItemList.Machine_Multi_PurificationUnitDegasifier.set(
            new MTEPurificationUnitDegasser(
                PURIFICATION_UNIT_DEGASIFIER.ID,
                "multimachine.purificationunitdegasifier",
                "Residual Decontaminant Degasser Purification Unit").getStackForm(1L));
        addItemTooltip(
            ItemList.Machine_Multi_PurificationUnitDegasifier.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
        ItemList.Machine_Multi_PurificationUnitParticleExtractor.set(
            new MTEPurificationUnitBaryonicPerfection(
                PURIFICATION_UNIT_PARTICLE_EXTRACTOR.ID,
                "multimachine.purificationunitextractor",
                "Absolute Baryonic Perfection Purification Unit").getStackForm(1L));
        addItemTooltip(
            ItemList.Machine_Multi_PurificationUnitParticleExtractor.get(1),
            chain(GTValues.AUTHOR_SUPPLIER, GTValues.AuthorNotAPenguinAnimated));
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
        addItemTooltip(
            ItemList.Machine_Multi_NanochipAssemblyComplex.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_AssemblyMatrix.set(
            new MTEAssemblyMatrixModule(
                NANOCHIP_MODULE_ASSEMBLY_MATRIX.ID,
                "multimachine.nanochipmodule.assemblymatrix",
                "Nanochip Assembly Matrix").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_AssemblyMatrix.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_SMDProcessor.set(
            new MTESMDProcessorModule(
                NANOCHIP_MODULE_SMD_PROCESSOR.ID,
                "multimachine.nanochipmodule.smdprocessor",
                "Part Preparation Apparatus").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_SMDProcessor.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_BoardProcessor.set(
            new MTEBoardProcessorModule(
                NANOCHIP_MODULE_BOARD_PROCESSOR.ID,
                "multimachine.nanochipmodule.boadprocessor",
                "Full-Board Immersion Device").getStackForm(1));
        addItemTooltip(ItemList.NanoChipModule_BoardProcessor.get(1), chain(() -> "Author: ", GTValues.AuthorNoc));
        ItemList.NanoChipModule_EtchingArray.set(
            new MTEEtchingArrayModule(
                NANOCHIP_MODULE_ETCHING_ARRAY.ID,
                "multimachine.nanochipmodule.etchingarray",
                "Ultra-high Energy Etching Array").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_EtchingArray.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_CuttingChamber.set(
            new MTECuttingChamberModule(
                NANOCHIP_MODULE_CUTTING_CHAMBER.ID,
                "multimachine.nanochipmodule.cuttingchamber",
                "Nanoprecision Cutting Chamber").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_CuttingChamber.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_WireTracer.set(
            new MTEWireTracerModule(
                NANOCHIP_MODULE_WIRE_TRACER.ID,
                "multimachine.nanochipmodule.wiretracer",
                "Nanoprecision Wire Tracer").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_WireTracer.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_SuperconductorSplitter.set(
            new MTESuperconductorSplitterModule(
                NANOCHIP_MODULE_SUPERCONDUCTOR_SPLITTER.ID,
                "multimachine.nanochipmodule.superconductorsplitter",
                "Superconductive Strand Splitter").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_SuperconductorSplitter.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_Splitter.set(
            new MTESplitterModule(
                NANOCHIP_MODULE_SPLITTER.ID,
                "multimachine.nanochipmodule.splitter",
                "Nanopart Splitter").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_Splitter.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_OpticalOrganizer.set(
            new MTEOpticalOrganizerModule(
                NANOCHIP_MODULE_OPTICAL_ORGANIZER.ID,
                "multimachine.nanochipmodule.opticalorganizer",
                "Optically Optimized Organizer").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_OpticalOrganizer.get(1),
            chain(() -> "Author: ", GTValues.fancyAuthorChrom));
        ItemList.NanoChipModule_EncasementWrapper.set(
            new MTEEncasementWrapperModule(
                NANOCHIP_MODULE_ENCASEMENT_WRAPPER.ID,
                "multimachine.nanochipmodule.encasementwrapper",
                "Nanometer Encasement Wrapper").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_EncasementWrapper.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));
        ItemList.NanoChipModule_BiologicalCoordinator.set(
            new MTEBiologicalCoordinationModule(
                NANOCHIP_MODULE_BIOLOGICAL_COORDINATOR.ID,
                "multimachine.nanochipmodule.biologicalcoordinator",
                "Accelerated Biological Coordinator").getStackForm(1));
        addItemTooltip(
            ItemList.NanoChipModule_BiologicalCoordinator.get(1),
            chain(() -> "Author: ", GTValues.AuthorNotAPenguinAnimated));

        if (Thaumcraft.isModLoaded()) {
            ItemList.ResearchCompleter.set(
                new MTEResearchCompleter(ResearchCompleter.ID, "Research Completer", "Research Completer")
                    .getStackForm(1));
        }

        ItemList.IndustrialWireFactory.set(
            new MTEIndustrialWireMill(
                IndustrialWireMill.ID,
                "industrialwiremill.controller.tier.single",
                "Industrial Wire Factory").getStackForm(1));

        ItemList.IndustrialPackager.set(
            new MTEIndustrialPackager(
                IndustrialPackager.ID,
                "amazonprime.controller.tier.single",
                "Amazon Warehousing Depot").getStackForm(1L));

        ItemList.IndustrialCentrifuge.set(
            new MTEIndustrialCentrifuge(
                IndustrialCentrifuge.ID,
                "industrialcentrifuge.controller.tier.single",
                "Industrial Centrifuge").getStackForm(1));

        ItemList.MegaChemicalReactor.set(
            new MTEMegaChemicalReactor(
                MegaChemicalReactor.ID,
                "multimachine.mega-chemical-reactor",
                "Mega Chemical Reactor").getStackForm(1));
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
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(UNPACKAGER_LV.ID)
                .setName("basicmachine.unboxinator.tier.01", "Basic Unpackager")
                .setTier(1)
                .setDescription(MachineType.UNPACKAGER.tooltipDescription())
                .setRecipes(RecipeMaps.unpackagerRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("UNBOXINATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Unboxinator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(UNPACKAGER_MV.ID)
                .setName("basicmachine.unboxinator.tier.02", "Advanced Unpackager")
                .setTier(2)
                .setDescription(MachineType.UNPACKAGER.tooltipDescription())
                .setRecipes(RecipeMaps.unpackagerRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("UNBOXINATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Unboxinator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(UNPACKAGER_HV.ID)
                .setName("basicmachine.unboxinator.tier.03", "Advanced Unpackager II")
                .setTier(3)
                .setDescription(MachineType.UNPACKAGER.tooltipDescription())
                .setRecipes(RecipeMaps.unpackagerRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("UNBOXINATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Unboxinator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(UNPACKAGER_EV.ID)
                .setName("basicmachine.unboxinator.tier.04", "Advanced Unpackager III")
                .setTier(4)
                .setDescription(MachineType.UNPACKAGER.tooltipDescription())
                .setRecipes(RecipeMaps.unpackagerRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("UNBOXINATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Unboxinator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(UNPACKAGER_IV.ID)
                .setName("basicmachine.unboxinator.tier.05", "Unboxinator")
                .setTier(5)
                .setDescription(MachineType.UNPACKAGER.tooltipDescription())
                .setRecipes(RecipeMaps.unpackagerRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("UNBOXINATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_LuV_Unboxinator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(UNPACKAGER_LuV.ID)
                .setName("basicmachine.unboxinator.tier.06", "Unboxinator")
                .setTier(6)
                .setDescription(MachineType.UNPACKAGER.tooltipDescription())
                .setRecipes(RecipeMaps.unpackagerRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("UNBOXINATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_ZPM_Unboxinator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(UNPACKAGER_ZPM.ID)
                .setName("basicmachine.unboxinator.tier.07", "Unboxinator")
                .setTier(7)
                .setDescription(MachineType.UNPACKAGER.tooltipDescription())
                .setRecipes(RecipeMaps.unpackagerRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("UNBOXINATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_UV_Unboxinator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(UNPACKAGER_UV.ID)
                .setName("basicmachine.unboxinator.tier.08", "Unboxinator")
                .setTier(8)
                .setDescription(MachineType.UNPACKAGER.tooltipDescription())
                .setRecipes(RecipeMaps.unpackagerRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("UNBOXINATOR")
                .build()
                .getStackForm(1L));
    }

    private static void registerAssemblingMachine() {

        ItemList.Machine_LV_Assembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLER_LV.ID)
                .setName("basicmachine.assembler.tier.01", "Basic Assembling Machine")
                .setTier(1)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Assembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLER_MV.ID)
                .setName("basicmachine.assembler.tier.02", "Advanced Assembling Machine")
                .setTier(2)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Assembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLER_HV.ID)
                .setName("basicmachine.assembler.tier.03", "Advanced Assembling Machine II")
                .setTier(3)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Assembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLER_EV.ID)
                .setName("basicmachine.assembler.tier.04", "Advanced Assembling Machine III")
                .setTier(4)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Assembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLER_IV.ID)
                .setName("basicmachine.assembler.tier.05", "Advanced Assembling Machine IV")
                .setTier(5)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.AssemblingMachineLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLING_MACHINE_LuV.ID)
                .setName("basicmachine.assembler.tier.06", "Elite Assembling Machine")
                .setTier(6)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.AssemblingMachineZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLING_MACHINE_ZPM.ID)
                .setName("basicmachine.assembler.tier.07", "Elite Assembling Machine II")
                .setTier(7)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.AssemblingMachineUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLING_MACHINE_UV.ID)
                .setName("basicmachine.assembler.tier.08", "Ultimate Assembly Constructor")
                .setTier(8)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.AssemblingMachineUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLING_MACHINE_UHV.ID)
                .setName("basicmachine.assembler.tier.09", "Epic Assembly Constructor")
                .setTier(9)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.AssemblingMachineUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLING_MACHINE_UEV.ID)
                .setName("basicmachine.assembler.tier.10", "Epic Assembly Constructor II")
                .setTier(10)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.AssemblingMachineUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLING_MACHINE_UIV.ID)
                .setName("basicmachine.assembler.tier.11", "Epic Assembly Constructor III")
                .setTier(11)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.AssemblingMachineUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ASSEMBLING_MACHINE_UMV.ID)
                .setName("basicmachine.assembler.tier.12", "Epic Assembly Constructor IV")
                .setTier(12)
                .setDescription(MachineType.ASSEMBLER.tooltipDescription())
                .setRecipes(assemblerRecipes)
                .setSlotsCount(9, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("ASSEMBLER")
                .build()
                .getStackForm(1L));
    }

    private static void registerMatterAmplifier() {
        ItemList.Machine_LV_Amplifab.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_LV.ID)
                .setName("basicmachine.amplifab.tier.01", "Basic Amplifabricator")
                .setTier(1)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Amplifab.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_MV.ID)
                .setName("basicmachine.amplifab.tier.02", "Advanced Amplifabricator")
                .setTier(2)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Amplifab.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_HV.ID)
                .setName("basicmachine.amplifab.tier.03", "Advanced Amplifabricator II")
                .setTier(3)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Amplifab.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_EV.ID)
                .setName("basicmachine.amplifab.tier.04", "Advanced Amplifabricator III")
                .setTier(4)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Amplifab.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_IV.ID)
                .setName("basicmachine.amplifab.tier.05", "Advanced Amplifabricator IV")
                .setTier(5)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.AmplifabricatorLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_LuV.ID)
                .setName("basicmachine.amplifab.tier.06", "Elite Amplifabricator")
                .setTier(6)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.AmplifabricatorZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_ZPM.ID)
                .setName("basicmachine.amplifab.tier.07", "Elite Amplifabricator II")
                .setTier(7)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.AmplifabricatorUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_UV.ID)
                .setName("basicmachine.amplifab.tier.08", "Ultimate Amplicreator")
                .setTier(8)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.AmplifabricatorUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_UHV.ID)
                .setName("basicmachine.amplifab.tier.09", "Epic Amplicreator")
                .setTier(9)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.AmplifabricatorUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_UEV.ID)
                .setName("basicmachine.amplifab.tier.10", "Epic Amplicreator II")
                .setTier(10)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.AmplifabricatorUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_UIV.ID)
                .setName("basicmachine.amplifab.tier.11", "Epic Amplicreator III")
                .setTier(11)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));

        ItemList.AmplifabricatorUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MATTER_AMPLIFIER_UMV.ID)
                .setName("basicmachine.amplifab.tier.12", "Epic Amplicreator IV")
                .setTier(12)
                .setDescription(MachineType.MATTER_AMPLIFIER.tooltipDescription())
                .setRecipes(amplifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true, 1000)
                .setSound(SoundResource.GTCEU_LOOP_REPLICATOR)
                .setOverlays("AMPLIFAB")
                .build()
                .getStackForm(1L));
    }

    private static void registerAlloySmelter() {
        ItemList.Machine_LV_AlloySmelter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_LV.ID)
                .setName("basicmachine.alloysmelter.tier.01", "Basic Alloy Smelter")
                .setTier(1)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_AlloySmelter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_MV.ID)
                .setName("basicmachine.alloysmelter.tier.02", "Advanced Alloy Smelter")
                .setTier(2)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_AlloySmelter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_HV.ID)
                .setName("basicmachine.alloysmelter.tier.03", "Advanced Alloy Smelter II")
                .setTier(3)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_AlloySmelter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_EV.ID)
                .setName("basicmachine.alloysmelter.tier.04", "Advanced Alloy Smelter III")
                .setTier(4)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_AlloySmelter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_IV.ID)
                .setName("basicmachine.alloysmelter.tier.05", "Advanced Alloy Smelter IV")
                .setTier(5)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.AlloySmelterLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_LuV.ID)
                .setName("basicmachine.alloysmelter.tier.06", "Elite Alloy Smelter")
                .setTier(6)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.AlloySmelterZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_ZPM.ID)
                .setName("basicmachine.alloysmelter.tier.07", "Elite Alloy Smelter II")
                .setTier(7)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.AlloySmelterUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_UV.ID)
                .setName("basicmachine.alloysmelter.tier.08", "Ultimate Alloy Integrator")
                .setTier(8)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.AlloySmelterUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_UHV.ID)
                .setName("basicmachine.alloysmelter.tier.09", "Epic Alloy Integrator")
                .setTier(9)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.AlloySmelterUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_UEV.ID)
                .setName("basicmachine.alloysmelter.tier.10", "Epic Alloy Integrator II")
                .setTier(10)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.AlloySmelterUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_UIV.ID)
                .setName("basicmachine.alloysmelter.tier.11", "Epic Alloy Integrator III")
                .setTier(11)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));

        ItemList.AlloySmelterUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ALLOY_SMELTER_UMV.ID)
                .setName("basicmachine.alloysmelter.tier.12", "Epic Alloy Integrator IV")
                .setTier(12)
                .setDescription(MachineType.ALLOY_SMELTER.tooltipDescription())
                .setRecipes(alloySmelterRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("ALLOY_SMELTER")
                .build()
                .getStackForm(1L));
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
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_LV.ID)
                .setName("basicmachine.chemicalbath.tier.01", "Basic Chemical Bath")
                .setTier(1)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_ChemicalBath.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_MV.ID)
                .setName("basicmachine.chemicalbath.tier.02", "Advanced Chemical Bath")
                .setTier(2)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_ChemicalBath.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_HV.ID)
                .setName("basicmachine.chemicalbath.tier.03", "Advanced Chemical Bath II")
                .setTier(3)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_ChemicalBath.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_EV.ID)
                .setName("basicmachine.chemicalbath.tier.04", "Advanced Chemical Bath III")
                .setTier(4)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_ChemicalBath.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_IV.ID)
                .setName("basicmachine.chemicalbath.tier.05", "Advanced Chemical Bath IV")
                .setTier(5)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalBathLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_LuV.ID)
                .setName("basicmachine.chemicalbath.tier.06", "Elite Chemical Bath")
                .setTier(6)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalBathZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_ZPM.ID)
                .setName("basicmachine.chemicalbath.tier.07", "Elite Chemical Bath II")
                .setTier(7)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalBathUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_UV.ID)
                .setName("basicmachine.chemicalbath.tier.08", "Ultimate Chemical Dunktron")
                .setTier(8)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalBathUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_UHV.ID)
                .setName("basicmachine.chemicalbath.tier.09", "Epic Chemical Dunktron")
                .setTier(9)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalBathUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_UEV.ID)
                .setName("basicmachine.chemicalbath.tier.10", "Epic Chemical Dunktron II")
                .setTier(10)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalBathUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_UIV.ID)
                .setName("basicmachine.chemicalbath.tier.11", "Epic Chemical Dunktron III")
                .setTier(11)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalBathUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_BATH_UMV.ID)
                .setName("basicmachine.chemicalbath.tier.12", "Epic Chemical Dunktron IV")
                .setTier(12)
                .setDescription(MachineType.CHEMICAL_BATH.tooltipDescription())
                .setRecipes(chemicalBathRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CHEMICAL_BATH")
                .build()
                .getStackForm(1L));
    }

    private void registerChemicalReactor() {
        ItemList.Machine_LV_ChemicalReactor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_LV.ID)
                .setName("basicmachine.chemicalreactor.tier.01", "Basic Chemical Reactor")
                .setTier(1)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_ChemicalReactor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_MV.ID)
                .setName("basicmachine.chemicalreactor.tier.02", "Advanced Chemical Reactor")
                .setTier(2)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_ChemicalReactor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_HV.ID)
                .setName("basicmachine.chemicalreactor.tier.03", "Advanced Chemical Reactor II")
                .setTier(3)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_ChemicalReactor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_EV.ID)
                .setName("basicmachine.chemicalreactor.tier.04", "Advanced Chemical Reactor III")
                .setTier(4)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_ChemicalReactor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_IV.ID)
                .setName("basicmachine.chemicalreactor.tier.05", "Advanced Chemical Reactor IV")
                .setTier(5)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalReactorLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_LuV.ID)
                .setName("basicmachine.chemicalreactor.tier.06", "Elite Chemical Reactor")
                .setTier(6)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalReactorZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_ZPM.ID)
                .setName("basicmachine.chemicalreactor.tier.07", "Elite Chemical Reactor II")
                .setTier(7)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalReactorUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_UV.ID)
                .setName("basicmachine.chemicalreactor.tier.08", "Ultimate Chemical Perforer")
                .setTier(8)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalReactorUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_UHV.ID)
                .setName("basicmachine.chemicalreactor.tier.09", "Epic Chemical Performer")
                .setTier(9)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalReactorUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_UEV.ID)
                .setName("basicmachine.chemicalreactor.tier.10", "Epic Chemical Performer II")
                .setTier(10)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalReactorUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_UIV.ID)
                .setName("basicmachine.chemicalreactor.tier.11", "Epic Chemical Performer III")
                .setTier(11)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ChemicalReactorUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CHEMICAL_REACTOR_UMV.ID)
                .setName("basicmachine.chemicalreactor.tier.12", "Epic Chemical Performer IV")
                .setTier(12)
                .setDescription(MachineType.CHEMICAL_REACTOR.tooltipDescription())
                .setRecipes(chemicalReactorRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("CHEMICAL_REACTOR")
                .build()
                .getStackForm(1L));

    }

    private void registerFermenter() {
        ItemList.Machine_LV_Fermenter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_LV.ID)
                .setName("basicmachine.fermenter.tier.01", "Basic Fermenter")
                .setTier(1)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Fermenter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_MV.ID)
                .setName("basicmachine.fermenter.tier.02", "Advanced Fermenter")
                .setTier(2)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Fermenter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_HV.ID)
                .setName("basicmachine.fermenter.tier.03", "Advanced Fermenter II")
                .setTier(3)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Fermenter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_EV.ID)
                .setName("basicmachine.fermenter.tier.04", "Advanced Fermenter III")
                .setTier(4)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Fermenter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_IV.ID)
                .setName("basicmachine.fermenter.tier.05", "Advanced Fermenter IV")
                .setTier(5)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.FermenterLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_LuV.ID)
                .setName("basicmachine.fermenter.tier.06", "Elite Fermenter")
                .setTier(6)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.FermenterZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_ZPM.ID)
                .setName("basicmachine.fermenter.tier.07", "Elite Fermenter II")
                .setTier(7)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.FermenterUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_UV.ID)
                .setName("basicmachine.fermenter.tier.08", "Ultimate Fermentation Hastener")
                .setTier(8)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.FermenterUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_UHV.ID)
                .setName("basicmachine.fermenter.tier.09", "Epic Fermentation Hastener")
                .setTier(9)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.FermenterUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_UEV.ID)
                .setName("basicmachine.fermenter.tier.10", "Epic Fermentation Hastener II")
                .setTier(10)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.FermenterUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_UIV.ID)
                .setName("basicmachine.fermenter.tier.11", "Epic Fermentation Hastener III")
                .setTier(11)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));

        ItemList.FermenterUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FERMENTER_UMV.ID)
                .setName("basicmachine.fermenter.tier.12", "Epic Fermentation Hastener IV")
                .setTier(12)
                .setDescription(MachineType.FERMENTER.tooltipDescription())
                .setRecipes(fermentingRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("FERMENTER")
                .build()
                .getStackForm(1L));
    }

    private void registerFluidCanner() {
        ItemList.Machine_LV_FluidCanner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_LV.ID)
                .setName("basicmachine.fluidcanner.tier.01", "Basic Fluid Canner")
                .setTier(1)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_FluidCanner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_MV.ID)
                .setName("basicmachine.fluidcanner.tier.02", "Advanced Fluid Canner")
                .setTier(2)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_FluidCanner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_HV.ID)
                .setName("basicmachine.fluidcanner.tier.03", "Quick Fluid Canner")
                .setTier(3)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_FluidCanner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_EV.ID)
                .setName("basicmachine.fluidcanner.tier.04", "Turbo Fluid Canner")
                .setTier(4)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_FluidCanner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_IV.ID)
                .setName("basicmachine.fluidcanner.tier.05", "Instant Fluid Canner")
                .setTier(5)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.FluidCannerLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_LuV.ID)
                .setName("basicmachine.fluidcanner.tier.06", "Elite Fluid Canner")
                .setTier(6)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.FluidCannerZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_ZPM.ID)
                .setName("basicmachine.fluidcanner.tier.07", "Elite Fluid Canner II")
                .setTier(7)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.FluidCannerUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_UV.ID)
                .setName("basicmachine.fluidcanner.tier.08", "Ultimate Liquid Can Actuator")
                .setTier(8)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.FluidCannerUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_UHV.ID)
                .setName("basicmachine.fluidcanner.tier.09", "Epic Liquid Can Actuator")
                .setTier(9)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.FluidCannerUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_UEV.ID)
                .setName("basicmachine.fluidcanner.tier.10", "Epic Liquid Can Actuator II")
                .setTier(10)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.FluidCannerUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_UIV.ID)
                .setName("basicmachine.fluidcanner.tier.11", "Epic Liquid Can Actuator III")
                .setTier(11)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));

        ItemList.FluidCannerUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_CANNER_UMV.ID)
                .setName("basicmachine.fluidcanner.tier.12", "Epic Liquid Can Actuator IV")
                .setTier(12)
                .setDescription(MachineType.FLUID_CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("FLUID_CANNER")
                .build()
                .getStackForm(1L));
    }

    private void registerFluidExtractor() {
        ItemList.Machine_LV_FluidExtractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_LV.ID)
                .setName("basicmachine.fluidextractor.tier.01", "Basic Fluid Extractor")
                .setTier(1)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_FluidExtractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_MV.ID)
                .setName("basicmachine.fluidextractor.tier.02", "Advanced Fluid Extractor")
                .setTier(2)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_FluidExtractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_HV.ID)
                .setName("basicmachine.fluidextractor.tier.03", "Advanced Fluid Extractor II")
                .setTier(3)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_FluidExtractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_EV.ID)
                .setName("basicmachine.fluidextractor.tier.04", "Advanced Fluid Extractor III")
                .setTier(4)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_FluidExtractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_IV.ID)
                .setName("basicmachine.fluidextractor.tier.05", "Advanced Fluid Extractor IV")
                .setTier(5)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.FluidExtractorLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_LuV.ID)
                .setName("basicmachine.fluidextractor.tier.06", "Elite Fluid Extractor")
                .setTier(6)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.FluidExtractorZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_ZPM.ID)
                .setName("basicmachine.fluidextractor.tier.07", "Elite Fluid Extractor II")
                .setTier(7)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.FluidExtractorUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_UV.ID)
                .setName("basicmachine.fluidextractor.tier.08", "Ultimate Liquefying Sucker")
                .setTier(8)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.FluidExtractorUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_UHV.ID)
                .setName("basicmachine.fluidextractor.tier.09", "Epic Liquefying Sucker")
                .setTier(9)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.FluidExtractorUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_UEV.ID)
                .setName("basicmachine.fluidextractor.tier.10", "Epic Liquefying Sucker II")
                .setTier(10)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.FluidExtractorUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_UIV.ID)
                .setName("basicmachine.fluidextractor.tier.11", "Epic Liquefying Sucker III")
                .setTier(11)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.FluidExtractorUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_EXTRACTOR_UMV.ID)
                .setName("basicmachine.fluidextractor.tier.12", "Epic Liquefying Sucker IV")
                .setTier(12)
                .setDescription(MachineType.FLUID_EXTRACTOR.tooltipDescription())
                .setRecipes(fluidExtractionRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(false, true)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("FLUID_EXTRACTOR")
                .build()
                .getStackForm(1L));
    }

    private void registerFluidHeater() {
        ItemList.Machine_LV_FluidHeater.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_LV.ID)
                .setName("basicmachine.fluidheater.tier.01", "Basic Fluid Heater")
                .setTier(1)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_FluidHeater.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_MV.ID)
                .setName("basicmachine.fluidheater.tier.02", "Advanced Fluid Heater")
                .setTier(2)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_FluidHeater.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_HV.ID)
                .setName("basicmachine.fluidheater.tier.03", "Advanced Fluid Heater II")
                .setTier(3)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_FluidHeater.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_EV.ID)
                .setName("basicmachine.fluidheater.tier.04", "Advanced Fluid Heater III")
                .setTier(4)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_FluidHeater.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_IV.ID)
                .setName("basicmachine.fluidheater.tier.05", "Advanced Fluid Heater IV")
                .setTier(5)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.FluidHeaterLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_LuV.ID)
                .setName("basicmachine.fluidheater.tier.06", "Elite Fluid Heater")
                .setTier(6)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.FluidHeaterZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_ZPM.ID)
                .setName("basicmachine.fluidheater.tier.07", "Elite Fluid Heater II")
                .setTier(7)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.FluidHeaterUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_UV.ID)
                .setName("basicmachine.fluidheater.tier.08", "Ultimate Heat Infuser")
                .setTier(8)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.FluidHeaterUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_UHV.ID)
                .setName("basicmachine.fluidheater.tier.09", "Epic Heat Infuser")
                .setTier(9)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.FluidHeaterUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_UEV.ID)
                .setName("basicmachine.fluidheater.tier.10", "Epic Heat Infuser II")
                .setTier(10)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.FluidHeaterUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_UIV.ID)
                .setName("basicmachine.fluidheater.tier.11", "Epic Heat Infuser III")
                .setTier(11)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));

        ItemList.FluidHeaterUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_HEATER_UMV.ID)
                .setName("basicmachine.fluidheater.tier.12", "Epic Heat Infuser IV")
                .setTier(12)
                .setDescription(MachineType.FLUID_HEATER.tooltipDescription())
                .setRecipes(fluidHeaterRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BOILER)
                .setOverlays("FLUID_HEATER")
                .build()
                .getStackForm(1L));
    }

    private void registerMixer() {
        ItemList.Machine_LV_Mixer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_LV.ID)
                .setName("basicmachine.mixer.tier.01", "Basic Mixer")
                .setTier(1)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Mixer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_MV.ID)
                .setName("basicmachine.mixer.tier.02", "Advanced Mixer")
                .setTier(2)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Mixer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_HV.ID)
                .setName("basicmachine.mixer.tier.03", "Advanced Mixer II")
                .setTier(3)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(6, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Mixer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_EV.ID)
                .setName("basicmachine.mixer.tier.04", "Advanced Mixer III")
                .setTier(4)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Mixer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_IV.ID)
                .setName("basicmachine.mixer.tier.05", "Advanced Mixer IV")
                .setTier(5)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.MixerLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_LuV.ID)
                .setName("basicmachine.mixer.tier.06", "Elite Mixer")
                .setTier(6)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.MixerZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_ZPM.ID)
                .setName("basicmachine.mixer.tier.07", "Elite Mixer II")
                .setTier(7)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.MixerUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_UV.ID)
                .setName("basicmachine.mixer.tier.08", "Ultimate Matter Organizer")
                .setTier(8)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.MixerUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_UHV.ID)
                .setName("basicmachine.mixer.tier.09", "Epic Matter Organizer")
                .setTier(9)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.MixerUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_UEV.ID)
                .setName("basicmachine.mixer.tier.10", "Epic Matter Organizer II")
                .setTier(10)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.MixerUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_UIV.ID)
                .setName("basicmachine.mixer.tier.11", "Epic Matter Organizer III")
                .setTier(11)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));

        ItemList.MixerUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MIXER_UMV.ID)
                .setName("basicmachine.mixer.tier.12", "Epic Matter Organizer IV")
                .setTier(12)
                .setDescription(MachineType.MIXER.tooltipDescription())
                .setRecipes(mixerRecipes)
                .setSlotsCount(9, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_MIXER)
                .setOverlays("MIXER")
                .build()
                .getStackForm(1L));
    }

    private void registerAutoclave() {
        ItemList.Machine_LV_Autoclave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_LV.ID)
                .setName("basicmachine.autoclave.tier.01", "Basic Autoclave")
                .setTier(1)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Autoclave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_MV.ID)
                .setName("basicmachine.autoclave.tier.02", "Advanced Autoclave")
                .setTier(2)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Autoclave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_HV.ID)
                .setName("basicmachine.autoclave.tier.03", "Advanced Autoclave II")
                .setTier(3)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 3)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Autoclave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_EV.ID)
                .setName("basicmachine.autoclave.tier.04", "Advanced Autoclave III")
                .setTier(4)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Autoclave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_IV.ID)
                .setName("basicmachine.autoclave.tier.05", "Advanced Autoclave IV")
                .setTier(5)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.AutoclaveLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_LuV.ID)
                .setName("basicmachine.autoclave.tier.06", "Elite Autoclave")
                .setTier(6)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.AutoclaveZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_ZPM.ID)
                .setName("basicmachine.autoclave.tier.07", "Elite Autoclave II")
                .setTier(7)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.AutoclaveUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_UV.ID)
                .setName("basicmachine.autoclave.tier.08", "Ultimate Pressure Cooker")
                .setTier(8)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.AutoclaveUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_UHV.ID)
                .setName("basicmachine.autoclave.tier.09", "Epic Pressure Cooker")
                .setTier(9)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.AutoclaveUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_UEV.ID)
                .setName("basicmachine.autoclave.tier.10", "Epic Pressure Cooker II")
                .setTier(10)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.AutoclaveUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_UIV.ID)
                .setName("basicmachine.autoclave.tier.11", "Epic Pressure Cooker III")
                .setTier(11)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

        ItemList.AutoclaveUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(AUTOCLAVE_UMV.ID)
                .setName("basicmachine.autoclave.tier.12", "Epic Pressure Cooker IV")
                .setTier(12)
                .setDescription(MachineType.AUTOCLAVE.tooltipDescription())
                .setRecipes(autoclaveRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("AUTOCLAVE")
                .build()
                .getStackForm(1L));

    }

    private void registerBendingMachine() {
        ItemList.Machine_LV_Bender.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_LV.ID)
                .setName("basicmachine.bender.tier.01", "Basic Bending Machine")
                .setTier(1)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Bender.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_MV.ID)
                .setName("basicmachine.bender.tier.02", "Advanced Bending Machine")
                .setTier(2)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Bender.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_HV.ID)
                .setName("basicmachine.bender.tier.03", "Advanced Bending Machine II")
                .setTier(3)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Bender.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_EV.ID)
                .setName("basicmachine.bender.tier.04", "Advanced Bending Machine III")
                .setTier(4)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Bender.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_IV.ID)
                .setName("basicmachine.bender.tier.05", "Advanced Bending Machine IV")
                .setTier(5)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.BendingMachineLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_LuV.ID)
                .setName("basicmachine.bender.tier.06", "Elite Bending Machine")
                .setTier(6)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.BendingMachineZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_ZPM.ID)
                .setName("basicmachine.bender.tier.07", "Elite Bending Machine II")
                .setTier(7)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.BendingMachineUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_UV.ID)
                .setName("basicmachine.bender.tier.08", "Ultimate Bending Unit")
                .setTier(8)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.BendingMachineUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_UHV.ID)
                .setName("basicmachine.bender.tier.09", "Epic Bending Unit")
                .setTier(9)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.BendingMachineUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_UEV.ID)
                .setName("basicmachine.bender.tier.10", "Epic Bending Unit II")
                .setTier(10)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.BendingMachineUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_UIV.ID)
                .setName("basicmachine.bender.tier.11", "Epic Bending Unit III")
                .setTier(11)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));

        ItemList.BendingMachineUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(BENDING_MACHINE_UMV.ID)
                .setName("basicmachine.bender.tier.12", "Epic Bending Unit IV")
                .setTier(12)
                .setDescription(MachineType.BENDING_MACHINE.tooltipDescription())
                .setRecipes(benderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("BENDER")
                .build()
                .getStackForm(1L));
    }

    private void registerCompressor() {
        ItemList.Machine_LV_Compressor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_LV.ID)
                .setName("basicmachine.compressor.tier.01", "Basic Compressor")
                .setTier(1)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Compressor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_MV.ID)
                .setName("basicmachine.compressor.tier.02", "Advanced Compressor")
                .setTier(2)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Compressor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_HV.ID)
                .setName("basicmachine.compressor.tier.03", "Advanced Compressor II")
                .setTier(3)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Compressor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_EV.ID)
                .setName("basicmachine.compressor.tier.04", "Advanced Compressor III")
                .setTier(4)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Compressor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_IV.ID)
                .setName("basicmachine.compressor.tier.05", "Singularity Compressor")
                .setTier(5)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.CompressorLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_LuV.ID)
                .setName("basicmachine.compressor.tier.06", "Elite Compressor")
                .setTier(6)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.CompressorZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_ZPM.ID)
                .setName("basicmachine.compressor.tier.07", "Elite Compressor II")
                .setTier(7)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.CompressorUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_UV.ID)
                .setName("basicmachine.compressor.tier.08", "Ultimate Matter Constrictor")
                .setTier(8)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.CompressorUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_UHV.ID)
                .setName("basicmachine.compressor.tier.09", "Epic Matter Constrictor")
                .setTier(9)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.CompressorUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_UEV.ID)
                .setName("basicmachine.compressor.tier.10", "Epic Matter Constrictor II")
                .setTier(10)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.CompressorUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_UIV.ID)
                .setName("basicmachine.compressor.tier.11", "Epic Matter Constrictor III")
                .setTier(11)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

        ItemList.CompressorUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(COMPRESSOR_UMV.ID)
                .setName("basicmachine.compressor.tier.12", "Epic Matter Constrictor IV")
                .setTier(12)
                .setDescription(MachineType.COMPRESSOR.tooltipDescription())
                .setRecipes(compressorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_COMPRESSOR)
                .setOverlays("COMPRESSOR")
                .build()
                .getStackForm(1L));

    }

    private void registerCuttingMachine() {
        ItemList.Machine_LV_Cutter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_LV.ID)
                .setName("basicmachine.cutter.tier.01", "Basic Cutting Machine")
                .setTier(1)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Cutter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_MV.ID)
                .setName("basicmachine.cutter.tier.02", "Advanced Cutting Machine")
                .setTier(2)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Cutter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_HV.ID)
                .setName("basicmachine.cutter.tier.03", "Advanced Cutting Machine II")
                .setTier(3)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Cutter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_EV.ID)
                .setName("basicmachine.cutter.tier.04", "Advanced Cutting Machine III")
                .setTier(4)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Cutter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_IV.ID)
                .setName("basicmachine.cutter.tier.05", "Advanced Cutting Machine IV")
                .setTier(5)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.CuttingMachineLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_LuV.ID)
                .setName("basicmachine.cutter.tier.06", "Elite Cutting Machine")
                .setTier(6)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.CuttingMachineZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_ZPM.ID)
                .setName("basicmachine.cutter.tier.07", "Elite Cutting Machine II")
                .setTier(7)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.CuttingMachineUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_UV.ID)
                .setName("basicmachine.cutter.tier.08", "Ultimate Object Divider")
                .setTier(8)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.CuttingMachineUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_UHV.ID)
                .setName("basicmachine.cutter.tier.09", "Epic Object Divider")
                .setTier(9)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.CuttingMachineUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_UEV.ID)
                .setName("basicmachine.cutter.tier.10", "Epic Object Divider II")
                .setTier(10)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.CuttingMachineUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_UIV.ID)
                .setName("basicmachine.cutter.tier.11", "Epic Object Divider III")
                .setTier(11)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

        ItemList.CuttingMachineUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CUTTING_MACHINE_UMV.ID)
                .setName("basicmachine.cutter.tier.12", "Epic Object Divider IV")
                .setTier(12)
                .setDescription(MachineType.CUTTING_MACHINE.tooltipDescription())
                .setRecipes(cutterRecipes)
                .setSlotsCount(2, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("CUTTER")
                .build()
                .getStackForm(1L));

    }

    private void registerDistillery() {
        ItemList.Machine_LV_Distillery.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_LV.ID)
                .setName("basicmachine.distillery.tier.01", "Basic Distillery")
                .setTier(1)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Distillery.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_MV.ID)
                .setName("basicmachine.distillery.tier.02", "Advanced Distillery")
                .setTier(2)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Distillery.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_HV.ID)
                .setName("basicmachine.distillery.tier.03", "Advanced Distillery II")
                .setTier(3)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Distillery.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_EV.ID)
                .setName("basicmachine.distillery.tier.04", "Advanced Distillery III")
                .setTier(4)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Distillery.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_IV.ID)
                .setName("basicmachine.distillery.tier.05", "Advanced Distillery IV")
                .setTier(5)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.DistilleryLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_LuV.ID)
                .setName("basicmachine.distillery.tier.06", "Elite Distillery")
                .setTier(6)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.DistilleryZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_ZPM.ID)
                .setName("basicmachine.distillery.tier.07", "Elite Distillery II")
                .setTier(7)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.DistilleryUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_UV.ID)
                .setName("basicmachine.distillery.tier.08", "Ultimate Fraction Splitter")
                .setTier(8)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.DistilleryUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_UHV.ID)
                .setName("basicmachine.distillery.tier.09", "Epic Fraction Splitter")
                .setTier(9)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.DistilleryUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_UEV.ID)
                .setName("basicmachine.distillery.tier.10", "Epic Fraction Splitter II")
                .setTier(10)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.DistilleryUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_UIV.ID)
                .setName("basicmachine.distillery.tier.11", "Epic Fraction Splitter III")
                .setTier(11)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

        ItemList.DistilleryUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(DISTILLERY_UMV.ID)
                .setName("basicmachine.distillery.tier.12", "Epic Fraction Splitter IV")
                .setTier(12)
                .setDescription(MachineType.DISTILLERY.tooltipDescription())
                .setRecipes(distilleryRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GT_MACHINES_DISTILLERY_LOOP)
                .setOverlays("DISTILLERY")
                .build()
                .getStackForm(1L));

    }

    private void registerElectricFurnace() {
        ItemList.Machine_LV_E_Furnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_LV.ID)
                .setName("basicmachine.e_furnace.tier.01", "Basic Electric Furnace")
                .setTier(1)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .setProgressBarTextureName("E_Furnace")
                .getStackForm(1L));

        ItemList.Machine_MV_E_Furnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_MV.ID)
                .setName("basicmachine.e_furnace.tier.02", "Advanced Electric Furnace")
                .setTier(2)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .setProgressBarTextureName("E_Furnace")
                .getStackForm(1L));

        ItemList.Machine_HV_E_Furnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_HV.ID)
                .setName("basicmachine.e_furnace.tier.03", "Advanced Electric Furnace II")
                .setTier(3)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .setProgressBarTextureName("E_Furnace")
                .getStackForm(1L));

        ItemList.Machine_EV_E_Furnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_EV.ID)
                .setName("basicmachine.e_furnace.tier.04", "Advanced Electric Furnace III")
                .setTier(4)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .setProgressBarTextureName("E_Furnace")
                .getStackForm(1L));

        ItemList.Machine_IV_E_Furnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_IV.ID)
                .setName("basicmachine.e_furnace.tier.05", "Electron Excitement Processor")
                .setTier(5)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .setProgressBarTextureName("E_Furnace")
                .getStackForm(1L));

        ItemList.ElectricFurnaceLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_LuV.ID)
                .setName("basicmachine.e_furnace.tier.06", "Elite Electric Furnace")
                .setTier(6)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ElectricFurnaceZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_ZPM.ID)
                .setName("basicmachine.e_furnace.tier.07", "Elite Electric Furnace II")
                .setTier(7)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ElectricFurnaceUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_UV.ID)
                .setName("basicmachine.e_furnace.tier.08", "Ultimate Atom Stimulator")
                .setTier(8)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ElectricFurnaceUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_UHV.ID)
                .setName("basicmachine.e_furnace.tier.09", "Epic Atom Stimulator")
                .setTier(9)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ElectricFurnaceUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_UEV.ID)
                .setName("basicmachine.e_furnace.tier.10", "Epic Atom Stimulator II")
                .setTier(10)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ElectricFurnaceUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_UIV.ID)
                .setName("basicmachine.e_furnace.tier.11", "Epic Atom Stimulator III")
                .setTier(11)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ElectricFurnaceUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTRIC_FURNACE_UMV.ID)
                .setName("basicmachine.e_furnace.tier.12", "Epic Atom Stimulator IV")
                .setTier(12)
                .setDescription(MachineType.ELECTRIC_FURNACE.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_FURNACE)
                .setOverlays("ELECTRIC_FURNACE")
                .build()
                .getStackForm(1L));
    }

    private void registerElectrolyzer() {
        ItemList.Machine_LV_Electrolyzer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYSER_LV.ID)
                .setName("basicmachine.electrolyzer.tier.01", "Basic Electrolyzer")
                .setTier(1)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Electrolyzer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYSER_MV.ID)
                .setName("basicmachine.electrolyzer.tier.02", "Advanced Electrolyzer")
                .setTier(2)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Electrolyzer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYSER_HV.ID)
                .setName("basicmachine.electrolyzer.tier.03", "Advanced Electrolyzer II")
                .setTier(3)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Electrolyzer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYSER_EV.ID)
                .setName("basicmachine.electrolyzer.tier.04", "Advanced Electrolyzer III")
                .setTier(4)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Electrolyzer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYSER_IV.ID)
                .setName("basicmachine.electrolyzer.tier.05", "Molecular Disintegrator E-4908")
                .setTier(5)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.ElectrolyzerLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYZER_LuV.ID)
                .setName("basicmachine.electrolyzer.tier.06", "Elite Electrolyzer")
                .setTier(6)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.ElectrolyzerZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYZER_ZPM.ID)
                .setName("basicmachine.electrolyzer.tier.07", "Elite Electrolyzer II")
                .setTier(7)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.ElectrolyzerUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYZER_UV.ID)
                .setName("basicmachine.electrolyzer.tier.08", "Ultimate Ionizer")
                .setTier(8)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.ElectrolyzerUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYZER_UHV.ID)
                .setName("basicmachine.electrolyzer.tier.09", "Epic Ionizer")
                .setTier(9)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.ElectrolyzerUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYZER_UEV.ID)
                .setName("basicmachine.electrolyzer.tier.10", "Epic Ionizer II")
                .setTier(10)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.ElectrolyzerUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYZER_UIV.ID)
                .setName("basicmachine.electrolyzer.tier.11", "Epic Ionizer III")
                .setTier(11)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

        ItemList.ElectrolyzerUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROLYZER_UMV.ID)
                .setName("basicmachine.electrolyzer.tier.12", "Epic Ionizer IV")
                .setTier(12)
                .setDescription(MachineType.ELECTROLYZER.tooltipDescription())
                .setRecipes(electrolyzerRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("ELECTROLYZER")
                .build()
                .getStackForm(1L));

    }

    private void registerElectromagneticSeparator() {
        ItemList.Machine_LV_ElectromagneticSeparator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_LV.ID)
                .setName("basicmachine.electromagneticseparator.tier.01", "Basic Electromagnetic Separator")
                .setTier(1)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_ElectromagneticSeparator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_MV.ID)
                .setName("basicmachine.electromagneticseparator.tier.02", "Advanced Electromagnetic Separator")
                .setTier(2)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_ElectromagneticSeparator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_HV.ID)
                .setName("basicmachine.electromagneticseparator.tier.03", "Advanced Electromagnetic Separator II")
                .setTier(3)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_ElectromagneticSeparator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_EV.ID)
                .setName("basicmachine.electromagneticseparator.tier.04", "Advanced Electromagnetic Separator III")
                .setTier(4)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_ElectromagneticSeparator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_IV.ID)
                .setName("basicmachine.electromagneticseparator.tier.05", "Advanced Electromagnetic Separator IV")
                .setTier(5)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.ElectromagneticSeparatorLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_LuV.ID)
                .setName("basicmachine.electromagneticseparator.tier.06", "Elite Electromagnetic Separator")
                .setTier(6)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.ElectromagneticSeparatorZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_ZPM.ID)
                .setName("basicmachine.electromagneticseparator.tier.07", "Elite Electromagnetic Separator II")
                .setTier(7)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.ElectromagneticSeparatorUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_UV.ID)
                .setName("basicmachine.electromagneticseparator.tier.08", "Ultimate Magnetar Separator")
                .setTier(8)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.ElectromagneticSeparatorUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_UHV.ID)
                .setName("basicmachine.electromagneticseparator.tier.09", "Epic Magnetar Separator")
                .setTier(9)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.ElectromagneticSeparatorUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_UEV.ID)
                .setName("basicmachine.electromagneticseparator.tier.10", "Epic Magnetar Separator II")
                .setTier(10)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.ElectromagneticSeparatorUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_UIV.ID)
                .setName("basicmachine.electromagneticseparator.tier.11", "Epic Magnetar Separator III")
                .setTier(11)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

        ItemList.ElectromagneticSeparatorUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ELECTROMAGNETIC_SEPARATOR_UMV.ID)
                .setName("basicmachine.electromagneticseparator.tier.12", "Epic Magnetar Separator IV")
                .setTier(12)
                .setDescription(MachineType.ELECTROMAGNETIC_SEPARATOR.tooltipDescription())
                .setRecipes(electroMagneticSeparatorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("ELECTROMAGNETIC_SEPARATOR")
                .build()
                .getStackForm(1L));

    }

    private void registerExtractor() {
        ItemList.Machine_LV_Extractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_LV.ID)
                .setName("basicmachine.extractor.tier.01", "Basic Extractor")
                .setTier(1)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Extractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_MV.ID)
                .setName("basicmachine.extractor.tier.02", "Advanced Extractor")
                .setTier(2)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Extractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_HV.ID)
                .setName("basicmachine.extractor.tier.03", "Advanced Extractor II")
                .setTier(3)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Extractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_EV.ID)
                .setName("basicmachine.extractor.tier.04", "Advanced Extractor III")
                .setTier(4)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Extractor.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_IV.ID)
                .setName("basicmachine.extractor.tier.05", "Vacuum Extractor")
                .setTier(5)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ExtractorLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_LuV.ID)
                .setName("basicmachine.extractor.tier.06", "Elite Extractor")
                .setTier(6)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ExtractorZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_ZPM.ID)
                .setName("basicmachine.extractor.tier.07", "Elite Extractor II")
                .setTier(7)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ExtractorUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_UV.ID)
                .setName("basicmachine.extractor.tier.08", "Ultimate Extractinator")
                .setTier(8)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ExtractorUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_UHV.ID)
                .setName("basicmachine.extractor.tier.09", "Epic Extractinator")
                .setTier(9)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ExtractorUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_UEV.ID)
                .setName("basicmachine.extractor.tier.10", "Epic Extractinator II")
                .setTier(10)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ExtractorUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_UIV.ID)
                .setName("basicmachine.extractor.tier.11", "Epic Extractinator III")
                .setTier(11)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

        ItemList.ExtractorUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRACTOR_UMV.ID)
                .setName("basicmachine.extractor.tier.12", "Epic Extractinator IV")
                .setTier(12)
                .setDescription(MachineType.EXTRACTOR.tooltipDescription())
                .setRecipes(extractorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_EXTRACTOR_OP)
                .setOverlays("EXTRACTOR")
                .build()
                .getStackForm(1L));

    }

    private void registerExtruder() {
        ItemList.Machine_LV_Extruder.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_LV.ID)
                .setName("basicmachine.extruder.tier.01", "Basic Extruder")
                .setTier(1)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Extruder.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_MV.ID)
                .setName("basicmachine.extruder.tier.02", "Advanced Extruder")
                .setTier(2)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Extruder.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_HV.ID)
                .setName("basicmachine.extruder.tier.03", "Advanced Extruder II")
                .setTier(3)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Extruder.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_EV.ID)
                .setName("basicmachine.extruder.tier.04", "Advanced Extruder III")
                .setTier(4)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Extruder.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_IV.ID)
                .setName("basicmachine.extruder.tier.05", "Advanced Extruder IV")
                .setTier(5)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.ExtruderLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_LuV.ID)
                .setName("basicmachine.extruder.tier.06", "Elite Extruder")
                .setTier(6)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.ExtruderZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_ZPM.ID)
                .setName("basicmachine.extruder.tier.07", "Elite Extruder II")
                .setTier(7)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.ExtruderUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_UV.ID)
                .setName("basicmachine.extruder.tier.08", "Ultimate Shape Driver")
                .setTier(8)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.ExtruderUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_UHV.ID)
                .setName("basicmachine.extruder.tier.09", "Epic Shape Driver")
                .setTier(9)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.ExtruderUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_UEV.ID)
                .setName("basicmachine.extruder.tier.10", "Epic Shape Driver II")
                .setTier(10)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.ExtruderUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_UIV.ID)
                .setName("basicmachine.extruder.tier.11", "Epic Shape Driver III")
                .setTier(11)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

        ItemList.ExtruderUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(EXTRUDER_UMV.ID)
                .setName("basicmachine.extruder.tier.12", "Epic Shape Driver IV")
                .setTier(12)
                .setDescription(MachineType.EXTRUDER.tooltipDescription())
                .setRecipes(extruderRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.IC2_MACHINES_INDUCTION_LOOP)
                .setOverlays("EXTRUDER")
                .build()
                .getStackForm(1L));

    }

    private void registerFluidSolidifier() {
        ItemList.Machine_LV_FluidSolidifier.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_LV.ID)
                .setName("basicmachine.fluidsolidifier.tier.01", "Basic Fluid Solidifier")
                .setTier(1)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_FluidSolidifier.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_MV.ID)
                .setName("basicmachine.fluidsolidifier.tier.02", "Advanced Fluid Solidifier")
                .setTier(2)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_FluidSolidifier.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_HV.ID)
                .setName("basicmachine.fluidsolidifier.tier.03", "Advanced Fluid Solidifier II")
                .setTier(3)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_FluidSolidifier.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_EV.ID)
                .setName("basicmachine.fluidsolidifier.tier.04", "Advanced Fluid Solidifier III")
                .setTier(4)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_FluidSolidifier.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_IV.ID)
                .setName("basicmachine.fluidsolidifier.tier.05", "Advanced Fluid Solidifier IV")
                .setTier(5)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.FluidSolidifierLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_LuV.ID)
                .setName("basicmachine.fluidsolidifier.tier.06", "Elite Fluid Solidifier")
                .setTier(6)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.FluidSolidifierZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_ZPM.ID)
                .setName("basicmachine.fluidsolidifier.tier.07", "Elite Fluid Solidifier II")
                .setTier(7)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.FluidSolidifierUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_UV.ID)
                .setName("basicmachine.fluidsolidifier.tier.08", "Ultimate Fluid Petrificator")
                .setTier(8)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.FluidSolidifierUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_UHV.ID)
                .setName("basicmachine.fluidsolidifier.tier.09", "Epic Fluid Petrificator")
                .setTier(9)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.FluidSolidifierUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_UEV.ID)
                .setName("basicmachine.fluidsolidifier.tier.10", "Epic Fluid Petrificator II")
                .setTier(10)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.FluidSolidifierUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_UIV.ID)
                .setName("basicmachine.fluidsolidifier.tier.11", "Epic Fluid Petrificator III")
                .setTier(11)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

        ItemList.FluidSolidifierUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FLUID_SOLIDIFIER_UMV.ID)
                .setName("basicmachine.fluidsolidifier.tier.12", "Epic Fluid Petrificator IV")
                .setTier(12)
                .setDescription(MachineType.FLUID_SOLIDIFIER.tooltipDescription())
                .setRecipes(fluidSolidifierRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_COOLING)
                .setOverlays("FLUID_SOLIDIFIER")
                .build()
                .getStackForm(1L));

    }

    private void registerFormingPress() {
        ItemList.Machine_LV_Press.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_LV.ID)
                .setName("basicmachine.press.tier.01", "Basic Forming Press")
                .setTier(1)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(2, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Press.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_MV.ID)
                .setName("basicmachine.press.tier.02", "Advanced Forming Press")
                .setTier(2)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(2, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Press.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_HV.ID)
                .setName("basicmachine.press.tier.03", "Advanced Forming Press II")
                .setTier(3)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Press.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_EV.ID)
                .setName("basicmachine.press.tier.04", "Advanced Forming Press III")
                .setTier(4)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Press.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_IV.ID)
                .setName("basicmachine.press.tier.05", "Advanced Forming Press IV")
                .setTier(5)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.FormingPressLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_LuV.ID)
                .setName("basicmachine.press.tier.06", "Elite Forming Press")
                .setTier(6)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.FormingPressZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_ZPM.ID)
                .setName("basicmachine.press.tier.07", "Elite Forming Press II")
                .setTier(7)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.FormingPressUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_UV.ID)
                .setName("basicmachine.press.tier.08", "Ultimate Surface Shifter")
                .setTier(8)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.FormingPressUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_UHV.ID)
                .setName("basicmachine.press.tier.09", "Epic Surface Shifter")
                .setTier(9)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.FormingPressUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_UEV.ID)
                .setName("basicmachine.press.tier.10", "Epic Surface Shifter II")
                .setTier(10)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.FormingPressUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_UIV.ID)
                .setName("basicmachine.press.tier.11", "Epic Surface Shifter III")
                .setTier(11)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

        ItemList.FormingPressUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORMING_PRESS_UMV.ID)
                .setName("basicmachine.press.tier.12", "Epic Surface Shifter IV")
                .setTier(12)
                .setDescription(MachineType.FORMING_PRESS.tooltipDescription())
                .setRecipes(formingPressRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("PRESS")
                .build()
                .getStackForm(1L));

    }

    private void registerForgeHammer() {
        ItemList.Machine_LV_Hammer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_LV.ID)
                .setName("basicmachine.hammer.tier.01", "Basic Forge Hammer")
                .setTier(1)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Hammer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_MV.ID)
                .setName("basicmachine.hammer.tier.02", "Advanced Forge Hammer")
                .setTier(2)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Hammer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_HV.ID)
                .setName("basicmachine.hammer.tier.03", "Advanced Forge Hammer II")
                .setTier(3)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Hammer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_EV.ID)
                .setName("basicmachine.hammer.tier.04", "Advanced Forge Hammer III")
                .setTier(4)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Hammer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_IV.ID)
                .setName("basicmachine.hammer.tier.05", "Advanced Forge Hammer IV")
                .setTier(5)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.ForgeHammerLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_LuV.ID)
                .setName("basicmachine.hammer.tier.06", "Elite Forge Hammer")
                .setTier(6)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.ForgeHammerZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_ZPM.ID)
                .setName("basicmachine.hammer.tier.07", "Elite Forge Hammer II")
                .setTier(7)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.ForgeHammerUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_UV.ID)
                .setName("basicmachine.hammer.tier.08", "Ultimate Impact Modulator")
                .setTier(8)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.ForgeHammerUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_UHV.ID)
                .setName("basicmachine.hammer.tier.09", "Epic Impact Modulator")
                .setTier(9)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.ForgeHammerUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_UEV.ID)
                .setName("basicmachine.hammer.tier.10", "Epic Impact Modulator II")
                .setTier(10)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.ForgeHammerUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_UIV.ID)
                .setName("basicmachine.hammer.tier.11", "Epic Impact Modulator III")
                .setTier(11)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

        ItemList.ForgeHammerUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(FORGE_HAMMER_UMV.ID)
                .setName("basicmachine.hammer.tier.12", "Epic Impact Modulator IV")
                .setTier(12)
                .setDescription(MachineType.FORGE_HAMMER.tooltipDescription())
                .setRecipes(hammerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_FORGE_HAMMER)
                .setOverlays("HAMMER")
                .build()
                .getStackForm(1L));

    }

    private void registerLathe() {
        ItemList.Machine_LV_Lathe.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_LV.ID)
                .setName("basicmachine.lathe.tier.01", "Basic Lathe")
                .setTier(1)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Lathe.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_MV.ID)
                .setName("basicmachine.lathe.tier.02", "Advanced Lathe")
                .setTier(2)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Lathe.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_HV.ID)
                .setName("basicmachine.lathe.tier.03", "Advanced Lathe II")
                .setTier(3)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Lathe.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_EV.ID)
                .setName("basicmachine.lathe.tier.04", "Advanced Lathe III")
                .setTier(4)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Lathe.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_IV.ID)
                .setName("basicmachine.lathe.tier.05", "Advanced Lathe IV")
                .setTier(5)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.LatheLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_LuV.ID)
                .setName("basicmachine.lathe.tier.06", "Elite Lathe")
                .setTier(6)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.LatheZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_ZPM.ID)
                .setName("basicmachine.lathe.tier.07", "Elite Lathe II")
                .setTier(7)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.LatheUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_UV.ID)
                .setName("basicmachine.lathe.tier.08", "Ultimate Turn-O-Matic")
                .setTier(8)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.LatheUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_UHV.ID)
                .setName("basicmachine.lathe.tier.09", "Epic Turn-O-Matic")
                .setTier(9)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.LatheUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_UEV.ID)
                .setName("basicmachine.lathe.tier.10", "Epic Turn-O-Matic II")
                .setTier(10)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.LatheUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_UIV.ID)
                .setName("basicmachine.lathe.tier.11", "Epic Turn-O-Matic III")
                .setTier(11)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

        ItemList.LatheUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LATHE_UMV.ID)
                .setName("basicmachine.lathe.tier.12", "Epic Turn-O-Matic IV")
                .setTier(12)
                .setDescription(MachineType.LATHE.tooltipDescription())
                .setRecipes(latheRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_CUT)
                .setOverlays("LATHE")
                .build()
                .getStackForm(1L));

    }

    private void registerPrecisionLaserEngraver() {
        ItemList.Machine_LV_LaserEngraver.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LASER_ENGRAVER_LV.ID)
                .setName("basicmachine.laserengraver.tier.01", "Basic Precision Laser Engraver")
                .setTier(1)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(2, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_LaserEngraver.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LASER_ENGRAVER_MV.ID)
                .setName("basicmachine.laserengraver.tier.02", "Advanced Precision Laser Engraver")
                .setTier(2)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(2, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_LaserEngraver.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LASER_ENGRAVER_HV.ID)
                .setName("basicmachine.laserengraver.tier.03", "Advanced Precision Laser Engraver II")
                .setTier(3)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(2, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_LaserEngraver.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LASER_ENGRAVER_EV.ID)
                .setName("basicmachine.laserengraver.tier.04", "Advanced Precision Laser Engraver III")
                .setTier(4)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_LaserEngraver.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(LASER_ENGRAVER_IV.ID)
                .setName("basicmachine.laserengraver.tier.05", "Advanced Precision Laser Engraver IV")
                .setTier(5)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.PrecisionLaserEngraverLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRECISION_LASER_ENGRAVER_LuV.ID)
                .setName("basicmachine.laserengraver.tier.06", "Elite Precision Laser Engraver")
                .setTier(6)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.PrecisionLaserEngraverZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRECISION_LASER_ENGRAVER_ZPM.ID)
                .setName("basicmachine.laserengraver.tier.07", "Elite Precision Laser Engraver II")
                .setTier(7)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.PrecisionLaserEngraverUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRECISION_LASER_ENGRAVER_UV.ID)
                .setName("basicmachine.laserengraver.tier.08", "Ultimate Exact Photon Cannon")
                .setTier(8)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.PrecisionLaserEngraverUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRECISION_LASER_ENGRAVER_UHV.ID)
                .setName("basicmachine.laserengraver.tier.09", "Epic Exact Photon Cannon")
                .setTier(9)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.PrecisionLaserEngraverUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRECISION_LASER_ENGRAVER_UEV.ID)
                .setName("basicmachine.laserengraver.tier.10", "Epic Exact Photon Cannon II")
                .setTier(10)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.PrecisionLaserEngraverUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRECISION_LASER_ENGRAVER_UIV.ID)
                .setName("basicmachine.laserengraver.tier.11", "Epic Exact Photon Cannon III")
                .setTier(11)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

        ItemList.PrecisionLaserEngraverUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRECISION_LASER_ENGRAVER_UMV.ID)
                .setName("basicmachine.laserengraver.tier.12", "Epic Exact Photon Cannon IV")
                .setTier(12)
                .setDescription(MachineType.LASER_ENGRAVER.tooltipDescription())
                .setRecipes(laserEngraverRecipes)
                .setSlotsCount(4, 1)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ELECTROLYZER)
                .setOverlays("LASER_ENGRAVER")
                .build()
                .getStackForm(1L));

    }

    private void registerMacerator() {
        ItemList.Machine_LV_Macerator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_LV.ID)
                .setName("basicmachine.macerator.tier.01", "Basic Macerator")
                .setTier(1)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("MACERATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Macerator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_MV.ID)
                .setName("basicmachine.macerator.tier.02", "Advanced Macerator")
                .setTier(2)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("MACERATOR")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Macerator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_HV.ID)
                .setName("basicmachine.macerator.tier.03", "Universal Macerator")
                .setTier(3)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 2)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Macerator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_EV.ID)
                .setName("basicmachine.macerator.tier.04", "Universal Pulverizer")
                .setTier(4)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Macerator.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_IV.ID)
                .setName("basicmachine.macerator.tier.05", "Blend-O-Matic 9001")
                .setTier(5)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 4)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.MaceratorLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_LuV.ID)
                .setName("basicmachine.macerator.tier.06", "Elite Pulverizer")
                .setTier(6)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 4)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.MaceratorZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_ZPM.ID)
                .setName("basicmachine.macerator.tier.07", "Elite Pulverizer II")
                .setTier(7)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 4)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.MaceratorUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_UV.ID)
                .setName("basicmachine.macerator.tier.08", "Ultimate Shape Eliminator")
                .setTier(8)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 4)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.MaceratorUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_UHV.ID)
                .setName("basicmachine.macerator.tier.09", "Epic Shape Eliminator")
                .setTier(9)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 4)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.MaceratorUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_UEV.ID)
                .setName("basicmachine.macerator.tier.10", "Epic Shape Eliminator II")
                .setTier(10)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 4)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.MaceratorUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_UIV.ID)
                .setName("basicmachine.macerator.tier.11", "Epic Shape Eliminator III")
                .setTier(11)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 4)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

        ItemList.MaceratorUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MACERATOR_UMV.ID)
                .setName("basicmachine.macerator.tier.12", "Epic Shape Eliminator IV")
                .setTier(12)
                .setDescription(MachineType.MACERATOR.tooltipDescription())
                .setRecipes(maceratorRecipes)
                .setSlotsCount(1, 4)
                .setSound(SoundResource.GTCEU_LOOP_MACERATOR)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PULVERIZER")
                .build()
                .getStackForm(1L));

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
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_OVEN_LV.ID)
                .setName("basicmachine.microwave.tier.01", "Basic Microwave")
                .setTier(1)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Microwave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_OVEN_MV.ID)
                .setName("basicmachine.microwave.tier.02", "Advanced Microwave")
                .setTier(2)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Microwave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_OVEN_HV.ID)
                .setName("basicmachine.microwave.tier.03", "Advanced Microwave II")
                .setTier(3)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Microwave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_OVEN_EV.ID)
                .setName("basicmachine.microwave.tier.04", "Advanced Microwave III")
                .setTier(4)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Microwave.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_OVEN_IV.ID)
                .setName("basicmachine.microwave.tier.05", "Advanced Microwave IV")
                .setTier(5)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.MicrowaveLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_LuV.ID)
                .setName("basicmachine.microwave.tier.06", "Elite Microwave")
                .setTier(6)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.MicrowaveZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_ZPM.ID)
                .setName("basicmachine.microwave.tier.07", "Elite Microwave II")
                .setTier(7)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.MicrowaveUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_UV.ID)
                .setName("basicmachine.microwave.tier.08", "Ultimate UFO Engine")
                .setTier(8)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.MicrowaveUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_UHV.ID)
                .setName("basicmachine.microwave.tier.09", "Epic UFO Engine")
                .setTier(9)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.MicrowaveUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_UEV.ID)
                .setName("basicmachine.microwave.tier.10", "Epic UFO Engine II")
                .setTier(10)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.MicrowaveUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_UIV.ID)
                .setName("basicmachine.microwave.tier.11", "Epic UFO Engine III")
                .setTier(11)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

        ItemList.MicrowaveUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(MICROWAVE_UMV.ID)
                .setName("basicmachine.microwave.tier.12", "Epic UFO Engine IV")
                .setTier(12)
                .setDescription(MachineType.MICROWAVE.tooltipDescription())
                .setRecipes(microwaveRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("MICROWAVE")
                .build()
                .getStackForm(1L));

    }

    private static void registerOven() {
        ItemList.Machine_LV_Oven.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(OVEN_LV.ID)
                .setName("basicmachine.e_oven.tier.01", "Basic Electric Oven")
                .setTier(1)
                .setDescription(MachineType.OVEN.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("ELECTRIC_OVEN")
                .build()
                .setProgressBarTextureName("E_Oven")
                .getStackForm(1L));

        ItemList.Machine_MV_Oven.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(OVEN_MV.ID)
                .setName("basicmachine.e_oven.tier.02", "Advanced Electric Oven")
                .setTier(2)
                .setDescription(MachineType.OVEN.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("ELECTRIC_OVEN")
                .build()
                .setProgressBarTextureName("E_Oven")
                .getStackForm(1L));

        ItemList.Machine_HV_Oven.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(OVEN_HV.ID)
                .setName("basicmachine.e_oven.tier.03", "Advanced Electric Oven II")
                .setTier(3)
                .setDescription(MachineType.OVEN.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("ELECTRIC_OVEN")
                .build()
                .setProgressBarTextureName("E_Oven")
                .getStackForm(1L));

        ItemList.Machine_EV_Oven.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(OVEN_EV.ID)
                .setName("basicmachine.e_oven.tier.04", "Advanced Electric Oven III")
                .setTier(4)
                .setDescription(MachineType.OVEN.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("ELECTRIC_OVEN")
                .build()
                .setProgressBarTextureName("E_Oven")
                .getStackForm(1L));

        ItemList.Machine_IV_Oven.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(OVEN_IV.ID)
                .setName("basicmachine.e_oven.tier.05", "Advanced Electric Oven IV")
                .setTier(5)
                .setDescription(MachineType.OVEN.tooltipDescription())
                .setRecipes(furnaceRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_HUM)
                .setOverlays("ELECTRIC_OVEN")
                .build()
                .setProgressBarTextureName("E_Oven")
                .getStackForm(1L));
    }

    private void registerOreWashingPlant() {
        ItemList.Machine_LV_OreWasher.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHER_LV.ID)
                .setName("basicmachine.orewasher.tier.01", "Basic Ore Washing Plant")
                .setTier(1)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_OreWasher.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHER_MV.ID)
                .setName("basicmachine.orewasher.tier.02", "Advanced Ore Washing Plant")
                .setTier(2)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_OreWasher.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHER_HV.ID)
                .setName("basicmachine.orewasher.tier.03", "Advanced Ore Washing Plant II")
                .setTier(3)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_OreWasher.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHER_EV.ID)
                .setName("basicmachine.orewasher.tier.04", "Advanced Ore Washing Plant III")
                .setTier(4)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_OreWasher.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHER_IV.ID)
                .setName("basicmachine.orewasher.tier.05", "Repurposed Laundry-Washer I-360")
                .setTier(5)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.OreWashingPlantLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHING_PLANT_LuV.ID)
                .setName("basicmachine.orewasher.tier.06", "Elite Ore Washing Plant")
                .setTier(6)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.OreWashingPlantZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHING_PLANT_ZPM.ID)
                .setName("basicmachine.orewasher.tier.07", "Elite Ore Washing Plant II")
                .setTier(7)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.OreWashingPlantUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHING_PLANT_UV.ID)
                .setName("basicmachine.orewasher.tier.08", "Ultimate Ore Washing Machine")
                .setTier(8)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.OreWashingPlantUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHING_PLANT_UHV.ID)
                .setName("basicmachine.orewasher.tier.09", "Epic Ore Washing Machine")
                .setTier(9)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.OreWashingPlantUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHING_PLANT_UEV.ID)
                .setName("basicmachine.orewasher.tier.10", "Epic Ore Washing Machine II")
                .setTier(10)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.OreWashingPlantUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHING_PLANT_UIV.ID)
                .setName("basicmachine.orewasher.tier.11", "Epic Ore Washing Machine III")
                .setTier(11)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

        ItemList.OreWashingPlantUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ORE_WASHING_PLANT_UMV.ID)
                .setName("basicmachine.orewasher.tier.12", "Epic Ore Washing Machine IV")
                .setTier(12)
                .setDescription(MachineType.ORE_WASHER.tooltipDescription())
                .setRecipes(oreWasherRecipes)
                .setSlotsCount(1, 3)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("ORE_WASHER")
                .build()
                .getStackForm(1L));

    }

    private void registerPolarizer() {
        ItemList.Machine_LV_Polarizer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_LV.ID)
                .setName("basicmachine.polarizer.tier.01", "Basic Polarizer")
                .setTier(1)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Polarizer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_MV.ID)
                .setName("basicmachine.polarizer.tier.02", "Advanced Polarizer")
                .setTier(2)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Polarizer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_HV.ID)
                .setName("basicmachine.polarizer.tier.03", "Advanced Polarizer II")
                .setTier(3)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Polarizer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_EV.ID)
                .setName("basicmachine.polarizer.tier.04", "Advanced Polarizer III")
                .setTier(4)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Polarizer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_IV.ID)
                .setName("basicmachine.polarizer.tier.05", "Advanced Polarizer IV")
                .setTier(5)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.PolarizerLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_LuV.ID)
                .setName("basicmachine.polarizer.tier.06", "Elite Polarizer")
                .setTier(6)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.PolarizerZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_ZPM.ID)
                .setName("basicmachine.polarizer.tier.07", "Elite Polarizer II")
                .setTier(7)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.PolarizerUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_UV.ID)
                .setName("basicmachine.polarizer.tier.08", "Ultimate Magnetism Inducer")
                .setTier(8)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.PolarizerUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_UHV.ID)
                .setName("basicmachine.polarizer.tier.09", "Epic Magnetism Inducer")
                .setTier(9)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.PolarizerUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_UEV.ID)
                .setName("basicmachine.polarizer.tier.10", "Epic Magnetism Inducer II")
                .setTier(10)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.PolarizerUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_UIV.ID)
                .setName("basicmachine.polarizer.tier.11", "Epic Magnetism Inducer III")
                .setTier(11)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

        ItemList.PolarizerUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(POLARIZER_UMV.ID)
                .setName("basicmachine.polarizer.tier.12", "Epic Magnetism Inducer IV")
                .setTier(12)
                .setDescription(MachineType.POLARIZER.tooltipDescription())
                .setRecipes(polarizerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_MAGNETIZER_LOOP)
                .setOverlays("POLARIZER")
                .build()
                .getStackForm(1L));

    }

    private static void registerPrinter() {
        ItemList.Machine_LV_Printer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRINTER_LV.ID)
                .setName("basicmachine.printer.tier.01", "Basic Printer")
                .setTier(1)
                .setDescription(MachineType.PRINTER.tooltipDescription())
                .setRecipes(RecipeMaps.printerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.IC2_MACHINES_COMPRESSOR_OP)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PRINTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Printer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRINTER_MV.ID)
                .setName("basicmachine.printer.tier.02", "Advanced Printer")
                .setTier(2)
                .setDescription(MachineType.PRINTER.tooltipDescription())
                .setRecipes(RecipeMaps.printerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.IC2_MACHINES_COMPRESSOR_OP)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PRINTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Printer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRINTER_HV.ID)
                .setName("basicmachine.printer.tier.03", "Advanced Printer II")
                .setTier(3)
                .setDescription(MachineType.PRINTER.tooltipDescription())
                .setRecipes(RecipeMaps.printerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.IC2_MACHINES_COMPRESSOR_OP)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PRINTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Printer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRINTER_EV.ID)
                .setName("basicmachine.printer.tier.04", "Advanced Printer III")
                .setTier(4)
                .setDescription(MachineType.PRINTER.tooltipDescription())
                .setRecipes(RecipeMaps.printerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.IC2_MACHINES_COMPRESSOR_OP)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PRINTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Printer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRINTER_IV.ID)
                .setName("basicmachine.printer.tier.05", "Advanced Printer IV")
                .setTier(5)
                .setDescription(MachineType.PRINTER.tooltipDescription())
                .setRecipes(RecipeMaps.printerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.IC2_MACHINES_COMPRESSOR_OP)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PRINTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_LuV_Printer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRINTER_LuV.ID)
                .setName("basicmachine.printer.tier.06", "Advanced Printer V")
                .setTier(6)
                .setDescription(MachineType.PRINTER.tooltipDescription())
                .setRecipes(RecipeMaps.printerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.IC2_MACHINES_COMPRESSOR_OP)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PRINTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_ZPM_Printer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRINTER_ZPM.ID)
                .setName("basicmachine.printer.tier.07", "Advanced Printer VI")
                .setTier(7)
                .setDescription(MachineType.PRINTER.tooltipDescription())
                .setRecipes(RecipeMaps.printerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.IC2_MACHINES_COMPRESSOR_OP)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PRINTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_UV_Printer.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PRINTER_UV.ID)
                .setName("basicmachine.printer.tier.08", "Advanced Printer VII")
                .setTier(8)
                .setDescription(MachineType.PRINTER.tooltipDescription())
                .setRecipes(RecipeMaps.printerRecipes)
                .setSlotsCount(1, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.IC2_MACHINES_COMPRESSOR_OP)
                .setSpecialEffect(MTEBasicMachineWithRecipe.SpecialEffects.TOP_SMOKE)
                .setOverlays("PRINTER")
                .build()
                .getStackForm(1L));
    }

    private void registerRecycler() {
        ItemList.Machine_LV_Recycler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_LV.ID)
                .setName("basicmachine.recycler.tier.01", "Basic Recycler")
                .setTier(1)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Recycler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_MV.ID)
                .setName("basicmachine.recycler.tier.02", "Advanced Recycler")
                .setTier(2)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Recycler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_HV.ID)
                .setName("basicmachine.recycler.tier.03", "Advanced Recycler II")
                .setTier(3)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Recycler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_EV.ID)
                .setName("basicmachine.recycler.tier.04", "Advanced Recycler III")
                .setTier(4)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Recycler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_IV.ID)
                .setName("basicmachine.recycler.tier.05", "The Oblitterator")
                .setTier(5)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.RecyclerLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_LuV.ID)
                .setName("basicmachine.recycler.tier.06", "Elite Recycler")
                .setTier(6)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.RecyclerZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_ZPM.ID)
                .setName("basicmachine.recycler.tier.07", "Elite Recycler II")
                .setTier(7)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.RecyclerUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_UV.ID)
                .setName("basicmachine.recycler.tier.08", "Ultimate Scrap-O-Matic")
                .setTier(8)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.RecyclerUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_UHV.ID)
                .setName("basicmachine.recycler.tier.09", "Epic Scrap-O-Matic")
                .setTier(9)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.RecyclerUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_UEV.ID)
                .setName("basicmachine.recycler.tier.10", "Epic Scrap-O-Matic II")
                .setTier(10)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.RecyclerUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_UIV.ID)
                .setName("basicmachine.recycler.tier.11", "Epic Scrap-O-Matic III")
                .setTier(11)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

        ItemList.RecyclerUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(RECYCLER_UMV.ID)
                .setName("basicmachine.recycler.tier.12", "Epic Scrap-O-Matic IV")
                .setTier(12)
                .setDescription(MachineType.RECYCLER.tooltipDescription())
                .setRecipes(recyclerRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.IC2_MACHINES_RECYCLER_OP)
                .setOverlays("RECYCLER")
                .build()
                .getStackForm(1L));

    }

    private void registerSiftingMachine() {
        ItemList.Machine_LV_Sifter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTER_LV.ID)
                .setName("basicmachine.sifter.tier.01", "Basic Sifting Machine")
                .setTier(1)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Sifter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTER_MV.ID)
                .setName("basicmachine.sifter.tier.02", "Advanced Sifting Machine")
                .setTier(2)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Sifter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTER_HV.ID)
                .setName("basicmachine.sifter.tier.03", "Advanced Sifting Machine II")
                .setTier(3)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Sifter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTER_EV.ID)
                .setName("basicmachine.sifter.tier.04", "Advanced Sifting Machine III")
                .setTier(4)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Sifter.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTER_IV.ID)
                .setName("basicmachine.sifter.tier.05", "Advanced Sifting Machine IV")
                .setTier(5)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.SiftingMachineLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTING_MACHINE_LuV.ID)
                .setName("basicmachine.sifter.tier.06", "Elite Sifting Machine")
                .setTier(6)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.SiftingMachineZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTING_MACHINE_ZPM.ID)
                .setName("basicmachine.sifter.tier.07", "Elite Sifting Machine II")
                .setTier(7)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.SiftingMachineUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTING_MACHINE_UV.ID)
                .setName("basicmachine.sifter.tier.08", "Ultimate Pulsation Filter")
                .setTier(8)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.SiftingMachineUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTING_MACHINE_UHV.ID)
                .setName("basicmachine.sifter.tier.09", "Epic Pulsation Filter")
                .setTier(9)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.SiftingMachineUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTING_MACHINE_UEV.ID)
                .setName("basicmachine.sifter.tier.10", "Epic Pulsation Filter II")
                .setTier(10)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.SiftingMachineUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTING_MACHINE_UIV.ID)
                .setName("basicmachine.sifter.tier.11", "Epic Pulsation Filter III")
                .setTier(11)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

        ItemList.SiftingMachineUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(SIFTING_MACHINE_UMV.ID)
                .setName("basicmachine.sifter.tier.12", "Epic Pulsation Filter IV")
                .setTier(12)
                .setDescription(MachineType.SIFTER.tooltipDescription())
                .setRecipes(sifterRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("SIFTER")
                .build()
                .getStackForm(1L));

    }

    private void registerThermalCentrifuge() {
        ItemList.Machine_LV_ThermalCentrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_LV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.01", "Basic Thermal Centrifuge")
                .setTier(1)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_ThermalCentrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_MV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.02", "Advanced Thermal Centrifuge")
                .setTier(2)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_ThermalCentrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_HV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.03", "Advanced Thermal Centrifuge II")
                .setTier(3)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_ThermalCentrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_EV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.04", "Advanced Thermal Centrifuge III")
                .setTier(4)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_ThermalCentrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_IV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.05", "Blaze Sweatshop T-6350")
                .setTier(5)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.ThermalCentrifugeLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_LuV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.06", "Elite Thermal Centrifuge")
                .setTier(6)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.ThermalCentrifugeZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_ZPM.ID)
                .setName("basicmachine.thermalcentrifuge.tier.07", "Elite Thermal Centrifuge II")
                .setTier(7)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.ThermalCentrifugeUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_UV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.08", "Ultimate Fire Cyclone")
                .setTier(8)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.ThermalCentrifugeUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_UHV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.09", "Epic Fire Cyclone")
                .setTier(9)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.ThermalCentrifugeUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_UEV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.10", "Epic Fire Cyclone II")
                .setTier(10)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.ThermalCentrifugeUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_UIV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.11", "Epic Fire Cyclone III")
                .setTier(11)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.ThermalCentrifugeUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(THERMAL_CENTRIFUGE_UMV.ID)
                .setName("basicmachine.thermalcentrifuge.tier.12", "Epic Fire Cyclone IV")
                .setTier(12)
                .setDescription(MachineType.THERMAL_CENTRIFUGE.tooltipDescription())
                .setRecipes(thermalCentrifugeRecipes)
                .setSlotsCount(1, 3)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("THERMAL_CENTRIFUGE")
                .build()
                .getStackForm(1L));
    }

    private void registerWiremill() {
        ItemList.Machine_LV_Wiremill.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_LV.ID)
                .setName("basicmachine.wiremill.tier.01", "Basic Wiremill")
                .setTier(1)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Wiremill.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_MV.ID)
                .setName("basicmachine.wiremill.tier.02", "Advanced Wiremill")
                .setTier(2)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Wiremill.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_HV.ID)
                .setName("basicmachine.wiremill.tier.03", "Advanced Wiremill II")
                .setTier(3)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Wiremill.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_EV.ID)
                .setName("basicmachine.wiremill.tier.04", "Advanced Wiremill III")
                .setTier(4)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Wiremill.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_IV.ID)
                .setName("basicmachine.wiremill.tier.05", "Advanced Wiremill IV")
                .setTier(5)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.WiremillLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_LuV.ID)
                .setName("basicmachine.wiremill.tier.06", "Elite Wiremill")
                .setTier(6)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.WiremillZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_ZPM.ID)
                .setName("basicmachine.wiremill.tier.07", "Elite Wiremill II")
                .setTier(7)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.WiremillUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_UV.ID)
                .setName("basicmachine.wiremill.tier.08", "Ultimate Wire Transfigurator")
                .setTier(8)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.WiremillUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_UHV.ID)
                .setName("basicmachine.wiremill.tier.09", "Epic Wire Transfigurator")
                .setTier(9)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.WiremillUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_UEV.ID)
                .setName("basicmachine.wiremill.tier.10", "Epic Wire Transfigurator II")
                .setTier(10)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.WiremillUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_UIV.ID)
                .setName("basicmachine.wiremill.tier.11", "Epic Wire Transfigurator III")
                .setTier(11)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

        ItemList.WiremillUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(WIREMILL_UMV.ID)
                .setName("basicmachine.wiremill.tier.12", "Epic Wire Transfigurator IV")
                .setTier(12)
                .setDescription(MachineType.WIREMILL.tooltipDescription())
                .setRecipes(wiremillRecipes)
                .setSlotsCount(2, 1)
                .setSound(SoundResource.GTCEU_LOOP_MOTOR)
                .setOverlays("WIREMILL")
                .build()
                .getStackForm(1L));

    }

    private void registerArcFurnace() {
        ItemList.Machine_LV_ArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_LV.ID)
                .setName("basicmachine.arcfurnace.tier.01", "Basic Arc Furnace")
                .setTier(1)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_ArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_MV.ID)
                .setName("basicmachine.arcfurnace.tier.02", "Advanced Arc Furnace")
                .setTier(2)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_ArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_HV.ID)
                .setName("basicmachine.arcfurnace.tier.03", "Advanced Arc Furnace II")
                .setTier(3)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 4)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_ArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_EV.ID)
                .setName("basicmachine.arcfurnace.tier.04", "Advanced Arc Furnace III")
                .setTier(4)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_ArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_IV.ID)
                .setName("basicmachine.arcfurnace.tier.05", "Advanced Arc Furnace IV")
                .setTier(5)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ArcFurnaceLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_LuV.ID)
                .setName("basicmachine.arcfurnace.tier.06", "Elite Arc Furnace")
                .setTier(6)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ArcFurnaceZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_ZPM.ID)
                .setName("basicmachine.arcfurnace.tier.07", "Elite Arc Furnace II")
                .setTier(7)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ArcFurnaceUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_UV.ID)
                .setName("basicmachine.arcfurnace.tier.08", "Ultimate Short Circuit Heater")
                .setTier(8)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ArcFurnaceUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_UHV.ID)
                .setName("basicmachine.arcfurnace.tier.09", "Epic Short Circuit Heater")
                .setTier(9)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ArcFurnaceUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_UEV.ID)
                .setName("basicmachine.arcfurnace.tier.10", "Epic Short Circuit Heater II")
                .setTier(10)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ArcFurnaceUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_UIV.ID)
                .setName("basicmachine.arcfurnace.tier.11", "Epic Short Circuit Heater III")
                .setTier(11)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.ArcFurnaceUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ARC_FURNACE_UMV.ID)
                .setName("basicmachine.arcfurnace.tier.12", "Epic Short Circuit Heater IV")
                .setTier(12)
                .setDescription(MachineType.ARC_FURNACE.tooltipDescription())
                .setRecipes(arcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("ARC_FURNACE")
                .build()
                .getStackForm(1L));

    }

    private void registerCentrifuge() {
        ItemList.Machine_LV_Centrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_LV.ID)
                .setName("basicmachine.centrifuge.tier.01", "Basic Centrifuge")
                .setTier(1)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Centrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_MV.ID)
                .setName("basicmachine.centrifuge.tier.02", "Advanced Centrifuge")
                .setTier(2)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Centrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_HV.ID)
                .setName("basicmachine.centrifuge.tier.03", "Turbo Centrifuge")
                .setTier(3)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Centrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_EV.ID)
                .setName("basicmachine.centrifuge.tier.04", "Molecular Separator")
                .setTier(4)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Centrifuge.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_IV.ID)
                .setName("basicmachine.centrifuge.tier.05", "Molecular Cyclone")
                .setTier(5)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.CentrifugeLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_LuV.ID)
                .setName("basicmachine.centrifuge.tier.06", "Elite Centrifuge")
                .setTier(6)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.CentrifugeZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_ZPM.ID)
                .setName("basicmachine.centrifuge.tier.07", "Elite Centrifuge II")
                .setTier(7)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.CentrifugeUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_UV.ID)
                .setName("basicmachine.centrifuge.tier.08", "Ultimate Molecular Tornado")
                .setTier(8)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.CentrifugeUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_UHV.ID)
                .setName("basicmachine.centrifuge.tier.09", "Epic Molecular Tornado")
                .setTier(9)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.CentrifugeUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_UEV.ID)
                .setName("basicmachine.centrifuge.tier.10", "Epic Molecular Tornado II")
                .setTier(10)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.CentrifugeUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_UIV.ID)
                .setName("basicmachine.centrifuge.tier.11", "Epic Molecular Tornado III")
                .setTier(11)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

        ItemList.CentrifugeUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CENTRIFUGE_UMV.ID)
                .setName("basicmachine.centrifuge.tier.12", "Epic Molecular Tornado IV")
                .setTier(12)
                .setDescription(MachineType.CENTRIFUGE.tooltipDescription())
                .setRecipes(centrifugeRecipes)
                .setSlotsCount(2, 6)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CENTRIFUGE)
                .setOverlays("CENTRIFUGE")
                .build()
                .getStackForm(1L));

    }

    private void registerPlasmaArcFurnace() {
        ItemList.Machine_LV_PlasmaArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_LV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.01", "Basic Plasma Arc Furnace")
                .setTier(1)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_PlasmaArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_MV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.02", "Advanced Plasma Arc Furnace")
                .setTier(2)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_PlasmaArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_HV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.03", "Advanced Plasma Arc Furnace II")
                .setTier(3)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 4)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_PlasmaArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_EV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.04", "Advanced Plasma Arc Furnace III")
                .setTier(4)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_PlasmaArcFurnace.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_IV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.05", "Advanced Plasma Arc Furnace IV")
                .setTier(5)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.PlasmaArcFurnaceLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_LuV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.06", "Elite Plasma Arc Furnace")
                .setTier(6)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.PlasmaArcFurnaceZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_ZPM.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.07", "Elite Plasma Arc Furnace II")
                .setTier(7)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.PlasmaArcFurnaceUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_UV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.08", "Ultimate Plasma Discharge Heater")
                .setTier(8)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.PlasmaArcFurnaceUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_UHV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.09", "Epic Plasma Discharge Heater")
                .setTier(9)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.PlasmaArcFurnaceUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_UEV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.10", "Epic Plasma Discharge Heater II")
                .setTier(10)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.PlasmaArcFurnaceUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_UIV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.11", "Epic Plasma Discharge Heater III")
                .setTier(11)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));

        ItemList.PlasmaArcFurnaceUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(PLASMA_ARC_FURNACE_UMV.ID)
                .setName("basicmachine.plasmaarcfurnace.tier.12", "Epic Plasma Discharge Heater IV")
                .setTier(12)
                .setDescription(MachineType.PLASMA_ARC_FURNACE.tooltipDescription())
                .setRecipes(plasmaArcFurnaceRecipes)
                .setSlotsCount(1, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_ARC)
                .setOverlays("PLASMA_ARC_FURNACE")
                .build()
                .getStackForm(1L));
    }

    private void registerCanningMachine() {
        ItemList.Machine_LV_Canner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNER_LV.ID)
                .setName("basicmachine.canner.tier.01", "Basic Canning Machine")
                .setTier(1)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_Canner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNER_MV.ID)
                .setName("basicmachine.canner.tier.02", "Advanced Canning Machine")
                .setTier(2)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_Canner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNER_HV.ID)
                .setName("basicmachine.canner.tier.03", "Advanced Canning Machine II")
                .setTier(3)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_Canner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNER_EV.ID)
                .setName("basicmachine.canner.tier.04", "Advanced Canning Machine III")
                .setTier(4)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_Canner.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNER_IV.ID)
                .setName("basicmachine.canner.tier.05", "Advanced Canning Machine IV")
                .setTier(5)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.CanningMachineLuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNING_MACHINE_LuV.ID)
                .setName("basicmachine.canner.tier.06", "Elite Canning Machine")
                .setTier(6)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.CanningMachineZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNING_MACHINE_ZPM.ID)
                .setName("basicmachine.canner.tier.07", "Elite Canning Machine II")
                .setTier(7)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.CanningMachineUV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNING_MACHINE_UV.ID)
                .setName("basicmachine.canner.tier.08", "Ultimate Can Operator")
                .setTier(8)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.CanningMachineUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNING_MACHINE_UHV.ID)
                .setName("basicmachine.canner.tier.09", "Epic Can Operator")
                .setTier(9)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.CanningMachineUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNING_MACHINE_UEV.ID)
                .setName("basicmachine.canner.tier.10", "Epic Can Operator II")
                .setTier(10)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.CanningMachineUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNING_MACHINE_UIV.ID)
                .setName("basicmachine.canner.tier.11", "Epic Can Operator III")
                .setTier(11)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));

        ItemList.CanningMachineUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CANNING_MACHINE_UMV.ID)
                .setName("basicmachine.canner.tier.12", "Epic Can Operator IV")
                .setTier(12)
                .setDescription(MachineType.CANNER.tooltipDescription())
                .setRecipes(cannerRecipes)
                .setSlotsCount(2, 2)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("CANNER")
                .build()
                .getStackForm(1L));
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

    private static void registerCompressedBus() {
        ItemList.CompressedOutputBusLuV.set(
            new MTEHatchOutputBusCompressed(
                HATCH_OUTPUT_BUS_COMPRESSED_1.ID,
                "hatch.comp-output-bus.tier.00",
                "Compressed Output Bus (LuV)",
                VoltageIndex.LuV,
                256).getStackForm(1));

        ItemList.CompressedOutputBusZPM.set(
            new MTEHatchOutputBusCompressed(
                HATCH_OUTPUT_BUS_COMPRESSED_2.ID,
                "hatch.comp-output-bus.tier.01",
                "Compressed Output Bus (ZPM)",
                VoltageIndex.ZPM,
                2048).getStackForm(1));

        ItemList.CompressedOutputBusUV.set(
            new MTEHatchOutputBusCompressed(
                HATCH_OUTPUT_BUS_COMPRESSED_3.ID,
                "hatch.comp-output-bus.tier.02",
                "Compressed Output Bus (UV)",
                VoltageIndex.UV,
                16384).getStackForm(1));

        ItemList.CompressedOutputBusUHV.set(
            new MTEHatchOutputBusCompressed(
                HATCH_OUTPUT_BUS_COMPRESSED_4.ID,
                "hatch.comp-output-bus.tier.03",
                "Compressed Output Bus (UHV)",
                VoltageIndex.UHV,
                131072).getStackForm(1));

        ItemList.CompressedOutputBusUEV.set(
            new MTEHatchOutputBusCompressed(
                HATCH_OUTPUT_BUS_QUANTUM_1.ID,
                "hatch.quantum-output-bus.tier.00",
                "Quantum Output Bus (UEV)",
                VoltageIndex.UEV,
                2097152).getStackForm(1));

        ItemList.CompressedOutputBusUIV.set(
            new MTEHatchOutputBusCompressed(
                HATCH_OUTPUT_BUS_QUANTUM_2.ID,
                "hatch.quantum-output-bus.tier.01",
                "Quantum Output Bus (UIV)",
                VoltageIndex.UIV,
                Integer.MAX_VALUE / 64L).getStackForm(1));

        ItemList.CompressedOutputBusUMV.set(
            new MTEHatchOutputBusCompressed(
                HATCH_OUTPUT_BUS_QUANTUM_3.ID,
                "hatch.quantum-output-bus.tier.02",
                "Quantum Output Bus (UMV)",
                VoltageIndex.UMV,
                Integer.MAX_VALUE * 64L).getStackForm(1));

        ItemList.CompressedOutputBusUXV.set(
            new MTEHatchOutputBusCompressed(
                HATCH_OUTPUT_BUS_QUANTUM_4.ID,
                "hatch.quantum-output-bus.tier.03",
                "Quantum Output Bus (UXV)",
                VoltageIndex.UXV,
                Long.MAX_VALUE / 64L).getStackForm(1));

        ItemList.CompressedInputBusLuV.set(
            new MTEHatchInputBusCompressed(
                HATCH_INPUT_BUS_COMPRESSED_1.ID,
                "hatch.comp-input-bus.tier.00",
                "Compressed Input Bus (LuV)",
                VoltageIndex.LuV,
                256).getStackForm(1));

        ItemList.CompressedInputBusZPM.set(
            new MTEHatchInputBusCompressed(
                HATCH_INPUT_BUS_COMPRESSED_2.ID,
                "hatch.comp-input-bus.tier.01",
                "Compressed Input Bus (ZPM)",
                VoltageIndex.ZPM,
                2048).getStackForm(1));

        ItemList.CompressedInputBusUV.set(
            new MTEHatchInputBusCompressed(
                HATCH_INPUT_BUS_COMPRESSED_3.ID,
                "hatch.comp-input-bus.tier.02",
                "Compressed Input Bus (UV)",
                VoltageIndex.UV,
                16384).getStackForm(1));

        ItemList.CompressedInputBusUHV.set(
            new MTEHatchInputBusCompressed(
                HATCH_INPUT_BUS_COMPRESSED_4.ID,
                "hatch.comp-input-bus.tier.03",
                "Compressed Input Bus (UHV)",
                VoltageIndex.UHV,
                131072).getStackForm(1));

        ItemList.CompressedInputBusUEV.set(
            new MTEHatchInputBusCompressed(
                HATCH_INPUT_BUS_QUANTUM_1.ID,
                "hatch.quantum-input-bus.tier.00",
                "Quantum Input Bus (UEV)",
                VoltageIndex.UEV,
                2097152).getStackForm(1));

        ItemList.CompressedInputBusUIV.set(
            new MTEHatchInputBusCompressed(
                HATCH_INPUT_BUS_QUANTUM_2.ID,
                "hatch.quantum-input-bus.tier.01",
                "Quantum Input Bus (UIV)",
                VoltageIndex.UIV,
                Integer.MAX_VALUE / 64L).getStackForm(1));

        ItemList.CompressedInputBusUMV.set(
            new MTEHatchInputBusCompressed(
                HATCH_INPUT_BUS_QUANTUM_3.ID,
                "hatch.quantum-input-bus.tier.02",
                "Quantum Input Bus (UMV)",
                VoltageIndex.UMV,
                Integer.MAX_VALUE * 64L).getStackForm(1));

        ItemList.CompressedInputBusUXV.set(
            new MTEHatchInputBusCompressed(
                HATCH_INPUT_BUS_QUANTUM_4.ID,
                "hatch.quantum-input-bus.tier.03",
                "Quantum Input Bus (UXV)",
                VoltageIndex.UXV,
                Long.MAX_VALUE / 64L).getStackForm(1));
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
                .getStackForm(1L));
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
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_LV.ID)
                .setName("basicmachine.circuitassembler.tier.01", "Basic Circuit Assembler")
                .setTier(1)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_MV_CircuitAssembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_MV.ID)
                .setName("basicmachine.circuitassembler.tier.02", "Advanced Circuit Assembler")
                .setTier(2)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_HV_CircuitAssembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_HV.ID)
                .setName("basicmachine.circuitassembler.tier.03", "Advanced Circuit Assembler II")
                .setTier(3)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_EV_CircuitAssembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_EV.ID)
                .setName("basicmachine.circuitassembler.tier.04", "Advanced Circuit Assembler III")
                .setTier(4)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_IV_CircuitAssembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_IV.ID)
                .setName("basicmachine.circuitassembler.tier.05", "Advanced Circuit Assembler IV")
                .setTier(5)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_LuV_CircuitAssembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_LuV.ID)
                .setName("basicmachine.circuitassembler.tier.06", "Advanced Circuit Assembler V")
                .setTier(6)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_ZPM_CircuitAssembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_ZPM.ID)
                .setName("basicmachine.circuitassembler.tier.07", "Advanced Circuit Assembler VI")
                .setTier(7)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.Machine_UV_CircuitAssembler.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_UV.ID)
                .setName("basicmachine.circuitassembler.tier.08", "Advanced Circuit Assembler VII")
                .setTier(8)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.CircuitAssemblerUHV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_UHV.ID)
                .setName("basicmachine.circuitassembler.tier.09", "Ultimate Circuit Assembling Machine")
                .setTier(9)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.CircuitAssemblerUEV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_UEV.ID)
                .setName("basicmachine.circuitassembler.tier.10", "Ultimate Circuit Assembling Machine II")
                .setTier(10)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.CircuitAssemblerUIV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_UIV.ID)
                .setName("basicmachine.circuitassembler.tier.11", "Ultimate Circuit Assembling Machine III")
                .setTier(11)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.CircuitAssemblerUMV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_UMV.ID)
                .setName("basicmachine.circuitassembler.tier.12", "Ultimate Circuit Assembling Machine IV")
                .setTier(12)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.CircuitAssemblerUXV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_UXV.ID)
                .setName("basicmachine.circuitassembler.tier.13", "Ultimate Circuit Assembling Machine V")
                .setTier(13)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));

        ItemList.CircuitAssemblerMAX.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(CIRCUIT_ASSEMBLER_MAX.ID)
                .setName("basicmachine.circuitassembler.tier.14", "MAX Circuit Assembling Machine")
                .setTier(14)
                .setDescription(MachineType.CIRCUIT_ASSEMBLER.tooltipDescription())
                .setRecipes(circuitAssemblerRecipes)
                .setSlotsCount(6, 1)
                .setFluidSlots(true, false)
                .setSound(SoundResource.GTCEU_LOOP_ASSEMBLER)
                .setOverlays("CIRCUITASSEMBLER")
                .build()
                .getStackForm(1L));
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

    private static void registerDrawerFramer() {

        // if storage drawers is not loaded, do not load the drawer framer machine
        if (!StorageDrawers.isModLoaded()) return;

        ItemList.Machine_LV_DrawerFramer
            .set(new MTEDrawerFramer(GT_Framer_LV.ID, "framer.tier.01", "Basic Drawer Framer", 1).getStackForm(1L));
        ItemList.Machine_MV_DrawerFramer
            .set(new MTEDrawerFramer(GT_Framer_MV.ID, "framer.tier.02", "Advanced Drawer Framer", 2).getStackForm(1L));
        ItemList.Machine_HV_DrawerFramer
            .set(new MTEDrawerFramer(GT_Framer_HV.ID, "framer.tier.03", "Precise Drawer Framer", 3).getStackForm(1L));

    }

    private static void registerNacHatches() {
        ItemList.Hatch_VacuumConveyor_Input.set(
            new MTEHatchVacuumConveyorInput(
                HATCH_VACUUM_CONVEYOR_INPUT.ID,
                "vacuum.hatch.input",
                "Vacuum Conveyor Input",
                11).getStackForm(1L));
        ItemList.Hatch_VacuumConveyor_Output.set(
            new MTEHatchVacuumConveyorOutput(
                HATCH_VACUUM_CONVEYOR_OUTPUT.ID,
                "vacuum.hatch.output",
                "Vacuum Conveyor Output",
                11).getStackForm(1L));
        ItemList.Hatch_Splitter_Level.set(
            new MTEHatchSplitterRedstone(
                HATCH_SPLITTER_LEVEL.ID,
                "hatch.splitter.redstone",
                "Splitter Redstone Input",
                10).getStackForm(1));

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
        registerCompressedBus();
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
        registerNacHatches();
        registerDrawerFramer();

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
