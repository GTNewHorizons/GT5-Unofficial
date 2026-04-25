package gregtech.api.items;

import static gregtech.api.util.GTUtility.addSeparatorIfNeeded;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.items.armor.AugmentBuilder.AugmentCategory;
import gregtech.api.items.armor.MechArmorAugmentRegistries.ArmorType;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Augments;
import gregtech.api.util.GTTextBuilder;
import gregtech.api.util.GTUtility;

public class ItemAugment extends ItemAugmentAbstract {

    public final Augments augment;

    public ItemAugment(Augments augment) {
        super(augment);
        this.augment = augment;
    }

    @Override
    protected void addAdditionalToolTips(List<String> desc, ItemStack augmentStack, EntityPlayer player) {
        boolean showAllInfo = showAllInfo();

        desc.add(getCategoryText(augment.getCategory()));

        addSeparatorIfNeeded(desc);

        if (showAllInfo) {
            desc.add(
                GTUtility.translate(
                    "GT5U.armor.tooltip.energycoreminimum",
                    augment.getMinimumCore()
                        .getLocalizedName()));

            addSeparatorIfNeeded(desc);

            if (augment.getMaxStack() > 1) {
                desc.add(
                    GTUtility.translate(
                        "GT5U.armor.tooltip.maxstack",
                        GTTextBuilder.VALUE.toString() + augment.getMaxStack()));
            }

            addSeparatorIfNeeded(desc);

            desc.add(GRAY + GTUtility.translate("GT5U.armor.tooltip.applicable"));
            for (ArmorType armor : augment.getAllowedArmorTypes()) {
                desc.add(
                    GRAY + "- "
                        + armor.getItem()
                            .get(1)
                            .getDisplayName());
            }

            addSeparatorIfNeeded(desc);
        }

        if (augment.hasTooltip()) {
            desc.add(augment.getTooltip());
        }

        addSeparatorIfNeeded(desc);

        super.addAdditionalToolTips(desc, augmentStack, player);
    }

    private static String getCategoryText(AugmentCategory c) {
        switch (c) {
            case Protection -> {
                return GTUtility
                    .translate("GT5U.armor.tooltip.category", GTUtility.translate("GT5U.armor.tooltip.protection"));
            }
            case Movement -> {
                return GTUtility
                    .translate("GT5U.armor.tooltip.category", GTUtility.translate("GT5U.armor.tooltip.movement"));
            }
            case Utility -> {
                return GTUtility
                    .translate("GT5U.armor.tooltip.category", GTUtility.translate("GT5U.armor.tooltip.utility"));
            }
            case Prismatic -> {
                return GTUtility
                    .translate("GT5U.armor.tooltip.category", GTUtility.translate("GT5U.armor.tooltip.prismatic"));
            }
            default -> {
                return "";
            }
        }
    }
}
