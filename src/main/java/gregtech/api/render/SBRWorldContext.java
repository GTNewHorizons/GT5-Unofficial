/*
 * SBRContext - Derived and adapted from @Mineshopper / carpentersblocks Copyright (c) 2013-2021. This library is
 * free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation version 2.1 of the License. This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

package gregtech.api.render;

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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;

/**
 * Represents the rendering context for a single block during a render pass.
 * <p>
 * This class holds the tightly-coupled mutable state required to coordinate lighting,
 * shading, ambient occlusion, and color calculation based on the block, its coordinates,
 * the renderer, and the world in which it is rendered. It is passed
 * to various rendering methods throughout a block's render cycle.
 */
@SideOnly(Side.CLIENT)
public final class SBRWorldContext extends SBRContextBase {

    private static final float NO_Z_FIGHT_OFFSET = 1.0F / 1024.0F;

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
    SBRWorldContext() {}

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

    public @NotNull IBlockAccess getBlockAccess() {
        return blockAccess;
    }

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
        final boolean aoDisabled = Minecraft.getMinecraft().gameSettings.ambientOcclusion == 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    // spotless:off
                    MBFB[dx + 1][dy + 1][dz + 1] = block.getMixedBrightnessForBlock(blockAccess, x + dx, y + dy, z + dz);
                    if (aoDisabled) continue;
                    AOLV[dx + 1][dy + 1][dz + 1] = blockAccess.getBlock(x + dx, y + dy, z + dz).getAmbientOcclusionLightValue();
                    //spotless:on
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
    public SBRWorldContext reset() {
        this.hasBrightnessOverride = false;
        this.hasColorOverride = false;
        this.hasLightnessOverride = false;
        this.renderBlocks.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.proxy.mRenderTileAmbientOcclusion;
        return this;
    }

    @Override
    public void renderNegativeYFacing(ITexture[] tex) {
        if (fullBlock && !renderBlocks.renderAllFaces && !block.shouldSideBeRendered(blockAccess, x, y - 1, z, 0))
            return;
        setupLightingYNeg();
        super.renderNegativeYFacing(tex);
    }

    @Override
    public void renderPositiveYFacing(ITexture[] tex) {
        if (fullBlock && !renderBlocks.renderAllFaces && !block.shouldSideBeRendered(blockAccess, x, y + 1, z, 1))
            return;
        setupLightingYPos();
        super.renderPositiveYFacing(tex);
    }

    @Override
    public void renderNegativeZFacing(ITexture[] tex) {
        if (fullBlock && !renderBlocks.renderAllFaces && !block.shouldSideBeRendered(blockAccess, x, y, z - 1, 2))
            return;
        setupLightingZNeg();
        super.renderNegativeZFacing(tex);
    }

    @Override
    public void renderPositiveZFacing(ITexture[] tex) {
        if (fullBlock && !renderBlocks.renderAllFaces && !block.shouldSideBeRendered(blockAccess, x, y, z + 1, 3))
            return;
        setupLightingZPos();
        super.renderPositiveZFacing(tex);
    }

    @Override
    public void renderNegativeXFacing(ITexture[] tex) {
        if (fullBlock && !renderBlocks.renderAllFaces && !block.shouldSideBeRendered(blockAccess, x - 1, y, z, 4))
            return;
        setupLightingXNeg();
        super.renderNegativeXFacing(tex);
    }

