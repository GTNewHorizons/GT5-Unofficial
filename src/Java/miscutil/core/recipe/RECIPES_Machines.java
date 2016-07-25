package miscutil.core.recipe;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.api.enums.GregtechOrePrefixes;
import miscutil.core.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import net.minecraft.item.ItemStack;

public class RECIPES_Machines {

	//Outputs
	//static ItemStack RECIPE_BufferCore_ULV = new ItemStack(GregtechEnergyBuffer.itemBufferCore);
	static ItemStack RECIPE_SteamCondenser = GregtechItemList.Condensor_MAX.get(1);
	static ItemStack RECIPE_IronBlastFurnace = GregtechItemList.Machine_Iron_BlastFurnace.get(1);
	static ItemStack RECIPE_IronPlatedBricks = GregtechItemList.Casing_IronPlatedBricks.get(1);
	static ItemStack RECIPE_Buffer_ULV = GregtechItemList.Energy_Buffer_1by1_ULV.get(1);
	static ItemStack RECIPE_Buffer_LV = GregtechItemList.Energy_Buffer_1by1_LV.get(1);
	static ItemStack RECIPE_Buffer_MV = GregtechItemList.Energy_Buffer_1by1_MV.get(1);
	static ItemStack RECIPE_Buffer_HV = GregtechItemList.Energy_Buffer_1by1_HV.get(1);
	static ItemStack RECIPE_Buffer_EV = GregtechItemList.Energy_Buffer_1by1_EV.get(1);
	static ItemStack RECIPE_Buffer_IV = GregtechItemList.Energy_Buffer_1by1_IV.get(1);
	static ItemStack RECIPE_Buffer_LuV = GregtechItemList.Energy_Buffer_1by1_LuV.get(1);
	static ItemStack RECIPE_Buffer_ZPM = GregtechItemList.Energy_Buffer_1by1_ZPM.get(1);
	static ItemStack RECIPE_Buffer_UV = GregtechItemList.Energy_Buffer_1by1_UV.get(1);
	static ItemStack RECIPE_Buffer_MAX = GregtechItemList.Energy_Buffer_1by1_MAX.get(1);
	//Industrial Centrifuge
	static ItemStack RECIPE_IndustrialCentrifugeController = GregtechItemList.Industrial_Centrifuge.get(1);
	static ItemStack RECIPE_IndustrialCentrifugeCasing = GregtechItemList.Casing_Centrifuge1.get(1);
	//Industrial Coke Oven
	static ItemStack RECIPE_IndustrialCokeOvenController = GregtechItemList.Industrial_CokeOven.get(1);
	static ItemStack RECIPE_IndustrialCokeOvenFrame = GregtechItemList.Casing_CokeOven.get(1);
	static ItemStack RECIPE_IndustrialCokeOvenCasingA = GregtechItemList.Casing_CokeOven_Coil1.get(1);
	static ItemStack RECIPE_IndustrialCokeOvenCasingB = GregtechItemList.Casing_CokeOven_Coil2.get(1);
	//
	static ItemStack RECIPE_IndustrialElectrolyzerController = GregtechItemList.Industrial_Electrolyzer.get(1);
	static ItemStack RECIPE_IndustrialElectrolyzerFrame = GregtechItemList.Casing_Electrolyzer.get(1);
	//
	static ItemStack RECIPE_IndustrialMaterialPressController = GregtechItemList.Industrial_PlatePress.get(1);
	static ItemStack RECIPE_IndustrialMaterialPressFrame = GregtechItemList.Casing_MaterialPress.get(1);
	//
	static ItemStack RECIPE_IndustrialMacerationStackController = GregtechItemList.Industrial_MacerationStack.get(1);
	static ItemStack RECIPE_IndustrialMacerationStackFrame = GregtechItemList.Casing_MacerationStack.get(1);
	//
	static ItemStack RECIPE_IndustrialWireFactoryController = GregtechItemList.Industrial_WireFactory.get(1);
	static ItemStack RECIPE_IndustrialWireFactoryFrame = GregtechItemList.Casing_WireFactory.get(1);


