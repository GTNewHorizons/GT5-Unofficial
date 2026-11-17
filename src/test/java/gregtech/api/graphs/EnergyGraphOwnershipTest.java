package gregtech.api.graphs;

import static net.minecraftforge.common.util.ForgeDirection.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.graphs.paths.NodePath;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTETransformer;
import gregtech.api.threads.RunnableCableUpdate;
import gregtech.api.threads.RunnableMachineUpdate;
import ic2.api.energy.tile.IEnergySink;

class EnergyGraphOwnershipTest {

    private MockedStatic<MinecraftServer> servers;
    private World world;
    private final Map<String, TileEntity> tiles = new HashMap<>();
    private final AtomicInteger tick = new AtomicInteger();
    private boolean updatesEnabled;

    @BeforeEach
    void setup() {
        BaseMetaPipeEntity.clearManagedCables();
        updatesEnabled = RunnableMachineUpdate.isEnabled();
        RunnableMachineUpdate.setEnabled();
        servers = mockStatic(MinecraftServer.class);
        MinecraftServer server = mock(MinecraftServer.class);
        servers.when(MinecraftServer::getServer)
            .thenReturn(server);
        when(server.getTickCounter()).thenAnswer(call -> tick.get());
        world = mock(World.class);
        when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
        when(world.getTileEntity(anyInt(), eq(64), anyInt()))
            .thenAnswer(call -> tiles.get(key(call.getArgument(0), call.getArgument(2))));
    }

    @AfterEach
    void cleanup() {
        try {
            RunnableCableUpdate.endTick();
        } finally {
            RunnableMachineUpdate.setEnabled(updatesEnabled);
            BaseMetaPipeEntity.clearManagedCables();
            servers.close();
        }
    }

    @Test
    void mergedGridStopsAtRemovedFormerEndpoint() throws Exception {
        BaseMetaPipeEntity a = pipe(0, 0, WEST, EAST, NORTH);
        pipe(1, 0, WEST, EAST);
        BaseMetaPipeEntity aEnd = pipe(2, 0, WEST);
        BaseMetaPipeEntity bEnd = pipe(3, 0, EAST);
        pipe(4, 0, WEST, EAST);
        BaseMetaPipeEntity b = pipe(5, 0, WEST, NORTH);
        TileEntity sinkA = receiver(0, -1, new AtomicLong());
        AtomicLong energyB = new AtomicLong();
        receiver(5, -1, energyB);
        BaseMetaTileEntity sourceA = source(a);
        assertEquals(1, offer(a));
        assertEquals(1, offer(b));
        Node oldB = bEnd.getNode();
        assertNotNull(oldB);

        ((MTECable) aEnd.getMetaTileEntity()).mConnections |= EAST.flag;
        ((MTECable) bEnd.getMetaTileEntity()).mConnections |= WEST.flag;
        aEnd.updateConnections();
        bEnd.updateConnections();
        tick.set(20);
        when(sinkA.isInvalid()).thenReturn(true);
        assertEquals(1, offer(a));
        assertTrue(oldB.mInvalid);
        assertNull(bEnd.getNode(), "Former endpoint must become a compressed cable without an old node");
        assertNotNull(bEnd.getNodePath());
        Node merged = a.getNode();

        tiles.remove(key(3, 0));
        bEnd.invalidate();
        assertTrue(merged.mInvalid);
        RunnableCableUpdate.setCableUpdateValues(world, 2, 64, 0);
        delayedRefresh(sourceA);
        long before = energyB.get();
        assertEquals(0, offer(a));
        assertEquals(before, energyB.get(), "No energy may cross the removed former endpoint");
    }

