package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.metatileentity.MetaPipeEntity;

class PowerNodesTest {

    @Test
    void stalePathUnwindsAndPreservesAcceptedAmperage() {
        withServerTick(() -> {
            ArrayList<ConsumerNode> consumers = new ArrayList<>();
            Node source = new Node(0, null, consumers);
            ConsumerNode powered = acceptingConsumer(1, consumers);
            ConsumerNode stale = acceptingConsumer(2, consumers);
            consumers.add(powered);
            consumers.add(stale);
            source.mHighestNodeValue = 2;
            connect(source, powered, 0, false);
            connect(source, stale, 1, true);

            NodeList pending = new NodeList(new Node[] { powered, stale });
            assertEquals(1, PowerNodes.powerNode(source, null, pending, 32, 2));
            assertTrue(pending.isStale());
        });
    }

    @Test
    void moreThanTwentySkippedConsumersAreProgressNotAnInfiniteLoop() {
        withServerTick(() -> {
            ArrayList<ConsumerNode> consumers = new ArrayList<>();
            Node source = new Node(0, null, consumers);
            ConsumerNode target = acceptingConsumer(1, consumers);
            source.mHighestNodeValue = 1;
            connect(source, target, 0, false);
            source.locks[0].addTileEntity(null);

            Node[] pendingNodes = new Node[22];
            for (int i = 0; i < pendingNodes.length; i++) {
                pendingNodes[i] = acceptingConsumer(1, consumers);
            }
            NodeList pending = new NodeList(pendingNodes);

            assertEquals(0, PowerNodes.powerNode(source, null, pending, 32, 1));
            assertFalse(pending.isStale());
        });
    }

    private static ConsumerNode acceptingConsumer(int value, ArrayList<ConsumerNode> consumers) {
        return new ConsumerNode(value, null, ForgeDirection.UNKNOWN, consumers) {

            @Override
            public int injectEnergy(long voltage, long maxAmps) {
                return maxAmps > 0 ? 1 : 0;
            }
        };
    }

    private static void connect(Node source, Node target, int side, boolean invalidatePath) {
        PowerNodePath path = new PowerNodePath(new MetaPipeEntity[0]);
        if (invalidatePath) path.invalidate();
        source.mNeighbourNodes[side] = target;
        source.mNodePaths[side] = path;
        source.locks[side] = new Lock();
    }

    private static void withServerTick(Runnable test) {
        MinecraftServer server = mock(MinecraftServer.class);
        when(server.getTickCounter()).thenReturn(1);
        try (MockedStatic<MinecraftServer> minecraftServer = mockStatic(MinecraftServer.class)) {
            minecraftServer.when(MinecraftServer::getServer)
                .thenReturn(server);
            test.run();
        }
    }
}
