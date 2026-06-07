package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.GhostMoldSlotWidget;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;

public class MTEHatchSolidifierGui extends MTEHatchBaseGui<MTEHatchSolidifier> {

    public MTEHatchSolidifierGui(MTEHatchSolidifier machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1);

        mainRow.child(createScreen(panel, syncManager, machine.getFluidTank()));
        mainRow.child(createIO(panel, syncManager, machine.getInputSlot(), machine.getOutputSlot()));
        mainRow.child(
            new GhostMoldSlotWidget(machine, syncManager)
                .slot(
                    new ModularSlot(machine.inventoryHandler, MTEHatchSolidifier.moldSlot)
                        .filter(itemStack -> machine.findMatchingMoldIndex(itemStack) != -1))
                .marginLeft(SLOT_SIZE * 3 / 2));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }
}
