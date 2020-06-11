package client;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import kekztech.KekzCore;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class GTTexture implements IIconContainer {

    public static final String MULTI_HATCH_OFF = "multi_hatch_off";
    public static final String MULTI_HATCH_ON = "multi_hatch_on";

    private static final HashMap<String, IIconContainer> icons = new HashMap<>();

    private final IIcon icon;
    private final IIcon overlayIcon;

    private GTTexture(String iconName) {
        icon = GregTech_API.sBlockIcons.registerIcon(KekzCore.MODID + ":" + iconName);
        overlayIcon = null;
    }

    private GTTexture(String iconName, String overlayIconName) {
        icon = GregTech_API.sBlockIcons.registerIcon(KekzCore.MODID + ":" + iconName);
        overlayIcon = GregTech_API.sBlockIcons.registerIcon(KekzCore.MODID + ":" + overlayIconName);
    }

    public static void registerTexture(String iconName) {
        icons.put(iconName, new GTTexture(iconName));
    }

    public static void registerTexture(String iconName, String overlayIconName) {
        icons.put(iconName, new GTTexture(iconName, overlayIconName));
    }

    public static void init() {
        registerTexture(MULTI_HATCH_ON);
        registerTexture(MULTI_HATCH_OFF);
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
        return overlayIcon;
    }

    @Override
    public ResourceLocation getTextureFile() {
        return TextureMap.locationBlocksTexture;
    }
}
