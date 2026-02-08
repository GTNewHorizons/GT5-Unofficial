package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gtnhlanth.common.hatch.MTEBusInputFocus;

public class MTEBusInputFocusGui extends MTEHatchNbtConsumableGui {

    public MTEBusInputFocusGui(MTEBusInputFocus hatch) {
        super(hatch);
    }

    @Override
    protected Flow createInputColumn(int amountPerSlotGroup) {
        Flow inputColumn = Flow.column()
            .coverChildren();
        // fits 4 * 4 slots on screen, with scroll bar to go lower
        ListWidget<IWidget, ?> list = new ListWidget<>().size(18 * 4, 18 * 4);
        String[] matrix = new String[hatch.getInputSlotCount() / 4];
        Arrays.fill(matrix, "cccc");
        list.child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key(
                    'c',
                    index -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index).slotGroup("input")))
                .build());
        return inputColumn.child(list);
    }

    @Override
    protected boolean doesAddGregTechLogo() {
        return false;
    }

    protected Flow createOutputColumn(int amountPerSlotGroup) {
        Flow outputColumn = Flow.column()
            .coverChildren();
        // fits 4 * 4 slots on screen, with scroll bar out
        ListWidget<IWidget, ?> list = new ListWidget<>().size(18 * 4, 18 * 4);
        String[] matrix = new String[hatch.getInputSlotCount() / 4];
        Arrays.fill(matrix, "cccc");
        list.child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key(
                    'c',
                    index -> new ItemSlot().slot(
                        new ModularSlot(hatch.inventoryHandler, index + hatch.getInputSlotCount()).slotGroup("output")))
                .build());
        return outputColumn.child(list);
    }
}
