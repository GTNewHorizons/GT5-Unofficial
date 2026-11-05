package tectech.loader.recipe;

import static gregtech.api.recipe.RecipeEnvironment.CLEANROOM;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import tectech.thing.CustomItemList;

public class CircuitAssembler implements Runnable {

    @Override
    public void run() {
        // Parametrizer Memory Card
        GTValues.RA.stdBuilder()
            .itemInputs(Circuits.LV.get(2), MaterialLibAPI.getStack(Materials.Gold, Shapes.foil, 4))
            .itemOutputs(CustomItemList.parametrizerMemory.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Polyethylene, FluidShapes.fluidMolten, 1 * HALF_INGOTS))
            .requiresEnvironment(CLEANROOM)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(circuitAssemblerRecipes);
    }
}
