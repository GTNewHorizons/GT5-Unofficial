package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private final ArrayList<Materials> mAlreadyListedOres = new ArrayList<>(1000);

    public ProcessingOre() {
        for (OrePrefixes tPrefix : OrePrefixes.values()) if ((tPrefix.name()
            .startsWith("ore")) && (tPrefix != OrePrefixes.orePoor)
            && (tPrefix != OrePrefixes.oreSmall)
            && (tPrefix != OrePrefixes.oreRich)
            && (tPrefix != OrePrefixes.oreNormal)) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        boolean tIsRich = false;

        // For Sake of god of balance!

        // Dense ore
        if (GTMod.gregtechproxy.mRichOreYieldMultiplier) {
            tIsRich = (aPrefix == OrePrefixes.oreRich) || (aPrefix == OrePrefixes.oreDense);
        }
        // NetherOre
        if (GTMod.gregtechproxy.mNetherOreYieldMultiplier && !tIsRich) {
            tIsRich = (aPrefix == OrePrefixes.oreNetherrack) || (aPrefix == OrePrefixes.oreNether);
        }
        // EndOre
        if (GTMod.gregtechproxy.mEndOreYieldMultiplier && !tIsRich) {
            tIsRich = (aPrefix == OrePrefixes.oreEndstone) || (aPrefix == OrePrefixes.oreEnd);
        }

        if (aMaterial == Materials.Oilsands) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .itemOutputs(new ItemStack(net.minecraft.init.Blocks.sand, 1, 0))
                .outputChances(tIsRich ? 2000 : 4000)
                .fluidOutputs(Materials.OilHeavy.getFluid(tIsRich ? 4000L : 2000L))
                .duration(tIsRich ? 30 * SECONDS : 15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(centrifugeRecipes);
        } else {
            registerStandardOreRecipes(aPrefix, aMaterial, GTUtility.copyAmount(1, aStack), tIsRich ? 2 : 1);
        }
    }

    private boolean registerStandardOreRecipes(OrePrefixes aPrefix, Materials aMaterial, ItemStack aOreStack,
        int aMultiplier) {
        if ((aOreStack == null) || (aMaterial == null)) return false;
        GTModHandler
            .addValuableOre(GTUtility.getBlockFromStack(aOreStack), aOreStack.getItemDamage(), aMaterial.mOreValue);
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
                GTUtility.copyAmount(aMaterial.mOreMultiplier * aMultiplier, tCleaned, tDust, tGem),
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
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            aOreStack,
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, aMultiplier))
                        .itemOutputs(
                            GTUtility.mul(aMultiplier * 3 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L))
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            aOreStack,
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, aMultiplier))
                        .itemOutputs(
                            GTUtility.mul(aMultiplier * 3 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L))
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                }
            } else if (aMaterial.contains(SubTag.BLASTFURNACE_CALCITE_DOUBLE)) {
                if (aMaterial.mAutoGenerateBlastFurnaceRecipes) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            aOreStack,
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, aMultiplier))
                        .itemOutputs(
                            GTUtility.mul(aMultiplier * 2 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L))
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            aOreStack,
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, aMultiplier))
                        .itemOutputs(
                            GTUtility.mul(aMultiplier * 2 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L))
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
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

        if (tCrushed != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(aOreStack)
                .itemOutputs(GTUtility.copy(GTUtility.copyAmount(tCrushed.stackSize, tGem), tCrushed))
                .duration(10)
                .eut(16)
                .addTo(hammerRecipes);

            int chanceOre2 = tPrimaryByProduct == null ? 0
                : tPrimaryByProduct.stackSize * 10 * aMultiplier * aMaterial.mByProductMultiplier;
            chanceOre2 = 100 * chanceOre2; // converting to the GT format, 100% is 10000
            GTValues.RA.stdBuilder()
                .itemInputs(aOreStack)
                .itemOutputs(
                    GTUtility.mul(2, tCrushed),
                    tMaterial.contains(SubTag.PULVERIZING_CINNABAR) ? GTOreDictUnificator.get(
                        OrePrefixes.crystal,
                        Materials.Cinnabar,
                        GTOreDictUnificator
                            .get(OrePrefixes.gem, tPrimaryByMaterial, GTUtility.copyAmount(1, tPrimaryByProduct), 1L),
                        1L)
                        : GTOreDictUnificator
                            .get(OrePrefixes.gem, tPrimaryByMaterial, GTUtility.copyAmount(1, tPrimaryByProduct), 1L),
                    GTOreDictUnificator.getDust(aPrefix.mSecondaryMaterial))
                .outputChances(10000, chanceOre2, 5000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
        return true;
    }
}
