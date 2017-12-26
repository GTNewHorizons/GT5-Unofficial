package com.github.technus.tectech.thing.metaTileEntity.single.gui;

import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_DebugStructureWriter;
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

public class GT_Container_DebugStructureWriter
        extends GT_ContainerMetaTile_Machine {
    public boolean size = false;
    public short[] numbers = new short[6];

    public GT_Container_DebugStructureWriter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
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
            GT_MetaTileEntity_DebugStructureWriter dsw = (GT_MetaTileEntity_DebugStructureWriter) mTileEntity.getMetaTileEntity();
            if (dsw.numbers == null) {
                return null;
            }
            switch (aSlotIndex) {
                case 0:
                    dsw.numbers[size ? 3 : 0] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 1:
                    dsw.numbers[size ? 4 : 1] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 2:
                    dsw.numbers[size ? 5 : 2] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 4:
                    dsw.numbers[size ? 3 : 0] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 5:
                    dsw.numbers[size ? 4 : 1] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 6:
                    dsw.numbers[size ? 5 : 2] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 8:
                    dsw.numbers[size ? 3 : 0] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 9:
                    dsw.numbers[size ? 4 : 1] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 10:
                    dsw.numbers[size ? 5 : 2] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 12:
                    dsw.numbers[size ? 3 : 0] += aShifthold == 1 ? 16 : 1;
                    return null;
                case 13:
                    dsw.numbers[size ? 4 : 1] += aShifthold == 1 ? 16 : 1;
                    return null;
                case 14:
                    dsw.numbers[size ? 5 : 2] += aShifthold == 1 ? 16 : 1;
                    return null;
                case 3:
                case 7:
                case 11:
                case 15:
                    dsw.size ^= true;
                    return null;
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
        GT_MetaTileEntity_DebugStructureWriter dsw = (GT_MetaTileEntity_DebugStructureWriter) mTileEntity.getMetaTileEntity();
        if (numbers != null) {
            System.arraycopy(dsw.numbers, 0, numbers, 0, dsw.numbers.length);
        }
        size = dsw.size;

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            if (numbers != null) {
                var1.sendProgressBarUpdate(this, 100, numbers[0]);
                var1.sendProgressBarUpdate(this, 101, numbers[1]);
                var1.sendProgressBarUpdate(this, 102, numbers[2]);
                var1.sendProgressBarUpdate(this, 103, numbers[3]);
                var1.sendProgressBarUpdate(this, 104, numbers[4]);
                var1.sendProgressBarUpdate(this, 105, numbers[5]);
            }
            var1.sendProgressBarUpdate(this, 106, size ? 1 : 0);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 106:
                size = par2 == 1;
                break;
            default:
                if (numbers != null && par1 >= 100 && par1 <= 105) {
                    numbers[par1 - 100] = (short) par2;
                }
                break;
        }
    }
}