	//Buffer Cores
	static ItemStack RECIPE_BufferCore_ULV = UtilsItems.getItemStack("miscutils:item.itemBufferCore1", 1);
	static ItemStack RECIPE_BufferCore_LV = UtilsItems.getItemStack("miscutils:item.itemBufferCore2", 1);
	static ItemStack RECIPE_BufferCore_MV = UtilsItems.getItemStack("miscutils:item.itemBufferCore3", 1);
	static ItemStack RECIPE_BufferCore_HV = UtilsItems.getItemStack("miscutils:item.itemBufferCore4", 1);
	static ItemStack RECIPE_BufferCore_EV = UtilsItems.getItemStack("miscutils:item.itemBufferCore5", 1);
	static ItemStack RECIPE_BufferCore_IV = UtilsItems.getItemStack("miscutils:item.itemBufferCore6", 1);
	static ItemStack RECIPE_BufferCore_LuV = UtilsItems.getItemStack("miscutils:item.itemBufferCore7", 1);
	static ItemStack RECIPE_BufferCore_ZPM = UtilsItems.getItemStack("miscutils:item.itemBufferCore8", 1);
	static ItemStack RECIPE_BufferCore_UV = UtilsItems.getItemStack("miscutils:item.itemBufferCore9", 1);
	static ItemStack RECIPE_BufferCore_MAX = UtilsItems.getItemStack("miscutils:item.itemBufferCore10", 1);


	//Wire
	static String wireTier1 = "wireGt08Lead";
	static String wireTier2 = "wireGt08Tin";
	static String wireTier3 = "wireGt08Copper";
	static String wireTier4 = "wireGt08Gold";
	static String wireTier5 = "wireGt08Aluminium";
	static String wireTier6 = "wireGt08Tungsten";
	static String wireTier7 = "wireGt08Osmium";
	static String wireTier8 = "wireGt08Naquadah";
	static String wireTier9 = "wireGt08Superconductor";
	static String wireTier10 = "wireGt16Superconductor";

	//Wire
	static String cableTier1 = "cableGt04Lead";
	static String cableTier2 = "cableGt04Tin";
	static String cableTier3 = "cableGt04Copper";
	static String cableTier4 = "cableGt04Gold";
	static String cableTier5 = "cableGt04Aluminium";
	static String cableTier6 = "cableGt04Tungsten";
	static String cableTier7 = "cableGt04Osmium";
	static String cableTier8 = "cableGt04Naquadah";
	static String cableTier9 = "cableGt04NiobiumTitanium";
	static String cableTier10 = "cableGt08NiobiumTitanium";


	//Plates
	static String plateTier1 = "plateLead";
	static String plateTier2 = "plateTin";
	static String plateTier3 = "plateCopper";
	static String plateTier4 = "plateGold";
	static String plateTier5 = "plateAluminium";
	static String plateTier6 = "plateThorium";
	static String plateTier7 = "plateTungsten";
	static String plateTier8 = "plateTungstenSteel";
	static String plateTier9 = "plateOsmium";
	static String plateTier10 = "plateNaquadah";
	static String plateTier11 = "plateNeutronium";

	//rods
	static String rodTier1 = "stickLead";
	static String rodTier2 = "stickTin";
	static String rodTier3 = "stickCopper";
	static String rodTier4 = "stickGold";
	static String rodTier5 = "stickAluminium";
	static String rodTier6 = "stickThorium";
	static String rodTier7 = "stickTungsten";
	static String rodTier8 = "stickTungstenSteel";
	static String rodTier9 = "stickOsmium";
	static String rodTier10 = "stickNaquadah";
	static String rodTier11 = "stickNeutronium";


	//Machine Casings
	static ItemStack machineCasing_ULV; 
	static ItemStack machineCasing_LV; 
	static ItemStack machineCasing_MV;
	static ItemStack machineCasing_HV;
	static ItemStack machineCasing_EV;
	static ItemStack machineCasing_IV;
	static ItemStack machineCasing_LuV; 
	static ItemStack machineCasing_ZPM; 
	static ItemStack machineCasing_UV;
	static ItemStack machineCasing_MAX;

