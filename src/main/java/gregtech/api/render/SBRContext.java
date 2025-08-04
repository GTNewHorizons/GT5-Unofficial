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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents the rendering context for a single block during a render pass.
 * <p>
 * This class holds the tightly-coupled mutable state required to coordinate lighting,
 * shading, ambient occlusion, and color calculation based on the block, its coordinates,
 * the renderer, and the world in which it is rendered. It is passed
 * to various rendering methods throughout a block's render cycle.
 * <p>
 * {@code @SuppressWarnings} is used here intentionally:
 * <ul>
 * <li>{@code "ClassWithTooManyFields"} - all fields represent a unified mutable rendering context and are
 * interdependent by design.</li>
 * <li>{@code "ClassWithTooManyMethods"} - all methods serve specific and tightly-scoped roles to configure or query the
 * context.</li>
 * <li>{@code "unused"} - some methods are present to conform to expected APIs or reserved for future use.</li>
 * </ul>
 */
@SuppressWarnings({ "unused", "ClassWithTooManyFields", "ClassWithTooManyMethods" })
@SideOnly(Side.CLIENT)
public class SBRContext {

    public static final int NORMAL_BRIGHTNESS = 0xff00ff;
    public static final int MAX_BRIGHTNESS = 0xf000f0;
    public static final float NO_Z_FIGHT_OFFSET = 1.0F / 1024.0F;
    protected static final float[] LIGHTNESS = { 0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F };
    public final RenderBlocks renderer;
    /**
     * Brightness for side.
     */
    private int brightness;
    /**
     * Ambient occlusion values for all four corners of side.
     */
    private float aoTopLeft, aoBottomLeft, aoBottomRight, aoTopRight;

    private boolean hasLightnessOverride;
    private float lightnessOverride;
    private boolean hasBrightnessOverride;
    private int brightnessOverride;
    private boolean hasColorOverride;
    private int colorOverride = 0xffffff;
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
    public final int x;
    public final int y;
    public final int z;
    public final int worldRenderPass;
    public final IBlockAccess world;
    public final Block block;
    public final int meta;
    public final int modelId;

