package gtPlusPlus.core.inventories.box;

import gtPlusPlus.core.item.tool.misc.box.CustomBoxInventory;
import gtPlusPlus.core.slots.SlotMagicToolBag;
import net.minecraft.item.ItemStack;

public class MagicBagInventory extends CustomBoxInventory {
	public MagicBagInventory(ItemStack stack) {
		super(stack, "Mystic Bag", gtPlusPlus.core.item.tool.misc.box.MagicToolBag.SLOTS);
	}
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return SlotMagicToolBag.isItemValid_STATIC(itemstack);
	}		
}
