package gregtech.api.graphs;

// keep track on which node is being looked for across the recursive functions
public class NodeList {

    Node[] mNodes;
    int mCounter = 0;

    public NodeList(Node[] mNodes) {
        this.mNodes = mNodes;
    }

    public void reset(Node[] nodes) {
        this.mNodes = nodes;
        this.mCounter = 0;
    }

    Node getNextNode() {
        if (++mCounter < mNodes.length) return mNodes[mCounter];
        else return null;
    }

    Node getNode() {
        if (mCounter < mNodes.length) return mNodes[mCounter];
        else return null;
    }
}
