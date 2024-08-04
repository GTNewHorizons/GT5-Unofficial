package goodgenerator.util;

import net.minecraftforge.fluids.FluidStack;

import goodgenerator.api.recipe.ExtremeHeatExchangerRecipe;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;

public class MyRecipeAdder {

    public static final MyRecipeAdder instance = new MyRecipeAdder();

    public static int computeRangeNKE(int maxNKE, int minNKE) {
        if (maxNKE <= 0) maxNKE = 1;
        if (maxNKE >= 1100) maxNKE = 1100;
        if (minNKE < 0) minNKE = 0;
        if (minNKE >= maxNKE) minNKE = maxNKE - 1;
        return maxNKE * 10000 + minNKE;
    }

    // todo: understand why it needs a custom recipe class and migrate it on RA2
    @Deprecated
    public void addExtremeHeatExchangerRecipe(FluidStack HotFluid, FluidStack ColdFluid, FluidStack WorkFluid,
        FluidStack HeatedWorkFluid, FluidStack OverHeatedWorkFluid, int Threshold) {
        GoodGeneratorRecipeMaps.extremeHeatExchangerFuels.addRecipe(
            new ExtremeHeatExchangerRecipe(
                new FluidStack[] { HotFluid, WorkFluid },
                new FluidStack[] { HeatedWorkFluid, OverHeatedWorkFluid, ColdFluid },
                Threshold));
    }
}
