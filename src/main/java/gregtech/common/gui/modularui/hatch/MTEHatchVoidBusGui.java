package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.api.metatileentity.implementations.MTEHatchVoidBus;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.FilterSlot;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTEHatchVoidBusGui extends MTEHatchBaseGui<MTEHatchVoidBus> {

    public MTEHatchVoidBusGui(MTEHatchVoidBus machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(2)
                .slotGroupKey("filter_inv")
                .itemSlotSupplier(FilterSlot::new)
                .build()
                .center());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
