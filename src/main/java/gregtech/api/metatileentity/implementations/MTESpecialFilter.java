package gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public abstract class MTESpecialFilter extends MTEFilterBase {

    private boolean allowNbt = false;

    public MTESpecialFilter(int aID, String aName, String aNameRegional, int aTier, String[] aDescription) {
        // 9 buffer slot, 1 representation slot, 1 holo slot. last seems not needed...
        super(aID, aName, aNameRegional, aTier, 11, aDescription);
    }

    public MTESpecialFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public boolean isAllowNbt() {
        return allowNbt;
    }

    public void setAllowNbt(boolean allowNbt) {
        this.allowNbt = allowNbt;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot == FILTER_SLOT_INDEX ? 1 : super.getSlotLimit(slot);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bNBTAllowed", this.allowNbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.allowNbt = aNBT.getBoolean("bNBTAllowed");
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack))
            && ((this.allowNbt) || (!aStack.hasTagCompound()))
            && (this.isStackAllowed(aStack) != this.invertFilter);
    }

    protected abstract boolean isStackAllowed(ItemStack aStack);
}
