package tectech.thing.metaTileEntity.multi.godforge_modules;

import static gregtech.api.util.GTOreDictUnificator.getAssociation;
import static gregtech.api.util.GTRecipeBuilder.BUCKETS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.api.util.ParallelHelper.addFluidsLong;
import static gregtech.api.util.ParallelHelper.addItemsLong;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import tectech.util.CommonValues;

public class MTEMoltenModule extends MTEBaseModule {

    private long EUt = 0;
    private int currentParallel = 0;

    public MTEMoltenModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMoltenModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMoltenModule(mName);
    }

    long wirelessEUt = 0;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            private FluidStack[] meltableItems;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {

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
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
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
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
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
            protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
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
                + formatNumbers(mProgresstime / 20)
                + RESET
                + " s / "
                + YELLOW
                + formatNumbers(mMaxProgresstime / 20)
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
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Blast Smelter")
            .addInfo("Controller block for the Helioflux Meltig Core, a module of the Godforge.")
            .addInfo("Must be part of a Godforge to function.")
            .addInfo("Used for high temperature material liquefaction.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("The second module of the Godforge, this module melts materials directly into")
            .addInfo("their liquid form. If an output material does not have a liquid form, it will be output")
            .addInfo("as a regular solid instead.")
            .addInfo("This module is specialized towards parallel processing.")
            .addInfo(TOOLTIP_BAR)
            .beginStructureBlock(7, 7, 13, false)
            .addStructureInfo("The structure is too complex! See schematic for details.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "20"
                    + EnumChatFormatting.GRAY
                    + " Singularity Reinforced Stellar Shielding Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "20"
                    + EnumChatFormatting.GRAY
                    + " Boundless Gravitationally Severed Structure Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "5" + EnumChatFormatting.GRAY + " Harmonic Phonon Transmission Conduit")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "5" + EnumChatFormatting.GRAY + " Celestial Matter Guidance Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Stellar Energy Siphon Casing")
            .toolTipFinisher(CommonValues.GODFORGE_MARK);
        return tt;
    }

}
