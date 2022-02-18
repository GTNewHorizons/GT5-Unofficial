package gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_SpecialFilter;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_SpecialFilter extends GT_ContainerMetaTile_Machine {

    private GT_Slot_Render mSpecialSlot;

    public GT_Container_SpecialFilter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(this.mTileEntity, 0, 98, 5));
        addSlotToContainer(new Slot(this.mTileEntity, 1, 116, 5));
        addSlotToContainer(new Slot(this.mTileEntity, 2, 134, 5));
        addSlotToContainer(new Slot(this.mTileEntity, 3, 98, 23));
        addSlotToContainer(new Slot(this.mTileEntity, 4, 116, 23));
        addSlotToContainer(new Slot(this.mTileEntity, 5, 134, 23));
        addSlotToContainer(new Slot(this.mTileEntity, 6, 98, 41));
        addSlotToContainer(new Slot(this.mTileEntity, 7, 116, 41));
        addSlotToContainer(new Slot(this.mTileEntity, 8, 134, 41));

        addSlotToContainer(mSpecialSlot = new GT_Slot_Render(this.mTileEntity, 9, 35, 23));

        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 10, 8, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 10, 26, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 10, 44, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 10, 62, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 10, 80, 63, false, true, 1));
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex < 9) {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        }
        Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
        if (tSlot != null) {
            if (this.mTileEntity.getMetaTileEntity() == null) {
                return null;
            }
            if (aSlotIndex == 9) {
                ((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).clickTypeIcon(aMouseclick != 0, aPlayer.inventory.getItemStack());
                return null;
            }
            if (aSlotIndex == 10) {
                ((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bOutput = (!((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bOutput);
                if (((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bOutput) {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("116","Emit Energy to Outputside"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("117","Don't emit Energy"));
                }
                return null;
            }
            if (aSlotIndex == 11) {
                ((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull = (!((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull);
                if (((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull) {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("122","Emit Redstone if slots contain something"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("123","Don't emit Redstone"));
                }
                return null;
            }
            if (aSlotIndex == 12) {
                ((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bInvert = (!((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bInvert);
                if (((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bInvert) {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("120","Invert Redstone"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("121","Don't invert Redstone"));
                }
                return null;
            }
            if (aSlotIndex == 13) {
                ((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bInvertFilter = (!((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bInvertFilter);
                if (((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bInvertFilter) {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("124","Invert Filter"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("125","Don't invert Filter"));
                }
                return null;
            }
            if (aSlotIndex == 14) {
                ((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bNBTAllowed = (!((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bNBTAllowed);
                if (((GT_MetaTileEntity_SpecialFilter) this.mTileEntity.getMetaTileEntity()).bNBTAllowed) {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("126","Ignore NBT"));
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("127","NBT has to match"));
                }
                return null;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public int getSlotCount() {
        return 9;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 9;
    }

    public GT_Slot_Render getSpecialSlot() {
        return mSpecialSlot;
    }
}
