package gregtech.common.render;

import static gregtech.api.enums.Mods.Angelica;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.prupe.mcpatcher.ctm.CTMUtils;

import gregtech.api.interfaces.IBlockContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.LightingHelper;

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
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        final IIcon aIcon = getIcon(ForgeDirection.EAST.ordinal(), aRenderer.blockAccess, aX, aY, aZ);
        aRenderer.field_152631_f = true;
        startDrawingQuads(aRenderer, 1.0f, 0.0f, 0.0f);
        new LightingHelper(aRenderer).setupLightingXPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.EAST, 0xffffff);
        aRenderer.renderFaceXPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
        aRenderer.field_152631_f = false;
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, -1.0f, 0.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.WEST.ordinal(), aRenderer.blockAccess, aX, aY, aZ);
        new LightingHelper(aRenderer).setupLightingXNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.WEST, 0xffffff);
        aRenderer.renderFaceXNeg(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.UP.ordinal(), aRenderer.blockAccess, aX, aY, aZ);
        new LightingHelper(aRenderer).setupLightingYPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.UP, 0xffffff);
        aRenderer.renderFaceYPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, -1.0f, 0.0f);
        final IIcon aIcon = getIcon(ForgeDirection.DOWN.ordinal(), aRenderer.blockAccess, aX, aY, aZ);
        new LightingHelper(aRenderer).setupLightingYNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.DOWN, 0xffffff);
        aRenderer.renderFaceYNeg(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, 1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.SOUTH.ordinal(), aRenderer.blockAccess, aX, aY, aZ);
        new LightingHelper(aRenderer).setupLightingZPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.SOUTH, 0xffffff);
        aRenderer.renderFaceZPos(aBlock, aX, aY, aZ, aIcon);
        draw(aRenderer);
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, -1.0f);
        final IIcon aIcon = getIcon(ForgeDirection.NORTH.ordinal(), aRenderer.blockAccess, aX, aY, aZ);
        aRenderer.field_152631_f = true;
        new LightingHelper(aRenderer).setupLightingZNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.NORTH, 0xffffff);
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
    public int getMeta() {
        return mMeta;
    }
}
