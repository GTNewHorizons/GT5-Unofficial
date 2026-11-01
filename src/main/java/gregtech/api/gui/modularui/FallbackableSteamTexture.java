package gregtech.api.gui.modularui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.enums.TieredVariant;

public class FallbackableSteamTexture {

    private final SteamTexture candidate;
    private final Object fallback;
    private final Map<TieredVariant, Boolean> useFallbackMap = new HashMap<>();

    private static final List<FallbackableSteamTexture> ALL_INSTANCES = new ArrayList<>();

    public FallbackableSteamTexture(SteamTexture candidate, SteamTexture fallback) {
        this(candidate, (Object) fallback);
    }

    public FallbackableSteamTexture(SteamTexture candidate, FallbackableSteamTexture fallback) {
        this(candidate, (Object) fallback);
    }

    public FallbackableSteamTexture(SteamTexture fallback) {
        this(null, fallback);
    }

    private FallbackableSteamTexture(SteamTexture candidate, Object fallback) {
        this.candidate = candidate;
        this.fallback = fallback;
        ALL_INSTANCES.add(this);
    }

    public UITexture get(TieredVariant tieredVariant) {
        verifyCandidate(tieredVariant);
        if (useFallbackMap.get(tieredVariant)) {
            return castFallback(tieredVariant);
        } else {
            return candidate.get(tieredVariant);
        }
    }

    private void verifyCandidate(TieredVariant tieredVariant) {
        if (useFallbackMap.get(tieredVariant) == null) {
            boolean useFallback;
            if (NetworkUtils.isDedicatedClient()) {
                if (candidate == null) {
                    useFallback = true;
                } else {
                    try {
                        Minecraft.getMinecraft()
                            .getResourceManager()
                            .getResource(candidate.get(tieredVariant).location);
                        useFallback = false;
                    } catch (IOException e) {
                        useFallback = true;
                    }
                }
            } else {
                useFallback = true;
            }
            useFallbackMap.put(tieredVariant, useFallback);
        }
    }

    private UITexture castFallback(TieredVariant tieredVariant) {
        if (fallback instanceof SteamTexture) {
            return ((SteamTexture) fallback).get(tieredVariant);
        } else if (fallback instanceof FallbackableSteamTexture) {
            return ((FallbackableSteamTexture) fallback).get(tieredVariant);
        } else {
            throw new RuntimeException("Unexpected type found for fallback: " + fallback.getClass());
        }
    }

    public static void reload() {
        for (FallbackableSteamTexture t : ALL_INSTANCES) {
            t.useFallbackMap.clear();
        }
    }
}
