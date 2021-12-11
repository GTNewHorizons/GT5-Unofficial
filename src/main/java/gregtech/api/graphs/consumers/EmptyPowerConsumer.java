package gregtech.api.graphs.consumers;

import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

//this is here to apply voltage to dead ends
public class EmptyPowerConsumer extends ConsumerNode{
    public EmptyPowerConsumer(int aNodeValue, TileEntity aTileEntity, byte aSide, ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, aTileEntity, aSide, aConsumers);
    }

    @Override
    public boolean needsEnergy() {
        return false;
    }

    @Override
    public int injectEnergy(long aVoltage, long aMaxAmps) {
        BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) mTileEntity;
        PowerNodePath tPath =(PowerNodePath) tPipe.getNodePath();
        tPath.applyVoltage(aVoltage,true);
        return 0;
    }
}
