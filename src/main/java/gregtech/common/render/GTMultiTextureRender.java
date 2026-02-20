package gregtech.common.render;

import gregtech.GTMod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ISBRContext;

/**
 * <p>
 * Lets Multiple ITextures Render overlay over each other.<
 * </p>
 * <p>
 * I should have done this much earlier...
 * </p>
 */
public class GTMultiTextureRender extends GTTextureBase implements ITexture {

    protected final ITexture[] mTextures;

    public static GTMultiTextureRender get(ITexture... aTextures) {
        return GTMod.GT.isClientSide() ? new GTMultiTextureRender(aTextures) : null;
    }

    public static ITexture create(ITexture[] textures) {
        return new GTMultiTextureRender(textures);
    }

    protected GTMultiTextureRender(ITexture... aTextures) {
        mTextures = aTextures;
    }

    @Override
    public void renderXPos(ISBRContext ctx) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture()) tTexture.renderXPos(ctx);
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture()) tTexture.renderXNeg(ctx);
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture()) tTexture.renderYPos(ctx);
    }

    @Override
    public void renderYNeg(ISBRContext ctx) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture()) tTexture.renderYNeg(ctx);
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture()) tTexture.renderZPos(ctx);
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture()) tTexture.renderZNeg(ctx);
    }

    @Override
    public boolean isValidTexture() {
        return true;
    }
}
