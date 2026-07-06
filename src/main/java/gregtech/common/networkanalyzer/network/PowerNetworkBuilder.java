package gregtech.common.networkanalyzer.network;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.GTValues;
import gregtech.api.graphs.Node;
import gregtech.api.graphs.PowerNode;
import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.consumers.EmptyPowerConsumer;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTETransformer;
import gregtech.api.util.Localized;
import gregtech.common.networkanalyzer.NetworkAnalyzerData;
import gregtech.common.networkanalyzer.NetworkAnalyzerData.AnalyzerModes;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.GraphNodeState;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.OverlayEdge;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.OverlayNode;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.SeverityLevel;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.reactor.IReactorChamber;

public class PowerNetworkBuilder {

    private static class DeadEndEntry {

        public TileEntity currTile;
        public TileEntity nextTile;

        public DeadEndEntry(TileEntity currTile, TileEntity nextTile) {
            this.currTile = currTile;
            this.nextTile = nextTile;
        }

        @Override
        public int hashCode() {
            return currTile.hashCode() * 17 + nextTile.hashCode();
        }

        @Override
        public boolean equals(Object o) {

            if (o instanceof DeadEndEntry other) {
                return this.currTile == other.currTile && this.nextTile == other.nextTile;
            }

            return false;
        }
    }

    static class EnergyResult {

        public long amperage;
        public long voltage;

        public EnergyResult(long amperage, long voltage) {
            this.amperage = amperage;
            this.voltage = voltage;
        }
    }

    private static class FlowResult {

        public long expectedLoss;
        public long actualLoss;
        public long expectedInputAmps;
        public long expectedVoltage;

        public FlowResult(long expectedLoss, long actualLoss, long expectedInputAmps, long expectedVoltage) {
            this.expectedLoss = expectedLoss;
            this.actualLoss = actualLoss;
            this.expectedInputAmps = expectedInputAmps;
            this.expectedVoltage = expectedVoltage;
        }
    }

    static class GraphMetrics {

        public long actualAmperage;
        public long expectedOutputAmps;
        public long expectedInputAmps;
        public long maxAmperage;

        public long actualLoss;
        public long actualVoltage;
        public long expectedLoss;
        public long maxVoltage;

        public SeverityLevel getAmperageLevel() {
            final long amperage = Math.min(this.expectedOutputAmps, this.expectedInputAmps);
            if (this.maxAmperage <= 0) return SeverityLevel.NONE;
            if (amperage <= this.maxAmperage) return SeverityLevel.SUCCESS;
            return SeverityLevel.ALERT;
        }

        public SeverityLevel getLossLevel(boolean useRealValues) {
            final long loss = useRealValues ? this.actualLoss : this.expectedLoss;
            if (this.maxVoltage <= 0) return SeverityLevel.NONE;
            if (loss <= this.maxVoltage / 4) return SeverityLevel.SUCCESS;
            if (loss <= this.maxVoltage / 2) return SeverityLevel.WARNING;
            return SeverityLevel.ALERT;
        }

        public SeverityLevel getVoltageLevel() {
            if (this.maxVoltage <= 0) return SeverityLevel.NONE;
            if (this.actualVoltage <= this.maxVoltage) return SeverityLevel.SUCCESS;
            return SeverityLevel.ALERT;
        }

        public SeverityLevel getMaxLevel(boolean real) {
            final SeverityLevel amperageLevel = getAmperageLevel();
            final SeverityLevel lossLevel = getLossLevel(real);
            final SeverityLevel voltageLevel = getVoltageLevel();
            final SeverityLevel max = amperageLevel.ordinal() >= lossLevel.ordinal() ? amperageLevel : lossLevel;
            return voltageLevel.ordinal() >= max.ordinal() ? voltageLevel : max;
        }

    }

    static class GraphEdge {

        public final GraphMetrics metrics = new GraphMetrics();
        public final List<TileEntity> path = new ArrayList<>();

    }

    private final Map<TileEntity, GraphNode> nodes = new HashMap<>();
    private final Set<DeadEndEntry> deadEndCandidates = new HashSet<>();
    private final Set<List<TileEntity>> deadEndTiles = new LinkedHashSet<>();
    private final Set<TileEntity> visited = new HashSet<>();
    private final Set<TileEntity> flowVisited = new HashSet<>();

