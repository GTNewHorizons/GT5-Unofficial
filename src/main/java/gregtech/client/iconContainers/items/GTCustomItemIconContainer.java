package gregtech.client.iconContainers.items;

import static gregtech.GTLoggers.GT_ICON_LOGGER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.client.ResourceUtils;

public class GTCustomItemIconContainer extends AbstractItemIconContainer implements Runnable {

    protected IIcon mIcon, mOverlay;
    protected String mIconName, mOverlayName;
    protected ResourceLocation iconResource, overlayResource;

    GTCustomItemIconContainer(@NotNull String domain, @NotNull String aIconName, @NotNull String aOverlayName) {
        mIconName = ResourceUtils.getIconRegisterName(domain, aIconName);
        iconResource = ResourceUtils.getCompleteItemTextureResourceLocation(domain, aIconName);
        mOverlayName = ResourceUtils.getIconRegisterName(domain, aOverlayName);
        overlayResource = ResourceUtils.getCompleteItemTextureResourceLocation(domain, aOverlayName);
        GregTechAPI.sGTItemIconload.add(this);
        logRegisterIcons();
    }

    protected void logRegisterIcons() {
        GT_ICON_LOGGER.info("R {}", iconResource);
        GT_ICON_LOGGER.info("O {}", overlayResource);
    }

    // 2026-13-05: Currently unused
    private static Map<List<String>, IIconContainer> INSTANCES = new HashMap<>();

    public static @NotNull IIconContainer create(@NotNull String domain, @NotNull String aIconName) {
        return create(domain, aIconName, aIconName + Textures.OverlaySuffix);
    }

    public static @NotNull IIconContainer create(@NotNull String domain, @NotNull String aIconName,
        @NotNull String aOverlayName) {
        return INSTANCES.computeIfAbsent(
            List.of(domain, aIconName, aOverlayName),
            key -> new GTCustomItemIconContainer(domain, aIconName, aOverlayName));
    }

    public static void cleanup() {
        INSTANCES = new HashMap<>();
    }

    @Override
    public IIcon getIcon() {
        return mIcon;
    }

    @Override
    public IIcon getOverlayIcon() {
        return mOverlay;
    }

    @Override
    public void run() {
        final boolean iconExists = ResourceUtils.resourceExists(iconResource);
        final boolean overlayExists = ResourceUtils.resourceExists(overlayResource);
        if (iconExists || overlayExists) {
            mIcon = iconExists ? GregTechAPI.sItemIcons.registerIcon(mIconName) : Textures.InvisibleIcon.INVISIBLE_ICON;
            mOverlay = overlayExists ? GregTechAPI.sItemIcons.registerIcon(mOverlayName)
                : Textures.InvisibleIcon.INVISIBLE_ICON;
        } else {
            mIcon = Textures.InvisibleIcon.INVISIBLE_ICON;
            mOverlay = Textures.GlobalIcons.RENDERING_ERROR.getOverlayIcon();
        }
    }
}
