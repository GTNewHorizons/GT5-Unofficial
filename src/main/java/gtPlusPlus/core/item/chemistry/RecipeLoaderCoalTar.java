package gtPlusPlus.core.item.chemistry;

import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.pyrolyseRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.semiFluidFuels;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoaderCoalTar {

    public static void generate() {
        recipeCreateEthylene();
        recipeCreateBenzene();
        recipeCreateEthylbenzene();

        recipeCoalToCoalTar();
        recipeCoalTarToCoalTarOil();
        recipeCoalTarOilToSulfuricOilToNaphthalene();
        recipeNaphthaleneToPhthalicAcid();
        recipePhthalicAcidToPhthalicAnhydride();
        generateFuelRecipes();
    }

    private static void recipeCreateEthylene() {

        FluidStack bioEth1 = FluidRegistry.getFluidStack("fluid.bioethanol", 1_000);
        FluidStack bioEth2 = Materials.Ethanol.getFluid(1_000);

        // C2H6O = C2H4 + H2O
        if (bioEth1 != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Cell_Empty.get(1))
                .circuit(17)
                .itemOutputs(Materials.Ethylene.getCells(1))
                .fluidInputs(bioEth1)
                .fluidOutputs(Materials.Water.getFluid(1_000))
                .eut(80)
                .duration(2 * MINUTES)
                .addTo(chemicalDehydratorRecipes);
        }

        if (bioEth2 != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Cell_Empty.get(1))
                .itemOutputs(Materials.Ethylene.getCells(1))
                .fluidInputs(bioEth2)
                .fluidOutputs(Materials.Water.getFluid(1_000))
                .eut(80)
                .duration(2 * MINUTES)
                .addTo(chemicalDehydratorRecipes);
        }
    }

    private static void recipeCreateBenzene() {
        // C7H8 + 2H = CH4 + C6H6
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Toluene.getCells(1))
            .itemOutputs(Materials.Benzene.getCells(1))
            .fluidInputs(Materials.Hydrogen.getGas(2_000))
            .fluidOutputs(Materials.Methane.getGas(1_000))
            .duration(10 * SECONDS)
            .eut(90)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void recipeCreateEthylbenzene() {
        // C2H4 + C6H6 = C8H10
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ethylene.getCells(2))
            .circuit(3)
            .itemOutputs(ItemList.Cell_Empty.get(2))
            .fluidInputs(Materials.Benzene.getFluid(2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Ethylbenzene, 2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Benzene.getCells(2))
            .circuit(3)
            .itemOutputs(ItemList.Cell_Empty.get(2))
            .fluidInputs(Materials.Ethylene.getGas(2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Ethylbenzene, 2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);
    }

    private static void recipeCoalToCoalTar() {
        // Charcoal
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 32))
            .circuit(8)
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDirt", 2))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTar, 800))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Cactus Charcoal
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CactusCharcoal.get(32))
            .circuit(8)
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDirt", 2))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTar, 800))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Sugar Charcoal
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.SugarCharcoal.get(32))
            .circuit(8)
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDirt", 2))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTar, 800))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Cactus coke
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CactusCoke.get(16))
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Ash, 2))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTar, 1_600))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Sugar coke
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.SugarCoke.get(16))
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Ash, 2))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTar, 1_600))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Lignite
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 16))
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.AshDark, 2))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTar, 800))
            .duration(54 * SECONDS)
            .eut(60)
            .addTo(pyrolyseRecipes);

        // Coal
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12))
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.AshDark, 2))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTar, 2_200))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Coke
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get("fuelCoke", 8))
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Ash, 3))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTar, 3_400))
            .duration(18 * SECONDS)
            .eut(240)
            .addTo(pyrolyseRecipes);
    }

    private static void recipeCoalTarToCoalTarOil() {
        // v - Distill (60% Tar oil/15% Naphtha/20% Ethylbenzene/5% Anthracene) +60% Kerosene

        // Distillery
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(new FluidStack(GTPPFluids.CoalTar, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.CoalTarOil, 600))
            .duration(30 * SECONDS)
            .eut(64)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(new FluidStack(GTPPFluids.CoalTar, 1_000))
            .fluidOutputs(Materials.Naphtha.getFluid(150))
            .duration(30 * SECONDS)
            .eut(64)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(new FluidStack(GTPPFluids.CoalTar, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Ethylbenzene, 200))
            .duration(30 * SECONDS)
            .eut(64)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(new FluidStack(GTPPFluids.CoalTar, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Anthracene, 50))
            .duration(30 * SECONDS)
            .eut(64)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .circuit(5)
            .fluidInputs(new FluidStack(GTPPFluids.CoalTar, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Kerosene, 600))
            .duration(30 * SECONDS)
            .eut(64)
            .addTo(distilleryRecipes);

        // Distillation Tower
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.CoalTar, 1_000))
            .fluidOutputs(
                new FluidStack(GTPPFluids.CoalTarOil, 600),
                Materials.Naphtha.getFluid(150),
                new FluidStack(GTPPFluids.Ethylbenzene, 200),
                new FluidStack(GTPPFluids.Anthracene, 50),
                new FluidStack(GTPPFluids.Kerosene, 600))
            .duration(15 * SECONDS)
            .eut(256)
            .addTo(distillationTowerRecipes);
    }

    private static void recipeCoalTarOilToSulfuricOilToNaphthalene() {
        // SulfuricCoalTarOil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTarOil", 8),
                Materials.SulfuricAcid.getCells(8))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricCoalTarOil", 16))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(new FluidStack(GTPPFluids.SulfuricCoalTarOil, 20))
            .fluidOutputs(new FluidStack(GTPPFluids.Naphthalene, 20))
            .eut(TierEU.RECIPE_LV)
            .duration(1 * SECONDS)
            .addTo(distilleryRecipes);
    }

    private static void recipeNaphthaleneToPhthalicAcid() {
        // SulfuricCoalTarOil
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Lithium.getDust(5))
            .fluidInputs(new FluidStack(GTPPFluids.Naphthalene, 2_000))
            .fluidOutputs(Materials.PhthalicAcid.getFluid(2_500))
            .eut(30)
            .duration(16 * SECONDS)
            .addTo(UniversalChemical);
    }

    private static void recipePhthalicAcidToPhthalicAnhydride() {
        GTValues.RA.stdBuilder()
            .itemOutputs(GregtechItemList.PhthalicAnhydrideDust.get(15))
            .fluidInputs(Materials.PhthalicAcid.getFluid(1_000))
            .eut(TierEU.RECIPE_MV)
            .duration(60 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void generateFuelRecipes() {
        // Burn the coal gas!
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalGas", 1))
            .metadata(FUEL_VALUE, 96)
            .metadata(FUEL_TYPE, GTRecipeConstants.FuelType.GasTurbine.ordinal())
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.SulfuricCoalTarOil, 1_000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 64)
            .addTo(semiFluidFuels);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.CoalTarOil, 1_000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 32)
            .addTo(semiFluidFuels);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.CoalTar, 1_000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 16)
            .addTo(semiFluidFuels);
    }
}
