package gtPlusPlus.core.slots;

import ic2.core.Ic2Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFuelRod extends Slot{

	public SlotFuelRod(final IInventory inventory, final int index, final int x, final int y) {
		super(inventory, index, x, y);

	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return itemstack.getItem().getClass() == Ic2Items.fuelRod.getItem().getClass();
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
