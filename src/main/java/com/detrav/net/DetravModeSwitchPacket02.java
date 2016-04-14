package com.detrav.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by wital_000 on 14.04.2016.
 */
public class DetravModeSwitchPacket02 extends DetravPacket {

    EntityPlayer player;

    public DetravModeSwitchPacket02()
    {
        player = null;
    }

    public DetravModeSwitchPacket02(EntityPlayer aPlayer)
    {
        player = aPlayer;
    }

    @Override
    public int getPacketID() {
        return 2;
    }

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(1);
        if (player != null)
            tOut.writeInt(player.getEntityId());
        else tOut.writeInt(Integer.MIN_VALUE);
        return tOut.toByteArray();
    }

    @Override
    public Object decode(ByteArrayDataInput aData) {
        int id = aData.readInt();
        if (id == Integer.MIN_VALUE)
            return new DetravModeSwitchPacket02();
        //ArrayList<EntityPlayerMP> allp = new ArrayList<EntityPlayerMP>();
        ListIterator itl;
        EntityPlayerMP temp = null;
        for(int i = 0; i<MinecraftServer.getServer().worldServers.length; i++) {
            itl = MinecraftServer.getServer().worldServers[i].playerEntities.listIterator();
            while(itl.hasNext()) {
                temp = (EntityPlayerMP) itl.next();
                if(temp.getEntityId() == id)
                    return new DetravModeSwitchPacket02(temp);
                temp = null;
            }
        }
        return new DetravModeSwitchPacket02(temp);
    }

    @Override
    public void process() {
        if(player!=null) {
            NBTTagCompound aData = player.getEntityData();
            //aData.hasNoTags()
            long minningMode = aData.getLong("detrav.minning.mode");
            if(minningMode == 0) {
                aData.setLong("detrav.minning.mode", 1);
                player.addChatMessage(new ChatComponentText("Mining mode 3x3 block"));
            }
            else {
                aData.setLong("detrav.minning.mode", 0);
                player.addChatMessage(new ChatComponentText("Mining mode 1x1 block"));
            }
        }
    }
}
