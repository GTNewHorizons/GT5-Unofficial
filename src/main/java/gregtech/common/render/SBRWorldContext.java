/*
 * SBRContext - Derived and adapted from @Mineshopper / carpentersblocks Copyright (c) 2013-2021. This library is
 * free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation version 2.1 of the License. This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

package gregtech.common.render;

import java.util.function.IntPredicate;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.SBRContextHolder;

/**
 * Represents the rendering context for a single block during a render pass.
 * <p>
 * This class holds the tightly-coupled mutable state required to coordinate lighting,
 * shading, ambient occlusion, and color calculation based on the block, its coordinates,
 * the renderer, and the world in which it is rendered. It is passed
 * to various rendering methods throughout a block's render cycle.
 */
public final class SBRWorldContext extends SBRContextBase implements ISBRWorldContext {

    /**
     * Used to determine if face is flush with negative neighbour
     */
    private static final double FLUSH_MIN = 0.001D;

    /**
     * Used to determine if face is flush with positive neighbour
     */
    private static final double FLUSH_MAX = 0.999D;

    /**
     * Mixed Brightness cache
     * <p>
     * Entries store the result of {@link Block#getMixedBrightnessForBlock(IBlockAccess, int, int, int)}<br>
     * for the block at the current coordinates<br>
     * and its 26 neighbors within a 3×3×3 cube centered on (x, y, z).
     */
    private final int[][][] MBFB = new int[3][3][3];

    /**
     * Ambient Occlusion Light Value cache
     * <p>
     * Entries store the result of {@link Block#getAmbientOcclusionLightValue()}<br>
     * for the block at the current coordinates<br>
     * and its 26 neighbors within a 3×3×3 cube centered on (x, y, z).
     */
    private final float[][][] AOLV = new float[3][3][3];

    private int worldRenderPass;
    /**
     * Non-null dummy world, replaced in {@link #setup}.
     */
    @NotNull
    private IBlockAccess blockAccess = GTValues.DW;
    /**
     * Brightness for side.
     */
    private int brightness;

    /**
     * Ambient occlusion values for all four corners of side.
     */
    private float aoTopLeft, aoBottomLeft, aoBottomRight, aoTopRight;

    /**
     * Package-private constructor.
     * <p>
     * Instances should be obtained via {@link SBRContextHolder#getSBRWorldContext}.
     */
    public SBRWorldContext() {}

    /**
     * Gets mixed ambient occlusion value from two inputs, with a ratio applied to the final result.
     *
     * @param ao1   the first ambient occlusion value
     * @param ao2   the second ambient occlusion value
     * @param ratio the ratio for mixing
     * @return the mixed red, green, blue float values
     */
    private static float getMixedAo(float ao1, float ao2, double ratio) {
        final float diff = (float) (Math.abs(ao1 - ao2) * (1.0F - ratio));

        return ao1 > ao2 ? ao1 - diff : ao1 + diff;
    }

    /**
     * Configures this {@link SBRWorldContext} used to render a single {@link Block} in world for the
     * current render pass at the given coordinates
     *
     * @param x            the x coordinate
     * @param y            the y coordinate
     * @param z            the z coordinate
     * @param block        the {@link Block} to render
     * @param modelId      the Model ID for the block
     * @param renderBlocks the {@link RenderBlocks} renderer to use
     * @return this context instance, configured with the given parameters
     */
    @SuppressWarnings("MethodWithTooManyParameters")
    public
    // Blame ISimpleBlockRenderingHandler.renderWorldBlock
    SBRWorldContext setup(int x, int y, int z, Block block, int modelId, RenderBlocks renderBlocks) {
        super.setup(block, modelId, renderBlocks);
        this.blockAccess = renderBlocks.blockAccess;
        this.worldRenderPass = ForgeHooksClient.getWorldRenderPass();
        this.x = x;
        this.y = y;
        this.z = z;
        this.renderBlocks.useInventoryTint = false;
        populatesLightingCaches();
        reset();
        return this;
    }

    @Override
    public @NotNull IBlockAccess getBlockAccess() {
        return blockAccess;
    }

    @Override
    public @Nullable TileEntity getTileEntity() {
        return blockAccess.getTileEntity(x, y, z);
    }

