package gregtech.api.graphs.paths;

import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;

//to contain all info about the path between nodes
public class NodePath {
    protected MetaPipeEntity[] mPipes;

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
        for (int i = 0; i < mPipes.length; i++) {
            BaseMetaPipeEntity tBasePipe = (BaseMetaPipeEntity) mPipes[i].getBaseMetaTileEntity();
            if (tBasePipe != null) {
                tBasePipe.setNodePath(null);
            }
        }
    }
}