	//Gearbox Casings
	static ItemStack gearboxCasing_Tier_1;
	static ItemStack gearboxCasing_Tier_2;
	static ItemStack gearboxCasing_Tier_3;
	static ItemStack gearboxCasing_Tier_4;

	//IV MACHINES	
	public static ItemStack IV_MACHINE_Electrolyzer;
	public static ItemStack IV_MACHINE_Centrifuge;
	public static ItemStack IV_MACHINE_BendingMachine;
	public static ItemStack IV_MACHINE_Wiremill;
	public static ItemStack IV_MACHINE_Macerator;
	public static ItemStack IV_MACHINE_MassFabricator;


	//Cables
	static String cableGt02Electrum = "cableGt02Electrum";


	//Plates
	static String plateElectricalSteel= "plateElectricalSteel";	
	static String plateEnergeticAlloy= "plateEnergeticAlloy";
	static String plateCobalt = "plateCobalt";
	static String plateBronze = "plateBronze";
	static String plateSteel = "plateSteel";

	//Pipes
	static String pipeLargeCopper="pipeLargeCopper";
	static String pipeHugeSteel="pipeHugeSteel";
	static String pipeHugeStainlessSteel="pipeHugeStainlessSteel";
	static String pipeHugeTitanium="pipeHugeTitanium";

	//Lava Boiler
	static ItemStack boiler_Coal;
	static ItemStack blockBricks = UtilsItems.getItemStack("minecraft:brick_block", 1);

	//Batteries
	static String batteryBasic = "batteryBasic";
	static String batteryAdvanced = "batteryAdvanced";
	static String batteryElite = "batteryElite";
	static String batteryMaster = "batteryMaster";
	static String batteryUltimate = "batteryUltimate";
	static ItemStack IC2MFE;
	static ItemStack IC2MFSU;

	//Circuits
	static String circuitPrimitive = "circuitBasic";
	static String circuitTier1 = "circuitGood";
	static String circuitTier2 = "circuitAdvanced";
	static String circuitTier3 = "circuitData";
	static String circuitTier4 = "circuitElite";
	static String circuitTier5 = "circuitMaster";
	static String circuitTier6 = "circuitUltimate";
	static String circuitTier7 = "circuitSymbiotic";
	static String circuitTier8 = "circuitNeutronic";
	static String circuitTier9 = "circuitQuantum";

	//Machine Components
	static ItemStack electricMotor_LV;
	static ItemStack electricMotor_MV;
	static ItemStack electricMotor_HV;
	static ItemStack electricMotor_EV;
	static ItemStack electricMotor_IV;
	static ItemStack electricPump_LV;
	static ItemStack electricPump_MV;
	static ItemStack electricPump_HV;
	static ItemStack electricPump_EV;
	static ItemStack electricPump_IV;
	static ItemStack electricPiston_LV;
	static ItemStack electricPiston_MV;
	static ItemStack electricPiston_HV;
	static ItemStack electricPiston_EV;
	static ItemStack electricPiston_IV;
	static ItemStack robotArm_LV;
	static ItemStack robotArm_MV;
	static ItemStack robotArm_HV;
	static ItemStack robotArm_EV;
	static ItemStack robotArm_IV;
	static ItemStack conveyorModule_LV;
	static ItemStack conveyorModule_MV;
	static ItemStack conveyorModule_HV;
	static ItemStack conveyorModule_EV;
	static ItemStack conveyorModule_IV;
	static ItemStack emitter_LV;
	static ItemStack emitter_MV;
	static ItemStack emitter_HV;
	static ItemStack emitter_EV;
	static ItemStack emitter_IV;
	static ItemStack fieldGenerator_LV;
	static ItemStack fieldGenerator_MV;
	static ItemStack fieldGenerator_HV;
	static ItemStack fieldGenerator_EV;
	static ItemStack fieldGenerator_IV;
	static ItemStack sensor_LV;
	static ItemStack sensor_MV;
	static ItemStack sensor_HV;
	static ItemStack sensor_EV;
	static ItemStack sensor_IV;

	//Misc
	static ItemStack INPUT_RCCokeOvenBlock;
	static ItemStack INPUT_IECokeOvenBlock;



	//RobotArm, Conveyor, Emitter, Sensor, Field Generator


