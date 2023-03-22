package gregtech.api.ModernMaterials.PartsClasses;

import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;
import org.jetbrains.annotations.NotNull;

public class CustomPartInfo {

    public final PartsEnum part;
    private TextureType textureType = Metallic;
    private String textureName;

    public CustomPartInfo(@NotNull final PartsEnum part, @NotNull final TextureType textureType) {
        this.part = part;
        this.textureName = part.partName;
        this.textureType = textureType;
    }

    public TextureType getTextureType() {
        return textureType;
    }

    public CustomPartInfo setTextureType(final TextureType aTextureType) {
        textureType = aTextureType;
        return this;
    }

    public String getTextureName() {
        return textureName;
    }
}
