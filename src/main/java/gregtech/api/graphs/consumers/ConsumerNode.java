package gregtech.api.graphs.consumers;

import gregtech.api.graphs.Node;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

// node attached to a tile entity that can consume stuff from the network
public class ConsumerNode extends Node {
    public byte mSide;
    public ConsumerNode(int aNodeValue, TileEntity aTileEntity, byte aSide, ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue,aTileEntity,aConsumers);
        this.mSide = aSide;
    }

    public boolean needsEnergy() {
        return !mTileEntity.isInvalid();
    }

    public int injectEnergy(long aVoltage, long aMaxAmps) {
        return 0;
    }
}
