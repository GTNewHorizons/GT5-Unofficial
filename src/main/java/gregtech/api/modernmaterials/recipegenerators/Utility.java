package gregtech.api.modernmaterials.recipegenerators;

import gregtech.api.modernmaterials.ModernMaterial;

public abstract class Utility {

    public static double AUTO_GENERATED_TIME_MULTIPLIER = 1; // config

    public static void registerAllMaterialsRecipes() {

        for (ModernMaterial material : ModernMaterial.getAllMaterials()) {
            material.registerRecipes(material);
        }

    }
}
