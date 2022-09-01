package net.glease.ggfab;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public enum BlockIcons implements IIconContainer, Runnable {
    ;
    public static final String RES_PATH = GGConstants.MODID + ":";
    private IIcon mIcon;

    BlockIcons() {
        GregTech_API.sGTBlockIconload.add(this);
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
        mIcon = GregTech_API.sBlockIcons.registerIcon(RES_PATH + "iconsets/" + this);
    }
}
