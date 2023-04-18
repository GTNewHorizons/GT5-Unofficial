package gregtech.api.graphs;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.NodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;

// generates the node map
public abstract class GenerateNodeMap {

    // clearing the node map to make sure it is gone on reset
    public static void clearNodeMap(Node aNode, int aReturnNodeValue) {
        if (aNode.mTileEntity instanceof BaseMetaPipeEntity tPipe) {
            tPipe.setNode(null);
            tPipe.setNodePath(null);
            if (aNode.mSelfPath != null) {
                aNode.mSelfPath.clearPath();
                aNode.mSelfPath = null;
            }
        }
        for (byte side : ALL_VALID_SIDES) {
            final NodePath tPath = aNode.mNodePaths[side];
            if (tPath != null) {
                tPath.clearPath();
                aNode.mNodePaths[side] = null;
            }
            final Node tNextNode = aNode.mNeighbourNodes[side];
            if (tNextNode == null) continue;
            if (tNextNode.mNodeValue != aReturnNodeValue) clearNodeMap(tNextNode, aNode.mNodeValue);
            aNode.mNeighbourNodes[side] = null;
        }
    }

    // get how many connections the pipe have
    private static int getNumberOfConnections(MetaPipeEntity aPipe) {
        int tCons = 0;
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (aPipe.isConnectedAtSide(side)) tCons++;
        }
        return tCons;
    }

    // gets the next node
    protected void generateNextNode(BaseMetaPipeEntity aPipe, Node aPipeNode, ForgeDirection aInvalidSide,
        int aNextNodeValue, ArrayList<ConsumerNode> tConsumers, HashSet<Node> tNodeMap) {
        final MetaPipeEntity tMetaPipe = (MetaPipeEntity) aPipe.getMetaTileEntity();
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (side == aInvalidSide) {
                continue;
            }
            final TileEntity tNextTileEntity = aPipe.getTileEntityAtSide(side);
            if (tNextTileEntity == null || (tMetaPipe != null && !tMetaPipe.isConnectedAtSide(side))) continue;
            final ArrayList<MetaPipeEntity> tNewPipes = new ArrayList<>();
            final Pair nextTileEntity = getNextValidTileEntity(tNextTileEntity, tNewPipes, side, tNodeMap);
            if (nextTileEntity != null) {
                final Node tNextNode = generateNode(
                    nextTileEntity.mTileEntity,
                    aPipeNode,
                    aNextNodeValue + 1,
                    tNewPipes,
                    nextTileEntity.mSide,
                    tConsumers,
                    tNodeMap);
                if (tNextNode != null) {
                    final int i = side.ordinal();
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
    protected Node generateNode(TileEntity aTileEntity, Node aPreviousNode, int aNextNodeValue,
        ArrayList<MetaPipeEntity> aPipes, ForgeDirection side, ArrayList<ConsumerNode> aConsumers,
        HashSet<Node> aNodeMap) {
        if (aTileEntity.isInvalid()) return null;
        final ForgeDirection tSideOp = side.getOpposite();
        final ForgeDirection tInvalidSide = aPreviousNode == null ? ForgeDirection.UNKNOWN : tSideOp;
        Node tThisNode = null;
        if (isPipe(aTileEntity)) {
            final BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) aTileEntity;
            final MetaPipeEntity tMetaPipe = (MetaPipeEntity) tPipe.getMetaTileEntity();
            final int tConnections = getNumberOfConnections(tMetaPipe);
            final Node tPipeNode;
            if (tConnections == 1) {
                tPipeNode = getEmptyNode(aNextNodeValue, tSideOp, aTileEntity, aConsumers);
                if (tPipeNode == null) return null;
            } else {
                tPipeNode = getPipeNode(aNextNodeValue, tSideOp, aTileEntity, aConsumers);
            }
            tPipe.setNode(tPipeNode);
            aNodeMap.add(tPipeNode);
            tPipeNode.mSelfPath = getNewPath(new MetaPipeEntity[] { tMetaPipe });
            tThisNode = tPipeNode;
            if (tInvalidSide != ForgeDirection.UNKNOWN) {
                final int iInvalid = tInvalidSide.ordinal();
                tPipeNode.mNeighbourNodes[iInvalid] = aPreviousNode;
                tPipeNode.mNodePaths[iInvalid] = getNewPath(aPipes.toArray(new MetaPipeEntity[0]));
                final Lock lock = new Lock();
                tPipeNode.mNodePaths[tSideOp.ordinal()].lock = lock;
                tPipeNode.locks[iInvalid] = lock;
                aPreviousNode.returnValues.mReturnPath = tPipeNode.mNodePaths[iInvalid];
                aPreviousNode.returnValues.returnLock = lock;
            }
            if (tConnections > 1)
                generateNextNode(tPipe, tPipeNode, tInvalidSide, aNextNodeValue, aConsumers, aNodeMap);
        } else if (addConsumer(aTileEntity, tSideOp, aNextNodeValue, aConsumers)) {
            final int i = tSideOp.ordinal();
            final ConsumerNode tConsumeNode = aConsumers.get(aConsumers.size() - 1);
            tConsumeNode.mNeighbourNodes[i] = aPreviousNode;
            tConsumeNode.mNodePaths[i] = getNewPath(aPipes.toArray(new MetaPipeEntity[0]));
            final Lock lock = new Lock();
            tConsumeNode.mNodePaths[i].lock = lock;
            aPreviousNode.returnValues.mReturnPath = tConsumeNode.mNodePaths[i];
            aPreviousNode.returnValues.returnLock = lock;
            tThisNode = tConsumeNode;
        }
        return tThisNode;
    }

    // go over the pipes until we see a valid tile entity that needs a node
    protected Pair getNextValidTileEntity(TileEntity aTileEntity, ArrayList<MetaPipeEntity> aPipes, ForgeDirection side,
        HashSet<Node> aNodeMap) {
        if (isPipe(aTileEntity)) {
            final BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) aTileEntity;
            final MetaPipeEntity tMetaPipe = (MetaPipeEntity) tPipe.getMetaTileEntity();
            final Node tNode = tPipe.getNode();
            if (tNode != null) {
                if (aNodeMap.contains(tNode)) return null;
            }
            final int tConnections = getNumberOfConnections(tMetaPipe);
            if (tConnections == 2) {
                final ForgeDirection tSideOp = side.getOpposite();
                for (final ForgeDirection s : ForgeDirection.VALID_DIRECTIONS) {
                    if (s == tSideOp || !(tMetaPipe.isConnectedAtSide(s))) continue;
                    final TileEntity tNewTileEntity = tPipe.getTileEntityAtSide(s);
                    if (tNewTileEntity == null) continue;
                    if (isPipe(tNewTileEntity)) {
                        aPipes.add(tMetaPipe);
                        return getNextValidTileEntity(tNewTileEntity, aPipes, s, aNodeMap);
                    } else {
                        return new Pair(aTileEntity, s);
                    }
                }
            } else {
                return new Pair(aTileEntity, side);
            }
        } else {
            return new Pair(aTileEntity, side);
        }
        return null;
    }

    // check if the tile entity is the correct pipe
    protected boolean isPipe(TileEntity aTileEntity) {
        return aTileEntity instanceof BaseMetaPipeEntity;
    }

    // checks if the tile entity is a consumer and add to the list
    protected abstract boolean addConsumer(TileEntity aTileEntity, ForgeDirection side, int aNodeValue,
        ArrayList<ConsumerNode> aConsumers);

    // get correct pathClass that you need for your node network
    protected abstract NodePath getNewPath(MetaPipeEntity[] aPipes);

    // used for if you need to use dead ends for something can be null
    protected Node getEmptyNode(int aNodeValue, ForgeDirection side, TileEntity aTileEntity,
        ArrayList<ConsumerNode> aConsumers) {
        return null;
    }

    // get correct node type you need for your network
    protected Node getPipeNode(int aNodeValue, ForgeDirection side, TileEntity aTileEntity,
        ArrayList<ConsumerNode> aConsumers) {
        return new Node(aNodeValue, aTileEntity, aConsumers);
    }

    private static class Pair {

        public ForgeDirection mSide;
        public TileEntity mTileEntity;

        public Pair(TileEntity aTileEntity, ForgeDirection side) {
            this.mTileEntity = aTileEntity;
            this.mSide = side;
        }
    }
}
