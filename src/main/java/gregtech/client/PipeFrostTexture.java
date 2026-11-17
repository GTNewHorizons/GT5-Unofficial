package gregtech.client;

import static gregtech.api.enums.Mods.GregTech;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTLoggers;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.FluidPipeThermals;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.render.ISBRContext;
import gregtech.api.render.ISBRWorldContext;

/**
 * Frost on a frosted fluid pipe, drawn as part of the pipe's block model in the translucent pass so it blends over the
 * pipe. Every outer face gets the frost texture, and every outside edge gets a strip of heavier frost.
 */
@SideOnly(Side.CLIENT)
public final class PipeFrostTexture implements ITexture {

    /** Tileable ice crystals, one tile per block. Lower alpha pixels only show on colder pipes. */
    private static final ResourceLocation FACE_SOURCE = new ResourceLocation(
        GregTech.resourceDomain,
        "textures/effects/pipe_frost.png");
    /** Frost along an edge, tiling along it and {@link #EDGE_WIDTH} wide. */
    private static final ResourceLocation EDGE_SOURCE = new ResourceLocation(
        GregTech.resourceDomain,
        "textures/effects/pipe_frost_edge.png");
    /** Alpha cut-off per frost level, lightest first, so the same crystals spread as it gets colder. */
    private static final float[] LEVEL_CUTOFFS = { 0.72F, 0.5F, 0.32F, 0.12F };
    private static final double EDGE_WIDTH = 0.25D;
    /** How far frost stands off the pipe's surface. */
    private static final double OFFSET = 0.003D;
    /** Box bounds closer than this count as the same. */
    private static final double EPSILON = 1.0E-4D;
    /** Mipmap levels the sprite data has room for, which is the most the game allows. */
    private static final int MAX_MIPMAP_LEVELS = 4;

    /** Frost sprites for each step, lightest first. Pixels at or below that step's cut-off are cleared. */
    private static final IIcon[] FACE_SPRITES = new IIcon[FluidPipeThermals.FROST_STEPS];
    private static final IIcon[] EDGE_SPRITES = new IIcon[FluidPipeThermals.FROST_STEPS];

    private final MTEFluidPipe pipe;
    private final int connections;
    private final int step;
    /** {@link MTEFluidPipe#getJoinedSides}, found the first time it's needed. */
    private int joined = -1;

    /**
     * @param connections the pipe's connected sides, one bit per {@link ForgeDirection}
     * @param step        how heavy the frost is, from 1 to {@link FluidPipeThermals#FROST_STEPS}
     */
    public PipeFrostTexture(MTEFluidPipe pipe, int connections, int step) {
        this.pipe = pipe;
        this.connections = connections;
        this.step = step;
    }

    /** Makes the sprites for every frost step. Called each time the block atlas is about to be stitched. */
    public static void registerSprites(TextureMap map) {
        for (int i = 0; i < FluidPipeThermals.FROST_STEPS; i++) {
            final float cutoff = PipeThermalEffectsRenderer
                .getStageValue(LEVEL_CUTOFFS, 1.0F, (i + 1) / (float) FluidPipeThermals.FROST_STEPS_PER_LEVEL);
            FACE_SPRITES[i] = registerSprite(map, "pipe_frost_" + i, FACE_SOURCE, cutoff);
            EDGE_SPRITES[i] = registerSprite(map, "pipe_frost_edge_" + i, EDGE_SOURCE, cutoff);
        }
    }

    private static IIcon registerSprite(TextureMap map, String name, ResourceLocation source, float cutoff) {
        final String fullName = GregTech.resourceDomain + ":generated/" + name;
        final TextureAtlasSprite sprite = new FrostSprite(fullName, source, cutoff);
        return map.setTextureEntry(fullName, sprite) ? sprite : map.getTextureExtry(fullName);
    }

    @Override
    public void renderXPos(ISBRContext ctx) {
        render(ctx, ForgeDirection.EAST);
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        render(ctx, ForgeDirection.WEST);
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        render(ctx, ForgeDirection.UP);
    }

