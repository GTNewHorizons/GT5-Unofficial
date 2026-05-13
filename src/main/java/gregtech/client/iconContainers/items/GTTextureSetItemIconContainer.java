package gregtech.client.iconContainers.items;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

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

    private static final String aTextMatIconDir = "materialicons/";

    private GTTextureSetItemIconContainer(@NotNull Pair<String, String> pair) {
        this(pair.getLeft(), pair.getRight());
    }

    private GTTextureSetItemIconContainer(@NotNull String setName, @NotNull String prefix) {
        this.iconName = createIconName(setName, prefix);
        this.fallbackIconName = createIconName("DEFAULT", prefix);
        iconResource = ResourceUtils.getCompleteItemTextureResourceLocation(iconName);
        iconFallbackResource = ResourceUtils.getCompleteItemTextureResourceLocation(fallbackIconName);

        this.iconOverlayName = createIconName(setName, prefix + "_OVERLAY");
        this.fallbackIconOverlayName = createIconName("DEFAULT", prefix + "_OVERLAY");
        iconOverlayResource = ResourceUtils.getCompleteItemTextureResourceLocation(iconOverlayName);
        iconOverlayFallbackResource = ResourceUtils.getCompleteItemTextureResourceLocation(fallbackIconOverlayName);

        GregTechAPI.sGTItemIconload.add(this);
        if (Gregtech.debug.logRegisterIcons) logRegisterIcons();
    }

    private static String createIconName(String setName, String prefix) {
        String iconName = aTextMatIconDir + setName + prefix;
        return iconName.contains(":") ? iconName : GregTech.resourceDomain + ":" + iconName;
    }

    static final Map<Pair<String, String>, IIconContainer> INSTANCES = new ConcurrentHashMap<>(3072);

    public static @NotNull IIconContainer create(@NotNull String setName, String prefix) {
        return INSTANCES.computeIfAbsent(Pair.of(setName, prefix), GTTextureSetItemIconContainer::new);
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
        mIcon = registerResourceOrFallback(iconResource, iconName, iconFallbackResource, fallbackIconName);
        mOverlay = registerResourceOrFallback(
            iconOverlayResource,
            iconOverlayName,
            iconOverlayFallbackResource,
            fallbackIconOverlayName);

        if (mIcon == InvisibleIcon.INVISIBLE_ICON && mOverlay == InvisibleIcon.INVISIBLE_ICON) {
            mOverlay = Textures.GlobalIcons.RENDERING_ERROR.getOverlayIcon();
        }
    }

    private static IIcon registerResourceOrFallback(ResourceLocation rl, String name, ResourceLocation fallback,
        String fallbackName) {
        if (ResourceUtils.resourceExists(rl)) {
            return GregTechAPI.sItemIcons.registerIcon(name);
        }
        if (ResourceUtils.resourceExists(fallback)) {
            return GregTechAPI.sItemIcons.registerIcon(fallbackName);
        }
        return Textures.InvisibleIcon.INVISIBLE_ICON;
    }
}
