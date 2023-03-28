package gregtech.api.ModernMaterials.PartsClasses;

import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;
import org.jetbrains.annotations.NotNull;

public class CustomPartInfo {

    public final PartsEnum part;
    private final TextureType textureType;

    public CustomPartInfo(@NotNull final PartsEnum part, @NotNull final TextureType textureType) {
        this.part = part;
        this.textureType = textureType;
    }

    public TextureType getTextureType() {
        return textureType;
    }

}
