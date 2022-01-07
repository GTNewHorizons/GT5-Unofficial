package gtPlusPlus.core.inventories.box;

import gtPlusPlus.core.item.tool.misc.box.CustomBoxInventory;
import gtPlusPlus.core.slots.SlotLunchBox;
import net.minecraft.item.ItemStack;

public class LunchBoxInventory extends CustomBoxInventory {
	public LunchBoxInventory(ItemStack stack) {
		super(stack, "Lunch Box", gtPlusPlus.core.item.tool.misc.box.AutoLunchBox.SLOTS);
	}
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return SlotLunchBox.isItemValid_STATIC(itemstack);
	}		
}
