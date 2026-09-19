package gregtech.api.graphs.paths;

import gregtech.api.graphs.GenerateNodeMap;
import gregtech.api.graphs.Lock;
import gregtech.api.graphs.Node;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;

// to contain all info about the path between nodes
public class NodePath {

    protected MetaPipeEntity[] mPipes;
    public Lock lock = new Lock();
    private Node node;
    private boolean valid = true;

    public NodePath(MetaPipeEntity[] aCables) {
        this.mPipes = aCables;
        processPipes();
    }

    public MetaPipeEntity[] getPipes() {
        return mPipes;
    }

    public void setNodeMap(Node node) {
        this.node = node;
    }

    public void invalidateNodeMap() {
        if (node == null) {
            clearPath();
        } else {
            GenerateNodeMap.clearNodeMap(node, -1);
        }
    }

    public Node getNodeMap() {
        return node;
    }

    public boolean isValid() {
        return valid;
    }

    public void invalidate() {
        valid = false;
    }

    protected void processPipes() {
        for (MetaPipeEntity tPipe : mPipes) {
            BaseMetaPipeEntity basePipe = (BaseMetaPipeEntity) tPipe.getBaseMetaTileEntity();
            basePipe.setNodePath(this);
        }
    }

    public void clearPath() {
        valid = false;
        node = null;
        for (MetaPipeEntity mPipe : mPipes) {
            BaseMetaPipeEntity tBasePipe = (BaseMetaPipeEntity) mPipe.getBaseMetaTileEntity();
            if (tBasePipe != null) {
                tBasePipe.setNodePath(null);
            }
        }
    }

    public void reloadLocks() {
        for (MetaPipeEntity pipe : mPipes) {
            BaseMetaPipeEntity basePipe = (BaseMetaPipeEntity) pipe.getBaseMetaTileEntity();
            if (basePipe != null) {
                basePipe.reloadLocks();
            }
        }
    }
}
