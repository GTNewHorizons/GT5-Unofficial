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
        float offeredGJ = restGJ;
        int ampsUsed = 0;
        if (restGJ < gjOut) {
            offeredGJ += gjOut;
            ampsUsed = 1;
        }

        float capacity = handler.getMaxEnergyStoredGC(eSource) - handler.getEnergyStoredGC(eSource);
        if (capacity >= offeredGJ) {
            float received = handler.receiveEnergyGC(eSource, offeredGJ, false);
            restGJ = offeredGJ - received;
            return ampsUsed;
        }
        // A rejected new packet must not prevent delivery of energy already paid for.
        if (restGJ > 0 && capacity > 0) {
            restGJ -= handler.receiveEnergyGC(eSource, Math.min(restGJ, capacity), false);
        }
        return 0;
    }
}
