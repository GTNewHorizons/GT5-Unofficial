package gregtech.api.util;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.GregTech_API;

public class GT_GC_Compat {

    public static long insertEnergyInto(TileEntity tTileEntity, long aVoltage, ForgeDirection tDirection) {
        // GC Compat
        if (GregTech_API.mGalacticraft && tTileEntity instanceof IEnergyHandlerGC) {
            if (!(tTileEntity instanceof IConnector)
                || ((IConnector) tTileEntity).canConnect(tDirection, NetworkType.POWER)) {
                EnergySource eSource = new EnergySourceAdjacent(tDirection);

                float tSizeToReceive = aVoltage * EnergyConfigHandler.IC2_RATIO,
                    tStored = ((IEnergyHandlerGC) tTileEntity).getEnergyStoredGC(eSource);
                if (tSizeToReceive >= tStored
                    || tSizeToReceive <= ((IEnergyHandlerGC) tTileEntity).getMaxEnergyStoredGC(eSource) - tStored) {
                    float tReceived = ((IEnergyHandlerGC) tTileEntity).receiveEnergyGC(eSource, tSizeToReceive, false);
                    if (tReceived > 0) {
                        tSizeToReceive -= tReceived;
                        while (tSizeToReceive > 0) {
                            tReceived = ((IEnergyHandlerGC) tTileEntity)
                                .receiveEnergyGC(eSource, tSizeToReceive, false);
                            if (tReceived < 1) break;
                            tSizeToReceive -= tReceived;
                        }
                        return 1;
                    }
                }
            }
            return 0;
        }
        return 2;
    }

    public static boolean canConnect(TileEntity tTileEntity, ForgeDirection tDirection) {
        // GC Compat
        if (GregTech_API.mGalacticraft && tTileEntity instanceof IEnergyHandlerGC
            && (!(tTileEntity instanceof IConnector)
                || ((IConnector) tTileEntity).canConnect(tDirection, NetworkType.POWER)))
            return true;
        return false;
    }
}
