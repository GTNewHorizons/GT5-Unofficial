package gregtech.common.misc.techtree.gui;

// Paper on layered DAG layouting, this algorithm will be used to draw the technology tree!
// https://web.archive.org/web/20180728233917/https://www.it.usyd.edu.au/~shhong/fab.pdf
// Some notes:
// 1. Since our technology tree has no cycles, we can omit the first step of the algorithm on cycle removal.
// Note that the acyclic-ness of the tree is enforced by the use of the technology builder. After construction,
// a technology cannot have prerequisites added to it, so it's impossible to construct a cyclic graph.
// 2. A major assumption the technology system will make is that the entire tech graph is connected.
// This simplifies some code greatly.
// 3. To traverse the technology tree more easily, we will implicitly define a root technology in the tree.
// This node will be hidden in the GUI and is only for internal purposes.
// Note that this implicit definition also enforces the connected-ness of the tree.
// 4. For layering, we will use Coffman-Graham layering. The reason for this is that we can have a hard limit on the
// width
// of each layer using this algorithm. The other algorithms focus on minimizing height or edge span, both of which are
// not the biggest issue in our case because we will need a large scroll region for the technology tree anyway.

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gregtech.common.misc.techtree.TechnologyRegistry;
import gregtech.common.misc.techtree.interfaces.IPrerequisite;
import gregtech.common.misc.techtree.interfaces.ITechnology;

public class TechTreeLayout {

    private static TechTreeLayout INSTANCE = null;

    public static class Layering {

        private final Map<String, Integer> layerForTech = new HashMap<>();
        private final Map<Integer, Set<ITechnology>> layers = new HashMap<>();

        public Set<ITechnology> getLayer(int layer) {
            return layers.computeIfAbsent(layer, k -> new HashSet<>());
        }

        public void setLayerForTech(ITechnology tech, int layer) {
            Set<ITechnology> layerSet = getLayer(layer);
            layerForTech.put(tech.getInternalName(), layer);
            layerSet.add(tech);
        }

        public int getDisplayLayer(ITechnology tech) {
            // Layers are assigned with sinks having a lower number, so for display purposes
            // simply invert it based on the number of layers
            int numLayers = layers.size();
            return numLayers - getInternalLayer(tech);
        }

        public int getInternalLayer(ITechnology tech) {
            return layerForTech.get(tech.getInternalName());
        }
    }

    public final Layering layerInfo;

    private TechTreeLayout(Layering layers) {
        this.layerInfo = layers;
    }

    private static boolean isOrderUnassigned(HashMap<String, Integer> order, ITechnology tech, int numNodes) {
        return order.get(tech.getInternalName()) == numNodes + 1;
    }

    private static Set<Integer> computeOrderSet(HashMap<String, Integer> order, ITechnology tech) {
        // The set of ordering numbers for this technology is the set of orders of its prerequisites
        Set<Integer> orderSet = new HashSet<>();
        for (IPrerequisite parent : tech.getPrerequisites()) {
            // If this parent is a technology, add its ordering number to the order set
            ITechnology parentTech = parent.getTechnology();
            if (parentTech != null) orderSet.add(order.get(parentTech.getInternalName()));
        }
        return orderSet;
    }

    // Returns true if s < t.
    // Note that this function MODIFIES the input sets, but since we do not need them later this is fine
    private static boolean compareOrderSets(Set<Integer> s, Set<Integer> t) {
        // s < t if s is empty and t is not
        if (s.isEmpty() && !t.isEmpty()) return true;
        // If neither is empty, do a proper comparison
        if (!s.isEmpty() && !t.isEmpty()) {
            Integer maxS = s.stream()
                .max(Integer::compare)
                .get();
            Integer maxT = t.stream()
                .max(Integer::compare)
                .get();
            // If the largest value in s is smaller than that in t, s < t
            if (maxS < maxT) return true;
            // If they are equal, remove the maximum values and keep comparing
            else if (maxS.equals(maxT)) {
                // Note that this modifies the sets, but because they are not used after this call it's fine to do so
                // (until we start caching)
                s.remove(maxS);
                t.remove(maxT);
                return compareOrderSets(s, t);
            }
        }
        // In any other case, t > s
        return false;
    }

