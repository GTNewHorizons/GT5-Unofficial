package com.gtnewhorizon.gtnhintergalactic.loader;

import com.gtnewhorizon.gtnhintergalactic.recipe.GasSiphonRecipes;
import com.gtnewhorizon.gtnhintergalactic.recipe.SpaceMiningRecipes;
import com.gtnewhorizon.gtnhintergalactic.recipe.SpacePumpingRecipes;

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
