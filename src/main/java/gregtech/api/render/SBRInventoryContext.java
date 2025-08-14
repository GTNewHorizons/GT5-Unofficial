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
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents the Inventory rendering context for a single block during a render pass.
 * <p>
 * This class holds the tightly-coupled mutable state required to coordinate lighting,
 * shading, ambient occlusion, and color calculation based on the block, its coordinates,
 * the renderer, and the world in which it is rendered. It is passed
 * to various rendering methods throughout a block's render cycle.
 */
@SideOnly(Side.CLIENT)
public class SBRInventoryContext extends SBRContextBase<SBRInventoryContext> {

    protected static final float[] LIGHTNESS = { 0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F };
    public final int meta;

    /**
     * Constructs a new {@link SBRInventoryContext} used to render a single {@link Block} inside an inventory
     *
     * @param block    the {@link Block} to render
     * @param meta     the meta value of the {@link Block}'s {@link Item} meta value
     * @param modelId  the Model ID for the block
     * @param renderer the {@link RenderBlocks} renderer to use
     */
    public SBRInventoryContext(@NotNull Block block, int meta, int modelId, @NotNull RenderBlocks renderer) {
        super(block, modelId, renderer);
        this.meta = meta;
        reset();
    }

    /**
     * Resets override flags to their default values.
     * <p>
     * This ensures deterministic rendering by clearing any leftover state
     * from previous use of this context instance.
     *
     * @return this {@link SBRInventoryContext} instance for chaining
     */
    public SBRInventoryContext reset() {
        hasBrightnessOverride = false;
        hasColorOverride = false;
        setLightnessOverride(1.0F);
        return this;
    }

    /**
     * Sets brightness override.
     *
     * @param brightness the brightness override
     * @return the {@link SBRInventoryContext}
     */
    public SBRInventoryContext setBrightnessOverride(int brightness) {
        hasBrightnessOverride = true;
        brightnessOverride = brightness;
        return this;
    }

    /**
     * Sets lightness override.
     *
     * @param lightness the lightness override
     * @return the {@link SBRInventoryContext}
     */
    public SBRInventoryContext setLightnessOverride(float lightness) {
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
    public SBRInventoryContext setupColor(ForgeDirection side, short[] rgba) {
        return setupColor(side, rgbaToInt(rgba));
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side     the side
     * @param hexColor the primary color
     */
    public SBRInventoryContext setupColor(ForgeDirection side, int hexColor) {
        final Tessellator tessellator = Tessellator.instance;
        final float lightness = hasLightnessOverride ? lightnessOverride : LIGHTNESS[side.ordinal()];
        final float[] rgb = hasColorOverride && !renderer.hasOverrideBlockTexture() ? getRGB(colorOverride)
            : getRGB(hexColor);

        if (hasBrightnessOverride) tessellator.setBrightness(brightnessOverride);
        tessellator.setColorOpaque_F(rgb[0] * lightness, rgb[1] * lightness, rgb[2] * lightness);
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
}
