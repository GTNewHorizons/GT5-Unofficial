package gregtech.api.graphs.paths;

import gregtech.api.graphs.Lock;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;

// to contain all info about the path between nodes
public class NodePath {

    protected MetaPipeEntity[] mPipes;
    public Lock lock = new Lock();

    public NodePath(MetaPipeEntity[] aCables) {
        this.mPipes = aCables;
        processPipes();
    }

    protected void processPipes() {
        for (MetaPipeEntity tPipe : mPipes) {
            BaseMetaPipeEntity basePipe = (BaseMetaPipeEntity) tPipe.getBaseMetaTileEntity();
            basePipe.setNodePath(this);
        }
    }

    public void clearPath() {
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
