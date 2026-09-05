package gregtech.api.graphs.consumers;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.graphs.ConsumerList;
import gregtech.api.interfaces.tileentity.IEnergyConnected;

public class NodeEnergyConnected extends ConsumerNode {

    public NodeEnergyConnected(int aNodeValue, IEnergyConnected aTileEntity, ForgeDirection side,
        ConsumerList aConsumers) {
        super(aNodeValue, (TileEntity) aTileEntity, side, aConsumers);
    }

    @Override
    public int injectEnergy(long aVoltage, long aMaxAmps) {
        return (int) ((IEnergyConnected) mTileEntity).injectEnergyUnits(mSide, aVoltage, aMaxAmps);
    }
}
