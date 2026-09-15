package gregtech.api.interfaces;

/**
 * To be implemented on blocks. Usually machine casing blocks.
 */
public interface IHasIndexedTexture {

    /**
     * Returns the statically mapped texture for this casing. Return
     * {@link gregtech.api.enums.Textures.BlockIcons#ERROR_TEXTURE_INDEX} if meta maps to a nonexistent block, or the
     * block does not have a statically mapped texture.
     * 
     * @param aMeta block meta
     * @return texture index into {@link gregtech.api.enums.Textures.BlockIcons#casingTexturePages}
     */
    int getTextureIndex(int aMeta);
}
