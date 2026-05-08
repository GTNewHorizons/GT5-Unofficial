package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.singleblock.base.MTEBufferBaseGui;
import gregtech.common.tileentities.automation.MTEChestBuffer;

public class MTEChestBufferGui extends MTEBufferBaseGui<MTEChestBuffer> {

    public MTEChestBufferGui(MTEChestBuffer machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new Grid().coverChildren()
                .gridOfWidthHeight(
                    9,
                    3,
                    ($x, $y, index) -> new ItemSlot()
                        .slot(new ModularSlot(machine.inventoryHandler, index).slotGroup("item_inv")))
                .horizontalCenter());
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftCornerFlow(panel, syncManager).child(
            GTGuiTextures.PICTURE_ARROW_22_RED.asWidget()
                .size(50, 22)
                .marginBottom(1)
                .marginLeft(1));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.registerSlotGroup("item_inv", 3);
    }
}
