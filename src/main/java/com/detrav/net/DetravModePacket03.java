package com.detrav.net;

import com.detrav.DetravScannerMod;
import com.detrav.events.DetravBlockSideRenderEventHandler;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by wital_000 on 18.04.2016.
 */
public class DetravModePacket03 extends DetravPacket {

    EntityPlayer player;
    long mode = 0L;

    public DetravModePacket03()
    {
        player =null;
    }

    public DetravModePacket03(EntityPlayer aPlayer)
    {
        player = aPlayer;
    }

    public DetravModePacket03(long aMode)
    {
        mode = aMode;
    }


    @Override
    public int getPacketID() {
        return 3;
    }

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(1);
        if (player != null)
            tOut.writeLong(player.getEntityData().getLong("detrav.minning.mode"));
        else tOut.writeLong(0L);
        return tOut.toByteArray();
    }

    @Override
    public Object decode(ByteArrayDataInput aData) {
        long aMode = aData.readLong();
        return new DetravModePacket03(aMode);
    }

    @Override
    public void process() {
        DetravBlockSideRenderEventHandler.modeBlockBreak = mode;
    }
}
