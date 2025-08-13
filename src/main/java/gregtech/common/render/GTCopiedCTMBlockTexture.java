package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.SBRContextBase;
import gregtech.api.util.GTRenderingWorld;

class GTCopiedCTMBlockTexture extends GTTextureBase implements ITexture, IBlockContainer {

    private final Block mBlock;
    private final byte mSide;
    private final int mMeta;

    GTCopiedCTMBlockTexture(Block aBlock, int ordinalSide, int aMeta, short[] aRGBa, boolean allowAlpha) {
        if (aRGBa.length != 4)
            throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTCopiedCTMBlockTexture");
        mBlock = aBlock;
        mSide = (byte) ordinalSide;
        mMeta = aMeta;
    }

    private IIcon getIcon(int ordinalSide, int aX, int aY, int aZ, RenderBlocks aRenderer) {
        final int tSide = mSide == 6 ? ordinalSide : mSide;
        return mBlock.getIcon(getBlockAccess(aRenderer), aX, aY, aZ, tSide);
    }

    private GTRenderingWorld getBlockAccess(RenderBlocks aRenderer) {
        return GTRenderingWorld.getInstance(aRenderer.blockAccess);
    }

    @Override
    public void renderXPos(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final IIcon aIcon = getIcon(ForgeDirection.EAST.ordinal(), ctx.x, ctx.y, ctx.z, ctx.renderer);
        ctx.renderer.field_152631_f = true;
        startDrawingQuads(ctx.renderer, 1.0f, 0.0f, 0.0f);
        ctx.reset()
            .setupColor(ForgeDirection.EAST, mBlock.colorMultiplier(getBlockAccess(ctx.renderer), ctx.x, ctx.y, ctx.z));
        ctx.renderer.renderFaceXPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
        ctx.renderer.field_152631_f = false;
    }

    @Override
    public void renderXNeg(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, -1.0f, 0.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.WEST.ordinal(), ctx.x, ctx.y, ctx.z, ctx.renderer);
        ctx.reset()
            .setupColor(ForgeDirection.WEST, mBlock.colorMultiplier(getBlockAccess(ctx.renderer), ctx.x, ctx.y, ctx.z));
        ctx.renderer.renderFaceXNeg(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderYPos(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.UP.ordinal(), ctx.x, ctx.y, ctx.z, ctx.renderer);
        ctx.reset()
            .setupColor(ForgeDirection.UP, mBlock.colorMultiplier(getBlockAccess(ctx.renderer), ctx.x, ctx.y, ctx.z));
        ctx.renderer.renderFaceYPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderYNeg(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, -1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.DOWN.ordinal(), ctx.x, ctx.y, ctx.z, ctx.renderer);
        ctx.reset()
            .setupColor(ForgeDirection.DOWN, mBlock.colorMultiplier(getBlockAccess(ctx.renderer), ctx.x, ctx.y, ctx.z));
        ctx.renderer.renderFaceYNeg(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderZPos(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 0.0f, 1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.SOUTH.ordinal(), ctx.x, ctx.y, ctx.z, ctx.renderer);
        ctx.reset()
            .setupColor(
                ForgeDirection.SOUTH,
                mBlock.colorMultiplier(getBlockAccess(ctx.renderer), ctx.x, ctx.y, ctx.z));
        ctx.renderer.renderFaceZPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderZNeg(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 0.0f, -1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.NORTH.ordinal(), ctx.x, ctx.y, ctx.z, ctx.renderer);
        ctx.renderer.field_152631_f = true;
        ctx.reset()
            .setupColor(
                ForgeDirection.NORTH,
                mBlock.colorMultiplier(getBlockAccess(ctx.renderer), ctx.x, ctx.y, ctx.z));
        ctx.renderer.renderFaceZNeg(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
        ctx.renderer.field_152631_f = false;
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
    public int getMeta() {
        return mMeta;
    }
}
