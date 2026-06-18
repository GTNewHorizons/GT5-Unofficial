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
import gregtech.api.util.GTRecipe.GTRecipe_WithAlt;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.recipe.GTRecipeUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class RecipeGenMultisUsingFluidInsteadOfCells {

    private static final ArrayList<ItemStack> mEmptyItems = new ArrayList<>();
    private static final ArrayList<ItemStack> mItemsToIgnore = new ArrayList<>();
    private static boolean mInit = false;

    private static void init() {
        if (!mInit) {
            mInit = true;

            ItemStack emptyCell = ItemList.Cell_Empty.get(1);
            mEmptyItems.add(emptyCell);
            mEmptyItems.add(new ItemStack(Items.bowl));
            mEmptyItems.add(new ItemStack(Items.bucket));
            mEmptyItems.add(new ItemStack(Items.glass_bottle));
            // Electrolyzed Water Cell
            mItemsToIgnore.add(new ItemStack(emptyCell.getItem(), 1, 8));
        }
    }

    private static boolean isEmptyCell(ItemStack aCell) {
        if (aCell == null) {
            return false;
        }
        for (ItemStack emptyItem : mEmptyItems) {
            if (GTUtility.areStacksEqual(emptyItem, aCell, true)) {
                return true;
            }
        }
        return false;
    }

    public static void generateRecipesNotUsingCells(RecipeMap<?> aInputs, RecipeMap<?> aOutputs) {
        init();
        ArrayList<GTRecipe> deDuplicationInputArray = new ArrayList<>();

        recipeLoop: for (GTRecipe recipe : aInputs.getAllRecipes()) {
            if (recipe == null) continue;

            ArrayList<ItemStack> aInputItemsMap = new ArrayList<>();
            ArrayList<ItemStack> aOutputItemsMap = new ArrayList<>();
            ArrayList<FluidStack> aInputFluidsMap = new ArrayList<>();
            ArrayList<FluidStack> aOutputFluidsMap = new ArrayList<>();

            // For GTRecipe_WithAlt
            List<ItemStack[]> oreDictList = null;
            IntList oreDictIds = null;
            GTRecipe_WithAlt altRecipe = null;

            if (recipe instanceof GTRecipe_WithAlt alt) {
                altRecipe = alt;
                if (altRecipe.mOreDictAlt != null) oreDictList = new ArrayList<>(altRecipe.mOreDictAlt.length);
                if (altRecipe.mOreDictIds != null) oreDictIds = new IntArrayList(altRecipe.mOreDictIds.length);
            }

            // Iterate Inputs, Convert valid items into fluids
            for (int i = 0; i < recipe.mInputs.length; i++) {
                ItemStack aInputStack = recipe.mInputs[i];
                FluidStack aFoundFluid = GTUtility.getFluidForFilledItem(aInputStack, true);
                if (aFoundFluid == null) {
                    for (ItemStack aBadStack : mItemsToIgnore) {
                        if (GTUtility.areStacksEqual(aInputStack, aBadStack, true)) {
                            continue recipeLoop; // Skip this recipe entirely if we find an item we don't like
                        }
                    }
                    if (!isEmptyCell(aInputStack)) {
                        aInputItemsMap.add(aInputStack);
                        if (altRecipe != null) {
                            if (oreDictList != null && i < altRecipe.mOreDictAlt.length)
                                oreDictList.add(altRecipe.mOreDictAlt[i]);
                            if (oreDictIds != null && i < altRecipe.mOreDictIds.length)
                                oreDictIds.add(altRecipe.mOreDictIds[i]);
                        }
                    }
                } else {
                    aFoundFluid.amount = aFoundFluid.amount * aInputStack.stackSize;
                    aInputFluidsMap.add(aFoundFluid);
                }
            }

            // Iterate Outputs, Convert valid items into fluids
            for (ItemStack aOutputStack : recipe.mOutputs) {
                FluidStack aFoundFluid = GTUtility.getFluidForFilledItem(aOutputStack, true);
                if (aFoundFluid == null) {
                    for (ItemStack aBadStack : mItemsToIgnore) {
                        if (GTUtility.areStacksEqual(aOutputStack, aBadStack, true)) {
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

            GTRecipe aNewRecipe = recipe.copyShallow();
            aNewRecipe.setInputs(aNewItemInputs);
            aNewRecipe.setOutputs(aNewItemOutputs);
            aNewRecipe.setFluidInputs(aNewFluidInputs);
            aNewRecipe.setFluidOutputs(aNewFluidOutputs);

            if (aNewRecipe instanceof GTRecipe_WithAlt alt) {
                if (oreDictList != null) alt.mOreDictAlt = oreDictList.toArray(new ItemStack[0][]);
                if (oreDictIds != null) alt.mOreDictIds = oreDictIds.toIntArray();
            }

            aNewRecipe.owners = recipe.owners == null ? null : new ArrayList<>(recipe.owners);

            // add all recipes to an intermediate array
            deDuplicationInputArray.add(aNewRecipe);
        }

        // cast arraylist of input to a regular array and pass it to a duplicate recipe remover.
        List<GTRecipe> deDuplicationOutputArray = GTRecipeUtils.removeDuplicates(deDuplicationInputArray);
        // add each recipe from the above output to the intended recipe map
        for (GTRecipe recipe : deDuplicationOutputArray) {
            aOutputs.add(recipe);
        }
    }
}
