package gregtech.api.ModernMaterials.PartProperties.ModernMaterialUtilities;

import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.item.ItemStack;

import static gregtech.api.ModernMaterials.ModernMaterials.materialIdToMaterial;

public class ModernMaterialUtilities {

    // Item damage values are material IDs.
    public static ModernMaterial getMaterialFromItemStack(ItemStack itemStack) {
        return materialIdToMaterial.get(itemStack.getItemDamage());
    }
}
