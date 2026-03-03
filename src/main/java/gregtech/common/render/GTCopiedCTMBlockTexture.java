package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ISBRContext;
import gregtech.api.util.GTRenderingWorld;

class GTCopiedCTMBlockTexture extends GTTextureBase implements ITexture, IBlockContainer {

    private final Block mBlock;
    private final byte mSide;
    private final int mMeta;

    public GTCopiedCTMBlockTexture(Block aBlock, int ordinalSide, int aMeta, short[] aRGBa) {
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

    // spotless:off
    @Override
    public void renderYNeg(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, -1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.DOWN.ordinal(), ctx.getX(), ctx.getY(), ctx.getZ(), renderBlocks);
        ctx.reset().setupColor(ForgeDirection.DOWN, mBlock.colorMultiplier(getBlockAccess(renderBlocks), ctx.getX(), ctx.getY(), ctx.getZ()));
        renderBlocks.renderFaceYNeg(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.UP.ordinal(), ctx.getX(), ctx.getY(), ctx.getZ(), renderBlocks);
        ctx.reset().setupColor(ForgeDirection.UP, mBlock.colorMultiplier(getBlockAccess(renderBlocks), ctx.getX(), ctx.getY(), ctx.getZ()));
        renderBlocks.renderFaceYPos(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 0.0f, -1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.NORTH.ordinal(), ctx.getX(), ctx.getY(), ctx.getZ(), renderBlocks);
        renderBlocks.field_152631_f = true;
        ctx.reset().setupColor(ForgeDirection.NORTH, mBlock.colorMultiplier(getBlockAccess(renderBlocks), ctx.getX(), ctx.getY(), ctx.getZ()));
        renderBlocks.renderFaceZNeg(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
        renderBlocks.field_152631_f = false;
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 0.0f, 1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.SOUTH.ordinal(), ctx.getX(), ctx.getY(), ctx.getZ(), renderBlocks);
        ctx.reset().setupColor(ForgeDirection.SOUTH, mBlock.colorMultiplier(getBlockAccess(renderBlocks), ctx.getX(), ctx.getY(), ctx.getZ()));
        renderBlocks.renderFaceZPos(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, -1.0f, 0.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.WEST.ordinal(), ctx.getX(), ctx.getY(), ctx.getZ(), renderBlocks);
        ctx.reset().setupColor(ForgeDirection.WEST, mBlock.colorMultiplier(getBlockAccess(renderBlocks), ctx.getX(), ctx.getY(), ctx.getZ()));
        renderBlocks.renderFaceXNeg(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
    }

    @Override
    public void renderXPos(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final IIcon aIcon = getIcon(ForgeDirection.EAST.ordinal(), ctx.getX(), ctx.getY(), ctx.getZ(), renderBlocks);
        renderBlocks.field_152631_f = true;
        startDrawingQuads(renderBlocks, 1.0f, 0.0f, 0.0f);
        ctx.reset().setupColor(ForgeDirection.EAST, mBlock.colorMultiplier(getBlockAccess(renderBlocks), ctx.getX(), ctx.getY(), ctx.getZ()));
        renderBlocks.renderFaceXPos(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
        renderBlocks.field_152631_f = false;
    }
    //spotless:on

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
