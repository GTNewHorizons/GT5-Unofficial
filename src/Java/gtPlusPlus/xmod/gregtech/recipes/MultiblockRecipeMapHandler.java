package gtPlusPlus.xmod.gregtech.recipes;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import gregtech.api.util.CustomRecipeMap;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class MultiblockRecipeMapHandler{
	
	public static final CustomRecipeMap mMultiElectrolyzer = new CustomRecipeMap(new HashSet<GT_Recipe>(50), "gt.recipe.fissionfuel", "Fission Fuel Processing", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 0, 1, E, 1, E, true, true);
	public static final CustomRecipeMap mMultiCentrifuge = new CustomRecipeMap(new HashSet<GT_Recipe>(50), "gt.recipe.fissionfuel", "Fission Fuel Processing", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 0, 1, E, 1, E, true, true);
	public static final CustomRecipeMap mMultiMacerator = new CustomRecipeMap(new HashSet<GT_Recipe>(50), "gt.recipe.fissionfuel", "Fission Fuel Processing", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 0, 1, E, 1, E, true, true);
	public static final CustomRecipeMap mMultiWireMill = new CustomRecipeMap(new HashSet<GT_Recipe>(50), "gt.recipe.fissionfuel", "Fission Fuel Processing", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 0, 1, E, 1, E, true, true);
	
	
	
	public static void run() {
		
		if(CORE.configSwitches.enableMultiblock_IndustrialElectrolyzer){
			generateMultimachineRecipeMap(GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, mMultiElectrolyzer);
		}
		if(CORE.configSwitches.enableMultiblock_IndustrialCentrifuge){
			generateMultimachineRecipeMap(GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, mMultiCentrifuge);
		}
		if(CORE.configSwitches.enableMultiblock_IndustrialMacerationStack){
			generateMultimachineRecipeMap(GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, mMultiMacerator);
		}
		if(CORE.configSwitches.enableMultiblock_IndustrialWireMill){
			generateMultimachineRecipeMap(GT_Recipe.GT_Recipe_Map.sWiremillRecipes, mMultiWireMill);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public static boolean isCell(ItemStack cell) {
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
	
	private FluidStack[] copyFluidElements(FluidStack[] fluids){
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
			if (isCell(input) && !isCellEmpty(input)){
				FluidStack cellFluid = FluidContainerRegistry.getFluidForFilledItem(input);
				cellFluid.amount = 1000;
				fluidInputs.add(cellFluid);
			}
		}
		FluidStack[] returnArray = new FluidStack[fluidInputs.size()];
		for (int h=0;h<fluidInputs.size();h++){
			returnArray[h] = fluidInputs.get(h);
		}
		return returnArray;
	}

	private static void generateMultimachineRecipeMap(GT_Recipe_Map inputMap, CustomRecipeMap newMap) {
		GT_Recipe_Map r = inputMap;
		final Collection<GT_Recipe> x = r.mRecipeList;
		for (final GT_Recipe newBo : x) {
			ItemStack[] mInputs = copyItemElements(newBo.mInputs);
			ItemStack[] mOutputs = copyItemElements(newBo.mOutputs);
			FluidStack[] mFluidInputs = addRemovedCellsToArray(newBo.mInputs, newBo.mFluidInputs);
			FluidStack[] mFluidOutputs = addRemovedCellsToArray(newBo.mOutputs, newBo.mFluidOutputs);			
			int duration = MathUtils.findPercentageOfInt(newBo.mDuration, 80);
			
			//Change bonus chances
			int[] outputChances = null;
			if (newBo.mChances != null){
				outputChances = newBo.mChances.clone();				
				for (int g=0;g<outputChances.length;g++){
					Utils.LOG_WARNING("Output["+g+"] chance = "+outputChances[g]);
					if (outputChances[g]<10000){
						int temp = outputChances[g];
						if (outputChances[g] < 8000 && outputChances[g] >= 1){
							outputChances[g] = temp+600;
							Utils.LOG_WARNING("Output["+g+"] chance now = "+outputChances[g]);
						}
						else if (outputChances[g] < 9000 && outputChances[g] >= 8000){
							outputChances[g] = temp+200;
							Utils.LOG_WARNING("Output["+g+"] chance now = "+outputChances[g]);
						}
						else if (outputChances[g] <= 9900 && outputChances[g] >= 9000){
							outputChances[g] = temp+100;
							Utils.LOG_WARNING("Output["+g+"] chance now = "+outputChances[g]);
						}
					}
				}
			}
			newMap.addRecipe(true, mInputs, mOutputs, newBo.mSpecialItems, outputChances, mFluidInputs, mFluidOutputs, duration, newBo.mEUt, newBo.mSpecialValue);
		}
	}

	

}
