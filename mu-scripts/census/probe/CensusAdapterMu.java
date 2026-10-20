package gttests.census;

import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.OreDictUtils;

final class CensusAdapter {

    static String associationName(ItemStack stack) {
        return OreDictUtils.associationName(stack);
    }

    static boolean isBlacklisted(ItemStack stack) {
        return OreDictUtils.isBlacklisted(stack);
    }

    static boolean isUnifiedCopy(ItemStack stack) {
        return OreDictUtils.isUnifiedCopy(stack);
    }

    static List<ItemStack> shownAlternatives(GTRecipe recipe, ItemStack stack) {
        return recipe.exactItemInputs ? List.of(stack) : OreDictUtils.getInterchangeableStacks(stack);
    }
}
