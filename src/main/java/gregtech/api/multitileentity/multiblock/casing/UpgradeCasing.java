package gregtech.api.multitileentity.multiblock.casing;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GTValues;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

public abstract class UpgradeCasing extends MultiBlockPart {

    protected int tier = 0;

    @Override
    public int getPartTier() {
        return tier;
    }

    @Override
    public void setTarget(IMultiBlockController newController, int aAllowedModes) {
        super.setTarget(newController, aAllowedModes);

        if (getTarget(false) != null) {
            customWork(getTarget(false));
        }
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        tier = aNBT.getInteger(GTValues.NBT.TIER);
    }

    protected abstract void customWork(IMultiBlockController aTarget);

}
