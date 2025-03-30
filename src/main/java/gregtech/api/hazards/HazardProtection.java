package gregtech.api.hazards;

import java.util.Collection;
import java.util.Map;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.GregTechAPI;
import gregtech.api.enchants.EnchantmentHazmat;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.GTUtility;

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
        return stack != null
            && (GTUtility.isStackInList(stack, getHazardProtectionEquipmentList(hazard)) || hasHazmatEnchant(stack)
                || (stack.getItem() instanceof IHazardProtector hazardProtector
                    && hazardProtector.protectsAgainst(stack, hazard)));
    }

    private static @NotNull Collection<GTItemStack> getHazardProtectionEquipmentList(@NotNull Hazard hazard) {
        return switch (hazard) {
            case BIOLOGICAL -> GregTechAPI.sBioHazmatList;
            case FROST -> GregTechAPI.sFrostHazmatList;
            case HEAT -> GregTechAPI.sHeatHazmatList;
            case RADIOLOGICAL -> GregTechAPI.sRadioHazmatList;
            case ELECTRICAL -> GregTechAPI.sElectroHazmatList;
            case GAS -> GregTechAPI.sGasHazmatList;
        };
    }

    public static boolean providesFullHazmatProtection(@Nullable ItemStack stack) {
        if (stack == null) return false;
        for (Hazard hazard : Hazard.values()) {
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
