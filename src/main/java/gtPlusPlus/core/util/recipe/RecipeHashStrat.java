package gtPlusPlus.core.util.recipe;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gnu.trove.strategy.HashingStrategy;
import gregtech.api.util.GT_Recipe;

public class RecipeHashStrat {

    public static final HashingStrategy<GT_Recipe> RecipeHashingStrategy = new HashingStrategy<>() {

        @Override
        public int computeHashCode(GT_Recipe recipe) {
            return com.google.common.base.Objects.hashCode(recipe.mDuration, recipe.mEUt);
        }

        @Override
        public boolean equals(GT_Recipe recipe1, GT_Recipe recipe2) {
            return areRecipesEqual(recipe1, recipe2);
        }
    };

    public static boolean areRecipesEqual(GT_Recipe recipe1, GT_Recipe recipe2) {
        // both item outputs use a copy to prevent interfering with chance based output orders
        // sort all the arrays for recipe1
        RecipeHashStrat.sortItemStackArray(recipe1.mInputs);
        ItemStack[] recipe1OutputCopy = recipe1.mOutputs.clone();
        RecipeHashStrat.sortItemStackArray(recipe1OutputCopy);
        RecipeHashStrat.sortFluidStackArray(recipe1.mFluidInputs);
        RecipeHashStrat.sortFluidStackArray(recipe1.mFluidOutputs);
        // sort all the arrays for recipe2

        RecipeHashStrat.sortItemStackArray(recipe2.mInputs);
        ItemStack[] recipe2OutputCopy = recipe2.mOutputs.clone();
        RecipeHashStrat.sortItemStackArray(recipe2OutputCopy);
        RecipeHashStrat.sortFluidStackArray(recipe2.mFluidInputs);
        RecipeHashStrat.sortFluidStackArray(recipe2.mFluidOutputs);

        // checks if the recipe EUt, Duration, inputs and outputs for both items and fluids are equal
        if (recipe1.mEUt != recipe2.mEUt) {
            return false;
        }
        if (recipe1.mDuration != recipe2.mDuration) {
            return false;
        }
        if (!areItemsStackArraysEqual(recipe1.mInputs, recipe2.mInputs)) {
            return false;
        }
        if (!areItemsStackArraysEqual(recipe1OutputCopy, recipe2OutputCopy)) {
            return false;
        }
        if (!areFluidStackArraysEqual(recipe1.mFluidInputs, recipe2.mFluidInputs)) {
            return false;
        }
        if (!areFluidStackArraysEqual(recipe1.mFluidOutputs, recipe2.mFluidOutputs)) {
            return false;
        }
        return true;

    }

    public static void sortItemStackArray(ItemStack[] itemStackArray) {
        Arrays.sort(
                itemStackArray,
                Comparator.<ItemStack, Integer>comparing(itemStack -> Item.getIdFromItem(itemStack.getItem()))
                        .thenComparing(ItemStack::getItemDamage).thenComparing(itemStack -> itemStack.stackSize));
    }

    public static void sortFluidStackArray(FluidStack[] fluidStackArray) {
        Arrays.sort(
                fluidStackArray,
                Comparator.comparing(FluidStack::getFluidID).thenComparing(fluidStack -> fluidStack.amount));
    }

    public static boolean areItemsStackArraysEqual(ItemStack[] array1, ItemStack[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (!Objects.equals(array1[i].getItem(), array2[i].getItem())) {
                return false;
            }
            if (!Objects.equals(array1[i].getItemDamage(), array2[i].getItemDamage())) {
                return false;
            }
            if (!Objects.equals(array1[i].stackSize, array2[i].stackSize)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areFluidStackArraysEqual(FluidStack[] array1, FluidStack[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            // check if the string representation of both FluidStacks are not equal
            if (!Objects.equals(array1[i].getFluid(), array2[i].getFluid())) {
                return false;
            }
            if (!Objects.equals(array1[i].amount, array2[i].amount)) {
                return false;
            }
        }
        return true;
    }
}
