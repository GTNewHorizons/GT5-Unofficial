package gregtech.api.graphs;

import gregtech.api.graphs.consumers.ConsumerNode;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

// base node for power networks
public class PowerNode extends Node{
    public boolean mHadVoltage = false;
    public PowerNode(int aNodeValue, TileEntity aTileEntity, ArrayList<ConsumerNode> aConsumers) {
        super(aNodeValue, aTileEntity, aConsumers);
    }
}
