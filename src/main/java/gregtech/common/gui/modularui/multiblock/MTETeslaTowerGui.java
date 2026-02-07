package gregtech.common.gui.modularui.multiblock;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.mojang.realmsclient.util.Pair;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.gui.ItemDisplayKey;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import gregtech.common.gui.modularui.synchandler.TeslaNodeData;
import gregtech.common.gui.modularui.synchandler.TeslaNodeListSyncHandler;
import gregtech.common.gui.modularui.widget.LineChartWidget;
import tectech.thing.metaTileEntity.multi.MTETeslaTower;

public class MTETeslaTowerGui extends TTMultiblockBaseGui<MTETeslaTower> {

    private Map<Vec3Impl, Map<Vec3Impl, Integer>> chunkToAmpsMap = new HashMap<>();
    private final int gridChunkRadius = 8;
    private final int gridChunkSize = gridChunkRadius * 2 + 1;
    private final int gridSquareSize = 8;

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
                IKey.lang("gt.blockmachines.multimachine.tm.teslaCoil.outputVoltage")
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
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(createChartButton(syncManager, parent))
            .child(createHeatMapButton(syncManager, parent));
    }

    @Override
    protected boolean shouldDisplayVoidExcess() {
        return false;
    }

    @Override
    protected boolean shouldDisplayInputSeparation() {
        return false;
    }

    @Override
    protected boolean shouldDisplayBatchMode() {
        return false;
    }

    @Override
    protected boolean shouldDisplayRecipeLock() {
        return false;
    }

    private IWidget createChartButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler chartPanel = syncManager.syncedPanel("chart", true,(a, b) -> openChartPanel(syncManager, parent));
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
        }.size(this.getBasePanelWidth(), this.getBasePanelHeight())
            .relative(parent)
            .leftRel(0, 0, 0)
            .padding(4)
            .child(
                Flow.column()
                    .sizeRel(1)
                    .child(createChartWidget())
                    .child(createChartEditColumn()));
    }

    private IWidget createChartWidget() {
        double specialThemeRate = 0.2;
        double roll = Math.random();
        return Flow.column()
            .expanded()
            .child(
                new LineChartWidget()
                    .syncHandler(
                        new GenericListSyncHandler<>(
                            multiblock::getOutputCurrentHistory,
                            null,
                            PacketBuffer::readDouble,
                            PacketBuffer::writeDouble,
                            Double::equals,
                            null))
                    .widgetTheme(
                        roll < specialThemeRate ? GTWidgetThemes.TESLA_TOWER_CHART_SPECIAL
                            : GTWidgetThemes.TESLA_TOWER_CHART)
                    .chartUnit("A")
                    .formatter(new DecimalFormat("0.0#"))
                    .marginBottom(2)
                    .sizeRel(1));
    }

    private IWidget createChartEditColumn() {
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
            .syncedPanel("heatMap", true,(a, b) -> openHeatMapPanel(syncManager, parent));
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
        int borderRadius = 4;
        return new ModularPanel("heatMap") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.relative(parent)
            .topRel(0)
            .rightRel(1, 0, 0)
            .size(gridSquareSize * gridChunkSize + borderRadius * 2)
            .child(createNodeGrid(syncManager, borderRadius));
    }

    private IWidget createNodeGrid(PanelSyncManager syncManager, int borderRadius) {
        List<List<Widget<?>>> matrix = new ArrayList<>();

        for (int i = 0; i < gridChunkSize; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < gridChunkSize; j++) {
                matrix.get(i)
                    .add(createMapSlot(syncManager, i, j).size(gridSquareSize));
            }
        }

        return new Grid().matrix(matrix)
            .size(gridSquareSize * gridChunkSize)
            .marginTop(borderRadius)
            .marginLeft(borderRadius);
    }

    private Widget<?> createMapSlot(PanelSyncManager syncManager, int gridX, int gridY) {

        return new DynamicDrawable(() -> getDrawableAt(syncManager, gridX, gridY)).asWidget()
            .overlay(this.emptyRectangle())
            .size(gridSquareSize)
            .tooltipDynamic(t -> addTooltip(t, gridX, gridY))
            .tooltipAutoUpdate(true);
    }

    private IDrawable emptyRectangle() {
        return (context, x, y, width, height, widgetTheme) -> {
            GL11.glEnable(GL_DEPTH_TEST);
            final Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(GL_LINE_STRIP);
            tessellator.setColorOpaque(0, 0, 0);

            int z = context.getCurrentDrawingZ() + 1;
            tessellator.addVertex(x, y, z);
            tessellator.addVertex(x + width, y, z);
            tessellator.addVertex(x + width, y + height, z);
            tessellator.addVertex(x, y + height, z);
            tessellator.addVertex(x, y, z);
            tessellator.draw();

            GL11.glDisable(GL_DEPTH_TEST);

        };
    }

    private IDrawable getDrawableAt(PanelSyncManager syncManager, int gridX, int gridY) {
        Map<Vec3Impl, Integer> ampsAtChunk = chunkToAmpsMap.get(new Vec3Impl(gridX, 0, gridY));
        boolean isGridCenter = gridX == gridChunkRadius && gridY == gridChunkRadius;

        if (ampsAtChunk == null) {
            return isGridCenter ? new Rectangle().color(Color.BLACK.main)
                .asIcon()
                .size(gridSquareSize) : IDrawable.EMPTY;
        }
        int usedAmps = ampsAtChunk.values()
            .stream()
            .reduce(Integer::sum)
            .orElse(0);

        LongSyncValue maxCurrentSyncer = syncManager.findSyncHandler("maxCurrent", LongSyncValue.class);
        long maxAmps = maxCurrentSyncer.getValue();
        double percentage = (double) usedAmps / maxAmps;

        int color;
        if (percentage < 0.1) {
            color = Color.GREEN.main;
        } else if (percentage < 0.25) {
            color = Color.YELLOW.main;
        } else {
            color = Color.RED.main;
        }

        return new Rectangle().color(color)
            .asIcon()
            .size(gridSquareSize);
    }

    private void addTooltip(RichTooltip t, int gridX, int gridY) {
        IGregTechTileEntity base = multiblock.getBaseMetaTileEntity();
        World world = base.getWorld();

        Chunk multiblockChunk = world.getChunkFromBlockCoords(base.getXCoord(), base.getZCoord());
        int chunkX = multiblockChunk.xPosition + gridX - gridChunkRadius;
        int chunkZ = multiblockChunk.zPosition + gridY - gridChunkRadius;
        t.addLine(String.format("Chunk: %d, %d", chunkX, chunkZ));

        boolean isGridCenter = gridX == gridChunkRadius && gridY == gridChunkRadius;
        if (isGridCenter) {
            t.addLine("Tesla Tower here!");
        }

        Map<Vec3Impl, Integer> ampsAtChunk = chunkToAmpsMap.get(new Vec3Impl(gridX, 0, gridY));
        if (ampsAtChunk == null) {
            return;
        }
        int totalAmps = ampsAtChunk.values()
            .stream()
            .reduce(Integer::sum)
            .orElse(0);
        t.addLine(totalAmps + "A");

        Map<ItemDisplayKey, Pair<Integer, Integer>> machineGroupings = new HashMap<>();
        ampsAtChunk.forEach((coords, amps) -> {
            TileEntity tileEntity = world.getTileEntity(coords.get0(), coords.get1(), coords.get2());
            // This should never happen but who knows?
            if (!(tileEntity instanceof IGregTechTileEntity igte)) {
                return;
            }
            ItemStack itemStack = new ItemStack(tileEntity.blockType, 1, igte.getMetaTileID());
            ItemDisplayKey key = new ItemDisplayKey(itemStack.getItem(), itemStack.getItemDamage(), null);
            Pair<Integer, Integer> machineGroup = machineGroupings.computeIfAbsent(key, k -> Pair.of(0, 0));
            Pair<Integer, Integer> newMachineGroup = Pair.of(machineGroup.first() + 1, machineGroup.second() + amps);
            machineGroupings.put(key, newMachineGroup);
        });

        machineGroupings.forEach((item, pair) -> {
            t.add(new ItemDrawable(new ItemStack(item.item(), 1, item.damage())))
                .add(" x" + pair.first() + " = " + pair.second() + "A\n");
        });

    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        TeslaNodeListSyncHandler teslaNodeSyncer = new TeslaNodeListSyncHandler(
            this::getTeslaNodes,
            this::setTeslaNodes);
        syncManager.syncValue("teslaNodes", teslaNodeSyncer);
        syncManager.syncValue(
            "outputCurrentMax",
            new LongSyncValue(multiblock::getOutputCurrentMax, multiblock::setOutputCurrentMax));
    }

    private List<TeslaNodeData> getTeslaNodes() {
        List<TeslaNodeData> result = new ArrayList<>();
        multiblock.getAmpsLastTickMap()
            .forEach((node, amps) -> {
                Vec3Impl coords = node.getTeslaPosition();
                result.add(new TeslaNodeData(coords, amps));
            });
        return result;
    }

    private void setTeslaNodes(List<TeslaNodeData> teslaNodeData) {
        IGregTechTileEntity base = multiblock.getBaseMetaTileEntity();
        World world = base.getWorld();

        // X,Z Chunk to machineCoords to Amps used
        Map<Vec3Impl, Map<Vec3Impl, Integer>> newMap = new HashMap<>();

        Chunk multiblockChunk = world.getChunkFromBlockCoords(base.getXCoord(), base.getZCoord());
        for (TeslaNodeData node : teslaNodeData) {
            Vec3Impl coords = node.getCoords();
            Chunk nodeChunk = world.getChunkFromBlockCoords(coords.get0(), coords.get2());
            int mapOffsetX = multiblockChunk.xPosition - nodeChunk.xPosition + gridChunkRadius;
            int mapOffsetZ = multiblockChunk.zPosition - nodeChunk.zPosition + gridChunkRadius;
            newMap.computeIfAbsent(new Vec3Impl(mapOffsetX, 0, mapOffsetZ), k -> new HashMap<>())
                .put(coords, node.getUsedAmps());
        }
        chunkToAmpsMap = newMap;
    }
}
