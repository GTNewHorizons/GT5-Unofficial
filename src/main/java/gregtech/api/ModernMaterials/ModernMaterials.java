package gregtech.api.ModernMaterials;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.PartsClasses.MaterialPart;
import gregtech.api.ModernMaterials.PartsClasses.Parts;

import java.util.HashMap;

public class ModernMaterials {

    public static final HashMap<Integer, ModernMaterial> materialIdToMaterial = new HashMap<>();

    public static void registerMaterial(ModernMaterial material) {
        materialIdToMaterial.put(material.getMaterialID(), material);
    }

    public static void registerAllMaterials() {
        for (Parts part : Parts.values()) {
            MaterialPart materialPart = new MaterialPart(part.getPartName());
            materialPart.setUnlocalizedName(part.getPartName());
            GameRegistry.registerItem(materialPart, part.getPartName());
        }
    }
}
