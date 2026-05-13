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

        Arrays.sort(recipe1.mInputs, itemStackComparator);
        Arrays.sort(recipe2.mInputs, itemStackComparator);
        if (!areItemsStackArraysEqual(recipe1.mInputs, recipe2.mInputs)) {
            return false;
        }

        ItemStack[] recipe1OutputCopy = recipe1.mOutputs.clone();
        ItemStack[] recipe2OutputCopy = recipe2.mOutputs.clone();
        Arrays.sort(recipe1OutputCopy, itemStackComparator);
        Arrays.sort(recipe2OutputCopy, itemStackComparator);
        if (!areItemsStackArraysEqual(recipe1OutputCopy, recipe2OutputCopy)) {
            return false;
        }

        Arrays.sort(recipe1.mFluidInputs, fluidStackComparator);
        Arrays.sort(recipe2.mFluidInputs, fluidStackComparator);
        if (!areFluidStackArraysEqual(recipe1.mFluidInputs, recipe2.mFluidInputs)) {
            return false;
        }

        Arrays.sort(recipe1.mFluidOutputs, fluidStackComparator);
        Arrays.sort(recipe2.mFluidOutputs, fluidStackComparator);
        return areFluidStackArraysEqual(recipe1.mFluidOutputs, recipe2.mFluidOutputs);
    }

    public static boolean areItemsStackArraysEqual(ItemStack[] array1, ItemStack[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
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

    public static boolean areFluidStackArraysEqual(FluidStack[] array1, FluidStack[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
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
