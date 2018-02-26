package gtPlusPlus.core.handler.Recipes;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.recipe.*;

public class RegistrationHandler {

	public static int recipesSuccess = 0;
	public static int recipesFailed = 0;

	public static void run(){
		init();
	}

	private final static void init(){
		RECIPES_Tools.loadRecipes();
		RECIPES_Machines.loadRecipes();
		RECIPES_Shapeless.loadRecipes();
		RECIPES_MachineComponents.loadRecipes();
		RECIPE_Batteries.loadRecipes();
		RECIPES_General.loadRecipes();
		//RECIPES_MTWRAPPER.run();
		Logger.INFO("Loaded: "+recipesSuccess+" Failed: "+recipesFailed);
		COMPAT_HANDLER.areInitItemsLoaded = true;
		//Utils.LOG_INFO("MT Loaded: "+RECIPES_MTWRAPPER.MT_RECIPES_LOADED+" MT Failed: "+RECIPES_MTWRAPPER.MT_RECIPES_FAILED);
	}

}