    public void addNetwork(PowerNode node) {
        final List<ConsumerNode> consumers = node.mConsumers.stream()
            .filter(c -> !(c instanceof EmptyPowerConsumer) && this.flowVisited.add(c.mTileEntity))
            .collect(Collectors.toList());
        if (consumers.isEmpty()) return;

        processNode(consumers.get(0));

        for (ConsumerNode cNode : consumers) {
            final EnergyResult consumerStats = getPotentialConsumerStats(cNode);

            if (consumerStats != null) {
                this.nodes.get(cNode.mTileEntity).metrics.maxVoltage = consumerStats.voltage;
                propagateFlow(cNode, consumerStats.amperage, new HashSet<>());
            }
        }
    }

    public NetworkAnalyzerData getData(AnalyzerModes mode) {
        NetworkAnalyzerData data = switch (mode) {
            case CRITICAL -> buildCriticalData();
            case ACTIVE -> buildActiveData();
            case AMPERAGE -> buildAmperageData();
            case VOLTAGE -> buildVoltageData();
            case TOPOLOGY -> buildTopologyData();
        };

        if (data.nodes.isEmpty() && data.edges.isEmpty()) {
            data = buildPlaceholderData();
        }

        return data;
    }

    private GraphNode processNode(Node currNode) {
        GraphNode currentGraph = this.nodes.get(currNode.mTileEntity);
        boolean hasTerminalNeighbour = currentGraph != null;

        if (currentGraph == null && this.visited.add(currNode.mTileEntity)) {
            final MTECable currentCable = getMTECable(currNode.mTileEntity);
            currentGraph = new GraphNode(currNode);

            if (currentGraph.state != null) {
                hasTerminalNeighbour = true;
            }

            for (int side = 0; side < 6; side++) {
                final Node neighbourNode = currNode.mNeighbourNodes[side];

                if (neighbourNode == null) {
                    final ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];
                    if (currentCable != null && currentCable.isConnectedAtSide(dir)) {
                        final MTECable metaCable = getMTECable(
                            currentCable.getBaseMetaTileEntity()
                                .getTileEntityAtSide(dir));
                        if (metaCable != null) {
                            this.deadEndCandidates.add(
                                new DeadEndEntry(currNode.mTileEntity, (TileEntity) metaCable.getBaseMetaTileEntity()));
                        }
                    }
                    continue;
                }

                if (this.visited.contains(neighbourNode.mTileEntity)) {
                    continue;
                }

                GraphNode neighbourGraph = null;

                if (neighbourNode instanceof PowerNode) {
                    neighbourGraph = processNode(neighbourNode);
                } else if (this.visited.add(neighbourNode.mTileEntity)) {
                    neighbourGraph = new GraphNode(neighbourNode);

                    if (neighbourGraph.state != null) {
                        this.nodes.put(neighbourNode.mTileEntity, neighbourGraph);
                    } else {
                        neighbourGraph = null;
                    }

                }

                if (neighbourGraph != null) {

                    if (currNode.mNodePaths[side] instanceof PowerNodePath path) {
                        addPathEdge(path, currentGraph, neighbourGraph, neighbourNode, side);
                    } else if (neighbourNode.mSelfPath instanceof PowerNodePath path) {
                        addPathEdge(path, currentGraph, neighbourGraph, neighbourNode, side);
                    }

                    hasTerminalNeighbour = true;
                } else if (currentCable != null && currNode.mNodePaths[side] instanceof PowerNodePath path
                    && path.getPipes().length > 0) {
                        final TileEntity firstPipe = (TileEntity) path.getPipes()[0].getBaseMetaTileEntity();
                        if (firstPipe != null) {
                            this.deadEndCandidates.add(new DeadEndEntry(currNode.mTileEntity, firstPipe));
                        }
                    } else {
                        addDeadEndEdge(currNode.mTileEntity, neighbourNode.mTileEntity);
                    }
            }

            if (hasTerminalNeighbour) {
                this.nodes.put(currNode.mTileEntity, currentGraph);
            }
        }

