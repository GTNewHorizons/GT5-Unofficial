package gtPlusPlus.core.util.minecraft;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class PlayerUtils {

    public static final Map<String, EntityPlayer> mCachedFakePlayers = new WeakHashMap<>();
    private static final Class mThaumcraftFakePlayer;

    static {
        if (ReflectionUtils.doesClassExist("thaumcraft.common.lib.FakeThaumcraftPlayer")) {
            mThaumcraftFakePlayer = ReflectionUtils.getClass("thaumcraft.common.lib.FakeThaumcraftPlayer");
        } else {
            mThaumcraftFakePlayer = null;
        }
    }

    public static List<EntityPlayerMP> getOnlinePlayers() {
        final List<EntityPlayerMP> onlinePlayers = MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList;
        return onlinePlayers;
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

    public static void cacheFakePlayer(EntityPlayer aPlayer) {
        ChunkCoordinates aChunkLocation = aPlayer.getPlayerCoordinates();
        // Cache Fake Player
        if (aPlayer instanceof FakePlayer
            || (mThaumcraftFakePlayer != null && mThaumcraftFakePlayer.isInstance(aPlayer))
            || (aPlayer.getCommandSenderName() == null || aPlayer.getCommandSenderName()
                .isEmpty())
            || (aPlayer.isEntityInvulnerable() && !aPlayer.canCommandSenderUseCommand(0, "") && (aChunkLocation == null)
                || (aChunkLocation.posX == 0 && aChunkLocation.posY == 0 && aChunkLocation.posZ == 0))) {
            mCachedFakePlayers.put(
                aPlayer.getUniqueID()
                    .toString(),
                aPlayer);
        }
    }

    public static boolean isCachedFakePlayer(String aUUID) {
        return mCachedFakePlayers.containsKey(aUUID);
    }

    public static boolean isRealPlayer(EntityLivingBase aEntity) {
        if (aEntity instanceof EntityPlayer p) {
            ChunkCoordinates aChunkLocation = p.getPlayerCoordinates();
            if (p instanceof FakePlayer) {
                cacheFakePlayer(p);
                return false;
            }
            if (mThaumcraftFakePlayer != null && mThaumcraftFakePlayer.isInstance(p)) {
                cacheFakePlayer(p);
                return false;
            }
            if (p.getCommandSenderName() == null) {
                cacheFakePlayer(p);
                return false;
            }
            if (p.getCommandSenderName()
                .isEmpty()) {
                cacheFakePlayer(p);
                return false;
            }
            if (p.isEntityInvulnerable() && !p.canCommandSenderUseCommand(0, "")
                && (aChunkLocation.posX == 0 && aChunkLocation.posY == 0 && aChunkLocation.posZ == 0)) {
                cacheFakePlayer(p);
                return false;
            }
            return !isCachedFakePlayer(
                p.getUniqueID()
                    .toString());
        }
        return false;
    }
}
