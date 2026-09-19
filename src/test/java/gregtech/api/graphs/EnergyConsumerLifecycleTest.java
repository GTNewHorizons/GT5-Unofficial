package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.consumers.NodeEnergyConnected;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.threads.RunnableCableUpdate;
import gregtech.common.covers.Cover;
import ic2.api.energy.tile.IEnergySink;

class EnergyConsumerLifecycleTest {

    @BeforeEach
    @AfterEach
    void clearManagedCables() {
        BaseMetaPipeEntity.clearManagedCables();
    }

    @Test
    void sideChangesAndReplacementAreRecheckedAfterGraphRebuild() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            BaseMetaPipeEntity pipe = pipe(world, 15, ForgeDirection.WEST, ForgeDirection.EAST);
            TileEntity first = receiver();
            TileEntity replacement = receiver();
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            when(world.getTileEntity(16, 64, 0)).thenReturn(first);
            new GenerateNodeMapPower(pipe);
            ConsumerNode original = pipe.getNode().mConsumers.get(0);
            assertSame(first, original.mTileEntity);
            when(((IEnergyConnected) first).inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(false);
            // The endpoint owns injection-side validation even while its old node is still selected.
            when(((IEnergyConnected) first).injectEnergyUnits(ForgeDirection.WEST, 32, 4)).thenReturn(0L);
            assertEquals(
                0,
                ((MTECable) pipe.getMetaTileEntity()).transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            GenerateNodeMap.clearNodeMap(pipe.getNode(), -1);
            new GenerateNodeMapPower(pipe);
            assertTrue(
                pipe.getNode().mConsumers.stream()
                    .noneMatch(node -> node.mTileEntity == first));
            when(first.isInvalid()).thenReturn(true);
            assertFalse(original.needsEnergy());
            when(world.getTileEntity(16, 64, 0)).thenReturn(replacement);
            GenerateNodeMap.clearNodeMap(pipe.getNode(), -1);
            new GenerateNodeMapPower(pipe);
            assertSame(replacement, pipe.getNode().mConsumers.get(0).mTileEntity);
            assertEquals(
                1,
                ((MTECable) pipe.getMetaTileEntity()).transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void middleChunkReloadRebuildsExternalOnlyNetwork(boolean reuseTiles) {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            MinecraftServer server = mock(MinecraftServer.class);
            servers.when(MinecraftServer::getServer)
                .thenReturn(server);
            World world = mock(World.class);
            BaseMetaPipeEntity root = pipe(world, 15, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity middle = pipe(world, 16, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity end = pipe(world, 17, ForgeDirection.WEST, ForgeDirection.EAST);
            middle.mTickTimer = 21;
            middle.mConnections = ((MTECable) middle.getMetaTileEntity()).mConnections;
            TileEntity receiver = receiver();
            receiver.xCoord = 18;
            receiver.yCoord = 64;
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            when(world.getTileEntity(15, 64, 0)).thenReturn(root);
            when(world.getTileEntity(16, 64, 0)).thenReturn(middle);
            when(world.getTileEntity(17, 64, 0)).thenReturn(end);
            when(world.getTileEntity(18, 64, 0)).thenReturn(receiver);
            new GenerateNodeMapPower(root);
            assertNotNull(middle.getNodePath());
            MTECable cable = (MTECable) root.getMetaTileEntity();
            assertEquals(1, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            clearInvocations(receiver);

            when(world.blockExists(16, 64, 0)).thenReturn(false);
            middle.onChunkUnload();

            assertTrue(middle.isDead());
            assertNull(root.getNode());
            assertNull(middle.getNodePath());
            assertNull(end.getNodePath());
            assertEquals(
                0,
                ((MTECable) root.getMetaTileEntity()).transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            verify((IEnergyConnected) receiver, never()).injectEnergyUnits(any(), anyLong(), anyLong());

            BaseMetaPipeEntity.clearManagedCables();
            BaseMetaPipeEntity reloaded = reuseTiles ? middle
                : pipe(world, 16, ForgeDirection.WEST, ForgeDirection.EAST);
            MTECable reloadedCable = spy((MTECable) reloaded.getMetaTileEntity());
            reloadedCable.setBaseMetaTileEntity(reloaded);
            doReturn(true).when(reloadedCable)
                .getGT6StyleConnection();
            doReturn(false).when(reloadedCable)
                .shouldJoinIc2Enet();
            doReturn(1).when(reloadedCable)
                .connect(any());
            when(world.blockExists(16, 64, 0)).thenReturn(true);
            when(world.getTileEntity(16, 64, 0)).thenReturn(reloaded);
            when(server.getTickCounter()).thenReturn(1);
            if (reuseTiles) reloaded.onChunkLoad();
            else reloaded.validate();
            BaseMetaPipeEntity.tickManagedCables();
            RunnableCableUpdate.endTick();
            assertNull(root.getNode());
            assertEquals(
                1,
                ((MTECable) root.getMetaTileEntity()).transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            assertNotNull(reloaded.getNodePath());
            if (reuseTiles) {
                Node restored = root.getNode();
                BaseMetaPipeEntity.tickManagedCables();
                RunnableCableUpdate.endTick();
                assertSame(restored, root.getNode());
            }
        }
    }

    @Test
    void ic2EmitterIdentityAtChunkBorderOnlyReadsLoadedNeighbor() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            GenerateNodeMapPower map = mock(GenerateNodeMapPower.class, CALLS_REAL_METHODS);
            TileEntity tile = mock(TileEntity.class, withSettings().extraInterfaces(IEnergySink.class));
            IEnergySink sink = (IEnergySink) tile;
            World world = mock(World.class);
            TileEntity emitter = mock(TileEntity.class);
            tile.xCoord = 16;
            when(tile.getWorldObj()).thenReturn(world);
            when(world.getTileEntity(15, 0, 0)).thenReturn(emitter);
            when(sink.acceptsEnergyFrom(any(), eq(ForgeDirection.WEST))).thenReturn(true);
            when(world.blockExists(15, 0, 0)).thenReturn(true);
            assertTrue(map.addConsumer(tile, ForgeDirection.WEST, 2, new ArrayList<>()));
            verify(sink).acceptsEnergyFrom(emitter, ForgeDirection.WEST);
            verify(world).getTileEntity(15, 0, 0);
            clearInvocations(sink, world);
            when(world.blockExists(15, 0, 0)).thenReturn(false);
            assertTrue(map.addConsumer(tile, ForgeDirection.WEST, 2, new ArrayList<>()));
            verify(sink).acceptsEnergyFrom(null, ForgeDirection.WEST);
            verify(world, never()).getTileEntity(anyInt(), anyInt(), anyInt());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void topologyChangeDuringIc2CallbackRetriesOnlyUnspentAmps(boolean duringDemand) {
        try (var servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            BaseMetaPipeEntity base = pipe(world, 0, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST);
            MTECable cable = (MTECable) base.getMetaTileEntity();
            base.mConnections = cable.mConnections;
            TileEntity first = mock(TileEntity.class, withSettings().extraInterfaces(IEnergySink.class));
            first.yCoord = 64;
            first.zCoord = -1;
            when(first.getWorldObj()).thenReturn(world);
            when(world.getTileEntity(0, 64, -1)).thenReturn(first);
            when(world.getTileEntity(0, 64, 0)).thenReturn(base);
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            IEnergySink sink = (IEnergySink) first;
            when(sink.acceptsEnergyFrom(any(), any())).thenReturn(true);
            when(sink.getDemandedEnergy()).thenAnswer(call -> {
                if (duringDemand) cable.disconnect(ForgeDirection.NORTH);
                return 128.0;
            });
            when(sink.injectEnergy(any(), eq(32.0), eq(32.0))).thenAnswer(call -> {
                cable.disconnect(ForgeDirection.NORTH);
                return 0.0;
            });
            TileEntity second = receiver();
            second.xCoord = 1;
            second.yCoord = 64;
            IEnergyConnected receiver = (IEnergyConnected) second;
            when(receiver.injectEnergyUnits(any(), eq(32L), anyLong())).thenAnswer(call -> call.getArgument(2));
            when(world.getTileEntity(1, 64, 0)).thenReturn(second);

            assertEquals(4, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            verify(sink, times(duringDemand ? 0 : 1)).injectEnergy(any(), eq(32.0), eq(32.0));
            verify(sink, times(1)).getDemandedEnergy();
            verify(receiver).injectEnergyUnits(ForgeDirection.WEST, 32, duringDemand ? 4 : 3);
        }
    }

    @Test
    void graphGenerationDefersSameTickReplacementUntilNextTick() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            MinecraftServer server = mock(MinecraftServer.class);
            servers.when(MinecraftServer::getServer)
                .thenReturn(server);
            World world = mock(World.class);
            BaseMetaPipeEntity pipe = pipe(world, 15, ForgeDirection.WEST, ForgeDirection.EAST);
            pipe.mConnections = (byte) (ForgeDirection.WEST.flag | ForgeDirection.EAST.flag);
            TileEntity first = receiver();
            TileEntity replacement = receiver();
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            when(world.getTileEntity(16, 64, 0)).thenReturn(first);
            BaseMetaTileEntity source = mock(BaseMetaTileEntity.class);
            when(source.isServerSide()).thenReturn(true);
            when(source.isEnetOutput()).thenReturn(true);
            when(source.outputsEnergyTo(ForgeDirection.EAST, false)).thenReturn(true);
            when(source.getIGregTechTileEntityAtSide(ForgeDirection.EAST)).thenReturn(pipe);
            doCallRealMethod().when(source)
                .generatePowerNodes();
            source.generatePowerNodes();
            Node initial = pipe.getNode();
            when(world.getTileEntity(16, 64, 0)).thenReturn(replacement);
            source.generatePowerNodes();
            assertSame(initial, pipe.getNode());
            when(server.getTickCounter()).thenReturn(1);
            source.generatePowerNodes();
            assertNotSame(initial, pipe.getNode());
            assertSame(replacement, pipe.getNode().mConsumers.get(0).mTileEntity);
        }
    }

    @Test
    void coverLockCanRejectAndReopenSameGraphWithoutCachingDemand() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            BaseMetaPipeEntity base = spy(pipe(mock(World.class), 15, ForgeDirection.DOWN));
            MTECable cable = (MTECable) base.getMetaTileEntity();
            cable.setBaseMetaTileEntity(base);
            TileEntity receiver = receiver();
            PowerNode root = EnergyTransferTest.root(
                base,
                new NodeEnergyConnected(2, (IEnergyConnected) receiver, ForgeDirection.WEST, new ArrayList<>()));
            base.setNode(root);
            root.mHadVoltage = true;
            Cover cover = mock(Cover.class);
            when(base.getCoverAtSide(ForgeDirection.DOWN)).thenReturn(cover);
            when(cover.isValid()).thenReturn(true);
            when(cover.letsEnergyOut()).thenReturn(true);
            cable.reloadLocks();
            assertTrue(root.locks[0].isLocked());
            assertEquals(0, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            verify((IEnergyConnected) receiver, never()).injectEnergyUnits(any(), anyLong(), anyLong());
            when(cover.letsEnergyIn()).thenReturn(true);
            cable.reloadLocks();
            assertFalse(root.locks[0].isLocked());
            assertEquals(1, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            assertSame(root, base.getNode());
        }
    }

    @Test
    void connectionChangesInvalidateRoutesButUnchangedConnectionsKeepThem() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            MinecraftServer server = mock(MinecraftServer.class);
            servers.when(MinecraftServer::getServer)
                .thenReturn(server);
            World world = mock(World.class);
            BaseMetaPipeEntity root = pipe(world, 15, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity end = pipe(world, 16, ForgeDirection.WEST, ForgeDirection.EAST);
            TileEntity receiver = receiver();
            receiver.xCoord = 17;
            receiver.yCoord = 64;
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            when(world.getTileEntity(15, 64, 0)).thenReturn(root);
            when(world.getTileEntity(16, 64, 0)).thenReturn(end);
            when(world.getTileEntity(17, 64, 0)).thenReturn(receiver);
            MTECable cable = (MTECable) root.getMetaTileEntity();
            MTECable endCable = (MTECable) end.getMetaTileEntity();
            root.mConnections = cable.mConnections;
            assertEquals(1, cable.transferElectricity(ForgeDirection.WEST, 32, 4, null));
            Node original = root.getNode();
            root.updateConnections();
            assertSame(original, root.getNode());

            cable.disconnect(ForgeDirection.EAST);
            root.updateConnections();
            assertSame(original, root.getNode());
            assertFalse(original.isNodeMapValid());
            assertEquals(0, cable.transferElectricity(ForgeDirection.WEST, 32, 4, null));

            cable.mConnections |= ForgeDirection.EAST.flag;
            endCable.mConnections |= ForgeDirection.WEST.flag;
            root.updateConnections();
            RunnableCableUpdate.endTick();
            assertEquals(0, cable.transferElectricity(ForgeDirection.WEST, 32, 4, null));
            when(server.getTickCounter()).thenReturn(10);
            assertEquals(1, cable.transferElectricity(ForgeDirection.WEST, 32, 4, null));
        }
    }

    private static BaseMetaPipeEntity pipe(World world, int x, ForgeDirection... sides) {
        BaseMetaPipeEntity base = new BaseMetaPipeEntity();
        base.xCoord = x;
        base.yCoord = 64;
        base.setWorldObj(world);
        MTECable cable = new MTECable("lifecycle", 0.5f, null, 0, 4, 128, false, false);
        for (ForgeDirection side : sides) cable.mConnections |= (byte) side.flag;
        cable.setBaseMetaTileEntity(base);
        return base;
    }

    private static TileEntity receiver() {
        TileEntity tile = mock(TileEntity.class, withSettings().extraInterfaces(IEnergyConnected.class));
        when(((IEnergyConnected) tile).inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(true);
        when(((IEnergyConnected) tile).injectEnergyUnits(ForgeDirection.WEST, 32, 4)).thenReturn(1L);
        return tile;
    }
}
