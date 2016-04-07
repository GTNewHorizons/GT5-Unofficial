package com.detrav.gui.containers;

import gregtech.common.items.armor.SlotLocked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by wital_000 on 07.04.2016.
 */
public class DetravPortableChargerContainer extends Container {
    public IInventory slots = new InventoryCrafting(this, 1, 1);
    public ItemStack mItem;
    private World worldObj;

    public DetravPortableChargerContainer(InventoryPlayer inventoryPlayer, World aWorld,ItemStack aStack) {
        this.worldObj = aWorld;
        this.addSlotToContainer(new Slot(slots, 0, 80, 35));
        mItem = aStack;
        //new Slot()
        bindPlayerInventory(inventoryPlayer);
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
            if (mItem!=null && stackInSlot!=null && mItem.getItem() == stackInSlot.getItem()) {
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
}