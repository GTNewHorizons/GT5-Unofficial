package com.github.bartimaeusnek.bartworks.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_Item_Destructopack extends Container {

    public GT_Container_Item_Destructopack(InventoryPlayer inventory) {

        addSlotToContainer(new delslot());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int aSlotIndex) {
        final Slot slotObject = (Slot) this.inventorySlots.get(aSlotIndex);
        slotObject.putStack(null);
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_)
    {
        final Slot slotObject = (Slot) this.inventorySlots.get(0);
        slotObject.decrStackSize(0);
    }


    class delslot extends Slot{
        public delslot()
        {
            super(new InventoryPlayer(null), 0, 80, 17);
        }

        public void putStack(ItemStack p_75215_1_)
        {
            p_75215_1_=null;
            this.onSlotChanged();
        }


    }
}