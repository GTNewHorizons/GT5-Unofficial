package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNoInput extends Slot{

	public SlotNoInput(IInventory inventory, int x, int y, int z) {
		super(inventory, x, y, z);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 0;
	}

}
