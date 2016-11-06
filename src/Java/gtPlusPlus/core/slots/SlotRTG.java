package gtPlusPlus.core.slots;

import ic2.core.Ic2Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRTG extends Slot{

	public SlotRTG(IInventory inventory, int x, int y, int z) {
		super(inventory, x, y, z);

	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.getItem().getClass() == Ic2Items.RTGPellets.getItem().getClass();
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
