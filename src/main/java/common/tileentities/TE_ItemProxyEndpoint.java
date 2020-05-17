package common.tileentities;

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TE_ItemProxyEndpoint extends TileEntity implements ISidedInventory {

	private byte channel = -1;
	private IInventory proxyInventory = null;
	private int tickCounter = 0;

	public TE_ItemProxyEndpoint() {
		channel = 0;
	}

	public void setChannel(byte channel) {
		this.channel = channel;
	}

	public int getChannel() { return channel; }
	
	@Override
	public void updateEntity() {
		if(tickCounter == 20) {
			if(channel != -1) {
				proxyInventory = searchSource();
			}
			tickCounter = 0;				
		}
		tickCounter++;
	}
	
	private TE_ItemProxySource searchSource() {
		
		final HashSet<TE_ItemProxySource> sources = new HashSet<>();
		final HashSet<String> visited = new HashSet<>();
		
		for(ForgeDirection next : ForgeDirection.VALID_DIRECTIONS) {
			final TileEntity te = super.getWorldObj().getTileEntity(
					super.xCoord + next.offsetX, 
					super.yCoord + next.offsetY, 
					super.zCoord + next.offsetZ);
			if(te instanceof TE_ItemProxyCable) {
				final TE_ItemProxyCable cable = (TE_ItemProxyCable) te;
				if(cable.isConnected(next.getOpposite())) {
					searchSourceRecursive(sources, visited, next.getOpposite(), cable);
				}
			}
		}
		
		if(sources.isEmpty()) {
			return null;
		} else {
			return sources.iterator().next();
		}
		
	}
	
	private void searchSourceRecursive(HashSet<TE_ItemProxySource> sources, HashSet<String> visited, 
			ForgeDirection from, TE_ItemProxyCable nextTarget) {
		
		if(!visited.contains(nextTarget.getIdentifier())) {
			visited.add(nextTarget.getIdentifier());

			for(ForgeDirection next : ForgeDirection.VALID_DIRECTIONS) {
				if(next != from) {
					final TileEntity te = super.getWorldObj().getTileEntity(
							nextTarget.xCoord + next.offsetX,
							nextTarget.yCoord + next.offsetY,
							nextTarget.zCoord + next.offsetZ);
					if(te instanceof TE_ItemProxyCable) {
						final TE_ItemProxyCable cable = (TE_ItemProxyCable) te;
						if(cable.isConnected(next.getOpposite())) {
							searchSourceRecursive(sources, visited, next.getOpposite(), cable);
						}
					} else if (te instanceof TE_ItemProxySource) {
						sources.add((TE_ItemProxySource) te);
					}
				}
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(proxyInventory != null && slot == 0) {
			return proxyInventory.getStackInSlot(channel);
		} else {
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(proxyInventory != null && slot == 0) {
			return proxyInventory.decrStackSize(channel, amount);
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return (proxyInventory != null) ? proxyInventory.getStackInSlotOnClosing(channel) : null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if(proxyInventory != null && slot == 0) {
			proxyInventory.setInventorySlotContents(channel, itemStack);
		}
	}

	@Override
	public String getInventoryName() {
		return (proxyInventory != null) ? "Connected: " + proxyInventory.getInventoryName() : "Untethered Proxy";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return (proxyInventory != null) ? proxyInventory.getInventoryStackLimit() : 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		if(proxyInventory != null && slot == 0) {
			return proxyInventory.isItemValidForSlot(channel, itemStack);
		} else {
			return false;
		}
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{0};
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return isItemValidForSlot(slot, itemStack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return slot == 0;
	}
	
}
