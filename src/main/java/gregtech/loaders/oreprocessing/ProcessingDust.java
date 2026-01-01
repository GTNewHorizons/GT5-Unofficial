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
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

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
        switch (aPrefix.getName()) {
            case "dust" -> {
                if (GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Schematic_Dust_Small.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 4L))
                        .duration(1 * SECONDS)
                        .eut(4)
                        .addTo(packagerRecipes);
                }
                if (aMaterial.mFuelPower > 0) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .metadata(FUEL_VALUE, aMaterial.mFuelPower)
                        .metadata(FUEL_TYPE, aMaterial.mFuelType)
                        .addTo(GTRecipeConstants.Fuel);
                }
                if ((GTUtility.getFluidForFilledItem(GTOreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L), true)
                    == null) && (GTOreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L) != null)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(aStack, ItemList.Cell_Empty.get(1L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, aMaterial, 1L))
                        .duration(5 * SECONDS)
                        .eut(1)
                        .addTo(cannerRecipes);
                }
                if (!aMaterial.mBlastFurnaceRequired) {
                    GTRecipeRegistrator
                        .registerReverseFluidSmelting(aStack, aMaterial, aPrefix.getMaterialAmount(), null, false);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GTRecipeRegistrator.registerReverseArcSmelting(
                            GTUtility.copyAmount(1, aStack),
                            aMaterial,
                            aPrefix.getMaterialAmount(),
                            null,
                            null,
                            null);
                    }
                }
                ItemStack tDustStack;
                if ((null != (tDustStack = GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L)))
                    && (!aMaterial.contains(SubTag.NO_SMELTING))) {
                    if (aMaterial.mBlastFurnaceRequired) {
                        GTModHandler.removeFurnaceSmelting(aStack);
                        if (aMaterial.mAutoGenerateBlastFurnaceRecipes) {
                            GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                            recipeBuilder.itemInputs(GTUtility.copyAmount(1, aStack))
                                .circuit(1);
                            if (aMaterial.mBlastFurnaceTemp > 1750) {
                                recipeBuilder.itemOutputs(
                                    GTOreDictUnificator
                                        .get(OrePrefixes.ingotHot, aMaterial.mSmeltInto, tDustStack, 1L));
                            } else {
                                recipeBuilder.itemOutputs(GTUtility.copyAmount(1, tDustStack));
                            }
                            recipeBuilder
                                .duration(
                                    (Math.max(aMaterial.getMass() / 40L, 1L) * aMaterial.mBlastFurnaceTemp) * TICKS)
                                .eut(TierEU.RECIPE_MV)
                                .metadata(COIL_HEAT, (int) aMaterial.mBlastFurnaceTemp)
                                .addTo(blastFurnaceRecipes);
                        }
                    } else {
                        GTModHandler.addSmeltingRecipe(aStack, tDustStack);
                    }
                } else if (!aMaterial.contains(SubTag.NO_WORKING)) {
                    if ((!OrePrefixes.block.isIgnored(aMaterial))
                        && (null == GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        && GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L) != null
                        && (aMaterial != Materials.Clay)
                        && (aMaterial != Materials.Netherrack)) {

                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, aStack))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L))
                            .duration(15 * SECONDS)
                            .eut(2)
                            .addTo(compressorRecipes);
                    }
                    if (((OrePrefixes.block.isIgnored(aMaterial))
                        || (null == GTOreDictUnificator.get(OrePrefixes.block, aMaterial, 1L)))
                        && (aMaterial != Materials.GraniteRed)
                        && (aMaterial != Materials.GraniteBlack)
                        && (aMaterial != Materials.Basalt)
                        && (aMaterial != Materials.Marble)
                        && (aMaterial != Materials.Glass)
                        && (aMaterial != Materials.Obsidian)
                        && (aMaterial != Materials.Glowstone)
                        && (aMaterial != Materials.Paper)
                        && (aMaterial != Materials.TranscendentMetal)
                        && (aMaterial != Materials.Clay)
                        && (aMaterial != Materials.Wood)
                        && (aMaterial != Materials.Carbon)) {
                        // compressor recipe
                        {
                            if (GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L) != null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(1, aStack))
                                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                                    .duration(15 * SECONDS)
                                    .eut(2)
                                    .addTo(compressorRecipes);
                            }
                        }
                    }
                }
                if ((!aMaterial.mMaterialList.isEmpty()) && ((aMaterial.mExtraData & 0x3) != 0)) {
                    long tItemAmount = 0L;
                    long tCapsuleCount = 0L;
                    long tDensityMultiplier = aMaterial.getDensity() > 3628800L ? aMaterial.getDensity() / 3628800L
                        : 1L;
                    ArrayList<ItemStack> tList = new ArrayList<>();
                    for (MaterialStack tMat : aMaterial.mMaterialList) if (tMat.mAmount > 0L) {
                        if (tMat.mMaterial == Materials.Air) {
                            tDustStack = ItemList.Cell_Air.get(tMat.mAmount / 2L);
                        } else {
                            tDustStack = GTOreDictUnificator.get(OrePrefixes.dust, tMat.mMaterial, tMat.mAmount);
                            if (tDustStack == null)
                                tDustStack = GTOreDictUnificator.get(OrePrefixes.cell, tMat.mMaterial, tMat.mAmount);
                        }
                        if (tItemAmount + tMat.mAmount * 3628800L
                            <= aStack.getMaxStackSize() * aMaterial.getDensity()) {
                            tItemAmount += tMat.mAmount * 3628800L;
                            if (tDustStack != null) {
                                tDustStack.stackSize = ((int) (tDustStack.stackSize * tDensityMultiplier));
                                while ((tDustStack.stackSize > 64) && (tList.size() < 6)
                                    && (tCapsuleCount + GTModHandler.getCapsuleCellContainerCount(tDustStack) * 64L
                                        <= 64L)) {
                                    tCapsuleCount += GTModHandler.getCapsuleCellContainerCount(tDustStack) * 64L;
                                    tList.add(GTUtility.copyAmount(64, tDustStack));
                                    tDustStack.stackSize -= 64;
                                }
                                if ((tDustStack.stackSize > 0) && (tList.size() < 6)
                                    && (tCapsuleCount
                                        + GTModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(tDustStack)
                                        <= 64L)) {
                                    tCapsuleCount += GTModHandler
                                        .getCapsuleCellContainerCountMultipliedWithStackSize(tDustStack);
                                    tList.add(tDustStack);
                                }
                            }
                        }
                    }
                    tItemAmount = (tItemAmount * tDensityMultiplier % aMaterial.getDensity() > 0L ? 1 : 0)
                        + tItemAmount * tDensityMultiplier / aMaterial.getDensity();
                    if (!tList.isEmpty()) {
                        FluidStack tFluid = null;
                        int tList_sS = tList.size();
                        for (int i = 0; i < tList_sS; i++) {
                            if ((!ItemList.Cell_Air.isStackEqual(tList.get(i)))
                                && ((tFluid = GTUtility.getFluidForFilledItem(tList.get(i), true)) != null)) {
                                tFluid.amount *= tList.get(i).stackSize;
                                tCapsuleCount -= GTModHandler
                                    .getCapsuleCellContainerCountMultipliedWithStackSize(tList.get(i));
                                tList.remove(i);
                                break;
                            }
                        }
                        if ((aMaterial.mExtraData & 0x1) != 0) {
                            if (!tList.isEmpty() || tFluid != null) {
                                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                                if (tCapsuleCount > 0L) {
                                    recipeBuilder.itemInputs(
                                        GTUtility.copyAmount(tItemAmount, aStack),
                                        ItemList.Cell_Empty.get(tCapsuleCount));
                                } else {
                                    recipeBuilder.itemInputs(GTUtility.copyAmount(tItemAmount, aStack));
                                }
                                if (!tList.isEmpty()) {
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
                            if (!tList.isEmpty() || tFluid != null) {
                                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                                if (tCapsuleCount > 0L) {
                                    recipeBuilder.itemInputs(
                                        GTUtility.copyAmount(tItemAmount, aStack),
                                        ItemList.Cell_Empty.get(tCapsuleCount));
                                } else {
                                    recipeBuilder.itemInputs(GTUtility.copyAmount(tItemAmount, aStack));
                                }
                                if (!tList.isEmpty()) {
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
                    && GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .circuit(1)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(7000)
                        .fluidInputs(Materials.Water.getFluid(200L))
                        .duration(1 * MINUTES + 40 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .circuit(2)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(9000)
                        .fluidInputs(GTModHandler.getDistilledWater(100L))
                        .duration(1 * MINUTES + 15 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .circuit(3)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(10000)
                        .fluidInputs(Materials.Void.getMolten(1 * QUARTER_INGOTS))
                        .duration(1 * MINUTES)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                }
                switch (aMaterial.mName) {
                    case "NULL", "Mercury", "Coal":
                        break;
                    case "Glass":
                        GTModHandler.addSmeltingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            new ItemStack(net.minecraft.init.Blocks.glass));
                        break;
                    case "NetherQuartz":
                    case "Quartz":
                    case "CertusQuartz":
                        GTModHandler.removeFurnaceSmelting(aStack);
                        break;
                    case "MeatRaw":
                        GTModHandler.addSmeltingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L));
                        break;
                    case "Oilsands":
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .fluidOutputs(Materials.OilHeavy.getFluid(1_000))
                            .duration(33 * SECONDS)
                            .eut(8)
                            .addTo(centrifugeRecipes);
                        break;
                    case "HydratedCoal":
                        GTModHandler.addSmeltingRecipe(
                            GTUtility.copyAmount(1, aStack),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L));
                        break;
                    case "Diamond": {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, aStack))
                            .itemOutputs(
                                ItemList.IC2_Industrial_Diamond.get(3L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 16L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .metadata(ADDITIVE_AMOUNT, 32)
                            .addTo(implosionRecipes);
                    }
                        break;
                    case "ManaDiamond":
                    case "BotaniaDragonstone": {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 16L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .metadata(ADDITIVE_AMOUNT, 32)
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
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 12L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .metadata(ADDITIVE_AMOUNT, 24)
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
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(4, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 3L),
                                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 8L))
                            .duration(1 * SECONDS)
                            .eut(TierEU.RECIPE_LV)
                            .metadata(ADDITIVE_AMOUNT, 16)
                            .addTo(implosionRecipes);
                    }
                }
            }
            case "dustPure", "dustImpure", "dustRefined" -> {
                if (aMaterial.contains(SubTag.NO_ORE_PROCESSING)) {
                    return;
                }

                Materials tByProduct = GTUtility.selectItemInList(
                    aPrefix == OrePrefixes.dustRefined ? 2 : aPrefix == OrePrefixes.dustPure ? 1 : 0,
                    aMaterial,
                    aMaterial.mOreByProducts);
                if (aPrefix == OrePrefixes.dustPure) {
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_GOLD)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Gold, 1L),
                                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_IRON)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 1L),
                                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                    if (aMaterial.contains(SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Neodymium, 1L),
                                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Neodymium, 1L))
                            .outputChances(10000, 4000, 2000)
                            .duration(20 * SECONDS)
                            .eut(24)
                            .addTo(electroMagneticSeparatorRecipes);
                    }
                }
                if (aMaterial.contains(SubTag.CRYSTALLISABLE)
                    && GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .circuit(1)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(aPrefix == OrePrefixes.dustPure ? 9500 : 9000)
                        .fluidInputs(Materials.Water.getFluid(200L))
                        .duration(1 * MINUTES + 40 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .circuit(2)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(aPrefix == OrePrefixes.dustPure ? 10000 : 9500)
                        .fluidInputs(GTModHandler.getDistilledWater(100L))
                        .duration(1 * MINUTES + 15 * SECONDS)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .circuit(3)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L))
                        .outputChances(10000)
                        .fluidInputs(Materials.Void.getMolten(1 * QUARTER_INGOTS))
                        .duration(1 * MINUTES)
                        .eut(24)
                        .addTo(autoclaveRecipes);
                }
                ItemStack tImpureStack = GTOreDictUnificator.get(
                    OrePrefixes.dustTiny,
                    tByProduct,
                    GTOreDictUnificator.get(OrePrefixes.nugget, tByProduct, 1L),
                    1L);
                if (tImpureStack == null) {
                    tImpureStack = GTOreDictUnificator.get(OrePrefixes.dustSmall, tByProduct, 1L);
                    if (tImpureStack == null) {
                        tImpureStack = GTOreDictUnificator.get(
                            OrePrefixes.dust,
                            tByProduct,
                            GTOreDictUnificator.get(OrePrefixes.gem, tByProduct, 1L),
                            1L);
                        if (tImpureStack == null) {
                            tImpureStack = GTOreDictUnificator.get(OrePrefixes.cell, tByProduct, 1L);
                            if (tImpureStack == null) {
                                GTValues.RA.stdBuilder()
                                    .itemInputs(GTUtility.copyAmount(1, aStack))
                                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                                    .duration(Math.max(1L, aMaterial.getMass()))
                                    .eut(5)
                                    .addTo(centrifugeRecipes);
                            } else {
                                FluidStack tFluid = GTUtility.getFluidForFilledItem(tImpureStack, true);
                                if (tFluid == null) {
                                    GTValues.RA.stdBuilder()
                                        .itemInputs(GTUtility.copyAmount(9, aStack), ItemList.Cell_Empty.get(1))
                                        .itemOutputs(
                                            GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 9L),
                                            tImpureStack)
                                        .duration(Math.max(1L, aMaterial.getMass() * 72L))
                                        .eut(5)
                                        .addTo(centrifugeRecipes);
                                } else {
                                    tFluid.amount /= 10;
                                    GTValues.RA.stdBuilder()
                                        .itemInputs(GTUtility.copyAmount(1, aStack))
                                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                                        .fluidOutputs(tFluid)
                                        .duration(Math.max(1L, aMaterial.getMass() * 8L))
                                        .eut(5)
                                        .addTo(centrifugeRecipes);
                                }
                            }
                        } else {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(9, aStack))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 9L), tImpureStack)
                                .duration(Math.max(1L, aMaterial.getMass() * 72L))
                                .eut(5)
                                .addTo(centrifugeRecipes);
                        }
                    } else {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(2, aStack))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L), tImpureStack)
                            .duration(Math.max(1L, aMaterial.getMass() * 16L))
                            .eut(5)
                            .addTo(centrifugeRecipes);
                    }
                } else {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .itemOutputs(
                            GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                            GTOreDictUnificator.get(
                                OrePrefixes.dust,
                                tByProduct,
                                GTOreDictUnificator.get(OrePrefixes.nugget, tByProduct, 1L),
                                1L))
                        .outputChances(10000, 1111)
                        .duration(Math.max(1L, aMaterial.getMass() * 8L))
                        .eut(5)
                        .addTo(centrifugeRecipes);
                }
            }
            case "dustSmall" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(4, aStack), ItemList.Schematic_Dust.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .duration(1 * SECONDS)
                    .eut(4)
                    .addTo(packagerRecipes);
                if (!aMaterial.mBlastFurnaceRequired) {
                    GTRecipeRegistrator
                        .registerReverseFluidSmelting(aStack, aMaterial, aPrefix.getMaterialAmount(), null, true);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GTRecipeRegistrator.registerReverseArcSmelting(
                            GTUtility.copyAmount(1, aStack),
                            aMaterial,
                            aPrefix.getMaterialAmount(),
                            null,
                            null,
                            null);
                    }
                }
            }
            case "dustTiny" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(9, aStack), ItemList.Schematic_Dust.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .duration(1 * SECONDS)
                    .eut(4)
                    .addTo(packagerRecipes);
                if (!aMaterial.mBlastFurnaceRequired) {
                    GTRecipeRegistrator
                        .registerReverseFluidSmelting(aStack, aMaterial, aPrefix.getMaterialAmount(), null, true);
                    if (aMaterial.mSmeltInto.mArcSmeltInto != aMaterial) {
                        GTRecipeRegistrator.registerReverseArcSmelting(
                            GTUtility.copyAmount(1, aStack),
                            aMaterial,
                            aPrefix.getMaterialAmount(),
                            null,
                            null,
                            null);
                    }
                }
                if (!aMaterial.contains(SubTag.NO_SMELTING)) {
                    if (aMaterial.mBlastFurnaceRequired) {
                        GTModHandler.removeFurnaceSmelting(aStack);
                    }
                }
            }
            default -> {}
        }
    }
}
