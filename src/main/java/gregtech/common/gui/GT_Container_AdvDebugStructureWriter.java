package gregtech.common.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.debug.GT_MetaTileEntity_AdvDebugStructureWriter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_AdvDebugStructureWriter extends GT_ContainerMetaTile_Machine {
    public short[] numbers = new short[6];
    public boolean transpose;
    public boolean showHighlightBox;

    public GT_Container_AdvDebugStructureWriter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 63, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 81, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 8, 99, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 63, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 81, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 26, 99, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 63, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 81, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 152, 99, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 5, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 23, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 41, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 63, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 81, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 134, 99, false, false, 1));

        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 12, 129, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 33, 129, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 54, 129, false, false, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 2, 75, 129, false, false, 1));
    }

    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 0) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) inventorySlots.get(aSlotIndex);
        if (tSlot != null && mTileEntity.getMetaTileEntity() != null) {
            GT_MetaTileEntity_AdvDebugStructureWriter dsw =
                (GT_MetaTileEntity_AdvDebugStructureWriter) mTileEntity.getMetaTileEntity();
            if (dsw.numbers == null) {
                return null;
            }
            switch (aSlotIndex) {
                case 0:
                    dsw.numbers[0] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 1:
                    dsw.numbers[1] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 2:
                    dsw.numbers[2] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 3:
                    dsw.numbers[3] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 4:
                    dsw.numbers[4] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 5:
                    dsw.numbers[5] -= aShifthold == 1 ? 512 : 64;
                    return null;
                case 6:
                    dsw.numbers[0] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 7:
                    dsw.numbers[1] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 8:
                    dsw.numbers[2] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 9:
                    dsw.numbers[3] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 10:
                    dsw.numbers[4] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 11:
                    dsw.numbers[5] -= aShifthold == 1 ? 16 : 1;
                    return null;
                case 12:
                    dsw.numbers[0] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 13:
                    dsw.numbers[1] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 14:
                    dsw.numbers[2] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 15:
                    dsw.numbers[3] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 16:
                    dsw.numbers[4] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 17:
                    dsw.numbers[5] += aShifthold == 1 ? 512 : 64;
                    return null;
                case 18:
                    dsw.numbers[0] += aShifthold == 1 ? 16 : 1;
                    return null;
                case 19:
                    dsw.numbers[1] += aShifthold == 1 ? 16 : 1;
                    return null;
                case 20:
                    dsw.numbers[2] += aShifthold == 1 ? 16 : 1;
                    return null;
                case 21:
                    dsw.numbers[3] += aShifthold == 1 ? 16 : 1;
                    return null;
                case 22:
                    dsw.numbers[4] += aShifthold == 1 ? 16 : 1;
                    return null;
                case 23:
                    dsw.numbers[5] += aShifthold == 1 ? 16 : 1;
                    return null;

                case 24:
                    if (dsw.getBaseMetaTileEntity().isServerSide()) {
                        dsw.printStructure(aPlayer);
                    }
                    return null;
                case 25:
                    dsw.transpose = !dsw.transpose;
                    return null;
                case 26:
                    dsw.showHighlightBox = !dsw.showHighlightBox;
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
        GT_MetaTileEntity_AdvDebugStructureWriter dsw =
            (GT_MetaTileEntity_AdvDebugStructureWriter) mTileEntity.getMetaTileEntity();
        if (numbers != null) {
            System.arraycopy(dsw.numbers, 0, numbers, 0, dsw.numbers.length);
        }

        transpose = dsw.transpose;
        showHighlightBox = dsw.showHighlightBox;

        for (Object crafter : crafters) {
            ICrafting var1 = (ICrafting) crafter;
            if (numbers != null) {
                var1.sendProgressBarUpdate(this, 100, numbers[0]);
                var1.sendProgressBarUpdate(this, 101, numbers[1]);
                var1.sendProgressBarUpdate(this, 102, numbers[2]);
                var1.sendProgressBarUpdate(this, 103, numbers[3]);
                var1.sendProgressBarUpdate(this, 104, numbers[4]);
                var1.sendProgressBarUpdate(this, 105, numbers[5]);
                var1.sendProgressBarUpdate(this, 106, transpose ? 1 : 0);
                var1.sendProgressBarUpdate(this, 107, showHighlightBox ? 1 : 0);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        if (par1 == 106) {
            transpose = par2 > 0;
        }
        else if (par1 == 107) {
            showHighlightBox = par2 > 0;
        } else {
            if (numbers != null && par1 >= 100 && par1 <= 105) {
                numbers[par1 - 100] = (short) par2;
            }
        }
    }
}
