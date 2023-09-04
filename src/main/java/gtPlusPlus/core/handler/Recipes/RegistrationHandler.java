package gtPlusPlus.core.handler.Recipes;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.recipe.RECIPES_General;
import gtPlusPlus.core.recipe.RECIPES_MachineComponents;
import gtPlusPlus.core.recipe.RECIPES_Machines;
import gtPlusPlus.core.recipe.RECIPE_Batteries;

public class RegistrationHandler {

    public static int recipesSuccess = 0;
    public static int recipesFailed = 0;

    public static void run() {
        init();
    }

    private static final void init() {
        RECIPES_General.loadRecipes();
        RECIPES_Machines.loadRecipes();
        RECIPES_MachineComponents.loadRecipes();
        RECIPE_Batteries.loadRecipes();
        Logger.INFO("Loaded: " + recipesSuccess + " Failed: " + recipesFailed);
        COMPAT_HANDLER.areInitItemsLoaded = true;
    }
}
