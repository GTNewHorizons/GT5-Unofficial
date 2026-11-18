package gregtech.common.gui.modularui.singleblock.base;

import java.util.function.Predicate;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.screen.ModularPanel;
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
            .childPadding(1);

        mainRow.child(createScreen(panel, syncManager));
        mainRow.child(createScreenIO(panel, syncManager));
        mainRow.childIf(supportsFilterScreen(), () -> createFilterScreen(panel, syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected int getInputSlotIndex() {
        return machine.getInputSlot();
    }

    @Override
    protected int getOutputSlotIndex() {
        return machine.getOutputSlot();
    }

    @Override
    protected Predicate<FluidStack> getFluidSlotFilter() {
        return machine::isFluidInputAllowed;
    }

    @Override
    protected IFluidTank getMachineFluidTank() {
        return machine.getFluidTank();
    }
}
