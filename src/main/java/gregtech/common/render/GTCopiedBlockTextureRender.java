package gregtech.common.render;

import static gregtech.api.enums.Mods.Angelica;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.prupe.mcpatcher.ctm.CTMUtils;

import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.SBRContext;

public class GTCopiedBlockTextureRender extends GTTextureBase implements ITexture, IBlockContainer {

    private final Block mBlock;
    private final byte mSide;
    private final int mMeta;

    protected GTCopiedBlockTextureRender(Block aBlock, int ordinalSide, int aMeta, short[] aRGBa, boolean allowAlpha) {
        if (aRGBa.length != 4)
            throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTCopiedBlockTextureRender");
        mBlock = aBlock;
        mSide = (byte) ordinalSide;
        mMeta = aMeta;
    }

    @Override
    public boolean isOldTexture() {
        return false;
    }

    private IIcon getIcon(int ordinalSide, IBlockAccess access, int x, int y, int z) {
        IIcon icon;
        if (mSide == 6) icon = mBlock.getIcon(ordinalSide, mMeta);
        else icon = mBlock.getIcon(mSide, mMeta);
        if (!Angelica.isModLoaded()) return icon;
        else {
            return CTMUtils.getBlockIcon(icon, mBlock, access, x, y, z, ordinalSide);
        }
    }

    @Override
    public void renderXPos(SBRContext ctx) {
        if (ctx.worldRenderPass != -1 && !mBlock.canRenderInPass(ctx.worldRenderPass)) return;
        final IIcon aIcon = getIcon(ForgeDirection.EAST.ordinal(), ctx.world, ctx.x, ctx.y, ctx.z);
        ctx.renderer.field_152631_f = true;
        startDrawingQuads(ctx.renderer, 1.0f, 0.0f, 0.0f);
        ctx.reset()
            .setupLightingXPos()
            .setupColor(ForgeDirection.EAST, 0xffffff);
        ctx.renderer.renderFaceXPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
        ctx.renderer.field_152631_f = false;
    }

    @Override
    public void renderXNeg(SBRContext ctx) {
        if (ctx.worldRenderPass != -1 && !mBlock.canRenderInPass(ctx.worldRenderPass)) return;
        startDrawingQuads(ctx.renderer, -1.0f, 0.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.WEST.ordinal(), ctx.world, ctx.x, ctx.y, ctx.z);
        ctx.reset()
            .setupLightingXNeg()
            .setupColor(ForgeDirection.WEST, 0xffffff);
        ctx.renderer.renderFaceXNeg(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderYPos(SBRContext ctx) {
        if (ctx.worldRenderPass != -1 && !mBlock.canRenderInPass(ctx.worldRenderPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.UP.ordinal(), ctx.world, ctx.x, ctx.y, ctx.z);
        ctx.reset()
            .setupLightingYPos()
            .setupColor(ForgeDirection.UP, 0xffffff);
        ctx.renderer.renderFaceYPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderYNeg(SBRContext ctx) {
        if (ctx.worldRenderPass != -1 && !mBlock.canRenderInPass(ctx.worldRenderPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, -1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.DOWN.ordinal(), ctx.world, ctx.x, ctx.y, ctx.z);
        ctx.reset()
            .setupLightingYNeg()
            .setupColor(ForgeDirection.DOWN, 0xffffff);
        ctx.renderer.renderFaceYNeg(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderZPos(SBRContext ctx) {
        if (ctx.worldRenderPass != -1 && !mBlock.canRenderInPass(ctx.worldRenderPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 0.0f, 1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.SOUTH.ordinal(), ctx.world, ctx.x, ctx.y, ctx.z);
        ctx.reset()
            .setupLightingZPos()
            .setupColor(ForgeDirection.SOUTH, 0xffffff);
        ctx.renderer.renderFaceZPos(ctx.block, ctx.x, ctx.y, ctx.z, aIcon);
        draw(ctx.renderer);
    }

    @Override
    public void renderZNeg(SBRContext ctx) {
        if (ctx.worldRenderPass != -1 && !mBlock.canRenderInPass(ctx.worldRenderPass)) return;
        startDrawingQuads(ctx.renderer, 0.0f, 0.0f, -1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.NORTH.ordinal(), ctx.world, ctx.x, ctx.y, ctx.z);
        ctx.renderer.field_152631_f = true;
        ctx.reset()
            .setupLightingZNeg()
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