    @Override
    public void renderYNeg(ISBRContext ctx) {
        render(ctx, ForgeDirection.DOWN);
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        render(ctx, ForgeDirection.SOUTH);
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        render(ctx, ForgeDirection.NORTH);
    }

    @Override
    public boolean isValidTexture() {
        return FACE_SPRITES[step - 1] != null && EDGE_SPRITES[step - 1] != null;
    }

    /** Draws the frost on one face of the box the pipe renderer is drawing, and the edge strips round it. */
    private void render(ISBRContext ctx, ForgeDirection face) {
        // Only in the world, and only in the translucent pass, so it blends over the pipe
        if (!(ctx instanceof ISBRWorldContext worldCtx) || !ctx.canRenderInPass(pass -> pass == 1)) return;

        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        final double[] min = { renderBlocks.renderMinX, renderBlocks.renderMinY, renderBlocks.renderMinZ };
        final double[] max = { renderBlocks.renderMaxX, renderBlocks.renderMaxY, renderBlocks.renderMaxZ };
        final int outer = getOuterSides(worldCtx, min, max);
        if ((outer & face.flag) == 0) return;

        // Grown out from its outer sides, so frost wraps round the corners
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((outer & side.flag) == 0) continue;
            if (positive(side)) max[axis(side)] += OFFSET;
            else min[axis(side)] -= OFFSET;
        }

