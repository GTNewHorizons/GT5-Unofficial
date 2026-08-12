package gregtech.api.graphs;

import net.minecraft.tileentity.TileEntity;

// base node for power networks
public class PowerNode extends Node {

    public boolean mHadVoltage = false;

    public PowerNode(int aNodeValue, TileEntity aTileEntity, ConsumerList aConsumers) {
        super(aNodeValue, aTileEntity, aConsumers);
    }
}
