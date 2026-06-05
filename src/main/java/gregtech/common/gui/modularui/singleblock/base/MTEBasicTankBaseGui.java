package gregtech.common.gui.modularui.singleblock.base;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.MTEBasicTank;

public class MTEBasicTankBaseGui<T extends MTEBasicTank> extends MTEMachineWithFluidScreenBaseGui<T> {

    public MTEBasicTankBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.child(createScreen(panel, syncManager, machine.getFluidTank()));
        mainRow.child(createIO(panel, syncManager, machine.getInputSlot(), machine.getOutputSlot()));
        mainRow.childIf(supportsFluidFilterScreen(), () -> createFilterScreen(panel, syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }
}
