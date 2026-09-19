package gregtech.api.graphs;

import java.util.ArrayList;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.NodePath;

// base Node class
public class Node {

    private Node nodeMapRoot = this;
    private boolean nodeMapValid = true;
    private long topologyVersion;
    private int topologyChangeTime;

    public Node(int aNodeValue, TileEntity aTileEntity, ArrayList<ConsumerNode> aConsumers) {
        this.mNodeValue = aNodeValue;
        this.mTileEntity = aTileEntity;
        this.mConsumers = aConsumers;
        mHighestNodeValue = aNodeValue;
        // you don't want to generate map multiple times in the same tick
        mCreationTime = MinecraftServer.getServer()
            .getTickCounter();
    }

    public void joinNodeMap(Node previousNode) {
        if (previousNode != null) nodeMapRoot = previousNode.nodeMapRoot;
    }

    public boolean isNodeMapValid() {
        return nodeMapRoot.nodeMapValid;
    }

    public void invalidateNodeMap() {
        nodeMapRoot.nodeMapValid = false;
        nodeMapRoot.topologyVersion++;
        nodeMapRoot.topologyChangeTime = MinecraftServer.getServer()
            .getTickCounter();
    }

    public boolean isNodeMapRefreshDue() {
        // External sources have no BaseMetaTileEntity to perform the delayed rebuild.
        return !isNodeMapValid() && MinecraftServer.getServer()
            .getTickCounter() - nodeMapRoot.topologyChangeTime >= 10;
    }

    public long getTopologyVersion() {
        return nodeMapRoot.topologyVersion;
    }

    public final TileEntity mTileEntity;
    public Node[] mNeighbourNodes = new Node[6];
    public NodePath[] mNodePaths = new NodePath[6];
    public Lock[] locks = new Lock[6];
    public ReturnPair returnValues = new ReturnPair();
    public NodePath mSelfPath;
    public ArrayList<ConsumerNode> mConsumers;
    public int mCreationTime;
    public int mNodeValue;
    public int mHighestNodeValue;
    public boolean mInvalid;

    public static class ReturnPair {

        public NodePath mReturnPath;
        public Lock returnLock;
    }
}
