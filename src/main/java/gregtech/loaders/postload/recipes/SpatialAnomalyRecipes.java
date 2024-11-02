package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.SpatialAnomalyRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

public class SpatialAnomalyRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Vyroxeres, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Infinity, 1L))
            .duration(5 * SECONDS)
            .eut(0)
            .addTo(SpatialAnomalyRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 64L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 64L))
            .duration(5 * SECONDS)
            .eut(0)
            .addTo(SpatialAnomalyRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Neutronium.getMolten(1000L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Naquadria, 64L))
            .duration(5 * SECONDS)
            .eut(0)
            .addTo(SpatialAnomalyRecipes);
    }
}
