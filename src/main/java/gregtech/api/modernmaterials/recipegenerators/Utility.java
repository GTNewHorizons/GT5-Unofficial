package gregtech.api.modernmaterials.recipegenerators;

import gregtech.api.modernmaterials.ModernMaterial;

public abstract class Utility {

    public static void registerAllMaterialsRecipes() {

        for (ModernMaterial material : ModernMaterial.getAllMaterials()) {
            material.registerRecipes(material);
        }

    }
}
