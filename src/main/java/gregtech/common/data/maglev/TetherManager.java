package gregtech.common.data.maglev;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

import com.gtnewhorizon.gtnhlib.datastructs.spatialhashgrid.SpatialHashGrid;
import com.gtnewhorizon.gtnhlib.util.DistanceUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;

public class TetherManager {

    /**
     * DimID, list of active pylons
     **/
    private final Int2ObjectArrayMap<SpatialHashGrid<Tether>> ACTIVE_PYLONS = new Int2ObjectArrayMap<>();

    /**
     * Used by pylons to determine if a player is connected
     */
    private final Map<EntityPlayer, Tether> PLAYER_TETHERS = new HashMap<>();

    public void registerPylon(int dimID, Tether tether) {
        SpatialHashGrid<Tether> grid = ACTIVE_PYLONS.get(dimID);
        if (grid == null) {
            grid = new SpatialHashGrid<>(16, (vec, t) -> vec.set(t.sourceX(), t.sourceY(), t.sourceZ()));
            ACTIVE_PYLONS.put(dimID, grid);
        }
        grid.insert(tether);
    }

    public void unregisterPylon(int dimID, Tether tether) {
        if (tether == null) return;
        final SpatialHashGrid<Tether> grid = ACTIVE_PYLONS.get(dimID);
        if (grid == null) return;
        grid.remove(tether);
    }

    public Tether getClosestActivePylon(EntityPlayer player, int radius) {
        final SpatialHashGrid<Tether> grid = ACTIVE_PYLONS.get(player.dimension);
        if (grid == null) {
            return null;
        }
        final int x = (int) player.posX;
        final int y = (int) player.posY;
        final int z = (int) player.posZ;
        final Iterator<Tether> iterator = grid
            .iterNearbyWithMetric(x, y, z, radius, SpatialHashGrid.DistanceFormula.Chebyshev);
        Tether closestTether = null;
        double closestDistance = Double.MAX_VALUE;
        while (iterator.hasNext()) {
            Tether tether = iterator.next();
            if (!tether.active()) continue;
            double distance = DistanceUtil
                .chebyshevDistance(x, y, z, tether.sourceX(), tether.sourceY(), tether.sourceZ());

            if (distance > tether.range()) {
                continue;
            }

            if (distance < closestDistance) {
                closestDistance = distance;
                closestTether = tether;
            }
        }
        return closestTether;
    }

    public void connectPlayer(EntityPlayer player, Tether tether) {
        PLAYER_TETHERS.put(player, tether);
    }

    public void disconnectPlayer(EntityPlayer player) {
        PLAYER_TETHERS.remove(player);
    }

    public boolean hasPlayerConnect(Tether tether) {
        return PLAYER_TETHERS.containsValue(tether);
    }

    public Tether getConnectedPylon(EntityPlayer player) {
        return PLAYER_TETHERS.get(player);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PLAYER_TETHERS.remove(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        PLAYER_TETHERS.remove(event.player);
    }
}
