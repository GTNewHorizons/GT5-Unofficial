package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import bartworks.common.tileentities.multis.MTEHighTempGasCooledReactor;
import bartworks.common.tileentities.multis.MTEThoriumHighTempReactor;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class FormingPress implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials),
                Materials.Graphite.getDust(64))
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 1),
                Materials.Silicon.getDust(64))
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 2))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 2),
                Materials.Graphite.getDust(64))
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 3))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(formingPressRecipes);

        int i = 0;
        for (MTEHighTempGasCooledReactor.HTGRMaterials.Fuel_ fuel : MTEHighTempGasCooledReactor.HTGRMaterials.sHTGR_Fuel) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, 1, i),
                    Materials.Carbon.getDust(64))
                .itemOutputs(new ItemStack(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, 1, i + 1))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, 1, i + 1),
                    Materials.Silicon.getDust(64))
                .itemOutputs(new ItemStack(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, 1, i + 2))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(formingPressRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, 1, i + 2),
                    Materials.Graphite.getDust(64))
                .itemOutputs(new ItemStack(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, 1, i + 3))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(formingPressRecipes);
            i += MTEHighTempGasCooledReactor.HTGRMaterials.MATERIALS_PER_FUEL;
        }
    }
}
