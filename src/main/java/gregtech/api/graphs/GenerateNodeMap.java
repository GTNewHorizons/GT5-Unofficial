package gregtech.api.graphs;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.NodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.HashSet;

import static gregtech.api.util.GT_Utility.getOppositeSide;


// generates the node map
abstract public class GenerateNodeMap {
    // clearing the node map to make sure it is gone on reset
    public static void clearNodeMap(Node aNode, int aReturnNodeValue) {
        if (aNode.mTileEntity instanceof BaseMetaPipeEntity) {
            BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) aNode.mTileEntity;
            tPipe.setNode(null);
            tPipe.setNodePath(null);
            if (aNode.mSelfPath != null) {
                aNode.mSelfPath.clearPath();
                aNode.mSelfPath = null;
            }
        }
        for (int i = 0; i < 6; i++) {
            NodePath tPath = aNode.mNodePaths[i];
            if (tPath != null) {
                tPath.clearPath();
                aNode.mNodePaths[i] = null;
            }
            Node tNextNode = aNode.mNeighbourNodes[i];
            if (tNextNode == null) continue;
            if (tNextNode.mNodeValue != aReturnNodeValue)
                clearNodeMap(tNextNode, aNode.mNodeValue);
            aNode.mNeighbourNodes[i] = null;
        }
    }

    // get how many connections the pipe have
    private static int getNumberOfConnections(MetaPipeEntity aPipe) {
        int tCons = 0;
        for (int i = 0; i < 6; i++) {
            if (aPipe.isConnectedAtSide(i)) tCons++;
        }
        return tCons;
    }

    // gets the next node
    protected void generateNextNode(BaseMetaPipeEntity aPipe, Node aPipeNode, byte aInvalidSide, int aNextNodeValue,
                                    ArrayList<ConsumerNode> tConsumers, HashSet<Node> tNodeMap) {
        MetaPipeEntity tMetaPipe = (MetaPipeEntity) aPipe.getMetaTileEntity();
        for (byte i = 0; i < 6; i++) {
            if (i == aInvalidSide) {
                continue;
            }
            TileEntity tNextTileEntity = aPipe.getTileEntityAtSide(i);
            if (tNextTileEntity == null || (tMetaPipe != null && !tMetaPipe.isConnectedAtSide(i))) continue;
            ArrayList<MetaPipeEntity> tNewPipes = new ArrayList<>();
            Pair nextTileEntity = getNextValidTileEntity(tNextTileEntity, tNewPipes, i, tNodeMap);
            if (nextTileEntity != null) {
                Node tNextNode = generateNode(nextTileEntity.mTileEntity, aPipeNode, aNextNodeValue + 1, tNewPipes,
                                              nextTileEntity.mSide, tConsumers, tNodeMap);
                if (tNextNode != null) {
                    aNextNodeValue = tNextNode.mHighestNodeValue;
                    aPipeNode.mHighestNodeValue = tNextNode.mHighestNodeValue;
                    aPipeNode.mNeighbourNodes[i] = tNextNode;
                    aPipeNode.mNodePaths[i] = aPipeNode.returnValues.mReturnPath;
                    aPipeNode.locks[i] = aPipeNode.returnValues.returnLock;
                    aPipeNode.mNodePaths[i].reloadLocks();
                }
            }
        }
        aPipe.reloadLocks();
    }

    // on a valid tile entity create a new node
    protected Node generateNode(TileEntity aTileEntity, Node aPreviousNode, int aNextNodeValue, ArrayList<MetaPipeEntity> aPipes,
                                int aSide, ArrayList<ConsumerNode> aConsumers, HashSet<Node> aNodeMap) {
        if (aTileEntity.isInvalid()) return null;
        byte tSideOp = getOppositeSide(aSide);
        byte tInvalidSide = aPreviousNode == null ? -1 : tSideOp;
        Node tThisNode = null;
        if (isPipe(aTileEntity)) {
            BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) aTileEntity;
            MetaPipeEntity tMetaPipe = (MetaPipeEntity) tPipe.getMetaTileEntity();
            int tConnections = getNumberOfConnections(tMetaPipe);
            Node tPipeNode;
            if (tConnections == 1) {
                tPipeNode = getEmptyNode(aNextNodeValue, tSideOp, aTileEntity, aConsumers);
                if (tPipeNode == null) return null;
            } else {
                tPipeNode = getPipeNode(aNextNodeValue, tSideOp, aTileEntity, aConsumers);
            }
            tPipe.setNode(tPipeNode);
            aNodeMap.add(tPipeNode);
            tPipeNode.mSelfPath = getNewPath(new MetaPipeEntity[]{tMetaPipe});
            tThisNode = tPipeNode;
            if (tInvalidSide > -1) {
                tPipeNode.mNeighbourNodes[tInvalidSide] = aPreviousNode;
                tPipeNode.mNodePaths[tInvalidSide] = getNewPath(aPipes.toArray(new MetaPipeEntity[0]));
                Lock lock = new Lock();
                tPipeNode.mNodePaths[tSideOp].lock = lock;
                tPipeNode.locks[tInvalidSide] = lock;
                aPreviousNode.returnValues.mReturnPath = tPipeNode.mNodePaths[tInvalidSide];
                aPreviousNode.returnValues.returnLock = lock;
            }
            if (tConnections > 1)
                generateNextNode(tPipe, tPipeNode, tInvalidSide, aNextNodeValue, aConsumers, aNodeMap);
        } else if (addConsumer(aTileEntity, tSideOp, aNextNodeValue, aConsumers)) {
            ConsumerNode tConsumeNode = aConsumers.get(aConsumers.size() - 1);
            tConsumeNode.mNeighbourNodes[tSideOp] = aPreviousNode;
            tConsumeNode.mNodePaths[tSideOp] = getNewPath(aPipes.toArray(new MetaPipeEntity[0]));
            Lock lock = new Lock();
            tConsumeNode.mNodePaths[tSideOp].lock = lock;
            aPreviousNode.returnValues.mReturnPath = tConsumeNode.mNodePaths[tSideOp];
            aPreviousNode.returnValues.returnLock = lock;
            tThisNode = tConsumeNode;
        }
        return tThisNode;
    }

    // go over the pipes until we see a valid tile entity that needs a node
    protected Pair getNextValidTileEntity(TileEntity aTileEntity, ArrayList<MetaPipeEntity> aPipes, byte aSide, HashSet<Node> aNodeMap) {
        if (isPipe(aTileEntity)) {
            BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) aTileEntity;
            MetaPipeEntity tMetaPipe = (MetaPipeEntity) tPipe.getMetaTileEntity();
            Node tNode = tPipe.getNode();
            if (tNode != null) {
                if (aNodeMap.contains(tNode))
                    return null;
            }
            int tConnections = getNumberOfConnections(tMetaPipe);
            if (tConnections == 2) {
                byte tSideOp = getOppositeSide(aSide);
                for (byte i = 0; i < 6; i++) {
                    if (i == tSideOp || !(tMetaPipe.isConnectedAtSide(i))) continue;
                    TileEntity tNewTileEntity = tPipe.getTileEntityAtSide(i);
                    if (tNewTileEntity == null) continue;
                    if (isPipe(tNewTileEntity)) {
                        aPipes.add(tMetaPipe);
                        return getNextValidTileEntity(tNewTileEntity, aPipes, i, aNodeMap);
                    } else {
                        return new Pair(aTileEntity, i);
                    }
                }
            } else {
                return new Pair(aTileEntity, aSide);
            }
        } else {
            return new Pair(aTileEntity, aSide);
        }
        return null;
    }

    // check if the tile entity is the correct pipe
    protected boolean isPipe(TileEntity aTileEntity) {
        return aTileEntity instanceof BaseMetaPipeEntity;
    }

    // checks if the tile entity is a consumer and add to the list
    abstract protected boolean addConsumer(TileEntity aTileEntity, byte aSide, int aNodeValue, ArrayList<ConsumerNode> aConsumers);

    // get correct pathClass  that you need for your node network
    protected abstract NodePath getNewPath(MetaPipeEntity[] aPipes);

    // used for if you need to use dead ends for something can be null
    protected Node getEmptyNode(int aNodeValue, byte aSide, TileEntity aTileEntity, ArrayList<ConsumerNode> aConsumers) {
        return null;
    }

    // get correct node type you need for your network
    protected Node getPipeNode(int aNodeValue, byte aSide, TileEntity aTileEntity, ArrayList<ConsumerNode> aConsumers) {
        return new Node(aNodeValue, aTileEntity, aConsumers);
    }

    private static class Pair {
        public byte mSide;
        public TileEntity mTileEntity;

        public Pair(TileEntity aTileEntity, byte aSide) {
            this.mTileEntity = aTileEntity;
            this.mSide = aSide;
        }
    }
}
