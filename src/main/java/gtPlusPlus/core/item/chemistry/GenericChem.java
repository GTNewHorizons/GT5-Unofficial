package gtPlusPlus.core.item.chemistry;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.item.circuit.GTPP_IntegratedCircuit_Item;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.NONMATERIAL;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.agrichem.BioRecipes;
import gtPlusPlus.plugin.agrichem.block.AgrichemFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GenericChem extends ItemPackage {

    /**
     * Materials
     */

    // Refined PTFE
    public static final Material TEFLON = new Material(
            "Teflon",
            MaterialState.SOLID,
            TextureSet.SET_SHINY,
            new short[] { 75, 45, 75 },
            330,
            640,
            -1,
            -1,
            false,
            null,
            0,
            new MaterialStack(NONMATERIAL.PTFE, 75),
            new MaterialStack(NONMATERIAL.PLASTIC, 15),
            new MaterialStack(ELEMENT.getInstance().CARBON, 5),
            new MaterialStack(ELEMENT.getInstance().SODIUM, 5));

    /**
     * Fluids
     */
    public static Fluid Benzene;

    public static Fluid NitroBenzene;
    public static Fluid Aniline;
    public static Fluid Polyurethane;
    public static Fluid Phenol; // https://en.wikipedia.org/wiki/Phenol#Uses
    public static Fluid Cyclohexane; // https://en.wikipedia.org/wiki/Cyclohexane
    public static Fluid Cyclohexanone; // https://en.wikipedia.org/wiki/Cyclohexanone
    public static Fluid Cadaverine; // https://en.wikipedia.org/wiki/Cadaverine
    public static Fluid Putrescine; // https://en.wikipedia.org/wiki/Putrescine
    public static Fluid BoricAcid;
    public static Fluid HydrochloricAcid;

    public static Fluid Ethylanthraquinone2;
    public static Fluid Ethylanthrahydroquinone2;
    public static Fluid Hydrogen_Peroxide;
    public static Fluid Lithium_Peroxide;
    public static Fluid Carbon_Disulfide;

    /**
     * Items
     */

    // Phenol Byproducts

    public static ItemGenericChemBase mGenericChemItem1;
    public static Item mAdvancedCircuit;

    private ItemStack mCatalystCarrier;

    public static ItemStack mRedCatalyst;
    public static ItemStack mYellowCatalyst;
    public static ItemStack mBlueCatalyst;
    public static ItemStack mOrangeCatalyst;
    public static ItemStack mPurpleCatalyst;
    public static ItemStack mBrownCatalyst;
    public static ItemStack mPinkCatalyst;
    public static ItemStack mFormaldehydeCatalyst;
    public static ItemStack mSolidAcidCatalyst;
    public static ItemStack mInfiniteMutationCatalyst;

    // QFT Catalysts
    public static ItemStack mPlatinumGroupCatalyst;
    public static ItemStack mPlasticPolymerCatalyst;
    public static ItemStack mRubberPolymerCatalyst;
    public static ItemStack mAdhesionPromoterCatalyst;
    public static ItemStack mTitaTungstenIndiumCatalyst;
    public static ItemStack mRadioactivityCatalyst;
    public static ItemStack mRareEarthGroupCatalyst;
    public static ItemStack mLimpidWaterCatalyst;
    public static ItemStack mSimpleNaquadahCatalyst;
    public static ItemStack mAdvancedNaquadahCatalyst;
    public static ItemStack mRawIntelligenceCatalyst;
    public static ItemStack mParticleAccelerationCatalyst;
    public static ItemStack mUltimatePlasticCatalyst;
    public static ItemStack mBiologicalIntelligenceCatalyst;
    public static ItemStack mFlawlessWaterCatalyst;
    public static ItemStack TemporalHarmonyCatalyst;
    public static ItemStack mSynchrotronCapableCatalyst;
    public static ItemStack mAlgagenicGrowthPromoterCatalyst;

    public static ItemStack mMillingBallAlumina;
    public static ItemStack mMillingBallSoapstone;

    public static ItemStack mSodiumEthoxide;
    public static ItemStack mSodiumEthylXanthate;
    public static ItemStack mPotassiumEthylXanthate;
    public static ItemStack mPotassiumHydroxide;

    @Override
    public void items() {

        MaterialGenerator.generate(TEFLON, false);

        mGenericChemItem1 = new ItemGenericChemBase();
        mAdvancedCircuit = new GTPP_IntegratedCircuit_Item("T3RecipeSelector", "science/general/AdvancedCircuit");
        GregtechItemList.Circuit_T3RecipeSelector.set(mAdvancedCircuit);

        registerItemStacks();
        registerOreDict();

        GregtechItemList.Milling_Ball_Alumina.set(mMillingBallAlumina);
        GregtechItemList.Milling_Ball_Soapstone.set(mMillingBallSoapstone);
    }

    public void registerItemStacks() {

        mCatalystCarrier = ItemUtils.simpleMetaStack(AgriculturalChem.mAgrichemItem1, 13, 1);

        mRedCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 0, 1);
        mYellowCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 1, 1);
        mBlueCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 2, 1);
        mOrangeCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 3, 1);
        mPurpleCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 4, 1);
        mBrownCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 5, 1);
        mPinkCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 6, 1);
        mMillingBallAlumina = ItemUtils.simpleMetaStack(mGenericChemItem1, 7, 1);
        mMillingBallSoapstone = ItemUtils.simpleMetaStack(mGenericChemItem1, 8, 1);
        mSodiumEthoxide = ItemUtils.simpleMetaStack(mGenericChemItem1, 9, 1);
        mSodiumEthylXanthate = ItemUtils.simpleMetaStack(mGenericChemItem1, 10, 1);
        mPotassiumEthylXanthate = ItemUtils.simpleMetaStack(mGenericChemItem1, 11, 1);
        mPotassiumHydroxide = ItemUtils.simpleMetaStack(mGenericChemItem1, 12, 1);
        mFormaldehydeCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 13, 1);
        mSolidAcidCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 14, 1);
        mInfiniteMutationCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 15, 1);

        // QFT Catalysts
        mPlatinumGroupCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 16, 1);
        mPlasticPolymerCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 17, 1);
        mRubberPolymerCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 18, 1);
        mAdhesionPromoterCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 19, 1);
        mTitaTungstenIndiumCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 20, 1);
        mRadioactivityCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 21, 1);
        mRareEarthGroupCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 22, 1);
        mSimpleNaquadahCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 23, 1);
        mAdvancedNaquadahCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 24, 1);
        mRawIntelligenceCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 25, 1);
        mUltimatePlasticCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 26, 1);
        mBiologicalIntelligenceCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 27, 1);
        TemporalHarmonyCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 28, 1);
        mLimpidWaterCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 29, 1);
        mFlawlessWaterCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 30, 1);
        mParticleAccelerationCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 31, 1);
        mSynchrotronCapableCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 32, 1);
        mAlgagenicGrowthPromoterCatalyst = ItemUtils.simpleMetaStack(mGenericChemItem1, 33, 1);
    }

    public void registerOreDict() {

        ItemUtils.addItemToOreDictionary(mRedCatalyst, "catalystIronCopper");
        ItemUtils.addItemToOreDictionary(mYellowCatalyst, "catalystTungstenNickel");
        ItemUtils.addItemToOreDictionary(mBlueCatalyst, "catalystCobaltTitanium");
        ItemUtils.addItemToOreDictionary(mOrangeCatalyst, "catalystVanadiumPalladium");
        ItemUtils.addItemToOreDictionary(mPurpleCatalyst, "catalystIridiumRuthenium");
        ItemUtils.addItemToOreDictionary(mBrownCatalyst, "catalystNickelAluminium");
        ItemUtils.addItemToOreDictionary(mPinkCatalyst, "catalystPlatinumRhodium");
        ItemUtils.addItemToOreDictionary(mMillingBallAlumina, "millingballAlumina");
        ItemUtils.addItemToOreDictionary(mMillingBallSoapstone, "millingballSoapstone");
        ItemUtils.addItemToOreDictionary(mSodiumEthoxide, "dustSodiumEthoxide");
        ItemUtils.addItemToOreDictionary(mSodiumEthylXanthate, "dustSodiumEthylXanthate");
        ItemUtils.addItemToOreDictionary(mPotassiumEthylXanthate, "dustPotassiumEthylXanthate");
        ItemUtils.addItemToOreDictionary(mPotassiumHydroxide, "dustPotassiumHydroxide");
        ItemUtils.addItemToOreDictionary(mFormaldehydeCatalyst, "catalystFormaldehyde");
        ItemUtils.addItemToOreDictionary(mSolidAcidCatalyst, "catalystSolidAcid");
        ItemUtils.addItemToOreDictionary(mInfiniteMutationCatalyst, "catalystInfiniteMutation");
        ItemUtils.addItemToOreDictionary(mPlatinumGroupCatalyst, "catalystPlatinumGroup");
        ItemUtils.addItemToOreDictionary(mPlasticPolymerCatalyst, "catalystPlasticPolymer");
        ItemUtils.addItemToOreDictionary(mRubberPolymerCatalyst, "catalystRubberPolymer");
        ItemUtils.addItemToOreDictionary(mAdhesionPromoterCatalyst, "catalystAdhesionPromoter");
        ItemUtils.addItemToOreDictionary(mTitaTungstenIndiumCatalyst, "catalystTitaTungstenIndium");
        ItemUtils.addItemToOreDictionary(mRadioactivityCatalyst, "catalystRadioactivity");
        ItemUtils.addItemToOreDictionary(mRareEarthGroupCatalyst, "catalystRareEarthGroup");
        ItemUtils.addItemToOreDictionary(mSimpleNaquadahCatalyst, "catalystSimpleNaquadah");
        ItemUtils.addItemToOreDictionary(mAdvancedNaquadahCatalyst, "catalystAdvancedNaquadah");
        ItemUtils.addItemToOreDictionary(mRawIntelligenceCatalyst, "catalystRawIntelligence");
        ItemUtils.addItemToOreDictionary(mUltimatePlasticCatalyst, "catalystUltimatePlastic");
        ItemUtils.addItemToOreDictionary(mBiologicalIntelligenceCatalyst, "catalystBiologicalIntelligence");
        ItemUtils.addItemToOreDictionary(TemporalHarmonyCatalyst, "catalystTemporalHarmony");
        ItemUtils.addItemToOreDictionary(mLimpidWaterCatalyst, "catalystLimpidWater");
        ItemUtils.addItemToOreDictionary(mFlawlessWaterCatalyst, "catalystFlawlessWater");
        ItemUtils.addItemToOreDictionary(mParticleAccelerationCatalyst, "catalystParticleAcceleration");
        ItemUtils.addItemToOreDictionary(mSynchrotronCapableCatalyst, "catalystSynchrotronCapable");
        ItemUtils.addItemToOreDictionary(mAlgagenicGrowthPromoterCatalyst, "catalystAlgagenicGrowthPromoter");
    }

    @Override
    public void blocks() {}

    @Override
    public void fluids() {

        if (!FluidRegistry.isFluidRegistered("benzene")) {
            Benzene = FluidUtils
                    .generateFluidNoPrefix("benzene", "Benzene", 278, new short[] { 100, 70, 30, 100 }, true);
        } else {
            Benzene = FluidRegistry.getFluid("benzene");
        }

        NitroBenzene = FluidUtils
                .generateFluidNoPrefix("nitrobenzene", "Nitrobenzene", 278, new short[] { 70, 50, 40, 100 }, true);

        Aniline = FluidUtils.generateFluidNoPrefix("aniline", "Aniline", 266, new short[] { 100, 100, 30, 100 }, true);

        BoricAcid = FluidUtils
                .generateFluidNoPrefix("boricacid", "Boric Acid", 278, new short[] { 90, 30, 120, 100 }, true);

        Polyurethane = FluidUtils
                .generateFluidNoPrefix("polyurethane", "Polyurethane", 350, new short[] { 100, 70, 100, 100 }, true);

        if (!FluidRegistry.isFluidRegistered("phenol")) {
            Phenol = FluidUtils.generateFluidNoPrefix("phenol", "Phenol", 313, new short[] { 100, 70, 30, 100 }, true);
        } else {
            Phenol = FluidRegistry.getFluid("phenol");
        }

        // Use GT's if it exists, else make our own.
        if (FluidRegistry.isFluidRegistered("hydrochloricacid_gt5u")) {
            HydrochloricAcid = FluidRegistry.getFluid("hydrochloricacid_gt5u");
        } else {
            HydrochloricAcid = FluidUtils.generateFluidNoPrefix(
                    "hydrochloricacid",
                    "Hydrochloric Acid",
                    285,
                    new short[] { 183, 200, 196, 100 },
                    true);
        }

        Cyclohexane = FluidUtils
                .generateFluidNoPrefix("cyclohexane", "Cyclohexane", 32 + 175, new short[] { 100, 70, 30, 100 }, true);
        Cyclohexanone = FluidUtils.generateFluidNoPrefix(
                "cyclohexanone",
                "Cyclohexanone",
                32 + 175,
                new short[] { 100, 70, 30, 100 },
                true);

        Cadaverine = FluidUtils
                .generateFluidNoPrefix("cadaverine", "Cadaverine", 32 + 175, new short[] { 100, 70, 30, 100 }, true);
        Putrescine = FluidUtils
                .generateFluidNoPrefix("putrescine", "Putrescine", 32 + 175, new short[] { 100, 70, 30, 100 }, true);

        // Create 2-Ethylanthraquinone
        // 2-Ethylanthraquinone is prepared from the reaction of phthalic anhydride and ethylbenzene
        Ethylanthraquinone2 = FluidUtils.generateFluidNonMolten(
                "2Ethylanthraquinone",
                "2-Ethylanthraquinone",
                415,
                new short[] { 227, 255, 159, 100 },
                null,
                null);
        // Create 2-Ethylanthrahydroquinone
        // Palladium plate + Hydrogen(250) + 2-Ethylanthraquinone(500) = 600 Ethylanthrahydroquinone
        Ethylanthrahydroquinone2 = FluidUtils.generateFluidNonMolten(
                "2Ethylanthrahydroquinone",
                "2-Ethylanthrahydroquinone",
                415,
                new short[] { 207, 225, 129, 100 },
                null,
                null);
        // Create Hydrogen Peroxide
        // Compressed Air(1500) + Ethylanthrahydroquinone(500) + Anthracene(5) = 450 Ethylanthraquinone && 200 Peroxide
        Hydrogen_Peroxide = FluidUtils.generateFluidNonMolten(
                "HydrogenPeroxide",
                "Hydrogen Peroxide",
                150,
                new short[] { 210, 255, 255, 100 },
                null,
                null);

        // Lithium Hydroperoxide - LiOH + H2O2 → LiOOH + 2 H2O
        // ItemUtils.generateSpecialUseDusts("LithiumHydroperoxide", "Lithium Hydroperoxide", "HLiO2",
        // Utils.rgbtoHexValue(125, 125, 125));
        // v - Dehydrate
        // Lithium Peroxide - 2 LiOOH → Li2O2 + H2O2 + 2 H2O
        Lithium_Peroxide = FluidUtils.generateFluidNonMolten(
                "LithiumPeroxide",
                "Lithium Peroxide",
                446,
                new short[] { 135, 135, 135, 100 },
                null,
                null);

        Carbon_Disulfide = FluidUtils
                .generateFluidNoPrefix("CarbonDisulfide", "Carbon Disulfide", 350, new short[] { 175, 175, 175, 100 });
    }

    @Override
    public String errorMessage() {
        return "Failed to generate recipes for GenericChem.";
    }

    @Override
    public boolean generateRecipes() {

        recipeAdvancedChip();
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

        return true;
    }

    private void recipeSodiumEthoxide() {
        // C2H5OH + Na → C2H5ONa + H
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(16), ELEMENT.getInstance().SODIUM.getDust(1) },
                new FluidStack[] { Materials.Ethanol.getFluid(1000) },
                new ItemStack[] { ItemUtils.getSimpleStack(mSodiumEthoxide, 9) },
                new FluidStack[] { ELEMENT.getInstance().HYDROGEN.getFluidStack(1000) },
                20 * 20,
                120,
                2);
    }

    private void recipePotassiumHydroxide() {
        // Ca(OH)2 + K2O + CO2 → CaCO3 + 2 KOH
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(18), Materials.Potash.getDust(3),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5), },
                new FluidStack[] { Materials.CarbonDioxide.getGas(1000) },
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumCarbonate", 5),
                        ItemUtils.getSimpleStack(mPotassiumHydroxide, 6) },
                new FluidStack[] {},
                20 * 30,
                120,
                2);
    }

    private void recipeEthylXanthates() {

        // Potassium ethyl xanthate - CH3CH2OH + CS2 + KOH → C3H5KOS2 + H2O
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(17), ItemUtils.getSimpleStack(mPotassiumHydroxide, 3), },
                new FluidStack[] { Materials.Ethanol.getFluid(1000),
                        FluidUtils.getFluidStack(Carbon_Disulfide, 1000), },
                new ItemStack[] { ItemUtils.getSimpleStack(mPotassiumEthylXanthate, 12) },
                new FluidStack[] { FluidUtils.getWater(1000) },
                20 * 60,
                120,
                4);

        // Sodium ethyl xanthate - CH3CH2ONa + CS2 → CH3CH2OCS2Na
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(17), ItemUtils.getSimpleStack(mSodiumEthoxide, 9) },
                new FluidStack[] { FluidUtils.getFluidStack(Carbon_Disulfide, 1000), },
                new ItemStack[] { ItemUtils.getSimpleStack(mSodiumEthylXanthate, 12) },
                new FluidStack[] {},
                20 * 60,
                120,
                4);
    }

    private void recipeCarbonDisulfide() {

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 16L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L))
                .fluidOutputs(FluidUtils.getFluidStack(Carbon_Disulfide, 4000)).duration(10 * MINUTES)
                .eut(TierEU.RECIPE_LV).metadata(COIL_HEAT, 1500).addTo(blastFurnaceRecipes);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(20), ItemUtils.getSimpleStack(mBrownCatalyst, 0),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 4) },
                new FluidStack[] { FluidUtils.getFluidStack(CoalTar.Coal_Gas, 1000), },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Carbon_Disulfide, 2000) },
                20 * 60 * 5,
                30,
                2);
    }

    private void recipeMutatedLivingSolder() {

        // Endgame soldering alloy meant for the bioware circuit line and beyond.
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { ItemUtils.getSimpleStack(GenericChem.mInfiniteMutationCatalyst, 0),
                        ItemList.Circuit_Chip_Biocell.get(64), ItemList.Gravistar.get(8),
                        Materials.InfinityCatalyst.getDust(2) },
                new FluidStack[] { FluidUtils.getFluidStack("plasma.tin", 18000),
                        FluidUtils.getFluidStack("plasma.bismuth", 18000),
                        FluidUtils.getFluidStack("cryotheum", 4000) },
                new ItemStack[] {},
                new FluidStack[] { MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 280) },
                20 * 800,
                3842160,
                7);
    }

    private static void registerFuels() {

        // Burnables

        // Gas Fuels
        GT_Values.RA.stdBuilder().itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellNitrobenzene", 1))
                .metadata(FUEL_VALUE, 1600).metadata(FUEL_TYPE, 1).duration(0).eut(0).addTo(GT_RecipeConstants.Fuel);
    }

    private void recipeGrindingBallAlumina() {
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        ItemUtils.getSimpleStack(AgriculturalChem.mAlumina, 64),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemUtils.getSimpleStack(mMillingBallAlumina, 8))
                .fluidInputs(FluidUtils.getFluidStack(GenericChem.Aniline, 4000)).duration(3 * MINUTES)
                .eut(TierEU.RECIPE_HV).addTo(assemblerRecipes);
    }

    private void recipeGrindingBallSoapstone() {
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 32L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemUtils.getSimpleStack(mMillingBallSoapstone, 8))
                .fluidInputs(FluidUtils.getFluidStack(AgrichemFluids.mLiquidResin, 2500)).duration(2 * MINUTES)
                .eut(TierEU.RECIPE_HV).addTo(assemblerRecipes);
    }

    private void recipeCyclohexane() {

        // C6H6 + 6H = C6H12
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { getTierTwoChip(), ItemUtils.getSimpleStack(mBrownCatalyst, 0) },
                new FluidStack[] { FluidUtils.getFluidStack(Benzene, 1000),
                        FluidUtils.getFluidStack("hydrogen", 6000) },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Cyclohexane, 1000), },
                20 * 120,
                120,
                2);
    }

    private void recipeCyclohexanone() {

        // C6H12 + 2O(Air) = C6H10O + H2O
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { getTierTwoChip(), ItemUtils.getSimpleStack(mBlueCatalyst, 0) },
                new FluidStack[] { FluidUtils.getFluidStack(Cyclohexane, 1000), FluidUtils.getFluidStack("air", 4000) },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Cyclohexanone, 1000), },
                20 * 120,
                120,
                2);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { getTierTwoChip(), },
                new FluidStack[] { FluidUtils.getFluidStack(Cyclohexane, 1000),
                        FluidUtils.getFluidStack("oxygen", 2000) },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Cyclohexanone, 1000), },
                20 * 120,
                120,
                2);
    }

    private void recipeCatalystRed() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierOneChip(),
                        CI.getEmptyCatalyst(10),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 2L))
                .itemOutputs(ItemUtils.getSimpleStack(mRedCatalyst, 10)).duration(20 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
    }

    private void recipeCatalystYellow() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierThreeChip(),
                        CI.getEmptyCatalyst(10),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 4L))
                .itemOutputs(ItemUtils.getSimpleStack(mYellowCatalyst, 10)).duration(1 * MINUTES).eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
    }

    private void recipeCatalystBlue() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierTwoChip(),
                        CI.getEmptyCatalyst(10),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 3L))
                .itemOutputs(ItemUtils.getSimpleStack(mBlueCatalyst, 10)).duration(40 * SECONDS).eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
    }

    private void recipeCatalystOrange() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierTwoChip(),
                        CI.getEmptyCatalyst(10),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 5L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 5L))
                .itemOutputs(ItemUtils.getSimpleStack(mOrangeCatalyst, 10)).duration(40 * SECONDS).eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
    }

    private void recipeCatalystPurple() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierFourChip(),
                        CI.getEmptyCatalyst(10),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 6L),
                        ELEMENT.getInstance().RUTHENIUM.getDust(6))
                .itemOutputs(ItemUtils.getSimpleStack(mPurpleCatalyst, 10)).duration(2 * MINUTES).eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);
    }

    private void recipeCatalystBrown() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierOneChip(),
                        CI.getEmptyCatalyst(10),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4L))
                .itemOutputs(ItemUtils.getSimpleStack(mBrownCatalyst, 10)).duration(15 * SECONDS).eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
    }

    private void recipeCatalystPink() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierThreeChip(),
                        CI.getEmptyCatalyst(10),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 4L),
                        ELEMENT.getInstance().RHODIUM.getDust(4))
                .itemOutputs(ItemUtils.getSimpleStack(mPinkCatalyst, 10)).duration(30 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
    }

    private void recipeCatalystFormaldehyde() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierThreeChip(),
                        CI.getEmptyCatalyst(4),
                        ItemUtils.getSimpleStack(RocketFuels.Formaldehyde_Catalyst_Dust, 8))
                .itemOutputs(ItemUtils.getSimpleStack(mFormaldehydeCatalyst, 4)).duration(30 * SECONDS)
                .eut(TierEU.RECIPE_HV / 2).addTo(assemblerRecipes);
    }

    private void recipeCatalystSolidAcid() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierThreeChip(),
                        CI.getEmptyCatalyst(5),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 2L))
                .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mSolidAcidCatalyst, 5))
                .fluidInputs(MISC_MATERIALS.SOLID_ACID_MIXTURE.getFluidStack(1000)).duration(30 * SECONDS)
                .eut(TierEU.RECIPE_EV).addTo(assemblerRecipes);
    }

    private void recipeCatalystInfiniteMutation() {
        // Assembler Recipe
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        getTierThreeChip(),
                        CI.getEmptyCatalyst(5),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 10L))
                .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mInfiniteMutationCatalyst, 5)).duration(5 * SECONDS)
                .eut(TierEU.RECIPE_UHV).addTo(assemblerRecipes);
    }

    private void recipeCadaverineAndPutrescine() {

        // Basic Recipe
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { getTierOneChip(), ItemUtils.getSimpleStack(Items.rotten_flesh, 64) },
                new FluidStack[] { FluidUtils.getHotWater(2000) },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Cadaverine, 250),
                        FluidUtils.getFluidStack(Putrescine, 250), },
                20 * 120,
                120,
                1);

        // Advanced Recipe
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { getTierTwoChip(), ItemUtils.getSimpleStack(Items.rotten_flesh, 128),
                        ItemUtils.simpleMetaStack(AgriculturalChem.mAgrichemItem1, 8, 32) },
                new FluidStack[] { FluidUtils.getHotWater(3000) },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Cadaverine, 750),
                        FluidUtils.getFluidStack(Putrescine, 750), },
                20 * 120,
                240,
                2);
    }

    private void recipeAniline() {

        // C6H5NO2 + 6H = C6H7N + 2H2O
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { getTierThreeChip(), ItemUtils.getSimpleStack(mBlueCatalyst, 0) },
                new FluidStack[] { FluidUtils.getFluidStack(NitroBenzene, 1000),
                        FluidUtils.getFluidStack("hydrogen", 6000) },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Aniline, 1000), },
                20 * 30,
                500,
                3);
    }

    private void recipeNitroBenzene() {

        // C6H6 + HNO3 =H2SO4= C6H5NO2 +H2O
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { getTierThreeChip(), },
                new FluidStack[] { FluidUtils.getFluidStack(Benzene, 5000),
                        FluidUtils.getFluidStack("sulfuricacid", 1000), FluidUtils.getFluidStack("nitricacid", 5000),
                        FluidUtils.getDistilledWater(10000) },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(NitroBenzene, 5000), },
                20 * 30,
                500,
                4);
    }

    private void recipe2Ethylanthraquinone() {

        // C6H4(CO)2O + C6H5CH2CH3 = C6H4(CO)2C6H3CH2CH3 + H2O
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(4),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustPhthalicAnhydride", 15), },
                new FluidStack[] { FluidUtils.getFluidStack(CoalTar.Ethylbenzene, 1000), },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Ethylanthraquinone2, 1000), },
                20 * 15,
                120,
                2);
    }

    private void recipe2Ethylanthrahydroquinone() {

        // C6H4(CO)2C6H3CH2CH3 + 2H = C6H4(COH)2C6H3CH2CH3
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(4), ItemUtils.getSimpleStack(mOrangeCatalyst, 0), },
                new FluidStack[] { FluidUtils.getFluidStack(Ethylanthraquinone2, 1000),
                        FluidUtils.getFluidStack("hydrogen", 2000), },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Ethylanthrahydroquinone2, 1000), },
                20 * 40,
                120,
                2);
    }

    private void recipeLithiumPeroxide() {
        // 2HLiO2 = Li2O2 + H2O2
        CORE.RA.addDehydratorRecipe(
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroperoxide", 8),
                        ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1) },
                null,
                null,
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumPeroxide", 4),
                        ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenPeroxide", 1), },
                new int[] { 10000, 10000 },
                20 * 100,
                120);
    }

    private void recipeLithiumHydroperoxide() {

        // LiOH + H2O2 = HLiO2 + H2O
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(4),
                        ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 3), },
                new FluidStack[] { FluidUtils.getFluidStack("fluid.hydrogenperoxide", 1000), },
                new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroperoxide", 4), },
                new FluidStack[] {},
                20 * 30,
                240,
                1);

    }

    private void recipeHydrogenPeroxide() {

        // C6H4(COH)2C6H3CH2CH3 + 2O =(C6H4CH)2= H2O2 + C6H4(CO)2C6H3CH2CH3
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(4), },
                new FluidStack[] { FluidUtils.getFluidStack("air", 20000),
                        FluidUtils.getFluidStack(Ethylanthrahydroquinone2, 5000),
                        FluidUtils.getFluidStack("fluid.anthracene", 50), },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Ethylanthraquinone2, 5000),
                        FluidUtils.getFluidStack("fluid.hydrogenperoxide", 5000), },
                20 * 30,
                240,
                1);

        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { CI.getNumberedCircuit(4), },
                new FluidStack[] { Materials.Oxygen.getGas(10000),
                        FluidUtils.getFluidStack(Ethylanthrahydroquinone2, 5000),
                        FluidUtils.getFluidStack("fluid.anthracene", 50), },
                new ItemStack[] {},
                new FluidStack[] { FluidUtils.getFluidStack(Ethylanthraquinone2, 5000),
                        FluidUtils.getFluidStack("fluid.hydrogenperoxide", 5000), },
                20 * 5,
                240,
                1);

    }

    private static ItemStack getTierOneChip() {
        return CI.getNumberedAdvancedCircuit(4);
    }

    private static ItemStack getTierTwoChip() {
        return CI.getNumberedAdvancedCircuit(8);
    }

    private static ItemStack getTierThreeChip() {
        return CI.getNumberedAdvancedCircuit(12);
    }

    private static ItemStack getTierFourChip() {
        return CI.getNumberedAdvancedCircuit(16);
    }

    private static void recipeAdvancedChip() {
        GT_ModHandler.addShapelessCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 0L),
                0,
                new Object[] { OrePrefixes.circuit.get(Materials.Advanced) });

        long bits = 0;
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 1L, new Object[0]),
                bits,
                new Object[] { "d  ", " P ", "   ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 2L, new Object[0]),
                bits,
                new Object[] { " d ", " P ", "   ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 3L, new Object[0]),
                bits,
                new Object[] { "  d", " P ", "   ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 4L, new Object[0]),
                bits,
                new Object[] { "   ", " Pd", "   ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 5L, new Object[0]),
                bits,
                new Object[] { "   ", " P ", "  d", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 6L, new Object[0]),
                bits,
                new Object[] { "   ", " P ", " d ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 7L, new Object[0]),
                bits,
                new Object[] { "   ", " P ", "d  ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 8L, new Object[0]),
                bits,
                new Object[] { "   ", "dP ", "   ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 9L, new Object[0]),
                bits,
                new Object[] { "P d", "   ", "   ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 10L, new Object[0]),
                bits,
                new Object[] { "P  ", "  d", "   ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 11L, new Object[0]),
                bits,
                new Object[] { "P  ", "   ", "  d", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 12L, new Object[0]),
                bits,
                new Object[] { "P  ", "   ", " d ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 13L, new Object[0]),
                bits,
                new Object[] { "  P", "   ", "  d", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 14L, new Object[0]),
                bits,
                new Object[] { "  P", "   ", " d ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 15L, new Object[0]),
                bits,
                new Object[] { "  P", "   ", "d  ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 16L, new Object[0]),
                bits,
                new Object[] { "  P", "d  ", "   ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 17L, new Object[0]),
                bits,
                new Object[] { "   ", "   ", "d P", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 18L, new Object[0]),
                bits,
                new Object[] { "   ", "d  ", "  P", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 19L, new Object[0]),
                bits,
                new Object[] { "d  ", "   ", "  P", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 20L, new Object[0]),
                bits,
                new Object[] { " d ", "   ", "  P", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 21L, new Object[0]),
                bits,
                new Object[] { "d  ", "   ", "P  ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 22L, new Object[0]),
                bits,
                new Object[] { " d ", "   ", "P  ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 23L, new Object[0]),
                bits,
                new Object[] { "  d", "   ", "P  ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
        BioRecipes.addCraftingRecipe(
                GregtechItemList.Circuit_T3RecipeSelector.getWithDamage(1L, 24L, new Object[0]),
                bits,
                new Object[] { "   ", "  d", "P  ", 'P',
                        GregtechItemList.Circuit_T3RecipeSelector.getWildcard(1L, new Object[0]) });
    }
}
