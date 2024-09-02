package bartworks.common.loaders.recipes;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;

import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.FluidLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;

public class ChemicalReactor implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10))
            .fluidInputs(new FluidStack(FluidLoader.heatedfulvicAcid, 1000))
            .fluidOutputs(new FluidStack(FluidLoader.Kerogen, 1000))
            .duration(3 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

    }
}
