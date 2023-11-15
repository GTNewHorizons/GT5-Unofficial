package gregtech.api.ModernMaterials.Items.PartProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum;
import gregtech.api.ModernMaterials.ModernMaterial;

public enum TextureType {

    Metal_Shiny,
    Metal_Dull,
    Custom;
    // Stone;

    private static final HashSet<ModernMaterial> customTextureMaterials = new HashSet<>();
    private final HashMap<ItemsEnum, ArrayList<IconWrapper>> standardTextureStorage = new HashMap<>();
    private static final HashMap<Integer, ArrayList<IconWrapper>> customTextureStorage = new HashMap<>();

    // Standard texture retrieval.
    public void addStandardTexture(ItemsEnum part, IconWrapper iconWrapper) {
        ArrayList<IconWrapper> iconList = standardTextureStorage.getOrDefault(part, new ArrayList<>());
        iconList.add(iconWrapper);

        standardTextureStorage.put(part, iconList);
    }

    public ArrayList<IconWrapper> getStandardTextureArray(ItemsEnum part) {
        return standardTextureStorage.get(part);
    }

    // Custom texture retrieval.
    public static void addCustomTexture(ModernMaterial material, ItemsEnum part, IconWrapper iconWrapper) {
        ArrayList<IconWrapper> iconList = customTextureStorage
            .getOrDefault(Objects.hash(material.getMaterialID(), part), new ArrayList<>());
        iconList.add(iconWrapper);

        customTextureStorage.put(Objects.hash(material.getMaterialID(), part), iconList);
    }

    public ArrayList<IconWrapper> getCustomTextures(ModernMaterial material, ItemsEnum part) {
        return customTextureStorage.get(Objects.hash(material.getMaterialID(), part));
    }

    public static void registerCustomMaterial(ModernMaterial material) {
        customTextureMaterials.add(material);
    }

    public static Set<ModernMaterial> getCustomTextureMaterials() {
        return Collections.unmodifiableSet(customTextureMaterials);
    }

}
