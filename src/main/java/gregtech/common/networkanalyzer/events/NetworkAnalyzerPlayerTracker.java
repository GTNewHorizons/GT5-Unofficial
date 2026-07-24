package gregtech.common.networkanalyzer.events;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

import appeng.api.util.DimensionalCoord;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class NetworkAnalyzerPlayerTracker {

    private static final long UPDATE_INTERVAL_TICKS = 30L;

    private static class Entry {

        public DimensionalCoord coord;
        public long last;

        public Entry(DimensionalCoord coord, long last) {
            this.coord = coord;
            this.last = last;
        }
    }

    private static Map<EntityPlayer, Entry> map = new HashMap<>();

    public static void init() {
        FMLCommonHandler.instance()
            .bus()
            .register(new NetworkAnalyzerPlayerTracker());
    }

    public static void clear() {
        map.clear();
    }

    public static boolean needToUpdate(EntityPlayer player, DimensionalCoord coord) {
        final long now = player.worldObj.getTotalWorldTime();

        if (map.containsKey(player)) {
            final Entry last = map.get(player);

            if (Math.abs(now - last.last) >= UPDATE_INTERVAL_TICKS || !last.coord.isEqual(coord)) {
                map.put(player, new Entry(coord, now));
                return true;
            } else {
                return false;
            }

        } else {
            map.put(player, new Entry(coord, now));
            return true;
        }

    }

    public static void reset(EntityPlayer player) {
        map.remove(player);
    }

    @SubscribeEvent
    public void handlePlayerLogout(PlayerLoggedOutEvent ev) {
        reset(ev.player);
    }

    @SubscribeEvent
    public void handlePlayerChangedDimension(PlayerChangedDimensionEvent ev) {
        reset(ev.player);
    }

    @SubscribeEvent
    public void handlePlayerRespawn(PlayerRespawnEvent ev) {
        reset(ev.player);
    }

}
