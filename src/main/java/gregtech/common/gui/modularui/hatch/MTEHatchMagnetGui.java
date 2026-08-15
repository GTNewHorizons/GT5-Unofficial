package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEHatchMagnet;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchMagnetGui extends MTEHatchBaseGui<MTEHatchMagnet> {

    public MTEHatchMagnetGui(MTEHatchMagnet machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, 0).singletonSlotGroup())
                .center());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
