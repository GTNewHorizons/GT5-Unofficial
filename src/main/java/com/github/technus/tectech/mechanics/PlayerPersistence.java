package com.github.technus.tectech.mechanics;

import com.github.technus.tectech.Util;
import com.github.technus.tectech.loader.network.NetworkDispatcher;
import com.github.technus.tectech.loader.network.PlayerDataMessage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.UUID;

import static java.nio.charset.Charset.forName;

public class PlayerPersistence {
    private final HashMap<UUID, NBTTagCompound> map=new HashMap<>();
    private final String extension;

    public PlayerPersistence(String extension) {
        this.extension = extension;
    }

    public NBTTagCompound getDataOrSetToNewTag(UUID uuid1, UUID uuid2){
        NBTTagCompound tag=map.get(uuid1);
        if(tag!=null){
            return tag;
        }
        tag=map.get(uuid2);
        if(tag!=null){
            return tag;
        }
        tag=Util.getPlayerData(uuid1,uuid2,extension);
        if(tag==null){
            tag=new NBTTagCompound();
        }
        map.put(uuid1,tag);
        map.put(uuid2,tag);
        return tag;
    }

    public NBTTagCompound getDataOrSetToNewTag(EntityPlayer player){
        return getDataOrSetToNewTag(player.getUniqueID(),UUID.nameUUIDFromBytes(player.getCommandSenderName().getBytes(forName("UTF-8"))));
    }

    public void putDataOrSetToNewTag(UUID uuid1, UUID uuid2, NBTTagCompound tagCompound){
        if(tagCompound==null){
            tagCompound=new NBTTagCompound();
        }
        map.put(uuid1,tagCompound);
        map.put(uuid2,tagCompound);
    }

    public void putDataOrSetToNewTag(EntityPlayer player, NBTTagCompound tagCompound){
        putDataOrSetToNewTag(player.getUniqueID(),UUID.nameUUIDFromBytes(player.getCommandSenderName().getBytes(forName("UTF-8"))),tagCompound);
    }

    public void saveData(EntityPlayer player){
        Util.savePlayerFile(player,extension,getDataOrSetToNewTag(player));
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.player instanceof EntityPlayerMP){
            NetworkDispatcher.INSTANCE.sendTo(new PlayerDataMessage.PlayerDataData(event.player),(EntityPlayerMP)event.player);
        }
    }

    public void clearData() {
        map.clear();
    }
}
