package com.github.technus.tectech.mechanics.data;

import static java.nio.charset.Charset.forName;

import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.util.TT_Utility;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class PlayerPersistence {
    private final HashMap<UUID, NBTTagCompound> map = new HashMap<>();
    private final String extension;

    public PlayerPersistence(String extension) {
        this.extension = extension;
    }

    public NBTTagCompound getDataOrSetToNewTag(UUID uuid1, UUID uuid2) {
        NBTTagCompound tag = map.get(uuid1);
        if (tag != null) {
            return tag;
        }
        tag = map.get(uuid2);
        if (tag != null) {
            return tag;
        }
        tag = TT_Utility.getPlayerData(uuid1, uuid2, extension);
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        map.put(uuid1, tag);
        map.put(uuid2, tag);
        return tag;
    }

    public NBTTagCompound getDataOrSetToNewTag(EntityPlayer player) {
        return getDataOrSetToNewTag(
                player.getUniqueID(),
                UUID.nameUUIDFromBytes(player.getCommandSenderName().getBytes(forName("UTF-8"))));
    }

    public void putDataOrSetToNewTag(UUID uuid1, UUID uuid2, NBTTagCompound tagCompound) {
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
        }
        map.put(uuid1, tagCompound);
        map.put(uuid2, tagCompound);
    }

    public void putDataOrSetToNewTag(EntityPlayer player, NBTTagCompound tagCompound) {
        putDataOrSetToNewTag(
                player.getUniqueID(),
                UUID.nameUUIDFromBytes(player.getCommandSenderName().getBytes(forName("UTF-8"))),
                tagCompound);
    }

    public void saveData(EntityPlayer player) {
        TT_Utility.savePlayerFile(player, extension, getDataOrSetToNewTag(player));
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
                for (Object playerEntity : worldServer.playerEntities) {
                    NetworkDispatcher.INSTANCE.sendTo(
                            new PlayerDataMessage.PlayerDataData((EntityPlayer) playerEntity),
                            (EntityPlayerMP) event.player);
                }
            }
            NetworkDispatcher.INSTANCE.sendToAll(new PlayerDataMessage.PlayerDataData(event.player));
        }
    }

    public void clearData() {
        map.clear();
    }
}
