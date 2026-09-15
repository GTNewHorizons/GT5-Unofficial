package gregtech.api.interfaces;

import javax.annotation.Nullable;

import net.minecraft.world.IBlockAccess;

public interface IBlockWithTextures {

    /**
     * @return Texture[forge direction ordinal][layer]
     */
    @Nullable
    default ITexture[][] getTextures(IBlockAccess world, int x, int y, int z) {
        return getTextures(world.getBlockMetadata(x, y, z));
    }

    /**
     * Allows overriding of texture displayed in the inventory.
     * 
     * @return Texture[forge direction ordinal][layer]
     */
    @Nullable
    default ITexture[][] getInventoryTextures(int meta) {
        return getTextures(meta);
    }

    /**
     * @return Texture[forge direction ordinal][layer]
     */
    @Nullable
    ITexture[][] getTextures(int meta);
}
