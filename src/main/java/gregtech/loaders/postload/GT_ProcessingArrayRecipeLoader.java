package gregtech.loaders.postload;

import gregtech.api.util.GT_ProcessingArray_Manager;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class GT_ProcessingArrayRecipeLoader {

    public static void registerDefaultGregtechMaps() {

        // Alloy Smelter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.alloysmelter", GT_Recipe_Map.sAlloySmelterRecipes);
        // Arc Furnace
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.arcfurnace", GT_Recipe_Map.sArcFurnaceRecipes);
        // Assembler
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.assembler", GT_Recipe_Map.sAssemblerRecipes);
        // Autoclave
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.autoclave", GT_Recipe_Map.sAutoclaveRecipes);
        // Bender
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.bender", GT_Recipe_Map.sBenderRecipes);
        // Boxinator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.boxinator", GT_Recipe_Map.sBoxinatorRecipes);
        // Brewery
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.brewery", GT_Recipe_Map.sBrewingRecipes);
        // Canner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.canner", GT_Recipe_Map.sCannerRecipes);
        // Centrifuge
        GT_ProcessingArray_Manager
                .addRecipeMapToPA("basicmachine.centrifuge", GT_Recipe_Map.sMultiblockCentrifugeRecipes);
        // Chemical Bath
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.chemicalbath", GT_Recipe_Map.sChemicalBathRecipes);
        // Chemical Reactor
        GT_ProcessingArray_Manager
                .addRecipeMapToPA("basicmachine.chemicalreactor", GT_Recipe_Map.sMultiblockChemicalRecipes);
        // Circuit Assembler
        GT_ProcessingArray_Manager
                .addRecipeMapToPA("basicmachine.circuitassembler", GT_Recipe_Map.sCircuitAssemblerRecipes);
        // Compressor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.compressor", GT_Recipe_Map.sCompressorRecipes);
        // Cutting Machine
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.cutter", GT_Recipe_Map.sCutterRecipes);
        // Distillery
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.distillery", GT_Recipe_Map.sDistilleryRecipes);
        // Electrolyzer
        GT_ProcessingArray_Manager
                .addRecipeMapToPA("basicmachine.electrolyzer", GT_Recipe_Map.sMultiblockElectrolyzerRecipes);
        // Extractor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.extractor", GT_Recipe_Map.sExtractorRecipes);
        // Extruder
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.extruder", GT_Recipe_Map.sExtruderRecipes);
        // Fermenter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fermenter", GT_Recipe_Map.sFermentingRecipes);
        // Fluid Canner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidcanner", GT_Recipe_Map.sFluidCannerRecipes);
        // Fluid Extractor
        GT_ProcessingArray_Manager
                .addRecipeMapToPA("basicmachine.fluidextractor", GT_Recipe_Map.sFluidExtractionRecipes);
        // Fluid Heater
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidheater", GT_Recipe_Map.sFluidHeaterRecipes);
        // Fluid Solidifier
        GT_ProcessingArray_Manager
                .addRecipeMapToPA("basicmachine.fluidsolidifier", GT_Recipe_Map.sFluidSolidficationRecipes);
        // Forge Hammer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.hammer", GT_Recipe_Map.sHammerRecipes);
        // Forming Press
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.press", GT_Recipe_Map.sPressRecipes);
        // Laser Engraver
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.laserengraver", GT_Recipe_Map.sLaserEngraverRecipes);
        // Lathe
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.lathe", GT_Recipe_Map.sLatheRecipes);
        // Macerator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.macerator", GT_Recipe_Map.sMaceratorRecipes);
        // Magnetic Separator
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.electromagneticseparator",
                GT_Recipe_Map.sElectroMagneticSeparatorRecipes);
        // Matter Amplifier
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.amplifab", GT_Recipe_Map.sAmplifiers);
        // Microwave
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.microwave", GT_Recipe_Map.sMicrowaveRecipes);
        // Mixer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.mixer", GT_Recipe_Map.sMultiblockMixerRecipes);
        // Ore Washer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.orewasher", GT_Recipe_Map.sOreWasherRecipes);
        // Plasma Arc Furnace
        GT_ProcessingArray_Manager
                .addRecipeMapToPA("basicmachine.plasmaarcfurnace", GT_Recipe_Map.sPlasmaArcFurnaceRecipes);
        // Polarizer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.polarizer", GT_Recipe_Map.sPolarizerRecipes);
        // Printer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.printer", GT_Recipe_Map.sPrinterRecipes);
        // Recycler
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.recycler", GT_Recipe_Map.sRecyclerRecipes);
        // Scanner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.scanner", GT_Recipe_Map.sScannerFakeRecipes);
        // Sifter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.sifter", GT_Recipe_Map.sSifterRecipes);
        // Slicer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.slicer", GT_Recipe_Map.sSlicerRecipes);
        // Thermal Centrifuge
        GT_ProcessingArray_Manager
                .addRecipeMapToPA("basicmachine.thermalcentrifuge", GT_Recipe_Map.sThermalCentrifugeRecipes);
        // Unboxinator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.unboxinator", GT_Recipe_Map.sUnboxinatorRecipes);
        // Wiremill
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.wiremill", GT_Recipe_Map.sWiremillRecipes);
    }
}
