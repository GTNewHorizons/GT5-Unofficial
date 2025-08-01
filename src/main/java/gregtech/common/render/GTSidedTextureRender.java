package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.LightingHelper;

public class GTSidedTextureRender extends GTTextureBase implements ITexture, IColorModulationContainer {

    protected final ITexture[] mTextures;
    /**
     * DO NOT MANIPULATE THE VALUES INSIDE THIS ARRAY!!!
     * <p/>
     * Just set this variable to another different Array instead. Otherwise some colored things will get Problems.
     */
    private final short[] mRGBa;

    protected GTSidedTextureRender(IIconContainer aIcon0, IIconContainer aIcon1, IIconContainer aIcon2,
        IIconContainer aIcon3, IIconContainer aIcon4, IIconContainer aIcon5, short[] aRGBa, boolean aAllowAlpha) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTSidedTextureRender");
        mTextures = new ITexture[] { TextureFactory.of(aIcon0, aRGBa, aAllowAlpha),
            TextureFactory.of(aIcon1, aRGBa, aAllowAlpha), TextureFactory.of(aIcon2, aRGBa, aAllowAlpha),
            TextureFactory.of(aIcon3, aRGBa, aAllowAlpha), TextureFactory.of(aIcon4, aRGBa, aAllowAlpha),
            TextureFactory.of(aIcon5, aRGBa, aAllowAlpha) };
        mRGBa = aRGBa;
    }

    @Override
    public boolean isOldTexture() {
        return false;
    }

    @Override
    public void renderXPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int worldRenderPass) {
        mTextures[5].renderXPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int worldRenderPass) {
        mTextures[4].renderXNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int worldRenderPass) {
        mTextures[1].renderYPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int worldRenderPass) {
        mTextures[0].renderYNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int worldRenderPass) {
        mTextures[3].renderZPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, LightingHelper lightingHelper, Block aBlock, int aX, int aY, int aZ,
        int worldRenderPass) {
        mTextures[2].renderZNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
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
