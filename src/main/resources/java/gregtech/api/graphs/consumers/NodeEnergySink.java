package gregtech.api.graphs.consumers;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import ic2.api.energy.tile.IEnergySink;

// consumer for IC2 machines
public class NodeEnergySink extends ConsumerNode {

    public NodeEnergySink(int nodeValue, IEnergySink tileEntity, ForgeDirection side,
        ArrayList<ConsumerNode> consumers) {
        super(nodeValue, (TileEntity) tileEntity, side, consumers);
    }

    @Override
    public boolean needsEnergy() {
        return super.needsEnergy() && ((IEnergySink) mTileEntity).getDemandedEnergy() > 0;
    }

    @Override
    public int injectEnergy(long aVoltage, long aMaxAmps) {
        int tUsedAmps = 0;
        while (aMaxAmps > tUsedAmps && ((IEnergySink) mTileEntity).getDemandedEnergy() > 0
            && ((IEnergySink) mTileEntity).injectEnergy(mSide, aVoltage, aVoltage) < aVoltage) tUsedAmps++;
        return tUsedAmps;
    }
}
