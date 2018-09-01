package gtPlusPlus.core.util.minecraft.network;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.preloader.DevHelper;

import java.lang.reflect.Method;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.WorldServer;

@SuppressWarnings("unchecked")
public class PacketDispatcher {
	private static final Class playerInstanceClass;
	private static final Method getOrCreateChunkWatcher;
	private static final Method sendToAllPlayersWatchingChunk;

	public static void sendToServer(CustomPacket packet) {
		PacketHandler.INSTANCE.channel.sendToServer(packet.getPacket());
	}

	public static void sendToPlayer(CustomPacket packet, EntityPlayerMP player) {
		PacketHandler.INSTANCE.channel.sendTo(packet.getPacket(), player);
	}

	public static void sendToAll(CustomPacket packet) {
		PacketHandler.INSTANCE.channel.sendToAll(packet.getPacket());
	}

	public static void sendToAllAround(CustomPacket packet, TargetPoint zone) {
		PacketHandler.INSTANCE.channel.sendToAllAround(packet.getPacket(), zone);
	}

	public static void sendToDimension(CustomPacket packet, int dimensionId) {
		PacketHandler.INSTANCE.channel.sendToDimension(packet.getPacket(), dimensionId);
	}

	public static void sendToWatchers(CustomPacket packet, WorldServer world, int worldX, int worldZ) {
		try {
			Object playerInstance = getOrCreateChunkWatcher.invoke(world.getPlayerManager(), worldX >> 4, worldZ >> 4,
					false);
			if (playerInstance != null) {
				sendToAllPlayersWatchingChunk.invoke(playerInstance, packet.getPacket());
			}

		} catch (Exception var5) {
			Logger.ERROR("Reflection Failure in PacketDispatcher.sendToWatchers() {0} {1}" + 20 + var5 +
					new Object[]{getOrCreateChunkWatcher.getName() + sendToAllPlayersWatchingChunk.getName()});
			throw new RuntimeException(var5);
		}
	}

	static {
		try {
			playerInstanceClass = PlayerManager.class.getDeclaredClasses()[0];

			Method a, b;
			
			try {
				a = DevHelper.getInstance().getForgeMethod(PlayerManager.class, "getOrCreateChunkWatcher", int.class, int.class, boolean.class);
			}
			catch (Throwable t) {
				a = ReflectionHelper.findMethod(playerInstanceClass, (Object) null,
						new String[]{"func_72690_a", "getOrCreateChunkWatcher"},
						new Class[]{Integer.TYPE, Integer.TYPE, Boolean.TYPE});
			}
			try {
				b = DevHelper.getInstance().getForgeMethod(PlayerManager.class, "sendToAllPlayersWatchingChunk", Packet.class);
			}
			catch (Throwable t) {
				b = ReflectionHelper.findMethod(playerInstanceClass, (Object) null,
						new String[]{"func_151251_a", "sendToAllPlayersWatchingChunk"},
						new Class[]{Packet.class});
			}	


			getOrCreateChunkWatcher = a;
			sendToAllPlayersWatchingChunk = b;
			getOrCreateChunkWatcher.setAccessible(true);
			sendToAllPlayersWatchingChunk.setAccessible(true);
		} catch (Exception var1) {
			Logger.ERROR("Reflection Failure in PacketDispatcher initalization {0} {1}" + var1);
			throw new RuntimeException(var1);
		}
	}
}