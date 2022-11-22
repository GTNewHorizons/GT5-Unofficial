package gregtech.api.metatileentity.implementations;

import gregtech.api.util.GT_ExoticEnergyInputHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Multiblock base class that allows machine to use power over int.
 */
public abstract class GT_MetaTileEntity_ExtendedPowerMultiBlockBase<
                T extends GT_MetaTileEntity_EnhancedMultiBlockBase<T>>
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
    protected void calculateOverclockedNessMultiInternal(
            long aEUt, int aDuration, int mAmperage, long maxInputVoltage, boolean perfectOC) {
        // 5% space for cable loss
        long zMaxInputVoltage = maxInputVoltage / 100L * 95L;
        long zTime = aDuration;
        long zEUt = aEUt;
        while (zEUt < zMaxInputVoltage) {
            zEUt = zEUt << 2;
            zTime = zTime >> (perfectOC ? 2 : 1);
            if (zTime <= 0) {
                break;
            }
        }
        if (zTime <= 0) {
            zTime = 1;
        }
        if (zEUt > zMaxInputVoltage) {
            zEUt = zEUt >> 2;
            zTime = zTime << (perfectOC ? 2 : 1);
        }
        if (zTime > Integer.MAX_VALUE - 1) {
            zTime = Integer.MAX_VALUE - 1;
        }
        this.lEUt = zEUt;
        this.mMaxProgresstime = (int) zTime;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.lEUt > 0) {
            addEnergyOutput((this.lEUt * mEfficiency) / 10000);
            return true;
        }
        if (this.lEUt < 0) {
            if (!drainEnergyInput(getActualEnergyUsage())) {
                criticalStopMachine();
                return false;
            }
        }
        return true;
    }

    @Override
    public void stopMachine() {
        this.lEUt = 0;
        super.stopMachine();
    }

    @Override
    protected long getActualEnergyUsage() {
        return (-this.lEUt * 10000) / Math.max(1000, mEfficiency);
    }

    public List<GT_MetaTileEntity_Hatch> getExoticAndNormalEnergyHatchList() {
        List<GT_MetaTileEntity_Hatch> tHatches = new ArrayList<>();
        tHatches.addAll(mExoticEnergyHatches);
        tHatches.addAll(mEnergyHatches);
        return tHatches;
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        return GT_ExoticEnergyInputHelper.drainEnergy(aEU, getExoticAndNormalEnergyHatchList());
    }
}
