package miscutil.core.recipe;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.util.Utils;
import miscutil.core.util.recipe.UtilsRecipe;
import miscutil.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPES_MachineComponents {

	//Outputs
	static ItemStack RECIPE_Circuit_IV = GregtechItemList.Circuit_IV.get(1);
	static ItemStack RECIPE_Circuit_LuV = GregtechItemList.Circuit_LuV.get(1);
	static ItemStack RECIPE_Circuit_ZPM = GregtechItemList.Circuit_ZPM.get(1);
	static ItemStack RECIPE_Circuit_Board_IV = GregtechItemList.Circuit_Board_IV.get(1);
	static ItemStack RECIPE_Circuit_Board_LuV = GregtechItemList.Circuit_Board_LuV.get(1);
	static ItemStack RECIPE_Circuit_Board_ZPM = GregtechItemList.Circuit_Board_ZPM.get(1);
	static ItemStack RECIPE_Circuit_Parts_Crystal_Chip_IV = GregtechItemList.Circuit_Parts_Crystal_Chip_IV.get(1);
	static ItemStack RECIPE_Circuit_Parts_Crystal_Chip_LuV = GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.get(1);
	static ItemStack RECIPE_Circuit_Parts_Crystal_Chip_ZPM = GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.get(1);
	static ItemStack RECIPE_Circuit_Parts_IV = GregtechItemList.Circuit_Parts_IV.get(1);
	static ItemStack RECIPE_Circuit_Parts_LuV = GregtechItemList.Circuit_Parts_LuV.get(1);
	static ItemStack RECIPE_Circuit_Parts_ZPM = GregtechItemList.Circuit_Parts_ZPM.get(1);
	
	//Wire
	static String wireTier6 = "wireGt08NiobiumTitanium";
	static String wireTier7 = "wireGt08Osmium";
	static String wireTier8 = "wireGt08Naquadah";
	static String wireTier9 = "wireGt08Superconductor";
	static String wireTier10 = "wireGt16Superconductor";

	//Wire
	static String cableTier6 = "cableGt04Tungsten";
	static String cableTier7 = "cableGt04NiobiumTitanium";
	static String cableTier8 = "cableGt04Osmium";
	static String cableTier9 = "cableGt04Naquadah";
	static String cableTier10 = "wireGt08Superconductor";

	//Plates
	static String plateTier7 = "plateChrome";
	static String plateTier8 = "plateIridium";
	static String plateTier9 = "plateOsmium";
	static String plateTier10 = "plateNeutronium";

	//rods
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
	static String screwTier7 = "screwChrome";
	static String screwTier8 = "screwIridium";
	static String screwTier9 = "screwOsmium";
	static String screwTier10 = "screwNeutronium";

	//Rotors
	static String rotorTier7 = "rotorChrome";
	static String rotorTier8 = "rotorIridium";
	static String rotorTier9 = "rotorOsmium";
	static String rotorTier10 = "rotorNeutronium";

	//Fluid Pipe
	static String pipeTier7 = "pipeHugeSteel";
	static String pipeTier8 = "pipeHugeStainlessSteel";
	static String pipeTier9 = "pipeHugeTitanium";
	static String pipeTier10 = "pipeHugeTungstenSteel";

	//Rubber Ring/Plate
	static String itemRubberRing = "ringRubber";
	static String plateRubber = "plateRubber";

	//Circuits
	static String circuitTier6 = "circuitMaster";
	static String circuitTier7 = "circuitUltimate";
	static String circuitTier8 = "circuitSymbiotic";
	static String circuitTier9 = "circuitNeutronic";
	static String circuitTier10 = "circuitQuantum";

	//small gears
	static String smallGearTier7 = "gearGtSmallChrome";
	static String smallGearTier8 = "gearGtSmallIridium";
	static String smallGearTier9 = "gearGtSmallOsmium";
	static String smallGearTier10 = "gearGtSmallNeutronium";

	//Crafting Tools
	static String craftingToolWrench = "craftingToolWrench";
	static String craftingToolScrewdriver = "craftingToolScrewdriver";

	//Machine Components
	static ItemStack electricMotor_LuV = GregtechItemList.Electric_Motor_LuV.get(1);
	static ItemStack electricMotor_ZPM = GregtechItemList.Electric_Motor_ZPM.get(1);
	static ItemStack electricMotor_UV = GregtechItemList.Electric_Motor_UV.get(1);
	static ItemStack electricMotor_MAX = GregtechItemList.Electric_Motor_MAX.get(1);

	static ItemStack electricPump_LuV = GregtechItemList.Electric_Pump_LuV.get(1);
	static ItemStack electricPump_ZPM = GregtechItemList.Electric_Pump_ZPM.get(1);
	static ItemStack electricPump_UV = GregtechItemList.Electric_Pump_UV.get(1);
	static ItemStack electricPump_MAX = GregtechItemList.Electric_Pump_MAX.get(1);

	static ItemStack electricPiston_LuV = GregtechItemList.Electric_Piston_LuV.get(1);
	static ItemStack electricPiston_ZPM = GregtechItemList.Electric_Piston_ZPM.get(1);
	static ItemStack electricPiston_UV = GregtechItemList.Electric_Piston_UV.get(1);
	static ItemStack electricPiston_MAX = GregtechItemList.Electric_Piston_MAX.get(1);

	static ItemStack robotArm_LuV = GregtechItemList.Robot_Arm_LuV.get(1);
	static ItemStack robotArm_ZPM = GregtechItemList.Robot_Arm_ZPM.get(1);
	static ItemStack robotArm_UV = GregtechItemList.Robot_Arm_UV.get(1);
	static ItemStack robotArm_MAX = GregtechItemList.Robot_Arm_MAX.get(1);

	static ItemStack conveyorModule_LuV = GregtechItemList.Conveyor_Module_LuV.get(1);
	static ItemStack conveyorModule_ZPM = GregtechItemList.Conveyor_Module_ZPM.get(1);
	static ItemStack conveyorModule_UV = GregtechItemList.Conveyor_Module_UV.get(1);
	static ItemStack conveyorModule_MAX = GregtechItemList.Conveyor_Module_MAX.get(1);

	static ItemStack emitter_LuV = GregtechItemList.Emitter_LuV.get(1);
	static ItemStack emitter_ZPM = GregtechItemList.Emitter_ZPM.get(1);
	static ItemStack emitter_UV = GregtechItemList.Emitter_UV.get(1);
	static ItemStack emitter_MAX = GregtechItemList.Emitter_MAX.get(1);

	static ItemStack fieldGenerator_LuV = GregtechItemList.Field_Generator_LuV.get(1);
	static ItemStack fieldGenerator_ZPM = GregtechItemList.Field_Generator_ZPM.get(1);
	static ItemStack fieldGenerator_UV = GregtechItemList.Field_Generator_UV.get(1);
	static ItemStack fieldGenerator_MAX = GregtechItemList.Field_Generator_MAX.get(1);

	static ItemStack sensor_LuV = GregtechItemList.Sensor_LuV.get(1);
	static ItemStack sensor_ZPM = GregtechItemList.Sensor_ZPM.get(1);
	static ItemStack sensor_UV = GregtechItemList.Sensor_UV.get(1);
	static ItemStack sensor_MAX = GregtechItemList.Sensor_MAX.get(1);

	public static final void RECIPES_LOAD(){
		run();
		Utils.LOG_INFO("Loading Recipes for the Various Circuits and their components.");
	}

	private static void run(){
		//Electric Motors
		UtilsRecipe.recipeBuilder(
				cableTier7, wireTier7, rodTier7a,
				wireTier7, rodTier7b, wireTier7,
				rodTier7a, wireTier7, cableTier7,
				electricMotor_LuV);
		UtilsRecipe.recipeBuilder(
				cableTier8, wireTier8, rodTier8a,
				wireTier8, rodTier8b, wireTier8,
				rodTier8a, wireTier8, cableTier8,
				electricMotor_ZPM);
		UtilsRecipe.recipeBuilder(
				cableTier9, wireTier9, rodTier9a,
				wireTier9, rodTier9b, wireTier9,
				rodTier9a, wireTier9, cableTier9,
				electricMotor_UV);
		UtilsRecipe.recipeBuilder(
				cableTier10, wireTier10, rodTier10a,
				wireTier10, rodTier10b, wireTier10,
				rodTier10a, wireTier10, cableTier10,
				electricMotor_MAX);

		//Electric Pump
		UtilsRecipe.recipeBuilder(
				cableTier7, rotorTier7, itemRubberRing,
				craftingToolScrewdriver, pipeTier7, craftingToolWrench,
				itemRubberRing, electricMotor_LuV, cableTier7,
				electricPump_LuV);
		UtilsRecipe.recipeBuilder(
				cableTier8, rotorTier8, itemRubberRing,
				craftingToolScrewdriver, pipeTier8, craftingToolWrench,
				itemRubberRing, electricMotor_ZPM, cableTier8,
				electricPump_ZPM);
		UtilsRecipe.recipeBuilder(
				cableTier9, rotorTier9, itemRubberRing,
				craftingToolScrewdriver, pipeTier9, craftingToolWrench,
				itemRubberRing, electricMotor_UV, cableTier9,
				electricPump_UV);
		UtilsRecipe.recipeBuilder(
				cableTier10, rotorTier10, itemRubberRing,
				craftingToolScrewdriver, pipeTier10, craftingToolWrench,
				itemRubberRing, electricMotor_MAX, cableTier10,
				electricPump_MAX);

		//Electric Pump
		UtilsRecipe.recipeBuilder(
				plateTier7, plateTier7, plateTier7,
				cableTier7, rodTier7a, rodTier7a,
				cableTier7, electricMotor_LuV, smallGearTier7,
				electricPiston_LuV);
		UtilsRecipe.recipeBuilder(
				plateTier8, plateTier8, plateTier8,
				cableTier8, rodTier8a, rodTier8a,
				cableTier8, electricMotor_ZPM, smallGearTier8,
				electricPiston_ZPM);
		UtilsRecipe.recipeBuilder(
				plateTier9, plateTier9, plateTier9,
				cableTier9, rodTier9a, rodTier9a,
				cableTier9, electricMotor_UV, smallGearTier9,
				electricPiston_UV);
		UtilsRecipe.recipeBuilder(
				plateTier10, plateTier10, plateTier10,
				cableTier10, rodTier10a, rodTier10a,
				cableTier10, electricMotor_MAX, smallGearTier10,
				electricPiston_MAX);

		//Robot Arms
		UtilsRecipe.recipeBuilder(
				cableTier7, cableTier7, cableTier7,
				electricMotor_LuV, rodTier7a, electricMotor_LuV,
				electricPiston_LuV, circuitTier7, rodTier7a,
				robotArm_LuV);
		UtilsRecipe.recipeBuilder(
				cableTier8, cableTier8, cableTier8,
				electricMotor_ZPM, rodTier8a, electricMotor_ZPM,
				electricPiston_ZPM, circuitTier8, rodTier8a,
				robotArm_ZPM);
		UtilsRecipe.recipeBuilder(
				cableTier9, cableTier9, cableTier9,
				electricMotor_UV, rodTier9a, electricMotor_UV,
				electricPiston_UV, circuitTier9, rodTier9a,
				robotArm_UV);
		UtilsRecipe.recipeBuilder(
				cableTier10, cableTier10, cableTier10,
				electricMotor_MAX, rodTier10a, electricMotor_MAX,
				electricPiston_MAX, circuitTier10, rodTier10a,
				robotArm_MAX);

		//Conveyor Modules
		UtilsRecipe.recipeBuilder(
				plateRubber, plateRubber, plateRubber,
				electricMotor_LuV, cableTier7, electricMotor_LuV,
				plateRubber, plateRubber, plateRubber,
				conveyorModule_LuV);
		UtilsRecipe.recipeBuilder(
				plateRubber, plateRubber, plateRubber,
				electricMotor_ZPM, cableTier8, electricMotor_ZPM,
				plateRubber, plateRubber, plateRubber,
				conveyorModule_ZPM);
		UtilsRecipe.recipeBuilder(
				plateRubber, plateRubber, plateRubber,
				electricMotor_UV, cableTier9, electricMotor_UV,
				plateRubber, plateRubber, plateRubber,
				conveyorModule_UV);
		UtilsRecipe.recipeBuilder(
				plateRubber, plateRubber, plateRubber,
				electricMotor_MAX, cableTier10, electricMotor_MAX,
				plateRubber, plateRubber, plateRubber,
				conveyorModule_MAX);

		//Emitter Modules
		UtilsRecipe.recipeBuilder(
				rodTier7c, rodTier7c, circuitTier7,
				cableTier7, circuitTier6, rodTier7c,
				circuitTier7, cableTier7, rodTier7c,
				emitter_LuV);
		UtilsRecipe.recipeBuilder(
				rodTier8c, rodTier8c, circuitTier8,
				cableTier8, circuitTier7, rodTier8c,
				circuitTier8, cableTier8, rodTier8c,
				emitter_ZPM);
		UtilsRecipe.recipeBuilder(
				rodTier9c, rodTier9c, circuitTier9,
				cableTier9, circuitTier8, rodTier9c,
				circuitTier9, cableTier9, rodTier9c,
				emitter_UV);
		UtilsRecipe.recipeBuilder(
				rodTier10c, rodTier10c, circuitTier10,
				cableTier10, circuitTier9, rodTier10c,
				circuitTier10, cableTier10, rodTier10c,
				emitter_MAX);

		//Field Generator Modules
		UtilsRecipe.recipeBuilder(
				wireTier7, circuitTier7, wireTier7,
				circuitTier7, circuitTier6, circuitTier7,
				wireTier7, circuitTier7, wireTier7,
				fieldGenerator_LuV);
		UtilsRecipe.recipeBuilder(
				wireTier8, circuitTier8, wireTier8,
				circuitTier8, circuitTier7, circuitTier8,
				wireTier8, circuitTier8, wireTier8,
				fieldGenerator_ZPM);
		UtilsRecipe.recipeBuilder(
				wireTier9, circuitTier9, wireTier9,
				circuitTier9, circuitTier8, circuitTier9,
				wireTier9, circuitTier9, wireTier9,
				fieldGenerator_UV);
		UtilsRecipe.recipeBuilder(
				wireTier10, circuitTier10, wireTier10,
				circuitTier10, circuitTier9, circuitTier10,
				wireTier10, circuitTier10, wireTier10,
				fieldGenerator_MAX);

		//Sensor Modules
		UtilsRecipe.recipeBuilder(
				plateTier7, null, circuitTier6,
				plateTier7, rodTier7c, null,
				circuitTier7, plateTier7, plateTier7,
				sensor_LuV);
		UtilsRecipe.recipeBuilder(
				plateTier8, null, circuitTier7,
				plateTier8, rodTier8c, null,
				circuitTier8, plateTier8, plateTier8,
				sensor_ZPM);
		UtilsRecipe.recipeBuilder(
				plateTier9, null, circuitTier8,
				plateTier9, rodTier9c, null,
				circuitTier9, plateTier9, plateTier9,
				sensor_UV);
		UtilsRecipe.recipeBuilder(
				plateTier10, null, circuitTier9,
				plateTier10, rodTier10c, null,
				circuitTier10, plateTier10, plateTier10,
				sensor_MAX);

		Utils.LOG_INFO("Done loading recipes for the Various circuit content.");
		GregtechMachinePhase();
	}		
	
	private static void GregtechMachinePhase(){
		Utils.LOG_INFO("Adding Gregtech machine recipes for the components.");
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GregtechItemList.Circuit_Parts_Wiring_IV.get(4L, new Object[0]), GregtechItemList.Circuit_Board_IV.get(1L, new Object[0]), 32, 256);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GregtechItemList.Circuit_Parts_Wiring_LuV.get(4L, new Object[0]), GregtechItemList.Circuit_Board_LuV.get(1L, new Object[0]), 64, 512);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 2L), GregtechItemList.Circuit_Parts_Wiring_ZPM.get(4L, new Object[0]), GregtechItemList.Circuit_Board_ZPM.get(1L, new Object[0]), 96, 1024);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GregtechItemList.Circuit_Parts_Crystal_Chip_IV.get(3L, new Object[0]), GregtechItemList.Circuit_Parts_IV.get(1L, new Object[0]), 32, 512);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.get(3L, new Object[0]), GregtechItemList.Circuit_Parts_LuV.get(1L, new Object[0]), 64, 1024);
		GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.get(3L, new Object[0]), GregtechItemList.Circuit_Parts_ZPM.get(1L, new Object[0]), 96, 2048);
		GT_Values.RA.addForgeHammerRecipe(ItemList.Circuit_Master.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_Crystal_Chip_IV.get(5L, new Object[0]), 32, 256);
		GT_Values.RA.addForgeHammerRecipe(GregtechItemList.Circuit_IV.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.get(5L, new Object[0]), 64, 512);
		GT_Values.RA.addForgeHammerRecipe(GregtechItemList.Circuit_LuV.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.get(5L, new Object[0]), 128, 1024);
		for (Materials tMat : Materials.VALUES) {
			if ((tMat.mStandardMoltenFluid != null) && (tMat.contains(SubTag.SOLDERING_MATERIAL))) {
				int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1 : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_IV.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_IV.get(1L, new Object[0]), tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Circuit_IV.get(1L, new Object[0]), 32, 512);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_LuV.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_LuV.get(1L, new Object[0]), tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Circuit_LuV.get(1L, new Object[0]), 64, 1024);
				GT_Values.RA.addAssemblerRecipe(GregtechItemList.Circuit_Board_ZPM.get(1L, new Object[0]), GregtechItemList.Circuit_Parts_ZPM.get(1L, new Object[0]), tMat.getMolten(144L * tMultiplier / 4L), GregtechItemList.Circuit_ZPM.get(1L, new Object[0]), 96, 2048);
				
			}
		}		
	}
}
