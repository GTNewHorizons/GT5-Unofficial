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
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.ITexture;

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
@SuppressWarnings({ "UnusedReturnValue", "ClassWithTooManyFields", "ForLoopReplaceableByForEach" })
@SideOnly(Side.CLIENT)
public abstract class SBRContextBase<T extends SBRContextBase<T>> {

    public static final int MAX_BRIGHTNESS = 0xf000f0;
    protected static final float[] LIGHTNESS = { 0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F };

    /** Non-null placeholder RenderBlocks, replaced in {@link #setup}. */
    @NotNull
    public RenderBlocks renderBlocks = RenderBlocks.getInstance();

    /** Non-null placeholder block, replaced in {@link #setup}. */
    @NotNull
    public Block block = Blocks.air;
    public int modelId;

    /**
     * Determines if block faces can be culled
     */
    public boolean fullBlock;

    /**
     * Rendering coordinates for {@link RenderBlocks}; present to sidestep API limitations
     * and keep logic out of rendering.
     * (0, 0, 0) is used in inventory contexts due to Minecraft’s questionable design.
     */
    public int x, y, z;

    protected boolean hasLightnessOverride;
    protected float lightnessOverride;
    protected boolean hasBrightnessOverride;
    protected int brightnessOverride;
    protected boolean hasColorOverride;
    protected int colorOverride;

    /**
     * Set up the {@link SBRContextBase} used to render a single {@link Block}
     *
     * @param block        the {@link Block} to render
     * @param modelId      the Model ID for the block
     * @param renderBlocks the {@link RenderBlocks} renderer to use
     */
    public T setup(@NotNull Block block, int modelId, @NotNull RenderBlocks renderBlocks) {
        this.block = block;
        this.modelId = modelId;
        this.renderBlocks = renderBlocks;
        @SuppressWarnings("unchecked")
        final T self = (T) this;
        return self;
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
     * Gets int color from RGBA array.
     *
     * @param rgba the short RGBA color array
     * @return int color
     */
    public static int rgbaToInt(short[] rgba) {
        return (rgba[2] & 0xff) | (rgba[1] & 0xff) << 8 | (rgba[0] & 0xff) << 16;
    }

    /**
     * Resets override flags to their default values.
     * <p>
     * This ensures deterministic rendering by clearing any leftover state
     * from previous use of this context instance.
     *
     * @return this {@link SBRContextBase} instance for chaining
     */
    public abstract T reset();

    /**
     * Sets brightness override.
     *
     * @param brightness the brightness override
     * @return the {@link SBRContextBase}
     */
    public T setBrightnessOverride(int brightness) {
        hasBrightnessOverride = true;
        brightnessOverride = brightness;
        @SuppressWarnings("unchecked")
        final T self = (T) this;
        return self;
    }

    /**
     * Sets lightness override.
     *
     * @param lightness the lightness override
     * @return the {@link SBRContextBase}
     */
    public T setLightnessOverride(float lightness) {
        hasLightnessOverride = true;
        lightnessOverride = lightness;
        @SuppressWarnings("unchecked")
        final T self = (T) this;
        return self;
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side the side
     * @param rgba the primary short[] RGBA color array
     */
    public T setupColor(ForgeDirection side, short[] rgba) {
        return setupColor(side, rgbaToInt(rgba));
    }

    /**
     * Provides per-face rendering methods for this block rendering context.
     * <p>
     * Each method renders all non-null texture layers from the given array
     * on the corresponding cuboid's face.
     *
     * @param tex the {@link ITexture} array containing the texture layers for the face
     * @see #renderNegativeYFacing(ITexture[])
     * @see #renderPositiveYFacing(ITexture[])
     * @see #renderNegativeZFacing(ITexture[])
     * @see #renderPositiveZFacing(ITexture[])
     * @see #renderNegativeXFacing(ITexture[])
     * @see #renderPositiveXFacing(ITexture[])
     */
    public void renderNegativeYFacing(ITexture[] tex) {
        final long length = tex.length;
        for (int i = 0; i < length; i++) {
            final ITexture layer = tex[i];
            if (layer == null) continue;
            layer.renderYNeg(this);
        }
    }

    public void renderPositiveYFacing(ITexture[] tex) {
        final long length = tex.length;
        for (int i = 0; i < length; i++) {
            final ITexture layer = tex[i];
            if (layer == null) continue;
            layer.renderYPos(this);
        }
    }

    public void renderNegativeZFacing(ITexture[] tex) {
        final long length = tex.length;
        for (int i = 0; i < length; i++) {
            final ITexture layer = tex[i];
            if (layer == null) continue;
            layer.renderZNeg(this);
        }
    }

    public void renderPositiveZFacing(ITexture[] tex) {
        final long length = tex.length;
        for (int i = 0; i < length; i++) {
            final ITexture layer = tex[i];
            if (layer == null) continue;
            layer.renderZPos(this);
        }
    }

    public void renderNegativeXFacing(ITexture[] tex) {
        final long length = tex.length;
        for (int i = 0; i < length; i++) {
            final ITexture layer = tex[i];
            if (layer == null) continue;
            layer.renderXNeg(this);
        }
    }

    public void renderPositiveXFacing(ITexture[] tex) {
        final long length = tex.length;
        for (int i = 0; i < length; i++) {
            final ITexture layer = tex[i];
            if (layer == null) continue;
            layer.renderXPos(this);
        }
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side     the side
     * @param hexColor the primary color
     */
    public abstract T setupColor(ForgeDirection side, int hexColor);

    /**
     * Checks if rendering is allowed for the current pass.
     *
     * @param predicate a {@code boolean function(int pass)} to evaluate permission
     * @return {@code true} if rendering is allowed for this pass
     */
    public abstract boolean canRenderInPass(@NotNull IntPredicate predicate);
}
