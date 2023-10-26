package gregtech.api.ModernMaterials.PartsClasses;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;

public class CustomPartInfo {

    public final ItemsEnum part;
    private final TextureType textureType;

    public CustomPartInfo(@NotNull final ItemsEnum part, @NotNull final TextureType textureType) {
        this.part = part;
        this.textureType = textureType;
    }

    public TextureType getTextureType() {
        return textureType;
    }

}
