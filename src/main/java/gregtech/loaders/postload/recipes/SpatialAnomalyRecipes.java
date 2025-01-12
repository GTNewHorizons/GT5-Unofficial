package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.SpatialAnomalyRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.metadata.SpatialAnomalyTierKey;
import gregtech.api.util.GTOreDictUnificator;

public class SpatialAnomalyRecipes implements Runnable {

    private static final SpatialAnomalyTierKey ANOMALY_TYPE = SpatialAnomalyTierKey.INSTANCE;

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.WeakInfinityCatalyst.get(11),
                ItemList.CrystalCube.get(1))
            .itemOutputs(ItemList.FractalAnomaly.get(1))
            .duration(5 * SECONDS)
            .metadata(ANOMALY_TYPE, 1)
            .eut(0)
            .addTo(SpatialAnomalyRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Vyroxeres, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Infinity, 1L))
            .duration(5 * SECONDS)
            .metadata(ANOMALY_TYPE, 1)
            .eut(0)
            .addTo(SpatialAnomalyRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 64L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 64L))
            .duration(5 * SECONDS)
            .metadata(ANOMALY_TYPE, 2)
            .eut(0)
            .addTo(SpatialAnomalyRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Neutronium.getMolten(1000L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Naquadria, 64L))
            .duration(5 * SECONDS)
            .metadata(ANOMALY_TYPE, 3)
            .eut(0)
            .addTo(SpatialAnomalyRecipes);
    }
}
