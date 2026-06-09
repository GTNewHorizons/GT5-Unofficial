package gregtech.api.recipe;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public final class OreRecipeRegistrationInputs {

    private OreRecipeRegistrationInputs() {}

    @Nullable
    public static ItemStack recipeInputStack(OrePrefixes prefix, Materials material, ItemStack stack) {
        return recipeInputStack(prefix, material, stack, 1);
    }

    @Nullable
    public static ItemStack recipeInputStack(OrePrefixes prefix, Materials material, ItemStack stack, long amount) {
        if (stack == null) {
            return null;
        }
        if (!OreRecipeDedupeFlags.canonicalInputsEnabled()) {
            return GTUtility.copyAmount(amount, stack);
        }
        ItemStack canonical = GTOreDictUnificator.get(prefix, material, amount);
        if (canonical == null) {
            return GTUtility.copyAmount(amount, stack);
        }
        return GTUtility.copyAmount(amount, canonical);
    }
}
