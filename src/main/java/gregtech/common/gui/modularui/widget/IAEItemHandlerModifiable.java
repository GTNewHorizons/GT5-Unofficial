package gregtech.common.gui.modularui.widget;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.gtnewhorizon.gtnhlib.item.ImmutableItemStack;
import com.gtnewhorizon.gtnhlib.item.InsertionItemStack;

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
    IAEItemStack insertAEItem(int slot, @NotNull IAEItemStack stack, boolean simulate, boolean forced);

    default int insertItem(int slot, @Nullable ImmutableItemStack stack, boolean simulate, boolean forced) {
        if (stack == null || stack.isEmpty()) return 0;

        IAEItemStack rejected = insertAEItem(slot, AEItemStack.create(stack.toStack()), simulate, forced);

        return rejected == null ? 0 : (int) rejected.getStackSize();
    }

    @Override
    default ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
        if (stack == null) return null;

        InsertionItemStack insertion = new InsertionItemStack(stack);

        insertion.set(insertItem(slot, insertion, simulate, false));

        return insertion.isEmpty() ? null : insertion.toStack();
    }

    @Nullable
    IAEItemStack extractAEItem(int slot, long amount, boolean simulate, boolean forced);

    default ItemStack extractItem(int slot, int amount, boolean simulate, boolean forced) {
        IAEItemStack stack = extractAEItem(slot, amount, simulate, forced);

        return stack == null ? null : stack.getItemStack();
    }

    @Override
    @Nullable
    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        return extractItem(slot, amount, simulate, false);
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
