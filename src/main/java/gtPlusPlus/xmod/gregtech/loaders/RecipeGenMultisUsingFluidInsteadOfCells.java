package gtPlusPlus.xmod.gregtech.loaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.recipe.GTRecipeUtils;

public class RecipeGenMultisUsingFluidInsteadOfCells {

    private static final ArrayList<ItemStack> mEmptyItems = new ArrayList<>();
    private static final ArrayList<ItemStack> mItemsToIgnore = new ArrayList<>();
    private static boolean mInit = false;

    private static void init() {
        if (!mInit) {
            mInit = true;

            mEmptyItems.add(ItemList.Cell_Empty.get(1));
            mEmptyItems.add(new ItemStack(Items.bowl));
            mEmptyItems.add(new ItemStack(Items.bucket));
            mEmptyItems.add(new ItemStack(Items.glass_bottle));
            mItemsToIgnore.add(
                new ItemStack(
                    ItemList.Cell_Empty.get(1)
                        .getItem(),
                    1,
                    8));
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
        for (ItemStack emptyItem : mEmptyItems) {
            emptyItem.stackSize = aCell.stackSize;
            if (GTUtility.areStacksEqual(emptyItem, aCell)) {
                return true;
            }

        }
        return false;
    }

    private static synchronized FluidStack getFluidFromItemStack(final ItemStack ingot) {
        if (ingot == null) {
            return null;
        }
        return GTUtility.getFluidForFilledItem(ingot, true);
    }

    public static synchronized int generateRecipesNotUsingCells(RecipeMap<?> aInputs, RecipeMap<?> aOutputs) {
        init();
        int aRecipesHandled = 0;
        ArrayList<GTRecipe> deDuplicationInputArray = new ArrayList<>();

        recipeLoop: for (GTRecipe recipe : aInputs.getAllRecipes()) {
            if (recipe == null) continue;

            ArrayList<ItemStack> aInputItemsMap = new ArrayList<>();
            ArrayList<ItemStack> aOutputItemsMap = new ArrayList<>();
            ArrayList<FluidStack> aInputFluidsMap = new ArrayList<>();
            ArrayList<FluidStack> aOutputFluidsMap = new ArrayList<>();

            // Iterate Inputs, Convert valid items into fluids
            for (ItemStack aInputStack : recipe.mInputs) {
                FluidStack aFoundFluid = getFluidFromItemStack(aInputStack);
                if (aFoundFluid == null) {
                    for (ItemStack aBadStack : mItemsToIgnore) {
                        if (doesItemMatchIgnoringStackSize(aInputStack, aBadStack)) {
                            continue recipeLoop; // Skip this recipe entirely if we find an item we don't like
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
            for (ItemStack aOutputStack : recipe.mOutputs) {
                FluidStack aFoundFluid = getFluidFromItemStack(aOutputStack);
                if (aFoundFluid == null) {
                    for (ItemStack aBadStack : mItemsToIgnore) {
                        if (doesItemMatchIgnoringStackSize(aOutputStack, aBadStack)) {
                            continue recipeLoop; // Skip this recipe entirely if we find an item we don't like
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
            Collections.addAll(aInputFluidsMap, recipe.mFluidInputs);
            // Add Output fluids second
            Collections.addAll(aOutputFluidsMap, recipe.mFluidOutputs);

            // Make some new Arrays
            ItemStack[] aNewItemInputs = aInputItemsMap.toArray(new ItemStack[0]);
            ItemStack[] aNewItemOutputs = aOutputItemsMap.toArray(new ItemStack[0]);
            FluidStack[] aNewFluidInputs = aInputFluidsMap.toArray(new FluidStack[0]);
            FluidStack[] aNewFluidOutputs = aOutputFluidsMap.toArray(new FluidStack[0]);

            if (!(ItemUtils.checkForInvalidItems(aNewItemInputs) && ItemUtils.checkForInvalidItems(aNewItemOutputs))) {
                continue; // Skip this recipe entirely if we find an item we don't like
            }

            GTRecipe aNewRecipe = new GTRecipe(
                aNewItemInputs,
                aNewItemOutputs,
                recipe.mSpecialItems,
                recipe.mInputChances,
                recipe.mOutputChances,
                recipe.mFluidInputChances,
                recipe.mFluidOutputChances,
                aNewFluidInputs,
                aNewFluidOutputs,
                recipe.mDuration,
                recipe.mEUt,
                recipe.mSpecialValue);
            aNewRecipe.owners = recipe.owners == null ? null : new ArrayList<>(recipe.owners);

            // add all recipes to an intermediate array
            deDuplicationInputArray.add(aNewRecipe);

            aRecipesHandled++;
        }

        // cast arraylist of input to a regular array and pass it to a duplicate recipe remover.
        List<GTRecipe> deDuplicationOutputArray = GTRecipeUtils.removeDuplicates(deDuplicationInputArray);
        // add each recipe from the above output to the intended recipe map
        for (GTRecipe recipe : deDuplicationOutputArray) {
            aOutputs.add(recipe);
        }

        return aRecipesHandled;
    }
}
