package com.gtnewhorizons.gtnhintergalactic.recipe;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

/**
 * Recipe for Space Mining in the Space Elevator
 *
 * @author minecraft7771
 */
public class IG_SpaceMiningRecipe extends GTRecipe {

    public String asteroidName;
    public int minDistance;
    public int maxDistance;
    public int minSize;
    public int maxSize;
    public int computation;
    public int recipeWeight;

    public IG_SpaceMiningRecipe(boolean aOptimize, String asteroidName, ItemStack[] aItemInputs,
            ItemStack[] aItemOutputs, FluidStack[] aFluidInputs, int[] aChances, int aDuration, int aEUt,
            int computation, int minModuleTier, int minDistance, int maxDistance, int minSize, int maxSize,
            int recipeWeight) {
        super(aOptimize, aItemInputs, aItemOutputs, null, aChances, aFluidInputs, null, aDuration, aEUt, minModuleTier);
        this.asteroidName = asteroidName;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.computation = computation;
        this.recipeWeight = recipeWeight;
    }

    /**
     * Get the non localized name of the asteroid, can be used in nei and gui
     * 
     * @return Asteroid Name
     */
    public String getAsteroidName() {
        return asteroidName;
    }

    /**
     * Get the weight of the recipe
     *
     * @return Recipe weight
     */
    public int getRecipeWeight() {
        return recipeWeight;
    }

    public int hashCode() {
        int res = 0;
        res = 31 * res + minDistance;
        res = 31 * res + maxDistance;
        res = 31 * res + minSize;
        res = 31 * res + maxSize;
        res = 31 * res + computation;
        res = 31 * res + recipeWeight;
        res = 31 * res + mSpecialValue;
        res = 31 * res + mDuration;
        res = 31 * res + mEUt;
        res = 31 * res + GTUtility.ItemId.createWithoutNBT(mInputs[0]).hashCode();
        // We don't care about the order of the output items, so we compute the first five sums
        // of powers of the hashes of the items. This is obviously order invariant, but highly sensitive
        // to changes of item hashes, which is what we want. Five is more than we need but whatever
        int[] moments = Arrays.stream(mOutputs).reduce(new int[5], (a, item) -> {
            int ph = item == null ? 7 : GTUtility.ItemId.createNoCopy(item).hashCode();
            int x = ph;
            for (int i = 0; i < a.length; ++i) {
                a[i] += x;
                x *= ph;
            }
            return a;
        }, (a, b) -> {
            for (int i = 0; i < a.length; ++i) {
                a[i] += b[i];
            }
            return a;
        });
        for (int ph : moments) {
            res = 31 * res + ph;
        }
        return res;
    }

    public boolean equals(Object _other) {
        if (!(_other instanceof IG_SpaceMiningRecipe)) {
            return false;
        }
        IG_SpaceMiningRecipe other = (IG_SpaceMiningRecipe) _other;
        if (minDistance != other.minDistance || maxDistance != other.maxDistance
                || minSize != other.minSize
                || maxSize != other.maxSize
                || computation != other.computation
                || recipeWeight != other.recipeWeight
                || mSpecialValue != other.mSpecialValue
                || mEUt != other.mEUt) {
            return false;
        }
        Collector<ItemStack, ?, Map<GTUtility.ItemId, Long>> collector = Collectors
                .toMap(GTUtility.ItemId::createNoCopy, input -> (long) input.stackSize, (a, b) -> a + b);
        if (!Arrays.stream(mInputs).filter(Objects::nonNull).collect(collector)
                .equals(Arrays.stream(other.mInputs).filter(Objects::nonNull).collect(collector))) {
            return false;
        }
        return Arrays.stream(mOutputs).filter(Objects::nonNull).collect(collector)
                .equals(Arrays.stream(other.mOutputs).filter(Objects::nonNull).collect(collector));
    }
}
