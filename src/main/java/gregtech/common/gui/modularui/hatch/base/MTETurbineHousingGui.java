package gregtech.common.gui.modularui.hatch.base;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbineProvider;

public class MTETurbineHousingGui extends MTEHatchBaseGui<MTEHatchTurbineProvider> {

    public MTETurbineHousingGui(MTEHatchTurbineProvider hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlot().slot(
                new ModularSlot(hatch.inventoryHandler, 0).singletonSlotGroup()
                    .filter(hatch::isItemStackTurbine))
                .center());
    }
}
