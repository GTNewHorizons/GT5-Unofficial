package gtnhintergalactic.recipe;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.common.RecipeAdder;

/**
 * GT recipe adder of GTNH-Intergalactic
 *
 * @author minecraft7771
 */
public class IG_RecipeAdder extends RecipeAdder {

    public static IG_RecipeAdder instance = new IG_RecipeAdder();

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
    @Deprecated
    public static boolean addSpaceResearchRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack output,
        int computationRequiredPerSec, int duration, int EUt, String neededSpaceProject,
        String neededSpaceProjectLocation) {
        GTRecipeBuilder builder = GTValues.RA.stdBuilder();

        if (aItemInputs != null) {
            builder.itemInputs(aItemInputs);
        }
        if (aFluidInputs != null) {
            builder.fluidInputs(aFluidInputs);
        }

        builder.itemOutputs(output)
            .specialValue(computationRequiredPerSec)
            .duration(duration)
            .eut(EUt);

        if (neededSpaceProject != null && !neededSpaceProject.isEmpty()) {
            builder.metadata(IGRecipeMaps.SPACE_PROJECT, neededSpaceProject);
        }
        if (neededSpaceProjectLocation != null && !neededSpaceProjectLocation.isEmpty()) {
            builder.metadata(IGRecipeMaps.SPACE_LOCATION, neededSpaceProjectLocation);
        }

        builder.addTo(IGRecipeMaps.spaceResearchRecipes);
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
    @Deprecated
    public static boolean addSpaceAssemblerRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack output,
        int requiredModuleTier, int duration, int EUt, String neededSpaceProject, String neededSpaceProjectLocation) {
        GTRecipeBuilder builder = GTValues.RA.stdBuilder();

        if (aItemInputs != null) {
            builder.itemInputs(aItemInputs);
        }
        if (aFluidInputs != null) {
            builder.fluidInputs(aFluidInputs);
        }

        builder.itemOutputs(output)
            .specialValue(requiredModuleTier)
            .duration(duration)
            .eut(EUt);

        if (neededSpaceProject != null && !neededSpaceProject.isEmpty()) {
            builder.metadata(IGRecipeMaps.SPACE_PROJECT, neededSpaceProject);
        }
        if (neededSpaceProjectLocation != null && !neededSpaceProjectLocation.isEmpty()) {
            builder.metadata(IGRecipeMaps.SPACE_LOCATION, neededSpaceProjectLocation);
        }

        builder.addTo(IGRecipeMaps.spaceAssemblerRecipes);
        return true;
    }

    /**
     * Add a Space Mining recipe
     *
     * @param asteroidName              Unlocalized name of the asteroid
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
    public static boolean addSpaceMiningRecipe(String asteroidName, ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
        int[] aChances, Materials[] ores, OrePrefixes orePrefixes, int minSize, int maxSize, int minDistance,
        int maxDistance, int computationRequiredPerSec, int minModuleTier, int duration, int EUt, int recipeWeight) {
        if (ores == null) return false;

        // Map ores to actual items with stack minSize 64
        ItemStack[] outputs = new ItemStack[ores.length];
        for (int i = 0; i < ores.length; i++) {
            outputs[i] = GTOreDictUnificator.get(orePrefixes, ores[i], 64);
        }

        return addSpaceMiningRecipe(
            asteroidName,
            aItemInputs,
            aFluidInputs,
            aChances,
            outputs,
            minSize,
            maxSize,
            minDistance,
            maxDistance,
            computationRequiredPerSec,
            minModuleTier,
            duration,
            EUt,
            recipeWeight);
    }

    public static boolean addSpaceMiningRecipe(String asteroidName, ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
        int[] aChances, ItemStack[] aItemOutputs, int minSize, int maxSize, int minDistance, int maxDistance,
        int computationRequiredPerSec, int minModuleTier, int duration, int EUt, int recipeWeight) {
        if ((aItemInputs == null && aFluidInputs == null) || aItemOutputs == null) {
            return false;
        }
        if (minDistance > maxDistance || minSize > maxSize) {
            return false;
        }
        if (recipeWeight <= 0) {
            GTLog.err
                .println("Weight of mining recipe for main material " + aItemOutputs[0].getUnlocalizedName() + " is 0");
        }
        if (aChances != null) {
            if (aChances.length < aItemOutputs.length) {
                return false;
            } else if (aChances.length > aItemOutputs.length) {
                GTLog.err.println(
                    "Chances and outputs of mining recipe for main material " + aItemOutputs[0].getUnlocalizedName()
                        + " have different length!");
            }
            if (Arrays.stream(aChances)
                .sum() != 10000) {
                GTLog.err.println(
                    "Sum of chances in mining recipe for main material " + aItemOutputs[0].getUnlocalizedName()
                        + " is not 100%! This will lead to no issue but might be unintentional");
            }
        } else {
            aChances = new int[aItemOutputs.length];
            Arrays.fill(aChances, 10000);
        }

        // Create space mining data storage
        SpaceMiningData miningData = new SpaceMiningData(
            asteroidName,
            minDistance,
            maxDistance,
            minSize,
            maxSize,
            computationRequiredPerSec,
            recipeWeight);

        GTRecipeBuilder builder = GTValues.RA.stdBuilder();

        if (aItemInputs != null) {
            builder.itemInputs(aItemInputs);
        }
        if (aFluidInputs != null) {
            builder.fluidInputs(aFluidInputs);
        }

        builder.itemOutputs(aItemOutputs)
            .outputChances(aChances)
            .metadata(IGRecipeMaps.MODULE_TIER, minModuleTier)
            .metadata(IGRecipeMaps.SPACE_MINING_DATA, miningData)
            .duration(duration)
            .eut(EUt)
            .addTo(IGRecipeMaps.spaceMiningRecipes);

        return true;
    }
}
