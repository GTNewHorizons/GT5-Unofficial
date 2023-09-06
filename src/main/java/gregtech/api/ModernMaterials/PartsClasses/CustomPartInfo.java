package gregtech.api.ModernMaterials.PartsClasses;

import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;
import org.jetbrains.annotations.NotNull;

public class CustomPartInfo {

    public final MaterialPartsEnum part;
    private final TextureType textureType;

    public CustomPartInfo(@NotNull final MaterialPartsEnum part, @NotNull final TextureType textureType) {
        this.part = part;
        this.textureType = textureType;
    }

    public TextureType getTextureType() {
        return textureType;
    }

}
