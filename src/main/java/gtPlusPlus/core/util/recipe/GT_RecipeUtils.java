package gtPlusPlus.core.util.recipe;

import static gtPlusPlus.core.slots.SlotIntegratedCircuit.isRegularProgrammableCircuit;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.set.hash.TCustomHashSet;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;

public class GT_RecipeUtils {

    public static List<GT_Recipe> removeDuplicates(List<GT_Recipe> inputRecipes, String recipeMapName) {
        TCustomHashSet<GT_Recipe> recipesHashSet = new TCustomHashSet<>(RecipeHashStrat.RecipeHashingStrategy);
        ArrayList<GT_Recipe> recipeOutput = new ArrayList<>();
        TCustomHashMap<GT_Recipe, ItemStack> circuitMap = new TCustomHashMap<>(RecipeHashStrat.RecipeHashingStrategy);
        int removedRecipeCount = 0;

        for (GT_Recipe recipeInput : inputRecipes) {
            ItemStack savedCircuit = null;
            // create a new input ItemStack array that does not contain programmable circuits if they were in the recipe
            ArrayList<ItemStack> itemInputsWithoutProgrammableCircuit = new ArrayList<>();
            // iterate over the recipe input items and add them all to a new array without any programmable circuits
            for (ItemStack itemStack : recipeInput.mInputs) {
                if (itemStack == null) {
                    continue;
                }
                if (isRegularProgrammableCircuit(itemStack) == -1) {
                    itemInputsWithoutProgrammableCircuit.add(itemStack);
                } else {
                    savedCircuit = itemStack;
                }
            }
            GT_Recipe newRecipe = new GT_Recipe(
                    false,
                    itemInputsWithoutProgrammableCircuit.toArray(new ItemStack[0]),
                    recipeInput.mOutputs,
                    recipeInput.mSpecialItems,
                    recipeInput.mChances,
                    recipeInput.mFluidInputs,
                    recipeInput.mFluidOutputs,
                    recipeInput.mDuration,
                    recipeInput.mEUt,
                    recipeInput.mSpecialValue);
            if (!recipesHashSet.contains(newRecipe)) {
                // if the recipes customHashSet does not contain the new recipe then add it
                recipesHashSet.add(newRecipe);
            } else {
                removedRecipeCount++;
            }
            if (savedCircuit != null) {
                // if the current recipe has a circuit and the recipe (without circuits) is already in the
                // circuit map then check make sure the circuit map saves the recipe with the smallest circuit
                // damage value. This is to prevent a case where recipe load order would affect which duplicate
                // recipes with multiple circuit values gets removed.
                if (circuitMap.containsKey(newRecipe)) {
                    if (circuitMap.get(newRecipe).getItemDamage() > savedCircuit.getItemDamage()) {
                        circuitMap.put(newRecipe, savedCircuit);
                    }
                } else {
                    // If the circuit map does not have the recipe in it yet then add it
                    circuitMap.put(newRecipe, savedCircuit);
                }
            }
        }
        // iterate over all recipes without duplicates and add them to the output. If the recipe had a programmable
        // circuit in it then add it back with its damage value coming from the circuit map.
        for (GT_Recipe filteredRecipe : recipesHashSet) {
            // check to see if the recipe is in the circuit map
            if (circuitMap.contains(filteredRecipe)) {
                // add the circuit back
                // update the item input array with the new input from
                // ItemInputsWithoutProgrammableCircuit + circuit map circuit
                filteredRecipe.mInputs = ArrayUtils.add(filteredRecipe.mInputs, circuitMap.get(filteredRecipe));
            }
            // if the recipe was not in the circuit map then just add it the output as no updates to the item input
            // needs to be made
            recipeOutput.add(filteredRecipe);
        }
        // print results to log
        Logger.INFO(
                "Recipe Array duplication removal process completed for '" + recipeMapName
                        + "': '"
                        + removedRecipeCount
                        + "' removed.");
        return recipeOutput;
    }
}
