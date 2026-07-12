package gtPlusPlus.core.item.chemistry;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

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
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1)))
            .circuit(16)
            .itemOutputs(GregtechItemList.SodiumEthoxide.get(9))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipePotassiumHydroxide() {
        // Ca(OH)2 + K2O + CO2 → CaCO3 + 2 KOH
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Potash, Materials2Shapes.dust, (int) (3)),
                GregtechItemList.CalciumHydroxideDust.get(5))
            .circuit(18)
            .itemOutputs(GregtechItemList.CalciumCarbonateDust.get(5), GregtechItemList.PotassiumHydroxide.get(6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeEthylXanthates() {
        // Potassium ethyl xanthate - CH3CH2OH + CS2 + KOH → C3H5KOS2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PotassiumHydroxide.get(3))
            .circuit(17)
            .itemOutputs(GregtechItemList.PotassiumEthylXanthate.get(12))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                new FluidStack(GTPPFluids.CarbonDisulfide, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

        // Sodium ethyl xanthate - CH3CH2ONa + CS2 → CH3CH2OCS2Na
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.SodiumEthoxide.get(9))
            .circuit(17)
            .itemOutputs(GregtechItemList.SodiumEthylXanthate.get(12))
            .fluidInputs(new FluidStack(GTPPFluids.CarbonDisulfide, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeCarbonDisulfide() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get("fuelCoke", 8),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (16)))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1))
            .fluidOutputs(new FluidStack(GTPPFluids.CarbonDisulfide, 4_000))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .metadata(COIL_HEAT, 1500)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.BrownMetalCatalyst.get(0),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (4)))
            .circuit(20)
            .fluidInputs(new FluidStack(GTPPFluids.CoalGas, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.CarbonDisulfide, 2_000))
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
                MaterialLibAPI.getStack(Materials2Materials.InfinityCatalyst, Materials2Shapes.dust, (int) (2)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tin, Materials2FluidShapes.fluidPlasma, (int) (18_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Bismuth, Materials2FluidShapes.fluidPlasma, (int) (18_000)),
                new FluidStack(TFFluids.fluidCryotheum, 4_000))
            .fluidOutputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4 * STACKS + 24 * INGOTS))
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
        ItemStack aluminaBallOutput = GregtechItemList.Milling_Ball_Alumina.get(8);
        ItemGenericChemBase.createMillingBallNBT(aluminaBallOutput);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Alumina.get(OrePrefixes.dust, 64))
            .circuit(10)
            .itemOutputs(aluminaBallOutput)
            .fluidInputs(new FluidStack(GTPPFluids.Aniline, 4_000))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void recipeGrindingBallSoapstone() {
        ItemStack soapstoneBallOutput = GregtechItemList.Milling_Ball_Soapstone.get(8);
        ItemGenericChemBase.createMillingBallNBT(soapstoneBallOutput);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Soapstone, Materials2Shapes.dust, (int) (32)))
            .circuit(10)
            .itemOutputs(soapstoneBallOutput)
            .fluidInputs(new FluidStack(GTPPFluids.LiquidResin, 2_500))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCyclohexane() {
        // C6H6 + 6H = C6H12
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BrownMetalCatalyst.get(0))
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (6_000)))
            .fluidOutputs(new FluidStack(GTPPFluids.Cyclohexane, 1_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeCyclohexanone() {
        // C6H12 + 2O(Air) = C6H10O + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BlueMetalCatalyst.get(0))
            .circuit(8)
            .fluidInputs(
                new FluidStack(GTPPFluids.Cyclohexane, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.fluidGas, (int) (4_000)))
            .fluidOutputs(new FluidStack(GTPPFluids.Cyclohexanone, 1_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                new FluidStack(GTPPFluids.Cyclohexane, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .fluidOutputs(new FluidStack(GTPPFluids.Cyclohexanone, 1_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeCatalystRed() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, (int) (2)))
            .circuit(4)
            .itemOutputs(GregtechItemList.RedMetalCatalyst.get(10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystYellow() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, (int) (4)))
            .circuit(12)
            .itemOutputs(GregtechItemList.YellowMetalCatalyst.get(10))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystBlue() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, (int) (3)))
            .circuit(8)
            .itemOutputs(GregtechItemList.BlueMetalCatalyst.get(10))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystOrange() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, (int) (5)),
                MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.dust, (int) (5)))
            .circuit(8)
            .itemOutputs(GregtechItemList.OrangeMetalCatalyst.get(10))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystPurple() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.dust, (int) (6)),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 6))
            .circuit(16)
            .itemOutputs(GregtechItemList.PurpleMetalCatalyst.get(10))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystBrown() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.dust, (int) (4)))
            .circuit(4)
            .itemOutputs(GregtechItemList.BrownMetalCatalyst.get(10))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystPink() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.dust, (int) (4)),
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 4))
            .circuit(12)
            .itemOutputs(GregtechItemList.PinkMetalCatalyst.get(10))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystFormaldehyde() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.EmptyCatalystCarrier.get(4), GregtechItemList.FormaldehydeCatalystDust.get(8))
            .circuit(12)
            .itemOutputs(GregtechItemList.FormaldehydeCatalyst.get(4))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystSolidAcid() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(5),
                MaterialLibAPI.getStack(Materials2Materials.Lapis, Materials2Shapes.dust, (int) (2)))
            .circuit(12)
            .itemOutputs(GregtechItemList.SolidAcidCatalyst.get(5))
            .fluidInputs(MaterialMisc.SOLID_ACID_MIXTURE.getFluidStack(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystInfiniteMutation() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(5),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Naquadria, Materials2Shapes.dust, (int) (10)))
            .circuit(12)
            .itemOutputs(GregtechItemList.InfiniteMutationCatalyst.get(5))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void recipeCadaverineAndPutrescine() {
        // Basic Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.rotten_flesh, 64))
            .circuit(4)
            .fluidInputs(GTModHandler.getHotWater(2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Cadaverine, 250), new FluidStack(GTPPFluids.Putrescine, 250))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        // Advanced Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.rotten_flesh, 128), GregtechItemList.Compost.get(32))
            .circuit(8)
            .fluidInputs(GTModHandler.getHotWater(3_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Cadaverine, 750), new FluidStack(GTPPFluids.Putrescine, 750))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_HV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeAniline() {
        // C6H5NO2 + 6H = C6H7N + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BlueMetalCatalyst.get(0))
            .circuit(12)
            .fluidInputs(
                new FluidStack(GTPPFluids.Nitrobenzene, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (6_000)))
            .fluidOutputs(new FluidStack(GTPPFluids.Aniline, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeNitroBenzene() {
        // C6H6 + HNO3 =H2SO4= C6H5NO2 +H2O
        GTValues.RA.stdBuilder()
            .circuit(12)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.fluidLiquid, (int) (5_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (5_000)),
                GTModHandler.getDistilledWater(10_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Nitrobenzene, 5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipe2Ethylanthraquinone() {
        // C6H4(CO)2O + C6H5CH2CH3 = C6H4(CO)2C6H3CH2CH3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PhthalicAnhydrideDust.get(15))
            .circuit(4)
            .fluidInputs(new FluidStack(GTPPFluids.Ethylbenzene, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.Ethylanthraquinone, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipe2Ethylanthrahydroquinone() {
        // C6H4(CO)2C6H3CH2CH3 + 2H = C6H4(COH)2C6H3CH2CH3
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.OrangeMetalCatalyst.get(0))
            .circuit(4)
            .fluidInputs(
                new FluidStack(GTPPFluids.Ethylanthraquinone, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .fluidOutputs(new FluidStack(GTPPFluids.Ethylanthrahydroquinone, 1_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeLithiumPeroxide() {
        // 2HLiO2 = Li2O2 + H2O2
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.LithiumHydroperoxide.get(8))
            .fluidOutputs(new FluidStack(GTPPFluids.HydrogenPeroxide, 1_000))
            .itemOutputs(GregtechItemList.LithiumPeroxideDust.get(4))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void recipeLithiumHydroperoxide() {
        // LiOH + H2O2 = HLiO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.LithiumHydroxideDust.get(3))
            .circuit(4)
            .itemOutputs(GregtechItemList.LithiumHydroperoxide.get(4))
            .fluidInputs(new FluidStack(GTPPFluids.HydrogenPeroxide, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeHydrogenPeroxide() {
        // C6H4(COH)2C6H3CH2CH3 + 2O =(C6H4CH)2= H2O2 + C6H4(CO)2C6H3CH2CH3
        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.fluidGas, (int) (20_000)),
                new FluidStack(GTPPFluids.Ethylanthrahydroquinone, 5_000),
                new FluidStack(GTPPFluids.Anthracene, 50))
            .fluidOutputs(
                new FluidStack(GTPPFluids.Ethylanthraquinone, 5_000),
                new FluidStack(GTPPFluids.HydrogenPeroxide, 5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (10_000)),
                new FluidStack(GTPPFluids.Ethylanthrahydroquinone, 5_000),
                new FluidStack(GTPPFluids.Anthracene, 50))
            .fluidOutputs(
                new FluidStack(GTPPFluids.Ethylanthraquinone, 5_000),
                new FluidStack(GTPPFluids.HydrogenPeroxide, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }
}
