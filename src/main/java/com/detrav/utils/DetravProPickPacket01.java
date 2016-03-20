package com.detrav.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by wital_000 on 20.03.2016.
 */
public class DetravProPickPacket01 extends DetravPacket {
    @Override
    public int getPacketID() {
        return 0;
    }

    public int level = -1;

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(1);
        tOut.writeInt(level);
        return tOut.toByteArray();
    }

    @Override
    public Object decode(ByteArrayDataInput aData) {
        DetravProPickPacket01 packet = new DetravProPickPacket01();
        packet.level = aData.readInt();
        return packet;
    }

    @Override
    public void process() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("NetworkTested: " + Integer.toString(level)));
    }
}
