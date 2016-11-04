package gtPlusPlus.core.tileentities.machines;

import gtPlusPlus.core.item.base.BaseItemWithCharge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityCharger extends TileEntity implements IInventory {
	private ItemStack[]	items				= new ItemStack[1];	// 18
	private int			progress_Current	= 1;
	private final int	progress_Max		= 1000;
	public float		charge_Current;
	public float		charge_Max			= 10000;
	private float		tempItemChargeValue;

	@Override
	public void closeInventory() {
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int amount) {
		if (this.items[slot] != null) {
			ItemStack itemstack;

			if (this.items[slot].stackSize == amount) {
				itemstack = this.items[slot];
				this.items[slot] = null;
				this.markDirty();
				return itemstack;
			}
			itemstack = this.items[slot].splitStack(amount);
			if (this.items[slot].stackSize == 0) {
				this.items[slot] = null;
			}
			this.markDirty();
			return itemstack;
		}
		return null;
	}

	public float getCharge() {
		return this.charge_Current;
	}

	@Override
	public String getInventoryName() {
		return "container.Charger";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public int getProgress() {
		return this.progress_Current;
	}

	@Override
	public int getSizeInventory() {
		return this.items.length;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return this.items[slot];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		if (this.items[slot] != null) {
			final ItemStack itemstack = this.items[slot];
			this.items[slot] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false
				: player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		final NBTTagList list = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.items = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound comp = list.getCompoundTagAt(i);
			final int j = comp.getByte("Slot") & 255;
			if (j >= 0 && j < this.items.length) {
				this.items[j] = ItemStack.loadItemStackFromNBT(comp);
			}
		}
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack) {
		if (stack != null) {
			this.items[slot] = stack;
			if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
				stack.stackSize = this.getInventoryStackLimit();
			}

			this.markDirty();
		}
	}

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote) {
			if (this.progress_Current < this.progress_Max) {
				this.progress_Current++;
			}
			else if (this.progress_Current >= this.progress_Max) {
				if (this.charge_Current < this.charge_Max) {
					this.charge_Current = this.charge_Current + 500;
				}
				if (this.getStackInSlot(0).getItem() instanceof BaseItemWithCharge) {
					float tempCharge;
					final ItemStack output = this.getStackInSlot(0).copy();
					if (output.stackTagCompound != null) {
						tempCharge = output.stackTagCompound.getFloat("charge_Current");
						output.stackTagCompound = new NBTTagCompound();
						output.stackTagCompound.setFloat("charge_Current", tempCharge + 40);
						this.charge_Current = this.charge_Current - 40;
						tempCharge = 0;
					}
				}
				this.progress_Current = 0;
			}
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		final NBTTagList list = new NBTTagList();

		for (int i = 0; i < this.items.length; ++i) {
			if (this.items[i] != null) {
				final NBTTagCompound comp = new NBTTagCompound();
				comp.setByte("Slot", (byte) i);
				this.items[i].writeToNBT(comp);
				list.appendTag(comp);
			}
		}

		nbt.setTag("Items", list);
	}

}