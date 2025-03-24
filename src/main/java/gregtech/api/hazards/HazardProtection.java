package gregtech.api.hazards;

import java.util.Map;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enchants.EnchantmentHazmat;
import gregtech.api.objects.GTHashSet;
import gregtech.api.util.GTUtility;

public class HazardProtection {

    public static boolean isWearingFullFrostHazmat(EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.FROST);
    }

    public static boolean isWearingFullHeatHazmat(EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.HEAT);
    }

    public static boolean isWearingFullBioHazmat(EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.BIOLOGICAL);
    }

    public static boolean isWearingFullRadioHazmat(EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.RADIOLOGICAL);
    }

    public static boolean isWearingFullElectroHazmat(EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.ELECTRICAL);
    }

    public static boolean isWearingFullGasHazmat(EntityLivingBase entity) {
        return isWearingFullHazmatAgainst(entity, Hazard.GAS);
    }

    public static boolean isWearingFullHazmatAgainst(EntityLivingBase entity, Hazard hazard) {
        for (byte i = 1; i < 5; i++) {
            ItemStack stack = entity.getEquipmentInSlot(i);

            if (!protectsAgainstHazard(stack, hazard)) {
                return false;
            }
        }
        return true;
    }

    public static boolean protectsAgainstHazard(ItemStack stack, Hazard hazard) {
        GTHashSet list = switch (hazard) {
            case BIOLOGICAL -> GregTechAPI.sBioHazmatList;
            case FROST -> GregTechAPI.sFrostHazmatList;
            case HEAT -> GregTechAPI.sHeatHazmatList;
            case RADIOLOGICAL -> GregTechAPI.sRadioHazmatList;
            case ELECTRICAL -> GregTechAPI.sElectroHazmatList;
            case GAS -> GregTechAPI.sGasHazmatList;
        };
        return GTUtility.isStackInList(stack, list) || hasHazmatEnchant(stack)
            || (stack.getItem() instanceof IHazardProtector hazardProtector
                && hazardProtector.protectsAgainst(stack, hazard));
    }

    public static boolean providesFullHazmatProtection(ItemStack stack) {
        for (Hazard hazard : Hazard.values()) {
            if (!protectsAgainstHazard(stack, hazard)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasHazmatEnchant(ItemStack stack) {
        if (stack == null) return false;
        Map<Integer, Integer> tEnchantments = EnchantmentHelper.getEnchantments(stack);
        Integer level = tEnchantments.get(EnchantmentHazmat.INSTANCE.effectId);

        return level != null && level >= 1;
    }
}
