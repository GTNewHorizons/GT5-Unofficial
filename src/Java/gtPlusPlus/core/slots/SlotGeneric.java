package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGeneric extends Slot {

	public SlotGeneric(final IInventory inventory, final int x, final int y, final int z) {
		super(inventory, x, y, z);

	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return true;
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

}
