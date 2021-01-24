package com.detrav.net;

/**
 * Created by wital_000 on 20.03.2016.
 */
public abstract class DetravPacket {

    public abstract int getPacketID() ;

    public abstract byte[] encode() ;

    public abstract void process();
}
