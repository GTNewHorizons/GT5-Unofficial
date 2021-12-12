package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGeneric extends Slot {

	public SlotGeneric(final IInventory inventory, final int aSlotID, final int x, final int y) {
		super(inventory, aSlotID, x, y);
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
