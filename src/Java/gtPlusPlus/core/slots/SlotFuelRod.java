package gtPlusPlus.core.slots;

import gregtech.api.enums.ItemList;
import ic2.core.Ic2Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFuelRod extends Slot{

	public SlotFuelRod(final IInventory inventory, final int index, final int x, final int y) {
		super(inventory, index, x, y);

	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		boolean returnValue = false;
		//Uranium Rods
		if (itemstack.getItem() == Ic2Items.reactorUraniumSimple.getItem()){
			returnValue = true;
		}
		else if (itemstack.getItem() == Ic2Items.reactorUraniumDual.getItem()){
			returnValue = true;
		}
		else if (itemstack.getItem() == Ic2Items.reactorUraniumQuad.getItem()){
			returnValue = true;
		}
		
		//Mox Rods
		if (itemstack.getItem() == Ic2Items.reactorMOXSimple.getItem()){
			returnValue = true;
		}
		else if (itemstack.getItem() == Ic2Items.reactorMOXDual.getItem()){
			returnValue = true;
		}
		else if (itemstack.getItem() == Ic2Items.reactorMOXQuad.getItem()){
			returnValue = true;
		}
		
		//Thorium Rods
		if (itemstack.getItem() == ItemList.ThoriumCell_1.getItem()){
			returnValue = true;
		}
		else if (itemstack.getItem() == ItemList.ThoriumCell_2.getItem()){
			returnValue = true;
		}
		else if (itemstack.getItem() == ItemList.ThoriumCell_4.getItem()){
			returnValue = true;
		}
		return returnValue;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
