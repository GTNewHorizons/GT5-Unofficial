package com.github.technus.tectech.things.metaTileEntity.hatch.gui;

import com.github.technus.tectech.things.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
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

public final class GT_Container_Param extends GT_ContainerMetaTile_Machine {
    public int exponent = 0;
    public int value2 = 0;
    public int value1 = 0;
    public int param = 0;
    public float value2f = 0;
    public float value1f = 0;
    public float input1f = 0;
    private int input1Bits = 0;
    public float input2f = 0;
    private int input2Bits = 0;

    public GT_Container_Param(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
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
            switch (aSlotIndex) {
                case 0:
                    paramH.param -= (aShifthold == 1 ? 512 : 64);
                    break;
                case 1:
                    paramH.value1 -= (aShifthold == 1 ? 512 : 64);
                    break;
                case 2:
                    paramH.value2 -= (aShifthold == 1 ? 512 : 64);
                    break;
                case 3:
                    paramH.exponent -= (aShifthold == 1 ? 16 : 8);
                    break;
                case 4:
                    paramH.param -= (aShifthold == 1 ? 16 : 1);
                    break;
                case 5:
                    paramH.value1 -= (aShifthold == 1 ? 16 : 1);
                    break;
                case 6:
                    paramH.value2 -= (aShifthold == 1 ? 16 : 1);
                    break;
                case 7:
                    paramH.exponent -= (aShifthold == 1 ? 4 : 1);
                    break;
                case 8:
                    paramH.param += (aShifthold == 1 ? 512 : 64);
                    break;
                case 9:
                    paramH.value1 += (aShifthold == 1 ? 512 : 64);
                    break;
                case 10:
                    paramH.value2 += (aShifthold == 1 ? 512 : 64);
                    break;
                case 11:
                    paramH.exponent += (aShifthold == 1 ? 16 : 8);
                    break;
                case 12:
                    paramH.param += (aShifthold == 1 ? 16 : 1);
                    break;
                case 13:
                    paramH.value1 += (aShifthold == 1 ? 16 : 1);
                    break;
                case 14:
                    paramH.value2 += (aShifthold == 1 ? 16 : 1);
                    break;
                case 15:
                    paramH.exponent += (aShifthold == 1 ? 4 : 1);
                    break;
                default:
                    doStuff = false;
            }
            if (doStuff) {
                if (paramH.param > 9) paramH.param = 9;
                if (paramH.param < -1) paramH.param = -1;
                paramH.value1f = (float) (paramH.value1 * Math.pow(2, paramH.exponent));
                paramH.value2f = (float) (paramH.value2 * Math.pow(2, paramH.exponent));
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
        this.value1 = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).value1;
        this.value2 = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).value2;
        this.exponent = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).exponent;
        this.input1Bits = Float.floatToIntBits(((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).input1f);
        this.input2Bits = Float.floatToIntBits(((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).input2f);

        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting) var2.next();
            var1.sendProgressBarUpdate(this, 100, this.param & 0xFFFF);
            var1.sendProgressBarUpdate(this, 101, this.param >>> 16);
            var1.sendProgressBarUpdate(this, 102, this.value1 & 0xFFFF);
            var1.sendProgressBarUpdate(this, 103, this.value1 >>> 16);
            var1.sendProgressBarUpdate(this, 104, this.value2 & 0xFFFF);
            var1.sendProgressBarUpdate(this, 105, this.value2 >>> 16);
            var1.sendProgressBarUpdate(this, 106, this.exponent & 0xFFFF);
            var1.sendProgressBarUpdate(this, 107, this.exponent >>> 16);
            var1.sendProgressBarUpdate(this, 108, this.input1Bits & 0xFFFF);
            var1.sendProgressBarUpdate(this, 109, this.input1Bits >>> 16);
            var1.sendProgressBarUpdate(this, 110, this.input2Bits & 0xFFFF);
            var1.sendProgressBarUpdate(this, 111, this.input2Bits >>> 16);
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
                this.param = (this.param & 0xFFFF | par2 << 16);
                return;
            case 102:
                this.value1 = (this.value1 & 0xFFFF0000 | par2);
                break;
            case 103:
                this.value1 = (this.value1 & 0xFFFF | par2 << 16);
                break;
            case 104:
                this.value2 = (this.value2 & 0xFFFF0000 | par2);
                break;
            case 105:
                this.value2 = (this.value2 & 0xFFFF | par2 << 16);
                break;
            case 106:
                this.exponent = (this.exponent & 0xFFFF0000 | par2);
                break;
            case 107:
                this.exponent = (this.exponent & 0xFFFF | par2 << 16);
                break;
            case 108:
                this.input1Bits = (this.input1Bits & 0xFFFF0000 | par2);
                this.input1f = Float.intBitsToFloat(input1Bits);
                return;
            case 109:
                this.input1Bits = (this.input1Bits & 0xFFFF | par2 << 16);
                this.input1f = Float.intBitsToFloat(input1Bits);
                return;
            case 110:
                this.input2Bits = (this.input2Bits & 0xFFFF0000 | par2);
                this.input2f = Float.intBitsToFloat(input2Bits);
                return;
            case 111:
                this.input2Bits = (this.input2Bits & 0xFFFF | par2 << 16);
                this.input2f = Float.intBitsToFloat(input2Bits);
                return;
            default:
                return;
        }
        this.value1f = (float) (this.value1 * Math.pow(2, this.exponent));
        this.value2f = (float) (this.value2 * Math.pow(2, this.exponent));
    }
}
