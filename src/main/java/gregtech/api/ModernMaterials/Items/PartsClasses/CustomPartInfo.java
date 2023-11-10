package gregtech.api.ModernMaterials.Items.PartsClasses;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ModernMaterials.Items.PartProperties.TextureType;

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
