package gtPlusPlus.core.inventories;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryWorkbenchHoloCrafting implements IInventory {

	/** Defining your inventory size this way is handy */
	public static final int		INV_SIZE	= 9;

	private final String		name		= "Inventory Crafting";

	/**
	 * Inventory's size must be same as number of slots you add to the Container
	 * class
	 */
	private final ItemStack[]	inventory	= new ItemStack[InventoryWorkbenchHoloCrafting.INV_SIZE];

	/**
	 * @param itemstack
	 *            - the ItemStack to which this inventory belongs
	 */
	public InventoryWorkbenchHoloCrafting() {

	}

	/*
	 * public void readFromNBT(NBTTagCompound nbt) { NBTTagList list =
	 * nbt.getTagList("Items", 10); inventory = new ItemStack[INV_SIZE]; for(int
	 * i = 0;i<list.tagCount();i++) { NBTTagCompound data =
	 * list.getCompoundTagAt(i); int slot = data.getInteger("Slot"); if(slot >=
	 * 0 && slot < INV_SIZE) { inventory[slot] =
	 * ItemStack.loadItemStackFromNBT(data); } } }
	 * 
	 * public void writeToNBT(NBTTagCompound nbt) { NBTTagList list = new
	 * NBTTagList(); for(int i = 0;i<INV_SIZE;i++) { ItemStack stack =
	 * inventory[i]; if(stack != null) { NBTTagCompound data = new
	 * NBTTagCompound(); stack.writeToNBT(data); data.setInteger("Slot", i);
	 * list.appendTag(data); } } nbt.setTag("Items", list); }
	 */

	@Override
	public void closeInventory() {
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int amount) {
		ItemStack stack = this.getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize > amount) {
				stack = stack.splitStack(amount);
				this.markDirty();
			}
			else {
				this.setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	public ItemStack[] getInventory() {
		return this.inventory;
	}

	@Override
	public String getInventoryName() {
		return this.name;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return this.inventory[slot];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		final ItemStack stack = this.getStackInSlot(slot);
		this.setInventorySlotContents(slot, null);
		return stack;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.name.length() > 0;
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void markDirty() {
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			final ItemStack temp = this.getStackInSlot(i);
			if (temp != null) {
				// Utils.LOG_INFO("Slot "+i+" contains "+temp.getDisplayName()+"
				// x"+temp.stackSize);
			}

			if (temp != null && temp.stackSize == 0) {
				this.inventory[i] = null;
			}
		}
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack) {
		this.inventory[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
		this.markDirty();
	}

}