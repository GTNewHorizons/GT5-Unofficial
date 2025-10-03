package gregtech.api.interfaces;

import javax.annotation.Nullable;

public interface IBlockWithTextures {

    /**
     * @return Texture[forge direction ordinal][layer]
     */
    @Nullable
    ITexture[][] getTextures(int metadata);
}
