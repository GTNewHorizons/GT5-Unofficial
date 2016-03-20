package com.detrav.utils;

import com.google.common.io.ByteArrayDataInput;
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

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public Object decode(ByteArrayDataInput aData) {
        return new DetravProPickPacket01();
    }

    @Override
    public void process() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("NetworkTested"));
    }
}
