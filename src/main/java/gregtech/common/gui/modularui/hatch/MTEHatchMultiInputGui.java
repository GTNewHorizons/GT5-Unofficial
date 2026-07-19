package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchMultiInputGui extends MTEHatchBaseGui<MTEHatchMultiInput> {

    public MTEHatchMultiInputGui(MTEHatchMultiInput hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        FluidStackTank[] fluidTanks = machine.getFluidTanks();

        return super.createContentSection(panel, syncManager).child(
            new Grid().coverChildren()
                .gridOfWidthHeight(
                    2,
                    2,
                    ($x, $y, index) -> new FluidSlot().syncHandler(new FluidSlotSyncHandler(fluidTanks[index])))
                .center());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
