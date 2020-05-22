package common.tileentities;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TE_ItemProxySource extends TileEntity implements IInventory {

	private ItemStack[] slots = new ItemStack[16];
	private String idCache = null;

	/**
	 * Builds a simple unique identifier for this TileEntity by appending
	 * the x, y, and z coordinates in a string.
	 *
	 * @return unique identifier for this TileEntity
	 */
	public String getIdentifier() {
		if(idCache == null) {
			idCache = "" + super.xCoord + super.yCoord + super.zCoord;
			return idCache;
		} else {
			return idCache;
		}
	}

	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(slots[slot] != null) {
			
			ItemStack copy;
			
			if(slots[slot].stackSize == amount) {
				copy = slots[slot];
				slots[slot] = null;
				super.markDirty();
				return copy;
			} else {
				copy = slots[slot].splitStack(amount);
				if(slots[slot].stackSize == 0) {
					slots[slot] = null;
				}
				return copy;
			}
			
		} else {
			return null;			
		}	
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		slots[slot] = itemStack;
		if(itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
			itemStack.stackSize = getInventoryStackLimit();
		}
		super.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "Item Proxy Source";
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
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
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
		return true;
	}
}
