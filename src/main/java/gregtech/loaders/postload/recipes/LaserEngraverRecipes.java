package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;

public class LaserEngraverRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentCrudeCatalyst.getFluid(1000))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTCC.getFluid(1000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .noOptimize()
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentProsaicCatalyst.getFluid(1000))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTPC.getFluid(1000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .noOptimize()
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentResplendentCatalyst.getFluid(1000))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTRC.getFluid(1000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .noOptimize()
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentExoticCatalyst.getFluid(1000))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTEC.getFluid(1000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .noOptimize()
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsUEVplus.DimensionallyTranscendentStellarCatalyst.getFluid(1000))
            .fluidOutputs(MaterialsUEVplus.ExcitedDTSC.getFluid(1000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UIV)
            .noOptimize()
            .addTo(laserEngraverRecipes);

    }
}
