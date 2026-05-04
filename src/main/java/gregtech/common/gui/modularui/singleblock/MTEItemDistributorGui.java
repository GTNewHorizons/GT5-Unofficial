package gregtech.common.gui.modularui.singleblock;

import java.util.Arrays;
import java.util.stream.IntStream;

import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.ByteSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTEBufferBaseGui;
import gregtech.common.tileentities.automation.MTEItemDistributor;

public class MTEItemDistributorGui extends MTEBufferBaseGui<MTEItemDistributor> {

    public MTEItemDistributorGui(MTEItemDistributor machine) {
        super(machine);
    }

    @Override
    protected boolean supportsSortStacks() {
        return false;
    }

    @Override
    protected boolean supportsStocking() {
        return false;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        String[] matrix = new String[3];
        Arrays.fill(matrix, "sssssssss");

        return super.createContentSection(panel, syncManager).child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key(
                    's',
                    index -> new ItemSlot()
                        .slot(new ModularSlot(machine.inventoryHandler, index).slotGroup("item_inv")))
                .build()
                .horizontalCenter());
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        IPanelHandler sideConfigPanel = syncManager
            .syncedPanel("sideConfigPanel", true, (p_syncManager, syncHandler) -> openSideConfigPanel(panel));

        return super.createLeftCornerFlow(panel, syncManager).child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            if (!sideConfigPanel.isPanelOpen()) sideConfigPanel.openPanel();
            else sideConfigPanel.closePanel();
            return true;
        })
            .overlay(GuiTextures.GEAR)
            .tooltip(t -> t.addLine(GTUtility.translate("GT5U.machines.item_distributor.open_config_panel.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY))
            .child(
                createArrow(GTGuiTextures.PICTURE_ARROW_22_RED, 68, 22, true).marginBottom(1)
                    .marginLeft(1));
    }

    private ModularPanel openSideConfigPanel(ModularPanel parent) {
        ModularPanel panel = GTGuis.createPopUpPanel("sideConfigPanel")
            .relative(parent)
            .size(120, 150)
            .topRel(0)
            .leftRel(1);

        Flow textColumn = Flow.column()
            .coverChildren()
            .childPadding(2)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .marginTop(5)
            .marginLeft(5);

        // header
        textColumn.child(
            IKey.lang("GT5U.machines.item_distributor.config_panel")
                .asWidget()
                .marginBottom(10));

        // config rows
        IntStream.range(0, 6)
            .forEach(index -> textColumn.child(createConfigRow(index)));

        panel.child(textColumn);

        return panel;
    }

    private Flow createConfigRow(int index) {
        ByteSyncValue itemsPerSideSyncer = new ByteSyncValue(
            () -> machine.getItemsPerSide(index),
            val -> machine.setItemsPerSide(index, val));

        Flow configRow = Flow.row()
            .coverChildren()
            .childPadding(3);

        // side label
        configRow.child(
            IKey.str(
                ForgeDirection.getOrientation(index)
                    .toString())
                .asWidget()
                .width(30));

        // text field
        configRow.child(
            new TextFieldWidget().width(35)
                .value(itemsPerSideSyncer)
                .setNumbers(0, 127)
                .setMaxLength(3));

        // front/back label
        configRow.childIf(
            ForgeDirection.getOrientation(index) == baseMetaTileEntity.getFrontFacing(),
            () -> IKey.str("(Front)")
                .asWidget());
        configRow.childIf(
            ForgeDirection.getOrientation(index) == baseMetaTileEntity.getBackFacing(),
            () -> IKey.str("(Back)")
                .asWidget());

        return configRow;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.registerSlotGroup("item_inv", 3);
    }
}
