package gregtech.crossmod.ae2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.metatileentity.implementations.MTEHatchInputBus;

public class InputBusInventoryProxy implements IInventory {

    @NotNull
    private List<MTEHatchInputBus> inputs;

    // map from slot number to index into this.inputs
    // this can't be a true map because it's a map of *ranges*
    // the entry at index i in slotIndexCache corresponds to the input at index i in inputs
    // INVARIANT: must be sorted such that lowest indexes come first
    // INVARIANT: ranges must not overlap
    @NotNull
    private final List<SlotMapEntry> slotIndexCache;
    private int totalSlotCountCache;

    public InputBusInventoryProxy() {
        this.inputs = new ArrayList<>(0);
        this.slotIndexCache = new ArrayList<>(0);
        this.updateInventoryCache();
    }

    public InputBusInventoryProxy(@NotNull List<MTEHatchInputBus> inputs) {
        this.inputs = inputs;
        this.slotIndexCache = new ArrayList<>(inputs.size());
        this.updateInventoryCache();
    }

    public void setInputs(@NotNull List<MTEHatchInputBus> inputs) {
        this.inputs = inputs;
        this.updateInventoryCache();
    }

    private void updateInventoryCache() {
        int totalSlotNum = 0;
        slotIndexCache.clear();
        for (int i = 0; i < inputs.size(); i++) {
            final int startSlot = totalSlotNum;

            final MTEHatchInputBus input = inputs.get(i);
            // -1 to account for the ghost circuit slot, which is the last slot in each inventory
            totalSlotNum += input.getSizeInventory() - 1;

            slotIndexCache.add(new SlotMapEntry(startSlot, totalSlotNum - 1));
            System.out.printf("inventory cache (%d-%d)->%d\n", startSlot, totalSlotNum - 1, i);
        }
        totalSlotCountCache = totalSlotNum;
        System.out.printf("updated cache (%d slots)\n", totalSlotCountCache);
    }

    @Nullable
    private Pair<Integer, Integer> getIndexAndRelativeSlot(int slot) {
        final int idx = Collections.binarySearch(slotIndexCache, new SlotMapEntry(slot, slot));
        if (idx >= 0) {
            // System.out.printf("found slot %d at idx %d offset %d\n", slot, entry.index, slot - entry.startSlot);
            return Pair.of(idx, slot - slotIndexCache.get(idx).startSlot);
        } else {
            // System.out.printf("slot %d not found\n", slot);
            return null;
        }
    }

    @Override
    public int getSizeInventory() {
        return totalSlotCountCache;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        // System.out.printf("Getting slot %d\n", slotIn);
        Pair<Integer, Integer> idxSlot = getIndexAndRelativeSlot(slotIn);
        if (idxSlot == null) {
            return null;
        }

        MTEHatchInputBus input = inputs.get(idxSlot.getLeft());
        return input.getStackInSlot(idxSlot.getRight());
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        // System.out.printf("decr slot %d by %d\n", index, count);
        Pair<Integer, Integer> idxSlot = getIndexAndRelativeSlot(index);
        if (idxSlot == null) {
            return null;
        }

        MTEHatchInputBus input = inputs.get(idxSlot.getLeft());
        return input.decrStackSize(idxSlot.getRight(), count);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        // System.out.printf("setting slot %d\n", index);
        Pair<Integer, Integer> idxSlot = getIndexAndRelativeSlot(index);
        if (idxSlot == null) {
            return;
        }

        MTEHatchInputBus input = inputs.get(idxSlot.getLeft());
        input.setInventorySlotContents(idxSlot.getRight(), stack);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        System.out.printf("is valid for slot %d\n", index);
        Pair<Integer, Integer> idxSlot = getIndexAndRelativeSlot(index);
        if (idxSlot == null) {
            return false;
        }

        MTEHatchInputBus input = inputs.get(idxSlot.getLeft());
        return input.isItemValidForSlot(idxSlot.getRight(), stack);
    }

    @Override
    public String getInventoryName() {
        return "FIXME: LOCALIZE (does this ever show?)";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        // FIXME: what should this be? does it matter?
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void markDirty() {
        // never called, and does not matter - the backing inventories mark themselves dirty as needed
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        // this method can never be called, the inventory is never interacted with
        throw new UnsupportedOperationException();
    }

    @Override
    public void openInventory() {
        // this method can never be called, the inventory is never interacted with
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeInventory() {
        // this method can never be called, the inventory is never interacted with
        throw new UnsupportedOperationException();
    }
}

final class SlotMapEntry implements Comparable<SlotMapEntry> {

    public final int startSlot;
    public final int endSlot;

    public SlotMapEntry(int startSlot, int endSlot) {
        this.startSlot = startSlot;
        this.endSlot = endSlot;
    }

    @Override
    public int compareTo(@NotNull SlotMapEntry o) {
        if (this.endSlot < o.startSlot) {
            return -1;
        } else if (this.startSlot > o.endSlot) {
            return 1;
        } else {
            return 0;
        }
    }
}
