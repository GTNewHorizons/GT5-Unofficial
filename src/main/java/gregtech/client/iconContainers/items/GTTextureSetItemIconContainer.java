package gregtech.client.iconContainers.items;

import static gregtech.GTLoggers.GT_ICON_LOGGER;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Textures.OverlaySuffix;
import static gregtech.api.enums.Textures.TextureMaterialIconDirectory;
import static gregtech.api.enums.Textures.TextureSetFallback;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.InvisibleIcon;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.client.ResourceUtils;

public class GTTextureSetItemIconContainer extends AbstractItemIconContainer implements Runnable {

    private IIcon mIcon, mOverlay;

    private final String iconName, iconOverlayName;
    private final String fallbackIconName, fallbackIconOverlayName;

    protected ResourceLocation iconResource, iconFallbackResource;
    protected ResourceLocation iconOverlayResource, iconOverlayFallbackResource;

    private GTTextureSetItemIconContainer(@NotNull String domain, @NotNull Pair<String, String> pair) {
        this(domain, pair.getLeft(), pair.getRight(), null);
    }

    private GTTextureSetItemIconContainer(@NotNull String domain, @NotNull String setName, @NotNull String prefix,
                                          @Nullable IIconRegister override) {
        String iconPath = createIconName(setName, prefix);
        String fallbackIconPath = createIconName(TextureSetFallback, prefix);
        this.iconName = domain + ":" + iconPath;
        this.fallbackIconName = domain + ":" + fallbackIconPath;
        iconResource = ResourceUtils.getCompleteItemTextureResourceLocation(domain, iconPath);
        iconFallbackResource = ResourceUtils.getCompleteItemTextureResourceLocation(domain, fallbackIconPath);

        String iconOverlayPath = createIconName(setName, prefix + OverlaySuffix);
        String fallbackIconOverlayPath = createIconName(TextureSetFallback, prefix + OverlaySuffix);
        this.iconOverlayName = domain + ":" + iconOverlayPath;
        this.fallbackIconOverlayName = domain + ":" + fallbackIconOverlayPath;
        iconOverlayResource = ResourceUtils.getCompleteItemTextureResourceLocation(domain, iconOverlayPath);
        iconOverlayFallbackResource = ResourceUtils
            .getCompleteItemTextureResourceLocation(domain, fallbackIconOverlayPath);

        if (override != null) {
            run(override);
        } else {
            GregTechAPI.sGTItemIconload.add(this);
        }
        logRegisterIcons();
    }

    public static String createIconName(String setName, String prefix) {
        return TextureMaterialIconDirectory + setName + prefix;
    }

    // 2026-13-05: Counted 7371 unique Item TextureSetIcons, so 9.4K will avoid resize until 7500 entries
    private static Map<Pair<String, String>, IIconContainer> INSTANCES = new HashMap<>(9375);

    public static @NotNull IIconContainer create(@NotNull String domain, @NotNull String setName, @NotNull String prefix,
                                                 IIconRegister override) {
        if (override != null) {
            return new GTTextureSetItemIconContainer(domain, setName, prefix, override);
        }
        return INSTANCES.computeIfAbsent(Pair.of(setName, prefix), key -> new GTTextureSetItemIconContainer(domain, key));
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
        run(GregTechAPI.sItemIcons);
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

        mIcon = iconPair.getLeft();
        mOverlay = overlayPair.getLeft();

        if (iconPair.getRight() == TextureSetIconType.OVERRIDE
            && overlayPair.getRight() == TextureSetIconType.FALLBACK) {
            mOverlay = InvisibleIcon.INVISIBLE_ICON;
        } else if (iconPair.getRight() == TextureSetIconType.FALLBACK
            && overlayPair.getRight() == TextureSetIconType.OVERRIDE) {
                mIcon = InvisibleIcon.INVISIBLE_ICON;
            } else if (mIcon == InvisibleIcon.INVISIBLE_ICON && mOverlay == InvisibleIcon.INVISIBLE_ICON) {
                mOverlay = Textures.GlobalIcons.RENDERING_ERROR.getOverlayIcon();
            }
    }

    public static Pair<IIcon, TextureSetIconType> registerResourceOrFallback(ResourceLocation rl, String name,
        ResourceLocation fallback, String fallbackName, IIconRegister register) {
        if (ResourceUtils.resourceExists(rl)) {
            return Pair.of(register.registerIcon(name), TextureSetIconType.OVERRIDE);
        }
        if (ResourceUtils.resourceExists(fallback)) {
            return Pair.of(register.registerIcon(fallbackName), TextureSetIconType.FALLBACK);
        }
        return Pair.of(Textures.InvisibleIcon.INVISIBLE_ICON, TextureSetIconType.INVISIBLE);
    }

    public enum TextureSetIconType {
        OVERRIDE,
        FALLBACK,
        INVISIBLE
    }
}
