package gregtech.api.multitileentity.multiblock.casing;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

public abstract class FunctionalCasing extends MultiBlockPart {

    private int tier = 0;

    @Override
    public int getPartTier() {
        return tier;
    }

    public abstract float getPartModifier();

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        tier = nbt.getInteger(GT_Values.NBT.TIER);
    }

    @Override
    public void writeToNBT(@Nonnull NBTTagCompound nbt) {
        super.writeToNBT(nbt);
    }
}
