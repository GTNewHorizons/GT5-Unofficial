package gregtech.api.items;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorBase;

public class ItemAugment extends ItemAugmentAbstract {

    public static final int CATEGORY_PROTECTION = 1;
    public static final int CATEGORY_MOVEMENT = 2;
    public static final int CATEGORY_UTILITY = 3;

    private final int category;

    public ItemAugment(AugmentBuilder builder) {
        super(
            builder.aUnlocalized,
            builder.aEnglish,
            builder.aEnglishTooltip,
            builder.validArmors,
            builder.attachedBehaviors,
            builder.requiredBehaviors,
            builder.incompatibleBehaviors,
            builder.visDiscount);
        this.category = builder.category;
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        if (!validArmors.isEmpty()) {
            aList.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("GT5U.armor.tooltip.applicable"));
            for (MechArmorBase armor : validArmors)
                aList.add("-" + StatCollector.translateToLocal(armor.getUnlocalizedName() + ".name"));
        }
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        aList.add(getCategoryText(category));
    }

    private static String getCategoryText(int c) {
        switch (c) {
            case CATEGORY_PROTECTION -> {
                return StatCollector.translateToLocalFormatted(
                    "GT5U.armor.tooltip.category",
                    EnumChatFormatting.GOLD + StatCollector.translateToLocal("GT5U.armor.tooltip.protection"));
            }
            case CATEGORY_MOVEMENT -> {
                return StatCollector.translateToLocalFormatted(
                    "GT5U.armor.tooltip.category",
                    EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.armor.tooltip.movement"));
            }
            case CATEGORY_UTILITY -> {
                return StatCollector.translateToLocalFormatted(
                    "GT5U.armor.tooltip.category",
                    EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("GT5U.armor.tooltip.utility"));
            }
            default -> {
                return "";
            }
        }
    }

    public static class AugmentBuilder {

        private final String aUnlocalized, aEnglish, aEnglishTooltip;

        private Collection<MechArmorBase> validArmors;
        private Collection<IArmorBehavior> attachedBehaviors = Collections.emptyList();
        private Collection<IArmorBehavior> requiredBehaviors = Collections.emptyList();
        private Collection<IArmorBehavior> incompatibleBehaviors = Collections.emptyList();
        private int visDiscount = 0;
        private int category = CATEGORY_PROTECTION;

        public AugmentBuilder(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
            this.aUnlocalized = aUnlocalized;
            this.aEnglish = aEnglish;
            this.aEnglishTooltip = aEnglishTooltip;
        }

        public AugmentBuilder validArmors(Collection<MechArmorBase> validArmors) {
            this.validArmors = validArmors;
            return this;
        }

        public AugmentBuilder attachedBehaviors(Collection<IArmorBehavior> attachedBehaviors) {
            this.attachedBehaviors = attachedBehaviors;
            return this;
        }

        public AugmentBuilder requiredBehaviors(Collection<IArmorBehavior> requiredBehaviors) {
            this.requiredBehaviors = requiredBehaviors;
            return this;
        }

        public AugmentBuilder incompatibleBehaviors(Collection<IArmorBehavior> incompatibleBehaviors) {
            this.incompatibleBehaviors = incompatibleBehaviors;
            return this;
        }

        public AugmentBuilder visDiscount(int visDiscount) {
            this.visDiscount = visDiscount;
            return this;
        }

        public AugmentBuilder category(int category) {
            this.category = category;
            return this;
        }

        public ItemAugment build() {
            return new ItemAugment(this);
        }
    }
}
