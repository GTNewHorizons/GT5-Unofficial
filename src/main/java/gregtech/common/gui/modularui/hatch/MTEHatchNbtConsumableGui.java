package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchNbtConsumable;

public class MTEHatchNbtConsumableGui extends MTEHatchBaseGui<MTEHatchNbtConsumable> {

    public MTEHatchNbtConsumableGui(MTEHatchNbtConsumable machine) {
        super(machine);
        dimension = (int) Math.floor(Math.sqrt(machine.getInputSlotCount()));
    }

    private final int dimension;

    @Override
    protected boolean doesAddGregTechLogo() {
        return dimension < 4;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow slotRow = Flow.row()
            .coverChildren()
            .center();

        // < 4x4 slot groups have logo in the corner, otherwise its in the center
        boolean logoOverride = !this.doesAddGregTechLogo();

        slotRow.child(createInputColumn(syncManager).marginRight(logoOverride ? 0 : ((4 - dimension) * SLOT_SIZE)));
        slotRow.childIf(logoOverride, this::createLogo);
        slotRow.child(createOutputColumn(syncManager));

        return super.createContentSection(panel, syncManager).child(slotRow);
    }

    protected Flow createInputColumn(PanelSyncManager syncManager) {
        return Flow.column()
            .coverChildren()
            .childIf(
                machine.getInputSlotCount() < 16,
                () -> IKey.lang("gtpp.gui.text.stock")
                    .asWidget()
                    .decoration())
            .child(
                new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(dimension)
                    .slotGroupKey("input")
                    .build());
    }

    protected Flow createOutputColumn(PanelSyncManager syncManager) {
        int inputSlotCount = machine.getInputSlotCount();

        return Flow.column()
            .coverChildren()
            .childIf(
                inputSlotCount < 16,
                () -> IKey.lang("gtpp.gui.text.active")
                    .asWidget()
                    .decoration())
            .child(
                new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(dimension)
                    .slotGroupKey("output")
                    .canPut(false)
                    .indexOffset(inputSlotCount)
                    .build());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
