package gtPlusPlus.core.slots;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBuzzSaw extends Slot{

	public SAWTOOL currentTool = SAWTOOL.NONE;

	public SlotBuzzSaw(final IInventory inventory, final int slot, final int x, final int y) {
		super(inventory, slot, x, y);

	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		boolean isValid = false;

		if (itemstack != null){
			if ((itemstack.getItem() instanceof GT_MetaGenerated_Item_02) || (itemstack.getItem() instanceof GT_MetaGenerated_Tool)){
				//Buzzsaw Blade //TODO
				/*if (OrePrefixes.toolHeadBuzzSaw.contains(itemstack)){
					isValid = false;
				}*/
				if (OrePrefixes.craftingTool.contains(itemstack)){
					if (itemstack.getDisplayName().toLowerCase().contains("saw") || itemstack.getDisplayName().toLowerCase().contains("gt.metatool.01.10") || itemstack.getDisplayName().toLowerCase().contains("gt.metatool.01.140")){
						if (itemstack.getItemDamage() == 10){
							isValid = true;
							this.currentTool = SAWTOOL.SAW;
						}
						if (itemstack.getItemDamage() == 140){
							isValid = true;
							this.currentTool = SAWTOOL.BUZZSAW;
						}
					}
				}
				else {
					this.currentTool = SAWTOOL.NONE;
				}
			}
			else {
				this.currentTool = SAWTOOL.NONE;
			}
		}
		else {
			this.currentTool = SAWTOOL.NONE;
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
