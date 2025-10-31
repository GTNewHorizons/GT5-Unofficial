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
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.SBRContextHolder;

/**
 * Represents the Inventory rendering context for a single block during a render pass.
 * <p>
 * This class holds the tightly-coupled mutable state required to coordinate lighting,
 * shading, ambient occlusion, and color calculation based on the block, its coordinates,
 * the renderer, and the world in which it is rendered. It is passed
 * to various rendering methods throughout a block's render cycle.
 */
public final class SBRInventoryContext extends SBRContextBase implements ISBRInventoryContext {

    private static final float[] LIGHTNESS = { 0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F };
    private int meta;

    /**
     * Package-private constructor.
     * <p>
     * Instances should be obtained via {@link SBRContextHolder#getSBRInventoryContext}.
     */
    public SBRInventoryContext() {}

    /**
     * Configures this {@link SBRInventoryContext} to render a single {@link Block}
     * inside an inventory, using the given parameters.
     *
     * @param block    the block to render
     * @param meta     the block's metadata value (corresponds to the {@link Item} damage value)
     * @param modelId  the model ID for the block
     * @param renderer the {@link RenderBlocks} renderer to use
     * @return this context instance, configured with the given parameters
     */
    public ISBRInventoryContext setup(@NotNull Block block, int meta, int modelId, @NotNull RenderBlocks renderer) {
        super.setup(block, modelId, renderer);
        this.meta = meta;
        reset();
        return this;
    }

    /**
     * Resets override flags to their default values.
     * <p>
     * This ensures deterministic rendering by clearing any leftover state
     * from previous use of this context instance.
     *
     * @return this {@link SBRInventoryContext} instance for chaining
     */
    @Override
    public ISBRInventoryContext reset() {
        hasBrightnessOverride = false;
        hasColorOverride = false;
        setLightnessOverride(1.0F);
        return this;
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side     the side
     * @param hexColor the primary color
     */
    @Override
    public ISBRInventoryContext setupColor(ForgeDirection side, int hexColor) {
        final Tessellator tessellator = Tessellator.instance;
        final float lightness = hasLightnessOverride ? lightnessOverride : LIGHTNESS[side.ordinal()];

        final int color = hasColorOverride ? colorOverride : hexColor;

        final float red = (color >> 16 & 0xff) / 255.0F;
        final float green = (color >> 8 & 0xff) / 255.0F;
        final float blue = (color & 0xff) / 255.0F;

        if (hasBrightnessOverride) tessellator.setBrightness(brightnessOverride);
        tessellator.setColorOpaque_F(red * lightness, green * lightness, blue * lightness);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Always true in Inventory Context
     */
    @Override
    public boolean canRenderInPass(@NotNull IntPredicate predicate) {
        return true;
    }

    @Override
    public int getMeta() {
        return meta;
    }
}
