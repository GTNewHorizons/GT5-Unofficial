package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchCapacitor;

public class MTEHatchCapacitorGui extends MTEHatchBaseGui<MTEHatchCapacitor> {

    public MTEHatchCapacitorGui(MTEHatchCapacitor hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {

        syncManager.registerSlotGroup("capacitor_inventory", 4);
        String[] matrix = { "xxxx", "xxxx", "xxxx", "xxxx" };
        return super.createContentHolderRow(panel, syncManager).child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key(
                    'x',
                    i -> new ItemSlot()
                        .slot(new ModularSlot(hatch.inventoryHandler, i).slotGroup("capacitor_inventory"))
                        .overlay(GTGuiTextures.OVERLAY_SLOT_CHARGER))
                .build()
                .coverChildren()
                .align(Alignment.Center));

    }
}
