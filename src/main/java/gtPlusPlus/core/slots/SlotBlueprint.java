package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.interfaces.IItemBlueprint;

public class SlotBlueprint extends Slot {

	public SlotBlueprint(final IInventory inventory, final int x, final int y, final int z) {
		super(inventory, x, y, z);
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		if (itemstack.getItem() instanceof IItemBlueprint) {
			Logger.WARNING(itemstack.getDisplayName() + " is a valid Blueprint.");
			return true;
		}
		Logger.WARNING(itemstack.getDisplayName() + " is not a valid Blueprint.");
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
