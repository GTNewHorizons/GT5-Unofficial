package gtPlusPlus.xmod.gregtech.loaders.misc;

import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_ProcessingArray_Manager;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;

public class AddCustomMachineToPA {
	public static void register() {

		// Simple Washers
		GT_ProcessingArray_Manager.addRecipeMapToPA("simplewasher.01", GTPP_Recipe.GTPP_Recipe_Map.sSimpleWasherRecipes);

	}

}
