package tileentities;

import kekztech.MultiItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TE_ItemServerIOPort extends TileEntity implements IInventory {
	
	private MultiItemHandler mih;
	
	public void setMultiItemHandler(MultiItemHandler mih) {
		this.mih = mih;
	}
	
	@Override
	public int getSizeInventory() {
		return mih.getItemTypeCapacity();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInventoryName() {
		return "Item Server";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return mih.getPerTypeCapacity();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		// TODO Auto-generated method stub
		return false;
	}

}
