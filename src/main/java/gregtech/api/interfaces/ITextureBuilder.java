package gregtech.api.interfaces;

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
     * @param iconContainer The {@link IIconContainer}s to add
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder addIcon(final IIconContainer iconContainer);

    /**
     * @param rgba The RGBA tint for this {@link ITexture}
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder setRGBA(final short[] rgba);

    /**
     * Texture will render with same orientation as with vanilla blocks
     *
     * @return {@link ITextureBuilder} for chaining
     */
    ITextureBuilder stdOrient();

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
