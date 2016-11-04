package gtPlusPlus.core.recipe;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import net.minecraft.item.ItemStack;

public class RECIPES_Machines {

	// Outputs
	// static ItemStack RECIPE_BufferCore_ULV = new
	// ItemStack(GregtechEnergyBuffer.itemBufferCore);
	static ItemStack		RECIPE_SteamCondenser						= GregtechItemList.Condensor_MAX.get(1);
	static ItemStack		RECIPE_IronBlastFurnace						= GregtechItemList.Machine_Iron_BlastFurnace
			.get(1);
	static ItemStack		RECIPE_IronPlatedBricks						= GregtechItemList.Casing_IronPlatedBricks
			.get(1);
	static ItemStack		RECIPE_Buffer_ULV							= GregtechItemList.Energy_Buffer_1by1_ULV
			.get(1);
	static ItemStack		RECIPE_Buffer_LV							= GregtechItemList.Energy_Buffer_1by1_LV.get(1);
	static ItemStack		RECIPE_Buffer_MV							= GregtechItemList.Energy_Buffer_1by1_MV.get(1);
	static ItemStack		RECIPE_Buffer_HV							= GregtechItemList.Energy_Buffer_1by1_HV.get(1);
	static ItemStack		RECIPE_Buffer_EV							= GregtechItemList.Energy_Buffer_1by1_EV.get(1);
	static ItemStack		RECIPE_Buffer_IV							= GregtechItemList.Energy_Buffer_1by1_IV.get(1);
	static ItemStack		RECIPE_Buffer_LuV							= GregtechItemList.Energy_Buffer_1by1_LuV
			.get(1);
	static ItemStack		RECIPE_Buffer_ZPM							= GregtechItemList.Energy_Buffer_1by1_ZPM
			.get(1);
	static ItemStack		RECIPE_Buffer_UV							= GregtechItemList.Energy_Buffer_1by1_UV.get(1);
	static ItemStack		RECIPE_Buffer_MAX							= GregtechItemList.Energy_Buffer_1by1_MAX
			.get(1);
	// Industrial Centrifuge
	static ItemStack		RECIPE_IndustrialCentrifugeController		= GregtechItemList.Industrial_Centrifuge.get(1);
	static ItemStack		RECIPE_IndustrialCentrifugeCasing			= GregtechItemList.Casing_Centrifuge1.get(1);
	// Industrial Coke Oven
	static ItemStack		RECIPE_IndustrialCokeOvenController			= GregtechItemList.Industrial_CokeOven.get(1);
	static ItemStack		RECIPE_IndustrialCokeOvenFrame				= GregtechItemList.Casing_CokeOven.get(1);
	static ItemStack		RECIPE_IndustrialCokeOvenCasingA			= GregtechItemList.Casing_CokeOven_Coil1.get(1);
	static ItemStack		RECIPE_IndustrialCokeOvenCasingB			= GregtechItemList.Casing_CokeOven_Coil2.get(1);
	//
	static ItemStack		RECIPE_IndustrialElectrolyzerController		= GregtechItemList.Industrial_Electrolyzer
			.get(1);
	static ItemStack		RECIPE_IndustrialElectrolyzerFrame			= GregtechItemList.Casing_Electrolyzer.get(1);
	//
	static ItemStack		RECIPE_IndustrialMaterialPressController	= GregtechItemList.Industrial_PlatePress.get(1);
	static ItemStack		RECIPE_IndustrialMaterialPressFrame			= GregtechItemList.Casing_MaterialPress.get(1);
	//
	static ItemStack		RECIPE_IndustrialMacerationStackController	= GregtechItemList.Industrial_MacerationStack
			.get(1);
	static ItemStack		RECIPE_IndustrialMacerationStackFrame		= GregtechItemList.Casing_MacerationStack
			.get(1);
	//
	static ItemStack		RECIPE_IndustrialWireFactoryController		= GregtechItemList.Industrial_WireFactory
			.get(1);
	static ItemStack		RECIPE_IndustrialWireFactoryFrame			= GregtechItemList.Casing_WireFactory.get(1);
	// Industrial Coke Oven
	static ItemStack		RECIPE_IndustrialBlastSmelterController		= GregtechItemList.Industrial_AlloyBlastSmelter
			.get(1);
	static ItemStack		RECIPE_IndustrialBlastSmelterFrame			= GregtechItemList.Casing_BlastSmelter.get(1);
	static ItemStack		RECIPE_IndustrialBlastSmelterCoil			= GregtechItemList.Casing_Coil_BlastSmelter
			.get(1);

	// Buffer Cores
	static ItemStack		RECIPE_BufferCore_ULV						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore1", 1);
	static ItemStack		RECIPE_BufferCore_LV						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore2", 1);
	static ItemStack		RECIPE_BufferCore_MV						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore3", 1);
	static ItemStack		RECIPE_BufferCore_HV						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore4", 1);
	static ItemStack		RECIPE_BufferCore_EV						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore5", 1);
	static ItemStack		RECIPE_BufferCore_IV						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore6", 1);
	static ItemStack		RECIPE_BufferCore_LuV						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore7", 1);
	static ItemStack		RECIPE_BufferCore_ZPM						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore8", 1);
	static ItemStack		RECIPE_BufferCore_UV						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore9", 1);
	static ItemStack		RECIPE_BufferCore_MAX						= ItemUtils
			.getItemStack("miscutils:item.itemBufferCore10", 1);

	// Wire
	static String			wireTier1									= "wireGt08Lead";
	static String			wireTier2									= "wireGt08Tin";
	static String			wireTier3									= "wireGt08Copper";
	static String			wireTier4									= "wireGt08Gold";
	static String			wireTier5									= "wireGt08Aluminium";
	static String			wireTier6									= "wireGt08Tungsten";
	static String			wireTier7									= "wireGt08Naquadah";
	static String			wireTier8									= "wireGt08Osmium";
	static String			wireTier9									= "wireGt08Superconductor";
	static String			wireTier10									= "wireGt16Superconductor";

