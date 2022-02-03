package gregtech.common.render;

import gregtech.api.interfaces.ITexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

/**
 * <p>Lets Multiple ITextures Render overlay over each other.<</p>
 * <p>I should have done this much earlier...</p>
 */
public class GT_MultiTexture extends GT_TextureBase implements ITexture {
    protected final ITexture[] mTextures;

    protected GT_MultiTexture(ITexture... aTextures) {
        mTextures = aTextures;
    }

    @Override
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.renderXPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.renderXNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.renderYPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.renderYNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.renderZPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        for (ITexture tTexture : mTextures)
            if (tTexture != null && tTexture.isValidTexture()) tTexture.renderZNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public boolean isValidTexture() {
        return true;
    }
}
