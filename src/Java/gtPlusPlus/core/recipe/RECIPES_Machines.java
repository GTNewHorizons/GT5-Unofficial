package gtPlusPlus.core.recipe;

import static gtPlusPlus.core.lib.CORE.GTNH;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.*;
import gtPlusPlus.everglades.dimension.Dimension_Everglades;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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

	//Cyclotron
	public static ItemStack RECIPE_CyclotronController;
	public static ItemStack RECIPE_CyclotronOuterCasing;
	public static ItemStack RECIPE_CyclotronInnerCoil;


	//Buffer Cores
	public static ItemStack RECIPE_BufferCore_ULV = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore1", 1);
	public static ItemStack RECIPE_BufferCore_LV = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore2", 1);
	public static ItemStack RECIPE_BufferCore_MV = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore3", 1);
	public static ItemStack RECIPE_BufferCore_HV = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore4", 1);
	public static ItemStack RECIPE_BufferCore_EV = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore5", 1);
	public static ItemStack RECIPE_BufferCore_IV = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore6", 1);
	public static ItemStack RECIPE_BufferCore_LuV = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore7", 1);
	public static ItemStack RECIPE_BufferCore_ZPM = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore8", 1);
	public static ItemStack RECIPE_BufferCore_UV = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore9", 1);
	public static ItemStack RECIPE_BufferCore_MAX = ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore10", 1);


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

	// EV/IV MACHINES
	public static ItemStack EV_MACHINE_Electrolyzer;
	public static ItemStack EV_MACHINE_Centrifuge;
	public static ItemStack EV_MACHINE_BendingMachine;
	public static ItemStack EV_MACHINE_Wiremill;
	public static ItemStack HV_MACHINE_Macerator;
	public static ItemStack EV_MACHINE_Macerator;
	public static ItemStack EV_MACHINE_Cutter;
	public static ItemStack EV_MACHINE_MassFabricator;
	public static ItemStack EV_MACHINE_Extruder;
	public static ItemStack EV_MACHINE_Sifter;
	public static ItemStack EV_MACHINE_ThermalCentrifuge;
	public static ItemStack EV_MACHINE_OreWasher;
	public static ItemStack EV_MACHINE_AlloySmelter;
	public static ItemStack EV_MACHINE_Mixer;


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
	public static ItemStack blockBricks = ItemUtils.getItemStackFromFQRN("minecraft:brick_block", 1);

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

	//Output Determiner
	public static int Casing_Amount;



	public static final void loadRecipes(){
		run();
		Logger.INFO("Loading Recipes for the Various machine blocks.");
	}

	private static void run(){
		
		//Determines Casing Recipe Output
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && !GTNH){
			Casing_Amount=2;
		}
		else {
			Casing_Amount=1;
		}	
		
		initModItems();
		controlCores();
		energyCores();
		wirelessChargers();
		largeArcFurnace();
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
			if (!GTNH){			
				EV_MACHINE_Electrolyzer = ItemList.Machine_EV_Electrolyzer.get(1);
				EV_MACHINE_BendingMachine= ItemList.Machine_EV_Bender.get(1);
				EV_MACHINE_Wiremill= ItemList.Machine_EV_Wiremill.get(1);
				HV_MACHINE_Macerator= ItemList.Machine_HV_Macerator.get(1);
				EV_MACHINE_Macerator= ItemList.Machine_EV_Macerator.get(1);
				EV_MACHINE_MassFabricator= ItemList.Machine_EV_Massfab.get(1);
				EV_MACHINE_Centrifuge= ItemList.Machine_EV_Centrifuge.get(1);
				EV_MACHINE_Cutter = ItemList.Machine_EV_Cutter.get(1);
				EV_MACHINE_Extruder = ItemList.Machine_EV_Extruder.get(1);
				EV_MACHINE_Sifter = ItemList.Machine_HV_Sifter.get(1);
				EV_MACHINE_ThermalCentrifuge = ItemList.Machine_EV_ThermalCentrifuge.get(1);
				EV_MACHINE_OreWasher = ItemList.Machine_EV_OreWasher.get(1);
				EV_MACHINE_AlloySmelter = ItemList.Machine_EV_AlloySmelter.get(1);  
				EV_MACHINE_Mixer = ItemList.Machine_EV_Mixer.get(1);
			}
			//Balanced opposites
			else {			    
				EV_MACHINE_Electrolyzer = ItemList.Machine_IV_Electrolyzer.get(1);
				EV_MACHINE_BendingMachine= ItemList.Machine_IV_Bender.get(1);
				EV_MACHINE_Wiremill= ItemList.Machine_IV_Wiremill.get(1);
				HV_MACHINE_Macerator= ItemList.Machine_EV_Macerator.get(1);
				EV_MACHINE_Macerator= ItemList.Machine_IV_Macerator.get(1);			
				EV_MACHINE_MassFabricator= CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK ? gtPlusPlus.core.util.Utils.getValueOfItemList("Machine_LuV_Massfab", ItemList.Machine_IV_Massfab).get(1) : ItemList.Machine_IV_Massfab.get(1);
				EV_MACHINE_Centrifuge= ItemList.Machine_IV_Centrifuge.get(1);
				EV_MACHINE_Cutter = ItemList.Machine_IV_Cutter.get(1);
				EV_MACHINE_Extruder = ItemList.Machine_IV_Extruder.get(1);
				EV_MACHINE_Sifter = ItemList.Machine_HV_Sifter.get(1);
				EV_MACHINE_ThermalCentrifuge = ItemList.Machine_IV_ThermalCentrifuge.get(1);
				EV_MACHINE_OreWasher = ItemList.Machine_IV_OreWasher.get(1);
				EV_MACHINE_AlloySmelter = ItemList.Machine_IV_AlloySmelter.get(1);  
				EV_MACHINE_Mixer = ItemList.Machine_IV_Mixer.get(1);
			}



		}
		if (CORE.ConfigSwitches.enableMultiblock_IndustrialCokeOven){
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
			CORE.RA.addSixSlotAssemblingRecipe(
					new ItemStack[] {
							ItemUtils.getSimpleStack(CI.getDataOrb(), 4 * (GTNH ? 2 : 1)),						
							ItemList.Cover_Screen.get(4),
							CI.machineHull_IV,
							ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(7), 2)
					}, 
					ELEMENT.getInstance().TANTALUM.getFluid(144 * 16), 
					GregtechItemList.Gregtech_Computer_Cube.get(1),
					60 * 20 * 3, 
					8000);

			//Circuit programmer			
			CORE.RA.addSixSlotAssemblingRecipe(
					new ItemStack[] {
							ItemUtils.getSimpleStack(CI.robotArm_LV, 4 * (GTNH ? 2 : 1)),						
							ItemList.Cover_Controller.get(1, CI.electricMotor_MV),
							CI.machineHull_MV,
							ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(1), 2),
							ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(2), 2)
					}, 
					ELEMENT.getInstance().IRON.getFluid(144 * 4), 
					ItemUtils.getSimpleStack(ModBlocks.blockCircuitProgrammer),
					60 * 10 * 1, 
					30);

			//Lead Lined Chest		
			CORE.RA.addSixSlotAssemblingRecipe(
					new ItemStack[] {
							CI.machineHull_LV,
							ItemUtils.getItemStackOfAmountFromOreDict("plateRubber", 32),
							ItemUtils.getItemStackOfAmountFromOreDict("plateDenseLead", 9),
							ItemUtils.getSimpleStack(Blocks.chest)
					}, 
					ELEMENT.getInstance().LEAD.getFluid(144 * 16), 
					ItemUtils.getSimpleStack(ModBlocks.blockDecayablesChest),
					60 * 10 * 3, 
					60);

			//RTG	
			CORE.RA.addSixSlotAssemblingRecipe(
					new ItemStack[] {
							ItemUtils.getItemStackWithMeta(true, "IC2:blockGenerator:6", "IC2-RTG", 6, 1),
							ALLOY.NITINOL_60.getPlate(GTNH ? 32 : 8),
							ALLOY.MARAGING350.getGear(GTNH ? 16 : 4),
							ItemUtils.getSimpleStack(GTNH ? CI.fieldGenerator_IV : CI.fieldGenerator_EV, 8 ),
							ItemUtils.getItemStackOfAmountFromOreDict("wireFinePlatinum", GTNH ? 64 : 32),
							ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(GTNH ? 7 : 6), GTNH ? 5 : 4)
					}, 
					ALLOY.NIOBIUM_CARBIDE.getFluid(144 * 16), 
					GregtechItemList.RTG.get(1),
					60 * 20 * 10, 
					8000);

			// Super Jukebox		
			CORE.RA.addSixSlotAssemblingRecipe(
					new ItemStack[] {
							CI.machineHull_LV,
							ItemUtils.getItemStackOfAmountFromOreDict("circuitBasic", 4),
							ItemUtils.getItemStackOfAmountFromOreDict("plateTumbaga", 8),
							ItemUtils.getSimpleStack(Blocks.jukebox)
					}, 
					ELEMENT.getInstance().COPPER.getFluid(144 * 2), 
					ItemUtils.getSimpleStack(ModBlocks.blockCustomJukebox),
					20 * 30, 
					30);
			
			//Poo Collector		
			CORE.RA.addSixSlotAssemblingRecipe(
					new ItemStack[] {
							CI.machineHull_MV,
							CI.getTieredComponent(OrePrefixes.circuit, 2, GTNH ? 4 : 2),
							CI.getTieredComponent(OrePrefixes.pipeMedium, 2, GTNH ? 4 : 2),
							CI.getElectricPump(2, GTNH ? 4 : 2),
							ALLOY.EGLIN_STEEL.getPlate(GTNH ? 8 : 4),
							ALLOY.POTIN.getScrew(GTNH ? 12 : 6)
					}, 
					ALLOY.TUMBAGA.getFluid(144 * 4), 
					ItemUtils.getSimpleStack(ModBlocks.blockPooCollector),
					20 * 60, 
					30);

			//Adv. Poo Collector		
			CORE.RA.addSixSlotAssemblingRecipe(
					new ItemStack[] {
							CI.getTieredMachineHull(-1),
							ItemUtils.getSimpleStack(ModBlocks.blockPooCollector),
							CI.getTieredComponent(OrePrefixes.circuit, 5, GTNH ? 8 : 4),
							CI.getTieredComponent(OrePrefixes.pipeHuge, 6, GTNH ? 8 : 4),
							CI.getElectricPump(5, GTNH ? 12 : 6),
							CI.getTieredComponent(OrePrefixes.screw, 6, GTNH ? 32 : 16)
					}, 
					CI.getAlternativeTieredFluid(5, 144 * 9), 
					ItemUtils.getSimpleStack(ModBlocks.blockPooCollector, 8, 1),
					20 * 60 * 5, 
					500);


			//Basic Steam Turbine
			RecipeUtils.addShapedGregtechRecipe(
					CI.getTieredComponent(OrePrefixes.pipeSmall, 0, 1), "circuitPrimitive", CI.getTieredComponent(OrePrefixes.pipeSmall, 0, 1),
					ALLOY.TUMBAGA.getRotor(1), CI.machineCasing_ULV, ALLOY.TUMBAGA.getRotor(1),
					CI.getElectricMotor(0, 1), "cableGt01RedAlloy", CI.getElectricMotor(0, 1),
					GregtechItemList.Generator_Steam_Turbine_ULV.get(1));

			//Basic Gas Turbine
			RecipeUtils.addShapedGregtechRecipe(
					"circuitPrimitive", ALLOY.TUMBAGA.getRotor(1), "circuitPrimitive",
					ALLOY.TUMBAGA.getRotor(1), CI.machineCasing_ULV, ALLOY.TUMBAGA.getRotor(1),
					CI.getElectricMotor(0, 1), "cableGt01RedAlloy", CI.getElectricMotor(0, 1),
					GregtechItemList.Generator_Gas_Turbine_ULV.get(1));

			//Basic Combustion Turbine
			RecipeUtils.addShapedGregtechRecipe(
					CI.getElectricPiston(0, 1), "circuitPrimitive", CI.getElectricPiston(0, 1),
					CI.getElectricMotor(0, 1), CI.machineCasing_ULV, CI.getElectricMotor(0, 1),
					ALLOY.TUMBAGA.getGear(1), "cableGt01RedAlloy", ALLOY.TUMBAGA.getGear(1),
					GregtechItemList.Generator_Diesel_ULV.get(1));



			//Steam Condenser
			if (CORE.ConfigSwitches.enableMachine_SteamConverter ){
				RECIPE_SteamCondenser = GregtechItemList.Condensor_MAX.get(1);
				RecipeUtils.addShapedGregtechRecipe(
						pipeLargeCopper, pipeHugeSteel, pipeLargeCopper,
						plateEnergeticAlloy, CI.electricPump_HV, plateEnergeticAlloy,
						plateEnergeticAlloy, pipeLargeCopper, plateEnergeticAlloy,
						RECIPE_SteamCondenser);
			}


			if (CORE.ConfigSwitches.enableMultiblock_IronBlastFurnace){

				RECIPE_IronBlastFurnace = GregtechItemList.Machine_Iron_BlastFurnace.get(1);
				RECIPE_IronPlatedBricks = GregtechItemList.Casing_IronPlatedBricks.get(Casing_Amount);

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

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialCentrifuge){
				//Industrial Centrifuge
				RECIPE_IndustrialCentrifugeController = GregtechItemList.Industrial_Centrifuge.get(1);
				RECIPE_IndustrialCentrifugeCasing = GregtechItemList.Casing_Centrifuge1.get(Casing_Amount);

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

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialCokeOven){
				//Industrial Coke Oven
				RECIPE_IndustrialCokeOvenController = GregtechItemList.Industrial_CokeOven.get(1);
				RECIPE_IndustrialCokeOvenFrame = GregtechItemList.Casing_CokeOven.get(Casing_Amount);
				RECIPE_IndustrialCokeOvenCasingA = GregtechItemList.Casing_CokeOven_Coil1.get(Casing_Amount);
				RECIPE_IndustrialCokeOvenCasingB = GregtechItemList.Casing_CokeOven_Coil2.get(Casing_Amount);

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

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialElectrolyzer){
				//Industrial Electrolyzer
				RECIPE_IndustrialElectrolyzerController = GregtechItemList.Industrial_Electrolyzer.get(1);
				RECIPE_IndustrialElectrolyzerFrame = GregtechItemList.Casing_Electrolyzer.get(Casing_Amount);

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

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialPlatePress){
				//Industrial Material Press
				RECIPE_IndustrialMaterialPressController = GregtechItemList.Industrial_PlatePress.get(1);
				RECIPE_IndustrialMaterialPressFrame = GregtechItemList.Casing_MaterialPress.get(Casing_Amount);

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

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialMacerationStack){
				//Industrial Maceration Stack
				RECIPE_IndustrialMacerationStackController = GregtechItemList.Industrial_MacerationStack.get(1);
				RECIPE_IndustrialMacerationStackFrame = GregtechItemList.Casing_MacerationStack.get(Casing_Amount);

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

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialWireMill){
				//Industrial Wire Factory
				RECIPE_IndustrialWireFactoryController = GregtechItemList.Industrial_WireFactory.get(1);
				RECIPE_IndustrialWireFactoryFrame = GregtechItemList.Casing_WireFactory.get(Casing_Amount);

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
			if (CORE.ConfigSwitches.enableMachine_FluidTanks){
				Logger.WARNING("Is New Horizons Loaded? "+GTNH);
				if (!GTNH){
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

			if (CORE.ConfigSwitches.enableMultiblock_MultiTank){
				//Industrial Multi Tank
				RECIPE_IndustrialMultiTankController = GregtechItemList.Industrial_MultiTank.get(1);
				RECIPE_IndustrialMultiTankFrame = GregtechItemList.Casing_MultitankExterior.get(Casing_Amount);

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

			//Semi-Fluid Generators
			ItemStack mSemiFluidgen = ItemUtils.getItemStackFromFQRN("IC2:blockGenerator:7", 1);
			mSemiFluidgen.setItemDamage(7);
					//ItemUtils.simpleMetaStack("IC2:blockGenerator:7", 7, 1);			
			ItemStack[] aSemifluids = new ItemStack[] {mSemiFluidgen, GregtechItemList.Generator_SemiFluid_LV.get(1), GregtechItemList.Generator_SemiFluid_MV.get(1), GregtechItemList.Generator_SemiFluid_HV.get(1)};
			for (int o=1;o<4;o++) {
				CORE.RA.addSixSlotAssemblingRecipe(
						new ItemStack[] {
								aSemifluids[o-1],
								CI.getElectricPiston(o, GTNH ? 4 : 2),
								CI.getElectricMotor(o, GTNH ? 2 : 1),
								CI.getTieredComponent(OrePrefixes.circuit, o, GTNH ? 4 : 2),
								CI.getPlate(o, GTNH ? 8 : 4),
								CI.getGear(o-1, GTNH ? 4 : 2)
						},
						CI.getAlternativeTieredFluid(o, 144 * 4),
						aSemifluids[o],
						20 * 30,
						(int) GT_Values.V[o]);
			}			

			if (CORE.ConfigSwitches.enableMultiblock_AlloyBlastSmelter){
				//Industrial Blast Smelter
				RECIPE_IndustrialBlastSmelterController = GregtechItemList.Industrial_AlloyBlastSmelter.get(1);
				RECIPE_IndustrialBlastSmelterFrame = GregtechItemList.Casing_BlastSmelter.get(Casing_Amount);
				RECIPE_IndustrialBlastSmelterCoil = GregtechItemList.Casing_Coil_BlastSmelter.get(Casing_Amount);

				//Blast Smelter
				RecipeUtils.addShapedGregtechRecipe(
						"plateZirconiumCarbide", CI.circuitTier4, "plateZirconiumCarbide",
						cableTier4, EV_MACHINE_AlloySmelter, cableTier4,
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

			if (CORE.ConfigSwitches.enableMultiblock_MatterFabricator){
				//Industrial Matter Fabricator
				RECIPE_IndustrialMatterFabController = GregtechItemList.Industrial_MassFab.get(1);
				RECIPE_IndustrialMatterFabFrame = GregtechItemList.Casing_MatterFab.get(Casing_Amount);
				RECIPE_IndustrialMatterFabCoil = GregtechItemList.Casing_MatterGen.get(Casing_Amount);

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

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialSifter){
				//Industrial Sieve
				RECIPE_IndustrialSieveController = GregtechItemList.Industrial_Sifter.get(1);
				RECIPE_IndustrialSieveFrame = GregtechItemList.Casing_Sifter.get(Casing_Amount);
				RECIPE_IndustrialSieveGrate = GregtechItemList.Casing_SifterGrate.get(Casing_Amount);

				//Industrial Sieve
				RecipeUtils.addShapedGregtechRecipe(
						"plateEglinSteel", CI.circuitTier2, "plateEglinSteel",
						cableTier3, EV_MACHINE_Sifter, cableTier3,
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

			if (CORE.ConfigSwitches.enableMultiblock_TreeFarmer){/*
				//Industrial Tree Farmer
				RECIPE_TreeFarmController = GregtechItemList.Industrial_TreeFarm.get(1);
				RECIPE_TreeFarmFrame = GregtechItemList.TreeFarmer_Structural.get(Casing_Amount);
				//Industrial Tree Farm Controller
				if (!GTNH) {
					RecipeUtils.addShapedGregtechRecipe(
							"plateEglinSteel", "rotorEglinSteel", "plateEglinSteel",
							"cableGt02Steel", "pipeMediumSteel", "cableGt02Steel",
							"plateEglinSteel", CI.machineCasing_MV, "plateEglinSteel",
							RECIPE_TreeFarmController);
				}
				if (GTNH) {
					RecipeUtils.addShapedGregtechRecipe(
							"plateEglinSteel", "rotorEglinSteel", "plateEglinSteel",
							"cableGt02Silver", "pipeMediumStainlessSteel", "cableGt02Silver",
							"plateEglinSteel", CI.machineCasing_HV, "plateEglinSteel",
							RECIPE_TreeFarmController);
				}
				//Industrial Tree Farm Frame
				RecipeUtils.addShapedGregtechRecipe(
						ItemUtils.getSimpleStack(Blocks.dirt), ItemUtils.getSimpleStack(Blocks.dirt), ItemUtils.getSimpleStack(Blocks.dirt),
						"plankWood", "frameGtTumbaga", "plankWood",
						"plankWood", "plankWood", "plankWood",
						RECIPE_TreeFarmFrame);
			 */}

			if (CORE.ConfigSwitches.enableMachine_Tesseracts){
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

			if (CORE.ConfigSwitches.enableMachine_SimpleWasher){
				ItemStack plateWrought = ItemUtils.getItemStackOfAmountFromOreDict("plateWroughtIron", 1);
				ItemStack washerPipe;
				
				
				if (CORE.ConfigSwitches.enableCustom_Pipes){
					washerPipe = ItemUtils.getItemStackOfAmountFromOreDict("pipeLargeClay", 1);
					RecipeUtils.addShapedGregtechRecipe(
							plateWrought, CI.electricPump_LV, plateWrought,
							plateWrought, washerPipe, plateWrought,
							plateWrought, CI.machineCasing_ULV, plateWrought,
							GregtechItemList.SimpleDustWasher_ULV.get(1));
				}
				//Add Recipe
				washerPipe = ItemUtils.getItemStackOfAmountFromOreDict("pipeLargeCopper", 1);					
				RecipeUtils.addShapedGregtechRecipe(
						plateWrought, CI.electricPump_LV, plateWrought,
						plateWrought, washerPipe, plateWrought,
						plateWrought, CI.machineCasing_ULV, plateWrought,
						GregtechItemList.SimpleDustWasher_ULV.get(1));
				
				int aSimpleWasherTier = 2;
				int aSlot = 0;
				ItemStack[][] aInputsForSimpleWashers = new ItemStack[4][6];

				aInputsForSimpleWashers[0] = new ItemStack[] {
						CI.getTieredMachineHull(2),
						CI.getTieredComponent(OrePrefixes.screw, 2, GTNH ? 16 : 8),
						CI.getTieredComponent(OrePrefixes.plate, 1, GTNH ? 8 : 4),
						CI.getTieredComponent(OrePrefixes.rod, 2, GTNH ? 4 : 2),
						CI.getTieredComponent(OrePrefixes.circuit, 2, GTNH ? 3 : 1),
						
				};
				aInputsForSimpleWashers[1] = new ItemStack[] {
						CI.getTieredMachineHull(4),
						CI.getTieredComponent(OrePrefixes.screw, 4, GTNH ? 24 : 12),
						CI.getTieredComponent(OrePrefixes.plate, 3, GTNH ? 12 : 6),
						CI.getTieredComponent(OrePrefixes.rod, 4, GTNH ? 6 : 3),
						CI.getTieredComponent(OrePrefixes.circuit, 4, GTNH ? 4 : 2),
						
				};
				aInputsForSimpleWashers[2] = new ItemStack[] {
						CI.getTieredMachineHull(6),
						CI.getTieredComponent(OrePrefixes.screw, 6, GTNH ? 48 : 24),
						CI.getTieredComponent(OrePrefixes.plate, 5, GTNH ? 16 : 8),
						CI.getTieredComponent(OrePrefixes.rod, 6, GTNH ? 8 : 4),
						CI.getTieredComponent(OrePrefixes.circuit, 6, GTNH ? 6 : 3),
						
				};
				aInputsForSimpleWashers[3] = new ItemStack[] {
						CI.getTieredMachineHull(8),
						CI.getTieredComponent(OrePrefixes.screw, 8, GTNH ? 64 : 32),
						CI.getTieredComponent(OrePrefixes.plate, 7, GTNH ? 32 : 16),
						CI.getTieredComponent(OrePrefixes.rod, 8, GTNH ? 10 : 5),
						CI.getTieredComponent(OrePrefixes.circuit, 8, GTNH ? 8 : 4),
						
				};
				
				
				
				
				
				
				ItemStack[] aSimpleWashers = new ItemStack[] {GregtechItemList.SimpleDustWasher_MV.get(1), GregtechItemList.SimpleDustWasher_EV.get(1), GregtechItemList.SimpleDustWasher_LuV.get(1), GregtechItemList.SimpleDustWasher_UV.get(1)};
				for (int i=0;i<4;i++) {
					
					CORE.RA.addSixSlotAssemblingRecipe(
							aInputsForSimpleWashers[aSlot],
							CI.getTieredFluid(aSimpleWasherTier, 144 * aSimpleWasherTier), 
							aSimpleWashers[aSlot], 
							20 * 15 * aSimpleWasherTier,
							(int) GT_Values.V[aSimpleWasherTier]);
					
					aSimpleWasherTier += 2;
					aSlot++;
				}
				
				
				
				
				
				
			}

			if (CORE.ConfigSwitches.enableMachine_Pollution && CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
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

				//Air Intake Hatch



				ItemList FluidRegulator_IV = Utils.getValueOfItemList("FluidRegulator_IV", ItemList.Pump_IV);				
				ItemStack aTieredFluidRegulator = CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK ? FluidRegulator_IV.get(1) : ItemList.Pump_IV.get(1);


				RecipeUtils.addShapedGregtechRecipe(
						CI.component_Plate[6], ItemList.Casing_Grate.get(1), CI.component_Plate[6],
						CI.component_Plate[6], aTieredFluidRegulator, CI.component_Plate[6],
						CI.getTieredCircuit(5), ItemList.Hatch_Input_IV.get(1), CI.getTieredCircuit(5),
						GregtechItemList.Hatch_Air_Intake.get(1));

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

			if (CORE.ConfigSwitches.enableMultiblock_ThermalBoiler){
				RECIPE_ThermalBoilerController = GregtechItemList.GT4_Thermal_Boiler.get(1);
				RECIPE_ThermalBoilerCasing = GregtechItemList.Casing_ThermalContainment.get(4);
				ItemStack centrifugeHV = ItemList.Machine_HV_Centrifuge.get(1);

				RecipeUtils.addShapedGregtechRecipe(
						"craftingGeothermalGenerator", centrifugeHV, "craftingGeothermalGenerator",
						"gearGtTitanium", "circuitElite", "gearGtTitanium",
						"craftingGeothermalGenerator", centrifugeHV, "craftingGeothermalGenerator",
						RECIPE_ThermalBoilerController);

				RecipeUtils.addShapedGregtechRecipe(
						"craftingGeothermalGenerator", centrifugeHV, "craftingGeothermalGenerator",
						"gearGtTungstenSteel", "circuitElite", "gearGtTungstenSteel",
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

			if (CORE.ConfigSwitches.enableMultiblock_LiquidFluorideThoriumReactor){

				//Thorium Reactor
				RECIPE_LFTRController = GregtechItemList.ThoriumReactor.get(1);
				RECIPE_LFTRInnerCasing = GregtechItemList.Casing_Reactor_II.get(1); //Zeron
				RECIPE_LFTROuterCasing = GregtechItemList.Casing_Reactor_I.get(1); //Hastelloy

				ItemStack controlCircuit = ItemUtils.getSimpleStack(ModItems.itemCircuitLFTR);
				if(!GTNH) {
					RecipeUtils.addShapedGregtechRecipe(
							controlCircuit, "cableGt12NaquadahAlloy", controlCircuit,
							"plateDoubleHastelloyN", GregtechItemList.Gregtech_Computer_Cube.get(1), "plateDoubleHastelloyN",
							"plateThorium232", CI.machineHull_UV, "plateThorium232",
							RECIPE_LFTRController);
				} else  {
					RecipeUtils.addShapedGregtechRecipe(
							controlCircuit, "cableGt12NaquadahAlloy", controlCircuit,
							"plateDoubleHastelloyN", GregtechItemList.Gregtech_Computer_Cube.get(1), "plateDoubleHastelloyN",
							"plateThorium232", CI.machineHull_LuV, "plateThorium232",
							RECIPE_LFTRController);
				}
				RecipeUtils.addShapedGregtechRecipe(
						"plateDoubleZeron100", CI.craftingToolScrewdriver, "plateDoubleZeron100",
						"gearGtTalonite", CI.fieldGenerator_ULV, "gearGtTalonite",
						"plateDoubleZeron100", CI.craftingToolHammer_Hard, "plateDoubleZeron100",
						RECIPE_LFTRInnerCasing);

				ItemStack IC2HeatPlate = ItemUtils.getItemStackFromFQRN("IC2:reactorPlatingHeat", 1);
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
						"gearGtStellite", CI.machineHull_LuV, "gearGtStellite",
						GregtechItemList.Industrial_FuelRefinery.get(1));

				ItemStack mInnerTank;

				if (GTNH || !CORE.ConfigSwitches.enableMachine_FluidTanks){
					mInnerTank = ItemList.Quantum_Tank_LV.get(1);
				}
				else {
					mInnerTank = GregtechItemList.GT_FluidTank_EV.get(1);					
				}

				//Incoloy Casing
				RecipeUtils.addShapedGregtechRecipe(
						"plateIncoloyDS", "pipeHugeStaballoy", "plateIncoloyDS",
						"gearGtIncoloyDS", mInnerTank, "gearGtIncoloyDS",
						"plateIncoloyDS", "pipeHugeStaballoy", "plateIncoloyDS",
						GregtechItemList.Casing_Refinery_Internal.get(Casing_Amount));

				//Hastelloy-N Sealant Casing
				RecipeUtils.addShapedGregtechRecipe(
						"plateIncoloyMA956", "plateHastelloyN", "plateIncoloyMA956",
						"plateHastelloyN", "frameGtHastelloyC276", "plateHastelloyN",
						"plateIncoloyMA956", "plateHastelloyN", "plateIncoloyMA956",
						GregtechItemList.Casing_Refinery_External.get(Casing_Amount));

				//Hastelloy-X Structural Casing
				RecipeUtils.addShapedGregtechRecipe(
						"ringInconel792", "gearGtHastelloyX", CI.component_Plate[5],
						CI.craftingToolHammer_Hard, "frameGtHastelloyC276", CI.craftingToolWrench,
						CI.component_Plate[5], CI.getTieredMachineCasing(4), "ringInconel792",
						GregtechItemList.Casing_Refinery_Structural.get(Casing_Amount));









			}

			//Shelves
			RecipeUtils.addShapedGregtechRecipe(
					"screwWood", "plateWood", "screwWood",
					CI.craftingToolHammer_Hard, "frameGtWood", CI.craftingToolHammer_Soft,
					"plateWood", "plateWood", "plateWood",
					GregtechItemList.GT4_Shelf.get(2));

			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {GregtechItemList.GT4_Shelf.get(1)}, GregtechItemList.GT4_Shelf_Compartment.get(1));
			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {GregtechItemList.GT4_Shelf_Compartment.get(1)}, GregtechItemList.GT4_Shelf_Desk.get(1));
			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {GregtechItemList.GT4_Shelf_Desk.get(1)}, GregtechItemList.GT4_Shelf_Iron.get(1));
			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {GregtechItemList.GT4_Shelf_Iron.get(1)}, GregtechItemList.GT4_Shelf_FileCabinet.get(1));
			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {GregtechItemList.GT4_Shelf_FileCabinet.get(1)}, GregtechItemList.GT4_Shelf.get(1));


			//Cyclotron			
			if (CORE.ConfigSwitches.enableMultiblock_Cyclotron){
				RECIPE_CyclotronController = GregtechItemList.COMET_Cyclotron.get(1);
				RECIPE_CyclotronOuterCasing = GregtechItemList.Casing_Cyclotron_External.get(Casing_Amount);
				RECIPE_CyclotronInnerCoil = GregtechItemList.Casing_Cyclotron_Coil.get(1);
				
				//Outer Casing
				CORE.RA.addSixSlotAssemblingRecipe(				
						new ItemStack[] {
								ItemList.Casing_FrostProof.get(1),
								ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 0, GTNH ? 8 : 4),
								ALLOY.INCOLOY_DS.getPlate(GTNH ? 16 : 8),
								ALLOY.INCONEL_690.getScrew(GTNH ? 32 : 16),
								ALLOY.EGLIN_STEEL.getLongRod(GTNH ? 16 : 4),
								CI.getElectricPiston(3, GTNH ? 4 : 2)
						},					
						ALLOY.ZIRCONIUM_CARBIDE.getFluid(144 * 8), //Input Fluid					
						RECIPE_CyclotronOuterCasing,					
						30 * 20 * 2, 
						MaterialUtils.getVoltageForTier(4));
				
				
				//Inner Coil
				CORE.RA.addSixSlotAssemblingRecipe(				
						new ItemStack[] {
								ItemList.Casing_Coil_Nichrome.get(1),
								ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 1, GTNH ? 32 : 8),
								ALLOY.INCOLOY_MA956.getPlate(GTNH ? 16 : 8),
								ALLOY.TANTALLOY_61.getBolt(GTNH ? 32 : 16),
								ALLOY.INCOLOY_020.getScrew(GTNH ? 64 : 32),
								CI.getFieldGenerator(4, GTNH ? 2 : 1)
						},					
						ALLOY.HG1223.getFluid(144 * 5), //Input Fluid					
						RECIPE_CyclotronInnerCoil,					
						60 * 20 * 2, 
						MaterialUtils.getVoltageForTier(5));


				//Controller
				CORE.RA.addSixSlotAssemblingRecipe(				
						new ItemStack[] {
								CI.machineHull_IV,
								ItemUtils.getSimpleStack(RECIPE_CyclotronInnerCoil, GTNH ? 4 : 2),
								ALLOY.INCOLOY_020.getPlate(GTNH ? 16 : 8),
								ALLOY.TANTALLOY_61.getGear(GTNH ? 4 : 2),
								ALLOY.INCOLOY_MA956.getScrew(GTNH ? 64 : 16),
								ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(GTNH ? 6 : 5), GTNH ? 8 : 16)
						},					
						ALLOY.INCOLOY_020.getFluid(144 * 9), //Input Fluid					
						RECIPE_CyclotronController,					
						60 * 20 * 5, 
						MaterialUtils.getVoltageForTier(5));







			}

			if (CORE.ConfigSwitches.enableMultiblock_PowerSubstation){
				RecipeUtils.recipeBuilder(
						"screwTitanium", "plateIncoloy020", "screwTitanium",
						"plateIncoloy020", "frameGtIncoloyMA956", "plateIncoloy020",
						"screwTitanium", "plateIncoloy020", "screwTitanium",
						GregtechItemList.Casing_Power_SubStation.get(Casing_Amount));

				ItemStack mBattery = ItemUtils.getSimpleStack(ModItems.itemCircuitLFTR);

				RecipeUtils.recipeBuilder(
						"plateIncoloyMA956", mBattery, "plateIncoloyMA956",
						GregtechItemList.Casing_Power_SubStation.get(1), GregtechItemList.Casing_Vanadium_Redox.get(1), GregtechItemList.Casing_Power_SubStation.get(1),
						"plateIncoloy020", "plateIncoloyMA956", "plateIncoloy020",
						GregtechItemList.PowerSubStation.get(1));
			}

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialThermalCentrifuge){
				RecipeUtils.recipeBuilder(
						"plateRedSteel", CI.craftingToolHammer_Hard, "plateRedSteel",
						"plateRedSteel", "frameGtBlackSteel", "plateRedSteel",
						"plateRedSteel", CI.craftingToolWrench, "plateRedSteel",
						GregtechItemList.Casing_ThermalCentrifuge.get(Casing_Amount));

				RecipeUtils.recipeBuilder(
						"plateRedSteel","circuitData","plateRedSteel",
						"stickTalonite",EV_MACHINE_ThermalCentrifuge,"stickTalonite",
						"plateRedSteel","gearGtTalonite","plateRedSteel",
						GregtechItemList.Industrial_ThermalCentrifuge.get(1));
			}

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialWashPlant){
				RecipeUtils.recipeBuilder(
						"plateGrisium", CI.craftingToolHammer_Hard, "plateGrisium",
						"plateTalonite", "frameGtGrisium", "plateTalonite",
						"plateGrisium", CI.craftingToolWrench, "plateGrisium",
						GregtechItemList.Casing_WashPlant.get(Casing_Amount));

				RecipeUtils.recipeBuilder( 
						"plateGrisium",CI.electricPump_MV,"plateGrisium",
						"plateTalonite",EV_MACHINE_OreWasher,"plateTalonite",
						"plateGrisium","circuitData","plateGrisium",
						GregtechItemList.Industrial_WashPlant.get(1));
			}

			if (CORE.ConfigSwitches.enableMultiblock_LargeAutoCrafter) {

				ItemStack aCoreBlock = CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK
						? ItemList.valueOf("Block_IridiumTungstensteel").get(1,
								ItemUtils.getItemStackOfAmountFromOreDict("blockOsmiridium", 1))
								: ItemUtils.getItemStackOfAmountFromOreDict("blockOsmiridium", 1);

						aCoreBlock.stackSize = GTNH ? 2 : 1;

						CORE.RA.addSixSlotAssemblingRecipe(
								new ItemStack[] {
										ItemUtils.getSimpleStack(GregtechItemList.Casing_Multi_Use.get(1), GTNH ? 2 : 1),
										aCoreBlock, 
										CI.getTieredComponent(OrePrefixes.circuit, GTNH ? 3 : 2, 16),
										CI.getTieredComponent(OrePrefixes.screw, GTNH ? 6 : 5, 32),
										CI.getTieredComponent(OrePrefixes.bolt, GTNH ? 6 : 5, 12),
										CI.getTieredComponent(OrePrefixes.plate, GTNH ? 7 : 6, 8), },
								CI.getTertiaryTieredFluid(6, 144 * (GTNH ? 12 : 4)),
								GregtechItemList.Casing_Autocrafter.get(Casing_Amount), 20 * 60 * 2,
								MaterialUtils.getVoltageForTier(GTNH ? 6 : 5));

						CORE.RA.addSixSlotAssemblingRecipe(
								new ItemStack[] { 
										GregtechItemList.Casing_Refinery_Structural.get(4),
										ItemUtils.getSimpleStack(ModItems.itemCircuitLFTR, GTNH ? 2 : 1),
										CI.getTieredComponent(OrePrefixes.cableGt08, GTNH ? 7 : 6, GTNH ? 32 : 16),
										CI.getEmitter(GTNH ? 6 : 5, 2), 
										CI.getSensor(GTNH ? 6 : 5, 2),
										GregtechItemList.Gregtech_Computer_Cube.get(1),
								},
								CI.getTieredFluid(7, 144 * (GTNH ? 32 : 8)), GregtechItemList.GT4_Multi_Crafter.get(1),
								20 * 60 * 5, MaterialUtils.getVoltageForTier(GTNH ? 6 : 5));

						CORE.RA.addSixSlotAssemblingRecipe(
								new ItemStack[] {
										ItemUtils.getSimpleStack(GregtechItemList.Casing_Multi_Use.get(1), Casing_Amount),
										CI.getEmitter(GTNH ? 5 : 4, GTNH ? 4 : 2), CI.getRobotArm(GTNH ? 5 : 4, GTNH ? 4 : 2),
										CI.getTieredComponent(OrePrefixes.circuit, GTNH ? 3 : 2, 8),
										CI.getTieredComponent(OrePrefixes.screw, GTNH ? 4 : 3, 8),
										CI.getTieredComponent(OrePrefixes.plate, 5, GTNH ? 16 : 4), },
								CI.getAlternativeTieredFluid(5, 144 * 4), ItemUtils.getSimpleStack(ModBlocks.blockProjectTable),
								20 * 30 * 3, MaterialUtils.getVoltageForTier(GTNH ? 5 : 4));

			}

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialCuttingMachine){
				ItemStack plate = ALLOY.MARAGING300.getPlate(1);
				RecipeUtils.recipeBuilder(
						plate, CI.craftingToolHammer_Hard, plate,
						"plateStellite", "frameGtTalonite", "plateStellite",
						plate, CI.craftingToolWrench, plate,
						GregtechItemList.Casing_CuttingFactoryFrame.get(Casing_Amount));

				RecipeUtils.recipeBuilder( 
						plate,CI.getTieredCircuit(3),plate,
						"wireFinePlatinum", EV_MACHINE_Cutter, "wireFinePlatinum",
						plate,CI.getTieredCircuit(4),plate,
						GregtechItemList.Industrial_CuttingFactoryController.get(1));
			}

			//EV_MACHINE_Extruder
			if (CORE.ConfigSwitches.enableMultiblock_IndustrialExtrudingMachine){
				ItemStack plate = ALLOY.INCONEL_690.getPlate(1);
				RecipeUtils.recipeBuilder(
						plate, CI.craftingToolHammer_Hard, plate,
						"plateTalonite", "frameGtStaballoy", "plateTalonite",
						plate, CI.craftingToolWrench, plate,
						GregtechItemList.Casing_Extruder.get(Casing_Amount));

				RecipeUtils.recipeBuilder( 
						plate,CI.getTieredCircuit(4),plate,
						CI.electricPiston_EV, EV_MACHINE_Extruder, CI.electricPiston_EV,
						plate,CI.getTieredCircuit(4),plate,
						GregtechItemList.Industrial_Extruder.get(1));
			}

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialFishingPort){
				ItemStack plate = ALLOY.AQUATIC_STEEL.getPlate(1);
				RecipeUtils.recipeBuilder(
						plate, CI.craftingToolHammer_Hard, plate,
						"plateEglinSteel", "frameGtEglinSteel", "plateEglinSteel",
						plate, CI.craftingToolWrench, plate,
						GregtechItemList.Casing_FishPond.get(Casing_Amount));

				RecipeUtils.recipeBuilder( 
						plate,CI.getTieredCircuit(3),plate,
						"wireFineElectrum", ItemUtils.getSimpleStack(ModBlocks.blockFishTrap), "wireFineElectrum",
						plate,CI.getTieredCircuit(2),plate,
						GregtechItemList.Industrial_FishingPond.get(1));
			}

			if (true) {
				//Advanced Vacuum Freezer
				ItemStack plate = ALLOY.HG1223.getPlateDouble(1);
				ItemStack gear = ALLOY.INCOLOY_MA956.getGear(1);
				ItemStack frame = ALLOY.LAFIUM.getFrameBox(1);
				ItemStack cell1 = ItemList.Reactor_Coolant_He_6.get(1);
				ItemStack cell2 = ItemList.Reactor_Coolant_NaK_6.get(1);

				RecipeUtils.recipeBuilder(
						plate, gear, plate,
						cell1, frame, cell2,
						plate, gear, plate,
						GregtechItemList.Casing_AdvancedVacuum.get(Casing_Amount));
				RecipeUtils.recipeBuilder( 
						gear,CI.getTieredCircuit(6),gear,
						CI.electricPiston_IV, GregtechItemList.Casing_AdvancedVacuum.get(1), CI.electricPiston_IV,
						plate, GregtechItemList.Gregtech_Computer_Cube.get(1), plate,
						GregtechItemList.Industrial_Cryogenic_Freezer.get(1));			

				//Advanced Blast Furnace
				plate = ALLOY.HASTELLOY_N.getPlateDouble(1);
				gear = ALLOY.HASTELLOY_W.getGear(1);
				frame = ALLOY.HASTELLOY_X.getFrameBox(1);
				cell1 = ItemUtils.simpleMetaStack("IC2:reactorHeatSwitchDiamond:1", 1, 1);
				cell2 = ItemUtils.simpleMetaStack("IC2:reactorVentGold:1", 1, 1);
				ItemStack cell3 = ItemUtils.simpleMetaStack("IC2:reactorVentDiamond:1:1", 1, 1);

				RecipeUtils.recipeBuilder(
						plate, cell1, plate,
						cell3, frame, cell2,
						plate, gear, plate,
						GregtechItemList.Casing_Adv_BlastFurnace.get(Casing_Amount));
				RecipeUtils.recipeBuilder( 
						gear,CI.getTieredCircuit(6),gear,
						CI.robotArm_IV, GregtechItemList.Casing_Adv_BlastFurnace.get(1), CI.robotArm_IV,
						plate, GregtechItemList.Gregtech_Computer_Cube.get(1), plate,
						GregtechItemList.Machine_Adv_BlastFurnace.get(1));	

				//Advanced Implosion Compressor
				plate = ItemUtils.getItemStackOfAmountFromOreDict("plateAlloyIridium", 1);
				gear = ALLOY.LEAGRISIUM.getGear(1);
				frame = ALLOY.CINOBITE.getFrameBox(1);
				cell1 = ItemUtils.simpleMetaStack("IC2:reactorHeatSwitchDiamond:1", 1, 1);
				cell2 = ItemUtils.simpleMetaStack("IC2:reactorVentGold:1", 1, 1);

				RecipeUtils.recipeBuilder( 
						gear,CI.getTieredCircuit(6),gear,
						CI.fieldGenerator_IV, CI.machineHull_ZPM, CI.robotArm_IV,
						plate, GregtechItemList.Gregtech_Computer_Cube.get(1), plate,
						GregtechItemList.Machine_Adv_ImplosionCompressor.get(1));	



				//Supply Depot
				plate = ALLOY.TUNGSTEN_CARBIDE.getPlateDouble(1);
				gear = ALLOY.TRINIUM_TITANIUM.getRing(1);
				frame = ALLOY.TUNGSTEN_CARBIDE.getFrameBox(1);
				cell1 = CI.conveyorModule_EV;
				cell2 = CI.electricMotor_IV;
				ItemStack casingAmazon = GregtechItemList.Casing_AmazonWarehouse.get(1);
				ItemStack aTieredUnboxinator = CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK ? ItemList.valueOf("Machine_LuV_Unboxinator").get(1, ItemList.Machine_IV_Unboxinator.get(1)) : ItemList.Machine_IV_Unboxinator.get(1);


				RecipeUtils.recipeBuilder(
						plate, ItemUtils.getItemStackOfAmountFromOreDict("cableGt12VanadiumGallium", 1), plate,
						cell1, frame, cell2,
						plate, gear, plate,
						GregtechItemList.Casing_AmazonWarehouse.get(Casing_Amount));
				RecipeUtils.recipeBuilder( 
						casingAmazon, CI.getTieredCircuit(7), casingAmazon,
						CI.robotArm_LuV, aTieredUnboxinator, CI.robotArm_LuV,
						CI.conveyorModule_LuV, GregtechItemList.Gregtech_Computer_Cube.get(1), CI.conveyorModule_LuV,
						GregtechItemList.Amazon_Warehouse_Controller.get(1));


				//Industrial Mixing Machine
				RecipeUtils.recipeBuilder( 
						"plateStaballoy",CI.getTieredCircuit(5),"plateStaballoy",
						"plateZirconiumCarbide", EV_MACHINE_Mixer, "plateZirconiumCarbide",
						"plateStaballoy",CI.getTieredCircuit(5),"plateStaballoy",
						GregtechItemList.Industrial_Mixer.get(1));

			}

			if (CORE.ConfigSwitches.enableMultiblock_IndustrialMultiMachine){				
				ItemStack plate = ALLOY.STABALLOY.getPlate(1);


				ItemStack o_Compressor;
				ItemStack o_Lathe;
				ItemStack o_Electromagnet;
				ItemStack o_Fermenter;
				ItemStack o_Distillery;
				ItemStack o_Extractor;
				if (GTNH) {
					o_Compressor = ItemList.Machine_IV_Compressor.get(1);
					o_Lathe = ItemList.Machine_IV_Lathe.get(1);
					o_Electromagnet = ItemList.Machine_IV_Polarizer.get(1);
					o_Fermenter = ItemList.Machine_IV_Fermenter.get(1);
					o_Distillery = ItemList.Machine_IV_FluidExtractor.get(1);
					o_Extractor = ItemList.Machine_IV_Extractor.get(1);	
				}
				else {
					o_Compressor = ItemList.Machine_EV_Compressor.get(1);
					o_Lathe = ItemList.Machine_EV_Lathe.get(1);
					o_Electromagnet = ItemList.Machine_EV_Polarizer.get(1);
					o_Fermenter = ItemList.Machine_EV_Fermenter.get(1);
					o_Distillery = ItemList.Machine_EV_FluidExtractor.get(1);
					o_Extractor = ItemList.Machine_EV_Extractor.get(1);	
				}					
				RecipeUtils.recipeBuilder(
						plate, CI.craftingToolHammer_Hard, plate,
						"plateStainlessSteel", "frameGtZirconiumCarbide", "plateStainlessSteel",
						plate, CI.craftingToolWrench, plate,
						GregtechItemList.Casing_Multi_Use.get(Casing_Amount));

				RecipeUtils.recipeBuilder( 
						o_Compressor, o_Lathe, o_Electromagnet,
						plate, ItemUtils.getSimpleStack(ModBlocks.blockProjectTable), plate,
						o_Fermenter, o_Distillery, o_Extractor,
						GregtechItemList.Industrial_MultiMachine.get(1));
			}




			/*
			 * 6/1/19 - Content additions
			 */

			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {

				/*
				 * Fusion MK4
				 */

				//Fusion MK4 Controller
				CORE.RA.addAssemblylineRecipe(
						ItemList.FusionComputer_UV.get(1),
						(int) GT_Values.V[5],
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(9), 4 * (GTNH ? 2 : 1)),
								ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(7), 32 * (GTNH ? 2 : 1)),
								ItemUtils.getItemStackOfAmountFromOreDict("wireGt16Superconductor", 8 * (GTNH ? 4 : 2)),
								ItemUtils.getItemStackOfAmountFromOreDict("plateDenseNeutronium", 2 * (GTNH ? 5 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.fieldGenerator_MAX : CI.fieldGenerator_ZPM), 5),
								ItemList.Energy_LapotronicOrb2.get(1 * (GTNH ? 64 : 8)),
								GTNH ? GregtechItemList.Compressed_Fusion_Reactor.get(1) : ItemList.FusionComputer_UV.get(1),
										GregtechItemList.Casing_Fusion_Internal.get(1)
						},
						new FluidStack[] {
								ALLOY.PIKYONIUM.getFluid(32 * 144 * (GTNH ? 2 : 1)),
								ALLOY.HG1223.getFluid(64 * 144)
						},
						GregtechItemList.FusionComputer_UV2.get(1),
						(int) GT_Values.V[6],
						(int) GT_Values.V[8]);

				//Fusion MK4 Casing
				CORE.RA.addAssemblylineRecipe(
						ItemList.Casing_Fusion2.get(1),
						(int) GT_Values.V[4],
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(5), 8 * (GTNH ? 2 : 1)),
								ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(4), 16 * (GTNH ? 2 : 1)),
								ItemUtils.getItemStackOfAmountFromOreDict("blockTungstenCarbide", 4 * (GTNH ? 2 : 1)),
								ItemUtils.getItemStackOfAmountFromOreDict("plateNeutronium", 2 * (GTNH ? 2 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.electricMotor_MAX : CI.electricMotor_ZPM), 3 * (GTNH ? 2 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.electricPiston_UV : CI.electricPiston_LuV), 4 * (GTNH ? 2 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.machineHull_MAX : CI.machineHull_ZPM), 1 * (GTNH ? 2 : 1)),
								ItemList.Casing_Fusion2.get(1)
						},
						new FluidStack[] {
								Materials.NaquadahAlloy.getMolten(576 * (GTNH ? 2 : 1)),
								ALLOY.ZERON_100.getFluid(16 * 144)
						},
						GregtechItemList.Casing_Fusion_External.get(1),
						(int) GT_Values.V[5],
						(int) GT_Values.V[7]);

				//Fusion MK4 Coil
				CORE.RA.addAssemblylineRecipe(
						ItemList.Casing_Fusion_Coil.get(1),
						(int) GT_Values.V[4],
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(8), 4 * (GTNH ? 2 : 1)),
								ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(6), 8 * (GTNH ? 2 : 1)),
								ItemUtils.getItemStackOfAmountFromOreDict("plateNeutronium", 2 * (GTNH ? 3 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.emitter_MAX : CI.emitter_ZPM), 2 * (GTNH ? 2 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.sensor_MAX : CI.sensor_ZPM), 2 * (GTNH ? 2 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.fieldGenerator_MAX : CI.fieldGenerator_LuV), 2 * (GTNH ? 2 : 1)),
								ItemUtils.getSimpleStack((GTNH ? ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(8), 8) : CI.getDataOrb()), 8 * (GTNH ? 2 : 1)),
								ItemList.Energy_LapotronicOrb2.get(2 * (GTNH ? 16 : 1)),
								ItemList.Casing_Fusion_Coil.get(1)
						},
						new FluidStack[] {
								ALLOY.CINOBITE.getFluid(16 * 144 * (GTNH ? 2 : 1)),
								ALLOY.ABYSSAL.getFluid(64 * 144)
						},
						GregtechItemList.Casing_Fusion_Internal.get(1),
						(int) GT_Values.V[5],
						(int) GT_Values.V[7]);




				ItemStack aDrillController = Utils.getValueOfItemList("OreDrill4", ItemList.Hull_UV).get(1);				

				//Drilling Platform
				CORE.RA.addSixSlotAssemblingRecipe(
						new ItemStack[] {
								aDrillController,
								ItemUtils.getItemStackOfAmountFromOreDict("frameGtTriniumNaquadahCarbonite", 3),
								ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(9), 2 * (GTNH ? 4 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.conveyorModule_UV : CI.conveyorModule_ZPM), 2 * (GTNH ? 2 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.electricPump_UV : CI.electricPump_ZPM), 4 * (GTNH ? 2 : 1)),
						},
						ALLOY.LAFIUM.getFluid(144 * 8  * (GTNH ? 2 : 1)),
						GregtechItemList.BedrockMiner_MKI.get(1),
						(int) GT_Values.V[5],
						(int) GT_Values.V[7]);

				//Drilling Platform Casings
				CORE.RA.addSixSlotAssemblingRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("frameGtTriniumNaquadahCarbonite", 4),
								ItemUtils.getItemStackOfAmountFromOreDict("plateDoubleTriniumTitaniumAlloy", 1 * (GTNH ? 2 : 1)),
								ItemUtils.getItemStackOfAmountFromOreDict("gearGtPikyonium64B", 2 * (GTNH ? 2 : 1)),
								ALLOY.TRINIUM_REINFORCED_STEEL.getPlateDouble(4 * (GTNH ? 2 : 1)),
								ItemUtils.getSimpleStack((GTNH ? CI.machineHull_UV : CI.machineHull_LuV), 1 * (GTNH ? 2 : 1)),
						},
						ALLOY.MARAGING350.getFluid(144 * 16  * (GTNH ? 2 : 1)),
						GregtechItemList.Casing_BedrockMiner.get(1),
						(int) GT_Values.V[4],
						(int) GT_Values.V[6]);









			}










		}

		int aCostMultiplier = GTNH ? 2 : 1;	

		//Mystic Frame
		CORE.RA.addSixSlotAssemblingRecipe(				
				new ItemStack[] {
						GregtechItemList.Casing_Multi_Use.get(1),
						ItemList.Field_Generator_MV.get(1, CI.circuitTier7),
						ItemList.Field_Generator_HV.get(1, CI.circuitTier7),
						ItemList.Emitter_HV.get(1, CI.circuitTier7),
						ItemList.Sensor_HV.get(1, CI.circuitTier7),
						CI.getTieredComponent(OrePrefixes.plate, 7, 8 * aCostMultiplier),
						CI.getTieredComponent(OrePrefixes.wireGt08, 8, 4 * aCostMultiplier),
				},					
				CI.getTieredFluid(6, (144 * 8)), //Input Fluid					
				ItemUtils.getSimpleStack(Dimension_Everglades.blockPortalFrame, 2),					
				45 * 20 * 1 * (6), 
				MaterialUtils.getVoltageForTier(6));


		//Player Doors
		ItemStack[] aDoorInputs = new ItemStack[] {
				ItemUtils.getSimpleStack(Blocks.log2),
				ItemUtils.getSimpleStack(Blocks.iron_block),
				ItemUtils.getSimpleStack(Blocks.glass),
				ItemUtils.getSimpleStack(Blocks.packed_ice),
				ItemUtils.getSimpleStack(Blocks.cactus),	
		};
		ItemStack[] aDoorOutputs = new ItemStack[] {
				ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorWooden),
				ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorIron),
				ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorCustom_Glass),
				ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorCustom_Ice),
				ItemUtils.getSimpleStack(ModBlocks.blockPlayerDoorCustom_Cactus),	
		};

		for (int y = 0; y < aDoorInputs.length; y++) {
			CORE.RA.addSixSlotAssemblingRecipe(				
					new ItemStack[] {
							ItemUtils.getSimpleStack(Items.iron_door),
							aDoorInputs[y],
							ItemList.Sensor_LV.get(1, CI.circuitTier7),
							CI.getTieredComponent(OrePrefixes.plate, 1, 2 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.wireGt02, 1, 2 * aCostMultiplier),
							ItemUtils.getSimpleStack(Items.redstone, 16)
					},					
					CI.getTieredFluid(1, (144 * 2)), //Input Fluid					
					aDoorOutputs[y],					
					100, 
					MaterialUtils.getVoltageForTier(1));			
		}






		Logger.INFO("Done loading recipes for the Various machine blocks.");
	}


	private static void controlCores() {

		Material[] aMat_A = new Material[] {
				ALLOY.POTIN,
				ALLOY.ZIRCONIUM_CARBIDE,
				ALLOY.TANTALLOY_61,
				ALLOY.INCONEL_792,
				ALLOY.STABALLOY,
				ALLOY.TALONITE,
				ALLOY.HASTELLOY_N,
				ALLOY.HG1223,
				ALLOY.LAFIUM,
				ALLOY.PIKYONIUM
		};		
		Material[] aMat_B = new Material[] {
				ALLOY.TUMBAGA,
				ALLOY.SILICON_CARBIDE,
				ALLOY.EGLIN_STEEL,
				ALLOY.NICHROME,
				ALLOY.TUNGSTEN_CARBIDE,
				ALLOY.STELLITE,
				ALLOY.HASTELLOY_C276,
				ALLOY.NITINOL_60,
				ALLOY.ZERON_100,
				ALLOY.CINOBITE
		};

		Item aBaseCore = ModItems.itemControlCore;		
		ItemStack[] aInputPrevTier = new ItemStack[] {
				GTNH ? ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore3", 1) : ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore2", 1),
						ItemUtils.simpleMetaStack(aBaseCore, 0, 1),
						ItemUtils.simpleMetaStack(aBaseCore, 1, 1),
						ItemUtils.simpleMetaStack(aBaseCore, 2, 1),
						ItemUtils.simpleMetaStack(aBaseCore, 3, 1),
						ItemUtils.simpleMetaStack(aBaseCore, 4, 1),
						ItemUtils.simpleMetaStack(aBaseCore, 5, 1),
						ItemUtils.simpleMetaStack(aBaseCore, 6, 1),
						ItemUtils.simpleMetaStack(aBaseCore, 7, 1),
						ItemUtils.simpleMetaStack(aBaseCore, 8, 1),
		};		
		ItemStack[] aOutput = new ItemStack[] {
				ItemUtils.simpleMetaStack(aBaseCore, 0, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 1, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 2, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 3, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 4, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 5, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 6, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 7, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 8, 1),
				ItemUtils.simpleMetaStack(aBaseCore, 9, 1),

		};

		CORE.RA.addSixSlotAssemblingRecipe(				
				new ItemStack[] {
						CI.machineHull_HV,
						aOutput[1],
						aMat_A[1].getGear(GTNH ? 4 : 2),
						aMat_B[2].getPlateDouble(GTNH ? 16 : 8),
						ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+(GTNH ? "2" : "1"), GTNH ? 4 : 2),
						ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName(GTNH ? 3 : 2), GTNH ? 10 : 5)
				},					
				aMat_B[3].getFluid(144 * 8), //Input Fluid					
				GregtechItemList.Hatch_Control_Core.get(1),					
				60 * 20 * 5, 
				MaterialUtils.getVoltageForTier(3));


		for (int i = 0; i < 10; i++) {			
			boolean aDub = false;
			ItemStack aPlateStack = aMat_A[i].getPlateDouble((GTNH ? 16 : 8) * (aDub ? 2 : 1));
			ItemStack aGearStack = aMat_B[i].getGear(GTNH ? 4 : 2 * (aDub ? 2 : 1));
			ItemStack aRodStack = aMat_A[i].getLongRod(GTNH ? 16 : 8 * (aDub ? 2 : 1));
			ItemStack aScrewStack = aMat_B[i].getScrew(32 * (aDub ? 2 : 1));

			if (!ItemUtils.checkForInvalidItems(aPlateStack)) {
				aPlateStack = aMat_A[i].getPlate((GTNH ? 16 : 8) * (aDub ? 2 : 1) * 2);
				if (!ItemUtils.checkForInvalidItems(aPlateStack)) {
					aPlateStack = aMat_B[i].getPlateDouble((GTNH ? 16 : 8) * (aDub ? 2 : 1));
					if (!ItemUtils.checkForInvalidItems(aPlateStack)) {
						aPlateStack = aMat_B[i].getPlate((GTNH ? 16 : 8) * (aDub ? 2 : 1) * 2);
					}
				}
			}
			if (!ItemUtils.checkForInvalidItems(aGearStack)) {
				aGearStack = aMat_A[i].getGear(GTNH ? 8 : 4 * (aDub ? 2 : 1));				
			}
			if (!ItemUtils.checkForInvalidItems(aRodStack)) {
				aRodStack = aMat_B[i].getLongRod(GTNH ? 32 : 16 * (aDub ? 2 : 1));				
			}
			if (!ItemUtils.checkForInvalidItems(aScrewStack)) {
				aScrewStack = aMat_A[i].getScrew(32 * (aDub ? 2 : 1));				
			}

			CORE.RA.addSixSlotAssemblingRecipe(				
					new ItemStack[] {
							CI.getEnergyCore(i, 4),
							aPlateStack,
							aGearStack,
							aRodStack,
							aScrewStack,
							ItemUtils.getItemStackOfAmountFromOreDict(CI.getTieredCircuitOreDictName((int) (4+Math.ceil((double) i / (double) 2))), GTNH ? (i * 2 * 2) : (i * 2))
					},					
					CI.getTieredFluid(i, 144 * 4 * (i+1)), //Input Fluid					
					aOutput[i],					
					60 * 20 * 1 * (i+1), 
					MaterialUtils.getVoltageForTier(i));
		}		
	}

	private static void energyCores() {

		//Simpler Recipes for normal Players, Force assembly crafting in GTNH
		if (!GTNH) {
			//Buffer Core
			/*RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[1], cableTier1, CI.component_Plate[1],
					"circuitPrimitive", "plateStaballoy", "circuitPrimitive",
					CI.component_Plate[1], cableTier1, CI.component_Plate[1],
					RECIPE_BufferCore_ULV);
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[2], cableTier2, CI.component_Plate[2],
					RECIPE_BufferCore_ULV, CI.machineHull_HV, RECIPE_BufferCore_ULV,
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
					RECIPE_BufferCore_MAX);*/


			/*RecipeUtils.addShapedGregtechRecipe(
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
					RECIPE_Buffer_MAX);*/
		}


		ItemStack[] aBufferOutput = new ItemStack[] {
				RECIPE_Buffer_ULV, RECIPE_Buffer_LV, RECIPE_Buffer_MV,
				RECIPE_Buffer_HV, RECIPE_Buffer_EV, RECIPE_Buffer_IV, 
				RECIPE_Buffer_LuV, RECIPE_Buffer_ZPM, RECIPE_Buffer_UV, RECIPE_Buffer_MAX };



		ItemStack[] aOutput = new ItemStack[] {
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"1", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"2", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"3", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"4", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"5", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"6", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"7", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"8", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"9", 1),
				ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore"+"10", 1)
		};		

		int aCostMultiplier = GTNH ? 4 : 1;	

		for (int i = 0; i < 10; i++) {			

			ItemStack aPrevTier = (i == 0 ? CI.getTieredMachineHull(GTNH ? 2 : 1) : aOutput[i-1]);
			aPrevTier.stackSize = GTNH ? 2 : 1;
			int aTier = (i + 1);
			CORE.RA.addSixSlotAssemblingRecipe(				
					new ItemStack[] {
							aPrevTier,
							CI.getTieredComponent(OrePrefixes.plate, aTier, 4 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.cableGt04, i, 2 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.circuit, aTier, 2 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.screw, aTier, 6 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.bolt, i, 12 * (GTNH ? 2 : 1)),
					},					
					CI.getTieredFluid(i, (144 * 4 * aTier)), //Input Fluid					
					aOutput[i],					
					45 * 10 * 1 * (aTier), 
					MaterialUtils.getVoltageForTier(i));

			//Energy Buffer
			CORE.RA.addSixSlotAssemblingRecipe(				
					new ItemStack[] {
							ItemUtils.getSimpleStack(aOutput[i], 4),
							CI.getTieredComponent(OrePrefixes.plate, aTier, 8 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.wireGt08, i, 4 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.circuit, i, 4 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.stickLong, aTier, 4 * aCostMultiplier),
							CI.getTieredComponent(OrePrefixes.gearGt, i, 5 * (GTNH ? 2 : 1)),
					},					
					CI.getTieredFluid(aTier, (144 * 16 * aTier)), //Input Fluid					
					aBufferOutput[i],					
					45 * 20 * 1 * (aTier), 
					MaterialUtils.getVoltageForTier(i));

		}




	}

	private static void wirelessChargers() {

		ItemStack[] aChargers = new ItemStack[] {
				null,
				GregtechItemList.Charger_LV.get(1), 
				GregtechItemList.Charger_MV.get(1),
				GregtechItemList.Charger_HV.get(1), 
				GregtechItemList.Charger_EV.get(1), 
				GregtechItemList.Charger_IV.get(1), 
				GregtechItemList.Charger_LuV.get(1), 
				GregtechItemList.Charger_ZPM.get(1), 
				GregtechItemList.Charger_UV.get(1),
				GregtechItemList.Charger_MAX.get(1) 
		};

		int aCostMultiplier = GTNH ? 2 : 1;	

		for (int i = 0; i < 10; i++) {			
			if (i == 0) {
				continue;
			}			
			int aTier = (i + 1);			
			ItemStack[] aInputs = new ItemStack[] {
					CI.getTieredMachineHull(i, 1 * aCostMultiplier),
					CI.getEmitter(i, 2 * aCostMultiplier),
					CI.getSensor(i, 2 * aCostMultiplier),
					CI.getFieldGenerator(i, 1 * aCostMultiplier),
					CI.getTieredComponent(OrePrefixes.plate, aTier, 4 * aCostMultiplier),
					CI.getTieredComponent(OrePrefixes.circuit, aTier, 2 * aCostMultiplier),
			};			
			CORE.RA.addSixSlotAssemblingRecipe(				
					aInputs,					
					CI.getAlternativeTieredFluid(i, (144 * 2 * aTier)), //Input Fluid					
					aChargers[i],					
					45 * 10 * 1 * (aTier), 
					MaterialUtils.getVoltageForTier(i));	

		}
	}
	
	private static void largeArcFurnace() {
		int aCostMultiplier = GTNH ? 2 : 1;		
		CORE.RA.addSixSlotAssemblingRecipe(				
				new ItemStack[] {
						CI.getTieredMachineHull(-1, 1 * aCostMultiplier),
						CI.getEmitter(2, 2 * aCostMultiplier),
						CI.getElectricPiston(4, 2 * aCostMultiplier),
						CI.getSensor(4, 1 * aCostMultiplier),
						CI.getTieredComponent(OrePrefixes.plate, 5, 4 * aCostMultiplier),
						CI.getTieredComponent(OrePrefixes.pipeSmall, 4, 1 * aCostMultiplier),
				},					
				CI.getAlternativeTieredFluid(5, (144 * 2 * 4)), //Input Fluid					
				GregtechItemList.Casing_Industrial_Arc_Furnace.get(Casing_Amount),					
				20 * 10 * 1 * (6), 
				MaterialUtils.getVoltageForTier(5));		
		CORE.RA.addSixSlotAssemblingRecipe(				
				new ItemStack[] {
						GregtechItemList.Casing_Industrial_Arc_Furnace.get(Casing_Amount),
						CI.getFieldGenerator(4, 2 * aCostMultiplier),
						CI.getRobotArm(5, 4 * aCostMultiplier),
						CI.getEnergyCore(4, 2 * aCostMultiplier),
						CI.getTieredComponent(OrePrefixes.plate, 6, 8 * aCostMultiplier),
						CI.getTieredComponent(OrePrefixes.circuit, 5, 8 * aCostMultiplier),
				},					
				CI.getAlternativeTieredFluid(6, (144 * 4 * 5)), //Input Fluid					
				GregtechItemList.Industrial_Arc_Furnace.get(1),					
				60 * 20 * 8, 
				MaterialUtils.getVoltageForTier(6));
	}
}
