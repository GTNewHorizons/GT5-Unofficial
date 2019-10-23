package gtPlusPlus.core.slots;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotNoInputLogging extends SlotNoInput {

	private final int aSlotIndex;
	
	public SlotNoInputLogging(final IInventory inventory, final int index, final int x, final int y) {
		super(inventory, index, x, y);
		aSlotIndex = index;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		if (ItemUtils.checkForInvalidItems(itemstack)) {
			Logger.INFO("Tried Inserting "+ItemUtils.getItemName(itemstack)+" into slot "+aSlotIndex);
		}		
		return false;
	}

}
