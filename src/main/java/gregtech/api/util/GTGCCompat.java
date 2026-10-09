package gregtech.api.util;

import static gregtech.api.enums.Mods.GalacticraftCore;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;

public class GTGCCompat {

    public static boolean canConnect(TileEntity tTileEntity, ForgeDirection tDirection) {
        // GC Compat
        return GalacticraftCore.isModLoaded() && tTileEntity instanceof IEnergyHandlerGC
            && (!(tTileEntity instanceof IConnector)
                || ((IConnector) tTileEntity).canConnect(tDirection, NetworkType.POWER));
    }
}
