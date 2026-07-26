package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.LFTR_OUTPUT_POWER;
import static gregtech.api.util.GTRecipeConstants.SPARGE_MAX_BYPRODUCT;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.liquidFluorineThoriumReactorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.spargeTowerRecipes;

import java.util.ArrayList;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;

public class RecipeLoaderLFTR {

    private static ArrayList<Fluid> mNobleGases;
    private static ArrayList<Fluid> mFluorideGases;
    private static ArrayList<Fluid> mSpargeGases;

    private static void configureSparging() {
        if (mSpargeGases == null) {
            mSpargeGases = new ArrayList<>();
            mSpargeGases.add(
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1))
                    .getFluid());
            mSpargeGases.add(
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (1))
                    .getFluid());
        }
        if (mNobleGases == null) {
            mNobleGases = new ArrayList<>();
            mNobleGases.add(mSpargeGases.get(0));
            mNobleGases.add(MU.legacyGtppFluidOf(Materials2Materials.Xenon));
            mNobleGases.add(MU.legacyGtppFluidOf(Materials2Materials.Neon));
            mNobleGases.add(MU.legacyGtppFluidOf(Materials2Materials.Argon));
            mNobleGases.add(MU.legacyGtppFluidOf(Materials2Materials.Krypton));
            mNobleGases.add(MU.legacyGtppFluidOf(Materials2Materials.Radon));
        }
        if (mFluorideGases == null) {
            mFluorideGases = new ArrayList<>();
            mFluorideGases.add(mSpargeGases.get(1));
            mFluorideGases.add(MU.legacyGtppFluidOf(Materials2Materials.LithiumFluoride));
            mFluorideGases.add(MU.legacyGtppFluidOf(Materials2Materials.NeptuniumHexafluoride));
            mFluorideGases.add(MU.legacyGtppFluidOf(Materials2Materials.TechnetiumHexafluoride));
            mFluorideGases.add(MU.legacyGtppFluidOf(Materials2Materials.SeleniumHexafluoride));
            mFluorideGases.add(MU.legacyGtppFluidOf(Materials2Materials.ThoriumTetrafluoride));
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
        FluidStack Li2BeF4 = MU.legacyGtppFluid(Materials2Materials.LithiumTetrafluoroberyllateLFTB, 200);

        // LiFBeF2ThF4UF4 - T3
        GTValues.RA.stdBuilder()
            .fluidInputs(MU.legacyGtppFluid(Materials2Materials.LFTRFuel3, 100), Li2BeF4)
            .fluidOutputs(
                MU.legacyGtppFluid(Materials2Materials.UraniumDepletedMoltenSaltUSalt, 100),
                MU.legacyGtppFluid(Materials2Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, 200),
                MU.legacyGtppFluid(Materials2Materials.UraniumHexafluoride, 20),
                MU.legacyGtppFluid(Materials2Materials.UraniumHexafluoride, 10))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 32768 * 4)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // LiFBeF2ZrF4UF4 - T2
        GTValues.RA.stdBuilder()
            .fluidInputs(MU.legacyGtppFluid(Materials2Materials.LFTRFuel2, 100), Li2BeF4)
            .fluidOutputs(
                MU.legacyGtppFluid(Materials2Materials.UraniumDepletedMoltenSaltUSalt, 50),
                MU.legacyGtppFluid(Materials2Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, 100),
                MU.legacyGtppFluid(Materials2Materials.UraniumHexafluoride, 10),
                MU.legacyGtppFluid(Materials2Materials.UraniumHexafluoride, 5))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 8192 * 4)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // LiFBeF2ZrF4U235 - T1
        GTValues.RA.stdBuilder()
            .fluidInputs(MU.legacyGtppFluid(Materials2Materials.LFTRFuel1, 100), Li2BeF4)
            .fluidOutputs(
                MU.legacyGtppFluid(Materials2Materials.UraniumDepletedMoltenSaltUSalt, 25),
                MU.legacyGtppFluid(Materials2Materials.ThoriumDepletedMoltenSaltTSalt, 50),
                MU.legacyGtppFluid(Materials2Materials.UraniumHexafluoride, 4),
                MU.legacyGtppFluid(Materials2Materials.UraniumHexafluoride, 2))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 8192)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // Sparging Recipes
        // For no randomization, set metadata to 0 with key SPARGE_MAX_BYPRODUCT.
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MU.legacyGtppFluid(Materials2Materials.UraniumDepletedMoltenSaltUSalt, 50))
            .fluidOutputs(
                MU.legacyGtppFluid(Materials2Materials.HeliumSpargedUSalt, 50),
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (0)),
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
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (100)),
                MU.legacyGtppFluid(Materials2Materials.ThoriumDepletedMoltenSaltTSalt, 50))
            .fluidOutputs(
                MU.legacyGtppFluid(Materials2Materials.FluorineSpargedTSalt, 50),
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (0)),
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
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (100)),
                MU.legacyGtppFluid(Materials2Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, 50))
            .fluidOutputs(
                MU.legacyGtppFluid(Materials2Materials.FluorineSpargedTBSalt, 50),
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (0)),
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
