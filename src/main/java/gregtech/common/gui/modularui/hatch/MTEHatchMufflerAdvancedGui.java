package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchMufflerAdvanced;

public class MTEHatchMufflerAdvancedGui extends MTEHatchBaseGui<MTEHatchMufflerAdvanced> {

    public MTEHatchMufflerAdvancedGui(MTEHatchMufflerAdvanced hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlot().slot(
                new ModularSlot(hatch.inventoryHandler, 0).singletonSlotGroup()
                    .filter(hatch::isAirFilter))
                .center());
    }
}
