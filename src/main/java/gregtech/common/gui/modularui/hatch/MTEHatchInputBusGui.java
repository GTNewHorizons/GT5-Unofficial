package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTEHatchInputBusGui extends MTEHatchBaseGui<MTEHatchInputBus> {

    public MTEHatchInputBusGui(MTEHatchInputBus hatch) {
        super(hatch);
    }

    // just in case any subclasses want to override this
    // value corresponds to the size of any side of the slot group grid
    protected int getDimension() {
        return Math.max(1, machine.mTier + 1);
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue stackSync = new BooleanSyncValue(() -> !machine.disableSort, val -> machine.disableSort = !val)
            .allowC2S();
        BooleanSyncValue insertionSync = new BooleanSyncValue(
            () -> !machine.disableLimited,
            val -> machine.disableLimited = !val).allowC2S();
        return super.createBottomLeftCornerFlow(panel, syncManager)
            .child(
                CommonButtons.createToggleButton(
                    stackSync,
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    "GT5U.machines.sorting_mode.tooltip"))
            .child(
                CommonButtons.createToggleButton(
                    insertionSync,
                    GTGuiTextures.OVERLAY_BUTTON_ONE_STACK_LIMIT,
                    "GT5U.machines.one_stack_limit.tooltip"));
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
