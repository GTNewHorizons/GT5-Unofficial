package com.detrav.gui.containers;

import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.utils.PortableAnvilInventory;
import gregtech.api.gui.GT_Slot_Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import gregtech.common.items.armor.gui.SlotLocked;

/**
 * Created by Detrav on 30.10.2016.
 */
public class DetravPortableAnvilContainer extends Container {

    public InventoryPlayer inventoryPlayer;
    public IInventory slots;
    ItemStack stack;
    World worldObj;

    public DetravPortableAnvilContainer(InventoryPlayer inventory, World world, ItemStack currentEquippedItem) {
        worldObj = world;
        inventoryPlayer = inventory;
        stack = currentEquippedItem;
        slots  = new PortableAnvilInventory("Item", false, 3);

        this.addSlotToContainer(new Slot(this.slots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.slots, 1, 76, 47));
        this.addSlotToContainer(new GT_Slot_Output(this.slots, 2, 134, 47));

        slots.setInventorySlotContents(1, DetravMetaGeneratedTool01.INSTANCE.getItemStackFromDetravData(stack));


        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            ItemStack stackInSlot = inventoryPlayer.getStackInSlot(i);
            if (currentEquippedItem != null && stackInSlot != null && currentEquippedItem == stackInSlot) {
                addSlotToContainer(new SlotLocked(inventoryPlayer, i, 8 + i * 18, 142));
            } else {
                this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return null;
    }

    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        if (!this.worldObj.isRemote) {
            ItemStack itemstack = this.slots.getStackInSlot(0);

            if (itemstack != null) {
                p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
            }
            itemstack = this.slots.getStackInSlot(1);
            if (itemstack != null) {
                if (stack == null || !DetravMetaGeneratedTool01.INSTANCE.setItemStackToDetravData(stack, itemstack)) {
                    p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
            //itemstack.writeToNBT()
        }
    }



    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}