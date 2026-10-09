package tectech.loader;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import tectech.Reference;
import tectech.mechanics.spark.RendererMessage;

public class NetworkDispatcher {

    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.MODID);

    public static void registerPackets() {
        INSTANCE
            .registerMessage(RendererMessage.ClientHandler.class, RendererMessage.RendererData.class, 2, Side.CLIENT);
    }
}
