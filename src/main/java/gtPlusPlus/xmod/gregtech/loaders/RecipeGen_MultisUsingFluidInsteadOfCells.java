package gtPlusPlus.xmod.gregtech.loaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.recipe.GT_RecipeUtils;

public class RecipeGen_MultisUsingFluidInsteadOfCells {

    private static ItemStack mEmptyCell;
    private static final AutoMap<ItemStack> mItemsToIgnore = new AutoMap<>();
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
            return a.getItemDamage() == b.getItemDamage();
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
            return GT_Utility.areStacksEqual(aTempStack, aCell);
        }
        return false;
    }

    private static synchronized FluidStack getFluidFromItemStack(final ItemStack ingot) {
        if (ingot == null) {
            return null;
        }
        return GT_Utility.getFluidForFilledItem(ingot, true);
    }

    public static synchronized int generateRecipesNotUsingCells(RecipeMap<?> aInputs, RecipeMap<?> aOutputs) {
        init();
        int aRecipesHandled = 0;
        int aInvalidRecipesToConvert = 0;
        int aOriginalCount = aInputs.getAllRecipes().size();
        ArrayList<GT_Recipe> deDuplicationInputArray = new ArrayList<>();

        recipe: for (GT_Recipe x : aInputs.getAllRecipes()) {
            if (x != null) {

                ItemStack[] aInputItems = x.mInputs.clone();
                ItemStack[] aOutputItems = x.mOutputs.clone();
                FluidStack[] aInputFluids = x.mFluidInputs.clone();
                FluidStack[] aOutputFluids = x.mFluidOutputs.clone();

                AutoMap<ItemStack> aInputItemsMap = new AutoMap<>();
                AutoMap<ItemStack> aOutputItemsMap = new AutoMap<>();
                AutoMap<FluidStack> aInputFluidsMap = new AutoMap<>();
                AutoMap<FluidStack> aOutputFluidsMap = new AutoMap<>();

                // Iterate Inputs, Convert valid items into fluids
                for (ItemStack aInputStack : aInputItems) {
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
                    } else {
                        aFoundFluid.amount = aFoundFluid.amount * aInputStack.stackSize;
                        aInputFluidsMap.add(aFoundFluid);
                    }
                }
                // Iterate Outputs, Convert valid items into fluids
                for (ItemStack aOutputStack : aOutputItems) {
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
                    } else {
                        aFoundFluid.amount = aFoundFluid.amount * aOutputStack.stackSize;
                        aOutputFluidsMap.add(aFoundFluid);
                    }
                }
                // Add Input fluids second
                aInputFluidsMap.addAll(Arrays.asList(aInputFluids));
                // Add Output fluids second
                aOutputFluidsMap.addAll(Arrays.asList(aOutputFluids));

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

                if (!ItemUtils.checkForInvalidItems(aNewItemInputs, aNewItemOutputs)) {
                    aInvalidRecipesToConvert++;
                    continue; // Skip this recipe entirely if we find an item we don't like
                }
                GT_Recipe aNewRecipe = new GT_Recipe(
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
                aNewRecipe.owners = new ArrayList<>(x.owners);

                // add all recipes to an intermediate array
                deDuplicationInputArray.add(aNewRecipe);

                aRecipesHandled++;
            } else {
                aInvalidRecipesToConvert++;
            }
        }
        // cast arraylist of input to a regular array and pass it to a duplicate recipe remover.
        List<GT_Recipe> deDuplicationOutputArray = GT_RecipeUtils
                .removeDuplicates(deDuplicationInputArray, aOutputs.unlocalizedName);
        // add each recipe from the above output to the intended recipe map
        for (GT_Recipe recipe : deDuplicationOutputArray) {
            aOutputs.add(recipe);
        }
        Logger.INFO("Generated Recipes for " + aOutputs.unlocalizedName);
        Logger.INFO("Original Map contains " + aOriginalCount + " recipes.");
        Logger.INFO("Output Map contains " + aRecipesHandled + " recipes.");
        Logger.INFO("There were " + aInvalidRecipesToConvert + " invalid recipes.");
        return aRecipesHandled;
    }
}
