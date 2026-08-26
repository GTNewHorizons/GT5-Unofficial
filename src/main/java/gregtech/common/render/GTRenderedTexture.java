package gregtech.common.render;

import static gregtech.api.enums.Mods.Angelica;
import static gregtech.api.enums.Textures.InvisibleIcon.INVISIBLE_ICON;
import static gregtech.api.render.ISBRWorldContext.MAX_BRIGHTNESS;

import javax.annotation.Nullable;

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
import com.prupe.mcpatcher.ctm.CTMUtils;

import gregtech.GTMod;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.ISBRContext;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;

public class GTRenderedTexture extends GTTextureBase implements IColorModulationContainer, IIconTexture {

    protected final IIconContainer mIconContainer;
    private final short[] mRGBa;
    private final boolean glow;
    private final boolean useExtFacing;
    private volatile FlippedIconCache inventoryBaseFlipCache;
    private volatile FlippedIconCache inventoryOverlayFlipCache;

    protected GTRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean glow, boolean extFacing) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTRenderedTexture");
        mIconContainer = aIcon;
        mRGBa = aRGBa;
        this.glow = glow;
        this.useExtFacing = extFacing;
    }

    @Override
    public IIcon getIcon(int ordinalSide, @Nullable ISBRContext ctx) {
        return getIcon(ForgeDirection.values()[ordinalSide], false, ctx);
    }

    public IIcon getIcon(ForgeDirection facing, boolean overlay, @Nullable ISBRContext ctx) {
        final IIcon icon = overlay ? mIconContainer.getOverlayIcon() : mIconContainer.getIcon();
        if (icon == null || !Angelica.isModLoaded()) return icon;
        else return ctx instanceof ISBRWorldContext ctxW ? CTMUtils.getBlockIcon(
            icon,
            ctx.getBlock(),
            ctxW.getBlockAccess(),
            ctxW.getX(),
            ctxW.getY(),
            ctxW.getZ(),
            facing.ordinal()) : icon;
    }

    @Override
    public void renderXPos(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final boolean startedDrawing = beginDrawingQuads(renderBlocks, 1.0f, 0.0f, 0.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                endDrawingQuads(renderBlocks, startedDrawing);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
            if (ctx instanceof ISBRWorldContext worldCtx) {
                worldCtx.finishLighting();
            }
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = getIcon(ForgeDirection.EAST, false, ctx);
        if (icon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.EAST, mRGBa);
            renderFaceXPos(ctx, mIconContainer.getIcon(), rotation, false);
        }
        final IIcon overlayIcon = getIcon(ForgeDirection.EAST, true, ctx);
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.EAST, 0xffffff);
            renderFaceXPos(ctx, overlayIcon, rotation, true);
        }
        renderBlocks.enableAO = enableAO;
        endDrawingQuads(renderBlocks, startedDrawing);
        if (glow && ctx instanceof ISBRWorldContext worldCtx) {
            worldCtx.tryStartAngelicaLighting();
        }
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final boolean startedDrawing = beginDrawingQuads(renderBlocks, -1.0f, 0.0f, 0.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                endDrawingQuads(renderBlocks, startedDrawing);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
            if (ctx instanceof ISBRWorldContext worldCtx) {
                worldCtx.finishLighting();
            }
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = getIcon(ForgeDirection.WEST, false, ctx);
        if (icon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.WEST, mRGBa);
            renderFaceXNeg(ctx, mIconContainer.getIcon(), rotation, false);
        }
        final IIcon overlayIcon = getIcon(ForgeDirection.WEST, true, ctx);
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.WEST, 0xffffff);
            renderFaceXNeg(ctx, overlayIcon, rotation, true);
        }
        renderBlocks.enableAO = enableAO;
        endDrawingQuads(renderBlocks, startedDrawing);
        if (glow && ctx instanceof ISBRWorldContext worldCtx) {
            worldCtx.tryStartAngelicaLighting();
        }
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final boolean startedDrawing = beginDrawingQuads(renderBlocks, 0.0f, 1.0f, 0.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                endDrawingQuads(renderBlocks, startedDrawing);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
            if (ctx instanceof ISBRWorldContext worldCtx) {
                worldCtx.finishLighting();
            }
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = getIcon(ForgeDirection.UP, false, ctx);
        if (icon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.UP, mRGBa);
            renderFaceYPos(ctx, mIconContainer.getIcon(), rotation, false);
        }
        final IIcon overlayIcon = getIcon(ForgeDirection.UP, true, ctx);
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.UP, 0xffffff);
            renderFaceYPos(ctx, overlayIcon, rotation, true);
        }
        renderBlocks.enableAO = enableAO;
        endDrawingQuads(renderBlocks, startedDrawing);
        if (glow && ctx instanceof ISBRWorldContext worldCtx) {
            worldCtx.tryStartAngelicaLighting();
        }
    }

    @Override
    public void renderYNeg(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final boolean startedDrawing = beginDrawingQuads(renderBlocks, 0.0f, -1.0f, 0.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                endDrawingQuads(renderBlocks, startedDrawing);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
            if (ctx instanceof ISBRWorldContext worldCtx) {
                worldCtx.finishLighting();
            }
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = getIcon(ForgeDirection.DOWN, false, ctx);
        if (icon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.DOWN, mRGBa);
            renderFaceYNeg(ctx, mIconContainer.getIcon(), rotation, false);
        }
        final IIcon overlayIcon = getIcon(ForgeDirection.DOWN, true, ctx);
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.DOWN, 0xffffff);
            renderFaceYNeg(ctx, overlayIcon, rotation, true);
        }
        renderBlocks.enableAO = enableAO;
        endDrawingQuads(renderBlocks, startedDrawing);
        if (glow && ctx instanceof ISBRWorldContext worldCtx) {
            worldCtx.tryStartAngelicaLighting();
        }
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final boolean startedDrawing = beginDrawingQuads(renderBlocks, 0.0f, 0.0f, 1.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                endDrawingQuads(renderBlocks, startedDrawing);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
            if (ctx instanceof ISBRWorldContext worldCtx) {
                worldCtx.finishLighting();
            }
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = getIcon(ForgeDirection.SOUTH, false, ctx);
        if (icon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.SOUTH, mRGBa);
            renderFaceZPos(ctx, mIconContainer.getIcon(), rotation, false);
        }
        final IIcon overlayIcon = getIcon(ForgeDirection.SOUTH, true, ctx);
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.SOUTH, 0xffffff);
            renderFaceZPos(ctx, overlayIcon, rotation, true);
        }
        renderBlocks.enableAO = enableAO;
        endDrawingQuads(renderBlocks, startedDrawing);
        if (glow && ctx instanceof ISBRWorldContext worldCtx) {
            worldCtx.tryStartAngelicaLighting();
        }
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final boolean startedDrawing = beginDrawingQuads(renderBlocks, 0.0f, 0.0f, -1.0f);
        ctx.reset();
        final boolean enableAO = renderBlocks.enableAO;
        if (glow) {
            if (!GTMod.proxy.mRenderGlowTextures) {
                endDrawingQuads(renderBlocks, startedDrawing);
                return;
            }
            renderBlocks.enableAO = false;
            ctx.setLightnessOverride(1.0F);
            ctx.setBrightnessOverride(MAX_BRIGHTNESS);
            if (ctx instanceof ISBRWorldContext worldCtx) {
                worldCtx.finishLighting();
            }
        }
        final ExtendedFacing rotation = getExtendedFacing(ctx.getX(), ctx.getY(), ctx.getZ());
        final IIcon icon = getIcon(ForgeDirection.NORTH, false, ctx);
        if (icon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.NORTH, mRGBa);
            renderFaceZNeg(ctx, mIconContainer.getIcon(), rotation, false);
        }
        final IIcon overlayIcon = getIcon(ForgeDirection.NORTH, true, ctx);
        if (overlayIcon != null && overlayIcon != INVISIBLE_ICON && canRenderInPass(ctx)) {
            ctx.setupColor(ForgeDirection.NORTH, 0xffffff);
            renderFaceZNeg(ctx, overlayIcon, rotation, true);
        }
        renderBlocks.enableAO = enableAO;
        endDrawingQuads(renderBlocks, startedDrawing);
        if (glow && ctx instanceof ISBRWorldContext worldCtx) {
            worldCtx.tryStartAngelicaLighting();
        }
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }

    private boolean canRenderInPass(ISBRContext ctx) {
        return ctx instanceof ISBRInventoryContext || ctx.canRenderInPass(mIconContainer::canRenderInPass);
    }

    @Override
    public boolean isValidTexture() {
        return mIconContainer != null;
    }

    /**
     * Renders the given texture to the bottom face of the block. Args: block, x, y, z, texture
     */
    private void renderFaceYNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, boolean isOverlay) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        renderBlocks.uvRotateBottom = getRotation(extendedFacing);
        icon = getFlipped(ctx, ForgeDirection.DOWN, extendedFacing, icon, isOverlay);

        renderBlocks.renderFaceYNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateBottom = 0;
    }

    /**
     * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
     */
    private void renderFaceYPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, boolean isOverlay) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateTop = getRotation(extendedFacing);
        icon = getFlipped(ctx, ForgeDirection.UP, extendedFacing, icon, isOverlay);

        renderBlocks.renderFaceYPos(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateTop = 0;
    }

    /**
     * Renders the given texture to the north (z-negative) face of the block. Args: block, x, y, z, texture
     */
    private void renderFaceZNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, boolean isOverlay) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateEast = getRotation(extendedFacing);
        renderBlocks.field_152631_f = true;
        icon = getFlipped(ctx, ForgeDirection.NORTH, extendedFacing, icon, isOverlay);

        renderBlocks.renderFaceZNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateEast = 0;
        renderBlocks.field_152631_f = false;
    }

    /**
     * Renders the given texture to the south (z-positive) face of the block. Args: block, x, y, z, texture
     */
    private void renderFaceZPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, boolean isOverlay) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateWest = getRotation(extendedFacing);
        icon = getFlipped(ctx, ForgeDirection.SOUTH, extendedFacing, icon, isOverlay);

        renderBlocks.renderFaceZPos(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateWest = 0;
    }

    /**
     * Renders the given texture to the west (x-negative) face of the block. Args: block, x, y, z, texture
     */
    private void renderFaceXNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, boolean isOverlay) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateNorth = getRotation(extendedFacing);
        icon = getFlipped(ctx, ForgeDirection.WEST, extendedFacing, icon, isOverlay);

        renderBlocks.renderFaceXNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateNorth = 0;
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block. Args: block, x, y, z, texture
     */
    private void renderFaceXPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, boolean isOverlay) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateSouth = getRotation(extendedFacing);
        renderBlocks.field_152631_f = true;
        icon = getFlipped(ctx, ForgeDirection.EAST, extendedFacing, icon, isOverlay);

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

    private IIcon getFlipped(ISBRContext ctx, ForgeDirection side, ExtendedFacing extendedFacing, IIcon icon,
        boolean isOverlay) {

        boolean flipU = false, flipV = false;

        // if we aren't rendering a multi, don't flip at all
        if (extendedFacing == null) {
            if (side == ForgeDirection.EAST || side == ForgeDirection.DOWN) {
                flipU = true;
            }

            if (side == ForgeDirection.NORTH) {
                flipV = true;
            }

            return getFlipped(ctx, icon, flipU, flipV, isOverlay);
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

        return getFlipped(ctx, icon, flipU, flipV, isOverlay);
    }

    private IIcon getFlipped(ISBRContext ctx, IIcon icon, boolean flipU, boolean flipV, boolean isOverlay) {
        if (!flipU && !flipV) return icon;
        if (!(ctx instanceof ISBRInventoryContext)) return new GTIconFlipped(icon, flipU, flipV);

        return getInventoryFlipped(isOverlay, flipU, flipV);
    }

    private IIcon getInventoryFlipped(boolean isOverlay, boolean flipU, boolean flipV) {
        if (!flipU && !flipV) {
            if (isOverlay) return mIconContainer.getOverlayIcon();
            return mIconContainer.getIcon();
        }

        FlippedIconCache cache = isOverlay ? inventoryOverlayFlipCache : inventoryBaseFlipCache;
        if (cache == null) {
            cache = new FlippedIconCache(mIconContainer, isOverlay);
            if (isOverlay) inventoryOverlayFlipCache = cache;
            else inventoryBaseFlipCache = cache;
        }
        return cache.get(flipU, flipV);
    }

    private static final class FlippedIconCache {

        private final IIconContainer iconContainer;
        private final boolean overlay;
        private volatile IIcon flippedU;
        private volatile IIcon flippedV;
        private volatile IIcon flippedUV;

        private FlippedIconCache(IIconContainer iconContainer, boolean overlay) {
            this.iconContainer = iconContainer;
            this.overlay = overlay;
        }

        private IIcon get(boolean flipU, boolean flipV) {
            IIcon flipped = flipU ? (flipV ? flippedUV : flippedU) : flippedV;
            if (flipped != null) return flipped;

            flipped = new InventoryIconFlipped(iconContainer, overlay, flipU, flipV);
            if (flipU && flipV) flippedUV = flipped;
            else if (flipU) flippedU = flipped;
            else flippedV = flipped;
            return flipped;
        }
    }

    private static final class InventoryIconFlipped extends GTIconFlipped {

        private final IIconContainer iconContainer;
        private final boolean overlay;

        private InventoryIconFlipped(IIconContainer iconContainer, boolean overlay, boolean flipU, boolean flipV) {
            super(null, flipU, flipV);
            this.iconContainer = iconContainer;
            this.overlay = overlay;
        }

        @Override
        IIcon getBaseIcon() {
            return overlay ? iconContainer.getOverlayIcon() : iconContainer.getIcon();
        }
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
