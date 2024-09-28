package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public interface IGT_NetworkHandler {

    void sendToPlayer(GTPacketNew aPacket, EntityPlayerMP aPlayer);

    void sendToAllAround(GTPacketNew aPacket, TargetPoint aPosition);

    void sendToAll(GTPacketNew aPacket);

    void sendToServer(GTPacketNew aPacket);

    void sendPacketToAllPlayersInRange(World aWorld, GTPacketNew aPacket, int aX, int aZ);
}