	// Wire
	static String			cableTier1									= "cableGt04Lead";
	static String			cableTier2									= "cableGt04Tin";
	static String			cableTier3									= "cableGt04Copper";
	static String			cableTier4									= "cableGt04Gold";
	static String			cableTier5									= "cableGt04Aluminium";
	static String			cableTier6									= "cableGt04Tungsten";
	static String			cableTier7									= "cableGt04Naquadah";
	static String			cableTier8									= "cableGt04Osmium";
	static String			cableTier9									= "cableGt04NiobiumTitanium";
	static String			cableTier10									= "cableGt08NiobiumTitanium";

	// Plates
	static String			plateTier1									= "plateLead";
	static String			plateTier2									= "plateTin";
	static String			plateTier3									= "plateCopper";
	static String			plateTier4									= "plateGold";
	static String			plateTier5									= "plateAluminium";
	static String			plateTier6									= "plateMaragingSteel250";
	static String			plateTier7									= "plateTantalloy61";
	static String			plateTier8									= "plateInconel792";
	static String			plateTier9									= "plateZeron100";
	static String			plateTier10									= "plateNaquadahEnriched";
	static String			plateTier11									= "plateNeutronium";

	// rods
	static String			rodTier1									= "stickLead";
	static String			rodTier2									= "stickTin";
	static String			rodTier3									= "stickCopper";
	static String			rodTier4									= "stickGold";
	static String			rodTier5									= "stickAluminium";
	static String			rodTier6									= "stickMaragingSteel250";
	static String			rodTier7									= "stickTantalloy61";
	static String			rodTier8									= "stickInconel792";
	static String			rodTier9									= "stickZeron100";
	static String			rodTier10									= "stickNaquadahEnriched";
	static String			rodTier11									= "stickNeutronium";

	static String			pipeTier1									= "pipeHuge" + "Potin";
	static String			pipeTier2									= "pipeHuge" + "Steel";
	static String			pipeTier3									= "pipeHuge" + "StainlessSteel";
	static String			pipeTier4									= "pipeHuge" + "Titanium";
	static String			pipeTier5									= "pipeHuge" + "TungstenSteel";
	static String			pipeTier6									= "pipeHuge" + "MaragingSteel300";
	static String			pipeTier7									= "pipeHuge" + "Tantalloy60";
	static String			pipeTier8									= "pipeHuge" + "Tantalloy61";
	static String			pipeTier9									= "pipeHuge" + "Inconel792";
	static String			pipeTier10									= "pipeHuge" + "HastelloyX";
	static String			pipeTier11									= "pipeHuge" + "Europium";

	// Machine Casings
	static ItemStack		machineCasing_ULV;
	static ItemStack		machineCasing_LV;
	static ItemStack		machineCasing_MV;
	static ItemStack		machineCasing_HV;
	static ItemStack		machineCasing_EV;
	static ItemStack		machineCasing_IV;
	static ItemStack		machineCasing_LuV;
	static ItemStack		machineCasing_ZPM;
	static ItemStack		machineCasing_UV;
	static ItemStack		machineCasing_MAX;

	// Gearbox Casings
	static ItemStack		gearboxCasing_Tier_1;
	static ItemStack		gearboxCasing_Tier_2;
	static ItemStack		gearboxCasing_Tier_3;
	static ItemStack		gearboxCasing_Tier_4;

	// IV MACHINES
	public static ItemStack	IV_MACHINE_Electrolyzer;
	public static ItemStack	IV_MACHINE_Centrifuge;
	public static ItemStack	IV_MACHINE_BendingMachine;
	public static ItemStack	IV_MACHINE_Wiremill;
	public static ItemStack	IV_MACHINE_Macerator;
	public static ItemStack	IV_MACHINE_MassFabricator;

	// Cables
	static String			cableGt02Electrum							= "cableGt02Electrum";

	// Plates
	static String			plateElectricalSteel						= "plateElectricalSteel";
	static String			plateEnergeticAlloy							= "plateEnergeticAlloy";
	static String			plateCobalt									= "plateCobalt";
	static String			plateBronze									= "plateBronze";
	static String			plateSteel									= "plateSteel";

	// Pipes
	static String			pipeLargeCopper								= "pipeLargeCopper";
	static String			pipeHugeSteel								= "pipeHugeSteel";
	static String			pipeHugeStainlessSteel						= "pipeHugeStainlessSteel";
	static String			pipeHugeTitanium							= "pipeHugeTitanium";

	// Lava Boiler
	static ItemStack		boiler_Coal;
	static ItemStack		blockBricks									= ItemUtils
			.getItemStack("minecraft:brick_block", 1);

	// Batteries
	static String			batteryBasic								= "batteryBasic";
	static String			batteryAdvanced								= "batteryAdvanced";
	static String			batteryElite								= "batteryElite";
	static String			batteryMaster								= "batteryMaster";
	static String			batteryUltimate								= "batteryUltimate";
	static ItemStack		IC2MFE;
	static ItemStack		IC2MFSU;

	// Circuits
	static String			circuitPrimitive							= "circuitBasic";
	static String			circuitTier1								= "circuitGood";
	static String			circuitTier2								= "circuitAdvanced";
	static String			circuitTier3								= "circuitData";
	static String			circuitTier4								= "circuitElite";
	static String			circuitTier5								= "circuitMaster";
	static String			circuitTier6								= "circuitUltimate";
	static String			circuitTier7								= "circuitSymbiotic";
	static String			circuitTier8								= "circuitNeutronic";
	static String			circuitTier9								= "circuitQuantum";

