package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.amplifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class MatterAmplifierRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrap.get(9L))
            .fluidOutputs(Materials.UUAmplifier.getFluid(1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(amplifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrapbox.get(1L))
            .fluidOutputs(Materials.UUAmplifier.getFluid(1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(amplifierRecipes);
    }
}
