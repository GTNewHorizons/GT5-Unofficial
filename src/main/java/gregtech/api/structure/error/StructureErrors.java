package gregtech.api.structure.error;

import static gregtech.api.enums.GTValues.VN;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.HatchElement;

public final class StructureErrors {

    private StructureErrors() {}

    public static TranslatableStructureError of(String langKey) {
        return new TranslatableStructureError(TranslatableText.lang(langKey));
    }

    public static TranslatableStructureError of(String langKey, TranslatableText... args) {
        return new TranslatableStructureError(TranslatableText.lang(langKey, args));
    }

    public static TranslatableStructureError missingCasings(int current, int required) {
        return of("GT5U.gui.missing_casings", TranslatableText.literal(required), TranslatableText.literal(current));
    }

    /**
     * Creates a hatch count error with the appropriate lang key based on the error type.
     */
    public static TranslatableStructureError hatchCount(ErrorType type, TranslatableText name, int current,
        int target) {
        return switch (type) {
            case TOO_FEW -> {
                if (target == 1) {
                    yield of("GT5U.gui.missing_hatch", name);
                } else {
                    yield of(
                        "GT5U.gui.text.too_few_hatch",
                        name,
                        TranslatableText.literal(target),
                        TranslatableText.literal(current));
                }
            }
            case NOT_MATCH -> of("GT5U.gui.text.not_match_hatch", TranslatableText.literal(target), name);
            case TOO_MANY -> of(
                "GT5U.gui.text.too_many_hatch",
                name,
                TranslatableText.literal(target),
                TranslatableText.literal(current));
        };
    }

    public static TranslatableStructureError hatchCount(ErrorType type, HatchElement element, int current, int target) {
        return hatchCount(type, TranslatableText.hatchName(element), current, target);
    }

    public static TranslatableStructureError hatchCount(ErrorType type, ItemStack stack, int current, int target) {
        return hatchCount(type, TranslatableText.itemName(stack), current, target);
    }

    public static TranslatableStructureError missingHatch(ItemStack stack) {
        return of("GT5U.gui.missing_hatch", TranslatableText.itemName(stack));
    }

    public static TranslatableStructureError tooManyHatches(ItemStack stack, int max) {
        return of("GT5U.gui.too_many_hatches", TranslatableText.itemName(stack), TranslatableText.literal(max));
    }

    public static TranslatableStructureError glassTierNotEnough(int requiredTier) {
        return of("GT5U.gui.text.glass_tier_not_enough", TranslatableText.literal(VN[requiredTier]));
    }

    public static TranslatableStructureError energyHatchTierTooLow(int needed) {
        return of("GT5U.gui.text.energy_hatch_tier_too_low", TranslatableText.literal(VN[needed]));
    }

    public static TranslatableStructureError missingOutputHatchDT(int layer) {
        return of("GT5U.gui.text.dt_missing_output_hatch", TranslatableText.literal(layer));
    }

    public static TranslatableStructureError tooManyInputHatch(int max, int current) {
        return of(
            "GT5U.gui.text.too_many_input_hatch",
            TranslatableText.literal(max),
            TranslatableText.literal(current));
    }
}
