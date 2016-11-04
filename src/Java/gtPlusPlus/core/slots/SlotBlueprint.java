package gtPlusPlus.core.slots;

import gtPlusPlus.core.interfaces.IItemBlueprint;
import gtPlusPlus.core.util.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBlueprint extends Slot {

	public SlotBlueprint(final IInventory inventory, final int x, final int y, final int z) {
		super(inventory, x, y, z);
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		if (itemstack.getItem() instanceof IItemBlueprint) {
			Utils.LOG_WARNING(itemstack.getDisplayName() + " is a valid Blueprint.");
			return true;
		}
		Utils.LOG_WARNING(itemstack.getDisplayName() + " is not a valid Blueprint.");
		return false;
	}

}
