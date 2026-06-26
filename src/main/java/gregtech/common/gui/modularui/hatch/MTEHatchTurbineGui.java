package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;

public class MTEHatchTurbineGui extends MTEHatchBaseGui<MTEHatchTurbine> {

    public MTEHatchTurbineGui(MTEHatchTurbine hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, 0).accessibility(false, false))
                .center());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
