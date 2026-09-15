package gregtech.api.metatileentity;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.GregTechAPI;
import gregtech.api.graphs.PowerNode;
import gregtech.api.metatileentity.implementations.MTECable;

class BaseMetaPipeEntityTest {

    @Test
    void connectionChangeClearsCachedPowerNode() {
        final BaseMetaPipeEntity pipe = new BaseMetaPipeEntity();
        final World world = mock(World.class);
        pipe.setWorldObj(world);
        pipe.xCoord = 10;
        pipe.yCoord = 20;
        pipe.zCoord = 30;
        pipe.mConnections = (byte) (ForgeDirection.WEST.flag | ForgeDirection.EAST.flag);
        pipe.mMetaTileEntity = mock(MetaPipeEntity.class);
        pipe.mMetaTileEntity.mConnections = (byte) ForgeDirection.EAST.flag;

        final MinecraftServer server = mock(MinecraftServer.class);
        try (MockedStatic<MinecraftServer> minecraft = mockStatic(MinecraftServer.class);
            MockedStatic<GregTechAPI> api = mockStatic(GregTechAPI.class)) {
            minecraft.when(MinecraftServer::getServer)
                .thenReturn(server);
            when(server.getTickCounter()).thenReturn(1);
            pipe.setNode(new PowerNode(0, pipe, new ArrayList<>()));

            pipe.updateConnections();

            assertNull(pipe.getNode());
            api.verify(() -> GregTechAPI.causeCableUpdate(world, 10, 20, 30));
            api.verifyNoMoreInteractions();
        }
    }

    @Test
    void cableConnectionChangeImmediatelyRebuildsPowerNode() {
        final BaseMetaPipeEntity pipe = new BaseMetaPipeEntity();
        final World world = mock(World.class);
        pipe.setWorldObj(world);
        pipe.xCoord = 10;
        pipe.yCoord = 20;
        pipe.zCoord = 30;
        pipe.mConnections = (byte) ForgeDirection.WEST.flag;
        pipe.mMetaTileEntity = mock(MTECable.class);
        pipe.mMetaTileEntity.mConnections = 0;

        final MinecraftServer server = mock(MinecraftServer.class);
        try (MockedStatic<MinecraftServer> minecraft = mockStatic(MinecraftServer.class);
            MockedStatic<GregTechAPI> api = mockStatic(GregTechAPI.class)) {
            minecraft.when(MinecraftServer::getServer)
                .thenReturn(server);
            when(server.getTickCounter()).thenReturn(1);
            final PowerNode oldNode = new PowerNode(0, pipe, new ArrayList<>());
            pipe.setNode(oldNode);

            pipe.updateConnections();

            assertNotSame(oldNode, pipe.getNode());
            api.verify(() -> GregTechAPI.causeCableUpdate(world, 10, 20, 30));
            api.verifyNoMoreInteractions();
        }
    }
}
