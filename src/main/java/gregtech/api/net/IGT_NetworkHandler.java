package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public interface IGT_NetworkHandler {

    void sendToPlayer(GTPacket aPacket, EntityPlayerMP aPlayer);

    void sendToAllAround(GTPacket aPacket, TargetPoint aPosition);

    void sendToAll(GTPacket aPacket);

    void sendToServer(GTPacket aPacket);

    void sendPacketToAllPlayersInRange(World aWorld, GTPacket aPacket, int aX, int aZ);

    void sendToWorld(World world, GTPacket packet);
}
