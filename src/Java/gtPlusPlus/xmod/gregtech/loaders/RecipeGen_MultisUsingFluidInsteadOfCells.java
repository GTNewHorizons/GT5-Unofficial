package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map_Internal;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_MultisUsingFluidInsteadOfCells {


	private static ItemStack mEmptyCell;
	private static AutoMap<ItemStack> mItemsToIgnore = new AutoMap<ItemStack>();
	private static boolean mInit = false;

	private static void init() {
		if (!mInit) {
			mInit = true;
			mItemsToIgnore.add(ItemUtils.simpleMetaStack(CI.emptyCells(1).getItem(), 8, 1));


		}		
	}

	private static boolean doesItemMatchIgnoringStackSize(ItemStack a, ItemStack b) {
		if (a == null || b == null) {
			return false;
		}
		if (a.getItem() == b.getItem()) {
			if (a.getItemDamage() == b.getItemDamage()) {
				return true;
			}
		}
		return false;
	}

	private static boolean isEmptyCell(ItemStack aCell) {
		if (aCell == null) {
			return false;
		}
		if (mEmptyCell == null) {
			mEmptyCell = CI.emptyCells(1);
		}
		if (mEmptyCell != null) {
			ItemStack aTempStack = mEmptyCell.copy();
			aTempStack.stackSize = aCell.stackSize;
			if (GT_Utility.areStacksEqual(aTempStack, aCell)) {
				return true;
			}
		}		
		return false;
	}

	private synchronized static FluidStack getFluidFromItemStack(final ItemStack ingot) {
		if (ingot == null) {
			return null;
		}
		FluidStack aFluid = GT_Utility.getFluidForFilledItem(ingot, true);
		if (aFluid != null) {
			return aFluid;
		}
		return null;	
	}

	public synchronized static int generateRecipesNotUsingCells(GT_Recipe_Map aInputs, GTPP_Recipe_Map_Internal aOutputs) {
		init();
		int aRecipesHandled = 0;
		int aInvalidRecipesToConvert = 0;		
		int aOriginalCount = aInputs.mRecipeList.size();

		recipe : for (GT_Recipe x : aInputs.mRecipeList) {
			if (x != null) {

				ItemStack[] aInputItems = x.mInputs.clone();
				ItemStack[] aOutputItems = x.mOutputs.clone();
				FluidStack[] aInputFluids = x.mFluidInputs.clone();
				FluidStack[] aOutputFluids = x.mFluidOutputs.clone();

				AutoMap<ItemStack> aInputItemsMap = new AutoMap<ItemStack>();
				AutoMap<ItemStack> aOutputItemsMap = new AutoMap<ItemStack>();
				AutoMap<FluidStack> aInputFluidsMap = new AutoMap<FluidStack>();
				AutoMap<FluidStack> aOutputFluidsMap = new AutoMap<FluidStack>();

				// Iterate Inputs, Convert valid items into fluids
				inputs : for (ItemStack aInputStack : aInputItems) {
					FluidStack aFoundFluid = getFluidFromItemStack(aInputStack);
					if (aFoundFluid == null) {
						for (ItemStack aBadStack : mItemsToIgnore) {
							if (doesItemMatchIgnoringStackSize(aInputStack, aBadStack)) {
								continue recipe; // Skip this recipe entirely if we find an item we don't like
							}
						}
						if (!isEmptyCell(aInputStack)) {
							aInputItemsMap.add(aInputStack);
						}
					}
					else {
						aFoundFluid.amount = aFoundFluid.amount * aInputStack.stackSize;
						aInputFluidsMap.add(aFoundFluid);
					}
				}
				// Iterate Outputs, Convert valid items into fluids
				outputs: for (ItemStack aOutputStack : aOutputItems) {
					FluidStack aFoundFluid = getFluidFromItemStack(aOutputStack);
					if (aFoundFluid == null) {
						for (ItemStack aBadStack : mItemsToIgnore) {
							if (doesItemMatchIgnoringStackSize(aOutputStack, aBadStack)) {
								continue recipe; // Skip this recipe entirely if we find an item we don't like
							}
						}
						if (!isEmptyCell(aOutputStack)) {
							aOutputItemsMap.add(aOutputStack);
						}
					}
					else {
						aFoundFluid.amount = aFoundFluid.amount * aOutputStack.stackSize;
						aOutputFluidsMap.add(aFoundFluid);
					}
				}
				// Add Input fluids second
				for (FluidStack aInputFluid : aInputFluids) {
					aInputFluidsMap.add(aInputFluid);					
				}
				// Add Output fluids second
				for (FluidStack aOutputFluid : aOutputFluids) {
					aOutputFluidsMap.add(aOutputFluid);					
				}

				// Make some new Arrays
				ItemStack[] aNewItemInputs = new ItemStack[aInputItemsMap.size()];
				ItemStack[] aNewItemOutputs = new ItemStack[aOutputItemsMap.size()];
				FluidStack[] aNewFluidInputs = new FluidStack[aInputFluidsMap.size()];
				FluidStack[] aNewFluidOutputs = new FluidStack[aOutputFluidsMap.size()];

				// Add AutoMap contents to Arrays
				for (int i = 0; i < aInputItemsMap.size(); i++) {
					aNewItemInputs[i] = aInputItemsMap.get(i);
				}
				for (int i = 0; i < aOutputItemsMap.size(); i++) {
					aNewItemOutputs[i] = aOutputItemsMap.get(i);
				}
				for (int i = 0; i < aInputFluidsMap.size(); i++) {
					aNewFluidInputs[i] = aInputFluidsMap.get(i);
				}
				for (int i = 0; i < aOutputFluidsMap.size(); i++) {
					aNewFluidOutputs[i] = aOutputFluidsMap.get(i);
				}

				// Add Recipe to map
				GT_Recipe aNewRecipe = new GTPP_Recipe(
						false,
						aNewItemInputs,
						aNewItemOutputs,
						x.mSpecialItems,
						x.mChances,
						aNewFluidInputs,
						aNewFluidOutputs,
						x.mDuration,
						x.mEUt,
						x.mSpecialValue);    	
				aOutputs.add(aNewRecipe);
				aRecipesHandled++;
			}
			else {
				aInvalidRecipesToConvert++;
			}
		}		

		Logger.INFO("Generated Recipes for "+aOutputs.mNEIName);
		Logger.INFO("Original Map contains "+aOriginalCount+" recipes.");
		Logger.INFO("Output Map contains "+aRecipesHandled+" recipes.");
		Logger.INFO("There were "+aInvalidRecipesToConvert+" invalid recipes.");
		return aRecipesHandled;
	}

}
