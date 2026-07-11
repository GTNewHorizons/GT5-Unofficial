package gregtech.common.networkanalyzer.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;

import gregtech.api.graphs.Node;
import gregtech.api.graphs.PowerNode;
import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.consumers.EmptyPowerConsumer;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.util.Localized;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.GraphNodeState;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.SeverityLevel;
import gregtech.common.networkanalyzer.network.PowerNetworkBuilder.EnergyResult;
import gregtech.common.networkanalyzer.network.PowerNetworkBuilder.GraphEdge;
import gregtech.common.networkanalyzer.network.PowerNetworkBuilder.GraphMetrics;

class GraphNode {

    public final GraphMetrics metrics = new GraphMetrics();
    public final TileEntity tile;
    public GraphNodeState state;
    public long adjacentGeneratorAmps;

    public final GraphEdge[] edges = new GraphEdge[6];

    public GraphNode(Node node) {
        this.tile = node.mTileEntity;

        if (node instanceof ConsumerNode consumer) {
            initAsOutput(consumer);
        } else {
            initAsInput(node);
        }
    }

    private void initAsInput(Node node) {
        final EnergyResult generatorStats = PowerNetworkBuilder.getPotentialGeneratorStats(node);

        if (generatorStats != null) {
            this.state = GraphNodeState.INPUT;
            this.adjacentGeneratorAmps = generatorStats.amperage;
            this.metrics.expectedInputAmps = generatorStats.amperage;
            this.metrics.actualVoltage = generatorStats.voltage;
        }

        if (node.mSelfPath instanceof PowerNodePath p) {
            this.metrics.actualAmperage = (long) p.getAmperage();
            this.metrics.maxAmperage = p.getMaxAmperage();
            this.metrics.maxVoltage = p.getMaxVoltage();
            this.metrics.actualLoss = p.getLoss();
        }
    }

    private void initAsOutput(ConsumerNode consumer) {
        if (consumer instanceof EmptyPowerConsumer) return;
        this.state = GraphNodeState.OUTPUT;

        for (int side = 0; side < 6; side++) {
            if (consumer.mNeighbourNodes[side] instanceof PowerNode neighbour
                && neighbour.mSelfPath instanceof PowerNodePath p) {
                this.metrics.maxAmperage = p.getMaxAmperage();
                this.metrics.maxVoltage = p.getMaxVoltage();
                break;
            }
        }

        if (consumer.mSelfPath instanceof PowerNodePath p) {
            this.metrics.actualAmperage = (long) p.getAmperage();
            this.metrics.actualLoss = p.getLoss();
        }
    }

    public Localized getVoltageDetails() {
        if (this.state == GraphNodeState.OUTPUT) {
            return new Localized(
                "gt.NetworkAnalyzer.voltage.explosion.full",
                this.metrics.actualVoltage,
                this.metrics.maxVoltage);
        } else {
            return new Localized(
                "gt.NetworkAnalyzer.voltage.burn.full",
                this.metrics.actualVoltage,
                this.metrics.maxVoltage);
        }
    }

    public Localized getVoltageLabel() {
        if (this.state == GraphNodeState.OUTPUT) {
            return new Localized("gt.NetworkAnalyzer.voltage.explosion");
        } else {
            return new Localized("gt.NetworkAnalyzer.voltage.burn");
        }
    }

    public Localized getFullAmperage() {
        return new Localized(
            "gt.NetworkAnalyzer.amperage.full",
            this.metrics.expectedInputAmps,
            this.metrics.expectedOutputAmps,
            this.metrics.maxAmperage);
    }

    public Localized getShortAmperage(long amperage) {
        return new Localized("gt.NetworkAnalyzer.amperage.short", amperage, this.metrics.maxAmperage);
    }

    public Localized[] getFullTooltip(boolean useRealValues) {
        final long amperage = Math.min(this.metrics.expectedOutputAmps, this.metrics.expectedInputAmps);
        final long loss = useRealValues ? this.metrics.actualLoss : this.metrics.expectedLoss;
        final List<Localized> lines = new ArrayList<>();

        if (this.metrics.maxAmperage > 0 && amperage > 0) {
            if (useRealValues) {
                lines.add(getShortAmperage(this.metrics.actualAmperage));
            } else {
                lines.add(getFullAmperage());
            }
        }

        if (this.metrics.maxVoltage > 0 && loss > 0) {
            final long lossPercent = loss * 100 / this.metrics.maxVoltage;
            lines.add(new Localized("gt.NetworkAnalyzer.loss.full", lossPercent, loss));
        }

        if (this.metrics.getVoltageLevel() == SeverityLevel.ALERT) {
            lines.add(getVoltageDetails());
        }

        return lines.isEmpty() ? null : lines.toArray(new Localized[0]);
    }

    public Localized[] getCriticalLabel() {
        final List<Localized> labels = new ArrayList<>();

        if (this.metrics.getAmperageLevel() == SeverityLevel.ALERT) {
            if (this.metrics.expectedOutputAmps < this.metrics.expectedInputAmps) {
                labels.add(
                    new Localized(
                        "gt.NetworkAnalyzer.amperage.output",
                        this.metrics.expectedOutputAmps,
                        this.metrics.maxAmperage));
            } else {
                labels.add(
                    new Localized(
                        "gt.NetworkAnalyzer.amperage.input",
                        this.metrics.expectedInputAmps,
                        this.metrics.maxAmperage));
            }
        }

        if (this.metrics.getLossLevel(false) == SeverityLevel.ALERT) {
            final long lossPercent = this.metrics.expectedLoss * 100 / this.metrics.maxVoltage;
            labels.add(new Localized("gt.NetworkAnalyzer.loss.short", lossPercent));
        }

        if (this.metrics.getVoltageLevel() == SeverityLevel.ALERT) {
            labels.add(getVoltageLabel());
        }

        return labels.isEmpty() ? null : labels.toArray(new Localized[0]);
    }
}
