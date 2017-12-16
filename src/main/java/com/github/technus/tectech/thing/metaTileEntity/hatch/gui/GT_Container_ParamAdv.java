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
    public int extra = 0;
    public int data1 = 0;
    public int data0 = 0;
    public int param = 0;
    public float value1f = 0;
    public float value0f = 0;
    public float input0f = 0;
    public float input1f = 0;
    private int input0Bits = 0;
    private int input1Bits = 0;

    public GT_Container_ParamAdv(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 8, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 8, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 8, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 8, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 26, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 26, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 26, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 26, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 152, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 152, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 152, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 152, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 134, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 134, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 134, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 2, 134, 59, false, false, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 0) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
        boolean doStuff = true;
        if ((tSlot != null) && (this.mTileEntity.getMetaTileEntity() != null)) {
            GT_MetaTileEntity_Hatch_Param paramH = (GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity();
            int columnPointer=paramH.extra&0xff;
            boolean secondRow=(paramH.extra&0x0100)!=0;
            boolean showInts=(paramH.extra&0x10000)!=0;
            switch (aSlotIndex) {
                case 0:
                    paramH.param -= (aShifthold == 1 ? 16 : 4);
                    break;
                case 1:
                    if (secondRow) {secondRow=false;} else {columnPointer -= (aShifthold == 1 ? 16 : 4);}
                    break;
                case 2:
                    if (secondRow) {columnPointer -= (aShifthold == 1 ? 16 : 4);} else {secondRow=true;}
                    break;
                case 3:
                    if(aShifthold==1){
                        if(secondRow) data1=0xFFFFFFFF;
                        else  data0=0xFFFFFFFF;
                    }else {
                        if(secondRow){
                            data1|=1<<columnPointer;
                        }else {
                            data0|=1<<columnPointer;
                        }
                    }
                    break;
                case 4:
                    paramH.param -= (aShifthold == 1 ? 2 : 1);
                    break;
                case 5:
                    if (secondRow) {secondRow=false;} else {columnPointer -= (aShifthold == 1 ? 2 : 1);}
                    break;
                case 6:
                    if (secondRow) {columnPointer -= (aShifthold == 1 ? 2 : 1);} else {secondRow=true;}
                    break;
                case 7:
                    if(aShifthold==1){
                        if(secondRow) data1=0;
                        else  data0=0;
                    }else {
                        if(secondRow){
                            data1&=~(1<<columnPointer);
                        }else {
                            data0&=~(1<<columnPointer);
                        }
                    }
                    break;
                case 8:
                    paramH.param += (aShifthold == 1 ? 16 : 4);
                    break;
                case 9:
                    if (secondRow) {secondRow=false;} else {columnPointer += (aShifthold == 1 ? 16 : 4);}
                    break;
                case 10:
                    if (secondRow) {columnPointer += (aShifthold == 1 ? 16 : 4);} else {secondRow=true;}
                    break;
                case 11:
                    if(aShifthold==1){
                        if(secondRow) data1^=0xFFFFFFFF;
                        else  data0^=0xFFFFFFFF;
                    }else {
                        if(secondRow){
                            data1^=1<<columnPointer;
                        }else {
                            data0^=1<<columnPointer;
                        }
                    }
                    break;
                case 12:
                    paramH.param += (aShifthold == 1 ? 2 : 1);
                    break;
                case 13:
                    if (secondRow) {secondRow=false;} else {columnPointer += (aShifthold == 1 ? 2 : 1);}
                    break;
                case 14:
                    if (secondRow) {columnPointer += (aShifthold == 1 ? 2 : 1);} else {secondRow=true;}
                    break;
                case 15:
                    showInts^=true;
                    break;
                default:
                    doStuff = false;
            }
            if (doStuff) {
                if(columnPointer>=32) columnPointer=31;
                else if(columnPointer<0) columnPointer=0;
                paramH.extra=columnPointer;
                if(secondRow) paramH.extra|=0x0100;
                if(showInts) paramH.extra|=0x10000;
                if (paramH.param > 9) paramH.param = 9;
                else if (paramH.param < -1) paramH.param = -1;
                paramH.value0f =Float.intBitsToFloat(Float.floatToIntBits(paramH.data0));
                paramH.value1f =Float.intBitsToFloat(Float.floatToIntBits(paramH.data1));
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if ((this.mTileEntity.isClientSide()) || (this.mTileEntity.getMetaTileEntity() == null)) {
            return;
        }
        this.param = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).param;
        this.data0 = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).data0;
        this.data1 = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).data1;
        this.extra = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).extra;
        this.input0Bits = Float.floatToIntBits(((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).input1f);
        this.input1Bits = Float.floatToIntBits(((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).input2f);

        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting) var2.next();
            var1.sendProgressBarUpdate(this, 100, this.param & 0xFFFF);
            var1.sendProgressBarUpdate(this, 101, this.param >>> 16);
            var1.sendProgressBarUpdate(this, 102, this.data0 & 0xFFFF);
            var1.sendProgressBarUpdate(this, 103, this.data0 >>> 16);
            var1.sendProgressBarUpdate(this, 104, this.data1 & 0xFFFF);
            var1.sendProgressBarUpdate(this, 105, this.data1 >>> 16);
            var1.sendProgressBarUpdate(this, 106, this.extra & 0xFFFF);
            var1.sendProgressBarUpdate(this, 107, this.extra >>> 16);
            var1.sendProgressBarUpdate(this, 108, this.input0Bits & 0xFFFF);
            var1.sendProgressBarUpdate(this, 109, this.input0Bits >>> 16);
            var1.sendProgressBarUpdate(this, 110, this.input1Bits & 0xFFFF);
            var1.sendProgressBarUpdate(this, 111, this.input1Bits >>> 16);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
                this.param = (this.param & 0xFFFF0000 | par2);
                return;
            case 101:
                this.param = (this.param & 0xFFFF | (par2 << 16));
                return;
            case 102:
                this.data0 = (this.data0 & 0xFFFF0000 | par2);
                break;
            case 103:
                this.data0 = (this.data0 & 0xFFFF | (par2 << 16));
                break;
            case 104:
                this.data1 = (this.data1 & 0xFFFF0000 | par2);
                break;
            case 105:
                this.data1 = (this.data1 & 0xFFFF | (par2 << 16));
                break;
            case 106:
                this.extra = (this.extra & 0xFFFF0000 | par2);
                break;
            case 107:
                this.extra = (this.extra & 0xFFFF | (par2 << 16));
                break;
            case 108:
                this.input0Bits = (this.input0Bits & 0xFFFF0000 | par2);
                this.input0f = Float.intBitsToFloat(input0Bits);
                return;
            case 109:
                this.input0Bits = (this.input0Bits & 0xFFFF | (par2 << 16));
                this.input0f = Float.intBitsToFloat(input0Bits);
                return;
            case 110:
                this.input1Bits = (this.input1Bits & 0xFFFF0000 | par2);
                this.input1f = Float.intBitsToFloat(input1Bits);
                return;
            case 111:
                this.input1Bits = (this.input1Bits & 0xFFFF | (par2 << 16));
                this.input1f = Float.intBitsToFloat(input1Bits);
                return;
            default:
                return;
        }
        value0f =Float.intBitsToFloat(data0);
        value1f =Float.intBitsToFloat(data1);
    }
}
