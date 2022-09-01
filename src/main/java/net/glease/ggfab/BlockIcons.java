package net.glease.ggfab;

import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.IIconContainer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public enum BlockIcons implements IIconContainer {
    ;
    private final CustomIcon backing;

    BlockIcons() {
        backing = new CustomIcon("iconsets/" + name());
    }

    @Override
    public IIcon getIcon() {
        return backing.getIcon();
    }

    @Override
    public IIcon getOverlayIcon() {
        return backing.getOverlayIcon();
    }

    @Override
    public ResourceLocation getTextureFile() {
        return backing.getTextureFile();
    }
}
