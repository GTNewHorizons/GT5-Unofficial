package gregtech.common.powergoggles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

import appeng.api.util.DimensionalCoord;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class PowerGogglesUtil {

    public static boolean isLSC(TileEntity tileEntity) {
        return tileEntity instanceof IGregTechTileEntity te
            && te.getMetaTileEntity() instanceof MTELapotronicSuperCapacitor;
    }

    public static ItemStack getPlayerGoggles(EntityPlayer player) {
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
        for (ItemStack bauble : baubles.stackList) {
            if (bauble != null && bauble.getItem() instanceof ItemPowerGoggles) {
                return bauble;
            }
        }
        return null;
    }

    public static boolean isPlayerWearingGoggles(EntityPlayer player) {
        return getPlayerGoggles(player) != null;
    }

    public static MTELapotronicSuperCapacitor getLsc(DimensionalCoord lscLink) {
        if (lscLink == null) {
            return null;
        }

        WorldServer lscDim = MinecraftServer.getServer()
            .worldServerForDimension(lscLink.getDimension());

        // TODO: REMOVE IN 2.9
        // Should be safe to remove this check in 2.9. This is a safeguard against a situation where in one
        // Singleplayer world you link goggles to something in a dimension that doesn't normally exist e.g. Personal Dim
        // And you quit and select another world.
        if (lscDim == null) {
            return null;
        }

        TileEntity tileEntity = lscDim.getTileEntity(lscLink.x, lscLink.y, lscLink.z);

        if (tileEntity == null) {
            return null;
        }
        if (!isLSC(tileEntity)) {
            return null;
        }

        return ((MTELapotronicSuperCapacitor) ((IGregTechTileEntity) tileEntity).getMetaTileEntity());
    }
}
