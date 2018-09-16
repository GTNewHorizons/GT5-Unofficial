package com.github.bartimaeusnek.bartworks.server.container;

import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_LESU extends GT_Container_MultiMachine {

    public GT_Container_LESU(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public GT_Container_LESU(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, boolean bindInventory) {
        super(aInventoryPlayer, aTileEntity, bindInventory);
    }

    public void addSlots(InventoryPlayer aInventoryPlayer) {
        this.addSlotToContainer(new Slot(this.mTileEntity, 1, 128, 14));
        this.addSlotToContainer(new Slot(this.mTileEntity, 0, 128, 50));
        this.addSlotToContainer(new Slot(this.mTileEntity, 2, 152, 5));
        this.addSlotToContainer(new Slot(this.mTileEntity, 3, 152, 23));
        this.addSlotToContainer(new Slot(this.mTileEntity, 4, 152, 41));
        this.addSlotToContainer(new Slot(this.mTileEntity, 5, 152, 59));
    }

    public int getSlotCount() {
        return 6;
    }

    public int getShiftClickSlotCount() {
        return 6;
    }

}
