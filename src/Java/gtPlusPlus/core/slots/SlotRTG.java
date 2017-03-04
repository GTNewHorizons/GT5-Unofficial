package gtPlusPlus.core.slots;

import ic2.core.Ic2Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRTG extends Slot{

	public SlotRTG(final IInventory inventory, final int x, final int y, final int z) {
		super(inventory, x, y, z);

	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return itemstack.getItem().getClass() == Ic2Items.RTGPellets.getItem().getClass();
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
