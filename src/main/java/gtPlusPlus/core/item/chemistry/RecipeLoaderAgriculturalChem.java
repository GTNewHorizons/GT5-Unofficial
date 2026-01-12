package gtPlusPlus.core.item.chemistry;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.semiFluidFuels;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.Optional;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.agrichem.BioRecipes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoaderAgriculturalChem {

    private static final ArrayList<ItemStack> mMeats = new ArrayList<>();
    private static final ArrayList<ItemStack> mFish = new ArrayList<>();
    private static final ArrayList<ItemStack> mFruits = new ArrayList<>();
    private static final ArrayList<ItemStack> mVege = new ArrayList<>();
    private static final ArrayList<ItemStack> mNuts = new ArrayList<>();
    private static final ArrayList<ItemStack> mSeeds = new ArrayList<>();
    private static final ArrayList<ItemStack> mPeat = new ArrayList<>();
    private static final ArrayList<ItemStack> mBones = new ArrayList<>();
    private static final ArrayList<ItemStack> mBoneMeal = new ArrayList<>();

    private static final ArrayList<ItemStack> mList_Master_Meats = new ArrayList<>();
    private static final ArrayList<ItemStack> mList_Master_FruitVege = new ArrayList<>();
    private static final ArrayList<ItemStack> mList_Master_Bones = new ArrayList<>();
    private static final ArrayList<ItemStack> mList_Master_Seeds = new ArrayList<>();

    public static void generate() {

        // Organise OreDict
        processAllOreDict();

        // Slurry Production
        addBasicSlurryRecipes();
        addAdvancedSlurryRecipes();

        // Organic Fert. Production
        addBasicOrganiseFertRecipes();
        addAdvancedOrganiseFertRecipes();

        recipeFermentationBase();
        addMiscRecipes();

        BioRecipes.init();
    }

    private static void processAllOreDict() {
        processOreDict("listAllmeatraw", mMeats);
        processOreDict("listAllfishraw", mFish);
        processOreDict("listAllfruit", mFruits);
        processOreDict("listAllveggie", mVege);
        processOreDict("listAllnut", mNuts);
        processOreDict("listAllseed", mSeeds);
        processOreDict("brickPeat", mPeat);
        processOreDict("bone", mBones);
        processOreDict("dustBone", mBoneMeal);
        // Just make a mega list, makes life easier.
        if (!mMeats.isEmpty()) {
            mList_Master_Meats.addAll(mMeats);
        }
        if (!mFish.isEmpty()) {
            for (ItemStack g : mFish) {
                boolean foundDupe = false;
                for (ItemStack old : mList_Master_Meats) {
                    if (GTUtility.areStacksEqual(g, old)) {
                        foundDupe = true;
                        break;
                    }
                }
                if (foundDupe) continue;
                mList_Master_Meats.add(g);
            }
        }
        if (!mFruits.isEmpty()) {
            mList_Master_FruitVege.addAll(mFruits);
        }
        if (!mVege.isEmpty()) {
            mList_Master_FruitVege.addAll(mVege);
        }
        if (!mNuts.isEmpty()) {
            mList_Master_FruitVege.addAll(mNuts);
        }
        if (!mBoneMeal.isEmpty()) {
            mList_Master_Bones.addAll(mBoneMeal);
        }
        if (!mSeeds.isEmpty()) {
            mList_Master_Seeds.addAll(mSeeds);
        }
        if (!mBones.isEmpty()) {
            for (ItemStack g : mBones) {
                boolean foundDupe = false;
                for (ItemStack old : mList_Master_Bones) {
                    if (GTUtility.areStacksEqual(g, old)) {
                        foundDupe = true;
                        break;
                    }
                }
                if (foundDupe) continue;
                mList_Master_Bones.add(g);
            }
        }
    }

    private static void processOreDict(String aOreName, ArrayList<ItemStack> aMap) {
        ArrayList<ItemStack> aTemp = OreDictionary.getOres(aOreName);
        if (!aTemp.isEmpty()) {
            aMap.addAll(aTemp);
        }
    }

    private static void addBasicSlurryRecipes() {
        ItemStack aManureByprod1 = GregtechItemList.TinyManureByproductsDust.get(1);
        ItemStack aManureByprod2 = GregtechItemList.SmallManureByproductsDust.get(1);
        ItemStack aDirtDust = GregtechItemList.DriedEarthDust.get(1);

        // Poop Juice to Basic Slurry
        GTValues.RA.stdBuilder()
            .circuit(21)
            .itemOutputs(aDirtDust, aDirtDust, aManureByprod1, aManureByprod1, aManureByprod1, aManureByprod1)
            .outputChances(2000, 2000, 500, 500, 250, 250)
            .fluidInputs(new FluidStack(GTPPFluids.PoopJuice, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.ManureSlurry, 250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        // More Efficient way to get byproducts, less Slurry
        GTValues.RA.stdBuilder()
            .circuit(20)
            .itemOutputs(aDirtDust, aDirtDust, aManureByprod1, aManureByprod1, aManureByprod2, aManureByprod2)
            .outputChances(4000, 3000, 1250, 1250, 675, 675)
            .fluidInputs(new FluidStack(GTPPFluids.PoopJuice, 1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.ManureSlurry, 50))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(centrifugeRecipes);
    }

    private static void addAdvancedSlurryRecipes() {
        ItemStack aBone;
        ItemStack aMeat;
        ItemStack aEmptyCells = Materials.Empty.getCells(2);
        ItemStack aInputCells = ItemUtils.getItemStackOfAmountFromOreDict("cellRawAnimalWaste", 2);
        FluidStack aOutput = new FluidStack(GTPPFluids.FertileManureSlurry, 1_000);

        for (FluidStack aBloodStack : GTPPFluids.getBloodFluids()) {
            for (ItemStack aBoneStack : mList_Master_Bones) {
                aBone = GTUtility.copyAmount(2, aBoneStack);
                for (ItemStack aMeatStack : mList_Master_Meats) {
                    aMeat = GTUtility.copyAmount(5, aMeatStack);
                    // Poop Juice to Fertile Slurry
                    GTValues.RA.stdBuilder()
                        .itemInputs(aBone, aMeat, aInputCells)
                        .circuit(10)
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
        FluidStack aInputFluid = new FluidStack(GTPPFluids.ManureSlurry, 1_000);
        ItemStack aOutputDust = GregtechItemList.OrganicFertilizerDust.get(3);
        ItemStack aPeat;
        ItemStack aMeat;
        for (ItemStack aPeatStack : mPeat) {
            aPeat = GTUtility.copyAmount(3, aPeatStack);
            for (ItemStack aMeatStack : mList_Master_Meats) {
                aMeat = GTUtility.copyAmount(5, aMeatStack);
                GTValues.RA.stdBuilder()
                    .itemInputs(aPeat, aMeat)
                    .itemOutputs(aOutputDust)
                    .fluidInputs(aInputFluid)
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(UniversalChemical);
            }
            aPeat = GTUtility.copyAmount(2, aPeatStack);
            for (ItemStack aMeatStack : mList_Master_FruitVege) {
                aMeat = GTUtility.copyAmount(9, aMeatStack);
                GTValues.RA.stdBuilder()
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
        FluidStack aInputFluid = new FluidStack(GTPPFluids.FertileManureSlurry, 1_000);
        ItemStack aOutputDust = GregtechItemList.OrganicFertilizerDust.get(7);
        ItemStack aPeat;
        ItemStack aMeat;
        for (ItemStack aPeatStack : mPeat) {
            aPeat = GTUtility.copyAmount(5, aPeatStack);
            for (ItemStack aMeatStack : mList_Master_Meats) {
                aMeat = GTUtility.copyAmount(7, aMeatStack);
                GTValues.RA.stdBuilder()
                    .itemInputs(aPeat, aMeat)
                    .itemOutputs(aOutputDust)
                    .fluidInputs(aInputFluid)
                    .duration(10 * SECONDS)
                    .eut(140)
                    .addTo(UniversalChemical);
            }
            aPeat = GTUtility.copyAmount(3, aPeatStack);
            for (ItemStack aMeatStack : mList_Master_FruitVege) {
                aMeat = GTUtility.copyAmount(12, aMeatStack);
                GTValues.RA.stdBuilder()
                    .itemInputs(aPeat, aMeat)
                    .itemOutputs(aOutputDust)
                    .fluidInputs(aInputFluid)
                    .duration(5 * SECONDS)
                    .eut(140)
                    .addTo(UniversalChemical);
            }
        }
    }

    private static void recipeFermentationBase() {
        List<ItemStack> aMap = OreDictionary.getOres("cropSugarbeet");
        for (ItemStack a : mList_Master_FruitVege) {
            if (aMap.contains(a)) {
                continue;
            }
            if (a != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(10, a))
                    .circuit(2)
                    .fluidInputs(GTModHandler.getDistilledWater(1_000))
                    .fluidOutputs(new FluidStack(GTPPFluids.FermentationBase, 1_000))
                    .duration(30 * SECONDS)
                    .eut(2)
                    .metadata(CHEMPLANT_CASING_TIER, 0)
                    .addTo(chemicalPlantRecipes);
            }
        }

        for (ItemStack a : mList_Master_Seeds) {
            if (a != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(20, a))
                    .circuit(3)
                    .fluidInputs(GTModHandler.getDistilledWater(1_000))
                    .fluidOutputs(new FluidStack(GTPPFluids.FermentationBase, 1_000))
                    .duration(30 * SECONDS)
                    .eut(2)
                    .metadata(CHEMPLANT_CASING_TIER, 0)
                    .addTo(chemicalPlantRecipes);
            }
        }

        // Sugar Cane
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.reeds, 32))
            .circuit(4)
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.FermentationBase, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 0)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.reeds, 32), GregtechItemList.CalciumCarbonateDust.get(2))
            .circuit(5)
            .fluidInputs(GTModHandler.getHotWater(2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.FermentationBase, 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 0)
            .addTo(chemicalPlantRecipes);

        // Sugar Beet
        if (!GTOreDictUnificator.getOres("cropSugarbeet")
            .isEmpty()) {

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("cropSugarbeet", 4))
                .circuit(4)
                .fluidInputs(GTModHandler.getDistilledWater(1_000))
                .fluidOutputs(new FluidStack(GTPPFluids.FermentationBase, 1_000))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .metadata(CHEMPLANT_CASING_TIER, 0)
                .addTo(chemicalPlantRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("cropSugarbeet", 4), GregtechItemList.CalciumCarbonateDust.get(2))
                .circuit(5)
                .fluidInputs(GTModHandler.getHotWater(2_000))
                .fluidOutputs(new FluidStack(GTPPFluids.FermentationBase, 2_000))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .metadata(CHEMPLANT_CASING_TIER, 0)
                .addTo(chemicalPlantRecipes);
        }

        // Produce Acetone, Butanol and Ethanol
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GoldenBrownCelluloseFiber.get(6), GregtechItemList.RedCelluloseFiber.get(16))
            .circuit(5)
            .fluidInputs(new FluidStack(GTPPFluids.FermentationBase, 48_000))
            .fluidOutputs(
                new FluidStack(GTPPFluids.Butanol, 18_000),
                Materials.Acetone.getFluid(9_000),
                Materials.Ethanol.getFluid(3_000))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void addMiscRecipes() {
        // Dehydrate Organise Fert to Normal Fert.
        if (Mods.Forestry.isModLoaded()) {
            addMiscForestryRecipes();
        }

        // IC2 Fertilizer
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.OrganicFertilizerDust.get(4))
            .circuit(12)
            .itemOutputs(
                ItemList.IC2_Fertilizer.get(3),
                GregtechItemList.ManureByproductsDust.get(1),
                GregtechItemList.ManureByproductsDust.get(1))
            .outputChances(10000, 2000, 2000)
            .eut(240)
            .duration(20 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Dirt Production
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.DriedEarthDust.get(9))
            .itemOutputs(new ItemStack(Blocks.dirt))
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(compressorRecipes);

        // Centrifuge Byproducts

        // Ammonium Nitrate, Phosphates, Calcium, Copper, Carbon
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.ManureByproductsDust.get(4))
            .circuit(20)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Phosphorus, 2L),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Calcium, 2L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Copper, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                GregtechItemList.DriedEarthDust.get(1),
                GregtechItemList.TinyAmmoniumNitrateDust.get(1))
            .outputChances(2500, 2500, 750, 1000, 5000, 250)
            .fluidInputs(Materials.SulfuricAcid.getFluid(250))
            .fluidOutputs(new FluidStack(GTPPFluids.SulfuricApatiteMix, 50))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(centrifugeRecipes);

        // Add Fuel Usages
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.PoopJuice, 1_000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 12)
            .addTo(semiFluidFuels);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.ManureSlurry, 1_000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 24)
            .addTo(semiFluidFuels);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.FertileManureSlurry, 1_000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 32)
            .addTo(semiFluidFuels);

        // Red Slurry / Tailings Processing
        GTValues.RA.stdBuilder()
            .circuit(10)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Nickel, 1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lead, 1))
            .outputChances(3000, 3000, 2000, 2000, 1000, 1000)
            .fluidInputs(new FluidStack(GTPPFluids.RedMud, 1_000))
            .fluidOutputs(Materials.Water.getFluid(500))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);
    }

    @Optional.Method(modid = Mods.ModIDs.FORESTRY)
    private static void addMiscForestryRecipes() {
        if (ItemList.FR_Fertilizer.hasBeenSet()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GregtechItemList.OrganicFertilizerDust.get(4))
                .circuit(11)
                .itemOutputs(
                    ItemList.FR_Fertilizer.get(3),
                    GregtechItemList.ManureByproductsDust.get(1),
                    GregtechItemList.ManureByproductsDust.get(1))
                .outputChances(100_00, 20_00, 20_00)
                .eut(240)
                .duration(20 * SECONDS)
                .addTo(chemicalDehydratorRecipes);
        }
    }
}