	public static final void RECIPES_LOAD(){
		run();
		Utils.LOG_INFO("Loading Recipes for the Various machine blocks.");
	}

	private static void run(){
		initModItems();
	}	

	private static void initModItems(){
		if (LoadedMods.IndustrialCraft2){
			IC2MFE = UtilsItems.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:blockElectric", "IC2_MFE", 1, 1);
			IC2MFSU = UtilsItems.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:blockElectric", "IC2_MFSU", 2, 1);
		}
		if (LoadedMods.Gregtech){
			RECIPES_Shapeless.dustStaballoy = UtilsItems.getItemStackWithMeta(LoadedMods.MiscUtils, "gregtech:gt.metaitem.01", "Staballoy Dust", 2319, 2);
			machineCasing_ULV = ItemList.Casing_ULV.get(1);
			machineCasing_LV = ItemList.Casing_LV.get(1);
			machineCasing_MV = ItemList.Casing_MV.get(1);
			machineCasing_HV = ItemList.Casing_HV.get(1);
			machineCasing_EV = ItemList.Casing_EV.get(1);
			machineCasing_IV = ItemList.Casing_IV.get(1);
			machineCasing_LuV = ItemList.Casing_LuV.get(1);
			machineCasing_ZPM = ItemList.Casing_ZPM.get(1);
			machineCasing_UV = ItemList.Casing_UV.get(1);
			machineCasing_MAX = ItemList.Casing_MAX.get(1);

			//Gearbox Casings
			gearboxCasing_Tier_1 = ItemList.Casing_Gearbox_Bronze.get(1);
			gearboxCasing_Tier_2 = ItemList.Casing_Gearbox_Steel.get(1);
			gearboxCasing_Tier_3 = ItemList.Casing_Gearbox_Titanium.get(1);
			gearboxCasing_Tier_4 = ItemList.Casing_Gearbox_TungstenSteel.get(1);

			//Lava Boiler
			boiler_Coal = ItemList.Machine_Bronze_Boiler.get(1);

			//Machine Components
			electricMotor_LV = ItemList.Electric_Motor_LV.get(1);
			electricMotor_MV = ItemList.Electric_Motor_MV.get(1);
			electricMotor_HV = ItemList.Electric_Motor_HV.get(1);
			electricMotor_EV = ItemList.Electric_Motor_EV.get(1);
			electricMotor_IV = ItemList.Electric_Motor_IV.get(1);
			electricPump_LV = ItemList.Electric_Pump_LV.get(1);
			electricPump_MV = ItemList.Electric_Pump_MV.get(1);
			electricPump_HV = ItemList.Electric_Pump_HV.get(1);
			electricPump_EV = ItemList.Electric_Pump_EV.get(1);
			electricPump_IV = ItemList.Electric_Pump_IV.get(1);
			electricPiston_LV = ItemList.Electric_Piston_LV.get(1);
			electricPiston_MV = ItemList.Electric_Piston_MV.get(1);
			electricPiston_HV = ItemList.Electric_Piston_HV.get(1);
			electricPiston_EV = ItemList.Electric_Piston_EV.get(1);
			electricPiston_IV = ItemList.Electric_Piston_IV.get(1);
			robotArm_LV = ItemList.Robot_Arm_LV.get(1);
			robotArm_MV = ItemList.Robot_Arm_MV.get(1);
			robotArm_HV = ItemList.Robot_Arm_HV.get(1);
			robotArm_EV = ItemList.Robot_Arm_EV.get(1);
			robotArm_IV = ItemList.Robot_Arm_IV.get(1);
			conveyorModule_LV = ItemList.Conveyor_Module_LV.get(1);
			conveyorModule_MV = ItemList.Conveyor_Module_MV.get(1);
			conveyorModule_HV = ItemList.Conveyor_Module_HV.get(1);
			conveyorModule_EV = ItemList.Conveyor_Module_EV.get(1);
			conveyorModule_IV = ItemList.Conveyor_Module_IV.get(1);
			emitter_LV = ItemList.Emitter_LV.get(1);
			emitter_MV = ItemList.Emitter_MV.get(1);
			emitter_HV = ItemList.Emitter_HV.get(1);
			emitter_EV = ItemList.Emitter_EV.get(1);
			emitter_IV = ItemList.Emitter_IV.get(1);
			fieldGenerator_LV = ItemList.Field_Generator_LV.get(1);
			fieldGenerator_MV = ItemList.Field_Generator_MV.get(1);
			fieldGenerator_HV = ItemList.Field_Generator_HV.get(1);
			fieldGenerator_EV = ItemList.Field_Generator_EV.get(1);
			fieldGenerator_IV = ItemList.Field_Generator_IV.get(1);
			sensor_LV = ItemList.Sensor_LV.get(1);
			sensor_MV = ItemList.Sensor_MV.get(1);
			sensor_HV = ItemList.Sensor_HV.get(1);
			sensor_EV = ItemList.Sensor_EV.get(1);
			sensor_IV = ItemList.Sensor_IV.get(1);


			//IV MACHINES	
			IV_MACHINE_Electrolyzer = ItemList.Machine_IV_Electrolyzer.get(1);
			IV_MACHINE_BendingMachine= ItemList.Machine_IV_Bender.get(1);
			IV_MACHINE_Wiremill= ItemList.Machine_IV_Wiremill.get(1);
			IV_MACHINE_Macerator= ItemList.Machine_IV_Macerator.get(1);
			IV_MACHINE_MassFabricator= ItemList.Machine_IV_Massfab.get(1);
			IV_MACHINE_Centrifuge= ItemList.Machine_IV_Centrifuge.get(1);


		}

		if(LoadedMods.Railcraft){
			//Misc
			INPUT_RCCokeOvenBlock = UtilsItems.getItemStackWithMeta(LoadedMods.Railcraft, "Railcraft:machine.alpha", "Coke_Oven_RC", 7, 1);
		}
		if(LoadedMods.ImmersiveEngineering){
			//Misc
			INPUT_IECokeOvenBlock = UtilsItems.getItemStackWithMeta(LoadedMods.ImmersiveEngineering, "ImmersiveEngineering:stoneDecoration", "Coke_Oven_IE", 1, 1);
		}
		runModRecipes();
	}	

