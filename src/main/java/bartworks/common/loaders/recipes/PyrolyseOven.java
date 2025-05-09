package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.FluidLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;

public class PyrolyseOven implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10), Materials.Wood.getDust(10))
            .fluidInputs(new FluidStack(FluidLoader.Kerogen, 1000))
            .fluidOutputs(Materials.Oil.getFluid(1000))
            .duration(5 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(pyrolyseRecipes);
    }
}
