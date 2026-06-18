package gregtech.client.iconContainers.blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.IIconContainer;

public class GTCustomAlphaBlockIconContainer extends GTCustomOptionalBlockIconContainer {

    GTCustomAlphaBlockIconContainer(@NotNull String aIconName) {
        super(aIconName);
    }

    // 2026-02-03: Counted 160 unique Block Custom Alpha Icons, so 255 will avoid resize until 162 entries
    private static final Map<String, GTCustomAlphaBlockIconContainer> INSTANCES = new ConcurrentHashMap<>(256);

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTCustomAlphaBlockIconContainer::new);
    }

    @Override
    public int getRenderIconPass() {
        return 1;
    }
}
