package gregtech.common.gui.modularui.item;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import gregtech.api.util.StringUtils;

import java.util.Arrays;

public class ToolboxGui {
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;
    private static final int BORDER_RADIUS = 4;
    private static final String SLOT_GROUP_SYNC_NAME = "toolbox_internal_inventory";

    private final ItemStackHandler inventory;

    public ToolboxGui(final ItemStackHandler inventory) {
        this.inventory = inventory;
    }

    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel("Advanced Toolbox", WIDTH, HEIGHT);
        panel.bindPlayerInventory();

        return panel.child(
            Flow.column()
                .padding(BORDER_RADIUS)
                .child(createToolboxInventoryRow(panel))
                .child(createPlayerInventoryRow(panel)));
    }

    protected IWidget createToolboxInventoryRow(ModularPanel panel) {
        String[] matrix = new String[2];
        String repeat = StringUtils.getRepetitionOf('s', 7);
        Arrays.fill(matrix, repeat);
        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key('s', i -> new ItemSlot().slot(new ModularSlot(inventory, i).slotGroup(SLOT_GROUP_SYNC_NAME)))
            .build()
            .coverChildren()
            .align(Alignment.Center);
    }

    protected IWidget createPlayerInventoryRow(ModularPanel panel) {
        return new Row().widthRel(1)
            .height(76)
            .alignX(0)
            .child(
                SlotGroupWidget.playerInventory(false)
                    .marginLeft(4)
                    .marginRight(2));
    }
}
