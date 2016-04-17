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
    boolean back;

    public DetravModeSwitchPacket02() {
        player = null;
    }

    public DetravModeSwitchPacket02(EntityPlayer aPlayer, boolean aBack) {
        player = aPlayer;
        back = aBack;
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
        tOut.writeBoolean(back);
        return tOut.toByteArray();
    }

    @Override
    public Object decode(ByteArrayDataInput aData) {
        int id = aData.readInt();
        if (id == Integer.MIN_VALUE)
            return new DetravModeSwitchPacket02();
        boolean aBack = aData.readBoolean();
        //ArrayList<EntityPlayerMP> allp = new ArrayList<EntityPlayerMP>();
        ListIterator itl;
        EntityPlayerMP temp = null;
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++) {
            itl = MinecraftServer.getServer().worldServers[i].playerEntities.listIterator();
            while (itl.hasNext()) {
                temp = (EntityPlayerMP) itl.next();
                if (temp.getEntityId() == id)
                    return new DetravModeSwitchPacket02(temp, aBack);
                temp = null;
            }
        }
        return new DetravModeSwitchPacket02(temp, aBack);
    }

    @Override
    public void process() {
        if (player != null) {
            NBTTagCompound aData = player.getEntityData();
            //aData.hasNoTags()
            long minningMode = aData.getLong("detrav.minning.mode");
            if (back) {
                if (--minningMode < 0)
                    minningMode = 4;
            } else {
                if (++minningMode > 4)
                    minningMode = 0;
            }
            switch ((int) minningMode) {
                case 0:
                    player.addChatMessage(new ChatComponentText("Mining mode 1x1, just one block"));
                    break;
                case 1:
                    player.addChatMessage(new ChatComponentText("Mining mode 3x3 by side, nine blocks"));
                    break;
                case 2:
                    player.addChatMessage(new ChatComponentText("Mining mode 3x3 horizontal, nine blocks"));
                    break;
                case 3:
                    player.addChatMessage(new ChatComponentText("Mining mode 3x3 vertical, nine blocks"));
                    break;
                case 4:
                    player.addChatMessage(new ChatComponentText("Mining mode 3x3x3 vein, twenty seven blocks by type"));
                    break;
            }
            aData.setLong("detrav.minning.mode", minningMode);
        }
    }
}