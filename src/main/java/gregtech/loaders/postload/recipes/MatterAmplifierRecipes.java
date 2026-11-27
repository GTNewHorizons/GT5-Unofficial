package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.amplifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;

public class MatterAmplifierRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrap.get(9L))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.UUAmplifier, FluidShapes.fluidLiquid, 1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(amplifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrapbox.get(1L))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.UUAmplifier, FluidShapes.fluidLiquid, 1))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(amplifierRecipes);
    }
}
