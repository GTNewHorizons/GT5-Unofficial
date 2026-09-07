package gregtech.api.graphs;

import static gregtech.api.enums.GTValues.ALL_VALID_SIDES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.NodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;

// generates the node map
public abstract class GenerateNodeMap {

    // Scratch for one walk, preserving the protected walk's existing null return on loops.
    private Pair loopClosure;

    // clearing the node map to make sure it is gone on reset
    public static void clearNodeMap(Node aNode, int aReturnNodeValue) {
        clearNodeMap(aNode, aReturnNodeValue, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    private static void clearNodeMap(Node aNode, int aReturnNodeValue, Set<Node> visited) {
        if (!visited.add(aNode)) return;
        // Detach shared metadata without following its cycles or touching an excluded parent.
        if (aNode.alternateEdges != null) {
            ArrayList<Node.AlternateEdge> edges = aNode.alternateEdges;
            aNode.alternateEdges = null;
            for (Node.AlternateEdge edge : edges) {
                Node other = edge.first == aNode ? edge.second : edge.first;
                if (other.alternateEdges != null) {
                    other.alternateEdges.remove(edge);
                    if (other.alternateEdges.isEmpty()) other.alternateEdges = null;
                }
            }
        }
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
            if (tNextNode.mNodeValue != aReturnNodeValue) clearNodeMap(tNextNode, aNode.mNodeValue, visited);
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
            if (tMetaPipe != null && !tMetaPipe.isConnectedAtSide(side)) continue;
            final TileEntity tNextTileEntity = aPipe.getTileEntityAtSide(side);
            if (tNextTileEntity == null) continue;
            final ArrayList<MetaPipeEntity> tNewPipes = new ArrayList<>();
            loopClosure = null;
            final Pair nextTileEntity = getNextValidTileEntity(tNextTileEntity, tNewPipes, side, tNodeMap);
            final Pair closure = loopClosure;
            loopClosure = null;
            if (nextTileEntity == null && closure != null) {
                retainAlternateEdge(
                    aPipeNode,
                    side,
                    ((BaseMetaPipeEntity) closure.mTileEntity).getNode(),
                    closure.mSide.getOpposite(),
                    tNewPipes);
            }
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

    private static void retainAlternateEdge(Node first, ForgeDirection firstSide, Node second,
        ForgeDirection secondSide, ArrayList<MetaPipeEntity> pipes) {
        if (first == null || second == null) return;
        if (first.alternateEdges != null) {
            for (Node.AlternateEdge edge : first.alternateEdges) {
                if (edge.first == first && edge.firstSide == firstSide
                    || edge.second == first && edge.secondSide == firstSide) return;
            }
        }
        for (MetaPipeEntity pipe : pipes) {
            if (!(pipe.getBaseMetaTileEntity() instanceof BaseMetaPipeEntity base) || base.isInvalid()
                || base.getMetaTileEntity() != pipe) return;
        }
        Node.AlternateEdge edge = new Node.AlternateEdge(
            first,
            firstSide,
            second,
            secondSide,
            pipes.toArray(new MetaPipeEntity[0]));
        if (first.alternateEdges == null) first.alternateEdges = new ArrayList<>();
        first.alternateEdges.add(edge);
        if (second != first) {
            if (second.alternateEdges == null) second.alternateEdges = new ArrayList<>();
            second.alternateEdges.add(edge);
        }
    }

    // on a valid tile entity create a new node
    protected Node generateNode(TileEntity aTileEntity, Node aPreviousNode, int aNextNodeValue,
        ArrayList<MetaPipeEntity> aPipes, ForgeDirection side, ArrayList<ConsumerNode> aConsumers,
        HashSet<Node> aNodeMap) {
        if (aTileEntity.isInvalid()) return null;
        final ForgeDirection oppositeSide = side.getOpposite();
        final ForgeDirection tInvalidSide = aPreviousNode == null ? ForgeDirection.UNKNOWN : oppositeSide;
        Node tThisNode = null;
        if (isPipe(aTileEntity)) {
            final BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) aTileEntity;
            final MetaPipeEntity tMetaPipe = (MetaPipeEntity) tPipe.getMetaTileEntity();
            final int tConnections = getNumberOfConnections(tMetaPipe);
            final Node tPipeNode;
            if (tConnections == 1) {
                tPipeNode = getEmptyNode(aNextNodeValue, oppositeSide, aTileEntity, aConsumers);
                if (tPipeNode == null) return null;
            } else {
                tPipeNode = getPipeNode(aNextNodeValue, oppositeSide, aTileEntity, aConsumers);
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
                tPipeNode.mNodePaths[oppositeSide.ordinal()].lock = lock;
                tPipeNode.locks[iInvalid] = lock;
                aPreviousNode.returnValues.mReturnPath = tPipeNode.mNodePaths[iInvalid];
                aPreviousNode.returnValues.returnLock = lock;
            }
            if (tConnections > 1)
                generateNextNode(tPipe, tPipeNode, tInvalidSide, aNextNodeValue, aConsumers, aNodeMap);
        } else if (addConsumer(aTileEntity, oppositeSide, aNextNodeValue, aConsumers)) {
            final int oppositeSideOrdinal = oppositeSide.ordinal();
            final ConsumerNode tConsumeNode = aConsumers.get(aConsumers.size() - 1);
            tConsumeNode.mNeighbourNodes[oppositeSideOrdinal] = aPreviousNode;
            tConsumeNode.mNodePaths[oppositeSideOrdinal] = getNewPath(aPipes.toArray(new MetaPipeEntity[0]));
            final Lock lock = new Lock();
            tConsumeNode.mNodePaths[oppositeSideOrdinal].lock = lock;
            aPreviousNode.returnValues.mReturnPath = tConsumeNode.mNodePaths[oppositeSideOrdinal];
            aPreviousNode.returnValues.returnLock = lock;
            tThisNode = tConsumeNode;
        }
        return tThisNode;
    }

    // Walk degree-two runs without consuming a stack frame per cable.
    protected Pair getNextValidTileEntity(TileEntity aTileEntity, ArrayList<MetaPipeEntity> aPipes, ForgeDirection side,
        HashSet<Node> aNodeMap) {
        walk: while (true) {
            if (!isPipe(aTileEntity)) return new Pair(aTileEntity, side);

            final BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) aTileEntity;
            final MetaPipeEntity tMetaPipe = (MetaPipeEntity) tPipe.getMetaTileEntity();
            final Node tNode = tPipe.getNode();
            if (tNode != null && aNodeMap.contains(tNode)) {
                // Preserve this walk's null return and DFS tree; retain only power topology metadata.
                if (this instanceof GenerateNodeMapPower && !tPipe.isInvalid()
                    && tMetaPipe.isConnectedAtSide(side.getOpposite())) {
                    loopClosure = new Pair(aTileEntity, side);
                }
                return null;
            }

            final ForgeDirection tSideOpposite = side.getOpposite();
            if (!tMetaPipe.isConnectedAtSide(tSideOpposite)) return null;

            final int tConnections = getNumberOfConnections(tMetaPipe);
            if (tConnections != 2) return new Pair(aTileEntity, side);

            for (final ForgeDirection s : ForgeDirection.VALID_DIRECTIONS) {
                if (s == tSideOpposite || !(tMetaPipe.isConnectedAtSide(s))) continue;
                final TileEntity tNewTileEntity = tPipe.getTileEntityAtSide(s);
                if (tNewTileEntity == null) return new Pair(aTileEntity, side);
                if (isPipe(tNewTileEntity)) {
                    aPipes.add(tMetaPipe);
                    aTileEntity = tNewTileEntity;
                    side = s;
                    continue walk;
                } else {
                    return new Pair(aTileEntity, s);
                }
            }
            return null;
        }
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
