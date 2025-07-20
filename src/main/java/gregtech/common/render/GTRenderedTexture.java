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

import gregtech.GTMod;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.LightingHelper;

public class GTRenderedTexture extends GTTextureBase implements ITexture, IColorModulationContainer {

    protected final IIconContainer mIconContainer;
    private final short[] mRGBa;
    private final boolean glow;
    private final boolean stdOrient;
    private final boolean useExtFacing;

    protected GTRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean glow, boolean stdOrient,
        boolean extFacing) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTRenderedTexture");
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
            if (!GTMod.proxy.mRenderGlowTextures) {
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
            if (!GTMod.proxy.mRenderGlowTextures) {
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
            if (!GTMod.proxy.mRenderGlowTextures) {
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
            if (!GTMod.proxy.mRenderGlowTextures) {
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
            if (!GTMod.proxy.mRenderGlowTextures) {
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
            if (!GTMod.proxy.mRenderGlowTextures) {
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

        aRenderer.uvRotateBottom = getRotation(extendedFacing);
        icon = getFlipped(ForgeDirection.DOWN, extendedFacing, icon);

        aRenderer.renderFaceYNeg(Blocks.air, x, y, z, icon);

        aRenderer.uvRotateBottom = 0;
    }

    /**
     * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceYPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        aRenderer.uvRotateTop = getRotation(extendedFacing);
        icon = getFlipped(ForgeDirection.UP, extendedFacing, icon);

        aRenderer.renderFaceYPos(Blocks.air, x, y, z, icon);

        aRenderer.uvRotateTop = 0;
    }

    /**
     * Renders the given texture to the north (z-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        aRenderer.uvRotateEast = getRotation(extendedFacing);
        aRenderer.field_152631_f = true;
        icon = getFlipped(ForgeDirection.NORTH, extendedFacing, icon);

        aRenderer.renderFaceZNeg(Blocks.air, x, y, z, icon);

        aRenderer.uvRotateEast = 0;
        aRenderer.field_152631_f = false;
    }

    /**
     * Renders the given texture to the south (z-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        aRenderer.uvRotateWest = getRotation(extendedFacing);
        icon = getFlipped(ForgeDirection.SOUTH, extendedFacing, icon);

        aRenderer.renderFaceZPos(Blocks.air, x, y, z, icon);

        aRenderer.uvRotateWest = 0;
    }

    /**
     * Renders the given texture to the west (x-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        aRenderer.uvRotateNorth = getRotation(extendedFacing);
        icon = getFlipped(ForgeDirection.WEST, extendedFacing, icon);

        aRenderer.renderFaceXNeg(Blocks.air, x, y, z, icon);

        aRenderer.uvRotateNorth = 0;
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon,
        ExtendedFacing extendedFacing) {

        aRenderer.uvRotateSouth = getRotation(extendedFacing);
        aRenderer.field_152631_f = true;
        icon = getFlipped(ForgeDirection.EAST, extendedFacing, icon);

        aRenderer.renderFaceXPos(Blocks.air, x, y, z, icon);

        aRenderer.uvRotateSouth = 0;
        aRenderer.field_152631_f = false;
    }

    private static final int NORMAL = 0;
    private static final int CLOCKWISE = 1;
    private static final int COUNTER_CLOCKWISE = 2;
    private static final int UPSIDE_DOWN = 3;

    private int getRotation(ExtendedFacing extendedFacing) {
        // if we aren't rendering a multi, don't rotate at all
        if (extendedFacing == null) return NORMAL;

        return switch (extendedFacing.getRotation()) {
            case NORMAL -> NORMAL;
            case CLOCKWISE -> CLOCKWISE;
            case UPSIDE_DOWN -> UPSIDE_DOWN;
            case COUNTER_CLOCKWISE -> COUNTER_CLOCKWISE;
        };
    }

    private IIcon getFlipped(ForgeDirection side, ExtendedFacing extendedFacing, IIcon icon) {

        boolean flipU = false, flipV = false;

        // if we aren't rendering a multi, don't flip at all
        if (extendedFacing == null) {
            if (side == ForgeDirection.EAST || side == ForgeDirection.DOWN) {
                flipU ^= true;
            }

            if (side == ForgeDirection.NORTH) {
                flipV = true;
            }

            return new GTIconFlipped(icon, flipU, flipV);
        }

        ForgeDirection dir = extendedFacing.getDirection();

        // certain directions need to be flipped horizontally seemingly randomly
        // maybe there's a reason, maybe there isn't, I haven't bothered to dig into the code to figure out why

        // spotless:off
        if (extendedFacing.getRotation().getIndex() % 2 == 0) {
            // flip U's if normal or upside down
            flipU ^= extendedFacing.getFlip().isHorizontallyFlipped();
        } else {
            // flip V's if clockwise or counter-clockwise
            flipV ^= extendedFacing.getFlip().isHorizontallyFlipped();

            if (side == ForgeDirection.EAST || side == ForgeDirection.NORTH) {
                flipU ^= true;
            }
        }
        // spotless:on

        if (side == ForgeDirection.DOWN) {
            flipU ^= true;
        }

        return new GTIconFlipped(icon, flipU, flipV);
    }

    private ExtendedFacing getExtendedFacing(int x, int y, int z) {
        if (stdOrient || !useExtFacing) return ExtendedFacing.DEFAULT;

        final EntityPlayer player = GTMod.proxy.getThePlayer();
        if (player == null) return ExtendedFacing.DEFAULT;

        final World w = player.getEntityWorld();
        if (w == null) return ExtendedFacing.DEFAULT;

        final TileEntity te = w.getTileEntity(x, y, z);

        IAlignment alignment = null;

        if (te instanceof IGregTechTileEntity) {
            final IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();

            if (meta instanceof IAlignmentProvider) {
                alignment = ((IAlignmentProvider) meta).getAlignment();
            }
        } else if (te instanceof IAlignmentProvider) {
            alignment = ((IAlignmentProvider) te).getAlignment();
        }

        if (alignment != null) return alignment.getExtendedFacing();

        // We don't want to rotate the textures if this block isn't a multi
        return null;
    }
}
