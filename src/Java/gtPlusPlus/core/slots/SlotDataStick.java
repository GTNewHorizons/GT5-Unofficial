package gtPlusPlus.core.slots;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import gregtech.common.items.behaviors.Behaviour_DataStick;
import gtPlusPlus.core.util.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDataStick extends Slot{

	public SlotDataStick(final IInventory inventory, final int slot, final int x, final int y) {
		super(inventory, slot, x, y);

	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		boolean isValid = false;

		if (itemstack != null){
			if ((itemstack.getItem() instanceof GT_MetaGenerated_Item_01 && itemstack.getItemDamage() == 32708) || (itemstack == ItemList.Tool_DataStick.get(1))){
			isValid = true;
			}
		}
		//Utils.LOG_INFO("Tried inserting "+itemstack.getDisplayName()+" | "+itemstack.getItemDamage()+" | ");
		return isValid;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
