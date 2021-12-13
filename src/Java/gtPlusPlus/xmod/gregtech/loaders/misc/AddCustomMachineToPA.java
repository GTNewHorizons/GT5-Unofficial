package gtPlusPlus.xmod.gregtech.loaders.misc;

import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_ProcessingArray_Manager;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;

public class AddCustomMachineToPA {

	public static final void registerRecipeMapForID(int aID, GT_Recipe_Map aMap) {
		Logger.INFO("Attempting to add map "+aMap.mNEIName+" to Processing Array for Meta Tile "+aID+". Success? "+GT_ProcessingArray_Manager.registerRecipeMapForMeta(aID, aMap));
	}

	public static final void registerRecipeMapBetweenRangeOfIDs(int aMin, int aMax, GT_Recipe_Map aMap) {
		for (int i=aMin; i<=aMax;i++) {
			registerRecipeMapForID(i, aMap);
		}	
	}

	public static void register() {

		// Simple Washers
		registerRecipeMapForID(767, GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes);
		registerRecipeMapBetweenRangeOfIDs(31017, 31020, GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes);

	}

}
