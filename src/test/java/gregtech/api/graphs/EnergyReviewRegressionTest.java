package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.common.covers.Cover;
import ic2.api.energy.tile.IEnergySink;

class EnergyReviewRegressionTest {

    @Test
    void rerootingCompressedCableRetiresOldGraphBeforeRemoval() {
        try (var servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            BaseMetaPipeEntity[] pipes = new BaseMetaPipeEntity[4];
            for (int x = 0; x < pipes.length; x++) {
                pipes[x] = pipe(world, x, ForgeDirection.WEST, ForgeDirection.EAST);
                when(world.getTileEntity(x, 64, 0)).thenReturn(pipes[x]);
            }
            TileEntity left = receiver(-1, 0);
            TileEntity right = receiver(4, 0);
            when(world.getTileEntity(-1, 64, 0)).thenReturn(left);
            when(world.getTileEntity(4, 64, 0)).thenReturn(right);
            MTECable a = (MTECable) pipes[1].getMetaTileEntity();
            MTECable b = (MTECable) pipes[2].getMetaTileEntity();
            assertEquals(2, a.transferElectricity(ForgeDirection.UNKNOWN, 32, 2, null));
            Node original = pipes[1].getNode();
            assertNull(pipes[2].getNode());
            assertNotNull(pipes[2].getNodePath());

            assertEquals(2, b.transferElectricity(ForgeDirection.UNKNOWN, 32, 2, null));
            assertTrue(original.mInvalid);
            assertNull(pipes[1].getNode());
            clearInvocations(left, right);
            pipes[2].invalidate();
            when(world.getTileEntity(2, 64, 0)).thenReturn(null);

            assertEquals(1, a.transferElectricity(ForgeDirection.UNKNOWN, 32, 2, null));
            verify((IEnergyConnected) right, never()).injectEnergyUnits(any(), anyLong(), anyLong());
            verify((IEnergyConnected) left).injectEnergyUnits(any(), eq(32L), anyLong());
        }
    }

    @Test
    void terminalBendKeepsArrivalSideAndItsCoverLock() {
        try (var servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            BaseMetaPipeEntity root = pipe(world, 0, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity bend = spy(pipe(world, 1, ForgeDirection.WEST, ForgeDirection.NORTH));
            MTECable bentCable = (MTECable) bend.getMetaTileEntity();
            bentCable.setBaseMetaTileEntity(bend);
            TileEntity receiver = receiver(1, -1);
            when(world.getTileEntity(1, 64, 0)).thenReturn(bend);
            when(world.getTileEntity(1, 64, -1)).thenReturn(receiver);
            Cover cover = mock(Cover.class);
            when(cover.isValid()).thenReturn(true);
            when(cover.letsEnergyOut()).thenReturn(true);
            when(bend.getCoverAtSide(ForgeDirection.WEST)).thenReturn(cover);
            new GenerateNodeMapPower(root);
            assertSame(root.getNode(), bend.getNode().mNeighbourNodes[ForgeDirection.WEST.ordinal()]);
            assertNull(bend.getNode().mNeighbourNodes[ForgeDirection.SOUTH.ordinal()]);
            assertTrue(bend.getNode().locks[ForgeDirection.WEST.ordinal()].isLocked());
            MTECable cable = (MTECable) root.getMetaTileEntity();
            assertEquals(0, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 1, null));
            verify((IEnergyConnected) receiver, never()).injectEnergyUnits(any(), anyLong(), anyLong());
            when(cover.letsEnergyIn()).thenReturn(true);
            bentCable.reloadLocks();
            assertEquals(1, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 1, null));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void lockedSubtreeAllowsProgressPastTwentyThreeConsumers(boolean above) {
        try (var servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            ArrayList<ConsumerNode> consumers = new ArrayList<>();
            Node root = new Node(1, mock(TileEntity.class), consumers);
            Node branch = new Node(2, mock(TileEntity.class), consumers);
            for (int i = 3; i <= 25; i++) {
                consumers.add(new ConsumerNode(i, mock(TileEntity.class), ForgeDirection.WEST, consumers));
            }
            ConsumerNode tail = spy(new ConsumerNode(26, mock(TileEntity.class), ForgeDirection.WEST, consumers));
            when(tail.injectEnergy(32, 4)).thenReturn(1);
            consumers.add(tail);
            root.mHighestNodeValue = 26;
            branch.mHighestNodeValue = 25;
            root.mNeighbourNodes[0] = branch;
            root.mNodePaths[0] = new PowerNodePath(new MetaPipeEntity[0]);
            root.locks[0] = new Lock();
            root.locks[0].addTileEntity(null);
            root.mNeighbourNodes[5] = tail;
            root.locks[5] = new Lock();
            root.mNodePaths[5] = new PowerNodePath(new MetaPipeEntity[0]);
            NodeList selected = new NodeList(consumers.toArray(new Node[0]));
            assertEquals(
                1,
                above ? PowerNodes.powerNodeAbove(root, null, selected, 32, 4)
                    : PowerNodes.powerNode(root, null, selected, 32, 4));
            verify(tail).injectEnergy(32, 4);
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void ic2StopsPacketsAfterDemandOrInjectionInvalidatesGraph(boolean duringDemand) {
        try (var servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            BaseMetaPipeEntity root = pipe(world, 0, ForgeDirection.WEST, ForgeDirection.EAST);
            TileEntity tile = mock(TileEntity.class, withSettings().extraInterfaces(IEnergySink.class));
            tile.xCoord = 1;
            tile.yCoord = 64;
            when(tile.getWorldObj()).thenReturn(world);
            when(world.getTileEntity(1, 64, 0)).thenReturn(tile);
            when(world.getTileEntity(0, 64, 0)).thenReturn(root);
            IEnergySink sink = (IEnergySink) tile;
            when(sink.acceptsEnergyFrom(any(), any())).thenReturn(true);
            when(sink.getDemandedEnergy()).thenAnswer(call -> {
                if (duringDemand) root.invalidate();
                return 128.0;
            });
            when(sink.injectEnergy(any(), eq(32.0), eq(32.0))).thenAnswer(call -> {
                root.invalidate();
                return 0.0;
            });
            MTECable cable = (MTECable) root.getMetaTileEntity();
            assertEquals(duringDemand ? 0 : 1, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            verify(sink, times(duringDemand ? 0 : 1)).injectEnergy(any(), eq(32.0), eq(32.0));
            verify(sink, times(1)).getDemandedEnergy();
        }
    }

    private static BaseMetaPipeEntity pipe(World world, int x, ForgeDirection... sides) {
        BaseMetaPipeEntity base = new BaseMetaPipeEntity();
        base.xCoord = x;
        base.yCoord = 64;
        base.setWorldObj(world);
        MTECable cable = new MTECable("review", 0.5f, null, 0, 4, 128, false, false);
        for (ForgeDirection side : sides) cable.mConnections |= (byte) side.flag;
        cable.setBaseMetaTileEntity(base);
        return base;
    }

    private static TileEntity receiver(int x, int z) {
        TileEntity tile = mock(TileEntity.class, withSettings().extraInterfaces(IEnergyConnected.class));
        tile.xCoord = x;
        tile.yCoord = 64;
        tile.zCoord = z;
        IEnergyConnected receiver = (IEnergyConnected) tile;
        when(receiver.inputEnergyFrom(any(), eq(false))).thenReturn(true);
        when(receiver.injectEnergyUnits(any(), eq(32L), anyLong())).thenReturn(1L);
        return tile;
    }
}
