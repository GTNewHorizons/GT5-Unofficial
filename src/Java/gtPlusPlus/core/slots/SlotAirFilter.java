package gtPlusPlus.core.slots;

import gtPlusPlus.core.item.general.ItemAirFilter;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAirFilter extends Slot {

	public SlotAirFilter(final IInventory inventory, final int slot, final int x, final int y) {
		super(inventory, slot, x, y);
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		}		
		if (itemstack.getItem() instanceof ItemAirFilter){
			return true;			
		}		
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
