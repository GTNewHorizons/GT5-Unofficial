package com.gtnewhorizons.gtnhintergalactic.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;

/**
 * GT recipes of GTNH-Intergalactic. These include a needed space project optionally.
 *
 * @author minecraft7771
 */
public class IG_Recipe extends GTRecipe {

    /** Space project that is needed to be constructed to do this recipe */
    protected final String neededSpaceProject;

    /** Space project location that is needed to be constructed to do this recipe */
    protected final String neededSpaceProjectLocation;

    /**
     * Create a new recipe for the Space Research Module
     *
     * @param aOptimize     Flag if the recipe should be optimized
     * @param aInputs       Item inputs of the recipe
     * @param aOutputs      Item outputs of the recipe
     * @param aSpecialItems Special item input that is needed (potentially catalyst)
     * @param aChances      Chances for each item output
     * @param aFluidInputs  Fluid inputs of the recipe
     * @param aFluidOutputs Fluid outputs of the recipe
     * @param aDuration     Duration of the recipe in ticks
     * @param aEUt          EU consumption of the recipe per tick
     * @param aSpecialValue Special value of the recipe
     */
    public IG_Recipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
            FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        this(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecialItems,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue,
                null,
                null);
    }

    /**
     * Create a new recipe for the Space Research Module
     *
     * @param aOptimize          Flag if the recipe should be optimized
     * @param aInputs            Item inputs of the recipe
     * @param aOutputs           Item outputs of the recipe
     * @param aSpecialItems      Special item input that is needed (potentially catalyst)
     * @param aChances           Chances for each item output
     * @param aFluidInputs       Fluid inputs of the recipe
     * @param aFluidOutputs      Fluid outputs of the recipe
     * @param aDuration          Duration of the recipe in ticks
     * @param aEUt               EU consumption of the recipe per tick
     * @param aSpecialValue      Special value of the recipe
     * @param neededSpaceProject Space project that is needed to do this recipe
     */
    public IG_Recipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
            FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue,
            String neededSpaceProject, String neededSpaceProjectLocation) {
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
        this.neededSpaceProject = neededSpaceProject;
        this.neededSpaceProjectLocation = neededSpaceProjectLocation;
    }

    /**
     * @return Space project that is needed to do this recipe
     */
    public String getNeededSpaceProject() {
        return neededSpaceProject;
    }

    /**
     * @return Space project location that is needed to do this recipe
     */
    public String getNeededSpaceProjectLocation() {
        return neededSpaceProjectLocation;
    }

    /**
     * Recipe for Space Mining in the Space Elevator
     *
     * @author minecraft7771
     */
    public static class IG_SpaceMiningRecipe extends IG_Recipe {

        public int minDistance;
        public int maxDistance;
        public int minSize;
        public int maxSize;
        public int computation;
        public int recipeWeight;

        public IG_SpaceMiningRecipe(boolean aOptimize, ItemStack[] aItemInputs, ItemStack[] aItemOutputs,
                FluidStack[] aFluidInputs, int[] aChances, int aDuration, int aEUt, int computation, int minModuleTier,
                int minDistance, int maxDistance, int minSize, int maxSize, int recipeWeight) {
            super(
                    aOptimize,
                    aItemInputs,
                    aItemOutputs,
                    null,
                    aChances,
                    aFluidInputs,
                    null,
                    aDuration,
                    aEUt,
                    minModuleTier);
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
}
