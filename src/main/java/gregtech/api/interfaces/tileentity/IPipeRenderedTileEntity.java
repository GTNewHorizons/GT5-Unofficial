package gregtech.api.interfaces.tileentity;

import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;

import java.util.EnumMap;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;

public interface IPipeRenderedTileEntity extends ICoverable, ITexturedTileEntity {

    float getThickNess();

    byte getConnections();

    /**
     * Gets the {@link ITexture} layers of Pipe without a Cover as an array for Side
     *
     * @param aSide the ordinal side
     * @return the {@link ITexture} array of layers
     * @deprecated {@link #getTextureUncovered(ForgeDirection)}
     */
    @Deprecated
    default ITexture[] getTextureUncovered(byte aSide) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the texture of a pipe without a cover as an array for side
     *
     * @param side the {@link ForgeDirection} side
     * @return the {@link ITexture} array of layers
     */
    ITexture getTextureUncovered(ForgeDirection side);

    /**
     * Gets the uncovered textures for all side directions of the pipe
     *
     * @return the <code>{@link EnumMap}&lt;{@link ForgeDirection}, {@link ITexture}&gt;</code>
     *         containing textures for each direction
     */
    EnumMap<ForgeDirection, ITexture> getTexturesMapUncovered();

    /**
     * Gets the {@link ITexture} layers of Pipe with a Cover texture at top layer as an array for Side
     *
     * @param aSide the ordinal side
     * @return the {@link ITexture} array of layers
     * @deprecated {@link #getTextureUncovered(ForgeDirection)}
     */
    @Deprecated
    default ITexture[] getTextureCovered(byte aSide) {
        throw new UnsupportedOperationException();
    }

    ITexture getTextureCovered(ForgeDirection side);

    default EnumMap<ForgeDirection, ITexture> getTexturesMapCovered() {
        EnumMap<ForgeDirection, ITexture> textureMap = new EnumMap<>(ForgeDirection.class);
        for (ForgeDirection direction : VALID_DIRECTIONS) {
            textureMap.put(direction, getTextureCovered(direction));
        }
        return textureMap;
    }
}