	private static void runModRecipes(){
		if (LoadedMods.Gregtech){
			//Staballoy Dust - TEMP
			UtilsRecipe.recipeBuilder("dustTitanium", "dustUranium", "dustUranium",
					"dustUranium", "dustUranium", "dustUranium",
					"dustUranium", "dustUranium", "dustUranium",
					RECIPES_Shapeless.dustStaballoy);

			UtilsRecipe.addShapedGregtechRecipe(
					ItemList.Electric_Piston_EV, GregtechOrePrefixes.circuit.get(Materials.Ultimate), ItemList.Electric_Piston_EV,
					ItemList.Electric_Motor_EV, ItemList.Hull_EV, ItemList.Electric_Motor_EV,
					OrePrefixes.gearGt.get(Materials.Titanium), OrePrefixes.cableGt02.get(Materials.Aluminium), OrePrefixes.gearGt.get(Materials.Titanium),
					GregtechItemList.Rocket_Engine_EV.get(1L, new Object[0]));
			UtilsRecipe.addShapedGregtechRecipe(
					ItemList.Electric_Piston_IV, GregtechOrePrefixes.circuit.get(GT_Materials.Symbiotic), ItemList.Electric_Piston_IV,
					ItemList.Electric_Motor_IV, ItemList.Hull_IV, ItemList.Electric_Motor_IV,
					OrePrefixes.gearGt.get(Materials.TungstenSteel), OrePrefixes.cableGt02.get(Materials.Platinum), OrePrefixes.gearGt.get(Materials.TungstenSteel),
					GregtechItemList.Rocket_Engine_IV.get(1L, new Object[0]));
			UtilsRecipe.addShapedGregtechRecipe(
					RECIPE_CONSTANTS.electricPiston_LuV, GregtechOrePrefixes.circuit.get(GT_Materials.Neutronic), RECIPE_CONSTANTS.electricPiston_LuV,
					RECIPE_CONSTANTS.electricMotor_LuV, ItemList.Hull_LuV, RECIPE_CONSTANTS.electricMotor_LuV,
					OrePrefixes.gearGt.get(Materials.Chrome), OrePrefixes.cableGt02.get(Materials.Tungsten), OrePrefixes.gearGt.get(Materials.Chrome),
					GregtechItemList.Rocket_Engine_LuV.get(1L, new Object[0]));

			//Buffer Core
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier1, cableTier1, plateTier1,
					circuitPrimitive, IC2MFE, circuitPrimitive,
					plateTier1, cableTier1, plateTier1,
					RECIPE_BufferCore_ULV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier2, cableTier2, plateTier2,
					circuitTier1, IC2MFE, circuitTier1,
					plateTier2, cableTier2, plateTier2,
					RECIPE_BufferCore_LV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier3, cableTier3, plateTier3,
					RECIPE_BufferCore_LV, circuitTier2, RECIPE_BufferCore_LV,
					plateTier3, cableTier3, plateTier3,
					RECIPE_BufferCore_MV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier4, cableTier4, plateTier4,
					RECIPE_BufferCore_MV, circuitTier3, RECIPE_BufferCore_MV,
					plateTier4, cableTier4, plateTier4,
					RECIPE_BufferCore_HV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier5, cableTier5, plateTier5,
					RECIPE_BufferCore_HV, circuitTier4, RECIPE_BufferCore_HV,
					plateTier5, cableTier5, plateTier5,
					RECIPE_BufferCore_EV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier6, cableTier6, plateTier6,
					RECIPE_BufferCore_EV, circuitTier5, RECIPE_BufferCore_EV,
					plateTier6, cableTier6, plateTier6,
					RECIPE_BufferCore_IV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier7, cableTier7, plateTier7,
					RECIPE_BufferCore_IV, circuitTier6, RECIPE_BufferCore_IV,
					plateTier7, cableTier7, plateTier7,
					RECIPE_BufferCore_LuV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier8, cableTier8, plateTier8,
					RECIPE_BufferCore_LuV, circuitTier7, RECIPE_BufferCore_LuV,
					plateTier8, cableTier8, plateTier8,
					RECIPE_BufferCore_ZPM);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier9, cableTier9, plateTier9,
					RECIPE_BufferCore_ZPM, circuitTier8, RECIPE_BufferCore_ZPM,
					plateTier9, cableTier9, plateTier9,
					RECIPE_BufferCore_UV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier10, cableTier10, plateTier10,
					RECIPE_BufferCore_UV, circuitTier9, RECIPE_BufferCore_UV,
					plateTier10, cableTier10, plateTier10,
					RECIPE_BufferCore_MAX);


