package gregtech.loaders.postload;

import gregtech.api.util.GT_ProcessingArray_Manager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class GT_ProcessingArrayRecipeLoader {

    public static void registerDefaultGregtechMaps() {

        // Centrifuge
        registerMapBetweenRange(361, 365, GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes);

        // Electrolyzer
        registerMapBetweenRange(371, 375, GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes);

        // Assembler
        registerMapBetweenRange(211, 215, GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);

        // Compressor
        registerMapBetweenRange(241, 245, GT_Recipe.GT_Recipe_Map.sCompressorRecipes);

        //Extractor
        registerMapBetweenRange(271, 275, GT_Recipe.GT_Recipe_Map.sExtractorRecipes);

        //Macerator
        registerMapBetweenRange(301, 305, GT_Recipe.GT_Recipe_Map.sMaceratorRecipes);

        // Microwave (New)
        registerMapBetweenRange(311, 315, GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes);

        //Recycler
        registerMapBetweenRange(331, 335, GT_Recipe.GT_Recipe_Map.sRecyclerRecipes);

        //Thermal Centrifuge
        registerMapBetweenRange(381, 385, GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes);

        // Ore Washer
        registerMapBetweenRange(391, 395, GT_Recipe.GT_Recipe_Map.sOreWasherRecipes);

        // Chemical Reactor
        registerMapBetweenRange(421, 425, GT_Recipe.GT_Recipe_Map.sChemicalRecipes);

        // Chemical Bath
        registerMapBetweenRange(541, 545, GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes);

        // Magnetic Seperator
        registerMapBetweenRange(561, 565, GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes);

        // Autoclave
        registerMapBetweenRange(571, 575, GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes);

        // Mixer
        registerMapBetweenRange(581, 585, GT_Recipe.GT_Recipe_Map.sMixerRecipes);

        // Forge Hammer
        registerMapBetweenRange(611, 615, GT_Recipe.GT_Recipe_Map.sHammerRecipes);

        // Sifter
        registerMapBetweenRange(641, 645, GT_Recipe.GT_Recipe_Map.sSifterRecipes);

        // Extruder
        registerMapBetweenRange(281, 285, GT_Recipe.GT_Recipe_Map.sExtruderRecipes);

        // Laser Engraver
        registerMapBetweenRange(591, 595, GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes);

        // Bender
        registerMapBetweenRange(221, 225, GT_Recipe.GT_Recipe_Map.sBenderRecipes);

        // Wiremill
        registerMapBetweenRange(351, 355, GT_Recipe.GT_Recipe_Map.sWiremillRecipes);

        // Arc Furnace
        registerMapBetweenRange(651, 655, GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes);

        // Plasma Arc Furnace
        registerMapBetweenRange(661, 665, GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes);

        // Brewery
        registerMapBetweenRange(491, 495, GT_Recipe.GT_Recipe_Map.sBrewingRecipes);

        // Canner
        registerMapBetweenRange(231, 235, GT_Recipe.GT_Recipe_Map.sCannerRecipes);

        // Cutter
        registerMapBetweenRange(251, 255, GT_Recipe.GT_Recipe_Map.sCutterRecipes);

        // Fermenter
        registerMapBetweenRange(501, 505, GT_Recipe.GT_Recipe_Map.sFermentingRecipes);

        // Fluid Extractor
        registerMapBetweenRange(511, 515, GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes);

        // Fluid Solidifier
        registerMapBetweenRange(521, 525, GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes);

        // Lathe
        registerMapBetweenRange(291, 295, GT_Recipe.GT_Recipe_Map.sLatheRecipes);

        // Boxinator
        registerMapBetweenRange(401, 408, GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes);

        // Unboxinator
        registerMapBetweenRange(411, 418, GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes);

        // Polarizer
        registerMapBetweenRange(551, 555, GT_Recipe.GT_Recipe_Map.sPolarizerRecipes);

        // Printer
        registerMapBetweenRange(321, 328, GT_Recipe.GT_Recipe_Map.sPrinterRecipes);

        // Fluid Canner
        registerMapBetweenRange(431, 435, GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes);

        // Fluid Heater
        registerMapBetweenRange(621, 625, GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes);

        // Distillery
        registerMapBetweenRange(531, 535, GT_Recipe.GT_Recipe_Map.sDistilleryRecipes);

        // Slicer
        registerMapBetweenRange(631, 635, GT_Recipe.GT_Recipe_Map.sSlicerRecipes);

        // Matter Amplifier
        registerMapBetweenRange(471, 475, GT_Recipe.GT_Recipe_Map.sAmplifiers);

        // Circuit Assembler
        registerMapBetweenRange(1180, 1187, GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes);

        // Alloy Smelter
        registerMapBetweenRange(201, 205, GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes);

        // Forming Press
        registerMapBetweenRange(601, 605, GT_Recipe.GT_Recipe_Map.sPressRecipes);

    }

    private static final void registerMapBetweenRange(int aMin, int aMax, GT_Recipe_Map aMap) {
        for (int i=aMin; i<=aMax;i++) {
            GT_ProcessingArray_Manager.registerRecipeMapForMeta(i, aMap);
        }
    }
}
