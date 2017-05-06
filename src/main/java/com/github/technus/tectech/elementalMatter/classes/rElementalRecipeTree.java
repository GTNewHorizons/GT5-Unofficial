package com.github.technus.tectech.elementalMatter.classes;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tec on 02.03.2017.
 */
public class rElementalRecipeTree {
    //Multimap for multiple recipes from the same thing - you know parameters might differ the output
    private TreeMap<cElementalDefinitionStackMap, Map<Short,rElementalRecipe> > recipes = new TreeMap<>();

    public rElementalRecipeTree() {}

    public rElementalRecipe put(rElementalRecipe in) {
        Map<Short, rElementalRecipe> r=recipes.get(in.inEM);
        if(r==null){
            r=new TreeMap<>();
            recipes.put(in.inEM,r);
        }
        return r.put(in.comparableID, in);
    }

    public void putAll(rElementalRecipe... contents) {
        for (rElementalRecipe recipe : contents)
            put(recipe);
    }

    public Map<Short,rElementalRecipe> findExact(cElementalDefinitionStackMap in) {
        return recipes.get(in);
    }

    public Map<Short,rElementalRecipe> findTopMatch(cElementalDefinitionStackMap in) {
        for (cElementalDefinitionStackMap requirement : recipes.descendingKeySet())
            if (in.removeAllAmounts(true, requirement))
                return recipes.get(requirement);
        return null;
    }

    public Map<Short,rElementalRecipe> findTopMatch(cElementalInstanceStackMap in, boolean testOnly) {
        for (cElementalDefinitionStackMap requirement : recipes.descendingKeySet())
            if (in.removeAllAmounts(testOnly, requirement))
                return recipes.get(requirement);
        return null;
    }

    public Map<Short,rElementalRecipe> findBottomMatch(cElementalDefinitionStackMap in) {
        for (cElementalDefinitionStackMap requirement : recipes.keySet())
            if (in.removeAllAmounts(true, requirement))
                return recipes.get(requirement);
        return null;
    }

    public Map<Short,rElementalRecipe> findBottomMatch(cElementalInstanceStackMap in, boolean testOnly) {
        for (cElementalDefinitionStackMap requirement : recipes.keySet())
            if (in.removeAllAmounts(testOnly, requirement))
                return recipes.get(requirement);
        return null;
    }
}
