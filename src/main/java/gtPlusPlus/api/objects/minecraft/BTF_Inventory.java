package gtPlusPlus.api.objects.minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.util.GTUtility;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class BTF_Inventory implements ISidedInventory {

    public final ItemStack @NotNull [] mInventory;
    public final TileEntityBase mTile;

    public BTF_Inventory(int aSlots, TileEntityBase tile) {
        this.mInventory = new ItemStack[aSlots];
        this.mTile = tile;
    }

    public ItemStack @NotNull [] getRealInventory() {
        purgeNulls();
        return this.mInventory;
    }

    @Override
    public int getSizeInventory() {
        return this.mInventory.length;
    }

    @Override
    public @Nullable ItemStack getStackInSlot(int aIndex) {
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
    public @Nullable ItemStack decrStackSize(int aIndex, int aAmount) {
        ItemStack tStack = this.getStackInSlot(aIndex);
        ItemStack rStack = GTUtility.copy(new Object[] { tStack });
        if (tStack != null) {
            if (tStack.stackSize <= aAmount) {
                if (this.setStackToZeroInsteadOfNull(aIndex)) {
                    tStack.stackSize = 0;
                } else {
                    this.setInventorySlotContents(aIndex, null);
                }
            } else {
                rStack = tStack.splitStack(aAmount);
                if (tStack.stackSize == 0 && !this.setStackToZeroInsteadOfNull(aIndex)) {
                    this.setInventorySlotContents(aIndex, null);
                }
            }
        }

        return rStack;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        ArrayList<Integer> tList = new ArrayList<>();

        for (int rArray = 0; rArray < this.getSizeInventory(); ++rArray) {
            if (this.isValidSlot(rArray)) {
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
    public boolean canInsertItem(int aIndex, @Nullable ItemStack aStack, int ordinalSide) {
        return this.isValidSlot(aIndex) && aStack != null
            && aIndex < this.mInventory.length
            && (this.mInventory[aIndex] == null || GTUtility.areStacksEqual(aStack, this.mInventory[aIndex]))
            && this.allowPutStack(this.mTile, aIndex, ForgeDirection.getOrientation(ordinalSide), aStack);
    }

    @Override
    public boolean canExtractItem(int aIndex, @Nullable ItemStack aStack, int ordinalSide) {
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
    public @Nullable ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public final boolean hasCustomInventoryName() {
        return mTile != null && mTile.hasCustomInventoryName();
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
            if (slot != null) return false;
        }
        return true;
    }

    public boolean addItemStack(@Nullable ItemStack aInput) {
        if (aInput != null & (isEmpty() || !isFull())) {
            for (int s = 0; s < this.getSizeInventory(); s++) {
                ItemStack slot = mInventory[s];
                if (slot == null) {
                    this.setInventorySlotContents(s, aInput);
                    return true;
                } else if (slot.getItem() != null && GTUtility.areStacksEqual(aInput, slot)
                    && slot.stackSize != slot.getItem()
                        .getItemStackLimit(slot)) {
                            slot.stackSize += aInput.stackSize;
                            this.setInventorySlotContents(s, slot);
                            return true;
                        }
            }
        }
        return false;
    }

    public final void purgeNulls() {
        List<ItemStack> list = new ObjectArrayList<>(this.mInventory);
        list.removeAll(Collections.singleton((ItemStack) null));
        ItemStack[] aTemp = list.toArray(new ItemStack[0]);
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
