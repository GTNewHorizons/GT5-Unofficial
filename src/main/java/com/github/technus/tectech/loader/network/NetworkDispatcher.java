package com.github.technus.tectech.loader.network;

import static com.github.technus.tectech.Reference.MODID;

public class NetworkDispatcher extends eu.usrv.yamcore.network.PacketDispatcher {
    public static NetworkDispatcher INSTANCE;

    public NetworkDispatcher() {
        super(MODID);
        INSTANCE = this;
        registerPackets();
    }

    @Override
    public void registerPackets() {
        registerMessage(PipeActivityMessage.ServerHandler.class, PipeActivityMessage.PipeActivityQuery.class);
        registerMessage(PipeActivityMessage.ClientHandler.class, PipeActivityMessage.PipeActivityData.class);
        registerMessage(RotationMessage.ServerHandler.class, RotationMessage.RotationQuery.class);
        registerMessage(RotationMessage.ClientHandler.class, RotationMessage.RotationData.class);
    }
}
