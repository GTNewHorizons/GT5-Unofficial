package com.github.technus.tectech.thing.metaTileEntity.single.gui;

import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_DebugPollutor;
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

public class GT_Container_DebugPollutor
        extends GT_ContainerMetaTile_Machine {
    public int pollution;
    public float anomaly;
    private int anomalyInt;

    public GT_Container_DebugPollutor(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
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
        if (tSlot != null && mTileEntity.getMetaTileEntity() != null) {
            GT_MetaTileEntity_DebugPollutor dpg = (GT_MetaTileEntity_DebugPollutor) mTileEntity.getMetaTileEntity();
            switch (aSlotIndex) {
                case 0:
                    dpg.pollution -= aShifthold == 1 ? 512 : 64;
                    break;
                case 1:
                    dpg.pollution /= aShifthold == 1 ? 512 : 64;
                    break;
                case 2:
                    dpg.anomaly -= aShifthold == 1 ? 512 : 64;
                    break;
                case 3:
                    dpg.anomaly /= aShifthold == 1 ? 512 : 64;
                    break;
                case 4:
                    dpg.pollution -= aShifthold == 1 ? 16 : 1;
                    break;
                case 5:
                    dpg.pollution /= aShifthold == 1 ? 16 : 2;
                    break;
                case 6:
                    dpg.anomaly -= aShifthold == 1 ? 16 : 1;
                    break;
                case 7:
                    dpg.anomaly /= aShifthold == 1 ? 16 : 2;
                    break;
                case 8:
                    dpg.pollution += aShifthold == 1 ? 512 : 64;
                    break;
                case 9:
                    dpg.pollution *= aShifthold == 1 ? 512 : 64;
                    break;
                case 10:
                    dpg.anomaly += aShifthold == 1 ? 512 : 64;
                    break;
                case 11:
                    dpg.anomaly *= aShifthold == 1 ? 512 : 64;
                    break;
                case 12:
                    dpg.pollution += aShifthold == 1 ? 16 : 1;
                    break;
                case 13:
                    dpg.pollution *= aShifthold == 1 ? 16 : 2;
                    break;
                case 14:
                    dpg.anomaly += aShifthold == 1 ? 16 : 1;
                    break;
                case 15:
                    dpg.anomaly *= aShifthold == 1 ? 16 : 2;
                    break;
                default: return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
            }
            if(dpg.anomaly==Float.POSITIVE_INFINITY){
                dpg.anomaly=Float.MAX_VALUE;
            }else if (dpg.anomaly==Float.NEGATIVE_INFINITY){
                dpg.anomaly=-Float.MAX_VALUE;
            }
            return null;
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) {
            return;
        }

        GT_MetaTileEntity_DebugPollutor dpg = (GT_MetaTileEntity_DebugPollutor) mTileEntity.getMetaTileEntity();
        pollution =dpg.pollution;
        anomaly =dpg.anomaly;
        anomalyInt=Float.floatToIntBits(anomaly);

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            Util.sendInteger(pollution,this,var1,100);
            Util.sendFloat(anomaly,this,var1,102);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
            case 101:
                pollution = Util.receiveInteger(pollution,100,par1,par2);
                break;
            case 102:
            case 103:
                anomaly = Float.intBitsToFloat(anomalyInt=Util.receiveInteger(anomalyInt,102,par1,par2));
                break;
        }
    }
}
