package com.github.technus.tectech.mechanics.elementalMatter.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tec on 02.03.2017.
 */
public class rElementalRecipeMap {//TODO FIX
    //Multimap for multiple recipes from the same thing - you know parameters might differ the output
    private final HashMap<cElementalDefinitionStackMap, HashMap<Short, rElementalRecipe>> recipes;

    public rElementalRecipeMap() {
        recipes = new HashMap<>();
    }

    public rElementalRecipe put(rElementalRecipe in) {
        HashMap<Short, rElementalRecipe> r = recipes.get(in.inEM);
        if (r == null) {
            r = new HashMap<>();
            recipes.put(in.inEM, r);
        }
        return r.put(in.ID, in);//IF THIS RETURN SHIT, it means that inputs are using the exact same types of matter as input - (non amount wise collision)
        //It is either bad, or unimportant if you use different id's
    }

    public void putAll(rElementalRecipe... contents) {
        for (rElementalRecipe recipe : contents) {
            put(recipe);
        }
    }

    public rElementalRecipe remove(cElementalStackMap map, short id) {
        return recipes.get(map).remove(id);//suspicious but ok, equals and hashcode methods are adjusted for that
    }

    public HashMap<Short, rElementalRecipe> remove(cElementalStackMap map) {
        return recipes.remove(map);//suspicious but ok, equals and hashcode methods are adjusted for that
    }

    public HashMap<Short, rElementalRecipe> findExact(cElementalInstanceStackMap in) {
        return recipes.get(in.toDefinitionMapForComparison());//suspicious but ok, equals and hashcode methods are adjusted for that
    }

    //Recipe founding should not check amounts - this checks if the types of matter in map are equal to any recipe!
    //Return a recipeShortMap when the content of input is equal (ignoring amounts and instance data)
    @Deprecated
    public HashMap<Short, rElementalRecipe> findExact(cElementalStackMap in) {
        return recipes.get(in);//suspicious but ok, equals and hashcode methods are adjusted for that
    }

    //this does check if the map contains all the requirements for any recipe, and the required amounts
    //Return a recipeShortMap when the content of input matches the recipe input - does not ignore amounts but ignores instance data!
    @Deprecated
    public HashMap<Short, rElementalRecipe> findMatch(cElementalMutableDefinitionStackMap in, boolean testOnlyTruePreferred) {
        for (Map.Entry<cElementalDefinitionStackMap, HashMap<Short, rElementalRecipe>> cElementalDefinitionStackMapHashMapEntry : recipes.entrySet()) {
            if (in.removeAllAmounts(testOnlyTruePreferred, cElementalDefinitionStackMapHashMapEntry.getKey())) {
                return cElementalDefinitionStackMapHashMapEntry.getValue();
            }
        }
        return null;
    }

    public HashMap<Short, rElementalRecipe> findMatch(cElementalInstanceStackMap in, boolean testOnly) {
        for (Map.Entry<cElementalDefinitionStackMap, HashMap<Short, rElementalRecipe>> cElementalDefinitionStackMapHashMapEntry : recipes.entrySet()) {
            if (in.removeAllAmounts(testOnly, cElementalDefinitionStackMapHashMapEntry.getKey())) {
                return cElementalDefinitionStackMapHashMapEntry.getValue();
            }
        }
        return null;
    }

    //To check for instance data and other things use recipe extensions!
}
