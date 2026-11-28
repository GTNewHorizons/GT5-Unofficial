package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.layeringMachineRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;

public class LayeringMachineRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.WovenKevlar.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.PolyurethaneResin, FluidShapes.fluidLiquid, 1000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Kevlar, Shapes.plate, 1))
            .circuit(1)
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(layeringMachineRecipes);
    }
}
