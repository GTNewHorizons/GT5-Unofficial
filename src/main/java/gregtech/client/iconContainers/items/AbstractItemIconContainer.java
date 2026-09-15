package gregtech.client.iconContainers.items;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import gregtech.api.interfaces.IIconContainer;

abstract class AbstractItemIconContainer implements IIconContainer {

    @Override
    public ResourceLocation getTextureFile() {
        return TextureMap.locationItemsTexture;
    }
}
