package gregtech.api.modernmaterials.items.partproperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.PartTextureConfig;
import gregtech.api.modernmaterials.items.partclasses.ItemsEnum;

public enum TextureType {

    Metal_Shiny,
    Metal_Dull,
    Custom,
    Stone,
    Dust,
    Gem;

    private static final HashSet<ModernMaterial> customTextureMaterials = new HashSet<>();
    private final HashMap<ItemsEnum, List<IconWrapper>> standardTextureStorage = new HashMap<>();
    private static final HashMap<Integer, List<IconWrapper>> customTextureStorage = new HashMap<>();
    public Map<String, PartTextureConfig> textureConfigs;

    // Standard texture retrieval.
    public void addStandardTexture(ItemsEnum part, IconWrapper iconWrapper) {
        List<IconWrapper> iconList = standardTextureStorage.getOrDefault(part, new ArrayList<>());
        iconList.add(iconWrapper);

        standardTextureStorage.put(part, iconList);
    }

    public List<IconWrapper> getStandardTextureArray(ItemsEnum part) {
        return standardTextureStorage.getOrDefault(part, Collections.emptyList());
    }

    // Custom texture retrieval.
    public static void addCustomTexture(ModernMaterial material, ItemsEnum part, IconWrapper iconWrapper) {
        List<IconWrapper> iconList = customTextureStorage
            .getOrDefault(Objects.hash(material.getMaterialID(), part), new ArrayList<>());
        iconList.add(iconWrapper);

        customTextureStorage.put(Objects.hash(material.getMaterialID(), part), iconList);
    }

    public List<IconWrapper> getCustomTextures(ModernMaterial material, ItemsEnum part) {
        return customTextureStorage.get(Objects.hash(material.getMaterialID(), part));
    }

    public static void registerCustomMaterial(ModernMaterial material) {
        customTextureMaterials.add(material);
    }

    public static Set<ModernMaterial> getCustomTextureMaterials() {
        return Collections.unmodifiableSet(customTextureMaterials);
    }

    HashMap<ModernMaterial, Map<String, PartTextureConfig>> customPartMap = new HashMap<>();

    public void setCustomJson(ModernMaterial material, Map<String, PartTextureConfig> textureConfigs) {
        if (this != Custom) throw new RuntimeException("Invalid registration.");

        customPartMap.put(material, textureConfigs);
    }
}
