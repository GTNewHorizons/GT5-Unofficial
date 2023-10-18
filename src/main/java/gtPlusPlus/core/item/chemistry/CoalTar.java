package gtPlusPlus.core.item.chemistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;

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

    private static void recipeEthylBenzineFuelsIntoHeavyFuel() {
        CORE.RA.addChemicalRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("cellFuel", 9),
                ItemUtils.getItemStackOfAmountFromOreDict("cellEthylbenzene", 2),
                null,
                FluidUtils.getFluidStack("nitrofuel", 7500),
                ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 11),
                100,
                1000);
        CORE.RA.addChemicalRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("cellBioDiesel", 9),
                ItemUtils.getItemStackOfAmountFromOreDict("cellEthylbenzene", 2),
                null,
                FluidUtils.getFluidStack("nitrofuel", 3000),
                ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 11),
                300,
                1000);
    }

    public static void recipeCreateEthylene() {

        FluidStack bioEth1 = FluidUtils.getFluidStack("fluid.bioethanol", 1000);
        FluidStack bioEth2 = FluidUtils.getFluidStack("bioethanol", 1000);

        // C2H6O = C2H4 + H2O
        if (bioEth1 != null) {
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { CI.getNumberedBioCircuit(17),
                            ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1) },
                    bioEth1,
                    FluidUtils.getWater(1000),
                    new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 1) },
                    new int[] { 10000 },
                    120 * 20,
                    80);
        }

        if (bioEth2 != null) {
            CORE.RA.addDehydratorRecipe(
                    new ItemStack[] { CI.getNumberedBioCircuit(18),
                            ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1) },
                    bioEth2,
                    FluidUtils.getWater(1000),
                    new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 1) },
                    new int[] { 10000 },
                    120 * 20,
                    80);
        }
    }

    public static void recipeCreateBenzene() {
        // C7H8 + 2H = CH4 + C6H6
        CORE.RA.addDehydratorRecipe(
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellToluene", 1),
                        ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 2) },
                null,
                null,
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellMethane", 1),
                        ItemUtils.getItemStackOfAmountFromOreDict("cellBenzene", 1), Materials.Empty.getCells(1) },
                new int[] { 10000, 10000, 10000 },
                20 * 10,
                90);
    }

    public static void recipeCreateEthylbenzene() {
        // C2H4 + C6H6 = C8H10
        GT_Values.RA.addChemicalRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 2),
                ItemUtils.getGregtechCircuit(1),
                FluidUtils.getFluidStack("benzene", 2000),
                FluidUtils.getFluidStack("fluid.ethylbenzene", 2000),
                ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
                300);

        GT_Values.RA.addChemicalRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("cellBenzene", 2),
                ItemUtils.getGregtechCircuit(1),
                FluidUtils.getFluidStack("ethylene", 2000),
                FluidUtils.getFluidStack("fluid.ethylbenzene", 2000),
                ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
                300);
    }

    public static void recipeCoalToCoalTar() {
        // Charcoal
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 32L),
                8,
                GT_Values.NF,
                ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDirt", 2),
                FluidUtils.getFluidStack("fluid.coaltar", 800),
                15,
                120);
        // Lignite
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 16L),
                8,
                GT_Values.NF,
                ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2),
                FluidUtils.getFluidStack("fluid.coaltar", 800),
                45,
                60);

        // Coal
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12L),
                8,
                GT_Values.NF,
                ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2),
                FluidUtils.getFluidStack("fluid.coaltar", 2200),
                30,
                120);

        // Coke
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(
                ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 8),
                8,
                GT_Values.NF,
                ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 3),
                FluidUtils.getFluidStack("fluid.coaltar", 3400),
                15,
                240);
    }

    private static void recipeCoalTarToCoalTarOil() {
        // v - Distill (60% Tar oil/15% Naphtha/20% Ethylbenzene/5% Anthracene)
        // Create Coal Tar Oil
        GT_Values.RA.addDistilleryRecipe(
                CI.getNumberedCircuit(1), // Circuit
                FluidUtils.getFluidStack("fluid.coaltar", 1000), // aInput
                FluidUtils.getFluidStack("fluid.coaltaroil", 600), // aOutput
                600, // aDuration
                64, // aEUt
                false // Hidden?
        );
        GT_Values.RA.addDistilleryRecipe(
                CI.getNumberedCircuit(2), // Circuit
                FluidUtils.getFluidStack("fluid.coaltar", 1000), // aInput
                FluidUtils.getFluidStack("liquid_naphtha", 150), // aOutput
                300, // aDuration
                30, // aEUt
                false // Hidden?
        );
        GT_Values.RA.addDistilleryRecipe(
                CI.getNumberedCircuit(3), // Circuit
                FluidUtils.getFluidStack("fluid.coaltar", 1000), // aInput
                FluidUtils.getFluidStack("fluid.ethylbenzene", 200), // aOutput
                450, // aDuration
                86, // aEUt
                false // Hidden?
        );
        GT_Values.RA.addDistilleryRecipe(
                CI.getNumberedCircuit(4), // Circuit
                FluidUtils.getFluidStack("fluid.coaltar", 1000), // aInput
                FluidUtils.getFluidStack("fluid.anthracene", 50), // aOutput
                900, // aDuration
                30, // aEUt
                false // Hidden?
        );
        GT_Values.RA.addDistilleryRecipe(
                CI.getNumberedCircuit(5), // Circuit
                FluidUtils.getFluidStack("fluid.coaltar", 1500), // aInput
                FluidUtils.getFluidStack("fluid.kerosene", 600), // aOutput
                300, // aDuration
                64, // aEUt
                false // Hidden?
        );

        GT_Values.RA.addDistillationTowerRecipe(
                FluidUtils.getFluidStack("fluid.coaltar", 1200),
                new FluidStack[] { FluidUtils.getFluidStack("fluid.coaltaroil", 500), // aOutput
                        FluidUtils.getFluidStack("liquid_naphtha", 100), // aOutput
                        FluidUtils.getFluidStack("fluid.ethylbenzene", 150), // aOutput
                        FluidUtils.getFluidStack("fluid.anthracene", 50), // aOutput
                        FluidUtils.getFluidStack("fluid.kerosene", 400), // aOutput
                },
                null,
                900,
                60);
    }

    private static void recipeCoalTarOilToSulfuricOilToNaphthalene() {
        // SulfuricCoalTarOil
        GT_Values.RA.addChemicalRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTarOil", 8),
                ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricAcid", 8),
                null,
                null,
                ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricCoalTarOil", 16),
                20 * 16);
        GT_Values.RA.addDistilleryRecipe(
                CI.getNumberedCircuit(6), // Circuit
                FluidUtils.getFluidStack("fluid.sulfuriccoaltaroil", 1000), // aInput
                FluidUtils.getFluidStack("fluid.naphthalene", 1000), // aOutput
                1200, // aDuration
                30, // aEUt
                false // Hidden?
        );
    }

    private static void recipeNaphthaleneToPhthalicAcid() {
        // SulfuricCoalTarOil
        GT_Values.RA.addChemicalRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("cellNaphthalene", 2),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithium", 5),
                null,
                Materials.PhthalicAcid.getFluid(2500),
                ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
                20 * 16);
    }

    private static void recipePhthalicAcidToPhthalicAnhydride() {
        CORE.RA.addDehydratorRecipe(
                new ItemStack[] { CI.getNumberedBioCircuit(15) },
                Materials.PhthalicAcid.getFluid(1000),
                null,
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustPhthalicAnhydride", 15) },
                new int[] { 10000 },
                60 * 20,
                120);
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

        recipeEthylBenzineFuelsIntoHeavyFuel();

        // Burn the coal gas!
        GT_Values.RA.addFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalGas", 1), null, 96, 1);
        CORE.RA.addSemifluidFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricCoalTarOil", 1), 64);
        CORE.RA.addSemifluidFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTarOil", 1), 32);
        CORE.RA.addSemifluidFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTar", 1), 16);

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
            Ethylene = FluidUtils.generateFluidNonMolten(
                    "ethylene",
                    "Ethylene",
                    -103,
                    new short[] { 255, 255, 255, 100 },
                    null,
                    null);
        } else {
            Ethylene = FluidUtils.getWildcardFluidStack("ethylene", 1).getFluid();
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
        Anthracene = FluidUtils.generateFluidNonMolten(
                "Anthracene",
                "Anthracene",
                340,
                new short[] { 255, 255, 255, 100 },
                null,
                null);
        // Toluene
        if (!FluidUtils.doesFluidExist("liquid_toluene")) {
            Toluene = FluidUtils.generateFluidNonMolten(
                    "liquid_toluene",
                    "Toluene",
                    -95,
                    new short[] { 140, 70, 20, 100 },
                    null,
                    null);
        } else {
            Toluene = FluidUtils.getWildcardFluidStack("liquid_toluene", 1).getFluid();
        }

        // Create Coal Tar
        Coal_Tar = FluidUtils
                .generateFluidNonMolten("CoalTar", "Coal Tar", 450, new short[] { 32, 32, 32, 100 }, null, null);
        // v - Distill (60% Tar oil/15% Naphtha/20% Ethylbenzene/5% Anthracene)
        // Create Coal Tar Oil
        Coal_Tar_Oil = FluidUtils.generateFluidNonMolten(
                "CoalTarOil",
                "Coal Tar Oil",
                240,
                new short[] { 240, 240, 150, 100 },
                null,
                null);
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
        Naphthalene = FluidUtils.generateFluidNonMolten(
                "Naphthalene",
                "Naphthalene",
                115,
                new short[] { 210, 185, 135, 100 },
                null,
                null);
    }
}
