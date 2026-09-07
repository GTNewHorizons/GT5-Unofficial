package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;

class EnergyGraphConstructionTest {

    @Test
    void connectedRunKeepsOrderedMembersAndLossAcrossChunkBorder() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            BaseMetaPipeEntity root = pipe(1, ForgeDirection.DOWN, ForgeDirection.EAST);
            BaseMetaPipeEntity dead = pipe(2, ForgeDirection.UP);
            BaseMetaPipeEntity run = pipe(3, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity end = pipe(4, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaTileEntity receiver = mock(BaseMetaTileEntity.class);
            when(receiver.inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(true);
            root.xCoord = 15;
            run.xCoord = 16;
            when(root.getTileEntityAtSide(ForgeDirection.DOWN)).thenReturn(dead);
            when(root.getTileEntityAtSide(ForgeDirection.EAST)).thenReturn(run);
            when(run.getTileEntityAtSide(ForgeDirection.EAST)).thenReturn(end);
            when(end.getTileEntityAtSide(ForgeDirection.EAST)).thenReturn(receiver);
            new GenerateNodeMapPower(root);
            Node node = root.getNode();
            assertEquals(2, node.mConsumers.size());
            assertSame(dead, node.mConsumers.get(0).mTileEntity);
            assertSame(receiver, node.mConsumers.get(1).mTileEntity);
            assertEquals(4, node.mHighestNodeValue);
            PowerNodePath path = (PowerNodePath) node.mNodePaths[ForgeDirection.EAST.ordinal()];
            assertArrayEquals(new Object[] { run.getMetaTileEntity() }, path.getPipes());
            assertEquals(3, path.getLoss());
            assertEquals(4, ((PowerNodePath) end.getNode().mSelfPath).getLoss());
            GenerateNodeMap.clearNodeMap(node, -1);
            assertNull(root.getNode());
            assertNull(end.getNode());
        }
    }

    @Test
    void disconnectedSideLookupCount() {
        GenerateNodeMap map = mock(GenerateNodeMap.class, CALLS_REAL_METHODS);
        BaseMetaPipeEntity base = pipe(1, ForgeDirection.EAST);
        map.generateNextNode(base, null, ForgeDirection.UNKNOWN, 1, new ArrayList<>(), new HashSet<>());
        verify(base, times(1)).getTileEntityAtSide(any());
        verify(base).getTileEntityAtSide(ForgeDirection.EAST);
    }

    private static BaseMetaPipeEntity pipe(long loss, ForgeDirection... sides) {
        BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
        MTECable cable = spy(new MTECable("test", 0.5f, null, loss, 4, 128, false, false));

        when(base.getMetaTileEntity()).thenReturn(cable);
        when(cable.getBaseMetaTileEntity()).thenReturn(base);
        for (ForgeDirection side : sides) when(cable.isConnectedAtSide(side)).thenReturn(true);
        doAnswer(call -> {
            when(base.getNode()).thenReturn(call.getArgument(0));
            return null;
        }).when(base)
            .setNode(any());
        return base;
    }

    @Test
    void branchAndLoopKeepDfsIdsCompressionAndLossesAtChunkBorder() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            BaseMetaPipeEntity root = pipe(1, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.EAST);
            BaseMetaPipeEntity down = pipe(2, ForgeDirection.UP);
            BaseMetaPipeEntity up = pipe(3, ForgeDirection.DOWN);
            BaseMetaPipeEntity run = pipe(4, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity loop = pipe(5, ForgeDirection.WEST, ForgeDirection.NORTH);
            root.xCoord = 15;
            run.xCoord = 16;
            when(root.getTileEntityAtSide(ForgeDirection.DOWN)).thenReturn(down);
            when(root.getTileEntityAtSide(ForgeDirection.UP)).thenReturn(up);
            when(root.getTileEntityAtSide(ForgeDirection.EAST)).thenReturn(run);
            when(run.getTileEntityAtSide(ForgeDirection.EAST)).thenReturn(loop);
            when(loop.getTileEntityAtSide(ForgeDirection.NORTH)).thenReturn(root);
            new GenerateNodeMapPower(root);
            Node node = root.getNode();
            assertEquals(1, node.mNodeValue);
            assertEquals(2, node.mConsumers.size());
            assertSame(down, node.mConsumers.get(0).mTileEntity);
            assertSame(up, node.mConsumers.get(1).mTileEntity);
            assertEquals(2, down.getNode().mNodeValue);
            assertEquals(3, up.getNode().mNodeValue);
            assertEquals(3, node.mHighestNodeValue);
            assertEquals(1, ((PowerNodePath) node.mSelfPath).getLoss());
            assertEquals(2, ((PowerNodePath) down.getNode().mSelfPath).getLoss());
            assertEquals(3, ((PowerNodePath) up.getNode().mSelfPath).getLoss());
            assertNull(node.mNeighbourNodes[ForgeDirection.EAST.ordinal()]);
            assertEquals(0, node.mNodePaths[ForgeDirection.DOWN.ordinal()].getPipes().length);
        }
    }

    @Test
    void nullMetaStillLooksUpEveryNonExcludedSide() {
        GenerateNodeMap map = mock(GenerateNodeMap.class, CALLS_REAL_METHODS);
        BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
        map.generateNextNode(base, null, ForgeDirection.DOWN, 1, new ArrayList<>(), new HashSet<>());
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            verify(base, times(side == ForgeDirection.DOWN ? 0 : 1)).getTileEntityAtSide(side);
        }
        verify(base).reloadLocks();
    }
}