        setupColor(ctx, face);
        addFrost(
            Tessellator.instance,
            ctx.getX(),
            ctx.getY(),
            ctx.getZ(),
            min,
            max,
            face,
            outer,
            FACE_SPRITES[step - 1],
            EDGE_SPRITES[step - 1]);
    }

    /**
     * The pipe renderer draws a centre box and an arm per connection, or one box for a straight pipe. Arms and straight
     * boxes run along one axis, and their ends are open where they join another pipe or meet the centre.
     *
     * @return the sides of this box on the outside of the pipe, one bit per {@link ForgeDirection}
     */
    private int getOuterSides(ISBRWorldContext ctx, double[] min, double[] max) {
        if (joined < 0) {
            joined = pipe.getJoinedSides(ctx.getBlockAccess(), ctx.getX(), ctx.getY(), ctx.getZ(), connections);
        }
        final double thickness = pipe.getThickness();
        if (thickness >= 1.0D) return ~joined & 63;

        final double pipeMin = (1.0D - thickness) / 2.0D, pipeMax = 1.0D - pipeMin;
        boolean centre = true;
        for (int a = 0; a < 3; a++) centre &= isAcrossPipe(min[a], max[a], pipeMin, pipeMax);

        int outer = 0;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final int a = axis(side);
            final boolean isOuter;
            if (isAcrossPipe(min[a], max[a], pipeMin, pipeMax)) {
                // The side of an arm, or of the centre where nothing connects
                isOuter = !centre || (connections & side.flag) == 0;
            } else {
                final double end = positive(side) ? max[a] : min[a];
                isOuter = (end < EPSILON || end > 1.0D - EPSILON) && (joined & side.flag) == 0;
            }
            if (isOuter) outer |= side.flag;
        }
        return outer;
    }

    /** @return whether a box spans exactly the pipe's width on an axis, rather than running along it */
    private static boolean isAcrossPipe(double lo, double hi, double pipeMin, double pipeMax) {
        return Math.abs(lo - pipeMin) < EPSILON && Math.abs(hi - pipeMax) < EPSILON;
    }

    /** Lights the frost like the pipe face under it. */
    private static void setupColor(ISBRContext ctx, ForgeDirection face) {
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        ctx.reset()
            .setupColor(face, 0xffffff);
        // Smooth lighting leaves a colour per corner for vanilla's face code, and these quads take the average
        if (renderBlocks.enableAO) {
            Tessellator.instance.setColorOpaque_F(
                (renderBlocks.colorRedTopLeft + renderBlocks.colorRedBottomLeft
                    + renderBlocks.colorRedBottomRight
                    + renderBlocks.colorRedTopRight) / 4.0F,
                (renderBlocks.colorGreenTopLeft + renderBlocks.colorGreenBottomLeft
                    + renderBlocks.colorGreenBottomRight
                    + renderBlocks.colorGreenTopRight) / 4.0F,
                (renderBlocks.colorBlueTopLeft + renderBlocks.colorBlueBottomLeft
                    + renderBlocks.colorBlueBottomRight
                    + renderBlocks.colorBlueTopRight) / 4.0F);
        }
    }

    /**
     * One face of frost, with a strip of edge frost along each outer edge. The face is cut where the strips end, so
     * every strip lies on a piece of face with exactly the same corners. Overlapping quads then have exactly the same
     * depth and both draw, whatever order the translucent pass sorts them in. Quads that only share a plane z-fight,
     * and a strip raised off the face hides the face frost whenever it's sorted first.
     */
    private static void addFrost(Tessellator t, double x, double y, double z, double[] min, double[] max,
        ForgeDirection face, int outer, IIcon faceIcon, IIcon edgeIcon) {
        final int fa = axis(face);
        final int ua = uAxis(fa);
        final int va = 3 - fa - ua;
        final double plane = positive(face) ? max[fa] : min[fa];
        final double[] us = getCuts(min[ua], max[ua], outer, ua);
        final double[] vs = getCuts(min[va], max[va], outer, va);
        for (int i = 0; i < 3; i++) {
            final double u0 = us[i], u1 = us[i + 1];
            if (u1 - u0 < EPSILON) continue;
            for (int j = 0; j < 3; j++) {
                final double v0 = vs[j], v1 = vs[j + 1];
                if (v1 - v0 < EPSILON) continue;
                addCell(t, x, y, z, face, plane, u0, u1, v0, v1, faceIcon, -1, 0);
                // Only edges with a strip have a first or last cell, as the others are cut with no width
                if (i == 0) addCell(t, x, y, z, face, plane, u0, u1, v0, v1, edgeIcon, ua, us[0]);
                if (i == 2) addCell(t, x, y, z, face, plane, u0, u1, v0, v1, edgeIcon, ua, us[3]);
                if (j == 0) addCell(t, x, y, z, face, plane, u0, u1, v0, v1, edgeIcon, va, vs[0]);
                if (j == 2) addCell(t, x, y, z, face, plane, u0, u1, v0, v1, edgeIcon, va, vs[3]);
            }
        }
    }

    /**
     * @return where a face is cut on one axis: its low end, the inner side of the low edge's strip, the inner side of
     *         the high edge's strip and its high end. An edge with no strip is cut at the end, leaving no width.
     */
    private static double[] getCuts(double lo, double hi, int outer, int axis) {
        final double width = Math.min(EDGE_WIDTH, (hi - lo) / 2.0D);
        final double low = (outer & getSide(axis, false).flag) != 0 ? width : 0;
        final double high = (outer & getSide(axis, true).flag) != 0 ? width : 0;
        return new double[] { lo, lo + low, hi - high, hi };
    }

    /**
     * One rectangle of a face. Every layer in the same rectangle goes through here with the same bounds, so they get
     * identical corners in the same order.
     *
     * @param edgeAxis  the axis across the edge strip being drawn, or -1 for the face texture, which follows the world
     *                  axes so it tiles across pipes
     * @param edgeOuter where that strip's outer edge is on {@code edgeAxis}. Its texture runs along the edge and
     *                  inwards
     *                  from it.
     */
    private static void addCell(Tessellator t, double x, double y, double z, ForgeDirection face, double plane,
        double u0, double u1, double v0, double v1, IIcon icon, int edgeAxis, double edgeOuter) {
        final int fa = axis(face);
        final int ua = uAxis(fa);
        final int va = 3 - fa - ua;
        final double[] p = new double[3];
        p[fa] = plane;
        // Corners go round from (u0, v0) to (u1, v0), (u1, v1) and (u0, v1), backwards if that faces inwards
        final boolean backwards = parity(ua, va, fa) != sign(face);
        for (int i = 0; i < 4; i++) {
            final int corner = backwards ? 3 - i : i;
            final double u = corner == 1 || corner == 2 ? u1 : u0;
            final double v = corner >= 2 ? v1 : v0;
            p[ua] = u;
            p[va] = v;
            final double textureU, textureV;
            if (edgeAxis < 0) {
                textureU = toPixels(u);
                textureV = toPixels(v);
            } else {
                final boolean acrossU = edgeAxis == ua;
                textureU = toPixels(acrossU ? v : u);
                textureV = Math.abs((acrossU ? u : v) - edgeOuter) / EDGE_WIDTH * 16.0D;
            }
            t.addVertexWithUV(
                x + p[0],
                y + p[1],
                z + p[2],
                icon.getInterpolatedU(textureU),
                icon.getInterpolatedV(textureV));
        }
    }

    private static ForgeDirection getSide(int axis, boolean positive) {
        return switch (axis) {
            case 0 -> positive ? ForgeDirection.EAST : ForgeDirection.WEST;
            case 1 -> positive ? ForgeDirection.UP : ForgeDirection.DOWN;
            default -> positive ? ForgeDirection.SOUTH : ForgeDirection.NORTH;
        };
    }

    /** @return a position within the block as sprite pixels, kept inside the sprite */
    private static double toPixels(double position) {
        return Math.min(1.0D, Math.max(0.0D, position)) * 16.0D;
    }

    private static int axis(ForgeDirection side) {
        return side.offsetX != 0 ? 0 : side.offsetY != 0 ? 1 : 2;
    }

    private static boolean positive(ForgeDirection side) {
        return side.offsetX + side.offsetY + side.offsetZ > 0;
    }

    private static int sign(ForgeDirection side) {
        return positive(side) ? 1 : -1;
    }

    /** Axis used for the texture's u on a face. Z for x-facing faces, otherwise x. */
    private static int uAxis(int faceAxis) {
        return faceAxis == 0 ? 2 : 0;
    }

    /** @return 1 if axis {@code a} crossed with axis {@code b} points along axis {@code c}, otherwise -1 */
    private static int parity(int a, int b, int c) {
        return (b - a + 3) % 3 == 1 ? 1 : -1;
    }

    /** A frost texture with the pixels at or below one step's alpha cut-off cleared. */
    private static final class FrostSprite extends TextureAtlasSprite {

        private final ResourceLocation source;
        private final float cutoff;

        FrostSprite(String name, ResourceLocation source, float cutoff) {
            super(name);
            this.source = source;
            this.cutoff = cutoff;
        }

        @Override
        public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
            return true;
        }

        /** @return false once loaded, which is what tells the atlas to stitch it */
        @Override
        public boolean load(IResourceManager manager, ResourceLocation location) {
            final BufferedImage image;
            try (InputStream stream = manager.getResource(source)
                .getInputStream()) {
                image = ImageIO.read(stream);
            } catch (IOException e) {
                GTLoggers.GT_FML_LOGGER.error("Couldn't load pipe frost texture " + source, e);
                return true;
            }
            if (image == null) {
                GTLoggers.GT_FML_LOGGER.error("Couldn't read pipe frost texture " + source);
                return true;
            }

            // Stretched to a square, as the atlas only takes square textures unless they're animated
            final int size = Math.max(image.getWidth(), image.getHeight());
            final BufferedImage stage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    final int argb = image.getRGB(x * image.getWidth() / size, y * image.getHeight() / size);
                    // Colour is kept on cleared pixels so mipmaps don't darken the edges of the crystals
                    stage.setRGB(x, y, (argb >>> 24) / 255.0F > cutoff ? argb : argb & 0xFFFFFF);
                }
            }
            final BufferedImage[] images = new BufferedImage[1 + MAX_MIPMAP_LEVELS];
            images[0] = stage;
            loadSprite(images, null, Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1);
            return false;
        }
    }
}
