package gregtech.common.gui;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchMufflerAdvanced;

public class MTEHatchMufflerAdvancedGui extends MTEHatchBaseGui<MTEHatchMufflerAdvanced> {
    public MTEHatchMufflerAdvancedGui(MTEHatchMufflerAdvanced hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        syncManager.registerSlotGroup("filter_slot", 1);
        return super.createContentHolderRow(panel, syncManager)
            .child(new ItemSlot().slot(hatch.inventoryHandler, 0)
                .align(Alignment.CENTER));
    }
}
