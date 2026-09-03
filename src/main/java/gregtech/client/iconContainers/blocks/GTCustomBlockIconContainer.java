package gregtech.client.iconContainers.blocks;

import static gregtech.GTLoggers.GT_ICON_LOGGER;
import static gregtech.api.enums.Mods.GregTech;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.client.ResourceUtils;

public class GTCustomBlockIconContainer extends AbstractBlockIconContainer implements Runnable {

    protected final ResourceLocation iconResource, overlayResource;
    protected final String mIconName, mOverlayName;
    protected IIcon mIcon, mOverlay = null;

    GTCustomBlockIconContainer(@NotNull String domain, @NotNull String aIconName) {
        mIconName = ResourceUtils.getIconRegisterName(domain, aIconName);
        iconResource = ResourceUtils.getCompleteBlockTextureResourceLocation(domain, aIconName);

        mOverlayName = ResourceUtils.getIconRegisterName(domain, aIconName + Textures.OverlaySuffix);
        overlayResource = ResourceUtils
            .getCompleteBlockTextureResourceLocation(domain, aIconName + Textures.OverlaySuffix);
        GregTechAPI.sGTBlockIconload.add(this);
        logRegisterIcons();
    }

    protected void logRegisterIcons() {
        GT_ICON_LOGGER.info("R {}", iconResource);
        GT_ICON_LOGGER.info("O {}", overlayResource);
    }

    // 2026-13-05: Counted 36 unique Block TextureSetIcons, so 52 will avoid resize until 50 entries
    private static Map<String, GTCustomBlockIconContainer> INSTANCES = new HashMap<>(52);

    public static @NotNull IIconContainer create(@NotNull String domain, @NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, key -> new GTCustomBlockIconContainer(domain, key));
    }

    public static void cleanup() {
        INSTANCES = new HashMap<>();
    }

    @Override
    public void run() {
        mIcon = GregTechAPI.sBlockIcons.registerIcon(mIconName);
        // This makes the block _OVERLAY icon totally optional
        if (ResourceUtils.resourceExists(overlayResource)) {
            mOverlay = GregTechAPI.sBlockIcons.registerIcon(mOverlayName);
        } else {
            mOverlay = null;
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
