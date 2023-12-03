package goodgenerator.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.api.recipe.ExtremeHeatExchangerRecipe;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;

public class MyRecipeAdder {

    public static final MyRecipeAdder instance = new MyRecipeAdder();

    public void addLiquidMentalFuel(FluidStack input, FluidStack output, int EUt, int ticks) {
        GoodGeneratorRecipeMaps.naquadahReactorFuels.addRecipe(
                true,
                null,
                null,
                null,
                new FluidStack[] { input },
                new FluidStack[] { output },
                ticks,
                0,
                EUt);
    }

    public void addNaquadahFuelRefineRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack output, int EUt,
            int ticks, int tier) {
        GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes
                .addRecipe(false, input2, null, null, input1, new FluidStack[] { output }, ticks, EUt, tier);
    }

    public void addNeutronActivatorRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack[] output1,
            ItemStack[] output2, int ticks, int maxNKE, int minNKE) {
        if (maxNKE <= 0) maxNKE = 1;
        if (maxNKE >= 1100) maxNKE = 1100;
        if (minNKE < 0) minNKE = 0;
        if (minNKE >= maxNKE) minNKE = maxNKE - 1;
        GoodGeneratorRecipeMaps.neutronActivatorRecipes
                .addRecipe(false, input2, output2, null, input1, output1, ticks, 0, maxNKE * 10000 + minNKE);
    }

    public void addExtremeHeatExchangerRecipe(FluidStack HotFluid, FluidStack ColdFluid, FluidStack WorkFluid,
            FluidStack HeatedWorkFluid, FluidStack OverHeatedWorkFluid, int Threshold) {
        GoodGeneratorRecipeMaps.extremeHeatExchangerFuels.addRecipe(
                new ExtremeHeatExchangerRecipe(
                        new FluidStack[] { HotFluid, WorkFluid },
                        new FluidStack[] { HeatedWorkFluid, OverHeatedWorkFluid, ColdFluid },
                        Threshold));
    }

    public void addPreciseAssemblerRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack aOutput,
            int aEUt, int aDuration, int aTier) {
        if (aOutput == null) return;
        GoodGeneratorRecipeMaps.preciseAssemblerRecipes.addRecipe(
                false,
                aItemInputs,
                new ItemStack[] { aOutput },
                null,
                null,
                aFluidInputs,
                null,
                aDuration,
                aEUt,
                aTier);
    }

    public void addComponentAssemblyLineRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
            ItemStack OutputItem, int aDuration, int aEUt, int casingLevel) {
        GoodGeneratorRecipeMaps.componentAssemblyLineRecipes.addRecipe(
                false,
                ItemInputArray,
                new ItemStack[] { OutputItem },
                null,
                FluidInputArray,
                null,
                aDuration,
                aEUt,
                casingLevel);
    }
}
