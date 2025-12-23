package gregtech.common.gui.modularui.hatch.base;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbineProvider;

public class MTETurbineHousingGui extends MTEHatchBaseGui<MTEHatchTurbineProvider> {

    public MTETurbineHousingGui(MTEHatchTurbineProvider hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        syncManager.registerSlotGroup("turbine", 0);
        return super.createContentHolderRow(panel, syncManager).child(
            new ItemSlot().slot(
                new ModularSlot(hatch.inventoryHandler, 0).slotGroup("turbine")
                    .filter((a) -> hatch.isItemStackTurbine(a)))
                .align(Alignment.CENTER));
    }
}
