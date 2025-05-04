package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.czRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class CZPullerRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Silicon_Seed_Crystal.get(1L))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(1L))
            .fluidInputs(Materials.Nitrogen.getFluid(1000L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(czRecipes);
    }
}
