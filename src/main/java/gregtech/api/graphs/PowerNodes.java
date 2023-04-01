package gregtech.api.graphs;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.PowerNodePath;

/*
 * look for and power node that need power how this works a node only contains nodes that has a higher value then it
 * self except for 1 which is the return node this node also contains the highest known node value of its network this
 * network only includes nodes that have a higher value then it self so it does not know the highest known value that
 * the return node knows with these rules we can know for the target node to be in the network of a node, the target
 * node must have a value no less than the node we are looking and no greater than the highest value that node knows
 * this way we don't have to go over the entire network to look for it we also hold a list of all consumers so we can
 * check before looking if that consumer actually needs power and only look for nodes that actually need power
 */
public class PowerNodes {

    // check if the looked for node is next to or get the next node that is closer to it
    public static long powerNode(Node aCurrentNode, Node aPreviousNode, NodeList aConsumers, long aVoltage,
            long aMaxAmps) {
        long tAmpsUsed = 0;
        ConsumerNode tConsumer = (ConsumerNode) aConsumers.getNode();
        int tLoopProtection = 0;
        while (tConsumer != null) {
            int tTargetNodeValue = tConsumer.mNodeValue;
            // if the target node has a value less then the current node
            if (tTargetNodeValue < aCurrentNode.mNodeValue || tTargetNodeValue > aCurrentNode.mHighestNodeValue) {
                for (int j = 0; j < 6; j++) {
                    final Node tNextNode = aCurrentNode.mNeighbourNodes[j];
                    if (tNextNode != null && tNextNode.mNodeValue < aCurrentNode.mNodeValue) {
                        if (tNextNode.mNodeValue == tConsumer.mNodeValue) {
                            tAmpsUsed += processNodeInject(aCurrentNode, tConsumer, j, aMaxAmps - tAmpsUsed, aVoltage);
                            tConsumer = (ConsumerNode) aConsumers.getNextNode();
                        } else {
                            if (aPreviousNode == tNextNode) return tAmpsUsed;
                            tAmpsUsed += processNextNode(
                                    aCurrentNode,
                                    tNextNode,
                                    aConsumers,
                                    j,
                                    aMaxAmps - tAmpsUsed,
                                    aVoltage);
                            tConsumer = (ConsumerNode) aConsumers.getNode();
                        }
                        break;
                    }
                }
            } else {
                // if the target node has a node value greater then current node value
                for (int side = 5; side > -1; side--) {
                    final Node tNextNode = aCurrentNode.mNeighbourNodes[side];
                    if (tNextNode == null) continue;
                    if (tNextNode.mNodeValue > aCurrentNode.mNodeValue && tNextNode.mNodeValue < tTargetNodeValue) {
                        if (tNextNode == aPreviousNode) return tAmpsUsed;
                        tAmpsUsed += processNextNodeAbove(
                                aCurrentNode,
                                tNextNode,
                                aConsumers,
                                side,
                                aMaxAmps - tAmpsUsed,
                                aVoltage);
                        tConsumer = (ConsumerNode) aConsumers.getNode();
                        break;
                    } else if (tNextNode.mNodeValue == tTargetNodeValue) {
                        tAmpsUsed += processNodeInject(aCurrentNode, tConsumer, side, aMaxAmps - tAmpsUsed, aVoltage);
                        tConsumer = (ConsumerNode) aConsumers.getNextNode();
                        break;
                    }
                }
            }
            if (aMaxAmps - tAmpsUsed <= 0) {
                return tAmpsUsed;
            }
            if (tLoopProtection++ > 20) {
                throw new NullPointerException("infinite loop in powering nodes ");
            }
        }
        return tAmpsUsed;
    }

