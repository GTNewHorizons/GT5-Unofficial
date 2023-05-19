package gregtech.common.render;

import static gregtech.api.util.LightingHelper.MAX_BRIGHTNESS;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;

import gregtech.GT_Mod;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.LightingHelper;

public class GT_RenderedTexture extends GT_TextureBase implements ITexture, IColorModulationContainer {

    protected final IIconContainer mIconContainer;
    private final short[] mRGBa;
    private final boolean glow;
    private final boolean stdOrient;
    private final boolean useExtFacing;

    protected GT_RenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean allowAlpha, boolean glow,
        boolean stdOrient, boolean extFacing) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_RenderedTexture");
        mIconContainer = aIcon;
        mRGBa = aRGBa;
        this.glow = glow;
        this.stdOrient = stdOrient;
        this.useExtFacing = extFacing;
    }

    @Override
    public boolean isOldTexture() {
        return false;
    }

    @Override
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 1.0f, 0.0f, 0.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GT_Mod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            if (enableAO) lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingXPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.EAST, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceXPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.EAST, 0xffffff);
            renderFaceXPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, -1.0f, 0.0f, 0.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GT_Mod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingXNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.WEST, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceXNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.WEST, 0xffffff);
            renderFaceXNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 1.0f, 0.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GT_Mod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingYPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.UP, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceYPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.UP, 0xffffff);
            renderFaceYPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, -1.0f, 0.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GT_Mod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingYNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.DOWN, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceYNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            Tessellator.instance.setColorRGBA(255, 255, 255, 255);
            renderFaceYNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, 1.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GT_Mod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingZPos(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.SOUTH, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceZPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.SOUTH, 0xffffff);
            renderFaceZPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        startDrawingQuads(aRenderer, 0.0f, 0.0f, -1.0f);
        final boolean enableAO = aRenderer.enableAO;
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (glow) {
            if (!GT_Mod.gregtechproxy.mRenderGlowTextures) {
                draw(aRenderer);
                return;
            }
            aRenderer.enableAO = false;
            lighting.setLightnessOverride(1.0F);
            lighting.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        lighting.setupLightingZNeg(aBlock, aX, aY, aZ)
            .setupColor(ForgeDirection.NORTH, mRGBa);
        final ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceZNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            lighting.setupColor(ForgeDirection.NORTH, 0xffffff);
            renderFaceZNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
        aRenderer.enableAO = enableAO;
        draw(aRenderer);
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }

    @Override
    public boolean isValidTexture() {
        return mIconContainer != null;
    }

    /**
     * Renders the given texture to the bottom face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceYNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        switch (useExtFacing ? extendedFacing.getRotation() : Rotation.NORMAL) {
            case COUNTER_CLOCKWISE -> aRenderer.uvRotateBottom = 2;
            case CLOCKWISE -> aRenderer.uvRotateBottom = 1;
            case UPSIDE_DOWN -> aRenderer.uvRotateBottom = 3;
            default -> aRenderer.uvRotateBottom = 0;
        }

        final Flip aFlip = extendedFacing.getFlip();
        aRenderer.renderFaceYNeg(
            Blocks.air,
            x,
            y,
            z,
            useExtFacing && GT_Mod.gregtechproxy.mRenderFlippedMachinesFlipped
                ? new GT_IconFlipped(icon, aFlip.isHorizontallyFlipped() ^ !stdOrient, aFlip.isVerticallyFliped())
                : new GT_IconFlipped(icon, !stdOrient, false));
        aRenderer.uvRotateBottom = 0;
    }

    /**
     * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceYPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        switch (useExtFacing ? extendedFacing.getRotation() : Rotation.NORMAL) {
            case COUNTER_CLOCKWISE -> aRenderer.uvRotateTop = 2;
            case CLOCKWISE -> aRenderer.uvRotateTop = 1;
            case UPSIDE_DOWN -> aRenderer.uvRotateTop = 3;
            default -> aRenderer.uvRotateTop = 0;
        }

        final Flip aFlip = extendedFacing.getFlip();
        aRenderer.renderFaceYPos(
            Blocks.air,
            x,
            y,
            z,
            useExtFacing && GT_Mod.gregtechproxy.mRenderFlippedMachinesFlipped
                ? new GT_IconFlipped(icon, aFlip.isHorizontallyFlipped(), aFlip.isVerticallyFliped())
                : icon);
        aRenderer.uvRotateTop = 0;
    }

    /**
     * Renders the given texture to the north (z-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {
        aRenderer.field_152631_f = true;
        // **NOT A BUG**: aRenderer.uvRotateEast REALLY CONTROLS THE ROTATION OF THE NORTH SIDE
        switch (useExtFacing ? extendedFacing.getRotation() : Rotation.NORMAL) {
            case COUNTER_CLOCKWISE -> aRenderer.uvRotateEast = 2;
            case CLOCKWISE -> aRenderer.uvRotateEast = 1;
            case UPSIDE_DOWN -> aRenderer.uvRotateEast = 3;
            default -> aRenderer.uvRotateEast = 0;
        }

        final Flip aFlip = extendedFacing.getFlip();
        aRenderer.renderFaceZNeg(
            Blocks.air,
            x,
            y,
            z,
            useExtFacing && GT_Mod.gregtechproxy.mRenderFlippedMachinesFlipped
                ? new GT_IconFlipped(icon, aFlip.isHorizontallyFlipped(), aFlip.isVerticallyFliped())
                : icon);
        aRenderer.uvRotateEast = 0;
        aRenderer.field_152631_f = false;
    }

    /**
     * Renders the given texture to the south (z-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {
        // **NOT A BUG**: aRenderer.uvRotateWest REALLY CONTROLS THE ROTATION OF THE SOUTH SIDE
        switch (useExtFacing ? extendedFacing.getRotation() : Rotation.NORMAL) {
            case COUNTER_CLOCKWISE -> aRenderer.uvRotateWest = 2;
            case CLOCKWISE -> aRenderer.uvRotateWest = 1;
            case UPSIDE_DOWN -> aRenderer.uvRotateWest = 3;
            default -> aRenderer.uvRotateWest = 0;
        }

        final Flip aFlip = extendedFacing.getFlip();
        aRenderer.renderFaceZPos(
            Blocks.air,
            x,
            y,
            z,
            useExtFacing && GT_Mod.gregtechproxy.mRenderFlippedMachinesFlipped
                ? new GT_IconFlipped(icon, aFlip.isHorizontallyFlipped(), aFlip.isVerticallyFliped())
                : icon);
        aRenderer.uvRotateWest = 0;
    }

    /**
     * Renders the given texture to the west (x-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {
        // **NOT A BUG**: aRenderer.uvRotateNorth REALLY CONTROLS THE ROTATION OF THE WEST SIDE
        switch (useExtFacing ? extendedFacing.getRotation() : Rotation.NORMAL) {
            case COUNTER_CLOCKWISE -> aRenderer.uvRotateNorth = 2;
            case CLOCKWISE -> aRenderer.uvRotateNorth = 1;
            case UPSIDE_DOWN -> aRenderer.uvRotateNorth = 3;
            default -> aRenderer.uvRotateNorth = 0;
        }

        final Flip aFlip = extendedFacing.getFlip();
        aRenderer.renderFaceXNeg(
            Blocks.air,
            x,
            y,
            z,
            useExtFacing && GT_Mod.gregtechproxy.mRenderFlippedMachinesFlipped
                ? new GT_IconFlipped(icon, aFlip.isHorizontallyFlipped(), aFlip.isVerticallyFliped())
                : icon);
        aRenderer.uvRotateNorth = 0;
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {
        aRenderer.field_152631_f = true;
        // **NOT A BUG**: aRenderer.uvRotateSouth REALLY CONTROLS THE ROTATION OF THE EAST SIDE
        switch (useExtFacing ? extendedFacing.getRotation() : Rotation.NORMAL) {
            case COUNTER_CLOCKWISE -> aRenderer.uvRotateSouth = 2;
            case CLOCKWISE -> aRenderer.uvRotateSouth = 1;
            case UPSIDE_DOWN -> aRenderer.uvRotateSouth = 3;
            default -> aRenderer.uvRotateSouth = 0;
        }

        final Flip aFlip = extendedFacing.getFlip();
        aRenderer.renderFaceXPos(
            Blocks.air,
            x,
            y,
            z,
            useExtFacing && GT_Mod.gregtechproxy.mRenderFlippedMachinesFlipped
                ? new GT_IconFlipped(icon, aFlip.isHorizontallyFlipped(), aFlip.isVerticallyFliped())
                : icon);
        aRenderer.uvRotateSouth = 0;
        aRenderer.field_152631_f = false;
    }

    private ExtendedFacing getExtendedFacing(int x, int y, int z) {
        if (stdOrient) return ExtendedFacing.DEFAULT;
        final EntityPlayer player = GT_Mod.gregtechproxy.getThePlayer();
        if (player == null) return ExtendedFacing.DEFAULT;
        final World w = player.getEntityWorld();
        if (w == null) return ExtendedFacing.DEFAULT;
        final TileEntity te = w.getTileEntity(x, y, z);
        IAlignment alignment = null;
        if (te instanceof IGregTechTileEntity) {
            final IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
            if (meta instanceof IAlignmentProvider) {
                alignment = ((IAlignmentProvider) meta).getAlignment();
            } else if (meta != null) {
                return ExtendedFacing.of(
                    meta.getBaseMetaTileEntity()
                        .getFrontFacing());
            }
        } else if (te instanceof IAlignmentProvider) {
            alignment = ((IAlignmentProvider) te).getAlignment();
        }
        if (alignment != null) return alignment.getExtendedFacing();
        return ExtendedFacing.DEFAULT;
    }
}
