package gregtech.api.util;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class MultiblockRecipeMapHandler{

	public static void run() {

		if(CORE.ConfigSwitches.enableMultiblock_IndustrialElectrolyzer){
			generateSimpleMultimachineRecipeMap(GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, CustomRecipeMap.mMultiElectrolyzer);
		}
		if(CORE.ConfigSwitches.enableMultiblock_IndustrialCentrifuge){
			generateSimpleMultimachineRecipeMap(GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, CustomRecipeMap.mMultiCentrifuge);
		}
		if(CORE.ConfigSwitches.enableMultiblock_IndustrialMacerationStack){
			generateSimpleMultimachineRecipeMap(GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, CustomRecipeMap.mMultiMacerator);
		}
		if(CORE.ConfigSwitches.enableMultiblock_IndustrialWireMill){
			generateSimpleMultimachineRecipeMap(GT_Recipe.GT_Recipe_Map.sWiremillRecipes, CustomRecipeMap.mMultiWireMill);
		}

	}























	public static boolean isCell(ItemStack cell) {
		if (cell == null){
			return false;
		}
		if (cell.getDisplayName().toLowerCase().contains("cell")
				|| cell.getUnlocalizedName().toLowerCase().contains("cell")
				|| FluidContainerRegistry.isFilledContainer(cell)) {
			if (cell.getDisplayName().toLowerCase().contains("plasma")
					|| cell.getUnlocalizedName().toLowerCase().contains("plasma")) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isCellEmpty(ItemStack cell){
		if (cell.getDisplayName().toLowerCase().contains("empty")
				|| cell.getUnlocalizedName().toLowerCase().contains("empty")
				) {
			return true;
		}
		return false;
	}

	private static ItemStack[] copyItemElements(ItemStack[] items){
		int inputCount = 0;
		ItemStack[] item = new ItemStack[9];
		for (ItemStack input : items) {
			if (!isCell(input)){
				item[inputCount] = input;
				inputCount++;
			}
		}
		return item;
	}
	
	private static ItemStack[] copyItemElementsWithCells(ItemStack[] items){
		int inputCount = 0;
		ItemStack[] item = new ItemStack[9];
		for (ItemStack input : items) {
				item[inputCount] = input;
				inputCount++;
		}
		return item;
	}

	private static FluidStack[] copyFluidElements(FluidStack[] fluids){
		FluidStack fluid[] = new FluidStack[9];
		for (int r = 0; r<fluids.length;r++){
			fluid[r] = fluids[r];
		}
		return fluid;
	}

	private static FluidStack[] addRemovedCellsToArray(ItemStack[] items, FluidStack[] fluids){
		ArrayList<FluidStack> fluidInputs = new ArrayList<FluidStack>();

		for (FluidStack fluid : fluids){
			fluidInputs.add(fluid);
		}

		for (ItemStack input : items) {
			if (input != null){
				if (isCell(input) && !isCellEmpty(input)){
					FluidStack cellFluid = FluidContainerRegistry.getFluidForFilledItem(input);
					if (cellFluid != null){
						cellFluid.amount = 1000;
						fluidInputs.add(cellFluid);						
					}
				}
			}
		}
		FluidStack[] returnArray = new FluidStack[fluidInputs.size()];
		for (int h=0;h<fluidInputs.size();h++){
			returnArray[h] = fluidInputs.get(h);
		}
		return returnArray;
	}

	private static void generateSimpleMultimachineRecipeMap(GT_Recipe_Map inputMap, CustomRecipeMap newMap) {
		GT_Recipe_Map r = inputMap;
		final Collection<GT_Recipe> x = r.mRecipeList;
		for (final GT_Recipe newBo : x) {			
			int duration = MathUtils.findPercentageOfInt(newBo.mDuration, 80);			
			if (newMap.addRecipe(new GT_Recipe(true, newBo.mInputs, newBo.mOutputs, newBo.mSpecialItems, newBo.mChances, newBo.mFluidInputs, newBo.mFluidOutputs, duration, newBo.mEUt, newBo.mSpecialValue), false, true, true) != null){
				Logger.INFO("Successfully added a simple recipe to the "+newMap.mNEIName+" map.");
			}
			else {
				Logger.INFO("Failed adding a simple recipe to the "+newMap.mNEIName+" map.");
			}
		}
	}
	
	private static void generateMultimachineRecipeMap(GT_Recipe_Map inputMap, CustomRecipeMap newMap) {
		GT_Recipe_Map r = inputMap;
		final Collection<GT_Recipe> x = r.mRecipeList;
		for (final GT_Recipe newBo : x) {
			ItemStack[] mInputs = copyItemElementsWithCells(newBo.mInputs);
			ItemStack[] mOutputs = copyItemElementsWithCells(newBo.mOutputs);
			FluidStack[] mFluidInputs = copyFluidElements(newBo.mFluidInputs);
			FluidStack[] mFluidOutputs = copyFluidElements(newBo.mFluidOutputs);			
			int duration = MathUtils.findPercentageOfInt(newBo.mDuration, 80);

			//Change bonus chances
			int[] outputChances = null;
			if (newBo.mChances != null){
				outputChances = newBo.mChances.clone();				
				for (int g=0;g<outputChances.length;g++){
					Logger.WARNING("Output["+g+"] chance = "+outputChances[g]);
					if (outputChances[g]<10000){
						int temp = outputChances[g];
						if (outputChances[g] < 8000 && outputChances[g] >= 1){
							outputChances[g] = temp+600;
							Logger.WARNING("Output["+g+"] chance now = "+outputChances[g]);
						}
						else if (outputChances[g] < 9000 && outputChances[g] >= 8000){
							outputChances[g] = temp+200;
							Logger.WARNING("Output["+g+"] chance now = "+outputChances[g]);
						}
						else if (outputChances[g] <= 9900 && outputChances[g] >= 9000){
							outputChances[g] = temp+100;
							Logger.WARNING("Output["+g+"] chance now = "+outputChances[g]);
						}
					}
				}
			}
			if (newMap.addRecipe(new GT_Recipe(true, mInputs, mOutputs, newBo.mSpecialItems, outputChances, mFluidInputs, mFluidOutputs, duration, newBo.mEUt, newBo.mSpecialValue), false, true, true) != null){
				Logger.INFO("Successfully added a recipe to the "+newMap.mNEIName+" map.");
			}
			else {
				Logger.INFO("Failed adding a recipe to the "+newMap.mNEIName+" map.");
			}
		}
	}



}
