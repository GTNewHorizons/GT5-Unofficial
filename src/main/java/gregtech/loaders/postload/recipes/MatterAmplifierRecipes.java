package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.amplifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;

public class MatterAmplifierRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrap.get(9L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.UUAmplifier, Materials2FluidShapes.shapeFluidLiquid, (int) (1)))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(amplifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrapbox.get(1L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.UUAmplifier, Materials2FluidShapes.shapeFluidLiquid, (int) (1)))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(amplifierRecipes);
    }
}
