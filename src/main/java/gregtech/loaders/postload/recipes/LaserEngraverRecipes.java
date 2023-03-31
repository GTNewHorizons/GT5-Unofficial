package gregtech.loaders.postload.recipes;

import gregtech.api.enums.TierEU;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

public class LaserEngraverRecipes implements Runnable {

    @Override
    public void run() {

        GT_Values.RA.stdBuilder()
            .noItemInputs()
            .noItemOutputs()
            .fluidInputs(
                Materials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000)
            )
            .fluidOutputs(
                Materials.ExcitedDTCC.getFluid(1000)
            )
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(sLaserEngraverRecipes);

        GT_Values.RA.stdBuilder()
            .noItemInputs()
            .noItemOutputs()
            .fluidInputs(
                Materials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000)
            )
            .fluidOutputs(
                Materials.ExcitedDTPC.getFluid(1000)
            )
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .addTo(sLaserEngraverRecipes);

        GT_Values.RA.stdBuilder()
            .noItemInputs()
            .noItemOutputs()
            .fluidInputs(
                Materials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000)
            )
            .fluidOutputs(
                Materials.ExcitedDTRC.getFluid(1000)
            )
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .addTo(sLaserEngraverRecipes);

        GT_Values.RA.stdBuilder()
            .noItemInputs()
            .noItemOutputs()
            .fluidInputs(
                Materials.DimensionallyTranscendentExoticCatalyst.getFluid(1000)
            )
            .fluidOutputs(
                Materials.ExcitedDTEC.getFluid(1000)
            )
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .addTo(sLaserEngraverRecipes);


    }
}
