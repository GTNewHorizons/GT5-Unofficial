package gregtech.api.graphs.consumers;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;

public class NodeGCEnergyHandler extends ConsumerNode {

    float restGJ = 0;

    public NodeGCEnergyHandler(int nodeValue, IEnergyHandlerGC tileEntity, ForgeDirection side,
        ArrayList<ConsumerNode> aConsumers) {
        super(nodeValue, (TileEntity) tileEntity, side, aConsumers);
    }

    @Override
    public int injectEnergy(long voltage, long maxAmps) {
        EnergySource eSource = new EnergySource.EnergySourceAdjacent(mSide);
        IEnergyHandlerGC handler = (IEnergyHandlerGC) mTileEntity;

        float gjOut = voltage * EnergyConfigHandler.IC2_RATIO;
        int ampsUsed = 0;
        if (restGJ < gjOut) {
            restGJ += gjOut;
            ampsUsed = 1;
        }

        float capacity = handler.getMaxEnergyStoredGC(eSource) - handler.getEnergyStoredGC(eSource);
        if (capacity >= restGJ) {
            float received = handler.receiveEnergyGC(eSource, restGJ, false);
            restGJ -= received;
            return ampsUsed;
        }
        return 0;
    }
}
