package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.tileentities.multis.MTEThoriumHighTempReactor;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;

public class FormingPress implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials),
                MaterialLibAPI.getStack(Materials2Materials.Graphite, Materials2Shapes.shapeDust, (int) (64)))
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, (int) (64)))
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 2))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 2),
                MaterialLibAPI.getStack(Materials2Materials.Graphite, Materials2Shapes.shapeDust, (int) (64)))
            .itemOutputs(new ItemStack(MTEThoriumHighTempReactor.THTRMaterials.aTHTR_Materials, 1, 3))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(formingPressRecipes);
    }
}
