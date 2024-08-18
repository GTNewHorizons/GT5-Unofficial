package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

@SuppressWarnings("deprecation")
public interface IGT_NetworkHandler {

    void sendToPlayer(GT_Packet aPacket, EntityPlayerMP aPlayer);

    void sendToAllAround(GT_Packet aPacket, TargetPoint aPosition);

    default void sendToAll(GT_Packet aPacket) {
        throw new UnsupportedOperationException("sendToAll not implemented");
    }

    void sendToServer(GT_Packet aPacket);

    void sendPacketToAllPlayersInRange(World aWorld, GT_Packet aPacket, int aX, int aZ);
}
