package client;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import kekztech.KekzCore;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class GTTexture implements IIconContainer, Runnable {

    public static final String TFFT_CASING = "TFFTCasing";
    public static final String MULTI_HATCH_OFF = "multi_hatch_off";
    public static final String MULTI_HATCH_ON = "multi_hatch_on";

    private static final HashMap<String, IIconContainer> icons = new HashMap<>();
    private static final String REL_PATH = "blocks/";

    static {
        registerTexture(TFFT_CASING);
        registerTexture(MULTI_HATCH_OFF);
        registerTexture(MULTI_HATCH_ON);
    }

    private IIcon icon;
    private IIcon iconOverlay;
    private final String iconName;
    private final String iconOverlayName;

    private GTTexture(String iconName) {
        this.iconName = iconName;
        this.iconOverlayName = "";
        GregTech_API.sGTBlockIconload.add(this);
    }

    private GTTexture(String iconName, String iconOverlayName) {
        this.iconName = iconName;
        this.iconOverlayName = iconOverlayName;
        GregTech_API.sGTBlockIconload.add(this);
    }

    public static void registerTexture(String iconName) {
        icons.put(iconName, new GTTexture(iconName));
    }

    public static void registerTexture(String iconName, String iconOverlayName) {
        icons.put(iconName, new GTTexture(iconName, iconOverlayName));
    }

    public static IIconContainer getIconContainer(String iconName) {
        return icons.get(iconName);
    }

    @Override
    public IIcon getIcon() {
        return icon;
    }

    @Override
    public IIcon getOverlayIcon() {
        return iconOverlay;
    }

    @Override
    public ResourceLocation getTextureFile() {
        return TextureMap.locationBlocksTexture;
    }

    @Override
    public void run() {
        icon = GregTech_API.sBlockIcons.registerIcon(KekzCore.MODID + ":" + REL_PATH + iconName);
        if(!iconOverlayName.equals("")) {
            iconOverlay = GregTech_API.sBlockIcons.registerIcon(KekzCore.MODID + ":" + REL_PATH + iconOverlayName);
        } else {
            KekzCore.LOGGER.info("No overlay texture specified for icon: " + iconName + "; This is fine.");
        }
    }
}
