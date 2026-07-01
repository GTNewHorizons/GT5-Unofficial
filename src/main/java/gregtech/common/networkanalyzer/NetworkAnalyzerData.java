package gregtech.common.networkanalyzer;

import java.util.ArrayList;
import java.util.List;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.util.GTDataUtils;
import gregtech.api.util.Localized;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.GraphNodeState;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.OverlayEdge;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.OverlayNode;
import gregtech.common.networkanalyzer.events.WorldOverlayRenderer.SeverityLevel;
import io.netty.buffer.ByteBuf;

public class NetworkAnalyzerData {

    public enum AnalyzerModes {
        TOPOLOGY,
        CRITICAL,
        ACTIVE,

        AMPERAGE,
        VOLTAGE,
    }

    public final List<OverlayNode> nodes = new ArrayList<>();
    public final List<OverlayEdge> edges = new ArrayList<>();

    public void add(OverlayNode node) {
        this.nodes.add(node);
    }

    public void add(OverlayEdge edge) {
        this.edges.add(edge);
    }

    public void readExternal(ByteArrayDataInput in) {
        int nodeCount = in.readInt();
        int linkCount = in.readInt();

        for (int i = 0; i < nodeCount; i++) {
            final int x = in.readInt();
            final int y = in.readInt();
            final int z = in.readInt();
            final GraphNodeState state = GTDataUtils.getIndexSafe(GraphNodeState.values(), in.readByte());
            final SeverityLevel level = GTDataUtils.getIndexSafe(SeverityLevel.values(), in.readByte());
            final Localized[] label = readLocalizedArray(in);
            final Localized[] tooltip = readLocalizedArray(in);

            this.nodes.add(new OverlayNode(x, y, z, state, level, label, tooltip));
        }

        for (int i = 0; i < linkCount; i++) {
            final int x1 = in.readInt();
            final int y1 = in.readInt();
            final int z1 = in.readInt();

            final int x2 = in.readInt();
            final int y2 = in.readInt();
            final int z2 = in.readInt();

            final SeverityLevel level = GTDataUtils.getIndexSafe(SeverityLevel.values(), in.readByte());

            this.edges.add(new OverlayEdge(x1, y1, z1, x2, y2, z2, level));
        }
    }

    public void writeExternal(ByteBuf out) {
        out.writeInt(this.nodes.size());
        out.writeInt(this.edges.size());

        for (OverlayNode node : this.nodes) {
            out.writeInt(node.x);
            out.writeInt(node.y);
            out.writeInt(node.z);
            out.writeByte(node.state == null ? -1 : node.state.ordinal());
            out.writeByte(node.level.ordinal());

            writeLocalizedArray(out, node.label);
            writeLocalizedArray(out, node.tooltip);
        }

        for (OverlayEdge edge : this.edges) {
            out.writeInt(edge.x1);
            out.writeInt(edge.y1);
            out.writeInt(edge.z1);

            out.writeInt(edge.x2);
            out.writeInt(edge.y2);
            out.writeInt(edge.z2);

            out.writeByte(edge.level.ordinal());
        }
    }

    private Localized[] readLocalizedArray(ByteArrayDataInput in) {
        final int count = in.readInt();
        if (count == 0) return null;
        final Localized[] result = new Localized[count];
        for (int i = 0; i < count; i++) {
            result[i] = new Localized();
            result[i].decode(in);
        }
        return result;
    }

    private void writeLocalizedArray(ByteBuf out, Localized[] arr) {
        if (arr == null || arr.length == 0) {
            out.writeInt(0);
            return;
        }
        out.writeInt(arr.length);
        for (Localized l : arr) {
            l.encode(out);
        }
    }

}
