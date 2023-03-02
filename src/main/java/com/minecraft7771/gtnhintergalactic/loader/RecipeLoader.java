package com.minecraft7771.gtnhintergalactic.loader;

import com.minecraft7771.gtnhintergalactic.recipe.SpaceMiningRecipes;
import com.minecraft7771.gtnhintergalactic.recipe.SpacePumpingRecipes;

/**
 * Loader for all recipes
 *
 * @author minecraft7771
 */
public class RecipeLoader implements Runnable {

    /**
     * Execute the recipe loader
     */
    @Override
    public void run() {
        SpaceMiningRecipes.addAsteroids();
        SpacePumpingRecipes.addPumpingRecipes();
    }
}
