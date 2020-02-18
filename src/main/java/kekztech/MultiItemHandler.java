package kekztech;

import net.minecraft.item.ItemStack;

public class MultiItemHandler {
	
	private int perTypeCapacity = 0; 
	
	private boolean locked = true;
	
	private ItemStack[] items;
	
	public MultiItemHandler() {
		
	}
	
	/** 
	 * Adapts the internal storage to structure changes.
	 * In the event of structure down-sizing, all excess items
	 * will be dropped on the ground.
	 * 
	 * @param itemTypeCapacity
	 */
	public void setItemTypeCapacity(int itemTypeCapacity) {
		if(items.length > itemTypeCapacity) {
			// Generate new smaller backing array
			final ItemStack[] newItems = new ItemStack[itemTypeCapacity];
			for(int i = 0; i < newItems.length; i++) {
				newItems[i] = items[i];
			}
			// Sort out item overflow
			final ItemStack[] toDrop = new ItemStack[items.length - itemTypeCapacity];
			for(int i = 0; i < toDrop.length; i++) {
				toDrop[i] = items[i + newItems.length - 1];
			}
			// TODO drop overflow items to the ground
			
			// Swap array
			items = newItems;
		} else {
			// Generate new larger backing array
			final ItemStack[] newItems = new ItemStack[itemTypeCapacity];
			for(int i = 0; i < items.length; i++) {
				newItems[i] = items[i];
			}
			
			// Swap array
			items = newItems;
		}
	}
	
	public void setPerTypeCapacity(int perTypeCapacity) {
		this.perTypeCapacity = perTypeCapacity;
	}
	
	/**
	 * Lock internal storage in case Item Server is not running.
	 * 
	 * @param state
	 * 				Lock state.
	 */
	public void setLock(boolean state) {
		locked = state;
	}
	
	public int getItemTypeCapacity() {
		return items != null ? items.length : 0;
	}
	
	public int getPerTypeCapacity() {
		return perTypeCapacity;
	}
	
	/**
	 * Returns the ItemStack from the specified slot.
	 * 
	 * @param slot
	 * 			Storage slot number. Zero indexed.
	 * @return
	 * 			ItemStack from storage or null if 
	 * 			storage is locked or invalid slot parameter.
	 */
	public ItemStack getStackInSlot(int slot) {
		System.out.println("Stack in slot " + slot + " requested");
		if(locked || slot >= items.length) {
			return null;
		} else {
			return items[slot];
		}
	}
	
	/**
	 * Inserts a new ItemStack into storage,
	 * but only if the slot is still unassigned.
	 * 
	 * @param slot
	 * 			Storage slot number. Zero indexed.
	 * @param itemStack
	 * 			ItemStack to insert.
	 * @return 
	 * 			Operation success state.
	 */
	public boolean insertStackInSlot(int slot, ItemStack itemStack) {
		System.out.println("Inserting " + itemStack.getDisplayName() + " into " + slot);
		if(itemStack == null 
				|| items[slot] != null
				|| locked
				|| slot >= items.length) {
			return false;
		} else {
			items[slot] = itemStack;
			return true;
		}
	}
	
	/**
	 * Tries to increase the item amount in a specified slot.
	 * 
	 * @param slot
	 * 			Storage slot number. Zero indexed.
	 * @param amount
	 * 			Amount to increase by.
	 * @return
	 * 			Actual amount the item amount was increased by.
	 */
	public int increaseStackInSlot(int slot, int amount) {
		System.out.println("Increasing item in slot " + slot + " by " + amount);
		if(slot >= items.length
				|| locked
				|| amount <= 0) {
			return 0;
		} else {
			final int space = perTypeCapacity - items[slot].stackSize;
			final int fit = Math.min(space, amount);
			items[slot].stackSize += fit;
			return fit;
		}
	}
	
	/**
	 * Tries to reduce the item amount in a specified slot.
	 * 
	 * @param slot
	 * 			Storage slot number. Zero indexed.
	 * @param amount
	 * 			Amount to decrease by.
	 * @return
	 * 			Actual amount the item amount was decreased by.
	 */
	public int reduceStackInSlot(int slot, int amount) {
		System.out.println("Reducing item in slot " + slot + " by " + amount);
		if(slot >= items.length
				|| locked
				|| amount <= 0) {
			return 0;
		} else {
			final int available = items[slot].stackSize;
			final int take = Math.min(available, amount);
			items[slot].stackSize -= take;
			if(take == available) {
				items[slot] = null;
			}
			return take;
		}
	}
	
	
}
