package gregtech.common.gui.modularui.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.synchandler.TeslaNodeData;
import gregtech.common.gui.modularui.synchandler.TeslaNodeListSyncHandler;
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
        return super.createPanelGap(parent, syncManager).child(createChartButton(syncManager, parent))
            .child(createHeatMapButton(syncManager, parent));
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
        return new ModularPanel("chart") {

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
                    .child(createChartWidget())
                    .child(createChartEditRow()));
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
            .renderTextureWithAlpha(0.05f)
            .chartUnit("A")
            .marginBottom(2);
    }

    private IWidget createChartEditRow() {
        return Flow.column()
            .widthRel(1)
            .coverChildrenHeight()
            .child(createTickRateRow())
            .child(createDataLimitRow());
    }

    private IWidget createTickRateRow() {
        return Flow.row()
            .widthRel(1)
            .coverChildrenHeight()
            .child(
                IKey.str("Update chart every")
                    .asWidget()
                    .marginRight(4))
            .child(
                new TextFieldWidget()
                    .value(
                        new IntSyncValue(multiblock::getTicksBetweenDataPoints, multiblock::setTicksBetweenDataPoints))
                    .size(25, 12)
                    .marginRight(4))
            .child(
                IKey.str("tick(s)")
                    .asWidget());
    }

    private IWidget createDataLimitRow() {
        return Flow.row()
            .widthRel(1)
            .coverChildrenHeight()
            .child(
                IKey.str("Show last")
                    .asWidget()
                    .marginRight(4))
            .child(
                new TextFieldWidget().value(new IntSyncValue(multiblock::getHistorySize, multiblock::setHistorySize))
                    .size(25, 12)
                    .marginRight(4))
            .child(
                IKey.str("measurement(s)")
                    .asWidget());
    }

    private IWidget createHeatMapButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler heatMapPanel = syncManager
            .panel("heatMap", (a, b) -> openHeatMapPanel(syncManager, parent), true);
        return new ButtonWidget<>().onMousePressed(d -> {
            if (!heatMapPanel.isPanelOpen()) {
                heatMapPanel.openPanel();
            } else {
                heatMapPanel.closePanel();
            }
            return true;
        })
            .tooltip(t -> t.addLine(IKey.lang("gt.blockmachines.multimachine.tm.teslaCoil.heatMapButtonTooltip")))
            .overlay(
                GTGuiTextures.OVERLAY_BUTTON_TESLA_TOWER_HEAT_MAP.asIcon()
                    .size(16));
    }

    private @NotNull ModularPanel openHeatMapPanel(PanelSyncManager syncManager, ModularPanel parent) {
        return new ModularPanel("heatMap").relative(parent)
            .topRel(0f)
            .leftRel(0f)
            .size(17 * 8, 17 * 8)
            .child(createNodeGrid(syncManager));
    }

    private IWidget createNodeGrid(PanelSyncManager syncManager) {
        TeslaNodeListSyncHandler teslaNodeListSyncer = syncManager
            .findSyncHandler("teslaNodes", TeslaNodeListSyncHandler.class);
        List<TeslaNodeData> nodes = teslaNodeListSyncer.getValue();
        IGregTechTileEntity base = multiblock.getBaseMetaTileEntity();
        World world = base.getWorld();

        int gridChunkRadius = 8;
        Chunk multiblockChunk = world.getChunkFromBlockCoords(base.getXCoord(), base.getZCoord());
        // X,Z Chunk to amps used
        Map<Vec3Impl, Integer> chunkToAmpMap = new HashMap<>();

        for (TeslaNodeData node : nodes) {
            Vec3Impl coords = node.getCoords();
            Chunk nodeChunk = world.getChunkFromBlockCoords(coords.get0(), coords.get2());
            int mapOffsetX = multiblockChunk.xPosition - nodeChunk.xPosition + gridChunkRadius;
            int mapOffsetZ = multiblockChunk.zPosition - nodeChunk.zPosition + gridChunkRadius;
            chunkToAmpMap.merge(new Vec3Impl(mapOffsetX, 0, mapOffsetZ), node.getUsedAmps(), Integer::sum);
        }
        Flow column = Flow.column()
            .sizeRel(1);

        chunkToAmpMap.forEach((chunk, amps) -> {
            column.child(
                new Rectangle().setColor(Color.GREEN.main)
                    .asIcon()
                    .size(8)
                    .asWidget()
                    .overlay()
                    .pos(chunk.get0() * 8, chunk.get2() * 8)
                    .size(8)
                    .tooltip(t -> t.addLine(amps + "A"))
                    .tooltipAutoUpdate(true));
        });

        return column;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        TeslaNodeListSyncHandler teslaNodeSyncer = new TeslaNodeListSyncHandler(() -> {
            List<TeslaNodeData> result = new ArrayList<>();
            multiblock.getAmpsLastTickMap()
                .forEach((node, amps) -> {
                    Vec3Impl coords = node.getTeslaPosition();
                    result.add(new TeslaNodeData(coords, amps));
                });
            return result;
        });
        syncManager.syncValue("teslaNodes", teslaNodeSyncer);
    }
}
