package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;

public class SlotVolumetricFlask extends Slot {

    public SlotVolumetricFlask(final IInventory inventory, final int slot, final int x, final int y) {
        super(inventory, slot, x, y);
    }

    @Override
    public synchronized boolean isItemValid(final ItemStack itemstack) {
        return isItemValidForSlot(itemstack);
    }

    public static synchronized boolean isItemValidForSlot(final ItemStack itemstack) {
        return VolumetricFlaskHelper.isVolumetricFlask(itemstack);
    }

    @Override
    public int getSlotStackLimit() {
        return 16;
    }
}
