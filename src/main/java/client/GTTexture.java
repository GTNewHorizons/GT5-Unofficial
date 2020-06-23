package client;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_CopiedBlockTexture;
import kekztech.KekzCore;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class GTTexture implements IIconContainer, Runnable {

    public static final GTTexture TFFT_CASING = new GTTexture("blocks/TFFTCasing");
    public static final GTTexture MULTI_HATCH_OFF = new GTTexture("blocks/multi_hatch_off");
    public static final GTTexture MULTI_HATCH_ON = new GTTexture("blocks/multi_hatch_on");

    private IIcon icon;
    private final String iconName;

    private GTTexture(String iconName) {
        this.iconName = iconName;
        GregTech_API.sGTBlockIconload.add(this);
    }

    @Override
    public IIcon getIcon() {
        return icon;
    }

    @Override
    public IIcon getOverlayIcon() {
        return null;
    }

    @Override
    public ResourceLocation getTextureFile() {
        return TextureMap.locationBlocksTexture;
    }

    @Override
    public void run() {
        icon = GregTech_API.sBlockIcons.registerIcon(KekzCore.MODID + ":" + iconName);
    }
}
