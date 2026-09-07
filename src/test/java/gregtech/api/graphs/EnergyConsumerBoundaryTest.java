package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import cofh.api.energy.IEnergyReceiver;
import gregtech.api.GregTechAPI;
import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.consumers.NodeEnergyConnected;
import gregtech.api.graphs.consumers.NodeEnergyReceiver;
import gregtech.api.graphs.consumers.NodeEnergySink;
import gregtech.api.graphs.consumers.NodeGCEnergyHandler;
import gregtech.api.graphs.consumers.NodeGTBaseMetaTile;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import ic2.api.energy.tile.IEnergySink;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;

class EnergyConsumerBoundaryTest {

    private MockedStatic<MinecraftServer> servers;
    private MinecraftServer server;
    private boolean outputRF, explosions;
    private int euToRF;

    @BeforeEach
    void setup() {
        servers = mockStatic(MinecraftServer.class);
        server = mock(MinecraftServer.class);
        servers.when(MinecraftServer::getServer)
            .thenReturn(server);
        outputRF = GregTechAPI.mOutputRF;
        explosions = GregTechAPI.mRFExplosions;
        euToRF = GregTechAPI.mEUtoRF;
        GregTechAPI.mOutputRF = true;
        GregTechAPI.mRFExplosions = false;
        GregTechAPI.mEUtoRF = 400;
    }

    @AfterEach
    void restore() {
        GregTechAPI.mOutputRF = outputRF;
        GregTechAPI.mRFExplosions = explosions;
        GregTechAPI.mEUtoRF = euToRF;
        servers.close();
    }

    private static TileEntity endpoint(Class<?>... protocols) {
        return mock(TileEntity.class, withSettings().extraInterfaces(protocols));
    }

    private static ConsumerNode select(TileEntity tile) {
        GenerateNodeMapPower map = mock(GenerateNodeMapPower.class, CALLS_REAL_METHODS);
        ArrayList<ConsumerNode> consumers = new ArrayList<>();
        boolean added = map.addConsumer(tile, ForgeDirection.WEST, 2, consumers);
        assertEquals(added ? 1 : 0, consumers.size());
        return added ? consumers.get(0) : null;
    }

    private static MTECable cable(ConsumerNode... consumers) {
        BaseMetaPipeEntity base = mock(BaseMetaPipeEntity.class);
        MTECable cable = EnergyTransferTest.cable(MTECable.class, base);
        EnergyTransferTest.root(base, consumers).mHadVoltage = true;
        return cable;
    }

    private static long offer(MTECable cable, long volts, long amps) {
        return cable.transferElectricity(ForgeDirection.UNKNOWN, volts, amps, null);
    }

