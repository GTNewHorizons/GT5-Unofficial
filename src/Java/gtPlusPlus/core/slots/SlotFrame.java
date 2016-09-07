package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import forestry.api.apiculture.IHiveFrame;

public class SlotFrame extends Slot{

	public SlotFrame(IInventory inventory, int x, int y, int z) {
		super(inventory, x, y, z);

	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.getItem() instanceof IHiveFrame;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
