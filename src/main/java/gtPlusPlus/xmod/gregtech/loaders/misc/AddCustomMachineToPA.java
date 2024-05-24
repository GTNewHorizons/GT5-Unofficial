package gtPlusPlus.xmod.gregtech.loaders.misc;

import gregtech.api.util.GT_ProcessingArray_Manager;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class AddCustomMachineToPA {

    public static void register() {

        // Simple Washers
        GT_ProcessingArray_Manager.addRecipeMapToPA("simplewasher.01", GTPPRecipeMaps.simpleWasherRecipes);
    }
}
