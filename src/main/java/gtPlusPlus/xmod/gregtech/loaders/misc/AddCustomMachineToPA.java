package gtPlusPlus.xmod.gregtech.loaders.misc;

import gregtech.api.util.ProcessingArrayManager;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class AddCustomMachineToPA {

    public static void register() {

        // Simple Washers
        ProcessingArrayManager.addRecipeMapToPA("simplewasher.01", GTPPRecipeMaps.simpleWasherRecipes);
    }
}
