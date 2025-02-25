package gtPlusPlus.core.item.chemistry;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoaderGenericChem {

    public static void generate() {
        recipeCatalystRed();
        recipeCatalystYellow();
        recipeCatalystBlue();
        recipeCatalystOrange();
        recipeCatalystPurple();
        recipeCatalystBrown();
        recipeCatalystPink();
        recipeCatalystFormaldehyde();
        recipeCatalystSolidAcid();
        recipeCatalystInfiniteMutation();

        recipeGrindingBallAlumina();
        recipeGrindingBallSoapstone();

        recipeNitroBenzene();
        recipeAniline();
        recipeCadaverineAndPutrescine();
        recipeCyclohexane();
        recipeCyclohexanone();

        recipe2Ethylanthraquinone();
        recipe2Ethylanthrahydroquinone();
        recipeHydrogenPeroxide();
        recipeLithiumHydroperoxide();
        recipeLithiumPeroxide();

        recipeSodiumEthoxide();
        recipeCarbonDisulfide();
        recipeEthylXanthates();
        recipePotassiumHydroxide();

        recipeMutatedLivingSolder();

        registerFuels();
    }

    private static void recipeSodiumEthoxide() {
        // C2H5OH + Na → C2H5ONa + H
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1))
            .itemOutputs(GregtechItemList.SodiumEthoxide.get(9))
            .fluidInputs(Materials.Ethanol.getFluid(1000))
            .fluidOutputs(Materials.Hydrogen.getGas(1000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    // todo
    private static void recipePotassiumHydroxide() {
        // Ca(OH)2 + K2O + CO2 → CaCO3 + 2 KOH
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                Materials.Potash.getDust(3),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumCarbonate", 5),
                GregtechItemList.PotassiumHydroxide.get(6))
            .fluidInputs(Materials.CarbonDioxide.getGas(1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeEthylXanthates() {
        // Potassium ethyl xanthate - CH3CH2OH + CS2 + KOH → C3H5KOS2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(17), GregtechItemList.PotassiumHydroxide.get(3))
            .itemOutputs(GregtechItemList.PotassiumEthylXanthate.get(12))
            .fluidInputs(Materials.Ethanol.getFluid(1000), new FluidStack(GTPPFluids.CarbonDisulfide, 1000))
            .fluidOutputs(GTModHandler.getWater(1000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

        // Sodium ethyl xanthate - CH3CH2ONa + CS2 → CH3CH2OCS2Na
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(17), GregtechItemList.SodiumEthoxide.get(9))
            .itemOutputs(GregtechItemList.SodiumEthylXanthate.get(12))
            .fluidInputs(new FluidStack(GTPPFluids.CarbonDisulfide, 1000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeCarbonDisulfide() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get("fuelCoke", 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 16))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1))
            .fluidOutputs(new FluidStack(GTPPFluids.CarbonDisulfide, 4000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .metadata(COIL_HEAT, 1500)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                GregtechItemList.BrownMetalCatalyst.get(0),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 4))
            .fluidInputs(new FluidStack(GTPPFluids.CoalGas, 1000))
            .fluidOutputs(new FluidStack(GTPPFluids.CarbonDisulfide, 2000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeMutatedLivingSolder() {
        // Endgame soldering alloy meant for the bioware circuit line and beyond.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.InfiniteMutationCatalyst.get(0),
                ItemList.Circuit_Chip_Biocell.get(64),
                ItemList.Gravistar.get(8),
                Materials.InfinityCatalyst.getDust(2))
            .fluidInputs(
                Materials.Tin.getPlasma(18000),
                Materials.Bismuth.getPlasma(18000),
                FluidRegistry.getFluidStack("cryotheum", 4000))
            .fluidOutputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(280 * INGOTS))
            .duration(13 * MINUTES + 20 * SECONDS)
            .eut(3842160)
            .metadata(CHEMPLANT_CASING_TIER, 7)
            .addTo(chemicalPlantRecipes);
    }

    private static void registerFuels() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellNitrobenzene", 1))
            .metadata(FUEL_VALUE, 1600)
            .metadata(FUEL_TYPE, 1)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);
    }

    private static void recipeGrindingBallAlumina() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mAlumina, 64), GTUtility.getIntegratedCircuit(10))
            .itemOutputs(GregtechItemList.Milling_Ball_Alumina.get(8))
            .fluidInputs(new FluidStack(GTPPFluids.Aniline, 4000))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void recipeGrindingBallSoapstone() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 32),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(GregtechItemList.Milling_Ball_Soapstone.get(8))
            .fluidInputs(new FluidStack(GTPPFluids.LiquidResin, 2500))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCyclohexane() {
        // C6H6 + 6H = C6H12
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(8), GregtechItemList.BrownMetalCatalyst.get(0))
            .fluidInputs(Materials.Benzene.getFluid(1000), Materials.Hydrogen.getGas(6000))
            .fluidOutputs(new FluidStack(GTPPFluids.Cyclohexane, 1000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeCyclohexanone() {
        // C6H12 + 2O(Air) = C6H10O + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(8), GregtechItemList.BlueMetalCatalyst.get(0))
            .fluidInputs(new FluidStack(GTPPFluids.Cyclohexane, 1000), Materials.Air.getGas(4000))
            .fluidOutputs(new FluidStack(GTPPFluids.Cyclohexanone, 1000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(8))
            .fluidInputs(new FluidStack(GTPPFluids.Cyclohexane, 1000), Materials.Oxygen.getGas(2000))
            .fluidOutputs(new FluidStack(GTPPFluids.Cyclohexanone, 1000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeCatalystRed() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                CI.getEmptyCatalyst(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 2))
            .itemOutputs(GregtechItemList.RedMetalCatalyst.get(10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystYellow() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(12),
                CI.getEmptyCatalyst(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 4))
            .itemOutputs(GregtechItemList.YellowMetalCatalyst.get(10))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystBlue() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(8),
                CI.getEmptyCatalyst(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 3))
            .itemOutputs(GregtechItemList.BlueMetalCatalyst.get(10))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystOrange() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(8),
                CI.getEmptyCatalyst(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 5))
            .itemOutputs(GregtechItemList.OrangeMetalCatalyst.get(10))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystPurple() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                CI.getEmptyCatalyst(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 6),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 6))
            .itemOutputs(GregtechItemList.PurpleMetalCatalyst.get(10))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystBrown() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                CI.getEmptyCatalyst(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4))
            .itemOutputs(GregtechItemList.BrownMetalCatalyst.get(10))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystPink() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(12),
                CI.getEmptyCatalyst(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 4),
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 4))
            .itemOutputs(GregtechItemList.PinkMetalCatalyst.get(10))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystFormaldehyde() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(12),
                CI.getEmptyCatalyst(4),
                new ItemStack(RocketFuels.Formaldehyde_Catalyst_Dust, 8))
            .itemOutputs(GregtechItemList.FormaldehydeCatalyst.get(4))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystSolidAcid() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(12),
                CI.getEmptyCatalyst(5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 2))
            .itemOutputs(GregtechItemList.SolidAcidCatalyst.get(5))
            .fluidInputs(MaterialMisc.SOLID_ACID_MIXTURE.getFluidStack(1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystInfiniteMutation() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(12),
                CI.getEmptyCatalyst(5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 10))
            .itemOutputs(GregtechItemList.InfiniteMutationCatalyst.get(5))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCadaverineAndPutrescine() {
        // Basic Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4), new ItemStack(Items.rotten_flesh, 64))
            .fluidInputs(FluidUtils.getHotWater(2000))
            .fluidOutputs(new FluidStack(GTPPFluids.Cadaverine, 250), new FluidStack(GTPPFluids.Putrescine, 250))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        // Advanced Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(8),
                new ItemStack(Items.rotten_flesh, 128),
                ItemUtils.simpleMetaStack(AgriculturalChem.mAgrichemItem1, 8, 32))
            .fluidInputs(FluidUtils.getHotWater(3000))
            .fluidOutputs(new FluidStack(GTPPFluids.Cadaverine, 750), new FluidStack(GTPPFluids.Putrescine, 750))
            .duration(2 * MINUTES)
            .eut(240)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeAniline() {
        // C6H5NO2 + 6H = C6H7N + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(12), GregtechItemList.BlueMetalCatalyst.get(0))
            .fluidInputs(new FluidStack(GTPPFluids.Nitrobenzene, 1000), Materials.Hydrogen.getGas(6000))
            .fluidOutputs(new FluidStack(GTPPFluids.Aniline, 1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeNitroBenzene() {
        // C6H6 + HNO3 =H2SO4= C6H5NO2 +H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(12))
            .fluidInputs(
                Materials.Benzene.getFluid(5000),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.NitricAcid.getFluid(5000),
                GTModHandler.getDistilledWater(10000))
            .fluidOutputs(new FluidStack(GTPPFluids.Nitrobenzene, 5000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipe2Ethylanthraquinone() {
        // C6H4(CO)2O + C6H5CH2CH3 = C6H4(CO)2C6H3CH2CH3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4), GregtechItemList.PhthalicAnhydrideDust.get(15))
            .fluidInputs(new FluidStack(GTPPFluids.Ethylbenzene, 1000))
            .fluidOutputs(new FluidStack(GTPPFluids.Ethylanthraquinone, 1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipe2Ethylanthrahydroquinone() {
        // C6H4(CO)2C6H3CH2CH3 + 2H = C6H4(COH)2C6H3CH2CH3
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4), GregtechItemList.OrangeMetalCatalyst.get(0))
            .fluidInputs(new FluidStack(GTPPFluids.Ethylanthraquinone, 1000), Materials.Hydrogen.getGas(2000))
            .fluidOutputs(new FluidStack(GTPPFluids.Ethylanthrahydroquinone, 1000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeLithiumPeroxide() {
        // 2HLiO2 = Li2O2 + H2O2
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.LithiumHydroperoxide.get(8))
            .fluidOutputs(new FluidStack(GTPPFluids.HydrogenPeroxide, 1000))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumPeroxide", 4))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void recipeLithiumHydroperoxide() {
        // LiOH + H2O2 = HLiO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 3))
            .itemOutputs(GregtechItemList.LithiumHydroperoxide.get(4))
            .fluidInputs(new FluidStack(GTPPFluids.HydrogenPeroxide, 1000))
            .duration(30 * SECONDS)
            .eut(240)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeHydrogenPeroxide() {
        // C6H4(COH)2C6H3CH2CH3 + 2O =(C6H4CH)2= H2O2 + C6H4(CO)2C6H3CH2CH3
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .fluidInputs(
                Materials.Air.getGas(20000),
                new FluidStack(GTPPFluids.Ethylanthrahydroquinone, 5000),
                new FluidStack(GTPPFluids.Anthracene, 50))
            .fluidOutputs(
                new FluidStack(GTPPFluids.Ethylanthraquinone, 5000),
                new FluidStack(GTPPFluids.HydrogenPeroxide, 5000))
            .duration(30 * SECONDS)
            .eut(240)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4))
            .fluidInputs(
                Materials.Oxygen.getGas(10000),
                new FluidStack(GTPPFluids.Ethylanthrahydroquinone, 5000),
                new FluidStack(GTPPFluids.Anthracene, 50))
            .fluidOutputs(
                new FluidStack(GTPPFluids.Ethylanthraquinone, 5000),
                new FluidStack(GTPPFluids.HydrogenPeroxide, 5000))
            .duration(5 * SECONDS)
            .eut(240)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }
}
