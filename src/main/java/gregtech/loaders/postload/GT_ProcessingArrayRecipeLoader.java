package gregtech.loaders.postload;

import gregtech.api.enums.SoundResource;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ProcessingArray_Manager;

public class GT_ProcessingArrayRecipeLoader {

    public static void registerDefaultGregtechMaps() {

        // Alloy Smelter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.alloysmelter", RecipeMaps.alloySmelterRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.alloysmelter", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Arc Furnace
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.arcfurnace", RecipeMaps.arcFurnaceRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.arcfurnace", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Assembler
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.assembler", RecipeMaps.assemblerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.assembler", SoundResource.NONE);
        // Autoclave
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.autoclave", RecipeMaps.autoclaveRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.autoclave", SoundResource.NONE);
        // Bender
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.bender", RecipeMaps.benderRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.bender", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Boxinator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.boxinator", RecipeMaps.packagerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.boxinator", SoundResource.NONE);
        // Brewery
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.brewery", RecipeMaps.brewingRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.brewery", SoundResource.NONE);
        // Canner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.canner", RecipeMaps.cannerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.canner", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Centrifuge
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.centrifuge", RecipeMaps.centrifugeNonCellRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.centrifuge", SoundResource.NONE);
        // Chemical Bath
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.chemicalbath", RecipeMaps.chemicalBathRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.chemicalbath", SoundResource.NONE);
        // Chemical Reactor
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.chemicalreactor", RecipeMaps.multiblockChemicalReactorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.chemicalreactor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Circuit Assembler
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.circuitassembler", RecipeMaps.circuitAssemblerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.circuitassembler", SoundResource.NONE);
        // Compressor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.compressor", RecipeMaps.compressorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.compressor", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Cutting Machine
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.cutter", RecipeMaps.cutterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.cutter", SoundResource.NONE);
        // Distillery
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.distillery", RecipeMaps.distilleryRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.distillery", SoundResource.GT_MACHINES_DISTILLERY_LOOP);
        // Electrolyzer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.electrolyzer", RecipeMaps.electrolyzerNonCellRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.electrolyzer", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Extractor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.extractor", RecipeMaps.extractorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.extractor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Extruder
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.extruder", RecipeMaps.extruderRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.extruder", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Fermenter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fermenter", RecipeMaps.fermentingRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fermenter", SoundResource.NONE);
        // Fluid Canner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidcanner", RecipeMaps.fluidCannerRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.fluidcanner", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Fluid Extractor
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidextractor", RecipeMaps.fluidExtractionRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.fluidextractor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Fluid Heater
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidheater", RecipeMaps.fluidHeaterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fluidheater", SoundResource.NONE);
        // Fluid Solidifier
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.fluidsolidifier", RecipeMaps.fluidSolidifierRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.fluidsolidifier", SoundResource.NONE);
        // Forge Hammer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.hammer", RecipeMaps.hammerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.hammer", SoundResource.RANDOM_ANVIL_USE);
        // Forming Press
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.press", RecipeMaps.formingPressRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.press", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Laser Engraver
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.laserengraver", RecipeMaps.laserEngraverRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.laserengraver", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Lathe
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.lathe", RecipeMaps.latheRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.lathe", SoundResource.NONE);
        // Macerator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.macerator", RecipeMaps.maceratorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.macerator", SoundResource.IC2_MACHINES_MACERATOR_OP);
        // Magnetic Separator
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.electromagneticseparator", RecipeMaps.electroMagneticSeparatorRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.electromagneticseparator", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Matter Amplifier
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.amplifab", RecipeMaps.amplifierRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.amplifab", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Microwave
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.microwave", RecipeMaps.microwaveRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.microwave", SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP);
        // Mixer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.mixer", RecipeMaps.mixerNonCellRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.mixer", SoundResource.NONE);
        // Ore Washer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.orewasher", RecipeMaps.oreWasherRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.orewasher", SoundResource.NONE);
        // Plasma Arc Furnace
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.plasmaarcfurnace", RecipeMaps.plasmaArcFurnaceRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.plasmaarcfurnace", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Polarizer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.polarizer", RecipeMaps.polarizerRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.polarizer", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Printer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.printer", RecipeMaps.printerRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.printer", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Recycler
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.recycler", RecipeMaps.recyclerRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.recycler", SoundResource.IC2_MACHINES_RECYCLER_OP);
        // Scanner
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.scanner", RecipeMaps.scannerFakeRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.scanner", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Sifter
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.sifter", RecipeMaps.sifterRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.sifter", SoundResource.NONE);
        // Slicer
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.slicer", RecipeMaps.slicerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.slicer", SoundResource.NONE);
        // Thermal Centrifuge
        GT_ProcessingArray_Manager
            .addRecipeMapToPA("basicmachine.thermalcentrifuge", RecipeMaps.thermalCentrifugeRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.thermalcentrifuge", SoundResource.NONE);
        // Unboxinator
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.unboxinator", RecipeMaps.unpackagerRecipes);
        GT_ProcessingArray_Manager.addSoundResourceToPA("basicmachine.unboxinator", SoundResource.NONE);
        // Wiremill
        GT_ProcessingArray_Manager.addRecipeMapToPA("basicmachine.wiremill", RecipeMaps.wiremillRecipes);
        GT_ProcessingArray_Manager
            .addSoundResourceToPA("basicmachine.wiremill", SoundResource.IC2_MACHINES_RECYCLER_OP);
    }
}
