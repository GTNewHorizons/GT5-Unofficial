package gregtech.common.render;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ISBRContext;
import gregtech.api.render.TextureFactory;

public class GTSidedTextureRender extends GTTextureBase implements ITexture, IColorModulationContainer {

    protected final ITexture[] mTextures;
    /**
     * DO NOT MANIPULATE THE VALUES INSIDE THIS ARRAY!!!
     * <p/>
     * Just set this variable to another different Array instead. Otherwise some colored things will get Problems.
     */
    private final short[] mRGBa;

    public GTSidedTextureRender(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2,
        IIconContainer aIcon3, IIconContainer aIcon4, IIconContainer aIcon5, short[] aRGBa) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTSidedTextureRender");
        mTextures = new ITexture[] { TextureFactory.of(aIcon0, aRGBa), TextureFactory.of(aIcon1, aRGBa),
            TextureFactory.of(aIcon2, aRGBa), TextureFactory.of(aIcon3, aRGBa), TextureFactory.of(aIcon4, aRGBa),
            TextureFactory.of(aIcon5, aRGBa) };
        mRGBa = aRGBa;
    }

    @Override
    public void renderXPos(ISBRContext ctx) {
        mTextures[5].renderXPos(ctx);
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        mTextures[4].renderXNeg(ctx);
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        mTextures[1].renderYPos(ctx);
    }

    @Override
    public void renderYNeg(ISBRContext ctx) {
        mTextures[0].renderYNeg(ctx);
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        mTextures[3].renderZPos(ctx);
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        mTextures[2].renderZNeg(ctx);
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }

    @Override
    public boolean isValidTexture() {
        for (ITexture renderedTexture : mTextures) {
            if (!renderedTexture.isValidTexture()) return false;
        }
        return true;
    }
}
