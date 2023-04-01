package gregtech.loaders.oreprocessing;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
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
                    if (aMaterial.mAutoGenerateBlastFurnaceRecipes) {
                        GT_Values.RA.addBlastRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Circuit_Integrated.getWithDamage(0L, 1L),
                                null,
                                null,
                                aMaterial.mBlastFurnaceTemp > 1750
                                        ? GT_OreDictUnificator.get(
                                                OrePrefixes.ingotHot,
                                                aMaterial,
                                                GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                                                1L)
                                        : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                                null,
                                (int) Math.max(aMaterial.getMass() / 4L, 1L) * aMaterial.mBlastFurnaceTemp,
                                120,
                                aMaterial.mBlastFurnaceTemp);
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
                            if (!aMaterial.contains(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE)) {
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        GT_Utility.copyAmount(2, aStack),
                                        GT_Values.NI,
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        GT_Values.NI,
                                        2400);
                            } else if (aMaterial == Materials.Chalcopyrite) {
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        aMaterial.getDust(2),
                                        new ItemStack(Blocks.sand, 2),
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustSmall(2 * outputAmount),
                                        2400);
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        aMaterial.getDust(2),
                                        Materials.Glass.getDust(2),
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDustTiny(7 * outputAmount),
                                        2400);
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        aMaterial.getDust(2),
                                        Materials.SiliconDioxide.getDust(2),
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDust(outputAmount),
                                        2400);
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        aMaterial.getDust(2),
                                        Materials.Quartzite.getDust(4),
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDust(outputAmount),
                                        2400);
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        aMaterial.getDust(2),
                                        Materials.NetherQuartz.getDust(2),
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDust(outputAmount),
                                        2400);
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        aMaterial.getDust(2),
                                        Materials.CertusQuartz.getDust(2),
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Ferrosilite.getDust(outputAmount),
                                        2400);
                            } else if (aMaterial == Materials.Tetrahedrite) {
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        aMaterial.getDust(2),
                                        GT_Values.NI,
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Antimony.getNuggets(3 * outputAmount),
                                        2400);
                            } else if (aMaterial == Materials.Galena) {
                                GT_Values.RA.addPrimitiveBlastRecipe(
                                        aMaterial.getDust(2),
                                        GT_Values.NI,
                                        2,
                                        aMaterial.mDirectSmelting.getIngots(outputAmount),
                                        Materials.Silver.getNuggets(3 * outputAmount),
                                        2400);
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
