package gregtech.api.metatileentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.HarvestTool;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.common.covers.Cover;
import io.netty.buffer.Unpooled;

class BaseMetaPipeEntityTest {

    @Test
    void gt5CableDiscoversNeighborLoadedWithoutBlockNotification() {
        BaseMetaPipeEntity.clearManagedCables();
        try (var servers = mockStatic(MinecraftServer.class); var api = mockStatic(GregTechAPI.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            World world = mock(World.class);
            when(world.blockExists(anyInt(), anyInt(), anyInt())).thenReturn(true);
            when(world.getChunkProvider()).thenReturn(mock(IChunkProvider.class));
            when(
                world.getChunkProvider()
                    .chunkExists(anyInt(), anyInt())).thenReturn(true);
            when(world.getBlock(anyInt(), anyInt(), anyInt())).thenReturn(mock(Block.class));
            BaseMetaPipeEntity base = new BaseMetaPipeEntity();
            base.xCoord = 15;
            base.setWorldObj(world);
            when(world.getTileEntity(15, 0, 0)).thenReturn(base);
            MTECable cable = spy(new MTECable("discovery", 0.5f, null, 0, 1, 32, false, false));
            doReturn(false).when(cable)
                .getGT6StyleConnection();
            doReturn(false).when(cable)
                .shouldJoinIc2Enet();
            doReturn(false).when(cable)
                .canConnect(any(), isNull());
            cable.setBaseMetaTileEntity(base);
            BaseMetaPipeEntity.tickManagedCables();
            assertFalse(cable.isConnectedAtSide(ForgeDirection.EAST));

            TileEntity receiver = mock(TileEntity.class, withSettings().extraInterfaces(IEnergyConnected.class));
            receiver.xCoord = 16;
            when(((IEnergyConnected) receiver).inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(true);
            when(world.getTileEntity(16, 0, 0)).thenReturn(receiver);
            for (long tick = 1; tick <= 20; tick++) {
                when(world.getTotalWorldTime()).thenReturn(tick);
                BaseMetaPipeEntity.tickManagedCables();
                assertEquals(tick == 20, cable.isConnectedAtSide(ForgeDirection.EAST));
            }
        } finally {
            BaseMetaPipeEntity.clearManagedCables();
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void clientCableInitializesAfterLoadingAndRequestsDescriptionCoverData(boolean placedLocally) {
        short id = 32760;
        IMetaTileEntity previous = GregTechAPI.METATILEENTITIES[id];
        var packet = Unpooled.buffer();
        try (var registry = mockStatic(CoverRegistry.class)) {
            GregTechAPI.METATILEENTITIES[id] = new MTECable("client", 0.5f, null, 0, 1, 32, false, false);
            World world = mock(World.class);
            world.isRemote = true;
            ClientCable base = new ClientCable();
            base.setWorldObj(world);
            base.validate();
            if (placedLocally) {
                base.setInitialValuesAsNBT(null, id);
                assertTrue(base.getTimer() > 1, "Placed client cable must allow interaction without ticking");
            }
            Cover cover = mock(Cover.class);
            when(cover.isDataNeededOnClient()).thenReturn(true);
            registry.when(() -> CoverRegistry.cover(base, ForgeDirection.UP, 123))
                .thenAnswer(call -> {
                    base.covers[ForgeDirection.UP.ordinal()] = cover;
                    return null;
                });
            packet.writeShort(id)
                .writeByte(ForgeDirection.UP.flag)
                .writeInt(123);
            packet.writeInt(0); // Connections, custom data, redstone and color.
            base.readDescriptionBuffer(packet);
            assertFalse(base.canUpdate());
            assertTrue(base.getTimer() > 1, "Deserialized client cable must allow interaction without ticking");
            assertEquals(1, base.coverRequests, "Request cover data once, after the cover IDs are installed");

            base.validate();
            assertTrue(base.getTimer() > 1, "Validation must not strand a non-ticking cable at timer zero");
            base.setInitialValuesAsNBT(null, id);
            assertTrue(base.getTimer() > 1, "Replacing the metatile entity must restore the interaction timer");
        } finally {
            packet.release();
            GregTechAPI.METATILEENTITIES[id] = previous;
        }
    }

    private static final class ClientCable extends BaseMetaPipeEntity {

        private int coverRequests;

        @Override
        protected void requestCoverDataIfNeeded() {
            if (getCoverAtSide(ForgeDirection.UP).isDataNeededOnClient()) coverRequests++;
        }

        @Override
        public void issueTextureUpdate() {}
    }

    @Test
    void addonCableSubclassesKeepNormalTicking() {
        BaseMetaPipeEntity base = new BaseMetaPipeEntity(HarvestTool.CutterLevel0.toTileEntityBaseType());
        base.setWorldObj(mock(World.class));
        assertFalse(base.canUpdate());
        int[] connectionChecks = { 0 };
        MTECable addon = new MTECable("addon", 0.5f, null, 0, 4, 128, false, false) {

            @Override
            protected void checkConnections() {
                connectionChecks[0]++;
            }
        };
        addon.setBaseMetaTileEntity(base);
        assertTrue(base.canUpdate());
        assertFalse(base.isTickDisabled());
        addon.setCheckConnections();
        addon.onPostTick(base, 20);
        assertEquals(1, connectionChecks[0]);
    }

    @Test
    void managedCableUnloadsAndReactivatesWhenChunkInstanceIsReused() {
        World world = mock(World.class);
        TrackingCable cable = new TrackingCable();
        cable.setWorldObj(world);

        BaseMetaPipeEntity.clearManagedCables();
        try {
            cable.validate();
            BaseMetaPipeEntity.unloadManagedCables(world);
            assertEquals(1, cable.unloads);
            assertTrue(cable.isDead);

            cable.onChunkLoad();
            assertFalse(cable.isDead);
            BaseMetaPipeEntity.unloadManagedCables(world);
            assertEquals(2, cable.unloads);

            BaseMetaPipeEntity.unloadManagedCables(world);
            assertEquals(2, cable.unloads);
        } finally {
            BaseMetaPipeEntity.clearManagedCables();
        }
    }

    private static final class TrackingCable extends BaseMetaPipeEntity {

        private int unloads;

        private TrackingCable() {
            super(HarvestTool.CutterLevel0.toTileEntityBaseType());
        }

        @Override
        public void onUnload() {
            unloads++;
            super.onUnload();
        }
    }
}
