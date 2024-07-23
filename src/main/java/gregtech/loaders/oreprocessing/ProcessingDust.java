package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.implosionRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_RecipeRegistrator;
import gregtech.api.util.GT_Utility;

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
                if (aMaterial.mFuelPower > 0) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(GT_RecipeConstants.Fuel);
                }
                if ((GT_Utility.getFluidForFilledItem(GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L), true)
                    == null) && (GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L) != null)) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aStack, ItemList.Cell_Empty.get(1L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L))
                        .duration(5 * SECONDS)
                        .eut(1)
                        .addTo(cannerRecipes);
                }
                if (!aMaterial.mBlastFurnaceRequired) {
                    GT_RecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GT_RecipeRegistrator.registerReverseArcSmelting(
                            GT_Utility.copyAmount(1, aStack),
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
                            GT_RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder();
                            recipeBuilder
                                .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(1));
                            if (aMaterial.mBlastFurnaceTemp > 1750) {
                                recipeBuilder.itemOutputs(
                                    GT_OreDictUnificator
                                        .get(OrePrefixes.ingotHot, aMaterial.mSmeltInto, tDustStack, 1L));
                            } else {
                                recipeBuilder.itemOutputs(GT_Utility.copyAmount(1, tDustStack));
                            }
                            recipeBuilder
                                .duration(
                                    (Math.max(aMaterial.getMass() / 40L, 1L) * aMaterial.mBlastFurnaceTemp) * TICKS)
                                .eut(TierEU.RECIPE_MV)
                                .metadata(COIL_HEAT, (int) aMaterial.mBlastFurnaceTemp)
                                .addTo(blastFurnaceRecipes);
                        }
                    } else {
                        GT_ModHandler.addSmeltingRecipe(aStack, tDustStack);
                    }
                } else if (!aMaterial.contains(SubTag.NO_WORKING)) {
                    if ((!OrePrefixes.block.isIgnored(aMaterial))
                        && (null == GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        && GT_OreDictUnificator.get(OrePrefixes.block, aMaterial, 1L) != null) {

                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(9, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.block, aMaterial, 1L))
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(compressorRecipes);
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
                        && (aMaterial != MaterialsUEVplus.TranscendentMetal)
                        && (aMaterial != Materials.Clay)) {
                        // compressor recipe
                        {
                            if (GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                                    .duration(15 * SECONDS)
                                    .eut(2)
                                    .addTo(compressorRecipes);
                            }
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
                                    && (tCapsuleCount + GT_ModHandler.getCapsuleCellContainerCount(tDustStack) * 64L
                                        <= 64L)) {
                                    tCapsuleCount += GT_ModHandler.getCapsuleCellContainerCount(tDustStack) * 64L;
                                    tList.add(GT_Utility.copyAmount(64, tDustStack));
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
                            if (tList.size() > 0 || tFluid != null) {
                                GT_RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder();
                                if (tCapsuleCount > 0L) {
                                    recipeBuilder.itemInputs(
                                        GT_Utility.copyAmount(tItemAmount, aStack),
                                        ItemList.Cell_Empty.get(tCapsuleCount));
                                } else {
                                    recipeBuilder.itemInputs(GT_Utility.copyAmount(tItemAmount, aStack));
                                }
                                if (tList.size() > 0) {
                                    ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                                    recipeBuilder.itemOutputs(outputsArray);
                                }
                                if (tFluid != null) {
                                    recipeBuilder.fluidOutputs(tFluid);
                                }
                                recipeBuilder
                                    .duration(Math.max(1L, Math.abs(aMaterial.getProtons() * 2L * tItemAmount)))
                                    .eut(Math.min(4, tList.size()) * 30)
                                    .addTo(electrolyzerRecipes);
                            }
                        }
                        if ((aMaterial.mExtraData & 0x2) != 0) {
                            if (tList.size() > 0 || tFluid != null) {
                                GT_RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder();
                                if (tCapsuleCount > 0L) {
                                    recipeBuilder.itemInputs(
                                        GT_Utility.copyAmount(tItemAmount, aStack),
                                        ItemList.Cell_Empty.get(tCapsuleCount));
                                } else {
                                    recipeBuilder.itemInputs(GT_Utility.copyAmount(tItemAmount, aStack));
                                }
                                if (tList.size() > 0) {
                                    ItemStack[] outputsArray = tList.toArray(new ItemStack[Math.min(tList.size(), 6)]);
                                    recipeBuilder.itemOutputs(outputsArray);
                                }
                                if (tFluid != null) {
                                    recipeBuilder.fluidOutputs(tFluid);
                                }
                                recipeBuilder.duration(Math.max(1L, Math.abs(aMaterial.getMass() * 4L * tItemAmount)))
                                    .eut(Math.min(4, tList.size()) * 5)
                                    .addTo(centrifugeRecipes);
                            }
                        }
                    }
                }
                if (aMaterial.contains(SubTag.CRYSTALLISABLE)
                    && GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(7000)
                        .fluidInputs(Materials.Water.getFluid(200L))
                        .duration(1 * MINUTES + 40 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(2))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(9000)
                        .fluidInputs(GT_ModHandler.getDistilledWater(100L))
                        .duration(1 * MINUTES + 15 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(3))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(10000)
                        .fluidInputs(Materials.Void.getMolten(36L))
                        .duration(1 * MINUTES)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                }
                switch (aMaterial.mName) {
                    case "NULL", "Mercury", "Coal":
                        break;
                    case "Glass":
                        GT_ModHandler.addSmeltingRecipe(
                            GT_Utility.copyAmount(1, aStack),
                            new ItemStack(net.minecraft.init.Blocks.glass));
                        break;
                    case "NetherQuartz":
                    case "Quartz":
                    case "CertusQuartz":
                        GT_ModHandler.removeFurnaceSmelting(aStack);
                        break;
                    case "MeatRaw":
                        GT_ModHandler.addSmeltingRecipe(
                            GT_Utility.copyAmount(1, aStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L));
                        break;
                    case "Oilsands":
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .fluidOutputs(Materials.OilHeavy.getFluid(1000))
                            .duration(33 * SECONDS)
                            .eut(8)
                            .addTo(centrifugeRecipes);
                        break;
                    case "HydratedCoal":
                        GT_ModHandler.addSmeltingRecipe(
                            GT_Utility.copyAmount(1, aStack),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L));
                        break;
                    case "Diamond": {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), ItemList.Block_Powderbarrel.get(64))
                            .itemOutputs(
                                ItemList.IC2_Industrial_Diamond.get(3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 16L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(
                                GT_Utility.copyAmount(4, aStack),
                                GT_ModHandler.getIC2Item("dynamite", 16, null))
                            .itemOutputs(
                                ItemList.IC2_Industrial_Diamond.get(3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 16L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), new ItemStack(Blocks.tnt, 32))
                            .itemOutputs(
                                ItemList.IC2_Industrial_Diamond.get(3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 16L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), GT_ModHandler.getIC2Item("industrialTnt", 8))
                            .itemOutputs(
                                ItemList.IC2_Industrial_Diamond.get(3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 16L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                    }
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
                    case "Amethyst": {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), ItemList.Block_Powderbarrel.get(48))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 12L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(
                                GT_Utility.copyAmount(4, aStack),
                                GT_ModHandler.getIC2Item("dynamite", 12, null))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 12L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), new ItemStack(Blocks.tnt, 24))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 12L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), GT_ModHandler.getIC2Item("industrialTnt", 6))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 12L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                    }
                        break;
                    case "FoolsRuby":
                    case "GarnetRed":
                    case "GarnetYellow":
                    case "Jasper":
                    case "Amber":
                    case "Monazite":
                    case "Forcicium":
                    case "Forcillium":
                    case "Force": {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), ItemList.Block_Powderbarrel.get(32))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 8L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), GT_ModHandler.getIC2Item("dynamite", 8, null))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 8L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), new ItemStack(Blocks.tnt, 16))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 8L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(4, aStack), GT_ModHandler.getIC2Item("industrialTnt", 4))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 8L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(implosionRecipes);
                    }
                }
            }
            case dustPure, dustImpure, dustRefined -> {
                Materials tByProduct = GT_Utility.selectItemInList(
                    aPrefix == OrePrefixes.dustRefined ? 2 : aPrefix == OrePrefixes.dustPure ? 1 : 0,
                    aMaterial,
                    aMaterial.mOreByProducts);
                if (aPrefix == OrePrefixes.dustPure) {
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD)) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Gold, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Neodymium, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Neodymium, 1L))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                }
                if (aMaterial.contains(SubTag.CRYSTALLISABLE)
                    && GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(9000)
                        .fluidInputs(Materials.Water.getFluid(200L))
                        .duration(1 * MINUTES + 40 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(2))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(9500)
                        .fluidInputs(GT_ModHandler.getDistilledWater(100L))
                        .duration(1 * MINUTES + 15 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(3))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(10000)
                        .fluidInputs(Materials.Void.getMolten(36L))
                        .duration(1 * MINUTES)
                        .eut(24)
                        .addTo(autoclaveRecipes);
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
                                GT_Values.RA.stdBuilder()
                                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                                    .duration(Math.max(1L, aMaterial.getMass()))
                                    .eut(5)
                                    .addTo(centrifugeRecipes);
                            } else {
                                FluidStack tFluid = GT_Utility.getFluidForFilledItem(tImpureStack, true);
                                if (tFluid == null) {
                                    GT_Values.RA.stdBuilder()
                                        .itemInputs(GT_Utility.copyAmount(9, aStack), ItemList.Cell_Empty.get(1))
                                        .itemOutputs(
                                            GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 9L),
                                            tImpureStack)
                                        .duration(Math.max(1L, aMaterial.getMass() * 72L))
                                        .eut(5)
                                        .addTo(centrifugeRecipes);
                                } else {
                                    tFluid.amount /= 10;
                                    GT_Values.RA.stdBuilder()
                                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                                        .fluidOutputs(tFluid)
                                        .duration(Math.max(1L, aMaterial.getMass() * 8L))
                                        .eut(5)
                                        .addTo(centrifugeRecipes);
                                }
                            }
                        } else {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(9, aStack))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 9L), tImpureStack)
                                .duration(Math.max(1L, aMaterial.getMass() * 72L))
                                .eut(5)
                                .addTo(centrifugeRecipes);
                        }
                    } else {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(2, aStack))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L), tImpureStack)
                            .duration(Math.max(1L, aMaterial.getMass() * 16L))
                            .eut(5)
                            .addTo(centrifugeRecipes);
                    }
                } else {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack))
                        .itemOutputs(
                            GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                            GT_OreDictUnificator.get(
                                OrePrefixes.dust,
                                tByProduct,
                                GT_OreDictUnificator.get(OrePrefixes.nugget, tByProduct, 1L),
                                1L))
                        .outputChances(10000, 1111)
                        .duration(Math.max(1L, aMaterial.getMass() * 8L))
                        .eut(5)
                        .addTo(centrifugeRecipes);
                }
            }
            case dustSmall -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(4, aStack), ItemList.Schematic_Dust.get(0L))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .duration(1 * SECONDS)
                    .eut(4)
                    .addTo(packagerRecipes);
                if (!aMaterial.mBlastFurnaceRequired) {
                    GT_RecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GT_RecipeRegistrator.registerReverseArcSmelting(
                            GT_Utility.copyAmount(1, aStack),
                            aMaterial,
                            aPrefix.mMaterialAmount,
                            null,
                            null,
                            null);
                    }
                }
            }
            case dustTiny -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(9, aStack), ItemList.Schematic_Dust.get(0L))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .duration(1 * SECONDS)
                    .eut(4)
                    .addTo(packagerRecipes);
                if (!aMaterial.mBlastFurnaceRequired) {
                    GT_RecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GT_RecipeRegistrator.registerReverseArcSmelting(
                            GT_Utility.copyAmount(1, aStack),
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
