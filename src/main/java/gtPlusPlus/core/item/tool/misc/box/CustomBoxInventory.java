package gtPlusPlus.core.item.tool.misc.box;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public abstract class CustomBoxInventory implements IInventory {

	private final String name;
	protected String uniqueID;
	
	/** Provides NBT Tag Compound to reference */
	private final ItemStack invItem;

	/** Defining your inventory size this way is handy */
	public final int INV_SIZE;

	/** Inventory's size must be same as number of slots you add to the Container class */
	private ItemStack[] inventory;

	/**
	 * @param itemstack - the ItemStack to which this inventory belongs
	 */
	public CustomBoxInventory(ItemStack stack, String name2){
		this(stack, name2, 8);
	}
	
	/**
	 * @param itemstack - the ItemStack to which this inventory belongs
	 */
	public CustomBoxInventory(ItemStack stack, String name2, int slots)
	{
		invItem = stack;
		name = name2;
		INV_SIZE = slots;
		inventory = new ItemStack[INV_SIZE];
		
		/** initialize variable within the constructor: */
		uniqueID = "";

		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			// no tag compound means the itemstack does not yet have a UUID, so assign one:
			uniqueID = UUID.randomUUID().toString();
		}

		/** When reading from NBT: */
		if ("".equals(uniqueID))
		{
			// try to read unique ID from NBT
			uniqueID = stack.getTagCompound().getString("uniqueID");
			// if it's still "", assign a new one:
			if ("".equals(uniqueID))
			{
				uniqueID = UUID.randomUUID().toString();
			}
		}

		/** Writing to NBT: */
		// just add this line:
		stack.getTagCompound().setString("uniqueID", this.uniqueID);
		
		// note that it's okay to use stack instead of invItem right there
		// both reference the same memory location, so whatever you change using
		// either reference will change in the other

		// Read the inventory contents from NBT
		readFromNBT(stack.getTagCompound());
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)
		{
			if(stack.stackSize > amount)
			{
				stack = stack.splitStack(amount);
				// Don't forget this line or your inventory will not be saved!
				markDirty();
			}
			else
			{
				// this method also calls onInventoryChanged, so we don't need to call it again
				setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inventory[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = getInventoryStackLimit();
		}

		// Don't forget this line or your inventory will not be saved!
		markDirty();
	}

	// 1.7.2+ renamed to getInventoryName
	@Override
	public String getInventoryName()
	{
		return name;
	}

	// 1.7.2+ renamed to hasCustomInventoryName
	@Override
	public boolean hasCustomInventoryName()
	{
		return name.length() > 0;
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
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0) {
				inventory[i] = null;
			}
		}
		
		// This line here does the work:		
		writeToNBT(invItem.getTagCompound());
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
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
	public abstract boolean isItemValidForSlot(int slot, ItemStack itemstack);

	/**
	 * A custom method to read our inventory from an ItemStack's NBT compound
	 */
	public void readFromNBT(NBTTagCompound compound)
	{
		// Gets the custom taglist we wrote to this compound, if any
		// 1.7.2+ change to compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
		NBTTagList items = compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < items.tagCount(); ++i)
		{
			// 1.7.2+ change to items.getCompoundTagAt(i)
			NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
			int slot = item.getInteger("Slot");

			// Just double-checking that the saved slot index is within our inventory array bounds
			if (slot >= 0 && slot < getSizeInventory()) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(item);
			}
		}
	}

	/**
	 * A custom method to write our inventory to an ItemStack's NBT compound
	 */
	public void writeToNBT(NBTTagCompound tagcompound)
	{
		// Create a new NBT Tag List to store itemstacks as NBT Tags
		NBTTagList items = new NBTTagList();

		for (int i = 0; i < getSizeInventory(); ++i)
		{
			// Only write stacks that contain items
			if (getStackInSlot(i) != null)
			{
				// Make a new NBT Tag Compound to write the itemstack and slot index to
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				// Writes the itemstack in slot(i) to the Tag Compound we just made
				getStackInSlot(i).writeToNBT(item);

				// add the tag compound to our tag list
				items.appendTag(item);
			}
		}
		// Add the TagList to the ItemStack's Tag Compound with the name "ItemInventory"
		tagcompound.setTag("ItemInventory", items);
	}

}