    @Test
    void editInDiscardedLoopDiscoversNewConsumer() throws Exception {
        BaseMetaPipeEntity root = pipe(0, 0, WEST, EAST, SOUTH);
        for (int x = 1; x < 8; x++) pipe(x, 0, WEST, EAST);
        pipe(8, 0, WEST, SOUTH, NORTH);
        pipe(0, 1, NORTH, SOUTH);
        pipe(8, 1, NORTH, SOUTH);
        pipe(0, 2, NORTH, EAST);
        pipe(8, 2, NORTH, WEST);
        for (int x = 1; x < 8; x++) pipe(x, 2, WEST, EAST);
        TileEntity oldSink = receiver(8, -1, new AtomicLong());
        BaseMetaTileEntity generator = source(root);
        assertEquals(1, offer(root));
        Node oldGraph = root.getNode();
        BaseMetaPipeEntity edited = (BaseMetaPipeEntity) tiles.get(key(4, 0));
        NodePath oldLoopPath = edited.getNodePath();
        assertNotNull(oldLoopPath);
        assertNull(edited.getNode());

        TileEntity tile = mock(TileEntity.class, withSettings().extraInterfaces(IEnergySink.class));
        tile.xCoord = 4;
        tile.yCoord = 64;
        tile.zCoord = -1;
        when(tile.getWorldObj()).thenReturn(world);
        IEnergySink sink = (IEnergySink) tile;
        when(sink.acceptsEnergyFrom(any(), any())).thenReturn(true);
        when(sink.getDemandedEnergy()).thenReturn(1000.0);
        when(sink.injectEnergy(any(), eq(32.0), eq(32.0))).thenReturn(0.0);
        tiles.put(key(4, -1), tile);
        ((MTECable) edited.getMetaTileEntity()).mConnections |= NORTH.flag;
        edited.updateConnections();
        assertFalse(oldGraph.isNodeMapValid());
        assertEquals(1, offer(root), "Existing consumer stays powered before the delayed refresh");
        delayedRefresh(generator);
        verify(generator).onMachineBlockUpdate();
        assertNotSame(oldGraph, root.getNode());
        assertFalse(oldLoopPath.isValid());
        when(((IEnergyConnected) oldSink).injectEnergyUnits(any(), anyLong(), anyLong())).thenReturn(0L);
        assertEquals(1, offer(root));
        verify(sink).injectEnergy(any(), eq(32.0), eq(32.0));

        GenerateNodeMap.clearNodeMap(root.getNode(), -1);
        for (TileEntity candidate : tiles.values()) {
            if (candidate instanceof BaseMetaPipeEntity pipe) assertNull(pipe.getNodeMap());
        }
    }

    @Test
    void oldCleanupCannotEraseReplacementBindings() {
        BaseMetaPipeEntity base = pipe(0, 0, WEST, EAST);
        new GenerateNodeMapPower(base);
        Node old = base.getNode();
        Node replacement = new PowerNode(1, base, new ArrayList<>());
        replacement.mSelfPath = new PowerNodePath(new MetaPipeEntity[] { (MetaPipeEntity) base.getMetaTileEntity() });
        replacement.mSelfPath.setNodeMap(replacement);
        base.setNode(replacement);

        GenerateNodeMap.clearNodeMap(old, -1);
        assertSame(replacement, base.getNode());
        assertSame(replacement.mSelfPath, base.getNodePath());
        GenerateNodeMap.clearNodeMap(replacement, -1);
        assertNull(base.getNode());
        assertNull(base.getNodePath());
    }

    @Test
    void receiverPlacedAtOpenConnectionIsDiscovered() {
        BaseMetaPipeEntity root = pipe(0, 0, WEST, EAST);
        pipe(1, 0, WEST, EAST);
        BaseMetaPipeEntity end = pipe(2, 0, WEST, EAST);
        tick.set(1);
        assertEquals(0, offer(root));
        Node graph = end.getNode();
        assertNotNull(graph, "A cable connected towards air ends the run as a node");

        end.onNeighborBlockChange(2, 64, 0);
        assertTrue(graph.isNodeMapValid(), "Unrelated neighbour updates must not invalidate the graph");

        receiver(3, 0, new AtomicLong());
        end.onNeighborBlockChange(2, 64, 0);
        assertFalse(graph.isNodeMapValid());
        tick.addAndGet(10);
        assertEquals(1, offer(root));
    }

