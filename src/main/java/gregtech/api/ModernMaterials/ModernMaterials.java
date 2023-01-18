package gregtech.api.ModernMaterials;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.PartProperties.Rendering.ModernMaterialRenderer;
import gregtech.api.ModernMaterials.PartsClasses.MaterialPart;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.HashMap;

public class ModernMaterials {

    public static final HashMap<Integer, ModernMaterial> materialIdToMaterial = new HashMap<>();

    public static void registerMaterial(ModernMaterial material) {
        materialIdToMaterial.put(material.getMaterialID(), material);
    }

    public static void registerAllMaterials() {
        for (PartsEnum part : PartsEnum.values()) {
            MaterialPart materialPart = new MaterialPart(part);
            materialPart.setUnlocalizedName(part.partName);

            // Registers the item with the game, only available in preInit.
            GameRegistry.registerItem(materialPart, part.partName);

            // Registers the renderer which allows for part colouring.
            MinecraftForgeClient.registerItemRenderer(materialPart, new ModernMaterialRenderer());
        }
    }
}
