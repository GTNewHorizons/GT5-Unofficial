package gtPlusPlus.core.util.recipe;

import static gtPlusPlus.core.slots.SlotIntegratedCircuit.isRegularProgrammableCircuit;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipe.GTRecipe_WithAlt;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
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

            // For GTRecipe_WithAlt
            List<ItemStack[]> oreDictList = null;
            IntList oreDictIds = null;
            GTRecipe_WithAlt altRecipe = null;

            if (recipeInput instanceof GTRecipe_WithAlt alt) {
                altRecipe = alt;
                if (altRecipe.mOreDictAlt != null) oreDictList = new ArrayList<>(altRecipe.mOreDictAlt.length);
                if (altRecipe.mOreDictIds != null) oreDictIds = new IntArrayList(altRecipe.mOreDictIds.length);
            }

            for (int i = 0; i < recipeInput.mInputs.length; i++) {
                ItemStack itemStack = recipeInput.mInputs[i];
                if (itemStack == null) {
                    continue;
                }
                if (isRegularProgrammableCircuit(itemStack)) {
                    savedCircuit = itemStack;
                } else {
                    itemInputsWithoutProgrammableCircuit.add(itemStack);
                    if (altRecipe != null) {
                        if (oreDictList != null && i < altRecipe.mOreDictAlt.length)
                            oreDictList.add(altRecipe.mOreDictAlt[i]);
                        if (oreDictIds != null && i < altRecipe.mOreDictIds.length)
                            oreDictIds.add(altRecipe.mOreDictIds[i]);
                    }
                }
            }

            // itemInputsWithoutProgrammableCircuit can have smaller length when a circuit or null value is stripped
            if (itemInputsWithoutProgrammableCircuit.size() != recipeInput.mInputs.length) {
                GTRecipe newRecipe = recipeInput.copyShallow();
                newRecipe.setInputs(itemInputsWithoutProgrammableCircuit.toArray(new ItemStack[0]));

                if (altRecipe != null) {
                    if (oreDictList != null) altRecipe.mOreDictAlt = oreDictList.toArray(new ItemStack[0][]);
                    if (oreDictIds != null) altRecipe.mOreDictIds = oreDictIds.toIntArray();
                }

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
                GTRecipe recipeWithCircuit = filteredRecipe.copyShallow();
                // append the chosen circuit to the end of the inputs
                recipeWithCircuit.setInputs(ArrayUtils.add(filteredRecipe.mInputs, circuit));

                recipeOutput.add(recipeWithCircuit);
            } else {
                recipeOutput.add(filteredRecipe);
            }
        }

        return recipeOutput;
    }
}
