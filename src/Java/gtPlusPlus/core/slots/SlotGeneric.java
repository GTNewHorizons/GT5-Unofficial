package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGeneric extends Slot{

	public SlotGeneric(IInventory inventory, int x, int y, int z) {
		super(inventory, x, y, z);

	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return true;
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}

}
