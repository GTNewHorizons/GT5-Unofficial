package gregtech.api.util;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;

public class GTRecipe_WithAlt extends GTRecipe {

    public ItemStack[][] mOreDictAlt;

    /**
     * Only for {@link GTRecipeBuilder}.
     */
    GTRecipe_WithAlt(ItemStack[] mInputs, ItemStack[] mOutputs, FluidStack[] mFluidInputs, FluidStack[] mFluidOutputs,
        int[] mChances, Object mSpecialItems, int mDuration, int mEUt, int mSpecialValue, boolean mEnabled,
        boolean mHidden, boolean mFakeRecipe, boolean mCanBeBuffered, boolean mNeedsEmptyOutput, boolean nbtSensitive,
        String[] neiDesc, @Nullable IRecipeMetadataStorage metadataStorage, RecipeCategory recipeCategory,
        ItemStack[][] mOreDictAlt) {
        super(
            mInputs,
            mOutputs,
            mFluidInputs,
            mFluidOutputs,
            mChances,
            mSpecialItems,
            mDuration,
            mEUt,
            mSpecialValue,
            mEnabled,
            mHidden,
            mFakeRecipe,
            mCanBeBuffered,
            mNeedsEmptyOutput,
            nbtSensitive,
            neiDesc,
            metadataStorage,
            recipeCategory);
        this.mOreDictAlt = mOreDictAlt;
    }

    public GTRecipe_WithAlt(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
        int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue, ItemStack[][] aAlt) {
        super(
            aOptimize,
            aInputs,
            aOutputs,
            aSpecialItems,
            aChances,
            aFluidInputs,
            aFluidOutputs,
            aDuration,
            aEUt,
            aSpecialValue);
        mOreDictAlt = aAlt;
    }

    public Object getAltRepresentativeInput(int aIndex) {
        if (aIndex < 0) return null;
        if (aIndex < mOreDictAlt.length) {
            if (mOreDictAlt[aIndex] != null && mOreDictAlt[aIndex].length > 0) {
                ItemStack[] rStacks = new ItemStack[mOreDictAlt[aIndex].length];
                for (int i = 0; i < mOreDictAlt[aIndex].length; i++) {
                    rStacks[i] = GTUtility.copyOrNull(mOreDictAlt[aIndex][i]);
                }
                return rStacks;
            }
        }
        if (aIndex >= mInputs.length) return null;
        return GTUtility.copyOrNull(mInputs[aIndex]);
    }
}
