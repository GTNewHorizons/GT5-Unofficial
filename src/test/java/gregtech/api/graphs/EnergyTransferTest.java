package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.consumers.EmptyPowerConsumer;
import gregtech.api.graphs.consumers.NodeGTBaseMetaTile;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.TileIC2EnergySink;
import gregtech.api.metatileentity.implementations.MTECable;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GTPPMTECable;

class EnergyTransferTest {

    @Test
    void receiverLocalResetAllowsAnotherOfferWithoutAdvancingServerTick() throws Exception {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
            MTECable cable = cable(MTECable.class, base);
            BaseMetaTileEntity receiver = mock(BaseMetaTileEntity.class);
            MetaTileEntity meta = mock(MetaTileEntity.class);
            var metaField = BaseMetaTileEntity.class.getDeclaredField("mMetaTileEntity");
            metaField.setAccessible(true);
            metaField.set(receiver, meta);
            when(receiver.canAccessData()).thenReturn(true);
            when(receiver.canAcceptEnergyThisTick()).thenCallRealMethod();
            when(meta.isElectric()).thenReturn(true);
            when(meta.maxAmperesIn()).thenReturn(1L);
            when(meta.maxEUStore()).thenReturn(1000L);
            when(receiver.inputEnergyFrom(ForgeDirection.WEST)).thenReturn(true);
            when(receiver.getInputVoltage()).thenReturn(32L);
            when(receiver.getEUCapacity()).thenReturn(1000L);
            when(receiver.increaseStoredEnergyUnits(anyLong(), eq(true))).thenReturn(true);
            when(receiver.injectEnergyUnits(any(), anyLong(), anyLong())).thenCallRealMethod();
            NodeGTBaseMetaTile consumer = new NodeGTBaseMetaTile(2, receiver, ForgeDirection.WEST, new ArrayList<>());
            root(base, consumer).mHadVoltage = true;
            var reset = BaseMetaTileEntity.class.getDeclaredMethod("handleElectricUpdates");
            reset.setAccessible(true);
            receiver.mTickTimer = 21;
            when(receiver.getEUCapacity()).thenReturn(0L);
            reset.invoke(receiver);
            when(receiver.getEUCapacity()).thenReturn(1000L);
            assertEquals(1, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            assertEquals(0, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            when(receiver.getEUCapacity()).thenReturn(0L);
            reset.invoke(receiver);
            when(receiver.getEUCapacity()).thenReturn(1000L);
            assertEquals(1, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
        }
    }

    @Test
    void blockedOfferCursorAllocationCount() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class);
            MockedConstruction<NodeList> cursors = mockConstruction(NodeList.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
            MTECable cable = cable(MTECable.class, base);
            ConsumerNode consumer = mock(ConsumerNode.class);
            root(base, consumer).mHadVoltage = true;
            assertEquals(0, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            verify(consumer).needsEnergy();
            assertEquals(
                0,
                cursors.constructed()
                    .size());
        }
    }

    private static MTECable cable(Class<? extends MTECable> type, BaseMetaPipeEntity base) {
        MTECable cable = type == MTECable.class ? spy(new MTECable("test", 0.5f, null, 0, 1, 32, false, false))
            : type == GTPPMTECable.class
                ? spy(
                    new GTPPMTECable(
                        "test",
                        0.5f,
                        (gregtech.api.enums.Materials) null,
                        0,
                        1,
                        32,
                        false,
                        false,
                        new short[4]))
                : mock(type, withSettings().useConstructor());
        when(cable.getBaseMetaTileEntity()).thenReturn(base);
        when(base.getMetaTileEntity()).thenReturn(cable);
        when(base.isServerSide()).thenReturn(true);

        doCallRealMethod().when(cable)
            .transferElectricity(any(), anyLong(), anyLong(), any());
        return cable;
    }

    private static PowerNode root(BaseMetaPipeEntity base, ConsumerNode... consumers) {
        PowerNode root = new PowerNode(1, base, new ArrayList<>(List.of(consumers)));
        root.mHighestNodeValue = consumers.length + 1;
        for (int i = 0; i < consumers.length; i++) {
            consumers[i].mNodeValue = i + 2;
            root.mNeighbourNodes[i] = consumers[i];
            root.mNodePaths[i] = new PowerNodePath(new MetaPipeEntity[0]);
            root.locks[i] = new Lock();
        }
        when(base.getNode()).thenReturn(root);
        return root;
    }

    @Test
    void selectionIsOrderedSnapshotAndNestedCallsHaveTheirOwnCursor() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
            MTECable cable = cable(MTECable.class, base);
            ConsumerNode first = mock(ConsumerNode.class);
            ConsumerNode second = mock(ConsumerNode.class);
            PowerNode root = root(base, first, second);
            root.mHadVoltage = true;
            List<String> trace = new ArrayList<>();
            when(first.needsEnergy()).thenAnswer(call -> {
                trace.add("select1");
                return true;
            });
            when(second.needsEnergy()).thenAnswer(call -> {
                trace.add("select2");
                return true;
            });
            when(first.injectEnergy(32, 4)).thenAnswer(call -> {
                trace.add("inject1");
                doReturn(false).when(first)
                    .needsEnergy();
                doReturn(false).when(second)
                    .needsEnergy();
                assertEquals(0, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
                return 1;
            });
            when(second.injectEnergy(32, 3)).thenAnswer(call -> {
                trace.add("inject2");
                return 2;
            });
            assertEquals(3, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
            assertEquals(List.of("select1", "select2", "inject1", "inject2"), trace);
            // No tick-level empty cache: a receiver can become eligible between offers.
            when(second.needsEnergy()).thenReturn(true);
            when(second.injectEnergy(32, 4)).thenReturn(1);
            assertEquals(1, cable.transferElectricity(ForgeDirection.UNKNOWN, 32, 4, null));
        }
    }

    @Test
    void firstVoltageReachesDeadEndAndBurnsEvenWithoutDemand() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
            MTECable cable = cable(MTECable.class, base);
            BaseMetaPipeEntity deadBase = mock(BaseMetaPipeEntity.class);
            MTECable deadCable = cable(MTECable.class, deadBase);
            PowerNodePath deadPath = new PowerNodePath(new MetaPipeEntity[] { deadCable });
            when(deadBase.getNodePath()).thenReturn(deadPath);
            EmptyPowerConsumer dead = new EmptyPowerConsumer(2, deadBase, ForgeDirection.DOWN, new ArrayList<>());
            PowerNode root = root(base, dead);
            assertEquals(0, cable.transferElectricity(ForgeDirection.UNKNOWN, 128, 4, null));
            assertTrue(root.mHadVoltage);
            verify(deadBase).setToFire();
            assertEquals(0, cable.transferElectricity(ForgeDirection.UNKNOWN, 128, 4, null));
            verify(deadBase, times(1)).setToFire();
        }
    }

    @Test
    void ic2UsesProductionCableTransferAndPreservesRemainderAndOverload() {
        try (MockedStatic<MinecraftServer> servers = mockStatic(MinecraftServer.class)) {
            servers.when(MinecraftServer::getServer)
                .thenReturn(mock(MinecraftServer.class));
            for (Class<? extends MTECable> type : List.of(MTECable.class, GTPPMTECable.class)) {
                BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
                MTECable cable = cable(type, base);
                ConsumerNode consumer = mock(ConsumerNode.class);
                PowerNode root = root(base, consumer);

                root.mSelfPath = new PowerNodePath(new MetaPipeEntity[] { cable });
                when(consumer.needsEnergy()).thenReturn(true);
                when(consumer.injectEnergy(32, 3)).thenReturn(2);
                when(consumer.injectEnergy(32, 42)).thenReturn(41);
                TileIC2EnergySink sink = new TileIC2EnergySink(base);
                assertEquals(33, sink.injectEnergy(ForgeDirection.UNKNOWN, 99, 128));
                assertEquals(32, sink.injectEnergy(ForgeDirection.UNKNOWN, 1344, 128));
                verify(cable).transferElectricity(ForgeDirection.UNKNOWN, 32, 3, null);
                verify(cable).transferElectricity(ForgeDirection.UNKNOWN, 32, 42, null);
                verify(base).setToFire();
            }
        }
    }

    abstract static class ExternalCable extends MTECable {

        ExternalCable() {
            super("test", 0.5f, null, 0, 4, 32, false, false);
        }

        @Override
        public long transferElectricity(ForgeDirection side, long voltage, long amps, HashSet<TileEntity> visited) {
            assertNotNull(visited);
            assertTrue(visited.remove((TileEntity) getBaseMetaTileEntity()));
            assertTrue(visited.isEmpty());
            return 1;
        }
    }

    @Test
    void externalOverrideGetsFreshMutableSeededSetIncludingAfterReplacement() {
        BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
        cable(MTECable.class, base);
        TileIC2EnergySink sink = new TileIC2EnergySink(base);
        cable(ExternalCable.class, base);
        assertEquals(32, sink.injectEnergy(ForgeDirection.UNKNOWN, 64, 32));
        assertEquals(32, sink.injectEnergy(ForgeDirection.UNKNOWN, 64, 32));
    }
}
