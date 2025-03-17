package gregtech.api.items;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import gregtech.api.items.armor.behaviors.IArmorBehavior;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class ItemAugmentBase extends GTGenericItem {

    // The behaviors that will be activated by this augment
    private final Collection<IArmorBehavior> attachedBehaviors;

    // Behavior dependencies
    private final Collection<IArmorBehavior> requiredBehaviors;
    private final Collection<IArmorBehavior> incompatibleBehaviors;

    private int visDiscount = 0;

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> attachedBehaviors, Collection<IArmorBehavior> requiredBehaviors,
        Collection<IArmorBehavior> incompatibleBehaviors) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = requiredBehaviors;
        this.incompatibleBehaviors = incompatibleBehaviors;
    }

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> attachedBehaviors, Collection<IArmorBehavior> requiredBehaviors,
        Collection<IArmorBehavior> incompatibleBehaviors, int visDiscount) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = requiredBehaviors;
        this.incompatibleBehaviors = incompatibleBehaviors;
        this.visDiscount = visDiscount;
    }

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> attachedBehaviors) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = Collections.emptyList();
        this.incompatibleBehaviors = Collections.emptyList();
    }

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> attachedBehaviors, int visDiscount) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = Collections.emptyList();
        this.incompatibleBehaviors = Collections.emptyList();
        this.visDiscount = visDiscount;
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        if (visDiscount > 0) {
            aList.add(
                EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount")
                    + ": " + visDiscount + "%");
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

    public int getVisDiscount() {
        return visDiscount;
    }
}
