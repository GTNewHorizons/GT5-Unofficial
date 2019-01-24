package com.github.technus.tectech.thing.metaTileEntity.single.gui;

import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_BuckConverter;
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

public class GT_Container_BuckConverter
        extends GT_ContainerMetaTile_Machine {
    public int EUT=0,AMP=0;

    public GT_Container_BuckConverter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
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
            GT_MetaTileEntity_BuckConverter buck = (GT_MetaTileEntity_BuckConverter) mTileEntity.getMetaTileEntity();
            switch (aSlotIndex) {
                case 0:
                    buck.EUT -= aShifthold == 1 ? 512 : 64;
                    break;
                case 1:
                    buck.EUT /= aShifthold == 1 ? 512 : 64;
                    break;
                case 2:
                    buck.AMP -= aShifthold == 1 ? 512 : 64;
                    break;
                case 3:
                    buck.AMP /= aShifthold == 1 ? 512 : 64;
                    break;
                case 4:
                    buck.EUT -= aShifthold == 1 ? 16 : 1;
                    break;
                case 5:
                    buck.EUT /= aShifthold == 1 ? 16 : 2;
                    break;
                case 6:
                    buck.AMP -= aShifthold == 1 ? 16 : 1;
                    break;
                case 7:
                    buck.AMP /= aShifthold == 1 ? 16 : 2;
                    break;
                case 8:
                    buck.EUT += aShifthold == 1 ? 512 : 64;
                    break;
                case 9:
                    buck.EUT *= aShifthold == 1 ? 512 : 64;
                    break;
                case 10:
                    buck.AMP += aShifthold == 1 ? 512 : 64;
                    break;
                case 11:
                    buck.AMP *= aShifthold == 1 ? 512 : 64;
                    break;
                case 12:
                    buck.EUT += aShifthold == 1 ? 16 : 1;
                    break;
                case 13:
                    buck.EUT *= aShifthold == 1 ? 16 : 2;
                    break;
                case 14:
                    buck.AMP += aShifthold == 1 ? 16 : 1;
                    break;
                case 15:
                    buck.AMP *= aShifthold == 1 ? 16 : 2;
                    break;
                default: return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
            }
            buck.getBaseMetaTileEntity().setActive((long)AMP*EUT>=0);
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

        GT_MetaTileEntity_BuckConverter buck = (GT_MetaTileEntity_BuckConverter) mTileEntity.getMetaTileEntity();
        EUT=buck.EUT;
        AMP=buck.AMP;
        buck.getBaseMetaTileEntity().setActive((long)AMP*EUT>=0);

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            var1.sendProgressBarUpdate(this, 100, EUT & 0xFFFF);
            var1.sendProgressBarUpdate(this, 101, EUT >>> 16);
            var1.sendProgressBarUpdate(this, 102, AMP & 0xFFFF);
            var1.sendProgressBarUpdate(this, 103, AMP >>> 16);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
                EUT = EUT & 0xFFFF0000 | par2;
                break;
            case 101:
                EUT = EUT & 0xFFFF | par2 << 16;
                break;
            case 102:
                AMP = AMP & 0xFFFF0000 | par2;
                break;
            case 103:
                AMP = AMP & 0xFFFF | par2 << 16;
                break;
        }
    }
}
