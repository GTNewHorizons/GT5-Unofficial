package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.recipe.common.CI;

public class SlotIntegratedCircuit extends Slot {

	public static Item mCircuitItem;
	public static Item mCircuitItem2;
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
		return isItemValidForSlot(mCircuitLock, itemstack);
	}

	public static synchronized boolean isItemValidForSlot(final ItemStack itemstack) {
		return isItemValidForSlot(-1, itemstack);
	}

	public static synchronized boolean isItemValidForSlot(int aLockedCircuitNumber, final ItemStack itemstack) {
		boolean isValid = false;
		if (mCircuitItem == null) {
			mCircuitItem = CI.getNumberedCircuit(0).getItem();
		}
		if (mCircuitItem2 == null) {
			mCircuitItem2 = CI.getNumberedBioCircuit(0).getItem();
		}
		if (mCircuitItem != null && mCircuitItem2 != null) {
			if (itemstack != null) {
				if (itemstack.getItem() == mCircuitItem || itemstack.getItem() == mCircuitItem2) {					
					if (aLockedCircuitNumber == -1) {
						isValid = true;
					}
					else {
						if (itemstack.getItemDamage() == aLockedCircuitNumber) {
							isValid = true;
						}
					}					
				}
			}
		}
		return isValid;
	}

	/**
	 * Returns the circuit type. -1 is invalid, 0 is standard, 1 is GT++ bio.
	 * @param itemstack - the Circuit Stack.
	 * @return
	 */
	public static synchronized int isRegularProgrammableCircuit(final ItemStack itemstack) {
		if (mCircuitItem == null) {
			mCircuitItem = CI.getNumberedCircuit(0).getItem();
		}
		if (mCircuitItem2 == null) {
			mCircuitItem2 = CI.getNumberedBioCircuit(0).getItem();
		}
		if (mCircuitItem != null && mCircuitItem2 != null) {
			if (itemstack != null) {
				if (itemstack.getItem() == mCircuitItem || itemstack.getItem() == mCircuitItem2) {
					if (itemstack.getItem() == mCircuitItem) {
						return 0;
					}
					else if (itemstack.getItem() == mCircuitItem2) {
						return 1;
					}				
				}
			}
		}
		return -1;
	}

	@Override
	public int getSlotStackLimit() {
		return 64;
	}
}
