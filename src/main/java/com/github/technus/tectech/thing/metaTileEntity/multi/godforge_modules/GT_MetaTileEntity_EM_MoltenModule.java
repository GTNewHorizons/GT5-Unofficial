package com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules;

import static gregtech.api.util.GT_OreDictUnificator.getAssociation;
import static gregtech.api.util.GT_ParallelHelper.addFluidsLong;
import static gregtech.api.util.GT_ParallelHelper.addItemsLong;
import static gregtech.api.util.GT_RecipeBuilder.BUCKETS;
import static gregtech.api.util.GT_RecipeBuilder.INGOTS;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.util.CommonValues;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_EM_MoltenModule extends GT_MetaTileEntity_EM_BaseModule {

    private long EUt = 0;
    private int currentParallel = 0;

    public GT_MetaTileEntity_EM_MoltenModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_MoltenModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_MoltenModule(mName);
    }

    long wirelessEUt = 0;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            private FluidStack[] meltableItems;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {

                if (recipe.mSpecialValue > getHeat()) {
                    return CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
                }
                wirelessEUt = (long) recipe.mEUt * getMaxParallel();
                if (getUserEU(userUUID).compareTo(BigInteger.valueOf(wirelessEUt * recipe.mDuration)) < 0) {
                    return CheckRecipeResultRegistry.insufficientPower(wirelessEUt * recipe.mDuration);
                }

                meltableItems = new FluidStack[recipe.mOutputs.length];
                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    ItemStack item = recipe.getOutput(i);
                    if (item == null) {
                        continue;
                    }
                    // if this is null it has to be a gt++ material
                    ItemData data = getAssociation(item);
                    Materials mat = data == null ? null : data.mMaterial.mMaterial;
                    if (mat != null) {
                        if (mat.mStandardMoltenFluid != null) {
                            meltableItems[i] = mat.getMolten(INGOTS);
                        } else if (mat.mFluid != null) {
                            meltableItems[i] = mat.getFluid(BUCKETS);
                        }
                    } else {
                        String dict = OreDictionary.getOreName(OreDictionary.getOreIDs(item)[0]);
                        // substring 8 because ingotHot is 8 characters long
                        String strippedOreDict = dict.substring(8);
                        meltableItems[i] = FluidRegistry
                            .getFluidStack("molten." + strippedOreDict.toLowerCase(), INGOTS);
                    }
                }

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).setEUt(getProcessingVoltage())
                    .setRecipeHeat(recipe.mSpecialValue)
                    .setHeatOC(true)
                    .setHeatDiscount(true)
                    .setMachineHeat(getHeatForOC())
                    .setHeatDiscountMultiplier(getHeatEnergyDiscount())
                    .setDurationDecreasePerOC(getOverclockTimeFactor());

            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GT_Recipe recipe) {
                if (!addEUToGlobalEnergyMap(userUUID, -calculatedEut * duration)) {
                    return CheckRecipeResultRegistry.insufficientPower(calculatedEut * duration);
                }
                addToPowerTally(
                    BigInteger.valueOf(calculatedEut)
                        .multiply(BigInteger.valueOf(duration)));
                addToRecipeTally(calculatedParallels);
                currentParallel = calculatedParallels;
                EUt = calculatedEut;
                setCalculatedEut(0);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
                return super.createParallelHelper(recipe).setCustomItemOutputCalculation(currentParallel -> {
                    ArrayList<ItemStack> outputItems = new ArrayList<>();
                    for (int i = 0; i < recipe.mOutputs.length; i++) {
                        ItemStack item = recipe.getOutput(i);
                        if (item == null || meltableItems[i] != null) {
                            continue;
                        }
                        ItemStack itemToAdd = item.copy();
                        addItemsLong(outputItems, itemToAdd, (long) item.stackSize * currentParallel);
                    }
                    return outputItems.toArray(new ItemStack[0]);
                })
                    .setCustomFluidOutputCalculation(currentParallel -> {
                        ArrayList<FluidStack> fluids = new ArrayList<>();

                        for (int i = 0; i < recipe.mOutputs.length; i++) {
                            FluidStack fluid = meltableItems[i];
                            if (fluid == null) {
                                continue;
                            }
                            FluidStack fluidToAdd = fluid.copy();
                            long fluidAmount = (long) fluidToAdd.amount * recipe.mOutputs[i].stackSize
                                * currentParallel;
                            addFluidsLong(fluids, fluidToAdd, fluidAmount);
                        }

                        for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                            FluidStack fluid = recipe.getFluidOutput(i);
                            if (fluid == null) {
                                continue;
                            }
                            FluidStack fluidToAdd = fluid.copy();
                            addFluidsLong(fluids, fluidToAdd, (long) fluidToAdd.amount * currentParallel);
                        }
                        return fluids.toArray(new FluidStack[0]);
                    });
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(Integer.MAX_VALUE);
        logic.setAmperageOC(false);
        logic.setMaxParallel(getMaxParallel());
        logic.setSpeedBonus(getSpeedBonus());
        logic.setEuModifier(getEnergyDiscount());
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>();
        str.add(
            "Progress: " + GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + RESET
                + " s / "
                + YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + RESET
                + " s");
        str.add("Currently using: " + RED + formatNumbers(EUt) + RESET + " EU/t");
        str.add(YELLOW + "Max Parallel: " + RESET + formatNumbers(getMaxParallel()));
        str.add(YELLOW + "Current Parallel: " + RESET + formatNumbers(currentParallel));
        str.add(YELLOW + "Heat Capacity: " + RESET + formatNumbers(getHeat()));
        str.add(YELLOW + "Effective Heat Capacity: " + RESET + formatNumbers(getHeatForOC()));
        str.add(YELLOW + "Recipe time multiplier: " + RESET + formatNumbers(getSpeedBonus()));
        str.add(YELLOW + "Energy multiplier: " + RESET + formatNumbers(getEnergyDiscount()));
        str.add(YELLOW + "Recipe time divisor per non-perfect OC: " + RESET + formatNumbers(getOverclockTimeFactor()));
        return str.toArray(new String[0]);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Blast Furnace")
            .addInfo("Controller block of the Molten Module")
            .addInfo("Uses a Star to to melt Metals")
            .addSeparator()
            .beginStructureBlock(1, 4, 2, false)
            .addEnergyHatch("Any Infinite Spacetime Casing", 1)
            .addMaintenanceHatch("Any Infinite Spacetime Casing", 1)
            .toolTipFinisher(CommonValues.GODFORGE_MARK);
        return tt;
    }

}
