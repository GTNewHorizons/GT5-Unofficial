package gregtech.common.data.maglev;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.tileentities.machines.basic.MTEMagLevPylon;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraftforge.event.world.WorldEvent;
import scala.reflect.internal.util.WeakHashSet;

@EventBusSubscriber
public class TetherManager {
    /**
     * DimID, list of active pylons
     * <br>
     * Initialized with empty set on world load and removed on unload
     **/
    public static final Int2ObjectOpenHashMap<WeakHashSet<MTEMagLevPylon>> ACTIVE_PYLONS = new Int2ObjectOpenHashMap<>();


    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        ACTIVE_PYLONS.computeIfAbsent(event.world.provider.dimensionId, v -> new WeakHashSet<>());
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        ACTIVE_PYLONS.remove(event.world.provider.dimensionId);
    }
}
