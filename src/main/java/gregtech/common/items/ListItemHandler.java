package gregtech.common.items;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.cleanroommc.modularui.api.future.IItemHandlerModifiable;

/**
 * Wraps list of {@link IItemHandlerModifiable} into one handler.
 */
public class ListItemHandler implements IItemHandlerModifiable {

    protected final Iterable<? extends IItemHandlerModifiable> listItemHandler;

    public ListItemHandler(Iterable<? extends IItemHandlerModifiable> listItemHandler) {
        this.listItemHandler = listItemHandler;
    }

    @Override
    public int getSlots() {
        int ret = 0;
        for (IItemHandlerModifiable itemHandler : listItemHandler) {
            ret += itemHandler.getSlots();
        }
        return ret;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        Pair<? extends IItemHandlerModifiable, Integer> result = findItemHandler(slot);
        return result.getLeft()
            .getStackInSlot(result.getRight());
    }

    @Nullable
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        Pair<? extends IItemHandlerModifiable, Integer> result = findItemHandler(slot);
        return result.getLeft()
            .insertItem(result.getRight(), stack, simulate);
    }

    @Nullable
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        Pair<? extends IItemHandlerModifiable, Integer> result = findItemHandler(slot);
        return result.getLeft()
            .extractItem(result.getRight(), amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        Pair<? extends IItemHandlerModifiable, Integer> result = findItemHandler(slot);
        return result.getLeft()
            .getSlotLimit(result.getRight());
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        Pair<? extends IItemHandlerModifiable, Integer> result = findItemHandler(slot);
        result.getLeft()
            .setStackInSlot(result.getRight(), stack);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        Pair<? extends IItemHandlerModifiable, Integer> result = findItemHandler(slot);
        return result.getLeft()
            .isItemValid(result.getRight(), stack);
    }

    /**
     * Searches all item handlers and find one matching handler that contains specified slot index
     *
     * @param slot Index to search
     * @return Pair of item handler and actual index for it
     */
    protected Pair<? extends IItemHandlerModifiable, Integer> findItemHandler(int slot) {
        int searching = 0;
        for (IItemHandlerModifiable itemHandler : listItemHandler) {
            int numSlots = itemHandler.getSlots();
            if (slot >= searching && slot < searching + numSlots) {
                return new ImmutablePair<>(itemHandler, slot - searching);
            }
            searching += numSlots;
        }
        throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
    }
}
