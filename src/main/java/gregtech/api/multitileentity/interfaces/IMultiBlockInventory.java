package gregtech.api.multitileentity.interfaces;

import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IMultiBlockInventory {
    boolean hasInventoryBeenModified        (MultiBlockPart aPart);
    boolean isValidSlot                     (MultiBlockPart aPart, int aIndex);
    boolean addStackToSlot                  (MultiBlockPart aPart, int aIndex, ItemStack aStack);
    boolean addStackToSlot                  (MultiBlockPart aPart, int aIndex, ItemStack aStack, int aAmount);
    int[] getAccessibleSlotsFromSide        (MultiBlockPart aPart, byte aSide);
    boolean canInsertItem                   (MultiBlockPart aPart, int aSlot, ItemStack aStack, byte aSide);
    boolean canExtractItem                  (MultiBlockPart aPart, int aSlot, ItemStack aStack, byte aSide);
    int getSizeInventory                    (MultiBlockPart aPart);
    ItemStack getStackInSlot                (MultiBlockPart aPart, int aSlot);
    ItemStack decrStackSize                 (MultiBlockPart aPart, int aSlot, int aDecrement);
    ItemStack getStackInSlotOnClosing       (MultiBlockPart aPart, int aSlot);
    void setInventorySlotContents           (MultiBlockPart aPart, int aSlot, ItemStack aStack);
    String getInventoryName                 (MultiBlockPart aPart);
    boolean hasCustomInventoryName          (MultiBlockPart aPart);
    int getInventoryStackLimit              (MultiBlockPart aPart);
    void markDirty                          (MultiBlockPart aPart);
    boolean isUseableByPlayer               (MultiBlockPart aPart, EntityPlayer aPlayer);
    void openInventory                      (MultiBlockPart aPart);
    void closeInventory                     (MultiBlockPart aPart);
    boolean isItemValidForSlot              (MultiBlockPart aPart, int aSlot, ItemStack aStack);
}
