package gtPlusPlus.core.inventories.box;

import gtPlusPlus.core.item.tool.misc.box.CustomBoxInventory;
import gtPlusPlus.core.item.tool.misc.box.UniversalToolBox;
import gtPlusPlus.core.slots.SlotToolBox;
import net.minecraft.item.ItemStack;

public class ToolBoxInventory extends CustomBoxInventory {
	public ToolBoxInventory(ItemStack stack) {
		super(stack, "Tool Box", UniversalToolBox.SLOTS);
	}
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return SlotToolBox.isItemValid_STATIC(itemstack);
	}		
}
