package com.detrav.net;

import com.detrav.gui.DetravPortableChargerGui;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * Created by wital_000 on 07.04.2016.
 */
public class DetravPortableChargerPacker02 extends DetravPacket {
    public long charge = 0;
    @Override
    public int getPacketID() {
        return 1;
    }

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(1);
        tOut.writeLong(charge);
        return tOut.toByteArray();
    }

    @Override
    public Object decode(ByteArrayDataInput aData) {
        DetravPortableChargerPacker02 packet = new DetravPortableChargerPacker02();
        packet.charge = aData.readLong();
        return packet;
    }

    @Override
    public void process() {
        DetravPortableChargerGui.charge = charge;
    }
}
