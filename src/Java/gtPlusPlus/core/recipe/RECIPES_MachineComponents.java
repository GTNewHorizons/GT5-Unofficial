package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RECIPES_MachineComponents {

	// Wire
	static String	wireTier6				= "wireGt08NiobiumTitanium";
	static String	wireTier7				= "wireGt08Osmium";
	static String	wireTier8				= "wireGt08Naquadah";
	static String	wireTier9				= "wireGt08Superconductor";
	static String	wireTier10				= "wireGt16Superconductor";

	// Wire
	static String	cableTier6				= "cableGt04Tungsten";
	static String	cableTier7				= "cableGt04NiobiumTitanium";
	static String	cableTier8				= "cableGt04Osmium";
	static String	cableTier9				= "cableGt04Naquadah";
	static String	cableTier10				= "wireGt08Superconductor";

	// Plates
	static String	plateTier7				= "plateChrome";
	static String	plateTier8				= "plateIridium";
	static String	plateTier9				= "plateOsmium";
	static String	plateTier10				= "plateNeutronium";

	// rods
	static String	rodTier7a				= "stickChrome";
	static String	rodTier8a				= "stickIridium";
	static String	rodTier9a				= "stickOsmium";
	static String	rodTier10a				= "stickNeutronium";
	static String	rodTier7b				= "stickPlatinum";
	static String	rodTier8b				= "stickChrome";
	static String	rodTier9b				= "stickIridium";
	static String	rodTier10b				= "stickOsmium";
	static String	rodTier7c				= "stickTitanium";
	static String	rodTier8c				= "stickTungstenSteel";
	static String	rodTier9c				= "stickNaquadah";
	static String	rodTier10c				= "stickOsmium";

	// Screws
	static String	screwTier7				= "screwChrome";
	static String	screwTier8				= "screwIridium";
	static String	screwTier9				= "screwOsmium";
	static String	screwTier10				= "screwNeutronium";

	// Rotors
	static String	rotorTier7				= "rotorChrome";
	static String	rotorTier8				= "rotorIridium";
	static String	rotorTier9				= "rotorOsmium";
	static String	rotorTier10				= "rotorNeutronium";

	// Fluid Pipe
	static String	pipeTier7				= "pipeHugeSteel";
	static String	pipeTier8				= "pipeHugeStainlessSteel";
	static String	pipeTier9				= "pipeHugeTitanium";
	static String	pipeTier10				= "pipeHugeTungstenSteel";

	// Rubber Ring/Plate
	static String	itemRubberRing			= "ringRubber";
	static String	plateRubber				= "plateRubber";

	// Circuits
	static String	circuitTier6			= "circuitMaster";
	static String	circuitTier7			= "circuitUltimate";
	static String	circuitTier8			= "circuitSymbiotic";
	static String	circuitTier9			= "circuitNeutronic";
	static String	circuitTier10			= "circuitQuantum";

	// small gears
	static String	smallGearTier7			= "gearGtSmallChrome";
	static String	smallGearTier8			= "gearGtSmallIridium";
	static String	smallGearTier9			= "gearGtSmallOsmium";
	static String	smallGearTier10			= "gearGtSmallNeutronium";

	// Crafting Tools
	static String	craftingToolWrench		= "craftingToolWrench";
	static String	craftingToolScrewdriver	= "craftingToolScrewdriver";

	private static void GregtechMachinePhase() {
		Utils.LOG_INFO("Adding Gregtech machine recipes for the circuits.");
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L),
				GregtechItemList.Circuit_Parts_Wiring_IV.get(4L, new Object[0]),
				GregtechItemList.Circuit_Board_IV.get(1L, new Object[0]), 32, 256);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L),
				GregtechItemList.Circuit_Parts_Wiring_LuV.get(4L, new Object[0]),
				GregtechItemList.Circuit_Board_LuV.get(1L, new Object[0]), 64, 512);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 2L),
				GregtechItemList.Circuit_Parts_Wiring_ZPM.get(4L, new Object[0]),
				GregtechItemList.Circuit_Board_ZPM.get(1L, new Object[0]), 96, 1024);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L),
				GregtechItemList.Circuit_Parts_Crystal_Chip_IV.get(3L, new Object[0]),
				GregtechItemList.Circuit_Parts_IV.get(1L, new Object[0]), 32, 512);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L),
				GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.get(3L, new Object[0]),
				GregtechItemList.Circuit_Parts_LuV.get(1L, new Object[0]), 64, 1024);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L),
				GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.get(3L, new Object[0]),
				GregtechItemList.Circuit_Parts_ZPM.get(1L, new Object[0]), 96, 2048);
		GT_Values.RA.addForgeHammerRecipe(ItemList.Circuit_Master.get(1L, new Object[0]),
				GregtechItemList.Circuit_Parts_Crystal_Chip_IV.get(5L, new Object[0]), 32, 256);
		GT_Values.RA.addForgeHammerRecipe(GregtechItemList.Circuit_IV.get(1L, new Object[0]),
				GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.get(5L, new Object[0]), 64, 512);
		GT_Values.RA.addForgeHammerRecipe(GregtechItemList.Circuit_LuV.get(1L, new Object[0]),
				GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.get(5L, new Object[0]), 128, 1024);
		for (final Materials tMat : Materials.values()) {
			if (tMat.mStandardMoltenFluid != null && tMat.contains(SubTag.SOLDERING_MATERIAL)) {
				final int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
						: tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_IV.get(1L, new Object[0]),
						GregtechItemList.Circuit_Parts_IV.get(1L, new Object[0]),
						tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Circuit_IV.get(1L, new Object[0]), 32,
						512);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_LuV.get(1L, new Object[0]),
						GregtechItemList.Circuit_Parts_LuV.get(1L, new Object[0]),
						tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Circuit_LuV.get(1L, new Object[0]),
						64, 1024);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_ZPM.get(1L, new Object[0]),
						GregtechItemList.Circuit_Parts_ZPM.get(1L, new Object[0]),
						tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Circuit_ZPM.get(1L, new Object[0]),
						96, 2048);

			}
		}
	}

	private static void onlyMaxComponents() {
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier10, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.rodTier10a, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.rodTier10b, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.rodTier10a, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.cableTier10, RECIPE_CONSTANTS.electricMotor_MAX);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier10,
				RECIPES_MachineComponents.rotorTier10, RECIPES_MachineComponents.itemRubberRing,
				RECIPES_MachineComponents.craftingToolScrewdriver, RECIPES_MachineComponents.pipeTier10,
				RECIPES_MachineComponents.craftingToolWrench, RECIPES_MachineComponents.itemRubberRing,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.cableTier10,
				RECIPE_CONSTANTS.electricPump_MAX);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier10,
				RECIPES_MachineComponents.plateTier10, RECIPES_MachineComponents.plateTier10,
				RECIPES_MachineComponents.cableTier10, RECIPES_MachineComponents.rodTier10a,
				RECIPES_MachineComponents.rodTier10a, RECIPES_MachineComponents.cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.smallGearTier10,
				RECIPE_CONSTANTS.electricPiston_MAX);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier10,
				RECIPES_MachineComponents.cableTier10, RECIPES_MachineComponents.cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.rodTier10a,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPE_CONSTANTS.electricPiston_MAX,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.rodTier10a,
				RECIPE_CONSTANTS.robotArm_MAX);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.conveyorModule_MAX);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.rodTier10c, RECIPES_MachineComponents.rodTier10c,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.cableTier10,
				RECIPES_MachineComponents.circuitTier9, RECIPES_MachineComponents.rodTier10c,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.cableTier10,
				RECIPES_MachineComponents.rodTier10c, RECIPE_CONSTANTS.emitter_MAX);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.circuitTier9,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.wireTier10,
				RECIPE_CONSTANTS.fieldGenerator_MAX);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier10, null,
				RECIPES_MachineComponents.circuitTier9, RECIPES_MachineComponents.plateTier10,
				RECIPES_MachineComponents.rodTier10c, null, RECIPES_MachineComponents.circuitTier10,
				RECIPES_MachineComponents.plateTier10, RECIPES_MachineComponents.plateTier10,
				RECIPE_CONSTANTS.sensor_MAX);
	}

	public static final void RECIPES_LOAD() {
		Utils.LOG_INFO("Loading Recipes for the Various Circuits and Machine components.");
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			RECIPES_MachineComponents.run();
		}
		else {
			RECIPES_MachineComponents.onlyMaxComponents();
		}
		RECIPES_MachineComponents.GregtechMachinePhase();
	}

	private static void run() {
		// Electric Motors
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier7, RECIPES_MachineComponents.wireTier7,
				RECIPES_MachineComponents.rodTier7a, RECIPES_MachineComponents.wireTier7,
				RECIPES_MachineComponents.rodTier7b, RECIPES_MachineComponents.wireTier7,
				RECIPES_MachineComponents.rodTier7a, RECIPES_MachineComponents.wireTier7,
				RECIPES_MachineComponents.cableTier7, RECIPE_CONSTANTS.electricMotor_LuV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier8, RECIPES_MachineComponents.wireTier8,
				RECIPES_MachineComponents.rodTier8a, RECIPES_MachineComponents.wireTier8,
				RECIPES_MachineComponents.rodTier8b, RECIPES_MachineComponents.wireTier8,
				RECIPES_MachineComponents.rodTier8a, RECIPES_MachineComponents.wireTier8,
				RECIPES_MachineComponents.cableTier8, RECIPE_CONSTANTS.electricMotor_ZPM);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier9, RECIPES_MachineComponents.wireTier9,
				RECIPES_MachineComponents.rodTier9a, RECIPES_MachineComponents.wireTier9,
				RECIPES_MachineComponents.rodTier9b, RECIPES_MachineComponents.wireTier9,
				RECIPES_MachineComponents.rodTier9a, RECIPES_MachineComponents.wireTier9,
				RECIPES_MachineComponents.cableTier9, RECIPE_CONSTANTS.electricMotor_UV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier10, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.rodTier10a, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.rodTier10b, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.rodTier10a, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.cableTier10, RECIPE_CONSTANTS.electricMotor_MAX);

		// Electric Pump
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier7, RECIPES_MachineComponents.rotorTier7,
				RECIPES_MachineComponents.itemRubberRing, RECIPES_MachineComponents.craftingToolScrewdriver,
				RECIPES_MachineComponents.pipeTier7, RECIPES_MachineComponents.craftingToolWrench,
				RECIPES_MachineComponents.itemRubberRing, RECIPE_CONSTANTS.electricMotor_LuV,
				RECIPES_MachineComponents.cableTier7, RECIPE_CONSTANTS.electricPump_LuV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier8, RECIPES_MachineComponents.rotorTier8,
				RECIPES_MachineComponents.itemRubberRing, RECIPES_MachineComponents.craftingToolScrewdriver,
				RECIPES_MachineComponents.pipeTier8, RECIPES_MachineComponents.craftingToolWrench,
				RECIPES_MachineComponents.itemRubberRing, RECIPE_CONSTANTS.electricMotor_ZPM,
				RECIPES_MachineComponents.cableTier8, RECIPE_CONSTANTS.electricPump_ZPM);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier9, RECIPES_MachineComponents.rotorTier9,
				RECIPES_MachineComponents.itemRubberRing, RECIPES_MachineComponents.craftingToolScrewdriver,
				RECIPES_MachineComponents.pipeTier9, RECIPES_MachineComponents.craftingToolWrench,
				RECIPES_MachineComponents.itemRubberRing, RECIPE_CONSTANTS.electricMotor_UV,
				RECIPES_MachineComponents.cableTier9, RECIPE_CONSTANTS.electricPump_UV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier10,
				RECIPES_MachineComponents.rotorTier10, RECIPES_MachineComponents.itemRubberRing,
				RECIPES_MachineComponents.craftingToolScrewdriver, RECIPES_MachineComponents.pipeTier10,
				RECIPES_MachineComponents.craftingToolWrench, RECIPES_MachineComponents.itemRubberRing,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.cableTier10,
				RECIPE_CONSTANTS.electricPump_MAX);

		// Electric Pump
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier7, RECIPES_MachineComponents.plateTier7,
				RECIPES_MachineComponents.plateTier7, RECIPES_MachineComponents.cableTier7,
				RECIPES_MachineComponents.rodTier7a, RECIPES_MachineComponents.rodTier7a,
				RECIPES_MachineComponents.cableTier7, RECIPE_CONSTANTS.electricMotor_LuV,
				RECIPES_MachineComponents.smallGearTier7, RECIPE_CONSTANTS.electricPiston_LuV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier8, RECIPES_MachineComponents.plateTier8,
				RECIPES_MachineComponents.plateTier8, RECIPES_MachineComponents.cableTier8,
				RECIPES_MachineComponents.rodTier8a, RECIPES_MachineComponents.rodTier8a,
				RECIPES_MachineComponents.cableTier8, RECIPE_CONSTANTS.electricMotor_ZPM,
				RECIPES_MachineComponents.smallGearTier8, RECIPE_CONSTANTS.electricPiston_ZPM);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier9, RECIPES_MachineComponents.plateTier9,
				RECIPES_MachineComponents.plateTier9, RECIPES_MachineComponents.cableTier9,
				RECIPES_MachineComponents.rodTier9a, RECIPES_MachineComponents.rodTier9a,
				RECIPES_MachineComponents.cableTier9, RECIPE_CONSTANTS.electricMotor_UV,
				RECIPES_MachineComponents.smallGearTier9, RECIPE_CONSTANTS.electricPiston_UV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier10,
				RECIPES_MachineComponents.plateTier10, RECIPES_MachineComponents.plateTier10,
				RECIPES_MachineComponents.cableTier10, RECIPES_MachineComponents.rodTier10a,
				RECIPES_MachineComponents.rodTier10a, RECIPES_MachineComponents.cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.smallGearTier10,
				RECIPE_CONSTANTS.electricPiston_MAX);

		// Robot Arms
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier7, RECIPES_MachineComponents.cableTier7,
				RECIPES_MachineComponents.cableTier7, RECIPE_CONSTANTS.electricMotor_LuV,
				RECIPES_MachineComponents.rodTier7a, RECIPE_CONSTANTS.electricMotor_LuV,
				RECIPE_CONSTANTS.electricPiston_LuV, RECIPES_MachineComponents.circuitTier7,
				RECIPES_MachineComponents.rodTier7a, RECIPE_CONSTANTS.robotArm_LuV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier8, RECIPES_MachineComponents.cableTier8,
				RECIPES_MachineComponents.cableTier8, RECIPE_CONSTANTS.electricMotor_ZPM,
				RECIPES_MachineComponents.rodTier8a, RECIPE_CONSTANTS.electricMotor_ZPM,
				RECIPE_CONSTANTS.electricPiston_ZPM, RECIPES_MachineComponents.circuitTier8,
				RECIPES_MachineComponents.rodTier8a, RECIPE_CONSTANTS.robotArm_ZPM);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier9, RECIPES_MachineComponents.cableTier9,
				RECIPES_MachineComponents.cableTier9, RECIPE_CONSTANTS.electricMotor_UV,
				RECIPES_MachineComponents.rodTier9a, RECIPE_CONSTANTS.electricMotor_UV,
				RECIPE_CONSTANTS.electricPiston_UV, RECIPES_MachineComponents.circuitTier9,
				RECIPES_MachineComponents.rodTier9a, RECIPE_CONSTANTS.robotArm_UV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.cableTier10,
				RECIPES_MachineComponents.cableTier10, RECIPES_MachineComponents.cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.rodTier10a,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPE_CONSTANTS.electricPiston_MAX,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.rodTier10a,
				RECIPE_CONSTANTS.robotArm_MAX);

		// Conveyor Modules
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.electricMotor_LuV, RECIPES_MachineComponents.cableTier7,
				RECIPE_CONSTANTS.electricMotor_LuV, RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.conveyorModule_LuV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.electricMotor_ZPM, RECIPES_MachineComponents.cableTier8,
				RECIPE_CONSTANTS.electricMotor_ZPM, RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.conveyorModule_ZPM);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.electricMotor_UV, RECIPES_MachineComponents.cableTier9,
				RECIPE_CONSTANTS.electricMotor_UV, RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.conveyorModule_UV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.cableTier10,
				RECIPE_CONSTANTS.electricMotor_MAX, RECIPES_MachineComponents.plateRubber,
				RECIPES_MachineComponents.plateRubber, RECIPES_MachineComponents.plateRubber,
				RECIPE_CONSTANTS.conveyorModule_MAX);

		// Emitter Modules
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.rodTier7c, RECIPES_MachineComponents.rodTier7c,
				RECIPES_MachineComponents.circuitTier7, RECIPES_MachineComponents.cableTier7,
				RECIPES_MachineComponents.circuitTier6, RECIPES_MachineComponents.rodTier7c,
				RECIPES_MachineComponents.circuitTier7, RECIPES_MachineComponents.cableTier7,
				RECIPES_MachineComponents.rodTier7c, RECIPE_CONSTANTS.emitter_LuV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.rodTier8c, RECIPES_MachineComponents.rodTier8c,
				RECIPES_MachineComponents.circuitTier8, RECIPES_MachineComponents.cableTier8,
				RECIPES_MachineComponents.circuitTier7, RECIPES_MachineComponents.rodTier8c,
				RECIPES_MachineComponents.circuitTier8, RECIPES_MachineComponents.cableTier8,
				RECIPES_MachineComponents.rodTier8c, RECIPE_CONSTANTS.emitter_ZPM);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.rodTier9c, RECIPES_MachineComponents.rodTier9c,
				RECIPES_MachineComponents.circuitTier9, RECIPES_MachineComponents.cableTier9,
				RECIPES_MachineComponents.circuitTier8, RECIPES_MachineComponents.rodTier9c,
				RECIPES_MachineComponents.circuitTier9, RECIPES_MachineComponents.cableTier9,
				RECIPES_MachineComponents.rodTier9c, RECIPE_CONSTANTS.emitter_UV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.rodTier10c, RECIPES_MachineComponents.rodTier10c,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.cableTier10,
				RECIPES_MachineComponents.circuitTier9, RECIPES_MachineComponents.rodTier10c,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.cableTier10,
				RECIPES_MachineComponents.rodTier10c, RECIPE_CONSTANTS.emitter_MAX);

		// Field Generator Modules
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.wireTier7, RECIPES_MachineComponents.circuitTier7,
				RECIPES_MachineComponents.wireTier7, RECIPES_MachineComponents.circuitTier7,
				RECIPES_MachineComponents.circuitTier6, RECIPES_MachineComponents.circuitTier7,
				RECIPES_MachineComponents.wireTier7, RECIPES_MachineComponents.circuitTier7,
				RECIPES_MachineComponents.wireTier7, RECIPE_CONSTANTS.fieldGenerator_LuV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.wireTier8, RECIPES_MachineComponents.circuitTier8,
				RECIPES_MachineComponents.wireTier8, RECIPES_MachineComponents.circuitTier8,
				RECIPES_MachineComponents.circuitTier7, RECIPES_MachineComponents.circuitTier8,
				RECIPES_MachineComponents.wireTier8, RECIPES_MachineComponents.circuitTier8,
				RECIPES_MachineComponents.wireTier8, RECIPE_CONSTANTS.fieldGenerator_ZPM);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.wireTier9, RECIPES_MachineComponents.circuitTier9,
				RECIPES_MachineComponents.wireTier9, RECIPES_MachineComponents.circuitTier9,
				RECIPES_MachineComponents.circuitTier8, RECIPES_MachineComponents.circuitTier9,
				RECIPES_MachineComponents.wireTier9, RECIPES_MachineComponents.circuitTier9,
				RECIPES_MachineComponents.wireTier9, RECIPE_CONSTANTS.fieldGenerator_UV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.circuitTier9,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.wireTier10,
				RECIPES_MachineComponents.circuitTier10, RECIPES_MachineComponents.wireTier10,
				RECIPE_CONSTANTS.fieldGenerator_MAX);

		// Sensor Modules
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier7, null,
				RECIPES_MachineComponents.circuitTier6, RECIPES_MachineComponents.plateTier7,
				RECIPES_MachineComponents.rodTier7c, null, RECIPES_MachineComponents.circuitTier7,
				RECIPES_MachineComponents.plateTier7, RECIPES_MachineComponents.plateTier7,
				RECIPE_CONSTANTS.sensor_LuV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier8, null,
				RECIPES_MachineComponents.circuitTier7, RECIPES_MachineComponents.plateTier8,
				RECIPES_MachineComponents.rodTier8c, null, RECIPES_MachineComponents.circuitTier8,
				RECIPES_MachineComponents.plateTier8, RECIPES_MachineComponents.plateTier8,
				RECIPE_CONSTANTS.sensor_ZPM);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier9, null,
				RECIPES_MachineComponents.circuitTier8, RECIPES_MachineComponents.plateTier9,
				RECIPES_MachineComponents.rodTier9c, null, RECIPES_MachineComponents.circuitTier9,
				RECIPES_MachineComponents.plateTier9, RECIPES_MachineComponents.plateTier9, RECIPE_CONSTANTS.sensor_UV);
		RecipeUtils.addShapedGregtechRecipe(RECIPES_MachineComponents.plateTier10, null,
				RECIPES_MachineComponents.circuitTier9, RECIPES_MachineComponents.plateTier10,
				RECIPES_MachineComponents.rodTier10c, null, RECIPES_MachineComponents.circuitTier10,
				RECIPES_MachineComponents.plateTier10, RECIPES_MachineComponents.plateTier10,
				RECIPE_CONSTANTS.sensor_MAX);

		Utils.LOG_INFO("Done loading recipes for the Various machine components.");

	}
}
