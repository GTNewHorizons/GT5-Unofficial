package gregtech.client.iconContainers.blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTLog;
import gregtech.api.util.client.ResourceUtils;

public class GTOptionalBlockIconContainer extends GTBlockIconContainer {

    private static final Map<String, GTOptionalBlockIconContainer> INSTANCES = new ConcurrentHashMap<>();

    GTOptionalBlockIconContainer(@NotNull String aIconName) {
        super(aIconName);
    }

    public static @NotNull IIconContainer create(@NotNull String aIconName) {
        return INSTANCES.computeIfAbsent(aIconName, GTOptionalBlockIconContainer::new);
    }

    @Override
    protected void logRegisterIcon() {
        GTLog.ico.println("O " + iconResource);
    }

    @Override
    public void run() {
        // Icons with a fallback are optional and later replaced with their fallback
        mIcon = ResourceUtils.resourceExists(iconResource) ? GregTechAPI.sBlockIcons.registerIcon(mIconName)
            : Textures.InvisibleIcon.INVISIBLE_ICON;
    }
}
