package gregtech.api.multitileentity.multiblock.casing;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

public abstract class UpgradeCasing extends MultiBlockPart {

    protected int tier = 0;

    @Override
    public int getPartTier() {
        return tier;
    }

    @Override
    public void setTarget(IMultiBlockController newTarget, int aAllowedModes) {
        super.setTarget(newTarget, aAllowedModes);

        if (getTarget(false) != null) {
            customWork(getTarget(false));
        }
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        tier = aNBT.getInteger(GT_Values.NBT.TIER);
    }

    protected abstract void customWork(IMultiBlockController aTarget);

}