    /**
     * Constructs a new {@link SBRContext} used to render a single {@link Block} inside an inventory
     *
     * @param block    the {@link Block} to render
     * @param meta     the meta value of the {@link Block}'s {@link Item} meta value
     * @param modelId  the Model ID for the block
     * @param renderer the {@link RenderBlocks} renderer to use
     */
    public SBRContext(Block block, int meta, int modelId, RenderBlocks renderer) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.block = block;
        this.meta = meta;
        this.modelId = modelId;
        this.renderer = renderer;
        this.world = renderer.blockAccess;
        this.worldRenderPass = -1;
        // Context is an inventory, so it needs its lightness maxed
        setLightnessOverride(1.0F);
    }

    /**
     * Constructs a new {@link SBRContext} used to render a single {@link Block} in world for the
     * current render pass at the given coordinates
     *
     * @param x            the x coordinate
     * @param y            the y coordinate
     * @param z            the z coordinate
     * @param block        the {@link Block} to render
     * @param modelId      the Model ID for the block
     * @param renderBlocks the {@link RenderBlocks} renderer to use
     */
    @SuppressWarnings("ConstructorWithTooManyParameters") // Blame ISimpleBlockRenderingHandler.renderWorldBlock
    public SBRContext(int x, int y, int z, Block block, int modelId, RenderBlocks renderBlocks) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.meta = -1;
        this.modelId = modelId;
        this.renderer = renderBlocks;
        this.world = renderBlocks.blockAccess;
        this.worldRenderPass = ForgeHooksClient.getWorldRenderPass();
        // Context is a World, so it has its own lightness
        clearLightnessOverride();
        populatesBlockAOCaches();
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
    private void populatesBlockAOCaches() {
        if (Minecraft.getMinecraft().gameSettings.ambientOcclusion == 0) return;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    MBFB[dx + 1][dy + 1][dz + 1] = block.getMixedBrightnessForBlock(world, x + dx, y + dy, z + dz);
                    AOLV[dx + 1][dy + 1][dz + 1] = world.getBlock(x + dx, y + dy, z + dz)
                        .getAmbientOcclusionLightValue();
                }
            }
        }
    }

    /**
     * Gets average brightness from two brightness values.
     *
     * @param brightnessA the first brightness value
     * @param brightnessB the second brightness value
     * @return the mixed brightness
     */
    public static int getAverageBrightness(int brightnessA, int brightnessB) {
        final int sectionA1 = brightnessA >> 16 & 0xff;
        final int sectionA2 = brightnessA & 255;

        final int sectionB1 = brightnessB >> 16 & 0xff;
        final int sectionB2 = brightnessB & 255;

        final int difference1 = (int) ((sectionA1 + sectionB1) / 2.0F);
        final int difference2 = (int) ((sectionA2 + sectionB2) / 2.0F);

        return difference1 << 16 | difference2;
    }

    /**
     * Gets rgb color from RGBA array.
     *
     * @param color the integer color
     * @return a float array with rgb values
     */
    public static float[] getRGB(short[] color) {
        final float red = color[0] / 255.0F;
        final float green = color[1] / 255.0F;
        final float blue = color[2] / 255.0F;

        return new float[] { red, green, blue };
    }

    /**
     * Resets override flags to their default values.
     * <p>
     * This ensures deterministic rendering by clearing any leftover state
     * from previous use of this context instance.
     *
     * @return this {@link SBRContext} instance for chaining
     */
    public SBRContext reset() {
        hasBrightnessOverride = false;
        hasColorOverride = false;
        hasLightnessOverride = false;
        return this;
    }

    /**
     * Clears brightness override.
     * 
     * @return the {@link SBRContext}
     */
    public SBRContext clearBrightnessOverride() {
        hasBrightnessOverride = false;
        return this;
    }

    /**
     * Clears color override.
     * 
     * @return the {@link SBRContext}
     */
    public SBRContext clearColorOverride() {
        hasColorOverride = false;
        return this;
    }

    /**
     * Clears lightness override.
     * 
     * @return the {@link SBRContext}
     */
    public SBRContext clearLightnessOverride() {
        hasLightnessOverride = false;
        return this;
    }

    /**
     * @return the Ambient Occlusion for Bottom-Left corner
     */
    public float getAoBottomLeft() {
        return aoBottomLeft;
    }

    /**
     * @return the Ambient Occlusion for Bottom-Right corner
     */
    public float getAoBottomRight() {
        return aoBottomRight;
    }

    /**
     * @return the Ambient Occlusion for Top-Left corner
     */
    public float getAoTopLeft() {
        return aoTopLeft;
    }

    /**
     * @return the Ambient Occlusion for Top-Right corner
     */
    public float getAoTopRight() {
        return aoTopRight;
    }

    /**
     * Sets brightness override.
     *
     * @param brightness the brightness override
     * @return the {@link SBRContext}
     */
    public SBRContext setBrightnessOverride(int brightness) {
        hasBrightnessOverride = true;
        brightnessOverride = brightness;
        return this;
    }

    public SBRContext setColorOverride(short[] color) {
        return setColorOverride(getColor(color));
    }

    /**
     * Sets color override.
     *
     * @param color the color override
     * @return the {@link SBRContext}
     */
    public SBRContext setColorOverride(int color) {
        hasColorOverride = true;
        colorOverride = color;
        return this;
    }

    /**
     * Gets int color from RGBA array.
     *
     * @param rgba the short RGBA color array
     * @return int color
     */
    public static int getColor(short[] rgba) {
        return (rgba[2] & 0xff) | (rgba[1] & 0xff) << 8 | (rgba[0] & 0xff) << 16;
    }

    /**
     * Sets lightness override.
     *
     * @param lightness the lightness override
     * @return the {@link SBRContext}
     */
    public SBRContext setLightnessOverride(float lightness) {
        hasLightnessOverride = true;
        lightnessOverride = lightness;
        return this;
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side the side
     * @param rgba the primary short[] RGBA color array
     */
    public void setupColor(ForgeDirection side, short[] rgba) {
        setupColor(side, getColor(rgba));
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side     the side
     * @param hexColor the primary color
     */
    public void setupColor(ForgeDirection side, int hexColor) {
        final Tessellator tessellator = Tessellator.instance;
        final float lightness = hasLightnessOverride ? lightnessOverride : LIGHTNESS[side.ordinal()];
        final float[] rgb = hasColorOverride && !renderer.hasOverrideBlockTexture() ? getRGB(colorOverride)
            : getRGB(hexColor);

        applyAnaglyph(rgb);

        if (renderer.enableAO) {
            tessellator.setBrightness(hasBrightnessOverride ? brightnessOverride : brightness);

            if (renderer.hasOverrideBlockTexture()) {

                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = rgb[0];
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = rgb[1];
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = rgb[2];

            } else {

                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = rgb[0]
                    * lightness;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = rgb[1]
                    * lightness;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = rgb[2]
                    * lightness;

                renderer.colorRedTopLeft *= aoTopLeft;
                renderer.colorGreenTopLeft *= aoTopLeft;
                renderer.colorBlueTopLeft *= aoTopLeft;
                renderer.colorRedBottomLeft *= aoBottomLeft;
                renderer.colorGreenBottomLeft *= aoBottomLeft;
                renderer.colorBlueBottomLeft *= aoBottomLeft;
                renderer.colorRedBottomRight *= aoBottomRight;
                renderer.colorGreenBottomRight *= aoBottomRight;
                renderer.colorBlueBottomRight *= aoBottomRight;
                renderer.colorRedTopRight *= aoTopRight;
                renderer.colorGreenTopRight *= aoTopRight;
                renderer.colorBlueTopRight *= aoTopRight;
            }

        } else {

            if (hasBrightnessOverride) tessellator.setBrightness(brightnessOverride);
            tessellator.setColorOpaque_F(rgb[0] * lightness, rgb[1] * lightness, rgb[2] * lightness);
        }
    }

    /**
     * Gets rgb color from integer.
     *
     * @param color the integer color
     * @return a float array with rgb values
     */
    public static float[] getRGB(int color) {
        final float red = (color >> 16 & 0xff) / 255.0F;
        final float green = (color >> 8 & 0xff) / 255.0F;
        final float blue = (color & 0xff) / 255.0F;

        return new float[] { red, green, blue };
    }

    /**
     * Will apply anaglyph color multipliers to RGB float array.
     * <p>
     * If {@link EntityRenderer#anaglyphEnable} is false, will do nothing.
     *
     * @param rgb array containing red, green and blue float values
     */
    public static void applyAnaglyph(float[] rgb) {
        if (EntityRenderer.anaglyphEnable) {
            rgb[0] = (rgb[0] * 30.0F + rgb[1] * 59.0F + rgb[2] * 11.0F) / 100.0F;
            rgb[1] = (rgb[0] * 30.0F + rgb[1] * 70.0F) / 100.0F;
            rgb[2] = (rgb[0] * 30.0F + rgb[2] * 70.0F) / 100.0F;
        }
    }

    /**
     * Gets mixed ambient occlusion value from two inputs, with a ratio applied to the final result.
     *
     * @param ao1   the first ambient occlusion value
     * @param ao2   the second ambient occlusion value
     * @param ratio the ratio for mixing
     * @return the mixed red, green, blue float values
     */
    public static float getMixedAo(float ao1, float ao2, double ratio) {
        final float diff = (float) (Math.abs(ao1 - ao2) * (1.0F - ratio));

        return ao1 > ao2 ? ao1 - diff : ao1 + diff;
    }

    /**
     * @see #setupLightingXNeg()
     * @see #setupLightingYNeg()
     * @see #setupLightingZNeg()
     * @see #setupLightingXPos()
     * @see #setupLightingYPos()
     * @see #setupLightingZPos()
     */
    public SBRContext setupLighting(Block block, int x, int y, int z, ForgeDirection facing) {
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
     * Sets up lighting for the West face and returns the {@link SBRContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     * 
     * @return the {@link SBRContext}
     */
    public SBRContext setupLightingXNeg() {

        if (renderer.enableAO) {

            final int iX = renderer.renderMinX > 0.0F + NO_Z_FIGHT_OFFSET ? 1 : 0;

            final int mixedBrightness = MBFB[iX][1][1];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderer.renderMinX;
            final float aoLightValue = AOLV[0][1][1];

            renderer.aoBrightnessXYNN = MBFB[iX][0][1];
            renderer.aoBrightnessXZNN = MBFB[iX][1][0];
            renderer.aoBrightnessXZNP = MBFB[iX][1][2];
            renderer.aoBrightnessXYNP = MBFB[iX][2][1];
            renderer.aoBrightnessXYZNNN = MBFB[iX][0][0];
            renderer.aoBrightnessXYZNNP = MBFB[iX][0][2];
            renderer.aoBrightnessXYZNPN = MBFB[iX][2][0];
            renderer.aoBrightnessXYZNPP = MBFB[iX][2][2];
            renderer.aoLightValueScratchXYNN = getMixedAo(AOLV[0][0][1], AOLV[1][0][1], ratio);
            renderer.aoLightValueScratchXZNN = getMixedAo(AOLV[0][1][0], AOLV[1][1][0], ratio);
            renderer.aoLightValueScratchXZNP = getMixedAo(AOLV[0][1][2], AOLV[1][1][2], ratio);
            renderer.aoLightValueScratchXYNP = getMixedAo(AOLV[0][2][1], AOLV[1][2][1], ratio);
            renderer.aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[1][0][0], ratio);
            renderer.aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[1][0][2], ratio);
            renderer.aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[1][2][0], ratio);
            renderer.aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[1][2][2], ratio);

            final int brightnessMixedXYZNPN = renderer.getAoBrightness(
                renderer.aoBrightnessXZNN,
                renderer.aoBrightnessXYZNPN,
                renderer.aoBrightnessXYNP,
                mixedBrightness);
            final int brightnessMixedXYZNNN = renderer.getAoBrightness(
                renderer.aoBrightnessXYZNNN,
                renderer.aoBrightnessXYNN,
                renderer.aoBrightnessXZNN,
                mixedBrightness);
            final int brightnessMixedXYZNNP = renderer.getAoBrightness(
                renderer.aoBrightnessXYNN,
                renderer.aoBrightnessXYZNNP,
                renderer.aoBrightnessXZNP,
                mixedBrightness);
            final int brightnessMixedXYZNPP = renderer.getAoBrightness(
                renderer.aoBrightnessXZNP,
                renderer.aoBrightnessXYNP,
                renderer.aoBrightnessXYZNPP,
                mixedBrightness);

            final float aoMixedXYZNPN = (renderer.aoLightValueScratchXZNN + aoLightValue
                + renderer.aoLightValueScratchXYZNPN
                + renderer.aoLightValueScratchXYNP) / 4.0F;
            final float aoMixedXYZNNN = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN
                + renderer.aoLightValueScratchXZNN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZNNP = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP
                + aoLightValue
                + renderer.aoLightValueScratchXZNP) / 4.0F;
            final float aoMixedXYZNPP = (aoLightValue + renderer.aoLightValueScratchXZNP
                + renderer.aoLightValueScratchXYNP
                + renderer.aoLightValueScratchXYZNPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPP * renderer.renderMaxY * renderer.renderMaxZ
                + aoMixedXYZNPN * renderer.renderMaxY * (1.0D - renderer.renderMaxZ)
                + aoMixedXYZNNN * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ)
                + aoMixedXYZNNP * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            aoBottomLeft = (float) (aoMixedXYZNPP * renderer.renderMaxY * renderer.renderMinZ
                + aoMixedXYZNPN * renderer.renderMaxY * (1.0D - renderer.renderMinZ)
                + aoMixedXYZNNN * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ)
                + aoMixedXYZNNP * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            aoBottomRight = (float) (aoMixedXYZNPP * renderer.renderMinY * renderer.renderMinZ
                + aoMixedXYZNPN * renderer.renderMinY * (1.0D - renderer.renderMinZ)
                + aoMixedXYZNNN * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ)
                + aoMixedXYZNNP * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            aoTopRight = (float) (aoMixedXYZNPP * renderer.renderMinY * renderer.renderMaxZ
                + aoMixedXYZNPN * renderer.renderMinY * (1.0D - renderer.renderMaxZ)
                + aoMixedXYZNNN * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ)
                + aoMixedXYZNNP * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);

            renderer.brightnessTopLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderer.renderMaxY * renderer.renderMaxZ,
                renderer.renderMaxY * (1.0D - renderer.renderMaxZ),
                (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ),
                (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderer.renderMaxY * renderer.renderMinZ,
                renderer.renderMaxY * (1.0D - renderer.renderMinZ),
                (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ),
                (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderer.renderMinY * renderer.renderMinZ,
                renderer.renderMinY * (1.0D - renderer.renderMinZ),
                (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ),
                (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNPN,
                brightnessMixedXYZNNN,
                brightnessMixedXYZNNP,
                renderer.renderMinY * renderer.renderMaxZ,
                renderer.renderMinY * (1.0D - renderer.renderMaxZ),
                (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ),
                (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
        }

        return this;
    }

    /**
     * Sets up lighting for the East face and returns the {@link SBRContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     * 
     * @return the {@link SBRContext}
     */
    public SBRContext setupLightingXPos() {

        if (renderer.enableAO) {

            final int iX = renderer.renderMaxX < 1.0F - NO_Z_FIGHT_OFFSET ? 1 : 2;

            final int mixedBrightness = MBFB[iX][1][1];
            brightness = mixedBrightness;

            final double ratio = renderer.renderMaxX;
            final float aoLightValue = AOLV[2][1][1];

            renderer.aoBrightnessXYPN = MBFB[iX][0][1];
            renderer.aoBrightnessXZPN = MBFB[iX][1][0];
            renderer.aoBrightnessXZPP = MBFB[iX][1][2];
            renderer.aoBrightnessXYPP = MBFB[iX][2][1];
            renderer.aoBrightnessXYZPNN = MBFB[iX][0][0];
            renderer.aoBrightnessXYZPNP = MBFB[iX][0][2];
            renderer.aoBrightnessXYZPPN = MBFB[iX][2][0];
            renderer.aoBrightnessXYZPPP = MBFB[iX][2][2];
            renderer.aoLightValueScratchXYPN = getMixedAo(AOLV[2][0][1], AOLV[1][0][1], ratio);
            renderer.aoLightValueScratchXZPN = getMixedAo(AOLV[2][1][0], AOLV[1][1][0], ratio);
            renderer.aoLightValueScratchXZPP = getMixedAo(AOLV[2][1][2], AOLV[1][1][2], ratio);
            renderer.aoLightValueScratchXYPP = getMixedAo(AOLV[2][2][1], AOLV[1][2][1], ratio);
            renderer.aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[1][0][0], ratio);
            renderer.aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[1][0][2], ratio);
            renderer.aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[1][2][0], ratio);
            renderer.aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[1][2][2], ratio);

            final int brightnessMixedXYZPPP = renderer.getAoBrightness(
                renderer.aoBrightnessXZPP,
                renderer.aoBrightnessXYPP,
                renderer.aoBrightnessXYZPPP,
                mixedBrightness);
            final int brightnessMixedXYZPNP = renderer.getAoBrightness(
                renderer.aoBrightnessXYPN,
                renderer.aoBrightnessXYZPNP,
                renderer.aoBrightnessXZPP,
                mixedBrightness);
            final int brightnessMixedXYZPNN = renderer.getAoBrightness(
                renderer.aoBrightnessXYZPNN,
                renderer.aoBrightnessXYPN,
                renderer.aoBrightnessXZPN,
                mixedBrightness);
            final int brightnessMixedXYZPPN = renderer.getAoBrightness(
                renderer.aoBrightnessXZPN,
                renderer.aoBrightnessXYZPPN,
                renderer.aoBrightnessXYPP,
                mixedBrightness);

            final float aoMixedXYZPPP = (aoLightValue + renderer.aoLightValueScratchXZPP
                + renderer.aoLightValueScratchXYPP
                + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            final float aoMixedXYZPNP = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP
                + aoLightValue
                + renderer.aoLightValueScratchXZPP) / 4.0F;
            final float aoMixedXYZPNN = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN
                + renderer.aoLightValueScratchXZPN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZPPN = (renderer.aoLightValueScratchXZPN + aoLightValue
                + renderer.aoLightValueScratchXYZPPN
                + renderer.aoLightValueScratchXYPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZPNP * (1.0D - renderer.renderMinY) * renderer.renderMaxZ
                + aoMixedXYZPNN * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ)
                + aoMixedXYZPPN * renderer.renderMinY * (1.0D - renderer.renderMaxZ)
                + aoMixedXYZPPP * renderer.renderMinY * renderer.renderMaxZ);
            aoBottomLeft = (float) (aoMixedXYZPNP * (1.0D - renderer.renderMinY) * renderer.renderMinZ
                + aoMixedXYZPNN * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ)
                + aoMixedXYZPPN * renderer.renderMinY * (1.0D - renderer.renderMinZ)
                + aoMixedXYZPPP * renderer.renderMinY * renderer.renderMinZ);
            aoBottomRight = (float) (aoMixedXYZPNP * (1.0D - renderer.renderMaxY) * renderer.renderMinZ
                + aoMixedXYZPNN * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ)
                + aoMixedXYZPPN * renderer.renderMaxY * (1.0D - renderer.renderMinZ)
                + aoMixedXYZPPP * renderer.renderMaxY * renderer.renderMinZ);
            aoTopRight = (float) (aoMixedXYZPNP * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ
                + aoMixedXYZPNN * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ)
                + aoMixedXYZPPN * renderer.renderMaxY * (1.0D - renderer.renderMaxZ)
                + aoMixedXYZPPP * renderer.renderMaxY * renderer.renderMaxZ);

            renderer.brightnessTopLeft = renderer.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderer.renderMinY) * renderer.renderMaxZ,
                (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ),
                renderer.renderMinY * (1.0D - renderer.renderMaxZ),
                renderer.renderMinY * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderer.renderMinY) * renderer.renderMinZ,
                (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ),
                renderer.renderMinY * (1.0D - renderer.renderMinZ),
                renderer.renderMinY * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderer.renderMaxY) * renderer.renderMinZ,
                (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ),
                renderer.renderMaxY * (1.0D - renderer.renderMinZ),
                renderer.renderMaxY * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPPP,
                (1.0D - renderer.renderMaxY) * renderer.renderMaxZ,
                (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ),
                renderer.renderMaxY * (1.0D - renderer.renderMaxZ),
                renderer.renderMaxY * renderer.renderMaxZ);
        }

        return this;
    }

    /**
     * Sets up lighting for the bottom face and returns the {@link SBRContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @return the {@link SBRContext}
     */
    public SBRContext setupLightingYNeg() {

        if (renderer.enableAO) {

            final int iY = renderer.renderMinY > 0.0F + NO_Z_FIGHT_OFFSET ? 1 : 0;

            final int mixedBrightness = MBFB[1][iY][1];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderer.renderMinY;
            final float aoLightValue = AOLV[1][0][1];

            renderer.aoBrightnessXYNN = MBFB[0][iY][1];
            renderer.aoBrightnessYZNN = MBFB[1][iY][0];
            renderer.aoBrightnessYZNP = MBFB[1][iY][2];
            renderer.aoBrightnessXYPN = MBFB[2][iY][1];
            renderer.aoBrightnessXYZNNN = MBFB[0][iY][0];
            renderer.aoBrightnessXYZNNP = MBFB[0][iY][2];
            renderer.aoBrightnessXYZPNN = MBFB[2][iY][0];
            renderer.aoBrightnessXYZPNP = MBFB[2][iY][2];
            renderer.aoLightValueScratchXYNN = getMixedAo(AOLV[0][0][1], AOLV[0][1][1], ratio);
            renderer.aoLightValueScratchYZNN = getMixedAo(AOLV[1][0][0], AOLV[1][1][0], ratio);
            renderer.aoLightValueScratchYZNP = getMixedAo(AOLV[1][0][2], AOLV[1][1][2], ratio);
            renderer.aoLightValueScratchXYPN = getMixedAo(AOLV[2][0][1], AOLV[2][1][1], ratio);
            renderer.aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[0][1][0], ratio);
            renderer.aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[0][1][2], ratio);
            renderer.aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[2][1][0], ratio);
            renderer.aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[2][1][2], ratio);

            final int brightnessMixedXYZPNP = renderer.getAoBrightness(
                renderer.aoBrightnessYZNP,
                renderer.aoBrightnessXYZPNP,
                renderer.aoBrightnessXYPN,
                mixedBrightness);
            final int brightnessMixedXYZPNN = renderer.getAoBrightness(
                renderer.aoBrightnessYZNN,
                renderer.aoBrightnessXYPN,
                renderer.aoBrightnessXYZPNN,
                mixedBrightness);
            final int brightnessMixedXYZNNN = renderer.getAoBrightness(
                renderer.aoBrightnessXYNN,
                renderer.aoBrightnessXYZNNN,
                renderer.aoBrightnessYZNN,
                mixedBrightness);
            final int brightnessMixedXYZNNP = renderer.getAoBrightness(
                renderer.aoBrightnessXYZNNP,
                renderer.aoBrightnessXYNN,
                renderer.aoBrightnessYZNP,
                mixedBrightness);

            final float aoMixedXYZPNP = (renderer.aoLightValueScratchYZNP + aoLightValue
                + renderer.aoLightValueScratchXYZPNP
                + renderer.aoLightValueScratchXYPN) / 4.0F;
            final float aoMixedXYZPNN = (aoLightValue + renderer.aoLightValueScratchYZNN
                + renderer.aoLightValueScratchXYPN
                + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            final float aoMixedXYZNNN = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN
                + aoLightValue
                + renderer.aoLightValueScratchYZNN) / 4.0F;
            final float aoMixedXYZNNP = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN
                + renderer.aoLightValueScratchYZNP
                + aoLightValue) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNNP * renderer.renderMaxZ * (1.0D - renderer.renderMinX)
                + aoMixedXYZPNP * renderer.renderMaxZ * renderer.renderMinX
                + aoMixedXYZPNN * (1.0D - renderer.renderMaxZ) * renderer.renderMinX
                + aoMixedXYZNNN * (1.0D - renderer.renderMaxZ) * (1.0D - renderer.renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNNP * renderer.renderMinZ * (1.0D - renderer.renderMinX)
                + aoMixedXYZPNP * renderer.renderMinZ * renderer.renderMinX
                + aoMixedXYZPNN * (1.0D - renderer.renderMinZ) * renderer.renderMinX
                + aoMixedXYZNNN * (1.0D - renderer.renderMinZ) * (1.0D - renderer.renderMinX));
            aoBottomRight = (float) (aoMixedXYZNNP * renderer.renderMinZ * (1.0D - renderer.renderMaxX)
                + aoMixedXYZPNP * renderer.renderMinZ * renderer.renderMaxX
                + aoMixedXYZPNN * (1.0D - renderer.renderMinZ) * renderer.renderMaxX
                + aoMixedXYZNNN * (1.0D - renderer.renderMinZ) * (1.0D - renderer.renderMaxX));
            aoTopRight = (float) (aoMixedXYZNNP * renderer.renderMaxZ * (1.0D - renderer.renderMaxX)
                + aoMixedXYZPNP * renderer.renderMaxZ * renderer.renderMaxX
                + aoMixedXYZPNN * (1.0D - renderer.renderMaxZ) * renderer.renderMaxX
                + aoMixedXYZNNN * (1.0D - renderer.renderMaxZ) * (1.0D - renderer.renderMaxX));

            renderer.brightnessTopLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderer.renderMaxZ * (1.0D - renderer.renderMinX),
                renderer.renderMaxZ * renderer.renderMinX,
                (1.0D - renderer.renderMaxZ) * renderer.renderMinX,
                (1.0D - renderer.renderMaxZ) * (1.0D - renderer.renderMinX));
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderer.renderMinZ * (1.0D - renderer.renderMinX),
                renderer.renderMinZ * renderer.renderMinX,
                (1.0D - renderer.renderMinZ) * renderer.renderMinX,
                (1.0D - renderer.renderMinZ) * (1.0D - renderer.renderMinX));
            renderer.brightnessBottomRight = renderer.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderer.renderMinZ * (1.0D - renderer.renderMaxX),
                renderer.renderMinZ * renderer.renderMaxX,
                (1.0D - renderer.renderMinZ) * renderer.renderMaxX,
                (1.0D - renderer.renderMinZ) * (1.0D - renderer.renderMaxX));
            renderer.brightnessTopRight = renderer.mixAoBrightness(
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderer.renderMaxZ * (1.0D - renderer.renderMaxX),
                renderer.renderMaxZ * renderer.renderMaxX,
                (1.0D - renderer.renderMaxZ) * renderer.renderMaxX,
                (1.0D - renderer.renderMaxZ) * (1.0D - renderer.renderMaxX));
        }

        return this;
    }

    /**
     * Sets up lighting for the top face and returns the {@link SBRContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     * 
     * @return the {@link SBRContext}
     */
    public SBRContext setupLightingYPos() {

        if (renderer.enableAO) {

            final int iY = renderer.renderMaxY < 1.0F - NO_Z_FIGHT_OFFSET ? 1 : 2;

            final int mixedBrightness = MBFB[1][iY][1];
            brightness = mixedBrightness;

            final double ratio = renderer.renderMaxY;
            final float aoLightValue = AOLV[1][2][1];

            renderer.aoBrightnessXYNP = MBFB[0][iY][1];
            renderer.aoBrightnessXYPP = MBFB[2][iY][1];
            renderer.aoBrightnessYZPN = MBFB[1][iY][0];
            renderer.aoBrightnessYZPP = MBFB[1][iY][2];
            renderer.aoBrightnessXYZNPN = MBFB[0][iY][0];
            renderer.aoBrightnessXYZPPN = MBFB[2][iY][0];
            renderer.aoBrightnessXYZNPP = MBFB[0][iY][2];
            renderer.aoBrightnessXYZPPP = MBFB[2][iY][2];
            renderer.aoLightValueScratchXYNP = getMixedAo(AOLV[0][2][1], AOLV[0][1][1], ratio);
            renderer.aoLightValueScratchXYPP = getMixedAo(AOLV[2][2][1], AOLV[2][1][1], ratio);
            renderer.aoLightValueScratchYZPN = getMixedAo(AOLV[1][2][0], AOLV[1][1][0], ratio);
            renderer.aoLightValueScratchYZPP = getMixedAo(AOLV[1][2][2], AOLV[1][1][2], ratio);
            renderer.aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[0][1][0], ratio);
            renderer.aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[2][1][0], ratio);
            renderer.aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[0][1][2], ratio);
            renderer.aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[2][1][2], ratio);

            final int brightnessMixedXYZPPP = renderer.getAoBrightness(
                renderer.aoBrightnessYZPP,
                renderer.aoBrightnessXYZPPP,
                renderer.aoBrightnessXYPP,
                mixedBrightness);
            final int brightnessMixedXYZPPN = renderer.getAoBrightness(
                renderer.aoBrightnessYZPN,
                renderer.aoBrightnessXYPP,
                renderer.aoBrightnessXYZPPN,
                mixedBrightness);
            final int brightnessMixedXYZNPN = renderer.getAoBrightness(
                renderer.aoBrightnessXYNP,
                renderer.aoBrightnessXYZNPN,
                renderer.aoBrightnessYZPN,
                mixedBrightness);
            final int brightnessMixedXYZNPP = renderer.getAoBrightness(
                renderer.aoBrightnessXYZNPP,
                renderer.aoBrightnessXYNP,
                renderer.aoBrightnessYZPP,
                mixedBrightness);

            final float aoMixedXYZPPP = (renderer.aoLightValueScratchYZPP + aoLightValue
                + renderer.aoLightValueScratchXYZPPP
                + renderer.aoLightValueScratchXYPP) / 4.0F;
            final float aoMixedXYZPPN = (aoLightValue + renderer.aoLightValueScratchYZPN
                + renderer.aoLightValueScratchXYPP
                + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            final float aoMixedXYZNPN = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN
                + aoLightValue
                + renderer.aoLightValueScratchYZPN) / 4.0F;
            final float aoMixedXYZNPP = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP
                + renderer.aoLightValueScratchYZPP
                + aoLightValue) / 4.0F;

            aoTopLeft /* SE */ = (float) (aoMixedXYZNPP * renderer.renderMaxZ * (1.0D - renderer.renderMaxX)
                + aoMixedXYZPPP * renderer.renderMaxZ * renderer.renderMaxX
                + aoMixedXYZPPN * (1.0D - renderer.renderMaxZ) * renderer.renderMaxX
                + aoMixedXYZNPN * (1.0D - renderer.renderMaxZ) * (1.0D - renderer.renderMaxX));
            aoBottomLeft /* NE */ = (float) (aoMixedXYZNPP * renderer.renderMinZ * (1.0D - renderer.renderMaxX)
                + aoMixedXYZPPP * renderer.renderMinZ * renderer.renderMaxX
                + aoMixedXYZPPN * (1.0D - renderer.renderMinZ) * renderer.renderMaxX
                + aoMixedXYZNPN * (1.0D - renderer.renderMinZ) * (1.0D - renderer.renderMaxX));
            aoBottomRight /* NW */ = (float) (aoMixedXYZNPP * renderer.renderMinZ * (1.0D - renderer.renderMinX)
                + aoMixedXYZPPP * renderer.renderMinZ * renderer.renderMinX
                + aoMixedXYZPPN * (1.0D - renderer.renderMinZ) * renderer.renderMinX
                + aoMixedXYZNPN * (1.0D - renderer.renderMinZ) * (1.0D - renderer.renderMinX));
            aoTopRight /* SW */ = (float) (aoMixedXYZNPP * renderer.renderMaxZ * (1.0D - renderer.renderMinX)
                + aoMixedXYZPPP * renderer.renderMaxZ * renderer.renderMinX
                + aoMixedXYZPPN * (1.0D - renderer.renderMaxZ) * renderer.renderMinX
                + aoMixedXYZNPN * (1.0D - renderer.renderMaxZ) * (1.0D - renderer.renderMinX));

            renderer.brightnessTopLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderer.renderMaxZ * (1.0D - renderer.renderMaxX),
                renderer.renderMaxZ * renderer.renderMaxX,
                (1.0D - renderer.renderMaxZ) * renderer.renderMaxX,
                (1.0D - renderer.renderMaxZ) * (1.0D - renderer.renderMaxX));
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderer.renderMinZ * (1.0D - renderer.renderMaxX),
                renderer.renderMinZ * renderer.renderMaxX,
                (1.0D - renderer.renderMinZ) * renderer.renderMaxX,
                (1.0D - renderer.renderMinZ) * (1.0D - renderer.renderMaxX));
            renderer.brightnessBottomRight = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderer.renderMinZ * (1.0D - renderer.renderMinX),
                renderer.renderMinZ * renderer.renderMinX,
                (1.0D - renderer.renderMinZ) * renderer.renderMinX,
                (1.0D - renderer.renderMinZ) * (1.0D - renderer.renderMinX));
            renderer.brightnessTopRight = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZPPP,
                brightnessMixedXYZPPN,
                brightnessMixedXYZNPN,
                renderer.renderMaxZ * (1.0D - renderer.renderMinX),
                renderer.renderMaxZ * renderer.renderMinX,
                (1.0D - renderer.renderMaxZ) * renderer.renderMinX,
                (1.0D - renderer.renderMaxZ) * (1.0D - renderer.renderMinX));
        }

        return this;
    }

    /**
     * Sets up lighting for the North face and returns the {@link SBRContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     * 
     * @return the {@link SBRContext}
     */
    public SBRContext setupLightingZNeg() {

        if (renderer.enableAO) {

            final int iZ = renderer.renderMinZ > 0.0F + NO_Z_FIGHT_OFFSET ? 1 : 0;

            final int mixedBrightness = MBFB[1][1][iZ];
            brightness = mixedBrightness;

            final double ratio = 1.0D - renderer.renderMinZ;
            final float aoLightValue = AOLV[1][1][0];

            renderer.aoBrightnessXZNN = MBFB[0][1][iZ];
            renderer.aoBrightnessYZNN = MBFB[1][0][iZ];
            renderer.aoBrightnessYZPN = MBFB[1][2][iZ];
            renderer.aoBrightnessXZPN = MBFB[2][1][iZ];
            renderer.aoBrightnessXYZNNN = MBFB[0][0][iZ];
            renderer.aoBrightnessXYZNPN = MBFB[0][2][iZ];
            renderer.aoBrightnessXYZPNN = MBFB[2][0][iZ];
            renderer.aoBrightnessXYZPPN = MBFB[2][2][iZ];
            renderer.aoLightValueScratchXZNN = getMixedAo(AOLV[0][1][0], AOLV[0][1][1], ratio);
            renderer.aoLightValueScratchYZNN = getMixedAo(AOLV[1][0][0], AOLV[1][0][1], ratio);
            renderer.aoLightValueScratchYZPN = getMixedAo(AOLV[1][2][0], AOLV[1][2][1], ratio);
            renderer.aoLightValueScratchXZPN = getMixedAo(AOLV[2][1][0], AOLV[2][1][1], ratio);
            renderer.aoLightValueScratchXYZNNN = getMixedAo(AOLV[0][0][0], AOLV[0][0][1], ratio);
            renderer.aoLightValueScratchXYZNPN = getMixedAo(AOLV[0][2][0], AOLV[0][2][1], ratio);
            renderer.aoLightValueScratchXYZPNN = getMixedAo(AOLV[2][0][0], AOLV[2][0][1], ratio);
            renderer.aoLightValueScratchXYZPPN = getMixedAo(AOLV[2][2][0], AOLV[2][2][1], ratio);

            final int brightnessMixedXYZPPN = renderer.getAoBrightness(
                renderer.aoBrightnessYZPN,
                renderer.aoBrightnessXZPN,
                renderer.aoBrightnessXYZPPN,
                mixedBrightness);
            final int brightnessMixedXYZPNN = renderer.getAoBrightness(
                renderer.aoBrightnessYZNN,
                renderer.aoBrightnessXYZPNN,
                renderer.aoBrightnessXZPN,
                mixedBrightness);
            final int brightnessMixedXYZNNN = renderer.getAoBrightness(
                renderer.aoBrightnessXYZNNN,
                renderer.aoBrightnessXZNN,
                renderer.aoBrightnessYZNN,
                mixedBrightness);
            final int brightnessMixedXYZNPN = renderer.getAoBrightness(
                renderer.aoBrightnessXZNN,
                renderer.aoBrightnessXYZNPN,
                renderer.aoBrightnessYZPN,
                mixedBrightness);

            final float aoMixedXYZPPN = (aoLightValue + renderer.aoLightValueScratchYZPN
                + renderer.aoLightValueScratchXZPN
                + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            final float aoMixedXYZPNN = (renderer.aoLightValueScratchYZNN + aoLightValue
                + renderer.aoLightValueScratchXYZPNN
                + renderer.aoLightValueScratchXZPN) / 4.0F;
            final float aoMixedXYZNNN = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN
                + renderer.aoLightValueScratchYZNN
                + aoLightValue) / 4.0F;
            final float aoMixedXYZNPN = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN
                + aoLightValue
                + renderer.aoLightValueScratchYZPN) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPN * renderer.renderMaxY * (1.0D - renderer.renderMinX)
                + aoMixedXYZPPN * renderer.renderMaxY * renderer.renderMinX
                + aoMixedXYZPNN * (1.0D - renderer.renderMaxY) * renderer.renderMinX
                + aoMixedXYZNNN * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNPN * renderer.renderMaxY * (1.0D - renderer.renderMaxX)
                + aoMixedXYZPPN * renderer.renderMaxY * renderer.renderMaxX
                + aoMixedXYZPNN * (1.0D - renderer.renderMaxY) * renderer.renderMaxX
                + aoMixedXYZNNN * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            aoBottomRight = (float) (aoMixedXYZNPN * renderer.renderMinY * (1.0D - renderer.renderMaxX)
                + aoMixedXYZPPN * renderer.renderMinY * renderer.renderMaxX
                + aoMixedXYZPNN * (1.0D - renderer.renderMinY) * renderer.renderMaxX
                + aoMixedXYZNNN * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            aoTopRight = (float) (aoMixedXYZNPN * renderer.renderMinY * (1.0D - renderer.renderMinX)
                + aoMixedXYZPPN * renderer.renderMinY * renderer.renderMinX
                + aoMixedXYZPNN * (1.0D - renderer.renderMinY) * renderer.renderMinX
                + aoMixedXYZNNN * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));

            renderer.brightnessTopLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderer.renderMaxY * (1.0D - renderer.renderMinX),
                renderer.renderMaxY * renderer.renderMinX,
                (1.0D - renderer.renderMaxY) * renderer.renderMinX,
                (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderer.renderMaxY * (1.0D - renderer.renderMaxX),
                renderer.renderMaxY * renderer.renderMaxX,
                (1.0D - renderer.renderMaxY) * renderer.renderMaxX,
                (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessBottomRight = renderer.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderer.renderMinY * (1.0D - renderer.renderMaxX),
                renderer.renderMinY * renderer.renderMaxX,
                (1.0D - renderer.renderMinY) * renderer.renderMaxX,
                (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessTopRight = renderer.mixAoBrightness(
                brightnessMixedXYZNPN,
                brightnessMixedXYZPPN,
                brightnessMixedXYZPNN,
                brightnessMixedXYZNNN,
                renderer.renderMinY * (1.0D - renderer.renderMinX),
                renderer.renderMinY * renderer.renderMinX,
                (1.0D - renderer.renderMinY) * renderer.renderMinX,
                (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
        }

        return this;
    }

    /**
     * Sets up lighting for the South face and returns the {@link SBRContext}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading with respect to the following attributes:
     * <p>
     * <ul>
     * <li>{@link RenderBlocks#enableAO}</li>
     * <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @return the {@link SBRContext}
     */
    public SBRContext setupLightingZPos() {

        if (renderer.enableAO) {

            final int iZ = renderer.renderMaxZ < 1.0F - NO_Z_FIGHT_OFFSET ? 1 : 2;

            final int mixedBrightness = MBFB[1][1][iZ];
            brightness = mixedBrightness;

            final double ratio = renderer.renderMaxZ;
            final float aoLightValue = AOLV[1][1][2];

            renderer.aoBrightnessXZNP = MBFB[0][1][iZ];
            renderer.aoBrightnessXZPP = MBFB[2][1][iZ];
            renderer.aoBrightnessYZNP = MBFB[1][0][iZ];
            renderer.aoBrightnessYZPP = MBFB[1][2][iZ];
            renderer.aoBrightnessXYZNNP = MBFB[0][0][iZ];
            renderer.aoBrightnessXYZNPP = MBFB[0][2][iZ];
            renderer.aoBrightnessXYZPNP = MBFB[2][0][iZ];
            renderer.aoBrightnessXYZPPP = MBFB[2][2][iZ];
            renderer.aoLightValueScratchXZNP = getMixedAo(AOLV[0][1][2], AOLV[0][1][1], ratio);
            renderer.aoLightValueScratchXZPP = getMixedAo(AOLV[2][1][2], AOLV[2][1][1], ratio);
            renderer.aoLightValueScratchYZNP = getMixedAo(AOLV[1][0][2], AOLV[1][0][1], ratio);
            renderer.aoLightValueScratchYZPP = getMixedAo(AOLV[1][2][2], AOLV[1][2][1], ratio);
            renderer.aoLightValueScratchXYZNNP = getMixedAo(AOLV[0][0][2], AOLV[0][0][1], ratio);
            renderer.aoLightValueScratchXYZNPP = getMixedAo(AOLV[0][2][2], AOLV[0][2][1], ratio);
            renderer.aoLightValueScratchXYZPNP = getMixedAo(AOLV[2][0][2], AOLV[2][0][1], ratio);
            renderer.aoLightValueScratchXYZPPP = getMixedAo(AOLV[2][2][2], AOLV[2][2][1], ratio);

            final int brightnessMixedXYZNPP = renderer.getAoBrightness(
                renderer.aoBrightnessXZNP,
                renderer.aoBrightnessXYZNPP,
                renderer.aoBrightnessYZPP,
                mixedBrightness);
            final int brightnessMixedXYZNNP = renderer.getAoBrightness(
                renderer.aoBrightnessXYZNNP,
                renderer.aoBrightnessXZNP,
                renderer.aoBrightnessYZNP,
                mixedBrightness);
            final int brightnessMixedXYZPNP = renderer.getAoBrightness(
                renderer.aoBrightnessYZNP,
                renderer.aoBrightnessXYZPNP,
                renderer.aoBrightnessXZPP,
                mixedBrightness);
            final int brightnessMixedXYZPPP = renderer.getAoBrightness(
                renderer.aoBrightnessYZPP,
                renderer.aoBrightnessXZPP,
                renderer.aoBrightnessXYZPPP,
                mixedBrightness);

            final float aoMixedXYZNPP = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP
                + aoLightValue
                + renderer.aoLightValueScratchYZPP) / 4.0F;
            final float aoMixedXYZNNP = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP
                + renderer.aoLightValueScratchYZNP
                + aoLightValue) / 4.0F;
            final float aoMixedXYZPNP = (renderer.aoLightValueScratchYZNP + aoLightValue
                + renderer.aoLightValueScratchXYZPNP
                + renderer.aoLightValueScratchXZPP) / 4.0F;
            final float aoMixedXYZPPP = (aoLightValue + renderer.aoLightValueScratchYZPP
                + renderer.aoLightValueScratchXZPP
                + renderer.aoLightValueScratchXYZPPP) / 4.0F;

            aoTopLeft = (float) (aoMixedXYZNPP * renderer.renderMaxY * (1.0D - renderer.renderMinX)
                + aoMixedXYZPPP * renderer.renderMaxY * renderer.renderMinX
                + aoMixedXYZPNP * (1.0D - renderer.renderMaxY) * renderer.renderMinX
                + aoMixedXYZNNP * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            aoBottomLeft = (float) (aoMixedXYZNPP * renderer.renderMinY * (1.0D - renderer.renderMinX)
                + aoMixedXYZPPP * renderer.renderMinY * renderer.renderMinX
                + aoMixedXYZPNP * (1.0D - renderer.renderMinY) * renderer.renderMinX
                + aoMixedXYZNNP * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            aoBottomRight = (float) (aoMixedXYZNPP * renderer.renderMinY * (1.0D - renderer.renderMaxX)
                + aoMixedXYZPPP * renderer.renderMinY * renderer.renderMaxX
                + aoMixedXYZPNP * (1.0D - renderer.renderMinY) * renderer.renderMaxX
                + aoMixedXYZNNP * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            aoTopRight = (float) (aoMixedXYZNPP * renderer.renderMaxY * (1.0D - renderer.renderMaxX)
                + aoMixedXYZPPP * renderer.renderMaxY * renderer.renderMaxX
                + aoMixedXYZPNP * (1.0D - renderer.renderMaxY) * renderer.renderMaxX
                + aoMixedXYZNNP * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));

            renderer.brightnessTopLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderer.renderMaxY * (1.0D - renderer.renderMinX),
                (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX),
                (1.0D - renderer.renderMaxY) * renderer.renderMinX,
                renderer.renderMaxY * renderer.renderMinX);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderer.renderMinY * (1.0D - renderer.renderMinX),
                (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX),
                (1.0D - renderer.renderMinY) * renderer.renderMinX,
                renderer.renderMinY * renderer.renderMinX);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderer.renderMinY * (1.0D - renderer.renderMaxX),
                (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX),
                (1.0D - renderer.renderMinY) * renderer.renderMaxX,
                renderer.renderMinY * renderer.renderMaxX);
            renderer.brightnessTopRight = renderer.mixAoBrightness(
                brightnessMixedXYZNPP,
                brightnessMixedXYZNNP,
                brightnessMixedXYZPNP,
                brightnessMixedXYZPPP,
                renderer.renderMaxY * (1.0D - renderer.renderMaxX),
                (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX),
                (1.0D - renderer.renderMaxY) * renderer.renderMaxX,
                renderer.renderMaxY * renderer.renderMaxX);
        }

        return this;
    }

}
