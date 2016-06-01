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
		RECIPES_Shapeless.RECIPES_LOAD();
		//RECIPES_MTWRAPPER.run();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			Utils.LOG_INFO(e.toString());
		}
		Utils.LOG_INFO("Loaded: "+recipesSuccess+" Failed: "+recipesFailed);
		Utils.LOG_INFO("MT Loaded: "+RECIPES_MTWRAPPER.MT_RECIPES_LOADED+" MT Failed: "+RECIPES_MTWRAPPER.MT_RECIPES_FAILED);
	}
	
}
