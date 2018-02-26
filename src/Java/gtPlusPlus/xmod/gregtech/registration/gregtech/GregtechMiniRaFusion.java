package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.Recipe_GT;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMiniRaFusion {

	public static void run() {
		//generateSlowFusionRecipes();
		// Register the Simple Fusion Entity.
		//GregtechItemList.Miniature_Fusion.set(new GregtechMetaTileEntity_CompactFusionReactor(993, "simplefusion.tier.00", "Ra, Sun God - Mk I", 6).getStackForm(1L));
		GregtechItemList.Miniature_Fusion.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(993, "basicmachine.simplefusion.tier.01", "Mímir", 8, "Universal Machine for Knowledge and Wisdom", Recipe_GT.Gregtech_Recipe_Map.sSlowFusion2Recipes, 2, 9, 64000, 0, 1, "Dehydrator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), false, false, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.SENSOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.FIELD_GENERATOR}).getStackForm(1L));
		run2();
		Logger.INFO("[Pocket Fusion] Generated "+mRecipeCount+" recipes for the Pocket Fusion Reactor.");


	}

	private static boolean run2() {
		addFusionReactorRecipe(Materials.Lithium, (16), Materials.Tungsten, (16), Materials.Iridium, (16), 32, 32768, 300000000);
		addFusionReactorRecipe(Materials.Deuterium, (125), Materials.Tritium, (125), Materials.Helium.getPlasma(125), 16, 4096, 40000000);  //Mark 1 Cheap //
		addFusionReactorRecipe(Materials.Deuterium, (125), Materials.Helium_3, (125), Materials.Helium.getPlasma(125), 16, 2048, 60000000); //Mark 1 Expensive //
		addFusionReactorRecipe(Materials.Carbon, (125), Materials.Helium_3, (125), Materials.Oxygen.getPlasma(125), 32, 4096, 80000000); //Mark 1 Expensive //
		addFusionReactorRecipe(Materials.Aluminium, (16), Materials.Lithium, (16), Materials.Sulfur.getPlasma(125), 32, 10240, 240000000); //Mark 2 Cheap
		addFusionReactorRecipe(Materials.Beryllium, (16), Materials.Deuterium, (375), Materials.Nitrogen.getPlasma(175), 16, 16384, 180000000); //Mark 2 Expensive //
		addFusionReactorRecipe(Materials.Silicon, (16), Materials.Magnesium, (16), Materials.Iron.getPlasma(125), 32, 8192, 360000000); //Mark 3 Cheap //
		addFusionReactorRecipe(Materials.Potassium, (16), Materials.Fluorine, (125), Materials.Nickel.getPlasma(125), 16, 32768, 480000000); //Mark 3 Expensive //
		addFusionReactorRecipe(Materials.Beryllium, (16), Materials.Tungsten, (16), Materials.Platinum, (16), 32, 32768, 150000000); //
		addFusionReactorRecipe(Materials.Neodymium, (16), Materials.Hydrogen, (48), Materials.Europium, (16), 64, 24576, 150000000); //
		addFusionReactorRecipe(Materials.Lutetium, (16), Materials.Chrome, (16), Materials.Americium, (16), 96, 49152, 200000000); //
		addFusionReactorRecipe(Materials.Plutonium, (16), Materials.Thorium, (16), Materials.Naquadah, (16), 64, 32768, 300000000); //
		addFusionReactorRecipe(Materials.Americium, (16), Materials.Naquadria, (16), Materials.Neutronium, (1), 1200, 98304, 600000000); //

		addFusionReactorRecipe(Materials.Tungsten, (16), Materials.Helium, (16), Materials.Osmium, (16), 64, 24578, 150000000); //
		addFusionReactorRecipe(Materials.Manganese, (16), Materials.Hydrogen, (16), Materials.Iron, (16), 64, 8192, 120000000); //
		addFusionReactorRecipe(Materials.Mercury, (16), Materials.Magnesium, (16), Materials.Uranium, (16), 64, 49152, 240000000); //
		addFusionReactorRecipe(Materials.Gold, (16), Materials.Aluminium, (16), Materials.Uranium, (16), 64, 49152, 240000000); //
		addFusionReactorRecipe(Materials.Uranium, (16), Materials.Helium, (16), Materials.Plutonium, (16), 128, 49152, 480000000); //
		addFusionReactorRecipe(Materials.Vanadium, (16), Materials.Hydrogen, (125), Materials.Chrome, (16), 64, 24576, 140000000); //
		addFusionReactorRecipe(Materials.Gallium, (16), Materials.Radon, (125), Materials.Duranium, (16), 64, 16384, 140000000); //
		addFusionReactorRecipe(Materials.Titanium, (48), Materials.Duranium, (32), Materials.Tritanium, (16), 64, 32768, 200000000); //
		addFusionReactorRecipe(Materials.Gold, (16), Materials.Mercury, (16), Materials.Radon, (125), 64, 32768, 200000000); //
		addFusionReactorRecipe(Materials.Tantalum, (16), Materials.Tritium, (16), Materials.Tungsten, (16), 16, 24576, 200000000); //
		addFusionReactorRecipe(Materials.Silver, (16), Materials.Lithium, (16), Materials.Indium, (16), 32, 24576, 380000000); //
		addFusionReactorRecipe(Materials.NaquadahEnriched, (15), Materials.Radon, (125), Materials.Naquadria, (3), 64, 49152, 400000000); //

		if (mRecipeCount > 0)
			return true;
		return false;
	}

	private static void addFusionReactorRecipe(Materials molten, int amnt, Materials gas, int amnt2, FluidStack plasma, int i, int j, int k) {/*
		Materials Fusion[] = new Materials[2];
		int amount[] = new int[2];
		int emptyAmount = 0;

		Fusion[0] = molten;
		amount[0] = amnt;
		Fusion[1] = gas;
		amount[1] = amnt2;

		ItemStack[] inputs = new ItemStack[9];
		ItemStack[][] temp = new ItemStack[2][9];
		for (int r=0;r<2;r++) {
			ItemStack tempCell;
			ItemStack cellsA = null;
			ItemStack cellsB = null;
			ItemStack cellsC = null;
			
			if (Fusion[r].getCells(1) == null) {
				Logger.INFO("[Pocket Fusion] Error trying to get a cell of "+MaterialUtils.getMaterialName(Fusion[r])+", using backup method.");
				Material ju = MaterialUtils.generateMaterialFromGtENUM(Fusion[r]);
				ItemStack htng = ju.getCell(1);
				if (htng == null) {
					Logger.INFO("[Pocket Fusion] Error trying to get a cell of "+MaterialUtils.getMaterialName(Fusion[r])+", failed using backup method.");
					new BaseItemCell(ju);
					tempCell = ItemUtils.getItemStackOfAmountFromOreDict("cell"+MaterialUtils.getMaterialName(Fusion[r]), 1);	
					//continue;
				}
				else {
					tempCell = htng;					
				}
			}
			else {
				tempCell = Fusion[r].getCells(1);
			}
					
			if (amount[r] <= 64) {
				cellsA = ItemUtils.getSimpleStack(tempCell, amount[r]);
				temp[r][0] = cellsA;
			}
			else if (amount[r] > 64 && amount[r] <= 128) {
				cellsA = ItemUtils.getSimpleStack(tempCell, 64);
				cellsB = ItemUtils.getSimpleStack(tempCell, amount[r]-64);
				temp[r][0] = cellsA;
				temp[r][1] = cellsB;	
			}
			else if (amount[r] > 128 && amount[r] <= 192) {
				cellsA = ItemUtils.getSimpleStack(tempCell, 64);
				cellsA = ItemUtils.getSimpleStack(tempCell, 64);
				cellsA = ItemUtils.getSimpleStack(tempCell, amount[r]-128);
				temp[r][0] = cellsA;
				temp[r][1] = cellsB;
				temp[r][2] = cellsC;
			}	
			emptyAmount += amount[r];		
		}

		//Build Clean Stacks
		int index = 0;
		for (int r=0;r<2;r++) {
			for (int g=0;g<9;g++) {
				if (temp[r][g] != null) {
					inputs[(r == 0 ? index++ : (3+index++))] = temp[r][g];
				}
			}
		}		

		ItemStack[] outputCells = new ItemStack[9];
		int tempInt = emptyAmount;
		int tempCounter = 0;
		while (tempInt > 0) {
			if (tempInt >= 64) {
				outputCells[tempCounter++] = ItemUtils.getEmptyCell(64);
				tempInt -= 64;
			}
			else {
				outputCells[tempCounter++] = ItemUtils.getEmptyCell(tempInt);
				tempInt -= tempInt;
			}
		}

		ItemStack[] cleanOutput = ArrayUtils.removeNulls(outputCells);

		FluidStack bigPlasma = plasma.copy();
		bigPlasma.amount = plasma.amount*1000;

		if (bigPlasma.amount > 32000) {
			Logger.INFO("[Pocket Fusion] Generated recipe for "+plasma.getLocalizedName()+" had incorrect output.");
		}

		if (Recipe_GT.Gregtech_Recipe_Map.sSlowFusion2Recipes.addRecipe(
				true, 
				inputs,
				cleanOutput, 
				null,
				null, //Fluid In
				new FluidStack[] {bigPlasma}, //Fluid Out
				i*8, //Duration
				j, //Eu
				k //Special 
				) != null){
			mRecipeCount++;					
		}
	*/}


	public static int mRecipeCount = 0;
	private static void addFusionReactorRecipe(Materials molten, int amnt, Materials gas, int amnt2, Materials output, int amnt3, int i, int j, int k) {/*
		Materials Fusion[] = new Materials[3];
		int amount[] = new int[3];
		int emptyAmount = 0;
		int outputCells = 0;

		Fusion[0] = molten;
		amount[0] = amnt;
		Fusion[1] = gas;
		amount[1] = amnt2;
		Fusion[2] = output;
		amount[2] = amnt3;

		ItemStack[] inputs = new ItemStack[9];
		ItemStack[][] temp = new ItemStack[3][9];
		ItemStack[] outputs = new ItemStack[9];
		for (int r=0;r<3;r++) {
			ItemStack cellsA = null;
			ItemStack cellsB = null;
			ItemStack cellsC = null;
			ItemStack tempCell;	
			
			if (Fusion[r].getCells(1) == null) {
				Logger.INFO("[Pocket Fusion] Error trying to get a cell of "+MaterialUtils.getMaterialName(Fusion[r])+", using backup method.");
				Material ju = MaterialUtils.generateMaterialFromGtENUM(Fusion[r]);
				ItemStack htng = ju.getCell(1);
				if (htng == null) {
					Logger.INFO("[Pocket Fusion] Error trying to get a cell of "+MaterialUtils.getMaterialName(Fusion[r])+", failed using backup method.");
					new BaseItemCell(ju);
					tempCell = ItemUtils.getItemStackOfAmountFromOreDict("cell"+MaterialUtils.getMaterialName(Fusion[r]), 1);	
					//continue;
				}
				else {
					tempCell = htng;					
				}
			}
			else {
				tempCell = Fusion[r].getCells(1);
			}			
			
			if (amount[r] <= 64) {
				cellsA = ItemUtils.getSimpleStack(tempCell, amount[r]);
				temp[r][0] = cellsA;
			}
			else if (amount[r] > 64 && amount[r] <= 128) {
				cellsA = ItemUtils.getSimpleStack(tempCell, 64);
				cellsB = ItemUtils.getSimpleStack(tempCell, amount[r]-64);
				temp[r][0] = cellsA;
				temp[r][1] = cellsB;	
			}
			else if (amount[r] > 128 && amount[r] <= 192) {
				cellsA = ItemUtils.getSimpleStack(tempCell, 64);
				cellsA = ItemUtils.getSimpleStack(tempCell, 64);
				cellsA = ItemUtils.getSimpleStack(tempCell, amount[r]-128);
				temp[r][0] = cellsA;
				temp[r][1] = cellsB;
				temp[r][2] = cellsC;
			}	
			if (r< 2) {
				emptyAmount += amount[r];				
			}	
			else {
				outputCells += amount[r];
			}
		}

		//Build Clean Stacks
		int index = 0;
		for (int r=0;r<3;r++) {
			for (int g=0;g<9;g++) {
				if (temp[r][g] != null) {
					if (r < 2) {
						inputs[(r == 0 ? index++ : (3+index++))] = temp[r][g];						
					}
					else {
						outputs[index++] = temp[r][g];			
					}
				}
			}
		}			

		ItemStack[] outputEmptyCells = new ItemStack[9];
		int tempInt = emptyAmount-outputCells;
		int tempCounter = 0;
		while (tempInt > 0) {
			if (tempInt >= 64) {
				outputEmptyCells[tempCounter++] = ItemUtils.getEmptyCell(64);
				tempInt -= 64;
			}
			else {
				outputEmptyCells[tempCounter++] = ItemUtils.getEmptyCell(tempInt);
				tempInt -= tempInt;
			}
		}	

		ItemStack[] comboOutput = new ItemStack[9];
		int jhn = 0;
		for (int b=0;b<9;b++) {

			if (outputs[b] != null) {
				comboOutput[b] = outputs[b];
				jhn++;
			}
			else {
				if (outputEmptyCells[(b-jhn)] != null) {
					comboOutput[b] = outputEmptyCells[(b-jhn)];
				}
			}

		}

		if (Recipe_GT.Gregtech_Recipe_Map.sSlowFusion2Recipes.addRecipe(
				true, 
				inputs,
				comboOutput, 
				null,
				null, //Fluid In
				null, //Fluid Out
				i*16, //Duration
				j, //Eu
				k //Special 
				) != null){
			mRecipeCount++;					
		}
	*/}
	
}
