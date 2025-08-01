package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import gregtech.GTMod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.LightingHelper;

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

    protected GTMultiTextureRender(ITexture... aTextures) {
        mTextures = aTextures;
    }

    @Override
    public void renderXPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture())
            tTexture.renderXPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, renderWorldPass);
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture())
            tTexture.renderXNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, renderWorldPass);
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture())
            tTexture.renderYPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, renderWorldPass);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture())
            tTexture.renderYNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, renderWorldPass);
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture())
            tTexture.renderZPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, renderWorldPass);
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int renderWorldPass) {
        for (ITexture tTexture : mTextures) if (tTexture != null && tTexture.isValidTexture())
            tTexture.renderZNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, renderWorldPass);
    }

    @Override
    public boolean isValidTexture() {
        return true;
    }
}
