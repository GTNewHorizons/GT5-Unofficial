package gregtech.api.render;

import java.util.function.IntPredicate;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.ITexture;
import gregtech.common.render.SBRContextBase;

@SuppressWarnings("UnusedReturnValue") // Fluent API is optional
public interface ISBRContext {

    int MAX_BRIGHTNESS = 0xf000f0;

    @NotNull
    RenderBlocks getRenderBlocks();

    @NotNull
    Block getBlock();

    int getModelId();

    int getX();

    int getY();

    int getZ();

    /**
     * Resets override flags to their default values.
     * <p>
     * This ensures deterministic rendering by clearing any leftover state
     * from previous use of this context instance.
     *
     * @return this {@link SBRContextBase} instance for chaining
     */
    ISBRContext reset();

    ISBRContext setBrightnessOverride(int brightness);

    ISBRContext setLightnessOverride(float lightness);

    ISBRContext setupColor(ForgeDirection side, short[] rgba);

    ISBRContext setRenderBoundsFromBlock();

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
    void renderNegativeYFacing(ITexture[] tex);

    void renderPositiveYFacing(ITexture[] tex);

    void renderNegativeZFacing(ITexture[] tex);

    void renderPositiveZFacing(ITexture[] tex);

    void renderNegativeXFacing(ITexture[] tex);

    void renderPositiveXFacing(ITexture[] tex);

    /**
     * Sets up the color using lightness, brightness, and the primary color value (usually the dye color) for the side.
     *
     * @param side     the side
     * @param hexColor the primary color
     */
    ISBRContext setupColor(ForgeDirection side, int hexColor);

    /**
     * Checks if rendering is allowed for the current pass.
     *
     * @param predicate a {@code boolean function(int pass)} to evaluate permission
     * @return {@code true} if rendering is allowed for this pass
     */
    boolean canRenderInPass(@NotNull IntPredicate predicate);
}
