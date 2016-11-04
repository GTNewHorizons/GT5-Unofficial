package gtPlusPlus.core.slots;

import forestry.api.apiculture.IHiveFrame;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFrame extends Slot {

	public SlotFrame(final IInventory inventory, final int x, final int y, final int z) {
		super(inventory, x, y, z);

	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return itemstack.getItem() instanceof IHiveFrame;
	}

}
