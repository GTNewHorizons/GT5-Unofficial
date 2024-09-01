package gtPlusPlus.core.handler.Recipes;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.handler.CompatHandler;
import gtPlusPlus.core.recipe.RecipesBatteries;
import gtPlusPlus.core.recipe.RecipesGeneral;
import gtPlusPlus.core.recipe.RecipesMachines;

public class RegistrationHandler {

    public static int recipesSuccess = 0;
    public static int recipesFailed = 0;

    public static void run() {
        init();
    }

    private static void init() {
        RecipesGeneral.loadRecipes();
        RecipesMachines.loadRecipes();
        RecipesBatteries.loadRecipes();
        Logger.INFO("Loaded: " + recipesSuccess + " Failed: " + recipesFailed);
        CompatHandler.areInitItemsLoaded = true;
    }
}
