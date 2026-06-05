package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchChiselBus;

public class MTEHatchChiselBusGui extends MTEHatchBaseGui<MTEHatchChiselBus> {

    private static final int INPUT_COLS = 8;

    public MTEHatchChiselBusGui(MTEHatchChiselBus hatch) {
        super(hatch);
    }

    private int getInputRows() {
        return MTEHatchChiselBus.getSlots(machine.mTier) / INPUT_COLS;
    }

    private int getGhostGridSize() {
        int ghostCount = MTEHatchChiselBus.getGhostTargetCount(machine.mTier);
        return (int) Math.ceil(Math.sqrt(ghostCount));
    }

    @Override
    protected int getBasePanelHeight() {
        int inputRows = getInputRows();
        return super.getBasePanelHeight() + Math.max(0, SLOT_SIZE * (inputRows - 4) + SLOT_SIZE);
    }

    @Override
    protected int getBasePanelWidth() {
        int totalCols = getGhostGridSize() + INPUT_COLS;
        return super.getBasePanelWidth() + Math.max(0, SLOT_SIZE * (totalCols - 9) + SLOT_SIZE);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(SLOT_SIZE)
            .horizontalCenter();

        // target phantom slots
        mainRow.child(
            new ItemSlotGridBuilder(machine.ghostTargets, syncManager).size(getGhostGridSize())
                .itemSlotSupplier(PhantomItemSlot::new)
                .build());

        // input slots
        mainRow.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(INPUT_COLS, getInputRows())
                .build());

        return super.createContentSection(panel, syncManager).child(mainRow);
    }
}
