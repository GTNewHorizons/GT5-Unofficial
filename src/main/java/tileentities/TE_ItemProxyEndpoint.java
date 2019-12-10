package tileentities;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TE_ItemProxyEndpoint extends TileEntity implements ISidedInventory {
	
	private UUID channel = null;
	private int subChannel = -1;
	private IInventory proxyInventory = null;
	private int tickCounter = 0;
	private ItemStack[] slots = new ItemStack[2];
	
	public void setChannel(UUID channel) {
		this.channel = channel;
	}
	
	public void setSubChannel(int subChannel) {
		this.subChannel = subChannel;
	}
	
	@Override
	public void updateEntity() {
		if(tickCounter == 20) {
			
			if(slots[1] == null || !slots[1].getUnlocalizedName().equals("gt.integrated_circuit") || slots[1].getItemDamage() >= 16) {
				setSubChannel(-1);
			}
			
			if(slots[1] != null && slots[1].getUnlocalizedName().equals("gt.integrated_circuit") && slots[1].getItemDamage() < 16) {
				setSubChannel(slots[1].getItemDamage());
			}
			
			if(channel != null && subChannel != -1) {
				TE_ItemProxySource source = searchSource(channel);
				if(source != null) {
					proxyInventory = source;		
				}
			}
			tickCounter = 0;				
		}
		tickCounter++;
	}
	
	public TE_ItemProxySource searchSource(UUID channel) {
		
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
					searchSourceRecursive(sources, visited, next.getOpposite(), cable, channel);					
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
			ForgeDirection from, TE_ItemProxyCable nextTarget, UUID channel) {
		
		if(visited.contains(nextTarget.getIdentifier())) {
			return;
		} else {
			visited.add(nextTarget.getIdentifier());
			
			for(ForgeDirection next : ForgeDirection.VALID_DIRECTIONS) {
				if(next == from || !nextTarget.isConnected(next)) {
					continue;
				}
				final TileEntity te = super.getWorldObj().getTileEntity(
						nextTarget.xCoord + next.offsetX, 
						nextTarget.yCoord + next.offsetY, 
						nextTarget.zCoord + next.offsetZ);
				if(te instanceof TE_ItemProxyCable) {
					final TE_ItemProxyCable cable = (TE_ItemProxyCable) te;
					if(cable.isConnected(next.getOpposite())) {
						searchSourceRecursive(sources, visited, next.getOpposite(), cable, channel);					
					}
				} else if (te instanceof TE_ItemProxySource) {
					final TE_ItemProxySource source = (TE_ItemProxySource) te;
					if(source.getChannel().equals(channel)) {
						sources.add((TE_ItemProxySource) te);						
					}
				}
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot == 0) {
			return (proxyInventory != null) ? proxyInventory.getStackInSlot(subChannel) : null;
		} else {
			return slots[slot];
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(slot == 0) {
			return (proxyInventory != null) ? proxyInventory.decrStackSize(subChannel, amount) : null;	
		} else {
			final ItemStack copy = slots[1].copy();
			slots[1] = null;
			super.markDirty();
			return copy;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return (proxyInventory != null) ? proxyInventory.getStackInSlotOnClosing(subChannel) : null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if(slot == 0 && proxyInventory != null) {
			proxyInventory.setInventorySlotContents(subChannel, itemStack);						
		} else {
			slots[slot] = itemStack;
		}
	}

	@Override
	public String getInventoryName() {
		return (proxyInventory != null) ? "Connected Proxy" : "Untethered Proxy";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return (proxyInventory != null) ? proxyInventory.getInventoryStackLimit() : 1;
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
		if(slot == 0 && proxyInventory != null) {
			return proxyInventory.isItemValidForSlot(subChannel, itemStack);
		} else {
			return itemStack != null && itemStack.getUnlocalizedName().equals("gt.integrated_circuit");
		}
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		final int[] as = {0};
		return as;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return isItemValidForSlot(slot, itemStack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return (slot == 0) ? true : false;
	}
	
}
