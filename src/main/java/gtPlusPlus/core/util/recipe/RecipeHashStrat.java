package gtPlusPlus.core.util.recipe;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.hash.Fnv1a32;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.util.GTRecipe;
import it.unimi.dsi.fastutil.Hash;

public class RecipeHashStrat {

    public static final Hash.Strategy<GTRecipe> RecipeHashingStrategy = new Hash.Strategy<>() {

        @Override
        public int hashCode(GTRecipe recipe) {
            int hash = Fnv1a32.initialState();
            if (recipe == null) return hash;

            // let's call it fastest effort to distinguish obviously different recipes
            hash = Fnv1a32.hashStep(hash, recipe.mDuration);
            hash = Fnv1a32.hashStep(hash, recipe.mEUt);
            hash = Fnv1a32.hashStep(hash, recipe.mInputs.length);
            hash = Fnv1a32.hashStep(hash, recipe.mOutputs.length);
            hash = Fnv1a32.hashStep(hash, recipe.mFluidInputs.length);
            hash = Fnv1a32.hashStep(hash, recipe.mFluidOutputs.length);
            hash = Fnv1a32.hashStep(hash, recipe.mSpecialValue);
            hash = Fnv1a32.hashStep(hash, Objects.hashCode(recipe.mSpecialItems));
            hash = Fnv1a32.hashStep(hash, recipe.isNBTSensitive ? 1 : 0);
            hash = Fnv1a32.hashStep(hash, recipe.mCanBeBuffered ? 1 : 0);
            hash = Fnv1a32.hashStep(hash, recipe.mNeedsEmptyOutput ? 1 : 0);
            hash = Fnv1a32.hashStep(hash, Arrays.hashCode(recipe.mInputChances));
            hash = Fnv1a32.hashStep(hash, Arrays.hashCode(recipe.mOutputChances));
            hash = Fnv1a32.hashStep(hash, Arrays.hashCode(recipe.mFluidInputChances));
            hash = Fnv1a32.hashStep(hash, Arrays.hashCode(recipe.mFluidOutputChances));
            hash = Fnv1a32.hashStep(hash, metadataHash(recipe.getMetadataStorage()));
            hash = Fnv1a32.hashStep(hash, itemStacksHash(recipe.mInputs));
            hash = Fnv1a32.hashStep(hash, itemStacksHash(recipe.mOutputs));
            hash = Fnv1a32.hashStep(hash, fluidStacksHash(recipe.mFluidInputs));
            hash = Fnv1a32.hashStep(hash, fluidStacksHash(recipe.mFluidOutputs));

            return hash;
        }

        @Override
        public boolean equals(GTRecipe recipe1, GTRecipe recipe2) {
            return areRecipesEqual(recipe1, recipe2);
        }
    };

    private static final Comparator<ItemStack> itemStackComparator = Comparator
        .comparing((ItemStack itemStack) -> Item.getIdFromItem(itemStack.getItem()))
        .thenComparing(ItemStack::getItemDamage)
        .thenComparing(itemStack -> itemStack.stackSize)
        .thenComparing(
            itemStack -> itemStack.getTagCompound() == null ? ""
                : itemStack.getTagCompound()
                    .toString());

    private static final Comparator<FluidStack> fluidStackComparator = Comparator.comparing(FluidStack::getFluidID)
        .thenComparing(fluidStack -> fluidStack.amount);

