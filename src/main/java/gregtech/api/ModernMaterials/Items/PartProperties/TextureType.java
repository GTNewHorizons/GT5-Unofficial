package gregtech.api.ModernMaterials.Items.PartProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum;

public enum TextureType {

    Metallic,
    Dull;
    // Stone,
    // Dull;
    // Dull,
    // CustomUnified,
    // CustomIndividual;

    private final HashMap<ItemsEnum, ArrayList<IconWrapper>> textureStorage = new HashMap<>();

    public void addTexture(ItemsEnum part, IconWrapper iconWrapper) {
        ArrayList<IconWrapper> iconList = textureStorage.getOrDefault(part, new ArrayList<>());
        iconList.add(iconWrapper);
        textureStorage.put(part, iconList);
    }

    public ArrayList<IconWrapper> getTextureArray(ItemsEnum part) {
        return textureStorage.get(part);
    }

    // Sort each items textures by priority determined by IconWrapper.
    public void sortLayers() {
        for (ArrayList<IconWrapper> iconList : textureStorage.values()) {
            iconList.sort(Comparator.comparingInt(IconWrapper::getPriority));
        }
    }

}
