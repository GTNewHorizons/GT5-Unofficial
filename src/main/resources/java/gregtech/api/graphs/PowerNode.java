package gregtech.api.graphs;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;

import gregtech.api.graphs.consumers.ConsumerNode;

// base node for power networks
public class PowerNode extends Node {

    public boolean mHadVoltage = false;

    public PowerNode(int aNodeValue, TileEntity aTileEntity, ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, aTileEntity, aConsumers);
    }
}
