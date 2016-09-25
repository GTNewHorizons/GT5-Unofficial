package gtPlusPlus.core.slots;

import gregtech.api.interfaces.IToolStats;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGtTool extends Slot{

	public SlotGtTool(IInventory inventory, int x, int y, int z) {
		super(inventory, x, y, z);

	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.getItem() instanceof IToolStats;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
