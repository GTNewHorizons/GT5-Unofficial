package gregtech.api.multitileentity.multiblock.casing;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GTValues;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

public abstract class FunctionalCasing extends MultiBlockPart {

    private int tier = 0;

    @Override
    public int getPartTier() {
        return tier;
    }

    public abstract float getPartModifier();

    @Override
    public void readMultiTileNBT(NBTTagCompound nbt) {
        super.readMultiTileNBT(nbt);
        tier = nbt.getInteger(GTValues.NBT.TIER);
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound nbt) {
        super.writeMultiTileNBT(nbt);
    }
}
