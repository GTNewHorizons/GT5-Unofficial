package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtruderRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

public class ExtruderRecipes implements Runnable {

    @Override
    public void run() {
        // wax capsule
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.FR_Wax.get(1L), ItemList.Shape_Extruder_Cell.get(0L))
            .itemOutputs(ItemList.FR_WaxCapsule.get(1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(sExtruderRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.FR_RefractoryWax.get(1L), ItemList.Shape_Extruder_Cell.get(0L))
            .itemOutputs(ItemList.FR_RefractoryCapsule.get(1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(16)
            .addTo(sExtruderRecipes);
    }
}
