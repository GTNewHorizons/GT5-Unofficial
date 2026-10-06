package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.recipe.RecipeMaps.liquidFluorineThoriumReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.spargeTowerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.LFTR_OUTPUT_POWER;
import static gregtech.api.util.GTRecipeConstants.SPARGE_MAX_BYPRODUCT;

import java.util.ArrayList;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;

public class RecipeLoaderLFTR {

    private static ArrayList<Fluid> mNobleGases;
    private static ArrayList<Fluid> mFluorideGases;
    private static ArrayList<Fluid> mSpargeGases;

    private static void configureSparging() {
        if (mSpargeGases == null) {
            mSpargeGases = new ArrayList<>();
            mSpargeGases.add(
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 1)
                    .getFluid());
            mSpargeGases.add(
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 1)
                    .getFluid());
        }
        if (mNobleGases == null) {
            mNobleGases = new ArrayList<>();
            mNobleGases.add(mSpargeGases.get(0));
            mNobleGases.add(MaterialLibAPI.getFluid(Materials.Xenon, FluidShapes.fluidLiquid));
            mNobleGases.add(MaterialLibAPI.getFluid(Materials.Neon, FluidShapes.fluidLiquid));
            mNobleGases.add(MaterialLibAPI.getFluid(Materials.Argon, FluidShapes.fluidGas));
            mNobleGases.add(MaterialLibAPI.getFluid(Materials.Krypton, FluidShapes.fluidLiquid));
            mNobleGases.add(MaterialLibAPI.getFluid(Materials.Radon, FluidShapes.fluidGas));
        }
        if (mFluorideGases == null) {
            mFluorideGases = new ArrayList<>();
            mFluorideGases.add(mSpargeGases.get(1));
            mFluorideGases.add(MaterialLibAPI.getFluid(Materials.LithiumFluoride, FluidShapes.fluidLiquid));
            mFluorideGases.add(MaterialLibAPI.getFluid(Materials.NeptuniumHexafluoride, FluidShapes.fluidGas));
            mFluorideGases.add(MaterialLibAPI.getFluid(Materials.TechnetiumHexafluoride, FluidShapes.fluidGas));
            mFluorideGases.add(MaterialLibAPI.getFluid(Materials.SeleniumHexafluoride, FluidShapes.fluidGas));
            mFluorideGases.add(MaterialLibAPI.getFluid(Materials.ThoriumTetrafluoride, FluidShapes.fluidLiquid));
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
        FluidStack Li2BeF4 = MaterialLibAPI
            .getFluidStack(Materials.LithiumTetrafluoroberyllateLFTB, FluidShapes.fluidLiquid, 200);

        // LiFBeF2ThF4UF4 - T3
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuel3, FluidShapes.fluidLiquid, 100), Li2BeF4)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.UraniumDepletedMoltenSaltUSalt, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI
                    .getFluidStack(Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.UraniumHexafluoride, FluidShapes.fluidLiquid, 20),
                MaterialLibAPI.getFluidStack(Materials.UraniumHexafluoride, FluidShapes.fluidLiquid, 10))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 32768 * 4)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // LiFBeF2ZrF4UF4 - T2
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuel2, FluidShapes.fluidLiquid, 100), Li2BeF4)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.UraniumDepletedMoltenSaltUSalt, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI
                    .getFluidStack(Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.UraniumHexafluoride, FluidShapes.fluidLiquid, 10),
                MaterialLibAPI.getFluidStack(Materials.UraniumHexafluoride, FluidShapes.fluidLiquid, 5))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 8192 * 4)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // LiFBeF2ZrF4U235 - T1
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.LFTRFuel1, FluidShapes.fluidLiquid, 100), Li2BeF4)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.UraniumDepletedMoltenSaltUSalt, FluidShapes.fluidLiquid, 25),
                MaterialLibAPI.getFluidStack(Materials.ThoriumDepletedMoltenSaltTSalt, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.UraniumHexafluoride, FluidShapes.fluidLiquid, 4),
                MaterialLibAPI.getFluidStack(Materials.UraniumHexafluoride, FluidShapes.fluidLiquid, 2))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 8192)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // Sparging Recipes
        // For no randomization, set metadata to 0 with key SPARGE_MAX_BYPRODUCT.
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.UraniumDepletedMoltenSaltUSalt, FluidShapes.fluidLiquid, 50))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.HeliumSpargedUSalt, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 0),
                new FluidStack(mNobleGases.get(1), 0),
                new FluidStack(mNobleGases.get(2), 0),
                new FluidStack(mNobleGases.get(3), 0),
                new FluidStack(mNobleGases.get(4), 0),
                new FluidStack(mNobleGases.get(5), 0))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(SPARGE_MAX_BYPRODUCT, 200)
            .addTo(spargeTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.ThoriumDepletedMoltenSaltTSalt, FluidShapes.fluidLiquid, 50))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.FluorineSpargedTSalt, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 0),
                new FluidStack(mFluorideGases.get(1), 0),
                new FluidStack(mFluorideGases.get(2), 0),
                new FluidStack(mFluorideGases.get(3), 0),
                new FluidStack(mFluorideGases.get(4), 0),
                new FluidStack(mFluorideGases.get(5), 0))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(SPARGE_MAX_BYPRODUCT, 10)
            .addTo(spargeTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 100),
                MaterialLibAPI
                    .getFluidStack(Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, FluidShapes.fluidLiquid, 50))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.FluorineSpargedTBSalt, FluidShapes.fluidLiquid, 50),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 0),
                new FluidStack(mFluorideGases.get(1), 0),
                new FluidStack(mFluorideGases.get(2), 0),
                new FluidStack(mFluorideGases.get(3), 0),
                new FluidStack(mFluorideGases.get(4), 0),
                new FluidStack(mFluorideGases.get(5), 0))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(SPARGE_MAX_BYPRODUCT, 20)
            .addTo(spargeTowerRecipes);
    }
}
