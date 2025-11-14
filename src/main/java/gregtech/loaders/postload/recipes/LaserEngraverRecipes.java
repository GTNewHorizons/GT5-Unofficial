package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class LaserEngraverRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTCC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTCC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTPC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTPC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTRC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTRC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTEC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTEC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UEV)
            .addTo(laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.DTSC.getFluid(1_000))
            .fluidOutputs(Materials.ExcitedDTSC.getFluid(1_000))
            .requiresCleanRoom()
            .duration(41 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_UIV)
            .addTo(laserEngraverRecipes);

    }
}
