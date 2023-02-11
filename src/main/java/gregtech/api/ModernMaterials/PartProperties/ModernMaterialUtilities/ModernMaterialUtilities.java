package gregtech.api.ModernMaterials.PartProperties.ModernMaterialUtilities;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.*;

import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.item.ItemStack;

public class ModernMaterialUtilities {

    // Item damage values are material IDs.
    public static ModernMaterial getMaterialFromItemStack(ItemStack itemStack) {
        return materialIdToMaterial.get(itemStack.getItemDamage());
    }

    public static ModernMaterial getMaterialFromName(String aName) {
        return mNameMaterialMap.get(aName);
    }
}
