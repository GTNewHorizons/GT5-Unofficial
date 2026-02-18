package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.MTEHatchPatternProvider;

public class MTEHatchPatternProviderGui extends MTEHatchBaseGui<MTEHatchPatternProvider> {

    public MTEHatchPatternProviderGui(MTEHatchPatternProvider hatch) {
        super(hatch);
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return false;
    }

    @Override
    protected boolean supportsRightCornerFlow() {
        return false;
    }

    private static final int ROW_SIZE = 9;

    int numRows() {
        return this.hatch.getSizeInventory() / ROW_SIZE;
    }

    private final int BUTTON_SIZE = 18;

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + Math.max(0, BUTTON_SIZE * (this.numRows() - 4) + 18);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createSlots(syncManager));
    }

    private static final String PATTERN_INV_NAME = "pattern_inv";

    private SlotGroupWidget createSlots(PanelSyncManager syncManager) {
        final var numRows = this.numRows();
        syncManager.registerSlotGroup(PATTERN_INV_NAME, numRows);

        String[] matrix = new String[numRows];
        String repeat = StringUtils.getRepetitionOf('s', ROW_SIZE);
        Arrays.fill(matrix, repeat);

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                's',
                index -> new PatternSlot().slot(
                    new ModularSlot(hatch.inventoryHandler, index)
                        .filter(itemStack -> hatch.isItemValidForSlot(index, itemStack))
                        .slotGroup(PATTERN_INV_NAME)))
            .build()
            .coverChildren()
            .alignX(Alignment.CENTER);
    }
}
