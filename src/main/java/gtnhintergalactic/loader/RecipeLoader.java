package gtnhintergalactic.loader;

import gtnhintergalactic.recipe.GasSiphonRecipes;
import gtnhintergalactic.recipe.SpaceMiningRecipes;
import gtnhintergalactic.recipe.SpacePumpingRecipes;

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
