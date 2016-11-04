package gtPlusPlus.core.inventories;

import gtPlusPlus.core.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryWorkbenchHoloSlots implements IInventory {

	/** Defining your inventory size this way is handy */
	public static final int	INV_SIZE	= 6;

	private final String	name		= "Inventory Holo";

	// Output Slot
	public IInventory		craftResult	= new InventoryCraftResult();

	/**
	 * Inventory's size must be same as number of slots you add to the Container
	 * class
	 */
	private ItemStack[]		inventory	= new ItemStack[InventoryWorkbenchHoloSlots.INV_SIZE];

	/** A list of one item containing the result of the crafting formula */
	private final ItemStack[] stackResult = new ItemStack[1];

	/**
	 * @param itemstack
	 *            - the ItemStack to which this inventory belongs
	 */
	public InventoryWorkbenchHoloSlots() {

	}

	// 1.7.2+ renamed to closeInventory(EntityPlayer player)
	@Override
	public void closeInventory() {
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	/*
	 * @Override public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
	 * { ItemStack stack = getStackInSlot(0); if (this.stackResult[0] != null) {
	 * ItemStack itemstack = this.stackResult[0]; this.stackResult[0] = null;
	 * return itemstack; } if(stack != null) { if(stack.stackSize > p_70298_2_)
	 * { stack = stack.splitStack(p_70298_2_); // Don't forget this line or your
	 * inventory will not be saved! markDirty(); } else { // this method also
	 * calls markDirty, so we don't need to call it again
	 * setInventorySlotContents(p_70298_1_, null); } } return stack; }
	 */
	@Override
	public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
		if (this.getStackInSlot(0) != null) {
			Utils.LOG_INFO("getStackInSlot(0) contains " + this.getStackInSlot(0).getDisplayName());
			if (this.stackResult[0] == null) {
				Utils.LOG_INFO("this.stackResult[0] == null");
				this.stackResult[0] = this.getStackInSlot(0);
			}
			else if (this.stackResult[0] != null) {
				Utils.LOG_INFO("this.stackResult[0] != null");
				if (this.stackResult[0].getDisplayName().toLowerCase()
						.equals(this.getStackInSlot(0).getDisplayName().toLowerCase())) {
					Utils.LOG_INFO("Items are the same?");
				}
				else {
					Utils.LOG_INFO("Items are not the same.");
				}
			}
		}

		if (this.stackResult[0] != null) {
			Utils.LOG_INFO("this.stackResult[0] != null - Really never should be though. - Returning "
					+ this.stackResult[0].getDisplayName());
			final ItemStack itemstack = this.stackResult[0];
			this.stackResult[0] = null;
			return itemstack;
		}
		return null;
	}

	public ItemStack[] getInventory() {
		return this.inventory;
	}

	// 1.7.2+ renamed to getInventoryName
	@Override
	public String getInventoryName() {
		return this.name;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return this.inventory[slot];
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
		if (this.stackResult[0] != null) {
			final ItemStack itemstack = this.stackResult[0];
			this.stackResult[0] = null;
			return itemstack;
		}
		return null;
	}

	// 1.7.2+ renamed to hasCustomInventoryName
	@Override
	public boolean hasCustomInventoryName() {
		return this.name.length() > 0;
	}

	/**
	 * This method doesn't seem to do what it claims to do, as items can still
	 * be left-clicked and placed in the inventory even when this returns false
	 */
	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
		return true;
	}

	/**
	 * This is the method that will handle saving the inventory contents, as it
	 * is called (or should be called!) anytime the inventory changes. Perfect.
	 * Much better than using onUpdate in an Item, as this will also let you
	 * change things in your inventory without ever opening a Gui, if you want.
	 */
	// 1.7.2+ renamed to markDirty
	@Override
	public void markDirty() {
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize == 0) {
				this.inventory[i] = null;
			}
		}
	}

	// 1.7.2+ renamed to openInventory(EntityPlayer player)
	@Override
	public void openInventory() {
	}

	public void readFromNBT(final NBTTagCompound nbt) {
		final NBTTagList list = nbt.getTagList("Items", 10);
		this.inventory = new ItemStack[InventoryWorkbenchHoloSlots.INV_SIZE];
		for (int i = 0; i < list.tagCount(); i++) {
			final NBTTagCompound data = list.getCompoundTagAt(i);
			final int slot = data.getInteger("Slot");
			if (slot >= 1 && slot < InventoryWorkbenchHoloSlots.INV_SIZE) {
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(data);
			}
		}
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack) {
		this.inventory[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		// Don't forget this line or your inventory will not be saved!
		this.markDirty();
	}

	public void writeToNBT(final NBTTagCompound nbt) {
		final NBTTagList list = new NBTTagList();
		for (int i = 0; i < InventoryWorkbenchHoloSlots.INV_SIZE; i++) {
			final ItemStack stack = this.inventory[i];
			if (stack != null && i != 0) {
				final NBTTagCompound data = new NBTTagCompound();
				stack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			}
		}
		nbt.setTag("Items", list);
	}

}

// Default Behaviour
/*
 * @Override public ItemStack decrStackSize(int slot, int amount) { if(stack !=
 * null) { if(stack.stackSize > amount) { stack = stack.splitStack(amount); //
 * Don't forget this line or your inventory will not be saved! markDirty(); }
 * else { // this method also calls markDirty, so we don't need to call it again
 * setInventorySlotContents(slot, null); } } return stack; }
 */

// Default Behaviour
/*
 * @Override public ItemStack getStackInSlotOnClosing(int slot) { ItemStack
 * stack = getStackInSlot(slot); setInventorySlotContents(slot, null); return
 * stack; }
 */
