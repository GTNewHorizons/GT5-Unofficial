package com.github.technus.tectech.elementalMatter.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
        for (rElementalRecipe recipe : contents)
            put(recipe);
    }

    public rElementalRecipe remove(cElementalInstanceStackMap map, short id) {
        return recipes.get(map).remove(id);
    }

    public HashMap<Short, rElementalRecipe> remove(cElementalInstanceStackMap map) {
        return recipes.remove(map);
    }

    //Recipe founding should not check amounts

    public HashMap<Short, rElementalRecipe> findExact(cElementalStackMap in) {
        return recipes.get(in);//suspicious but ok
    }

    public HashMap<Short, rElementalRecipe> findExact(cElementalInstanceStackMap in) {
        return recipes.get(in);//suspicious but ok
    }

    public HashMap<Short, rElementalRecipe> findMatch(cElementalMutableDefinitionStackMap in) {
        for (cElementalDefinitionStackMap requirement : recipes.keySet())
            if (in.removeAllAmounts(true, requirement))
                return recipes.get(requirement);
        return null;
    }

    public HashMap<Short, rElementalRecipe> findMatch(cElementalInstanceStackMap in, boolean testOnly) {
        for (cElementalDefinitionStackMap requirement : recipes.keySet())
            if (in.removeAllAmounts(testOnly, requirement))
                return recipes.get(requirement);
        return null;
    }
}
