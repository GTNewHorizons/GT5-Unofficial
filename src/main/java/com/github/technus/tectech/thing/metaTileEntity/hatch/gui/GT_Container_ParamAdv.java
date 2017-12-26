package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public class GT_Container_ParamAdv extends GT_ContainerMetaTile_Machine {
    public boolean usesFloats = false;
    public int pointer=0;
    public int param = 0;
    public int value1f = 0;
    public int value0f = 0;
    public int input0f = 0;
    public int input1f = 0;

    public GT_Container_ParamAdv(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 59, false, false, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 0) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) inventorySlots.get(aSlotIndex);
        boolean doStuff = true;
        if (tSlot != null && mTileEntity.getMetaTileEntity() != null) {
            GT_MetaTileEntity_Hatch_Param paramH = (GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity();
            int columnPointer=paramH.pointer &0xff;
            boolean secondRow=(paramH.pointer &0x0100)!=0;
            boolean showInts=!paramH.isUsingFloats();
            switch (aSlotIndex) {
                case 0:
                    paramH.param -= aShifthold == 1 ? 16 : 4;
                    break;
                case 1:
                    if (secondRow) secondRow = false;
                    else columnPointer -= aShifthold == 1 ? 16 : 4;
                    break;
                case 2:
                    if (secondRow) columnPointer -= aShifthold == 1 ? 16 : 4;
                    else secondRow = true;
                    break;
                case 3:
                    if (aShifthold == 1) {
                        if (secondRow) paramH.value1i = 0xFFFFFFFF;
                        else paramH.value0i = 0xFFFFFFFF;
                    } else {
                        if (secondRow) paramH.value1i |= 1 << columnPointer;
                        else paramH.value0i |= 1 << columnPointer;
                    }
                    break;
                case 4:
                    paramH.param -= aShifthold == 1 ? 2 : 1;
                    break;
                case 5:
                    if (secondRow) secondRow = false;
                    else columnPointer -= aShifthold == 1 ? 2 : 1;
                    break;
                case 6:
                    if (secondRow) columnPointer -= aShifthold == 1 ? 2 : 1;
                    else secondRow = true;
                    break;
                case 7:
                    if (aShifthold == 1) {
                        if (secondRow) paramH.value1i = 0;
                        else paramH.value0i = 0;
                    } else {
                        if (secondRow) paramH.value1i &= ~(1 << columnPointer);
                        else paramH.value0i &= ~(1 << columnPointer);
                    }
                    break;
                case 8:
                    paramH.param += aShifthold == 1 ? 16 : 4;
                    break;
                case 9:
                    if (secondRow) secondRow = false;
                    else columnPointer += aShifthold == 1 ? 16 : 4;
                    break;
                case 10:
                    if (secondRow) columnPointer += aShifthold == 1 ? 16 : 4;
                    else secondRow = true;
                    break;
                case 11:
                    showInts ^= true;
                    break;
                case 12:
                    paramH.param += aShifthold == 1 ? 2 : 1;
                    break;
                case 13:
                    if (secondRow) secondRow = false;
                    else columnPointer += aShifthold == 1 ? 2 : 1;
                    break;
                case 14:
                    if (secondRow) columnPointer += aShifthold == 1 ? 2 : 1;
                    else secondRow = true;
                    break;
                case 15:
                    if (aShifthold == 1) {
                        if (secondRow) paramH.value1i ^= 0xFFFFFFFF;
                        else paramH.value0i ^= 0xFFFFFFFF;
                    } else {
                        if (secondRow) paramH.value1i ^= 1 << columnPointer;
                        else paramH.value0i ^= 1 << columnPointer;
                    }
                    break;
                default:
                    doStuff = false;
            }
            if (doStuff) {
                if(columnPointer>=32) columnPointer=31;
                else if(columnPointer<0) columnPointer=0;
                paramH.pointer=secondRow?columnPointer+0x100:columnPointer;
                paramH.setUsingFloats(!showInts);
                if (paramH.param > 9) paramH.param = 9;
                else if (paramH.param < -1) paramH.param = -1;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
            return;
        }
        param = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).param;
        value0f = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).value0i;
        value1f = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).value1i;
        input0f = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).input0i;
        input1f = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).input1i;
        pointer = ((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).pointer;
        usesFloats =((GT_MetaTileEntity_Hatch_Param) mTileEntity.getMetaTileEntity()).isUsingFloats();

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            var1.sendProgressBarUpdate(this, 100, param & 0xFFFF);
            var1.sendProgressBarUpdate(this, 101, param >>> 16);
            var1.sendProgressBarUpdate(this, 102, value0f & 0xFFFF);
            var1.sendProgressBarUpdate(this, 103, value0f >>> 16);
            var1.sendProgressBarUpdate(this, 104, value1f & 0xFFFF);
            var1.sendProgressBarUpdate(this, 105, value1f >>> 16);
            var1.sendProgressBarUpdate(this, 106, input0f & 0xFFFF);
            var1.sendProgressBarUpdate(this, 107, input0f >>> 16);
            var1.sendProgressBarUpdate(this, 108, input1f & 0xFFFF);
            var1.sendProgressBarUpdate(this, 109, input1f >>> 16);
            var1.sendProgressBarUpdate(this, 110, usesFloats ? pointer + 0x10000 : pointer);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
                param = param & 0xFFFF0000 | par2;
                return;
            case 101:
                param = param & 0xFFFF | par2 << 16;
                return;
            case 102:
                value0f = value0f & 0xFFFF0000 | par2;
                break;
            case 103:
                value0f = value0f & 0xFFFF | par2 << 16;
                break;
            case 104:
                value1f = value1f & 0xFFFF0000 | par2;
                break;
            case 105:
                value1f = value1f & 0xFFFF | par2 << 16;
                break;
            case 106:
                input0f = input0f & 0xFFFF0000 | par2;
                break;
            case 107:
                input0f = input0f & 0xFFFF | par2 << 16;
                break;
            case 108:
                input1f = input1f & 0xFFFF0000 | par2;
                return;
            case 109:
                input1f = input1f & 0xFFFF | par2 << 16;
                return;
            case 110:
                pointer = par2 & 0xFFFF;
                usesFloats =par2>=0x10000;
            default:
        }
    }
}
