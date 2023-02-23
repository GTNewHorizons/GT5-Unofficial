package gregtech.api.ModernMaterials.PartProperties.ModernMaterialUtilities;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.*;

import net.minecraft.item.ItemStack;

import gregtech.api.ModernMaterials.ModernMaterial;

public class ModernMaterialUtilities {

    // Item damage values are material IDs.
    public static ModernMaterial getMaterialFromItemStack(ItemStack itemStack) {
        return materialIdToMaterial.get(itemStack.getItemDamage());
    }

    public static ModernMaterial getMaterialFromName(String aName) {
        return mNameMaterialMap.get(aName);
    }
}
