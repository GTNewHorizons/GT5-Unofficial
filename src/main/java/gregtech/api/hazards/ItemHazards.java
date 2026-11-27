package gregtech.api.hazards;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.StackMaterialInfo;

import gregtech.api.enchants.EnchantmentRadioactivity;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.GTShapeProperties;
import gregtech.api.material.MaterialData;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.ItemData;

/// The hazards an item stack inflicts on whoever carries it, read from its material and shape. A stack is resolved
/// through MaterialLib, and through [MaterialData] for items MaterialLib does not know.
public final class ItemHazards {

    /// The radiation level of `stack`: the highest of its [IRadioactiveItem] level, its material's
    /// [GTMaterialProperties#RADIATION_LEVEL], and its own radioactivity enchantment. `0` when it is not radioactive.
    public static int radiationLevel(ItemStack stack) {
        int level = stack.getItem() instanceof IRadioactiveItem item ? item.getRadiationLevel(stack) : 0;
        Material material = materialOf(stack);
        if (material != null) {
            Integer materialLevel = material.getProperty(GTMaterialProperties.RADIATION_LEVEL);
            if (materialLevel != null) level = Math.max(level, materialLevel);
        }
        return Math
            .max(level, EnchantmentHelper.getEnchantmentLevel(EnchantmentRadioactivity.INSTANCE.effectId, stack));
    }

    /// Whether `stack`'s material is [GTMaterialProperties#TOXIC].
    public static boolean isToxic(ItemStack stack) {
        Material material = materialOf(stack);
        return material != null && Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.TOXIC));
    }

    /// The heat damage `stack` deals: its shape's [GTShapeProperties#HEAT_DAMAGE] plus its material's
    /// [GTMaterialProperties#HEAT_DAMAGE]. Negative is frost damage.
    public static float heatDamage(ItemStack stack) {
        StackMaterialInfo info = MaterialLibAPI.lookupStack(stack);
        if (info != null) {
            return info.shape()
                .getProperty(GTShapeProperties.HEAT_DAMAGE) + MaterialUtils.heatDamage(info.material());
        }
        ItemData data = MaterialData.get(stack);
        if (data == null) return 0;
        return (data.prefix == null ? 0 : data.prefix.heatDamage)
            + (data.hasValidMaterialData() ? MaterialUtils.heatDamage(data.material.material) : 0);
    }

    private static @Nullable Material materialOf(ItemStack stack) {
        StackMaterialInfo info = MaterialLibAPI.lookupStack(stack);
        if (info != null) return info.material();
        ItemData data = MaterialData.get(stack);
        return data != null && data.hasValidMaterialData() ? data.material.material : null;
    }

    private ItemHazards() {}
}
