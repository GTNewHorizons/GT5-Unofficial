package gtPlusPlus.core.recipe;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class RECIPES_MachineComponents {

    // Wire
    public static String wireTier0 = "wireGt01Lead";
    public static String wireTier1 = "wireGt01RedAlloy";
    public static String wireTier6 = "wireGt08NiobiumTitanium";
    public static String wireTier7 = "wireGt08Osmium";
    public static String wireTier8 = "wireGt08Naquadah";
    public static String wireTier9 = "wireGt08Superconductor";
    public static String wireTier10 = "wireGt16Superconductor";

    // Cable
    public static String cableTier0 = "cableGt01Lead";
    public static String cableTier1 = "cableGt01RedAlloy";
    public static String cableTier6 = "cableGt04Tungsten";
    public static String cableTier7 = "cableGt04NiobiumTitanium";
    public static String cableTier8 = "cableGt04Osmium";
    public static String cableTier9 = "cableGt04Naquadah";
    public static String cableTier10 = "wireGt08Superconductor";

    // Plates
    public static String plateTier0 = "plateWroughtIron";
    public static String plateTier7 = "plateChrome";
    public static String plateTier8 = "plateIridium";
    public static String plateTier9 = "plateOsmium";
    public static String plateTier10 = "plateNeutronium";

    // rods
    public static String rodTier0 = "stickWroughtIron";
    public static String rodTier1 = "stickPotin";
    public static String rodTier2 = "stickIronMagnetic";
    public static String rodTier7a = "stickChrome";
    public static String rodTier8a = "stickIridium";
    public static String rodTier9a = "stickOsmium";
    public static String rodTier10a = "stickNeutronium";
    public static String rodTier7b = "stickPlatinum";
    public static String rodTier8b = "stickChrome";
    public static String rodTier9b = "stickIridium";
    public static String rodTier10b = "stickOsmium";
    public static String rodTier7c = "stickTitanium";
    public static String rodTier8c = "stickTungstenSteel";
    public static String rodTier9c = "stickNaquadah";
    public static String rodTier10c = "stickOsmium";

    // Screws
    public static String screwTier0 = "screwPotin";
    public static String screwTier7 = "screwChrome";
    public static String screwTier8 = "screwIridium";
    public static String screwTier9 = "screwOsmium";
    public static String screwTier10 = "screwNeutronium";

    // Rotors
    public static String rotorTier0 = "rotorPotin";
    public static String rotorTier7 = "rotorChrome";
    public static String rotorTier8 = "rotorIridium";
    public static String rotorTier9 = "rotorOsmium";
    public static String rotorTier10 = "rotorNeutronium";

    // Fluid Pipe
    public static String pipeTier0 = "pipeSmallLead";
    public static String pipeTier7 = "pipeHugeSteel";
    public static String pipeTier8 = "pipeHugeStainlessSteel";
    public static String pipeTier9 = "pipeHugeTitanium";
    public static String pipeTier10 = "pipeHugeTungstenSteel";

    // Rubber Ring/Plate
    public static String itemRubberRing = "ringRubber";
    public static String plateRubber = "plateRubber";

    // Circuits
    public static String circuitTier0 = "circuitPrimitive";
    public static String circuitTier1 = "circuitBasic";
    public static String circuitTier6 = "circuitMaster";
    public static String circuitTier7 = "circuitUltimate";
    public static String circuitTier8 = "circuitSuperconductor";
    public static String circuitTier9 = "circuitInfinite";
    public static String circuitTier10 = "circuitQuantum";

    // small gears
    public static String smallGearTier0 = "gearGtSmallWroughtIron";
    public static String smallGearTier7 = "gearGtSmallChrome";
    public static String smallGearTier8 = "gearGtSmallIridium";
    public static String smallGearTier9 = "gearGtSmallOsmium";
    public static String smallGearTier10 = "gearGtSmallNeutronium";

    public static final void loadRecipes() {
        Logger.INFO("Loading Recipes for the Various Circuits and Machine components.");
        onlyULVComponents();
    }

    private static void onlyULVComponents() {
        RecipeUtils.addShapedGregtechRecipe(
                cableTier1,
                wireTier0,
                rodTier0,
                wireTier0,
                rodTier2,
                wireTier0,
                rodTier0,
                wireTier0,
                cableTier1,
                CI.electricMotor_ULV);

        RecipeUtils.addShapedGregtechRecipe(
                cableTier0,
                rotorTier0,
                itemRubberRing,
                CI.craftingToolScrewdriver,
                pipeTier0,
                CI.craftingToolWrench,
                itemRubberRing,
                CI.electricMotor_ULV,
                cableTier0,
                CI.electricPump_ULV);

        RecipeUtils.addShapedGregtechRecipe(
                plateTier0,
                plateTier0,
                plateTier0,
                cableTier0,
                rodTier1,
                rodTier1,
                cableTier0,
                CI.electricMotor_ULV,
                smallGearTier0,
                CI.electricPiston_ULV);

        RecipeUtils.addShapedGregtechRecipe(
                cableTier0,
                cableTier0,
                cableTier0,
                CI.electricMotor_ULV,
                rodTier1,
                CI.electricMotor_ULV,
                CI.electricPiston_ULV,
                circuitTier0,
                rodTier1,
                CI.robotArm_ULV);

        RecipeUtils.addShapedGregtechRecipe(
                plateRubber,
                plateRubber,
                plateRubber,
                CI.electricMotor_ULV,
                cableTier0,
                CI.electricMotor_ULV,
                plateRubber,
                plateRubber,
                plateRubber,
                CI.conveyorModule_ULV);

        RecipeUtils.addShapedGregtechRecipe(
                rodTier2,
                rodTier2,
                circuitTier0,
                cableTier0,
                circuitTier1,
                rodTier2,
                circuitTier0,
                cableTier0,
                rodTier2,
                CI.emitter_ULV);

        RecipeUtils.addShapedGregtechRecipe(
                wireTier0,
                circuitTier0,
                wireTier0,
                circuitTier0,
                circuitTier1,
                circuitTier0,
                wireTier0,
                circuitTier0,
                wireTier0,
                CI.fieldGenerator_ULV);

        RecipeUtils.addShapedRecipe(
                plateTier0,
                null,
                circuitTier1,
                plateTier0,
                rodTier2,
                null,
                circuitTier0,
                plateTier0,
                plateTier0,
                CI.sensor_ULV);
    }
}
