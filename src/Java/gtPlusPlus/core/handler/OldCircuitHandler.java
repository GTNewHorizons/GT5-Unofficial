package gtPlusPlus.core.handler;

import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableOldGTcircuits;

import java.lang.reflect.Field;
import java.util.HashSet;

import org.apache.commons.lang3.reflect.FieldUtils;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.EmptyRecipeMap;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class OldCircuitHandler {

	public static void preInit(){
		if (enableOldGTcircuits && CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			removeCircuitRecipeMap(); //Bye shitty recipes.			
		}	
	}
	
	public static void init(){
		
	}
	
	public static void postInit(){
		
	}

	private static boolean removeCircuitRecipeMap(){
		try {			
			Utils.LOG_INFO("[Old Feature - Circuits] Trying to override the Circuit Assembler Recipe map, so that no recipes for new circuits get added.");
			ReflectionUtils.setFinalStatic(GT_Recipe_Map.class.getDeclaredField("sCircuitAssemblerRecipes"), new EmptyRecipeMap(new HashSet<GT_Recipe>(0), "gt.recipe.removed", "Removed", null, GT_Values.RES_PATH_GUI + "basicmachines/Default", 0, 0, 0, 0, 0, GT_Values.E, 0, GT_Values.E, true, false));		
			Field jaffar = GT_Recipe_Map.class.getDeclaredField("sCircuitAssemblerRecipes");
			FieldUtils.removeFinalModifier(jaffar, true);
			jaffar.set(null, new EmptyRecipeMap(new HashSet<GT_Recipe>(0), "gt.recipe.removed", "Removed", null, GT_Values.RES_PATH_GUI + "basicmachines/Default", 0, 0, 0, 0, 0, GT_Values.E, 0, GT_Values.E, true, false));
			Utils.LOG_INFO("[Old Feature - Circuits] Successfully replaced circuit assembler recipe map with one that cannot hold recipes.");
		}
		catch (Exception e) {
			Utils.LOG_INFO("[Old Feature - Circuits] Failed removing circuit assembler recipe map.");
			return false;
		}
		return true;
	}
	
}
