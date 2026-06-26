package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.PatternSlot;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gregtech.common.tileentities.machines.MTEHatchPatternProvider;

public class MTEHatchPatternProviderGui extends MTEHatchBaseGui<MTEHatchPatternProvider> {

    public MTEHatchPatternProviderGui(MTEHatchPatternProvider hatch) {
        super(hatch);
    }

    private static final int ROW_SIZE = 9;
    private static final String PATTERN_INV_NAME = "pattern_inv";

    private int numRows() {
        return this.machine.getSizeInventory() / ROW_SIZE;
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + Math.max(0, SLOT_SIZE * (this.numRows() - 4));
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(ROW_SIZE, numRows())
                .slotGroupKey(PATTERN_INV_NAME)
                .itemSlotSupplier(PatternSlot::new)
                .build()
                .horizontalCenter());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }

    @Override
    protected boolean doesAddGregTechLogo() {
        return false;
    }
}
