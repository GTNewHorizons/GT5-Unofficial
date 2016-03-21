package com.detrav.net;

import com.google.common.io.ByteArrayDataInput;

/**
 * Created by wital_000 on 20.03.2016.
 */
public abstract class DetravPacket {

    public abstract int getPacketID() ;

    public abstract byte[] encode() ;

    public abstract Object decode(ByteArrayDataInput aData) ;

    public abstract void process();
}
