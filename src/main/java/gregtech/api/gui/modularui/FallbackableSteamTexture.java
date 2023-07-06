package gregtech.api.gui.modularui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.network.NetworkUtils;

import gregtech.api.enums.SteamVariant;

public class FallbackableSteamTexture {

    private final SteamTexture candidate;
    private final Object fallback;
    private final Map<SteamVariant, Boolean> useFallbackMap = new HashMap<>();

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

    public UITexture get(SteamVariant steamVariant) {
        verifyCandidate(steamVariant);
        if (useFallbackMap.get(steamVariant)) {
            return castFallback(steamVariant);
        } else {
            return candidate.get(steamVariant);
        }
    }

    private void verifyCandidate(SteamVariant steamVariant) {
        if (useFallbackMap.get(steamVariant) == null) {
            boolean useFallback;
            if (NetworkUtils.isDedicatedClient()) {
                if (candidate == null) {
                    useFallback = true;
                } else {
                    try {
                        Minecraft.getMinecraft()
                            .getResourceManager()
                            .getResource(candidate.get(steamVariant).location);
                        useFallback = false;
                    } catch (IOException e) {
                        useFallback = true;
                    }
                }
            } else {
                useFallback = true;
            }
            useFallbackMap.put(steamVariant, useFallback);
        }
    }

    private UITexture castFallback(SteamVariant steamVariant) {
        if (fallback instanceof SteamTexture) {
            return ((SteamTexture) fallback).get(steamVariant);
        } else if (fallback instanceof FallbackableSteamTexture) {
            return ((FallbackableSteamTexture) fallback).get(steamVariant);
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
