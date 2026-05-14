package gregtech.client.iconContainers.blocks;

import static gregtech.api.enums.Mods.GregTech;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTLog;
import gregtech.api.util.client.ResourceUtils;
import gregtech.common.config.Gregtech;

public class GTCustomBlockIconContainer extends AbstractBlockIconContainer implements Runnable {

    protected final ResourceLocation iconResource, overlayResource;
    protected final String mIconName, mOverlayName;
    protected IIcon mIcon, mOverlay = null;

    GTCustomBlockIconContainer(@NotNull String aIconName) {
        mIconName = aIconName.contains(":") ? aIconName : GregTech.getResourcePath(aIconName);
        iconResource = ResourceUtils.getCompleteBlockTextureResourceLocation(mIconName);

        mOverlayName = mIconName + Textures.OverlaySuffix;
        overlayResource = ResourceUtils.getCompleteBlockTextureResourceLocation(mOverlayName);
        GregTechAPI.sGTBlockIconload.add(this);
        if (Gregtech.debug.logRegisterIcons) logRegisterIcons();
    }

    protected void logRegisterIcons() {
        GTLog.ico.println("R " + iconResource);
        GTLog.ico.println("O " + overlayResource);
    }

    // 2026-13-05: Counted 36 unique Block TextureSetIcons, so 52 will avoid resize until 50 entries
    private static Map<String, GTCustomBlockIconContainer> INSTANCES = new HashMap<>(52);

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTCustomBlockIconContainer::new);
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
