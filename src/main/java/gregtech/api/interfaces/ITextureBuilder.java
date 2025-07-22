package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import gregtech.api.render.TextureFactory;

/**
 * <p>
 * This Interface defines operations to configure and build instances of the {@link ITexture} implementations
 * </p>
 * <p>
 * Use the {@link TextureFactory#builder()} method to get an instance of the {@link ITextureBuilder} implementation.
 * </p>
 */
public interface ITextureBuilder {

    /**
     * Build the {@link ITexture}
     *
     * @return The built {@link ITexture}
     * @throws IllegalStateException if setFromBlock has never been called.
     */
    ITexture build();

    /**
     * @param block The {@link Block}
     * @param meta  The meta value for the Block
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setFromBlock(final Block block, final int meta);

    /**
     * @param side
     *             <p>
     *             The {@link ForgeDirection} side providing the texture
     *             </p>
     *             <p>
     *             Default is {@link ForgeDirection#UNKNOWN} to use same side as rendered
     *             </p>
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setFromSide(final ForgeDirection side);

    /**
     * @param iconContainers The {@link IIconContainer}s to add
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder addIcon(final IIconContainer... iconContainers);

    /**
     * @param rgba The RGBA tint for this {@link ITexture}
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setRGBA(final short[] rgba);

    /**
     * @param iTextures The {@link ITexture} layers to add
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder addLayer(final ITexture... iTextures);

    /**
     * Set alpha blending
     *
     * @param allowAlpha to set
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setAllowAlpha(final boolean allowAlpha);

    /**
     * Texture will render with same orientation as with vanilla blocks
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder stdOrient();

    /**
     * Force using world coord overload of getIcon.
     *
     * @return {@link ITextureBuilder} for chaining
     * @throws IllegalStateException if setFromBlock has never been called.
     */
    ITextureBuilder useWorldCoord();

    /**
     * Force using meta overload of getIcon.
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder noWorldCoord();

    /**
     * Texture will orientate from block's {@link ExtendedFacing}
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder extFacing();

    /**
     * Texture always render with full brightness to glow in the dark
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder glow();
}
