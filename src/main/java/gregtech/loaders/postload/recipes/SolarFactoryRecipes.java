package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.solarFactoryRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;

public class SolarFactoryRecipes implements Runnable {

    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Bottle_Beer.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_UV.get(1))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .specialValue(2)
            .addTo(solarFactoryRecipes);
    }
}
