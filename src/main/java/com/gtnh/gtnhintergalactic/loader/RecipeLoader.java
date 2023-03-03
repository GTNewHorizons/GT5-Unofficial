package com.gtnh.gtnhintergalactic.loader;

import com.gtnh.gtnhintergalactic.recipe.GasSiphonRecipes;
import com.gtnh.gtnhintergalactic.recipe.SpaceMiningRecipes;
import com.gtnh.gtnhintergalactic.recipe.SpacePumpingRecipes;

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
        GasSiphonRecipes.addPumpingRecipes();
    }
}
