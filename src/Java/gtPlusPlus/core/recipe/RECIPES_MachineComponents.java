package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RECIPES_MachineComponents {

	//Wire
	public static String wireTier0 = "wireGt01Lead";
	public static String wireTier1 = "wireGt01RedAlloy";
	public static String wireTier6 = "wireGt08NiobiumTitanium";
	public static String wireTier7 = "wireGt08Osmium";
	public static String wireTier8 = "wireGt08Naquadah";
	public static String wireTier9 = "wireGt08Superconductor";
	public static String wireTier10 = "wireGt16Superconductor";

	//Cable
	public static String cableTier0 = "cableGt01Lead";
	public static String cableTier1 = "cableGt01RedAlloy";
	public static String cableTier6 = "cableGt04Tungsten";
	public static String cableTier7 = "cableGt04NiobiumTitanium";
	public static String cableTier8 = "cableGt04Osmium";
	public static String cableTier9 = "cableGt04Naquadah";
	public static String cableTier10 = "wireGt08Superconductor";

	//Plates
	public static String plateTier0 = "plateWroughtIron";
	public static String plateTier7 = "plateChrome";
	public static String plateTier8 = "plateIridium";
	public static String plateTier9 = "plateOsmium";
	public static String plateTier10 = "plateNeutronium";

	//rods
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

	//Screws
	public static String screwTier0 = "screwPotin";
	public static String screwTier7 = "screwChrome";
	public static String screwTier8 = "screwIridium";
	public static String screwTier9 = "screwOsmium";
	public static String screwTier10 = "screwNeutronium";

	//Rotors
	public static String rotorTier0 = "rotorPotin";
	public static String rotorTier7 = "rotorChrome";
	public static String rotorTier8 = "rotorIridium";
	public static String rotorTier9 = "rotorOsmium";
	public static String rotorTier10 = "rotorNeutronium";

	//Fluid Pipe
	public static String pipeTier0 = "pipeSmallLead";
	public static String pipeTier7 = "pipeHugeSteel";
	public static String pipeTier8 = "pipeHugeStainlessSteel";
	public static String pipeTier9 = "pipeHugeTitanium";
	public static String pipeTier10 = "pipeHugeTungstenSteel";

	//Rubber Ring/Plate
	public static String itemRubberRing = "ringRubber";
	public static String plateRubber = "plateRubber";

	//Circuits
	public static String circuitTier0 = "circuitPrimitive";
	public static String circuitTier1 = "circuitBasic";
	public static String circuitTier6 = "circuitMaster";
	public static String circuitTier7 = "circuitUltimate";
	public static String circuitTier8 = "circuitSuperconductor";
	public static String circuitTier9 = "circuitInfinite";
	public static String circuitTier10 = "circuitQuantum";

	//small gears
	public static String smallGearTier0 = "gearGtSmallWroughtIron";
	public static String smallGearTier7 = "gearGtSmallChrome";
	public static String smallGearTier8 = "gearGtSmallIridium";
	public static String smallGearTier9 = "gearGtSmallOsmium";
	public static String smallGearTier10 = "gearGtSmallNeutronium";

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
				CI.electricMotor_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier8, wireTier8, rodTier8a,
				wireTier8, rodTier8b, wireTier8,
				rodTier8a, wireTier8, cableTier8,
				CI.electricMotor_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier9, wireTier9, rodTier9a,
				wireTier9, rodTier9b, wireTier9,
				rodTier9a, wireTier9, cableTier9,
				CI.electricMotor_UV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, wireTier10, rodTier10a,
				wireTier10, rodTier10b, wireTier10,
				rodTier10a, wireTier10, cableTier10,
				CI.electricMotor_MAX);

		//Electric Pump
		RecipeUtils.addShapedGregtechRecipe(
				cableTier7, rotorTier7, itemRubberRing,
				CI.craftingToolScrewdriver, pipeTier7, CI.craftingToolWrench,
				itemRubberRing, CI.electricMotor_LuV, cableTier7,
				CI.electricPump_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier8, rotorTier8, itemRubberRing,
				CI.craftingToolScrewdriver, pipeTier8, CI.craftingToolWrench,
				itemRubberRing, CI.electricMotor_ZPM, cableTier8,
				CI.electricPump_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier9, rotorTier9, itemRubberRing,
				CI.craftingToolScrewdriver, pipeTier9, CI.craftingToolWrench,
				itemRubberRing, CI.electricMotor_UV, cableTier9,
				CI.electricPump_UV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, rotorTier10, itemRubberRing,
				CI.craftingToolScrewdriver, pipeTier10, CI.craftingToolWrench,
				itemRubberRing, CI.electricMotor_MAX, cableTier10,
				CI.electricPump_MAX);

		//Electric Pump
		RecipeUtils.addShapedGregtechRecipe(
				plateTier7, plateTier7, plateTier7,
				cableTier7, rodTier7a, rodTier7a,
				cableTier7, CI.electricMotor_LuV, smallGearTier7,
				CI.electricPiston_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier8, plateTier8, plateTier8,
				cableTier8, rodTier8a, rodTier8a,
				cableTier8, CI.electricMotor_ZPM, smallGearTier8,
				CI.electricPiston_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier9, plateTier9, plateTier9,
				cableTier9, rodTier9a, rodTier9a,
				cableTier9, CI.electricMotor_UV, smallGearTier9,
				CI.electricPiston_UV);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier10, plateTier10, plateTier10,
				cableTier10, rodTier10a, rodTier10a,
				cableTier10, CI.electricMotor_MAX, smallGearTier10,
				CI.electricPiston_MAX);

		//Robot Arms
		RecipeUtils.addShapedGregtechRecipe(
				cableTier7, cableTier7, cableTier7,
				CI.electricMotor_LuV, rodTier7a, CI.electricMotor_LuV,
				CI.electricPiston_LuV, circuitTier7, rodTier7a,
				CI.robotArm_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier8, cableTier8, cableTier8,
				CI.electricMotor_ZPM, rodTier8a, CI.electricMotor_ZPM,
				CI.electricPiston_ZPM, circuitTier8, rodTier8a,
				CI.robotArm_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier9, cableTier9, cableTier9,
				CI.electricMotor_UV, rodTier9a, CI.electricMotor_UV,
				CI.electricPiston_UV, circuitTier9, rodTier9a,
				CI.robotArm_UV);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, cableTier10, cableTier10,
				CI.electricMotor_MAX, rodTier10a, CI.electricMotor_MAX,
				CI.electricPiston_MAX, circuitTier10, rodTier10a,
				CI.robotArm_MAX);

		//Conveyor Modules
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				CI.electricMotor_LuV, cableTier7, CI.electricMotor_LuV,
				plateRubber, plateRubber, plateRubber,
				CI.conveyorModule_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				CI.electricMotor_ZPM, cableTier8, CI.electricMotor_ZPM,
				plateRubber, plateRubber, plateRubber,
				CI.conveyorModule_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				CI.electricMotor_UV, cableTier9, CI.electricMotor_UV,
				plateRubber, plateRubber, plateRubber,
				CI.conveyorModule_UV);
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				CI.electricMotor_MAX, cableTier10, CI.electricMotor_MAX,
				plateRubber, plateRubber, plateRubber,
				CI.conveyorModule_MAX);

		//Emitter Modules
		RecipeUtils.addShapedGregtechRecipe(
				rodTier7c, rodTier7c, circuitTier7,
				cableTier7, circuitTier6, rodTier7c,
				circuitTier7, cableTier7, rodTier7c,
				CI.emitter_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				rodTier8c, rodTier8c, circuitTier8,
				cableTier8, circuitTier7, rodTier8c,
				circuitTier8, cableTier8, rodTier8c,
				CI.emitter_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				rodTier9c, rodTier9c, circuitTier9,
				cableTier9, circuitTier8, rodTier9c,
				circuitTier9, cableTier9, rodTier9c,
				CI.emitter_UV);
		RecipeUtils.addShapedGregtechRecipe(
				rodTier10c, rodTier10c, circuitTier10,
				cableTier10, circuitTier9, rodTier10c,
				circuitTier10, cableTier10, rodTier10c,
				CI.emitter_MAX);

		//Field Generator Modules
		RecipeUtils.addShapedGregtechRecipe(
				wireTier7, circuitTier7, wireTier7,
				circuitTier7, circuitTier6, circuitTier7,
				wireTier7, circuitTier7, wireTier7,
				CI.fieldGenerator_LuV);
		RecipeUtils.addShapedGregtechRecipe(
				wireTier8, circuitTier8, wireTier8,
				circuitTier8, circuitTier7, circuitTier8,
				wireTier8, circuitTier8, wireTier8,
				CI.fieldGenerator_ZPM);
		RecipeUtils.addShapedGregtechRecipe(
				wireTier9, circuitTier9, wireTier9,
				circuitTier9, circuitTier8, circuitTier9,
				wireTier9, circuitTier9, wireTier9,
				CI.fieldGenerator_UV);
		RecipeUtils.addShapedGregtechRecipe(
				wireTier10, circuitTier10, wireTier10,
				circuitTier10, circuitTier9, circuitTier10,
				wireTier10, circuitTier10, wireTier10,
				CI.fieldGenerator_MAX);

		//Sensor Modules
		RecipeUtils.recipeBuilder(
				plateTier7, null, circuitTier6,
				plateTier7, rodTier7c, null,
				circuitTier7, plateTier7, plateTier7,
				CI.sensor_LuV);
		RecipeUtils.recipeBuilder(
				plateTier8, null, circuitTier7,
				plateTier8, rodTier8c, null,
				circuitTier8, plateTier8, plateTier8,
				CI.sensor_ZPM);
		RecipeUtils.recipeBuilder(
				plateTier9, null, circuitTier8,
				plateTier9, rodTier9c, null,
				circuitTier9, plateTier9, plateTier9,
				CI.sensor_UV);
		RecipeUtils.recipeBuilder(
				plateTier10, null, circuitTier9,
				plateTier10, rodTier10c, null,
				circuitTier10, plateTier10, plateTier10,
				CI.sensor_MAX);

		onlyULVComponents();

		Utils.LOG_INFO("Done loading recipes for the Various machine components.");

	}

	private static void onlyULVComponents(){
		RecipeUtils.addShapedGregtechRecipe(
				cableTier1, wireTier0, rodTier0,
				wireTier0, rodTier2, wireTier0,
				rodTier0, wireTier0, cableTier1,
				CI.electricMotor_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				cableTier0, rotorTier0, itemRubberRing,
				CI.craftingToolScrewdriver, pipeTier0, CI.craftingToolWrench,
				itemRubberRing, CI.electricMotor_ULV, cableTier0,
				CI.electricPump_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				plateTier0, plateTier0, plateTier0,
				cableTier0, rodTier1, rodTier1,
				cableTier0, CI.electricMotor_ULV, smallGearTier0,
				CI.electricPiston_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				cableTier0, cableTier0, cableTier0,
				CI.electricMotor_ULV, rodTier1, CI.electricMotor_ULV,
				CI.electricPiston_ULV, circuitTier0, rodTier1,
				CI.robotArm_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				CI.electricMotor_ULV, cableTier0, CI.electricMotor_ULV,
				plateRubber, plateRubber, plateRubber,
				CI.conveyorModule_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				rodTier2, rodTier2, circuitTier0,
				cableTier0, circuitTier1, rodTier2,
				circuitTier0, cableTier0, rodTier2,
				CI.emitter_ULV);

		RecipeUtils.addShapedGregtechRecipe(
				wireTier0, circuitTier0, wireTier0,
				circuitTier0, circuitTier1, circuitTier0,
				wireTier0, circuitTier0, wireTier0,
				CI.fieldGenerator_ULV);

		RecipeUtils.recipeBuilder(
				plateTier0, null, circuitTier1,
				plateTier0, rodTier2, null,
				circuitTier0, plateTier0, plateTier0,
				CI.sensor_ULV);
	}

	private static void onlyMaxComponents(){
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, wireTier10, rodTier10a,
				wireTier10, rodTier10b, wireTier10,
				rodTier10a, wireTier10, cableTier10,
				CI.electricMotor_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, rotorTier10, itemRubberRing,
				CI.craftingToolScrewdriver, pipeTier10, CI.craftingToolWrench,
				itemRubberRing, CI.electricMotor_MAX, cableTier10,
				CI.electricPump_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				plateTier10, plateTier10, plateTier10,
				cableTier10, rodTier10a, rodTier10a,
				cableTier10, CI.electricMotor_MAX, smallGearTier10,
				CI.electricPiston_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				cableTier10, cableTier10, cableTier10,
				CI.electricMotor_MAX, rodTier10a, CI.electricMotor_MAX,
				CI.electricPiston_MAX, circuitTier10, rodTier10a,
				CI.robotArm_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				plateRubber, plateRubber, plateRubber,
				CI.electricMotor_MAX, cableTier10, CI.electricMotor_MAX,
				plateRubber, plateRubber, plateRubber,
				CI.conveyorModule_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				rodTier10c, rodTier10c, circuitTier10,
				cableTier10, circuitTier9, rodTier10c,
				circuitTier10, cableTier10, rodTier10c,
				CI.emitter_MAX);
		RecipeUtils.addShapedGregtechRecipe(
				wireTier10, circuitTier10, wireTier10,
				circuitTier10, circuitTier9, circuitTier10,
				wireTier10, circuitTier10, wireTier10,
				CI.fieldGenerator_MAX);
		RecipeUtils.recipeBuilder(
				plateTier10, null, circuitTier9,
				plateTier10, rodTier10c, null,
				circuitTier10, plateTier10, plateTier10,
				CI.sensor_MAX);
	}

	private static void GregtechMachinePhase(){
		if (CORE.configSwitches.enableCustomCircuits && !CORE.GTNH){
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
}
