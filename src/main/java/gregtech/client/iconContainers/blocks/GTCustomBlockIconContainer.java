package gregtech.client.iconContainers.blocks;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTLog;
import gregtech.api.util.client.ResourceUtils;
import gregtech.common.config.Gregtech;

public class GTCustomBlockIconContainer extends AbstractBlockIconContainer implements Runnable {

    protected final ResourceLocation iconResource, overlayResource;
    protected final String mIconName, mOverlayName;
    protected IIcon mIcon, mOverlay = null;

    // TODO: Change to package-private once API no longer extends this implementation
    protected GTCustomBlockIconContainer(@NotNull String aIconName) {
        mIconName = aIconName.contains(":") ? aIconName : GregTech.getResourcePath(aIconName);
        iconResource = ResourceUtils.getCompleteBlockTextureResourceLocation(mIconName);

        mOverlayName = mIconName + "_OVERLAY";
        overlayResource = ResourceUtils.getCompleteBlockTextureResourceLocation(mOverlayName);
        GregTechAPI.sGTBlockIconload.add(this);
        if (Gregtech.debug.logRegisterIcons) logRegisterIcons();
    }

    protected void logRegisterIcons() {
        GTLog.ico.println("R " + iconResource);
        GTLog.ico.println("O " + overlayResource);
    }

    // 2026-02-03: Counted 1771 unique Block CustomIcons, so 2.5K will avoid resize until 1920 entries
    private static final Map<String, GTCustomBlockIconContainer> INSTANCES = new ConcurrentHashMap<>(2560);

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTCustomBlockIconContainer::new);
    }

    @Override
    public void run() {
        mIcon = GregTechAPI.sBlockIcons.registerIcon(mIconName);
        // This makes the block _OVERLAY icon totally optional
        if (ResourceUtils.resourceExists(overlayResource)) {
            mOverlay = GregTechAPI.sBlockIcons.registerIcon(mOverlayName);
        }
    }

    @Override
    public IIcon getIcon() {
        return mIcon;
    }

    @Override
    public IIcon getOverlayIcon() {
        return mOverlay;
    }
}
