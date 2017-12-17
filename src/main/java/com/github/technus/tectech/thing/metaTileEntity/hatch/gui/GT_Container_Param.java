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

public class GT_Container_Param extends GT_ContainerMetaTile_Machine {
    public int param = 0;
    public int value0f = 0;
    public int value1f = 0;
    public int input0f = 0;
    public int input1f = 0;

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
                    paramH.param -= (aShifthold == 1 ? 16 : 4);
                    break;
                case 1:
                    paramH.value0i -= (aShifthold == 1 ? 4096 : 256);
                    break;
                case 2:
                    paramH.value1i -= (aShifthold == 1 ? 4096 : 256);
                    break;
                case 3:
                    paramH.value0i >>= aShifthold == 1 ? 16 : 4;
                    paramH.value1i >>= aShifthold == 1 ? 16 : 4;
                    break;
                case 4:
                    paramH.param -= (aShifthold == 1 ? 2 : 1);
                    break;
                case 5:
                    paramH.value0i -= (aShifthold == 1 ? 16 : 1);
                    break;
                case 6:
                    paramH.value1i -= (aShifthold == 1 ? 16 : 1);
                    break;
                case 7:
                    paramH.value0i >>= aShifthold == 1 ? 2 : 1;
                    paramH.value1i >>= aShifthold == 1 ? 2 : 1;
                    break;
                case 8:
                    paramH.param += (aShifthold == 1 ? 16 : 4);
                    break;
                case 9:
                    paramH.value0i += (aShifthold == 1 ? 4096 : 256);
                    break;
                case 10:
                    paramH.value1i += (aShifthold == 1 ? 4096 : 256);
                    break;
                case 11:
                    paramH.value0i <<= aShifthold == 1 ? 16 : 4;
                    paramH.value1i <<= aShifthold == 1 ? 16 : 4;
                    break;
                case 12:
                    paramH.param += (aShifthold == 1 ? 2 : 1);
                    break;
                case 13:
                    paramH.value0i += (aShifthold == 1 ? 16 : 1);
                    break;
                case 14:
                    paramH.value1i += (aShifthold == 1 ? 16 : 1);
                    break;
                case 15:
                    paramH.value0i <<= aShifthold == 1 ? 2 : 1;
                    paramH.value1i <<= aShifthold == 1 ? 2 : 1;
                    break;
                default:
                    doStuff = false;
            }
            if (doStuff) {
                if (paramH.param > 9) paramH.param = 9;
                else if (paramH.param < -1) paramH.param = -1;
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
        this.value0f = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).value0i;
        this.value1f = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).value1i;
        this.input0f = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).input0i;
        this.input1f = ((GT_MetaTileEntity_Hatch_Param) this.mTileEntity.getMetaTileEntity()).input1i;

        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting) var2.next();
            var1.sendProgressBarUpdate(this, 100, this.param & 0xFFFF);
            var1.sendProgressBarUpdate(this, 101, this.param >>> 16);
            var1.sendProgressBarUpdate(this, 102, this.value0f & 0xFFFF);
            var1.sendProgressBarUpdate(this, 103, this.value0f >>> 16);
            var1.sendProgressBarUpdate(this, 104, this.value1f & 0xFFFF);
            var1.sendProgressBarUpdate(this, 105, this.value1f >>> 16);
            var1.sendProgressBarUpdate(this, 106, this.input0f & 0xFFFF);
            var1.sendProgressBarUpdate(this, 107, this.input0f >>> 16);
            var1.sendProgressBarUpdate(this, 108, this.input1f & 0xFFFF);
            var1.sendProgressBarUpdate(this, 109, this.input1f >>> 16);
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
                this.value0f = (this.value0f & 0xFFFF0000 | par2);
                break;
            case 103:
                this.value0f = (this.value0f & 0xFFFF | (par2 << 16));
                break;
            case 104:
                this.value1f = (this.value1f & 0xFFFF0000 | par2);
                break;
            case 105:
                this.value1f = (this.value1f & 0xFFFF | (par2 << 16));
                break;
            case 106:
                this.input0f = (this.input0f & 0xFFFF0000 | par2);
                break;
            case 107:
                this.input0f = (this.input0f & 0xFFFF | (par2 << 16));
                break;
            case 108:
                this.input1f = (this.input1f & 0xFFFF0000 | par2);
                return;
            case 109:
                this.input1f = (this.input1f & 0xFFFF | (par2 << 16));
                return;
            default:
        }
    }
}
