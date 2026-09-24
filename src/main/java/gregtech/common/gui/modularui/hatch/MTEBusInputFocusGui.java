package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gtnhlanth.common.hatch.MTEBusInputFocus;

public class MTEBusInputFocusGui extends MTEHatchNbtConsumableGui {

    public MTEBusInputFocusGui(MTEBusInputFocus hatch) {
        super(hatch);
    }

    @Override
    protected Flow createInputColumn(PanelSyncManager syncManager) {
        Flow inputColumn = Flow.column()
            .coverChildren();

        // fits 4 * 4 slots on screen, with scroll bar to go lower
        ListWidget<IWidget, ?> list = new ListWidget<>().size(4 * SLOT_SIZE);
        list.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(4, machine.getInputSlotCount() / 4)
                .slotGroupKey("input")
                .build());

        return inputColumn.child(list);
    }

    @Override
    protected boolean doesAddGregTechLogo() {
        return false;
    }

    @Override
    protected Flow createOutputColumn(PanelSyncManager syncManager) {
        Flow outputColumn = Flow.column()
            .coverChildren();

        // fits 4 * 4 slots on screen, with scroll bar out
        ListWidget<IWidget, ?> list = new ListWidget<>().size(4 * SLOT_SIZE);
        int inputSlotCount = machine.getInputSlotCount();
        list.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(4, inputSlotCount / 4)
                .slotGroupKey("output")
                .canPut(false)
                .indexOffset(inputSlotCount)
                .build());

        return outputColumn.child(list);
    }
}
