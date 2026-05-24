package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchChiselBus;

public class MTEHatchChiselBusGui extends MTEHatchBaseGui<MTEHatchChiselBus> {

    private static final int INPUT_COLS = 8;

    public MTEHatchChiselBusGui(MTEHatchChiselBus hatch) {
        super(hatch);
    }

    private int getInputRows() {
        return MTEHatchChiselBus.getSlots(machine.mTier) / INPUT_COLS;
    }

    private int getGhostCols() {
        int ghostCount = MTEHatchChiselBus.getGhostTargetCount(machine.mTier);
        return (int) Math.ceil(Math.sqrt(ghostCount));
    }

    private int getGhostRows() {
        int ghostCount = MTEHatchChiselBus.getGhostTargetCount(machine.mTier);
        int cols = getGhostCols();
        return (int) Math.ceil((double) ghostCount / cols);
    }

    @Override
    protected int getBasePanelHeight() {
        int inputRows = getInputRows();
        return super.getBasePanelHeight() + Math.max(0, SLOT_SIZE * (inputRows - 4) + SLOT_SIZE);
    }

    @Override
    protected int getBasePanelWidth() {
        int ghostCols = getGhostCols();
        int totalCols = ghostCols + INPUT_COLS;
        return super.getBasePanelWidth() + Math.max(0, SLOT_SIZE * (totalCols - 9) + 3);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        int totalSlots = MTEHatchChiselBus.getSlots(machine.mTier);
        int ghostCount = MTEHatchChiselBus.getGhostTargetCount(machine.mTier);
        int inputRows = getInputRows();
        int ghostCols = getGhostCols();
        int ghostRows = getGhostRows();

        syncManager.registerSlotGroup("inputs", INPUT_COLS);

        // Ghost target phantom slots
        String[] ghostMatrix = new String[ghostRows];
        Arrays.fill(ghostMatrix, StringUtils.getRepetitionOf('t', ghostCols));

        var ghostWidget = SlotGroupWidget.builder()
            .matrix(ghostMatrix)
            .key(
                't',
                index -> new PhantomItemSlot()
                    .slot(new ModularSlot(machine.ghostTargets, index).accessibility(true, false)))
            .build();

        // Input slots grid
        String[] inputMatrix = new String[inputRows];
        Arrays.fill(inputMatrix, StringUtils.getRepetitionOf('x', INPUT_COLS));

        var inputWidget = SlotGroupWidget.builder()
            .matrix(inputMatrix)
            .key(
                'x',
                index -> new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, index).slotGroup("inputs")))
            .build();

        return super.createContentSection(panel, syncManager).child(
            Flow.row()
                .coverChildrenHeight()
                .topRel(0)
                .horizontalCenter()
                .child(ghostWidget)
                .child(inputWidget.marginLeft(3)));
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
