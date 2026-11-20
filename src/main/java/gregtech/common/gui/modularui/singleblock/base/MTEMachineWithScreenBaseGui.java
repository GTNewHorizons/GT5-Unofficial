package gregtech.common.gui.modularui.singleblock.base;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonWidgets;

public abstract class MTEMachineWithScreenBaseGui<T extends MTETieredMachineBlock>
    extends MTETieredMachineBlockBaseGui<T> {

    protected static final int SCREEN_WIDTH = 71;
    protected static final int SCREEN_HEIGHT = 54;
    protected static final int FILTER_SCREEN_WIDTH = 71;
    protected static final int FILTER_SCREEN_HEIGHT = 54;

    public MTEMachineWithScreenBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.registerSlotGroup("io_inv", 1);
    }

    protected boolean supportsScreen() {
        return true;
    }

    protected ParentWidget<?> createScreen(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> screen = CommonWidgets.createHatchScreen(SCREEN_WIDTH, SCREEN_HEIGHT);

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        textColumn.child(createScreenAmountLabel(panel, syncManager));
        textColumn.child(createScreenAmount(panel, syncManager));

        screen.child(textColumn);

        screen.child(createSlot(panel, syncManager));

        return screen;
    }

    protected abstract TextWidget<?> createScreenAmountLabel(ModularPanel panel, PanelSyncManager syncManager);

    protected abstract TextWidget<?> createScreenAmount(ModularPanel panel, PanelSyncManager syncManager);

    protected abstract Widget<?> createSlot(ModularPanel panel, PanelSyncManager syncManager);

    protected boolean supportsScreenIOColumn() {
        return true;
    }

    protected Flow createScreenIO(ModularPanel panel, PanelSyncManager syncManager) {
        Flow ioColumn = Flow.column()
            .coverChildrenWidth()
            .fullHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN);

        ioColumn.child(createInputSlot(panel, syncManager, getInputSlotIndex()));
        ioColumn.child(createScreenIOWidget(panel, syncManager));
        ioColumn.child(createOutputSlot(panel, syncManager, getOutputSlotIndex()));

        return ioColumn;
    }

    protected abstract int getInputSlotIndex();

    protected abstract int getOutputSlotIndex();

    protected abstract Widget<?> createScreenIOWidget(ModularPanel panel, PanelSyncManager syncManager);

    protected ItemSlot createInputSlot(ModularPanel panel, PanelSyncManager syncManager, int inputSlot) {
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, inputSlot).slotGroup("io_inv"))
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_IN_STANDARD);
    }

    protected ItemSlot createOutputSlot(ModularPanel panel, PanelSyncManager syncManager, int outputSlot) {
        return new ItemSlot().slot(
            new ModularSlot(machine.inventoryHandler, outputSlot).slotGroup("io_inv")
                .canPut(false))
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD);
    }

    protected abstract boolean supportsFilterScreen();

    protected ParentWidget<?> createFilterScreen(ModularPanel panel, PanelSyncManager syncManager) {
        if (!supportsFilterScreen())
            throw new UnsupportedOperationException("Machine does not support a filter screen!");

        ParentWidget<?> screen = CommonWidgets.createHatchScreen(FILTER_SCREEN_WIDTH, FILTER_SCREEN_HEIGHT);

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        textColumn.child(createFilterScreenAmountLabel(panel, syncManager));
        textColumn.child(createFilterScreenAmount(panel, syncManager));

        screen.child(textColumn);

        screen.child(createFilterSlot(panel, syncManager));

        return screen;
    }

    protected abstract TextWidget<?> createFilterScreenAmountLabel(ModularPanel panel, PanelSyncManager syncManager);

    protected abstract TextWidget<?> createFilterScreenAmount(ModularPanel panel, PanelSyncManager syncManager);

    protected abstract Widget<?> createFilterSlot(ModularPanel panel, PanelSyncManager syncManager);
}
