package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.BUCKETS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;

public class LaserEngraverRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentCrudeCatalyst.getFluid(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTCC.getFluid(1 * BUCKETS))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentProsaicCatalyst.getFluid(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTPC.getFluid(1 * BUCKETS))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentResplendentCatalyst.getFluid(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTRC.getFluid(1 * BUCKETS))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentExoticCatalyst.getFluid(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTEC.getFluid(1 * BUCKETS))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentStellarCatalyst.getFluid(1 * BUCKETS))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTSC.getFluid(1 * BUCKETS))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UIV)
            .addTo(laserEngraverRecipes);

    }
}
