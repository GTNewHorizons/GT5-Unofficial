package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchInputBusGui extends MTEHatchBaseGui<MTEHatchInputBus> {

    public MTEHatchInputBusGui(MTEHatchInputBus hatch) {
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
        BooleanSyncValue stackSync = new BooleanSyncValue(() -> !hatch.disableSort, val -> hatch.disableSort = !val);
        BooleanSyncValue insertionSync = new BooleanSyncValue(
            () -> !hatch.disableLimited,
            val -> hatch.disableLimited = !val);
        return super.createLeftCornerFlow(panel, syncManager)
            .child(
                createToggleButton(
                    stackSync,
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    "GT5U.machines.sorting_mode.tooltip"))
            .child(
                createToggleButton(
                    insertionSync,
                    GTGuiTextures.OVERLAY_BUTTON_ONE_STACK_LIMIT,
                    "GT5U.machines.one_stack_limit.tooltip"));
    }

    @Override
    protected Flow createRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createRightCornerFlow(panel, syncManager);
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

    private ToggleButton createToggleButton(BooleanSyncValue syncValue, UITexture texture, String key) {
        return new ToggleButton().value(syncValue)
            .overlay(texture)
            .addTooltipLine(GTUtility.translate(key));
    }
}
