package gregtech.common.gui.modularui.multiblock;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.widget.LineChartWidget;
import tectech.thing.metaTileEntity.multi.MTETeslaTower;

public class MTETeslaTowerGui extends TTMultiblockBaseGui<MTETeslaTower> {

    public MTETeslaTowerGui(MTETeslaTower multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.createTerminalTextWidget(syncManager, parent).child(createOutputVoltageText(syncManager))
            .child(createOutputCurrentWidget(syncManager));
    }

    private IWidget createOutputVoltageText(PanelSyncManager syncManager) {
        LongSyncValue voltageSyncer = new LongSyncValue(multiblock::getTeslaOutputVoltage);
        syncManager.syncValue("voltage", voltageSyncer);
        return Flow.row()
            .coverChildren()
            .child(
                IKey.lang(GTUtility.translate("gt.blockmachines.multimachine.tm.teslaCoil.outputVoltage"))
                    .asWidget()
                    .color(Color.WHITE.main))
            .child(
                IKey.dynamic(
                    () -> " " + voltageSyncer.getValue()
                        .toString())
                    .asWidget()
                    // Minecraft &a equivalent
                    .color(0x55FF55));
    }

    private IWidget createOutputCurrentWidget(PanelSyncManager syncManager) {
        LongSyncValue currentSyncer = new LongSyncValue(multiblock::getOutputCurrentLastTick);
        LongSyncValue maxCurrentSyncer = new LongSyncValue(multiblock::getTeslaOutputCurrent);
        syncManager.syncValue("current", currentSyncer);
        syncManager.syncValue("maxCurrent", maxCurrentSyncer);
        return new ProgressWidget()
            .progress(() -> (double) currentSyncer.getValue() / Math.max(1, maxCurrentSyncer.getValue()))
            .texture(GTGuiTextures.PICTURE_TRANSPARENT, GTGuiTextures.PROGRESSBAR_TESLA_TOWER_CURRENT, 100)
            .widthRel(0.75f)
            .height(9)
            .tooltipDynamic(
                t -> t.addLine(
                    String.format("Output current: %d/%d", currentSyncer.getValue(), maxCurrentSyncer.getValue())))
            .tooltipAutoUpdate(true);
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createPanelGap(parent, syncManager).child(createChartButton(syncManager, parent));
    }

    private IWidget createChartButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler chartPanel = syncManager.panel("chart", (a, b) -> openChartPanel(syncManager, parent), true);
        return new ButtonWidget<>().onMousePressed(d -> {
            if (!chartPanel.isPanelOpen()) {
                chartPanel.openPanel();
            } else {
                chartPanel.closePanel();
            }
            return true;
        })
            .tooltip(t -> t.addLine(IKey.lang("gt.blockmachines.multimachine.tm.teslaCoil.chartButtonTooltip")))
            .overlay(
                GTGuiTextures.OVERLAY_BUTTON_TESLA_TOWER_CHART.asIcon()
                    .size(16));
    }

    private @NotNull ModularPanel openChartPanel(PanelSyncManager syncManager, ModularPanel parent) {
        return new ModularPanel("chart"){
            @Override
            public boolean isDraggable() {
                return false;
            }
        }.coverChildren()
            .relative(parent)
            .topRel(0)
            .rightRel(1, 0, 0)
            .padding(4)
            .child(
                Flow.column()
                    .coverChildren()
                    .child(createChartWidget()));
    }

    private IWidget createChartWidget() {
        return new LineChartWidget()
            .syncHandler(
                new GenericListSyncHandler<>(
                    multiblock::getOutputCurrentHistory,
                    null,
                    PacketBuffer::readDouble,
                    PacketBuffer::writeDouble,
                    Double::equals,
                    null))
            .size(225, 150)
            .widgetTheme(GTWidgetThemes.TESLA_TOWER_CHART)
            .renderTextureWithAlpha()
            .chartUnit("A")
            .dataPointLimit(30);
    }

}
