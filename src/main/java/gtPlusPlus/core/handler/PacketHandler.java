package gtPlusPlus.core.handler;

import static gregtech.api.enums.Mods.GTPlusPlus;

import gtPlusPlus.core.network.packet.PacketTurbineHatchUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import gtPlusPlus.core.network.handler.AbstractClientMessageHandler;
import gtPlusPlus.core.network.packet.PacketVolumetricFlaskGui;
import gtPlusPlus.core.network.packet.PacketVolumetricFlaskGui2;

public class PacketHandler {

    private static byte packetId = 0;

    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(GTPlusPlus.ID);

    public static void init() {
        registerMessage(PacketVolumetricFlaskGui.class, PacketVolumetricFlaskGui.class);
        registerMessage(PacketVolumetricFlaskGui2.class, PacketVolumetricFlaskGui2.class);
        registerMessage(PacketTurbineHatchUpdate.class, PacketTurbineHatchUpdate.class);
    }

    /**
     * Registers a message and message handler
     */
    private static void registerMessage(Class handlerClass, Class messageClass) {
        Side side = AbstractClientMessageHandler.class.isAssignableFrom(handlerClass) ? Side.CLIENT : Side.SERVER;
        registerMessage(handlerClass, messageClass, side);
    }

    private static void registerMessage(Class handlerClass, Class messageClass, Side side) {
        INSTANCE.registerMessage(handlerClass, messageClass, packetId++, side);
    }

    /**
     * Send this message to the specified player. See {@link SimpleNetworkWrapper#sendTo(IMessage, EntityPlayerMP)}
     */
    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

    /**
     * Send this message to everyone within a certain range of a point. See
     * {@link SimpleNetworkWrapper#sendToAllAround(IMessage, NetworkRegistry.TargetPoint)}
     */
    public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        INSTANCE.sendToAllAround(message, point);
    }

    /**
     * Sends a message to everyone within a certain range of the coordinates in the same dimension.
     */
    public static void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
        sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }

    /**
     * Sends a message to everyone within a certain range of the player provided.
     */
    public static void sendToAllAround(IMessage message, EntityPlayer player, double range) {
        sendToAllAround(message, player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, range);
    }

    /**
     * Send this message to everyone within the supplied dimension. See
     * {@link SimpleNetworkWrapper#sendToDimension(IMessage, int)}
     */
    public static void sendToDimension(IMessage message, int dimensionId) {
        INSTANCE.sendToDimension(message, dimensionId);
    }

    /**
     * Send this message to the server. See {@link SimpleNetworkWrapper#sendToServer(IMessage)}
     */
    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }
}
