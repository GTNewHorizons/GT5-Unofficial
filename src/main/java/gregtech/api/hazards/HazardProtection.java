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

    public static boolean isWearingFullFrostHazmat(EntityLivingBase aEntity) {
        return isWearingFullHazmatAgainst(aEntity, Hazard.FROST);
    }

    public static boolean isWearingFullHeatHazmat(EntityLivingBase aEntity) {
        return isWearingFullHazmatAgainst(aEntity, Hazard.HEAT);
    }

    public static boolean isWearingFullBioHazmat(EntityLivingBase aEntity) {
        return isWearingFullHazmatAgainst(aEntity, Hazard.BIOLOGICAL);
    }

    public static boolean isWearingFullRadioHazmat(EntityLivingBase aEntity) {
        return isWearingFullHazmatAgainst(aEntity, Hazard.RADIOLOGICAL);
    }

    public static boolean isWearingFullElectroHazmat(EntityLivingBase aEntity) {
        return isWearingFullHazmatAgainst(aEntity, Hazard.ELECTRICAL);
    }

    public static boolean isWearingFullGasHazmat(EntityLivingBase aEntity) {
        return isWearingFullHazmatAgainst(aEntity, Hazard.GAS);
    }

    public static boolean isWearingFullHazmatAgainst(EntityLivingBase aEntity, Hazard hazard) {
        for (byte i = 1; i < 5; i++) {
            ItemStack tStack = aEntity.getEquipmentInSlot(i);

            if (!protectsAgainstHazard(tStack, hazard)) {
                return false;
            }
        }
        return true;
    }

    public static boolean protectsAgainstHazard(ItemStack tStack, Hazard hazard) {
        GTHashSet list = switch (hazard) {
            case BIOLOGICAL: {
                yield GregTechAPI.sBioHazmatList;
            }
            case FROST: {
                yield GregTechAPI.sFrostHazmatList;
            }
            case HEAT: {
                yield GregTechAPI.sHeatHazmatList;
            }
            case RADIOLOGICAL: {
                yield GregTechAPI.sRadioHazmatList;
            }
            case ELECTRICAL: {
                yield GregTechAPI.sElectroHazmatList;
            }
            case GAS: {
                yield GregTechAPI.sGasHazmatList;
            }
        };
        return GTUtility.isStackInList(tStack, list) || hasHazmatEnchant(tStack)
            || (tStack.getItem() instanceof IHazardProtector
                && ((IHazardProtector) tStack.getItem()).protectsAgainst(tStack, hazard));
    }

    public static boolean providesFullHazmatProtection(ItemStack aStack) {
        for (Hazard hazard : Hazard.values()) {
            if (!protectsAgainstHazard(aStack, hazard)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasHazmatEnchant(ItemStack aStack) {
        if (aStack == null) return false;
        Map<Integer, Integer> tEnchantments = EnchantmentHelper.getEnchantments(aStack);
        Integer tLevel = tEnchantments.get(EnchantmentHazmat.INSTANCE.effectId);

        return tLevel != null && tLevel >= 1;
    }
}
