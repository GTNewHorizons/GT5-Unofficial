package gtPlusPlus.core.inventories;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryWorkbenchCrafting implements IInventory{

	private String name = "Inventory Crafting";

	/** Defining your inventory size this way is handy */
	public static final int INV_SIZE = 9;

	/** Inventory's size must be same as number of slots you add to the Container class */
	private ItemStack[] inventory = new ItemStack[INV_SIZE];

	/**
	 * @param itemstack - the ItemStack to which this inventory belongs
	 */
	public InventoryWorkbenchCrafting()
	{

	}
	
	public void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagList list = nbt.getTagList("Items", 10);
        inventory = new ItemStack[INV_SIZE];
        for(int i = 0;i<list.tagCount();i++)
        {
            NBTTagCompound data = list.getCompoundTagAt(i);
            int slot = data.getInteger("Slot");
            if(slot >= 0 && slot < INV_SIZE)
            {
                inventory[slot] = ItemStack.loadItemStackFromNBT(data);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList list = new NBTTagList();
        for(int i = 0;i<INV_SIZE;i++)
        {
            ItemStack stack = inventory[i];
            if(stack != null)
            {
                NBTTagCompound data = new NBTTagCompound();
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
		return inventory.length;
	}
	
	public ItemStack[] getInventory(){
		return inventory;
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
				// this method also calls markDirty, so we don't need to call it again
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
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		// Don't want to be able to store the inventory item within itself
		// Bad things will happen, like losing your inventory
		// Actually, this needs a custom Slot to work
		return true;
	}
}