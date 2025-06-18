package gregtech.common.data.maglev;

import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;

import com.gtnewhorizon.gtnhlib.datastructs.spatialhashgrid.SpatialHashGrid;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

@SuppressWarnings("unused")
@EventBusSubscriber
public class TetherManager {

    /**
     * DimID, list of active pylons
     * <br>
     * Initialized with empty set on world load and removed on unload
     **/
    public static final Int2ObjectOpenHashMap<SpatialHashGrid<Tether>> ACTIVE_PYLONS = new Int2ObjectOpenHashMap<>();

    /**
     * Used by pylons to determine if a player is connected
     */
    public static final WeakHashMap<EntityPlayer, Tether> PLAYER_TETHERS = new WeakHashMap<>();

    // client clean up
    @SubscribeEvent
    public static void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent ignored) {
        PLAYER_TETHERS.clear();
        ACTIVE_PYLONS.clear();
    }

    // server side
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        // just in case
        if (event.player instanceof FakePlayer) return;
        PLAYER_TETHERS.remove(event.player);
    }

    // server side
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // just in case
        if (event.player instanceof FakePlayer) return;
        PLAYER_TETHERS.putIfAbsent(event.player, null);
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        ACTIVE_PYLONS.computeIfAbsent(
            event.world.provider.dimensionId,
            v -> new SpatialHashGrid<>(
                16,
                (vec, tether) -> vec.set(tether.sourceX(), tether.sourceY(), tether.sourceZ())));
    }

    public static void onPlayerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player instanceof FakePlayer) return;
        PLAYER_TETHERS.replace(event.player, null);
    }
}