    private static HashMap<String, Integer> orderNodes() {
        HashMap<String, Integer> nodeOrdering = new HashMap<>();

        // First initialize all values in the order to the maximum value.
        // If we have n nodes, the maximum order value is n + 1.
        int numNodes = TechnologyRegistry.numTechnologies();
        for (ITechnology tech : TechnologyRegistry.getTechnologies()) {
            String id = tech.getInternalName();
            nodeOrdering.put(id, numNodes + 1);
        }

        // Now we loop over all possible ordering values and assign them
        for (int i = 1; i <= numNodes; ++i) {
            // Among the set of unordered nodes (this is the nodes with ordering n + 1), we find the node
            // with the minimum order value and assign it to this. Note that currently, this algorithm is *quite* slow.
            // I'm sure there is a better way to construct this, but for now we will go with a simple brute force
            // version to demonstrate a proof of concept and find something that works.
            ITechnology chosenTech = null;
            for (ITechnology tech : TechnologyRegistry.getTechnologies()) {
                // If this node is unassigned, consider it for our next candidate
                if (isOrderUnassigned(nodeOrdering, tech, numNodes)) {
                    // If we don't have anything to compare it to yet, simply pick this one for now.
                    if (chosenTech == null) {
                        chosenTech = tech;
                        continue;
                    }
                    // If we do have something to compare it to, we need to do the comparison.
                    // Note that while it is quite inefficient to recompute this ordering every time, since the map
                    // of orders can change the simplest way for now is to recompute it, since caching it is not
                    // correct.
                    Set<Integer> candidateOrder = computeOrderSet(nodeOrdering, tech);
                    Set<Integer> knownOrder = computeOrderSet(nodeOrdering, chosenTech);
                    // If the new candidate has a smaller ordering, assign it as our new chosen technology.
                    if (compareOrderSets(candidateOrder, knownOrder)) {
                        chosenTech = tech;
                    }
                }
            }
            if (chosenTech == null) throw new NullPointerException("Previous loop should never produce a null value");
            nodeOrdering.put(chosenTech.getInternalName(), i);
        }

        return nodeOrdering;
    }

    private static boolean canLayerNode(ITechnology node, Set<ITechnology> nodesToLayer) {
        // A node can be assigned a layer if all of its descendants have been layered
        for (ITechnology descendant : node.getChildren()) {
            if (nodesToLayer.contains(descendant)) return false;
        }
        return true;
    }

    private static boolean mayAssignToLayer(ITechnology node, int layer, Layering layering) {
        // If all outgoing nodes are contained in lower layers than this layer, we can add this node to this layer
        for (ITechnology descendant : node.getChildren()) {
            // We know that this node must have a layer assigned,
            // based on the precondition in canLayerNode
            int descLayer = layering.getInternalLayer(descendant);
            // If this layer is not strictly smaller than the layer we are trying to assign this node to,
            // forbid it
            if (descLayer >= layer) {
                return false;
            }
        }
        return true;
    }

    private static Layering calculateLayering(int maxWidth) {
        // Implements the Coffman-Graham layering algorithm to assign a layer to each node in the graph
        Layering layering = new Layering();

        // Phase 1: Construct an ordering of all nodes in the tree
        HashMap<String, Integer> order = orderNodes();
        // Phase 2: Assign a layer to each node

        // Set of all nodes without an assigned layer. Initially this is obviously all nodes.
        Set<ITechnology> nodesToLayer = new HashSet<>(TechnologyRegistry.getTechnologies());

        // Keep repeating as long as there are nodes without an unassigned layer
        int currentLayer = 1;
        while (!nodesToLayer.isEmpty()) {
            // Find the next node to layer. We do this by finding the node with minimal order that can be layered
            int minOrder = Integer.MAX_VALUE;
            ITechnology minNode = null;
            for (ITechnology node : nodesToLayer) {
                // If we haven't found a node to layer yet, assign one
                if (canLayerNode(node, nodesToLayer)) {
                    // Compare the order of this node with our current minimum.
                    int nodeOrder = order.get(node.getInternalName());
                    if (nodeOrder < minOrder) {
                        minOrder = nodeOrder;
                        minNode = node;
                    }
                }
            }
            if (minNode == null) throw new NullPointerException("This loop should never produce a null value");
            // Found a minimal order, layerable node, now assign it a layer
            Set<ITechnology> layerSet = layering.getLayer(currentLayer);
            // If this layer is not exceeding the max width, and we can assign this node to it, do so
            if (layerSet.size() < maxWidth && mayAssignToLayer(minNode, currentLayer, layering)) {
                layering.setLayerForTech(minNode, currentLayer);
            } else {
                // Otherwise assign it to a new layer
                currentLayer += 1;
                layering.setLayerForTech(minNode, currentLayer);
            }
            nodesToLayer.remove(minNode);
        }

        return layering;
    }

    private static TechTreeLayout construct() {
        // Construct layout information of the technology tree based on the current technology registry.

        // TODO: Parameter to configure this somehow, or decide based on gui layout
        final int maxWidth = 3;
        Layering layers = calculateLayering(maxWidth);
        return new TechTreeLayout(layers);
    }

    public static TechTreeLayout constructOrGet() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = construct();
        return INSTANCE;
    }
}
