package gregtech.crossmod.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.navigator.api.journeymap.waypoints.JMWaypointManager;
import com.gtnewhorizons.navigator.api.model.SupportedMods;
import com.gtnewhorizons.navigator.api.model.layers.InteractableLayerManager;
import com.gtnewhorizons.navigator.api.model.layers.LayerRenderer;
import com.gtnewhorizons.navigator.api.model.layers.UniversalInteractableRenderer;
import com.gtnewhorizons.navigator.api.model.locations.ILocationProvider;
import com.gtnewhorizons.navigator.api.model.locations.IWaypointAndLocationProvider;
import com.gtnewhorizons.navigator.api.model.markers.MapMarker;
import com.gtnewhorizons.navigator.api.model.waypoints.WaypointManager;
import com.gtnewhorizons.navigator.api.xaero.waypoints.XaeroWaypointManager;

import gregtech.GTMod;
import gregtech.common.data.GTPowerfailTracker;

public class PowerfailLayerManager extends InteractableLayerManager {

    public static final PowerfailLayerManager INSTANCE = new PowerfailLayerManager();

    private String[] searchTokens = {};

    private PowerfailLayerManager() {
        super(new PowerfailButtonManager());
        setHasSearchField(true);
    }

    @Override
    protected @Nullable LayerRenderer addLayerRenderer(InteractableLayerManager manager, SupportedMods mod) {
        return new UniversalInteractableRenderer(manager)
            .withRenderStep(location -> new PowerfailRenderStep((PowerfailLocationWrapper) location))
            .withMapMarker(location -> createMapMarker((PowerfailLocationWrapper) location));
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
    protected Collection<? extends IWaypointAndLocationProvider> generateVisibleLocations(int minBlockX, int minBlockZ,
        int maxBlockX, int maxBlockZ, int dimension) {
        var powerfails = GTMod.clientProxy().powerfailRenderer.powerfails.get(dimension);
        if (powerfails == null || powerfails.isEmpty()) return Collections.emptyList();

        ArrayList<PowerfailLocationWrapper> visible = new ArrayList<>();
        for (GTPowerfailTracker.Powerfail powerfail : powerfails.values()) {
            if (powerfail.x >= minBlockX && powerfail.x <= maxBlockX
                && powerfail.z >= minBlockZ
                && powerfail.z <= maxBlockZ) {
                visible.add(new PowerfailLocationWrapper(powerfail));
            }
        }
        return visible;
    }

    @Override
    public void updateElement(IWaypointAndLocationProvider location) {
        if (!(location instanceof PowerfailLocationWrapper wrapper)) return;
        var powerfails = GTMod.clientProxy().powerfailRenderer.powerfails.get(wrapper.getDimensionId());
        if (powerfails != null) {
            GTPowerfailTracker.Powerfail latest = powerfails.get(wrapper.toLong());
            if (latest != null) wrapper.update(latest);
        }
        wrapper.highlighted = matchesSearch(wrapper);
    }

    @Override
    public void onSearch(@NotNull String searchString) {
        searchTokens = searchString.isEmpty() ? new String[0]
            : searchString.toLowerCase(Locale.ROOT)
                .split("\\s+");
        for (ILocationProvider location : getCachedLocations()) {
            if (location instanceof PowerfailLocationWrapper wrapper) wrapper.highlighted = matchesSearch(wrapper);
        }
    }

    public void removePowerfail(long coord) {
        invalidateLocation(coord);
    }

    private boolean matchesSearch(PowerfailLocationWrapper wrapper) {
        for (String token : searchTokens) {
            if (!wrapper.mteName.contains(token)) return false;
        }
        return true;
    }

    private static @Nullable MapMarker createMapMarker(PowerfailLocationWrapper location) {
        if (!location.highlighted) return null;
        if (!(GTMod.clientProxy().powerfailRenderer.powerfailIcon instanceof TextureAtlasSprite icon)) return null;
        return new MapMarker(TextureMap.locationBlocksTexture, icon).setDisplaySize(32, 32)
            .setDisplayZoomScale(1, 2, 3, 5)
            .setLabelSupplier(
                () -> location.powerfail.toSummary()
                    .toString())
            .setLabelZoomScale(1, 3, 3, 5)
            .setLabelMinZoom(2)
            .setLabelOffsetY(24)
            .setLabelOnMinimap(false);
    }
}
