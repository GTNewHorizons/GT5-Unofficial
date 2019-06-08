package com.github.technus.tectech.thing.metaTileEntity.multi.base.network;

import eu.usrv.yamcore.network.PacketDispatcher;

import static com.github.technus.tectech.Reference.MODID;

public class PipeActivityPacketDispatcher extends PacketDispatcher {
    public static PipeActivityPacketDispatcher INSTANCE;

    public PipeActivityPacketDispatcher() {
        super(MODID);
        INSTANCE = this;
        registerPackets();
    }

    @Override
    public void registerPackets() {
        registerMessage(PipeActivityMessage.ServerHandler.class, PipeActivityMessage.PipeActivityQuery.class);
        registerMessage(PipeActivityMessage.ClientHandler.class, PipeActivityMessage.PipeActivityData.class);
    }
}
