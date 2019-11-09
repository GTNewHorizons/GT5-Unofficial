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
		ItemDistributionNetworkController.placeConduit(this);
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
		return network.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		
		if(network.getStackInSlot(slot) != null) {
			if(network.getStackInSlot(slot).stackSize == amount) {
				final ItemStack itemStack = network.getStackInSlot(slot);
				network.setStackInSlot(slot, null);
				super.markDirty();
				return itemStack;
			} else {
				final ItemStack itemStack = network.getStackInSlot(slot).splitStack(amount);
				if(network.getStackInSlot(slot).stackSize == 0) {
					network.setStackInSlot(slot, null);
				}
				super.markDirty();
				return itemStack;
			}
		}
		
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(network.getStackInSlot(slot) != null) {
			final ItemStack itemStack = network.getStackInSlot(slot);
			network.setStackInSlot(slot, null);
			return itemStack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if(itemStack == null) {
			return;
		}
		if(itemStack.stackSize > getInventoryStackLimit()) {
			itemStack.stackSize = getInventoryStackLimit();
		}
		network.setStackInSlot(slot, itemStack);
		super.markDirty();
	}

	@Override
	public String getInventoryName() {
		return network.getUUID().toString();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
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
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return network.isInputSlot(slot) && network.getStackInSlot(slot).isItemEqual(itemStack);
	}

}
