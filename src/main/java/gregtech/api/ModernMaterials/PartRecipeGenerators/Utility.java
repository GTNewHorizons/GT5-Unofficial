package gregtech.api.ModernMaterials.PartRecipeGenerators;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;

public abstract class Utility {

    public static void registerAllMaterialsRecipes() {

        for (ModernMaterial material : ModernMaterialUtilities.materialIDToMaterial.values()) {
            material.registerRecipes(material);
        }

    }
}
