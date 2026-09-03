package gregtech.client.iconContainers.blocks;

import static gregtech.GTLoggers.GT_ICON_LOGGER;
import static gregtech.api.enums.Textures.OverlaySuffix;
import static gregtech.api.enums.Textures.TextureSetFallback;
import static gregtech.client.iconContainers.items.GTTextureSetItemIconContainer.createIconName;
import static gregtech.client.iconContainers.items.GTTextureSetItemIconContainer.registerResourceOrFallback;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.client.ResourceUtils;
import gregtech.client.iconContainers.items.GTTextureSetItemIconContainer.TextureSetIconType;

public class GTTextureSetBlockIconContainer extends AbstractBlockIconContainer implements Runnable {

    private IIcon mIcon, mOverlay;

    private final String iconName, iconOverlayName;
    private final String fallbackIconName, fallbackIconOverlayName;

    protected ResourceLocation iconResource, iconFallbackResource;
    protected ResourceLocation iconOverlayResource, iconOverlayFallbackResource;

    private GTTextureSetBlockIconContainer(@NotNull String domain, @NotNull Pair<String, String> pair) {
        this(domain, pair.getLeft(), pair.getRight(), null);
    }

    private GTTextureSetBlockIconContainer(@NotNull String domain, @NotNull String setName, @NotNull String prefix,
                                           @Nullable IIconRegister override) {
        String iconPath = createIconName(setName, prefix);
        String fallbackIconPath = createIconName(TextureSetFallback, prefix);
        this.iconName = ResourceUtils.getIconRegisterName(domain, iconPath);
        this.fallbackIconName = ResourceUtils.getIconRegisterName(domain, fallbackIconPath);
        iconResource = ResourceUtils.getCompleteBlockTextureResourceLocation(domain, iconPath);
        iconFallbackResource = ResourceUtils.getCompleteBlockTextureResourceLocation(domain, fallbackIconPath);

        String iconOverlayPath = createIconName(setName, prefix + OverlaySuffix);
        String fallbackIconOverlayPath = createIconName(TextureSetFallback, prefix + OverlaySuffix);
        this.iconOverlayName = ResourceUtils.getIconRegisterName(domain, iconOverlayPath);
        this.fallbackIconOverlayName = ResourceUtils.getIconRegisterName(domain, fallbackIconOverlayPath);
        iconOverlayResource = ResourceUtils.getCompleteBlockTextureResourceLocation(domain, iconOverlayPath);
        iconOverlayFallbackResource = ResourceUtils
            .getCompleteBlockTextureResourceLocation(domain, fallbackIconOverlayPath);

        if (override != null) {
            run(override);
        } else {
            GregTechAPI.sGTBlockIconload.add(this);
        }
        logRegisterIcons();
    }

    // 2026-13-05: Counted 1782 unique Block TextureSetIcons, so 2.5K will avoid resize until 1920 entries
    private static Map<Pair<String, String>, IIconContainer> INSTANCES = new HashMap<>(2520);

    public static @NotNull IIconContainer create(@NotNull String domain, @NotNull String setName, @NotNull String prefix,
                                                 IIconRegister override) {
        if (override != null) {
            return new GTTextureSetBlockIconContainer(domain, setName, prefix, override);
        }
        return INSTANCES.computeIfAbsent(Pair.of(setName, prefix), key -> new GTTextureSetBlockIconContainer(domain, key));
    }

    public static void cleanup() {
        INSTANCES = new HashMap<>();
    }

    protected void logRegisterIcons() {
        GT_ICON_LOGGER.info("R {}", iconResource);
        GT_ICON_LOGGER.info("O {}", iconOverlayResource);
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
        run(GregTechAPI.sBlockIcons);
    }

    private void run(IIconRegister register) {
        Pair<IIcon, TextureSetIconType> iconPair = registerResourceOrFallback(
            iconResource,
            iconName,
            iconFallbackResource,
            fallbackIconName,
            register);
        Pair<IIcon, TextureSetIconType> overlayPair = registerResourceOrFallback(
            iconOverlayResource,
            iconOverlayName,
            iconOverlayFallbackResource,
            fallbackIconOverlayName,
            register);
        mIcon = iconPair.getRight() == TextureSetIconType.INVISIBLE ? GregTechAPI.sBlockIcons.registerIcon(iconName)
            : iconPair.getLeft();
        if (iconPair.getRight() == overlayPair.getRight() && overlayPair.getRight() != TextureSetIconType.INVISIBLE) {
            mOverlay = overlayPair.getLeft();
        } else {
            mOverlay = null;
        }
    }

}
