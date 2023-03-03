package com.gtnewhorizons.gtnhintergalactic.loader;

import com.gtnewhorizons.gtnhintergalactic.recipe.GasSiphonRecipes;
import com.gtnewhorizons.gtnhintergalactic.recipe.SpaceMiningRecipes;
import com.gtnewhorizons.gtnhintergalactic.recipe.SpacePumpingRecipes;

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
