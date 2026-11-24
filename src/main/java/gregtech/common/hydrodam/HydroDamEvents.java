package gregtech.common.hydrodam;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.ChunkEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

/// Forwards world ticks, chunk loads and players entering dimensions to each dimension's {@link DamVolumes}.
@EventBusSubscriber
public final class HydroDamEvents {

    private HydroDamEvents() {}

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.world.isRemote) return;

        DamVolumes.get(event.world)
            .tick();
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (event.world.isRemote) return;

        DamVolumes.get(event.world)
            .queueChunk(event.getChunk().xPosition, event.getChunk().zPosition);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sendLevels(event.player);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sendLevels(event.player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        sendLevels(event.player);
    }

    @SubscribeEvent
    public static void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        ClientDamLevels.clearAll();
    }

    private static void sendLevels(EntityPlayer player) {
        if (player instanceof EntityPlayerMP playerMP) {
            DamVolumes.get(playerMP.worldObj)
                .sendLevelsTo(playerMP);
        }
    }
}
