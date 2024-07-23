package gtPlusPlus.core.item.chemistry;

import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.semiFluidFuels;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.circuit.GTPP_IntegratedCircuit_Item;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.plugin.agrichem.BioRecipes;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAgrichemBase;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAlgaeBase;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class AgriculturalChem extends ItemPackage {

    private static boolean aBOP;
    private static boolean aTiCon;

    private static AutoMap<FluidStack> mBloodFluids = new AutoMap<>();

    /**
     * Fluids
     */

    // Poop Juice
    public static Fluid PoopJuice;
    // Manure Slurry
    public static Fluid ManureSlurry;
    // Fertile Manure Slurry
    public static Fluid FertileManureSlurry;
    // Blood
    public static Fluid CustomBlood;
    // Red Mud
    public static Fluid RedMud;
    /**
     * Items
     */

    // Manure Byproducts
    public static Item dustManureByproducts;
    // Organic Fertilizer
    public static Item dustOrganicFertilizer;
    // Dirt
    public static Item dustDirt;

    // Poop Juice
    // vv - Centrifuge
    // Manure Slurry && Manure Byproducts -> (Elements) Centrifuge to several tiny
    // piles
    // vv - Chem Reactor - Add Peat, Meat
    // Organic Fertilizer
    // vv - Dehydrate
    // Fertilizer

    // Poop Juice
    // vv - Mixer - Add Blood, Bone, Meat (1000L Poo, 200L Blood, x2 Bone, x3 Meat)
    // Fertile Manure Slurry
    // vv - Chem Reactor - Add Peat x1.5
    // Organic Fertilizer x3
    // vv - Dehydrate
    // Fertilizer

    public static Item mAlgae;
    public static Item mBioCircuit;
    public static Item mAgrichemItem1;

    /*
     * 0 - Algae Biomass 1 - Green Algae Biomass 2 - Brown Algae Biomass 3 - Golden-Brown Algae Biomass 4 - Red Algae
     * Biomass 5 - Cellulose Fiber 6 - Golden-Brown Cellulose Fiber 7 - Red Cellulose Fiber 8 - Compost 9 - Wood Pellet
     * 10 - Wood Brick 11 - Cellulose Pulp 12 - Raw Bio Resin 13 - Catalyst Carrier 14 - Green Metal Catalyst 15 -
     * Alginic Acid 16 - Alumina 17 - Aluminium Pellet 18 - Sodium Aluminate 19 - Sodium Hydroxide // Exists in Newer GT
     * 20 - Sodium Carbonate 21 - Lithium Chloride 22 - Pellet Mold 23 - Clean Aluminium Mix 24 - Pinecone
     */

    public static ItemStack mAlgaeBiosmass;
    public static ItemStack mGreenAlgaeBiosmass;
    public static ItemStack mBrownAlgaeBiosmass;
    public static ItemStack mGoldenBrownAlgaeBiosmass;
    public static ItemStack mRedAlgaeBiosmass;
    public static ItemStack mCelluloseFiber;
    public static ItemStack mGoldenBrownCelluloseFiber;
    public static ItemStack mRedCelluloseFiber;
    public static ItemStack mCompost;
    public static ItemStack mWoodPellet;
    public static ItemStack mWoodBrick;
    public static ItemStack mCellulosePulp;
    public static ItemStack mRawBioResin;
    public static ItemStack mCatalystCarrier;
    public static ItemStack mGreenCatalyst;
    public static ItemStack mAlginicAcid;
    public static ItemStack mAlumina;
    public static ItemStack mAluminiumPellet;
    public static ItemStack mSodiumAluminate;
    public static ItemStack mSodiumHydroxide;
    public static ItemStack mSodiumCarbonate;
    public static ItemStack mLithiumChloride;
    public static ItemStack mPelletMold;
    public static ItemStack mCleanAluminiumMix;
    public static ItemStack mPinecone;
    public static ItemStack mCrushedPine;

    @Override
    public void items() {
        // Nitrogen, Ammonium Nitrate, Phosphates, Calcium, Copper, Carbon
        dustManureByproducts = ItemUtils.generateSpecialUseDusts(
            "ManureByproducts",
            "Manure Byproduct",
            "(N2H4O3)N2P2Ca3CuC8",
            Utils.rgbtoHexValue(110, 75, 25))[0];

        // Basically Guano
        dustOrganicFertilizer = ItemUtils.generateSpecialUseDusts(
            "OrganicFertilizer",
            "Organic Fertilizer",
            "Ca5(PO4)3(OH)",
            Utils.rgbtoHexValue(240, 240, 240))[0];

        // Dirt Dust :)
        dustDirt = ItemUtils.generateSpecialUseDusts("Dirt", "Dried Earth", Utils.rgbtoHexValue(65, 50, 15))[0];

        mAlgae = new ItemAlgaeBase();
        mAgrichemItem1 = new ItemAgrichemBase();
        mBioCircuit = new GTPP_IntegratedCircuit_Item("BioRecipeSelector", "bioscience/BioCircuit");
        GregtechItemList.Circuit_BioRecipeSelector.set(mBioCircuit);

        mAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 0, 1);
        mGreenAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 1, 1);
        mBrownAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 2, 1);
        mGoldenBrownAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 3, 1);
        mRedAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 4, 1);
        mCelluloseFiber = ItemUtils.simpleMetaStack(mAgrichemItem1, 5, 1);
        mGoldenBrownCelluloseFiber = ItemUtils.simpleMetaStack(mAgrichemItem1, 6, 1);
        mRedCelluloseFiber = ItemUtils.simpleMetaStack(mAgrichemItem1, 7, 1);
        mCompost = ItemUtils.simpleMetaStack(mAgrichemItem1, 8, 1);
        mWoodPellet = ItemUtils.simpleMetaStack(mAgrichemItem1, 9, 1);
        mWoodBrick = ItemUtils.simpleMetaStack(mAgrichemItem1, 10, 1);
        mCellulosePulp = ItemUtils.simpleMetaStack(mAgrichemItem1, 11, 1);
        mRawBioResin = ItemUtils.simpleMetaStack(mAgrichemItem1, 12, 1);
        mCatalystCarrier = ItemUtils.simpleMetaStack(mAgrichemItem1, 13, 1);
        mGreenCatalyst = ItemUtils.simpleMetaStack(mAgrichemItem1, 14, 1);
        mAlginicAcid = ItemUtils.simpleMetaStack(mAgrichemItem1, 15, 1);
        mAlumina = ItemUtils.simpleMetaStack(mAgrichemItem1, 16, 1);
        mAluminiumPellet = ItemUtils.simpleMetaStack(mAgrichemItem1, 17, 1);
        mSodiumAluminate = ItemUtils.simpleMetaStack(mAgrichemItem1, 18, 1);

        /*
         * If It exists, don't add a new one.
         */
        if (OreDictionary.doesOreNameExist("dustSodiumHydroxide_GT5U")
            || OreDictionary.doesOreNameExist("dustSodiumHydroxide")) {
            List<ItemStack> aTest = OreDictionary.getOres("dustSodiumHydroxide", false);
            ItemStack aTestStack;
            if (aTest.isEmpty()) {
                aTest = OreDictionary.getOres("dustSodiumHydroxide_GT5U", false);
                if (aTest.isEmpty()) {
                    aTestStack = ItemUtils.simpleMetaStack(mAgrichemItem1, 19, 1);
                } else {
                    aTestStack = aTest.get(0);
                }
            } else {
                aTestStack = aTest.get(0);
            }
            mSodiumHydroxide = aTestStack;
        } else {
            mSodiumHydroxide = ItemUtils.simpleMetaStack(mAgrichemItem1, 19, 1);
        }
        mSodiumCarbonate = ItemUtils.simpleMetaStack(mAgrichemItem1, 20, 1);
        mLithiumChloride = ItemUtils.simpleMetaStack(mAgrichemItem1, 21, 1);
        mPelletMold = ItemUtils.simpleMetaStack(mAgrichemItem1, 22, 1);
        mCleanAluminiumMix = ItemUtils.simpleMetaStack(mAgrichemItem1, 23, 1);
        mPinecone = ItemUtils.simpleMetaStack(mAgrichemItem1, 24, 1);
        mCrushedPine = ItemUtils.simpleMetaStack(mAgrichemItem1, 25, 1);

        ItemUtils.addItemToOreDictionary(mGreenAlgaeBiosmass, "biomassGreenAlgae");
        ItemUtils.addItemToOreDictionary(mBrownAlgaeBiosmass, "biomassBrownAlgae");
        ItemUtils.addItemToOreDictionary(mGoldenBrownAlgaeBiosmass, "biomassGoldenBrownAlgae");
        ItemUtils.addItemToOreDictionary(mRedAlgaeBiosmass, "biomassRedAlgae");

        ItemUtils.addItemToOreDictionary(mCelluloseFiber, "fiberCellulose");
        ItemUtils.addItemToOreDictionary(mGoldenBrownCelluloseFiber, "fiberCellulose");
        ItemUtils.addItemToOreDictionary(mGoldenBrownCelluloseFiber, "fiberGoldenBrownCellulose");
        ItemUtils.addItemToOreDictionary(mRedCelluloseFiber, "fiberCellulose");
        ItemUtils.addItemToOreDictionary(mRedCelluloseFiber, "fiberRedCellulose");

        ItemUtils.addItemToOreDictionary(mWoodPellet, "pelletWood");
        ItemUtils.addItemToOreDictionary(mWoodBrick, "brickWood");
        ItemUtils.addItemToOreDictionary(mCellulosePulp, "pulpCellulose");

        ItemUtils.addItemToOreDictionary(mCatalystCarrier, "catalystEmpty");
        ItemUtils.addItemToOreDictionary(mGreenCatalyst, "catalystAluminiumSilver");
        ItemUtils.addItemToOreDictionary(mAlginicAcid, "dustAlginicAcid");
        ItemUtils.addItemToOreDictionary(mAlumina, "dustAlumina");
        ItemUtils.addItemToOreDictionary(mAluminiumPellet, "pelletAluminium");

        ItemUtils.addItemToOreDictionary(mSodiumAluminate, "dustSodiumAluminate");
        ItemUtils.addItemToOreDictionary(mSodiumHydroxide, "dustSodiumHydroxide");
        ItemUtils.addItemToOreDictionary(mSodiumCarbonate, "dustSodiumCarbonate");
        ItemUtils.addItemToOreDictionary(mLithiumChloride, "dustLithiumChloride");
        ItemUtils.addItemToOreDictionary(mPinecone, "pinecone");
        ItemUtils.addItemToOreDictionary(mCrushedPine, "crushedPineMaterial");

        // Handle GT NaOH dusts
        List<ItemStack> NaOHSmall = OreDictionary.getOres("dustSmallSodiumHydroxide_GT5U", false);
        if (!NaOHSmall.isEmpty()) {
            ItemUtils.addItemToOreDictionary(NaOHSmall.get(0), "dustSmallSodiumHydroxide");
        }
        List<ItemStack> NaOHTiny = OreDictionary.getOres("dustTinySodiumHydroxide_GT5U", false);
        if (!NaOHTiny.isEmpty()) {
            ItemUtils.addItemToOreDictionary(NaOHTiny.get(0), "dustTinySodiumHydroxide");
        }
    }

    @Override
    public void blocks() {
        // None yet
    }

    @Override
    public void fluids() {
        // Sewage
        PoopJuice = FluidUtils.generateFluidNonMolten(
            "raw.waste",
            "Raw Animal Waste",
            32 + 175,
            new short[] { 100, 70, 30, 100 },
            null,
            null,
            0,
            true);

        // Sewage
        ManureSlurry = FluidUtils.generateFluidNonMolten(
            "manure.slurry",
            "Manure Slurry",
            39 + 175,
            new short[] { 75, 45, 15, 100 },
            null,
            null,
            0,
            true);

        // Sewage
        FertileManureSlurry = FluidUtils.generateFluidNonMolten(
            "fertile.manure.slurry",
            "Fertile Manure Slurry",
            45 + 175,
            new short[] { 65, 50, 15, 100 },
            null,
            null,
            0,
            true);

        RedMud = FluidUtils.generateFluidNoPrefix(
            "mud.red.slurry",
            "Red Mud Slurry",
            32 + 175,
            new short[] { 180, 35, 25, 100 },
            true);
    }

    public AgriculturalChem() {
        super();

        aBOP = BiomesOPlenty.isModLoaded();
        aTiCon = TinkerConstruct.isModLoaded();

        Logger.INFO("Adding Agrochemical content");

        FluidStack aBlood;
        if (aBOP) {
            aBlood = FluidUtils.getFluidStack("hell_blood", 100);
            if (aBlood != null) {
                Logger.INFO("Found Biome's o Plenty, enabled Blood support.");
                CustomBlood = aBlood.getFluid();
                mBloodFluids.put(aBlood);
            }
        }

        if (aTiCon) {
            aBlood = FluidUtils.getFluidStack("blood", 100);
            if (aBlood != null) {
                Logger.INFO("Found Tinker's Construct, enabled Blood support.");
                CustomBlood = aBlood.getFluid();
                mBloodFluids.put(FluidUtils.getFluidStack("blood", 100));
            }
        }

        // Handle Blood Internally, Create if required.
        if (mBloodFluids.isEmpty() || CustomBlood == null) {
            Logger.INFO(
                "Did not find any existing Blood fluids. Trying to wildcard search the fluid registry, then generate our own if that fails.");
            FluidStack aTempBlood = FluidUtils.getWildcardFluidStack("blood", 100);
            if (aTempBlood != null) {
                CustomBlood = aTempBlood.getFluid();
            } else {
                aTempBlood = FluidUtils.getWildcardFluidStack("hell_blood", 100);
                if (aTempBlood == null) {
                    CustomBlood = FluidUtils
                        .generateFluidNoPrefix("blood", "Blood", 32 + 175, new short[] { 175, 25, 25, 100 }, true);
                } else {
                    CustomBlood = aTempBlood.getFluid();
                }
            }
            Logger.INFO("Using " + CustomBlood.getName());
            mBloodFluids.put(FluidUtils.getFluidStack(CustomBlood, 100));
        }
    }

    private static final AutoMap<ItemStack> mMeats = new AutoMap<>();
    private static final AutoMap<ItemStack> mFish = new AutoMap<>();
    private static final AutoMap<ItemStack> mFruits = new AutoMap<>();
    private static final AutoMap<ItemStack> mVege = new AutoMap<>();
    private static final AutoMap<ItemStack> mNuts = new AutoMap<>();
    private static final AutoMap<ItemStack> mSeeds = new AutoMap<>();
    private static final AutoMap<ItemStack> mPeat = new AutoMap<>();
    private static final AutoMap<ItemStack> mBones = new AutoMap<>();
    private static final AutoMap<ItemStack> mBoneMeal = new AutoMap<>();

    private static final AutoMap<ItemStack> mList_Master_Meats = new AutoMap<>();
    private static final AutoMap<ItemStack> mList_Master_FruitVege = new AutoMap<>();
    private static final AutoMap<ItemStack> mList_Master_Seeds = new AutoMap<>();
    private static final AutoMap<ItemStack> mList_Master_Bones = new AutoMap<>();

    private static void processAllOreDict() {
        processOreDict("listAllmeatraw", mMeats);
        processOreDict("listAllfishraw", mFish);
        processOreDict("listAllfruit", mFruits);
        processOreDict("listAllVeggie", mVege);
        processOreDict("listAllnut", mNuts);
        processOreDict("listAllSeed", mSeeds);
        processOreDict("brickPeat", mPeat);
        processOreDict("bone", mBones);
        processOreDict("dustBone", mBoneMeal);
        // Just make a mega list, makes life easier.
        if (!mMeats.isEmpty()) {
            for (ItemStack g : mMeats) {
                mList_Master_Meats.put(g);
            }
        }
        if (!mFish.isEmpty()) {
            for (ItemStack g : mFish) {
                mList_Master_Meats.put(g);
            }
        }
        if (!mFruits.isEmpty()) {
            for (ItemStack g : mFruits) {
                mList_Master_FruitVege.put(g);
            }
        }
        if (!mVege.isEmpty()) {
            for (ItemStack g : mVege) {
                mList_Master_FruitVege.put(g);
            }
        }
        if (!mNuts.isEmpty()) {
            for (ItemStack g : mNuts) {
                mList_Master_FruitVege.put(g);
            }
        }
        if (!mSeeds.isEmpty()) {
            for (ItemStack g : mSeeds) {
                mList_Master_Seeds.put(g);
            }
        }
        if (!mBoneMeal.isEmpty()) {
            for (ItemStack g : mBoneMeal) {
                mList_Master_Bones.put(g);
            }
        }
        if (!mBones.isEmpty()) {
            for (ItemStack g : mBones) {
                mList_Master_Bones.put(g);
            }
        }
    }

    private static void processOreDict(String aOreName, AutoMap<ItemStack> aMap) {
        ArrayList<ItemStack> aTemp = OreDictionary.getOres(aOreName);
        if (!aTemp.isEmpty()) {
            for (ItemStack stack : aTemp) {
                aMap.put(stack);
            }
        }
    }

    private static void addBasicSlurryRecipes() {

        ItemStack aManureByprod1 = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyManureByproducts", 1);
        ItemStack aManureByprod2 = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1);
        ItemStack aDirtDust = ItemUtils.getSimpleStack(dustDirt, 1);

        // Poop Juice to Basic Slurry
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(10))
            .itemOutputs(aDirtDust, aDirtDust, aManureByprod1, aManureByprod1, aManureByprod1, aManureByprod1)
            .outputChances(2000, 2000, 500, 500, 250, 250)
            .fluidInputs(FluidUtils.getFluidStack(PoopJuice, 1000))
            .fluidOutputs(FluidUtils.getFluidStack(ManureSlurry, 250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        // More Efficient way to get byproducts, less Slurry
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(aDirtDust, aDirtDust, aManureByprod1, aManureByprod1, aManureByprod2, aManureByprod2)
            .outputChances(4000, 3000, 1250, 1250, 675, 675)
            .fluidInputs(FluidUtils.getFluidStack(PoopJuice, 1000))
            .fluidOutputs(FluidUtils.getFluidStack(ManureSlurry, 50))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(centrifugeRecipes);
    }

    private static void addAdvancedSlurryRecipes() {

        ItemStack aCircuit = GT_Utility.getIntegratedCircuit(10);
        ItemStack aBone;
        ItemStack aMeat;
        ItemStack aEmptyCells = Materials.Empty.getCells(2);
        ItemStack aInputCells = ItemUtils.getItemStackOfAmountFromOreDict("cellRawAnimalWaste", 2);
        FluidStack aOutput = FluidUtils.getFluidStack(FertileManureSlurry, 1000);

        for (FluidStack aBloodStack : mBloodFluids) {
            for (ItemStack aBoneStack : mList_Master_Bones) {
                aBone = ItemUtils.getSimpleStack(aBoneStack, 2);
                for (ItemStack aMeatStack : mList_Master_Meats) {
                    aMeat = ItemUtils.getSimpleStack(aMeatStack, 5);
                    // Poop Juice to Fertile Slurry
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aCircuit, aBone, aMeat, aInputCells)
                        .itemOutputs(aEmptyCells)
                        .fluidInputs(aBloodStack)
                        .fluidOutputs(aOutput)
                        .duration(8 * SECONDS)
                        .eut(TierEU.RECIPE_MV / 2)
                        .addTo(mixerRecipes);
                }
            }
        }
    }

    private static void addBasicOrganiseFertRecipes() {
        FluidStack aInputFluid = FluidUtils.getFluidStack(ManureSlurry, 1000);
        ItemStack aOutputDust = ItemUtils.getSimpleStack(dustOrganicFertilizer, 3);
        ItemStack aPeat;
        ItemStack aMeat;
        for (ItemStack aPeatStack : mPeat) {
            aPeat = ItemUtils.getSimpleStack(aPeatStack, 3);
            for (ItemStack aMeatStack : mList_Master_Meats) {
                aMeat = ItemUtils.getSimpleStack(aMeatStack, 5);
                GT_Values.RA.stdBuilder()
                    .itemInputs(aPeat, aMeat)
                    .itemOutputs(aOutputDust)
                    .fluidInputs(aInputFluid)
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(UniversalChemical);
            }
            aPeat = ItemUtils.getSimpleStack(aPeatStack, 2);
            for (ItemStack aMeatStack : mList_Master_FruitVege) {
                aMeat = ItemUtils.getSimpleStack(aMeatStack, 9);
                GT_Values.RA.stdBuilder()
                    .itemInputs(aPeat, aMeat)
                    .itemOutputs(aOutputDust)
                    .fluidInputs(aInputFluid)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(UniversalChemical);
            }
        }
    }

    private static void addAdvancedOrganiseFertRecipes() {
        FluidStack aInputFluid = FluidUtils.getFluidStack(FertileManureSlurry, 1000);
        ItemStack aOutputDust = ItemUtils.getSimpleStack(dustOrganicFertilizer, 7);
        ItemStack aPeat;
        ItemStack aMeat;
        for (ItemStack aPeatStack : mPeat) {
            aPeat = ItemUtils.getSimpleStack(aPeatStack, 5);
            for (ItemStack aMeatStack : mList_Master_Meats) {
                aMeat = ItemUtils.getSimpleStack(aMeatStack, 7);
                GT_Values.RA.stdBuilder()
                    .itemInputs(aPeat, aMeat)
                    .itemOutputs(aOutputDust)
                    .fluidInputs(aInputFluid)
                    .duration(10 * SECONDS)
                    .eut(140)
                    .addTo(UniversalChemical);
            }
            aPeat = ItemUtils.getSimpleStack(aPeatStack, 3);
            for (ItemStack aMeatStack : mList_Master_FruitVege) {
                aMeat = ItemUtils.getSimpleStack(aMeatStack, 12);
                GT_Values.RA.stdBuilder()
                    .itemInputs(aPeat, aMeat)
                    .itemOutputs(aOutputDust)
                    .fluidInputs(aInputFluid)
                    .duration(5 * SECONDS)
                    .eut(140)
                    .addTo(UniversalChemical);
            }
        }
    }

    public static ItemStack aFertForestry;
    public static ItemStack aFertIC2;

    private static void addMiscRecipes() {

        ItemStack aDustOrganicFert = ItemUtils.getSimpleStack(dustOrganicFertilizer, 1);
        ItemStack aManureByprod = ItemUtils.getSimpleStack(dustManureByproducts, 1);

        // Dehydrate Organise Fert to Normal Fert.

        /*
         * Forestry Support
         */
        if (Forestry.isModLoaded()) {
            Field aItemField = ReflectionUtils
                .getField(ReflectionUtils.getClass("forestry.plugins.PluginCore"), "items");
            try {
                Object aItemRegInstance = aItemField != null ? aItemField.get(aItemField) : null;
                if (aItemRegInstance != null) {
                    Field aFertField = ReflectionUtils.getField(aItemRegInstance.getClass(), "fertilizerCompound");
                    Object aItemInstance = aFertField.get(aItemRegInstance);
                    if (aItemInstance instanceof Item aForestryFert) {
                        aFertForestry = ItemUtils.getSimpleStack((Item) aItemInstance);

                        GT_Values.RA.stdBuilder()
                            .itemInputs(
                                GT_Utility.getIntegratedCircuit(11),
                                ItemUtils.getSimpleStack(aDustOrganicFert, 4))
                            .itemOutputs(ItemUtils.getSimpleStack(aForestryFert, 3), aManureByprod, aManureByprod)
                            .outputChances(100_00, 20_00, 20_00)
                            .eut(240)
                            .duration(20 * SECONDS)
                            .addTo(chemicalDehydratorRecipes);
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {

            }
        }

        /*
         * IC2 Support
         */
        aFertIC2 = ItemUtils.getItemStackFromFQRN("IC2:itemFertilizer", 1);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(12), ItemUtils.getSimpleStack(aDustOrganicFert, 4))
            .itemOutputs(ItemUtils.getItemStackFromFQRN("IC2:itemFertilizer", 3), aManureByprod, aManureByprod)
            .outputChances(100_00, 20_00, 20_00)
            .eut(240)
            .duration(20 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Dirt Production
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(dustDirt, 9))
            .itemOutputs(ItemUtils.getSimpleStack(Blocks.dirt))
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(compressorRecipes);

        // Centrifuge Byproducts

        // Ammonium Nitrate, Phosphates, Calcium, Copper, Carbon
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(aManureByprod, 4), GT_Utility.getIntegratedCircuit(20))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Phosphorus, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Calcium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Copper, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                ItemUtils.getSimpleStack(dustDirt, 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAmmoniumNitrate", 1))
            .outputChances(2500, 2500, 750, 1000, 5000, 250)
            .fluidInputs(Materials.SulfuricAcid.getFluid(250))
            .fluidOutputs(FluidUtils.getFluidStack("sulfuricapatite", 50))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(centrifugeRecipes);

        // Add Fuel Usages
        GT_Values.RA.stdBuilder()
            .fluidInputs(FluidUtils.getFluidStack(PoopJuice, 1000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 12)
            .addTo(semiFluidFuels);

        GT_Values.RA.stdBuilder()
            .fluidInputs(FluidUtils.getFluidStack(ManureSlurry, 1000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 24)
            .addTo(semiFluidFuels);

        GT_Values.RA.stdBuilder()
            .fluidInputs(FluidUtils.getFluidStack(FertileManureSlurry, 1000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 32)
            .addTo(semiFluidFuels);

        // Red Slurry / Tailings Processing
        GT_Values.RA.stdBuilder()
            .itemInputs(CI.getNumberedBioCircuit(10))
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Copper, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Tin, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Nickel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lead, 1L))
            .outputChances(3000, 3000, 2000, 2000, 1000, 1000)
            .fluidInputs(FluidUtils.getFluidStack(AgriculturalChem.RedMud, 1000))
            .fluidOutputs(Materials.Water.getFluid(500))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);
    }

    @Override
    public String errorMessage() {
        return "Failed to generate recipes for AgroChem.";
    }

    @Override
    public boolean generateRecipes() {
        if (mBloodFluids.isEmpty()) {
            Logger.INFO("Could not find, nor create Blood fluid. Unable to add recipes.");
            return false;
        }

        // Organise OreDict
        processAllOreDict();

        // Slurry Production
        addBasicSlurryRecipes();
        addAdvancedSlurryRecipes();

        // Organic Fert. Production
        addBasicOrganiseFertRecipes();
        addAdvancedOrganiseFertRecipes();

        addMiscRecipes();

        BioRecipes.init();

        return true;
    }
}
