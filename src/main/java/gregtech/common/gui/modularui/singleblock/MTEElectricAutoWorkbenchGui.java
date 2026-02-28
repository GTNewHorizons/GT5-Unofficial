package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTEElectricAutoWorkbench;

public class MTEElectricAutoWorkbenchGui {

    private final MTEElectricAutoWorkbench machine;

    private static final String[] MODE_TEXT = new String[] { "Normal Crafting Table", "???", "1x1", "2x2", "3x3",
        "Unifier", "Dust", "???", "Hammer?", "Circle" };

    public MTEElectricAutoWorkbenchGui(MTEElectricAutoWorkbench machine) {
        this.machine = machine;
    }

    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(machine, data, syncManager, uiSettings)
            .doesAddGregTechLogo(false)
            .build();

        Flow column = new Column().coverChildren()
            .marginTop(4)
            .marginLeft(7);
        column.child(createMainRow().marginBottom(1));

        column.child(
            SlotGroupWidget.builder()
                .matrix("SSSSSSSSS")
                .key(
                    'S',
                    index -> new ItemSlot()
                        .slot(new ModularSlot(machine.inventoryHandler, index + 9).accessibility(false, true))
                        .background(GTGuiTextures.SLOT_ITEM_DARK))
                .build());

        panel.child(column);
        return panel;
    }

    private Flow createMainRow() {
        Flow row = new Row().coverChildren();

        row.child(
            SlotGroupWidget.builder()
                .matrix("SSS", "SSS", "SSS")
                .key('S', index -> new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, index)))
                .build()
                .marginRight(1));

        ParentWidget<?> funny3x3 = new ParentWidget<>().coverChildren()
            .marginRight(2);
        funny3x3.child(
            GTGuiTextures.PICTURE_SLOTS_HOLO_3BY3.asWidget()
                .size(54));
        funny3x3.child(
            SlotGroupWidget.builder()
                .matrix("SSS", "SSS", "SSS")
                .key(
                    'S',
                    index -> new PhantomItemSlot().slot(new ModularSlot(machine.inventoryHandler, index + 19))
                        .background(IDrawable.EMPTY))
                .build());
        row.child(funny3x3);

        row.child(createSquareGroup());
        return row;
    }

    private Flow createSquareGroup() {
        Flow column = new Column().size(51, 54);

        Flow topRow = new Row().widthRel(1)
            .height(18);

        CycleButtonWidget throughputButton = new CycleButtonWidget().size(18)
            .marginLeft(2)
            .marginRight(13)
            .value(new IntSyncValue(() -> machine.mThroughPut, val -> machine.mThroughPut = val))
            .stateCount(MTEElectricAutoWorkbench.MAX_THROUGHPUT);

        for (int i = 0; i < MTEElectricAutoWorkbench.MAX_THROUGHPUT; i++) {
            throughputButton.stateOverlay(i, GTGuiTextures.OVERLAY_BUTTON_THROUGHPUT[i]);
        }
        topRow.child(throughputButton);

        topRow.child(
            new PhantomItemSlot().slot(new ModularSlot(machine.inventoryHandler, 28).accessibility(false, false))
                .overlay(GTGuiTextures.OVERLAY_SLOT_ARROW_4));

        Flow middleRow = new Row().widthRel(1)
            .height(18);

        middleRow.child(
            GTGuiTextures.PICTURE_GT_LOGO_STANDARD.asWidget()
                .marginBottom(1)
                .size(17)
                .marginRight(1));

        middleRow.child(
            GTGuiTextures.PICTURE_WORKBENCH_CIRCLE.asWidget()
                .size(16)
                .marginTop(1)
                .marginRight(3));

        middleRow.child(
            GTGuiTextures.PICTURE_ARROW_WHITE_DOWN.asWidget()
                .size(10, 16)
                .marginTop(1));

        Flow bottomRow = new Row().widthRel(1)
            .coverChildrenHeight();

        CycleButtonWidget modeButton = new CycleButtonWidget().size(18)
            .marginLeft(2)
            .marginRight(13)
            .value(new IntSyncValue(() -> machine.mMode, val -> {
                machine.mMode = val;
                machine.switchMode();
            }))
            .stateCount(MTEElectricAutoWorkbench.MAX_MODES);

        for (int i = 0; i < MTEElectricAutoWorkbench.MAX_MODES; i++) {
            modeButton.stateOverlay(i, GTGuiTextures.OVERLAY_BUTTON_MODE[i]);
            modeButton.addTooltip(i, "Mode: " + MODE_TEXT[i]);
        }
        bottomRow.child(modeButton);

        bottomRow.child(
            new ItemSlot().background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD)
                .slot(new ModularSlot(machine.inventoryHandler, 18).accessibility(false, true)));

        column.child(topRow);
        column.child(middleRow);
        column.child(bottomRow);
        return column;
    }
}