    public static boolean areRecipesEqual(GTRecipe recipe1, GTRecipe recipe2) {
        if (recipe1 == null || recipe2 == null) {
            return false;
        }

        if (recipe1.mEUt != recipe2.mEUt) {
            return false;
        }

        if (recipe1.mDuration != recipe2.mDuration) {
            return false;
        }

        if (recipe1.mSpecialValue != recipe2.mSpecialValue
            || !Objects.equals(recipe1.mSpecialItems, recipe2.mSpecialItems)
            || recipe1.isNBTSensitive != recipe2.isNBTSensitive
            || recipe1.mCanBeBuffered != recipe2.mCanBeBuffered
            || recipe1.mNeedsEmptyOutput != recipe2.mNeedsEmptyOutput) {
            return false;
        }

        if (!Arrays.equals(recipe1.mInputChances, recipe2.mInputChances)
            || !Arrays.equals(recipe1.mOutputChances, recipe2.mOutputChances)
            || !Arrays.equals(recipe1.mFluidInputChances, recipe2.mFluidInputChances)
            || !Arrays.equals(recipe1.mFluidOutputChances, recipe2.mFluidOutputChances)) {
            return false;
        }

        if (!areMetadataEqual(recipe1.getMetadataStorage(), recipe2.getMetadataStorage())) {
            return false;
        }

        if (!areItemsStackArraysEqual(recipe1.mInputs, recipe2.mInputs)) {
            return false;
        }

        if (!areItemsStackArraysEqual(recipe1.mOutputs, recipe2.mOutputs)) {
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

        ItemStack[] sortedCopy1 = Arrays.copyOf(array1, array1.length);
        ItemStack[] sortedCopy2 = Arrays.copyOf(array2, array2.length);

        Arrays.sort(sortedCopy1, itemStackComparator);
        Arrays.sort(sortedCopy2, itemStackComparator);

        for (int i = 0; i < sortedCopy1.length; i++) {
            if (sortedCopy1[i].stackSize != sortedCopy2[i].stackSize) {
                return false;
            }
            if (sortedCopy1[i].getItem() != sortedCopy2[i].getItem()) {
                return false;
            }
            if (sortedCopy1[i].getItemDamage() != sortedCopy2[i].getItemDamage()) {
                return false;
            }
            if (!ItemStack.areItemStackTagsEqual(sortedCopy1[i], sortedCopy2[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean areFluidStackArraysEqual(FluidStack[] array1, FluidStack[] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        FluidStack[] sortedCopy1 = Arrays.copyOf(array1, array1.length);
        FluidStack[] sortedCopy2 = Arrays.copyOf(array2, array2.length);

        Arrays.sort(sortedCopy1, fluidStackComparator);
        Arrays.sort(sortedCopy2, fluidStackComparator);

        for (int i = 0; i < sortedCopy1.length; i++) {
            if (sortedCopy1[i].amount != sortedCopy2[i].amount) {
                return false;
            }
            if (sortedCopy1[i].getFluid() != sortedCopy2[i].getFluid()) {
                return false;
            }
        }
        return true;
    }

    private static int itemStacksHash(ItemStack[] stacks) {
        ItemStack[] sortedCopy = Arrays.copyOf(stacks, stacks.length);
        Arrays.sort(sortedCopy, itemStackComparator);
        int hash = Fnv1a32.initialState();
        for (ItemStack stack : sortedCopy) {
            hash = Fnv1a32.hashStep(hash, Item.getIdFromItem(stack.getItem()));
            hash = Fnv1a32.hashStep(hash, stack.getItemDamage());
            hash = Fnv1a32.hashStep(hash, stack.stackSize);
            hash = Fnv1a32.hashStep(hash, Objects.hashCode(stack.getTagCompound()));
        }
        return hash;
    }

    private static int fluidStacksHash(FluidStack[] stacks) {
        FluidStack[] sortedCopy = Arrays.copyOf(stacks, stacks.length);
        Arrays.sort(sortedCopy, fluidStackComparator);
        int hash = Fnv1a32.initialState();
        for (FluidStack stack : sortedCopy) {
            hash = Fnv1a32.hashStep(hash, stack.getFluidID());
            hash = Fnv1a32.hashStep(hash, stack.amount);
        }
        return hash;
    }

    private static boolean areMetadataEqual(IRecipeMetadataStorage first, IRecipeMetadataStorage second) {
        if (first.getEntries()
            .size()
            != second.getEntries()
                .size()) {
            return false;
        }
        for (Map.Entry<RecipeMetadataKey<?>, Object> entry : first.getEntries()) {
            if (!Objects.equals(entry.getValue(), second.getMetadata(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    private static int metadataHash(IRecipeMetadataStorage storage) {
        int hash = Fnv1a32.initialState();
        for (Map.Entry<RecipeMetadataKey<?>, Object> entry : storage.getEntries()) {
            hash = Fnv1a32.hashStep(hash, Objects.hashCode(entry.getKey()));
            hash = Fnv1a32.hashStep(hash, Objects.hashCode(entry.getValue()));
        }
        return hash;
    }
}