    @Override
    public void renderPositiveXFacing(ITexture[] tex) {
        if (fullBlock && !renderBlocks.renderAllFaces && !block.shouldSideBeRendered(blockAccess, x + 1, y, z, 5))
            return;
        setupLightingXPos();
        super.renderPositiveXFacing(tex);
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side     the side
     * @param hexColor the primary color
     */
    public SBRWorldContext setupColor(ForgeDirection side, int hexColor) {
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
     * {@inheritDoc}
     *
     * @implNote Check against the world render pass
     */
    @Override
    public boolean canRenderInPass(@NotNull IntPredicate predicate) {
        return worldRenderPass == -1 || predicate.test(worldRenderPass);
    }

    /**
     * @see #setupLightingXNeg()
     * @see #setupLightingYNeg()
     * @see #setupLightingZNeg()
     * @see #setupLightingXPos()
     * @see #setupLightingYPos()
     * @see #setupLightingZPos()
     */
    public SBRWorldContext setupLighting(ForgeDirection facing) {
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
    private SBRWorldContext setupLightingYNeg() {

        if (renderBlocks.enableAO) {

            final int iY = renderBlocks.renderMinY > 0.0F + NO_Z_FIGHT_OFFSET ? 1 : 0;

            final int mixedBrightness = MBFB[1][iY][1];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderBlocks.renderMinY;
            final float aoLightValue = AOLV[1][0][1];

            renderBlocks.aoBrightnessXYNN = MBFB[0][iY][1];
            renderBlocks.aoBrightnessYZNN = MBFB[1][iY][0];
            renderBlocks.aoBrightnessYZNP = MBFB[1][iY][2];
            renderBlocks.aoBrightnessXYPN = MBFB[2][iY][1];
            renderBlocks.aoBrightnessXYZNNN = MBFB[0][iY][0];
            renderBlocks.aoBrightnessXYZNNP = MBFB[0][iY][2];
            renderBlocks.aoBrightnessXYZPNN = MBFB[2][iY][0];
            renderBlocks.aoBrightnessXYZPNP = MBFB[2][iY][2];
            renderBlocks.aoLightValueScratchXYNN = getMixedAo(AOLV[0][0][1], AOLV[0][1][1], ratio);
            renderBlocks.aoLightValueScratchYZNN = getMixedAo(AOLV[1][0][0], AOLV[1][1][0], ratio);
            renderBlocks.aoLightValueScratchYZNP = getMixedAo(AOLV[1][0][2], AOLV[1][1][2], ratio);
            renderBlocks.aoLightValueScratchXYPN = getMixedAo(AOLV[2][0][1], AOLV[2][1][1], ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[0][1][0], ratio);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[0][1][2], ratio);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[2][1][0], ratio);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[2][1][2], ratio);

            final int brightnessMixedXYZPNP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessYZNP,
                renderBlocks.aoBrightnessXYZPNP,
                renderBlocks.aoBrightnessXYPN,
                mixedBrightness);
            final int brightnessMixedXYZPNN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessYZNN,
                renderBlocks.aoBrightnessXYPN,
                renderBlocks.aoBrightnessXYZPNN,
                mixedBrightness);
            final int brightnessMixedXYZNNN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYNN,
                renderBlocks.aoBrightnessXYZNNN,
                renderBlocks.aoBrightnessYZNN,
                mixedBrightness);
            final int brightnessMixedXYZNNP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYZNNP,
                renderBlocks.aoBrightnessXYNN,
                renderBlocks.aoBrightnessYZNP,
                mixedBrightness);

            final float aoMixedXYZPNP = (renderBlocks.aoLightValueScratchYZNP + aoLightValue
                + renderBlocks.aoLightValueScratchXYZPNP
                + renderBlocks.aoLightValueScratchXYPN) / 4.0F;
            final float aoMixedXYZPNN = (aoLightValue + renderBlocks.aoLightValueScratchYZNN
                + renderBlocks.aoLightValueScratchXYPN
                + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F;
            final float aoMixedXYZNNN = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN
                + aoLightValue
                + renderBlocks.aoLightValueScratchYZNN) / 4.0F;
            final float aoMixedXYZNNP = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN
                + renderBlocks.aoLightValueScratchYZNP
                + aoLightValue) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNNP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX)
                + aoMixedXYZPNP * renderBlocks.renderMaxZ * renderBlocks.renderMinX
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNNP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX)
                + aoMixedXYZPNP * renderBlocks.renderMinZ * renderBlocks.renderMinX
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
            aoBottomRight = (float) (aoMixedXYZNNP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX)
                + aoMixedXYZPNP * renderBlocks.renderMinZ * renderBlocks.renderMaxX
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
            aoTopRight = (float) (aoMixedXYZNNP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX)
                + aoMixedXYZPNP * renderBlocks.renderMaxZ * renderBlocks.renderMaxX
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX),
                renderBlocks.renderMaxZ * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX),
                renderBlocks.renderMinZ * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX),
                renderBlocks.renderMinZ * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX),
                renderBlocks.renderMaxZ * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
        } else {
            final int iY = block.getBlockBoundsMinY() < FLUSH_MIN ? 0 : 1;
            // Use neighbor brightness if face is flush with neighbor, otherwise current block brightness
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
    private SBRWorldContext setupLightingYPos() {

        if (renderBlocks.enableAO) {

            final int iY = renderBlocks.renderMaxY < 1.0F - NO_Z_FIGHT_OFFSET ? 1 : 2;

            final int mixedBrightness = MBFB[1][iY][1];
            brightness = mixedBrightness;

            final double ratio = renderBlocks.renderMaxY;
            final float aoLightValue = AOLV[1][2][1];

            renderBlocks.aoBrightnessXYNP = MBFB[0][iY][1];
            renderBlocks.aoBrightnessXYPP = MBFB[2][iY][1];
            renderBlocks.aoBrightnessYZPN = MBFB[1][iY][0];
            renderBlocks.aoBrightnessYZPP = MBFB[1][iY][2];
            renderBlocks.aoBrightnessXYZNPN = MBFB[0][iY][0];
            renderBlocks.aoBrightnessXYZPPN = MBFB[2][iY][0];
            renderBlocks.aoBrightnessXYZNPP = MBFB[0][iY][2];
            renderBlocks.aoBrightnessXYZPPP = MBFB[2][iY][2];
            renderBlocks.aoLightValueScratchXYNP = getMixedAo(AOLV[0][2][1], AOLV[0][1][1], ratio);
            renderBlocks.aoLightValueScratchXYPP = getMixedAo(AOLV[2][2][1], AOLV[2][1][1], ratio);
            renderBlocks.aoLightValueScratchYZPN = getMixedAo(AOLV[1][2][0], AOLV[1][1][0], ratio);
            renderBlocks.aoLightValueScratchYZPP = getMixedAo(AOLV[1][2][2], AOLV[1][1][2], ratio);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[0][1][0], ratio);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[2][1][0], ratio);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[0][1][2], ratio);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[2][1][2], ratio);

            final int brightnessMixedXYZPPP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessYZPP,
                renderBlocks.aoBrightnessXYZPPP,
                renderBlocks.aoBrightnessXYPP,
                mixedBrightness);
            final int brightnessMixedXYZPPN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessYZPN,
                renderBlocks.aoBrightnessXYPP,
                renderBlocks.aoBrightnessXYZPPN,
                mixedBrightness);
            final int brightnessMixedXYZNPN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYNP,
                renderBlocks.aoBrightnessXYZNPN,
                renderBlocks.aoBrightnessYZPN,
                mixedBrightness);
            final int brightnessMixedXYZNPP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYZNPP,
                renderBlocks.aoBrightnessXYNP,
                renderBlocks.aoBrightnessYZPP,
                mixedBrightness);

            final float aoMixedXYZPPP = (renderBlocks.aoLightValueScratchYZPP + aoLightValue
                + renderBlocks.aoLightValueScratchXYZPPP
                + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
            final float aoMixedXYZPPN = (aoLightValue + renderBlocks.aoLightValueScratchYZPN
                + renderBlocks.aoLightValueScratchXYPP
                + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
            final float aoMixedXYZNPN = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN
                + aoLightValue
                + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
            final float aoMixedXYZNPP = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP
                + renderBlocks.aoLightValueScratchYZPP
                + aoLightValue) / 4.0F;

            aoTopLeft /* SE */ = (float) (aoMixedXYZNPP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX)
                + aoMixedXYZPPP * renderBlocks.renderMaxZ * renderBlocks.renderMaxX
                + aoMixedXYZPPN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX
                + aoMixedXYZNPN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
            aoBottomLeft /* NE */ = (float) (aoMixedXYZNPP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX)
                + aoMixedXYZPPP * renderBlocks.renderMinZ * renderBlocks.renderMaxX
                + aoMixedXYZPPN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX
                + aoMixedXYZNPN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
            aoBottomRight /* NW */ = (float) (aoMixedXYZNPP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX)
                + aoMixedXYZPPP * renderBlocks.renderMinZ * renderBlocks.renderMinX
                + aoMixedXYZPPN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX
                + aoMixedXYZNPN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
            aoTopRight /* SW */ = (float) (aoMixedXYZNPP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX)
                + aoMixedXYZPPP * renderBlocks.renderMaxZ * renderBlocks.renderMinX
                + aoMixedXYZPPN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX
                + aoMixedXYZNPN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX),
                renderBlocks.renderMaxZ * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX),
                renderBlocks.renderMinZ * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX),
                renderBlocks.renderMinZ * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX),
                renderBlocks.renderMaxZ * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
        } else {
            final int iY = block.getBlockBoundsMaxY() > FLUSH_MAX ? 2 : 1;
            // Use neighbor brightness if face is flush with neighbor, otherwise current block brightness
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
    private SBRWorldContext setupLightingZNeg() {

        if (renderBlocks.enableAO) {

            final int iZ = renderBlocks.renderMinZ > 0.0F + NO_Z_FIGHT_OFFSET ? 1 : 0;

            final int mixedBrightness = MBFB[1][1][iZ];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderBlocks.renderMinZ;
            final float aoLightValue = AOLV[1][1][0];

            renderBlocks.aoBrightnessXZNN = MBFB[0][1][iZ];
            renderBlocks.aoBrightnessYZNN = MBFB[1][0][iZ];
            renderBlocks.aoBrightnessYZPN = MBFB[1][2][iZ];
            renderBlocks.aoBrightnessXZPN = MBFB[2][1][iZ];
            renderBlocks.aoBrightnessXYZNNN = MBFB[0][0][iZ];
            renderBlocks.aoBrightnessXYZNPN = MBFB[0][2][iZ];
            renderBlocks.aoBrightnessXYZPNN = MBFB[2][0][iZ];
            renderBlocks.aoBrightnessXYZPPN = MBFB[2][2][iZ];
            renderBlocks.aoLightValueScratchXZNN = getMixedAo(AOLV[0][1][0], AOLV[0][1][1], ratio);
            renderBlocks.aoLightValueScratchYZNN = getMixedAo(AOLV[1][0][0], AOLV[1][0][1], ratio);
            renderBlocks.aoLightValueScratchYZPN = getMixedAo(AOLV[1][2][0], AOLV[1][2][1], ratio);
            renderBlocks.aoLightValueScratchXZPN = getMixedAo(AOLV[2][1][0], AOLV[2][1][1], ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[0][0][1], ratio);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[0][2][1], ratio);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[2][0][1], ratio);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[2][2][1], ratio);

            final int brightnessMixedXYZPPN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessYZPN,
                renderBlocks.aoBrightnessXZPN,
                renderBlocks.aoBrightnessXYZPPN,
                mixedBrightness);
            final int brightnessMixedXYZPNN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessYZNN,
                renderBlocks.aoBrightnessXYZPNN,
                renderBlocks.aoBrightnessXZPN,
                mixedBrightness);
            final int brightnessMixedXYZNNN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYZNNN,
                renderBlocks.aoBrightnessXZNN,
                renderBlocks.aoBrightnessYZNN,
                mixedBrightness);
            final int brightnessMixedXYZNPN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXZNN,
                renderBlocks.aoBrightnessXYZNPN,
                renderBlocks.aoBrightnessYZPN,
                mixedBrightness);

            final float aoMixedXYZPPN = (aoLightValue + renderBlocks.aoLightValueScratchYZPN
                + renderBlocks.aoLightValueScratchXZPN
                + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
            final float aoMixedXYZPNN = (renderBlocks.aoLightValueScratchYZNN + aoLightValue
                + renderBlocks.aoLightValueScratchXYZPNN
                + renderBlocks.aoLightValueScratchXZPN) / 4.0F;
            final float aoMixedXYZNNN = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN
                + renderBlocks.aoLightValueScratchYZNN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZNPN = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN
                + aoLightValue
                + renderBlocks.aoLightValueScratchYZPN) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX)
                + aoMixedXYZPPN * renderBlocks.renderMaxY * renderBlocks.renderMinX
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX)
                + aoMixedXYZPPN * renderBlocks.renderMaxY * renderBlocks.renderMaxX
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
            aoBottomRight = (float) (aoMixedXYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX)
                + aoMixedXYZPPN * renderBlocks.renderMinY * renderBlocks.renderMaxX
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
            aoTopRight = (float) (aoMixedXYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX)
                + aoMixedXYZPPN * renderBlocks.renderMinY * renderBlocks.renderMinX
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX),
                renderBlocks.renderMaxY * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX),
                renderBlocks.renderMaxY * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX),
                renderBlocks.renderMinY * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX,
                (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX),
                renderBlocks.renderMinY * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX,
                (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
        } else {
            final int iZ = block.getBlockBoundsMinZ() < FLUSH_MIN ? 0 : 1;
            // Use neighbor brightness if face is flush with neighbor, otherwise current block brightness
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
    private SBRWorldContext setupLightingZPos() {

        if (renderBlocks.enableAO) {

            final int iZ = renderBlocks.renderMaxZ < 1.0F - NO_Z_FIGHT_OFFSET ? 1 : 2;

            final int mixedBrightness = MBFB[1][1][iZ];
            brightness = mixedBrightness;

            final double ratio = renderBlocks.renderMaxZ;
            final float aoLightValue = AOLV[1][1][2];

            renderBlocks.aoBrightnessXZNP = MBFB[0][1][iZ];
            renderBlocks.aoBrightnessXZPP = MBFB[2][1][iZ];
            renderBlocks.aoBrightnessYZNP = MBFB[1][0][iZ];
            renderBlocks.aoBrightnessYZPP = MBFB[1][2][iZ];
            renderBlocks.aoBrightnessXYZNNP = MBFB[0][0][iZ];
            renderBlocks.aoBrightnessXYZNPP = MBFB[0][2][iZ];
            renderBlocks.aoBrightnessXYZPNP = MBFB[2][0][iZ];
            renderBlocks.aoBrightnessXYZPPP = MBFB[2][2][iZ];
            renderBlocks.aoLightValueScratchXZNP = getMixedAo(AOLV[0][1][2], AOLV[0][1][1], ratio);
            renderBlocks.aoLightValueScratchXZPP = getMixedAo(AOLV[2][1][2], AOLV[2][1][1], ratio);
            renderBlocks.aoLightValueScratchYZNP = getMixedAo(AOLV[1][0][2], AOLV[1][0][1], ratio);
            renderBlocks.aoLightValueScratchYZPP = getMixedAo(AOLV[1][2][2], AOLV[1][2][1], ratio);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[0][0][1], ratio);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[0][2][1], ratio);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[2][0][1], ratio);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[2][2][1], ratio);

            final int brightnessMixedXYZNPP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXZNP,
                renderBlocks.aoBrightnessXYZNPP,
                renderBlocks.aoBrightnessYZPP,
                mixedBrightness);
            final int brightnessMixedXYZNNP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYZNNP,
                renderBlocks.aoBrightnessXZNP,
                renderBlocks.aoBrightnessYZNP,
                mixedBrightness);
            final int brightnessMixedXYZPNP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessYZNP,
                renderBlocks.aoBrightnessXYZPNP,
                renderBlocks.aoBrightnessXZPP,
                mixedBrightness);
            final int brightnessMixedXYZPPP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessYZPP,
                renderBlocks.aoBrightnessXZPP,
                renderBlocks.aoBrightnessXYZPPP,
                mixedBrightness);

            final float aoMixedXYZNPP = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP
                + aoLightValue
                + renderBlocks.aoLightValueScratchYZPP) / 4.0F;
            final float aoMixedXYZNNP = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP
                + renderBlocks.aoLightValueScratchYZNP
                + aoLightValue) / 4.0F;
            final float aoMixedXYZPNP = (renderBlocks.aoLightValueScratchYZNP + aoLightValue
                + renderBlocks.aoLightValueScratchXYZPNP
                + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
            final float aoMixedXYZPPP = (aoLightValue + renderBlocks.aoLightValueScratchYZPP
                + renderBlocks.aoLightValueScratchXZPP
                + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPP * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX)
                + aoMixedXYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMinX
                + aoMixedXYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX
                + aoMixedXYZNNP * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNPP * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX)
                + aoMixedXYZPPP * renderBlocks.renderMinY * renderBlocks.renderMinX
                + aoMixedXYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX
                + aoMixedXYZNNP * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
            aoBottomRight = (float) (aoMixedXYZNPP * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX)
                + aoMixedXYZPPP * renderBlocks.renderMinY * renderBlocks.renderMaxX
                + aoMixedXYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX
                + aoMixedXYZNNP * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
            aoTopRight = (float) (aoMixedXYZNPP * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX)
                + aoMixedXYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMaxX
                + aoMixedXYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX
                + aoMixedXYZNNP * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX),
                (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX),
                (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX,
                renderBlocks.renderMaxY * renderBlocks.renderMinX);
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX),
                (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX),
                (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX,
                renderBlocks.renderMinY * renderBlocks.renderMinX);
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX),
                (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX),
                (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX,
                renderBlocks.renderMinY * renderBlocks.renderMaxX);
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX),
                (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX),
                (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX,
                renderBlocks.renderMaxY * renderBlocks.renderMaxX);
        } else {
            final int iZ = block.getBlockBoundsMaxZ() > FLUSH_MAX ? 2 : 1;
            // Use neighbor brightness if face is flush with neighbor, otherwise current block brightness
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
    private SBRWorldContext setupLightingXNeg() {

        if (renderBlocks.enableAO) {

            final int iX = renderBlocks.renderMinX > 0.0F + NO_Z_FIGHT_OFFSET ? 1 : 0;

            final int mixedBrightness = MBFB[iX][1][1];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderBlocks.renderMinX;
            final float aoLightValue = AOLV[0][1][1];

            renderBlocks.aoBrightnessXYNN = MBFB[iX][0][1];
            renderBlocks.aoBrightnessXZNN = MBFB[iX][1][0];
            renderBlocks.aoBrightnessXZNP = MBFB[iX][1][2];
            renderBlocks.aoBrightnessXYNP = MBFB[iX][2][1];
            renderBlocks.aoBrightnessXYZNNN = MBFB[iX][0][0];
            renderBlocks.aoBrightnessXYZNNP = MBFB[iX][0][2];
            renderBlocks.aoBrightnessXYZNPN = MBFB[iX][2][0];
            renderBlocks.aoBrightnessXYZNPP = MBFB[iX][2][2];
            renderBlocks.aoLightValueScratchXYNN = getMixedAo(AOLV[0][0][1], AOLV[1][0][1], ratio);
            renderBlocks.aoLightValueScratchXZNN = getMixedAo(AOLV[0][1][0], AOLV[1][1][0], ratio);
            renderBlocks.aoLightValueScratchXZNP = getMixedAo(AOLV[0][1][2], AOLV[1][1][2], ratio);
            renderBlocks.aoLightValueScratchXYNP = getMixedAo(AOLV[0][2][1], AOLV[1][2][1], ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[1][0][0], ratio);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[1][0][2], ratio);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[1][2][0], ratio);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[1][2][2], ratio);

            final int brightnessMixedXYZNPN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXZNN,
                renderBlocks.aoBrightnessXYZNPN,
                renderBlocks.aoBrightnessXYNP,
                mixedBrightness);
            final int brightnessMixedXYZNNN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYZNNN,
                renderBlocks.aoBrightnessXYNN,
                renderBlocks.aoBrightnessXZNN,
                mixedBrightness);
            final int brightnessMixedXYZNNP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYNN,
                renderBlocks.aoBrightnessXYZNNP,
                renderBlocks.aoBrightnessXZNP,
                mixedBrightness);
            final int brightnessMixedXYZNPP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXZNP,
                renderBlocks.aoBrightnessXYNP,
                renderBlocks.aoBrightnessXYZNPP,
                mixedBrightness);

            final float aoMixedXYZNPN = (renderBlocks.aoLightValueScratchXZNN + aoLightValue
                + renderBlocks.aoLightValueScratchXYZNPN
                + renderBlocks.aoLightValueScratchXYNP) / 4.0F;
            final float aoMixedXYZNNN = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN
                + renderBlocks.aoLightValueScratchXZNN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZNNP = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP
                + aoLightValue
                + renderBlocks.aoLightValueScratchXZNP) / 4.0F;
            final float aoMixedXYZNPP = (aoLightValue + renderBlocks.aoLightValueScratchXZNP
                + renderBlocks.aoLightValueScratchXYNP
                + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPP * renderBlocks.renderMaxY * renderBlocks.renderMaxZ
                + aoMixedXYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ)
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ)
                + aoMixedXYZNNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ);
            aoBottomLeft = (float) (aoMixedXYZNPP * renderBlocks.renderMaxY * renderBlocks.renderMinZ
                + aoMixedXYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ)
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ)
                + aoMixedXYZNNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ);
            aoBottomRight = (float) (aoMixedXYZNPP * renderBlocks.renderMinY * renderBlocks.renderMinZ
                + aoMixedXYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ)
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ)
                + aoMixedXYZNNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ);
            aoTopRight = (float) (aoMixedXYZNPP * renderBlocks.renderMinY * renderBlocks.renderMaxZ
                + aoMixedXYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ)
                + aoMixedXYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ)
                + aoMixedXYZNNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ);

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderBlocks.renderMaxY * renderBlocks.renderMaxZ,
                renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ),
                (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ),
                (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ);
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderBlocks.renderMaxY * renderBlocks.renderMinZ,
                renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ),
                (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ),
                (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ);
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderBlocks.renderMinY * renderBlocks.renderMinZ,
                renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ),
                (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ),
                (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ);
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderBlocks.renderMinY * renderBlocks.renderMaxZ,
                renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ),
                (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ),
                (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ);
        } else {
            final int iX = block.getBlockBoundsMinX() < FLUSH_MIN ? 0 : 1;
            // Use neighbor brightness if face is flush with neighbor, otherwise current block brightness
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
    private SBRWorldContext setupLightingXPos() {

        if (renderBlocks.enableAO) {

            final int iX = renderBlocks.renderMaxX < 1.0F - NO_Z_FIGHT_OFFSET ? 1 : 2;

            final int mixedBrightness = MBFB[iX][1][1];
            brightness = mixedBrightness;

            final double ratio = renderBlocks.renderMaxX;
            final float aoLightValue = AOLV[2][1][1];

            renderBlocks.aoBrightnessXYPN = MBFB[iX][0][1];
            renderBlocks.aoBrightnessXZPN = MBFB[iX][1][0];
            renderBlocks.aoBrightnessXZPP = MBFB[iX][1][2];
            renderBlocks.aoBrightnessXYPP = MBFB[iX][2][1];
            renderBlocks.aoBrightnessXYZPNN = MBFB[iX][0][0];
            renderBlocks.aoBrightnessXYZPNP = MBFB[iX][0][2];
            renderBlocks.aoBrightnessXYZPPN = MBFB[iX][2][0];
            renderBlocks.aoBrightnessXYZPPP = MBFB[iX][2][2];
            renderBlocks.aoLightValueScratchXYPN = getMixedAo(AOLV[2][0][1], AOLV[1][0][1], ratio);
            renderBlocks.aoLightValueScratchXZPN = getMixedAo(AOLV[2][1][0], AOLV[1][1][0], ratio);
            renderBlocks.aoLightValueScratchXZPP = getMixedAo(AOLV[2][1][2], AOLV[1][1][2], ratio);
            renderBlocks.aoLightValueScratchXYPP = getMixedAo(AOLV[2][2][1], AOLV[1][2][1], ratio);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[1][0][0], ratio);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[1][0][2], ratio);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[1][2][0], ratio);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[1][2][2], ratio);

            final int brightnessMixedXYZPPP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXZPP,
                renderBlocks.aoBrightnessXYPP,
                renderBlocks.aoBrightnessXYZPPP,
                mixedBrightness);
            final int brightnessMixedXYZPNP = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYPN,
                renderBlocks.aoBrightnessXYZPNP,
                renderBlocks.aoBrightnessXZPP,
                mixedBrightness);
            final int brightnessMixedXYZPNN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXYZPNN,
                renderBlocks.aoBrightnessXYPN,
                renderBlocks.aoBrightnessXZPN,
                mixedBrightness);
            final int brightnessMixedXYZPPN = renderBlocks.getAoBrightness(
                renderBlocks.aoBrightnessXZPN,
                renderBlocks.aoBrightnessXYZPPN,
                renderBlocks.aoBrightnessXYPP,
                mixedBrightness);

            final float aoMixedXYZPPP = (aoLightValue + renderBlocks.aoLightValueScratchXZPP
                + renderBlocks.aoLightValueScratchXYPP
                + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
            final float aoMixedXYZPNP = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP
                + aoLightValue
                + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
            final float aoMixedXYZPNN = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN
                + renderBlocks.aoLightValueScratchXZPN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZPPN = (renderBlocks.aoLightValueScratchXZPN + aoLightValue
                + renderBlocks.aoLightValueScratchXYZPPN
                + renderBlocks.aoLightValueScratchXYPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ)
                + aoMixedXYZPPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ)
                + aoMixedXYZPPP * renderBlocks.renderMinY * renderBlocks.renderMaxZ);
            aoBottomLeft = (float) (aoMixedXYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ)
                + aoMixedXYZPPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ)
                + aoMixedXYZPPP * renderBlocks.renderMinY * renderBlocks.renderMinZ);
            aoBottomRight = (float) (aoMixedXYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ)
                + aoMixedXYZPPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ)
                + aoMixedXYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMinZ);
            aoTopRight = (float) (aoMixedXYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ
                + aoMixedXYZPNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ)
                + aoMixedXYZPPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ)
                + aoMixedXYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMaxZ);

            renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ,
                (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ),
                renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ),
                renderBlocks.renderMinY * renderBlocks.renderMaxZ);
            renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ,
                (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ),
                renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ),
                renderBlocks.renderMinY * renderBlocks.renderMinZ);
            renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ,
                (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ),
                renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ),
                renderBlocks.renderMaxY * renderBlocks.renderMinZ);
            renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ,
                (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ),
                renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ),
                renderBlocks.renderMaxY * renderBlocks.renderMaxZ);
        } else {
            final int iX = block.getBlockBoundsMaxX() > FLUSH_MAX ? 2 : 1;
            // Use neighbor brightness if face is flush with neighbor, otherwise current block brightness
            Tessellator.instance.setBrightness(MBFB[iX][1][1]);
        }

        return this;
    }

}
