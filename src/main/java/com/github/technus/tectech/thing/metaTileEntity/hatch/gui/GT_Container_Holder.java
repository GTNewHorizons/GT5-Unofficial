package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

/**
 * Created by Tec on 09.04.2017.
 */
public class GT_Container_Holder extends GT_ContainerMetaTile_Machine {
    public GT_Container_Holder(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public void addSlots(InventoryPlayer aInventoryPlayer) {
        this.addSlotToContainer(new Slot(this.mTileEntity, 0, 80, 39));
    }

    public int getSlotCount() {
        return 1;
    }

    public int getShiftClickSlotCount() {
        return 1;
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if(mActive!=0) return null;
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
        if(mActive!=0) return null;
        return super.transferStackInSlot(aPlayer, aSlotIndex);
    }

    @Override
    public boolean canDragIntoSlot(Slot par1Slot) {
        if(mActive!=0) return false;
        return super.canDragIntoSlot(par1Slot);
    }

    @Override
    public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack) {
        if(mActive!=0) return;
        super.putStacksInSlots(par1ArrayOfItemStack);
    }

    @Override
    protected boolean mergeItemStack(ItemStack aStack, int aStartIndex, int aSlotCount, boolean par4) {
        if(mActive!=0) return false;
        return super.mergeItemStack(aStack, aStartIndex, aSlotCount, par4);
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack) {
        if(mActive!=0) return;
        super.putStackInSlot(par1, par2ItemStack);
    }
}
