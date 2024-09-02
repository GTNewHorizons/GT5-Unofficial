package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.LFTR_OUTPUT_POWER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.liquidFluorineThoriumReactorRecipes;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.util.GasSpargingRecipeMap;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.nuclear.MaterialsNuclides;

public class RecipeLoaderLFTR {

    private static AutoMap<Fluid> mNobleGases;
    private static AutoMap<Fluid> mFluorideGases;
    private static AutoMap<Fluid> mSpargeGases;

    private static void configureSparging() {
        if (mSpargeGases == null) {
            mSpargeGases = new AutoMap<>();
            mSpargeGases.add(
                Materials.Helium.getGas(1)
                    .getFluid());
            mSpargeGases.add(
                Materials.Fluorine.getGas(1)
                    .getFluid());
        }
        if (mNobleGases == null) {
            mNobleGases = new AutoMap<>();
            mNobleGases.add(mSpargeGases.get(0));
            mNobleGases.add(MaterialsElements.getInstance().XENON.getFluid());
            mNobleGases.add(MaterialsElements.getInstance().NEON.getFluid());
            mNobleGases.add(MaterialsElements.getInstance().ARGON.getFluid());
            mNobleGases.add(MaterialsElements.getInstance().KRYPTON.getFluid());
            mNobleGases.add(MaterialsElements.getInstance().RADON.getFluid());
        }
        if (mFluorideGases == null) {
            mFluorideGases = new AutoMap<>();
            mFluorideGases.add(mSpargeGases.get(1));
            mFluorideGases.add(MaterialsFluorides.LITHIUM_FLUORIDE.getFluid());
            mFluorideGases.add(MaterialsFluorides.NEPTUNIUM_HEXAFLUORIDE.getFluid());
            mFluorideGases.add(MaterialsFluorides.TECHNETIUM_HEXAFLUORIDE.getFluid());
            mFluorideGases.add(MaterialsFluorides.SELENIUM_HEXAFLUORIDE.getFluid());
            mFluorideGases.add(MaterialsFluorides.THORIUM_TETRAFLUORIDE.getFluid());
        }
    }

    public static void generate() {
        // Fli2BeF4 + Thorium TetraFluoride = Uranium233
        // 72k Ticks/hr
        // 1l/4t = 1000l/hr
        // 1l/40t = 1000l/10hr (Probably better) LiFBeF2ThF4UF4
        // 1l/20t= 1000l/2.5hr LiFBeF2ZrF4UF4
        // 1l/10t= 1000l/2.5hr LiFBeF2ZrF4U235

        configureSparging();
        FluidStack Li2BeF4 = MaterialsNuclides.Li2BeF4.getFluidStack(200);

        // LiFBeF2ThF4UF4 - T3
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsNuclides.LiFBeF2ThF4UF4.getFluidStack(100), Li2BeF4)
            .fluidOutputs(
                MaterialsNuclides.LiFBeF2UF4FP.getFluidStack(100),
                MaterialsNuclides.LiFBeF2ThF4.getFluidStack(200),
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getFluidStack(20),
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getFluidStack(10))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 32768 * 4)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // LiFBeF2ZrF4UF4 - T2
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsNuclides.LiFBeF2ZrF4UF4.getFluidStack(100), Li2BeF4)
            .fluidOutputs(
                MaterialsNuclides.LiFBeF2UF4FP.getFluidStack(50),
                MaterialsNuclides.LiFBeF2ThF4.getFluidStack(100),
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getFluidStack(10),
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getFluidStack(5))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 8192 * 4)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // LiFBeF2ZrF4U235 - T1
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsNuclides.LiFBeF2ZrF4U235.getFluidStack(100), Li2BeF4)
            .fluidOutputs(
                MaterialsNuclides.LiFBeF2UF4FP.getFluidStack(25),
                MaterialsNuclides.LiFThF4.getFluidStack(50),
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getFluidStack(4),
                MaterialsFluorides.URANIUM_HEXAFLUORIDE.getFluidStack(2))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 8192)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // Sparging NEI Recipes
        GasSpargingRecipeMap.addRecipe(
            new FluidStack(mSpargeGases.get(0), 1000),
            MaterialsNuclides.LiFBeF2UF4FP.getFluidStack(50),
            MaterialsNuclides.Sparged_LiFBeF2UF4FP.getFluidStack(50),
            new FluidStack[] { new FluidStack(mNobleGases.get(1), 10), new FluidStack(mNobleGases.get(2), 10),
                new FluidStack(mNobleGases.get(3), 10), new FluidStack(mNobleGases.get(4), 10),
                new FluidStack(mNobleGases.get(5), 10) },
            new int[] { 20000, 20000, 20000, 20000, 20000 });

        GasSpargingRecipeMap.addRecipe(
            new FluidStack(mSpargeGases.get(1), 100),
            MaterialsNuclides.LiFThF4.getFluidStack(50),
            MaterialsNuclides.Sparged_LiFThF4.getFluidStack(50),
            new FluidStack[] { new FluidStack(mFluorideGases.get(1), 5), new FluidStack(mFluorideGases.get(2), 5),
                new FluidStack(mFluorideGases.get(3), 5), new FluidStack(mFluorideGases.get(4), 5),
                new FluidStack(mFluorideGases.get(5), 5) },
            new int[] { 1000, 1000, 1000, 1000, 1000 });

        GasSpargingRecipeMap.addRecipe(
            new FluidStack(mSpargeGases.get(1), 100),
            MaterialsNuclides.LiFBeF2ThF4.getFluidStack(50),
            MaterialsNuclides.Sparged_LiFBeF2ThF4.getFluidStack(50),
            new FluidStack[] { new FluidStack(mFluorideGases.get(1), 10), new FluidStack(mFluorideGases.get(2), 10),
                new FluidStack(mFluorideGases.get(3), 10), new FluidStack(mFluorideGases.get(4), 10),
                new FluidStack(mFluorideGases.get(5), 10) },
            new int[] { 2000, 2000, 2000, 2000, 2000 });
    }
}