        return hasTerminalNeighbour ? currentGraph : null;
    }

    private void addPathEdge(PowerNodePath path, GraphNode fromGraph, GraphNode toGraph, Node neighbourNode, int side) {
        final MetaPipeEntity[] pipes = path.getPipes();
        final GraphEdge edge = new GraphEdge();
        TileEntity fromTile = fromGraph.tile;
        TileEntity toTile = toGraph.tile;

        if (pipes.length > 0) {
            final TileEntity firstPipeTile = (TileEntity) pipes[0].getBaseMetaTileEntity();
            if (firstPipeTile == null) return;
            final boolean reversed = Math.abs(fromTile.xCoord - firstPipeTile.xCoord)
                + Math.abs(fromTile.yCoord - firstPipeTile.yCoord)
                + Math.abs(fromTile.zCoord - firstPipeTile.zCoord) != 1;

            if (reversed) {
                fromTile = toGraph.tile;
                toTile = fromGraph.tile;
            }

            edge.path.add(fromTile);
            this.visited.add(fromTile);

            for (MetaPipeEntity pipe : pipes) {
                final TileEntity tile = (TileEntity) pipe.getBaseMetaTileEntity();
                if (tile == null) return;
                edge.path.add(tile);
                this.visited.add(tile);
            }

        } else {
            edge.path.add(fromTile);
            this.visited.add(fromTile);
        }

        edge.path.add(toTile);
        this.visited.add(toTile);

        edge.metrics.actualAmperage = (long) path.getAmperage();
        edge.metrics.maxAmperage = path.getMaxAmperage();
        edge.metrics.maxVoltage = path.getMaxVoltage();
        edge.metrics.actualLoss = path.getLoss();

        if (edge.metrics.maxAmperage == Integer.MAX_VALUE) {
            edge.metrics.maxAmperage = fromGraph.metrics.maxAmperage;
        }

        if (edge.metrics.maxVoltage == Integer.MAX_VALUE) {
            edge.metrics.maxVoltage = fromGraph.metrics.maxVoltage;
        }

        fromGraph.edges[side] = edge;

        for (int i = 0; i < 6; i++) {
            if (neighbourNode.mNodePaths[i] == path) {
                toGraph.edges[i] = edge;
                break;
            }
        }
    }

    private FlowResult propagateFlow(Node node, long amps, Set<TileEntity> propagateVisited) {
        if (!this.nodes.containsKey(node.mTileEntity) || !propagateVisited.add(node.mTileEntity)) return null;

        final GraphNode graphNode = this.nodes.get(node.mTileEntity);
        boolean reachesTarget = graphNode.state == GraphNodeState.INPUT;
        long expectedInputAmps = 0L;
        long expectedVoltage = 0L;
        long expectedLoss = 0L;
        long actualLoss = 0L;

        if (reachesTarget && node.mSelfPath instanceof PowerNodePath selfPath) {
            expectedLoss = Math.max(expectedLoss, selfPath.getLoss());
            actualLoss = Math.max(actualLoss, selfPath.getLoss());
        }

        if (reachesTarget) {
            expectedInputAmps = graphNode.adjacentGeneratorAmps;
            expectedVoltage = graphNode.metrics.actualVoltage;
        }

        for (int side = 0; side < 6; side++) {
            final Node neighbourNode = node.mNeighbourNodes[side];
            if (neighbourNode != null && !propagateVisited.contains(neighbourNode.mTileEntity)) {
                final FlowResult result = propagateFlow(neighbourNode, amps, propagateVisited);

                if (result != null) {
                    final long pathLoss = getPathLossThrough(node, side);
                    final GraphEdge edge = graphNode.edges[side];

                    expectedLoss = Math.max(expectedLoss, pathLoss + result.expectedLoss);
                    actualLoss = Math.max(actualLoss, pathLoss + result.actualLoss);
                    expectedInputAmps += result.expectedInputAmps;
                    expectedVoltage = Math.max(expectedVoltage, result.expectedVoltage);
                    reachesTarget = true;

                    if (edge != null) {
                        edge.metrics.expectedOutputAmps += amps;
                        edge.metrics.expectedLoss = Math.max(edge.metrics.expectedLoss, pathLoss + result.expectedLoss);
                        edge.metrics.actualLoss = Math.max(edge.metrics.actualLoss, pathLoss + result.actualLoss);
                        edge.metrics.expectedInputAmps = result.expectedInputAmps;
                        edge.metrics.actualVoltage = result.expectedVoltage;
                    }
                }
            }
        }

        if (reachesTarget) {
            graphNode.metrics.expectedOutputAmps += amps;
            graphNode.metrics.expectedLoss = Math.max(graphNode.metrics.expectedLoss, expectedLoss);
            graphNode.metrics.actualLoss = Math.max(graphNode.metrics.actualLoss, actualLoss);
            graphNode.metrics.expectedInputAmps = Math.max(graphNode.metrics.expectedInputAmps, expectedInputAmps);
            graphNode.metrics.actualVoltage = Math.max(graphNode.metrics.actualVoltage, expectedVoltage);

            if (graphNode.state == null) {
                graphNode.state = GraphNodeState.FORK;
            }
        }

        return reachesTarget ? new FlowResult(expectedLoss, actualLoss, expectedInputAmps, expectedVoltage) : null;
    }

    private static long getPathLossThrough(Node current, int side) {
        long loss = 0;

        if (current.mSelfPath instanceof PowerNodePath selfPath) {
            loss += selfPath.getLoss();
        }

        if (current.mNodePaths[side] instanceof PowerNodePath path) {
            loss += path.getLoss();
        }

        return loss;
    }

    private void resolveDeadEndCandidates() {
        for (DeadEndEntry entry : this.deadEndCandidates) {
            if (!this.visited.add(entry.nextTile)) {
                continue;
            }

            addDeadEndEdge(entry.currTile, entry.nextTile);
            addDeadEndBranch(entry.nextTile);
        }
    }

    private void addDeadEndBranch(TileEntity startTile) {
        final ArrayDeque<TileEntity> queue = new ArrayDeque<>();
        queue.add(startTile);

        while (!queue.isEmpty()) {
            final TileEntity tile = queue.poll();

            if (tile instanceof BaseMetaPipeEntity currPipe && currPipe.getMetaTileEntity() instanceof MTECable cable) {
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    if (cable.isConnectedAtSide(side)
                        && currPipe.getTileEntityAtSide(side) instanceof BaseMetaPipeEntity nextPipe
                        && nextPipe.getMetaTileEntity() instanceof MTECable) {
                        addDeadEndEdge(tile, nextPipe);

                        if (this.visited.add(nextPipe) && !this.nodes.containsKey(nextPipe)) {
                            queue.add(nextPipe);
                        }
                    }
                }
            }
        }
    }

    private void addDeadEndEdge(TileEntity a, TileEntity b) {
        final int hashA = System.identityHashCode(a);
        final int hashB = System.identityHashCode(b);
        this.deadEndTiles.add(Arrays.asList(hashA <= hashB ? new TileEntity[] { a, b } : new TileEntity[] { b, a }));
    }

    private NetworkAnalyzerData buildTopologyData() {
        final NetworkAnalyzerData data = new NetworkAnalyzerData();

        resolveDeadEndCandidates();

        for (GraphNode node : this.nodes.values()) {
            if (node.state == GraphNodeState.OUTPUT) {
                data.add(createOutputNode(node));
            } else {
                data.add(createOverlayNode(node, null, node.getFullTooltip(false), node.metrics.getMaxLevel(false)));
            }
        }

        forEachUniqueEdge((node, edge) -> addEdgeOverlays(data, edge, edge.metrics.getMaxLevel(false)));

        for (List<TileEntity> pair : this.deadEndTiles) {
            data.add(new OverlayEdge(pair.get(0), pair.get(1), SeverityLevel.NONE));
        }

        return data;
    }

    private NetworkAnalyzerData buildCriticalData() {
        final NetworkAnalyzerData data = new NetworkAnalyzerData();

        for (GraphNode node : this.nodes.values()) {
            if (node.state == GraphNodeState.OUTPUT) {

                if (hasEdgeWith(node, e -> e.metrics.getMaxLevel(false) == SeverityLevel.ALERT)) {
                    data.add(createOutputNode(node));
                }

            } else {
                final SeverityLevel maxLevel = node.metrics.getMaxLevel(false);

                if (maxLevel == SeverityLevel.ALERT) {
                    data.add(
                        createOverlayNode(
                            node,
                            node.getCriticalLabel(),
                            node.getFullTooltip(false),
                            SeverityLevel.ALERT));
                } else if (hasEdgeWith(node, e -> e.metrics.getMaxLevel(false) == SeverityLevel.ALERT)) {
                    final SeverityLevel nodeLevel = node.state == GraphNodeState.FORK ? maxLevel
                        : SeverityLevel.SUCCESS;

                    data.add(createOverlayNode(node, null, node.getFullTooltip(false), nodeLevel));
                }

            }
        }

        forEachUniqueEdge((node, edge) -> {
            final SeverityLevel level = edge.metrics.getMaxLevel(false);
            if (level == SeverityLevel.ALERT) {
                addEdgeOverlays(data, edge, SeverityLevel.ALERT);
            }
        });

        return data;
    }

    private NetworkAnalyzerData buildActiveData() {
        final NetworkAnalyzerData data = new NetworkAnalyzerData();

        for (GraphNode node : this.nodes.values()) {
            if (node.state == GraphNodeState.OUTPUT) {

                if (hasEdgeWith(node, e -> e.metrics.actualAmperage > 0)) {
                    data.add(createOutputNode(node));
                }

            } else if (node.metrics.actualAmperage > 0) {
                final Localized[] label = new Localized[] { node.getShortAmperage(node.metrics.actualAmperage) };
                SeverityLevel level = SeverityLevel.SUCCESS;

                if (node.state == GraphNodeState.FORK || node.metrics.getMaxLevel(true) == SeverityLevel.ALERT) {
                    level = node.metrics.getLossLevel(true);
                }

                data.add(createOverlayNode(node, label, node.getFullTooltip(true), level));
            }
        }

        forEachUniqueEdge((node, edge) -> {
            if (edge.metrics.actualAmperage > 0) {
                addEdgeOverlays(data, edge, edge.metrics.getLossLevel(true));
            }
        });

        return data;
    }

    private NetworkAnalyzerData buildAmperageData() {
        final NetworkAnalyzerData data = new NetworkAnalyzerData();

        for (GraphNode node : this.nodes.values()) {
            if (node.metrics.expectedOutputAmps <= 0) continue;

            if (node.state == GraphNodeState.OUTPUT) {
                data.add(createOutputNode(node));
            } else {
                final Localized[] label = new Localized[] { node.getShortAmperage(node.metrics.expectedOutputAmps) };
                final Localized[] tooltip = new Localized[] { node.getFullAmperage() };
                SeverityLevel level = SeverityLevel.SUCCESS;

                if (node.state == GraphNodeState.FORK || node.metrics.getMaxLevel(false) == SeverityLevel.ALERT) {
                    level = node.metrics.getAmperageLevel();
                }

                data.add(createOverlayNode(node, label, tooltip, level));
            }
        }

        forEachUniqueEdge((node, edge) -> addEdgeOverlays(data, edge, edge.metrics.getAmperageLevel()));

        return data;
    }

    private NetworkAnalyzerData buildVoltageData() {
        final NetworkAnalyzerData data = new NetworkAnalyzerData();

        for (GraphNode node : this.nodes.values()) {
            if (node.state == GraphNodeState.OUTPUT) {

                if (hasEdgeWith(node, e -> e.metrics.maxVoltage > 0)) {
                    data.add(createOutputNode(node));
                }

            } else if (node.metrics.maxVoltage > 0) {
                final long lossPercent = node.metrics.expectedLoss * 100 / node.metrics.maxVoltage;
                final SeverityLevel voltageLevel = node.metrics.getVoltageLevel();
                final SeverityLevel lossLevel = node.metrics.getLossLevel(false);
                final SeverityLevel maxLevel = voltageLevel.ordinal() >= lossLevel.ordinal() ? voltageLevel : lossLevel;
                final List<Localized> tooltip = new ArrayList<>();
                SeverityLevel level = SeverityLevel.SUCCESS;
                Localized label;

                if (node.state == GraphNodeState.FORK || maxLevel == SeverityLevel.ALERT) {
                    level = maxLevel;
                }

                tooltip.add(new Localized("gt.NetworkAnalyzer.loss.full", lossPercent, node.metrics.expectedLoss));

                if (voltageLevel == SeverityLevel.ALERT) {
                    label = node.getVoltageLabel();
                    tooltip.add(node.getVoltageDetails());
                } else {
                    label = new Localized("gt.NetworkAnalyzer.loss.short", lossPercent);
                }

                data.add(createOverlayNode(node, new Localized[] { label }, tooltip.toArray(new Localized[0]), level));
            }
        }

        forEachUniqueEdge((node, edge) -> {
            final SeverityLevel voltageLevel = edge.metrics.getVoltageLevel();
            final SeverityLevel lossLevel = edge.metrics.getLossLevel(false);
            addEdgeOverlays(data, edge, voltageLevel.ordinal() >= lossLevel.ordinal() ? voltageLevel : lossLevel);
        });

        return data;
    }

    private NetworkAnalyzerData buildPlaceholderData() {
        final NetworkAnalyzerData data = new NetworkAnalyzerData();

        for (GraphNode graphNode : this.nodes.values()) {
            if (graphNode.state == GraphNodeState.OUTPUT) {
                data.add(new OverlayNode(graphNode.tile, null, SeverityLevel.NONE, null, null));
            } else if (graphNode.state != null) {
                data.add(
                    new OverlayNode(graphNode.tile, null, SeverityLevel.NONE, null, graphNode.getFullTooltip(false)));
            }
        }

        forEachUniqueEdge((node, edge) -> {
            for (int i = 1; i < edge.path.size(); i++) {
                data.add(new OverlayEdge(edge.path.get(i - 1), edge.path.get(i), SeverityLevel.NONE));
            }
        });

        return data;
    }

    private static void addEdgeOverlays(NetworkAnalyzerData data, GraphEdge edge, SeverityLevel level) {
        final List<TileEntity> edgeTiles = edge.path;
        for (int i = 1; i < edgeTiles.size(); i++) {
            data.add(new OverlayEdge(edgeTiles.get(i - 1), edgeTiles.get(i), level));
        }
    }

    private static OverlayNode createOutputNode(GraphNode node) {
        final SeverityLevel voltageLevel = node.metrics.getVoltageLevel();
        final List<Localized> tooltip = new ArrayList<>();
        Localized[] label = null;

        if (voltageLevel == SeverityLevel.ALERT) {
            label = new Localized[] { node.getVoltageLabel() };
        }

        if (node.metrics.getVoltageLevel() == SeverityLevel.ALERT) {
            tooltip.add(node.getVoltageDetails());
        }

        return new OverlayNode(
            node.tile,
            node.state,
            voltageLevel == SeverityLevel.NONE ? SeverityLevel.SUCCESS : voltageLevel,
            label,
            tooltip.isEmpty() ? null : tooltip.toArray(new Localized[0]));
    }

    private static OverlayNode createOverlayNode(GraphNode node, Localized[] label, Localized[] tooltip,
        SeverityLevel level) {
        return new OverlayNode(node.tile, node.state, level, label, tooltip);
    }

    private void forEachUniqueEdge(BiConsumer<GraphNode, GraphEdge> consumer) {
        final Set<GraphEdge> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        for (GraphNode node : this.nodes.values()) {
            for (int side = 0; side < 6; side++) {
                if (node.edges[side] != null && seen.add(node.edges[side])) {
                    consumer.accept(node, node.edges[side]);
                }
            }
        }
    }

    private boolean hasEdgeWith(GraphNode node, Predicate<GraphEdge> condition) {

        for (int side = 0; side < 6; side++) {
            final GraphEdge edge = node.edges[side];
            if (edge != null && condition.test(edge)) {
                return true;
            }
        }

        return false;
    }

    static EnergyResult getPotentialGeneratorStats(Node node) {
        final Set<IEnergySource> countedEmitters = Collections.newSetFromMap(new IdentityHashMap<>());
        final MTECable cable = getMTECable(node.mTileEntity);
        long totalAmperage = 0L;
        long maxVoltage = 0L;

        if (cable != null) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (node.mNeighbourNodes[side.ordinal()] == null && cable.isConnectedAtSide(side)) {
                    final TileEntity sideEntity = cable.getBaseMetaTileEntity()
                        .getTileEntityAtSide(side);

                    if (sideEntity != null && !(sideEntity instanceof BaseMetaPipeEntity)) {

                        if (sideEntity instanceof BaseMetaTileEntity metaTile) {
                            long outputAmperage = metaTile.getMetaTileEntity() instanceof MetaTileEntity metaTileEntity
                                ? metaTileEntity.maxAmperesOut()
                                : metaTile.getOutputAmperage();
                            long outputVoltage = metaTile.getOutputVoltage();
                            if (outputAmperage > 0 && outputAmperage < Integer.MAX_VALUE && outputVoltage > 0) {
                                totalAmperage += outputAmperage;
                                maxVoltage = Math.max(maxVoltage, outputVoltage);
                            }
                        } else {
                            final IEnergySource emitter = sideEntity instanceof IReactorChamber chamber
                                && chamber.getReactor() instanceof IEnergySource reactorEmitter ? reactorEmitter
                                    : sideEntity instanceof IEnergySource sideEmitter ? sideEmitter : null;

                            if (emitter != null && countedEmitters.add(emitter)) {
                                final long amps = Math.max(1L, (long) (emitter.getOfferedEnergy() / cable.mVoltage));
                                totalAmperage += amps;
                                maxVoltage = cable.mVoltage;
                            }
                        }

                    }
                }
            }
        }

        if (totalAmperage > 0) {
            return new EnergyResult(totalAmperage, maxVoltage);
        }

        return null;
    }

    private static EnergyResult getPotentialConsumerStats(ConsumerNode consumer) {

        if (consumer.mTileEntity instanceof BaseMetaTileEntity baseMeta) {
            long voltage = baseMeta.getInputVoltage();
            long amps = 0L;

            if (baseMeta.getMetaTileEntity() instanceof MTETransformer transformer) {
                final long vIn = transformer.maxEUInput();
                final long vOut = transformer.maxEUOutput();
                if (vIn > 0) {
                    amps = Math.max(0L, transformer.maxAmperesOut() * vOut / vIn);
                }
                voltage = vIn;
            } else if (baseMeta.getMetaTileEntity() instanceof MTEHatch hatch) {
                amps = Math.max(0L, hatch.maxWorkingAmperesIn());
            } else if (baseMeta.getMetaTileEntity() instanceof MetaTileEntity metaTile) {
                amps = Math.max(0L, metaTile.maxAmperesIn());
            }

            if (amps <= 0) {
                amps = Math.max(0L, baseMeta.getInputAmperage());
            }

            if (amps <= 0 && voltage > 0) {
                amps = 1L;
            }

            if (amps >= Integer.MAX_VALUE) {
                amps = 1L;
            }

            return new EnergyResult(amps, voltage);
        }

        long inferredAmperage = 0L;
        long inferredVoltage = 0L;

        for (int side = 0; side < 6; side++) {
            if (consumer.mNodePaths[side] instanceof PowerNodePath path) {
                final long pathAmps = path.getMaxAmperage() >= Integer.MAX_VALUE ? 0L : path.getMaxAmperage();
                if (pathAmps > inferredAmperage) {
                    inferredAmperage = pathAmps;
                    inferredVoltage = path.getMaxVoltage() >= Integer.MAX_VALUE ? 0L : path.getMaxVoltage();
                }
            }
        }

        if (inferredVoltage > 0 && inferredAmperage > 0) {
            return new EnergyResult(inferredAmperage, inferredVoltage);
        }

        long voltage = 0L;
        if (consumer.mTileEntity instanceof IEnergySink sink) {
            final int tier = sink.getSinkTier();
            if (tier >= 0 && tier < GTValues.V.length) {
                voltage = GTValues.V[tier];
            }
        }

        return new EnergyResult(1L, voltage);
    }

    private static MTECable getMTECable(TileEntity tile) {

        if (tile instanceof BaseMetaPipeEntity metaEntity && metaEntity.getMetaTileEntity() instanceof MTECable cable) {
            return cable;
        }

        return null;
    }
}
