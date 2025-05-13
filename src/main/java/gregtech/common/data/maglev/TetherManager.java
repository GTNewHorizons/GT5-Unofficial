package gregtech.common.data.maglev;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import gregtech.common.tileentities.machines.basic.MTEMagLevPylon;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;
import scala.reflect.internal.util.WeakHashSet;

import java.util.WeakHashMap;

@EventBusSubscriber
public class TetherManager {
    /**
     * DimID, list of active pylons
     * <br>
     * Initialized with empty set on world load and removed on unload
     **/
    public static final Int2ObjectOpenHashMap<WeakHashSet<MTEMagLevPylon>> ACTIVE_PYLONS = new Int2ObjectOpenHashMap<>();
    public static final WeakHashMap<EntityPlayer, Tether> PLAYER_TETHERS = new WeakHashMap<>();


    // client side
    @SubscribeEvent
    public static void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent ignored){
        PLAYER_TETHERS.clear();
        ACTIVE_PYLONS.clear();
    }

    // server side
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event){
        // just in case
        if (event.player instanceof FakePlayer) return;
        PLAYER_TETHERS.remove(event.player);
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        ACTIVE_PYLONS.computeIfAbsent(event.world.provider.dimensionId, v -> new WeakHashSet<>());
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        ACTIVE_PYLONS.remove(event.world.provider.dimensionId);
    }
}
