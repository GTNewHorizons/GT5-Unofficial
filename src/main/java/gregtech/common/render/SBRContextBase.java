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

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ISBRContext;

/**
 * Base class to represent the rendering context for a single block during a render pass.
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
 * <li>{@code "UnusedReturnValue"} - methods form a fluent interface where returned values might be ignored.</li>
 * <li>{@code "ForLoopReplaceableByForEach"} — A traditional {@code for} loop is used
 * for critical speed, as it avoids creating an iterator object.</li>
 * </ul>
 */
@SuppressWarnings({ "UnusedReturnValue", "ClassWithTooManyFields" })
public abstract class SBRContextBase implements ISBRContext {

    protected static final float[] LIGHTNESS = { 0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F };
    /**
     * Non-null placeholder RenderBlocks, replaced in {@link #setup}.
     */
    @NotNull
    protected RenderBlocks renderBlocks = RenderBlocks.getInstance();
    /**
     * Non-null placeholder block, replaced in {@link #setup}.
     */
    @NotNull
    protected Block block = Blocks.air;
    /**
     * Rendering coordinates for {@link RenderBlocks}; present to sidestep API limitations
     * and keep logic out of rendering.
     * (0, 0, 0) is used in inventory contexts due to Minecraft’s questionable design.
     */
    protected int x, y, z;
    protected int modelId;

    protected boolean hasLightnessOverride;
    protected float lightnessOverride;
    protected boolean hasBrightnessOverride;
    protected int brightnessOverride;
    protected boolean hasColorOverride;
    protected int colorOverride;

    /**
     * Gets int color from RGBA array.
     *
     * @param rgba the short RGBA color array
     * @return int color
     */
    private static int rgbaToInt(short @NotNull [] rgba) {
        return (rgba[2] & 0xff) | (rgba[1] & 0xff) << 8 | (rgba[0] & 0xff) << 16;
    }

    /**
     * Set up the {@link SBRContextBase} used to render a single {@link Block}
     *
     * @param block        the {@link Block} to render
     * @param modelId      the Model ID for the block
     * @param renderBlocks the {@link RenderBlocks} renderer to use
     */
    public ISBRContext setup(@NotNull Block block, int modelId, @NotNull RenderBlocks renderBlocks) {
        this.block = block;
        this.modelId = modelId;
        this.renderBlocks = renderBlocks;
        return this;
    }

    @Override
    public final @NotNull RenderBlocks getRenderBlocks() {
        return renderBlocks;
    }

    @Override
    public final @NotNull Block getBlock() {
        return block;
    }

    @Override
    public final int getModelId() {
        return modelId;
    }

    @Override
    public final int getX() {
        return x;
    }

    @Override
    public final int getY() {
        return y;
    }

    @Override
    public final int getZ() {
        return z;
    }

    /**
     * Sets brightness override.
     *
     * @param brightness the brightness override
     * @return the {@link SBRContextBase}
     */
    @Override
    public final ISBRContext setBrightnessOverride(int brightness) {
        hasBrightnessOverride = true;
        brightnessOverride = brightness;
        return this;
    }

    /**
     * Sets lightness override.
     *
     * @param lightness the lightness override
     * @return the {@link SBRContextBase}
     */
    @Override
    public final ISBRContext setLightnessOverride(float lightness) {
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
    @Override
    public final ISBRContext setupColor(ForgeDirection side, short[] rgba) {
        return setupColor(side, rgbaToInt(rgba));
    }

    /**
     * Like setRenderBounds, but automatically pulling the bounds from the context's block.
     * 
     * @see net.minecraft.client.renderer.RenderBlocks#setRenderBoundsFromBlock(Block)
     */
    @Override
    public final ISBRContext setRenderBoundsFromBlock() {
        renderBlocks.setRenderBoundsFromBlock(block);
        return this;
    }

    @Override
    public void renderNegativeYFacing(ITexture[] tex) {
        for (final ITexture layer : tex) {
            if (layer != null) layer.renderYNeg(this);
        }
    }

    @Override
    public void renderPositiveYFacing(ITexture[] tex) {
        for (final ITexture layer : tex) {
            if (layer != null) layer.renderYPos(this);
        }
    }

    @Override
    public void renderNegativeZFacing(ITexture[] tex) {
        for (final ITexture layer : tex) {
            if (layer != null) layer.renderZNeg(this);
        }
    }

    @Override
    public void renderPositiveZFacing(ITexture[] tex) {
        for (final ITexture layer : tex) {
            if (layer != null) layer.renderZPos(this);
        }
    }

    @Override
    public void renderNegativeXFacing(ITexture[] tex) {
        for (final ITexture layer : tex) {
            if (layer != null) layer.renderXNeg(this);
        }
    }

    @Override
    public void renderPositiveXFacing(ITexture[] tex) {
        for (final ITexture layer : tex) {
            if (layer != null) layer.renderXPos(this);
        }
    }

}
