package gtPlusPlus.core.item.chemistry;

import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class RocketFuels extends ItemPackage {

    public static HashSet<String> mValidRocketFuelNames = new HashSet<>();
    public static HashMap<Integer, Fluid> mValidRocketFuels = new HashMap<>();

    public static Fluid Oil_Heavy;
    public static Fluid Diesel;
    public static Fluid Kerosene;
    public static Fluid RP1;
    public static Fluid Nitrogen_Tetroxide;
    public static Fluid Hydrazine;
    public static Fluid Monomethylhydrazine;
    public static Fluid Unsymmetrical_Dimethylhydrazine;
    public static Fluid Nitrous_Oxide;
    public static Fluid Hydrated_Ammonium_Nitrate_Slurry;
    public static Fluid Liquid_Oxygen;
    public static Fluid Liquid_Hydrogen;
    public static Fluid Formaldehyde;

    // Rocket Fuel Mixes
    public static Fluid Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide;
    public static Fluid RP1_Plus_Liquid_Oxygen;
    public static Fluid Dense_Hydrazine_Mix;
    public static Fluid Monomethylhydrazine_Plus_Nitric_Acid;

    public static Item Ammonium_Nitrate_Dust;
    public static Item Formaldehyde_Catalyst_Dust;
    public static ItemStack Formaldehyde_Catalyst_Stack;

    public RocketFuels() {
        super(true);
    }

    public static void createKerosene() {

        FluidStack fuelA = FluidUtils.getFluidStack("diesel", 3000);
        FluidStack fuelB = FluidUtils.getFluidStack("fuel", 3000);

        if (fuelA != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(23))
                .fluidInputs(fuelA)
                .fluidOutputs(FluidUtils.getFluidStack(Kerosene, 1800))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(distilleryRecipes);
        }
        if (fuelA == null && fuelB != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(23))
                .fluidInputs(fuelB)
                .fluidOutputs(FluidUtils.getFluidStack(Kerosene, 1800))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(distilleryRecipes);
        }
    }

    public static void createRP1() {
        FluidStack fuelA = FluidUtils.getFluidStack(Kerosene, 1000);
        if (fuelA != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(23))
                .fluidInputs(fuelA)
                .fluidOutputs(FluidUtils.getFluidStack(RP1, 750))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(distilleryRecipes);
        }
    }

    public static void createNitrogenTetroxide() {
        // 2HNO3 + Cu = N2O4 + H2O + CuO
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1),
                ItemUtils.getSimpleStack(GenericChem.mOrangeCatalyst, 0))
            .itemOutputs(Materials.CupricOxide.getDust(2))
            .fluidInputs(FluidUtils.getFluidStack("nitricacid", 2000))
            .fluidOutputs(FluidUtils.getFluidStack(Nitrogen_Tetroxide, 1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

    }

    public static void createHydrazine() {

        // H2O2 + 2NH3 = N2H4 + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(21))
            .fluidInputs(
                FluidUtils.getFluidStack("fluid.hydrogenperoxide", 1000),
                FluidUtils.getFluidStack("ammonia", 2000))
            .fluidOutputs(FluidUtils.getFluidStack(Hydrazine, 1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    public static void createMonomethylhydrazine() {

        // C + 2H + N2H4 = CH6N2
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(21), ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 1))
            .fluidInputs(FluidUtils.getFluidStack("hydrogen", 2000), FluidUtils.getFluidStack(Hydrazine, 1000))
            .fluidOutputs(FluidUtils.getFluidStack(Monomethylhydrazine, 1000))
            .duration(48 * SECONDS)
            .eut(240)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

    }

    private static void createLOH() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("cellLiquidHydrogen", 1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);
    }

    private static void createHydratedAmmoniumNitrateSlurry() {

        // NH3 + HNO3 = NH4NO3
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(21))
            .fluidInputs(FluidUtils.getFluidStack("ammonia", 4000), FluidUtils.getFluidStack("nitricacid", 4000))
            .fluidOutputs(FluidUtils.getFluidStack(Hydrated_Ammonium_Nitrate_Slurry, 5184))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void createAmmoniumNitrateDust() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(8))
            .itemOutputs(ItemUtils.getSimpleStack(Ammonium_Nitrate_Dust, 8))
            .fluidInputs(FluidUtils.getFluidStack(Hydrated_Ammonium_Nitrate_Slurry, 8 * 144))
            .fluidOutputs(FluidUtils.getWater(2000))
            .eut(TierEU.RECIPE_HV)
            .duration(1 * MINUTES + 30 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void createFormaldehyde() {

        // O + CH4O = CH2O + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(21),
                ItemUtils.getSimpleStack(GenericChem.mFormaldehydeCatalyst, 0))
            .fluidInputs(FluidUtils.getFluidStack("oxygen", 32000), FluidUtils.getFluidStack("methanol", 32000))
            .fluidOutputs(FluidUtils.getFluidStack(Formaldehyde, 32000))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void createFormaldehydeCatalyst() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 16L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1L),
                GTUtility.getIntegratedCircuit(18))
            .itemOutputs(ItemUtils.getSimpleStack(Formaldehyde_Catalyst_Dust, 4))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
    }

    private static void createUnsymmetricalDimethylhydrazine() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getNumberedAdvancedCircuit(21),
                ItemUtils.getSimpleStack(GenericChem.mFormaldehydeCatalyst, 0))
            .fluidInputs(
                FluidUtils.getFluidStack("fluid.hydrazine", 2000),
                FluidUtils.getFluidStack(Formaldehyde, 2000),
                FluidUtils.getFluidStack("hydrogen", 4000))
            .fluidOutputs(FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine, 2000), FluidUtils.getWater(2000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

    }

    private static void addRocketFuelsToMap() {
        HashMap<Integer, GTRecipe> mRocketFuels = new LinkedHashMap<>();
        mRocketFuels.put(
            0,
            new GTRecipe(
                true,
                new ItemStack[] {},
                new ItemStack[] {},
                null,
                new int[] {},
                new FluidStack[] { FluidUtils.getFluidStack(RP1_Plus_Liquid_Oxygen, 1000) },
                new FluidStack[] {},
                0,
                0,
                512)); // Fuel Value

        mRocketFuels.put(
            1,
            new GTRecipe(
                true,
                new ItemStack[] {},
                new ItemStack[] {},
                null,
                new int[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Dense_Hydrazine_Mix, 1000) },
                new FluidStack[] {},
                0,
                0,
                1024)); // Fuel Value

        mRocketFuels.put(
            2,
            new GTRecipe(
                true,
                new ItemStack[] {},
                new ItemStack[] {},
                null,
                new int[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Monomethylhydrazine_Plus_Nitric_Acid, 1000) },
                new FluidStack[] {},
                0,
                0,
                2048)); // Fuel Value

        mRocketFuels.put(
            3,
            new GTRecipe(
                true,
                new ItemStack[] {},
                new ItemStack[] {},
                null,
                new int[] {},
                new FluidStack[] {
                    FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide, 1000) },
                new FluidStack[] {},
                0,
                0,
                4196)); // Fuel Value

        // Add in default Diesel for the Buggy
        mValidRocketFuels.put(-1, Diesel);

        mValidRocketFuelNames.add(FluidRegistry.getFluidName(Diesel));
        for (int mID : mRocketFuels.keySet()) {
            GTRecipe aFuelRecipe = mRocketFuels.get(mID);
            if (aFuelRecipe != null) {
                mValidRocketFuelNames.add(FluidRegistry.getFluidName(aFuelRecipe.mFluidInputs[0].getFluid()));
                mValidRocketFuels.put(mID, aFuelRecipe.mFluidInputs[0].getFluid());
                GTPPRecipeMaps.rocketFuels.add(aFuelRecipe);
            }
        }
    }

    private static void createRocketFuels() {

        // RP1_Plus_Liquid_Oxygen
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(FluidUtils.getFluidStack(Liquid_Oxygen, 2000), FluidUtils.getFluidStack(RP1, 500))
            .fluidOutputs(FluidUtils.getFluidStack(RP1_Plus_Liquid_Oxygen, 1500))
            .duration(15 * SECONDS)
            .eut(240)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
        // Dense_Hydrazine_Mix
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(FluidUtils.getFluidStack(Hydrazine, 4000), FluidUtils.getFluidStack("methanol", 6000))
            .fluidOutputs(FluidUtils.getFluidStack(Dense_Hydrazine_Mix, 10000))
            .duration(30 * SECONDS)
            .eut(240)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
        // Monomethylhydrazine_Plus_Nitric_Acid
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
            .fluidInputs(
                FluidUtils.getFluidStack(Monomethylhydrazine, 2000),
                FluidUtils.getFluidStack("nitricacid", 1000))
            .fluidOutputs(FluidUtils.getFluidStack(Monomethylhydrazine_Plus_Nitric_Acid, 2000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);
        // Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .fluidInputs(
                FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine, 2000),
                FluidUtils.getFluidStack(Nitrogen_Tetroxide, 2000))
            .fluidOutputs(FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide, 5000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 6)
            .addTo(chemicalPlantRecipes);

    }

    @Override
    public String errorMessage() {
        return "Bad Rocket Fuel Science!";
    }

    @Override
    public boolean generateRecipes() {
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

        return true;
    }

    @Override
    public void items() {
        Formaldehyde_Catalyst_Dust = ItemUtils.generateSpecialUseDusts(
            "FormaldehydeCatalyst",
            "Formaldehyde Catalyst",
            "Fe16V1",
            Utils.rgbtoHexValue(25, 5, 25))[0];
        Formaldehyde_Catalyst_Stack = ItemUtils.getSimpleStack(Formaldehyde_Catalyst_Dust);
    }

    @Override
    public void blocks() {}

    @Override
    public void fluids() {

        // Register default fluids
        Diesel = MaterialUtils.getMaterial("Fuel", "Diesel")
            .getFluid(1)
            .getFluid();

        // 5.08 Compat
        if (!FluidUtils.doesFluidExist("liquid_heavy_oil")) {
            Oil_Heavy = FluidUtils
                .generateFluidNoPrefix("liquid_heavy_oil", "Heavy Oil", 200, new short[] { 10, 10, 10, 100 });
        } else {
            Oil_Heavy = MaterialUtils.getMaterial("OilHeavy", "Oil")
                .getFluid(1)
                .getFluid();
            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellOilHeavy", 1) == null) {
                new BaseItemComponent("OilHeavy", "Heavy Oil", new short[] { 10, 10, 10 });
            }
        }

        // Create Kerosene
        Kerosene = FluidUtils
            .generateFluidNonMolten("Kerosene", "Kerosene", 233, new short[] { 150, 40, 150, 100 }, null, null);
        CoalTar.Coal_Oil = Kerosene;

        // RP! Focket Fuel
        RP1 = FluidUtils.generateFluidNonMolten("RP1Fuel", "RP-1", 500, new short[] { 210, 50, 50, 100 }, null, null);

        // Create Nitrogen Tetroxide
        Nitrogen_Tetroxide = FluidUtils.generateFluidNonMolten(
            "NitrogenTetroxide",
            "Nitrogen Tetroxide",
            261,
            new short[] { 170, 170, 0, 100 },
            null,
            null);

        // Create Hydrazine
        Hydrazine = FluidUtils
            .generateFluidNonMolten("Hydrazine", "Hydrazine", 275, new short[] { 250, 250, 250, 100 }, null, null);

        // Create Monomethylhydrazine
        Monomethylhydrazine = FluidUtils.generateFluidNonMolten(
            "Monomethylhydrazine",
            "Monomethylhydrazine",
            221,
            new short[] { 125, 125, 125, 100 },
            null,
            null);

        // Create Anthracene
        Nitrous_Oxide = FluidUtils.generateFluidNonMolten(
            "NitrousOxide",
            "Nitrous Oxide",
            182,
            new short[] { 255, 255, 255, 100 },
            null,
            null);

        // Nos
        if (!FluidUtils.doesFluidExist("NitrousOxide")) {
            Nitrous_Oxide = FluidUtils
                .generateFluidNoPrefix("NitrousOxide", "Nitrous Oxide", 182, new short[] { 255, 255, 255, 100 });
        } else {
            Nitrous_Oxide = FluidUtils.getWildcardFluidStack("NitrousOxide", 1)
                .getFluid();
            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellNitrousOxide", 1) == null) {
                new BaseItemComponent("NitrousOxide", "Nitrous Oxide", new short[] { 10, 10, 175 });
            }
        }

        // Unsymmetrical_Dimethylhydrazine
        if (FluidUtils.getFluidStack("1,1dimethylhydrazine", 1) == null) {
            Unsymmetrical_Dimethylhydrazine = FluidUtils.generateFluidNonMolten(
                "UnsymmetricalDimethylhydrazine",
                "Unsymmetrical Dimethylhydrazine",
                216,
                new short[] { 70, 210, 20, 100 },
                null,
                null);
        } else {
            Unsymmetrical_Dimethylhydrazine = FluidUtils.getFluidStack("1,1dimethylhydrazine", 1000)
                .getFluid();
        }

        // Create Hydrated_Ammonium_Nitrate_Slurry
        Hydrated_Ammonium_Nitrate_Slurry = FluidUtils.generateFluidNonMolten(
            "AmmoniumNitrateSlurry",
            "Hydrated Ammonium Nitrate Slurry",
            450,
            new short[] { 150, 75, 150, 100 },
            null,
            null);

        // Lithium Hydroperoxide - LiOH + H2O2 → LiOOH + 2 H2O
        Ammonium_Nitrate_Dust = ItemUtils.generateSpecialUseDusts(
            "AmmoniumNitrate",
            "Ammonium Nitrate",
            "N2H4O3",
            Utils.rgbtoHexValue(150, 75, 150))[0];

        // Create Liquid_Oxygen
        if (FluidUtils.getFluidStack("LiquidOxygen", 1) == null
            && FluidUtils.getFluidStack("liquidoxygen", 1) == null) {
            Liquid_Oxygen = FluidUtils.generateFluidNonMolten(
                "LiquidOxygen",
                "Liquid Oxygen",
                54,
                new short[] { 75, 75, 220, 100 },
                null,
                null);
        } else {
            if (FluidUtils.getFluidStack("LiquidOxygen", 1) != null) {
                Liquid_Oxygen = FluidUtils.getFluidStack("LiquidOxygen", 1)
                    .getFluid();
            } else {
                Liquid_Oxygen = FluidUtils.getFluidStack("liquidoxygen", 1)
                    .getFluid();
            }
            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellLiquidOxygen", 1) == null) {
                new BaseItemComponent("LiquidOxygen", "Liquid Oxygen", new short[] { 10, 10, 175 });
            }
        }

        // Create Liquid_Hydrogen
        if (FluidUtils.getFluidStack("LiquidHydrogen", 1) == null
            && FluidUtils.getFluidStack("liquidhydrogen", 1) == null) {
            Liquid_Hydrogen = FluidUtils.generateFluidNonMolten(
                "LiquidHydrogen",
                "Liquid Hydrogen",
                14,
                new short[] { 75, 75, 220, 100 },
                null,
                null);
        } else {
            if (FluidUtils.getFluidStack("LiquidHydrogen", 1) != null) {
                Liquid_Hydrogen = FluidUtils.getFluidStack("LiquidHydrogen", 1)
                    .getFluid();
            } else {
                Liquid_Hydrogen = FluidUtils.getFluidStack("liquidhydrogen", 1)
                    .getFluid();
            }
            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellLiquidHydrogen", 1) == null) {
                new BaseItemComponent("LiquidHydrogen", "Liquid Hydrogen", new short[] { 10, 10, 175 });
            }
        }

        Formaldehyde = FluidUtils
            .generateFluidNonMolten("Formaldehyde", "Formaldehyde", 185, new short[] { 150, 75, 150, 100 }, null, null);

        Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide = FluidUtils.generateFluidNonMolten(
            "RocketFuelMixA",
            "H8N4C2O4 Rocket Fuel",
            216,
            new short[] { 50, 220, 50, 100 },
            null,
            null);
        RP1_Plus_Liquid_Oxygen = FluidUtils.generateFluidNonMolten(
            "RocketFuelMixB",
            "Rp-1 Rocket Fuel",
            250,
            new short[] { 250, 50, 50, 100 },
            null,
            null);
        Monomethylhydrazine_Plus_Nitric_Acid = FluidUtils.generateFluidNonMolten(
            "RocketFuelMixC",
            "CN3H7O3 Rocket Fuel",
            221,
            new short[] { 125, 75, 180, 100 },
            null,
            null);
        Dense_Hydrazine_Mix = FluidUtils.generateFluidNonMolten(
            "RocketFuelMixD",
            "Dense Hydrazine Fuel Mixture",
            275,
            new short[] { 175, 80, 120, 100 },
            null,
            null);
    }
}
