package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.primitiveBlastRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

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
        GTModHandler.removeFurnaceSmelting(aStack);
        if (!aMaterial.contains(SubTag.NO_SMELTING)) {
            if ((aMaterial.mBlastFurnaceRequired) || (aMaterial.mDirectSmelting.mBlastFurnaceRequired)) {
                if (aMaterial.mBlastFurnaceTemp < 1000 && aMaterial.mDirectSmelting.mBlastFurnaceTemp < 1000)
                    if (aMaterial.mAutoGenerateBlastFurnaceRecipes
                        && GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
                            GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                            recipeBuilder
                                .itemInputs(GTUtility.copyAmount(1, aStack), GTUtility.getIntegratedCircuit(1));
                            if (aMaterial.mBlastFurnaceTemp > 1750) {
                                recipeBuilder.itemOutputs(
                                    GTOreDictUnificator.get(
                                        OrePrefixes.ingotHot,
                                        aMaterial,
                                        GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                                        1L));
                            } else {
                                recipeBuilder.itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L));
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
                            if (GTMod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                                outputSize = 6;
                                outputPrefix = OrePrefixes.nugget;
                            } else {
                                outputSize = 1;
                                outputPrefix = OrePrefixes.ingot;
                            }
                        }
                        break;
                    case dust:
                        int outputAmount = GTMod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;
                        if (aMaterial.mDirectSmelting != aMaterial) {
                            if (!aMaterial.contains(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)
                                && aMaterial.mDirectSmelting.getIngots(1) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(2, aStack))
                                    .itemOutputs(aMaterial.mDirectSmelting.getIngots(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                            } else if (aMaterial == Materials.Chalcopyrite) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), new ItemStack(Blocks.sand, 2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(2 * outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.Glass.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(7 * outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.SiliconDioxide.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.Quartzite.getDust(4))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.NetherQuartz.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                                GTValues.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2), Materials.CertusQuartz.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                            } else if (aMaterial == Materials.Tetrahedrite) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(aMaterial.getDust(2))
                                    .itemOutputs(
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Antimony.getNuggets(3 * outputAmount))
                                    .duration(2 * MINUTES)
                                    .metadata(ADDITIVE_AMOUNT, 2)
                                    .addTo(primitiveBlastRecipes);
                            } else if (aMaterial == Materials.Galena) {
                                GTValues.RA.stdBuilder()
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
                            if (GTMod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
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
                ItemStack tStack = GTOreDictUnificator.get(outputPrefix, aMaterial.mDirectSmelting, outputSize);
                if (tStack == null) tStack = GTOreDictUnificator.get(
                    aMaterial.contains(SubTag.SMELTING_TO_GEM) ? OrePrefixes.gem : OrePrefixes.ingot,
                    aMaterial.mDirectSmelting,
                    1L);
                if ((tStack == null) && (!aMaterial.contains(SubTag.SMELTING_TO_GEM)))
                    tStack = GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial.mDirectSmelting, 1L);
                GTModHandler.addSmeltingRecipe(aStack, tStack);
            }
        }
    }
}
