package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.consumers.NodeEnergyConnected;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.common.covers.Cover;
import ic2.api.energy.tile.IEnergySink;

class EnergyConsumerLifecycleTest {

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

    @Test
    void middleChunkUnloadLeavesStaleGraphUntilExplicitRebuild_currentKnownIssue() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            BaseMetaPipeEntity root = pipe(world, 15, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity middle = pipe(world, 16, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity end = pipe(world, 17, ForgeDirection.WEST, ForgeDirection.EAST);
            TileEntity receiver = receiver();
            receiver.xCoord = 18;
            receiver.yCoord = 64;
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            when(world.getTileEntity(16, 64, 0)).thenReturn(middle);
            when(world.getTileEntity(17, 64, 0)).thenReturn(end);
            when(world.getTileEntity(18, 64, 0)).thenReturn(receiver);
            new GenerateNodeMapPower(root);
            Node stale = root.getNode();
            assertSame(receiver, stale.mConsumers.get(0).mTileEntity);
            assertNotNull(middle.getNodePath());
            middle.onChunkUnload();
            assertTrue(middle.isDead());
            assertSame(stale, root.getNode());
            assertNotNull(middle.getNodePath());
            when(world.blockExists(16, 64, 0)).thenReturn(false);
            // Known stale-route behavior, not the intended future lifecycle contract.
            assertEquals(
                1,
                ((MTECable) root.getMetaTileEntity()).transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            GenerateNodeMap.clearNodeMap(stale, -1);
            assertNull(middle.getNodePath());
            assertNull(end.getNode());
            new GenerateNodeMapPower(root);
            assertTrue(
                root.getNode().mConsumers.stream()
                    .noneMatch(node -> node.mTileEntity == receiver));
            when(world.blockExists(16, 64, 0)).thenReturn(true);
            BaseMetaPipeEntity reloaded = pipe(world, 16, ForgeDirection.WEST, ForgeDirection.EAST);
            when(world.getTileEntity(16, 64, 0)).thenReturn(reloaded);
            GenerateNodeMap.clearNodeMap(root.getNode(), -1);
            new GenerateNodeMapPower(root);
            assertSame(receiver, root.getNode().mConsumers.get(0).mTileEntity);
            assertNotNull(reloaded.getNodePath());
        }
    }

    @Test
    void ic2EmitterIdentityAtChunkBorderRecordsInvertedLoadedCheck_currentKnownIssue() {
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
            verify(sink).acceptsEnergyFrom(null, ForgeDirection.WEST);
            verify(world, never()).getTileEntity(anyInt(), anyInt(), anyInt());
            when(world.blockExists(15, 0, 0)).thenReturn(false);
            assertTrue(map.addConsumer(tile, ForgeDirection.WEST, 2, new ArrayList<>()));
            verify(sink).acceptsEnergyFrom(emitter, ForgeDirection.WEST);
            verify(world).getTileEntity(15, 0, 0);
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
