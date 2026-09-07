package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.graphs.paths.NodePath;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;

/** Item 10 contract/oracle and legacy characterization; the production router is still DFS. */
class EnergyRoutingContractTest {

    static final class Layout {

        final Map<List<Integer>, TileEntity> tiles = new LinkedHashMap<>();
        final BaseMetaPipeEntity entry, end, lowerLeft, lowerMiddle, lowerRight;
        final BaseMetaTileEntity receiver = mock(BaseMetaTileEntity.class);

        Layout(long lowerLoss) {
            entry = pipe(0, 0, 1);
            lowerLeft = pipe(0, -1, lowerLoss);
            lowerMiddle = pipe(1, -1, lowerLoss);
            lowerRight = pipe(2, -1, lowerLoss);
            end = pipe(2, 0, 2);
            connect(entry, ForgeDirection.DOWN);
            connect(lowerLeft, ForgeDirection.EAST);
            connect(lowerMiddle, ForgeDirection.EAST);
            connect(lowerRight, ForgeDirection.UP);
            cable(entry).mConnections |= ForgeDirection.WEST.flag; // Source face.
            cable(end).mConnections |= ForgeDirection.EAST.flag;
            tiles.put(List.of(3, 0), receiver);
            when(receiver.inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(true);
            when(receiver.injectEnergyUnits(eq(ForgeDirection.WEST), anyLong(), anyLong())).thenReturn(1L);
        }

        BaseMetaPipeEntity pipe(int x, int y, long loss) {
            BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
            base.xCoord = x;
            base.yCoord = 64 + y;
            MTECable cable = spy(new MTECable("route", 0.5f, null, loss, 4, 512, false, false));
            when(base.getMetaTileEntity()).thenReturn(cable);
            when(cable.getBaseMetaTileEntity()).thenReturn(base);
            when(base.getTileEntityAtSide(any())).thenAnswer(call -> {
                ForgeDirection side = call.getArgument(0);
                return side.offsetZ == 0 ? tiles.get(List.of(x + side.offsetX, y + side.offsetY)) : null;
            });
            Node[] node = { null };
            NodePath[] path = { null };
            when(base.getNode()).thenAnswer(call -> node[0]);
            when(base.getNodePath()).thenAnswer(call -> path[0]);
            doAnswer(call -> {
                node[0] = call.getArgument(0);
                return null;
            }).when(base)
                .setNode(any());
            doAnswer(call -> {
                path[0] = call.getArgument(0);
                return null;
            }).when(base)
                .setNodePath(any());
            tiles.put(List.of(x, y), base);
            return base;
        }

        void connect(BaseMetaPipeEntity from, ForgeDirection side) {
            BaseMetaPipeEntity to = (BaseMetaPipeEntity) from.getTileEntityAtSide(side);
            cable(from).mConnections |= side.flag;
            cable(to).mConnections |= side.getOpposite().flag;
        }

        BaseMetaPipeEntity shortcut(long loss) {
            BaseMetaPipeEntity middle = pipe(1, 0, loss);
            connect(entry, ForgeDirection.EAST);
            connect(middle, ForgeDirection.EAST);
            return middle;
        }

        void rebuild() {
            if (entry.getNode() != null) GenerateNodeMap.clearNodeMap(entry.getNode(), -1);
            new GenerateNodeMapPower(entry);
        }

        long deliver() {
            Node root = entry.getNode();
            return PowerNodes.powerNode(root, null, new NodeList(root.mConsumers.toArray(new Node[0])), 128, 1);
        }
    }

    static MTECable cable(BaseMetaPipeEntity pipe) {
        return (MTECable) pipe.getMetaTileEntity();
    }

    static long loss(List<BaseMetaPipeEntity> route) {
        long total = 0;
        for (BaseMetaPipeEntity pipe : route) {
            long value = cable(pipe).mCableLossPerMeter;
            if (value < 0) throw new IllegalArgumentException("Negative routing weight");
            total = Math.addExact(total, value);
        }
        return total;
    }

    // ponytail: exhaustive simple paths for tiny fixtures only; production must use bounded per-entry preparation.
    static List<BaseMetaPipeEntity> bestRoute(BaseMetaPipeEntity entry, BaseMetaPipeEntity end, boolean reverse) {
        List<List<BaseMetaPipeEntity>> routes = new ArrayList<>();
        enumerate(entry, end, new ArrayList<>(), routes, reverse);
        Comparator<List<BaseMetaPipeEntity>> order = Comparator.comparingLong(EnergyRoutingContractTest::loss);
        order = order.thenComparingInt(List::size)
            .thenComparing((a, b) -> {
                for (int i = 0; i < a.size(); i++) {
                    int c = Integer.compare(a.get(i).xCoord, b.get(i).xCoord);
                    if (c == 0) c = Integer.compare(a.get(i).yCoord, b.get(i).yCoord);
                    if (c == 0) c = Integer.compare(a.get(i).zCoord, b.get(i).zCoord);
                    if (c != 0) return c;
                }
                return 0;
            });
        return routes.stream()
            .min(order)
            .orElseThrow();
    }

