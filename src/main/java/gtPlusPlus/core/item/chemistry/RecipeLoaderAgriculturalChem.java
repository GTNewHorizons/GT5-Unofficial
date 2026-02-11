package gtPlusPlus.core.item.chemistry;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
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
}
