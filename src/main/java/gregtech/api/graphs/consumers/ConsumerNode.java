package gregtech.api.graphs.consumers;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.graphs.ConsumerList;
import gregtech.api.graphs.Node;

/**
 * A node attached to a {@code TileEntity} that can consume stuff from the network.
 */
public class ConsumerNode extends Node {

    public ForgeDirection mSide;

    public ConsumerNode(int aNodeValue, TileEntity aTileEntity, ForgeDirection side, ConsumerList aConsumers) {
        super(aNodeValue, aTileEntity, aConsumers);
        this.mSide = side;
    }

    public boolean needsEnergy() {
        return !mTileEntity.isInvalid();
    }

    // false when needsEnergy() can never become true, so ConsumerList can drop this node once and for all
    public boolean canEverNeedEnergy() {
        return true;
    }

    public int injectEnergy(long aVoltage, long aMaxAmps) {
        return 0;
    }
}
