package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.recipe.OreRecipeRegistrationGuard;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class ProcessingOre implements IOreRecipeRegistrator {

    private final ArrayList<Materials> mAlreadyListedOres = new ArrayList<>(1000);
    private static final Set<String> GENERATED_ORE_RECIPE_KEYS = new HashSet<>();

    public ProcessingOre() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            final String name = prefix.getName();
            if (!name.startsWith("ore")) continue;
            if (prefix == OrePrefixes.orePoor) continue;
            if (prefix == OrePrefixes.oreSmall) continue;
            if (prefix == OrePrefixes.oreRich) continue;
            if (prefix == OrePrefixes.oreNormal) continue;
            prefix.add(this);
        }
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.contains(SubTag.NO_ORE_PROCESSING)) {
            return;
        }

        if (!OreRecipeRegistrationGuard.tryProcessStack(aPrefix, aMaterial, aStack, "ProcessingOre")) {
            return;
        }

        ItemStack sourceStack = GTUtility.copyAmount(1, aStack);

        boolean tIsRich = false;

        // For Sake of god of balance!

        // Dense ore
        if (GTMod.proxy.mRichOreYieldMultiplier) {
            tIsRich = (aPrefix == OrePrefixes.oreRich) || (aPrefix == OrePrefixes.oreDense);
        }
        // NetherOre
        if (GTMod.proxy.mNetherOreYieldMultiplier && !tIsRich) {
            tIsRich = (aPrefix == OrePrefixes.oreNetherrack) || (aPrefix == OrePrefixes.oreNether);
        }
        // EndOre
        if (GTMod.proxy.mEndOreYieldMultiplier && !tIsRich) {
            tIsRich = (aPrefix == OrePrefixes.oreEndstone) || (aPrefix == OrePrefixes.oreEnd);
        }

        if (aMaterial == Materials.Oilsands) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, sourceStack))
                .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
                .outputChances(tIsRich ? 2000 : 4000)
                .fluidOutputs(Materials.OilHeavy.getFluid(tIsRich ? 4000L : 2000L))
                .duration(tIsRich ? 30 * SECONDS : 15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(centrifugeRecipes);
        } else {
            registerStandardOreRecipes(
                aPrefix,
                aMaterial,
                GTUtility.copyAmount(1, sourceStack),
                sourceStack,
                tIsRich ? 2 : 1);
        }
    }

    private boolean registerStandardOreRecipes(OrePrefixes aPrefix, Materials aMaterial, ItemStack aOreStack,
        ItemStack aSourceStack, int aMultiplier) {
        if ((aOreStack == null) || (aMaterial == null)) return false;
        Materials tMaterial = aMaterial.mOreReplacement;
        Materials tPrimaryByMaterial = null;
        aMultiplier = Math.max(1, aMultiplier);
        aOreStack = GTUtility.copyAmount(1, aOreStack);
        aOreStack.stackSize = 1;

        ItemStack tIngot = GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial.mDirectSmelting, 1L);
        ItemStack tGem = GTOreDictUnificator.get(OrePrefixes.gem, tMaterial, 1L);
        ItemStack tSmeltInto = tIngot
            == null
                ? null
                : aMaterial.contains(SubTag.SMELTING_TO_GEM)
                    ? GTOreDictUnificator.get(
                        OrePrefixes.gem,
                        tMaterial.mDirectSmelting,
                        GTOreDictUnificator.get(
                            OrePrefixes.crystal,
                            tMaterial.mDirectSmelting,
                            GTOreDictUnificator.get(
                                OrePrefixes.gem,
                                tMaterial,
                                GTOreDictUnificator.get(OrePrefixes.crystal, tMaterial, 1L),
                                1L),
                            1L),
                        1L)
                    : tIngot;

        ItemStack tDust = GTOreDictUnificator.get(OrePrefixes.dust, tMaterial, tGem, 1L);
        ItemStack tCleaned = GTOreDictUnificator.get(OrePrefixes.crushedPurified, tMaterial, tDust, 1L);
        ItemStack tCrushed = GTOreDictUnificator
            .get(OrePrefixes.crushed, tMaterial, (long) aMaterial.mOreMultiplier * aMultiplier);
        ItemStack tPrimaryByProduct = null;

        if (tCrushed == null) {
            tCrushed = GTOreDictUnificator.get(
                OrePrefixes.dustImpure,
                tMaterial,
                GTUtility.copyAmount((long) aMaterial.mOreMultiplier * aMultiplier, tCleaned, tDust, tGem),
                (long) aMaterial.mOreMultiplier * aMultiplier);
        }

        for (Materials tMat : aMaterial.mOreByProducts) {
            GTOreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
            if (tPrimaryByProduct == null) {
                tPrimaryByMaterial = tMat;
                tPrimaryByProduct = GTOreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
                if (GTOreDictUnificator.get(OrePrefixes.dustSmall, tMat, 1L) == null) GTOreDictUnificator
                    .get(OrePrefixes.dustTiny, tMat, GTOreDictUnificator.get(OrePrefixes.nugget, tMat, 2L), 2L);
            }
            GTOreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
            if (GTOreDictUnificator.get(OrePrefixes.dustSmall, tMat, 1L) == null) GTOreDictUnificator
                .get(OrePrefixes.dustTiny, tMat, GTOreDictUnificator.get(OrePrefixes.nugget, tMat, 2L), 2L);
        }

        if (tPrimaryByMaterial == null) tPrimaryByMaterial = tMaterial;
        if (tPrimaryByProduct == null) tPrimaryByProduct = tDust;
        boolean tHasSmelting = false;

        if (tSmeltInto != null) {
            if ((aMaterial.mBlastFurnaceRequired) || (aMaterial.mDirectSmelting.mBlastFurnaceRequired)) {
                GTModHandler.removeFurnaceSmelting(aOreStack);
            } else {
                tHasSmelting = GTModHandler.addSmeltingRecipe(
                    aOreStack,
                    GTUtility.copyAmount(aMultiplier * aMaterial.mSmeltingMultiplier, tSmeltInto));
            }

            if (aMaterial.contains(SubTag.BLASTFURNACE_CALCITE_TRIPLE)) {
                if (aMaterial.mAutoGenerateBlastFurnaceRecipes) {
                    addGeneratedOreRecipe(
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                aOreStack,
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, aMultiplier))
                            .itemOutputs(
                                GTUtility.mul(aMultiplier * 3 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1L))
                            .outputChances(10000, 2500)
                            .duration(tSmeltInto.stackSize * 25 * SECONDS)
                            .eut(TierEU.RECIPE_MV)
                            .metadata(COIL_HEAT, 1500),
                        blastFurnaceRecipes,
                        "blast-calcite-triple");
                    addGeneratedOreRecipe(
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                aOreStack,
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, aMultiplier))
                            .itemOutputs(
                                GTUtility.mul(aMultiplier * 3 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1L))
                            .outputChances(10000, 2500)
                            .duration(tSmeltInto.stackSize * 25 * SECONDS)
                            .eut(TierEU.RECIPE_MV)
                            .metadata(COIL_HEAT, 1500),
                        blastFurnaceRecipes,
                        "blast-quicklime-triple");
                }
            } else if (aMaterial.contains(SubTag.BLASTFURNACE_CALCITE_DOUBLE)) {
                if (aMaterial.mAutoGenerateBlastFurnaceRecipes) {
                    addGeneratedOreRecipe(
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                aOreStack,
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, aMultiplier))
                            .itemOutputs(
                                GTUtility.mul(aMultiplier * 2 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1L))
                            .outputChances(10000, 2500)
                            .duration(tSmeltInto.stackSize * 25 * SECONDS)
                            .eut(TierEU.RECIPE_MV)
                            .metadata(COIL_HEAT, 1500),
                        blastFurnaceRecipes,
                        "blast-calcite-double");
                    addGeneratedOreRecipe(
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                aOreStack,
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, aMultiplier))
                            .itemOutputs(
                                GTUtility.mul(aMultiplier * 2 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                                GTOreDictUnificator.get(OrePrefixes.dust, Materials.AshDark, 1L))
                            .outputChances(10000, 2500)
                            .duration(tSmeltInto.stackSize * 25 * SECONDS)
                            .eut(TierEU.RECIPE_MV)
                            .metadata(COIL_HEAT, 1500),
                        blastFurnaceRecipes,
                        "blast-quicklime-double");
                }
            }
        }

        if (!tHasSmelting) {
            GTModHandler.addSmeltingRecipe(
                aOreStack,
                GTOreDictUnificator.get(
                    OrePrefixes.gem,
                    tMaterial.mDirectSmelting,
                    Math.max(1, aMultiplier * aMaterial.mSmeltingMultiplier / 2)));
        }

        if (tCrushed != null && aMaterial != Materials.Knightmetal) {
            addGeneratedOreRecipe(
                GTValues.RA.stdBuilder()
                    .itemInputs(aOreStack)
                    .itemOutputs(GTUtility.copy(GTUtility.copyAmount(tCrushed.stackSize, tGem), tCrushed))
                    .duration(10)
                    .eut(TierEU.RECIPE_LV / 2),
                hammerRecipes,
                "hammer");

            ItemStack byproduct = GTOreDictUnificator
                .get(OrePrefixes.gem, tPrimaryByMaterial, GTUtility.copyAmount(1, tPrimaryByProduct), 1L);

            if (tMaterial.contains(SubTag.PULVERIZING_CINNABAR)) {
                byproduct = GTOreDictUnificator.get(OrePrefixes.crystal, Materials.Cinnabar, byproduct, 1L);
            }

            ItemStack stoneDust = null;

            try (OreInfo<?> info = OreManager.getOreInfo(aSourceStack != null ? aSourceStack : aOreStack)) {
                if (info != null) {
                    stoneDust = info.stoneType.getDust(true, 1);
                }
            }

            if (stoneDust == null) {
                stoneDust = GTOreDictUnificator.getDust(aPrefix.mSecondaryMaterial);
            }

            int chanceOre2 = tPrimaryByProduct == null ? 0
                : tPrimaryByProduct.stackSize * 10 * aMultiplier * aMaterial.mByProductMultiplier;
            chanceOre2 = 100 * chanceOre2; // converting to the GT format, 100% is 10000
            addGeneratedOreRecipe(
                GTValues.RA.stdBuilder()
                    .itemInputs(aOreStack)
                    .itemOutputs(GTUtility.mul(2, tCrushed), byproduct, stoneDust)
                    .outputChances(10000, chanceOre2, 5000)
                    .duration(20 * SECONDS)
                    .nbtSensitive()
                    .eut(2),
                maceratorRecipes,
                "macerator");
        }
        return true;
    }

    private static boolean addGeneratedOreRecipe(GTRecipeBuilder builder, RecipeMap<?> recipeMap, String scope) {
        Optional<GTRecipe> candidate = builder.build();
        if (!candidate.isPresent()) {
            return false;
        }
        GTRecipe recipe = candidate.get();
        String key = recipeMap.toString() + '|' + scope + '|' + generatedRecipeBodyKey(recipe);
        if (!GENERATED_ORE_RECIPE_KEYS.add(key)) {
            return false;
        }
        return recipeMap.addRecipe(recipe, false, false, false) != null;
    }

    private static String generatedRecipeBodyKey(GTRecipe recipe) {
        // Raw ore inputs differ across source blocks, but recipe lookup collapses equivalent ore registrations.
        return "duration=" + recipe.mDuration
            + "|eut="
            + recipe.mEUt
            + "|specialValue="
            + recipe.mSpecialValue
            + "|nbt="
            + recipe.isNBTSensitive
            + "|outputs="
            + itemStacksKey(recipe.mOutputs)
            + "|fluidOutputs="
            + fluidStacksKey(recipe.mFluidOutputs)
            + "|outputChances="
            + Arrays.toString(recipe.mOutputChances)
            + "|fluidOutputChances="
            + Arrays.toString(recipe.mFluidOutputChances)
            + "|metadata="
            + recipe.getMetadataStorage()
                .getEntries();
    }

    private static String itemStacksKey(ItemStack[] stacks) {
        StringBuilder key = new StringBuilder("[");
        for (ItemStack stack : stacks) {
            if (key.length() > 1) {
                key.append(',');
            }
            key.append(itemStackKey(stack));
        }
        return key.append(']')
            .toString();
    }

    private static String itemStackKey(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return "null";
        }
        return stack.getItem()
            .getUnlocalizedName(stack) + ':'
            + stack.getItemDamage()
            + 'x'
            + stack.stackSize
            + ':'
            + stack.stackTagCompound;
    }

    private static String fluidStacksKey(FluidStack[] stacks) {
        StringBuilder key = new StringBuilder("[");
        for (FluidStack stack : stacks) {
            if (key.length() > 1) {
                key.append(',');
            }
            key.append(fluidStackKey(stack));
        }
        return key.append(']')
            .toString();
    }

    private static String fluidStackKey(FluidStack stack) {
        if (stack == null || stack.getFluid() == null) {
            return "null";
        }
        return stack.getFluid()
            .getName() + 'x'
            + stack.amount
            + ':'
            + stack.tag;
    }
}