    // checking if target node is next to it or has a higher value then current node value
    // these functions are different to either go down or up the stack
    protected static long powerNodeAbove(Node aCurrentNode, Node aPreviousNode, NodeList aConsumers, long aVoltage,
            long aMaxAmps) {
        long tAmpsUsed = 0;
        int tLoopProtection = 0;
        ConsumerNode tConsumer = (ConsumerNode) aConsumers.getNode();
        while (tConsumer != null) {
            int tTargetNodeValue = tConsumer.mNodeValue;
            if (tTargetNodeValue > aCurrentNode.mHighestNodeValue || tTargetNodeValue < aCurrentNode.mNodeValue) {
                return tAmpsUsed;
            } else {
                for (int side = 5; side > -1; side--) {
                    final Node tNextNode = aCurrentNode.mNeighbourNodes[side];
                    if (tNextNode == null) continue;
                    if (tNextNode.mNodeValue > aCurrentNode.mNodeValue && tNextNode.mNodeValue < tTargetNodeValue) {
                        if (tNextNode == aPreviousNode) return tAmpsUsed;
                        tAmpsUsed += processNextNodeAbove(
                                aCurrentNode,
                                tNextNode,
                                aConsumers,
                                side,
                                aMaxAmps - tAmpsUsed,
                                aVoltage);
                        tConsumer = (ConsumerNode) aConsumers.getNode();
                        break;
                    } else if (tNextNode.mNodeValue == tTargetNodeValue) {
                        tAmpsUsed += processNodeInject(aCurrentNode, tConsumer, side, aMaxAmps - tAmpsUsed, aVoltage);
                        tConsumer = (ConsumerNode) aConsumers.getNextNode();
                        break;
                    }
                }
            }
            if (aMaxAmps - tAmpsUsed <= 0) {
                return tAmpsUsed;
            }
            if (tLoopProtection++ > 20) {
                throw new NullPointerException("infinite loop in powering nodes ");
            }
        }
        return tAmpsUsed;
    }

    protected static long processNextNode(Node aCurrentNode, Node aNextNode, NodeList aConsumers, int aSide,
            long aMaxAmps, long aVoltage) {
        if (aCurrentNode.locks[aSide].isLocked()) {
            aConsumers.getNextNode();
            return 0;
        }
        final PowerNodePath tPath = (PowerNodePath) aCurrentNode.mNodePaths[aSide];
        final PowerNodePath tSelfPath = (PowerNodePath) aCurrentNode.mSelfPath;
        long tVoltLoss = 0;
        if (tSelfPath != null) {
            tVoltLoss += tSelfPath.getLoss();
            tSelfPath.applyVoltage(aVoltage, false);
        }
        tPath.applyVoltage(aVoltage - tVoltLoss, true);
        tVoltLoss += tPath.getLoss();
        long tAmps = powerNode(aNextNode, aCurrentNode, aConsumers, aVoltage - tVoltLoss, aMaxAmps);
        tPath.addAmps(tAmps);
        if (tSelfPath != null) tSelfPath.addAmps(tAmps);
        return tAmps;
    }

    protected static long processNextNodeAbove(Node aCurrentNode, Node aNextNode, NodeList aConsumers, int aSide,
            long aMaxAmps, long aVoltage) {
        if (aCurrentNode.locks[aSide].isLocked()) {
            aConsumers.getNextNode();
            return 0;
        }
        final PowerNodePath tPath = (PowerNodePath) aCurrentNode.mNodePaths[aSide];
        final PowerNodePath tSelfPath = (PowerNodePath) aCurrentNode.mSelfPath;
        long tVoltLoss = 0;
        if (tSelfPath != null) {
            tVoltLoss += tSelfPath.getLoss();
            tSelfPath.applyVoltage(aVoltage, false);
        }
        tPath.applyVoltage(aVoltage - tVoltLoss, true);
        tVoltLoss += tPath.getLoss();
        long tAmps = powerNodeAbove(aNextNode, aCurrentNode, aConsumers, aVoltage - tVoltLoss, aMaxAmps);
        tPath.addAmps(tAmps);
        if (tSelfPath != null) tSelfPath.addAmps(tAmps);
        return tAmps;
    }

    protected static long processNodeInject(Node aCurrentNode, ConsumerNode aConsumer, int aSide, long aMaxAmps,
            long aVoltage) {
        if (aCurrentNode.locks[aSide].isLocked()) return 0;
        final PowerNodePath tPath = (PowerNodePath) aCurrentNode.mNodePaths[aSide];
        final PowerNodePath tSelfPath = (PowerNodePath) aCurrentNode.mSelfPath;
        long tVoltLoss = 0;
        if (tSelfPath != null) {
            tVoltLoss += tSelfPath.getLoss();
            tSelfPath.applyVoltage(aVoltage, false);
        }
        tPath.applyVoltage(aVoltage - tVoltLoss, true);
        tVoltLoss += tPath.getLoss();
        long tAmps = aConsumer.injectEnergy(aVoltage - tVoltLoss, aMaxAmps);
        tPath.addAmps(tAmps);
        if (tSelfPath != null) tSelfPath.addAmps(tAmps);
        return tAmps;
    }
}
