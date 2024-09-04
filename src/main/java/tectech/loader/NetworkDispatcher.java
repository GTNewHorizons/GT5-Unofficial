package tectech.loader;

import static tectech.Reference.MODID;

import tectech.mechanics.pipe.PipeActivityMessage;
import tectech.mechanics.spark.RendererMessage;

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