	// Machine Components
	static ItemStack		electricMotor_LV;
	static ItemStack		electricMotor_MV;
	static ItemStack		electricMotor_HV;
	static ItemStack		electricMotor_EV;
	static ItemStack		electricMotor_IV;
	static ItemStack		electricPump_LV;
	static ItemStack		electricPump_MV;
	static ItemStack		electricPump_HV;
	static ItemStack		electricPump_EV;
	static ItemStack		electricPump_IV;
	static ItemStack		electricPiston_LV;
	static ItemStack		electricPiston_MV;
	static ItemStack		electricPiston_HV;
	static ItemStack		electricPiston_EV;
	static ItemStack		electricPiston_IV;
	static ItemStack		robotArm_LV;
	static ItemStack		robotArm_MV;
	static ItemStack		robotArm_HV;
	static ItemStack		robotArm_EV;
	static ItemStack		robotArm_IV;
	static ItemStack		conveyorModule_LV;
	static ItemStack		conveyorModule_MV;
	static ItemStack		conveyorModule_HV;
	static ItemStack		conveyorModule_EV;
	static ItemStack		conveyorModule_IV;
	static ItemStack		emitter_LV;
	static ItemStack		emitter_MV;
	static ItemStack		emitter_HV;
	static ItemStack		emitter_EV;
	static ItemStack		emitter_IV;
	static ItemStack		fieldGenerator_LV;
	static ItemStack		fieldGenerator_MV;
	static ItemStack		fieldGenerator_HV;
	static ItemStack		fieldGenerator_EV;
	static ItemStack		fieldGenerator_IV;
	static ItemStack		sensor_LV;
	static ItemStack		sensor_MV;
	static ItemStack		sensor_HV;
	static ItemStack		sensor_EV;
	static ItemStack		sensor_IV;

	// Misc
	static ItemStack		INPUT_RCCokeOvenBlock;
	static ItemStack		INPUT_IECokeOvenBlock;

	// RobotArm, Conveyor, Emitter, Sensor, Field Generator

