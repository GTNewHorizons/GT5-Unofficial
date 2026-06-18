package gregtech.client.iconContainers.items;

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
import gregtech.api.util.GTLog;
import gregtech.api.util.client.ResourceUtils;
import gregtech.common.config.Gregtech;

public class GTTextureSetItemIconContainer extends AbstractItemIconContainer implements Runnable {

    private IIcon mIcon, mOverlay;

    private final String iconName, iconOverlayName;
    private final String fallbackIconName, fallbackIconOverlayName;

    protected ResourceLocation iconResource, iconFallbackResource;
    protected ResourceLocation iconOverlayResource, iconOverlayFallbackResource;

    private GTTextureSetItemIconContainer(@NotNull Pair<String, String> pair) {
        this(pair.getLeft(), pair.getRight(), null);
    }

    private GTTextureSetItemIconContainer(@NotNull String setName, @NotNull String prefix,
        @Nullable IIconRegister override) {
        this.iconName = createIconName(setName, prefix);
        this.fallbackIconName = createIconName(TextureSetFallback, prefix);
        iconResource = ResourceUtils.getCompleteItemTextureResourceLocation(iconName);
        iconFallbackResource = ResourceUtils.getCompleteItemTextureResourceLocation(fallbackIconName);

        this.iconOverlayName = createIconName(setName, prefix + OverlaySuffix);
        this.fallbackIconOverlayName = createIconName(TextureSetFallback, prefix + OverlaySuffix);
        iconOverlayResource = ResourceUtils.getCompleteItemTextureResourceLocation(iconOverlayName);
        iconOverlayFallbackResource = ResourceUtils.getCompleteItemTextureResourceLocation(fallbackIconOverlayName);

        if (override != null) {
            run(override);
        } else {
            GregTechAPI.sGTItemIconload.add(this);
        }
        if (Gregtech.debug.logRegisterIcons) logRegisterIcons();
    }

    public static String createIconName(String setName, String prefix) {
        String iconName = TextureMaterialIconDirectory + setName + prefix;
        return iconName.contains(":") ? iconName : GregTech.resourceDomain + ":" + iconName;
    }

    // 2026-13-05: Counted 7371 unique Item TextureSetIcons, so 9.4K will avoid resize until 7500 entries
    private static Map<Pair<String, String>, IIconContainer> INSTANCES = new HashMap<>(9375);

    public static @NotNull IIconContainer create(@NotNull String setName, @NotNull String prefix,
        IIconRegister override) {
        if (override != null) {
            return new GTTextureSetItemIconContainer(setName, prefix, override);
        }
        return INSTANCES.computeIfAbsent(Pair.of(setName, prefix), GTTextureSetItemIconContainer::new);
    }

    public static void cleanup() {
        INSTANCES = new HashMap<>();
    }

    protected void logRegisterIcons() {
        GTLog.ico.println("R " + iconResource);
        GTLog.ico.println("O " + iconOverlayResource);
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
