package gtPlusPlus.api.objects.minecraft;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverInfo;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.util.data.ArrayUtils;

public class BTF_Inventory implements ISidedInventory {

    public final ItemStack[] mInventory;
    public final TileEntityBase mTile;

    public BTF_Inventory(int aSlots, TileEntityBase tile) {
        this.mInventory = new ItemStack[aSlots];
        this.mTile = tile;
    }

    public ItemStack[] getRealInventory() {
        purgeNulls();
        return this.mInventory;
    }

    @Override
    public int getSizeInventory() {
        return this.mInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        return aIndex >= 0 && aIndex < this.mInventory.length ? this.mInventory[aIndex] : null;
    }

    @Override
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

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean setStackToZeroInsteadOfNull(int aIndex) {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        return isValidSlot(aIndex);
    }

    @Override
    public ItemStack decrStackSize(int aIndex, int aAmount) {
        ItemStack tStack = this.getStackInSlot(aIndex);
        ItemStack rStack = GTUtility.copy(new Object[] { tStack });
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

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        ArrayList<Integer> tList = new ArrayList<>();
        CoverInfo coverInfo = this.mTile.getCoverInfoAtSide(side);
        boolean tSkip = coverInfo.letsItemsIn(-2) || coverInfo.letsItemsIn(-2);

        for (int rArray = 0; rArray < this.getSizeInventory(); ++rArray) {
            if (this.isValidSlot(rArray)
                && (tSkip || coverInfo.letsItemsOut(rArray) || coverInfo.letsItemsIn(rArray))) {
                tList.add(rArray);
            }
        }

        int[] arg6 = new int[tList.size()];

        for (int i = 0; i < arg6.length; ++i) {
            arg6[i] = tList.get(i);
        }

        return arg6;
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return this.isValidSlot(aIndex) && aStack != null
            && aIndex < this.mInventory.length
            && (this.mInventory[aIndex] == null || GTUtility.areStacksEqual(aStack, this.mInventory[aIndex]))
            && this.allowPutStack(this.mTile, aIndex, ForgeDirection.getOrientation(ordinalSide), aStack);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return this.isValidSlot(aIndex) && aStack != null
            && aIndex < this.mInventory.length
            && this.allowPullStack(this.mTile, aIndex, ForgeDirection.getOrientation(ordinalSide), aStack);
    }

    public boolean allowPullStack(TileEntityBase mTile2, int aIndex, ForgeDirection side, ItemStack aStack) {
        return aIndex >= 0 && aIndex < this.getSizeInventory();
    }

    public boolean allowPutStack(TileEntityBase aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return (aIndex >= 0 && aIndex < this.getSizeInventory())
            && (this.mInventory[aIndex] == null || GTUtility.areStacksEqual(this.mInventory[aIndex], aStack));
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public final boolean hasCustomInventoryName() {
        return mTile != null ? mTile.hasCustomInventoryName() : false;
    }

    @Override
    public void markDirty() {
        if (mTile != null) {
            purgeNulls();
            mTile.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public final String getInventoryName() {
        return this.mTile != null ? mTile.getInventoryName() : "";
    }

    public boolean isFull() {
        for (int s = 0; s < this.getSizeInventory(); s++) {
            ItemStack slot = mInventory[s];
            if (slot == null || slot.stackSize != slot.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (int s = 0; s < this.getSizeInventory(); s++) {
            ItemStack slot = mInventory[s];
            if (slot == null) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean addItemStack(ItemStack aInput) {
        if (aInput != null & (isEmpty() || !isFull())) {
            for (int s = 0; s < this.getSizeInventory(); s++) {
                if (mInventory != null && mInventory[s] != null) {
                    ItemStack slot = mInventory[s];
                    if (slot == null || (slot != null && GTUtility.areStacksEqual(aInput, slot)
                        && slot.stackSize != slot.getItem()
                            .getItemStackLimit(slot))) {
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
        }
        return false;
    }

    public final void purgeNulls() {
        ItemStack[] aTemp = ArrayUtils.removeNulls(this.mInventory);
        for (int g = 0; g < this.getSizeInventory(); g++) {
            if (aTemp.length < this.getSizeInventory()) {
                if (g <= aTemp.length - 1) {
                    this.mInventory[g] = aTemp[g];
                } else {
                    this.mInventory[g] = null;
                }
            } else {
                this.mInventory[g] = aTemp[g];
            }
        }
    }
}
