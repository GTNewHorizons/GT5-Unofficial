package gregtech.api.graphs.consumers;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.graphs.paths.PowerNodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;

// this is here to apply voltage to dead ends
public class EmptyPowerConsumer extends ConsumerNode {

    public EmptyPowerConsumer(int aNodeValue, TileEntity aTileEntity, ForgeDirection side,
        ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, aTileEntity, side, aConsumers);
    }

    @Override
    public boolean needsEnergy() {
        return false;
    }

    @Override
    public int injectEnergy(long aVoltage, long aMaxAmps) {
        BaseMetaPipeEntity tPipe = (BaseMetaPipeEntity) mTileEntity;
        PowerNodePath tPath = (PowerNodePath) tPipe.getNodePath();
        tPath.applyVoltage(aVoltage, true);
        return 0;
    }
}
