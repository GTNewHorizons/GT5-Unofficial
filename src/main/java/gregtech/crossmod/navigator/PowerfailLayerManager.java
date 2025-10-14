package gregtech.crossmod.navigator;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.navigator.api.journeymap.waypoints.JMWaypointManager;
import com.gtnewhorizons.navigator.api.model.SupportedMods;
import com.gtnewhorizons.navigator.api.model.layers.InteractableLayerManager;
import com.gtnewhorizons.navigator.api.model.layers.LayerRenderer;
import com.gtnewhorizons.navigator.api.model.layers.UniversalInteractableRenderer;
import com.gtnewhorizons.navigator.api.model.locations.ILocationProvider;
import com.gtnewhorizons.navigator.api.model.waypoints.WaypointManager;
import com.gtnewhorizons.navigator.api.xaero.waypoints.XaeroWaypointManager;

import gregtech.GTMod;
import gregtech.common.data.GTPowerfailTracker;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public class PowerfailLayerManager extends InteractableLayerManager {

    public static final PowerfailLayerManager INSTANCE = new PowerfailLayerManager();

    private final Long2ObjectLinkedOpenHashMap<PowerfailLocationWrapper> wrappers = new Long2ObjectLinkedOpenHashMap<>();

    private PowerfailLayerManager() {
        super(new PowerfailButtonManager());
        setHasSearchField(true);
    }

    @Override
    protected @Nullable LayerRenderer addLayerRenderer(InteractableLayerManager manager, SupportedMods mod) {
        return new UniversalInteractableRenderer(manager)
            .withRenderStep(location -> new PowerfailRenderStep((PowerfailLocationWrapper) location));
    }

    @Nullable
    @Override
    protected WaypointManager addWaypointManager(InteractableLayerManager manager, SupportedMods mod) {
        return switch (mod) {
            case JourneyMap -> new JMWaypointManager(manager);
            case XaeroWorldMap -> new XaeroWaypointManager(manager, "!");
            default -> null;
        };
    }

    @Override
    public void onUpdatePost(int chunkMinX, int chunkMaxX, int chunkMinZ, int chunkMaxZ) {
        super.onUpdatePost(chunkMinX, chunkMaxX, chunkMinZ, chunkMaxZ);

        ObjectCollection<GTPowerfailTracker.Powerfail> pfs = GTMod.clientProxy().powerfailRenderer.powerfails.values();

        for (GTPowerfailTracker.Powerfail p : pfs) {
            long coord = p.getCoord();

            PowerfailLocationWrapper w = wrappers.get(coord);

            if (w == null) {
                w = new PowerfailLocationWrapper(p);
                wrappers.put(coord, w);
            }

            updateElement(w);
            getVisibleLocations().add(w);
        }

        // Copy the set because java is stupid and doesn't like to allow generic upcasting.
        // We also use a linked collection to prevent what looks like z fighting in the HUD minimap.
        // It isn't actually z fighting, hashed collections just have pseudorandom/arbitrary iteration order.
        Set<ILocationProvider> locs = new LinkedHashSet<>(getVisibleLocations());

        layerRenderer.values()
            .forEach(layer -> layer.refreshVisibleElements(locs));
    }

    @Override
    public void onSearch(@NotNull String searchString) {
        super.onSearch(searchString);

        if (searchString.isEmpty()) {
            for (PowerfailLocationWrapper wrapper : wrappers.values()) {
                wrapper.highlighted = true;
            }
        } else {
            String[] tokens = searchString.toLowerCase()
                .split("\\s+");

            for (PowerfailLocationWrapper wrapper : wrappers.values()) {
                wrapper.highlighted = true;

                for (String token : tokens) {
                    if (!wrapper.mteName.contains(token)) {
                        wrapper.highlighted = false;
                        break;
                    }
                }
            }
        }
    }

    public void removePowerfail(long coord) {
        if (getCurrentDimCache().getOrDefault(coord, null) != null) {
            removeLocation(coord);
        }
        if (wrappers.containsKey(coord)) {
            wrappers.remove(coord);
        }
    }
}
