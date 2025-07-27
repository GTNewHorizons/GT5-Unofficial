package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

public class GTSidedTextureRender extends GTTextureBase implements ITexture {

    private final ITexture[] mTextures;

    // spotless:off
    protected GTSidedTextureRender(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2, IIconContainer aIcon3, IIconContainer aIcon4, IIconContainer aIcon5, int colorRGB, boolean aAllowAlpha) {
        mTextures = new ITexture[] {
            TextureFactory.of(aIcon0, colorRGB, aAllowAlpha),
            TextureFactory.of(aIcon1, colorRGB, aAllowAlpha),
            TextureFactory.of(aIcon2, colorRGB, aAllowAlpha),
            TextureFactory.of(aIcon3, colorRGB, aAllowAlpha),
            TextureFactory.of(aIcon4, colorRGB, aAllowAlpha),
            TextureFactory.of(aIcon5, colorRGB, aAllowAlpha) };
    }
    // spotless:on

    @Override
    public boolean isOldTexture() {
        return false;
    }

    @Override
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        mTextures[5].renderXPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        mTextures[4].renderXNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        mTextures[1].renderYPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        mTextures[0].renderYNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        mTextures[3].renderZPos(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        mTextures[2].renderZNeg(aRenderer, aBlock, aX, aY, aZ);
    }

    @Override
    public boolean isValidTexture() {
        for (ITexture renderedTexture : mTextures) {
            if (!renderedTexture.isValidTexture()) return false;
        }
        return true;
    }
}
