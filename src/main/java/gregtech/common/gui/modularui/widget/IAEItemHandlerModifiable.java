package gregtech.common.gui.modularui.widget;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;

import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import gregtech.api.util.GTUtility;
import gregtech.common.inventory.IAEInventory;

/**
 * A version of {@link IItemHandlerModifiable} that supports AE stacks (stacks bigger than int max).
 * By default all MC stack operations proxy to the AE method equivalents and truncate any stack sizes >int max.
 */
public interface IAEItemHandlerModifiable extends IItemHandlerModifiable, IAEInventory {

    IAEItemStack getAEStackInSlot(int slot);

    @Override
    default ItemStack getStackInSlot(int slot) {
        IAEItemStack stack = getAEStackInSlot(slot);

        return stack == null ? null : stack.getItemStack();
    }

    @Nullable
    IAEItemStack insertAEItem(int slot, @NotNull IAEItemStack stack, boolean simulate);

    @Override
    @Nullable
    default ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
        if (!GTUtility.isStackValid(stack)) return null;

        IAEItemStack rejected = insertAEItem(slot, AEItemStack.create(stack), simulate);

        return rejected == null ? null : rejected.getItemStack();
    }

    @Nullable
    IAEItemStack extractAEItem(int slot, long amount, boolean simulate);

    @Override
    @Nullable
    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        IAEItemStack stack = extractAEItem(slot, amount, simulate);

        return stack == null ? null : stack.getItemStack();
    }

    default long getAESlotLimit(int slot) {
        return getAESlotLimit(slot, getAEStackInSlot(slot));
    }

    long getAESlotLimit(int slot, IAEItemStack stack);

    @Override
    default int getSlotLimit(int slot) {
        return GTUtility.longToInt(getAESlotLimit(slot));
    }

    @Override
    default boolean isItemValid(int slot, ItemStack stack) {
        return isAEItemValid(slot, AEItemStack.create(stack));
    }

    default List<IAEItemStack> getAEStacks() {
        List<IAEItemStack> ret = new ArrayList<>();
        for (int i = 0; i < getSlots(); i++) {
            ret.add(getAEStackInSlot(i));
        }
        return ret;
    }

    void setStackInSlot(int slot, IAEItemStack stack);

    @Override
    default void setStackInSlot(int slot, ItemStack stack) {
        setStackInSlot(slot, AEItemStack.create(stack));
    }
}
