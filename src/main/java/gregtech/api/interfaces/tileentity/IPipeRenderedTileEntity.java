package gregtech.api.interfaces.tileentity;

import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;

import java.util.EnumMap;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

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
    ITexture[] getTextureUncovered(byte aSide);

    /**
     * Gets the texture of a pipe without a cover as an array for side
     *
     * @param side the {@link ForgeDirection} side
     * @return the {@link ITexture} array of layers
     */
    default ITexture getTextureUncovered(ForgeDirection side) {
        return TextureFactory.of(getTextureUncovered((byte) side.ordinal()));
    }

    /**
     * Gets the uncovered textures for all side directions of the pipe
     *
     * @return the <code>{@link EnumMap}&lt;{@link ForgeDirection}, {@link ITexture}&gt;</code>
     *         containing textures for each direction
     */
    default EnumMap<ForgeDirection, ITexture> getTexturesMapUncovered() {
        EnumMap<ForgeDirection, ITexture> textureMap = new EnumMap<>(ForgeDirection.class);
        for (ForgeDirection direction : VALID_DIRECTIONS) {
            textureMap.put(direction, getTextureUncovered(direction));
        }
        return textureMap;
    }

    /**
     * Gets the {@link ITexture} layers of Pipe with a Cover texture at top layer as an array for Side
     *
     * @param aSide the ordinal side
     * @return the {@link ITexture} array of layers
     * @deprecated {@link #getTextureUncovered(ForgeDirection)}
     */
    @Deprecated
    default ITexture[] getTextureCovered(byte aSide) {
        return getTextureUncovered(aSide);
    }

    default ITexture getTextureCovered(ForgeDirection direction) {
        return TextureFactory.of(getTextureCovered((byte) direction.ordinal()));
    }

    default EnumMap<ForgeDirection, ITexture> getTexturesMapCovered() {
        EnumMap<ForgeDirection, ITexture> textureMap = new EnumMap<>(ForgeDirection.class);
        for (ForgeDirection direction : VALID_DIRECTIONS) {
            textureMap.put(direction, getTextureCovered(direction));
        }
        return textureMap;
    }
}
