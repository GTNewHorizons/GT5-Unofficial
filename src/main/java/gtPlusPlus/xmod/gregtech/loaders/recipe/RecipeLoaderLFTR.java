package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.recipe.RecipeMaps.liquidFluorineThoriumReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.spargeTowerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.LFTR_OUTPUT_POWER;
import static gregtech.api.util.GTRecipeConstants.SPARGE_MAX_BYPRODUCT;

import java.util.ArrayList;

import gregtech.api.enums.materials2.Materials;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.material.MaterialUtils;

public class RecipeLoaderLFTR {

    private static ArrayList<Fluid> mNobleGases;
    private static ArrayList<Fluid> mFluorideGases;
    private static ArrayList<Fluid> mSpargeGases;

    private static void configureSparging() {
        if (mSpargeGases == null) {
            mSpargeGases = new ArrayList<>();
            mSpargeGases.add(
                MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1))
                    .getFluid());
            mSpargeGases.add(
                MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (1))
                    .getFluid());
        }
        if (mNobleGases == null) {
            mNobleGases = new ArrayList<>();
            mNobleGases.add(mSpargeGases.get(0));
            mNobleGases.add(MaterialUtils.legacyGtppFluidOf(Materials.Xenon));
            mNobleGases.add(MaterialUtils.legacyGtppFluidOf(Materials.Neon));
            mNobleGases.add(MaterialUtils.legacyGtppFluidOf(Materials.Argon));
            mNobleGases.add(MaterialUtils.legacyGtppFluidOf(Materials.Krypton));
            mNobleGases.add(MaterialUtils.legacyGtppFluidOf(Materials.Radon));
        }
        if (mFluorideGases == null) {
            mFluorideGases = new ArrayList<>();
            mFluorideGases.add(mSpargeGases.get(1));
            mFluorideGases.add(MaterialUtils.legacyGtppFluidOf(Materials.LithiumFluoride));
            mFluorideGases.add(MaterialUtils.legacyGtppFluidOf(Materials.NeptuniumHexafluoride));
            mFluorideGases.add(MaterialUtils.legacyGtppFluidOf(Materials.TechnetiumHexafluoride));
            mFluorideGases.add(MaterialUtils.legacyGtppFluidOf(Materials.SeleniumHexafluoride));
            mFluorideGases.add(MaterialUtils.legacyGtppFluidOf(Materials.ThoriumTetrafluoride));
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
        FluidStack Li2BeF4 = MaterialUtils.legacyGtppFluid(Materials.LithiumTetrafluoroberyllateLFTB, 200);

        // LiFBeF2ThF4UF4 - T3
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.LFTRFuel3, 100), Li2BeF4)
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.UraniumDepletedMoltenSaltUSalt, 100),
                MaterialUtils.legacyGtppFluid(Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, 200),
                MaterialUtils.legacyGtppFluid(Materials.UraniumHexafluoride, 20),
                MaterialUtils.legacyGtppFluid(Materials.UraniumHexafluoride, 10))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 32768 * 4)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // LiFBeF2ZrF4UF4 - T2
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.LFTRFuel2, 100), Li2BeF4)
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.UraniumDepletedMoltenSaltUSalt, 50),
                MaterialUtils.legacyGtppFluid(Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, 100),
                MaterialUtils.legacyGtppFluid(Materials.UraniumHexafluoride, 10),
                MaterialUtils.legacyGtppFluid(Materials.UraniumHexafluoride, 5))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 8192 * 4)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // LiFBeF2ZrF4U235 - T1
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.LFTRFuel1, 100), Li2BeF4)
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.UraniumDepletedMoltenSaltUSalt, 25),
                MaterialUtils.legacyGtppFluid(Materials.ThoriumDepletedMoltenSaltTSalt, 50),
                MaterialUtils.legacyGtppFluid(Materials.UraniumHexafluoride, 4),
                MaterialUtils.legacyGtppFluid(Materials.UraniumHexafluoride, 2))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(0)
            .metadata(LFTR_OUTPUT_POWER, 8192)
            .addTo(liquidFluorineThoriumReactorRecipes);

        // Sparging Recipes
        // For no randomization, set metadata to 0 with key SPARGE_MAX_BYPRODUCT.
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1_000)),
                MaterialUtils.legacyGtppFluid(Materials.UraniumDepletedMoltenSaltUSalt, 50))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.HeliumSpargedUSalt, 50),
                MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, (int) (0)),
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
                MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialUtils.legacyGtppFluid(Materials.ThoriumDepletedMoltenSaltTSalt, 50))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.FluorineSpargedTSalt, 50),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (0)),
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
                MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (100)),
                MaterialUtils.legacyGtppFluid(Materials.ThoriumBerylliumDepletedMoltenSaltTBSalt, 50))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.FluorineSpargedTBSalt, 50),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (0)),
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
