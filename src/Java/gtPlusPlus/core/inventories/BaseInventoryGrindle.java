package gtPlusPlus.core.inventories;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import gtPlusPlus.core.item.base.BaseItemBackpack;
import net.minecraftforge.common.util.Constants;

public class BaseInventoryGrindle implements IInventory{

	private final String name = "Inventory Item";

	/** Provides NBT Tag Compound to reference */
	private final ItemStack invItem;

	/** Defining your inventory size this way is handy */
	public static final int INV_SIZE = 6;

	/** Inventory's size must be same as number of slots you add to the Container class */
	private final ItemStack[] inventory = new ItemStack[INV_SIZE];

	// declaration of variable:
	protected String uniqueID;

	/**
	 * @param itemstack - the ItemStack to which this inventory belongs
	 */
	public BaseInventoryGrindle(final ItemStack stack)
	{
		this.invItem = stack;

		/** initialize variable within the constructor: */
		this.uniqueID = "";

		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			// no tag compound means the itemstack does not yet have a UUID, so assign one:
			this.uniqueID = UUID.randomUUID().toString();
		}

		// Create a new NBT Tag Compound if one doesn't already exist, or you will crash
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		// note that it's okay to use stack instead of invItem right there
		// both reference the same memory location, so whatever you change using
		// either reference will change in the other

		// Read the inventory contents from NBT
		this.readFromNBT(stack.getTagCompound());
	}
	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
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
		return 1;
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
			if ((this.getStackInSlot(i) != null) && (this.getStackInSlot(i).stackSize == 0)) {
				this.inventory[i] = null;
			}
		}

		// This line here does the work:
		this.writeToNBT(this.invItem.getTagCompound());
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
		return !(itemstack.getItem() instanceof BaseItemBackpack);
	}

	/**
	 * A custom method to read our inventory from an ItemStack's NBT compound
	 */
	public void readFromNBT(final NBTTagCompound compound)
	{
		// Gets the custom taglist we wrote to this compound, if any
		// 1.7.2+ change to compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
		final NBTTagList items = compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

		if ("".equals(this.uniqueID))
		{
			// try to read unique ID from NBT
			this.uniqueID = compound.getString("uniqueID");
			// if it's still "", assign a new one:
			if ("".equals(this.uniqueID))
			{
				this.uniqueID = UUID.randomUUID().toString();
			}
		}

		for (int i = 0; i < items.tagCount(); ++i)
		{
			// 1.7.2+ change to items.getCompoundTagAt(i)
			final NBTTagCompound item = items.getCompoundTagAt(i);
			final int slot = item.getInteger("Slot");

			// Just double-checking that the saved slot index is within our inventory array bounds
			if ((slot >= 0) && (slot < this.getSizeInventory())) {
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(item);
			}
		}
	}

	/**
	 * A custom method to write our inventory to an ItemStack's NBT compound
	 */
	public void writeToNBT(final NBTTagCompound tagcompound)
	{
		// Create a new NBT Tag List to store itemstacks as NBT Tags
		final NBTTagList items = new NBTTagList();

		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			// Only write stacks that contain items
			if (this.getStackInSlot(i) != null)
			{
				// Make a new NBT Tag Compound to write the itemstack and slot index to
				final NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				// Writes the itemstack in slot(i) to the Tag Compound we just made
				this.getStackInSlot(i).writeToNBT(item);

				// add the tag compound to our tag list
				items.appendTag(item);
			}
		}
		tagcompound.setString("uniqueID", this.uniqueID);
		// Add the TagList to the ItemStack's Tag Compound with the name "ItemInventory"
		tagcompound.setTag("ItemInventory", items);
	}
}