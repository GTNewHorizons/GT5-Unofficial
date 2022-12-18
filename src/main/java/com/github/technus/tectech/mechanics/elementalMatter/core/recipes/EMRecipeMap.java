package com.github.technus.tectech.mechanics.elementalMatter.core.recipes;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.IEMMapRead;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tec on 02.03.2017.
 */
public class EMRecipeMap<T> { // TODO FIX
    // Multimap for multiple recipes from the same thing - you know parameters might differ the output
    private final Map<EMConstantStackMap, Map<Integer, EMRecipe<T>>> recipes;

    public EMRecipeMap() {
        recipes = new HashMap<>();
    }

    public EMRecipe<T> put(EMRecipe<T> in) {
        Map<Integer, EMRecipe<T>> r = getRecipes().computeIfAbsent(in.getInEM(), k -> new HashMap<>());
        return r.put(
                in.getID(),
                in); // IF THIS RETURN SHIT, it means that inputs are using the exact same types of matter as input -
        // (non amount wise collision)
        // It is either bad, or unimportant if you use different id's
    }

    public void putAll(EMRecipe<T>... contents) {
        for (EMRecipe<T> recipe : contents) {
            put(recipe);
        }
    }

    public EMRecipe<T> remove(IEMMapRead<EMDefinitionStack> map, int id) {
        Map<Integer, EMRecipe<T>> recipesMap = getRecipes().get(map);
        return recipesMap != null
                ? recipesMap.remove(id)
                : null; // todo check, suspicious but ok, equals and hashcode methods are adjusted for that
    }

    public Map<Integer, EMRecipe<T>> remove(IEMMapRead<EMDefinitionStack> map) {
        return getRecipes()
                .remove(map); // todo check, suspicious but ok, equals and hashcode methods are adjusted for that
    }

    // Recipe founding should not check amounts - this checks if the types of matter in map are equal to any recipe!
    // Return a recipeShortMap when the content of input is equal (ignoring amounts and instance data)
    @Deprecated
    public Map<Integer, EMRecipe<T>> findExact(IEMMapRead<? extends IEMStack> in) {
        return getRecipes().get(in); // suspicious but ok, equals and hashcode methods are adjusted for that
    }

    // this does check if the map contains all the requirements for any recipe, and the required amounts
    // Return a recipeShortMap when the content of input matches the recipe input - does not ignore amounts but ignores
    // instance data!
    public Map<Integer, EMRecipe<T>> findMatch(IEMMapRead<? extends IEMStack> in) {
        for (Map.Entry<EMConstantStackMap, Map<Integer, EMRecipe<T>>> cElementalDefinitionStackMapHashMapEntry :
                getRecipes().entrySet()) {
            if (in.containsAllAmounts(cElementalDefinitionStackMapHashMapEntry.getKey())) {
                return cElementalDefinitionStackMapHashMapEntry.getValue();
            }
        }
        return null;
    }

    public Map<EMConstantStackMap, Map<Integer, EMRecipe<T>>> getRecipes() {
        return recipes;
    }

    // To check for instance data and other things use recipe extensions!
}
