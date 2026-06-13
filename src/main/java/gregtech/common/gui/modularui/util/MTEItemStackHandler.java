package gregtech.common.gui.modularui.util;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

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

    @Override
    protected int getStackLimit(int slot, @Nullable ItemStack stack) {
        return Math.min(mte.getStackSizeLimit(slot, stack), super.getStackLimit(slot, stack));
    }
}
