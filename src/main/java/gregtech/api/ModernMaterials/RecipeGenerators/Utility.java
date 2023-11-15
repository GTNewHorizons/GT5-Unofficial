package gregtech.api.ModernMaterials.RecipeGenerators;

import gregtech.api.ModernMaterials.ModernMaterial;

public abstract class Utility {

    public static void registerAllMaterialsRecipes() {

        for (ModernMaterial material : ModernMaterial.allMaterials()) {
            material.registerRecipes(material);
        }

    }
}
