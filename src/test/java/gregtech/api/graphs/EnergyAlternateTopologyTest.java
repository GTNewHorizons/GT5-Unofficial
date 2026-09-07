package gregtech.api.graphs;

import static gregtech.api.graphs.EnergyRoutingContractTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.graphs.paths.NodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;

class EnergyAlternateTopologyTest {

    @Test
    void representationOwnsEachPhysicalCableOnceFromEitherEntry() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(1);
            layout.shortcut(1);
            for (BaseMetaPipeEntity entry : List.of(layout.entry, layout.end)) {
                new GenerateNodeMapPower(entry);
                Set<Node> nodes = new HashSet<>();
                Set<NodePath> paths = new HashSet<>();
                Set<Node.AlternateEdge> edges = new HashSet<>();
                List<Node> pending = new ArrayList<>(List.of(entry.getNode()));
                for (int i = 0; i < pending.size(); i++) {
                    Node node = pending.get(i);
                    if (!nodes.add(node)) continue;
                    if (node.mSelfPath != null) paths.add(node.mSelfPath);
                    if (node.alternateEdges != null) edges.addAll(node.alternateEdges);
                    for (int side = 0; side < 6; side++) {
                        if (node.mNodePaths[side] != null) paths.add(node.mNodePaths[side]);
                        if (node.mNeighbourNodes[side] != null) pending.add(node.mNeighbourNodes[side]);
                    }
                }
                Map<MetaPipeEntity, Integer> occurrences = new IdentityHashMap<>();
                for (NodePath path : paths) {
                    for (MetaPipeEntity pipe : path.getPipes()) occurrences.merge(pipe, 1, Integer::sum);
                }
                for (Node.AlternateEdge edge : edges) {
                    for (MetaPipeEntity pipe : edge.pipes) occurrences.merge(pipe, 1, Integer::sum);
                }
                assertEquals(1, edges.size());
                assertEquals(6, occurrences.size());
                for (TileEntity tile : layout.tiles.values()) {
                    if (tile instanceof BaseMetaPipeEntity pipe) assertEquals(1, occurrences.get(cable(pipe)));
                }
                GenerateNodeMap.clearNodeMap(entry.getNode(), -1);
                for (Node node : nodes) assertNull(node.alternateEdges);
            }
        }
    }

    @Test
    void shortcutIsSharedByItsEndpointsWithoutChangingLegacyTransfer() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(3);
            BaseMetaPipeEntity shortcut = layout.shortcut(1);
            layout.rebuild();
            Node root = layout.entry.getNode(), end = layout.end.getNode();
            assertEquals(1, root.alternateEdges.size());
            Node.AlternateEdge edge = root.alternateEdges.get(0);
            assertSame(edge, end.alternateEdges.get(0));
            assertSame(end, edge.first);
            assertEquals(ForgeDirection.WEST, edge.firstSide);
            assertSame(root, edge.second);
            assertEquals(ForgeDirection.EAST, edge.secondSide);
            assertArrayEquals(new MetaPipeEntity[] { cable(shortcut) }, edge.pipes);
            assertNull(root.mNeighbourNodes[ForgeDirection.EAST.ordinal()]);
            assertNull(shortcut.getNodePath());
            assertEquals(1, layout.deliver());
            verify(layout.receiver).injectEnergyUnits(ForgeDirection.WEST, 116, 1);
            verify(shortcut, never()).setNodePath(any());
            verify(shortcut, never()).setToFire();
            GenerateNodeMap.clearNodeMap(end, -1);
            assertNull(root.alternateEdges);
            assertNull(end.alternateEdges);
            assertNull(layout.entry.getNode());
            assertNull(layout.end.getNode());
            assertDoesNotThrow(() -> GenerateNodeMap.clearNodeMap(root, -1));
        }
    }

    @Test
    void parallelRunsRetainOrderedMembersAndAreNotDeduplicatedByEndpointIdentity() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(1);
            BaseMetaPipeEntity shortcut = layout.shortcut(1);
            BaseMetaPipeEntity upperLeft = layout.pipe(0, 1, 2);
            BaseMetaPipeEntity upperMiddle = layout.pipe(1, 1, 3);
            BaseMetaPipeEntity upperRight = layout.pipe(2, 1, 4);
            layout.connect(layout.entry, ForgeDirection.UP);
            layout.connect(upperLeft, ForgeDirection.EAST);
            layout.connect(upperMiddle, ForgeDirection.EAST);
            layout.connect(upperRight, ForgeDirection.DOWN);
            layout.rebuild();
            Node root = layout.entry.getNode(), end = layout.end.getNode();
            assertEquals(2, root.alternateEdges.size());
            assertEquals(root.alternateEdges, end.alternateEdges);
            Node.AlternateEdge upper = root.alternateEdges.get(0);
            assertEquals(ForgeDirection.UP, upper.firstSide);
            assertEquals(ForgeDirection.UP, upper.secondSide);
            assertArrayEquals(
                new MetaPipeEntity[] { cable(upperRight), cable(upperMiddle), cable(upperLeft) },
                upper.pipes);
            assertArrayEquals(new MetaPipeEntity[] { cable(shortcut) }, root.alternateEdges.get(1).pipes);
            GenerateNodeMap.clearNodeMap(root, -1);
            assertNull(end.alternateEdges);
        }
    }

    @Test
    void degreeTwoRingKeepsOneSelfLoopWithBothFacesAndClearsSafely() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(0);
            BaseMetaPipeEntity shortcut = layout.shortcut(0);
            cable(layout.entry).mConnections &= ~ForgeDirection.WEST.flag;
            cable(layout.end).mConnections &= ~ForgeDirection.EAST.flag;
            layout.rebuild();
            Node root = layout.entry.getNode();
            assertTrue(root.mConsumers.isEmpty());
            assertEquals(1, root.alternateEdges.size());
            Node.AlternateEdge edge = root.alternateEdges.get(0);
            assertSame(root, edge.first);
            assertSame(root, edge.second);
            assertEquals(ForgeDirection.DOWN, edge.firstSide);
            assertEquals(ForgeDirection.EAST, edge.secondSide);
            assertArrayEquals(
                new MetaPipeEntity[] { cable(layout.lowerLeft), cable(layout.lowerMiddle), cable(layout.lowerRight),
                    cable(layout.end), cable(shortcut) },
                edge.pipes);
            assertTrue(
                Arrays.stream(root.mNeighbourNodes)
                    .allMatch(node -> node == null));
            assertDoesNotThrow(() -> GenerateNodeMap.clearNodeMap(root, -1));
            assertNull(root.alternateEdges);
            assertNull(layout.entry.getNode());
        }
    }

    @Test
    void directlyAdjacentLoopEndpointsHaveAnEmptyInteriorRun() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(1);
            BaseMetaPipeEntity shortcut = layout.shortcut(1);
            cable(shortcut).mConnections |= ForgeDirection.UP.flag; // Force a junction node.
            layout.rebuild();
            Node.AlternateEdge edge = layout.entry.getNode().alternateEdges.get(0);
            assertSame(shortcut.getNode(), edge.first);
            assertSame(layout.entry.getNode(), edge.second);
            assertEquals(0, edge.pipes.length);
            assertEquals(ForgeDirection.WEST, edge.firstSide);
            assertEquals(ForgeDirection.EAST, edge.secondSide);
        }
    }

    @Test
    void rebuildDropsInvalidUnavailableReplacedAndOneWayRuns() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(1);
            BaseMetaPipeEntity original = layout.shortcut(1);
            layout.rebuild();
            Node old = layout.entry.getNode();
            when(original.isInvalid()).thenReturn(true);
            layout.rebuild();
            assertNull(old.alternateEdges);
            assertNull(layout.entry.getNode().alternateEdges);
            layout.tiles.remove(List.of(1, 0)); // Neighbor unavailable, as after removal/unload.
            layout.rebuild();
            assertNull(layout.entry.getNode().alternateEdges);
            BaseMetaPipeEntity replacement = layout.shortcut(2);
            layout.rebuild();
            assertArrayEquals(
                new MetaPipeEntity[] { cable(replacement) },
                layout.entry.getNode().alternateEdges.get(0).pipes);
            cable(layout.entry).mConnections &= ~ForgeDirection.EAST.flag;
            layout.rebuild();
            assertNull(layout.entry.getNode().alternateEdges);
            assertNull(layout.end.getNode().alternateEdges);
        }
    }

    @Test
    void partialClearDetachesAlternateMetadataWithoutClearingExcludedParent() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(1);
            layout.shortcut(1);
            layout.rebuild();
            Node root = layout.entry.getNode(), end = layout.end.getNode();
            GenerateNodeMap.clearNodeMap(end, root.mNodeValue);
            assertSame(root, layout.entry.getNode());
            assertNotNull(root.mSelfPath);
            assertNull(root.alternateEdges);
            assertNull(end.alternateEdges);
            assertNull(layout.end.getNode());
        }
    }

    @Test
    void cleanupTerminatesEvenIfNeighborArraysContainACycle() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Node first = new Node(1, mock(TileEntity.class), new ArrayList<>());
            Node second = new Node(2, mock(TileEntity.class), new ArrayList<>());
            Node third = new Node(3, mock(TileEntity.class), new ArrayList<>());
            first.mNeighbourNodes[0] = second;
            second.mNeighbourNodes[0] = third;
            third.mNeighbourNodes[0] = first;
            assertDoesNotThrow(() -> GenerateNodeMap.clearNodeMap(first, -1));
            for (Node node : List.of(first, second, third)) assertNull(node.mNeighbourNodes[0]);
        }
    }
}
