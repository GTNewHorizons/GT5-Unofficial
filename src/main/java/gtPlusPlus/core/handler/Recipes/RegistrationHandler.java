package gtPlusPlus.core.handler.Recipes;

import gtPlusPlus.core.recipe.RecipesBatteries;
import gtPlusPlus.core.recipe.RecipesGeneral;
import gtPlusPlus.core.recipe.RecipesMachines;

public class RegistrationHandler {

    public static void run() {
        init();
    }

    private static void init() {
        RecipesGeneral.loadRecipes();
        RecipesMachines.loadRecipes();
        RecipesBatteries.loadRecipes();
    }
}
