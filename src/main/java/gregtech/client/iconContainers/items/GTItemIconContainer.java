package gregtech.client.iconContainers.items;

import static gregtech.GTLoggers.GT_ICON_LOGGER;
import static gregtech.api.enums.Mods.GregTech;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import gregtech.api.enums.Mods;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.client.ResourceUtils;

public class GTItemIconContainer extends AbstractItemIconContainer implements Runnable {

    private static final Map<String, GTItemIconContainer> INSTANCES = new ConcurrentHashMap<>();
    final String mIconName;
    final ResourceLocation iconResource, overlayResource;
    final String mOverlayName;
    IIcon mIcon, mOverlay;

    GTItemIconContainer(@NotNull String aIconName) {
        String iconPath = "iconsets/" + aIconName;
        mIconName = ResourceUtils.getIconRegisterName(GregTech.resourceDomain, iconPath);
        iconResource = ResourceUtils.getCompleteItemTextureResourceLocation(GregTech.resourceDomain, iconPath);
        mOverlayName = ResourceUtils.getIconRegisterName(GregTech.resourceDomain, iconPath + "_OVERLAY");
        overlayResource = ResourceUtils
            .getCompleteItemTextureResourceLocation(GregTech.resourceDomain, iconPath + "_OVERLAY");
        GregTechAPI.sGTItemIconload.add(this);
        logRegisterIcon();
    }

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTItemIconContainer::new);
    }

    protected void logRegisterIcon() {
        GT_ICON_LOGGER.info("R {}", iconResource);
        GT_ICON_LOGGER.info("O {}", overlayResource);
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
