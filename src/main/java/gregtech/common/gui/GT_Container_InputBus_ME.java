package gregtech.common.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.gui.GT_Slot_Holo_ME;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_InputBus_ME extends GT_ContainerMetaTile_Machine {
    private static final int LEFT_OFFSET = 8;
    private static final int TOP_OFFSET = 10;
    private static final int SLOT_SIZE = 18;
    public static final int CIRCUIT_SLOT = 32;

    public GT_Container_InputBus_ME(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        for (int y = 0; y < 4; ++y)
            for (int x = 0; x < 4; ++x)
                addSlotToContainer(new GT_Slot_Holo(
                        this.mTileEntity,
                        x + y * 4,
                        LEFT_OFFSET + x * SLOT_SIZE,
                        TOP_OFFSET + y * SLOT_SIZE,
                        false,
                        true,
                        1));
        for (int y = 0; y < 4; ++y)
            for (int x = 0; x < 4; ++x) {
                GT_Slot_Holo_ME slot = new GT_Slot_Holo_ME(
                        this.mTileEntity,
                        x + y * 4 + 16,
                        LEFT_OFFSET + x * SLOT_SIZE + 90,
                        TOP_OFFSET + y * SLOT_SIZE,
                        false,
                        true);
                addSlotToContainer(slot);
            }
        super.addSlots(aInventoryPlayer);
    }

    private boolean containsSuchStack(ItemStack tStack) {
        for (int i = 0; i < 16; ++i) {
            Slot tSlot = (Slot) this.inventorySlots.get(i);
            if (tSlot != null && GT_Utility.areStacksEqual(tSlot.getStack(), tStack, false)) return true;
        }
        return false;
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (aSlotIndex >= 0 && aSlotIndex < 16) {
            Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
            if (tSlot != null) {
                if (this.mTileEntity.getMetaTileEntity() == null) return null;
                ItemStack tStack = aPlayer.inventory.getItemStack();
                if (tStack == null) {
                    tSlot.putStack(null);
                } else {
                    if (containsSuchStack(tStack)) return null;
                    tSlot.putStack(GT_Utility.copyAmount(1L, tStack));
                }
                if (mTileEntity.isServerSide()) {
                    ItemStack newInfo = ((GT_MetaTileEntity_Hatch_InputBus_ME) mTileEntity.getMetaTileEntity())
                            .updateInformationSlot(aSlotIndex, tStack);
                    ((Slot) this.inventorySlots.get(aSlotIndex + 16)).putStack(newInfo);
                    detectAndSendChanges();
                }
                return null;
            }
        }
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }
}
