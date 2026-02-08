package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.FluidLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;

public class FluidHeater implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .circuit(10)
            .fluidInputs(new FluidStack(FluidLoader.fulvicAcid, 1000))
            .fluidOutputs(new FluidStack(FluidLoader.heatedfulvicAcid, 1000))
            .duration(4 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidHeaterRecipes);
    }
}
