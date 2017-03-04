package gtPlusPlus.core.handler.Recipes;

import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.recipe.ShapedRecipeObject;
import gtPlusPlus.core.util.Utils;

public class LateRegistrationHandler {

	public static int recipesSuccess = 0;
	public static int recipesFailed = 0;

	public static void run(){
		init();
	}

	private final static void init(){
		for(final ShapedRecipeObject item : COMPAT_HANDLER.AddRecipeQueue){
			item.buildRecipe();
		}
		try {
			Thread.sleep(10);
		} catch (final InterruptedException e) {
			Utils.LOG_INFO(e.toString());
		}
		Utils.LOG_INFO("Late Recipes Loaded: "+recipesSuccess+" Failed: "+recipesFailed);
	}

}
