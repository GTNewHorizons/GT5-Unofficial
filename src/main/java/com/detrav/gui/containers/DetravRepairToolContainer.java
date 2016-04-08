package com.detrav.gui.containers;

import com.detrav.items.DetravMetaGeneratedTool01;
import forestry.core.gui.slots.SlotLocked;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by wital_000 on 08.04.2016.
 */
public class DetravRepairToolContainer extends Container {
    public IInventory slots = new InventoryCrafting(this, 1, 1);
    public ItemStack mItem;
    private World worldObj;

    public DetravRepairToolContainer(InventoryPlayer inventoryPlayer, World aWorld, ItemStack aStack) {
        this.worldObj = aWorld;
        this.addSlotToContainer(new Slot(slots, 0, 80, 35));
        mItem = aStack;
        //new Slot()
        bindPlayerInventory(inventoryPlayer);

        //DetravMetaGeneratedTool01.INSTANCE.getToolStats(mItem).
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, 84 + i * 18));
            }
        }


        for (int i = 0; i < 9; i++) {
            ItemStack stackInSlot = inventoryPlayer.getStackInSlot(i);
            if (mItem!=null && stackInSlot!=null && mItem == stackInSlot) {
                addSlotToContainer(new SlotLocked(inventoryPlayer, i, 8 + i * 18, 142));
            } else {
                addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (!this.worldObj.isRemote) {
            //for (int i = 0; i < 9; ++i)
            //{
            ItemStack itemstack = this.slots.getStackInSlotOnClosing(0);

            if (itemstack != null) {
                p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
            }
            //}
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return null;
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex != 0) return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        if(DetravMetaGeneratedTool01.getToolDamage(mItem) >= DetravMetaGeneratedTool01.getToolMaxDamage(mItem))
            return null;
        ItemStack tStack = aPlayer.inventory.getItemStack();
        if (tStack != null) {
            if(tStack.getUnlocalizedName()!="gt.detrav.metatool.01.2") {
                if (DetravMetaGeneratedTool01.getToolMaxDamage(tStack) > 0) {
                    long damage = DetravMetaGeneratedTool01.getToolDamage(tStack);
                    damage /= 10;
                    DetravMetaGeneratedTool01.setToolDamage(tStack, damage);
                    DetravMetaGeneratedTool01.INSTANCE.doDamage(mItem, 1000L);
                }
                //((GT_MetaTileEntity_Hatch_Maintenance) mTileEntity.getMetaTileEntity()).onToolClick(tStack, aPlayer);
                //if (tStack.stackSize <= 0) aPlayer.inventory.setItemStack(null);
            }
        }
        return null;
    }


    /*@Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack result = super.transferStackInSlot(par1EntityPlayer,par2);
        par1EntityPlayer.addChatMessage(new ChatComponentText(result.getDisplayName()));

        return result;
    }*/
}
