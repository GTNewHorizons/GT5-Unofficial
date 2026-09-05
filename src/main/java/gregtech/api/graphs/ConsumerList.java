package gregtech.api.graphs;

import java.util.ArrayList;

import gregtech.api.graphs.consumers.ConsumerNode;

// every consumer reachable from one power network, shared by all of that network's nodes
// holds per network what each power transfer used to rebuild per emitter per tick
public class ConsumerList extends ArrayList<ConsumerNode> {

    private static final long serialVersionUID = 1L;
    private static final ConsumerNode[] NO_CONSUMERS = new ConsumerNode[0];

    private ConsumerNode[] candidates = NO_CONSUMERS;
    private Node[] buffer;
    private NodeList nodeList;
    private int builtFor = -1;
    private boolean bufferInUse = false;

    // dead ends answer needsEnergy() with a constant false, so skipping them here skips them from every scan
    public ConsumerNode[] candidates() {
        build();
        return candidates;
    }

    /** @return a buffer with one spare entry for the null {@link NodeList} stops at, or null while one is lent out. */
    public Node[] borrowBuffer() {
        if (bufferInUse) return null;
        build();
        bufferInUse = true;
        return buffer;
    }

    public NodeList borrowedNodeList(Node[] borrowed) {
        nodeList.reset(borrowed);
        return nodeList;
    }

    public void releaseBuffer() {
        bufferInUse = false;
    }

    // rebuilds while the map generator is still adding to the list
    private void build() {
        final int size = size();
        if (builtFor == size) return;

        int count = 0;
        for (int i = 0; i < size; i++) {
            if (get(i).canEverNeedEnergy()) count++;
        }

        candidates = count == 0 ? NO_CONSUMERS : new ConsumerNode[count];
        count = 0;
        for (int i = 0; i < size; i++) {
            final ConsumerNode consumer = get(i);
            if (consumer.canEverNeedEnergy()) candidates[count++] = consumer;
        }

        buffer = new Node[size + 1];
        nodeList = new NodeList(buffer);
        builtFor = size;
    }
}