    /**
     * Populates the caches for Mixed Brightness for Blocks (MBFB)
     * and Ambient Occlusion Light Values (AOLV).
     * <p>
     * These caches store lighting values for the block at the current coordinates
     * and its neighbors within a 3×3×3 cube centered on (x, y, z).
     * <p>
     * This method skips processing if Ambient Occlusion is disabled in the game settings.
     */
    private void populatesLightingCaches() {
        final Block blk = this.block;
        final IBlockAccess acc = this.blockAccess;
        final int x = this.x, y = this.y, z = this.z;
        final int[][][] MBFB = this.MBFB;
        final boolean aoDisabled = Minecraft.getMinecraft().gameSettings.ambientOcclusion == 0;
        if (aoDisabled) {
            MBFB[1][1][1] = blk.getMixedBrightnessForBlock(acc, x, y, z);
            MBFB[0][1][1] = blk.getMixedBrightnessForBlock(acc, x - 1, y, z);
            MBFB[2][1][1] = blk.getMixedBrightnessForBlock(acc, x + 1, y, z);
            MBFB[1][0][1] = blk.getMixedBrightnessForBlock(acc, x, y - 1, z);
            MBFB[1][2][1] = blk.getMixedBrightnessForBlock(acc, x, y + 1, z);
            MBFB[1][1][0] = blk.getMixedBrightnessForBlock(acc, x, y, z - 1);
            MBFB[1][1][2] = blk.getMixedBrightnessForBlock(acc, x, y, z + 1);
        } else {
            final float[][][] AOLV = this.AOLV;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        MBFB[dx + 1][dy + 1][dz + 1] = blk.getMixedBrightnessForBlock(acc, x + dx, y + dy, z + dz);
                        AOLV[dx + 1][dy + 1][dz + 1] = acc.getBlock(x + dx, y + dy, z + dz)
                            .getAmbientOcclusionLightValue();
                    }
                }
            }
        }
    }

    /**
     * Resets override flags to their default values.
     * <p>
     * This ensures deterministic rendering by clearing any leftover state
     * from previous use of this context instance.
     *
     * @return this {@link SBRContextBase} instance for chaining
     */
    @Override
    public ISBRWorldContext reset() {
        this.hasBrightnessOverride = false;
        this.hasColorOverride = false;
        this.hasLightnessOverride = false;
        this.renderBlocks.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.proxy.mRenderTileAmbientOcclusion;
        return this;
    }

    @Override
    public void renderNegativeYFacing(ITexture[] tex) {
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (!renderBlocks.partialRenderBounds && !renderBlocks.renderAllFaces
            && !block.shouldSideBeRendered(blockAccess, x, y - 1, z, 0)) return;
        setupLightingYNeg();
        super.renderNegativeYFacing(tex);
    }

    @Override
    public void renderPositiveYFacing(ITexture[] tex) {
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (!renderBlocks.partialRenderBounds && !renderBlocks.renderAllFaces
            && !block.shouldSideBeRendered(blockAccess, x, y + 1, z, 1)) return;
        setupLightingYPos();
        super.renderPositiveYFacing(tex);
    }

    @Override
    public void renderNegativeZFacing(ITexture[] tex) {
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (!renderBlocks.partialRenderBounds && !renderBlocks.renderAllFaces
            && !block.shouldSideBeRendered(blockAccess, x, y, z - 1, 2)) return;
        setupLightingZNeg();
        super.renderNegativeZFacing(tex);
    }

    @Override
    public void renderPositiveZFacing(ITexture[] tex) {
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (!renderBlocks.partialRenderBounds && !renderBlocks.renderAllFaces
            && !block.shouldSideBeRendered(blockAccess, x, y, z + 1, 3)) return;
        setupLightingZPos();
        super.renderPositiveZFacing(tex);
    }

    @Override
    public void renderNegativeXFacing(ITexture[] tex) {
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (!renderBlocks.partialRenderBounds && !renderBlocks.renderAllFaces
            && !block.shouldSideBeRendered(blockAccess, x - 1, y, z, 4)) return;
        setupLightingXNeg();
        super.renderNegativeXFacing(tex);
    }

    @Override
    public void renderPositiveXFacing(ITexture[] tex) {
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (!renderBlocks.partialRenderBounds && !renderBlocks.renderAllFaces
            && !block.shouldSideBeRendered(blockAccess, x + 1, y, z, 5)) return;
        setupLightingXPos();
        super.renderPositiveXFacing(tex);
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side     the side
     * @param hexColor the primary color
     */
    @Override
    public ISBRWorldContext setupColor(ForgeDirection side, int hexColor) {
        final float lightness = hasLightnessOverride ? lightnessOverride : LIGHTNESS[side.ordinal()];
        final int color = hasColorOverride ? colorOverride : hexColor;

        final float baseRed = (color >> 16 & 0xff) / 255.0F;
        final float baseGreen = (color >> 8 & 0xff) / 255.0F;
        final float baseBlue = (color & 0xff) / 255.0F;

        final float red, green, blue;

        if (EntityRenderer.anaglyphEnable) {
            red = (baseRed * 30.0F + baseGreen * 59.0F + baseBlue * 11.0F) / 100.0F;
            green = (red * 30.0F + baseGreen * 70.0F) / 100.0F;
            blue = (red * 30.0F + baseBlue * 70.0F) / 100.0F;
        } else {
            red = baseRed;
            green = baseGreen;
            blue = baseBlue;
        }

        final Tessellator tessellator = Tessellator.instance;
        if (renderBlocks.enableAO) {
            tessellator.setBrightness(hasBrightnessOverride ? brightnessOverride : brightness);

            if (renderBlocks.hasOverrideBlockTexture()) {

                renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = red;
                renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = green;
                renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = blue;

            } else {

                renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = red
                    * lightness;
                renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = green
                    * lightness;
                renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = blue
                    * lightness;

                renderBlocks.colorRedTopLeft *= aoTopLeft;
                renderBlocks.colorGreenTopLeft *= aoTopLeft;
                renderBlocks.colorBlueTopLeft *= aoTopLeft;
                renderBlocks.colorRedBottomLeft *= aoBottomLeft;
                renderBlocks.colorGreenBottomLeft *= aoBottomLeft;
                renderBlocks.colorBlueBottomLeft *= aoBottomLeft;
                renderBlocks.colorRedBottomRight *= aoBottomRight;
                renderBlocks.colorGreenBottomRight *= aoBottomRight;
                renderBlocks.colorBlueBottomRight *= aoBottomRight;
                renderBlocks.colorRedTopRight *= aoTopRight;
                renderBlocks.colorGreenTopRight *= aoTopRight;
                renderBlocks.colorBlueTopRight *= aoTopRight;
            }
        } else {
            if (hasBrightnessOverride) tessellator.setBrightness(brightnessOverride);
            tessellator.setColorOpaque_F(red * lightness, green * lightness, blue * lightness);
        }
        return this;
    }

    /**
     * Optionally loads the {@code blockrenderer6343.client.world.DummyWorld} class
     */
    private static final Class<?> BR63430_DUMMY_WORLD_CLASS;
    static {
        Class<?> c = null;
        try {
            c = Class.forName("blockrenderer6343.client.world.DummyWorld");
        } catch (ClassNotFoundException ignored) {}
        BR63430_DUMMY_WORLD_CLASS = c;
    }

    /**
     * Performs an optional instanceof check
     * 
     * @param blockAccess the world access interface to check
     * @return {@code true} if {@code blockAccess instanceof blockrenderer6343.client.world.DummyWorld}
     */
    private static boolean isBlockRenderer6343DummyWorld(IBlockAccess blockAccess) {
        return BR63430_DUMMY_WORLD_CLASS != null && BR63430_DUMMY_WORLD_CLASS.isInstance(blockAccess);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Check against the world render pass
     */
    @Override
    public boolean canRenderInPass(@NotNull IntPredicate predicate) {
        return predicate.test(worldRenderPass) || isBlockRenderer6343DummyWorld(blockAccess);
    }

    /**
     * @see #setupLightingXNeg()
     * @see #setupLightingYNeg()
     * @see #setupLightingZNeg()
     * @see #setupLightingXPos()
     * @see #setupLightingYPos()
     * @see #setupLightingZPos()
     */
    @Override
    public ISBRWorldContext setupLighting(ForgeDirection facing) {
        return switch (facing) {
            case DOWN -> setupLightingYNeg();
            case UP -> setupLightingYPos();
            case NORTH -> setupLightingZNeg();
            case SOUTH -> setupLightingZPos();
            case WEST -> setupLightingXNeg();
            case EAST -> setupLightingXPos();
            default -> throw new IllegalArgumentException("Unknown side: " + facing);
        };
    }

    /**
     * Sets up lighting for the bottom face and returns the {@link SBRWorldContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @return the {@link SBRWorldContext}
     */
    private ISBRWorldContext setupLightingYNeg() {
        final int[][][] MBFB = this.MBFB;
        // Use neighbor light if face is flush with neighbor, otherwise current block brightness
        final int iY = block.getBlockBoundsMinY() < FLUSH_MIN ? 0 : 1;
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (renderBlocks.enableAO) {
            final float[][][] AOLV = this.AOLV;
            final double renderMinY = renderBlocks.renderMinY;
            final double renderMinZ = renderBlocks.renderMinZ;
            final double renderMaxZ = renderBlocks.renderMaxZ;
            final double renderMinX = renderBlocks.renderMinX;
            final double renderMaxX = renderBlocks.renderMaxX;

            final int mixedBrightness = MBFB[1][iY][1];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderMinY;
            final float aoLightValue = AOLV[1][0][1];

            // spotless:off
            final int aoBrightnessXYNN = MBFB[0][iY][1]; renderBlocks.aoBrightnessXYNN = aoBrightnessXYNN;
            final int aoBrightnessYZNN = MBFB[1][iY][0]; renderBlocks.aoBrightnessYZNN = aoBrightnessYZNN;
            final int aoBrightnessYZNP = MBFB[1][iY][2]; renderBlocks.aoBrightnessYZNP = aoBrightnessYZNP;
            final int aoBrightnessXYPN = MBFB[2][iY][1]; renderBlocks.aoBrightnessXYPN = aoBrightnessXYPN;
            final int aoBrightnessXYZNNN = MBFB[0][iY][0]; renderBlocks.aoBrightnessXYZNNN = aoBrightnessXYZNNN;
            final int aoBrightnessXYZNNP = MBFB[0][iY][2]; renderBlocks.aoBrightnessXYZNNP = aoBrightnessXYZNNP;
            final int aoBrightnessXYZPNN = MBFB[2][iY][0]; renderBlocks.aoBrightnessXYZPNN = aoBrightnessXYZPNN;
            final int aoBrightnessXYZPNP = MBFB[2][iY][2]; renderBlocks.aoBrightnessXYZPNP = aoBrightnessXYZPNP;
            final float aoLightValueScratchXYNN = getMixedAo(AOLV[0][0][1], AOLV[0][1][1], ratio); renderBlocks.aoLightValueScratchXYNN = aoLightValueScratchXYNN;
            final float aoLightValueScratchYZNN = getMixedAo(AOLV[1][0][0], AOLV[1][1][0], ratio); renderBlocks.aoLightValueScratchYZNN = aoLightValueScratchYZNN;
            final float aoLightValueScratchYZNP = getMixedAo(AOLV[1][0][2], AOLV[1][1][2], ratio); renderBlocks.aoLightValueScratchYZNP = aoLightValueScratchYZNP;
            final float aoLightValueScratchXYPN = getMixedAo(AOLV[2][0][1], AOLV[2][1][1], ratio); renderBlocks.aoLightValueScratchXYPN = aoLightValueScratchXYPN;
            final float aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[0][1][0], ratio); renderBlocks.aoLightValueScratchXYZNNN = aoLightValueScratchXYZNNN;
            final float aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[0][1][2], ratio); renderBlocks.aoLightValueScratchXYZNNP = aoLightValueScratchXYZNNP;
            final float aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[2][1][0], ratio); renderBlocks.aoLightValueScratchXYZPNN = aoLightValueScratchXYZPNN;
            final float aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[2][1][2], ratio); renderBlocks.aoLightValueScratchXYZPNP = aoLightValueScratchXYZPNP;
            // spotless:on

            final int brightnessMixedXYZPNP = renderBlocks
                .getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, mixedBrightness);
            final int brightnessMixedXYZPNN = renderBlocks
                .getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, mixedBrightness);
            final int brightnessMixedXYZNNN = renderBlocks
                .getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, mixedBrightness);
            final int brightnessMixedXYZNNP = renderBlocks
                .getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, mixedBrightness);

            final float aoMixedXYZPNP = (aoLightValueScratchYZNP + aoLightValue
                + aoLightValueScratchXYZPNP
                + aoLightValueScratchXYPN) / 4.0F;
            final float aoMixedXYZPNN = (aoLightValue + aoLightValueScratchYZNN
                + aoLightValueScratchXYPN
                + aoLightValueScratchXYZPNN) / 4.0F;
            final float aoMixedXYZNNN = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN
                + aoLightValue
                + aoLightValueScratchYZNN) / 4.0F;
            final float aoMixedXYZNNP = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN
                + aoLightValueScratchYZNP
                + aoLightValue) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNNP * renderMaxZ * (1.0D - renderMinX)
                + aoMixedXYZPNP * renderMaxZ * renderMinX
                + aoMixedXYZPNN * (1.0D - renderMaxZ) * renderMinX
                + aoMixedXYZNNN * (1.0D - renderMaxZ) * (1.0D - renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNNP * renderMinZ * (1.0D - renderMinX)
                + aoMixedXYZPNP * renderMinZ * renderMinX
                + aoMixedXYZPNN * (1.0D - renderMinZ) * renderMinX
                + aoMixedXYZNNN * (1.0D - renderMinZ) * (1.0D - renderMinX));
            aoBottomRight = (float) (aoMixedXYZNNP * renderMinZ * (1.0D - renderMaxX)
                + aoMixedXYZPNP * renderMinZ * renderMaxX
                + aoMixedXYZPNN * (1.0D - renderMinZ) * renderMaxX
                + aoMixedXYZNNN * (1.0D - renderMinZ) * (1.0D - renderMaxX));
            aoTopRight = (float) (aoMixedXYZNNP * renderMaxZ * (1.0D - renderMaxX)
                + aoMixedXYZPNP * renderMaxZ * renderMaxX
                + aoMixedXYZPNN * (1.0D - renderMaxZ) * renderMaxX
                + aoMixedXYZNNN * (1.0D - renderMaxZ) * (1.0D - renderMaxX));

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderMaxZ * (1.0D - renderMinX),
                renderMaxZ * renderMinX,
                (1.0D - renderMaxZ) * renderMinX,
                (1.0D - renderMaxZ) * (1.0D - renderMinX));
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderMinZ * (1.0D - renderMinX),
                renderMinZ * renderMinX,
                (1.0D - renderMinZ) * renderMinX,
                (1.0D - renderMinZ) * (1.0D - renderMinX));
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderMinZ * (1.0D - renderMaxX),
                renderMinZ * renderMaxX,
                (1.0D - renderMinZ) * renderMaxX,
                (1.0D - renderMinZ) * (1.0D - renderMaxX));
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderMaxZ * (1.0D - renderMaxX),
                renderMaxZ * renderMaxX,
                (1.0D - renderMaxZ) * renderMaxX,
                (1.0D - renderMaxZ) * (1.0D - renderMaxX));
        } else {
            Tessellator.instance.setBrightness(MBFB[1][iY][1]);
        }

        return this;
    }

    /**
     * Sets up lighting for the top face and returns the {@link SBRWorldContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @return the {@link SBRWorldContext}
     */
    private ISBRWorldContext setupLightingYPos() {
        final int[][][] MBFB = this.MBFB;
        // Use neighbor light if face is flush with neighbor, otherwise current block brightness
        final int iY = block.getBlockBoundsMaxY() > FLUSH_MAX ? 2 : 1;
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (renderBlocks.enableAO) {
            final float[][][] AOLV = this.AOLV;
            final double renderMaxY = renderBlocks.renderMaxY;
            final double renderMinZ = renderBlocks.renderMinZ;
            final double renderMaxZ = renderBlocks.renderMaxZ;
            final double renderMinX = renderBlocks.renderMinX;
            final double renderMaxX = renderBlocks.renderMaxX;

            final int mixedBrightness = MBFB[1][iY][1];
            brightness = mixedBrightness;

            @SuppressWarnings("UnnecessaryLocalVariable") // Clarity
            final double ratio = renderMaxY;
            final float aoLightValue = AOLV[1][2][1];

            // spotless:off
            final int aoBrightnessXYNP = MBFB[0][iY][1]; renderBlocks.aoBrightnessXYNP = aoBrightnessXYNP;
            final int aoBrightnessXYPP = MBFB[2][iY][1]; renderBlocks.aoBrightnessXYPP = aoBrightnessXYPP;
            final int aoBrightnessYZPN = MBFB[1][iY][0]; renderBlocks.aoBrightnessYZPN = aoBrightnessYZPN;
            final int aoBrightnessYZPP = MBFB[1][iY][2]; renderBlocks.aoBrightnessYZPP = aoBrightnessYZPP;
            final int aoBrightnessXYZNPN = MBFB[0][iY][0]; renderBlocks.aoBrightnessXYZNPN = aoBrightnessXYZNPN;
            final int aoBrightnessXYZPPN = MBFB[2][iY][0]; renderBlocks.aoBrightnessXYZPPN = aoBrightnessXYZPPN;
            final int aoBrightnessXYZNPP = MBFB[0][iY][2]; renderBlocks.aoBrightnessXYZNPP = aoBrightnessXYZNPP;
            final int aoBrightnessXYZPPP = MBFB[2][iY][2]; renderBlocks.aoBrightnessXYZPPP = aoBrightnessXYZPPP;
            final float aoLightValueScratchXYNP = getMixedAo(AOLV[0][2][1], AOLV[0][1][1], ratio); renderBlocks.aoLightValueScratchXYNP = aoLightValueScratchXYNP;
            final float aoLightValueScratchXYPP = getMixedAo(AOLV[2][2][1], AOLV[2][1][1], ratio); renderBlocks.aoLightValueScratchXYPP = aoLightValueScratchXYPP;
            final float aoLightValueScratchYZPN = getMixedAo(AOLV[1][2][0], AOLV[1][1][0], ratio); renderBlocks.aoLightValueScratchYZPN = aoLightValueScratchYZPN;
            final float aoLightValueScratchYZPP = getMixedAo(AOLV[1][2][2], AOLV[1][1][2], ratio); renderBlocks.aoLightValueScratchYZPP = aoLightValueScratchYZPP;
            final float aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[0][1][0], ratio); renderBlocks.aoLightValueScratchXYZNPN = aoLightValueScratchXYZNPN;
            final float aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[2][1][0], ratio); renderBlocks.aoLightValueScratchXYZPPN = aoLightValueScratchXYZPPN;
            final float aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[0][1][2], ratio); renderBlocks.aoLightValueScratchXYZNPP = aoLightValueScratchXYZNPP;
            final float aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[2][1][2], ratio); renderBlocks.aoLightValueScratchXYZPPP = aoLightValueScratchXYZPPP;
            // spotless:on

            final int brightnessMixedXYZPPP = renderBlocks
                .getAoBrightness(aoBrightnessYZPP, aoBrightnessXYZPPP, aoBrightnessXYPP, mixedBrightness);
            final int brightnessMixedXYZPPN = renderBlocks
                .getAoBrightness(aoBrightnessYZPN, aoBrightnessXYPP, aoBrightnessXYZPPN, mixedBrightness);
            final int brightnessMixedXYZNPN = renderBlocks
                .getAoBrightness(aoBrightnessXYNP, aoBrightnessXYZNPN, aoBrightnessYZPN, mixedBrightness);
            final int brightnessMixedXYZNPP = renderBlocks
                .getAoBrightness(aoBrightnessXYZNPP, aoBrightnessXYNP, aoBrightnessYZPP, mixedBrightness);

            final float aoMixedXYZPPP = (aoLightValueScratchYZPP + aoLightValue
                + aoLightValueScratchXYZPPP
                + aoLightValueScratchXYPP) / 4.0F;
            final float aoMixedXYZPPN = (aoLightValue + aoLightValueScratchYZPN
                + aoLightValueScratchXYPP
                + aoLightValueScratchXYZPPN) / 4.0F;
            final float aoMixedXYZNPN = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN
                + aoLightValue
                + aoLightValueScratchYZPN) / 4.0F;
            final float aoMixedXYZNPP = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP
                + aoLightValueScratchYZPP
                + aoLightValue) / 4.0F;

            aoTopLeft /* SE */ = (float) (aoMixedXYZNPP * renderMaxZ * (1.0D - renderMaxX)
                + aoMixedXYZPPP * renderMaxZ * renderMaxX
                + aoMixedXYZPPN * (1.0D - renderMaxZ) * renderMaxX
                + aoMixedXYZNPN * (1.0D - renderMaxZ) * (1.0D - renderMaxX));
            aoBottomLeft /* NE */ = (float) (aoMixedXYZNPP * renderMinZ * (1.0D - renderMaxX)
                + aoMixedXYZPPP * renderMinZ * renderMaxX
                + aoMixedXYZPPN * (1.0D - renderMinZ) * renderMaxX
                + aoMixedXYZNPN * (1.0D - renderMinZ) * (1.0D - renderMaxX));
            aoBottomRight /* NW */ = (float) (aoMixedXYZNPP * renderMinZ * (1.0D - renderMinX)
                + aoMixedXYZPPP * renderMinZ * renderMinX
                + aoMixedXYZPPN * (1.0D - renderMinZ) * renderMinX
                + aoMixedXYZNPN * (1.0D - renderMinZ) * (1.0D - renderMinX));
            aoTopRight /* SW */ = (float) (aoMixedXYZNPP * renderMaxZ * (1.0D - renderMinX)
                + aoMixedXYZPPP * renderMaxZ * renderMinX
                + aoMixedXYZPPN * (1.0D - renderMaxZ) * renderMinX
                + aoMixedXYZNPN * (1.0D - renderMaxZ) * (1.0D - renderMinX));

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderMaxZ * (1.0D - renderMaxX),
                renderMaxZ * renderMaxX,
                (1.0D - renderMaxZ) * renderMaxX,
                (1.0D - renderMaxZ) * (1.0D - renderMaxX));
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderMinZ * (1.0D - renderMaxX),
                renderMinZ * renderMaxX,
                (1.0D - renderMinZ) * renderMaxX,
                (1.0D - renderMinZ) * (1.0D - renderMaxX));
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderMinZ * (1.0D - renderMinX),
                renderMinZ * renderMinX,
                (1.0D - renderMinZ) * renderMinX,
                (1.0D - renderMinZ) * (1.0D - renderMinX));
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderMaxZ * (1.0D - renderMinX),
                renderMaxZ * renderMinX,
                (1.0D - renderMaxZ) * renderMinX,
                (1.0D - renderMaxZ) * (1.0D - renderMinX));
        } else {
            Tessellator.instance.setBrightness(MBFB[1][iY][1]);
        }

        return this;
    }

    /**
     * Sets up lighting for the North face and returns the {@link SBRWorldContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @return the {@link SBRWorldContext}
     */
    private ISBRWorldContext setupLightingZNeg() {
        final int[][][] MBFB = this.MBFB;
        // Use neighbor light if face is flush with neighbor, otherwise current block brightness
        final int iZ = block.getBlockBoundsMinZ() < FLUSH_MIN ? 0 : 1;
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (renderBlocks.enableAO) {
            final float[][][] AOLV = this.AOLV;
            final double renderMinY = renderBlocks.renderMinY;
            final double renderMaxY = renderBlocks.renderMaxY;
            final double renderMinZ = renderBlocks.renderMinZ;
            final double renderMinX = renderBlocks.renderMinX;
            final double renderMaxX = renderBlocks.renderMaxX;

            final int mixedBrightness = MBFB[1][1][iZ];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderMinZ;
            final float aoLightValue = AOLV[1][1][0];

            // spotless:off
            final int aoBrightnessXZNN = MBFB[0][1][iZ]; renderBlocks.aoBrightnessXZNN = aoBrightnessXZNN;
            final int aoBrightnessYZNN = MBFB[1][0][iZ]; renderBlocks.aoBrightnessYZNN = aoBrightnessYZNN;
            final int aoBrightnessYZPN = MBFB[1][2][iZ]; renderBlocks.aoBrightnessYZPN = aoBrightnessYZPN;
            final int aoBrightnessXZPN = MBFB[2][1][iZ]; renderBlocks.aoBrightnessXZPN = aoBrightnessXZPN;
            final int aoBrightnessXYZNNN = MBFB[0][0][iZ]; renderBlocks.aoBrightnessXYZNNN = aoBrightnessXYZNNN;
            final int aoBrightnessXYZNPN = MBFB[0][2][iZ]; renderBlocks.aoBrightnessXYZNPN = aoBrightnessXYZNPN;
            final int aoBrightnessXYZPNN = MBFB[2][0][iZ]; renderBlocks.aoBrightnessXYZPNN = aoBrightnessXYZPNN;
            final int aoBrightnessXYZPPN = MBFB[2][2][iZ]; renderBlocks.aoBrightnessXYZPPN = aoBrightnessXYZPPN;
            final float aoLightValueScratchXZNN = getMixedAo(AOLV[0][1][0], AOLV[0][1][1], ratio); renderBlocks.aoLightValueScratchXZNN = aoLightValueScratchXZNN;
            final float aoLightValueScratchYZNN = getMixedAo(AOLV[1][0][0], AOLV[1][0][1], ratio); renderBlocks.aoLightValueScratchYZNN = aoLightValueScratchYZNN;
            final float aoLightValueScratchYZPN = getMixedAo(AOLV[1][2][0], AOLV[1][2][1], ratio); renderBlocks.aoLightValueScratchYZPN = aoLightValueScratchYZPN;
            final float aoLightValueScratchXZPN = getMixedAo(AOLV[2][1][0], AOLV[2][1][1], ratio); renderBlocks.aoLightValueScratchXZPN = aoLightValueScratchXZPN;
            final float aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[0][0][1], ratio); renderBlocks.aoLightValueScratchXYZNNN = aoLightValueScratchXYZNNN;
            final float aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[0][2][1], ratio); renderBlocks.aoLightValueScratchXYZNPN = aoLightValueScratchXYZNPN;
            final float aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[2][0][1], ratio); renderBlocks.aoLightValueScratchXYZPNN = aoLightValueScratchXYZPNN;
            final float aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[2][2][1], ratio); renderBlocks.aoLightValueScratchXYZPPN = aoLightValueScratchXYZPPN;
            // spotless:on

            final int brightnessMixedXYZPPN = renderBlocks
                .getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN, aoBrightnessXYZPPN, mixedBrightness);
            final int brightnessMixedXYZPNN = renderBlocks
                .getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN, aoBrightnessXZPN, mixedBrightness);
            final int brightnessMixedXYZNNN = renderBlocks
                .getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN, aoBrightnessYZNN, mixedBrightness);
            final int brightnessMixedXYZNPN = renderBlocks
                .getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessYZPN, mixedBrightness);

            final float aoMixedXYZPPN = (aoLightValue + aoLightValueScratchYZPN
                + aoLightValueScratchXZPN
                + aoLightValueScratchXYZPPN) / 4.0F;
            final float aoMixedXYZPNN = (aoLightValueScratchYZNN + aoLightValue
                + aoLightValueScratchXYZPNN
                + aoLightValueScratchXZPN) / 4.0F;
            final float aoMixedXYZNNN = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN
                + aoLightValueScratchYZNN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZNPN = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN
                + aoLightValue
                + aoLightValueScratchYZPN) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPN * renderMaxY * (1.0D - renderMinX)
                + aoMixedXYZPPN * renderMaxY * renderMinX
                + aoMixedXYZPNN * (1.0D - renderMaxY) * renderMinX
                + aoMixedXYZNNN * (1.0D - renderMaxY) * (1.0D - renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNPN * renderMaxY * (1.0D - renderMaxX)
                + aoMixedXYZPPN * renderMaxY * renderMaxX
                + aoMixedXYZPNN * (1.0D - renderMaxY) * renderMaxX
                + aoMixedXYZNNN * (1.0D - renderMaxY) * (1.0D - renderMaxX));
            aoBottomRight = (float) (aoMixedXYZNPN * renderMinY * (1.0D - renderMaxX)
                + aoMixedXYZPPN * renderMinY * renderMaxX
                + aoMixedXYZPNN * (1.0D - renderMinY) * renderMaxX
                + aoMixedXYZNNN * (1.0D - renderMinY) * (1.0D - renderMaxX));
            aoTopRight = (float) (aoMixedXYZNPN * renderMinY * (1.0D - renderMinX)
                + aoMixedXYZPPN * renderMinY * renderMinX
                + aoMixedXYZPNN * (1.0D - renderMinY) * renderMinX
                + aoMixedXYZNNN * (1.0D - renderMinY) * (1.0D - renderMinX));

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderMaxY * (1.0D - renderMinX),
                renderMaxY * renderMinX,
                (1.0D - renderMaxY) * renderMinX,
                (1.0D - renderMaxY) * (1.0D - renderMinX));
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderMaxY * (1.0D - renderMaxX),
                renderMaxY * renderMaxX,
                (1.0D - renderMaxY) * renderMaxX,
                (1.0D - renderMaxY) * (1.0D - renderMaxX));
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderMinY * (1.0D - renderMaxX),
                renderMinY * renderMaxX,
                (1.0D - renderMinY) * renderMaxX,
                (1.0D - renderMinY) * (1.0D - renderMaxX));
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderMinY * (1.0D - renderMinX),
                renderMinY * renderMinX,
                (1.0D - renderMinY) * renderMinX,
                (1.0D - renderMinY) * (1.0D - renderMinX));
        } else {
            Tessellator.instance.setBrightness(MBFB[1][1][iZ]);
        }

        return this;
    }

    /**
     * Sets up lighting for the South face and returns the {@link SBRWorldContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @return the {@link SBRWorldContext}
     */
    private ISBRWorldContext setupLightingZPos() {
        final int[][][] MBFB = this.MBFB;
        // Use neighbor light if face is flush with neighbor, otherwise current block brightness
        final int iZ = block.getBlockBoundsMaxZ() > FLUSH_MAX ? 2 : 1;
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (renderBlocks.enableAO) {
            final float[][][] AOLV = this.AOLV;
            final double renderMinY = renderBlocks.renderMinY;
            final double renderMaxY = renderBlocks.renderMaxY;
            final double renderMaxZ = renderBlocks.renderMaxZ;
            final double renderMinX = renderBlocks.renderMinX;
            final double renderMaxX = renderBlocks.renderMaxX;

            final int mixedBrightness = MBFB[1][1][iZ];
            brightness = mixedBrightness;

            @SuppressWarnings("UnnecessaryLocalVariable") // Clarity
            final double ratio = renderMaxZ;
            final float aoLightValue = AOLV[1][1][2];

            // spotless:off
            final int aoBrightnessXZNP = MBFB[0][1][iZ]; renderBlocks.aoBrightnessXZNP = aoBrightnessXZNP;
            final int aoBrightnessXZPP = MBFB[2][1][iZ]; renderBlocks.aoBrightnessXZPP = aoBrightnessXZPP;
            final int aoBrightnessYZNP = MBFB[1][0][iZ]; renderBlocks.aoBrightnessYZNP = aoBrightnessYZNP;
            final int aoBrightnessYZPP = MBFB[1][2][iZ]; renderBlocks.aoBrightnessYZPP = aoBrightnessYZPP;
            final int aoBrightnessXYZNNP = MBFB[0][0][iZ]; renderBlocks.aoBrightnessXYZNNP = aoBrightnessXYZNNP;
            final int aoBrightnessXYZNPP = MBFB[0][2][iZ]; renderBlocks.aoBrightnessXYZNPP = aoBrightnessXYZNPP;
            final int aoBrightnessXYZPNP = MBFB[2][0][iZ]; renderBlocks.aoBrightnessXYZPNP = aoBrightnessXYZPNP;
            final int aoBrightnessXYZPPP = MBFB[2][2][iZ]; renderBlocks.aoBrightnessXYZPPP = aoBrightnessXYZPPP;
            final float aoLightValueScratchXZNP = getMixedAo(AOLV[0][1][2], AOLV[0][1][1], ratio); renderBlocks.aoLightValueScratchXZNP = aoLightValueScratchXZNP;
            final float aoLightValueScratchXZPP = getMixedAo(AOLV[2][1][2], AOLV[2][1][1], ratio); renderBlocks.aoLightValueScratchXZPP = aoLightValueScratchXZPP;
            final float aoLightValueScratchYZNP = getMixedAo(AOLV[1][0][2], AOLV[1][0][1], ratio); renderBlocks.aoLightValueScratchYZNP = aoLightValueScratchYZNP;
            final float aoLightValueScratchYZPP = getMixedAo(AOLV[1][2][2], AOLV[1][2][1], ratio); renderBlocks.aoLightValueScratchYZPP = aoLightValueScratchYZPP;
            final float aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[0][0][1], ratio); renderBlocks.aoLightValueScratchXYZNNP = aoLightValueScratchXYZNNP;
            final float aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[0][2][1], ratio); renderBlocks.aoLightValueScratchXYZNPP = aoLightValueScratchXYZNPP;
            final float aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[2][0][1], ratio); renderBlocks.aoLightValueScratchXYZPNP = aoLightValueScratchXYZPNP;
            final float aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[2][2][1], ratio); renderBlocks.aoLightValueScratchXYZPPP = aoLightValueScratchXYZPPP;
            // spotless:on

            final int brightnessMixedXYZNPP = renderBlocks
                .getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP, aoBrightnessYZPP, mixedBrightness);
            final int brightnessMixedXYZNNP = renderBlocks
                .getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP, aoBrightnessYZNP, mixedBrightness);
            final int brightnessMixedXYZPNP = renderBlocks
                .getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXZPP, mixedBrightness);
            final int brightnessMixedXYZPPP = renderBlocks
                .getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP, aoBrightnessXYZPPP, mixedBrightness);

            final float aoMixedXYZNPP = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP
                + aoLightValue
                + aoLightValueScratchYZPP) / 4.0F;
            final float aoMixedXYZNNP = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP
                + aoLightValueScratchYZNP
                + aoLightValue) / 4.0F;
            final float aoMixedXYZPNP = (aoLightValueScratchYZNP + aoLightValue
                + aoLightValueScratchXYZPNP
                + aoLightValueScratchXZPP) / 4.0F;
            final float aoMixedXYZPPP = (aoLightValue + aoLightValueScratchYZPP
                + aoLightValueScratchXZPP
                + aoLightValueScratchXYZPPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPP * renderMaxY * (1.0D - renderMinX)
                + aoMixedXYZPPP * renderMaxY * renderMinX
                + aoMixedXYZPNP * (1.0D - renderMaxY) * renderMinX
                + aoMixedXYZNNP * (1.0D - renderMaxY) * (1.0D - renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNPP * renderMinY * (1.0D - renderMinX)
                + aoMixedXYZPPP * renderMinY * renderMinX
                + aoMixedXYZPNP * (1.0D - renderMinY) * renderMinX
                + aoMixedXYZNNP * (1.0D - renderMinY) * (1.0D - renderMinX));
            aoBottomRight = (float) (aoMixedXYZNPP * renderMinY * (1.0D - renderMaxX)
                + aoMixedXYZPPP * renderMinY * renderMaxX
                + aoMixedXYZPNP * (1.0D - renderMinY) * renderMaxX
                + aoMixedXYZNNP * (1.0D - renderMinY) * (1.0D - renderMaxX));
            aoTopRight = (float) (aoMixedXYZNPP * renderMaxY * (1.0D - renderMaxX)
                + aoMixedXYZPPP * renderMaxY * renderMaxX
                + aoMixedXYZPNP * (1.0D - renderMaxY) * renderMaxX
                + aoMixedXYZNNP * (1.0D - renderMaxY) * (1.0D - renderMaxX));

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderMaxY * (1.0D - renderMinX),
                (1.0D - renderMaxY) * (1.0D - renderMinX),
                (1.0D - renderMaxY) * renderMinX,
                renderMaxY * renderMinX);
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderMinY * (1.0D - renderMinX),
                (1.0D - renderMinY) * (1.0D - renderMinX),
                (1.0D - renderMinY) * renderMinX,
                renderMinY * renderMinX);
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderMinY * (1.0D - renderMaxX),
                (1.0D - renderMinY) * (1.0D - renderMaxX),
                (1.0D - renderMinY) * renderMaxX,
                renderMinY * renderMaxX);
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderMaxY * (1.0D - renderMaxX),
                (1.0D - renderMaxY) * (1.0D - renderMaxX),
                (1.0D - renderMaxY) * renderMaxX,
                renderMaxY * renderMaxX);
        } else {
            Tessellator.instance.setBrightness(MBFB[1][1][iZ]);
        }

        return this;
    }

    /**
     * Sets up lighting for the West face and returns the {@link SBRWorldContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @return the {@link SBRWorldContext}
     */
    private ISBRWorldContext setupLightingXNeg() {
        final int[][][] MBFB = this.MBFB;
        // Use neighbor light if face is flush with neighbor, otherwise current block brightness
        final int iX = block.getBlockBoundsMinX() < FLUSH_MIN ? 0 : 1;
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (renderBlocks.enableAO) {
            final float[][][] AOLV = this.AOLV;
            final double renderMinY = renderBlocks.renderMinY;
            final double renderMaxY = renderBlocks.renderMaxY;
            final double renderMinZ = renderBlocks.renderMinZ;
            final double renderMaxZ = renderBlocks.renderMaxZ;
            final double renderMinX = renderBlocks.renderMinX;

            final int mixedBrightness = MBFB[iX][1][1];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderMinX;
            final float aoLightValue = AOLV[0][1][1];

            // spotless:off
            final int aoBrightnessXYNN = MBFB[iX][0][1]; renderBlocks.aoBrightnessXYNN = aoBrightnessXYNN;
            final int aoBrightnessXZNN = MBFB[iX][1][0]; renderBlocks.aoBrightnessXZNN = aoBrightnessXZNN;
            final int aoBrightnessXZNP = MBFB[iX][1][2]; renderBlocks.aoBrightnessXZNP = aoBrightnessXZNP;
            final int aoBrightnessXYNP = MBFB[iX][2][1]; renderBlocks.aoBrightnessXYNP = aoBrightnessXYNP;
            final int aoBrightnessXYZNNN = MBFB[iX][0][0]; renderBlocks.aoBrightnessXYZNNN = aoBrightnessXYZNNN;
            final int aoBrightnessXYZNNP = MBFB[iX][0][2]; renderBlocks.aoBrightnessXYZNNP = aoBrightnessXYZNNP;
            final int aoBrightnessXYZNPN = MBFB[iX][2][0]; renderBlocks.aoBrightnessXYZNPN = aoBrightnessXYZNPN;
            final int aoBrightnessXYZNPP = MBFB[iX][2][2]; renderBlocks.aoBrightnessXYZNPP = aoBrightnessXYZNPP;
            final float aoLightValueScratchXYNN = getMixedAo(AOLV[0][0][1], AOLV[1][0][1], ratio); renderBlocks.aoLightValueScratchXYNN = aoLightValueScratchXYNN;
            final float aoLightValueScratchXZNN = getMixedAo(AOLV[0][1][0], AOLV[1][1][0], ratio); renderBlocks.aoLightValueScratchXZNN = aoLightValueScratchXZNN;
            final float aoLightValueScratchXZNP = getMixedAo(AOLV[0][1][2], AOLV[1][1][2], ratio); renderBlocks.aoLightValueScratchXZNP = aoLightValueScratchXZNP;
            final float aoLightValueScratchXYNP = getMixedAo(AOLV[0][2][1], AOLV[1][2][1], ratio); renderBlocks.aoLightValueScratchXYNP = aoLightValueScratchXYNP;
            final float aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[1][0][0], ratio); renderBlocks.aoLightValueScratchXYZNNN = aoLightValueScratchXYZNNN;
            final float aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[1][0][2], ratio); renderBlocks.aoLightValueScratchXYZNNP = aoLightValueScratchXYZNNP;
            final float aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[1][2][0], ratio); renderBlocks.aoLightValueScratchXYZNPN = aoLightValueScratchXYZNPN;
            final float aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[1][2][2], ratio); renderBlocks.aoLightValueScratchXYZNPP = aoLightValueScratchXYZNPP;
            // spotless:on

            final int brightnessMixedXYZNPN = renderBlocks
                .getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessXYNP, mixedBrightness);
            final int brightnessMixedXYZNNN = renderBlocks
                .getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN, aoBrightnessXZNN, mixedBrightness);
            final int brightnessMixedXYZNNP = renderBlocks
                .getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP, aoBrightnessXZNP, mixedBrightness);
            final int brightnessMixedXYZNPP = renderBlocks
                .getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP, aoBrightnessXYZNPP, mixedBrightness);

            final float aoMixedXYZNPN = (aoLightValueScratchXZNN + aoLightValue
                + aoLightValueScratchXYZNPN
                + aoLightValueScratchXYNP) / 4.0F;
            final float aoMixedXYZNNN = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN
                + aoLightValueScratchXZNN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZNNP = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP
                + aoLightValue
                + aoLightValueScratchXZNP) / 4.0F;
            final float aoMixedXYZNPP = (aoLightValue + aoLightValueScratchXZNP
                + aoLightValueScratchXYNP
                + aoLightValueScratchXYZNPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPP * renderMaxY * renderMaxZ
                + aoMixedXYZNPN * renderMaxY * (1.0D - renderMaxZ)
                + aoMixedXYZNNN * (1.0D - renderMaxY) * (1.0D - renderMaxZ)
                + aoMixedXYZNNP * (1.0D - renderMaxY) * renderMaxZ);
            aoBottomLeft = (float) (aoMixedXYZNPP * renderMaxY * renderMinZ
                + aoMixedXYZNPN * renderMaxY * (1.0D - renderMinZ)
                + aoMixedXYZNNN * (1.0D - renderMaxY) * (1.0D - renderMinZ)
                + aoMixedXYZNNP * (1.0D - renderMaxY) * renderMinZ);
            aoBottomRight = (float) (aoMixedXYZNPP * renderMinY * renderMinZ
                + aoMixedXYZNPN * renderMinY * (1.0D - renderMinZ)
                + aoMixedXYZNNN * (1.0D - renderMinY) * (1.0D - renderMinZ)
                + aoMixedXYZNNP * (1.0D - renderMinY) * renderMinZ);
            aoTopRight = (float) (aoMixedXYZNPP * renderMinY * renderMaxZ
                + aoMixedXYZNPN * renderMinY * (1.0D - renderMaxZ)
                + aoMixedXYZNNN * (1.0D - renderMinY) * (1.0D - renderMaxZ)
                + aoMixedXYZNNP * (1.0D - renderMinY) * renderMaxZ);

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderMaxY * renderMaxZ,
                renderMaxY * (1.0D - renderMaxZ),
                (1.0D - renderMaxY) * (1.0D - renderMaxZ),
                (1.0D - renderMaxY) * renderMaxZ);
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderMaxY * renderMinZ,
                renderMaxY * (1.0D - renderMinZ),
                (1.0D - renderMaxY) * (1.0D - renderMinZ),
                (1.0D - renderMaxY) * renderMinZ);
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderMinY * renderMinZ,
                renderMinY * (1.0D - renderMinZ),
                (1.0D - renderMinY) * (1.0D - renderMinZ),
                (1.0D - renderMinY) * renderMinZ);
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderMinY * renderMaxZ,
                renderMinY * (1.0D - renderMaxZ),
                (1.0D - renderMinY) * (1.0D - renderMaxZ),
                (1.0D - renderMinY) * renderMaxZ);
        } else {
            Tessellator.instance.setBrightness(MBFB[iX][1][1]);
        }

        return this;
    }

    /**
     * Sets up lighting for the East face and returns the {@link SBRWorldContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @return the {@link SBRWorldContext}
     */
    private ISBRWorldContext setupLightingXPos() {
        final int[][][] MBFB = this.MBFB;
        // Use neighbor light if face is flush with neighbor, otherwise current block brightness
        final int iX = block.getBlockBoundsMaxX() > FLUSH_MAX ? 2 : 1;
        final RenderBlocks renderBlocks = this.renderBlocks;
        if (renderBlocks.enableAO) {
            final float[][][] AOLV = this.AOLV;
            final double renderMinY = renderBlocks.renderMinY;
            final double renderMaxY = renderBlocks.renderMaxY;
            final double renderMinZ = renderBlocks.renderMinZ;
            final double renderMaxZ = renderBlocks.renderMaxZ;
            final double renderMaxX = renderBlocks.renderMaxX;

            final int mixedBrightness = MBFB[iX][1][1];
            brightness = mixedBrightness;

            @SuppressWarnings("UnnecessaryLocalVariable") // Clarity
            final double ratio = renderMaxX;
            final float aoLightValue = AOLV[2][1][1];

            // spotless:off
            final int aoBrightnessXYPN = MBFB[iX][0][1]; renderBlocks.aoBrightnessXYPN = aoBrightnessXYPN;
            final int aoBrightnessXZPN = MBFB[iX][1][0]; renderBlocks.aoBrightnessXZPN = aoBrightnessXZPN;
            final int aoBrightnessXZPP = MBFB[iX][1][2]; renderBlocks.aoBrightnessXZPP = aoBrightnessXZPP;
            final int aoBrightnessXYPP = MBFB[iX][2][1]; renderBlocks.aoBrightnessXYPP = aoBrightnessXYPP;
            final int aoBrightnessXYZPNN = MBFB[iX][0][0]; renderBlocks.aoBrightnessXYZPNN = aoBrightnessXYZPNN;
            final int aoBrightnessXYZPNP = MBFB[iX][0][2]; renderBlocks.aoBrightnessXYZPNP = aoBrightnessXYZPNP;
            final int aoBrightnessXYZPPN = MBFB[iX][2][0]; renderBlocks.aoBrightnessXYZPPN = aoBrightnessXYZPPN;
            final int aoBrightnessXYZPPP = MBFB[iX][2][2]; renderBlocks.aoBrightnessXYZPPP = aoBrightnessXYZPPP;
            final float aoLightValueScratchXYPN = getMixedAo(AOLV[2][0][1], AOLV[1][0][1], ratio); renderBlocks.aoLightValueScratchXYPN = aoLightValueScratchXYPN;
            final float aoLightValueScratchXZPN = getMixedAo(AOLV[2][1][0], AOLV[1][1][0], ratio); renderBlocks.aoLightValueScratchXZPN = aoLightValueScratchXZPN;
            final float aoLightValueScratchXZPP = getMixedAo(AOLV[2][1][2], AOLV[1][1][2], ratio); renderBlocks.aoLightValueScratchXZPP = aoLightValueScratchXZPP;
            final float aoLightValueScratchXYPP = getMixedAo(AOLV[2][2][1], AOLV[1][2][1], ratio); renderBlocks.aoLightValueScratchXYPP = aoLightValueScratchXYPP;
            final float aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[1][0][0], ratio); renderBlocks.aoLightValueScratchXYZPNN = aoLightValueScratchXYZPNN;
            final float aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[1][0][2], ratio); renderBlocks.aoLightValueScratchXYZPNP = aoLightValueScratchXYZPNP;
            final float aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[1][2][0], ratio); renderBlocks.aoLightValueScratchXYZPPN = aoLightValueScratchXYZPPN;
            final float aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[1][2][2], ratio); renderBlocks.aoLightValueScratchXYZPPP = aoLightValueScratchXYZPPP;
            // spotless:on

            final int brightnessMixedXYZPPP = renderBlocks
                .getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, mixedBrightness);
            final int brightnessMixedXYZPNP = renderBlocks
                .getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP, aoBrightnessXZPP, mixedBrightness);
            final int brightnessMixedXYZPNN = renderBlocks
                .getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN, aoBrightnessXZPN, mixedBrightness);
            final int brightnessMixedXYZPPN = renderBlocks
                .getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, mixedBrightness);

            final float aoMixedXYZPPP = (aoLightValue + aoLightValueScratchXZPP
                + aoLightValueScratchXYPP
                + aoLightValueScratchXYZPPP) / 4.0F;
            final float aoMixedXYZPNP = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP
                + aoLightValue
                + aoLightValueScratchXZPP) / 4.0F;
            final float aoMixedXYZPNN = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN
                + aoLightValueScratchXZPN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZPPN = (aoLightValueScratchXZPN + aoLightValue
                + aoLightValueScratchXYZPPN
                + aoLightValueScratchXYPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZPNP * (1.0D - renderMinY) * renderMaxZ
                + aoMixedXYZPNN * (1.0D - renderMinY) * (1.0D - renderMaxZ)
                + aoMixedXYZPPN * renderMinY * (1.0D - renderMaxZ)
                + aoMixedXYZPPP * renderMinY * renderMaxZ);
            aoBottomLeft = (float) (aoMixedXYZPNP * (1.0D - renderMinY) * renderMinZ
                + aoMixedXYZPNN * (1.0D - renderMinY) * (1.0D - renderMinZ)
                + aoMixedXYZPPN * renderMinY * (1.0D - renderMinZ)
                + aoMixedXYZPPP * renderMinY * renderMinZ);
            aoBottomRight = (float) (aoMixedXYZPNP * (1.0D - renderMaxY) * renderMinZ
                + aoMixedXYZPNN * (1.0D - renderMaxY) * (1.0D - renderMinZ)
                + aoMixedXYZPPN * renderMaxY * (1.0D - renderMinZ)
                + aoMixedXYZPPP * renderMaxY * renderMinZ);
            aoTopRight = (float) (aoMixedXYZPNP * (1.0D - renderMaxY) * renderMaxZ
                + aoMixedXYZPNN * (1.0D - renderMaxY) * (1.0D - renderMaxZ)
                + aoMixedXYZPPN * renderMaxY * (1.0D - renderMaxZ)
                + aoMixedXYZPPP * renderMaxY * renderMaxZ);

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderMinY) * renderMaxZ,
                (1.0D - renderMinY) * (1.0D - renderMaxZ),
                renderMinY * (1.0D - renderMaxZ),
                renderMinY * renderMaxZ);
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderMinY) * renderMinZ,
                (1.0D - renderMinY) * (1.0D - renderMinZ),
                renderMinY * (1.0D - renderMinZ),
                renderMinY * renderMinZ);
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderMaxY) * renderMinZ,
                (1.0D - renderMaxY) * (1.0D - renderMinZ),
                renderMaxY * (1.0D - renderMinZ),
                renderMaxY * renderMinZ);
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderMaxY) * renderMaxZ,
                (1.0D - renderMaxY) * (1.0D - renderMaxZ),
                renderMaxY * (1.0D - renderMaxZ),
                renderMaxY * renderMaxZ);
        } else {
            Tessellator.instance.setBrightness(MBFB[iX][1][1]);
        }

        return this;
    }

}
