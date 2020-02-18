package tileentities;

import kekztech.MultiItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TE_ItemServerIOPort extends TileEntity implements ISidedInventory {
	
	private MultiItemHandler mih;
	
	public void setMultiItemHandler(MultiItemHandler mih) {
		this.mih = mih;
		System.out.println("MIH set");
	}
	
	@Override
	public int getSizeInventory() {
		return (mih != null) ? mih.getItemTypeCapacity() : 0;
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
		System.out.println("Set slot: " + slot);
		if(mih != null) {
			if(itemStack == null) {
				return;
			} else {
				if(!mih.insertStackInSlot(slot, itemStack)) {
					final int delta = itemStack.stackSize - mih.getStackInSlot(slot).stackSize;
					if(delta < 0) {
						System.out.println("Set slot reduce: " + itemStack.getDisplayName());
						mih.reduceStackInSlot(slot, delta);
					} else {
						System.out.println("Set slot increase: " + itemStack.getDisplayName());
						mih.increaseStackInSlot(slot, delta);
					}
					
				} else {
					System.out.println("Allocated new slot for: " + itemStack.getDisplayName());
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
		return (mih != null) ? (mih.getStackInSlot(slot).isItemEqual(itemStack) || mih.getStackInSlot(slot) == null) : false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if(mih != null) {
			final int[] as = new int[mih.getItemTypeCapacity()];
			for(int i = 0; i < mih.getItemTypeCapacity(); i++) {
				as[i] = i;
			}
			return as;
		} else {
			return new int[1];
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return (mih != null) ? true : false;
	}

}
