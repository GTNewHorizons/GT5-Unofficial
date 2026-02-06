package gregtech.client.iconContainers.blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTLog;
import gregtech.api.util.client.ResourceUtils;

public class GTCustomOptionalBlockIconContainer extends GTCustomBlockIconContainer {

    GTCustomOptionalBlockIconContainer(@NotNull String aIconName) {
        super(aIconName);
    }

    @Override
    protected void logRegisterIcons() {
        GTLog.ico.println("O " + iconResource);
        GTLog.ico.println("O " + overlayResource);
    }

    // 2026-02-03: Counted 3723 unique Block Custom Optional Icons, so 5K will avoid resize until 3840 entries
    private static final Map<String, GTCustomOptionalBlockIconContainer> INSTANCES = new ConcurrentHashMap<>(5120);

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTCustomOptionalBlockIconContainer::new);
    }

    @Override
    public void run() {
        mIcon = ResourceUtils.resourceExists(iconResource) ? GregTechAPI.sBlockIcons.registerIcon(mIconName)
            : Textures.InvisibleIcon.INVISIBLE_ICON;
        // This makes the block _OVERLAY icon totally optional
        if (ResourceUtils.resourceExists(overlayResource)) {
            mOverlay = GregTechAPI.sBlockIcons.registerIcon(mOverlayName);
        }
    }
}
