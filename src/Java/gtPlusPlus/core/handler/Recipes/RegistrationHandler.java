package gtPlusPlus.core.handler.Recipes;

import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.recipe.*;
import gtPlusPlus.core.util.Utils;

public class RegistrationHandler {

	public static int recipesSuccess = 0;
	public static int recipesFailed = 0;

	public static void run(){
		init();
	}

	private final static void init(){
		RECIPES_Tools.RECIPES_LOAD();
		RECIPES_Machines.RECIPES_LOAD();
		RECIPES_Shapeless.RECIPES_LOAD();
		RECIPES_MachineComponents.RECIPES_LOAD();
		RECIPE_Batteries.RECIPES_LOAD();
		RECIPES_General.RECIPES_LOAD();
		//RECIPES_MTWRAPPER.run();
		Utils.LOG_INFO("Loaded: "+recipesSuccess+" Failed: "+recipesFailed);
		COMPAT_HANDLER.areInitItemsLoaded = true;
		//Utils.LOG_INFO("MT Loaded: "+RECIPES_MTWRAPPER.MT_RECIPES_LOADED+" MT Failed: "+RECIPES_MTWRAPPER.MT_RECIPES_FAILED);
	}

}
