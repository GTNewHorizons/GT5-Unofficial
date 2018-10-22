package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.recipe.common.CI;

public class SlotIntegratedCircuit extends Slot {

	public static Item mCircuitItem;
	private final short mCircuitLock;

	public SlotIntegratedCircuit(final IInventory inventory, final int slot, final int x, final int y) {
		this(Short.MAX_VALUE+1, inventory, slot, x, y);
	}
	
	public SlotIntegratedCircuit(int mTypeLock, final IInventory inventory, final int slot, final int x, final int y) {
		super(inventory, slot, x, y);
		if (mTypeLock > Short.MAX_VALUE || mTypeLock < Short.MIN_VALUE) {
			mCircuitLock = -1;
		}
		else {
			mCircuitLock = (short) mTypeLock;
		}
		
		
	}

	@Override
	public synchronized boolean isItemValid(final ItemStack itemstack) {
		boolean isValid = false;
		if (mCircuitItem == null) {
			mCircuitItem = CI.getNumberedCircuit(0).getItem();
		}
		if (mCircuitItem != null) {
			if (itemstack != null) {
				if (itemstack.getItem() == mCircuitItem) {
					
					if (mCircuitLock == -1) {
						isValid = true;
					}
					else {
						if (itemstack.getItemDamage() == mCircuitLock) {
							isValid = true;
						}
					}
					
				}
			}
		}
		return isValid;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
