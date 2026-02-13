package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import appeng.items.misc.ItemEncodedPattern;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.MTEHatchPatternProvider;

public class MTEHatchPatternProviderGui extends MTEHatchBaseGui<MTEHatchPatternProvider> {

    public MTEHatchPatternProviderGui(MTEHatchPatternProvider hatch) {
        super(hatch);
    }

    int rowSize() {
        return 9;
    }

    int numRows() {
        final var rowSize = this.rowSize();
        return this.hatch.getSizeInventory() / rowSize;
    }

    private final int BUTTON_SIZE = 18;

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + Math.max(0, BUTTON_SIZE * (this.numRows() - 4) + 18);
    }

    @Override
    protected int getBasePanelWidth() {
        return super.getBasePanelWidth() + Math.max(0, BUTTON_SIZE * (this.rowSize() - 9));
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {

        return super.createContentSection(panel, syncManager).child(createSlots(syncManager));
    }

    protected SlotGroupWidget createSlots(PanelSyncManager syncManager) {

        final int rowSize = this.rowSize();
        final int numRows = this.numRows();
        syncManager.registerSlotGroup("item_inv", rowSize);

        String[] matrix = new String[numRows];
        String repeat = StringUtils.getRepetitionOf('s', rowSize);
        Arrays.fill(matrix, repeat);
        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                's',
                index -> new ItemSlot().slot(
                    new ModularSlot(hatch.inventoryHandler, index).slotGroup("item_inv")
                        .filter(is -> is.getItem() instanceof ItemEncodedPattern)))
            .build()
            .coverChildren()
            .marginTop((BUTTON_SIZE / 2) * (4 - numRows))
            .horizontalCenter();
    }

}
