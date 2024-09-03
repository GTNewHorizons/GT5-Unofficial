package gtPlusPlus.core.item.chemistry;

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

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class CoalTar extends ItemPackage {

    public static Fluid Coal_Gas;
    public static Fluid Coal_Oil;
    public static Fluid Ethylene;
    public static Fluid Ethylbenzene;
    public static Fluid Anthracene;
    public static Fluid Toluene;
    public static Fluid Coal_Tar;
    public static Fluid Coal_Tar_Oil;
    public static Fluid Sulfuric_Coal_Tar_Oil;
    public static Fluid Naphthalene;

    public static void recipeCreateEthylene() {

        FluidStack bioEth1 = FluidUtils.getFluidStack("fluid.bioethanol", 1000);
        FluidStack bioEth2 = FluidUtils.getFluidStack("bioethanol", 1000);

        // C2H6O = C2H4 + H2O
        if (bioEth1 != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(CI.getNumberedBioCircuit(17), ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 1))
                .fluidInputs(bioEth1)
                .fluidOutputs(FluidUtils.getWater(1000))
                .eut(80)
                .duration(2 * MINUTES)
                .addTo(chemicalDehydratorRecipes);
        }

        if (bioEth2 != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(CI.getNumberedBioCircuit(18), ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 1))
                .fluidInputs(bioEth2)
                .fluidOutputs(FluidUtils.getWater(1000))
                .eut(80)
                .duration(2 * MINUTES)
                .addTo(chemicalDehydratorRecipes);
        }
    }

    public static void recipeCreateBenzene() {
        // C7H8 + 2H = CH4 + C6H6
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Toluene.getCells(1))
            .itemOutputs(Materials.Benzene.getCells(1))
            .fluidInputs(Materials.Hydrogen.getGas(2000))
            .fluidOutputs(Materials.Methane.getGas(1000))
            .duration(10 * SECONDS)
            .eut(90)
            .noOptimize()
            .addTo(chemicalDehydratorRecipes);
    }

    public static void recipeCreateEthylbenzene() {
        // C2H4 + C6H6 = C8H10
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 2), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2))
            .fluidInputs(FluidUtils.getFluidStack("benzene", 2000))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.ethylbenzene", 2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellBenzene", 2), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2))
            .fluidInputs(FluidUtils.getFluidStack("ethylene", 2000))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.ethylbenzene", 2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    public static void recipeCoalToCoalTar() {
        // Charcoal
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(8),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 32L))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDirt", 2))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.coaltar", 800))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Lignite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(8),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 16L))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.coaltar", 800))
            .duration(54 * SECONDS)
            .eut(60)
            .addTo(pyrolyseRecipes);

        // Coal
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(8),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12L))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.coaltar", 2200))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(pyrolyseRecipes);

        // Coke
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(8), ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 8))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 3))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.coaltar", 3400))
            .duration(18 * SECONDS)
            .eut(240)
            .addTo(pyrolyseRecipes);
    }

    private static void recipeCoalTarToCoalTarOil() {
        // v - Distill (60% Tar oil/15% Naphtha/20% Ethylbenzene/5% Anthracene) +60% Kerosene
        // Create Coal Tar Oil

        FluidStack[] distOutputs = new FluidStack[] { FluidUtils.getFluidStack("fluid.coaltaroil", 600),
            FluidUtils.getFluidStack("liquid_naphtha", 150), FluidUtils.getFluidStack("fluid.ethylbenzene", 200),
            FluidUtils.getFluidStack("fluid.anthracene", 50), FluidUtils.getFluidStack("fluid.kerosene", 600) };
        for (int i = 0; i < distOutputs.length; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(i + 1))
                .fluidInputs(FluidUtils.getFluidStack("fluid.coaltar", 1000))
                .fluidOutputs(distOutputs[i])
                .duration(30 * SECONDS)
                .eut(64)
                .addTo(distilleryRecipes);
        }
        GTValues.RA.stdBuilder()
            .fluidInputs(FluidUtils.getFluidStack("fluid.coaltar", 1000))
            .fluidOutputs(distOutputs)
            .duration(15 * SECONDS)
            .eut(256)
            .addTo(distillationTowerRecipes);
    }

    private static void recipeCoalTarOilToSulfuricOilToNaphthalene() {
        // SulfuricCoalTarOil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTarOil", 8),
                ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricAcid", 8))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricCoalTarOil", 16))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6))
            .fluidInputs(FluidUtils.getFluidStack("fluid.sulfuriccoaltaroil", 1000))
            .fluidOutputs(FluidUtils.getFluidStack("fluid.naphthalene", 1000))
            .eut(TierEU.RECIPE_LV)
            .duration(60 * SECONDS)
            .addTo(distilleryRecipes);
    }

    private static void recipeNaphthaleneToPhthalicAcid() {
        // SulfuricCoalTarOil
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Lithium.getDust(5))
            .fluidInputs(FluidUtils.getFluidStack(Naphthalene, 2000))
            .fluidOutputs(Materials.PhthalicAcid.getFluid(2500))
            .eut(30)
            .duration(16 * SECONDS)
            .noOptimize()
            .addTo(UniversalChemical);
    }

    private static void recipePhthalicAcidToPhthalicAnhydride() {
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedBioCircuit(15))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustPhthalicAnhydride", 15))
            .fluidInputs(Materials.PhthalicAcid.getFluid(1000))
            .eut(TierEU.RECIPE_MV)
            .duration(60 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }

    @Override
    public String errorMessage() {
        return "Bad Coal Science!";
    }

    @Override
    public boolean generateRecipes() {
        recipeCreateEthylene();
        recipeCreateBenzene();
        recipeCreateEthylbenzene();

        recipeCoalToCoalTar();
        recipeCoalTarToCoalTarOil();
        recipeCoalTarOilToSulfuricOilToNaphthalene();
        recipeNaphthaleneToPhthalicAcid();
        recipePhthalicAcidToPhthalicAnhydride();

        // Burn the coal gas!
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalGas", 1))
            .metadata(FUEL_VALUE, 96)
            .metadata(FUEL_TYPE, GTRecipeConstants.FuelType.GasTurbine.ordinal())
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                FluidContainerRegistry
                    .getFluidForFilledItem(ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricCoalTarOil", 1)))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 64)
            .addTo(semiFluidFuels);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                FluidContainerRegistry
                    .getFluidForFilledItem(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTarOil", 1)))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 32)
            .addTo(semiFluidFuels);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                FluidContainerRegistry
                    .getFluidForFilledItem(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTar", 1)))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 16)
            .addTo(semiFluidFuels);
        return true;
    }

    @Override
    public void items() {
        // v - Dehydrate at 180C+
        // Create Phthalic Anhydride
        ItemUtils.generateSpecialUseDusts(
            "PhthalicAnhydride",
            "Phthalic Anhydride",
            "C6H4(CO)2O",
            Utils.rgbtoHexValue(175, 175, 175));

        // Lithium Hydroperoxide - LiOH + H2O2 → LiOOH + 2 H2O
        ItemUtils.generateSpecialUseDusts(
            "LithiumHydroperoxide",
            "Lithium Hydroperoxide",
            "HLiO2",
            Utils.rgbtoHexValue(125, 125, 125));
        // v - Dehydrate
        // Lithium Peroxide - 2 LiOOH → Li2O2 + H2O2 + 2 H2O
    }

    @Override
    public void blocks() {}

    @Override
    public void fluids() {

        // Create Coal Gas
        Coal_Gas = FluidUtils
            .generateFluidNonMolten("CoalGas", "Coal Gas", 500, new short[] { 48, 48, 48, 100 }, null, null);
        // Ethanol
        // v - Dehydrate cells to remove water

        // Create Ethylene
        if (!FluidUtils.doesFluidExist("ethylene")) {
            Ethylene = FluidUtils
                .generateFluidNonMolten("ethylene", "Ethylene", -103, new short[] { 255, 255, 255, 100 }, null, null);
        } else {
            Ethylene = FluidUtils.getWildcardFluidStack("ethylene", 1)
                .getFluid();
        }

        // Create Ethylbenzene - Ethylbenzene is produced in on a large scale by combining benzene and ethylene in an
        // acid-catalyzed chemical reaction
        // Use Chemical Reactor
        Ethylbenzene = FluidUtils.generateFluidNonMolten(
            "Ethylbenzene",
            "Ethylbenzene",
            136,
            new short[] { 255, 255, 255, 100 },
            null,
            null);
        // Create Anthracene
        Anthracene = FluidUtils
            .generateFluidNonMolten("Anthracene", "Anthracene", 340, new short[] { 255, 255, 255, 100 }, null, null);
        // Toluene
        if (!FluidUtils.doesFluidExist("liquid_toluene")) {
            Toluene = FluidUtils
                .generateFluidNonMolten("liquid_toluene", "Toluene", -95, new short[] { 140, 70, 20, 100 }, null, null);
        } else {
            Toluene = FluidUtils.getWildcardFluidStack("liquid_toluene", 1)
                .getFluid();
        }

        // Create Coal Tar
        Coal_Tar = FluidUtils
            .generateFluidNonMolten("CoalTar", "Coal Tar", 450, new short[] { 32, 32, 32, 100 }, null, null);
        // v - Distill (60% Tar oil/15% Naphtha/20% Ethylbenzene/5% Anthracene)
        // Create Coal Tar Oil
        Coal_Tar_Oil = FluidUtils
            .generateFluidNonMolten("CoalTarOil", "Coal Tar Oil", 240, new short[] { 240, 240, 150, 100 }, null, null);
        // v - Wash With Sulfuric Acid
        // Create Sulfuric Coal Tar Oil
        Sulfuric_Coal_Tar_Oil = FluidUtils.generateFluidNonMolten(
            "SulfuricCoalTarOil",
            "Sulfuric Coal Tar Oil",
            240,
            new short[] { 250, 170, 12, 100 },
            null,
            null);
        // v - Distill (No loss, just time consuming)
        // Create Naphthalene
        Naphthalene = FluidUtils
            .generateFluidNonMolten("Naphthalene", "Naphthalene", 115, new short[] { 210, 185, 135, 100 }, null, null);
    }
}
