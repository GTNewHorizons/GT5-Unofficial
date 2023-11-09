package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;

public class ProcessingOreSmelting implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private final OrePrefixes[] mSmeltingPrefixes = { OrePrefixes.crushed, OrePrefixes.crushedPurified,
        OrePrefixes.crushedCentrifuged, OrePrefixes.dust, OrePrefixes.dustImpure, OrePrefixes.dustPure,
        OrePrefixes.dustRefined };

    public ProcessingOreSmelting() {
        for (OrePrefixes tPrefix : this.mSmeltingPrefixes) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GT_ModHandler.removeFurnaceSmelting(aStack);
        if (!aMaterial.contains(SubTag.NO_SMELTING)) {
            if ((aMaterial.mBlastFurnaceRequired) || (aMaterial.mDirectSmelting.mBlastFurnaceRequired)) {
                if (aMaterial.mBlastFurnaceTemp < 1000 && aMaterial.mDirectSmelting.mBlastFurnaceTemp < 1000)
                    if (aMaterial.mAutoGenerateBlastFurnaceRecipes
                        && GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
                            GT_RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder();
                            recipeBuilder
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1));
                            if (aMaterial.mBlastFurnaceTemp > 1750) {
                                recipeBuilder.itemOutputs(
                                    GT_OreDictUnificator.get(
                                        OrePrefixes.ingotHot,
                                        aMaterial,
                                        GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                                        1L));
                            } else {
                                recipeBuilder.itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L));
                            }
                            recipeBuilder
                                .duration(Math.max(aMaterial.getMass() / 4L, 1L) * aMaterial.mBlastFurnaceTemp * TICKS)
                                .eut(TierEU.RECIPE_MV)
                                .metadata(COIL_HEAT, (int) aMaterial.mBlastFurnaceTemp)
                                .addTo(blastFurnaceRecipes);
                        }
            } else {
                OrePrefixes outputPrefix;
                int outputSize;
                switch (aPrefix) {
                    case crushed:
                    case crushedPurified:
                    case crushedCentrifuged:
                        if (aMaterial.mDirectSmelting == aMaterial) {
                            outputSize = 10;
                            outputPrefix = OrePrefixes.nugget;
                        } else {
                            if (GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                                outputSize = 6;
                                outputPrefix = OrePrefixes.nugget;
                            } else {
                                outputSize = 1;
                                outputPrefix = OrePrefixes.ingot;
                            }
                        }
                        break;
                    case dust:
                        int outputAmount = GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;
                        if (aMaterial.mDirectSmelting != aMaterial) {
                            if (!aMaterial.contains(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)
                                && aMaterial.mDirectSmelting.getIngots(1) != null) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(2, aStack))
                                    .itemOutputs(aMaterial.mDirectSmelting.getIngots(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                            } else if (aMaterial == Materials.Chalcopyrite) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), new ItemStack(Blocks.sand, 2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(2 * outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.Glass.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(7 * outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.SiliconDioxide.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.Quartzite.getDust(4))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.NetherQuartz.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.CertusQuartz.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                            } else if (aMaterial == Materials.Tetrahedrite) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Antimony.getNuggets(3 * outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                            } else if (aMaterial == Materials.Galena) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Silver.getNuggets(3 * outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                            }
                        }
                    case dustImpure:
                    case dustPure:
                    case dustRefined:
                        if (aMaterial.mDirectSmelting == aMaterial) {
                            outputPrefix = OrePrefixes.ingot;
                            outputSize = 1;
                        } else {
                            if (GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                                outputSize = 6;
                                outputPrefix = OrePrefixes.nugget;
                            } else {
                                outputSize = 1;
                                outputPrefix = OrePrefixes.ingot;
                            }
                        }
                        break;
                    default:
                        outputPrefix = OrePrefixes.ingot;
                        outputSize = 1;
                        break;
                }
                ItemStack tStack = GT_OreDictUnificator.get(outputPrefix, aMaterial.mDirectSmelting, outputSize);
                if (tStack == null) tStack = GT_OreDictUnificator.get(
                    aMaterial.contains(SubTag.SMELTING_TO_GEM) ? OrePrefixes.gem : OrePrefixes.ingot,
                    aMaterial.mDirectSmelting,
                    1L);
                if ((tStack == null) && (!aMaterial.contains(SubTag.SMELTING_TO_GEM)))
                    tStack = GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mDirectSmelting, 1L);
                GT_ModHandler.addSmeltingRecipe(aStack, tStack);
            }
        }
    }
}
