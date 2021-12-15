package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gregtech.api.items.GT_MetaGenerated_Tool;

import gtPlusPlus.api.objects.Logger;

public class SlotGtTool extends Slot {

	public SlotGtTool(final IInventory inventory, final int x, final int y, final int z) {
		super(inventory, x, y, z);

	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		if (itemstack.getItem() instanceof GT_MetaGenerated_Tool) {
			Logger.WARNING(itemstack.getDisplayName() + " is a valid Tool.");
			return true;
		}
		Logger.WARNING(itemstack.getDisplayName() + " is not a valid Tool.");
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
