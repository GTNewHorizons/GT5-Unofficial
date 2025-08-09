package gregtech.api.render;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import gregtech.common.render.GTTextureBuilder;

/**
 * <p>
 * This class contains a collection of static factory methods to access the New Texture API.
 * </p>
 * <p>
 * The {@link #of} methods directly returns ready-to-use instances of {@link ITexture} implementations.
 * </p>
 * <p>
 * To get more specific implementations of {@link ITexture} instances, use the {@link #builder()} method.
 * </p>
 * <p>
 * Example of the {@link #builder()}:
 * </p>
 *
 * <pre>
 * {@code
 *     // Texture that glows in the dark
 *     TextureFactory.builder().addIcon(OVERLAY_FUSION1_GLOW).glow().build());
 *
 *     // Texture with same bottom flipped orientation as vanilla
 *     TextureFactory.builder().addIcon(GRANITE_RED_STONE).stdOrient().build();
 * }
 * </pre>
 *
 * See: the {@link ITextureBuilder} interface
 */
@SuppressWarnings("unused")
public final class TextureFactory {

    private TextureFactory() {
        throw new AssertionError("Non-instantiable class");
    }

    /**
     * Multi-layered {@link ITexture} factory
     *
     * @param texture The layer of {@link ITexture} from bottom to top
     * @return The instance of an {@link ITexture} implementation
     */
    public static ITexture of(final ITexture texture) {
        return builder().addLayer(texture)
            .build();
    }

    /**
     * Multi-layered {@link ITexture} factory
     *
     * @param textures The layers of {@link ITexture} from bottom to top
     * @return The instance of an {@link ITexture} implementation
     */
    public static ITexture of(final ITexture... textures) {
        return builder().addLayer(textures)
            .build();
    }

    /**
     * All-Sided {@link ITexture} factory
     *
     * @param bottom   The {@link IIconContainer} Icon for the Bottom Side.
     * @param top      The {@link IIconContainer} Icon for the Top Side.
     * @param north    The {@link IIconContainer} Icon for the North Side.
     * @param south    The {@link IIconContainer} Icon for the South Side.
     * @param west     The {@link IIconContainer} Icon for the West Side.
     * @param east     The {@link IIconContainer} Icon for the East Side.
     * @param colorRGB The {@code int} RGB tint for all sides.
     * @return The instance of an {@link ITexture} implementation
     */
    public static ITexture of(final IIconContainer bottom, final IIconContainer top, final IIconContainer north,
        final IIconContainer south, final IIconContainer west, final IIconContainer east, final int colorRGB) {
        return builder().addIcon(bottom, top, north, south, west, east)
            .setRGB(colorRGB)
            .build();
    }

    /**
     * Bottom-Top-Sides-Sided {@link ITexture} factory
     *
     * @param bottom   The {@link IIconContainer} Icon for the Bottom Side.
     * @param top      The {@link IIconContainer} Icon for the Top Side.
     * @param sides    The {@link IIconContainer} Icon for the North, South, West and East Sides.
     * @param colorRGB The {@code int} RGB tint for all sides.
     * @return The instance of an {@link ITexture} implementation
     */
    public static ITexture of(final IIconContainer bottom, final IIconContainer top, final IIconContainer sides,
        final int colorRGB) {
        return builder().addIcon(bottom, top, sides, sides, sides, sides)
            .setRGB(colorRGB)
            .build();
    }

    /**
     * @deprecated use int color method instead
     */
    @Deprecated
    public static ITexture of(final IIconContainer iconContainer, final short[] rgba) {
        return builder().addIcon(iconContainer)
            .setRGBA(rgba)
            .build();
    }

    public static ITexture of(final IIconContainer iconContainer, final int colorRGB) {
        return builder().addIcon(iconContainer)
            .setRGB(colorRGB)
            .build();
    }

    public static ITexture of(final IIconContainer iconContainer) {
        return builder().addIcon(iconContainer)
            .build();
    }

    /**
     * Copied-Block {@link ITexture} factory that will render a texture copied from the side of a {@link Block}.
     *
     * @param block    The {@link Block} that will provide the texture
     * @param meta     The meta value for the Block
     * @param side     The {@link ForgeDirection} side providing the texture
     * @param colorRGB The RGB tint to apply
     * @return The instance of an {@link ITexture} implementation
     */
    public static ITexture of(final Block block, final int meta, final ForgeDirection side, final int colorRGB) {
        return builder().setFromBlock(block, meta)
            .setFromSide(side)
            .setRGB(colorRGB)
            .build();
    }

    public static ITexture of(final Block block, final int meta, final ForgeDirection side) {
        return builder().setFromBlock(block, meta)
            .setFromSide(side)
            .build();
    }

    public static ITexture of(final Block block, final int meta) {
        return builder().setFromBlock(block, meta)
            .build();
    }

    public static ITexture of(final Block block) {
        return of(block, 0);
    }

    /**
     * {@link ITextureBuilder} factory
     *
     * @return An instance of the {@link ITextureBuilder} implementation
     */
    public static ITextureBuilder builder() {
        return new GTTextureBuilder();
    }
}
