package gregtech.client.textures.items;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTLog;
import gregtech.api.util.client.ResourceUtils;
import gregtech.common.config.Gregtech;

public class GTCustomItemIconContainer implements IIconContainer, Runnable {

    protected IIcon mIcon, mOverlay;
    protected String mIconName, mOverlayName;
    protected ResourceLocation iconResource, overlayResource;

    protected GTCustomItemIconContainer(@NotNull String aIconName) {
        mIconName = aIconName.contains(":") ? aIconName : GregTech.resourceDomain + ":" + aIconName;
        iconResource = ResourceUtils.getCompleteItemTextureResourceLocation(mIconName);
        mOverlayName = mIconName + "_OVERLAY";
        overlayResource = ResourceUtils.getCompleteItemTextureResourceLocation(mOverlayName);
        GregTechAPI.sGTItemIconload.add(this);
        if (Gregtech.debug.logRegisterIcons) {
            GTLog.ico.println("R " + iconResource);
            GTLog.ico.println("O " + overlayResource);
        }
    }

    // 2026-02-03: Counted 1928 unique Item CustomIcons, so 3K will avoid resize until 2304 entries
    static final Map<String, IIconContainer> INSTANCES = new ConcurrentHashMap<>(3072);

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTCustomItemIconContainer::new);
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
    public ResourceLocation getTextureFile() {
        return TextureMap.locationItemsTexture;
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
