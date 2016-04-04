package miscutil.core.handler.registration;

import miscutil.core.util.Utils;

public class RegistrationHandler {

	public static int recipesSuccess = 0;
	public static int recipesFailed = 0;
	
	public static void run(){
	init();	
	}
	
	private final static void init(){
		RECIPES_Tools.RECIPES_LOAD();
		RECIPES_Machines.RECIPES_LOAD();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			Utils.LOG_INFO(e.toString());
		}
		Utils.LOG_INFO("Loaded: "+recipesSuccess+" Failed: "+recipesFailed);
	}
	
}
