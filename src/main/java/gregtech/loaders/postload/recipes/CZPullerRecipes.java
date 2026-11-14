package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.czRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.metadata.CZPullerTierKey;

public class CZPullerRecipes implements Runnable {

    private static final CZPullerTierKey TIER = CZPullerTierKey.INSTANCE;

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Silicon_Seed_Crystal.get(1))
            .fluidInputs(Materials.Nitrogen.getGas(1000L))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(1))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(TIER, 1)
            .addTo(czRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.AlGaAs_Seed_Crystal.get(1))
            .fluidInputs(Materials.UUMatter.getFluid(16000L))
            .itemOutputs(ItemList.AlGaAs_Monocrystal.get(1))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(TIER, 2)
            .addTo(czRecipes);
    }
}
