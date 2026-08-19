package gregtech.client.iconContainers.blocks;

import static gregtech.GTLoggers.GT_ICON_LOGGER;
import static gregtech.api.enums.Mods.GregTech;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.client.ResourceUtils;

public class GTBlockIconContainer extends AbstractBlockIconContainer implements Runnable {

    private static final Map<String, GTBlockIconContainer> INSTANCES = new ConcurrentHashMap<>();
    final String mIconName;
    final ResourceLocation iconResource;
    IIcon mIcon;

    GTBlockIconContainer(@NotNull String aIconName) {
        mIconName = GregTech.resourceDomain + ":iconsets/" + aIconName;
        iconResource = ResourceUtils.getCompleteBlockTextureResourceLocation(mIconName);
        GregTechAPI.sGTBlockIconload.add(this);
        logRegisterIcon();
    }

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTBlockIconContainer::new);
    }

    protected void logRegisterIcon() {
        GT_ICON_LOGGER.info("R {}", iconResource);
    }

    @Override
    public IIcon getIcon() {
        return mIcon;
    }

    @Override
    public void run() {
        mIcon = GregTechAPI.sBlockIcons.registerIcon(mIconName);
    }
}
