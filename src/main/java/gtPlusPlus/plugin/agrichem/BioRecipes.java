package gtPlusPlus.plugin.agrichem;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.OreDictUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.plugin.agrichem.block.AgrichemFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.railcraft.utils.RailcraftUtils;
import ic2.core.Ic2Items;

public class BioRecipes {

    private static Item mFert;
    private static Item mDustDirt;

    private static Fluid mSalineWater;
    private static Fluid mDistilledWater;
    private static Fluid mThermalWater;
    private static Fluid mAir;
    private static Fluid mSulfuricWasteWater;
    private static Fluid mAmmonia;
    private static Fluid mMethanol;
    private static Fluid mAceticAcid;
    private static Fluid mPropionicAcid;
    private static Fluid mLiquidPlastic;
    private static Fluid mFermentationBase;
    private static Fluid mCarbonDioxide;
    private static Fluid mCarbonMonoxide;
    private static Fluid mEthylene;
    private static Fluid mEthanol;
    private static Fluid mChlorine;
    private static Fluid mHydrogen;
    private static Fluid mDilutedSulfuricAcid;
    private static Fluid mSulfuricAcid;
    private static Fluid mUrea;
    public static Fluid mFormaldehyde;
    private static Fluid mLiquidResin;
    private static Fluid mMethane;
    private static Fluid mBenzene;
    private static Fluid mEthylbenzene;
    private static Fluid mStyrene;
    private static Fluid mButanol;
    private static Fluid mAcetone;

    private static ItemStack getGreenAlgaeRecipeChip() {
        return getBioChip(4);
    }

    private static ItemStack getBrownAlgaeRecipeChip() {
        return getBioChip(8);
    }

    private static ItemStack getGoldenBrownAlgaeRecipeChip() {
        return getBioChip(12);
    }

    private static ItemStack getRedAlgaeRecipeChip() {
        return getBioChip(16);
    }

    private static ItemStack getBioChip(int aID) {
        return ItemUtils.simpleMetaStack(AgriculturalChem.mBioCircuit, aID, 0);
    }

    public static void init() {
        Logger.INFO("[Bio] Setting Variables");
        initRecipeVars();
        Logger.INFO("[Bio] Generating Biochip Recipes");
        recipeBioChip();
        Logger.INFO("[Bio] Generating Recipes");
        recipeAlgaeBiomass();
        Logger.INFO("[Bio] Finished with recipes");
    }

    private static void initRecipeVars() {
        mFert = AgriculturalChem.dustOrganicFertilizer;
        mDustDirt = AgriculturalChem.dustDirt;

        // 5.08 Salt Water Solution ;)
        if (!FluidUtils.doesFluidExist("saltwater")) {
            mSalineWater = FluidUtils
                .generateFluidNoPrefix("saltwater", "Salt Water", 200, new short[] { 10, 30, 220, 100 });
        } else {
            Materials aSaltWater = MaterialUtils.getMaterial("saltwater");
            if (aSaltWater != null) {
                FluidStack aWaterStack = aSaltWater.getFluid(1);
                if (aWaterStack != null) {
                    mSalineWater = aSaltWater.getFluid(1)
                        .getFluid();
                }
            }
            if (mSalineWater == null) {
                mSalineWater = FluidUtils.getWildcardFluidStack("saltwater", 1)
                    .getFluid();
            }
            if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellSaltWater", 1) == null) {
                new BaseItemComponent("saltwater", "Salt Water", new short[] { 10, 30, 220 });
            }
        }

