package com.github.technus.tectech.elementalMatter.classes;

import java.util.TreeMap;

/**
 * Created by Tec on 02.03.2017.
 */
public class rElementalRecipeTree {
    private TreeMap<cElementalDefinitionStackTree,rElementalRecipe> recipes=new TreeMap<>();

    public rElementalRecipeTree(rElementalRecipe... contents){
        for(rElementalRecipe recipe:contents)
            recipes.put(recipe.inEM,recipe);
    }

    public rElementalRecipe put(rElementalRecipe in){
        return recipes.put(in.inEM,in);
    }

    public void putAll(rElementalRecipe... contents){
        for(rElementalRecipe recipe:contents)
            recipes.put(recipe.inEM,recipe);
    }

    public rElementalRecipe findExact(cElementalDefinitionStackTree in){
        return recipes.get(in);
    }

    public rElementalRecipe findTopMatch(cElementalDefinitionStackTree in){
        for (cElementalDefinitionStackTree requirement:recipes.descendingKeySet()){
            if(in.removeAllAmounts(true,requirement))
                return recipes.get(requirement);
        }
        return null;
    }

    public rElementalRecipe findTopMatch(cElementalInstanceStackTree in, boolean testOnly){
        for (cElementalDefinitionStackTree requirement:recipes.descendingKeySet()){
            if(in.removeAllAmounts(testOnly,requirement))
                return recipes.get(requirement);
        }
        return null;
    }

    public rElementalRecipe findBottomMatch(cElementalDefinitionStackTree in){
        for (cElementalDefinitionStackTree requirement:recipes.keySet()){
            if(in.removeAllAmounts(true,requirement)){
                return recipes.get(requirement);
            }
        }
        return null;
    }

    public rElementalRecipe findBottomMatch(cElementalInstanceStackTree in, boolean testOnly){
        for (cElementalDefinitionStackTree requirement:recipes.keySet()){
            if(in.removeAllAmounts(testOnly,requirement)){
                return recipes.get(requirement);
            }
        }
        return null;
    }
}
