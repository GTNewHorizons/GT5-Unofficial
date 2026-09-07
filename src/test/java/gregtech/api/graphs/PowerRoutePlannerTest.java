package gregtech.api.graphs;

import static gregtech.api.graphs.EnergyRoutingContractTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.common.covers.Cover;

class PowerRoutePlannerTest {

    @Test
    void cachedValidationRejectsMutationAndNestedPreparationFallsBack() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(1);
            BaseMetaPipeEntity shortcut = layout.shortcut(1);
            ready(layout);
            PowerRoutePlanner planner = new PowerRoutePlanner(layout.entry.getNode());
            PowerRoutePlanner.Tree tree = planner.prepare(layout.entry.getNode());
            Cover cover = mock(Cover.class);
            when(cover.isValid()).thenReturn(true);
            when(cover.letsEnergyIn()).thenReturn(true);
            when(cover.letsEnergyOut()).thenAnswer(call -> {
                assertNull(planner.prepare(layout.entry.getNode()));
                return true;
            });
            when(shortcut.getCoverAtSide(ForgeDirection.WEST)).thenReturn(cover);
            assertSame(tree, planner.prepare(layout.entry.getNode()));
            doAnswer(call -> {
                cable(layout.entry).mConnections &= ~ForgeDirection.EAST.flag;
                return true;
            }).when(cover)
                .letsEnergyOut();
            assertNull(planner.prepare(layout.entry.getNode()));
            assertFalse(tree.isCurrent());
        }
    }

    @Test
    void oversizedConsumerSnapshotUsesFallbackBeforeAllocatingRouteTrees() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(1);
            ready(layout);
            Node entry = layout.entry.getNode();
            entry.mConsumers = new ArrayList<>(
                Collections.nCopies(PowerRoutePlanner.MAX_NODES + 1, entry.mConsumers.get(0)));
            assertNull(new PowerRoutePlanner(entry).prepare(entry));
            verify(layout.receiver, never()).injectEnergyUnits(any(), anyLong(), anyLong());
        }
    }

    private static void ready(Layout layout) {
        layout.receiver.xCoord = 3;
        layout.receiver.yCoord = 64;
        Cover empty = mock(Cover.class);
        for (TileEntity tile : layout.tiles.values()) {
            if (tile instanceof BaseMetaPipeEntity base) when(base.getCoverAtSide(any())).thenReturn(empty);
        }
        layout.rebuild();
    }

    private static void matchesOracle(PowerRoutePlanner planner, BaseMetaPipeEntity entry, BaseMetaPipeEntity end) {
        PowerRoutePlanner.Tree tree = planner.prepare(entry.getNode());
        assertNotNull(tree);
        PowerRoutePlanner.Route route = tree.routeTo(end.getNode());
        List<BaseMetaPipeEntity> expected = bestRoute(entry, end, false);
        assertEquals(loss(expected), route.loss);
        assertEquals(
            expected.stream()
                .map(EnergyRoutingContractTest::cable)
                .toList(),
            route.cables());
        assertEquals(expected.size(), route.cableCount);
    }

    @Test
    void shortcutsMixedLossAndReverseEntriesMatchPhysicalOracleWithoutTransferring() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            for (long lower : new long[] { 0, 1, 3 }) {
                for (long shortcut : new long[] { 0, 1, 20 }) {
                    Layout layout = new Layout(lower);
                    layout.shortcut(shortcut);
                    ready(layout);
                    PowerRoutePlanner planner = new PowerRoutePlanner(layout.entry.getNode());
                    matchesOracle(planner, layout.entry, layout.end);
                    matchesOracle(planner, layout.end, layout.entry);
                    ConsumerNode receiver = layout.entry.getNode().mConsumers.get(0);
                    assertEquals(
                        loss(bestRoute(layout.entry, layout.end, false)),
                        planner.prepare(layout.entry.getNode())
                            .routeTo(receiver).loss);
                    verify(layout.receiver, never()).injectEnergyUnits(any(), anyLong(), anyLong());
                    verify(layout.receiver, never()).canAcceptEnergyThisTick();
                    if (lower == 3 && shortcut == 1) {
                        assertEquals(1, layout.deliver());
                        verify(layout.receiver).injectEnergyUnits(ForgeDirection.WEST, 116, 1);
                    }
                }
            }
        }
    }

    @Test
    void equalCostPrefersShorterThenCoordinatesAcrossZeroLossCycles() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            for (long lower : new long[] { 0, 1 }) {
                Layout layout = new Layout(lower);
                layout.shortcut(3 * lower);
                ready(layout);
                matchesOracle(new PowerRoutePlanner(layout.entry.getNode()), layout.entry, layout.end);
                layout.tiles.remove(List.of(1, 0));
                cable(layout.entry).mConnections &= ~ForgeDirection.EAST.flag;
                cable(layout.end).mConnections &= ~ForgeDirection.WEST.flag;
                BaseMetaPipeEntity left = layout.pipe(0, 1, lower);
                BaseMetaPipeEntity middle = layout.pipe(1, 1, lower);
                BaseMetaPipeEntity right = layout.pipe(2, 1, lower);
                layout.connect(layout.entry, ForgeDirection.UP);
                layout.connect(left, ForgeDirection.EAST);
                layout.connect(middle, ForgeDirection.EAST);
                layout.connect(right, ForgeDirection.DOWN);
                ready(layout);
                matchesOracle(new PowerRoutePlanner(layout.entry.getNode()), layout.entry, layout.end);
            }
        }
    }

    @Test
    void smallRandomGridsMatchOracleAndManyEntriesEvictOldestPreparation() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Random random = new Random(903);
            for (int sample = 0; sample < 6; sample++) {
                Layout layout = new Layout(0);
                layout.tiles.clear();
                BaseMetaPipeEntity[][] grid = new BaseMetaPipeEntity[3][3];
                List<BaseMetaPipeEntity> all = new ArrayList<>();
                Cover empty = mock(Cover.class);
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        BaseMetaPipeEntity pipe = layout.pipe(x, y, random.nextInt(4));
                        grid[x][y] = pipe;
                        all.add(pipe);
                        when(pipe.getCoverAtSide(any())).thenReturn(empty);
                        cable(pipe).mConnections |= ForgeDirection.NORTH.flag; // Keep every entry as a node.
                    }
                }
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        if (x < 2) layout.connect(grid[x][y], ForgeDirection.EAST);
                        if (y < 2) layout.connect(grid[x][y], ForgeDirection.UP);
                    }
                }
                new GenerateNodeMapPower(all.get(0));
                PowerRoutePlanner planner = new PowerRoutePlanner(
                    all.get(0)
                        .getNode());
                PowerRoutePlanner.Tree first = planner.prepare(
                    all.get(0)
                        .getNode());
                assertSame(
                    first,
                    planner.prepare(
                        all.get(0)
                            .getNode()));
                for (BaseMetaPipeEntity entry : all) {
                    for (BaseMetaPipeEntity end : all) matchesOracle(planner, entry, end);
                }
                assertEquals(PowerRoutePlanner.MAX_ENTRIES + 1, all.size());
                assertNotSame(
                    first,
                    planner.prepare(
                        all.get(0)
                            .getNode()));
            }
        }
    }

    @Test
    void coversAndLocksRecomputeButReceiverCapacityDoesNotInvalidate() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(3);
            BaseMetaPipeEntity shortcut = layout.shortcut(1);
            ready(layout);
            Cover cover = mock(Cover.class);
            when(cover.isValid()).thenReturn(true);
            when(cover.letsEnergyIn()).thenReturn(true);
            when(cover.letsEnergyOut()).thenReturn(true);
            when(shortcut.getCoverAtSide(ForgeDirection.WEST)).thenReturn(cover);
            Node entry = layout.entry.getNode(), end = layout.end.getNode();
            PowerRoutePlanner planner = new PowerRoutePlanner(entry);
            PowerRoutePlanner.Tree original = planner.prepare(entry);
            assertEquals(4, original.routeTo(end).loss);
            when(layout.receiver.canAcceptEnergyThisTick()).thenReturn(false);
            assertSame(original, planner.prepare(entry));
            when(cover.letsEnergyOut()).thenReturn(false);
            assertFalse(original.isCurrent());
            assertThrows(IllegalStateException.class, () -> original.routeTo(end));
            PowerRoutePlanner.Tree blocked = planner.prepare(entry);
            assertEquals(12, blocked.routeTo(end).loss);
            entry.locks[ForgeDirection.DOWN.ordinal()].addTileEntity(null);
            assertFalse(blocked.isCurrent());
            assertNull(
                planner.prepare(entry)
                    .routeTo(end));
            entry.locks[ForgeDirection.DOWN.ordinal()].removeTileEntity(null);
            when(cover.letsEnergyOut()).thenReturn(true);
            assertEquals(
                4,
                planner.prepare(entry)
                    .routeTo(end).loss);
        }
    }

    @Test
    void connectivityRecolorAndClearRequireANewComponentOwner() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            for (boolean recolor : new boolean[] { false, true }) {
                Layout layout = new Layout(1);
                BaseMetaPipeEntity shortcut = layout.shortcut(1);
                ready(layout);
                PowerRoutePlanner planner = new PowerRoutePlanner(layout.entry.getNode());
                PowerRoutePlanner.Tree first = planner.prepare(layout.entry.getNode());
                if (recolor) when(shortcut.getColorization()).thenReturn((byte) 2);
                else cable(layout.entry).mConnections &= ~ForgeDirection.EAST.flag;
                assertFalse(first.isCurrent());
                assertNull(planner.prepare(layout.entry.getNode()));
                ready(layout);
                PowerRoutePlanner rebuilt = new PowerRoutePlanner(layout.entry.getNode());
                assertEquals(
                    6,
                    rebuilt.prepare(layout.entry.getNode())
                        .routeTo(layout.end.getNode()).loss);
                PowerRoutePlanner.Tree tree = rebuilt.prepare(layout.entry.getNode());
                GenerateNodeMap.clearNodeMap(layout.entry.getNode(), -1);
                assertFalse(tree.isCurrent());
            }
        }
    }

    @Test
    void unloadedOrReplacedWorldTileInvalidatesWithoutLoadingItsChunk() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            for (boolean unload : new boolean[] { false, true }) {
                Layout layout = new Layout(1);
                layout.shortcut(1);
                ready(layout);
                World world = mock(World.class);
                when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
                when(world.getTileEntity(anyInt(), anyInt(), anyInt())).thenAnswer(
                    call -> layout.tiles.get(List.of((int) call.getArgument(0), (int) call.getArgument(1) - 64)));
                for (TileEntity tile : layout.tiles.values()) when(tile.getWorldObj()).thenReturn(world);
                PowerRoutePlanner planner = new PowerRoutePlanner(layout.entry.getNode());
                PowerRoutePlanner.Tree tree = planner.prepare(layout.entry.getNode());
                assertNotNull(tree);
                if (unload) when(world.blockExists(1, 64, 0)).thenReturn(false);
                else layout.tiles.put(List.of(1, 0), mock(TileEntity.class));
                clearInvocations(world);
                clearInvocations(layout.entry);
                assertFalse(tree.isCurrent());
                if (unload) {
                    verify(world, never()).getTileEntity(1, 64, 0);
                    verify(layout.entry, never()).getTileEntityAtSide(ForgeDirection.EAST);
                }
            }
        }
    }

    @Test
    void negativeAndOverflowingWeightsReturnExplicitFallback() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            for (long loss : new long[] { -1, Long.MAX_VALUE }) {
                Layout layout = new Layout(loss);
                layout.shortcut(1);
                ready(layout);
                assertNull(new PowerRoutePlanner(layout.entry.getNode()).prepare(layout.entry.getNode()));
            }
        }
    }

    @Test
    void synchronousCoverMutationCannotPublishPreparationForOldTopology() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            Layout layout = new Layout(1);
            BaseMetaPipeEntity shortcut = layout.shortcut(1);
            ready(layout);
            Cover cover = mock(Cover.class);
            when(cover.isValid()).thenReturn(true);
            when(cover.letsEnergyIn()).thenReturn(true);
            when(cover.letsEnergyOut()).thenAnswer(call -> {
                cable(layout.entry).mConnections &= ~ForgeDirection.EAST.flag;
                return true;
            });
            when(shortcut.getCoverAtSide(ForgeDirection.WEST)).thenReturn(cover);
            assertNull(new PowerRoutePlanner(layout.entry.getNode()).prepare(layout.entry.getNode()));
        }
    }
}
