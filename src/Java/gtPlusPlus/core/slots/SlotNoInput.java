package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNoInput extends Slot{

	public SlotNoInput(final IInventory inventory, final int x, final int y, final int z) {
		super(inventory, x, y, z);
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
