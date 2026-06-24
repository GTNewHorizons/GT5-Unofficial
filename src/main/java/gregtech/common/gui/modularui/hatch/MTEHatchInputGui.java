package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchInputGui extends MTEHatchBaseGui<MTEHatchInput> {

    public MTEHatchInputGui(MTEHatchInput machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1);

        mainRow.child(createScreen(panel, syncManager, machine.getFluidTank()));
        mainRow.child(
            createIO(panel, syncManager, machine.getInputSlot(), machine.getOutputSlot(), machine.getFluidTank()));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }
}
