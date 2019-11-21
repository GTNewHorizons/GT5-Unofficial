package kekztech;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class MultiItemHandler {
	
	private int perTypeCapacity = 0; 
	
	private boolean locked = true;
	
	private final ArrayList<ItemStack> items = new ArrayList<>();
	
	public MultiItemHandler() {
		
	}
	
	/**
	 * Tries to adapt the internal storage to match structure changes.
	 * Structure should turn off and give a warning if this returns false. 
	 * Otherwise items might unavailable.
	 * 
	 * @param itemTypeCapacity
	 * 			New item array length to adapt to.
	 * @return Success status of the operation. 
	 */
	public boolean setItemTypeCapacity(int itemTypeCapacity) {
		if(items.size() > itemTypeCapacity) {
			System.out.println("WARNING: ITEM SERVER STRUCTURE WAS DOWNSIZED TOO FAR! LOCKING FOR SAFETY.");
			setLock(true);
			return false;
		} else {
			items.ensureCapacity(itemTypeCapacity);
			// If the lock was engaged, it should only be disengaged by turning 
			// the structure back on after fixing the above warning.
			return true;
		}
	}
	
	public void setPerTypeCapacity(int perTypeCapacity) {
		this.perTypeCapacity = perTypeCapacity;
	}
	
	/**
	 * Lock internal storage in case Item Server is not running.
	 * May also be engaged in case of item safety issues.
	 * 
	 * @param state
	 * 				Lock state.
	 */
	public void setLock(boolean state) {
		locked = state;
	}
	
	public int getItemTypeCapacity() {
		return items.size();
	}
	
	public int getPerTypeCapacity() {
		return perTypeCapacity;
	}
	
	public ItemStack getStackInSlot(int slot) {
		System.out.println("Stack in slot " + slot + " requested");
		if(locked || slot >= items.size()) {
			return null;
		} else {
			return items.get(slot);
		}
	}
	
	public void insertStackInSlot(int slot, ItemStack itemStack) {
		System.out.println("Inserting " + itemStack.getDisplayName() + " into " + slot);
		if(itemStack == null 
				|| items.get(slot) != null
				|| locked
				|| slot >= items.size()) {
			return;
		} else {
			items.set(slot, itemStack);
		}
	}
	
	public int increaseStackInSlot(int slot, int amount) {
		System.out.println("Increasing item in slot " + slot + " by " + amount);
		if(slot >= items.size()
				|| locked
				|| amount <= 0) {
			return 0;
		} else {
			final int space = perTypeCapacity - items.get(slot).stackSize;
			final int fit = Math.min(space, amount);
			items.get(slot).stackSize += fit;
			return fit;
		}
	}
	
	public int reduceStackInSlot(int slot, int amount) {
		System.out.println("Reducing item in slot " + slot + " by " + amount);
		if(slot >= items.size()
				|| locked
				|| amount <= 0) {
			return 0;
		} else {
			final int available = items.get(slot).stackSize;
			final int take = Math.min(available, amount);
			items.get(slot).stackSize -= take;
			if(take == available) {
				items.set(slot, null);
			}
			return take;
		}
	}
	
	
}
