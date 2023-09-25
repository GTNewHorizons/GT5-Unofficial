package gregtech.loaders.postload;

import gregtech.api.enums.SoundResource;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_ProcessingArray_Manager;

public class GT_ProcessingArrayRecipeLoader {

    public static void registerDefaultGregtechMaps() {

        // Alloy Smelter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.alloysmelter", RecipeMap.sAlloySmelterRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.alloysmelter", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Arc Furnace
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.arcfurnace", RecipeMap.sArcFurnaceRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.arcfurnace", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Assembler
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.assembler", RecipeMap.sAssemblerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.assembler", SoundResource.NONE);
        // Autoclave
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.autoclave", RecipeMap.sAutoclaveRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.autoclave", SoundResource.NONE);
        // Bender
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.bender", RecipeMap.sBenderRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.bender", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Boxinator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.boxinator", RecipeMap.sBoxinatorRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.boxinator", SoundResource.NONE);
        // Brewery
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.brewery", RecipeMap.sBrewingRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.brewery", SoundResource.NONE);
        // Canner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.canner", RecipeMap.sCannerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.canner", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Centrifuge
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.centrifuge", RecipeMap.sMultiblockCentrifugeRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.centrifuge", SoundResource.NONE);
        // Chemical Bath
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.chemicalbath", RecipeMap.sChemicalBathRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.chemicalbath", SoundResource.NONE);
        // Chemical Reactor
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.chemicalreactor", RecipeMap.sMultiblockChemicalRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.chemicalreactor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Circuit Assembler
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.circuitassembler", RecipeMap.sCircuitAssemblerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.circuitassembler", SoundResource.NONE);
        // Compressor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.compressor", RecipeMap.sCompressorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.compressor", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Cutting Machine
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.cutter", RecipeMap.sCutterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.cutter", SoundResource.NONE);
        // Distillery
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.distillery", RecipeMap.sDistilleryRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.distillery", SoundResource.GT_MACHINES_DISTILLERY_LOOP);
        // Electrolyzer
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.electrolyzer", RecipeMap.sMultiblockElectrolyzerRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.electrolyzer", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Extractor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.extractor", RecipeMap.sExtractorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.extractor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Extruder
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.extruder", RecipeMap.sExtruderRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.extruder", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Fermenter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fermenter", RecipeMap.sFermentingRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fermenter", SoundResource.NONE);
        // Fluid Canner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidcanner", RecipeMap.sFluidCannerRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.fluidcanner", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Fluid Extractor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidextractor", RecipeMap.sFluidExtractionRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.fluidextractor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Fluid Heater
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidheater", RecipeMap.sFluidHeaterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fluidheater", SoundResource.NONE);
        // Fluid Solidifier
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.fluidsolidifier", RecipeMap.sFluidSolidficationRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fluidsolidifier", SoundResource.NONE);
        // Forge Hammer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.hammer", RecipeMap.sHammerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.hammer", SoundResource.RANDOM_ANVIL_USE);
        // Forming Press
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.press", RecipeMap.sPressRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.press", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Laser Engraver
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.laserengraver", RecipeMap.sLaserEngraverRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.laserengraver", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Lathe
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.lathe", RecipeMap.sLatheRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.lathe", SoundResource.NONE);
        // Macerator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.macerator", RecipeMap.sMaceratorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.macerator", SoundResource.IC2_MACHINES_MACERATOR_OP);
        // Magnetic Separator
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.electromagneticseparator", RecipeMap.sElectroMagneticSeparatorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.electromagneticseparator", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Matter Amplifier
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.amplifab", RecipeMap.sAmplifiers);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.amplifab", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Microwave
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.microwave", RecipeMap.sMicrowaveRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.microwave", SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP);
        // Mixer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.mixer", RecipeMap.sMultiblockMixerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.mixer", SoundResource.NONE);
        // Ore Washer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.orewasher", RecipeMap.sOreWasherRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.orewasher", SoundResource.NONE);
        // Plasma Arc Furnace
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.plasmaarcfurnace", RecipeMap.sPlasmaArcFurnaceRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.plasmaarcfurnace", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Polarizer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.polarizer", RecipeMap.sPolarizerRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.polarizer", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Printer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.printer", RecipeMap.sPrinterRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.printer", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Recycler
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.recycler", RecipeMap.sRecyclerRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.recycler", SoundResource.IC2_MACHINES_RECYCLER_OP);
        // Scanner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.scanner", RecipeMap.sScannerFakeRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.scanner", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Sifter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.sifter", RecipeMap.sSifterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.sifter", SoundResource.NONE);
        // Slicer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.slicer", RecipeMap.sSlicerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.slicer", SoundResource.NONE);
        // Thermal Centrifuge
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.thermalcentrifuge", RecipeMap.sThermalCentrifugeRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.thermalcentrifuge", SoundResource.NONE);
        // Unboxinator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.unboxinator", RecipeMap.sUnboxinatorRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.unboxinator", SoundResource.NONE);
        // Wiremill
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.wiremill", RecipeMap.sWiremillRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.wiremill", SoundResource.IC2_MACHINES_RECYCLER_OP);
    }
}
