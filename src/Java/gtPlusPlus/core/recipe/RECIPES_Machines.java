package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gtPlusPlus.core.item.ModItems;
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
	//Tesseracts
	public static ItemStack RECIPE_TesseractGenerator;
	public static ItemStack RECIPE_TesseractTerminal;
	//Thermal Boiler
	public static ItemStack RECIPE_ThermalBoilerController;
	public static ItemStack RECIPE_ThermalBoilerCasing;

	//Thorium Reactor
	public static ItemStack RECIPE_LFTRController;
	public static ItemStack RECIPE_LFTROuterCasing;
	public static ItemStack RECIPE_LFTRInnerCasing;


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

	//IV MACHINES
	public static ItemStack EV_MACHINE_Electrolyzer;
	public static ItemStack EV_MACHINE_Centrifuge;
	public static ItemStack EV_MACHINE_BendingMachine;
	public static ItemStack EV_MACHINE_Wiremill;
	public static ItemStack HV_MACHINE_Macerator;
	public static ItemStack EV_MACHINE_Macerator;
	public static ItemStack EV_MACHINE_MassFabricator;


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




			//Lava Boiler
			boiler_Coal = ItemList.Machine_Bronze_Boiler.get(1);

			//IV MACHINES
			EV_MACHINE_Electrolyzer = ItemList.Machine_EV_Electrolyzer.get(1);
			EV_MACHINE_BendingMachine= ItemList.Machine_EV_Bender.get(1);
			EV_MACHINE_Wiremill= ItemList.Machine_EV_Wiremill.get(1);
			HV_MACHINE_Macerator= ItemList.Machine_HV_Macerator.get(1);
			EV_MACHINE_Macerator= ItemList.Machine_EV_Macerator.get(1);
			EV_MACHINE_MassFabricator= ItemList.Machine_EV_Massfab.get(1);
			EV_MACHINE_Centrifuge= ItemList.Machine_EV_Centrifuge.get(1);


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

			//Computer Cube
			RecipeUtils.addShapedGregtechRecipe(
					CI.getDataOrb(),ItemList.Cover_Screen.get(1),"circuitMaster",
					ItemList.Cover_Screen.get(1),CI.machineHull_EV,ItemList.Cover_Screen.get(1),
					"circuitMaster",ItemList.Cover_Screen.get(1),CI.getDataOrb(),
					GregtechItemList.Gregtech_Computer_Cube.get(1));

			RecipeUtils.addShapedGregtechRecipe(
					ItemList.Electric_Piston_EV, OrePrefixes.circuit.get(Materials.Ultimate), ItemList.Electric_Piston_EV,
					ItemList.Electric_Motor_EV, CI.machineCasing_EV, ItemList.Electric_Motor_EV,
					"gearGtTitanium", "cableGt02Aluminium", "gearGtTitanium",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 793, 1));
			RecipeUtils.addShapedGregtechRecipe(
					ItemList.Electric_Piston_IV, OrePrefixes.circuit.get(Materials.Superconductor), ItemList.Electric_Piston_IV,
					ItemList.Electric_Motor_IV, CI.machineCasing_IV, ItemList.Electric_Motor_IV,
					"gearGtTungstenSteel", "cableGt02Platinum", "gearGtTungstenSteel",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 794, 1));
			RecipeUtils.addShapedGregtechRecipe(
					CI.electricPiston_LuV, OrePrefixes.circuit.get(Materials.Infinite), CI.electricPiston_LuV,
					CI.electricMotor_LuV, CI.machineCasing_LuV, CI.electricMotor_LuV,
					"gearGtChrome", "cableGt02Tungsten", "gearGtChrome",
					ItemUtils.simpleMetaStack("gregtech:gt.blockmachines", 795, 1));

			//Buffer Core
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[1], cableTier1, CI.component_Plate[1],
					CI.circuitPrimitive, IC2MFE, CI.circuitPrimitive,
					CI.component_Plate[1], cableTier1, CI.component_Plate[1],
					RECIPE_BufferCore_ULV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[2], cableTier2, CI.component_Plate[2],
					CI.circuitTier1, IC2MFE, CI.circuitTier1,
					CI.component_Plate[2], cableTier2, CI.component_Plate[2],
					RECIPE_BufferCore_LV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[3], cableTier3, CI.component_Plate[3],
					RECIPE_BufferCore_LV, CI.circuitTier2, RECIPE_BufferCore_LV,
					CI.component_Plate[3], cableTier3, CI.component_Plate[3],
					RECIPE_BufferCore_MV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[4], cableTier4, CI.component_Plate[4],
					RECIPE_BufferCore_MV, CI.circuitTier3, RECIPE_BufferCore_MV,
					CI.component_Plate[4], cableTier4, CI.component_Plate[4],
					RECIPE_BufferCore_HV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[5], cableTier5, CI.component_Plate[5],
					RECIPE_BufferCore_HV, CI.circuitTier4, RECIPE_BufferCore_HV,
					CI.component_Plate[5], cableTier5, CI.component_Plate[5],
					RECIPE_BufferCore_EV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[6], cableTier6, CI.component_Plate[6],
					RECIPE_BufferCore_EV, CI.circuitTier5, RECIPE_BufferCore_EV,
					CI.component_Plate[6], cableTier6, CI.component_Plate[6],
					RECIPE_BufferCore_IV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[7], cableTier7, CI.component_Plate[7],
					RECIPE_BufferCore_IV, CI.circuitTier6, RECIPE_BufferCore_IV,
					CI.component_Plate[7], cableTier7, CI.component_Plate[7],
					RECIPE_BufferCore_LuV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[8], cableTier8, CI.component_Plate[8],
					RECIPE_BufferCore_LuV, CI.circuitTier7, RECIPE_BufferCore_LuV,
					CI.component_Plate[8], cableTier8, CI.component_Plate[8],
					RECIPE_BufferCore_ZPM);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[9], cableTier9, CI.component_Plate[9],
					RECIPE_BufferCore_ZPM, CI.circuitTier8, RECIPE_BufferCore_ZPM,
					CI.component_Plate[9], cableTier9, CI.component_Plate[9],
					RECIPE_BufferCore_UV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[10], cableTier10, CI.component_Plate[10],
					RECIPE_BufferCore_UV, CI.circuitTier9, RECIPE_BufferCore_UV,
					CI.component_Plate[10], cableTier10, CI.component_Plate[10],
					RECIPE_BufferCore_MAX);


			RecipeUtils.addShapedGregtechRecipe(
					wireTier1, RECIPE_BufferCore_ULV, wireTier1,
					wireTier1, CI.machineCasing_ULV, wireTier1,
					CI.circuitPrimitive, CI.circuitTier1, CI.circuitPrimitive,
					RECIPE_Buffer_ULV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier2, RECIPE_BufferCore_LV, wireTier2,
					wireTier2, CI.machineCasing_LV, wireTier2,
					CI.circuitTier1, RECIPE_BufferCore_LV, CI.circuitTier1,
					RECIPE_Buffer_LV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier3, RECIPE_BufferCore_MV, wireTier3,
					wireTier3, CI.machineCasing_MV, wireTier3,
					CI.circuitTier2, RECIPE_BufferCore_MV, CI.circuitTier2,
					RECIPE_Buffer_MV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier4, RECIPE_BufferCore_HV, wireTier4,
					wireTier4, CI.machineCasing_HV, wireTier4,
					CI.circuitTier3, RECIPE_BufferCore_HV, CI.circuitTier3,
					RECIPE_Buffer_HV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier5, RECIPE_BufferCore_EV, wireTier5,
					wireTier5, CI.machineCasing_EV, wireTier5,
					CI.circuitTier4, RECIPE_BufferCore_EV, CI.circuitTier4,
					RECIPE_Buffer_EV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier6, RECIPE_BufferCore_IV, wireTier6,
					wireTier6, CI.machineCasing_IV, wireTier6,
					CI.circuitTier5, RECIPE_BufferCore_IV, CI.circuitTier5,
					RECIPE_Buffer_IV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier7, RECIPE_BufferCore_LuV, wireTier7,
					wireTier7, CI.machineCasing_LuV, wireTier7,
					CI.circuitTier6, RECIPE_BufferCore_LuV, CI.circuitTier6,
					RECIPE_Buffer_LuV);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier8, RECIPE_BufferCore_ZPM, wireTier8,
					wireTier8, CI.machineCasing_ZPM, wireTier8,
					CI.circuitTier7, RECIPE_BufferCore_ZPM, CI.circuitTier7,
					RECIPE_Buffer_ZPM);
			RecipeUtils.addShapedGregtechRecipe(
					wireTier9, RECIPE_BufferCore_UV, wireTier9,
					wireTier9, CI.machineCasing_UV, wireTier9,
					CI.circuitTier8, RECIPE_BufferCore_UV, CI.circuitTier8,
					RECIPE_Buffer_UV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[11], RECIPE_BufferCore_MAX, CI.component_Plate[11],
					wireTier10, CI.machineCasing_MAX, wireTier10,
					CI.circuitTier9, RECIPE_BufferCore_MAX, CI.circuitTier9,
					RECIPE_Buffer_MAX);

			//Steam Condenser
			if (CORE.configSwitches.enableMachine_SteamConverter ){
				RECIPE_SteamCondenser = GregtechItemList.Condensor_MAX.get(1);
				RecipeUtils.addShapedGregtechRecipe(
						pipeLargeCopper, pipeHugeSteel, pipeLargeCopper,
						plateEnergeticAlloy, CI.electricPump_HV, plateEnergeticAlloy,
						plateEnergeticAlloy, pipeLargeCopper, plateEnergeticAlloy,
						RECIPE_SteamCondenser);
			}


			if (CORE.configSwitches.enableMultiblock_IronBlastFurnace){

				RECIPE_IronBlastFurnace = GregtechItemList.Machine_Iron_BlastFurnace.get(1);
				RECIPE_IronPlatedBricks = GregtechItemList.Casing_IronPlatedBricks.get(1);

				//Iron BF
				RecipeUtils.addShapedGregtechRecipe(
						"plateDoubleAnyIron", "craftingFurnace", "plateDoubleAnyIron",
						boiler_Coal, CI.machineCasing_ULV, boiler_Coal,
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
						CI.circuitTier5, pipeHugeStainlessSteel, CI.circuitTier5,
						CI.component_Plate[6], EV_MACHINE_Centrifuge, CI.component_Plate[6],
						CI.component_Plate[8], CI.machineCasing_IV, CI.component_Plate[8],
						RECIPE_IndustrialCentrifugeController);
				//Centrifuge Casing
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[6], "stickTumbaga", CI.component_Plate[6],
						CI.component_Plate[8], "stickTumbaga", CI.component_Plate[8],
						CI.component_Plate[6], "stickTumbaga", CI.component_Plate[6],
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
							CI.machineCasing_HV, INPUT_RCCokeOvenBlock, CI.machineCasing_HV,
							plateCobalt, CI.circuitTier5, plateCobalt,
							RECIPE_IndustrialCokeOvenController);
				}
				if (LoadedMods.ImmersiveEngineering){
					//Industrial Coke Oven
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[8], CI.circuitTier4, CI.component_Plate[8],
							CI.machineCasing_HV, INPUT_IECokeOvenBlock, CI.machineCasing_HV,
							CI.component_Plate[8], CI.circuitTier3, CI.component_Plate[8],
							RECIPE_IndustrialCokeOvenController);
				}
				//Coke Oven Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[7], CI.component_Rod[7], CI.component_Plate[7],
						CI.component_Rod[7], "frameGtTantalloy61", CI.component_Rod[7],
						CI.component_Plate[7], CI.component_Rod[7], CI.component_Plate[7],
						RECIPE_IndustrialCokeOvenFrame);
				//Coke Oven Coil 1
				RecipeUtils.addShapedGregtechRecipe(
						plateBronze, plateBronze, plateBronze,
						"frameGtBronze", CI.gearboxCasing_Tier_1, "frameGtBronze",
						plateBronze, plateBronze, plateBronze,
						RECIPE_IndustrialCokeOvenCasingA);
				//Coke Oven Coil 2
				RecipeUtils.addShapedGregtechRecipe(
						plateSteel, plateSteel, plateSteel,
						"frameGtSteel", CI.gearboxCasing_Tier_2, "frameGtSteel",
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
						"plateStellite", CI.circuitTier5, "plateStellite",
						CI.machineCasing_EV, EV_MACHINE_Electrolyzer, CI.machineCasing_EV,
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
						CI.machineCasing_EV, EV_MACHINE_BendingMachine, CI.machineCasing_EV,
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
						"plateTungstenCarbide", EV_MACHINE_Macerator, "plateTungstenCarbide",
						HV_MACHINE_Macerator, CI.circuitTier7, HV_MACHINE_Macerator,
						"plateTungstenCarbide", CI.machineCasing_IV, "plateTungstenCarbide",
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
						"plateZeron100", CI.machineCasing_IV, "plateZeron100",
						CI.circuitTier5, EV_MACHINE_Wiremill, CI.circuitTier5,
						"plateZeron100", CI.machineCasing_IV, "plateZeron100",
						RECIPE_IndustrialWireFactoryController);
			}



			//Tiered Tanks
			if (CORE.configSwitches.enableMachine_FluidTanks){
				Utils.LOG_WARNING("Is New Horizons Loaded? "+CORE.GTNH);
				if (!CORE.GTNH){
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[1], CI.component_Plate[1], CI.component_Plate[1],
							CI.component_Plate[1], pipeTier1, CI.component_Plate[1],
							CI.component_Plate[1], GregtechItemList.Fluid_Cell_144L.get(1), CI.component_Plate[1],
							GregtechItemList.GT_FluidTank_ULV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[2], CI.component_Plate[2], CI.component_Plate[2],
							CI.component_Plate[2], pipeTier2, CI.component_Plate[2],
							CI.component_Plate[2], CI.electricPump_LV, CI.component_Plate[2],
							GregtechItemList.GT_FluidTank_LV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[3], CI.component_Plate[3], CI.component_Plate[3],
							CI.component_Plate[3], pipeTier3, CI.component_Plate[3],
							CI.component_Plate[3], CI.electricPump_MV, CI.component_Plate[3],
							GregtechItemList.GT_FluidTank_MV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[4], CI.component_Plate[4], CI.component_Plate[4],
							CI.component_Plate[4], pipeTier4, CI.component_Plate[4],
							CI.component_Plate[4], CI.electricPump_HV, CI.component_Plate[4],
							GregtechItemList.GT_FluidTank_HV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[5], CI.component_Plate[5], CI.component_Plate[5],
							CI.component_Plate[5], pipeTier5, CI.component_Plate[5],
							CI.component_Plate[5], CI.electricPump_EV, CI.component_Plate[5],
							GregtechItemList.GT_FluidTank_EV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[6], CI.component_Plate[6], CI.component_Plate[6],
							CI.component_Plate[6], pipeTier6, CI.component_Plate[6],
							CI.component_Plate[6], CI.electricPump_IV, CI.component_Plate[6],
							GregtechItemList.GT_FluidTank_IV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[7], CI.component_Plate[7], CI.component_Plate[7],
							CI.component_Plate[7], pipeTier7, CI.component_Plate[7],
							CI.component_Plate[7], CI.electricPump_LuV, CI.component_Plate[7],
							GregtechItemList.GT_FluidTank_LuV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[8], CI.component_Plate[8], CI.component_Plate[8],
							CI.component_Plate[8], pipeTier8, CI.component_Plate[8],
							CI.component_Plate[8], CI.electricPump_ZPM, CI.component_Plate[8],
							GregtechItemList.GT_FluidTank_ZPM.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[9], CI.component_Plate[9], CI.component_Plate[9],
							CI.component_Plate[9], pipeTier9, CI.component_Plate[9],
							CI.component_Plate[9], CI.electricPump_UV, CI.component_Plate[9],
							GregtechItemList.GT_FluidTank_UV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[10], CI.component_Plate[10], CI.component_Plate[10],
							CI.component_Plate[10], pipeTier10, CI.component_Plate[10],
							CI.component_Plate[10], CI.electricPump_MAX, CI.component_Plate[10],
							GregtechItemList.GT_FluidTank_MAX.get(1));
				}
				else {

					CI.component_Plate[1] = "plateTin";
					pipeTier1 = "pipeLargeClay";
					CI.circuitTier1 = ItemList.Circuit_Primitive.get(1);
					CI.component_Plate[2] = "plateCopper";
					pipeTier2 = "pipeHugeClay";
					CI.component_Plate[3] = "plateBronze";
					pipeTier3 = "pipeMediumBronze";
					CI.component_Plate[4] = "plateIron";
					pipeTier4 = "pipeMediumSteel";
					CI.component_Plate[5] = "plateSteel";
					CI.component_Plate[6] = "plateRedstone";
					CI.component_Plate[7] = "plateAluminium";
					CI.component_Plate[8] = "plateDarkSteel";
					ItemStack waterBucket = ItemUtils.getSimpleStack(Items.water_bucket);

					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[1], CI.component_Plate[5], CI.component_Plate[1],
							CI.component_Plate[4], pipeTier1, CI.component_Plate[4],
							CI.component_Plate[4], waterBucket, CI.component_Plate[4],
							GregtechItemList.GT_FluidTank_ULV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[5], CI.component_Plate[4], CI.component_Plate[5],
							CI.component_Plate[3], pipeTier2, CI.component_Plate[3],
							CI.component_Plate[3], CI.electricPump_LV, CI.component_Plate[3],
							GregtechItemList.GT_FluidTank_LV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.component_Plate[8], CI.component_Plate[3], CI.component_Plate[8],
							CI.component_Plate[5], pipeTier3, CI.component_Plate[5],
							CI.component_Plate[5], CI.electricPump_LV, CI.component_Plate[5],
							GregtechItemList.GT_FluidTank_MV.get(1));
					RecipeUtils.addShapedGregtechRecipe(
							CI.circuitTier1, CI.component_Plate[7], CI.circuitTier1,
							CI.component_Plate[8], pipeTier4, CI.component_Plate[8],
							CI.circuitTier1, CI.electricPump_MV, CI.circuitTier1,
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
						cableTier4, CI.machineCasing_EV, cableTier4,
						"plateZirconiumCarbide", CI.circuitTier3, "plateZirconiumCarbide",
						RECIPE_IndustrialBlastSmelterController);
				//Blast Smelter Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
						"plateZirconiumCarbide", CI.component_Rod[5], "plateZirconiumCarbide",
						CI.component_Rod[5], "frameGtTumbaga", CI.component_Rod[5],
						"plateZirconiumCarbide", CI.component_Rod[5], "plateZirconiumCarbide",
						RECIPE_IndustrialBlastSmelterFrame);
				//Blast Smelter Coil
				RecipeUtils.addShapedGregtechRecipe(
						"plateStaballoy", "plateStaballoy", "plateStaballoy",
						"frameGtStaballoy", CI.gearboxCasing_Tier_3, "frameGtStaballoy",
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
						cableTier8, CI.machineCasing_LuV, cableTier8,
						"plateDoubleQuantum", CI.circuitTier5, "plateDoubleQuantum",
						RECIPE_IndustrialMatterFabController);
				//Matter Fabricator Frame Casing
				RecipeUtils.addShapedGregtechRecipe(
						"plateNiobiumCarbide", CI.component_Rod[8], "plateNiobiumCarbide",
						CI.component_Rod[8], "frameGtInconel690", CI.component_Rod[8],
						"plateNiobiumCarbide", CI.component_Rod[8], "plateNiobiumCarbide",
						RECIPE_IndustrialMatterFabFrame);
				//Matter Fabricator Coil
				RecipeUtils.addShapedGregtechRecipe(
						"plateQuantum", "plateQuantum", "plateQuantum",
						"frameGtStellite", CI.machineCasing_UV, "frameGtStellite",
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
						cableTier3, CI.machineCasing_MV, cableTier3,
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
						"plateEglinSteel", CI.machineCasing_MV, "plateEglinSteel",
						RECIPE_TreeFarmController);
				//Industrial Tree Farm Frame
				RecipeUtils.addShapedGregtechRecipe(
						ItemUtils.getSimpleStack(Blocks.dirt), ItemUtils.getSimpleStack(Blocks.dirt), ItemUtils.getSimpleStack(Blocks.dirt),
						"plankWood", "frameGtTumbaga", "plankWood",
						"plankWood", "plankWood", "plankWood",
						RECIPE_TreeFarmFrame);
			}

			if (CORE.configSwitches.enableMachine_Tesseracts){
				//Tesseracts
				RECIPE_TesseractGenerator = GregtechItemList.GT4_Tesseract_Generator.get(1);
				RECIPE_TesseractTerminal = GregtechItemList.GT4_Tesseract_Terminal.get(1);
				//Tesseract Generator
				RecipeUtils.addShapedGregtechRecipe(
						"plateTitanium","circuitMaster","plateTitanium",
						"circuitMaster",ItemUtils.getSimpleStack(Blocks.ender_chest),"circuitMaster",
						"plateTitanium",GregtechItemList.Gregtech_Computer_Cube.get(1),"plateTitanium",
						RECIPE_TesseractGenerator);
				//Tesseract Terminal
				RecipeUtils.addShapedGregtechRecipe(
						"plateTitanium","circuitElite","plateTitanium",
						"circuitElite",ItemUtils.getSimpleStack(Blocks.ender_chest),"circuitElite",
						"plateTitanium",CI.machineHull_EV,"plateTitanium",
						RECIPE_TesseractTerminal);
			}

			if (CORE.configSwitches.enableMachine_SimpleWasher){
				ItemStack plateWrought = ItemUtils.getItemStackOfAmountFromOreDict("plateWroughtIron", 1);
				ItemStack washerPipe;
				if (CORE.configSwitches.enableCustom_Pipes){
					washerPipe = ItemUtils.getItemStackOfAmountFromOreDict("pipeLargeClay", 1);
				}
				else {
					washerPipe = ItemUtils.getItemStackOfAmountFromOreDict("pipeLargeCopper", 1);				
				}
				//Add Recipe
				RecipeUtils.addShapedGregtechRecipe(
						plateWrought, CI.electricPump_LV, plateWrought,
						plateWrought, washerPipe, plateWrought,
						plateWrought, CI.machineCasing_ULV, plateWrought,
						GregtechItemList.SimpleDustWasher.get(1));
			}

			if (CORE.configSwitches.enableMachine_Pollution && CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				RecipeUtils.addShapedGregtechRecipe(
						"plateCarbon", "plateCarbon", "plateCarbon",
						"dustCarbon", "dustCarbon", "dustCarbon",
						"plateCarbon", "plateCarbon", "plateCarbon",
						ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1));
				
				RecipeUtils.addShapedGregtechRecipe(
						"plateCarbon", "plateCarbon", "plateCarbon",
						"cellLithiumPeroxide", "dustCarbon", "cellLithiumPeroxide",
						"plateCarbon", "plateCarbon", "plateCarbon",
						ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1));

				//Pollution Detector
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[1], CI.sensor_MV, CI.component_Plate[1],
						CI.component_Plate[2], CI.electricMotor_ULV, CI.component_Plate[2],
						CI.getTieredCircuit(1), CI.machineHull_LV, CI.getTieredCircuit(0),
						GregtechItemList.Pollution_Detector.get(1));

				//ULV
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[0], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1), CI.component_Plate[0],
						CI.component_Plate[0], CI.electricMotor_ULV, CI.component_Plate[0],
						CI.getTieredCircuit(0), CI.machineHull_ULV, CI.getTieredCircuit(0),
						GregtechItemList.Pollution_Cleaner_ULV.get(1));
				//LV
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[1], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1), CI.component_Plate[1],
						CI.component_Plate[1], CI.electricMotor_LV, CI.component_Plate[1],
						CI.getTieredCircuit(1), CI.machineHull_LV, CI.getTieredCircuit(1),
						GregtechItemList.Pollution_Cleaner_LV.get(1));
				//MV
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[2], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1), CI.component_Plate[2],
						CI.component_Plate[2], CI.electricMotor_MV, CI.component_Plate[2],
						CI.getTieredCircuit(2), CI.machineHull_MV, CI.getTieredCircuit(2),
						GregtechItemList.Pollution_Cleaner_MV.get(1));
				//HV
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[3], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1), CI.component_Plate[3],
						CI.component_Plate[3], CI.electricMotor_HV, CI.component_Plate[3],
						CI.getTieredCircuit(3), CI.machineHull_HV, CI.getTieredCircuit(3),
						GregtechItemList.Pollution_Cleaner_HV.get(1));
				//EV
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[4], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 0, 1), CI.component_Plate[4],
						CI.component_Plate[4], CI.electricMotor_EV, CI.component_Plate[4],
						CI.getTieredCircuit(4), CI.machineHull_EV, CI.getTieredCircuit(4),
						GregtechItemList.Pollution_Cleaner_EV.get(1));
				//IV
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[5], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1), CI.component_Plate[5],
						CI.component_Plate[5], CI.electricMotor_IV, CI.component_Plate[5],
						CI.getTieredCircuit(5), CI.machineHull_IV, CI.getTieredCircuit(5),
						GregtechItemList.Pollution_Cleaner_IV.get(1));
				//LuV
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[6], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1), CI.component_Plate[6],
						CI.component_Plate[6], CI.electricMotor_LuV, CI.component_Plate[6],
						CI.getTieredCircuit(6), CI.machineHull_LuV, CI.getTieredCircuit(6),
						GregtechItemList.Pollution_Cleaner_LuV.get(1));
				//ZPM
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[7], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1), CI.component_Plate[7],
						CI.component_Plate[7], CI.electricMotor_ZPM, CI.component_Plate[7],
						CI.getTieredCircuit(7), CI.machineHull_ZPM, CI.getTieredCircuit(7),
						GregtechItemList.Pollution_Cleaner_ZPM.get(1));
				//UV
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[8], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1), CI.component_Plate[8],
						CI.component_Plate[8], CI.electricMotor_UV, CI.component_Plate[8],
						CI.getTieredCircuit(8), CI.machineHull_UV, CI.getTieredCircuit(8),
						GregtechItemList.Pollution_Cleaner_UV.get(1));
				//MAX
				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[9], ItemUtils.simpleMetaStack(ModItems.itemAirFilter, 1, 1), CI.component_Plate[9],
						CI.component_Plate[9], CI.electricMotor_MAX, CI.component_Plate[9],
						CI.getTieredCircuit(9), CI.machineHull_MAX, CI.getTieredCircuit(9),
						GregtechItemList.Pollution_Cleaner_MAX.get(1));


			}

			if (CORE.configSwitches.enableMultiblock_ThermalBoiler){
				RECIPE_ThermalBoilerController = GregtechItemList.GT4_Thermal_Boiler.get(1);
				RECIPE_ThermalBoilerCasing = GregtechItemList.Casing_ThermalContainment.get(4);
				ItemStack centrifugeHV = ItemList.Machine_HV_Centrifuge.get(1);

				RecipeUtils.addShapedGregtechRecipe(
						"craftingGeothermalGenerator", centrifugeHV, "craftingGeothermalGenerator",
						"gearTitanium", "circuitElite", "gearTitanium",
						"craftingGeothermalGenerator", centrifugeHV, "craftingGeothermalGenerator",
						RECIPE_ThermalBoilerController);

				RecipeUtils.addShapedGregtechRecipe(
						"craftingGeothermalGenerator", centrifugeHV, "craftingGeothermalGenerator",
						"gearTungstenSteel", "circuitElite", "gearTungstenSteel",
						"craftingGeothermalGenerator", centrifugeHV, "craftingGeothermalGenerator",
						RECIPE_ThermalBoilerController);

				RecipeUtils.addShapedGregtechRecipe(
						"plateStainlessSteel", "plateStainlessSteel", "plateStainlessSteel",
						"circuitAdvanced", CI.machineCasing_HV, "circuitAdvanced",
						"plateStainlessSteel", "plateStainlessSteel", "plateStainlessSteel",
						RECIPE_ThermalBoilerCasing);

				//Lava Filter Recipe
				GT_Values.RA.addAssemblerRecipe(ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:itemPartCarbonMesh", "RawCarbonMesh", 0, 16), CI.getNumberedCircuit(18), ItemUtils.getSimpleStack(ModItems.itemLavaFilter), 80*20, 16);
			}

			if (CORE.configSwitches.enableMultiblock_LiquidFluorideThoriumReactor){

				//Thorium Reactor
				RECIPE_LFTRController = GregtechItemList.ThoriumReactor.get(1);
				RECIPE_LFTRInnerCasing = GregtechItemList.Casing_Reactor_II.get(1); //Zeron
				RECIPE_LFTROuterCasing = GregtechItemList.Casing_Reactor_I.get(1); //Hastelloy

				ItemStack controlCircuit = ItemUtils.getSimpleStack(ModItems.itemCircuitLFTR);
				RecipeUtils.addShapedGregtechRecipe(
						controlCircuit, "cableGt12NaquadahAlloy", controlCircuit,
						"plateDoubleHastelloyN", GregtechItemList.Gregtech_Computer_Cube.get(1), "plateDoubleHastelloyN",
						"plateThorium232", CI.machineHull_UV, "plateThorium232",
						RECIPE_LFTRController);

				RecipeUtils.addShapedGregtechRecipe(
						"plateDoubleZeron100", CI.craftingToolScrewdriver, "plateDoubleZeron100",
						"gearTalonite", CI.fieldGenerator_ULV, "gearTalonite",
						"plateDoubleZeron100", CI.craftingToolHammer_Hard, "plateDoubleZeron100",
						RECIPE_LFTRInnerCasing);

				ItemStack IC2HeatPlate = ItemUtils.getItemStack("IC2:reactorPlatingHeat", 1);
				RecipeUtils.addShapedGregtechRecipe(
						"plateDoubleHastelloyN", IC2HeatPlate, "plateDoubleHastelloyN",
						IC2HeatPlate, "frameGtHastelloyC276", IC2HeatPlate,
						"plateDoubleHastelloyN", IC2HeatPlate, "plateDoubleHastelloyN",
						RECIPE_LFTROuterCasing);

				//LFTR Control Circuit
				ItemStack circuitT5 = ItemList.Circuit_Master.get(1);
				GT_Values.RA.addAssemblerRecipe(circuitT5, CI.fieldGenerator_HV, controlCircuit, 240*20, 500);


				//Fission Fuel Plant
				RecipeUtils.addShapedGregtechRecipe(
						CI.getTieredCircuit(5), CI.craftingToolSolderingIron, CI.getTieredCircuit(5),
						"plateDenseTungstenSteel", GregtechItemList.Gregtech_Computer_Cube.get(1), "plateDenseTungstenSteel",
						"gearStellite", CI.machineHull_LuV, "gearStellite",
						GregtechItemList.Industrial_FuelRefinery.get(1));

				ItemStack mInnerTank;

				if (CORE.GTNH || !CORE.configSwitches.enableMachine_FluidTanks){
					mInnerTank = ItemList.Quantum_Tank_LV.get(1);
				}
				else {
					mInnerTank = GregtechItemList.GT_FluidTank_EV.get(1);					
				}

				//Incoloy Casing
				RecipeUtils.addShapedGregtechRecipe(
						"plateIncoloyDS", "pipeHugeStaballoy", "plateIncoloyDS",
						"gearIncoloyDS", mInnerTank, "gearIncoloyDS",
						"plateIncoloyDS", "pipeHugeStaballoy", "plateIncoloyDS",
						GregtechItemList.Casing_Refinery_Internal.get(1));

				//Hastelloy-N Sealant Casing
				RecipeUtils.addShapedGregtechRecipe(
						"plateIncoloyMA956", "plateHastelloyN", "plateIncoloyMA956",
						"plateHastelloyN", "frameGtHastelloyC276", "plateHastelloyN",
						"plateIncoloyMA956", "plateHastelloyN", "plateIncoloyMA956",
						GregtechItemList.Casing_Refinery_External.get(1));

				//Hastelloy-X Structural Casing
				RecipeUtils.addShapedGregtechRecipe(
						"ringInconel792", "gearHastelloyX", CI.component_Plate[5],
						CI.craftingToolHammer_Hard, "frameGtHastelloyC276", CI.craftingToolWrench,
						CI.component_Plate[5], CI.getTieredMachineCasing(4), "ringInconel792",
						GregtechItemList.Casing_Refinery_Structural.get(1));









			}

			if (CORE.configSwitches.enableMultiblock_PowerSubstation){
				RecipeUtils.recipeBuilder(
						null, "plateIncoloy020", null,
						"plateIncoloy020", "frameGtIncoloyMA956", "plateIncoloy020",
						null, "plateIncoloy020", null,
						GregtechItemList.Casing_Power_SubStation.get(1));

				RecipeUtils.recipeBuilder(
						"plateIncoloyMA956", GregtechItemList.Battery_RE_EV_Lithium.get(1), "plateIncoloyMA956",
						GregtechItemList.Casing_Power_SubStation.get(1), GregtechItemList.Casing_Vanadium_Redox.get(1), GregtechItemList.Casing_Power_SubStation.get(1),
						"plateIncoloy020", "plateIncoloyMA956", "plateIncoloy020",
						GregtechItemList.PowerSubStation.get(1));
			}
			
			if (CORE.configSwitches.enableMultiblock_IndustrialThermalCentrifuge){
				RecipeUtils.recipeBuilder(
						"plateRedSteel", CI.craftingToolHammer_Hard, "plateRedSteel",
						"plateRedSteel", "frameGtBlackSteel", "plateRedSteel",
						"plateRedSteel", CI.craftingToolWrench, "plateRedSteel",
						GregtechItemList.Casing_ThermalCentrifuge.get(2));

				RecipeUtils.recipeBuilder(
						"plateRedSteel","circuitData","plateRedSteel",
						"stickTalonite",GregtechItemList.Casing_ThermalCentrifuge.get(1),"stickTalonite",
						"plateRedSteel","gearTalonite","plateRedSteel",
						GregtechItemList.Industrial_ThermalCentrifuge.get(1));
			}
			
			if (CORE.configSwitches.enableMultiblock_IndustrialWashPlant){
				RecipeUtils.recipeBuilder(
						"plateGrisium", CI.craftingToolHammer_Hard, "plateGrisium",
						"plateTalonite", "frameGtGrisium", "plateTalonite",
						"plateGrisium", CI.craftingToolWrench, "plateGrisium",
						GregtechItemList.Casing_WashPlant.get(2));

				RecipeUtils.recipeBuilder( 
						"plateGrisium",CI.electricPump_MV,"plateGrisium",
						"plateTalonite",GregtechItemList.Casing_WashPlant.get(1),"plateTalonite",
						"plateGrisium","circuitData","plateGrisium",
						GregtechItemList.Industrial_WashPlant.get(1));
			}


		}
		Utils.LOG_INFO("Done loading recipes for the Various machine blocks.");
	}
}