package miscutil.core.recipe;

import gregtech.api.enums.ItemList;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import miscutil.gregtech.api.enums.GregtechItemList;
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

	//Circuits
	static String circuitPrimitive = "circuitPrimitive";
	static String circuitTier1 = "circuitBasic";
	static String circuitTier2 = "circuitGood";
	static String circuitTier3 = "circuitAdvanced";
	static String circuitTier4 = "circuitData";
	static String circuitTier5 = "circuitElite";
	static String circuitTier6 = "circuitMaster";
	static String circuitTier7 = "circuitUltimate";
	static String circuitTier8 = "circuitSymbiotic";
	static String circuitTier9 = "circuitNeutronic";
	static String circuitTier10 = "circuitQuantum";

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



	//RobotArm, Conveyor, Emitter, Sensor, Field Generator


	public static final void RECIPES_LOAD(){
		run();
		Utils.LOG_INFO("Loading Recipes for the Various machine blocks.");
	}
	
	private static void run(){
		initModItems();
	}	

	private static void initModItems(){
		if (LoadedMods.Gregtech){
		RECIPES_Shapeless.dustStaballoy = UtilsItems.getItemStackWithMeta(LoadedMods.Gregtech, "gregtech:gt.metaitem.01", "Staballoy Dust", 2319, 1);
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
		sensor_IV = ItemList.Sensor_IV.get(1);}

		if(LoadedMods.Railcraft){
		//Misc
		INPUT_RCCokeOvenBlock = UtilsItems.getItemStackWithMeta(LoadedMods.Railcraft, "Railcraft:machine.alpha", "Coke_Oven_RC", 7, 1);
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
			
			//Buffer Core
			UtilsRecipe.recipeBuilder(
					plateTier1, cableTier1, plateTier1,
					circuitTier1, batteryBasic, circuitTier1,
					plateTier1, cableTier1, plateTier1,
					RECIPE_BufferCore_ULV);
			UtilsRecipe.recipeBuilder(
					plateTier2, cableTier2, plateTier2,
					circuitTier2, batteryBasic, circuitTier2,
					plateTier2, cableTier2, plateTier2,
					RECIPE_BufferCore_LV);
			UtilsRecipe.recipeBuilder(
					plateTier3, cableTier3, plateTier3,
					circuitTier3, batteryAdvanced, circuitTier3,
					plateTier3, cableTier3, plateTier3,
					RECIPE_BufferCore_MV);
			UtilsRecipe.recipeBuilder(
					plateTier4, cableTier4, plateTier4,
					circuitTier4, batteryAdvanced, circuitTier4,
					plateTier4, cableTier4, plateTier4,
					RECIPE_BufferCore_HV);
			UtilsRecipe.recipeBuilder(
					plateTier5, cableTier5, plateTier5,
					circuitTier5, batteryElite, circuitTier5,
					plateTier5, cableTier5, plateTier5,
					RECIPE_BufferCore_EV);
			UtilsRecipe.recipeBuilder(
					plateTier6, cableTier6, plateTier6,
					circuitTier6, batteryElite, circuitTier6,
					plateTier6, cableTier6, plateTier6,
					RECIPE_BufferCore_IV);
			UtilsRecipe.recipeBuilder(
					plateTier7, cableTier7, plateTier7,
					circuitTier7, batteryMaster, circuitTier7,
					plateTier7, cableTier7, plateTier7,
					RECIPE_BufferCore_LuV);
			UtilsRecipe.recipeBuilder(
					plateTier8, cableTier8, plateTier8,
					circuitTier8, batteryMaster, circuitTier8,
					plateTier8, cableTier8, plateTier8,
					RECIPE_BufferCore_ZPM);
			UtilsRecipe.recipeBuilder(
					plateTier9, cableTier9, plateTier9,
					circuitTier9, batteryUltimate, circuitTier9,
					plateTier9, cableTier9, plateTier9,
					RECIPE_BufferCore_UV);
			UtilsRecipe.recipeBuilder(
					plateTier10, cableTier10, plateTier10,
					circuitTier10, batteryUltimate, circuitTier10,
					plateTier10, cableTier10, plateTier10,
					RECIPE_BufferCore_MAX);

			
			UtilsRecipe.recipeBuilder(
					wireTier1, RECIPE_BufferCore_ULV, wireTier1,
					wireTier1, machineCasing_ULV, wireTier1,
					circuitPrimitive, circuitTier1, circuitPrimitive,
					RECIPE_Buffer_ULV);
			UtilsRecipe.recipeBuilder(
					wireTier2, RECIPE_BufferCore_LV, wireTier2,
					wireTier2, machineCasing_LV, wireTier2,
					circuitTier1, circuitTier2, circuitTier1,
					RECIPE_Buffer_LV);
			UtilsRecipe.recipeBuilder(
					wireTier3, RECIPE_BufferCore_MV, wireTier3,
					wireTier3, machineCasing_MV, wireTier3,
					circuitTier2, circuitTier3, circuitTier2,
					RECIPE_Buffer_MV);
			UtilsRecipe.recipeBuilder(
					wireTier4, RECIPE_BufferCore_HV, wireTier4,
					wireTier4, machineCasing_HV, wireTier4,
					circuitTier3, circuitTier4, circuitTier3,
					RECIPE_Buffer_HV);
			UtilsRecipe.recipeBuilder(
					wireTier5, RECIPE_BufferCore_EV, wireTier5,
					wireTier5, machineCasing_EV, wireTier5,
					circuitTier4, circuitTier5, circuitTier4,
					RECIPE_Buffer_EV);
			UtilsRecipe.recipeBuilder(
					wireTier6, RECIPE_BufferCore_IV, wireTier6,
					wireTier6, machineCasing_IV, wireTier6,
					circuitTier5, circuitTier6, circuitTier5,
					RECIPE_Buffer_IV);
			UtilsRecipe.recipeBuilder(
					wireTier7, RECIPE_BufferCore_LuV, wireTier7,
					wireTier7, machineCasing_LuV, wireTier7,
					circuitTier6, circuitTier7, circuitTier6,
					RECIPE_Buffer_LuV);
			UtilsRecipe.recipeBuilder(
					wireTier8, RECIPE_BufferCore_ZPM, wireTier8,
					wireTier8, machineCasing_ZPM, wireTier8,
					circuitTier7, circuitTier8, circuitTier7,
					RECIPE_Buffer_ZPM);
			UtilsRecipe.recipeBuilder(
					wireTier9, RECIPE_BufferCore_UV, wireTier9,
					wireTier9, machineCasing_UV, wireTier9,
					circuitTier8, circuitTier9, circuitTier8,
					RECIPE_Buffer_UV);
			UtilsRecipe.recipeBuilder(
					plateTier11, RECIPE_BufferCore_MAX, plateTier11,
					wireTier10, machineCasing_MAX, wireTier10,
					circuitTier9, circuitTier10, circuitTier9,
					RECIPE_Buffer_MAX);


			//Steam Condenser
			UtilsRecipe.recipeBuilder(
					pipeLargeCopper, pipeHugeSteel, pipeLargeCopper,
					plateEnergeticAlloy, electricPump_HV, plateEnergeticAlloy,
					plateEnergeticAlloy, pipeLargeCopper, plateEnergeticAlloy,
					RECIPE_SteamCondenser);

			//Iron BF
			UtilsRecipe.recipeBuilder(
					"plateDoubleAnyIron", "craftingFurnace", "plateDoubleAnyIron",
					boiler_Coal, machineCasing_ULV, boiler_Coal,
					"plateDoubleAnyIron", "bucketLava", "plateDoubleAnyIron",
					RECIPE_IronBlastFurnace);

			//Iron plated Bricks
			UtilsRecipe.recipeBuilder(
					"plateAnyIron", RECIPES_Tools.craftingToolHardHammer, "plateAnyIron",
					"plateAnyIron", blockBricks, "plateAnyIron",
					"plateAnyIron", RECIPES_Tools.craftingToolWrench, "plateAnyIron",
					RECIPE_IronPlatedBricks);

			//Industrial Centrifuge
			UtilsRecipe.recipeBuilder(
					circuitTier6, pipeHugeStainlessSteel, circuitTier6,
					plateTier6, electricPump_EV, plateTier6,
					plateTier8, machineCasing_EV, plateTier8,
					RECIPE_IndustrialCentrifugeController);

			//Centrifuge Casing
			UtilsRecipe.recipeBuilder(
					plateTier6, "stickElectrum", plateTier6,
					plateTier8, "stickElectrum", plateTier8,
					plateTier6, "stickElectrum", plateTier6,
					RECIPE_IndustrialCentrifugeCasing);

			//Industrial Coke Oven
			UtilsRecipe.recipeBuilder(
					plateCobalt, circuitTier3, plateCobalt,
					machineCasing_HV, INPUT_RCCokeOvenBlock, machineCasing_HV,
					plateCobalt, circuitTier3, plateCobalt,
					RECIPE_IndustrialCokeOvenController);

			//Coke Oven Frame Casing
			UtilsRecipe.recipeBuilder(
					plateTier8, rodTier8, plateTier8,
					rodTier8, "frameGtCobalt", rodTier8,
					plateTier8, rodTier8, plateTier8,
					RECIPE_IndustrialCokeOvenFrame);

			//Coke Oven Coil 1
			UtilsRecipe.recipeBuilder(
					plateBronze, plateBronze, plateBronze,
					"frameGtBronze", gearboxCasing_Tier_1, "frameGtBronze",
					plateBronze, plateBronze, plateBronze,
					RECIPE_IndustrialCokeOvenCasingA);

			//Coke Oven Coil 2
			UtilsRecipe.recipeBuilder(
					plateSteel, plateSteel, plateSteel,
					"frameGtSteel", gearboxCasing_Tier_2, "frameGtSteel",
					plateSteel, plateSteel, plateSteel,
					RECIPE_IndustrialCokeOvenCasingB);
		}


		Utils.LOG_INFO("Done loading recipes for the Various machine blocks.");
	}
}
