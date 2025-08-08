package gregtech.api.graphs.consumers;

import java.util.ArrayList;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseMetaTileEntity;

// consumer for gt machines
public class NodeGTBaseMetaTile extends ConsumerNode {

    public NodeGTBaseMetaTile(int aNodeValue, BaseMetaTileEntity aTileEntity, ForgeDirection side,
        ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, aTileEntity, side, aConsumers);
    }

    @Override
    public int injectEnergy(long aVoltage, long aMaxAmps) {
        return (int) ((IEnergyConnected) mTileEntity).injectEnergyUnits(mSide, aVoltage, aMaxAmps);
    }

    @Override
    public boolean needsEnergy() {
        BaseMetaTileEntity tTileEntity = (BaseMetaTileEntity) mTileEntity;
        return super.needsEnergy() && tTileEntity.getStoredEU() < tTileEntity.getEUCapacity();
    }
}
