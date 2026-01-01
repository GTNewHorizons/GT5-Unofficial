package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GTUtility;

public class SlotDataStick extends Slot {

    public SlotDataStick(final IInventory inventory, final int slot, final int x, final int y) {
        super(inventory, slot, x, y);
    }

    public static ItemStack[] mDataItems = new ItemStack[2];

    @Override
    public synchronized boolean isItemValid(final ItemStack itemstack) {
        boolean isValid = false;
        if (itemstack != null) {
            if (mDataItems[0] == null) {
                mDataItems[0] = ItemList.Tool_DataStick.get(1);
            }
            if (mDataItems[1] == null) {
                mDataItems[1] = ItemList.Tool_DataOrb.get(1);
            }
            if (mDataItems[0] != null && mDataItems[1] != null) {
                if (GTUtility.areStacksEqual(itemstack, mDataItems[0], true)
                    || GTUtility.areStacksEqual(itemstack, mDataItems[1], true)) {
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
