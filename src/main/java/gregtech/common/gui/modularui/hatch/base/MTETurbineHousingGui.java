package gregtech.common.gui.modularui.hatch.base;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbineProvider;

public class MTETurbineHousingGui extends MTEHatchBaseGui<MTEHatchTurbineProvider> {

    public MTETurbineHousingGui(MTEHatchTurbineProvider hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentHolderRow(panel, syncManager).child(
            new ItemSlot().slot(hatch.inventoryHandler, 0)
                .align(Alignment.CENTER));
    }
}
