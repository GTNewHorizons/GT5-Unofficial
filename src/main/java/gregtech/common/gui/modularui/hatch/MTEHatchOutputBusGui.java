package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.FilterSlot;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTEHatchOutputBusGui extends MTEHatchBaseGui<MTEHatchOutputBus> {

    public MTEHatchOutputBusGui(MTEHatchOutputBus hatch) {
        super(hatch);
    }

    // just in case any subclasses want to override this
    // value corresponds to the size of any side of the slot group grid
    protected int getDimension() {
        return Math.max(1, machine.mTier + 1);
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomLeftCornerFlow(panel, syncManager)
            .child(new FilterSlot(machine::getLockedItem, machine::setLockedItem));
    }

    @Override
    protected int getBasePanelHeight() {
        // we subtract 3 from the dimension before adding this value as a 4x4 slot grid is the maximum that fits on the
        // default panel
        int dimension = getDimension();
        return super.getBasePanelHeight() + (dimension > 4 ? (dimension - 3) * SLOT_SIZE : 0);
    }

    @Override
    protected int getBasePanelWidth() {
        // we subtract 9 from the dimension before adding this value as a 9x9 slot grid is the maximum that fits on the
        // default width panel
        return super.getBasePanelWidth() + Math.max(0, SLOT_SIZE * (this.getDimension() - 9));
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(getDimension())
                .build()
                .center());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return getDimension() <= 4;
    }
}
