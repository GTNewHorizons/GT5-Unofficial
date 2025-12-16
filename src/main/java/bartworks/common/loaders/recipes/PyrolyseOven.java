package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.FluidLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class PyrolyseOven implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Wood.getDust(10))
            .circuit(10)
            .fluidInputs(new FluidStack(FluidLoader.Kerogen, 1_000))
            .fluidOutputs(Materials.Oil.getFluid(1_000))
            .duration(5 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(pyrolyseRecipes);
    }
}
