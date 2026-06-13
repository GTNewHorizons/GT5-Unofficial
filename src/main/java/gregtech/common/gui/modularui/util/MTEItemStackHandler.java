package gregtech.common.gui.modularui.util;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import gregtech.api.metatileentity.MetaTileEntity;

public class MTEItemStackHandler extends ItemStackHandler {

    private final MetaTileEntity mte;

    public MTEItemStackHandler(ItemStack[] inventory, MetaTileEntity mte) {
        super(inventory);
        this.mte = mte;
    }

    @Override
    protected void onContentsChanged(int slot) {
        mte.onContentsChanged(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return mte.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return mte.isItemValidForSlot(slot, stack);
    }
}
