package miscutil.core.handler.registration;

import miscutil.core.handler.COMPAT_HANDLER;
import miscutil.core.util.Utils;
import miscutil.core.util.recipe.ShapedRecipeObject;

public class LateRegistrationHandler {

	public static int recipesSuccess = 0;
	public static int recipesFailed = 0;
	
	public static void run(){
	init();	
	}
	
	private final static void init(){
		for(ShapedRecipeObject item : COMPAT_HANDLER.AddRecipeQueue){
					item.buildRecipe();
				}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			Utils.LOG_INFO(e.toString());
		}
		Utils.LOG_INFO("Loaded: "+recipesSuccess+" Failed: "+recipesFailed);
	}
	
}
