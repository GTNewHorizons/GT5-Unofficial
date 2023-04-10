package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GT_RenderingWorld;
import gregtech.api.util.LightingHelper;

class GT_CopiedCTMBlockTexture extends GT_TextureBase implements ITexture, IBlockContainer {

    private final Block mBlock;
    private final byte mSide, mMeta;

    GT_CopiedCTMBlockTexture(Block aBlock, int aSide, int aMeta, short[] aRGBa, boolean allowAlpha) {
        if (aRGBa.length != 4)
            throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_CopiedCTMBlockTexture");
        mBlock = aBlock;
        mSide = (byte) aSide;
        mMeta = (byte) aMeta;
    }

    @Override
    public boolean isOldTexture() {
        return false;
    }

    private IIcon getIcon(int aSide, int aX, int aY, int aZ, RenderBlocks aRenderer) {
        final int tSide = mSide == 6 ? aSide : mSide;
        return mBlock.getIcon(getBlockAccess(aRenderer), aX, aY, aZ, tSide);
    }

    private GT_RenderingWorld getBlockAccess(RenderBlocks aRenderer) {
        return GT_RenderingWorld.getInstance(aRenderer.blockAccess);
    }

    @Override
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        final IIcon aIcon = getIcon(ForgeDirection.EAST.ordinal(), aX, aY, aZ, aRenderer);
        aRenderer.field_152631_f = true;
        startDrawingQuads(aRenderer, 1.0f, 0.0f, 0.0f);
        new LightingHelper(aRenderer).setupLightingXPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.EAST.ordinal(), mBlock.colorMultiplier(getBlockAccess(aRenderer), aX, aY, aZ));
        aRenderer.renderFaceXPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        aRenderer.field_152631_f = false;
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, -1.0f, 0.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.WEST.ordinal(), aX, aY, aZ, aRenderer);
        new LightingHelper(aRenderer).setupLightingXNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.WEST.ordinal(), mBlock.colorMultiplier(getBlockAccess(aRenderer), aX, aY, aZ));
        aRenderer.renderFaceXNeg(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.UP.ordinal(), aX, aY, aZ, aRenderer);
        new LightingHelper(aRenderer).setupLightingYPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.UP.ordinal(), mBlock.colorMultiplier(getBlockAccess(aRenderer), aX, aY, aZ));
        aRenderer.renderFaceYPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, -1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.DOWN.ordinal(), aX, aY, aZ, aRenderer);
        new LightingHelper(aRenderer).setupLightingYNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.DOWN.ordinal(), mBlock.colorMultiplier(getBlockAccess(aRenderer), aX, aY, aZ));
        aRenderer.renderFaceYNeg(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, 1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.SOUTH.ordinal(), aX, aY, aZ, aRenderer);
        new LightingHelper(aRenderer).setupLightingZPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.SOUTH.ordinal(), mBlock.colorMultiplier(getBlockAccess(aRenderer), aX, aY, aZ));
        aRenderer.renderFaceZPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, -1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.NORTH.ordinal(), aX, aY, aZ, aRenderer);
        aRenderer.field_152631_f = true;
        new LightingHelper(aRenderer).setupLightingZNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.NORTH.ordinal(), mBlock.colorMultiplier(getBlockAccess(aRenderer), aX, aY, aZ));
        aRenderer.renderFaceZNeg(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        aRenderer.field_152631_f = false;
    }

    @Override
    public boolean isValidTexture() {
        return mBlock != null;
    }

    @Override
    public Block getBlock() {
        return mBlock;
    }

    @Override
    public byte getMeta() {
        return mMeta;
    }
}
