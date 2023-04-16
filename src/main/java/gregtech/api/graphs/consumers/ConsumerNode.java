package gregtech.api.graphs.consumers;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;

import gregtech.api.graphs.Node;

// node attached to a tile entity that can consume stuff from the network
public class ConsumerNode extends Node {

    public byte mSide;

    public ConsumerNode(int aNodeValue, TileEntity aTileEntity, ForgeDirection aSide,
        ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, aTileEntity, aConsumers);
        this.mSide = aSide;
    }

    public boolean needsEnergy() {
        return !mTileEntity.isInvalid();
    }

    public int injectEnergy(long aVoltage, long aMaxAmps) {
        return 0;
    }
}
