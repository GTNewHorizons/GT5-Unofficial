package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.util.GTUtility;
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
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .horizontalCenter();

        // input slots
        mainRow.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(INPUT_COLS, getInputRows())
                .build());

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        IPanelHandler templatePanel = syncManager
            .syncedPanel("template_panel", true, (_, _) -> createTemplatePanel(panel, syncManager));

        int gridSize = getGhostGridSize();
        return super.createBottomLeftCornerFlow(panel, syncManager).collapseDisabledChild()
            .childIf(
                gridSize == 1,
                () -> new PhantomItemSlot().slot(new ModularSlot(machine.ghostTargets, 0))
                    .addTooltipLine(GTUtility.translate("GT5U.hatch.chisel.configure.singleton")))
            .childIf(gridSize > 1, () -> createTemplatePanelButton(templatePanel));
    }

    private ButtonWidget<?> createTemplatePanelButton(IPanelHandler templatePanel) {
        return new ButtonWidget<>().onMousePressed(_ -> {
            if (!templatePanel.isPanelOpen()) templatePanel.openPanel();
            else templatePanel.closePanel();
            return true;
        })
            .overlay(GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
            .addTooltipLine(GTUtility.translate("GT5U.hatch.chisel.configure.grid"));
    }

    private ModularPanel createTemplatePanel(ModularPanel panel, PanelSyncManager syncManager) {
        ModularPanel templatePanel = GTGuis.createPopUpPanel("template_panel")
            .coverChildren()
            .rightRel(1)
            .padding(4)
            .relative(panel);

        // target phantom slots
        templatePanel.child(
            new ItemSlotGridBuilder(machine.ghostTargets, syncManager).size(getGhostGridSize())
                .hasSlotGroup(false)
                .itemSlotSupplier(PhantomItemSlot::new)
                .build()
                .marginTop(12));

        return templatePanel;
    }
}
