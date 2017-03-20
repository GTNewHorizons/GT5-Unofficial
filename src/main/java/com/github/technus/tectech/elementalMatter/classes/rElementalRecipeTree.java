package com.github.technus.tectech.elementalMatter.classes;

import java.util.TreeMap;

/**
 * Created by Tec on 02.03.2017.
 */
public class rElementalRecipeTree {
    //Multimap for multiple recipes from the same thing - you know parameters might differ the output
    private TreeMap<cElementalDefinitionStackTree, TreeMap<Short,rElementalRecipe> > recipes = new TreeMap<>();

    public rElementalRecipeTree() {}

    public rElementalRecipe put(rElementalRecipe in) {
        TreeMap<Short, rElementalRecipe> r=recipes.get(in.inEM);
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

    public TreeMap<Short,rElementalRecipe> findExact(cElementalDefinitionStackTree in) {
        return recipes.get(in);
    }

    public TreeMap<Short,rElementalRecipe> findTopMatch(cElementalDefinitionStackTree in) {
        for (cElementalDefinitionStackTree requirement : recipes.descendingKeySet())
            if (in.removeAllAmounts(true, requirement))
                return recipes.get(requirement);
        return null;
    }

    public TreeMap<Short,rElementalRecipe> findTopMatch(cElementalInstanceStackTree in, boolean testOnly) {
        for (cElementalDefinitionStackTree requirement : recipes.descendingKeySet())
            if (in.removeAllAmounts(testOnly, requirement))
                return recipes.get(requirement);
        return null;
    }

    public TreeMap<Short,rElementalRecipe> findBottomMatch(cElementalDefinitionStackTree in) {
        for (cElementalDefinitionStackTree requirement : recipes.keySet())
            if (in.removeAllAmounts(true, requirement))
                return recipes.get(requirement);
        return null;
    }

    public TreeMap<Short,rElementalRecipe> findBottomMatch(cElementalInstanceStackTree in, boolean testOnly) {
        for (cElementalDefinitionStackTree requirement : recipes.keySet())
            if (in.removeAllAmounts(testOnly, requirement))
                return recipes.get(requirement);
        return null;
    }
}
