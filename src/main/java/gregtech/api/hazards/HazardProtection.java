package gregtech.api.hazards;

import java.util.Map;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.enchants.EnchantmentHazmat;

public class HazardProtection {

    public static boolean isWearingFullFrostHazmat(@NotNull EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.FROST);
    }

    public static boolean isWearingFullHeatHazmat(@NotNull EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.HEAT);
    }

    public static boolean isWearingFullBioHazmat(@NotNull EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.BIOLOGICAL);
    }

    public static boolean isWearingFullRadioHazmat(@NotNull EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.RADIOLOGICAL);
    }

    public static boolean isWearingFullElectroHazmat(@NotNull EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.ELECTRICAL);
    }

    public static boolean isWearingFullGasHazmat(@NotNull EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.GAS);
    }

    public static boolean isWearingFullSpaceHazmat(@NotNull EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.SPACE);
    }

    public static boolean isWearingFullHazmatAgainst(@NotNull EntityLivingBase entity, @NotNull Hazard hazard) {
        for (byte i = 1; i < 5; i++) {
            ItemStack stack = entity.getEquipmentInSlot(i);

            if (!protectsAgainstHazard(stack, hazard)) {
                return false;
            }
        }
        return true;
    }

    public static boolean protectsAgainstHazard(@Nullable ItemStack stack, @NotNull Hazard hazard) {
        return stack != null && (hasHazmatEnchant(stack) || (stack.getItem() instanceof IHazardProtector hazardProtector
            && hazardProtector.protectsAgainst(stack, hazard)));
    }

    public static boolean providesFullHazmatProtection(@Nullable ItemStack stack) {
        if (stack == null) return false;
        for (Hazard hazard : Hazard.VALUES) {
            if (!protectsAgainstHazard(stack, hazard)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasHazmatEnchant(@Nullable ItemStack stack) {
        if (stack == null) return false;
        Map<Integer, Integer> tEnchantments = EnchantmentHelper.getEnchantments(stack);
        Integer level = tEnchantments.get(EnchantmentHazmat.INSTANCE.effectId);

        return level != null && level >= 1;
    }
}
