package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RECIPES_Machines {

	//Outputs
	//static ItemStack RECIPE_BufferCore_ULV = new ItemStack(GregtechEnergyBuffer.itemBufferCore);
	public static ItemStack RECIPE_SteamCondenser;
	public static ItemStack RECIPE_IronBlastFurnace;
	public static ItemStack RECIPE_IronPlatedBricks;
	public static ItemStack RECIPE_Buffer_ULV = GregtechItemList.Energy_Buffer_1by1_ULV.get(1);
	public static ItemStack RECIPE_Buffer_LV = GregtechItemList.Energy_Buffer_1by1_LV.get(1);
	public static ItemStack RECIPE_Buffer_MV = GregtechItemList.Energy_Buffer_1by1_MV.get(1);
	public static ItemStack RECIPE_Buffer_HV = GregtechItemList.Energy_Buffer_1by1_HV.get(1);
	public static ItemStack RECIPE_Buffer_EV = GregtechItemList.Energy_Buffer_1by1_EV.get(1);
	public static ItemStack RECIPE_Buffer_IV = GregtechItemList.Energy_Buffer_1by1_IV.get(1);
	public static ItemStack RECIPE_Buffer_LuV = GregtechItemList.Energy_Buffer_1by1_LuV.get(1);
	public static ItemStack RECIPE_Buffer_ZPM = GregtechItemList.Energy_Buffer_1by1_ZPM.get(1);
	public static ItemStack RECIPE_Buffer_UV = GregtechItemList.Energy_Buffer_1by1_UV.get(1);
	public static ItemStack RECIPE_Buffer_MAX = GregtechItemList.Energy_Buffer_1by1_MAX.get(1);
	//Industrial Centrifuge
	public static ItemStack RECIPE_IndustrialCentrifugeController;
	public static ItemStack RECIPE_IndustrialCentrifugeCasing;
	//Industrial Coke Oven
	public static ItemStack RECIPE_IndustrialCokeOvenController;
	public static ItemStack RECIPE_IndustrialCokeOvenFrame;
	public static ItemStack RECIPE_IndustrialCokeOvenCasingA;
	public static ItemStack RECIPE_IndustrialCokeOvenCasingB;
	//Industrial Electrolyzer
	public static ItemStack RECIPE_IndustrialElectrolyzerController;
	public static ItemStack RECIPE_IndustrialElectrolyzerFrame;
	//Industrial Material Press
	public static ItemStack RECIPE_IndustrialMaterialPressController;
	public static ItemStack RECIPE_IndustrialMaterialPressFrame;
	//Industrial Maceration Stack
	public static ItemStack RECIPE_IndustrialMacerationStackController;
	public static ItemStack RECIPE_IndustrialMacerationStackFrame;
	//Industrial Wire Factory
	public static ItemStack RECIPE_IndustrialWireFactoryController;
	public static ItemStack RECIPE_IndustrialWireFactoryFrame;
	//Industrial Multi Tank
	public static ItemStack RECIPE_IndustrialMultiTankController;
	public static ItemStack RECIPE_IndustrialMultiTankFrame;
	//Industrial Matter Fabricator
	public static ItemStack RECIPE_IndustrialMatterFabController;
	public static ItemStack RECIPE_IndustrialMatterFabFrame;
	public static ItemStack RECIPE_IndustrialMatterFabCoil;
	//Industrial Blast Smelter
	public static ItemStack RECIPE_IndustrialBlastSmelterController;
	public static ItemStack RECIPE_IndustrialBlastSmelterFrame;
	public static ItemStack RECIPE_IndustrialBlastSmelterCoil;
	//Industrial Sieve
	public static ItemStack RECIPE_IndustrialSieveController;
	public static ItemStack RECIPE_IndustrialSieveFrame;
	public static ItemStack RECIPE_IndustrialSieveGrate;
	//Industrial Tree Farmer
	public static ItemStack RECIPE_TreeFarmController;
	public static ItemStack RECIPE_TreeFarmFrame;


	//Buffer Cores
	public static ItemStack RECIPE_BufferCore_ULV = ItemUtils.getItemStack("miscutils:item.itemBufferCore1", 1);
	public static ItemStack RECIPE_BufferCore_LV = ItemUtils.getItemStack("miscutils:item.itemBufferCore2", 1);
	public static ItemStack RECIPE_BufferCore_MV = ItemUtils.getItemStack("miscutils:item.itemBufferCore3", 1);
	public static ItemStack RECIPE_BufferCore_HV = ItemUtils.getItemStack("miscutils:item.itemBufferCore4", 1);
	public static ItemStack RECIPE_BufferCore_EV = ItemUtils.getItemStack("miscutils:item.itemBufferCore5", 1);
	public static ItemStack RECIPE_BufferCore_IV = ItemUtils.getItemStack("miscutils:item.itemBufferCore6", 1);
	public static ItemStack RECIPE_BufferCore_LuV = ItemUtils.getItemStack("miscutils:item.itemBufferCore7", 1);
	public static ItemStack RECIPE_BufferCore_ZPM = ItemUtils.getItemStack("miscutils:item.itemBufferCore8", 1);
	public static ItemStack RECIPE_BufferCore_UV = ItemUtils.getItemStack("miscutils:item.itemBufferCore9", 1);
	public static ItemStack RECIPE_BufferCore_MAX = ItemUtils.getItemStack("miscutils:item.itemBufferCore10", 1);


	//Wire
	public static String wireTier1 = "wireGt08Lead";
	public static String wireTier2 = "wireGt08Tin";
	public static String wireTier3 = "wireGt08Copper";
	public static String wireTier4 = "wireGt08Gold";
	public static String wireTier5 = "wireGt08Aluminium";
	public static String wireTier6 = "wireGt08Tungsten";
	public static String wireTier7 = "wireGt08Naquadah";
	public static String wireTier8 = "wireGt08Osmium";
	public static String wireTier9 = "wireGt08Superconductor";
	public static String wireTier10 = "wireGt16Superconductor";

	//Wire
	public static String cableTier1 = "cableGt04Lead";
	public static String cableTier2 = "cableGt04Tin";
	public static String cableTier3 = "cableGt04Copper";
	public static String cableTier4 = "cableGt04Gold";
	public static String cableTier5 = "cableGt04Aluminium";
	public static String cableTier6 = "cableGt04Tungsten";
	public static String cableTier7 = "cableGt04Naquadah";
	public static String cableTier8 = "cableGt04Osmium";
	public static String cableTier9 = "cableGt04NiobiumTitanium";
	public static String cableTier10 = "cableGt08NiobiumTitanium";


	//Plates
	public static String plateTier1 = "plateLead";
	public static String plateTier2 = "plateTin";
	public static String plateTier3 = "plateCopper";
	public static String plateTier4 = "plateGold";
	public static String plateTier5 = "plateAluminium";
	public static String plateTier6 = "plateMaragingSteel250";
	public static String plateTier7 = "plateTantalloy61";
	public static String plateTier8 = "plateInconel792";
	public static String plateTier9 = "plateZeron100";
	public static String plateTier10 = "plateNaquadahEnriched";
	public static String plateTier11 = "plateNeutronium";

	//rods
	public static String rodTier1 = "stickLead";
	public static String rodTier2 = "stickTin";
	public static String rodTier3 = "stickCopper";
	public static String rodTier4 = "stickGold";
	public static String rodTier5 = "stickAluminium";
	public static String rodTier6 = "stickMaragingSteel250";
	public static String rodTier7 = "stickTantalloy61";
	public static String rodTier8 = "stickInconel792";
	public static String rodTier9 = "stickZeron100";
	public static String rodTier10 = "stickNaquadahEnriched";
	public static String rodTier11 = "stickNeutronium";

	public static String pipeTier1 = "pipeHuge"+"Clay";
	public static String pipeTier2 = "pipeHuge"+"Potin";
	public static String pipeTier3 = "pipeHuge"+"Steel";
	public static String pipeTier4 = "pipeHuge"+"StainlessSteel";
	public static String pipeTier5 = "pipeHuge"+"TungstenSteel";
	public static String pipeTier6 = "pipeHuge"+"MaragingSteel300";
	public static String pipeTier7 = "pipeHuge"+"Tantalloy60";
	public static String pipeTier8 = "pipeHuge"+"Tantalloy61";
	public static String pipeTier9 = "pipeHuge"+"Inconel792";
	public static String pipeTier10 = "pipeHuge"+"HastelloyX";
	public static String pipeTier11 = "pipeHuge"+"Europium";


	//Machine Casings
	public static ItemStack machineCasing_ULV;
	public static ItemStack machineCasing_LV;
	public static ItemStack machineCasing_MV;
	public static ItemStack machineCasing_HV;
	public static ItemStack machineCasing_EV;
	public static ItemStack machineCasing_IV;
	public static ItemStack machineCasing_LuV;
	public static ItemStack machineCasing_ZPM;
	public static ItemStack machineCasing_UV;
	public static ItemStack machineCasing_MAX;

	//Gearbox Casings
	public static ItemStack gearboxCasing_Tier_1;
	public static ItemStack gearboxCasing_Tier_2;
	public static ItemStack gearboxCasing_Tier_3;
	public static ItemStack gearboxCasing_Tier_4;

	//IV MACHINES
	public static ItemStack IV_MACHINE_Electrolyzer;
	public static ItemStack IV_MACHINE_Centrifuge;
	public static ItemStack IV_MACHINE_BendingMachine;
	public static ItemStack IV_MACHINE_Wiremill;
	public static ItemStack EV_MACHINE_Macerator;
	public static ItemStack IV_MACHINE_Macerator;
	public static ItemStack IV_MACHINE_MassFabricator;


	//Cables
	public static String cableGt02Electrum = "cableGt02Electrum";


	//Plates
	public static String plateElectricalSteel= "plateElectricalSteel";
	public static String plateEnergeticAlloy= "plateEnergeticAlloy";
	public static String plateCobalt = "plateCobalt";
	public static String plateBronze = "plateBronze";
	public static String plateSteel = "plateSteel";

	//Pipes
	public static String pipeLargeCopper="pipeLargeCopper";
	public static String pipeHugeSteel="pipeHugeSteel";
	public static String pipeHugeStainlessSteel="pipeHugeStainlessSteel";
	public static String pipeHugeTitanium="pipeHugeTitanium";

	//Lava Boiler
	public static ItemStack boiler_Coal;
	public static ItemStack blockBricks = ItemUtils.getItemStack("minecraft:brick_block", 1);

	//Batteries
	public static String batteryBasic = "batteryBasic";
	public static String batteryAdvanced = "batteryAdvanced";
	public static String batteryElite = "batteryElite";
	public static String batteryMaster = "batteryMaster";
	public static String batteryUltimate = "batteryUltimate";
	public static ItemStack IC2MFE;
	public static ItemStack IC2MFSU;

	//Machine Components
	public static ItemStack electricMotor_LV;
	public static ItemStack electricMotor_MV;
	public static ItemStack electricMotor_HV;
	public static ItemStack electricMotor_EV;
	public static ItemStack electricMotor_IV;
	public static ItemStack electricPump_LV;
	public static ItemStack electricPump_MV;
	public static ItemStack electricPump_HV;
	public static ItemStack electricPump_EV;
	public static ItemStack electricPump_IV;
	public static ItemStack electricPiston_LV;
	public static ItemStack electricPiston_MV;
	public static ItemStack electricPiston_HV;
	public static ItemStack electricPiston_EV;
	public static ItemStack electricPiston_IV;
	public static ItemStack robotArm_LV;
	public static ItemStack robotArm_MV;
	public static ItemStack robotArm_HV;
	public static ItemStack robotArm_EV;
	public static ItemStack robotArm_IV;
	public static ItemStack conveyorModule_LV;
	public static ItemStack conveyorModule_MV;
	public static ItemStack conveyorModule_HV;
	public static ItemStack conveyorModule_EV;
	public static ItemStack conveyorModule_IV;
	public static ItemStack emitter_LV;
	public static ItemStack emitter_MV;
	public static ItemStack emitter_HV;
	public static ItemStack emitter_EV;
	public static ItemStack emitter_IV;
	public static ItemStack fieldGenerator_LV;
	public static ItemStack fieldGenerator_MV;
	public static ItemStack fieldGenerator_HV;
	public static ItemStack fieldGenerator_EV;
	public static ItemStack fieldGenerator_IV;
	public static ItemStack sensor_LV;
	public static ItemStack sensor_MV;
	public static ItemStack sensor_HV;
	public static ItemStack sensor_EV;
	public static ItemStack sensor_IV;

	//Misc
	public static ItemStack INPUT_RCCokeOvenBlock;
	public static ItemStack INPUT_IECokeOvenBlock;



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
			IC2MFE = ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:blockElectric", "IC2_MFE", 1, 1);
			IC2MFSU = ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:blockElectric", "IC2_MFSU", 2, 1);
		}
		if (LoadedMods.Gregtech){
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
			EV_MACHINE_Macerator= ItemList.Machine_EV_Macerator.get(1);
			IV_MACHINE_Macerator= ItemList.Machine_IV_Macerator.get(1);
			IV_MACHINE_MassFabricator= ItemList.Machine_IV_Massfab.get(1);
			IV_MACHINE_Centrifuge= ItemList.Machine_IV_Centrifuge.get(1);


		}
		if (CORE.configSwitches.enableMultiblock_IndustrialCokeOven){
			if(LoadedMods.Railcraft){
				//Misc
				INPUT_RCCokeOvenBlock = ItemUtils.getItemStackWithMeta(LoadedMods.Railcraft, "Railcraft:machine.alpha", "Coke_Oven_RC", 7, 1);
			}
			if(LoadedMods.ImmersiveEngineering){
				//Misc
				INPUT_IECokeOvenBlock = ItemUtils.getItemStackWithMeta(LoadedMods.ImmersiveEngineering, "ImmersiveEngineering:stoneDecoration", "Coke_Oven_IE", 1, 1);
			}
		}
		runModRecipes();
	}

	private static void runModRecipes(){
		if (LoadedMods.Gregtech){

			RecipeUtils.addShapedGregtechRecipe(
			ItemList.Electric_Piston_EV, OrePrefixes.circuit.get(Materials.Ultimate), ItemList.Electric_Piston_EV,
			ItemList.Electric_Motor_EV, machineCasing_EV, ItemList.Electric_Motor_EV,
			"gearGtTitanium", "cableGt02Aluminium", "gearGtTitanium",
			ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 793, 1));
			RecipeUtils.addShapedGregtechRecipe(
			ItemList.Electric_Piston_IV, OrePrefixes.circuit.get(Materials.Superconductor), ItemList.Electric_Piston_IV,
			ItemList.Electric_Motor_IV, machineCasing_IV, ItemList.Electric_Motor_IV,
			"gearGtTungstenSteel", "cableGt02Platinum", "gearGtTungstenSteel",
			ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 794, 1));
			RecipeUtils.addShapedGregtechRecipe(
			CI.electricPiston_LuV, OrePrefixes.circuit.get(Materials.Infinite), CI.electricPiston_LuV,
			CI.electricMotor_LuV, machineCasing_LuV, CI.electricMotor_LuV,
			"gearGtChrome", "cableGt02Tungsten", "gearGtChrome",
			ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 795, 1));

			//Buffer Core
			RecipeUtils.addShapedGregtechRecipe(
			plateTier1, cableTier1, plateTier1,
			CI.circuitPrimitive, IC2MFE, CI.circuitPrimitive,
			plateTier1, cableTier1, plateTier1,
			RECIPE_BufferCore_ULV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier2, cableTier2, plateTier2,
			CI.circuitTier1, IC2MFE, CI.circuitTier1,
			plateTier2, cableTier2, plateTier2,
			RECIPE_BufferCore_LV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier3, cableTier3, plateTier3,
			RECIPE_BufferCore_LV, CI.circuitTier2, RECIPE_BufferCore_LV,
			plateTier3, cableTier3, plateTier3,
			RECIPE_BufferCore_MV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier4, cableTier4, plateTier4,
			RECIPE_BufferCore_MV, CI.circuitTier3, RECIPE_BufferCore_MV,
			plateTier4, cableTier4, plateTier4,
			RECIPE_BufferCore_HV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier5, cableTier5, plateTier5,
			RECIPE_BufferCore_HV, CI.circuitTier4, RECIPE_BufferCore_HV,
			plateTier5, cableTier5, plateTier5,
			RECIPE_BufferCore_EV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier6, cableTier6, plateTier6,
			RECIPE_BufferCore_EV, CI.circuitTier5, RECIPE_BufferCore_EV,
			plateTier6, cableTier6, plateTier6,
			RECIPE_BufferCore_IV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier7, cableTier7, plateTier7,
			RECIPE_BufferCore_IV, CI.circuitTier6, RECIPE_BufferCore_IV,
			plateTier7, cableTier7, plateTier7,
			RECIPE_BufferCore_LuV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier8, cableTier8, plateTier8,
			RECIPE_BufferCore_LuV, CI.circuitTier7, RECIPE_BufferCore_LuV,
			plateTier8, cableTier8, plateTier8,
			RECIPE_BufferCore_ZPM);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier9, cableTier9, plateTier9,
			RECIPE_BufferCore_ZPM, CI.circuitTier8, RECIPE_BufferCore_ZPM,
			plateTier9, cableTier9, plateTier9,
			RECIPE_BufferCore_UV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier10, cableTier10, plateTier10,
			RECIPE_BufferCore_UV, CI.circuitTier9, RECIPE_BufferCore_UV,
			plateTier10, cableTier10, plateTier10,
			RECIPE_BufferCore_MAX);


			RecipeUtils.addShapedGregtechRecipe(
			wireTier1, RECIPE_BufferCore_ULV, wireTier1,
			wireTier1, machineCasing_ULV, wireTier1,
			CI.circuitPrimitive, CI.circuitTier1, CI.circuitPrimitive,
			RECIPE_Buffer_ULV);
			RecipeUtils.addShapedGregtechRecipe(
			wireTier2, RECIPE_BufferCore_LV, wireTier2,
			wireTier2, machineCasing_LV, wireTier2,
			CI.circuitTier1, RECIPE_BufferCore_LV, CI.circuitTier1,
			RECIPE_Buffer_LV);
			RecipeUtils.addShapedGregtechRecipe(
			wireTier3, RECIPE_BufferCore_MV, wireTier3,
			wireTier3, machineCasing_MV, wireTier3,
			CI.circuitTier2, RECIPE_BufferCore_MV, CI.circuitTier2,
			RECIPE_Buffer_MV);
			RecipeUtils.addShapedGregtechRecipe(
			wireTier4, RECIPE_BufferCore_HV, wireTier4,
			wireTier4, machineCasing_HV, wireTier4,
			CI.circuitTier3, RECIPE_BufferCore_HV, CI.circuitTier3,
			RECIPE_Buffer_HV);
			RecipeUtils.addShapedGregtechRecipe(
			wireTier5, RECIPE_BufferCore_EV, wireTier5,
			wireTier5, machineCasing_EV, wireTier5,
			CI.circuitTier4, RECIPE_BufferCore_EV, CI.circuitTier4,
			RECIPE_Buffer_EV);
			RecipeUtils.addShapedGregtechRecipe(
			wireTier6, RECIPE_BufferCore_IV, wireTier6,
			wireTier6, machineCasing_IV, wireTier6,
			CI.circuitTier5, RECIPE_BufferCore_IV, CI.circuitTier5,
			RECIPE_Buffer_IV);
			RecipeUtils.addShapedGregtechRecipe(
			wireTier7, RECIPE_BufferCore_LuV, wireTier7,
			wireTier7, machineCasing_LuV, wireTier7,
			CI.circuitTier6, RECIPE_BufferCore_LuV, CI.circuitTier6,
			RECIPE_Buffer_LuV);
			RecipeUtils.addShapedGregtechRecipe(
			wireTier8, RECIPE_BufferCore_ZPM, wireTier8,
			wireTier8, machineCasing_ZPM, wireTier8,
			CI.circuitTier7, RECIPE_BufferCore_ZPM, CI.circuitTier7,
			RECIPE_Buffer_ZPM);
			RecipeUtils.addShapedGregtechRecipe(
			wireTier9, RECIPE_BufferCore_UV, wireTier9,
			wireTier9, machineCasing_UV, wireTier9,
			CI.circuitTier8, RECIPE_BufferCore_UV, CI.circuitTier8,
			RECIPE_Buffer_UV);
			RecipeUtils.addShapedGregtechRecipe(
			plateTier11, RECIPE_BufferCore_MAX, plateTier11,
			wireTier10, machineCasing_MAX, wireTier10,
			CI.circuitTier9, RECIPE_BufferCore_MAX, CI.circuitTier9,
			RECIPE_Buffer_MAX);

			//Steam Condenser
			if (CORE.configSwitches.enableMachine_SteamConverter ){
				RECIPE_SteamCondenser = GregtechItemList.Condensor_MAX.get(1);
				RecipeUtils.addShapedGregtechRecipe(
				pipeLargeCopper, pipeHugeSteel, pipeLargeCopper,
				plateEnergeticAlloy, electricPump_HV, plateEnergeticAlloy,
				plateEnergeticAlloy, pipeLargeCopper, plateEnergeticAlloy,
				RECIPE_SteamCondenser);
			}

			
			if (CORE.configSwitches.enableMultiblock_IronBlastFurnace){
				
				RECIPE_IronBlastFurnace = GregtechItemList.Machine_Iron_BlastFurnace.get(1);
				RECIPE_IronPlatedBricks = GregtechItemList.Casing_IronPlatedBricks.get(1);

				//Iron BF
				RecipeUtils.addShapedGregtechRecipe(
				"plateDoubleAnyIron", "craftingFurnace", "plateDoubleAnyIron",
				boiler_Coal, machineCasing_ULV, boiler_Coal,
				"plateDoubleAnyIron", "bucketLava", "plateDoubleAnyIron",
				RECIPE_IronBlastFurnace);
				//Iron plated Bricks
				RecipeUtils.addShapedGregtechRecipe(
				"plateAnyIron", RECIPES_Tools.craftingToolHardHammer, "plateAnyIron",
				"plateAnyIron", blockBricks, "plateAnyIron",
				"plateAnyIron", RECIPES_Tools.craftingToolWrench, "plateAnyIron",
				RECIPE_IronPlatedBricks);

				//Add recycle recipes for the Iron Plated Bricks
				//GT_ModHandler.addPulverisationRecipe(RECIPE_IronPlatedBricks, ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustIron", 6), ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustClay", 2), true);
				RECIPES_GREGTECH.addPulverisationRecipe(
				RECIPE_IronPlatedBricks,
				ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustIron", 6),
				ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustClay", 2),
				null);
				GT_Values.RA.addArcFurnaceRecipe(RECIPE_IronPlatedBricks, new ItemStack[]{ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotWroughtIron", 6), ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustAsh", 2)}, new int[]{0}, 32*20, 32);
			}

			if (CORE.configSwitches.enableMultiblock_IndustrialCentrifuge){
				//Industrial Centrifuge
				RECIPE_IndustrialCentrifugeController = GregtechItemList.Industrial_Centrifuge.get(1);
				RECIPE_IndustrialCentrifugeCasing = GregtechItemList.Casing_Centrifuge1.get(1);

				//Industrial Centrifuge
				RecipeUtils.addShapedGregtechRecipe(
				CI.circuitTier6, pipeHugeStainlessSteel, CI.circuitTier6,
				plateTier6, IV_MACHINE_Centrifuge, plateTier6,
				plateTier8, machineCasing_IV, plateTier8,
				RECIPE_IndustrialCentrifugeController);
				//Centrifuge Casing
				RecipeUtils.addShapedGregtechRecipe(
				plateTier6, "stickTumbaga", plateTier6,
				plateTier8, "stickTumbaga", plateTier8,
				plateTier6, "stickTumbaga", plateTier6,
				RECIPE_IndustrialCentrifugeCasing);
			}

			if (CORE.configSwitches.enableMultiblock_IndustrialCokeOven){
				//Industrial Coke Oven
				RECIPE_IndustrialCokeOvenController = GregtechItemList.Industrial_CokeOven.get(1);
				RECIPE_IndustrialCokeOvenFrame = GregtechItemList.Casing_CokeOven.get(1);
				RECIPE_IndustrialCokeOvenCasingA = GregtechItemList.Casing_CokeOven_Coil1.get(1);
				RECIPE_IndustrialCokeOvenCasingB = GregtechItemList.Casing_CokeOven_Coil2.get(1);

				if (LoadedMods.Railcraft){
					//Industrial Coke Oven
					RecipeUtils.addShapedGregtechRecipe(
					plateCobalt, CI.circuitTier4, plateCobalt,
					machineCasing_HV, INPUT_RCCokeOvenBlock, machineCasing_HV,
					plateCobalt, CI.circuitTier5, plateCobalt,
					RECIPE_IndustrialCokeOvenController);
				}
				if (LoadedMods.ImmersiveEngineering){
					//Industrial Coke Oven
					RecipeUtils.addShapedGregtechRecipe(
					plateTier8, CI.circuitTier4, plateTier8,
					machineCasing_HV, INPUT_IECokeOvenBlock, machineCasing_HV,
					plateTier8, CI.circuitTier3, plateTier8,
					RECIPE_IndustrialCokeOvenController);
				}
				//Coke Oven Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
				plateTier7, rodTier7, plateTier7,
				rodTier7, "frameGtTantalloy61", rodTier7,
				plateTier7, rodTier7, plateTier7,
				RECIPE_IndustrialCokeOvenFrame);
				//Coke Oven Coil 1
				RecipeUtils.addShapedGregtechRecipe(
				plateBronze, plateBronze, plateBronze,
				"frameGtBronze", gearboxCasing_Tier_1, "frameGtBronze",
				plateBronze, plateBronze, plateBronze,
				RECIPE_IndustrialCokeOvenCasingA);
				//Coke Oven Coil 2
				RecipeUtils.addShapedGregtechRecipe(
				plateSteel, plateSteel, plateSteel,
				"frameGtSteel", gearboxCasing_Tier_2, "frameGtSteel",
				plateSteel, plateSteel, plateSteel,
				RECIPE_IndustrialCokeOvenCasingB);
			}

			if (CORE.configSwitches.enableMultiblock_IndustrialElectrolyzer){
				//Industrial Electrolyzer
				RECIPE_IndustrialElectrolyzerController = GregtechItemList.Industrial_Electrolyzer.get(1);
				RECIPE_IndustrialElectrolyzerFrame = GregtechItemList.Casing_Electrolyzer.get(1);

				//Electrolyzer Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
				"platePotin", "stickLongChrome", "platePotin",
				"stickLongPotin", "frameGtPotin", "stickLongPotin",
				"platePotin", "stickLongPotin", "platePotin",
				RECIPE_IndustrialElectrolyzerFrame);
				//Industrial Electrolyzer
				RecipeUtils.addShapedGregtechRecipe(
				"plateStellite", CI.circuitTier6, "plateStellite",
				machineCasing_EV, IV_MACHINE_Electrolyzer, machineCasing_EV,
				"plateStellite", "rotorStellite", "plateStellite",
				RECIPE_IndustrialElectrolyzerController);
			}

			if (CORE.configSwitches.enableMultiblock_IndustrialPlatePress){
				//Industrial Material Press
				RECIPE_IndustrialMaterialPressController = GregtechItemList.Industrial_PlatePress.get(1);
				RECIPE_IndustrialMaterialPressFrame = GregtechItemList.Casing_MaterialPress.get(1);

				//Material Press Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
				"plateTitanium", "stickLongTumbaga", "plateTitanium",
				"stickTantalloy60", "frameGtTumbaga", "stickTantalloy60",
				"plateTitanium", "stickLongTumbaga", "plateTitanium",
				RECIPE_IndustrialMaterialPressFrame);
				//Industrial Material Press
				RecipeUtils.addShapedGregtechRecipe(
				"plateTitanium", CI.circuitTier5, "plateTitanium",
				machineCasing_EV, IV_MACHINE_BendingMachine, machineCasing_EV,
				"plateTitanium", CI.circuitTier5, "plateTitanium",
				RECIPE_IndustrialMaterialPressController);
			}

			if (CORE.configSwitches.enableMultiblock_IndustrialMacerationStack){
				//Industrial Maceration Stack
				RECIPE_IndustrialMacerationStackController = GregtechItemList.Industrial_MacerationStack.get(1);
				RECIPE_IndustrialMacerationStackFrame = GregtechItemList.Casing_MacerationStack.get(1);

				//Maceration Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
				"platePalladium", "platePalladium", "platePalladium",
				"stickPlatinum", "frameGtInconel625", "stickPlatinum",
				"platePalladium", "stickLongPalladium", "platePalladium",
				RECIPE_IndustrialMacerationStackFrame);
				//Industrial Maceration stack
				RecipeUtils.addShapedGregtechRecipe(
				"plateTungstenCarbide", IV_MACHINE_Macerator, "plateTungstenCarbide",
				EV_MACHINE_Macerator, CI.circuitTier7, EV_MACHINE_Macerator,
				"plateTungstenCarbide", machineCasing_IV, "plateTungstenCarbide",
				RECIPE_IndustrialMacerationStackController);
			}

			if (CORE.configSwitches.enableMultiblock_IndustrialWireMill){
				//Industrial Wire Factory
				RECIPE_IndustrialWireFactoryController = GregtechItemList.Industrial_WireFactory.get(1);
				RECIPE_IndustrialWireFactoryFrame = GregtechItemList.Casing_WireFactory.get(1);

				//Wire Factory Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
				"plateBlueSteel", "stickBlueSteel", "plateBlueSteel",
				"stickBlueSteel", "frameGtBlueSteel", "stickBlueSteel",
				"plateBlueSteel", "stickBlueSteel", "plateBlueSteel",
				RECIPE_IndustrialWireFactoryFrame);
				//Industrial Wire Factory
				RecipeUtils.addShapedGregtechRecipe(
				"plateZeron100", machineCasing_IV, "plateZeron100",
				CI.circuitTier6, IV_MACHINE_Wiremill, CI.circuitTier6,
				"plateZeron100", machineCasing_IV, "plateZeron100",
				RECIPE_IndustrialWireFactoryController);
			}



			//Tiered Tanks
			if (CORE.configSwitches.enableMachine_FluidTanks){
				Utils.LOG_INFO("Is New Horizons Loaded? "+CORE.GTNH);
				if (!CORE.GTNH){
					RecipeUtils.addShapedGregtechRecipe(
					plateTier1, plateTier1, plateTier1,
					plateTier1, pipeTier1, plateTier1,
					plateTier1, GregtechItemList.Fluid_Cell_144L.get(1), plateTier1,
					GregtechItemList.GT_FluidTank_ULV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier2, plateTier2, plateTier2,
					plateTier2, pipeTier2, plateTier2,
					plateTier2, electricPump_LV, plateTier2,
					GregtechItemList.GT_FluidTank_LV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier3, plateTier3, plateTier3,
					plateTier3, pipeTier3, plateTier3,
					plateTier3, electricPump_MV, plateTier3,
					GregtechItemList.GT_FluidTank_MV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier4, plateTier4, plateTier4,
					plateTier4, pipeTier4, plateTier4,
					plateTier4, electricPump_HV, plateTier4,
					GregtechItemList.GT_FluidTank_HV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier5, plateTier5, plateTier5,
					plateTier5, pipeTier5, plateTier5,
					plateTier5, electricPump_EV, plateTier5,
					GregtechItemList.GT_FluidTank_EV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier6, plateTier6, plateTier6,
					plateTier6, pipeTier6, plateTier6,
					plateTier6, electricPump_IV, plateTier6,
					GregtechItemList.GT_FluidTank_IV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier7, plateTier7, plateTier7,
					plateTier7, pipeTier7, plateTier7,
					plateTier7, CI.electricPump_LuV, plateTier7,
					GregtechItemList.GT_FluidTank_LuV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier8, plateTier8, plateTier8,
					plateTier8, pipeTier8, plateTier8,
					plateTier8, CI.electricPump_ZPM, plateTier8,
					GregtechItemList.GT_FluidTank_ZPM.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier9, plateTier9, plateTier9,
					plateTier9, pipeTier9, plateTier9,
					plateTier9, CI.electricPump_UV, plateTier9,
					GregtechItemList.GT_FluidTank_UV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier10, plateTier10, plateTier10,
					plateTier10, pipeTier10, plateTier10,
					plateTier10, CI.electricPump_MAX, plateTier10,
					GregtechItemList.GT_FluidTank_MAX.get(1));
				}
				else {

					plateTier1 = "plateTin";
					pipeTier1 = "pipeLargeClay";
					CI.circuitTier1 = ItemList.Circuit_Primitive.get(1);
					plateTier2 = "plateCopper";
					pipeTier2 = "pipeHugeClay";
					plateTier3 = "plateBronze";
					pipeTier3 = "pipeMediumBronze";
					plateTier4 = "plateIron";
					pipeTier4 = "pipeMediumSteel";
					plateTier5 = "plateSteel";
					plateTier6 = "plateRedstone";
					plateTier7 = "plateRedstoneAlloy";
					plateTier8 = "plateDarkSteel";
					ItemStack waterBucket = ItemUtils.getSimpleStack(Items.water_bucket);


					Utils.LOG_INFO("Loading New Horizon Tanks, with custom recipes.");
					Utils.LOG_INFO("Using "+plateTier1+" with "+pipeTier1);
					Utils.LOG_INFO("Using "+plateTier2+" with "+pipeTier2);
					Utils.LOG_INFO("Using " + plateTier3 + " with " + pipeTier3);
					Utils.LOG_INFO("Using " + plateTier4 + " with " + pipeTier4);

					RecipeUtils.addShapedGregtechRecipe(
					plateTier1, plateTier4, plateTier1,
					plateTier1, pipeTier1, plateTier1,
					plateTier1, waterBucket, plateTier1,
					GregtechItemList.GT_FluidTank_ULV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier2, plateTier5, plateTier2,
					plateTier2, pipeTier2, plateTier2,
					plateTier2, electricPump_LV, plateTier2,
					GregtechItemList.GT_FluidTank_LV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					plateTier6, plateTier7, plateTier6,
					plateTier3, pipeTier3, plateTier3,
					plateTier6, electricPump_LV, plateTier6,
					GregtechItemList.GT_FluidTank_MV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
					CI.circuitTier1, plateTier8, CI.circuitTier1,
					plateTier4, pipeTier4, plateTier4,
					CI.circuitTier1, electricPump_LV, CI.circuitTier1,
					GregtechItemList.GT_FluidTank_HV.get(1));
				}
			}

			if (CORE.configSwitches.enableMultiblock_MultiTank){
				//Industrial Multi Tank
				RECIPE_IndustrialMultiTankController = GregtechItemList.Industrial_MultiTank.get(1);
				RECIPE_IndustrialMultiTankFrame = GregtechItemList.Casing_MultitankExterior.get(1);

				//Industrial Multi Tank Casing
				RecipeUtils.addShapedGregtechRecipe(
				"stickGrisium", "plateGrisium", "stickGrisium",
				"plateGrisium", "frameGtGrisium", "plateGrisium",
				"plateGrisium", "plateGrisium", "plateGrisium",
				RECIPE_IndustrialMultiTankFrame);
				//Industrial Multi Tank
				RecipeUtils.addShapedGregtechRecipe(
				"pipeHugeTantalloy60", "gearGrisium", "pipeHugeTantalloy60",
				CI.circuitTier4, RECIPE_IndustrialMultiTankFrame, CI.circuitTier4,
				"plateDoubleGrisium", "rotorGrisium", "plateDoubleGrisium",
				RECIPE_IndustrialMultiTankController);
			}

			if (CORE.configSwitches.enableMultiblock_AlloyBlastSmelter){
				//Industrial Blast Smelter
				RECIPE_IndustrialBlastSmelterController = GregtechItemList.Industrial_AlloyBlastSmelter.get(1);
				RECIPE_IndustrialBlastSmelterFrame = GregtechItemList.Casing_BlastSmelter.get(1);
				RECIPE_IndustrialBlastSmelterCoil = GregtechItemList.Casing_Coil_BlastSmelter.get(1);

				//Blast Smelter
				RecipeUtils.addShapedGregtechRecipe(
				"plateZirconiumCarbide", CI.circuitTier4, "plateZirconiumCarbide",
				cableTier4, machineCasing_EV, cableTier4,
				"plateZirconiumCarbide", CI.circuitTier3, "plateZirconiumCarbide",
				RECIPE_IndustrialBlastSmelterController);
				//Blast Smelter Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
				"plateZirconiumCarbide", rodTier5, "plateZirconiumCarbide",
				rodTier5, "frameGtTumbaga", rodTier5,
				"plateZirconiumCarbide", rodTier5, "plateZirconiumCarbide",
				RECIPE_IndustrialBlastSmelterFrame);
				//Blast Smelter Coil
				RecipeUtils.addShapedGregtechRecipe(
				"plateStaballoy", "plateStaballoy", "plateStaballoy",
				"frameGtStaballoy", gearboxCasing_Tier_3, "frameGtStaballoy",
				"plateStaballoy", "plateStaballoy", "plateStaballoy",
				RECIPE_IndustrialBlastSmelterCoil);
			}

			if (CORE.configSwitches.enableMultiblock_MatterFabricator){
				//Industrial Matter Fabricator
				RECIPE_IndustrialMatterFabController = GregtechItemList.Industrial_MassFab.get(1);
				RECIPE_IndustrialMatterFabFrame = GregtechItemList.Casing_MatterFab.get(1);
				RECIPE_IndustrialMatterFabCoil = GregtechItemList.Casing_MatterGen.get(1);

				//Matter Fabricator CPU
				RecipeUtils.addShapedGregtechRecipe(
				"plateDoubleQuantum", CI.circuitTier5, "plateDoubleQuantum",
				cableTier8, machineCasing_LuV, cableTier8,
				"plateDoubleQuantum", CI.circuitTier5, "plateDoubleQuantum",
				RECIPE_IndustrialMatterFabController);
				//Matter Fabricator Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
				"plateNiobiumCarbide", rodTier8, "plateNiobiumCarbide",
				rodTier8, "frameGtInconel690", rodTier8,
				"plateNiobiumCarbide", rodTier8, "plateNiobiumCarbide",
				RECIPE_IndustrialMatterFabFrame);
				//Matter Fabricator Coil
				RecipeUtils.addShapedGregtechRecipe(
				"plateQuantum", "plateQuantum", "plateQuantum",
				"frameGtStellite", machineCasing_UV, "frameGtStellite",
				"plateQuantum", "plateQuantum", "plateQuantum",
				RECIPE_IndustrialMatterFabCoil);
			}

			if (CORE.configSwitches.enableMultiblock_IndustrialSifter){
				//Industrial Sieve
				RECIPE_IndustrialSieveController = GregtechItemList.Industrial_Sifter.get(1);
				RECIPE_IndustrialSieveFrame = GregtechItemList.Casing_Sifter.get(1);
				RECIPE_IndustrialSieveGrate = GregtechItemList.Casing_SifterGrate.get(1);

				//Industrial Sieve
				RecipeUtils.addShapedGregtechRecipe(
				"plateEglinSteel", CI.circuitTier2, "plateEglinSteel",
				cableTier3, machineCasing_MV, cableTier3,
				"plateEglinSteel", CI.circuitTier2, "plateEglinSteel",
				RECIPE_IndustrialSieveController);
				//Industrial Sieve Casing
				RecipeUtils.addShapedGregtechRecipe(
				"plateEglinSteel", "plateEglinSteel", "plateEglinSteel",
				"plateEglinSteel", "frameGtTumbaga", "plateEglinSteel",
				"plateEglinSteel", "plateEglinSteel", "plateEglinSteel",
				RECIPE_IndustrialSieveFrame);
				//Industrial Sieve Grate
				RecipeUtils.addShapedGregtechRecipe(
				"frameGtEglinSteel", "wireFineSteel", "frameGtEglinSteel",
				"wireFineSteel", "wireFineSteel", "wireFineSteel",
				"frameGtEglinSteel", "wireFineSteel", "frameGtEglinSteel",
				RECIPE_IndustrialSieveGrate);
			}

			if (CORE.configSwitches.enableMultiblock_TreeFarmer){
				//Industrial Tree Farmer
				RECIPE_TreeFarmController = GregtechItemList.Industrial_TreeFarm.get(1);
				RECIPE_TreeFarmFrame = GregtechItemList.TreeFarmer_Structural.get(1);
				//Industrial Tree Farm Controller
				RecipeUtils.addShapedGregtechRecipe(
				"plateEglinSteel", "rotorEglinSteel", "plateEglinSteel",
				"cableGt02Steel", "pipeMediumSteel", "cableGt02Steel",
				"plateEglinSteel", machineCasing_MV, "plateEglinSteel",
				RECIPE_TreeFarmController);
				//Industrial Tree Farm Frame
				RecipeUtils.addShapedGregtechRecipe(
				ItemUtils.getSimpleStack(Blocks.dirt), ItemUtils.getSimpleStack(Blocks.dirt), ItemUtils.getSimpleStack(Blocks.dirt),
				"plankWood", "frameGtTumbaga", "plankWood",
				"plankWood", "plankWood", "plankWood",
				RECIPE_TreeFarmFrame);
			}

		}


		Utils.LOG_INFO("Done loading recipes for the Various machine blocks.");
	}
}