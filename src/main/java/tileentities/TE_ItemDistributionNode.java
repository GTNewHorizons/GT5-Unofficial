package tileentities;

import kekztech.IConduit;
import kekztech.ItemDistributionNetworkController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TE_ItemDistributionNode extends TileEntity implements IConduit, IInventory {
	
private ItemDistributionNetworkController network;
	
	public TE_ItemDistributionNode() {
	}
	
	@Override
	public void setNetwork(ItemDistributionNetworkController network) {
		this.network = network;
	}
	
	@Override
	public ItemDistributionNetworkController getNetwork() {
		return network;
	}
	
	@Override
	public int getSizeInventory() {
		return 16;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
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
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		// TODO Auto-generated method stub
		return false;
	}

}
