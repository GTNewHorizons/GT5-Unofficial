package gtPlusPlus.core.inventories.projecttable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryProjectMain implements IInventory{

	private final String name = "Inventory Grid";

	/** Defining your inventory size this way is handy */
	public static final int INV_SIZE = 9;

	/** Inventory's size must be same as number of slots you add to the Container class */
	private ItemStack[] inventory = new ItemStack[INV_SIZE];

	/**
	 * @param itemstack - the ItemStack to which this inventory belongs
	 */
	public InventoryProjectMain()
	{

	}

	public void readFromNBT(final NBTTagCompound nbt)
	{
		final NBTTagList list = nbt.getTagList("Items", 10);
		this.inventory = new ItemStack[INV_SIZE];
		for(int i = 0;i<list.tagCount();i++)
		{
			final NBTTagCompound data = list.getCompoundTagAt(i);
			final int slot = data.getInteger("Slot");
			if((slot >= 0) && (slot < INV_SIZE))
			{
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(data);
			}
		}
	}

	public void writeToNBT(final NBTTagCompound nbt)
	{
		final NBTTagList list = new NBTTagList();
		for(int i = 0;i<INV_SIZE;i++)
		{
			final ItemStack stack = this.inventory[i];
			if(stack != null)
			{
				final NBTTagCompound data = new NBTTagCompound();
				stack.writeToNBT(data);
				data.setInteger("Slot", i);
				list.appendTag(data);
			}
		}
		nbt.setTag("Items", list);
	}

	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}

	public ItemStack[] getInventory(){
		return this.inventory;
	}

	@Override
	public ItemStack getStackInSlot(final int slot)
	{
		return this.inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int amount)
	{
		ItemStack stack = this.getStackInSlot(slot);
		if(stack != null)
		{
			if(stack.stackSize > amount)
			{
				stack = stack.splitStack(amount);
				// Don't forget this line or your inventory will not be saved!
				this.markDirty();
			}
			else
			{
				// this method also calls markDirty, so we don't need to call it again
				this.setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot)
	{
		final ItemStack stack = this.getStackInSlot(slot);
		this.setInventorySlotContents(slot, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack)
	{
		this.inventory[slot] = stack;

		if ((stack != null) && (stack.stackSize > this.getInventoryStackLimit()))
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		// Don't forget this line or your inventory will not be saved!
		this.markDirty();
	}

	// 1.7.2+ renamed to getInventoryName
	@Override
	public String getInventoryName()
	{
		return this.name;
	}

	// 1.7.2+ renamed to hasCustomInventoryName
	@Override
	public boolean hasCustomInventoryName()
	{
		return this.name.length() > 0;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * This is the method that will handle saving the inventory contents, as it is called (or should be called!)
	 * anytime the inventory changes. Perfect. Much better than using onUpdate in an Item, as this will also
	 * let you change things in your inventory without ever opening a Gui, if you want.
	 */
	// 1.7.2+ renamed to markDirty
	@Override
	public void markDirty()
	{
		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			final ItemStack temp = this.getStackInSlot(i);
			if (temp != null){
				//Utils.LOG_INFO("Slot "+i+" contains "+temp.getDisplayName()+" x"+temp.stackSize);
			}

			if ((temp != null) && (temp.stackSize == 0)) {
				this.inventory[i] = null;
			}
		}
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer entityplayer)
	{
		return true;
	}

	// 1.7.2+ renamed to openInventory(EntityPlayer player)
	@Override
	public void openInventory() {}

	// 1.7.2+ renamed to closeInventory(EntityPlayer player)
	@Override
	public void closeInventory() {}

	/**
	 * This method doesn't seem to do what it claims to do, as
	 * items can still be left-clicked and placed in the inventory
	 * even when this returns false
	 */
	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack)
	{
		// Don't want to be able to store the inventory item within itself
		// Bad things will happen, like losing your inventory
		// Actually, this needs a custom Slot to work
		return true;
	}

}