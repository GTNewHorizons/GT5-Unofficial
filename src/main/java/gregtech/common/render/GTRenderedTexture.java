package gregtech.common.render;

import static gregtech.api.enums.GTValues.UNCOLORED_RGBA;
import static gregtech.api.enums.Mods.Angelica;
import static gregtech.api.enums.Textures.InvisibleIcon.INVISIBLE_ICON;
import static gregtech.api.render.ISBRWorldContext.MAX_BRIGHTNESS;

import java.util.Arrays;

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

/**
 * Draws an {@link IIconContainer}'s whole icon layer stack on every face: layer 0 in this texture's own RGBA and each
 * later layer in the color the container reports for it.
 */
public class GTRenderedTexture extends GTTextureBase implements IColorModulationContainer, IIconTexture {

    protected final IIconContainer mIconContainer;
    private final short[] mRGBa;
    private final boolean glow;
    private final boolean useExtFacing;
    private final boolean untintOverrideIcon;
    private volatile FlippedIconCache[] inventoryFlipCaches = new FlippedIconCache[0];

    protected GTRenderedTexture(IIconContainer aIcon, short[] aRGBa, boolean glow, boolean extFacing,
        boolean untintOverrideIcon) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GTRenderedTexture");
        mIconContainer = aIcon;
        mRGBa = aRGBa;
        this.glow = glow;
        this.useExtFacing = extFacing;
        this.untintOverrideIcon = untintOverrideIcon;
    }

    /**
     * @return The tint for this draw: the built-in RGBA, or none when the icon currently resolves to resource pack
     *         override art and the builder opted into {@link ITextureBuilder#untintOverrideIcon}.
     */
    private short[] drawRGBa() {
        return untintOverrideIcon && mIconContainer.hasOverrideIcon() ? UNCOLORED_RGBA : mRGBa;
    }

    @Override
    public IIcon getIcon(int ordinalSide, @Nullable ISBRContext ctx) {
        return getLayerIcon(ForgeDirection.values()[ordinalSide], 0, ctx);
    }

    private IIcon getLayerIcon(ForgeDirection facing, int layer, @Nullable ISBRContext ctx) {
        final IIcon icon = mIconContainer.getLayerIcon(layer);
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
        renderFace(ctx, ForgeDirection.EAST, this::renderFaceXPos);
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        renderFace(ctx, ForgeDirection.WEST, this::renderFaceXNeg);
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        renderFace(ctx, ForgeDirection.UP, this::renderFaceYPos);
    }

    @Override
    public void renderYNeg(ISBRContext ctx) {
        renderFace(ctx, ForgeDirection.DOWN, this::renderFaceYNeg);
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        renderFace(ctx, ForgeDirection.SOUTH, this::renderFaceZPos);
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        renderFace(ctx, ForgeDirection.NORTH, this::renderFaceZNeg);
    }

    /**
     * Draws the {@code side} face through {@code face} once per icon layer, from layer 0 upwards.
     */
    private void renderFace(ISBRContext ctx, ForgeDirection side, FaceRenderer face) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final boolean startedDrawing = beginDrawingQuads(renderBlocks, side.offsetX, side.offsetY, side.offsetZ);
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
        if (canRenderInPass(ctx)) {
            final ExtendedFacing rotation = getExtendedFacing(ctx);
            final int passes = mIconContainer.getIconPasses();
            for (int layer = 0; layer < passes; layer++) {
                final IIcon icon = getLayerIcon(side, layer, ctx);
                if (icon == null || icon == INVISIBLE_ICON) continue;
                ctx.setupColor(side, layer == 0 ? drawRGBa() : mIconContainer.getIconColor(layer));
                face.render(ctx, icon, rotation, layer);
            }
        }
        renderBlocks.enableAO = enableAO;
        endDrawingQuads(renderBlocks, startedDrawing);
        if (glow && ctx instanceof ISBRWorldContext worldCtx) {
            worldCtx.tryStartAngelicaLighting();
        }
    }

    @FunctionalInterface
    private interface FaceRenderer {

        void render(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, int layer);
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
    protected void renderFaceYNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, int layer) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        renderBlocks.uvRotateBottom = getRotation(extendedFacing);
        icon = getFlipped(ctx, ForgeDirection.DOWN, extendedFacing, icon, layer);

        renderBlocks.renderFaceYNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateBottom = 0;
    }

    /**
     * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceYPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, int layer) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateTop = getRotation(extendedFacing);
        icon = getFlipped(ctx, ForgeDirection.UP, extendedFacing, icon, layer);

        renderBlocks.renderFaceYPos(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateTop = 0;
    }

    /**
     * Renders the given texture to the north (z-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, int layer) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateEast = getRotation(extendedFacing);
        renderBlocks.field_152631_f = true;
        icon = getFlipped(ctx, ForgeDirection.NORTH, extendedFacing, icon, layer);

        renderBlocks.renderFaceZNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateEast = 0;
        renderBlocks.field_152631_f = false;
    }

    /**
     * Renders the given texture to the south (z-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceZPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, int layer) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateWest = getRotation(extendedFacing);
        icon = getFlipped(ctx, ForgeDirection.SOUTH, extendedFacing, icon, layer);

        renderBlocks.renderFaceZPos(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateWest = 0;
    }

    /**
     * Renders the given texture to the west (x-negative) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXNeg(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, int layer) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateNorth = getRotation(extendedFacing);
        icon = getFlipped(ctx, ForgeDirection.WEST, extendedFacing, icon, layer);

        renderBlocks.renderFaceXNeg(Blocks.air, ctx.getX(), ctx.getY(), ctx.getZ(), icon);

        renderBlocks.uvRotateNorth = 0;
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block. Args: block, x, y, z, texture
     */
    protected void renderFaceXPos(ISBRContext ctx, IIcon icon, ExtendedFacing extendedFacing, int layer) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();

        renderBlocks.uvRotateSouth = getRotation(extendedFacing);
        renderBlocks.field_152631_f = true;
        icon = getFlipped(ctx, ForgeDirection.EAST, extendedFacing, icon, layer);

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
        int layer) {

        boolean flipU = false, flipV = false;

        // if we aren't rendering a multi, don't flip at all
        if (extendedFacing == null) {
            if (side == ForgeDirection.EAST || side == ForgeDirection.DOWN) {
                flipU = true;
            }

            if (side == ForgeDirection.NORTH) {
                flipV = true;
            }

            return getFlipped(ctx, icon, flipU, flipV, layer);
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

        return getFlipped(ctx, icon, flipU, flipV, layer);
    }

    private IIcon getFlipped(ISBRContext ctx, IIcon icon, boolean flipU, boolean flipV, int layer) {
        if (!flipU && !flipV) return icon;
        if (!(ctx instanceof ISBRInventoryContext)) return new GTIconFlipped(icon, flipU, flipV);

        return getInventoryFlipped(layer, flipU, flipV);
    }

    private IIcon getInventoryFlipped(int layer, boolean flipU, boolean flipV) {
        FlippedIconCache[] caches = inventoryFlipCaches;
        if (layer >= caches.length) {
            caches = Arrays.copyOf(caches, layer + 1);
            inventoryFlipCaches = caches;
        }
        FlippedIconCache cache = caches[layer];
        if (cache == null) {
            cache = new FlippedIconCache(mIconContainer, layer);
            caches[layer] = cache;
        }
        return cache.get(flipU, flipV);
    }

    private static final class FlippedIconCache {

        private final IIconContainer iconContainer;
        private final int layer;
        private volatile IIcon flippedU;
        private volatile IIcon flippedV;
        private volatile IIcon flippedUV;

        private FlippedIconCache(IIconContainer iconContainer, int layer) {
            this.iconContainer = iconContainer;
            this.layer = layer;
        }

        private IIcon get(boolean flipU, boolean flipV) {
            IIcon flipped = flipU ? (flipV ? flippedUV : flippedU) : flippedV;
            if (flipped != null) return flipped;

            flipped = new InventoryIconFlipped(iconContainer, layer, flipU, flipV);
            if (flipU && flipV) flippedUV = flipped;
            else if (flipU) flippedU = flipped;
            else flippedV = flipped;
            return flipped;
        }
    }

    private static final class InventoryIconFlipped extends GTIconFlipped {

        // Resolve through the container so cached wrappers survive atlas sprite replacement
        private final IIconContainer iconContainer;
        private final int layer;

        private InventoryIconFlipped(IIconContainer iconContainer, int layer, boolean flipU, boolean flipV) {
            super(null, flipU, flipV);
            this.iconContainer = iconContainer;
            this.layer = layer;
        }

        @Override
        IIcon getBaseIcon() {
            return iconContainer.getLayerIcon(layer);
        }
    }

    private ExtendedFacing getExtendedFacing(ISBRContext ctx) {
        if (!useExtFacing || ctx instanceof ISBRInventoryContext) return ExtendedFacing.DEFAULT;

        final EntityPlayer player = GTMod.proxy.getThePlayer();
        if (player == null) return ExtendedFacing.DEFAULT;

        final World w = player.getEntityWorld();
        if (w == null) return ExtendedFacing.DEFAULT;

        final TileEntity te = w.getTileEntity(ctx.getX(), ctx.getY(), ctx.getZ());

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
