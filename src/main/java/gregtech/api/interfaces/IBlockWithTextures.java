package gregtech.api.interfaces;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.RenderBlocks;

public interface IBlockWithTextures {

    /**
     * @return Texture[forge direction ordinal][layer]
     */
    @Nullable
    ITexture[][] getTextures(int metadata);

    @Nullable
    default ITexture[][] getTextures(RenderBlocks renderBlocks, int metadata) {
        return getTextures(metadata);
    }
}
