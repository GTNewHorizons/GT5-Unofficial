package gregtech.common.gui.modularui.util;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;

public class ProxiedItemHandlerModifiable implements IItemHandlerModifiable {

    public final IItemHandlerModifiable proxied;

    public ProxiedItemHandlerModifiable(IItemHandlerModifiable proxied) {
        this.proxied = proxied;
    }

    @Override
    public void setStackInSlot(int slot, @Nullable ItemStack stack) {
        proxied.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return proxied.getSlots();
    }

    @Override
    public @Nullable ItemStack getStackInSlot(int slot) {
        return proxied.getStackInSlot(slot);
    }

    @Override
    public @Nullable ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
        return proxied.insertItem(slot, stack, simulate);
    }

    @Override
    public @Nullable ItemStack extractItem(int slot, int amount, boolean simulate) {
        return proxied.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return proxied.getSlotLimit(slot);
    }
}
