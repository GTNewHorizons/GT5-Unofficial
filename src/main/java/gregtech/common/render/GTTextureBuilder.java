package gregtech.common.render;

import javax.annotation.Nullable;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;

public final class GTTextureBuilder implements ITextureBuilder {

    private @Nullable IIconContainer iconContainer;
    private short[] rgba;
    private boolean extFacing;
    private boolean glow;

    public GTTextureBuilder() {
        rgba = Dyes._NULL.getRGBA();
    }

    @Override
    public ITextureBuilder addIcon(final IIconContainer iconContainer) {
        if (this.iconContainer != null) throw new IllegalArgumentException("iconContainer already defined!");
        this.iconContainer = iconContainer;
        return this;
    }

    @Override
    public ITextureBuilder setRGBA(final short[] rgba) {
        this.rgba = rgba;
        return this;
    }

    @Override
    @Deprecated
    // TODO remove this (extFacing is false by default)
    public ITextureBuilder stdOrient() {
        this.extFacing = false;
        return this;
    }

    @Override
    public ITextureBuilder extFacing() {
        this.extFacing = true;
        return this;
    }

    @Override
    public ITextureBuilder glow() {
        glow = true;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ITexture build() {
        if (iconContainer == null) {
            throw new IllegalStateException("iconContainer not specified!");
        }
        return new GTRenderedTexture(iconContainer, rgba, glow, extFacing);
    }
}
