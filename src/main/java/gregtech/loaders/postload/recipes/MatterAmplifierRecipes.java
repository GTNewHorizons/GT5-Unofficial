package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMap.sAmplifiers;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class MatterAmplifierRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrap.get(9L))
            .fluidOutputs(Materials.UUAmplifier.getFluid(1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sAmplifiers);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrapbox.get(1L))
            .fluidOutputs(Materials.UUAmplifier.getFluid(1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sAmplifiers);
    }
}
