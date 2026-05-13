package gtPlusPlus.core.util.recipe;

import static gtPlusPlus.core.slots.SlotIntegratedCircuit.isRegularProgrammableCircuit;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.util.GTRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;

public class GTRecipeUtils {

    public static List<GTRecipe> removeDuplicates(List<GTRecipe> inputRecipes) {
        ArrayList<GTRecipe> recipeOutput = new ArrayList<>();
        ObjectOpenCustomHashSet<GTRecipe> recipesHashSet = new ObjectOpenCustomHashSet<>(
            RecipeHashStrat.RecipeHashingStrategy);
        Object2ObjectOpenCustomHashMap<GTRecipe, ItemStack> circuitMap = new Object2ObjectOpenCustomHashMap<>(
            RecipeHashStrat.RecipeHashingStrategy);

        for (GTRecipe recipeInput : inputRecipes) {
            ItemStack savedCircuit = null;
            ArrayList<ItemStack> itemInputsWithoutProgrammableCircuit = new ArrayList<>();

            for (ItemStack itemStack : recipeInput.mInputs) {
                if (itemStack == null) {
                    continue;
                }
                if (isRegularProgrammableCircuit(itemStack)) {
                    savedCircuit = itemStack;
                } else {
                    itemInputsWithoutProgrammableCircuit.add(itemStack);
                }
            }

            // itemInputsWithoutProgrammableCircuit can have smaller length when a circuit or null value is stripped
            if (itemInputsWithoutProgrammableCircuit.size() != recipeInput.mInputs.length) {
                GTRecipe newRecipe = new GTRecipe(
                    itemInputsWithoutProgrammableCircuit.toArray(new ItemStack[0]),
                    recipeInput.mOutputs,
                    recipeInput.mSpecialItems,
                    recipeInput.mInputChances,
                    recipeInput.mOutputChances,
                    recipeInput.mFluidInputChances,
                    recipeInput.mFluidOutputChances,
                    recipeInput.mFluidInputs,
                    recipeInput.mFluidOutputs,
                    recipeInput.mDuration,
                    recipeInput.mEUt,
                    recipeInput.mSpecialValue);

                recipesHashSet.add(newRecipe);

                // if the current recipe has a circuit and the recipe (without circuits) is already in the
                // circuit map then check make sure the circuit map saves the recipe with the smallest circuit
                // damage value. This is to prevent a case where recipe load order would affect which duplicate
                // recipes with multiple circuit values gets removed.
                if (savedCircuit != null) {
                    ItemStack prevCircuit = circuitMap.get(newRecipe);
                    if (prevCircuit == null || prevCircuit.getItemDamage() > savedCircuit.getItemDamage()) {
                        circuitMap.put(newRecipe, savedCircuit);
                    }
                }
            } else {
                recipesHashSet.add(recipeInput);
            }
        }

        for (GTRecipe filteredRecipe : recipesHashSet) {
            ItemStack circuit = circuitMap.get(filteredRecipe);

            if (circuit != null) {
                GTRecipe recipeWithCircuit = new GTRecipe(
                    // append the chosen circuit to the end of the inputs
                    ArrayUtils.add(filteredRecipe.mInputs, circuit),
                    filteredRecipe.mOutputs,
                    filteredRecipe.mSpecialItems,
                    filteredRecipe.mInputChances,
                    filteredRecipe.mOutputChances,
                    filteredRecipe.mFluidInputChances,
                    filteredRecipe.mFluidOutputChances,
                    filteredRecipe.mFluidInputs,
                    filteredRecipe.mFluidOutputs,
                    filteredRecipe.mDuration,
                    filteredRecipe.mEUt,
                    filteredRecipe.mSpecialValue);

                recipeOutput.add(recipeWithCircuit);
            } else {
                recipeOutput.add(filteredRecipe);
            }
        }

        return recipeOutput;
    }
}
