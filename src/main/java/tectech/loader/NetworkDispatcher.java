package tectech.loader;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import tectech.Reference;
import tectech.mechanics.pipe.BatchedPipeActivityMessage;
import tectech.mechanics.pipe.PipeActivity;
import tectech.mechanics.pipe.PipeActivityMessage;
import tectech.mechanics.spark.RendererMessage;

public class NetworkDispatcher {

    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.MODID);

    public static void registerPackets() {
        INSTANCE.registerMessage(
            PipeActivityMessage.ServerHandler.class,
            PipeActivityMessage.PipeActivityQuery.class,
            0,
            Side.SERVER);
        INSTANCE.registerMessage(
            PipeActivityMessage.ClientHandler.class,
            PipeActivityMessage.PipeActivityData.class,
            1,
            Side.CLIENT);

        INSTANCE
            .registerMessage(RendererMessage.ClientHandler.class, RendererMessage.RendererData.class, 2, Side.CLIENT);

        PipeActivity.init();
        INSTANCE.registerMessage(PipeActivity.Handler.class, BatchedPipeActivityMessage.class, 3, Side.CLIENT);
    }
}
