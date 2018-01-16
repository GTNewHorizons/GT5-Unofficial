package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Uncertainty;
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

public class GT_Container_Uncertainty extends GT_ContainerMetaTile_Machine {
    public short[] matrix = new short[]{500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};
    public byte selection = -1, mode = 0, status = -128;

    public GT_Container_Uncertainty(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
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

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 59, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 59, false, false, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 0) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) inventorySlots.get(aSlotIndex);
        if (tSlot != null && mTileEntity.getMetaTileEntity() != null) {
            GT_MetaTileEntity_Hatch_Uncertainty catH = (GT_MetaTileEntity_Hatch_Uncertainty) mTileEntity.getMetaTileEntity();
            if (aSlotIndex < 16 && catH.matrix != null) {
                IGregTechTileEntity base=catH.getBaseMetaTileEntity();
                TecTech.proxy.playSound(base,"fx_click");
                if (catH.selection == -1) {
                    catH.selection = (byte) aSlotIndex;
                } else {
                    short temp = catH.matrix[catH.selection];
                    catH.matrix[catH.selection] = catH.matrix[aSlotIndex];
                    catH.matrix[aSlotIndex] = temp;
                    catH.selection = -1;
                }

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

        ((GT_MetaTileEntity_Hatch_Uncertainty) mTileEntity.getMetaTileEntity()).compute();

        if (matrix != null) {
            for (int i = 0; i < 16; i++) {
                matrix[i] = ((GT_MetaTileEntity_Hatch_Uncertainty) mTileEntity.getMetaTileEntity()).matrix[i];
            }
        }

        selection = ((GT_MetaTileEntity_Hatch_Uncertainty) mTileEntity.getMetaTileEntity()).selection;
        mode = ((GT_MetaTileEntity_Hatch_Uncertainty) mTileEntity.getMetaTileEntity()).mode;
        status = ((GT_MetaTileEntity_Hatch_Uncertainty) mTileEntity.getMetaTileEntity()).status;

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            var1.sendProgressBarUpdate(this, 100, selection);
            var1.sendProgressBarUpdate(this, 101, mode);
            var1.sendProgressBarUpdate(this, 102, status);

            if (matrix != null) {
                for (int i = 0; i < 16; i++) {
                    var1.sendProgressBarUpdate(this, 103 + i, matrix[i]);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
                selection = (byte) par2;
                break;
            case 101:
                mode = (byte) par2;
                break;
            case 102:
                status = (byte) par2;
                break;
            default:
                if (matrix != null && par1 >= 103 && par1 < 119) {
                    matrix[par1 - 103] = (short) par2;
                    break;
                }
        }
    }
}
