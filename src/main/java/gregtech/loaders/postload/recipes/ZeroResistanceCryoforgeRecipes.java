package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.zeroResistanceCryoforgeRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;

public class ZeroResistanceCryoforgeRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_UV.get(1),
                MaterialLibAPI.getStack(Materials.Neutronium, TEBlockShapes.pipeTiny, 14))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SuperconductorUV, TEBlockShapes.wireGt01, 21))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SuperconductorUVBase, FluidShapes.fluidMolten, 21 * HALF_INGOTS),
                MaterialLibAPI.getFluidStack(Materials.LiquidHelium, FluidShapes.fluidLiquid, 20_000))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(zeroResistanceCryoforgeRecipes);
    }
}
