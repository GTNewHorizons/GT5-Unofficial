package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_ExoticEnergyInputHelper;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

/**
 * Multiblock base class that allows machine to use power over int.
 */
public abstract class GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T extends GT_MetaTileEntity_EnhancedMultiBlockBase<T>>
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<T> {

    public long lEUt;

    protected GT_MetaTileEntity_ExtendedPowerMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_ExtendedPowerMultiBlockBase(String aName) {
        super(aName);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // NBT can be loaded as any primitive type, so we can load it as long.
        this.lEUt = aNBT.getLong("mEUt");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("mEUt", this.lEUt);
    }

    @Override
    protected void calculateOverclockedNessMultiInternal(long aEUt, int aDuration, int mAmperage, long maxInputVoltage,
        boolean perfectOC) {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(aEUt)
            .setEUt(maxInputVoltage * mAmperage)
            .setDuration(aDuration)
            .setDurationDecreasePerOC(perfectOC ? 2 : 1)
            .calculate();
        lEUt = calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.lEUt > 0) {
            addEnergyOutput((this.lEUt * mEfficiency) / 10000);
            return true;
        }
        if (this.lEUt < 0) {
            if (!drainEnergyInput(getActualEnergyUsage())) {
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                return false;
            }
        }
        return true;
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        this.lEUt = 0;
        super.stopMachine(reason);
    }

    @Override
    protected long getActualEnergyUsage() {
        return (-this.lEUt * 10000) / Math.max(1000, mEfficiency);
    }

    public List<GT_MetaTileEntity_Hatch> getExoticAndNormalEnergyHatchList() {
        List<GT_MetaTileEntity_Hatch> tHatches = new ArrayList<>();
        tHatches.addAll(filterValidMTEs(mExoticEnergyHatches));
        tHatches.addAll(filterValidMTEs(mEnergyHatches));
        return tHatches;
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        return GT_ExoticEnergyInputHelper.drainEnergy(aEU, getExoticAndNormalEnergyHatchList());
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            if (tileEntity.isActive()) {
                if (lEUt < 0) tag.setLong("energyUsage", getActualEnergyUsage());
                else tag.setLong("energyUsage", -lEUt * mEfficiency / 10000);
            }
        }
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getAverageInputVoltage());
        logic.setAvailableAmperage(getMaxInputAmps());
        logic.setAmperageOC(true);
    }

    @Nonnull
    @Override
    protected CheckRecipeResult postCheckRecipe(@Nonnull CheckRecipeResult result,
        @Nonnull ProcessingLogic processingLogic) {
        return result;
    }

    @Override
    protected void setEnergyUsage(ProcessingLogic processingLogic) {
        lEUt = processingLogic.getCalculatedEut();
        if (lEUt > 0) {
            lEUt = (-lEUt);
        }
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : filterValidMTEs(mMufflerHatches)) {
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : getExoticAndNormalEnergyHatchList()) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        long voltage = getAverageInputVoltage();
        long amps = getMaxInputAmps();

        return new String[] {
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(voltage)
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + amps
                + " A)"
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility.getTier(voltage)]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            /* 6 */ StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + mPollutionReduction
                + EnumChatFormatting.RESET
                + " %" };
    }

    @Override
    public long getMaxInputVoltage() {
        return GT_ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList());
    }

    @Override
    public long getAverageInputVoltage() {
        return GT_ExoticEnergyInputHelper.getAverageInputVoltageMulti(getExoticAndNormalEnergyHatchList());
    }

    @Override
    public long getMaxInputAmps() {
        return GT_ExoticEnergyInputHelper.getMaxWorkingInputAmpsMulti(getExoticAndNormalEnergyHatchList());
    }

    @Override
    public long getMaxInputEu() {
        return GT_ExoticEnergyInputHelper.getTotalEuMulti(getExoticAndNormalEnergyHatchList());
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mExoticEnergyHatches.clear();
    }
}
