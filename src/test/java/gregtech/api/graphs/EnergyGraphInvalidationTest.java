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
import org.junit.jupiter.params.provider.CsvSource;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;

class EnergyGraphInvalidationTest {

    @ParameterizedTest
    @CsvSource({ "false, 0", "false, 1", "false, 2", "true, 0", "true, 1", "true, 2" })
    void burningCableStopsTransferInEitherDirection(boolean reverse, int burningIndex) {
        try (var servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            BaseMetaPipeEntity[] pipes = new BaseMetaPipeEntity[3];
            for (int i = 0; i < pipes.length; i++) {
                pipes[i] = pipe(world, i, i == burningIndex ? 32 : 128, ForgeDirection.WEST, ForgeDirection.EAST);
                when(world.getTileEntity(i, 64, 0)).thenReturn(pipes[i]);
            }
            TileEntity receiver = mock(TileEntity.class, withSettings().extraInterfaces(IEnergyConnected.class));
            receiver.xCoord = reverse ? -1 : 3;
            receiver.yCoord = 64;
            when(world.getTileEntity(receiver.xCoord, 64, 0)).thenReturn(receiver);
            when(((IEnergyConnected) receiver).inputEnergyFrom(any(), eq(false))).thenReturn(true);
            when(world.setBlock(eq(burningIndex), eq(64), eq(0), any())).thenAnswer(call -> {
                pipes[burningIndex].invalidate();
                return true;
            });
            new GenerateNodeMapPower(pipes[0]);
            Node original = pipes[0].getNode();
            MTECable entry = (MTECable) pipes[reverse ? 2 : 0].getMetaTileEntity();

            assertEquals(0, entry.transferElectricity(ForgeDirection.UNKNOWN, 128, 4, null));

            assertTrue(original.mInvalid);
            for (BaseMetaPipeEntity pipe : pipes) assertNull(pipe.getNodePath());
            verify((IEnergyConnected) receiver, never()).injectEnergyUnits(any(), anyLong(), anyLong());
        }
    }

    @Test
    void burningEntryDoesNotDereferenceClearedDeadEndPath() {
        try (var servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            BaseMetaPipeEntity root = pipe(world, 0, 32, ForgeDirection.WEST, ForgeDirection.EAST);
            BaseMetaPipeEntity dead = pipe(world, 1, 32, ForgeDirection.WEST);
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            when(world.getTileEntity(1, 64, 0)).thenReturn(dead);
            when(world.setBlock(eq(0), eq(64), eq(0), any())).thenAnswer(call -> {
                root.invalidate();
                return true;
            });
            MTECable cable = (MTECable) root.getMetaTileEntity();
            assertEquals(0, cable.transferElectricity(ForgeDirection.WEST, 128, 1, null));
            assertNull(dead.getNodePath());
        }
    }

    @Test
    void receiverInvalidationPreservesAcceptedAmpsAndStopsRemainingConsumers() {
        try (var servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
            MTECable cable = EnergyTransferTest.cable(MTECable.class, base);
            ConsumerNode first = spy(new ConsumerNode(2, base, ForgeDirection.WEST, new ArrayList<>()));
            ConsumerNode second = spy(new ConsumerNode(3, base, ForgeDirection.WEST, new ArrayList<>()));
            PowerNode root = EnergyTransferTest.root(base, first, second);
            when(first.injectEnergy(32, 4)).thenAnswer(call -> {
                GenerateNodeMap.clearNodeMap(root, -1);
                return 1;
            });

            assertEquals(1, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));

            verify(second, never()).injectEnergy(anyLong(), anyLong());
            assertTrue(root.mInvalid);
        }
    }

    private static BaseMetaPipeEntity pipe(World world, int x, long voltage, ForgeDirection... sides) {
        BaseMetaPipeEntity base = new BaseMetaPipeEntity();
        base.xCoord = x;
        base.yCoord = 64;
        base.setWorldObj(world);
        MTECable cable = new MTECable("invalidation", 0.5f, null, 0, 4, voltage, false, false);
        for (ForgeDirection side : sides) cable.mConnections |= (byte) side.flag;
        cable.setBaseMetaTileEntity(base);
        return base;
    }
}
