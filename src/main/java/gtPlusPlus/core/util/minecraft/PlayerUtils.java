package gtPlusPlus.core.util.minecraft;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.Utils;

public class PlayerUtils {

    public static List<EntityPlayerMP> getOnlinePlayers() {
        return MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList;
    }

    public static void messagePlayer(final EntityPlayer P, final String S) {
        GTUtility.sendChatToPlayer(P, S);
    }

    public static EntityPlayer getPlayer(final String name) {
        try {
            for (final EntityPlayer temp : getOnlinePlayers()) {
                if (temp.getDisplayName()
                    .equalsIgnoreCase(name)) {
                    return temp;
                }
            }
        } catch (final Throwable ignored) {}
        return null;
    }

    public static UUID getPlayersUUIDByName(final String aPlayerName) {
        final EntityPlayer player = PlayerUtils.getPlayer(aPlayerName);
        if (player != null) {
            return player.getUniqueID();
        }
        return null;
    }

    public static void messageAllPlayers(String string) {
        Utils.sendServerMessage(string);
    }

    public static boolean isRealPlayer(EntityLivingBase entity) {
        return entity instanceof EntityPlayer p && !p.getClass()
            .getName()
            .contains("Fake");
    }
}
