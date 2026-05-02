package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.MTELargerTurbineBaseLegacy;

public class MTEHatchTurbineGui extends MTEHatchBaseGui<MTEHatchTurbine> {

    public MTEHatchTurbineGui(MTEHatchTurbine hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlot().slot(
                    .filter(MTELargerTurbineBaseLegacy::isValidTurbine))
                new ModularSlot(hatch.inventoryHandler, 0).singletonSlotGroup()
                    .filter(MTELargerTurbineBaseLegacy::isValidTurbine))
                .center());
    }
}
