package gregtech.common.powergoggles;

import java.math.BigInteger;
import java.math.RoundingMode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

import com.google.common.math.BigIntegerMath;

import appeng.api.util.DimensionalCoord;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;
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

    public static String format(BigInteger EU) {
        switch (PowerGogglesConfigHandler.formatIndex) {
            case 1:
                return toCustom(EU);
            case 2:
                return toCustom(EU, true, 3);
            default:
                return toCustom(EU, false, 1);
        }
    }

    private static String toCustom(BigInteger EU) {
        return toCustom(EU, false, 3);
    }

    private static String toCustom(BigInteger EU, boolean useSI, int baseDigits) {
        String[] suffixes = { "", "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };
        if (EU.abs()
            .compareTo(BigInteger.valueOf(1)) < 0) {
            return "0";
        }
        int exponent = BigIntegerMath.log10(EU.abs(), RoundingMode.FLOOR);
        int remainder = exponent % baseDigits;

        String euString = EU.toString();
        if (EU.abs()
            .compareTo(BigInteger.valueOf(1000)) < 0) {
            return euString;
        }
        int negative = EU.compareTo(BigInteger.valueOf(1)) < 0 ? 1 : 0;
        String base = euString.substring(0, remainder + 1 + negative);
        String decimal = euString.substring(remainder + 1 + negative, Math.min(exponent, remainder + 4));
        int E = exponent - remainder;

        if (useSI && (E / 3) <= suffixes.length - 1) return String.format("%s.%s%s", base, decimal, suffixes[E / 3]);
        return String.format("%s.%sE%d", base, decimal, E);
    }
}
