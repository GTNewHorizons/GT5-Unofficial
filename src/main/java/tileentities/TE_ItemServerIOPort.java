package tileentities;

import kekztech.MultiItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TE_ItemServerIOPort extends TileEntity implements IInventory {
	
	private MultiItemHandler mih;
	
	public void setMultiItemHandler(MultiItemHandler mih) {
		System.out.println("MIH set");
		this.mih = mih;
	}
	
	@Override
	public int getSizeInventory() {
		return (mih != null) ? mih.getItemTypeCapacity() : 9;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return (mih != null) ? mih.getStackInSlot(slot) : null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(mih != null) {
			if(mih.getStackInSlot(slot) != null) {
				final ItemStack obtained = mih.getStackInSlot(slot).copy();
				obtained.stackSize = mih.reduceStackInSlot(slot, amount);
				super.markDirty();
				return obtained;
			} else {
				return null;			
			}			
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if(mih != null) {
			if(itemStack == null || !itemStack.isItemEqual(mih.getStackInSlot(slot))) {
				return;
			} else {
				final int change = itemStack.stackSize - mih.getStackInSlot(slot).stackSize;
				if(change < 0) {
					mih.reduceStackInSlot(slot, change);
				} else {
					mih.increaseStackInSlot(slot, change);
				}
				super.markDirty();
			}			
		}
	}

	@Override
	public String getInventoryName() {
		return "Item Server IO Port";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return (mih != null) ? mih.getPerTypeCapacity() : 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return (mih != null) ? (mih.getStackInSlot(slot).isItemEqual(itemStack) || mih.getStackInSlot(slot) == null) : false;
	}

}
