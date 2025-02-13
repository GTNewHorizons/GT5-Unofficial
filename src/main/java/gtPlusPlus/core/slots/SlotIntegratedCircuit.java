package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;

public class SlotIntegratedCircuit extends Slot {

    public static Item mCircuitItem;
    private final short mCircuitLock;

    public SlotIntegratedCircuit(final IInventory inventory, final int slot, final int x, final int y) {
        this(Short.MAX_VALUE + 1, inventory, slot, x, y);
    }

    public SlotIntegratedCircuit(int mTypeLock, final IInventory inventory, final int slot, final int x, final int y) {
        super(inventory, slot, x, y);
        if (mTypeLock > Short.MAX_VALUE || mTypeLock < Short.MIN_VALUE) {
            mCircuitLock = -1;
        } else {
            mCircuitLock = (short) mTypeLock;
        }
    }

    @Override
    public synchronized boolean isItemValid(final ItemStack itemstack) {
        return isItemValidForSlot(mCircuitLock, itemstack);
    }

    public static synchronized boolean isItemValidForSlot(final ItemStack itemstack) {
        return isItemValidForSlot(-1, itemstack);
    }

    public static synchronized boolean isItemValidForSlot(int aLockedCircuitNumber, final ItemStack itemstack) {
        if (!isRegularProgrammableCircuit(itemstack)) return false;
        return aLockedCircuitNumber == -1 || aLockedCircuitNumber == itemstack.getItemDamage();
    }

    public static synchronized boolean isRegularProgrammableCircuit(final ItemStack itemstack) {
        if (mCircuitItem == null) {
            mCircuitItem = GTUtility.getIntegratedCircuit(0)
                .getItem();
        }
        return itemstack != null && itemstack.getItem() == mCircuitItem;
    }

    @Override
    public int getSlotStackLimit() {
        return 64;
    }
}
