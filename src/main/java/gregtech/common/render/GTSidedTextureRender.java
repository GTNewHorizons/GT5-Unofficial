package gregtech.common.render;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.SBRContext;
import gregtech.api.render.TextureFactory;

public class GTSidedTextureRender extends GTTextureBase implements ITexture {

    private final ITexture[] mTextures;

    // spotless:off
    protected GTSidedTextureRender(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2, IIconContainer aIcon3, IIconContainer aIcon4, IIconContainer aIcon5, int colorRGB) {
        mTextures = new ITexture[] {
            TextureFactory.of(aIcon0, colorRGB),
            TextureFactory.of(aIcon1, colorRGB),
            TextureFactory.of(aIcon2, colorRGB),
            TextureFactory.of(aIcon3, colorRGB),
            TextureFactory.of(aIcon4, colorRGB),
            TextureFactory.of(aIcon5, colorRGB) };
    }
    // spotless:on

    @Override
    public boolean isOldTexture() {
        return false;
    }

    @Override
    public void renderXPos(SBRContext ctx) {
        mTextures[5].renderXPos(ctx);
    }

    @Override
    public void renderXNeg(SBRContext ctx) {
        mTextures[4].renderXNeg(ctx);
    }

    @Override
    public void renderYPos(SBRContext ctx) {
        mTextures[1].renderYPos(ctx);
    }

    @Override
    public void renderYNeg(SBRContext ctx) {
        mTextures[0].renderYNeg(ctx);
    }

    @Override
    public void renderZPos(SBRContext ctx) {
        mTextures[3].renderZPos(ctx);
    }

    @Override
    public void renderZNeg(SBRContext ctx) {
        mTextures[2].renderZNeg(ctx);
    }

    @Override
    public boolean isValidTexture() {
        for (ITexture renderedTexture : mTextures) {
            if (!renderedTexture.isValidTexture()) return false;
        }
        return true;
    }
}
