package gtPlusPlus.core.util.minecraft;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

    public static void messagePlayer(final EntityPlayer P, final IChatComponent S) {
        P.addChatComponentMessage(S);
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

    public static EntityPlayer getPlayerOnServerFromUUID(final UUID parUUID) {
        if (parUUID == null) {
            return null;
        }
        for (final EntityPlayerMP player : getOnlinePlayers()) {
            if (player.getUniqueID()
                .equals(parUUID)) {
                return player;
            }
        }
        return null;
    }

    public static boolean isPlayerOP(final EntityPlayer player) {
        return player.canCommandSenderUseCommand(2, "");
    }

    // Not Clientside
    public static ItemStack getItemStackInPlayersHand(final World world, final String Name) {
        return PlayerUtils.getItemStackInPlayersHand(getPlayer(Name));
    }

    @SideOnly(Side.CLIENT)
    public static ItemStack getItemStackInPlayersHand() {
        return PlayerUtils.getItemStackInPlayersHand(Minecraft.getMinecraft().thePlayer);
    }

    public static ItemStack getItemStackInPlayersHand(final EntityPlayer player) {
        if (player == null) return null;
        return player.getHeldItem();
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

    public static boolean isCreative(EntityPlayer aPlayer) {
        return aPlayer.capabilities.isCreativeMode;
    }

    public static boolean canTakeDamage(EntityPlayer aPlayer) {
        return !aPlayer.capabilities.disableDamage;
    }

    public static boolean isRealPlayer(EntityLivingBase entity) {
        return entity instanceof EntityPlayer p && !p.getClass()
            .getName()
            .contains("Fake");
    }
}
