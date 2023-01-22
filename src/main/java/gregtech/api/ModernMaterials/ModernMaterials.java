package gregtech.api.ModernMaterials;

import static gregtech.api.enums.ConfigCategories.ModernMaterials.*;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.PartProperties.Rendering.ModernMaterialRenderer;
import gregtech.api.ModernMaterials.PartsClasses.MaterialPart;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraftforge.client.MinecraftForgeClient;

public class ModernMaterials {

    private static final List<ModernMaterial> mNewMaterials = new ArrayList<>();
    public static final HashMap<Integer, ModernMaterial> materialIdToMaterial = new HashMap<>();
    public static final HashMap<String, ModernMaterial> mNameMaterialMap = new HashMap<>();

    public static void registerMaterial(ModernMaterial aMaterial) {
        final int tCurrentMaterialID = GregTech_API.sModernMaterialIDs
                .mConfig
                .get(materialID.name(), aMaterial.getName(), -1)
                .getInt();
        if (tCurrentMaterialID == -1) {
            mNewMaterials.add(aMaterial);
        } else {
            aMaterial.setID(tCurrentMaterialID);
            materialIdToMaterial.put(tCurrentMaterialID, aMaterial);
            if (tCurrentMaterialID > GregTech_API.mLastMaterialID) {
                GregTech_API.mLastMaterialID = tCurrentMaterialID;
            }
        }
        mNameMaterialMap.put(aMaterial.getName(), aMaterial);
    }

    public static void registerAllMaterials() {
        for (ModernMaterial tMaterial : mNewMaterials) {
            tMaterial.setID(++GregTech_API.mLastMaterialID);
            GregTech_API.sModernMaterialIDs
                    .mConfig
                    .get(materialID.name(), tMaterial.getName(), 0)
                    .set(GregTech_API.mLastMaterialID);
            materialIdToMaterial.put(GregTech_API.mLastMaterialID, tMaterial);
        }
        for (PartsEnum tPart : PartsEnum.values()) {
            MaterialPart materialPart = new MaterialPart(tPart);
            materialPart.setUnlocalizedName(tPart.partName);

            // Registers the item with the game, only available in preInit.
            GameRegistry.registerItem(materialPart, tPart.partName);

            // Registers the renderer which allows for part colouring.
            MinecraftForgeClient.registerItemRenderer(materialPart, new ModernMaterialRenderer());
        }
    }
}
