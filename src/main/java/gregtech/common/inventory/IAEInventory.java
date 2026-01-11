package gregtech.common.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import appeng.api.storage.data.IAEItemStack;

public interface IAEInventory {

    int getSlots();

    @Nullable
    IAEItemStack getAEStackInSlot(int slot);

    @Nullable
    IAEItemStack insertAEItem(int slot, @NotNull IAEItemStack stack, boolean simulate, boolean forced);

    @Nullable
    IAEItemStack extractAEItem(int slot, long amount, boolean simulate, boolean forced);

    default boolean isAEItemValid(int slot, @NotNull IAEItemStack stack) {
        return true;
    }

    long getAESlotLimit(int slot, @Nullable IAEItemStack stack);

    void setStackInSlot(int slot, @Nullable IAEItemStack stack);
}
