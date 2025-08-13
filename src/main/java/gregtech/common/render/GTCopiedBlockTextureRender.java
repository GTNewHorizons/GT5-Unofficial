package gregtech.common.render;

import static gregtech.api.enums.Mods.Angelica;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import com.prupe.mcpatcher.ctm.CTMUtils;

import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.SBRContextBase;
import gregtech.api.render.SBRWorldContext;

public class GTCopiedBlockTextureRender extends GTTextureBase implements ITexture, IBlockContainer {

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

    private IIcon getIcon(int ordinalSide, SBRContextBase<? extends SBRContextBase<?>> ctx) {
        final IIcon icon;
        if (mSide == 6) icon = mBlock.getIcon(ordinalSide, mMeta);
        else icon = mBlock.getIcon(mSide, mMeta);
        if (!Angelica.isModLoaded()) return icon;
        else return ctx instanceof SBRWorldContext ctxW
            ? CTMUtils.getBlockIcon(icon, mBlock, ctxW.world, ctxW.x, ctxW.y, ctxW.z, ordinalSide)
            : CTMUtils.getBlockIcon(icon, mBlock, ordinalSide);
    }

    @Override
    public void renderXPos(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        final IIcon aIcon = getIcon(ForgeDirection.EAST.ordinal(), ctx);
        ctx.renderer.field_152631_f = true;
        startDrawingQuads(ctx.renderer, 1.0f, 0.0f, 0.0f);
        ctx.reset()
            .setupColor(ForgeDirection.EAST, 0xffffff);
        ctx.renderer.renderFaceXPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
        ctx.renderer.field_152631_f = false;
    }

    @Override
    public void renderXNeg(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, -1.0f, 0.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.WEST.ordinal(), ctx);
        ctx.reset()
            .setupColor(ForgeDirection.WEST, 0xffffff);
        ctx.renderer.renderFaceXNeg(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderYPos(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.UP.ordinal(), ctx);
        ctx.reset()
            .setupColor(ForgeDirection.UP, 0xffffff);
        ctx.renderer.renderFaceYPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderYNeg(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, -1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.DOWN.ordinal(), ctx);
        ctx.reset()
            .setupColor(ForgeDirection.DOWN, 0xffffff);
        ctx.renderer.renderFaceYNeg(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderZPos(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 0.0f, 1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.SOUTH.ordinal(), ctx);
        ctx.reset()
            .setupColor(ForgeDirection.SOUTH, 0xffffff);
        ctx.renderer.renderFaceZPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderZNeg(SBRContextBase<? extends SBRContextBase<?>> ctx) {
        if (!ctx.canRenderInPass(mBlock::canRenderInPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 0.0f, -1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.NORTH.ordinal(), ctx);
        ctx.renderer.field_152631_f = true;
        ctx.reset()
            .setupColor(ForgeDirection.NORTH, 0xffffff);
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
