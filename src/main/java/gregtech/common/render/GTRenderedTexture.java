package gregtech.common.render;

import static gregtech.api.enums.Textures.InvisibleIcon.*;
import static gregtech.api.render.ISBRWorldContext.MAX_BRIGHTNESS;

import net.minecraft.client.renderer.RenderBlocks;
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
import gregtech.api.render.ISBRContext;

public class GTRenderedTexture extends GTTextureBase implements ITexture, IColorModulationContainer {

    protected final IIconContainer mIconContainer;
    private final short[] mRGBa;
    private final boolean glow;
    private final boolean useExtFacing;

    protected GTRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean glow, boolean extFacing) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTRenderedTexture");
        mIconContainer = aIcon;
        mRGBa = aRGBa;
        this.glow = glow;
        this.useExtFacing = extFacing;
    }

    @Override
    public void renderXPos(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 1.0f, 0.0f, 0.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                draw(renderBlocks);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = mIconContainer.getIcon();
        if (icon != INVISIBLE_ICON && ctx.canRenderInPass(mIconContainer::canRenderInPass)) {
            ctx.setupColor(ForgeDirection.EAST, mRGBa);
            renderFaceXPos(ctx, mIconContainer.getIcon(), rotation);
        }
        final IIcon overlayIcon = mIconContainer.getOverlayIcon();
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && ctx.canRenderInPass(pass -> pass == 1)) {
            ctx.setupColor(ForgeDirection.EAST, 0xffffff);
            renderFaceXPos(ctx, overlayIcon, rotation);
        }
        renderBlocks.enableAO = enableAO;
        draw(renderBlocks);
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, -1.0f, 0.0f, 0.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                draw(renderBlocks);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = mIconContainer.getIcon();
        if (icon != INVISIBLE_ICON && ctx.canRenderInPass(mIconContainer::canRenderInPass)) {
            ctx.setupColor(ForgeDirection.WEST, mRGBa);
            renderFaceXNeg(ctx, mIconContainer.getIcon(), rotation);
        }
        final IIcon overlayIcon = mIconContainer.getOverlayIcon();
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && ctx.canRenderInPass(pass -> pass == 1)) {
            ctx.setupColor(ForgeDirection.WEST, 0xffffff);
            renderFaceXNeg(ctx, overlayIcon, rotation);
        }
        renderBlocks.enableAO = enableAO;
        draw(renderBlocks);
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 1.0f, 0.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                draw(renderBlocks);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = mIconContainer.getIcon();
        if (icon != INVISIBLE_ICON && ctx.canRenderInPass(mIconContainer::canRenderInPass)) {
            ctx.setupColor(ForgeDirection.UP, mRGBa);
            renderFaceYPos(ctx, mIconContainer.getIcon(), rotation);
        }
        final IIcon overlayIcon = mIconContainer.getOverlayIcon();
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && ctx.canRenderInPass(pass -> pass == 1)) {
            ctx.setupColor(ForgeDirection.UP, 0xffffff);
            renderFaceYPos(ctx, overlayIcon, rotation);
        }
        renderBlocks.enableAO = enableAO;
        draw(renderBlocks);
    }

    @Override
    public void renderYNeg(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, -1.0f, 0.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                draw(renderBlocks);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = mIconContainer.getIcon();
        if (icon != INVISIBLE_ICON && ctx.canRenderInPass(mIconContainer::canRenderInPass)) {
            ctx.setupColor(ForgeDirection.DOWN, mRGBa);
            renderFaceYNeg(ctx, mIconContainer.getIcon(), rotation);
        }
        final IIcon overlayIcon = mIconContainer.getOverlayIcon();
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && ctx.canRenderInPass(pass -> pass == 1)) {
            ctx.setupColor(ForgeDirection.DOWN, 0xffffff);
            renderFaceYNeg(ctx, overlayIcon, rotation);
        }
        renderBlocks.enableAO = enableAO;
        draw(renderBlocks);
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 0.0f, 1.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                draw(renderBlocks);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = mIconContainer.getIcon();
        if (icon != INVISIBLE_ICON && ctx.canRenderInPass(mIconContainer::canRenderInPass)) {
            ctx.setupColor(ForgeDirection.SOUTH, mRGBa);
            renderFaceZPos(ctx, mIconContainer.getIcon(), rotation);
        }
        final IIcon overlayIcon = mIconContainer.getOverlayIcon();
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && ctx.canRenderInPass(pass -> pass == 1)) {
            ctx.setupColor(ForgeDirection.SOUTH, 0xffffff);
            renderFaceZPos(ctx, overlayIcon, rotation);
        }
        renderBlocks.enableAO = enableAO;
        draw(renderBlocks);
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        startDrawingQuads(renderBlocks, 0.0f, 0.0f, -1.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                draw(renderBlocks);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = mIconContainer.getIcon();
        if (icon != INVISIBLE_ICON && ctx.canRenderInPass(mIconContainer::canRenderInPass)) {
            ctx.setupColor(ForgeDirection.NORTH, mRGBa);
            renderFaceZNeg(ctx, mIconContainer.getIcon(), rotation);
        }
        final IIcon overlayIcon = mIconContainer.getOverlayIcon();
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && ctx.canRenderInPass(pass -> pass == 1)) {
            ctx.setupColor(ForgeDirection.NORTH, 0xffffff);
            renderFaceZNeg(ctx, overlayIcon, rotation);
        }
        renderBlocks.enableAO = enableAO;
        draw(renderBlocks);
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
    protected void renderFaceYNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        renderBlocks.uvRotateBottom = getRotation(extendedFacing);
        icon = getFlipped(ForgeDirection.DOWN, extendedFacing, icon);

        renderBlocks.renderFaceYNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateBottom = 0;
    }

    /**
     * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceYPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateTop = getRotation(extendedFacing);
        icon = getFlipped(ForgeDirection.UP, extendedFacing, icon);

        renderBlocks.renderFaceYPos(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateTop = 0;
    }

    /**
     * Renders the given texture to the north (z-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateEast = getRotation(extendedFacing);
        renderBlocks.field_152631_f = true;
        icon = getFlipped(ForgeDirection.NORTH, extendedFacing, icon);

        renderBlocks.renderFaceZNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateEast = 0;
        renderBlocks.field_152631_f = false;
    }

    /**
     * Renders the given texture to the south (z-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateWest = getRotation(extendedFacing);
        icon = getFlipped(ForgeDirection.SOUTH, extendedFacing, icon);

        renderBlocks.renderFaceZPos(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateWest = 0;
    }

    /**
     * Renders the given texture to the west (x-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateNorth = getRotation(extendedFacing);
        icon = getFlipped(ForgeDirection.WEST, extendedFacing, icon);

        renderBlocks.renderFaceXNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateNorth = 0;
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateSouth = getRotation(extendedFacing);
        renderBlocks.field_152631_f = true;
        icon = getFlipped(ForgeDirection.EAST, extendedFacing, icon);

        renderBlocks.renderFaceXPos(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateSouth = 0;
        renderBlocks.field_152631_f = false;
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
                flipU = true;
            }

            if (side == ForgeDirection.NORTH) {
                flipV = true;
            }

            return new GTIconFlipped(icon, flipU, flipV);
        }

        // certain directions need to be flipped horizontally seemingly randomly
        // maybe there's a reason, maybe there isn't, I haven't bothered to dig into the code to figure out why

        // spotless:off
        if (extendedFacing.getRotation().getIndex() % 2 == 0) {
            // flip U's if normal or upside down
            flipU = extendedFacing.getFlip().isHorizontallyFlipped();
        } else {
            // flip V's if clockwise or counter-clockwise
            flipV = extendedFacing.getFlip().isHorizontallyFlipped();

            if (side == ForgeDirection.EAST || side == ForgeDirection.NORTH) {
                flipU = true;
            }
        }
        // spotless:on

        if (side == ForgeDirection.DOWN && !GTMod.clientProxy().fixedBottomFaceUV) {
            flipU ^= true;
        }

        return new GTIconFlipped(icon, flipU, flipV);
    }

    private ExtendedFacing getExtendedFacing(int x, int y, int z) {
        if (!useExtFacing) return ExtendedFacing.DEFAULT;

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
