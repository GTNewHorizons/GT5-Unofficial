package gregtech.api.graphs;

import java.util.ArrayList;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.NodePath;
import gregtech.api.metatileentity.MetaPipeEntity;

// base Node class
public class Node {

    public Node(int aNodeValue, TileEntity aTileEntity, ArrayList<ConsumerNode> aConsumers) {
        this.mNodeValue = aNodeValue;
        this.mTileEntity = aTileEntity;
        this.mConsumers = aConsumers;
        mHighestNodeValue = aNodeValue;
        // you don't want to generate map multiple times in the same tick
        mCreationTime = MinecraftServer.getServer()
            .getTickCounter();
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

    // Only loop-closing edges: legacy transfer continues to use mNeighbourNodes/mNodePaths.
    ArrayList<AlternateEdge> alternateEdges;

    static final class AlternateEdge {

        final Node first, second;
        final ForgeDirection firstSide, secondSide;
        // Ordered from first to second, excluding the endpoint self cables.
        final MetaPipeEntity[] pipes;

        AlternateEdge(Node first, ForgeDirection firstSide, Node second, ForgeDirection secondSide,
            MetaPipeEntity[] pipes) {
            this.first = first;
            this.firstSide = firstSide;
            this.second = second;
            this.secondSide = secondSide;
            this.pipes = pipes;
        }
    }

    public static class ReturnPair {

        public NodePath mReturnPath;
        public Lock returnLock;
    }
}
