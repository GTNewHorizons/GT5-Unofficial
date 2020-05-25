package gtPlusPlus.core.slots;

import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotVolumetricFlask extends Slot {

	public static Item mFlask;

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
