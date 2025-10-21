package gregtech.common.gui.modularui.cover;

import java.util.Arrays;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.common.covers.CoverChest;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;

public class CoverChestGui extends CoverBaseGui<CoverChest> {

    public CoverChestGui(CoverChest cover) {
        super(cover);
    }

    @Override
    protected boolean doesBindPlayerInventory() {
        return true;
    }

    @Override
    protected int getGUIWidth() {
        int widthPerSlot = 18;
        int additionalSpace = 15;
        return widthPerSlot * 9 + additionalSpace;
    }

    @Override
    protected int getGUIHeight() {
        int borderRadius = 4;
        int panelMargin = 2;
        int titleRowHeight = 16;
        int titleMargin = 4;
        int rows = cover.getSlotCount() / 3;
        int heightPerSlot = 18;

        return borderRadius * 2 + panelMargin * 2 + titleRowHeight + titleMargin + rows * heightPerSlot;
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        int rows = cover.getSlotCount() / 3;
        String[] matrix = new String[rows];
        Arrays.fill(matrix, "xxx");

        IItemHandlerModifiable handler = cover.getItems();

        column.child(
            // A bit of a hack to force the flow to be the same width as the window so the slot group gets centered
            Flow.row()
                .width((getGUIWidth() - 10))
                .height(0))
            .child(
                SlotGroupWidget.builder()
                    .matrix(matrix)
                    .key('x', i -> new ItemSlot().slot(new ModularSlot(handler, i)))
                    .build()
                    .alignX(0.5f));
    }
}
