package com.gtnewhorizons.gtnhintergalactic.recipe;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_RecipeAdder;

/**
 * GT recipe adder of GTNH-Intergalactic
 *
 * @author minecraft7771
 */
public class IG_RecipeAdder extends GT_RecipeAdder {

    public static IG_RecipeAdder instance = new IG_RecipeAdder();
    public static final ItemStack[] nullItem = new ItemStack[0];
    public static final FluidStack[] nullFluid = new FluidStack[0];

    public static void init() {

    }

    public static void postInit() {

    }

    /**
     * Add a recipe to the Space Research Module
     *
     * @param aItemInputs                Needed item inputs
     * @param aFluidInputs               Needed fluid inputs
     * @param output                     Item output
     * @param computationRequiredPerSec  Required computation
     * @param duration                   Duration of the recipe
     * @param EUt                        Consumed EU per tick
     * @param neededSpaceProject         Name of the needed space project
     * @param neededSpaceProjectLocation Location of the needed space project or null if any
     * @return True if recipe was added, else false
     */
    public static boolean addSpaceResearchRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack output,
            int computationRequiredPerSec, int duration, int EUt, String neededSpaceProject,
            String neededSpaceProjectLocation) {
        if (aItemInputs == null) {
            aItemInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }

        IGRecipeMaps.spaceResearchRecipes.add(
                new IG_Recipe(
                        false,
                        aItemInputs,
                        new ItemStack[] { output },
                        null,
                        null,
                        aFluidInputs,
                        null,
                        duration,
                        EUt,
                        computationRequiredPerSec,
                        neededSpaceProject,
                        neededSpaceProjectLocation));
        return true;
    }

    /**
     * Add a recipe to the Space Research Module
     *
     * @param aItemInputs                Needed item inputs
     * @param aFluidInputs               Needed fluid inputs
     * @param output                     Item output
     * @param requiredModuleTier         Required minimum module tier
     * @param duration                   Duration of the recipe
     * @param EUt                        Consumed EU per tick
     * @param neededSpaceProject         Name of the needed space project
     * @param neededSpaceProjectLocation Location of the needed space project or null if any
     * @return True if recipe was added, else false
     */
    public static boolean addSpaceAssemblerRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack output,
            int requiredModuleTier, int duration, int EUt, String neededSpaceProject,
            String neededSpaceProjectLocation) {
        if (aItemInputs == null) {
            aItemInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }

        IGRecipeMaps.spaceAssemblerRecipes.add(
                new IG_Recipe(
                        false,
                        aItemInputs,
                        new ItemStack[] { output },
                        null,
                        null,
                        aFluidInputs,
                        null,
                        duration,
                        EUt,
                        requiredModuleTier,
                        neededSpaceProject,
                        neededSpaceProjectLocation));
        return true;
    }

    /**
     * Add a Space Mining recipe
     *
     * @param aItemInputs               Equipment used for the mining operation
     * @param aFluidInputs              Additional input fluids
     * @param aChances                  Chances to get each ore type
     * @param ores                      Ores that should spawn in this asteroid
     * @param minSize                   Minimum size of the asteroid in stacks
     * @param maxSize                   Maximum size of the asteroid in stacks
     * @param minDistance               Minimal distance in which you will find the asteroid
     * @param maxDistance               Maximal distance in which you will find the asteroid
     * @param computationRequiredPerSec Required computation for the mining operation
     * @param minModuleTier             Minimum module tier that is required to mine this asteroid
     * @param duration                  Duration of the mining operation
     * @param EUt                       Used energy for the mining operation per tick
     * @param recipeWeight              Weight of this recipe (Used in determining which recipe to execute)
     * @return True if recipes could be added, else false
     */
    public static boolean addSpaceMiningRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int[] aChances,
            Materials[] ores, OrePrefixes orePrefixes, int minSize, int maxSize, int minDistance, int maxDistance,
            int computationRequiredPerSec, int minModuleTier, int duration, int EUt, int recipeWeight) {
        if ((aItemInputs == null && aFluidInputs == null) || ores == null) {
            return false;
        }
        if (minDistance > maxDistance || minSize > maxSize) {
            return false;
        }
        if (recipeWeight <= 0) {
            GT_Log.err.println("Weight of mining recipe for main material " + ores[0].toString() + " is 0");
        }
        if (aChances != null) {
            if (aChances.length < ores.length) {
                return false;
            } else if (aChances.length > ores.length) {
                GT_Log.err.println(
                        "Chances and outputs of mining recipe for main material " + ores[0].toString()
                                + " have different length!");
            }
            if (Arrays.stream(aChances).sum() != 10000) {
                GT_Log.err.println(
                        "Sum of chances in mining recipe for main material " + ores[0].toString()
                                + " is not 100%! This will lead to no issue but might be unintentional");
            }
        } else {
            aChances = new int[ores.length];
            Arrays.fill(aChances, 10000);
        }
        if (aItemInputs == null) {
            aItemInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (orePrefixes == null) {
            return false;
        }

        // Map ores to actual items with stack minSize 64
        ItemStack[] outputs = new ItemStack[ores.length];
        for (int i = 0; i < ores.length; i++) {
            outputs[i] = GT_OreDictUnificator.get(orePrefixes, ores[i], 64);
        }

        IGRecipeMaps.spaceMiningRecipes.add(
                new IG_Recipe.IG_SpaceMiningRecipe(
                        false,
                        aItemInputs,
                        outputs,
                        aFluidInputs,
                        aChances,
                        duration,
                        EUt,
                        computationRequiredPerSec,
                        minModuleTier,
                        minDistance,
                        maxDistance,
                        minSize,
                        maxSize,
                        recipeWeight));
        return true;
    }

    public static boolean addSpaceMiningRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int[] aChances,
            ItemStack[] aItemOutputs, int minSize, int maxSize, int minDistance, int maxDistance,
            int computationRequiredPerSec, int minModuleTier, int duration, int EUt, int recipeWeight) {
        if ((aItemInputs == null && aFluidInputs == null) || aItemOutputs == null) {
            return false;
        }
        if (minDistance > maxDistance || minSize > maxSize) {
            return false;
        }
        if (recipeWeight <= 0) {
            GT_Log.err.println(
                    "Weight of mining recipe for main material " + aItemOutputs[0].getUnlocalizedName() + " is 0");
        }
        if (aChances != null) {
            if (aChances.length < aItemOutputs.length) {
                return false;
            } else if (aChances.length > aItemOutputs.length) {
                GT_Log.err.println(
                        "Chances and outputs of mining recipe for main material " + aItemOutputs[0].getUnlocalizedName()
                                + " have different length!");
            }
            if (Arrays.stream(aChances).sum() != 10000) {
                GT_Log.err.println(
                        "Sum of chances in mining recipe for main material " + aItemOutputs[0].getUnlocalizedName()
                                + " is not 100%! This will lead to no issue but might be unintentional");
            }
        } else {
            aChances = new int[aItemOutputs.length];
            Arrays.fill(aChances, 10000);
        }
        if (aItemInputs == null) {
            aItemInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }

        IGRecipeMaps.spaceMiningRecipes.add(
                new IG_Recipe.IG_SpaceMiningRecipe(
                        false,
                        aItemInputs,
                        aItemOutputs,
                        aFluidInputs,
                        aChances,
                        duration,
                        EUt,
                        computationRequiredPerSec,
                        minModuleTier,
                        minDistance,
                        maxDistance,
                        minSize,
                        maxSize,
                        recipeWeight));

        return true;
    }
}
