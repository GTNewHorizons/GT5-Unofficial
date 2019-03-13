package gtPlusPlus.api.objects.minecraft;

import java.util.ArrayList;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class BTF_Inventory implements ISidedInventory{

	public final ItemStack[] mInventory;
	public final TileEntityBase mTile;
	
	public BTF_Inventory(int aSlots, TileEntityBase tile) {
		this.mInventory = new ItemStack[aSlots];
		this.mTile = tile;
	}

	public ItemStack[] getRealInventory() {
		return this.mInventory;
	}
	
	public int getSizeInventory() {
		return this.mInventory.length;
	}

	public ItemStack getStackInSlot(int aIndex) {
		return aIndex >= 0 && aIndex < this.mInventory.length ? this.mInventory[aIndex] : null;
	}

	public void setInventorySlotContents(int aIndex, ItemStack aStack) {
		if (aIndex >= 0 && aIndex < this.mInventory.length) {
			this.mInventory[aIndex] = aStack;
		}
	}	

	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	public boolean isValidSlot(int aIndex) {
		return true;
	}
	
	public int getInventoryStackLimit() {
		return 64;
	}
	
 
    public boolean setStackToZeroInsteadOfNull(int aIndex) {
        return false;
}

	public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
		return isValidSlot(aIndex);
	}

	public ItemStack decrStackSize(int aIndex, int aAmount) {
		ItemStack tStack = this.getStackInSlot(aIndex);
		ItemStack rStack = GT_Utility.copy(new Object[]{tStack});
		if (tStack != null) {
			if (tStack.stackSize <= aAmount) {
				if (this.setStackToZeroInsteadOfNull(aIndex)) {
					tStack.stackSize = 0;
				} else {
					this.setInventorySlotContents(aIndex, (ItemStack) null);
				}
			} else {
				rStack = tStack.splitStack(aAmount);
				if (tStack.stackSize == 0 && !this.setStackToZeroInsteadOfNull(aIndex)) {
					this.setInventorySlotContents(aIndex, (ItemStack) null);
				}
			}
		}

		return rStack;
	}

	public int[] getAccessibleSlotsFromSide(int aSide) {
		ArrayList<Integer> tList = new ArrayList<Integer>();
		TileEntityBase tTileEntity = this.mTile;
		boolean tSkip = tTileEntity.getCoverBehaviorAtSide((byte) aSide).letsItemsIn((byte) aSide,
				tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getCoverDataAtSide((byte) aSide), -2,
				tTileEntity)
				|| tTileEntity.getCoverBehaviorAtSide((byte) aSide).letsItemsOut((byte) aSide,
						tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getCoverDataAtSide((byte) aSide), -2,
						tTileEntity);

		for (int rArray = 0; rArray < this.getSizeInventory(); ++rArray) {
			if (this.isValidSlot(rArray) && (tSkip
					|| tTileEntity.getCoverBehaviorAtSide((byte) aSide).letsItemsOut((byte) aSide,
							tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getCoverDataAtSide((byte) aSide),
							rArray, tTileEntity)
					|| tTileEntity.getCoverBehaviorAtSide((byte) aSide).letsItemsIn((byte) aSide,
							tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getCoverDataAtSide((byte) aSide),
							rArray, tTileEntity))) {
				tList.add(Integer.valueOf(rArray));
			}
		}

		int[] arg6 = new int[tList.size()];

		for (int i = 0; i < arg6.length; ++i) {
			arg6[i] = ((Integer) tList.get(i)).intValue();
		}

		return arg6;
	}

	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		return this.isValidSlot(aIndex) && aStack != null && aIndex < this.mInventory.length
				&& (this.mInventory[aIndex] == null || GT_Utility.areStacksEqual(aStack, this.mInventory[aIndex]))
				&& this.allowPutStack(this.mTile, aIndex, (byte) aSide, aStack);
	}

	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
		return this.isValidSlot(aIndex) && aStack != null && aIndex < this.mInventory.length
				&& this.allowPullStack(this.mTile, aIndex, (byte) aSide, aStack);
	}
	
	public boolean allowPullStack(TileEntityBase mTile2, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex >= 0 && aIndex < this.getSizeInventory();
	}

	public boolean allowPutStack(TileEntityBase aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return (aIndex >= 0 && aIndex < this.getSizeInventory()) && (this.mInventory[aIndex] == null || GT_Utility.areStacksEqual(this.mInventory[aIndex], aStack));
	}
	
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	public final boolean hasCustomInventoryName() {
		return mTile != null ? mTile.hasCustomInventoryName() : false;
	}


	public void markDirty() {
		if (mTile != null) {
			mTile.markDirty();
		}
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	public void openInventory() {
		
	}

	public void closeInventory() {
		
	}
	
	@Override
	public final String getInventoryName() {
		return  this.mTile != null ? mTile.getInventoryName() : "";
	}

	public boolean isFull() {
		for (int s=0;s<this.getSizeInventory();s++) {
			ItemStack slot = mInventory[s];
			if (slot == null || slot.stackSize != slot.getMaxStackSize()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isEmpty() {
		for (int s=0;s<this.getSizeInventory();s++) {
			ItemStack slot = mInventory[s];
			if (slot == null) {
				continue;
			}
			else {
				return false;
			}
		}
		return true;
	}

	public boolean addItemStack(ItemStack aInput) {
		if (isEmpty() || !isFull()) {
			for (int s = 0; s < this.getSizeInventory(); s++) {
				ItemStack slot = mInventory[s];
				if (slot == null
						|| (GT_Utility.areStacksEqual(aInput, slot) && slot.stackSize != slot.getMaxStackSize())) {
					if (slot == null) {
						slot = aInput.copy();
					} else {
						slot.stackSize++;
					}
					this.setInventorySlotContents(s, slot);
					return true;
				}
			}
		}
		return false;
	}


}
