package com.github.technus.tectech.mechanics;

import com.github.technus.tectech.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.UUID;

public class PlayerPersistence {
    private final HashMap<UUID, NBTTagCompound> map=new HashMap<>();
    private final String extension;

    public PlayerPersistence(String extension) {
        this.extension = extension;
    }

    public final NBTTagCompound getData(EntityPlayer player){
        NBTTagCompound tag=map.get(player.getUniqueID());
        if(tag!=null){
            return tag;
        }
        tag=map.get(UUID.fromString(player.getCommandSenderName()));
        if(tag!=null){
            return tag;
        }
        tag=Util.getPlayerData(player,extension);
        if(tag!=null){
            map.put(player.getUniqueID(),tag);
            map.put(UUID.fromString(player.getCommandSenderName()),tag);
        }
        return tag;
    }

    public final void saveData(EntityPlayer player){
        NBTTagCompound tag=map.get(player.getUniqueID());
        if(tag==null){
            tag=map.get(UUID.fromString(player.getCommandSenderName()));
        }
        Util.savePlayerFile(player,extension,tag);
    }
}
