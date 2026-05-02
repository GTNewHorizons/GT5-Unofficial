package gregtech.client.iconContainers.blocks;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTLog;
import gregtech.api.util.client.ResourceUtils;
import gregtech.common.config.Gregtech;

public class GTBlockIconContainer extends AbstractBlockIconContainer implements Runnable {

    private static final Map<String, GTBlockIconContainer> INSTANCES = new ConcurrentHashMap<>();
    final String mIconName;
    final ResourceLocation iconResource;
    IIcon mIcon;

    GTBlockIconContainer(@NotNull String aIconName) {
        mIconName = GregTech.resourceDomain + ":iconsets/" + aIconName;
        iconResource = ResourceUtils.getCompleteBlockTextureResourceLocation(mIconName);
        GregTechAPI.sGTBlockIconload.add(this);
        if (Gregtech.debug.logRegisterIcons) logRegisterIcon();
    }

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTBlockIconContainer::new);
    }

    protected void logRegisterIcon() {
        GTLog.ico.println("R " + iconResource);
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
