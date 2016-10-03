package gtPlusPlus.core.slots;

import gregtech.api.items.GT_MetaGenerated_Tool;
import gtPlusPlus.core.util.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGtTool extends Slot{

	public SlotGtTool(IInventory inventory, int x, int y, int z) {
		super(inventory, x, y, z);

	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		if (itemstack.getItem() instanceof GT_MetaGenerated_Tool){
			Utils.LOG_WARNING(itemstack.getDisplayName()+" is a valid Tool.");
			return true;
		}
		Utils.LOG_WARNING(itemstack.getDisplayName()+" is not a valid Tool.");		
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
