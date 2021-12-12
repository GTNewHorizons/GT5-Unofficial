package gtPlusPlus.core.handler.Recipes;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.recipe.ShapedRecipeObject;

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
			Logger.INFO(e.toString());
		}
		Logger.INFO("Late Recipes Loaded: "+recipesSuccess+" Failed: "+recipesFailed);
	}

}
