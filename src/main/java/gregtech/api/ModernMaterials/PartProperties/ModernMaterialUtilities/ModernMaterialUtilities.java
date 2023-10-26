package gregtech.api.ModernMaterials.PartProperties.ModernMaterialUtilities;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.*;

import net.minecraft.item.ItemStack;

import gregtech.api.ModernMaterials.ModernMaterial;

public class ModernMaterialUtilities {

    // Item damage values are material IDs.
    public static ModernMaterial getMaterialFromItemStack(ItemStack itemStack) {
        return materialIDToMaterial.get(itemStack.getItemDamage());
    }

}
