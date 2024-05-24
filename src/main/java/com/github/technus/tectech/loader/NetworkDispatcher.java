package com.github.technus.tectech.loader;

import static com.github.technus.tectech.Reference.MODID;

import com.github.technus.tectech.mechanics.pipe.PipeActivityMessage;
import com.github.technus.tectech.mechanics.spark.RendererMessage;

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

        registerMessage(RendererMessage.ClientHandler.class, RendererMessage.RendererData.class);
    }
}