			UtilsRecipe.addShapedGregtechRecipe(
					wireTier1, RECIPE_BufferCore_ULV, wireTier1,
					wireTier1, machineCasing_ULV, wireTier1,
					circuitPrimitive, circuitTier1, circuitPrimitive,
					RECIPE_Buffer_ULV);
			UtilsRecipe.addShapedGregtechRecipe(
					wireTier2, RECIPE_BufferCore_LV, wireTier2,
					wireTier2, machineCasing_LV, wireTier2,
					circuitTier1, RECIPE_BufferCore_LV, circuitTier1,
					RECIPE_Buffer_LV);
			UtilsRecipe.addShapedGregtechRecipe(
					wireTier3, RECIPE_BufferCore_MV, wireTier3,
					wireTier3, machineCasing_MV, wireTier3,
					circuitTier2, RECIPE_BufferCore_MV, circuitTier2,
					RECIPE_Buffer_MV);
			UtilsRecipe.addShapedGregtechRecipe(
					wireTier4, RECIPE_BufferCore_HV, wireTier4,
					wireTier4, machineCasing_HV, wireTier4,
					circuitTier3, RECIPE_BufferCore_HV, circuitTier3,
					RECIPE_Buffer_HV);
			UtilsRecipe.addShapedGregtechRecipe(
					wireTier5, RECIPE_BufferCore_EV, wireTier5,
					wireTier5, machineCasing_EV, wireTier5,
					circuitTier4, RECIPE_BufferCore_EV, circuitTier4,
					RECIPE_Buffer_EV);
			UtilsRecipe.addShapedGregtechRecipe(
					wireTier6, RECIPE_BufferCore_IV, wireTier6,
					wireTier6, machineCasing_IV, wireTier6,
					circuitTier5, RECIPE_BufferCore_IV, circuitTier5,
					RECIPE_Buffer_IV);
			UtilsRecipe.addShapedGregtechRecipe(
					wireTier7, RECIPE_BufferCore_LuV, wireTier7,
					wireTier7, machineCasing_LuV, wireTier7,
					circuitTier6, RECIPE_BufferCore_LuV, circuitTier6,
					RECIPE_Buffer_LuV);
			UtilsRecipe.addShapedGregtechRecipe(
					wireTier8, RECIPE_BufferCore_ZPM, wireTier8,
					wireTier8, machineCasing_ZPM, wireTier8,
					circuitTier7, RECIPE_BufferCore_ZPM, circuitTier7,
					RECIPE_Buffer_ZPM);
			UtilsRecipe.addShapedGregtechRecipe(
					wireTier9, RECIPE_BufferCore_UV, wireTier9,
					wireTier9, machineCasing_UV, wireTier9,
					circuitTier8, RECIPE_BufferCore_UV, circuitTier8,
					RECIPE_Buffer_UV);
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier11, RECIPE_BufferCore_MAX, plateTier11,
					wireTier10, machineCasing_MAX, wireTier10,
					circuitTier9, RECIPE_BufferCore_MAX, circuitTier9,
					RECIPE_Buffer_MAX);

			//Steam Condenser
			UtilsRecipe.addShapedGregtechRecipe(
					pipeLargeCopper, pipeHugeSteel, pipeLargeCopper,
					plateEnergeticAlloy, electricPump_HV, plateEnergeticAlloy,
					plateEnergeticAlloy, pipeLargeCopper, plateEnergeticAlloy,
					RECIPE_SteamCondenser);

			//Iron BF
			UtilsRecipe.addShapedGregtechRecipe(
					"plateDoubleAnyIron", "craftingFurnace", "plateDoubleAnyIron",
					boiler_Coal, machineCasing_ULV, boiler_Coal,
					"plateDoubleAnyIron", "bucketLava", "plateDoubleAnyIron",
					RECIPE_IronBlastFurnace);
			//Iron plated Bricks
			UtilsRecipe.addShapedGregtechRecipe(
					"plateAnyIron", RECIPES_Tools.craftingToolHardHammer, "plateAnyIron",
					"plateAnyIron", blockBricks, "plateAnyIron",
					"plateAnyIron", RECIPES_Tools.craftingToolWrench, "plateAnyIron",
					RECIPE_IronPlatedBricks);

			//Industrial Centrifuge
			UtilsRecipe.addShapedGregtechRecipe(
					circuitTier6, pipeHugeStainlessSteel, circuitTier6,
					plateTier6, IV_MACHINE_Centrifuge, plateTier6,
					plateTier8, machineCasing_IV, plateTier8,
					RECIPE_IndustrialCentrifugeController);
			//Centrifuge Casing
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier6, "stickElectrum", plateTier6,
					plateTier8, "stickElectrum", plateTier8,
					plateTier6, "stickElectrum", plateTier6,
					RECIPE_IndustrialCentrifugeCasing);

			if (LoadedMods.Railcraft){
				//Industrial Coke Oven
				UtilsRecipe.addShapedGregtechRecipe(
						plateCobalt, circuitTier4, plateCobalt,
						machineCasing_HV, INPUT_RCCokeOvenBlock, machineCasing_HV,
						plateCobalt, circuitTier5, plateCobalt,
						RECIPE_IndustrialCokeOvenController);
			}
			if (LoadedMods.ImmersiveEngineering){
				//Industrial Coke Oven
				UtilsRecipe.addShapedGregtechRecipe(
						plateCobalt, circuitTier4, plateCobalt,
						machineCasing_HV, INPUT_IECokeOvenBlock, machineCasing_HV,
						plateCobalt, circuitTier5, plateCobalt,
						RECIPE_IndustrialCokeOvenController);
			}
			//Coke Oven Frame Casing
			UtilsRecipe.addShapedGregtechRecipe(
					plateTier8, rodTier8, plateTier8,
					rodTier8, "frameGtCobalt", rodTier8,
					plateTier8, rodTier8, plateTier8,
					RECIPE_IndustrialCokeOvenFrame);
			//Coke Oven Coil 1
			UtilsRecipe.addShapedGregtechRecipe(
					plateBronze, plateBronze, plateBronze,
					"frameGtBronze", gearboxCasing_Tier_1, "frameGtBronze",
					plateBronze, plateBronze, plateBronze,
					RECIPE_IndustrialCokeOvenCasingA);
			//Coke Oven Coil 2
			UtilsRecipe.addShapedGregtechRecipe(
					plateSteel, plateSteel, plateSteel,
					"frameGtSteel", gearboxCasing_Tier_2, "frameGtSteel",
					plateSteel, plateSteel, plateSteel,
					RECIPE_IndustrialCokeOvenCasingB);

			//Electrolyzer Frame Casing
			UtilsRecipe.addShapedGregtechRecipe(
					"plateSterlingSilver", "stickLongChrome", "plateSterlingSilver",
					"stickLongSterlingSilver", "frameGtSterlingSilver", "stickLongSterlingSilver",
					"plateSterlingSilver", "stickLongSterlingSilver", "plateSterlingSilver",
					RECIPE_IndustrialElectrolyzerFrame);
			//Industrial Electrolyzer
			UtilsRecipe.addShapedGregtechRecipe(
					"plateSterlingSilver", circuitTier6, "plateSterlingSilver",
					machineCasing_EV, IV_MACHINE_Electrolyzer, machineCasing_EV,
					"plateSterlingSilver", "rotorSterlingSilver", "plateSterlingSilver",
					RECIPE_IndustrialElectrolyzerController);

			//Material Press Frame Casing
			UtilsRecipe.addShapedGregtechRecipe(
					"plateTitanium", "stickLongTitanium", "plateTitanium",
					"stickMagnesium", "frameGtTitanium", "stickMagnesium",
					"plateTitanium", "stickLongTitanium", "plateTitanium",
					RECIPE_IndustrialMaterialPressFrame);
			//Industrial Material Press
			UtilsRecipe.addShapedGregtechRecipe(
					"plateTitanium", circuitTier5, "plateTitanium",
					machineCasing_EV, IV_MACHINE_BendingMachine, machineCasing_EV,
					"plateTitanium", circuitTier5, "plateTitanium",
					RECIPE_IndustrialMaterialPressController);

			//Maceration Frame Casing
			UtilsRecipe.addShapedGregtechRecipe(
					"platePalladium", "platePalladium", "platePalladium",
					"stickPlatinum", "frameGtPalladium", "stickPlatinum",
					"platePalladium", "stickLongPalladium", "platePalladium",
					RECIPE_IndustrialMacerationStackFrame);
			//Industrial Maceration stack 
			UtilsRecipe.addShapedGregtechRecipe(
					"plateDenseCarbon", IV_MACHINE_Macerator, "plateDenseCarbon",
					IV_MACHINE_Macerator, circuitTier8, IV_MACHINE_Macerator,
					"plateDenseCarbon", machineCasing_IV, "plateDenseCarbon",
					RECIPE_IndustrialMacerationStackController);

			//Wire Factory Frame Casing
			UtilsRecipe.addShapedGregtechRecipe(
					"plateBlueSteel", "stickBlueSteel", "plateBlueSteel",
					"stickBlueSteel", "frameGtBlueSteel", "stickBlueSteel",
					"plateBlueSteel", "stickBlueSteel", "plateBlueSteel",
					RECIPE_IndustrialWireFactoryFrame);
			//Industrial Wire Factory
			UtilsRecipe.addShapedGregtechRecipe(
					"plateOsmium", machineCasing_IV, "plateOsmium",
					circuitTier6, IV_MACHINE_Wiremill, circuitTier6,
					"plateOsmium", machineCasing_IV, "plateOsmium",
					RECIPE_IndustrialWireFactoryController);


		}


		Utils.LOG_INFO("Done loading recipes for the Various machine blocks.");
	}
}
