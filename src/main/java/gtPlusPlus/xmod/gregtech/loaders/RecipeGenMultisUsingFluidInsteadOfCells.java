package gtPlusPlus.xmod.gregtech.loaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
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
        int aInvalidRecipesToConvert = 0;
        int aOriginalCount = aInputs.getAllRecipes()
            .size();
        ArrayList<GTRecipe> deDuplicationInputArray = new ArrayList<>();

        recipe: for (GTRecipe x : aInputs.getAllRecipes()) {
            if (x != null) {

                ItemStack[] aInputItems = x.mInputs.clone();
                ItemStack[] aOutputItems = x.mOutputs.clone();
                FluidStack[] aInputFluids = x.mFluidInputs.clone();
                FluidStack[] aOutputFluids = x.mFluidOutputs.clone();

                ArrayList<ItemStack> aInputItemsMap = new ArrayList<>();
                ArrayList<ItemStack> aOutputItemsMap = new ArrayList<>();
                ArrayList<FluidStack> aInputFluidsMap = new ArrayList<>();
                ArrayList<FluidStack> aOutputFluidsMap = new ArrayList<>();

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

                if (!(ItemUtils.checkForInvalidItems(aNewItemInputs)
                    && ItemUtils.checkForInvalidItems(aNewItemOutputs))) {
                    aInvalidRecipesToConvert++;
                    continue; // Skip this recipe entirely if we find an item we don't like
                }
                GTRecipe aNewRecipe = new GTRecipe(
                    aNewItemInputs,
                    aNewItemOutputs,
                    x.mSpecialItems,
                    x.mChances,
                    aNewFluidInputs,
                    aNewFluidOutputs,
                    x.mDuration,
                    x.mEUt,
                    x.mSpecialValue);
                aNewRecipe.owners = x.owners == null ? null : new ArrayList<>(x.owners);

                // add all recipes to an intermediate array
                deDuplicationInputArray.add(aNewRecipe);

                aRecipesHandled++;
            } else {
                aInvalidRecipesToConvert++;
            }
        }
        // cast arraylist of input to a regular array and pass it to a duplicate recipe remover.
        List<GTRecipe> deDuplicationOutputArray = GTRecipeUtils
            .removeDuplicates(deDuplicationInputArray, aOutputs.unlocalizedName);
        // add each recipe from the above output to the intended recipe map
        for (GTRecipe recipe : deDuplicationOutputArray) {
            aOutputs.add(recipe);
        }
        Logger.INFO("Generated Recipes for " + aOutputs.unlocalizedName);
        Logger.INFO("Original Map contains " + aOriginalCount + " recipes.");
        Logger.INFO("Output Map contains " + aRecipesHandled + " recipes.");
        Logger.INFO("There were " + aInvalidRecipesToConvert + " invalid recipes.");
        return aRecipesHandled;
    }
}
