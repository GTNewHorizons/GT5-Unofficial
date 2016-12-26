package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.*;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
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
	//Industrial Electrolyzer
	static ItemStack RECIPE_IndustrialElectrolyzerController = GregtechItemList.Industrial_Electrolyzer.get(1);
	static ItemStack RECIPE_IndustrialElectrolyzerFrame = GregtechItemList.Casing_Electrolyzer.get(1);
	//Industrial Material Press
	static ItemStack RECIPE_IndustrialMaterialPressController = GregtechItemList.Industrial_PlatePress.get(1);
	static ItemStack RECIPE_IndustrialMaterialPressFrame = GregtechItemList.Casing_MaterialPress.get(1);
	//Industrial Maceration Stack
	static ItemStack RECIPE_IndustrialMacerationStackController = GregtechItemList.Industrial_MacerationStack.get(1);
	static ItemStack RECIPE_IndustrialMacerationStackFrame = GregtechItemList.Casing_MacerationStack.get(1);
	//Industrial Wire Factory
	static ItemStack RECIPE_IndustrialWireFactoryController = GregtechItemList.Industrial_WireFactory.get(1);
	static ItemStack RECIPE_IndustrialWireFactoryFrame = GregtechItemList.Casing_WireFactory.get(1);
	//Industrial Multi Tank
	static ItemStack RECIPE_IndustrialMultiTankController = GregtechItemList.Industrial_MultiTank.get(1);
	static ItemStack RECIPE_IndustrialMultiTankFrame = GregtechItemList.Casing_MultitankExterior.get(1);
	//Industrial Matter Fabricator
	static ItemStack RECIPE_IndustrialMatterFabController = GregtechItemList.Industrial_MassFab.get(1);
	static ItemStack RECIPE_IndustrialMatterFabFrame = GregtechItemList.Casing_MatterFab.get(1);
	static ItemStack RECIPE_IndustrialMatterFabCoil = GregtechItemList.Casing_MatterGen.get(1);
	//Industrial Blast Smelter
	static ItemStack RECIPE_IndustrialBlastSmelterController = GregtechItemList.Industrial_AlloyBlastSmelter.get(1);
	static ItemStack RECIPE_IndustrialBlastSmelterFrame = GregtechItemList.Casing_BlastSmelter.get(1);
	static ItemStack RECIPE_IndustrialBlastSmelterCoil = GregtechItemList.Casing_Coil_BlastSmelter.get(1);


	//Buffer Cores
	static ItemStack RECIPE_BufferCore_ULV = ItemUtils.getItemStack("miscutils:item.itemBufferCore1", 1);
	static ItemStack RECIPE_BufferCore_LV = ItemUtils.getItemStack("miscutils:item.itemBufferCore2", 1);
	static ItemStack RECIPE_BufferCore_MV = ItemUtils.getItemStack("miscutils:item.itemBufferCore3", 1);
	static ItemStack RECIPE_BufferCore_HV = ItemUtils.getItemStack("miscutils:item.itemBufferCore4", 1);
	static ItemStack RECIPE_BufferCore_EV = ItemUtils.getItemStack("miscutils:item.itemBufferCore5", 1);
	static ItemStack RECIPE_BufferCore_IV = ItemUtils.getItemStack("miscutils:item.itemBufferCore6", 1);
	static ItemStack RECIPE_BufferCore_LuV = ItemUtils.getItemStack("miscutils:item.itemBufferCore7", 1);
	static ItemStack RECIPE_BufferCore_ZPM = ItemUtils.getItemStack("miscutils:item.itemBufferCore8", 1);
	static ItemStack RECIPE_BufferCore_UV = ItemUtils.getItemStack("miscutils:item.itemBufferCore9", 1);
	static ItemStack RECIPE_BufferCore_MAX = ItemUtils.getItemStack("miscutils:item.itemBufferCore10", 1);


	//Wire
	static String wireTier1 = "wireGt08Lead";
	static String wireTier2 = "wireGt08Tin";
	static String wireTier3 = "wireGt08Copper";
	static String wireTier4 = "wireGt08Gold";
	static String wireTier5 = "wireGt08Aluminium";
	static String wireTier6 = "wireGt08Tungsten";
	static String wireTier7 = "wireGt08Naquadah";
	static String wireTier8 = "wireGt08Osmium";
	static String wireTier9 = "wireGt08Superconductor";
	static String wireTier10 = "wireGt16Superconductor";

	//Wire
	static String cableTier1 = "cableGt04Lead";
	static String cableTier2 = "cableGt04Tin";
	static String cableTier3 = "cableGt04Copper";
	static String cableTier4 = "cableGt04Gold";
	static String cableTier5 = "cableGt04Aluminium";
	static String cableTier6 = "cableGt04Tungsten";
	static String cableTier7 = "cableGt04Naquadah";
	static String cableTier8 = "cableGt04Osmium";
	static String cableTier9 = "cableGt04NiobiumTitanium";
	static String cableTier10 = "cableGt08NiobiumTitanium";


	//Plates
	static String plateTier1 = "plateLead";
	static String plateTier2 = "plateTin";
	static String plateTier3 = "plateCopper";
	static String plateTier4 = "plateGold";
	static String plateTier5 = "plateAluminium";
	static String plateTier6 = "plateMaragingSteel250";
	static String plateTier7 = "plateTantalloy61";
	static String plateTier8 = "plateInconel792";
	static String plateTier9 = "plateZeron100";
	static String plateTier10 = "plateNaquadahEnriched";
	static String plateTier11 = "plateNeutronium";

	//rods
	static String rodTier1 = "stickLead";
	static String rodTier2 = "stickTin";
	static String rodTier3 = "stickCopper";
	static String rodTier4 = "stickGold";
	static String rodTier5 = "stickAluminium";
	static String rodTier6 = "stickMaragingSteel250";
	static String rodTier7 = "stickTantalloy61";
	static String rodTier8 = "stickInconel792";
	static String rodTier9 = "stickZeron100";
	static String rodTier10 = "stickNaquadahEnriched";
	static String rodTier11 = "stickNeutronium";

	static String pipeTier1 = "pipeHuge"+"Potin";
	static String pipeTier2 = "pipeHuge"+"DarkSteel";
	static String pipeTier3 = "pipeHuge"+"StainlessSteel";
	static String pipeTier4 = "pipeHuge"+"Titanium";
	static String pipeTier5 = "pipeHuge"+"TungstenSteel";
	static String pipeTier6 = "pipeHuge"+"MaragingSteel300";
	static String pipeTier7 = "pipeHuge"+"Tantalloy60";
	static String pipeTier8 = "pipeHuge"+"Tantalloy61";
	static String pipeTier9 = "pipeHuge"+"Inconel792";
	static String pipeTier10 = "pipeHuge"+"HastelloyX";
	static String pipeTier11 = "pipeHuge"+"Europium";


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
	public static ItemStack EV_MACHINE_Macerator;
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
	static ItemStack blockBricks = ItemUtils.getItemStack("minecraft:brick_block", 1);

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
			IC2MFE = ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:blockElectric", "IC2_MFE", 1, 1);
			IC2MFSU = ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:blockElectric", "IC2_MFSU", 2, 1);
		}
		if (LoadedMods.Gregtech){
			RECIPES_Shapeless.dustStaballoy = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils, "gregtech:gt.metaitem.01", "Staballoy Dust", 2319, 2);
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

		if(LoadedMods.Railcraft){
			//Misc
			INPUT_RCCokeOvenBlock = ItemUtils.getItemStackWithMeta(LoadedMods.Railcraft, "Railcraft:machine.alpha", "Coke_Oven_RC", 7, 1);
		}
		if(LoadedMods.ImmersiveEngineering){
			//Misc
			INPUT_IECokeOvenBlock = ItemUtils.getItemStackWithMeta(LoadedMods.ImmersiveEngineering, "ImmersiveEngineering:stoneDecoration", "Coke_Oven_IE", 1, 1);
		}
		runModRecipes();
	}	

	private static void runModRecipes(){
		if (LoadedMods.Gregtech){			

			RecipeUtils.addShapedGregtechRecipe(
					ItemList.Electric_Piston_EV, GregtechOrePrefixes.circuit.get(Materials.Ultimate), ItemList.Electric_Piston_EV,
					ItemList.Electric_Motor_EV, machineCasing_EV, ItemList.Electric_Motor_EV,
					"gearGtTitanium", "cableGt02Aluminium", "gearGtTitanium",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 793, 1));
			RecipeUtils.addShapedGregtechRecipe(
					ItemList.Electric_Piston_IV, GregtechOrePrefixes.circuit.get(GT_Materials.Symbiotic), ItemList.Electric_Piston_IV,
					ItemList.Electric_Motor_IV, machineCasing_IV, ItemList.Electric_Motor_IV,
					"gearGtTungstenSteel", "cableGt02Platinum", "gearGtTungstenSteel",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 794, 1));
			RecipeUtils.addShapedGregtechRecipe(
					RECIPE_CONSTANTS.electricPiston_LuV, GregtechOrePrefixes.circuit.get(GT_Materials.Neutronic), RECIPE_CONSTANTS.electricPiston_LuV,
					RECIPE_CONSTANTS.electricMotor_LuV, machineCasing_LuV, RECIPE_CONSTANTS.electricMotor_LuV,
					"gearGtChrome", "cableGt02Tungsten", "gearGtChrome",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 795, 1));

			//Buffer Core
			RecipeUtils.addShapedGregtechRecipe(
					plateTier1, cableTier1, plateTier1,
					circuitPrimitive, IC2MFE, circuitPrimitive,
					plateTier1, cableTier1, plateTier1,
					RECIPE_BufferCore_ULV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier2, cableTier2, plateTier2,
					circuitTier1, IC2MFE, circuitTier1,
					plateTier2, cableTier2, plateTier2,
					RECIPE_BufferCore_LV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier3, cableTier3, plateTier3,
					RECIPE_BufferCore_LV, circuitTier2, RECIPE_BufferCore_LV,
					plateTier3, cableTier3, plateTier3,
					RECIPE_BufferCore_MV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier4, cableTier4, plateTier4,
					RECIPE_BufferCore_MV, circuitTier3, RECIPE_BufferCore_MV,
					plateTier4, cableTier4, plateTier4,
					RECIPE_BufferCore_HV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier5, cableTier5, plateTier5,
					RECIPE_BufferCore_HV, circuitTier4, RECIPE_BufferCore_HV,
					plateTier5, cableTier5, plateTier5,
					RECIPE_BufferCore_EV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier6, cableTier6, plateTier6,
					RECIPE_BufferCore_EV, circuitTier5, RECIPE_BufferCore_EV,
					plateTier6, cableTier6, plateTier6,
					RECIPE_BufferCore_IV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier7, cableTier7, plateTier7,
					RECIPE_BufferCore_IV, circuitTier6, RECIPE_BufferCore_IV,
					plateTier7, cableTier7, plateTier7,
					RECIPE_BufferCore_LuV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier8, cableTier8, plateTier8,
					RECIPE_BufferCore_LuV, circuitTier7, RECIPE_BufferCore_LuV,
					plateTier8, cableTier8, plateTier8,
					RECIPE_BufferCore_ZPM);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier9, cableTier9, plateTier9,
					RECIPE_BufferCore_ZPM, circuitTier8, RECIPE_BufferCore_ZPM,
					plateTier9, cableTier9, plateTier9,
					RECIPE_BufferCore_UV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier10, cableTier10, plateTier10,
					RECIPE_BufferCore_UV, circuitTier9, RECIPE_BufferCore_UV,
					plateTier10, cableTier10, plateTier10,
					RECIPE_BufferCore_MAX);


			RecipeUtils.addShapedGregtechRecipe(
					wireTier1, RECIPE_BufferCore_ULV, wireTier1,
					wireTier1, machineCasing_ULV, wireTier1,
					circuitPrimitive, circuitTier1, circuitPrimitive,
					RECIPE_Buffer_ULV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier2, RECIPE_BufferCore_LV, wireTier2,
					wireTier2, machineCasing_LV, wireTier2,
					circuitTier1, RECIPE_BufferCore_LV, circuitTier1,
					RECIPE_Buffer_LV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier3, RECIPE_BufferCore_MV, wireTier3,
					wireTier3, machineCasing_MV, wireTier3,
					circuitTier2, RECIPE_BufferCore_MV, circuitTier2,
					RECIPE_Buffer_MV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier4, RECIPE_BufferCore_HV, wireTier4,
					wireTier4, machineCasing_HV, wireTier4,
					circuitTier3, RECIPE_BufferCore_HV, circuitTier3,
					RECIPE_Buffer_HV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier5, RECIPE_BufferCore_EV, wireTier5,
					wireTier5, machineCasing_EV, wireTier5,
					circuitTier4, RECIPE_BufferCore_EV, circuitTier4,
					RECIPE_Buffer_EV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier6, RECIPE_BufferCore_IV, wireTier6,
					wireTier6, machineCasing_IV, wireTier6,
					circuitTier5, RECIPE_BufferCore_IV, circuitTier5,
					RECIPE_Buffer_IV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier7, RECIPE_BufferCore_LuV, wireTier7,
					wireTier7, machineCasing_LuV, wireTier7,
					circuitTier6, RECIPE_BufferCore_LuV, circuitTier6,
					RECIPE_Buffer_LuV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier8, RECIPE_BufferCore_ZPM, wireTier8,
					wireTier8, machineCasing_ZPM, wireTier8,
					circuitTier7, RECIPE_BufferCore_ZPM, circuitTier7,
					RECIPE_Buffer_ZPM);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier9, RECIPE_BufferCore_UV, wireTier9,
					wireTier9, machineCasing_UV, wireTier9,
					circuitTier8, RECIPE_BufferCore_UV, circuitTier8,
					RECIPE_Buffer_UV);
			RecipeUtils.addShapedGregtechRecipe(
					plateTier11, RECIPE_BufferCore_MAX, plateTier11,
					wireTier10, machineCasing_MAX, wireTier10,
					circuitTier9, RECIPE_BufferCore_MAX, circuitTier9,
					RECIPE_Buffer_MAX);

			//Steam Condenser
			RecipeUtils.addShapedGregtechRecipe(
					pipeLargeCopper, pipeHugeSteel, pipeLargeCopper,
					plateEnergeticAlloy, electricPump_HV, plateEnergeticAlloy,
					plateEnergeticAlloy, pipeLargeCopper, plateEnergeticAlloy,
					RECIPE_SteamCondenser);

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
			GT_ModHandler.addPulverisationRecipe(RECIPE_IronPlatedBricks, ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustIron", 6), ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustClay", 2), true);
			GT_Values.RA.addArcFurnaceRecipe(RECIPE_IronPlatedBricks, new ItemStack[]{ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotWroughtIron", 6), ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustAsh", 2)}, new int[]{0}, 32*20, 32);
			
			/*//Electrolyzer Frame Casing
			UtilsRecipe.addShapedGregtechRecipe(
					"platePotin", "stickLongChrome", "platePotin",
					"stickLongPotin", "frameGtPotin", "stickLongPotin",
					"platePotin", "stickLongPotin", "platePotin",
					RECIPE_IndustrialCentrifugeCasing);
			//Industrial Electrolyzer
			UtilsRecipe.addShapedGregtechRecipe(
					"plateStellite", circuitTier6, "plateStellite",
					machineCasing_EV, IV_MACHINE_Electrolyzer, machineCasing_EV,
					"plateStellite", "rotorStellite", "plateStellite",
					RECIPE_IndustrialCentrifugeController);*/

			//Industrial Centrifuge
			RecipeUtils.addShapedGregtechRecipe(
					circuitTier6, pipeHugeStainlessSteel, circuitTier6,
					plateTier6, IV_MACHINE_Centrifuge, plateTier6,
					plateTier8, machineCasing_IV, plateTier8,
					RECIPE_IndustrialCentrifugeController);
			//Centrifuge Casing
			RecipeUtils.addShapedGregtechRecipe(
					plateTier6, "stickTumbaga", plateTier6,
					plateTier8, "stickTumbaga", plateTier8,
					plateTier6, "stickTumbaga", plateTier6,
					RECIPE_IndustrialCentrifugeCasing);

			if (LoadedMods.Railcraft){
				//Industrial Coke Oven
				RecipeUtils.addShapedGregtechRecipe(
						plateCobalt, circuitTier4, plateCobalt,
						machineCasing_HV, INPUT_RCCokeOvenBlock, machineCasing_HV,
						plateCobalt, circuitTier5, plateCobalt,
						RECIPE_IndustrialCokeOvenController);
			}
			if (LoadedMods.ImmersiveEngineering){
				//Industrial Coke Oven
				RecipeUtils.addShapedGregtechRecipe(
						plateTier8, circuitTier4, plateTier8,
						machineCasing_HV, INPUT_IECokeOvenBlock, machineCasing_HV,
						plateTier8, circuitTier3, plateTier8,
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

			//Electrolyzer Frame Casing
			RecipeUtils.addShapedGregtechRecipe(
					"platePotin", "stickLongChrome", "platePotin",
					"stickLongPotin", "frameGtPotin", "stickLongPotin",
					"platePotin", "stickLongPotin", "platePotin",
					RECIPE_IndustrialElectrolyzerFrame);
			//Industrial Electrolyzer
			RecipeUtils.addShapedGregtechRecipe(
					"plateStellite", circuitTier6, "plateStellite",
					machineCasing_EV, IV_MACHINE_Electrolyzer, machineCasing_EV,
					"plateStellite", "rotorStellite", "plateStellite",
					RECIPE_IndustrialElectrolyzerController);

			//Material Press Frame Casing
			RecipeUtils.addShapedGregtechRecipe(
					"plateTitanium", "stickLongTumbaga", "plateTitanium",
					"stickTantalloy60", "frameGtTumbaga", "stickTantalloy60",
					"plateTitanium", "stickLongTumbaga", "plateTitanium",
					RECIPE_IndustrialMaterialPressFrame);
			//Industrial Material Press
			RecipeUtils.addShapedGregtechRecipe(
					"plateTitanium", circuitTier5, "plateTitanium",
					machineCasing_EV, IV_MACHINE_BendingMachine, machineCasing_EV,
					"plateTitanium", circuitTier5, "plateTitanium",
					RECIPE_IndustrialMaterialPressController);

			//Maceration Frame Casing
			RecipeUtils.addShapedGregtechRecipe(
					"platePalladium", "platePalladium", "platePalladium",
					"stickPlatinum", "frameGtInconel625", "stickPlatinum",
					"platePalladium", "stickLongPalladium", "platePalladium",
					RECIPE_IndustrialMacerationStackFrame);
			//Industrial Maceration stack 
			RecipeUtils.addShapedGregtechRecipe(
					"plateTungstenCarbide", IV_MACHINE_Macerator, "plateTungstenCarbide",
					EV_MACHINE_Macerator, circuitTier7, EV_MACHINE_Macerator,
					"plateTungstenCarbide", machineCasing_IV, "plateTungstenCarbide",
					RECIPE_IndustrialMacerationStackController);

			//Wire Factory Frame Casing
			RecipeUtils.addShapedGregtechRecipe(
					"plateBlueSteel", "stickBlueSteel", "plateBlueSteel",
					"stickBlueSteel", "frameGtBlueSteel", "stickBlueSteel",
					"plateBlueSteel", "stickBlueSteel", "plateBlueSteel",
					RECIPE_IndustrialWireFactoryFrame);
			//Industrial Wire Factory
			RecipeUtils.addShapedGregtechRecipe(
					"plateZeron100", machineCasing_IV, "plateZeron100",
					circuitTier6, IV_MACHINE_Wiremill, circuitTier6,
					"plateZeron100", machineCasing_IV, "plateZeron100",
					RECIPE_IndustrialWireFactoryController);



			//Tiered Tanks
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
					plateTier7, RECIPE_CONSTANTS.electricPump_LuV, plateTier7,
					GregtechItemList.GT_FluidTank_LuV.get(1));
			RecipeUtils.addShapedGregtechRecipe(
					plateTier8, plateTier8, plateTier8,
					plateTier8, pipeTier8, plateTier8,
					plateTier8, RECIPE_CONSTANTS.electricPump_ZPM, plateTier8,
					GregtechItemList.GT_FluidTank_ZPM.get(1));
			RecipeUtils.addShapedGregtechRecipe(
					plateTier9, plateTier9, plateTier9,
					plateTier9, pipeTier9, plateTier9,
					plateTier9, RECIPE_CONSTANTS.electricPump_UV, plateTier9,
					GregtechItemList.GT_FluidTank_UV.get(1));
			RecipeUtils.addShapedGregtechRecipe(
					plateTier10, plateTier10, plateTier10,
					plateTier10, pipeTier10, plateTier10,
					plateTier10, RECIPE_CONSTANTS.electricPump_MAX, plateTier10,
					GregtechItemList.GT_FluidTank_MAX.get(1));


			//Industrial Multi Tank Casing
			RecipeUtils.addShapedGregtechRecipe(
					"stickGrisium", "plateGrisium", "stickGrisium",
					"plateGrisium", "frameGtGrisium", "plateGrisium",
					"plateGrisium", "plateGrisium", "plateGrisium",
					RECIPE_IndustrialMultiTankFrame);
			//Industrial Multi Tank
			RecipeUtils.addShapedGregtechRecipe(
					"pipeHugeDarkSteel", "gearGrisium", "pipeHugeDarkSteel",
					circuitTier4, RECIPE_IndustrialMultiTankFrame, circuitTier4,
					"plateDoubleGrisium", "rotorGrisium", "plateDoubleGrisium",
					RECIPE_IndustrialMultiTankController);


			//Blast Smelter
			RecipeUtils.addShapedGregtechRecipe(
					"plateZirconiumCarbide", circuitTier4, "plateZirconiumCarbide",
					cableTier4, machineCasing_EV, cableTier4,
					"plateZirconiumCarbide", circuitTier3, "plateZirconiumCarbide",
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
			
			
			//Matter Fabricator CPU
			RecipeUtils.addShapedGregtechRecipe(
					"plateDoubleQuantum", circuitTier5, "plateDoubleQuantum",
					cableTier8, machineCasing_LuV, cableTier8,
					"plateDoubleQuantum", circuitTier5, "plateDoubleQuantum",
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


		Utils.LOG_INFO("Done loading recipes for the Various machine blocks.");
	}
}
