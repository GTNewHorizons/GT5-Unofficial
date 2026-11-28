package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.laserCutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;

public final class LaserCutterRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Precise_Silicon_Boule.get(1))
            .itemOutputs(ItemList.Crude_Si_Wafer.get(48))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(laserCutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Precise_Microwaved_Silicon_Boule.get(1))
            .itemOutputs(ItemList.Microwaved_Si_Wafer.get(48))
            .duration(240 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(laserCutterRecipes);
    }
}
