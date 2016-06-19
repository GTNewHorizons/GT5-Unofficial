package miscutil.core.tileentities.machines;

import miscutil.core.item.base.BaseItemWithCharge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityCharger extends TileEntity implements IInventory
{
	private ItemStack[] items = new ItemStack[1]; //18
	private int progress_Current = 1;
	private int progress_Max = 1000;
	public float charge_Current;
	public float charge_Max = 10000;
	private float tempItemChargeValue;
	
	public float getCharge(){
		return charge_Current;
	}

	public int getProgress(){
		return progress_Current;
	}

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
		return "container.Charger";
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

	@Override
	public void updateEntity() {
		if(!this.worldObj.isRemote){
			if (progress_Current < progress_Max){
				progress_Current++;
			}
			else if (progress_Current >= progress_Max){
				if (charge_Current < charge_Max){
					charge_Current = charge_Current+500;
				}	
				if (getStackInSlot(0).getItem() instanceof BaseItemWithCharge){
					float tempCharge;
					ItemStack output = getStackInSlot(0).copy();
					if (output.stackTagCompound != null){
						tempCharge = output.stackTagCompound.getFloat("charge_Current");
						output.stackTagCompound = new NBTTagCompound();
						output.stackTagCompound.setFloat("charge_Current", tempCharge+40);
						this.charge_Current = charge_Current-40;
						tempCharge = 0;
					}
				}
				progress_Current = 0;
			}
		}
	}

}