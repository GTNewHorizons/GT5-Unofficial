package gregtech.api.interfaces;

/**
 * Interface to a list of bottom-up layered {@link ITexture}
 */
public interface ILayeredTexture extends ITexture, Iterable<ITexture> {

    /**
     * Adds an {@link ITexture} layer atop
     *
     * @param iTexture the texture as top layer
     * @return self for chaining
     */
    ILayeredTexture add(ITexture iTexture);

    /**
     * Adds the {@link ITexture} at layer index
     *
     * @param index    the {@link ITexture} to add
     * @param iTexture the texture layer to add
     * @return self for chaining
     */
    ILayeredTexture add(int index, ITexture iTexture);

    /**
     * Adds all the {@link ITexture} layers atop
     *
     * @param iTexture the {@link ITexture} layers
     * @return self for chaining
     */
    ILayeredTexture addAll(ITexture... iTexture);

    /**
     * Adds all the {@link ITexture} layers starting at index
     *
     * @param index    the index to add the layers at
     * @param iTexture the {@link ITexture} layers to add
     * @return self for chaining
     */
    ILayeredTexture addAll(int index, ITexture... iTexture);

    /**
     * @return the number of {@link ITexture} layers
     */
    int size();

    /**
     * @return true if there is no layer
     */
    boolean isEmpty();
}
