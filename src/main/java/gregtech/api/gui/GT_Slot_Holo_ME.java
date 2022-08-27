package gregtech.api.gui;

import net.minecraft.inventory.IInventory;

public class GT_Slot_Holo_ME extends GT_Slot_Holo {
    public GT_Slot_Holo_ME(
            IInventory inventory, int slotIndex, int xPos, int yPos, boolean aCanInsertItem, boolean aCanStackItem) {
        super(inventory, slotIndex, xPos, yPos, aCanInsertItem, aCanStackItem, Integer.MAX_VALUE);
    }
}
