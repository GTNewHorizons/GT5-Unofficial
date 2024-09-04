package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

@SuppressWarnings("deprecation")
public interface IGT_NetworkHandler {

    void sendToPlayer(GTPacket aPacket, EntityPlayerMP aPlayer);

    void sendToAllAround(GTPacket aPacket, TargetPoint aPosition);

    default void sendToAll(GTPacket aPacket) {
        throw new UnsupportedOperationException("sendToAll not implemented");
    }

    void sendToServer(GTPacket aPacket);

    void sendPacketToAllPlayersInRange(World aWorld, GTPacket aPacket, int aX, int aZ);
}
