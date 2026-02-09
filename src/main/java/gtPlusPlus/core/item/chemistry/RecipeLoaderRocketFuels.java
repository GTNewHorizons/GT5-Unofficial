package gtPlusPlus.core.item.chemistry;

import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoaderRocketFuels {

    public static void generate() {
        createKerosene();
        createRP1();
        createNitrogenTetroxide();
        createHydrazine();
        createMonomethylhydrazine();

        createLOH();

        createHydratedAmmoniumNitrateSlurry();
        createAmmoniumNitrateDust();
        createFormaldehyde();
        createFormaldehydeCatalyst();
        createUnsymmetricalDimethylhydrazine();

        createRocketFuels();
        addRocketFuelsToMap();
    }

    private static void createKerosene() {
        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(Materials.Diesel.getFluid(250))
            .fluidOutputs(new FluidStack(GTPPFluids.Kerosene, 150))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);
    }

    private static void createRP1() {
        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(new FluidStack(GTPPFluids.Kerosene, 20))
            .fluidOutputs(new FluidStack(GTPPFluids.RP1, 15))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distilleryRecipes);
    }

    private static void createNitrogenTetroxide() {
        // 2HNO3 + Cu = N2O4 + H2O + CuO
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(1), GregtechItemList.OrangeMetalCatalyst.get(0))
            .itemOutputs(Materials.CupricOxide.getDust(2))
            .fluidInputs(Materials.NitricAcid.getFluid(2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.NitrogenTetroxide, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void createHydrazine() {
        // H2O2 + 2NH3 = N2H4 + 2H2O
        GTValues.RA.stdBuilder()
            .circuit(21)
            .fluidInputs(new FluidStack(GTPPFluids.HydrogenPeroxide, 1_000), Materials.Ammonia.getGas(2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Hydrazine, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void createMonomethylhydrazine() {
        // C + 2H + N2H4 = CH6N2
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(1))
            .circuit(21)
            .fluidInputs(Materials.Hydrogen.getGas(2_000), new FluidStack(GTPPFluids.Hydrazine, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Monomethylhydrazine, 1_000))
            .duration(48 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void createLOH() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Hydrogen.getCells(1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellLiquidHydrogen", 1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);
    }

    private static void createHydratedAmmoniumNitrateSlurry() {
        // NH3 + HNO3 = NH4NO3
        GTValues.RA.stdBuilder()
            .circuit(21)
            .fluidInputs(Materials.Ammonia.getGas(4_000), Materials.NitricAcid.getFluid(4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.HydratedAmmoniumNitrateSlurry, 36 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void createAmmoniumNitrateDust() {
        GTValues.RA.stdBuilder()
            .circuit(8)
            .itemOutputs(GregtechItemList.AmmoniumNitrateDust.get(8))
            .fluidInputs(new FluidStack(GTPPFluids.HydratedAmmoniumNitrateSlurry, 8 * INGOTS))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .eut(TierEU.RECIPE_HV)
            .duration(1 * MINUTES + 30 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void createFormaldehyde() {
        // O + CH4O = CH2O + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.FormaldehydeCatalyst.get(0))
            .circuit(21)
            .fluidInputs(Materials.Oxygen.getGas(32_000), Materials.Methanol.getFluid(32_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Formaldehyde, 32_000))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void createFormaldehydeCatalyst() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1))
            .circuit(18)
            .itemOutputs(GregtechItemList.FormaldehydeCatalystDust.get(4))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
    }

    private static void createUnsymmetricalDimethylhydrazine() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.FormaldehydeCatalyst.get(0))
            .circuit(21)
            .fluidInputs(
                new FluidStack(GTPPFluids.Hydrazine, 2_000),
                new FluidStack(GTPPFluids.Formaldehyde, 2_000),
                Materials.Hydrogen.getGas(4_000))
            .fluidOutputs(Materials.Dimethylhydrazine.getFluid(2_000), Materials.Water.getFluid(2_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void addRocketFuelsToMap() {
        addFuelRecipe(GTPPFluids.RP1RocketFuel, 512);
        addFuelRecipe(GTPPFluids.DenseHydrazineFuelMixture, 1024);
        addFuelRecipe(GTPPFluids.CN3H7O3RocketFuel, 2048);
        addFuelRecipe(GTPPFluids.H8N4C2O4RocketFuel, 4196);
    }

    private static void addFuelRecipe(Fluid fluid, int fuelValue) {
        GTPPRecipeMaps.rocketFuels.add(
            new GTRecipe(
                GTValues.emptyItemStackArray,
                GTValues.emptyItemStackArray,
                null,
                null,
                null,
                null,
                null,
                new FluidStack[] { new FluidStack(fluid, 1000) },
                GTValues.emptyFluidStackArray,
                0,
                0,
                fuelValue));
    }

    private static void createRocketFuels() {
        // RP1 Rocket Fuel
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.LiquidOxygen.getGas(2_000), new FluidStack(GTPPFluids.RP1, 500))
            .fluidOutputs(new FluidStack(GTPPFluids.RP1RocketFuel, 1_500))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Dense Hydrazine Fuel Mixture
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(new FluidStack(GTPPFluids.Hydrazine, 4_000), Materials.Methanol.getFluid(6_000))
            .fluidOutputs(new FluidStack(GTPPFluids.DenseHydrazineFuelMixture, 10_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

        // CN3H7O3 Rocket Fuel
        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(new FluidStack(GTPPFluids.Monomethylhydrazine, 2_000), Materials.NitricAcid.getFluid(1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.CN3H7O3RocketFuel, 2_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        // H8N4C2O4 Rocket Fuel
        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                Materials.Dimethylhydrazine.getFluid(2_000),
                new FluidStack(GTPPFluids.NitrogenTetroxide, 2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.H8N4C2O4RocketFuel, 5_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 6)
            .addTo(chemicalPlantRecipes);
    }
}