    @Test
    void transformerRoleChangeRefreshesAdjacentGraph() throws Exception {
        BaseMetaPipeEntity root = pipe(0, 0, WEST, EAST);
        BaseMetaTileEntity base = new BaseMetaTileEntity();
        base.xCoord = -1;
        base.yCoord = 64;
        base.setWorldObj(world);
        new MTETransformer("transformer-test", 1, new String[0], null).setBaseMetaTileEntity(base);
        tiles.put(key(-1, 0), base);
        var updateFaces = BaseMetaTileEntity.class.getDeclaredMethod("updateActiveEUIOFaces");
        updateFaces.setAccessible(true);
        updateFaces.invoke(base);

        tick.set(1);
        offer(root);
        Node graph = root.getNode();
        assertTrue(
            graph.mConsumers.stream()
                .noneMatch(c -> c.mTileEntity == base),
            "East face starts as output");
        updateFaces.invoke(base);
        assertTrue(graph.isNodeMapValid(), "Unchanged faces must not invalidate the graph");

        base.disableWorking();
        updateFaces.invoke(base);
        assertFalse(graph.isNodeMapValid());
        tick.addAndGet(10);
        offer(root);
        assertTrue(
            root.getNode().mConsumers.stream()
                .anyMatch(c -> c.mTileEntity == base));
    }

    private static String key(int x, int z) {
        return x + "," + z;
    }

    private BaseMetaPipeEntity pipe(int x, int z, ForgeDirection... sides) {
        BaseMetaPipeEntity base = new BaseMetaPipeEntity();
        base.xCoord = x;
        base.yCoord = 64;
        base.zCoord = z;
        base.setWorldObj(world);
        MTECable cable = new MTECable("ownership", .5f, null, 0, 64, 128, false, false);
        for (ForgeDirection side : sides) cable.mConnections |= (byte) side.flag;
        cable.setBaseMetaTileEntity(base);
        base.mConnections = cable.mConnections;
        tiles.put(key(x, z), base);
        return base;
    }

    private TileEntity receiver(int x, int z, AtomicLong energy) {
        TileEntity tile = mock(TileEntity.class, withSettings().extraInterfaces(IEnergyConnected.class));
        tile.xCoord = x;
        tile.yCoord = 64;
        tile.zCoord = z;
        when(tile.getWorldObj()).thenReturn(world);
        IEnergyConnected sink = (IEnergyConnected) tile;
        when(sink.inputEnergyFrom(any(), eq(false))).thenReturn(true);
        when(sink.injectEnergyUnits(any(), anyLong(), anyLong())).thenAnswer(call -> {
            energy.addAndGet((long) call.getArgument(1));
            return 1L;
        });
        tiles.put(key(x, z), tile);
        return tile;
    }

    private static long offer(BaseMetaPipeEntity base) {
        return ((MTECable) base.getMetaTileEntity()).transferElectricity(UNKNOWN, 32, 1, null);
    }

    private BaseMetaTileEntity source(BaseMetaPipeEntity root) {
        BaseMetaTileEntity source = mock(BaseMetaTileEntity.class);
        when(source.isServerSide()).thenReturn(true);
        when(source.isEnetOutput()).thenReturn(true);
        when(source.outputsEnergyTo(EAST, false)).thenReturn(true);
        when(source.getIGregTechTileEntityAtSide(EAST)).thenReturn(root);
        doCallRealMethod().when(source)
            .onMachineBlockUpdate();
        tiles.put(key(root.xCoord - 1, root.zCoord), source);
        return source;
    }

    private void delayedRefresh(BaseMetaTileEntity source) throws Exception {
        RunnableCableUpdate.endTick();
        tick.addAndGet(20);
        var generate = BaseMetaTileEntity.class.getDeclaredMethod("generatePowerNodes", boolean.class);
        generate.setAccessible(true);
        generate.invoke(source, false);
    }
}
