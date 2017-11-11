package gtPlusPlus.core.slots;

import gtPlusPlus.core.item.bauble.ModularBauble;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotModularBauble extends Slot{

	public SlotModularBauble(final IInventory inventory, final int slot, final int x, final int y) {
		super(inventory, slot, x, y);

	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		boolean isValid = false;

		if (itemstack != null){
			if (itemstack.getItem() instanceof ModularBauble){				
				isValid = true;
			}
		}
		return isValid;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
