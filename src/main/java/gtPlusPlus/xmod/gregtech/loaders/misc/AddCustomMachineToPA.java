package gtPlusPlus.xmod.gregtech.loaders.misc;

import java.lang.reflect.Method;

import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class AddCustomMachineToPA {

	private static final boolean sDoesPatchExist;
	private static final Class sManagerPA;
	private static final Method sRegisterRecipeMapForMeta;

	static {
		sDoesPatchExist = ReflectionUtils.doesClassExist("gregtech.api.util.GT_ProcessingArray_Manager");
		if (sDoesPatchExist) {
			sManagerPA = ReflectionUtils.getClass("gregtech.api.util.GT_ProcessingArray_Manager");
			sRegisterRecipeMapForMeta = ReflectionUtils.getMethod(sManagerPA, "registerRecipeMapForMeta", int.class, GT_Recipe_Map.class);
		}
		else {
			sManagerPA = null;
			sRegisterRecipeMapForMeta = null;
		}
	}

	public static final void registerRecipeMapForID(int aID, GT_Recipe_Map aMap) {
		if (sDoesPatchExist) {
			ReflectionUtils.invokeNonBool(null, sRegisterRecipeMapForMeta, new Object[] {aID, aMap});
		}

	}

	public static final void registerRecipeMapBetweenRangeOfIDs(int aMin, int aMax, GT_Recipe_Map aMap) {
		if (sDoesPatchExist) {
			for (int i=aMin; i<=aMax;i++) {
				ReflectionUtils.invokeNonBool(null, sRegisterRecipeMapForMeta, new Object[] {i, aMap});
				//GT_ProcessingArray_Manager.registerRecipeMapForMeta(i, aMap);
			}
		}		
	}

	public static void register() {
		
		// Simple Washers
		registerRecipeMapForID(767, GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes);
		registerRecipeMapBetweenRangeOfIDs(31017, 31020, GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes);
		
	}

}