        mDistilledWater = FluidUtils.getDistilledWater(1)
            .getFluid();
        mThermalWater = FluidUtils.getFluidStack("ic2hotwater", 1)
            .getFluid();
        mAir = FluidUtils.getFluidStack("air", 1)
            .getFluid();
        mSulfuricWasteWater = FluidUtils.getFluidStack("sulfuricapatite", 1)
            .getFluid();
        mAmmonia = MaterialMisc.AMMONIA.getFluidStack(1)
            .getFluid();
        mEthylene = FluidUtils.getFluidStack("ethylene", 1)
            .getFluid();
        mEthanol = FluidUtils.getFluidStack("bioethanol", 1)
            .getFluid();
        mDilutedSulfuricAcid = FluidUtils.getFluidStack("dilutedsulfuricacid", 1)
            .getFluid();
        mSulfuricAcid = FluidUtils.getFluidStack("sulfuricacid", 1)
            .getFluid();
        mFormaldehyde = FluidUtils.getFluidStack("fluid.formaldehyde", 1)
            .getFluid();
        mMethane = FluidUtils.getFluidStack("methane", 1)
            .getFluid();
        mBenzene = FluidUtils.getFluidStack("benzene", 1)
            .getFluid();
        mEthylbenzene = FluidUtils.getFluidStack("fluid.ethylbenzene", 1)
            .getFluid();
        mStyrene = FluidUtils.getFluidStack("styrene", 1)
            .getFluid();
        mMethanol = FluidUtils.getFluidStack("methanol", 1)
            .getFluid();
        mLiquidPlastic = FluidUtils.getWildcardFluidStack("plastic", 1)
            .getFluid();
        mCarbonDioxide = MaterialMisc.CARBON_DIOXIDE.getFluidStack(1)
            .getFluid();
        mCarbonMonoxide = MaterialMisc.CARBON_MONOXIDE.getFluidStack(1)
            .getFluid();
        mChlorine = FluidUtils.getFluidStack("chlorine", 1)
            .getFluid();
        mHydrogen = FluidUtils.getFluidStack("hydrogen", 1)
            .getFluid();
        mAceticAcid = AgrichemFluids.mAceticAcid;
        mPropionicAcid = AgrichemFluids.mPropionicAcid;
        mUrea = AgrichemFluids.mUrea;
        mLiquidResin = AgrichemFluids.mLiquidResin;
        mFermentationBase = AgrichemFluids.mFermentationBase;
        mButanol = AgrichemFluids.mButanol;
        mAcetone = AgrichemFluids.mAcetone;
    }

    private static void recipeAlgaeBiomass() {

        // TODO
        // Add in recipes to get initial Biomass

        recipeGreenAlgae();
        recipeBrownAlgae();
        recipeGoldenBrownAlgae();
        recipeRedAlgae();
        recipeWoodPellets();
        recipeWoodBricks();
        recipeCellulosePulp();
        recipeCatalystCarrier();
        recipeAluminiumSilverCatalyst();
        recipeAceticAcid();
        recipePropionicAcid();
        recipeFermentationBase();
        recipeEthanol();
        recipeCelluloseFibre();
        recipeGoldenBrownCelluloseFiber();
        recipeRedCelluloseFiber();
        recipeSodiumHydroxide();
        recipeSodiumCarbonate();
        recipePelletMold();
        recipeAluminiumPellet();
        recipeAlumina();
        recipeAluminium();
        recipeLithiumChloride();
        recipeSulfuricAcid();
        recipeUrea();
        recipeRawBioResin();
        recipeLiquidResin();
        recipeCompost();
        recipeMethane();
        recipeBenzene();
        recipeStyrene();
        registerFuels();
    }

    private static void registerFuels() {

        // Burnables
        ItemUtils.registerFuel(ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1), 800);
        ItemUtils.registerFuel(ItemUtils.getSimpleStack(AgriculturalChem.mWoodBrick, 1), 4800);

        // Combustion Fuels
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellButanol", 1))
            .metadata(FUEL_VALUE, 400)
            .metadata(FUEL_TYPE, 0)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);
    }

    private static void recipeGreenAlgae() {
        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 4))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Turn into Cellulose
        GTValues.RA.stdBuilder()
            .itemInputs(getGreenAlgaeRecipeChip(), ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 10))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 5))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
    }

    private static void recipeBrownAlgae() {
        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 2))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Alginic acid
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 10))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mAlginicAcid, 2))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(extractorRecipes);

        // Lithium Chloride
        GTValues.RA.stdBuilder()
            .itemInputs(getBrownAlgaeRecipeChip(), ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 20))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mLithiumChloride, 5))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // Sodium Carbonate
        GTValues.RA.stdBuilder()
            .itemInputs(getBrownAlgaeRecipeChip(), ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 40))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 20))
            .fluidInputs(FluidUtils.getDistilledWater(2000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void recipeGoldenBrownAlgae() {
        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 1))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Turn into Cellulose
        GTValues.RA.stdBuilder()
            .itemInputs(
                getGoldenBrownAlgaeRecipeChip(),
                ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 10))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 5))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    private static void recipeRedAlgae() {
        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 1))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 2))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Turn into Cellulose
        GTValues.RA.stdBuilder()
            .itemInputs(getRedAlgaeRecipeChip(), ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 10))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 5))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);
    }

    private static void recipeCelluloseFibre() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 8),
                ItemUtils.getSimpleStack(AgriculturalChem.mAlginicAcid, 2))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 10))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(UniversalChemical);

        // Craft into Wood Pellets
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(2), ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 12))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 24))
            .duration(2 * SECONDS + 8 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);

        // Methanol Extraction
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 12))
            .fluidOutputs(Materials.Methanol.getFluid(1000L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidExtractionRecipes);

        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 3))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Plastic
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(16), ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 4))
            .fluidInputs(
                FluidUtils.getFluidStack(BioRecipes.mAceticAcid, 500),
                FluidUtils.getFluidStack(BioRecipes.mPropionicAcid, 500))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mLiquidPlastic, (1000)))
            .duration(10 * SECONDS)
            .eut(240)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeWoodPellets() {
        // Shapeless Recipe
        RecipeUtils.addShapelessGregtechRecipe(
            new ItemStack[] { ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
                ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
                ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
                ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
                ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
                ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
                ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
                ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1) },
            ItemUtils.getSimpleStack(AgriculturalChem.mWoodBrick, 2));

        // Extruder Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 12),
                ItemUtils.getSimpleStack(AgriculturalChem.mPelletMold, 0))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 3))
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(extruderRecipes);

        // Assembly Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(2), ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 8))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mWoodBrick, 2))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        // CO2
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1))
            .fluidOutputs(FluidUtils.getFluidStack(mCarbonDioxide, 70))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidExtractionRecipes);

        // Add Charcoal Recipe
        if (Railcraft.isModLoaded()) {
            RailcraftUtils.addCokeOvenRecipe(
                ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 2),
                true,
                true,
                ItemUtils.getItemStackOfAmountFromOreDict("gemCharcoal", 3),
                GTValues.NF,
                1200);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 2), getBioChip(3))
            .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("gemCharcoal", 3))
            .eut(16)
            .duration(6 * SECONDS)
            .addTo(cokeOvenRecipes);
    }

    private static void recipeWoodBricks() {

        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(3), ItemUtils.getOrePrefixStack(OrePrefixes.dust, Materials.Wood, 50))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mWoodBrick, 1))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
    }

    private static void recipeCellulosePulp() {

        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(2), ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 4))
            .itemOutputs(ItemUtils.getSimpleStack(Items.paper, 4))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystCarrier() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(20),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 8L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 4L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Tin, 6L))
            .itemOutputs(CI.getEmptyCatalyst(1))
            .duration(5 * MINUTES)
            .eut(16)
            .addTo(assemblerRecipes);
    }

    private static void recipeAluminiumSilverCatalyst() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(4),
                CI.getEmptyCatalyst(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4L))
            .itemOutputs(CI.getGreenCatalyst(10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    private static void recipeAceticAcid() {

        // CH4O + CO = C2H4O2
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getGreenCatalyst(0))
            .fluidInputs(
                FluidUtils.getFluidStack(BioRecipes.mMethanol, 700),
                FluidUtils.getFluidStack(BioRecipes.mCarbonMonoxide, 700))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mAceticAcid, 700))
            .duration(2 * MINUTES)
            .eut(60)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedBioCircuit(14))
            .fluidInputs(FluidUtils.getFluidStack(mFermentationBase, 1000))
            .fluidOutputs(FluidUtils.getFluidStack(mAceticAcid, 1000))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 2))
            .duration(60 * SECONDS)
            .eut(16)
            .noOptimize()
            .addTo(chemicalDehydratorRecipes);
    }

    public static final HashSet<GTItemStack> mFruits = new HashSet<>();
    public static final HashSet<GTItemStack> mVege = new HashSet<>();
    public static final HashSet<GTItemStack> mNuts = new HashSet<>();
    public static final HashSet<GTItemStack> mSeeds = new HashSet<>();

    public static final AutoMap<ItemStack> mList_Master_FruitVege = new AutoMap<>();
    public static final AutoMap<ItemStack> mList_Master_Seeds = new AutoMap<>();

    private static void processFermentationOreDict() {
        processOreDictEntry("listAllfruit", mFruits);
        processOreDictEntry("listAllFruit", mFruits);
        processOreDictEntry("listAllveggie", mVege);
        processOreDictEntry("listAllVeggie", mVege);
        processOreDictEntry("listAllnut", mNuts);
        processOreDictEntry("listAllNut", mNuts);
        processOreDictEntry("listAllseed", mSeeds);
        processOreDictEntry("listAllSeed", mSeeds);

        if (!mFruits.isEmpty()) {
            for (GTItemStack g : mFruits) {
                mList_Master_FruitVege.put(g.toStack());
            }
        }
        if (!mVege.isEmpty()) {
            for (GTItemStack g : mVege) {
                mList_Master_FruitVege.put(g.toStack());
            }
        }
        if (!mNuts.isEmpty()) {
            for (GTItemStack g : mNuts) {
                mList_Master_FruitVege.put(g.toStack());
            }
        }
        if (!mSeeds.isEmpty()) {
            for (GTItemStack g : mSeeds) {
                mList_Master_Seeds.put(g.toStack());
            }
        }
    }

    // Make Fermentation
    private static void processOreDictEntry(String aOreName, HashSet<GTItemStack> mfruits2) {
        ArrayList<ItemStack> aTemp = OreDictionary.getOres(aOreName);
        if (!aTemp.isEmpty()) {
            for (ItemStack stack : aTemp) {
                mfruits2.add(new GTItemStack(stack));
            }
        }
    }

    private static void recipeFermentationBase() {
        processFermentationOreDict();
        AutoMap<ItemStack> aFruitVege = mList_Master_FruitVege;
        AutoMap<ItemStack> aSeeds = mList_Master_Seeds;
        ArrayList<ItemStack> aMap = OreDictionary.getOres("cropSugarbeet");
        for (ItemStack a : aFruitVege) {
            if (aMap.contains(a)) {
                continue;
            }
            if (ItemUtils.checkForInvalidItems(a)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(getBioChip(2), ItemUtils.getSimpleStack(a, 10))
                    .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000))
                    .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 1000))
                    .duration(30 * SECONDS)
                    .eut(2)
                    .metadata(CHEMPLANT_CASING_TIER, 0)
                    .addTo(chemicalPlantRecipes);

            }
        }
        for (ItemStack a : aSeeds) {
            if (ItemUtils.checkForInvalidItems(a)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(getBioChip(3), ItemUtils.getSimpleStack(a, 20))
                    .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000))
                    .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 1000))
                    .duration(30 * SECONDS)
                    .eut(2)
                    .metadata(CHEMPLANT_CASING_TIER, 0)
                    .addTo(chemicalPlantRecipes);

            }
        }

        // Sugar Cane
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(4), ItemUtils.getSimpleStack(Items.reeds, 32))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 0)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(5),
                ItemUtils.getSimpleStack(Items.reeds, 32),
                ItemUtils.getSimpleStack(ModItems.dustCalciumCarbonate, 2))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mThermalWater, 2000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 2000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 0)
            .addTo(chemicalPlantRecipes);

        // Sugar Beet
        if (OreDictUtils.containsValidEntries("cropSugarbeet")) {

            GTValues.RA.stdBuilder()
                .itemInputs(getBioChip(4), ItemUtils.getItemStackOfAmountFromOreDict("cropSugarbeet", 4))
                .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000))
                .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 1000))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .metadata(CHEMPLANT_CASING_TIER, 0)
                .addTo(chemicalPlantRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    getBioChip(5),
                    ItemUtils.getItemStackOfAmountFromOreDict("cropSugarbeet", 4),
                    ItemUtils.getSimpleStack(ModItems.dustCalciumCarbonate, 2))
                .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mThermalWater, 2000))
                .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 2000))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .metadata(CHEMPLANT_CASING_TIER, 0)
                .addTo(chemicalPlantRecipes);

        }

        // Produce Acetone, Butanol and Ethanol
        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(5),
                ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 6),
                ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 16))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 48000))
            .fluidOutputs(
                FluidUtils.getFluidStack(BioRecipes.mButanol, 18000),
                FluidUtils.getFluidStack(BioRecipes.mAcetone, 9000),
                FluidUtils.getFluidStack(BioRecipes.mEthanol, 3000))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .noOptimize()
            .addTo(chemicalPlantRecipes);
    }

    private static void recipePropionicAcid() {
        // C2H4 + CO + H2O = C3H6O2
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getGreenCatalyst(0))
            .fluidInputs(
                FluidUtils.getFluidStack(BioRecipes.mEthylene, 1000),
                FluidUtils.getFluidStack(BioRecipes.mCarbonMonoxide, 1000),
                FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mPropionicAcid, 1000))
            .duration(10 * SECONDS)
            .eut(60)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeEthanol() {

        GTValues.RA.stdBuilder()
            .itemInputs(BioRecipes.getBioChip(2))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 1000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mEthanol, 100))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);
    }

    private static void recipeGoldenBrownCelluloseFiber() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 5))
            .fluidOutputs(Materials.Ammonia.getGas(500))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);
    }

    private static void recipeRedCelluloseFiber() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 3))
            .itemOutputs(ItemUtils.getSimpleStack(ModItems.dustCalciumCarbonate, 5))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(extractorRecipes);
    }

    private static void recipeSodiumHydroxide() {
        // NaCl·H2O = NaOH + Cl + H
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(4))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mSodiumHydroxide, 3))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mSalineWater, 1000))
            .fluidOutputs(
                FluidUtils.getFluidStack(BioRecipes.mChlorine, 1000),
                FluidUtils.getFluidStack(BioRecipes.mHydrogen, 1000))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
        // Na + H2O = NaOH + H
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(5), ItemUtils.getItemStackOfAmountFromOreDict("dustSodium", 5))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mSodiumHydroxide, 15))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 5000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mHydrogen, 5000))
            .duration(60 * SECONDS)
            .eut(60)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeSodiumCarbonate() {

        if (OreDictUtils.containsValidEntries("fuelCoke")) {
            // Na2CO3 + Al2O3 =C= 2NaAlO2 + CO2
            GTValues.RA.stdBuilder()
                .itemInputs(
                    getBioChip(18),
                    ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 1),
                    ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 6),
                    ItemUtils.getSimpleStack(AgriculturalChem.mAluminiumPellet, 5))
                .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mSodiumAluminate, 8))
                .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mCarbonDioxide, 1000))
                .duration(2 * MINUTES)
                .eut(TierEU.RECIPE_MV)
                .metadata(CHEMPLANT_CASING_TIER, 1)
                .addTo(chemicalPlantRecipes);

        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(18),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCoal", 2),
                ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 6),
                ItemUtils.getSimpleStack(AgriculturalChem.mAluminiumPellet, 5))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mSodiumAluminate, 8))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mCarbonDioxide, 1000))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipePelletMold() {
        GregtechItemList.Pellet_Mold.set(ItemUtils.getSimpleStack(AgriculturalChem.mPelletMold, 1));
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsAlloy.TUMBAGA.getBlock(1))
            .itemOutputs(GregtechItemList.Pellet_Mold.get(1))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV / 4 * 3)
            .addTo(latheRecipes);
    }

    private static void recipeAluminiumPellet() {

        // Ore Names, no prefix
        AutoMap<String> aOreNames = new AutoMap<>();

        aOreNames.put("Lazurite");
        aOreNames.put("Bauxite");
        aOreNames.put("Grossular");
        aOreNames.put("Pyrope");
        aOreNames.put("Sodalite");
        aOreNames.put("Spodumene");
        aOreNames.put("Ruby");
        aOreNames.put("Sapphire");
        aOreNames.put("GreenSapphire");

        // Voltage
        HashMap<String, Integer> aOreData1 = new HashMap<>();
        // Input Count
        HashMap<String, Integer> aOreData2 = new HashMap<>();
        // Output Count
        HashMap<String, Integer> aOreData3 = new HashMap<>();

        aOreData1.put("Lazurite", 120);
        aOreData1.put("Bauxite", 90);
        aOreData1.put("Grossular", 90);
        aOreData1.put("Pyrope", 90);
        aOreData1.put("Sodalite", 90);
        aOreData1.put("Spodumene", 90);
        aOreData1.put("Ruby", 60);
        aOreData1.put("Sapphire", 30);
        aOreData1.put("GreenSapphire", 30);
        aOreData2.put("Lazurite", 14);
        aOreData2.put("Bauxite", 39);
        aOreData2.put("Grossular", 20);
        aOreData2.put("Pyrope", 20);
        aOreData2.put("Sodalite", 11);
        aOreData2.put("Spodumene", 10);
        aOreData2.put("Ruby", 6);
        aOreData2.put("Sapphire", 5);
        aOreData2.put("GreenSapphire", 5);
        aOreData3.put("Lazurite", 3);
        aOreData3.put("Bauxite", 16);
        aOreData3.put("Grossular", 2);
        aOreData3.put("Pyrope", 2);
        aOreData3.put("Sodalite", 3);
        aOreData3.put("Spodumene", 1);
        aOreData3.put("Ruby", 2);
        aOreData3.put("Sapphire", 2);
        aOreData3.put("GreenSapphire", 2);

        // Assemble all valid crushed ore types for making pellet mix
        HashMap<String, ItemStack> aOreCache = new HashMap<>();
        for (String aOreName : aOreNames) {
            String aTemp = aOreName;
            aOreName = "crushedPurified" + aOreName;
            if (ItemUtils.doesOreDictHaveEntryFor(aOreName)) {
                aOreCache.put(aTemp, ItemUtils.getItemStackOfAmountFromOreDict(aOreName, 1));
            }
        }

        for (String aOreName : aOreNames) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    CI.getNumberedBioCircuit(14),
                    ItemUtils.getSimpleStack(aOreCache.get(aOreName), aOreData2.get(aOreName)))
                .itemOutputs(
                    ItemUtils.getSimpleStack(
                        AgriculturalChem.mCleanAluminiumMix,
                        (int) (Math.ceil(aOreData3.get(aOreName) * 1.4))))
                .fluidInputs(FluidUtils.getSteam(2000 * aOreData2.get(aOreName)))
                .fluidOutputs(
                    FluidUtils
                        .getFluidStack(AgriculturalChem.RedMud, 100 * (int) (Math.ceil(aOreData3.get(aOreName) * 1.4))))
                .duration(60 * SECONDS)
                .eut(aOreData1.get(aOreName))
                .metadata(CHEMPLANT_CASING_TIER, aOreName.equals("Bauxite") ? 2 : 1)
                .addTo(chemicalPlantRecipes);

        }
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(AgriculturalChem.mCleanAluminiumMix, 3),
                ItemUtils.getSimpleStack(AgriculturalChem.mPelletMold, 0))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mAluminiumPellet, 4))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(extruderRecipes);
    }

    private static void recipeAlumina() {
        // 2NaAlO2 + 2NaOH + 2CO2 = Al2O3 + 2Na2CO3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(AgriculturalChem.mSodiumAluminate, 8),
                ItemUtils.getSimpleStack(AgriculturalChem.mSodiumHydroxide, 6))
            .itemOutputs(
                ItemUtils.getSimpleStack(AgriculturalChem.mAlumina, 5),
                ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 12))
            .fluidInputs(Materials.CarbonDioxide.getGas(2000L))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);
    }

    private static void recipeAluminium() {
        // 2Al2O3 + 3C = 4Al + 3CO2
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(AgriculturalChem.mAlumina, 10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4L))
            .fluidOutputs(Materials.CarbonDioxide.getGas(3000L))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1600)
            .addTo(blastFurnaceRecipes);
    }

    private static void recipeLithiumChloride() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.RockSalt, 8),
                ItemUtils.getSimpleStack(AgriculturalChem.mLithiumChloride, 10))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 2),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lithium, 3),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lithium, 3),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 2),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Potassium, 5))
            .outputChances(7500, 8000, 8500, 9000, 7500, 8500)
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mAir, 4000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mChlorine, 500))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(multiblockChemicalReactorRecipes);
        if (OreDictUtils.containsValidEntries("dustPotash")) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Potash, 10),
                    ItemUtils.getSimpleStack(AgriculturalChem.mLithiumChloride, 16))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 3),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lithium, 5),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lithium, 5),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 7),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 2),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 2))
                .outputChances(7500, 8000, 8500, 9000, 9000, 9000)
                .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mThermalWater, 2000))
                .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mChlorine, 250))
                .duration(1 * MINUTES)
                .eut(TierEU.RECIPE_MV)
                .addTo(multiblockChemicalReactorRecipes);
        }
    }

    private static void recipeSulfuricAcid() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(7),
                ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 10),
                ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 6))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 5000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mSulfuricAcid, 5000))
            .duration(50 * SECONDS)
            .eut(60)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(7),
                ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 2),
                ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 10))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 5000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mSulfuricAcid, 5000))
            .duration(6 * SECONDS)
            .eut(180)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeUrea() {

        // 2NH3 + CO2 = CH4N2O + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(9))
            .fluidInputs(
                FluidUtils.getFluidStack(BioRecipes.mAmmonia, 600),
                FluidUtils.getFluidStack(BioRecipes.mCarbonDioxide, 300))
            .fluidOutputs(
                FluidUtils.getFluidStack(BioRecipes.mUrea, 300),
                FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 300))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(9))
            .fluidInputs(
                FluidUtils.getFluidStack(BioRecipes.mUrea, 200),
                FluidUtils.getFluidStack(BioRecipes.mFormaldehyde, 200))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mLiquidResin, 200))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeRawBioResin() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(3),
                ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 5),
                ItemUtils.getSimpleStack(Blocks.dirt, 1))
            .itemOutputs(ItemUtils.getSimpleStack(AgriculturalChem.mRawBioResin, 1))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 100))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeLiquidResin() {

        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(3), ItemUtils.getSimpleStack(AgriculturalChem.mRawBioResin, 1))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mEthanol, 200))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mLiquidResin, 500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(3), ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 8))
            .itemOutputs(ItemUtils.getSimpleStack(Ic2Items.resin, 32))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mLiquidResin, 144))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeCompost() {
        ItemStack aFert;
        if (Forestry.isModLoaded()) {
            aFert = ItemUtils.getSimpleStack(AgriculturalChem.aFertForestry, 32);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    getBioChip(11),
                    ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 16),
                    ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 8))
                .itemOutputs(aFert)
                .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mUrea, 200))
                .duration(30 * SECONDS)
                .eut(60)
                .metadata(CHEMPLANT_CASING_TIER, 1)
                .addTo(chemicalPlantRecipes);

        }

        aFert = ItemUtils.getSimpleStack(AgriculturalChem.aFertIC2, 32);
        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(12),
                ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 16),
                ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 8))
            .itemOutputs(aFert)
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mUrea, 200))
            .duration(30 * SECONDS)
            .eut(60)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeMethane() {

        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(12), ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 10))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 500))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mMethane, 500))
            .duration(5 * SECONDS)
            .eut(64)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                getBioChip(13),
                ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 8),
                ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 6),
                ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 4))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mMethane, 2000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mEthylene, 2000))
            .duration(10 * SECONDS)
            .eut(60)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeBenzene() {

        // 6CH4 = C6H6 + 18H
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(19), CI.getGreenCatalyst(0))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mMethane, 6000))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mBenzene, 1000), Materials.Hydrogen.getGas(18000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeStyrene() {

        // C8H10 = C8H8 + 2H
        GTValues.RA.stdBuilder()
            .itemInputs(getBioChip(20), CI.getGreenCatalyst(0))
            .fluidInputs(FluidUtils.getFluidStack(BioRecipes.mEthylbenzene, 100))
            .fluidOutputs(FluidUtils.getFluidStack(BioRecipes.mStyrene, 100), Materials.Hydrogen.getGas(200))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeBioChip() {
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 0L),
            0,
            new Object[] { OrePrefixes.circuit.get(Materials.ULV) });

        long bits = 0;
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 1L),
            bits,
            new Object[] { "d  ", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 2L),
            bits,
            new Object[] { " d ", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 3L),
            bits,
            new Object[] { "  d", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 4L),
            bits,
            new Object[] { "   ", " Pd", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 5L),
            bits,
            new Object[] { "   ", " P ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 6L),
            bits,
            new Object[] { "   ", " P ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 7L),
            bits,
            new Object[] { "   ", " P ", "d  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 8L),
            bits,
            new Object[] { "   ", "dP ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 9L),
            bits,
            new Object[] { "P d", "   ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 10L),
            bits,
            new Object[] { "P  ", "  d", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 11L),
            bits,
            new Object[] { "P  ", "   ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 12L),
            bits,
            new Object[] { "P  ", "   ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 13L),
            bits,
            new Object[] { "  P", "   ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 14L),
            bits,
            new Object[] { "  P", "   ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 15L),
            bits,
            new Object[] { "  P", "   ", "d  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 16L),
            bits,
            new Object[] { "  P", "d  ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 17L),
            bits,
            new Object[] { "   ", "   ", "d P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 18L),
            bits,
            new Object[] { "   ", "d  ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 19L),
            bits,
            new Object[] { "d  ", "   ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 20L),
            bits,
            new Object[] { " d ", "   ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 21L),
            bits,
            new Object[] { "d  ", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 22L),
            bits,
            new Object[] { " d ", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 23L),
            bits,
            new Object[] { "  d", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
        addCraftingRecipe(
            GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 24L),
            bits,
            new Object[] { "   ", "  d", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L) });
    }

    public static boolean addCraftingRecipe(ItemStack aResult, long aBitMask, Object[] aRecipe) {
        Method mAddRecipe = ReflectionUtils.getMethod(
            GTModHandler.class,
            "addCraftingRecipe",
            ItemStack.class,
            Enchantment[].class,
            int[].class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            Object[].class);
        boolean didInvoke = false;
        if (mAddRecipe != null) {
            try {
                didInvoke = (boolean) mAddRecipe.invoke(
                    null,
                    aResult,
                    new Enchantment[] {},
                    new int[] {},
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    true,
                    aRecipe);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return didInvoke;
    }
}
