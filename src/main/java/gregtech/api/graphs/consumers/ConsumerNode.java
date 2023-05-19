package gregtech.api.graphs.consumers;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.graphs.Node;

// node attached to a tile entity that can consume stuff from the network
public class ConsumerNode extends Node {

    public ForgeDirection mSide;

    public ConsumerNode(int aNodeValue, TileEntity aTileEntity, ForgeDirection side,
        ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, aTileEntity, aConsumers);
        this.mSide = side;
    }

    public boolean needsEnergy() {
        return !mTileEntity.isInvalid();
    }

    public int injectEnergy(long aVoltage, long aMaxAmps) {
        return 0;
    }
}
