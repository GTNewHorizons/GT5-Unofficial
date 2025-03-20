package gregtech.api.interfaces;

import javax.annotation.Nullable;

public interface IBlockWithTextures {

    /**
     * @return Texture[forge direction ordinal][layer]
     */
    @Nullable
    public ITexture[][] getTextures(int metadata);
}
