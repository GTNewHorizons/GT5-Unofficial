package gregtech.common.gui.modularui.widget;

import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;

import appeng.api.storage.data.IAEItemStack;

public class AEBaseSlot extends BaseSlot {

    private final IAEItemHandlerModifiable inventory;

    public AEBaseSlot(IAEItemHandlerModifiable inventory, int index, boolean phantom) {
        super(inventory, index, phantom);
        this.inventory = inventory;
    }

    public AEBaseSlot(IAEItemHandlerModifiable inventory, int index) {
        super(inventory, index);
        this.inventory = inventory;
    }

    public IAEItemStack getAEStack() {
        return inventory.getAEStackInSlot(getSlotIndex());
    }
}
