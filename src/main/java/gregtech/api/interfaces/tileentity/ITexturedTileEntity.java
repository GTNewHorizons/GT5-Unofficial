package gregtech.api.interfaces.tileentity;

import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;

import java.util.EnumMap;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

public interface ITexturedTileEntity {

    /**
     * @return the Textures rendered by the GT Rendering
     * @deprecated use {@link #getTexture(Block, ForgeDirection)
     */
    @Deprecated
    default ITexture[] getTexture(Block aBlock, byte aSide) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the texture to be rendered for the block's side at given direction
     *
     * @param block the {@link Block} being textured
     * @param side  the {@link ForgeDirection} of the side being textured
     * @return the {@link ITexture} to be rendered
     */
    default ITexture getTexture(Block block, ForgeDirection side) {
        return TextureFactory.of(getTexture(block, (byte) side.ordinal()));
    }

    /**
     * Gets the textures to be rendered for all side directions of the block
     *
     * @param block the {@link Block} being textured
     * @return the <code>{@link EnumMap}&lt;{@link ForgeDirection}, {@link ITexture}&gt;</code>
     *         containing textures for each direction
     */
    default EnumMap<ForgeDirection, ITexture> getTexturesMap(Block block) {
        EnumMap<ForgeDirection, ITexture> directionLayeredTextures = new EnumMap<>(ForgeDirection.class);
        for (ForgeDirection direction : VALID_DIRECTIONS) {
            directionLayeredTextures.put(direction, getTexture(block, direction));
        }
        return directionLayeredTextures;
    }
}
