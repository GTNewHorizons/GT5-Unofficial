package com.github.technus.tectech.thing.metaTileEntity.multi.base.network;

import eu.usrv.yamcore.network.PacketDispatcher;

import static com.github.technus.tectech.Reference.MODID;

public class RotationPacketDispatcher extends PacketDispatcher {
    public static RotationPacketDispatcher INSTANCE;

    public RotationPacketDispatcher() {
        super(MODID);
        INSTANCE = this;
        registerPackets();
    }

    @Override
    public void registerPackets() {
        registerMessage(RotationMessage.ServerHandler.class, RotationMessage.RotationQuery.class);
        registerMessage(RotationMessage.ClientHandler.class, RotationMessage.RotationData.class);
    }
}
