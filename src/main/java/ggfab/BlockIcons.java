package ggfab;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;

public enum BlockIcons implements IIconContainer, Runnable {

    OVERLAY_FRONT_ADV_ASSLINE_ACTIVE,
    OVERLAY_FRONT_ADV_ASSLINE_ACTIVE_GLOW,
    OVERLAY_FRONT_ADV_ASSLINE_STUCK,
    OVERLAY_FRONT_ADV_ASSLINE_STUCK_GLOW,
    OVERLAY_FRONT_ADV_ASSLINE,
    OVERLAY_FRONT_ADV_ASSLINE_GLOW,;

    public static final String RES_PATH = GGConstants.MODID + ":";
    private IIcon mIcon;

    BlockIcons() {
        GregTechAPI.sGTBlockIconload.add(this);
    }

    @Override
    public IIcon getIcon() {
        return mIcon;
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
        mIcon = GregTechAPI.sBlockIcons.registerIcon(RES_PATH + "iconsets/" + this);
    }
}
