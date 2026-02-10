package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbineBase;

public class MTEHatchTurbineGui extends MTEHatchBaseGui<MTEHatchTurbine> {

    public MTEHatchTurbineGui(MTEHatchTurbine hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        syncManager.registerSlotGroup("turbine", 1);
        return super.createContentHolderRow(panel, syncManager).child(
            new ItemSlot().slot(
                new ModularSlot(hatch.inventoryHandler, 0).slotGroup("turbine")
                    .filter(MTELargerTurbineBase::isValidTurbine))
                .align(Alignment.Center));
    }
}
