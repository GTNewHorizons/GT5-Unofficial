package gregtech.api.items;

import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorBase;

public abstract class ItemAugmentAbstract extends GTGenericItem {

    // The behaviors that will be activated by this augment
    final Collection<IArmorBehavior> attachedBehaviors;

    // Behavior dependencies
    final Collection<IArmorBehavior> requiredBehaviors;
    final Collection<IArmorBehavior> incompatibleBehaviors;

    // Compatible items
    final Collection<MechArmorBase> validArmors;

    int visDiscount = 0;

    public ItemAugmentAbstract(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<MechArmorBase> validArmors, Collection<IArmorBehavior> attachedBehaviors,
        Collection<IArmorBehavior> requiredBehaviors, Collection<IArmorBehavior> incompatibleBehaviors,
        int visDiscount) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.validArmors = validArmors;
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = requiredBehaviors;
        this.incompatibleBehaviors = incompatibleBehaviors;
        this.visDiscount = visDiscount;
        addBehaviorsToArmor();
    }

    private void addBehaviorsToArmor() {
        for (MechArmorBase armor : validArmors) {
            for (IArmorBehavior behavior : attachedBehaviors) {
                armor.addBehavior(behavior);
            }
        }
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        if (!attachedBehaviors.isEmpty()) {
            aList.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.armor.tooltip.effects"));
            for (IArmorBehavior behavior : attachedBehaviors) aList.add(
                "-" + behavior.getBehaviorName()
                    + (behavior.isStackable() ? StatCollector.translateToLocal("GT5U.armor.tooltip.stackable") : ""));
        }
        if (!requiredBehaviors.isEmpty()) {
            aList.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("GT5U.armor.tooltip.requires"));
            for (IArmorBehavior behavior : requiredBehaviors) aList.add("-" + behavior.getBehaviorName());
        }
        if (!incompatibleBehaviors.isEmpty()) {
            aList.add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.armor.tooltip.incompatible"));
            for (IArmorBehavior behavior : incompatibleBehaviors) aList.add("-" + behavior.getBehaviorName());
        }
        if (visDiscount > 0) {
            aList.add(
                EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount")
                    + ": "
                    + visDiscount
                    + "%");
        }
        super.addAdditionalToolTips(aList, aStack, aPlayer);
    }

    public Collection<IArmorBehavior> getAttachedBehaviors() {
        return attachedBehaviors;
    }

    public Collection<IArmorBehavior> getRequiredBehaviors() {
        return requiredBehaviors;
    }

    public Collection<IArmorBehavior> getIncompatibleBehaviors() {
        return incompatibleBehaviors;
    }

    public Collection<MechArmorBase> getValidArmors() {
        return validArmors;
    }

    public int getVisDiscount() {
        return visDiscount;
    }
}
