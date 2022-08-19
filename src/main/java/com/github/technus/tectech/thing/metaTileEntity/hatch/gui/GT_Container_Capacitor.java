package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_Capacitor extends GT_ContainerMetaTile_Machine {
    public GT_Container_Capacitor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        this.addSlotToContainer(new Slot(this.mTileEntity, 0, 53, 8));
        this.addSlotToContainer(new Slot(this.mTileEntity, 1, 71, 8));
        this.addSlotToContainer(new Slot(this.mTileEntity, 2, 89, 8));
        this.addSlotToContainer(new Slot(this.mTileEntity, 3, 107, 8));
        this.addSlotToContainer(new Slot(this.mTileEntity, 4, 53, 26));
        this.addSlotToContainer(new Slot(this.mTileEntity, 5, 71, 26));
        this.addSlotToContainer(new Slot(this.mTileEntity, 6, 89, 26));
        this.addSlotToContainer(new Slot(this.mTileEntity, 7, 107, 26));
        this.addSlotToContainer(new Slot(this.mTileEntity, 8, 53, 44));
        this.addSlotToContainer(new Slot(this.mTileEntity, 9, 71, 44));
        this.addSlotToContainer(new Slot(this.mTileEntity, 10, 89, 44));
        this.addSlotToContainer(new Slot(this.mTileEntity, 11, 107, 44));
        this.addSlotToContainer(new Slot(this.mTileEntity, 12, 53, 62));
        this.addSlotToContainer(new Slot(this.mTileEntity, 13, 71, 62));
        this.addSlotToContainer(new Slot(this.mTileEntity, 14, 89, 62));
        this.addSlotToContainer(new Slot(this.mTileEntity, 15, 107, 62));
    }

    @Override
    public int getSlotCount() { return 16; }

    @Override
    public int getShiftClickSlotCount() { return getSlotCount(); }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
            return;
        }
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (mActive != 0) {
            return null;
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
        if (mActive != 0) {
            return null;
        }
        return super.transferStackInSlot(aPlayer, aSlotIndex);
    }

    @Override
    public boolean canDragIntoSlot(Slot par1Slot) {
        if (mActive != 0) {
            return false;
        }
        return super.canDragIntoSlot(par1Slot);
    }

    @Override
    public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack) {
        if (mActive != 0) {
            return;
        }
        super.putStacksInSlots(par1ArrayOfItemStack);
    }

    @Override
    protected boolean mergeItemStack(ItemStack aStack, int aStartIndex, int aSlotCount, boolean par4) {
        if (mActive != 0) {
            return false;
        }
        return super.mergeItemStack(aStack, aStartIndex, aSlotCount, par4);
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack) {
        if (mActive != 0) {
            return;
        }
        super.putStackInSlot(par1, par2ItemStack);
    }
}
