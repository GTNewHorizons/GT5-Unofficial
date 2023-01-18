package gregtech.api.ModernMaterials.PartsClasses;

import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;

import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Metallic;

public class CustomPartInfo {

    public final PartsEnum partsEnum;
    private TextureType textureType = Metallic;
    private String textureName;
    private boolean animated;

    public CustomPartInfo(PartsEnum partsEnum) {
        this.partsEnum = partsEnum;
        this.textureName = partsEnum.partName;
    }

    public TextureType getTextureType() {
        return textureType;
    }

    public CustomPartInfo setTextureType(final TextureType textureType) {
        this.textureType = textureType;
        return this;
    }

    public boolean isNotAnimated() {
        return !animated;
    }

    public CustomPartInfo enableCustomTexture() {
        this.animated = true;
        return this;
    }

    public CustomPartInfo setCustomPartTextureOverride(final String filename) {
        this.animated = true;
        this.textureName = filename;
        return this;
    }

    public String getTextureName() {
        return textureName;
    }

}
