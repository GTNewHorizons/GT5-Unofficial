package gregtech.loaders.postload;

import gregtech.api.enums.SoundResource;
import gregtech.api.util.GT_ProcessingArray_Manager;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class GT_ProcessingArrayRecipeLoader {

    public static void registerDefaultGregtechMaps() {

        // Alloy Smelter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.alloysmelter", GT_Recipe_Map.sAlloySmelterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.alloysmelter",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Arc Furnace
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.arcfurnace", GT_Recipe_Map.sArcFurnaceRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.arcfurnace",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Assembler
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.assembler", GT_Recipe_Map.sAssemblerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.assembler", SoundResource.NONE);
        // Autoclave
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.autoclave", GT_Recipe_Map.sAutoclaveRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.autoclave", SoundResource.NONE);
        // Bender
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.bender", GT_Recipe_Map.sBenderRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.bender",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Boxinator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.boxinator", GT_Recipe_Map.sBoxinatorRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.boxinator", SoundResource.NONE);
        // Brewery
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.brewery", GT_Recipe_Map.sBrewingRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.brewery", SoundResource.NONE);
        // Canner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.canner", GT_Recipe_Map.sCannerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.canner", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Centrifuge
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.centrifuge",
                GT_Recipe_Map.sMultiblockCentrifugeRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.centrifuge", SoundResource.NONE);
        // Chemical Bath
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.chemicalbath", GT_Recipe_Map.sChemicalBathRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.chemicalbath", SoundResource.NONE);
        // Chemical Reactor
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.chemicalreactor",
                GT_Recipe_Map.sMultiblockChemicalRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.chemicalreactor",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Circuit Assembler
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.circuitassembler",
                GT_Recipe_Map.sCircuitAssemblerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.circuitassembler", SoundResource.NONE);
        // Compressor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.compressor", GT_Recipe_Map.sCompressorRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.compressor",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Cutting Machine
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.cutter", GT_Recipe_Map.sCutterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.cutter", SoundResource.NONE);
        // Distillery
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.distillery", GT_Recipe_Map.sDistilleryRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.distillery",
                SoundResource.GT_MACHINES_DISTILLERY_LOOP);
        // Electrolyzer
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.electrolyzer",
                GT_Recipe_Map.sMultiblockElectrolyzerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.electrolyzer",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Extractor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.extractor", GT_Recipe_Map.sExtractorRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.extractor",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Extruder
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.extruder", GT_Recipe_Map.sExtruderRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.extruder",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Fermenter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fermenter", GT_Recipe_Map.sFermentingRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fermenter", SoundResource.NONE);
        // Fluid Canner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidcanner", GT_Recipe_Map.sFluidCannerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.fluidcanner",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Fluid Extractor
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.fluidextractor",
                GT_Recipe_Map.sFluidExtractionRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.fluidextractor",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Fluid Heater
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidheater", GT_Recipe_Map.sFluidHeaterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fluidheater", SoundResource.NONE);
        // Fluid Solidifier
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.fluidsolidifier",
                GT_Recipe_Map.sFluidSolidficationRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fluidsolidifier", SoundResource.NONE);
        // Forge Hammer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.hammer", GT_Recipe_Map.sHammerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.hammer", SoundResource.RANDOM_ANVIL_USE);
        // Forming Press
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.press", GT_Recipe_Map.sPressRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.press", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Laser Engraver
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.laserengraver", GT_Recipe_Map.sLaserEngraverRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.laserengraver",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Lathe
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.lathe", GT_Recipe_Map.sLatheRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.lathe", SoundResource.NONE);
        // Macerator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.macerator", GT_Recipe_Map.sMaceratorRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.macerator",
                SoundResource.IC2_MACHINES_MACERATOR_OP);
        // Magnetic Separator
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.electromagneticseparator",
                GT_Recipe_Map.sElectroMagneticSeparatorRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.electromagneticseparator",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Matter Amplifier
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.amplifab", GT_Recipe_Map.sAmplifiers);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.amplifab",
                SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Microwave
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.microwave", GT_Recipe_Map.sMicrowaveRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.microwave",
                SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP);
        // Mixer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.mixer", GT_Recipe_Map.sMultiblockMixerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.mixer", SoundResource.NONE);
        // Ore Washer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.orewasher", GT_Recipe_Map.sOreWasherRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.orewasher", SoundResource.NONE);
        // Plasma Arc Furnace
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.plasmaarcfurnace",
                GT_Recipe_Map.sPlasmaArcFurnaceRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.plasmaarcfurnace",
                SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Polarizer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.polarizer", GT_Recipe_Map.sPolarizerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.polarizer",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Printer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.printer", GT_Recipe_Map.sPrinterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.printer",
                SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Recycler
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.recycler", GT_Recipe_Map.sRecyclerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.recycler",
                SoundResource.IC2_MACHINES_RECYCLER_OP);
        // Scanner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.scanner", GT_Recipe_Map.sScannerFakeRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.scanner",
                SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Sifter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.sifter", GT_Recipe_Map.sSifterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.sifter", SoundResource.NONE);
        // Slicer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.slicer", GT_Recipe_Map.sSlicerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.slicer", SoundResource.NONE);
        // Thermal Centrifuge
        GT_ProcessingArray_Manager.addRecipeMapToPA(
                "basicmachine.thermalcentrifuge",
                GT_Recipe_Map.sThermalCentrifugeRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.thermalcentrifuge", SoundResource.NONE);
        // Unboxinator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.unboxinator", GT_Recipe_Map.sUnboxinatorRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.unboxinator", SoundResource.NONE);
        // Wiremill
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.wiremill", GT_Recipe_Map.sWiremillRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA(
                "basicmachine.wiremill",
                SoundResource.IC2_MACHINES_RECYCLER_OP);
    }
}
