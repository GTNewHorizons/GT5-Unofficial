package gttests.census;

import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

final class CensusAdapter {

    static String associationName(ItemStack stack) {
        ItemData data = GTOreDictUnificator.getAssociation(stack);
        return data != null && data.hasValidPrefixMaterialData() ? data.toString() : null;
    }

    static boolean isBlacklisted(ItemStack stack) {
        return GTOreDictUnificator.isBlacklisted(stack);
    }

    static boolean isUnifiedCopy(ItemStack stack) {
        ItemStack target = GTOreDictUnificator.get_nocopy(true, stack);
        return target != null && !GTUtility.areStacksEqual(target, stack, true);
    }

    static List<ItemStack> shownAlternatives(gregtech.api.util.GTRecipe recipe, ItemStack stack) {
        return GTOreDictUnificator.getNonUnifiedStacks(stack);
    }
}
