package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RECIPES_MachineComponents {

	//Wire
	static String wireTier0 = "wireGt01Lead";
	static String wireTier1 = "wireGt01RedAlloy";
	static String wireTier6 = "wireGt08NiobiumTitanium";
	static String wireTier7 = "wireGt08Osmium";
	static String wireTier8 = "wireGt08Naquadah";
	static String wireTier9 = "wireGt08Superconductor";
	static String wireTier10 = "wireGt16Superconductor";

	//Cable
	static String cableTier0 = "cableGt01Lead";
	static String cableTier1 = "cableGt01RedAlloy";
	static String cableTier6 = "cableGt04Tungsten";
	static String cableTier7 = "cableGt04NiobiumTitanium";
	static String cableTier8 = "cableGt04Osmium";
	static String cableTier9 = "cableGt04Naquadah";
	static String cableTier10 = "wireGt08Superconductor";

	//Plates
	static String plateTier0 = "plateWroughtIron";
	static String plateTier7 = "plateChrome";
	static String plateTier8 = "plateIridium";
	static String plateTier9 = "plateOsmium";
	static String plateTier10 = "plateNeutronium";

	//rods
	static String rodTier0 = "stickWroughtIron";
	static String rodTier1 = "stickPotin";
	static String rodTier2 = "stickIronMagnetic";
	static String rodTier7a = "stickChrome";
	static String rodTier8a = "stickIridium";
	static String rodTier9a = "stickOsmium";
	static String rodTier10a = "stickNeutronium";
	static String rodTier7b = "stickPlatinum";
	static String rodTier8b = "stickChrome";
	static String rodTier9b = "stickIridium";
	static String rodTier10b = "stickOsmium";
	static String rodTier7c = "stickTitanium";
	static String rodTier8c = "stickTungstenSteel";
	static String rodTier9c = "stickNaquadah";
	static String rodTier10c = "stickOsmium";

	//Screws
	static String screwTier0 = "screwPotin";
	static String screwTier7 = "screwChrome";
	static String screwTier8 = "screwIridium";
	static String screwTier9 = "screwOsmium";
	static String screwTier10 = "screwNeutronium";

	//Rotors
	static String rotorTier0 = "rotorPotin";
	static String rotorTier7 = "rotorChrome";
	static String rotorTier8 = "rotorIridium";
	static String rotorTier9 = "rotorOsmium";
	static String rotorTier10 = "rotorNeutronium";

	//Fluid Pipe
	static String pipeTier0 = "pipeSmallLead";
	static String pipeTier7 = "pipeHugeSteel";
	static String pipeTier8 = "pipeHugeStainlessSteel";
	static String pipeTier9 = "pipeHugeTitanium";
	static String pipeTier10 = "pipeHugeTungstenSteel";

	//Rubber Ring/Plate
	static String itemRubberRing = "ringRubber";
	static String plateRubber = "plateRubber";

	//Circuits
	static String circuitTier0 = "circuitPrimitive";
	static String circuitTier1 = "circuitBasic";
	static String circuitTier6 = "circuitMaster";
	static String circuitTier7 = "circuitUltimate";
	static String circuitTier8 = "circuitSymbiotic";
	static String circuitTier9 = "circuitNeutronic";
	static String circuitTier10 = "circuitQuantum";

	//small gears
	static String smallGearTier0 = "gearGtSmallWroughtIron";
	static String smallGearTier7 = "gearGtSmallChrome";
	static String smallGearTier8 = "gearGtSmallIridium";
	static String smallGearTier9 = "gearGtSmallOsmium";
	static String smallGearTier10 = "gearGtSmallNeutronium";

	//Crafting Tools
	static String craftingToolWrench = "craftingToolWrench";
	static String craftingToolScrewdriver = "craftingToolScrewdriver";

	public static final void RECIPES_LOAD(){
		Utils.LOG_INFO("Loading Recipes for the Various Circuits and Machine components.");
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			run();
		}
		else {
			onlyULVComponents();
			onlyMaxComponents();
		}
		GregtechMachinePhase();
	}

	private static void run(){
		//Electric Motors
		RecipeUtils.addShapedGregtechRecipe(
				cableTier7, wireTier7, rodTier7a,
				wireTier7, rodTier7b, wireTier7,
				rodTier7a, wireTier7, cableTier7,
				RECIPE_CONSTANTS.electricMotor_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier8, wireTier8, rodTier8a,
				wireTier8, rodTier8b, wireTier8,
				rodTier8a, wireTier8, cableTier8,
				RECIPE_CONSTANTS.electricMotor_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier9, wireTier9, rodTier9a,
				wireTier9, rodTier9b, wireTier9,
				rodTier9a, wireTier9, cableTier9,
				RECIPE_CONSTANTS.electricMotor_UV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, wireTier10, rodTier10a,
				wireTier10, rodTier10b, wireTier10,
				rodTier10a, wireTier10, cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX);

		//Electric Pump
		RecipeUtils.addShapedGregtechRecipe(
				cableTier7, rotorTier7, itemRubberRing,
				craftingToolScrewdriver, pipeTier7, craftingToolWrench,
				itemRubberRing, RECIPE_CONSTANTS.electricMotor_LuV, cableTier7,
				RECIPE_CONSTANTS.electricPump_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier8, rotorTier8, itemRubberRing,
				craftingToolScrewdriver, pipeTier8, craftingToolWrench,
				itemRubberRing, RECIPE_CONSTANTS.electricMotor_ZPM, cableTier8,
				RECIPE_CONSTANTS.electricPump_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier9, rotorTier9, itemRubberRing,
				craftingToolScrewdriver, pipeTier9, craftingToolWrench,
				itemRubberRing, RECIPE_CONSTANTS.electricMotor_UV, cableTier9,
				RECIPE_CONSTANTS.electricPump_UV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, rotorTier10, itemRubberRing,
				craftingToolScrewdriver, pipeTier10, craftingToolWrench,
				itemRubberRing, RECIPE_CONSTANTS.electricMotor_MAX, cableTier10,
				RECIPE_CONSTANTS.electricPump_MAX);

		//Electric Pump
		RecipeUtils.addShapedGregtechRecipe(
				plateTier7, plateTier7, plateTier7,
				cableTier7, rodTier7a, rodTier7a,
				cableTier7, RECIPE_CONSTANTS.electricMotor_LuV, smallGearTier7,
				RECIPE_CONSTANTS.electricPiston_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier8, plateTier8, plateTier8,
				cableTier8, rodTier8a, rodTier8a,
				cableTier8, RECIPE_CONSTANTS.electricMotor_ZPM, smallGearTier8,
				RECIPE_CONSTANTS.electricPiston_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier9, plateTier9, plateTier9,
				cableTier9, rodTier9a, rodTier9a,
				cableTier9, RECIPE_CONSTANTS.electricMotor_UV, smallGearTier9,
				RECIPE_CONSTANTS.electricPiston_UV);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier10, plateTier10, plateTier10,
				cableTier10, rodTier10a, rodTier10a,
				cableTier10, RECIPE_CONSTANTS.electricMotor_MAX, smallGearTier10,
				RECIPE_CONSTANTS.electricPiston_MAX);

		//Robot Arms
		RecipeUtils.addShapedGregtechRecipe(
				cableTier7, cableTier7, cableTier7,
				RECIPE_CONSTANTS.electricMotor_LuV, rodTier7a, RECIPE_CONSTANTS.electricMotor_LuV,
				RECIPE_CONSTANTS.electricPiston_LuV, circuitTier7, rodTier7a,
				RECIPE_CONSTANTS.robotArm_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier8, cableTier8, cableTier8,
				RECIPE_CONSTANTS.electricMotor_ZPM, rodTier8a, RECIPE_CONSTANTS.electricMotor_ZPM,
				RECIPE_CONSTANTS.electricPiston_ZPM, circuitTier8, rodTier8a,
				RECIPE_CONSTANTS.robotArm_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier9, cableTier9, cableTier9,
				RECIPE_CONSTANTS.electricMotor_UV, rodTier9a, RECIPE_CONSTANTS.electricMotor_UV,
				RECIPE_CONSTANTS.electricPiston_UV, circuitTier9, rodTier9a,
				RECIPE_CONSTANTS.robotArm_UV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, cableTier10, cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX, rodTier10a, RECIPE_CONSTANTS.electricMotor_MAX,
				RECIPE_CONSTANTS.electricPiston_MAX, circuitTier10, rodTier10a,
				RECIPE_CONSTANTS.robotArm_MAX);

		//Conveyor Modules
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.electricMotor_LuV, cableTier7, RECIPE_CONSTANTS.electricMotor_LuV,
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.conveyorModule_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.electricMotor_ZPM, cableTier8, RECIPE_CONSTANTS.electricMotor_ZPM,
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.conveyorModule_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.electricMotor_UV, cableTier9, RECIPE_CONSTANTS.electricMotor_UV,
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.conveyorModule_UV);
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.electricMotor_MAX, cableTier10, RECIPE_CONSTANTS.electricMotor_MAX,
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.conveyorModule_MAX);

		//Emitter Modules
		RecipeUtils.addShapedGregtechRecipe(
				rodTier7c, rodTier7c, circuitTier7,
				cableTier7, circuitTier6, rodTier7c,
				circuitTier7, cableTier7, rodTier7c,
				RECIPE_CONSTANTS.emitter_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				rodTier8c, rodTier8c, circuitTier8,
				cableTier8, circuitTier7, rodTier8c,
				circuitTier8, cableTier8, rodTier8c,
				RECIPE_CONSTANTS.emitter_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				rodTier9c, rodTier9c, circuitTier9,
				cableTier9, circuitTier8, rodTier9c,
				circuitTier9, cableTier9, rodTier9c,
				RECIPE_CONSTANTS.emitter_UV);
		RecipeUtils.addShapedGregtechRecipe(
				rodTier10c, rodTier10c, circuitTier10,
				cableTier10, circuitTier9, rodTier10c,
				circuitTier10, cableTier10, rodTier10c,
				RECIPE_CONSTANTS.emitter_MAX);

		//Field Generator Modules
		RecipeUtils.addShapedGregtechRecipe(
				wireTier7, circuitTier7, wireTier7,
				circuitTier7, circuitTier6, circuitTier7,
				wireTier7, circuitTier7, wireTier7,
				RECIPE_CONSTANTS.fieldGenerator_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				wireTier8, circuitTier8, wireTier8,
				circuitTier8, circuitTier7, circuitTier8,
				wireTier8, circuitTier8, wireTier8,
				RECIPE_CONSTANTS.fieldGenerator_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				wireTier9, circuitTier9, wireTier9,
				circuitTier9, circuitTier8, circuitTier9,
				wireTier9, circuitTier9, wireTier9,
				RECIPE_CONSTANTS.fieldGenerator_UV);
		RecipeUtils.addShapedGregtechRecipe(
				wireTier10, circuitTier10, wireTier10,
				circuitTier10, circuitTier9, circuitTier10,
				wireTier10, circuitTier10, wireTier10,
				RECIPE_CONSTANTS.fieldGenerator_MAX);

		//Sensor Modules
		RecipeUtils.addShapedGregtechRecipe(
				plateTier7, null, circuitTier6,
				plateTier7, rodTier7c, null,
				circuitTier7, plateTier7, plateTier7,
				RECIPE_CONSTANTS.sensor_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier8, null, circuitTier7,
				plateTier8, rodTier8c, null,
				circuitTier8, plateTier8, plateTier8,
				RECIPE_CONSTANTS.sensor_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier9, null, circuitTier8,
				plateTier9, rodTier9c, null,
				circuitTier9, plateTier9, plateTier9,
				RECIPE_CONSTANTS.sensor_UV);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier10, null, circuitTier9,
				plateTier10, rodTier10c, null,
				circuitTier10, plateTier10, plateTier10,
				RECIPE_CONSTANTS.sensor_MAX);

		onlyULVComponents();
		
		Utils.LOG_INFO("Done loading recipes for the Various machine components.");

	}
	
	private static void onlyULVComponents(){
		RecipeUtils.addShapedGregtechRecipe(
				cableTier1, wireTier0, rodTier0,
				wireTier0, rodTier2, wireTier0,
				rodTier0, wireTier0, cableTier1,
				RECIPE_CONSTANTS.electricMotor_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				cableTier0, rotorTier0, itemRubberRing,
				craftingToolScrewdriver, pipeTier0, craftingToolWrench,
				itemRubberRing, RECIPE_CONSTANTS.electricMotor_ULV, cableTier0,
				RECIPE_CONSTANTS.electricPump_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				plateTier0, plateTier0, plateTier0,
				cableTier0, rodTier1, rodTier1,
				cableTier0, RECIPE_CONSTANTS.electricMotor_ULV, smallGearTier0,
				RECIPE_CONSTANTS.electricPiston_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				cableTier0, cableTier0, cableTier0,
				RECIPE_CONSTANTS.electricMotor_ULV, rodTier1, RECIPE_CONSTANTS.electricMotor_ULV,
				RECIPE_CONSTANTS.electricPiston_ULV, circuitTier0, rodTier1,
				RECIPE_CONSTANTS.robotArm_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.electricMotor_ULV, cableTier0, RECIPE_CONSTANTS.electricMotor_ULV,
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.conveyorModule_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				rodTier2, rodTier2, circuitTier0,
				cableTier0, circuitTier1, rodTier2,
				circuitTier0, cableTier0, rodTier2,
				RECIPE_CONSTANTS.emitter_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				wireTier0, circuitTier0, wireTier0,
				circuitTier0, circuitTier1, circuitTier0,
				wireTier0, circuitTier0, wireTier0,
				RECIPE_CONSTANTS.fieldGenerator_ULV);

		RecipeUtils.recipeBuilder(
				plateTier0, null, circuitTier1,
				plateTier0, rodTier2, null,
				circuitTier0, plateTier0, plateTier0,
				RECIPE_CONSTANTS.sensor_ULV);
	}

	private static void onlyMaxComponents(){
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, wireTier10, rodTier10a,
				wireTier10, rodTier10b, wireTier10,
				rodTier10a, wireTier10, cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, rotorTier10, itemRubberRing,
				craftingToolScrewdriver, pipeTier10, craftingToolWrench,
				itemRubberRing, RECIPE_CONSTANTS.electricMotor_MAX, cableTier10,
				RECIPE_CONSTANTS.electricPump_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier10, plateTier10, plateTier10,
				cableTier10, rodTier10a, rodTier10a,
				cableTier10, RECIPE_CONSTANTS.electricMotor_MAX, smallGearTier10,
				RECIPE_CONSTANTS.electricPiston_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, cableTier10, cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX, rodTier10a, RECIPE_CONSTANTS.electricMotor_MAX,
				RECIPE_CONSTANTS.electricPiston_MAX, circuitTier10, rodTier10a,
				RECIPE_CONSTANTS.robotArm_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.electricMotor_MAX, cableTier10, RECIPE_CONSTANTS.electricMotor_MAX,
				plateRubber, plateRubber, plateRubber,
				RECIPE_CONSTANTS.conveyorModule_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				rodTier10c, rodTier10c, circuitTier10,
				cableTier10, circuitTier9, rodTier10c,
				circuitTier10, cableTier10, rodTier10c,
				RECIPE_CONSTANTS.emitter_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				wireTier10, circuitTier10, wireTier10,
				circuitTier10, circuitTier9, circuitTier10,
				wireTier10, circuitTier10, wireTier10,
				RECIPE_CONSTANTS.fieldGenerator_MAX);
		RecipeUtils.recipeBuilder(
				plateTier10, null, circuitTier9,
				plateTier10, rodTier10c, null,
				circuitTier10, plateTier10, plateTier10,
				RECIPE_CONSTANTS.sensor_MAX);
	}

	private static void GregtechMachinePhase(){
		Utils.LOG_INFO("Adding Gregtech machine recipes for the circuits.");
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GregtechItemList.Circuit_Parts_Wiring_IV.get(4L, new Object[0]), GregtechItemList.Circuit_Board_IV.get(1L, new Object[0]), 32, 256);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GregtechItemList.Circuit_Parts_Wiring_LuV.get(4L, new Object[0]), GregtechItemList.Circuit_Board_LuV.get(1L, new Object[0]), 64, 512);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 2L), GregtechItemList.Circuit_Parts_Wiring_ZPM.get(4L, new Object[0]), GregtechItemList.Circuit_Board_ZPM.get(1L, new Object[0]), 96, 1024);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GregtechItemList.Circuit_Parts_Crystal_Chip_IV.get(3L, new Object[0]), GregtechItemList.Circuit_Parts_IV.get(1L, new Object[0]), 32, 512);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.get(3L, new Object[0]), GregtechItemList.Circuit_Parts_LuV.get(1L, new Object[0]), 64, 1024);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.get(3L, new Object[0]), GregtechItemList.Circuit_Parts_ZPM.get(1L, new Object[0]), 96, 2048);
		GT_Values.RA.addForgeHammerRecipe(ItemList.Circuit_Master.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_Crystal_Chip_IV.get(5L, new Object[0]), 32, 256);
		GT_Values.RA.addForgeHammerRecipe(GregtechItemList.Circuit_IV.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.get(5L, new Object[0]), 64, 512);
		GT_Values.RA.addForgeHammerRecipe(GregtechItemList.Circuit_LuV.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.get(5L, new Object[0]), 128, 1024);
		for (final Materials tMat : Materials.values()) {
			if ((tMat.mStandardMoltenFluid != null) && (tMat.contains(SubTag.SOLDERING_MATERIAL))) {
				final int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1 : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_IV.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_IV.get(1L, new Object[0]), tMat.getMolten((144L * tMultiplier) / 4L), GregtechItemList.Circuit_IV.get(1L, new Object[0]), 32, 512);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_LuV.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_LuV.get(1L, new Object[0]), tMat.getMolten((144L * tMultiplier) / 4L), GregtechItemList.Circuit_LuV.get(1L, new Object[0]), 64, 1024);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_ZPM.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_ZPM.get(1L, new Object[0]), tMat.getMolten((144L * tMultiplier) / 4L), GregtechItemList.Circuit_ZPM.get(1L, new Object[0]), 96, 2048);

			}
		}
	}
}