    static void enumerate(BaseMetaPipeEntity current, BaseMetaPipeEntity end, List<BaseMetaPipeEntity> prefix,
        List<List<BaseMetaPipeEntity>> routes, boolean reverse) {
        if (prefix.contains(current)) return;
        prefix.add(current);
        if (current == end) routes.add(new ArrayList<>(prefix));
        else {
            List<ForgeDirection> sides = new ArrayList<>(List.of(ForgeDirection.VALID_DIRECTIONS));
            if (reverse) Collections.reverse(sides);
            for (ForgeDirection side : sides) {
                if (cable(current).isConnectedAtSide(side)
                    && current.getTileEntityAtSide(side) instanceof BaseMetaPipeEntity next
                    && cable(next).isConnectedAtSide(side.getOpposite())) {
                    enumerate(next, end, prefix, routes, reverse);
                }
            }
        }
        prefix.remove(prefix.size() - 1);
    }

    @Test
    void shortcutContractExposesLegacyDfsGapAndRebuildUsesRemainingRoute() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(3);
            layout.rebuild();
            assertEquals(1, layout.deliver());
            verify(layout.receiver).injectEnergyUnits(ForgeDirection.WEST, 116, 1);
            assertEquals(12, loss(bestRoute(layout.entry, layout.end, false)));

            BaseMetaPipeEntity shortcut = layout.shortcut(1);
            layout.rebuild();
            assertEquals(List.of(layout.entry, shortcut, layout.end), bestRoute(layout.entry, layout.end, false));
            assertEquals(4, loss(bestRoute(layout.entry, layout.end, true)));
            assertEquals(1, layout.deliver());
            // Characterization to replace at integration: physical shortcut exists but DFS discarded it.
            verify(layout.receiver, times(2)).injectEnergyUnits(ForgeDirection.WEST, 116, 1);
            assertNull(shortcut.getNodePath());
            assertEquals(1, ((PowerNodePath) layout.entry.getNode().mSelfPath).getLoss());
            assertEquals(
                9,
                ((PowerNodePath) layout.entry.getNode().mNodePaths[ForgeDirection.DOWN.ordinal()]).getLoss());
            assertEquals(2, ((PowerNodePath) layout.end.getNode().mSelfPath).getLoss());

            cable(layout.entry).mConnections &= ~ForgeDirection.DOWN.flag;
            cable(layout.lowerLeft).mConnections &= ~ForgeDirection.UP.flag;
            layout.rebuild();
            assertEquals(1, layout.deliver());
            verify(layout.receiver).injectEnergyUnits(ForgeDirection.WEST, 124, 1);
        }
    }

    @Test
    void lossWinsOverDistanceAndRoutesDependOnEntry() {
        Layout layout = new Layout(1);
        layout.shortcut(20);
        assertEquals(
            List.of(layout.entry, layout.lowerLeft, layout.lowerMiddle, layout.lowerRight, layout.end),
            bestRoute(layout.entry, layout.end, false));
        assertEquals(6, loss(bestRoute(layout.entry, layout.end, true)));
        assertEquals(List.of(layout.lowerRight, layout.end), bestRoute(layout.lowerRight, layout.end, false));
    }

    @Test
    void equalLossPrefersFewerCablesAndZeroLossCyclesStayAcyclic() {
        for (long lowerLoss : new long[] { 0, 1 }) {
            Layout layout = new Layout(lowerLoss);
            BaseMetaPipeEntity shortcut = layout.shortcut(3 * lowerLoss);
            assertEquals(List.of(layout.entry, shortcut, layout.end), bestRoute(layout.entry, layout.end, false));
            assertEquals(bestRoute(layout.entry, layout.end, false), bestRoute(layout.entry, layout.end, true));
        }
    }

    @Test
    void equalLossAndLengthUseCoordinatesRatherThanDiscoveryOrder() {
        Layout layout = new Layout(1);
        BaseMetaPipeEntity upperLeft = layout.pipe(0, 1, 1);
        BaseMetaPipeEntity upperMiddle = layout.pipe(1, 1, 1);
        BaseMetaPipeEntity upperRight = layout.pipe(2, 1, 1);
        layout.connect(layout.entry, ForgeDirection.UP);
        layout.connect(upperLeft, ForgeDirection.EAST);
        layout.connect(upperMiddle, ForgeDirection.EAST);
        layout.connect(upperRight, ForgeDirection.DOWN);
        List<BaseMetaPipeEntity> expected = List
            .of(layout.entry, layout.lowerLeft, layout.lowerMiddle, layout.lowerRight, layout.end);
        assertEquals(expected, bestRoute(layout.entry, layout.end, false));
        assertEquals(expected, bestRoute(layout.entry, layout.end, true));
    }

    @Test
    void unsupportedWeightsAreExplicitInsteadOfWrappingIntoCheapRoutes() {
        Layout layout = new Layout(0);
        BaseMetaPipeEntity negative = layout.pipe(10, 0, -1);
        BaseMetaPipeEntity huge = layout.pipe(11, 0, Long.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> loss(List.of(negative)));
        assertThrows(ArithmeticException.class, () -> loss(List.of(layout.entry, huge)));
        // Existing constructors/summation do not enforce the future router's weight domain.
        assertEquals(-1, new PowerNodePath(new MetaPipeEntity[] { cable(negative) }).getLoss());
        assertEquals(
            Long.MIN_VALUE,
            new PowerNodePath(new MetaPipeEntity[] { cable(layout.entry), cable(huge) }).getLoss());
    }
}
