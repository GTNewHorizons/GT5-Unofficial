package com.gtnewhorizons.gtnhintergalactic.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;

/**
 * Recipe for Space Mining in the Space Elevator
 *
 * @author minecraft7771
 */
public class IG_SpaceMiningRecipe extends GTRecipe {

    public int minDistance;
    public int maxDistance;
    public int minSize;
    public int maxSize;
    public int computation;
    public int recipeWeight;

    public IG_SpaceMiningRecipe(boolean aOptimize, ItemStack[] aItemInputs, ItemStack[] aItemOutputs,
            FluidStack[] aFluidInputs, int[] aChances, int aDuration, int aEUt, int computation, int minModuleTier,
            int minDistance, int maxDistance, int minSize, int maxSize, int recipeWeight) {
        super(aOptimize, aItemInputs, aItemOutputs, null, aChances, aFluidInputs, null, aDuration, aEUt, minModuleTier);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.computation = computation;
        this.recipeWeight = recipeWeight;
    }

    /**
     * Get the weight of the recipe
     *
     * @return Recipe weight
     */
    public int getRecipeWeight() {
        return recipeWeight;
    }
}
