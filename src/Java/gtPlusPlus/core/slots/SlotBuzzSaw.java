package gtPlusPlus.core.slots;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBuzzSaw extends Slot{

	public SAWTOOL currentTool = SAWTOOL.NONE;

	public SlotBuzzSaw(IInventory inventory, int slot, int x, int y) {
		super(inventory, slot, x, y);

	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		boolean isValid = false;

		if (itemstack != null){
			if (itemstack.getItem() instanceof GT_MetaGenerated_Item_02 || itemstack.getItem() instanceof GT_MetaGenerated_Tool){
				//Buzzsaw Blade //TODO
				/*if (OrePrefixes.toolHeadBuzzSaw.contains(itemstack)){
					isValid = false;					
				}*/
				if (OrePrefixes.craftingTool.contains(itemstack)){
					if (itemstack.getDisplayName().toLowerCase().contains("saw")){					
						if (itemstack.getItemDamage() == 10){
							isValid = true;
							currentTool = SAWTOOL.SAW;
						}
						if (itemstack.getItemDamage() == 140){
							isValid = true;
							currentTool = SAWTOOL.BUZZSAW;
						}
					}
				}
				else {
					currentTool = SAWTOOL.NONE;
				}
			}
			else {
				currentTool = SAWTOOL.NONE;
			}
		}	
		else {
			currentTool = SAWTOOL.NONE;
		}
		return isValid;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	public enum SAWTOOL {
		NONE,
		SAW,
		BUZZSAW
	}

}
