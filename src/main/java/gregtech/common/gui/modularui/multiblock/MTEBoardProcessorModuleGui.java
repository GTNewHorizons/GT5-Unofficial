package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.drawable.IRichTextBuilder;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;

import cpw.mods.fml.relauncher.Side;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTEBoardProcessorModule;

public class MTEBoardProcessorModuleGui extends MTEMultiBlockBaseGui<MTEBoardProcessorModule> {

    public MTEBoardProcessorModuleGui(MTEBoardProcessorModule multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("processedItems", new IntSyncValue(multiblock::getProcessedItems));
        syncManager.syncValue("impurity", new DoubleSyncValue(multiblock::getImpurityPercentage));

        syncManager.registerSyncedAction("fillTank", Side.SERVER, p -> multiblock.FillTank());
        syncManager.registerSyncedAction("flushTank", Side.SERVER, p -> multiblock.FlushTank());

    }

    @Override
    protected ParentWidget<?> createTerminalParentWidget(ModularPanel panel, PanelSyncManager syncManager) {

        final FluidStackTank fluidTank = new FluidStackTank(
            multiblock::getStoredFluid,
            multiblock::setStoredFluid,
            multiblock::getCapacity);

        final FluidStackTank impurityFluidTank = new FluidStackTank(
            multiblock::getImpurityFluid,
            multiblock::setImpurityFluid,
            multiblock::getCapacity);

        final FluidSlot fluidSlot = new FluidSlot().syncHandler(
            new FluidSlotSyncHandler(fluidTank).canFillSlot(false)
                .controlsAmount(false))
            .alwaysShowFull(false)
            .size(36, 94)
            .tooltipBuilder(IRichTextBuilder::clearText);

        final FluidSlot impurityFluidSlot = new FluidSlot()
            .syncHandler(
                new FluidSlotSyncHandler(impurityFluidTank).canFillSlot(false)
                    .controlsAmount(false))
            .alwaysShowFull(false)
            .size(36, 94)
            .tooltipBuilder(IRichTextBuilder::clearText)
            .pos(376, 71);

        return new Row().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
            .paddingTop(4)
            .paddingBottom(4)
            .paddingLeft(4)
            .paddingRight(0)
            .childPadding(3)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .child(
                createTerminalTextWidget(syncManager, panel)
                    .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                    .collapseDisabledChild())
            .childIf(
                multiblock.supportsTerminalRightCornerColumn(),
                createTerminalRightCornerColumn(panel, syncManager))
            .childIf(multiblock.supportsTerminalLeftCornerColumn(), createTerminalLeftCornerColumn(panel, syncManager))
            .child(fluidSlot)
            .child(impurityFluidSlot);
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {

        return super.createLeftPanelGapRow(parent, syncManager).child(
            new ButtonWidget<>().size(18, 18)
                .playClickSound(true)
                .onMousePressed(d -> {
                    syncManager.callSyncedAction("fillTank", $ -> {});
                    return false;
                })
                .tooltipBuilder(t -> t.addLine("Fill"))
                .tooltipShowUpTimer(TOOLTIP_DELAY))
            .child(
                new ButtonWidget<>().size(18, 18)
                    .playClickSound(true)
                    .onMousePressed(d -> {
                        syncManager.callSyncedAction("flushTank", $ -> {});
                        return false;
                    })
                    .tooltipBuilder(t -> t.addLine("Flush"))
                    .tooltipShowUpTimer(TOOLTIP_DELAY));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {

        IntSyncValue processedItems = syncManager.findSyncHandler("processedItems", IntSyncValue.class);
        DoubleSyncValue impurity = syncManager.findSyncHandler("impurity", DoubleSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(new TextWidget<>(IKey.dynamic(processedItems::getStringValue)))
            .child(new TextWidget<>(IKey.dynamic(impurity::getStringValue)));
    }

    @Override
    protected int getTerminalWidgetWidth() {
        return 148;
    }

}
