package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.FilterSlot;

public class MTEHatchOutputBusGui extends MTEHatchBaseGui<MTEHatchOutputBus> {

    public MTEHatchOutputBusGui(MTEHatchOutputBus hatch) {
        super(hatch);
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    // just in case any subclasses want to override this
    // value corresponds to the size of any side of the slot group grid
    protected int getDimension() {
        return Math.max(1, hatch.mTier + 1);
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createLeftCornerFlow(panel, syncManager)
            .child(new FilterSlot(hatch::getLockedItem, hatch::setLockedItem));
    }

    private final int BUTTON_SIZE = 18;

    @Override
    protected int getBasePanelHeight() {
        // we subtract 4 from the dimension before adding this value as a 4x4 slot grid is the maximum that fits on the
        // default panel
        return super.getBasePanelHeight() + Math.max(0, BUTTON_SIZE * (this.getDimension() - 4) + 18);
    }

    @Override
    protected int getBasePanelWidth() {
        // we subtract 9 from the dimension before adding this value as a 9x9 slot grid is the maximum that fits on the
        // default width panel
        return super.getBasePanelWidth() + Math.max(0, BUTTON_SIZE * (this.getDimension() - 9));
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {

        return super.createContentSection(panel, syncManager).child(createSlots(syncManager));
    }

    protected SlotGroupWidget createSlots(PanelSyncManager syncManager) {

        final int dimension = this.getDimension();
        syncManager.registerSlotGroup("item_inv", dimension);

        String[] matrix = new String[dimension];
        String repeat = StringUtils.getRepetitionOf('s', dimension);
        Arrays.fill(matrix, repeat);
        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                's',
                index -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index).slotGroup("item_inv")))
            .build()
            .coverChildren()
            .marginTop((BUTTON_SIZE / 2) * (4 - this.getDimension()))
            .horizontalCenter();
    }
}
