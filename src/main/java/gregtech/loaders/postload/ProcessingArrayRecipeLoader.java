package gregtech.loaders.postload;

import gregtech.api.enums.SoundResource;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.ProcessingArrayManager;

public class ProcessingArrayRecipeLoader {

    public static void registerDefaultGregtechMaps() {

        // Alloy Smelter
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.alloysmelter", RecipeMaps.alloySmelterRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.alloysmelter", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Arc Furnace
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.arcfurnace", RecipeMaps.arcFurnaceRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.arcfurnace", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Assembler
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.assembler", RecipeMaps.assemblerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.assembler", SoundResource.NONE);
        // Autoclave
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.autoclave", RecipeMaps.autoclaveRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.autoclave", SoundResource.NONE);
        // Bender
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.bender", RecipeMaps.benderRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.bender", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Boxinator
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.boxinator", RecipeMaps.packagerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.boxinator", SoundResource.NONE);
        // Brewery
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.brewery", RecipeMaps.brewingRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.brewery", SoundResource.NONE);
        // Canner
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.canner", RecipeMaps.cannerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.canner", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Centrifuge
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.centrifuge", RecipeMaps.centrifugeNonCellRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.centrifuge", SoundResource.NONE);
        // Chemical Bath
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.chemicalbath", RecipeMaps.chemicalBathRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.chemicalbath", SoundResource.NONE);
        // Chemical Reactor
        ProcessingArrayManager
            .addRecipeMapToPA("basicmachine.chemicalreactor", RecipeMaps.multiblockChemicalReactorRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.chemicalreactor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Circuit Assembler
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.circuitassembler", RecipeMaps.circuitAssemblerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.circuitassembler", SoundResource.NONE);
        // Compressor
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.compressor", RecipeMaps.compressorRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.compressor", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Cutting Machine
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.cutter", RecipeMaps.cutterRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.cutter", SoundResource.NONE);
        // Distillery
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.distillery", RecipeMaps.distilleryRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.distillery", SoundResource.GT_MACHINES_DISTILLERY_LOOP);
        // Electrolyzer
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.electrolyzer", RecipeMaps.electrolyzerNonCellRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.electrolyzer", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Extractor
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.extractor", RecipeMaps.extractorRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.extractor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Extruder
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.extruder", RecipeMaps.extruderRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.extruder", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Fermenter
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.fermenter", RecipeMaps.fermentingRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.fermenter", SoundResource.NONE);
        // Fluid Canner
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.fluidcanner", RecipeMaps.fluidCannerRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.fluidcanner", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Fluid Extractor
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.fluidextractor", RecipeMaps.fluidExtractionRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.fluidextractor", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Fluid Heater
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.fluidheater", RecipeMaps.fluidHeaterRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.fluidheater", SoundResource.NONE);
        // Fluid Solidifier
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.fluidsolidifier", RecipeMaps.fluidSolidifierRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.fluidsolidifier", SoundResource.NONE);
        // Forge Hammer
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.hammer", RecipeMaps.hammerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.hammer", SoundResource.RANDOM_ANVIL_USE);
        // Forming Press
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.press", RecipeMaps.formingPressRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.press", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Laser Engraver
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.laserengraver", RecipeMaps.laserEngraverRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.laserengraver", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Lathe
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.lathe", RecipeMaps.latheRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.lathe", SoundResource.NONE);
        // Macerator
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.macerator", RecipeMaps.maceratorRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.macerator", SoundResource.IC2_MACHINES_MACERATOR_OP);
        // Magnetic Separator
        ProcessingArrayManager
            .addRecipeMapToPA("basicmachine.electromagneticseparator", RecipeMaps.electroMagneticSeparatorRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.electromagneticseparator", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Matter Amplifier
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.amplifab", RecipeMaps.amplifierRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.amplifab", SoundResource.IC2_MACHINES_EXTRACTOR_OP);
        // Microwave
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.microwave", RecipeMaps.microwaveRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.microwave", SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP);
        // Mixer
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.mixer", RecipeMaps.mixerNonCellRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.mixer", SoundResource.NONE);
        // Ore Washer
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.orewasher", RecipeMaps.oreWasherRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.orewasher", SoundResource.NONE);
        // Plasma Arc Furnace
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.plasmaarcfurnace", RecipeMaps.plasmaArcFurnaceRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.plasmaarcfurnace", SoundResource.IC2_MACHINES_INDUCTION_LOOP);
        // Polarizer
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.polarizer", RecipeMaps.polarizerRecipes);
        ProcessingArrayManager
            .addSoundResourceToPA("basicmachine.polarizer", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Printer
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.printer", RecipeMaps.printerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.printer", SoundResource.IC2_MACHINES_COMPRESSOR_OP);
        // Recycler
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.recycler", RecipeMaps.recyclerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.recycler", SoundResource.IC2_MACHINES_RECYCLER_OP);
        // Scanner
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.scanner", RecipeMaps.scannerFakeRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.scanner", SoundResource.IC2_MACHINES_MAGNETIZER_LOOP);
        // Sifter
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.sifter", RecipeMaps.sifterRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.sifter", SoundResource.NONE);
        // Slicer
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.slicer", RecipeMaps.slicerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.slicer", SoundResource.NONE);
        // Thermal Centrifuge
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.thermalcentrifuge", RecipeMaps.thermalCentrifugeRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.thermalcentrifuge", SoundResource.NONE);
        // Unboxinator
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.unboxinator", RecipeMaps.unpackagerRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.unboxinator", SoundResource.NONE);
        // Wiremill
        ProcessingArrayManager.addRecipeMapToPA("basicmachine.wiremill", RecipeMaps.wiremillRecipes);
        ProcessingArrayManager.addSoundResourceToPA("basicmachine.wiremill", SoundResource.IC2_MACHINES_RECYCLER_OP);
    }
}