	private static void initModItems() {
		if (LoadedMods.IndustrialCraft2) {
			RECIPES_Machines.IC2MFE = ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:blockElectric",
					"IC2_MFE", 1, 1);
			RECIPES_Machines.IC2MFSU = ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:blockElectric",
					"IC2_MFSU", 2, 1);
		}
		if (LoadedMods.Gregtech) {
			RECIPES_Shapeless.dustStaballoy = ItemUtils.getItemStackWithMeta(LoadedMods.MiscUtils,
					"gregtech:gt.metaitem.01", "Staballoy Dust", 2319, 2);
			RECIPES_Machines.machineCasing_ULV = ItemList.Casing_ULV.get(1);
			RECIPES_Machines.machineCasing_LV = ItemList.Casing_LV.get(1);
			RECIPES_Machines.machineCasing_MV = ItemList.Casing_MV.get(1);
			RECIPES_Machines.machineCasing_HV = ItemList.Casing_HV.get(1);
			RECIPES_Machines.machineCasing_EV = ItemList.Casing_EV.get(1);
			RECIPES_Machines.machineCasing_IV = ItemList.Casing_IV.get(1);
			RECIPES_Machines.machineCasing_LuV = ItemList.Casing_LuV.get(1);
			RECIPES_Machines.machineCasing_ZPM = ItemList.Casing_ZPM.get(1);
			RECIPES_Machines.machineCasing_UV = ItemList.Casing_UV.get(1);
			RECIPES_Machines.machineCasing_MAX = ItemList.Casing_MAX.get(1);

			// Gearbox Casings
			RECIPES_Machines.gearboxCasing_Tier_1 = ItemList.Casing_Gearbox_Bronze.get(1);
			RECIPES_Machines.gearboxCasing_Tier_2 = ItemList.Casing_Gearbox_Steel.get(1);
			RECIPES_Machines.gearboxCasing_Tier_3 = ItemList.Casing_Gearbox_Titanium.get(1);
			RECIPES_Machines.gearboxCasing_Tier_4 = ItemList.Casing_Gearbox_TungstenSteel.get(1);

			// Lava Boiler
			RECIPES_Machines.boiler_Coal = ItemList.Machine_Bronze_Boiler.get(1);

			// Machine Components
			RECIPES_Machines.electricMotor_LV = ItemList.Electric_Motor_LV.get(1);
			RECIPES_Machines.electricMotor_MV = ItemList.Electric_Motor_MV.get(1);
			RECIPES_Machines.electricMotor_HV = ItemList.Electric_Motor_HV.get(1);
			RECIPES_Machines.electricMotor_EV = ItemList.Electric_Motor_EV.get(1);
			RECIPES_Machines.electricMotor_IV = ItemList.Electric_Motor_IV.get(1);
			RECIPES_Machines.electricPump_LV = ItemList.Electric_Pump_LV.get(1);
			RECIPES_Machines.electricPump_MV = ItemList.Electric_Pump_MV.get(1);
			RECIPES_Machines.electricPump_HV = ItemList.Electric_Pump_HV.get(1);
			RECIPES_Machines.electricPump_EV = ItemList.Electric_Pump_EV.get(1);
			RECIPES_Machines.electricPump_IV = ItemList.Electric_Pump_IV.get(1);
			RECIPES_Machines.electricPiston_LV = ItemList.Electric_Piston_LV.get(1);
			RECIPES_Machines.electricPiston_MV = ItemList.Electric_Piston_MV.get(1);
			RECIPES_Machines.electricPiston_HV = ItemList.Electric_Piston_HV.get(1);
			RECIPES_Machines.electricPiston_EV = ItemList.Electric_Piston_EV.get(1);
			RECIPES_Machines.electricPiston_IV = ItemList.Electric_Piston_IV.get(1);
			RECIPES_Machines.robotArm_LV = ItemList.Robot_Arm_LV.get(1);
			RECIPES_Machines.robotArm_MV = ItemList.Robot_Arm_MV.get(1);
			RECIPES_Machines.robotArm_HV = ItemList.Robot_Arm_HV.get(1);
			RECIPES_Machines.robotArm_EV = ItemList.Robot_Arm_EV.get(1);
			RECIPES_Machines.robotArm_IV = ItemList.Robot_Arm_IV.get(1);
			RECIPES_Machines.conveyorModule_LV = ItemList.Conveyor_Module_LV.get(1);
			RECIPES_Machines.conveyorModule_MV = ItemList.Conveyor_Module_MV.get(1);
			RECIPES_Machines.conveyorModule_HV = ItemList.Conveyor_Module_HV.get(1);
			RECIPES_Machines.conveyorModule_EV = ItemList.Conveyor_Module_EV.get(1);
			RECIPES_Machines.conveyorModule_IV = ItemList.Conveyor_Module_IV.get(1);
			RECIPES_Machines.emitter_LV = ItemList.Emitter_LV.get(1);
			RECIPES_Machines.emitter_MV = ItemList.Emitter_MV.get(1);
			RECIPES_Machines.emitter_HV = ItemList.Emitter_HV.get(1);
			RECIPES_Machines.emitter_EV = ItemList.Emitter_EV.get(1);
			RECIPES_Machines.emitter_IV = ItemList.Emitter_IV.get(1);
			RECIPES_Machines.fieldGenerator_LV = ItemList.Field_Generator_LV.get(1);
			RECIPES_Machines.fieldGenerator_MV = ItemList.Field_Generator_MV.get(1);
			RECIPES_Machines.fieldGenerator_HV = ItemList.Field_Generator_HV.get(1);
			RECIPES_Machines.fieldGenerator_EV = ItemList.Field_Generator_EV.get(1);
			RECIPES_Machines.fieldGenerator_IV = ItemList.Field_Generator_IV.get(1);
			RECIPES_Machines.sensor_LV = ItemList.Sensor_LV.get(1);
			RECIPES_Machines.sensor_MV = ItemList.Sensor_MV.get(1);
			RECIPES_Machines.sensor_HV = ItemList.Sensor_HV.get(1);
			RECIPES_Machines.sensor_EV = ItemList.Sensor_EV.get(1);
			RECIPES_Machines.sensor_IV = ItemList.Sensor_IV.get(1);

			// IV MACHINES
			RECIPES_Machines.IV_MACHINE_Electrolyzer = ItemList.Machine_IV_Electrolyzer.get(1);
			RECIPES_Machines.IV_MACHINE_BendingMachine = ItemList.Machine_IV_Bender.get(1);
			RECIPES_Machines.IV_MACHINE_Wiremill = ItemList.Machine_IV_Wiremill.get(1);
			RECIPES_Machines.IV_MACHINE_Macerator = ItemList.Machine_IV_Macerator.get(1);
			RECIPES_Machines.IV_MACHINE_MassFabricator = ItemList.Machine_IV_Massfab.get(1);
			RECIPES_Machines.IV_MACHINE_Centrifuge = ItemList.Machine_IV_Centrifuge.get(1);

		}

		if (LoadedMods.Railcraft) {
			// Misc
			RECIPES_Machines.INPUT_RCCokeOvenBlock = ItemUtils.getItemStackWithMeta(LoadedMods.Railcraft,
					"Railcraft:machine.alpha", "Coke_Oven_RC", 7, 1);
		}
		if (LoadedMods.ImmersiveEngineering) {
			// Misc
			RECIPES_Machines.INPUT_IECokeOvenBlock = ItemUtils.getItemStackWithMeta(LoadedMods.ImmersiveEngineering,
					"ImmersiveEngineering:stoneDecoration", "Coke_Oven_IE", 1, 1);
		}
		RECIPES_Machines.runModRecipes();
	}

	public static final void RECIPES_LOAD() {
		RECIPES_Machines.run();
		Utils.LOG_INFO("Loading Recipes for the Various machine blocks.");
	}

	private static void run() {
		RECIPES_Machines.initModItems();
	}

	private static void runModRecipes() {
		if (LoadedMods.Gregtech) {

			RecipeUtils.addShapedGregtechRecipe(ItemList.Electric_Piston_EV,
					GregtechOrePrefixes.circuit.get(Materials.Ultimate), ItemList.Electric_Piston_EV,
					ItemList.Electric_Motor_EV, RECIPES_Machines.machineCasing_EV, ItemList.Electric_Motor_EV,
					"gearGtTitanium", "cableGt02Aluminium", "gearGtTitanium",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 793, 1));
			RecipeUtils.addShapedGregtechRecipe(ItemList.Electric_Piston_IV,
					GregtechOrePrefixes.circuit.get(GT_Materials.Symbiotic), ItemList.Electric_Piston_IV,
					ItemList.Electric_Motor_IV, RECIPES_Machines.machineCasing_IV, ItemList.Electric_Motor_IV,
					"gearGtTungstenSteel", "cableGt02Platinum", "gearGtTungstenSteel",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 794, 1));
			RecipeUtils.addShapedGregtechRecipe(RECIPE_CONSTANTS.electricPiston_LuV,
					GregtechOrePrefixes.circuit.get(GT_Materials.Neutronic), RECIPE_CONSTANTS.electricPiston_LuV,
					RECIPE_CONSTANTS.electricMotor_LuV, RECIPES_Machines.machineCasing_LuV,
					RECIPE_CONSTANTS.electricMotor_LuV, "gearGtChrome", "cableGt02Tungsten", "gearGtChrome",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 795, 1));

			// Buffer Core
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier1, RECIPES_Machines.cableTier1,
					RECIPES_Machines.plateTier1, RECIPES_Machines.circuitPrimitive, RECIPES_Machines.IC2MFE,
					RECIPES_Machines.circuitPrimitive, RECIPES_Machines.plateTier1, RECIPES_Machines.cableTier1,
					RECIPES_Machines.plateTier1, RECIPES_Machines.RECIPE_BufferCore_ULV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier2, RECIPES_Machines.cableTier2,
					RECIPES_Machines.plateTier2, RECIPES_Machines.circuitTier1, RECIPES_Machines.IC2MFE,
					RECIPES_Machines.circuitTier1, RECIPES_Machines.plateTier2, RECIPES_Machines.cableTier2,
					RECIPES_Machines.plateTier2, RECIPES_Machines.RECIPE_BufferCore_LV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier3, RECIPES_Machines.cableTier3,
					RECIPES_Machines.plateTier3, RECIPES_Machines.RECIPE_BufferCore_LV, RECIPES_Machines.circuitTier2,
					RECIPES_Machines.RECIPE_BufferCore_LV, RECIPES_Machines.plateTier3, RECIPES_Machines.cableTier3,
					RECIPES_Machines.plateTier3, RECIPES_Machines.RECIPE_BufferCore_MV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier4, RECIPES_Machines.cableTier4,
					RECIPES_Machines.plateTier4, RECIPES_Machines.RECIPE_BufferCore_MV, RECIPES_Machines.circuitTier3,
					RECIPES_Machines.RECIPE_BufferCore_MV, RECIPES_Machines.plateTier4, RECIPES_Machines.cableTier4,
					RECIPES_Machines.plateTier4, RECIPES_Machines.RECIPE_BufferCore_HV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier5, RECIPES_Machines.cableTier5,
					RECIPES_Machines.plateTier5, RECIPES_Machines.RECIPE_BufferCore_HV, RECIPES_Machines.circuitTier4,
					RECIPES_Machines.RECIPE_BufferCore_HV, RECIPES_Machines.plateTier5, RECIPES_Machines.cableTier5,
					RECIPES_Machines.plateTier5, RECIPES_Machines.RECIPE_BufferCore_EV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier6, RECIPES_Machines.cableTier6,
					RECIPES_Machines.plateTier6, RECIPES_Machines.RECIPE_BufferCore_EV, RECIPES_Machines.circuitTier5,
					RECIPES_Machines.RECIPE_BufferCore_EV, RECIPES_Machines.plateTier6, RECIPES_Machines.cableTier6,
					RECIPES_Machines.plateTier6, RECIPES_Machines.RECIPE_BufferCore_IV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier7, RECIPES_Machines.cableTier7,
					RECIPES_Machines.plateTier7, RECIPES_Machines.RECIPE_BufferCore_IV, RECIPES_Machines.circuitTier6,
					RECIPES_Machines.RECIPE_BufferCore_IV, RECIPES_Machines.plateTier7, RECIPES_Machines.cableTier7,
					RECIPES_Machines.plateTier7, RECIPES_Machines.RECIPE_BufferCore_LuV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier8, RECIPES_Machines.cableTier8,
					RECIPES_Machines.plateTier8, RECIPES_Machines.RECIPE_BufferCore_LuV, RECIPES_Machines.circuitTier7,
					RECIPES_Machines.RECIPE_BufferCore_LuV, RECIPES_Machines.plateTier8, RECIPES_Machines.cableTier8,
					RECIPES_Machines.plateTier8, RECIPES_Machines.RECIPE_BufferCore_ZPM);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier9, RECIPES_Machines.cableTier9,
					RECIPES_Machines.plateTier9, RECIPES_Machines.RECIPE_BufferCore_ZPM, RECIPES_Machines.circuitTier8,
					RECIPES_Machines.RECIPE_BufferCore_ZPM, RECIPES_Machines.plateTier9, RECIPES_Machines.cableTier9,
					RECIPES_Machines.plateTier9, RECIPES_Machines.RECIPE_BufferCore_UV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier10, RECIPES_Machines.cableTier10,
					RECIPES_Machines.plateTier10, RECIPES_Machines.RECIPE_BufferCore_UV, RECIPES_Machines.circuitTier9,
					RECIPES_Machines.RECIPE_BufferCore_UV, RECIPES_Machines.plateTier10, RECIPES_Machines.cableTier10,
					RECIPES_Machines.plateTier10, RECIPES_Machines.RECIPE_BufferCore_MAX);

			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier1, RECIPES_Machines.RECIPE_BufferCore_ULV,
					RECIPES_Machines.wireTier1, RECIPES_Machines.wireTier1, RECIPES_Machines.machineCasing_ULV,
					RECIPES_Machines.wireTier1, RECIPES_Machines.circuitPrimitive, RECIPES_Machines.circuitTier1,
					RECIPES_Machines.circuitPrimitive, RECIPES_Machines.RECIPE_Buffer_ULV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier2, RECIPES_Machines.RECIPE_BufferCore_LV,
					RECIPES_Machines.wireTier2, RECIPES_Machines.wireTier2, RECIPES_Machines.machineCasing_LV,
					RECIPES_Machines.wireTier2, RECIPES_Machines.circuitTier1, RECIPES_Machines.RECIPE_BufferCore_LV,
					RECIPES_Machines.circuitTier1, RECIPES_Machines.RECIPE_Buffer_LV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier3, RECIPES_Machines.RECIPE_BufferCore_MV,
					RECIPES_Machines.wireTier3, RECIPES_Machines.wireTier3, RECIPES_Machines.machineCasing_MV,
					RECIPES_Machines.wireTier3, RECIPES_Machines.circuitTier2, RECIPES_Machines.RECIPE_BufferCore_MV,
					RECIPES_Machines.circuitTier2, RECIPES_Machines.RECIPE_Buffer_MV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier4, RECIPES_Machines.RECIPE_BufferCore_HV,
					RECIPES_Machines.wireTier4, RECIPES_Machines.wireTier4, RECIPES_Machines.machineCasing_HV,
					RECIPES_Machines.wireTier4, RECIPES_Machines.circuitTier3, RECIPES_Machines.RECIPE_BufferCore_HV,
					RECIPES_Machines.circuitTier3, RECIPES_Machines.RECIPE_Buffer_HV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier5, RECIPES_Machines.RECIPE_BufferCore_EV,
					RECIPES_Machines.wireTier5, RECIPES_Machines.wireTier5, RECIPES_Machines.machineCasing_EV,
					RECIPES_Machines.wireTier5, RECIPES_Machines.circuitTier4, RECIPES_Machines.RECIPE_BufferCore_EV,
					RECIPES_Machines.circuitTier4, RECIPES_Machines.RECIPE_Buffer_EV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier6, RECIPES_Machines.RECIPE_BufferCore_IV,
					RECIPES_Machines.wireTier6, RECIPES_Machines.wireTier6, RECIPES_Machines.machineCasing_IV,
					RECIPES_Machines.wireTier6, RECIPES_Machines.circuitTier5, RECIPES_Machines.RECIPE_BufferCore_IV,
					RECIPES_Machines.circuitTier5, RECIPES_Machines.RECIPE_Buffer_IV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier7, RECIPES_Machines.RECIPE_BufferCore_LuV,
					RECIPES_Machines.wireTier7, RECIPES_Machines.wireTier7, RECIPES_Machines.machineCasing_LuV,
					RECIPES_Machines.wireTier7, RECIPES_Machines.circuitTier6, RECIPES_Machines.RECIPE_BufferCore_LuV,
					RECIPES_Machines.circuitTier6, RECIPES_Machines.RECIPE_Buffer_LuV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier8, RECIPES_Machines.RECIPE_BufferCore_ZPM,
					RECIPES_Machines.wireTier8, RECIPES_Machines.wireTier8, RECIPES_Machines.machineCasing_ZPM,
					RECIPES_Machines.wireTier8, RECIPES_Machines.circuitTier7, RECIPES_Machines.RECIPE_BufferCore_ZPM,
					RECIPES_Machines.circuitTier7, RECIPES_Machines.RECIPE_Buffer_ZPM);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.wireTier9, RECIPES_Machines.RECIPE_BufferCore_UV,
					RECIPES_Machines.wireTier9, RECIPES_Machines.wireTier9, RECIPES_Machines.machineCasing_UV,
					RECIPES_Machines.wireTier9, RECIPES_Machines.circuitTier8, RECIPES_Machines.RECIPE_BufferCore_UV,
					RECIPES_Machines.circuitTier8, RECIPES_Machines.RECIPE_Buffer_UV);
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier11, RECIPES_Machines.RECIPE_BufferCore_MAX,
					RECIPES_Machines.plateTier11, RECIPES_Machines.wireTier10, RECIPES_Machines.machineCasing_MAX,
					RECIPES_Machines.wireTier10, RECIPES_Machines.circuitTier9, RECIPES_Machines.RECIPE_BufferCore_MAX,
					RECIPES_Machines.circuitTier9, RECIPES_Machines.RECIPE_Buffer_MAX);

			// Steam Condenser
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.pipeLargeCopper, RECIPES_Machines.pipeHugeSteel,
					RECIPES_Machines.pipeLargeCopper, RECIPES_Machines.plateEnergeticAlloy,
					RECIPES_Machines.electricPump_HV, RECIPES_Machines.plateEnergeticAlloy,
					RECIPES_Machines.plateEnergeticAlloy, RECIPES_Machines.pipeLargeCopper,
					RECIPES_Machines.plateEnergeticAlloy, RECIPES_Machines.RECIPE_SteamCondenser);

			// Iron BF
			RecipeUtils.addShapedGregtechRecipe("plateDoubleAnyIron", "craftingFurnace", "plateDoubleAnyIron",
					RECIPES_Machines.boiler_Coal, RECIPES_Machines.machineCasing_ULV, RECIPES_Machines.boiler_Coal,
					"plateDoubleAnyIron", "bucketLava", "plateDoubleAnyIron", RECIPES_Machines.RECIPE_IronBlastFurnace);
			// Iron plated Bricks
			RecipeUtils.addShapedGregtechRecipe("plateAnyIron", RECIPES_Tools.craftingToolHardHammer, "plateAnyIron",
					"plateAnyIron", RECIPES_Machines.blockBricks, "plateAnyIron", "plateAnyIron",
					RECIPES_Tools.craftingToolWrench, "plateAnyIron", RECIPES_Machines.RECIPE_IronPlatedBricks);

			/*
			 * //Electrolyzer Frame Casing UtilsRecipe.addShapedGregtechRecipe(
			 * "platePotin", "stickLongChrome", "platePotin", "stickLongPotin",
			 * "frameGtPotin", "stickLongPotin", "platePotin", "stickLongPotin",
			 * "platePotin", RECIPE_IndustrialCentrifugeCasing); //Industrial
			 * Electrolyzer UtilsRecipe.addShapedGregtechRecipe(
			 * "plateStellite", circuitTier6, "plateStellite", machineCasing_EV,
			 * IV_MACHINE_Electrolyzer, machineCasing_EV, "plateStellite",
			 * "rotorStellite", "plateStellite",
			 * RECIPE_IndustrialCentrifugeController);
			 */

			// Industrial Centrifuge
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.circuitTier6, RECIPES_Machines.pipeHugeStainlessSteel,
					RECIPES_Machines.circuitTier6, RECIPES_Machines.plateTier6, RECIPES_Machines.IV_MACHINE_Centrifuge,
					RECIPES_Machines.plateTier6, RECIPES_Machines.plateTier8, RECIPES_Machines.machineCasing_IV,
					RECIPES_Machines.plateTier8, RECIPES_Machines.RECIPE_IndustrialCentrifugeController);
			// Centrifuge Casing
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier6, "stickTumbaga",
					RECIPES_Machines.plateTier6, RECIPES_Machines.plateTier8, "stickTumbaga",
					RECIPES_Machines.plateTier8, RECIPES_Machines.plateTier6, "stickTumbaga",
					RECIPES_Machines.plateTier6, RECIPES_Machines.RECIPE_IndustrialCentrifugeCasing);

			if (LoadedMods.Railcraft) {
				// Industrial Coke Oven
				RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateCobalt, RECIPES_Machines.circuitTier4,
						RECIPES_Machines.plateCobalt, RECIPES_Machines.machineCasing_HV,
						RECIPES_Machines.INPUT_RCCokeOvenBlock, RECIPES_Machines.machineCasing_HV,
						RECIPES_Machines.plateCobalt, RECIPES_Machines.circuitTier5, RECIPES_Machines.plateCobalt,
						RECIPES_Machines.RECIPE_IndustrialCokeOvenController);
			}
			if (LoadedMods.ImmersiveEngineering) {
				// Industrial Coke Oven
				RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier8, RECIPES_Machines.circuitTier4,
						RECIPES_Machines.plateTier8, RECIPES_Machines.machineCasing_HV,
						RECIPES_Machines.INPUT_IECokeOvenBlock, RECIPES_Machines.machineCasing_HV,
						RECIPES_Machines.plateTier8, RECIPES_Machines.circuitTier3, RECIPES_Machines.plateTier8,
						RECIPES_Machines.RECIPE_IndustrialCokeOvenController);
			}
			// Coke Oven Frame Casing
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier7, RECIPES_Machines.rodTier7,
					RECIPES_Machines.plateTier7, RECIPES_Machines.rodTier7, "frameGtTantalloy61",
					RECIPES_Machines.rodTier7, RECIPES_Machines.plateTier7, RECIPES_Machines.rodTier7,
					RECIPES_Machines.plateTier7, RECIPES_Machines.RECIPE_IndustrialCokeOvenFrame);
			// Coke Oven Coil 1
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateBronze, RECIPES_Machines.plateBronze,
					RECIPES_Machines.plateBronze, "frameGtBronze", RECIPES_Machines.gearboxCasing_Tier_1,
					"frameGtBronze", RECIPES_Machines.plateBronze, RECIPES_Machines.plateBronze,
					RECIPES_Machines.plateBronze, RECIPES_Machines.RECIPE_IndustrialCokeOvenCasingA);
			// Coke Oven Coil 2
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateSteel, RECIPES_Machines.plateSteel,
					RECIPES_Machines.plateSteel, "frameGtSteel", RECIPES_Machines.gearboxCasing_Tier_2, "frameGtSteel",
					RECIPES_Machines.plateSteel, RECIPES_Machines.plateSteel, RECIPES_Machines.plateSteel,
					RECIPES_Machines.RECIPE_IndustrialCokeOvenCasingB);

			// Electrolyzer Frame Casing
			RecipeUtils.addShapedGregtechRecipe("platePotin", "stickLongChrome", "platePotin", "stickLongPotin",
					"frameGtPotin", "stickLongPotin", "platePotin", "stickLongPotin", "platePotin",
					RECIPES_Machines.RECIPE_IndustrialElectrolyzerFrame);
			// Industrial Electrolyzer
			RecipeUtils.addShapedGregtechRecipe("plateStellite", RECIPES_Machines.circuitTier6, "plateStellite",
					RECIPES_Machines.machineCasing_EV, RECIPES_Machines.IV_MACHINE_Electrolyzer,
					RECIPES_Machines.machineCasing_EV, "plateStellite", "rotorStellite", "plateStellite",
					RECIPES_Machines.RECIPE_IndustrialElectrolyzerController);

			// Material Press Frame Casing
			RecipeUtils.addShapedGregtechRecipe("plateTitanium", "stickLongTumbaga", "plateTitanium",
					"stickTantalloy60", "frameGtTumbaga", "stickTantalloy60", "plateTitanium", "stickLongTumbaga",
					"plateTitanium", RECIPES_Machines.RECIPE_IndustrialMaterialPressFrame);
			// Industrial Material Press
			RecipeUtils.addShapedGregtechRecipe("plateTitanium", RECIPES_Machines.circuitTier5, "plateTitanium",
					RECIPES_Machines.machineCasing_EV, RECIPES_Machines.IV_MACHINE_BendingMachine,
					RECIPES_Machines.machineCasing_EV, "plateTitanium", RECIPES_Machines.circuitTier5, "plateTitanium",
					RECIPES_Machines.RECIPE_IndustrialMaterialPressController);

			// Maceration Frame Casing
			RecipeUtils.addShapedGregtechRecipe("platePalladium", "platePalladium", "platePalladium", "stickPlatinum",
					"frameGtInconel625", "stickPlatinum", "platePalladium", "stickLongPalladium", "platePalladium",
					RECIPES_Machines.RECIPE_IndustrialMacerationStackFrame);
			// Industrial Maceration stack
			RecipeUtils.addShapedGregtechRecipe("plateTungstenCarbide", RECIPES_Machines.IV_MACHINE_Macerator,
					"plateTungstenCarbide", RECIPES_Machines.IV_MACHINE_Macerator, RECIPES_Machines.circuitTier8,
					RECIPES_Machines.IV_MACHINE_Macerator, "plateTungstenCarbide", RECIPES_Machines.machineCasing_IV,
					"plateTungstenCarbide", RECIPES_Machines.RECIPE_IndustrialMacerationStackController);

			// Wire Factory Frame Casing
			RecipeUtils.addShapedGregtechRecipe("plateBlueSteel", "stickBlueSteel", "plateBlueSteel", "stickBlueSteel",
					"frameGtBlueSteel", "stickBlueSteel", "plateBlueSteel", "stickBlueSteel", "plateBlueSteel",
					RECIPES_Machines.RECIPE_IndustrialWireFactoryFrame);
			// Industrial Wire Factory
			RecipeUtils.addShapedGregtechRecipe("plateZeron100", RECIPES_Machines.machineCasing_IV, "plateZeron100",
					RECIPES_Machines.circuitTier6, RECIPES_Machines.IV_MACHINE_Wiremill, RECIPES_Machines.circuitTier6,
					"plateZeron100", RECIPES_Machines.machineCasing_IV, "plateZeron100",
					RECIPES_Machines.RECIPE_IndustrialWireFactoryController);

			// Tiered Tanks
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier1, RECIPES_Machines.plateTier1,
					RECIPES_Machines.plateTier1, RECIPES_Machines.plateTier1, RECIPES_Machines.pipeTier1,
					RECIPES_Machines.plateTier1, RECIPES_Machines.plateTier1, GregtechItemList.Fluid_Cell_144L.get(1),
					RECIPES_Machines.plateTier1, GregtechItemList.GT_FluidTank_ULV.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier2, RECIPES_Machines.plateTier2,
					RECIPES_Machines.plateTier2, RECIPES_Machines.plateTier2, RECIPES_Machines.pipeTier2,
					RECIPES_Machines.plateTier2, RECIPES_Machines.plateTier2, RECIPES_Machines.electricPump_LV,
					RECIPES_Machines.plateTier2, GregtechItemList.GT_FluidTank_LV.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier3, RECIPES_Machines.plateTier3,
					RECIPES_Machines.plateTier3, RECIPES_Machines.plateTier3, RECIPES_Machines.pipeTier3,
					RECIPES_Machines.plateTier3, RECIPES_Machines.plateTier3, RECIPES_Machines.electricPump_MV,
					RECIPES_Machines.plateTier3, GregtechItemList.GT_FluidTank_MV.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier4, RECIPES_Machines.plateTier4,
					RECIPES_Machines.plateTier4, RECIPES_Machines.plateTier4, RECIPES_Machines.pipeTier4,
					RECIPES_Machines.plateTier4, RECIPES_Machines.plateTier4, RECIPES_Machines.electricPump_HV,
					RECIPES_Machines.plateTier4, GregtechItemList.GT_FluidTank_HV.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier5, RECIPES_Machines.plateTier5,
					RECIPES_Machines.plateTier5, RECIPES_Machines.plateTier5, RECIPES_Machines.pipeTier5,
					RECIPES_Machines.plateTier5, RECIPES_Machines.plateTier5, RECIPES_Machines.electricPump_EV,
					RECIPES_Machines.plateTier5, GregtechItemList.GT_FluidTank_EV.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier6, RECIPES_Machines.plateTier6,
					RECIPES_Machines.plateTier6, RECIPES_Machines.plateTier6, RECIPES_Machines.pipeTier6,
					RECIPES_Machines.plateTier6, RECIPES_Machines.plateTier6, RECIPES_Machines.electricPump_IV,
					RECIPES_Machines.plateTier6, GregtechItemList.GT_FluidTank_IV.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier7, RECIPES_Machines.plateTier7,
					RECIPES_Machines.plateTier7, RECIPES_Machines.plateTier7, RECIPES_Machines.pipeTier7,
					RECIPES_Machines.plateTier7, RECIPES_Machines.plateTier7, RECIPE_CONSTANTS.electricPump_LuV,
					RECIPES_Machines.plateTier7, GregtechItemList.GT_FluidTank_LuV.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier8, RECIPES_Machines.plateTier8,
					RECIPES_Machines.plateTier8, RECIPES_Machines.plateTier8, RECIPES_Machines.pipeTier8,
					RECIPES_Machines.plateTier8, RECIPES_Machines.plateTier8, RECIPE_CONSTANTS.electricPump_ZPM,
					RECIPES_Machines.plateTier8, GregtechItemList.GT_FluidTank_ZPM.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier9, RECIPES_Machines.plateTier9,
					RECIPES_Machines.plateTier9, RECIPES_Machines.plateTier9, RECIPES_Machines.pipeTier9,
					RECIPES_Machines.plateTier9, RECIPES_Machines.plateTier9, RECIPE_CONSTANTS.electricPump_UV,
					RECIPES_Machines.plateTier9, GregtechItemList.GT_FluidTank_UV.get(1));
			RecipeUtils.addShapedGregtechRecipe(RECIPES_Machines.plateTier10, RECIPES_Machines.plateTier10,
					RECIPES_Machines.plateTier10, RECIPES_Machines.plateTier10, RECIPES_Machines.pipeTier10,
					RECIPES_Machines.plateTier10, RECIPES_Machines.plateTier10, RECIPE_CONSTANTS.electricPump_MAX,
					RECIPES_Machines.plateTier10, GregtechItemList.GT_FluidTank_MAX.get(1));

			// Blast Smelter
			RecipeUtils.addShapedGregtechRecipe("plateZirconiumCarbide", RECIPES_Machines.circuitTier4,
					"plateZirconiumCarbide", RECIPES_Machines.cableTier4, RECIPES_Machines.machineCasing_EV,
					RECIPES_Machines.cableTier4, "plateZirconiumCarbide", RECIPES_Machines.circuitTier3,
					"plateZirconiumCarbide", RECIPES_Machines.RECIPE_IndustrialBlastSmelterController);
			// Blast Smelter Frame Casing
			RecipeUtils.addShapedGregtechRecipe("plateZirconiumCarbide", RECIPES_Machines.rodTier5,
					"plateZirconiumCarbide", RECIPES_Machines.rodTier5, "frameGtTumbaga", RECIPES_Machines.rodTier5,
					"plateZirconiumCarbide", RECIPES_Machines.rodTier5, "plateZirconiumCarbide",
					RECIPES_Machines.RECIPE_IndustrialBlastSmelterFrame);
			// Blast Smelter Coil
			RecipeUtils.addShapedGregtechRecipe("plateStaballoy", "plateStaballoy", "plateStaballoy",
					"frameGtStaballoy", RECIPES_Machines.gearboxCasing_Tier_3, "frameGtStaballoy", "plateStaballoy",
					"plateStaballoy", "plateStaballoy", RECIPES_Machines.RECIPE_IndustrialBlastSmelterCoil);

		}

		Utils.LOG_INFO("Done loading recipes for the Various machine blocks.");
	}
}
