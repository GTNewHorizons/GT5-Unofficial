package gregtech.common.render;

import static gregtech.api.enums.Mods.Angelica;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.prupe.mcpatcher.ctm.CTMUtils;

import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.render.ISBRContext;
import gregtech.api.render.ISBRWorldContext;

public class GTCopiedBlockTextureRender extends GTTextureBase implements IBlockContainer {

    private static final double COMPACT_CTM_LAYER_OFFSET = 0.001D;

    private final Block mBlock;
    private final byte mSide;
    private final int mMeta;

    protected GTCopiedBlockTextureRender(Block aBlock, int ordinalSide, int aMeta, short[] aRGBa) {
        if (aRGBa.length != 4)
            throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTCopiedBlockTextureRender");
        mBlock = aBlock;
        mSide = (byte) ordinalSide;
        mMeta = aMeta;
    }

    public IIcon getIcon(int ordinalSide, ISBRContext ctx) {
        final IIcon icon;
        if (mSide == 6) icon = mBlock.getIcon(ordinalSide, mMeta);
        else icon = mBlock.getIcon(mSide, mMeta);
        if (!Angelica.isModLoaded()) return icon;
        else return ctx instanceof ISBRWorldContext ctxW
            ? CTMUtils
                .getBlockIcon(icon, mBlock, ctxW.getBlockAccess(), ctxW.getX(), ctxW.getY(), ctxW.getZ(), ordinalSide)
            : icon;
    }

    @Override
    public void renderXPos(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final IIcon aIcon = getIcon(ForgeDirection.EAST.ordinal(), ctx);
        boolean usesCompactCtm = usesCompactCtm(ctx);
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        renderBlocks.field_152631_f = true;
        startDrawingQuads(renderBlocks, 1.0f, 0.0f, 0.0f);
        ctx.reset()
            .setupColor(ForgeDirection.EAST, 0xffffff);
        renderBlocks.renderFaceXPos(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
        if (usesCompactCtm) renderBlocks.renderMaxX += COMPACT_CTM_LAYER_OFFSET;
        renderBlocks.field_152631_f = false;
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, -1.0f, 0.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.WEST.ordinal(), ctx);
        boolean usesCompactCtm = usesCompactCtm(ctx);
        ctx.reset()
            .setupColor(ForgeDirection.WEST, 0xffffff);
        renderBlocks.renderFaceXNeg(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
        if (usesCompactCtm) renderBlocks.renderMinX -= COMPACT_CTM_LAYER_OFFSET;
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.UP.ordinal(), ctx);
        boolean usesCompactCtm = usesCompactCtm(ctx);
        ctx.reset()
            .setupColor(ForgeDirection.UP, 0xffffff);
        renderBlocks.renderFaceYPos(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
        if (usesCompactCtm) renderBlocks.renderMaxY += COMPACT_CTM_LAYER_OFFSET;
    }

    @Override
    public void renderYNeg(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, -1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.DOWN.ordinal(), ctx);
        boolean usesCompactCtm = usesCompactCtm(ctx);
        ctx.reset()
            .setupColor(ForgeDirection.DOWN, 0xffffff);
        renderBlocks.renderFaceYNeg(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
        if (usesCompactCtm) renderBlocks.renderMinY -= COMPACT_CTM_LAYER_OFFSET;
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 0.0f, 1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.SOUTH.ordinal(), ctx);
        boolean usesCompactCtm = usesCompactCtm(ctx);
        ctx.reset()
            .setupColor(ForgeDirection.SOUTH, 0xffffff);
        renderBlocks.renderFaceZPos(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
        if (usesCompactCtm) renderBlocks.renderMaxZ += COMPACT_CTM_LAYER_OFFSET;
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 0.0f, -1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.NORTH.ordinal(), ctx);
        boolean usesCompactCtm = usesCompactCtm(ctx);
        renderBlocks.field_152631_f = true;
        ctx.reset()
            .setupColor(ForgeDirection.NORTH, 0xffffff);
        renderBlocks.renderFaceZNeg(ctx.getBlock(), ctx.getX(), ctx.getY(), ctx.getZ(), aIcon);
        draw(renderBlocks);
        renderBlocks.field_152631_f = false;
        if (usesCompactCtm) renderBlocks.renderMinZ -= COMPACT_CTM_LAYER_OFFSET;
    }

    private static boolean usesCompactCtm(ISBRContext ctx) {
        if (!Angelica.isModLoaded()) return false;
        return ctx instanceof ISBRWorldContext && CTMUtils.getCurrentCompact() != null;
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
