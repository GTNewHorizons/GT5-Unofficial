package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTEHatchDataAccessGui extends MTEHatchBaseGui<MTEHatchDataAccess> {

    public MTEHatchDataAccessGui(MTEHatchDataAccess hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(machine.mTier == 4 ? 2 : 4)
                .slotGroupKey("data_inv")
                .itemSlotSupplier(() -> new ItemSlot().backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CIRCUIT))
                .build()
                .center());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
