package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class ProcessingRawOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private final OrePrefixes[] mRawOrePrefixes = { OrePrefixes.rawOre };

    public ProcessingRawOre() {
        for (OrePrefixes tPrefix : this.mRawOrePrefixes) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial == Materials.Oilsands) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, aStack))
                .itemOutputs(new ItemStack(net.minecraft.init.Blocks.sand, 1, 0))
                .outputChances(4000)
                .fluidOutputs(Materials.OilHeavy.getFluid(2000L))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(centrifugeRecipes);
        } else {
            registerStandardOreRecipes(
                aPrefix,
                aMaterial,
                GT_Utility.copyAmount(1, aStack),
                Math.max(
                    1,
                    gregtech.api.GregTech_API.sOPStuff.get(
                        gregtech.api.enums.ConfigCategories.Materials.oreprocessingoutputmultiplier,
                        aMaterial.toString(),
                        1)));
        }
    }

    private boolean registerStandardOreRecipes(OrePrefixes aPrefix, Materials aMaterial, ItemStack aOreStack,
        int aMultiplier) {
        if ((aOreStack == null) || (aMaterial == null)) return false;
        GT_ModHandler
            .addValuableOre(GT_Utility.getBlockFromStack(aOreStack), aOreStack.getItemDamage(), aMaterial.mOreValue);
        Materials tMaterial = aMaterial.mOreReplacement;
        Materials tPrimaryByMaterial = null;
        aMultiplier = Math.max(1, aMultiplier);
        aOreStack = GT_Utility.copyAmount(1, aOreStack);
        aOreStack.stackSize = 1;
        ItemStack tIngot = OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mDirectSmelting, 1L);
        ItemStack tGem = OreDictUnificator.get(OrePrefixes.gem, tMaterial, 1L);
        ItemStack tSmeltInto = tIngot == null ? null
            : aMaterial.contains(SubTag.SMELTING_TO_GEM) ? OreDictUnificator.get(
                OrePrefixes.gem,
                tMaterial.mDirectSmelting,
                OreDictUnificator.get(
                    OrePrefixes.crystal,
                    tMaterial.mDirectSmelting,
                    OreDictUnificator
                        .get(OrePrefixes.gem, tMaterial, OreDictUnificator.get(OrePrefixes.crystal, tMaterial, 1L), 1L),
                    1L),
                1L) : tIngot;
        ItemStack tDust = OreDictUnificator.get(OrePrefixes.dust, tMaterial, tGem, 1L);
        ItemStack tCleaned = OreDictUnificator.get(OrePrefixes.crushedPurified, tMaterial, tDust, 1L);
        ItemStack tCrushed = OreDictUnificator.get(OrePrefixes.crushed, tMaterial, (long) aMaterial.mOreMultiplier);
        ItemStack tPrimaryByProduct = null;

        if (tCrushed == null) {
            tCrushed = OreDictUnificator.get(
                OrePrefixes.dustImpure,
                tMaterial,
                GT_Utility.copyAmount(aMaterial.mOreMultiplier, tCleaned, tDust, tGem),
                (long) aMaterial.mOreMultiplier);
        }

        for (Materials tMat : aMaterial.mOreByProducts) {
            OreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
            if (tPrimaryByProduct == null) {
                tPrimaryByMaterial = tMat;
                tPrimaryByProduct = OreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
                if (OreDictUnificator.get(OrePrefixes.dustSmall, tMat, 1L) == null) OreDictUnificator
                    .get(OrePrefixes.dustTiny, tMat, OreDictUnificator.get(OrePrefixes.nugget, tMat, 2L), 2L);
            }
            OreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
            if (OreDictUnificator.get(OrePrefixes.dustSmall, tMat, 1L) == null) OreDictUnificator
                .get(OrePrefixes.dustTiny, tMat, OreDictUnificator.get(OrePrefixes.nugget, tMat, 2L), 2L);
        }

        if (tPrimaryByMaterial == null) tPrimaryByMaterial = tMaterial;
        if (tPrimaryByProduct == null) tPrimaryByProduct = tDust;
        boolean tHasSmelting = false;

        if (tSmeltInto != null) {
            if ((aMaterial.mBlastFurnaceRequired) || (aMaterial.mDirectSmelting.mBlastFurnaceRequired)) {
                GT_ModHandler.removeFurnaceSmelting(aOreStack);
            } else {
                tHasSmelting = GT_ModHandler
                    .addSmeltingRecipe(aOreStack, GT_Utility.copyAmount(aMaterial.mSmeltingMultiplier, tSmeltInto));
            }

            if (aMaterial.contains(SubTag.BLASTFURNACE_CALCITE_TRIPLE)) {
                if (aMaterial.mAutoGenerateBlastFurnaceRecipes) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aOreStack, OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, aMultiplier))
                        .itemOutputs(
                            GT_Utility.mul(3 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                            OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L))
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            aOreStack,
                            OreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, aMultiplier))
                        .itemOutputs(
                            GT_Utility.mul(3 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                            OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L))
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                }
            } else if (aMaterial.contains(SubTag.BLASTFURNACE_CALCITE_DOUBLE)) {
                if (aMaterial.mAutoGenerateBlastFurnaceRecipes) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aOreStack, OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, aMultiplier))
                        .itemOutputs(
                            GT_Utility.mul(2 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                            OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L))
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            aOreStack,
                            OreDictUnificator.get(OrePrefixes.dust, Materials.Quicklime, aMultiplier))
                        .itemOutputs(
                            GT_Utility.mul(2 * aMaterial.mSmeltingMultiplier, tSmeltInto),
                            OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L))
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                }
            }
        }

        if (!tHasSmelting) {
            GT_ModHandler.addSmeltingRecipe(
                aOreStack,
                OreDictUnificator
                    .get(OrePrefixes.gem, tMaterial.mDirectSmelting, Math.max(1, aMaterial.mSmeltingMultiplier / 2)));
        }

        if (tCrushed != null) {
            GT_ModHandler.addShapelessCraftingRecipe(
                GT_Utility.mul(1, tCrushed),
                new Object[] { aOreStack, ToolDictNames.craftingToolHardHammer });

            GT_Values.RA.stdBuilder()
                .itemInputs(aOreStack)
                .itemOutputs(GT_Utility.copy(GT_Utility.copyAmount(tCrushed.stackSize, tGem), tCrushed))
                .duration(10)
                .eut(16)
                .addTo(hammerRecipes);

            int chanceOre2 = tPrimaryByProduct == null ? 0
                : tPrimaryByProduct.stackSize * 5 * aMaterial.mByProductMultiplier;
            chanceOre2 = 100 * chanceOre2; // converting to the GT format, 100% is 10000
            GT_Values.RA.stdBuilder()
                .itemInputs(aOreStack)
                .itemOutputs(
                    GT_Utility.mul(2, tCrushed),
                    tMaterial.contains(SubTag.PULVERIZING_CINNABAR) ? OreDictUnificator.get(
                        OrePrefixes.crystal,
                        Materials.Cinnabar,
                        OreDictUnificator
                            .get(OrePrefixes.gem, tPrimaryByMaterial, GT_Utility.copyAmount(1, tPrimaryByProduct), 1L),
                        1L)
                        : OreDictUnificator
                            .get(OrePrefixes.gem, tPrimaryByMaterial, GT_Utility.copyAmount(1, tPrimaryByProduct), 1L),
                    OreDictUnificator.getDust(aPrefix.mSecondaryMaterial))
                .outputChances(10000, chanceOre2, 5000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
        return true;
    }
}
