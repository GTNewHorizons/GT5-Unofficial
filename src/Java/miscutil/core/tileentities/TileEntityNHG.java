package miscutil.core.tileentities;

import miscutil.core.item.ModItems;
import miscutil.core.item.general.fuelrods.FuelRod_Base;
import miscutil.core.util.UtilsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityNHG extends TileEntity implements IInventory
{
	private ItemStack[] items = new ItemStack[19]; //18
	private int internalClock = 0;

	@Override
	public int getSizeInventory()
	{
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
	
			return items[slot];

	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (items[slot] != null)
		{
			ItemStack itemstack;

			if (items[slot].stackSize == amount)
			{
				itemstack = items[slot];
				items[slot] = null;
				markDirty();
				return itemstack;
			}
			itemstack = items[slot].splitStack(amount);
			if (items[slot].stackSize == 0) items[slot] = null;
			markDirty();
			return itemstack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (items[slot] != null)
		{
			ItemStack itemstack = items[slot];
			items[slot] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		if (stack != null){
			items[slot] = stack;
			if (stack != null && stack.stackSize > getInventoryStackLimit())
			{
				stack.stackSize = getInventoryStackLimit();
			}


			markDirty();
		}
	}

	@Override
	public String getInventoryName()
	{
		return "container.NHG";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		items = new ItemStack[getSizeInventory()];

		for (int i = 0; i < list.tagCount(); ++i) { NBTTagCompound comp = list.getCompoundTagAt(i); int j = comp.getByte("Slot") & 255; if (j >= 0 && j < items.length)
		{
			items[j] = ItemStack.loadItemStackFromNBT(comp);
		}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < items.length; ++i)
		{
			if (items[i] != null)
			{
				NBTTagCompound comp = new NBTTagCompound();
				comp.setByte("Slot", (byte)i);
				items[i].writeToNBT(comp);
				list.appendTag(comp);
			}
		}

		nbt.setTag("Items", list);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return true;
	}

	//Machine Code - TODO

	@Override
	public void updateEntity() {			
				if (internalClock <= 20){
					internalClock++;
				}
				else {
					ItemStack validFuelRod_Type_A = UtilsItems.getSimpleStack(ModItems.FuelRod_Thorium);
					ItemStack validFuelRod_Type_B = UtilsItems.getSimpleStack(ModItems.FuelRod_Uranium);
					ItemStack validFuelRod_Type_C = UtilsItems.getSimpleStack(ModItems.FuelRod_Plutonium);
					for (int i = 0; i < getSizeInventory(); i++){
						ItemStack tempCurrent = items[i];
						if (tempCurrent == validFuelRod_Type_A ||tempCurrent == validFuelRod_Type_B || tempCurrent == validFuelRod_Type_C){
							Item tempItem = tempCurrent.getItem();
							FuelRod_Base tempItem2 = (FuelRod_Base) tempItem;
							if (tempItem2.getHeat() <= 5000){
								tempItem2.addHeat(5);
							}
							if (tempItem2.getFuelRemaining() >= 1){
								tempItem2.addFuel(-1);
							}
							ItemStack validFuelRod = UtilsItems.getSimpleStack(tempItem2);
							setInventorySlotContents(i, validFuelRod);
						}
					}

					internalClock=0;
				}			
		super.updateEntity();
	}



}