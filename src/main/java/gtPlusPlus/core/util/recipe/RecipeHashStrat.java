package gtPlusPlus.core.util.recipe;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gnu.trove.strategy.HashingStrategy;
import gregtech.api.util.GTRecipe;

public class RecipeHashStrat {

    public static final HashingStrategy<GTRecipe> RecipeHashingStrategy = new HashingStrategy<>() {

        @Override
        public int computeHashCode(GTRecipe recipe) {
            return Objects.hash(recipe.mDuration, recipe.mEUt, recipe.mInputs.length, recipe.mOutputs.length);
        }

        @Override
        public boolean equals(GTRecipe recipe1, GTRecipe recipe2) {
            return areRecipesEqual(recipe1, recipe2);
        }
    };

    private static final Comparator<ItemStack> itemStackComparator = Comparator
        .comparing((ItemStack itemStack) -> Item.getIdFromItem(itemStack.getItem()))
        .thenComparing(ItemStack::getItemDamage)
        .thenComparing(itemStack -> itemStack.stackSize);

    private static final Comparator<FluidStack> fluidStackComparator = Comparator.comparing(FluidStack::getFluidID)
        .thenComparing(fluidStack -> fluidStack.amount);

    public static boolean areRecipesEqual(GTRecipe recipe1, GTRecipe recipe2) {
        if (recipe1.mEUt != recipe2.mEUt) {
            return false;
        }

        if (recipe1.mDuration != recipe2.mDuration) {
            return false;
        }

        if (!areItemsStackArraysEqual(recipe1.mInputs, recipe2.mInputs)) {
            return false;
        }

        ItemStack[] recipe1OutputCopy = recipe1.mOutputs.clone();
        ItemStack[] recipe2OutputCopy = recipe2.mOutputs.clone();
        if (!areItemsStackArraysEqual(recipe1OutputCopy, recipe2OutputCopy)) {
            return false;
        }

        if (!areFluidStackArraysEqual(recipe1.mFluidInputs, recipe2.mFluidInputs)) {
            return false;
        }

        return areFluidStackArraysEqual(recipe1.mFluidOutputs, recipe2.mFluidOutputs);
    }

    private static boolean areItemsStackArraysEqual(ItemStack[] array1, ItemStack[] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        Arrays.sort(array1, itemStackComparator);
        Arrays.sort(array2, itemStackComparator);

        for (int i = 0; i < array1.length; i++) {
            if (array1[i].stackSize != array2[i].stackSize) {
                return false;
            }
            if (array1[i].getItem() != array2[i].getItem()) {
                return false;
            }
            if (array1[i].getItemDamage() != array2[i].getItemDamage()) {
                return false;
            }
        }
        return true;
    }

    private static boolean areFluidStackArraysEqual(FluidStack[] array1, FluidStack[] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        Arrays.sort(array1, fluidStackComparator);
        Arrays.sort(array2, fluidStackComparator);

        for (int i = 0; i < array1.length; i++) {
            if (array1[i].amount != array2[i].amount) {
                return false;
            }
            if (array1[i].getFluid() != array2[i].getFluid()) {
                return false;
            }
        }
        return true;
    }
}
