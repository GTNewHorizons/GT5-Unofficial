package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.TICKS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

public class ExtruderRecipes implements Runnable {

    @Override
    public void run() {
        // wax capsule
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.FR_Wax.get(1L), ItemList.Shape_Extruder_Cell.get(0L))
            .itemOutputs(ItemList.FR_WaxCapsule.get(1L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(extruderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.FR_RefractoryWax.get(1L), ItemList.Shape_Extruder_Cell.get(0L))
            .itemOutputs(ItemList.FR_RefractoryCapsule.get(1L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(16)
            .addTo(extruderRecipes);
    }
}
