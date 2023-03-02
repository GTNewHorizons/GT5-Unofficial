package com.minecraft7771.gtnhintergalactic.recipe;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * GT recipes of GTNH-Intergalactic. These include a needed space project optionally.
 *
 * @author minecraft7771
 */
public class IG_Recipe extends GT_Recipe {

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
     * GT recipes maps of GTNH-Intergalactic
     *
     * @author minecraft7771
     */
    public static class GT_Recipe_MapGS extends GT_Recipe.GT_Recipe_Map {

        public GT_Recipe_MapGS(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                               String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                               int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                               int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
                               boolean aNEIAllowed) {
            super(
                aRecipeList,
                aUnlocalizedName,
                aLocalName,
                aNEIName,
                aNEIGUIPath,
                aUsualInputCount,
                aUsualOutputCount,
                aMinimalInputItems,
                aMinimalInputFluids,
                aAmperage,
                aNEISpecialValuePre,
                aNEISpecialValueMultiplier,
                aNEISpecialValuePost,
                aShowVoltageAmperageInNEI,
                aNEIAllowed);
        }

        public List<IG_SpaceMiningRecipe> findRecipes(IHasWorldObjectAndCoords aTileEntity,
                                                      IG_SpaceMiningRecipe aRecipe, boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage,
                                                      FluidStack[] aFluids, ItemStack aSpecialSlot, int distance, int moduleTier, ItemStack... aInputs) {
            // Search algorithm copied from findRecipe, just fitted to add all found recipes
            if (this.mRecipeList.isEmpty()) {
                return null;
            } else {
                int var12;
                if (GregTech_API.sPostloadFinished) {
                    int tAmount;
                    int var13;
                    if (this.mMinimalInputFluids > 0) {
                        if (aFluids == null) {
                            return null;
                        }

                        tAmount = 0;
                        FluidStack[] var11 = aFluids;
                        var12 = aFluids.length;

                        for (var13 = 0; var13 < var12; ++var13) {
                            FluidStack aFluid = var11[var13];
                            if (aFluid != null) {
                                ++tAmount;
                            }
                        }

                        if (tAmount < this.mMinimalInputFluids) {
                            return null;
                        }
                    }

                    if (this.mMinimalInputItems > 0) {
                        if (aInputs == null) {
                            return null;
                        }

                        tAmount = 0;
                        ItemStack[] var17 = aInputs;
                        var12 = aInputs.length;

                        for (var13 = 0; var13 < var12; ++var13) {
                            ItemStack aInput = var17[var13];
                            if (aInput != null) {
                                ++tAmount;
                            }
                        }

                        if (tAmount < this.mMinimalInputItems) {
                            return null;
                        }
                    }
                }

                List<IG_SpaceMiningRecipe> recipes = new ArrayList<>();
                if (aNotUnificated) {
                    aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
                }

                if (aRecipe != null && !aRecipe.mFakeRecipe
                    && aRecipe.mCanBeBuffered
                    && aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                    if (aRecipe.mEnabled && aVoltage * (long) this.mAmperage >= (long) aRecipe.mEUt
                        && aRecipe.minDistance <= distance
                        && aRecipe.maxDistance >= distance
                        && aRecipe.mSpecialValue <= moduleTier) {
                        recipes.add(aRecipe);
                    }
                } else {
                    Iterator var15;
                    IG_SpaceMiningRecipe tRecipe;
                    int var19;
                    Collection tRecipes;
                    if (this.mUsualInputCount > 0 && aInputs != null) {
                        ItemStack[] var18 = aInputs;
                        var19 = aInputs.length;

                        for (var12 = 0; var12 < var19; ++var12) {
                            ItemStack tStack = var18[var12];
                            if (tStack != null) {
                                tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(tStack));
                                if (tRecipes != null) {
                                    var15 = tRecipes.iterator();

                                    while (var15.hasNext()) {
                                        tRecipe = (IG_SpaceMiningRecipe) var15.next();
                                        if (!tRecipe.mFakeRecipe && tRecipe
                                            .isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                                            if (tRecipe.mEnabled
                                                && aVoltage * (long) this.mAmperage >= (long) tRecipe.mEUt
                                                && tRecipe.minDistance <= distance
                                                && tRecipe.maxDistance >= distance
                                                && tRecipe.mSpecialValue <= moduleTier) {
                                                recipes.add(tRecipe);
                                            }
                                        }
                                    }
                                }

                                tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(tStack, true));
                                if (tRecipes != null) {
                                    var15 = tRecipes.iterator();

                                    while (var15.hasNext()) {
                                        tRecipe = (IG_SpaceMiningRecipe) var15.next();
                                        if (!tRecipe.mFakeRecipe && tRecipe
                                            .isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                                            if (tRecipe.mEnabled
                                                && aVoltage * (long) this.mAmperage >= (long) tRecipe.mEUt
                                                && tRecipe.minDistance <= distance
                                                && tRecipe.maxDistance >= distance
                                                && tRecipe.mSpecialValue <= moduleTier) {
                                                recipes.add(tRecipe);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (this.mMinimalInputItems == 0 && aFluids != null) {
                        FluidStack[] var20 = aFluids;
                        var19 = aFluids.length;

                        for (var12 = 0; var12 < var19; ++var12) {
                            FluidStack aFluid = var20[var12];
                            if (aFluid != null) {
                                tRecipes = this.mRecipeFluidMap.get(aFluid.getFluid());
                                if (tRecipes != null) {
                                    var15 = tRecipes.iterator();

                                    while (var15.hasNext()) {
                                        tRecipe = (IG_SpaceMiningRecipe) var15.next();
                                        if (!tRecipe.mFakeRecipe && tRecipe
                                            .isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                                            if (tRecipe.mEnabled
                                                && aVoltage * (long) this.mAmperage >= (long) tRecipe.mEUt
                                                && tRecipe.minDistance <= distance
                                                && tRecipe.maxDistance >= distance
                                                && tRecipe.mSpecialValue <= moduleTier) {
                                                recipes.add(tRecipe);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return recipes;
            }
        }
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
