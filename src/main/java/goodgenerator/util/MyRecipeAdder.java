package goodgenerator.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.api.recipe.ExtremeHeatExchangerRecipe;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.enums.GT_Values;

public class MyRecipeAdder {

    public static final MyRecipeAdder instance = new MyRecipeAdder();

    @Deprecated
    public void addLiquidMentalFuel(FluidStack input, FluidStack output, int EUt, int ticks) {
        GT_Values.RA.stdBuilder()
            .fluidInputs(input)
            .fluidOutputs(output)
            .duration(ticks)
            .eut(0)
            .specialValue(EUt)
            .addTo(GoodGeneratorRecipeMaps.naquadahReactorFuels);
    }

    @Deprecated
    public void addNaquadahFuelRefineRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack output, int EUt,
        int ticks, int tier) {
        GT_Values.RA.stdBuilder()
            .itemInputs(input2)
            .fluidInputs(input1)
            .fluidOutputs(output)
            .duration(ticks)
            .eut(EUt)
            .specialValue(tier)
            .noOptimize()
            .addTo(GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes);
    }

    public void addNeutronActivatorRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack[] output1,
        ItemStack[] output2, int ticks, int maxNKE, int minNKE) {
        if (maxNKE <= 0) maxNKE = 1;
        if (maxNKE >= 1100) maxNKE = 1100;
        if (minNKE < 0) minNKE = 0;
        if (minNKE >= maxNKE) minNKE = maxNKE - 1;
        GT_Values.RA.stdBuilder()
            .itemInputs(input2)
            .itemOutputs(output2)
            .fluidInputs(input1)
            .fluidOutputs(output1)
            .duration(ticks)
            .eut(0)
            .specialValue(maxNKE * 10000 + minNKE)
            .noOptimize()
            .addTo(GoodGeneratorRecipeMaps.neutronActivatorRecipes);
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

    @Deprecated
    public void addPreciseAssemblerRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack aOutput,
        int aEUt, int aDuration, int aTier) {
        if (aOutput == null) return;
        GT_Values.RA.stdBuilder()
            .itemInputs(aItemInputs)
            .itemOutputs(aOutput)
            .fluidInputs(aFluidInputs)
            .duration(aDuration)
            .eut(aEUt)
            .specialValue(aTier)
            .noOptimize()
            .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);
    }

    @Deprecated
    public void addComponentAssemblyLineRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
        ItemStack OutputItem, int aDuration, int aEUt, int casingLevel) {
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemInputArray)
            .itemOutputs(OutputItem)
            .fluidInputs(FluidInputArray)
            .duration(aDuration)
            .eut(aEUt)
            .specialValue(casingLevel)
            .noOptimize()
            .addTo(GoodGeneratorRecipeMaps.componentAssemblyLineRecipes);
    }
}
