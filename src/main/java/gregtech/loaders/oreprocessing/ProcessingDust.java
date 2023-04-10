package gregtech.loaders.oreprocessing;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeRegistrator;
import gregtech.api.util.GT_Utility;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCompressorRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

public class ProcessingDust implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingDust() {
        OrePrefixes.dust.add(this);
        OrePrefixes.dustPure.add(this);
        OrePrefixes.dustImpure.add(this);
        OrePrefixes.dustRefined.add(this);
        OrePrefixes.dustSmall.add(this);
        OrePrefixes.dustTiny.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aPrefix) {
            case dust -> {
                if (aMaterial.mFuelPower > 0) GT_Values.RA
                    .addFuel(GT_Utility.copyAmount(1L, aStack), null, aMaterial.mFuelPower, aMaterial.mFuelType);
                if (GT_Utility.getFluidForFilledItem(GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L), true)
                    == null)
                    GT_Values.RA.addCannerRecipe(
                        aStack,
                        ItemList.Cell_Empty.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L),
                        null,
                        100,
                        1);
                if (!aMaterial.mBlastFurnaceRequired) {
                    GT_RecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GT_RecipeRegistrator.registerReverseArcSmelting(
                            GT_Utility.copyAmount(1L, aStack),
                            aMaterial,
                            aPrefix.mMaterialAmount,
                            null,
                            null,
                            null);
                    }
                }
                ItemStack tDustStack;
                if ((null != (tDustStack = GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L)))
                    && (!aMaterial.contains(SubTag.NO_SMELTING))) {
                    if (aMaterial.mBlastFurnaceRequired) {
                        GT_ModHandler.removeFurnaceSmelting(aStack);
                        if (aMaterial.mAutoGenerateBlastFurnaceRecipes) {
                            GT_Values.RA.addBlastRecipe(
                                GT_Utility.copyAmount(1L, aStack),
                                ItemList.Circuit_Integrated.getWithDamage(0L, 1L),
                                null,
                                null,
                                aMaterial.mBlastFurnaceTemp > 1750
                                    ? GT_OreDictUnificator
                                        .get(OrePrefixes.ingotHot, aMaterial.mSmeltInto, tDustStack, 1L)
                                    : GT_Utility.copyAmount(1L, tDustStack),
                                null,
                                (int) Math.max(aMaterial.getMass() / 40L, 1L) * aMaterial.mBlastFurnaceTemp,
                                120,
                                aMaterial.mBlastFurnaceTemp);
                        }
                    } else {
                        GT_ModHandler.addSmeltingRecipe(aStack, tDustStack);
                    }
                } else if (!aMaterial.contains(SubTag.NO_WORKING)) {
                    if ((!OrePrefixes.block.isIgnored(aMaterial))
                        && (null == GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))) {

                        GT_Values.RA.stdBuilder()
                            .itemInputs(
                                GT_Utility.copyAmount(9L, aStack)
                            )
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.block, aMaterial, 1L)
                            )
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(15*SECONDS)
                            .eut(2)
                            .addTo(sCompressorRecipes);
                    }
                    // This is so disgustingly bad.
                    if (((OrePrefixes.block.isIgnored(aMaterial))
                        || (null == GT_OreDictUnificator.get(OrePrefixes.block, aMaterial, 1L)))
                        && (aMaterial != Materials.GraniteRed)
                        && (aMaterial != Materials.GraniteBlack)
                        && (aMaterial != Materials.Basalt)
                        && (aMaterial != Materials.Marble)
                        && (aMaterial != Materials.Glass)
                        && (aMaterial != Materials.Obsidian)
                        && (aMaterial != Materials.Glowstone)
                        && (aMaterial != Materials.Paper)
                        && (aMaterial != Materials.TranscendentMetal)
                        && (aMaterial != Materials.Clay)) {
                        //compressor recipe
                        {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(
                                    GT_Utility.copyAmount(1L, aStack)
                                )
                                .itemOutputs(
                                    GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L)
                                )
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(15 * SECONDS)
                                .eut(2)
                                .addTo(sCompressorRecipes);
                        }
                    }
                }
                if ((aMaterial.mMaterialList.size() > 0) && ((aMaterial.mExtraData & 0x3) != 0)) {
                    long tItemAmount = 0L;
                    long tCapsuleCount = 0L;
                    long tDensityMultiplier = aMaterial.getDensity() > 3628800L ? aMaterial.getDensity() / 3628800L
                        : 1L;
                    ArrayList<ItemStack> tList = new ArrayList<>();
                    for (MaterialStack tMat : aMaterial.mMaterialList) if (tMat.mAmount > 0L) {
                        if (tMat.mMaterial == Materials.Air) {
                            tDustStack = ItemList.Cell_Air.get(tMat.mAmount / 2L);
                        } else {
                            tDustStack = GT_OreDictUnificator.get(OrePrefixes.dust, tMat.mMaterial, tMat.mAmount);
                            if (tDustStack == null)
                                tDustStack = GT_OreDictUnificator.get(OrePrefixes.cell, tMat.mMaterial, tMat.mAmount);
                        }
                        if (tItemAmount + tMat.mAmount * 3628800L
                            <= aStack.getMaxStackSize() * aMaterial.getDensity()) {
                            tItemAmount += tMat.mAmount * 3628800L;
                            if (tDustStack != null) {
                                tDustStack.stackSize = ((int) (tDustStack.stackSize * tDensityMultiplier));
                                while ((tDustStack.stackSize > 64) && (tList.size() < 6)
                                    && (tCapsuleCount + GT_ModHandler.getCapsuleCellContainerCount(tDustStack) * 64
                                        <= 64L)) {
                                    tCapsuleCount += GT_ModHandler.getCapsuleCellContainerCount(tDustStack) * 64;
                                    tList.add(GT_Utility.copyAmount(64L, tDustStack));
                                    tDustStack.stackSize -= 64;
                                }
                                if ((tDustStack.stackSize > 0) && (tList.size() < 6)
                                    && (tCapsuleCount
                                        + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(tDustStack)
                                        <= 64L)) {
                                    tCapsuleCount += GT_ModHandler
                                        .getCapsuleCellContainerCountMultipliedWithStackSize(tDustStack);
                                    tList.add(tDustStack);
                                }
                            }
                        }
                    }
                    tItemAmount = (tItemAmount * tDensityMultiplier % aMaterial.getDensity() > 0L ? 1 : 0)
                        + tItemAmount * tDensityMultiplier / aMaterial.getDensity();
                    if (tList.size() > 0) {
                        FluidStack tFluid = null;
                        int tList_sS = tList.size();
                        for (int i = 0; i < tList_sS; i++) {
                            if ((!ItemList.Cell_Air.isStackEqual(tList.get(i)))
                                && ((tFluid = GT_Utility.getFluidForFilledItem(tList.get(i), true)) != null)) {
                                tFluid.amount *= tList.get(i).stackSize;
                                tCapsuleCount -= GT_ModHandler
                                    .getCapsuleCellContainerCountMultipliedWithStackSize(tList.get(i));
                                tList.remove(i);
                                break;
                            }
                        }
                        if ((aMaterial.mExtraData & 0x1) != 0) {
                            ItemStack cells = tCapsuleCount > 0L ? ItemList.Cell_Empty.get(tCapsuleCount) : null;

                            GT_Values.RA.stdBuilder()
                                .itemInputs(
                                    GT_Utility.copyAmount(tItemAmount, aStack),
                                    cells
                                )
                                .itemOutputs(
                                    tList.size() < 1 ? null : tList.get(0),
                                    tList.size() < 2 ? null : tList.get(1),
                                    tList.size() < 3 ? null : tList.get(2),
                                    tList.size() < 4 ? null : tList.get(3),
                                    tList.size() < 5 ? null : tList.get(4),
                                    tList.size() < 6 ? null : tList.get(5)
                                )
                                .noFluidInputs()
                                .fluidOutputs(tFluid)
                                .duration(Math.max(1L, Math.abs(aMaterial.getProtons() * 2L * tItemAmount)))
                                .eut(Math.min(4, tList.size()) * 30)
                                .addTo(sElectrolyzerRecipes);
                        }
                        if ((aMaterial.mExtraData & 0x2) != 0) {
                            GT_Values.RA.addCentrifugeRecipe(
                                GT_Utility.copyAmount(tItemAmount, aStack),
                                tCapsuleCount > 0L ? ItemList.Cell_Empty.get(tCapsuleCount) : null,
                                null,
                                tFluid,
                                tList.size() < 1 ? null : tList.get(0),
                                tList.size() < 2 ? null : tList.get(1),
                                tList.size() < 3 ? null : tList.get(2),
                                tList.size() < 4 ? null : tList.get(3),
                                tList.size() < 5 ? null : tList.get(4),
                                tList.size() < 6 ? null : tList.get(5),
                                null,
                                (int) Math.max(1L, Math.abs(aMaterial.getMass() * 4L * tItemAmount)),
                                Math.min(4, tList.size()) * 5);
                        }
                    }
                }
                if (aMaterial.contains(SubTag.CRYSTALLISABLE)) {
                    GT_Values.RA.addAutoclaveRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.Water.getFluid(200L),
                        GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L),
                        7000,
                        2000,
                        24,
                        false);
                    GT_Values.RA.addAutoclaveRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        GT_Utility.getIntegratedCircuit(2),
                        GT_ModHandler.getDistilledWater(100L),
                        GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L),
                        9000,
                        1500,
                        24,
                        false);
                    GT_Values.RA.addAutoclaveRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        GT_Utility.getIntegratedCircuit(3),
                        Materials.Void.getMolten(36L),
                        GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L),
                        10000,
                        1200,
                        24,
                        false);
                }
                switch (aMaterial.mName) {
                    case "NULL":
                        break;
                    case "Glass":
                        GT_ModHandler.addSmeltingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            new ItemStack(net.minecraft.init.Blocks.glass));
                        break;
                    case "NetherQuartz":
                    case "Quartz":
                    case "CertusQuartz":
                        if (gregtech.api.GregTech_API.sRecipeFile.get(
                            gregtech.api.enums.ConfigCategories.Recipes.disabledrecipes,
                            "QuartzDustSmeltingIntoAESilicon",
                            true)) GT_ModHandler.removeFurnaceSmelting(aStack);
                        break;
                    case "MeatRaw":
                        GT_ModHandler.addSmeltingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L));
                        break;
                    case "Mercury":
                        break;
                    case "Oilsands":
                        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(
                            true,
                            new ItemStack[] { GT_Utility.copyAmount(1L, aStack) },
                            null,
                            null,
                            null,
                            new FluidStack[] { Materials.OilHeavy.getFluid(1000) },
                            660,
                            8,
                            0);
                        break;
                    case "Coal":
                        break;
                    case "HydratedCoal":
                        GT_ModHandler.addSmeltingRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L));
                        break;
                    case "Diamond":
                        GT_Values.RA.addImplosionRecipe(
                            GT_Utility.copyAmount(4L, aStack),
                            32,
                            ItemList.IC2_Industrial_Diamond.get(3L),
                            GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 16L));
                        break;
                    case "Opal":
                    case "Olivine":
                    case "Emerald":
                    case "Ruby":
                    case "Sapphire":
                    case "GreenSapphire":
                    case "Topaz":
                    case "BlueTopaz":
                    case "Tanzanite":
                    case "Amethyst":
                        GT_Values.RA.addImplosionRecipe(
                            GT_Utility.copyAmount(4L, aStack),
                            24,
                            GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                            GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 12L));
                        break;
                    case "FoolsRuby":
                    case "GarnetRed":
                    case "GarnetYellow":
                    case "Jasper":
                    case "Amber":
                    case "Monazite":
                    case "Forcicium":
                    case "Forcillium":
                    case "Force":
                        GT_Values.RA.addImplosionRecipe(
                            GT_Utility.copyAmount(4L, aStack),
                            16,
                            GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                            GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 8L));
                }
            }
            case dustPure, dustImpure, dustRefined -> {
                Materials tByProduct = GT_Utility.selectItemInList(
                    aPrefix == OrePrefixes.dustRefined ? 2 : aPrefix == OrePrefixes.dustPure ? 1 : 0,
                    aMaterial,
                    aMaterial.mOreByProducts);
                if (aPrefix == OrePrefixes.dustPure) {
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD))
                        GT_Values.RA.addElectromagneticSeparatorRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Gold, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L),
                            new int[] { 10000, 4000, 2000 },
                            400,
                            24);
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_IRON))
                        GT_Values.RA.addElectromagneticSeparatorRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L),
                            new int[] { 10000, 4000, 2000 },
                            400,
                            24);
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)) {
                        GT_Values.RA.addElectromagneticSeparatorRecipe(
                            GT_Utility.copyAmount(1L, aStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Neodymium, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Neodymium, 1L),
                            new int[] { 10000, 4000, 2000 },
                            400,
                            24);
                    }
                }
                if (aMaterial.contains(SubTag.CRYSTALLISABLE)) {
                    GT_Values.RA.addAutoclaveRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.Water.getFluid(200L),
                        GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L),
                        9000,
                        2000,
                        24,
                        false);
                    GT_Values.RA.addAutoclaveRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        GT_Utility.getIntegratedCircuit(2),
                        GT_ModHandler.getDistilledWater(100L),
                        GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L),
                        9500,
                        1500,
                        24,
                        false);
                    GT_Values.RA.addAutoclaveRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        GT_Utility.getIntegratedCircuit(3),
                        Materials.Void.getMolten(36L),
                        GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L),
                        10000,
                        1200,
                        24,
                        false);
                }
                ItemStack tImpureStack = GT_OreDictUnificator.get(
                    OrePrefixes.dustTiny,
                    tByProduct,
                    GT_OreDictUnificator.get(OrePrefixes.nugget, tByProduct, 1L),
                    1L);
                if (tImpureStack == null) {
                    tImpureStack = GT_OreDictUnificator.get(OrePrefixes.dustSmall, tByProduct, 1L);
                    if (tImpureStack == null) {
                        tImpureStack = GT_OreDictUnificator.get(
                            OrePrefixes.dust,
                            tByProduct,
                            GT_OreDictUnificator.get(OrePrefixes.gem, tByProduct, 1L),
                            1L);
                        if (tImpureStack == null) {
                            tImpureStack = GT_OreDictUnificator.get(OrePrefixes.cell, tByProduct, 1L);
                            if (tImpureStack == null) {
                                GT_Values.RA.addCentrifugeRecipe(
                                    GT_Utility.copyAmount(1L, aStack),
                                    0,
                                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    (int) Math.max(1L, aMaterial.getMass()));
                            } else {
                                FluidStack tFluid = GT_Utility.getFluidForFilledItem(tImpureStack, true);
                                if (tFluid == null) {
                                    GT_Values.RA.addCentrifugeRecipe(
                                        GT_Utility.copyAmount(9L, aStack),
                                        1,
                                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 9L),
                                        tImpureStack,
                                        null,
                                        null,
                                        null,
                                        null,
                                        (int) Math.max(1L, aMaterial.getMass() * 72L));
                                } else {
                                    tFluid.amount /= 10;
                                    GT_Values.RA.addCentrifugeRecipe(
                                        GT_Utility.copyAmount(1L, aStack),
                                        null,
                                        null,
                                        tFluid,
                                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        (int) Math.max(1L, aMaterial.getMass() * 8L),
                                        5);
                                }
                            }
                        } else {
                            GT_Values.RA.addCentrifugeRecipe(
                                GT_Utility.copyAmount(9L, aStack),
                                0,
                                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 9L),
                                tImpureStack,
                                null,
                                null,
                                null,
                                null,
                                (int) Math.max(1L, aMaterial.getMass() * 72L));
                        }
                    } else {
                        GT_Values.RA.addCentrifugeRecipe(
                            GT_Utility.copyAmount(2L, aStack),
                            0,
                            GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L),
                            tImpureStack,
                            null,
                            null,
                            null,
                            null,
                            (int) Math.max(1L, aMaterial.getMass() * 16L));
                    }
                } else {
                    GT_Values.RA.addCentrifugeRecipe(
                        GT_Utility.copyAmount(1L, aStack),
                        null,
                        null,
                        null,
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                        GT_OreDictUnificator.get(
                            OrePrefixes.dust,
                            tByProduct,
                            GT_OreDictUnificator.get(OrePrefixes.nugget, tByProduct, 1L),
                            1L),
                        null,
                        null,
                        null,
                        null,
                        new int[] { 10000, 1111 },
                        (int) Math.max(1L, aMaterial.getMass() * 8L),
                        5);
                }
            }
            case dustSmall -> {
                GT_Values.RA.addBoxingRecipe(
                    GT_Utility.copyAmount(4L, aStack),
                    ItemList.Schematic_Dust.get(0L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                    20,
                    4);
                if (!aMaterial.mBlastFurnaceRequired) {
                    GT_RecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GT_RecipeRegistrator.registerReverseArcSmelting(
                            GT_Utility.copyAmount(1L, aStack),
                            aMaterial,
                            aPrefix.mMaterialAmount,
                            null,
                            null,
                            null);
                    }
                }
            }
            case dustTiny -> {
                GT_Values.RA.addBoxingRecipe(
                    GT_Utility.copyAmount(9L, aStack),
                    ItemList.Schematic_Dust.get(0L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                    20,
                    4);
                if (!aMaterial.mBlastFurnaceRequired) {
                    GT_RecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GT_RecipeRegistrator.registerReverseArcSmelting(
                            GT_Utility.copyAmount(1L, aStack),
                            aMaterial,
                            aPrefix.mMaterialAmount,
                            null,
                            null,
                            null);
                    }
                }
                if (!aMaterial.contains(SubTag.NO_SMELTING)) {
                    if (aMaterial.mBlastFurnaceRequired) {
                        GT_ModHandler.removeFurnaceSmelting(aStack);
                    }
                }
            }
            default -> {}
        }
    }
}