    @Test
    void protocolPrecedenceDoesNotFallThroughWhenNativeInputRefuses() {
        BaseMetaTileEntity base = mock(
            BaseMetaTileEntity.class,
            withSettings().extraInterfaces(IEnergyHandlerGC.class, IEnergySink.class, IEnergyReceiver.class));
        when(base.inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(true);
        assertInstanceOf(NodeGTBaseMetaTile.class, select(base));
        when(base.inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(false);
        assertNull(select(base));
        TileEntity nativeTile = endpoint(
            IEnergyConnected.class,
            IEnergyHandlerGC.class,
            IEnergySink.class,
            IEnergyReceiver.class);
        IEnergyConnected nativeInput = (IEnergyConnected) nativeTile;
        when(nativeInput.inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(true);
        assertInstanceOf(NodeEnergyConnected.class, select(nativeTile));
        when(nativeInput.inputEnergyFrom(ForgeDirection.WEST, false)).thenReturn(false);
        assertNull(select(nativeTile));
        verify((IEnergySink) nativeTile, never()).acceptsEnergyFrom(any(), any());
        TileEntity gc = endpoint(IEnergyHandlerGC.class, IEnergySink.class, IEnergyReceiver.class);
        assertInstanceOf(NodeGCEnergyHandler.class, select(gc));
        verify((IEnergySink) gc, never()).acceptsEnergyFrom(any(), any());
        TileEntity ic2 = endpoint(IEnergySink.class, IEnergyReceiver.class);
        World world = mock(World.class);
        ic2.xCoord = 4;
        when(ic2.getWorldObj()).thenReturn(world);
        when(((IEnergySink) ic2).acceptsEnergyFrom(any(), eq(ForgeDirection.WEST))).thenReturn(true);
        assertInstanceOf(NodeEnergySink.class, select(ic2));
        when(((IEnergySink) ic2).acceptsEnergyFrom(any(), any())).thenReturn(false);
        assertNull(select(ic2));
        TileEntity rf = endpoint(IEnergyReceiver.class);
        assertInstanceOf(NodeEnergyReceiver.class, select(rf));
        GregTechAPI.mOutputRF = false;
        assertNull(select(rf));
    }

    @Test
    void strictOneAmpEndpointRetainsExactBoundaryAndReceiverSide() {
        TileEntity tile = endpoint(IEnergyConnected.class);
        IEnergyConnected receiver = (IEnergyConnected) tile;
        AtomicLong demand = new AtomicLong(64);
        when(receiver.injectEnergyUnits(ForgeDirection.WEST, 32, 4)).thenAnswer(call -> {
            if (demand.get() <= 32) return 0L;
            demand.addAndGet(-32);
            return 1L;
        });
        MTECable cable = cable(new NodeEnergyConnected(2, receiver, ForgeDirection.WEST, new ArrayList<>()));
        assertEquals(1, offer(cable, 32, 4));
        assertEquals(0, offer(cable, 32, 4));
        demand.addAndGet(1); // Another event can reopen demand within the same server tick.
        assertEquals(1, offer(cable, 32, 4));
        assertEquals(1, demand.get());
        verify(receiver, times(3)).injectEnergyUnits(ForgeDirection.WEST, 32, 4);
    }

    @Test
    void wholeBatchNativeEndpointKeepsOverfillAndSeparateRfStore() {
        TileEntity tile = endpoint(IEnergyConnected.class, IEnergyReceiver.class);
        IEnergyConnected receiver = (IEnergyConnected) tile;
        AtomicLong eu = new AtomicLong(39999);
        when(receiver.inputEnergyFrom(ForgeDirection.WEST, false)).thenAnswer(call -> eu.get() < 40000);
        when(receiver.injectEnergyUnits(ForgeDirection.WEST, 32, 4)).thenAnswer(call -> {
            if (eu.get() >= 40000) return 0L;
            eu.addAndGet(128);
            return 4L;
        });
        MTECable cable = cable(select(tile));
        assertEquals(4, offer(cable, 32, 4));
        assertEquals(40127, eu.get());
        assertEquals(0, offer(cable, 32, 4));
        assertNull(select(tile));
        verify((IEnergyReceiver) tile, never()).receiveEnergy(any(), anyInt(), anyBoolean());
    }

    @Test
    void ic2PartialRemainderCountsOneAmpAndDemandIsRecheckedPerPacket() {
        TileEntity tile = endpoint(IEnergySink.class);
        IEnergySink sink = (IEnergySink) tile;
        when(sink.getDemandedEnergy()).thenReturn(70.0);
        when(sink.injectEnergy(ForgeDirection.WEST, 32, 32)).thenReturn(0.0, 12.0, 32.0);
        ConsumerNode node = new NodeEnergySink(2, sink, ForgeDirection.WEST, new ArrayList<>());
        assertEquals(2, offer(cable(node), 32, 4));
        verify(sink, times(3)).injectEnergy(ForgeDirection.WEST, 32, 32);
        when(sink.getDemandedEnergy()).thenReturn(0.0);
        assertFalse(node.needsEnergy());
        when(tile.isInvalid()).thenReturn(true);
        clearInvocations(sink);
        assertFalse(node.needsEnergy());
        verify(sink, never()).getDemandedEnergy();
    }

    @Test
    void rfPartialReceiptUsesPerCallAllowanceAndRetainsPaidRemainder() {
        TileEntity tile = endpoint(IEnergyReceiver.class);
        IEnergyReceiver rf = (IEnergyReceiver) tile;
        when(rf.receiveEnergy(ForgeDirection.WEST, 128, true)).thenReturn(20);
        when(rf.receiveEnergy(ForgeDirection.WEST, 128, false)).thenReturn(20);
        when(rf.receiveEnergy(ForgeDirection.WEST, 236, true)).thenReturn(0);
        ConsumerNode node = new NodeEnergyReceiver(2, rf, ForgeDirection.WEST, new ArrayList<>());
        assertEquals(1, node.injectEnergy(32, 4));
        assertEquals(0, node.injectEnergy(32, 4));
        // Space alone is not demand: a BuildCraft-style tick allowance can be exhausted.
        verify(rf, never()).getEnergyStored(any());
        when(rf.receiveEnergy(ForgeDirection.WEST, 236, true)).thenReturn(236);
        when(rf.receiveEnergy(ForgeDirection.WEST, 236, false)).thenReturn(236);
        assertEquals(0, node.injectEnergy(32, 4));
        // Known accounting bug: the rejected second call buffered 128 RF without returning a paid amp.
        verify(rf).receiveEnergy(ForgeDirection.WEST, 236, false);
    }

    @Test
    void gcCapacityGateAndUnpaidRemainderAreCurrentBehaviorNotAContract() {
        TileEntity tile = endpoint(IEnergyHandlerGC.class);
        IEnergyHandlerGC gc = (IEnergyHandlerGC) tile;
        float packet = 32 * EnergyConfigHandler.IC2_RATIO;
        when(gc.getMaxEnergyStoredGC(any())).thenReturn(packet - 1);
        ConsumerNode node = new NodeGCEnergyHandler(2, gc, ForgeDirection.WEST, new ArrayList<>());
        assertEquals(0, node.injectEnergy(32, 4));
        verify(gc, never()).receiveEnergyGC(any(), anyFloat(), anyBoolean());
        when(gc.getMaxEnergyStoredGC(any())).thenReturn(packet);
        when(gc.receiveEnergyGC(any(), eq(packet), eq(false))).thenReturn(packet);
        assertEquals(0, node.injectEnergy(32, 4));
        // Known bug: this receipt spends the remainder created by the previously rejected offer.
        verify(gc).receiveEnergyGC(any(), eq(packet), eq(false));
    }

    @Test
    void forwardingRetargetsSynchronouslyAndPreservesConsumerOrder() {
        TileEntity tile = endpoint(IEnergyConnected.class);
        IEnergyConnected forwarder = (IEnergyConnected) tile;
        TileEntity destination = endpoint(IEnergyConnected.class);
        IEnergyConnected target = (IEnergyConnected) destination;
        List<String> trace = new ArrayList<>();
        when(target.injectEnergyUnits(ForgeDirection.NORTH, 32, 4)).thenAnswer(call -> {
            trace.add("target");
            return 2L;
        });
        MTECable downstream = cable(new NodeEnergyConnected(2, target, ForgeDirection.NORTH, new ArrayList<>()));
        when(forwarder.injectEnergyUnits(ForgeDirection.WEST, 32, 4)).thenAnswer(call -> {
            trace.add("forward");
            return offer(downstream, 32, 4);
        });
        TileEntity tail = endpoint(IEnergyConnected.class);
        IEnergyConnected last = (IEnergyConnected) tail;
        when(last.injectEnergyUnits(ForgeDirection.UP, 32, 2)).thenAnswer(call -> {
            trace.add("tail");
            return 1L;
        });
        MTECable upstream = cable(
            new NodeEnergyConnected(2, forwarder, ForgeDirection.WEST, new ArrayList<>()),
            new NodeEnergyConnected(3, last, ForgeDirection.UP, new ArrayList<>()));
        assertEquals(3, offer(upstream, 32, 4));
        assertEquals(List.of("forward", "target", "tail"), trace);
        doReturn(1L).when(forwarder)
            .injectEnergyUnits(ForgeDirection.WEST, 32, 4);
        assertEquals(1, offer(upstream, 32, 4));
        verify(target, times(1)).injectEnergyUnits(any(), anyLong(), anyLong());
    }

    @Test
    void rejectingNativeEndpointStillEnergizesAndBurnsPath() {
        TileEntity tile = endpoint(IEnergyConnected.class);
        MTECable cable = cable(
            new NodeEnergyConnected(2, (IEnergyConnected) tile, ForgeDirection.WEST, new ArrayList<>()));
        BaseMetaPipeEntity base = (BaseMetaPipeEntity) cable.getBaseMetaTileEntity();
        PowerNodePath path = new PowerNodePath(new MetaPipeEntity[] { cable });
        base.getNode().mSelfPath = path;
        assertEquals(0, offer(cable, 128, 4));
        verify(base).setToFire();
        when(server.getTickCounter()).thenReturn(1);
        assertEquals(128, path.getVoltage());
        assertEquals(0, path.getAmperage());
    }

    @Test
    void deliveredVoltageAndPathDiagnosticsIncludeBothLosses() {
        TileEntity tile = endpoint(IEnergyConnected.class);
        IEnergyConnected receiver = (IEnergyConnected) tile;
        when(receiver.injectEnergyUnits(ForgeDirection.WEST, 27, 4)).thenReturn(2L);
        MTECable cable = cable(new NodeEnergyConnected(2, receiver, ForgeDirection.WEST, new ArrayList<>()));
        Node root = ((BaseMetaPipeEntity) cable.getBaseMetaTileEntity()).getNode();
        MTECable entry = new MTECable("entry-loss", 0.5f, null, 3, 4, 128, false, false);
        entry.setBaseMetaTileEntity(new BaseMetaPipeEntity());
        MTECable run = new MTECable("run-loss", 0.5f, null, 2, 4, 128, false, false);
        run.setBaseMetaTileEntity(new BaseMetaPipeEntity());
        PowerNodePath entryPath = new PowerNodePath(new MetaPipeEntity[] { entry });
        PowerNodePath runPath = new PowerNodePath(new MetaPipeEntity[] { run });
        root.mSelfPath = entryPath;
        root.mNodePaths[0] = runPath;
        assertEquals(2, offer(cable, 32, 4));
        verify(receiver).injectEnergyUnits(ForgeDirection.WEST, 27, 4);
        when(server.getTickCounter()).thenReturn(1);
        assertEquals(29, entryPath.getVoltage());
        assertEquals(27, runPath.getVoltage());
        assertEquals(2, entryPath.getAmperage());
        assertEquals(2, runPath.getAmperage());
    }

    @Test
    void productionSourceDebitsAcceptedAmpsIncludingOutputLoss() throws Exception {
        BaseMetaTileEntity source = mock(BaseMetaTileEntity.class);
        MetaTileEntity meta = mock(MetaTileEntity.class);
        var metaField = BaseMetaTileEntity.class.getDeclaredField("mMetaTileEntity");
        metaField.setAccessible(true);
        metaField.set(source, meta);
        var voltageField = BaseMetaTileEntity.class.getDeclaredField("oldOutput");
        voltageField.setAccessible(true);
        voltageField.setLong(source, 32);
        when(meta.isEnetOutput()).thenReturn(true);
        when(source.getStoredEU()).thenReturn(132L);
        when(source.getOutputAmperage()).thenReturn(4L);
        when(source.outputsEnergyTo(ForgeDirection.EAST)).thenReturn(true);
        TileEntity tile = endpoint(IEnergyConnected.class);
        IEnergyConnected receiver = (IEnergyConnected) tile;
        when(receiver.injectEnergyUnits(ForgeDirection.WEST, 32, 4)).thenReturn(2L);
        when(source.getTileEntityAtSide(ForgeDirection.EAST)).thenReturn(tile);
        var output = BaseMetaTileEntity.class.getDeclaredMethod("handleEUOutput");
        output.setAccessible(true);
        output.invoke(source);
        verify(source).decreaseStoredEU(66, true);
        when(receiver.injectEnergyUnits(ForgeDirection.WEST, 32, 4)).thenReturn(0L);
        output.invoke(source);
        verify(source).decreaseStoredEU(0, true);
    }
}
