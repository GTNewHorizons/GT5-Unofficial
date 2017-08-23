package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNoInput extends Slot{

	public SlotNoInput(final IInventory inventory, final int index, final int x, final int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 0;
	}

}
