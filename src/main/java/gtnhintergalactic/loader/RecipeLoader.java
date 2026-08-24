package gtnhintergalactic.loader;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;
import gtnhintergalactic.recipe.GasSiphonRecipes;
import gtnhintergalactic.recipe.SpaceMiningRecipes;
import gtnhintergalactic.recipe.SpacePumpingRecipes;

/**
 * Loader for all recipes
 *
 * @author minecraft7771
 */
public class RecipeLoader implements Runnable {

    /**
     * Execute the recipe loader
     */
    @Override
    public void run() {
        SpaceMiningRecipes.addAsteroids();
        SpacePumpingRecipes.addPumpingRecipes();
        GasSiphonRecipes.addPumpingRecipes();

        // Planetary Gas Siphon Conversion Recipe (2.9 -> next major migration).
        // Lives here (postInit) rather than in gregtech's MTERecipeLoader (init) because the siphon item is
        // only set by this mod's MachineLoader during init — gregtech's init runs first in the FML event order
        // and would trip ItemList.sanityCheck if it referenced the siphon there.
        GTModHandler.addShapelessCraftingRecipe(
            ItemList.PlanetarySiphon.get(1),
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { ItemList.PlanetaryGasSiphonController });
    }
}
