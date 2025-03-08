package gregtech.api.items;

import java.util.Collection;
import java.util.Collections;

import gregtech.api.items.armor.behaviors.IArmorBehavior;

public class ItemAugmentBase extends GTGenericItem {

    // The behaviors that will be activated by this augment
    private final Collection<IArmorBehavior> attachedBehaviors;

    // Behavior dependencies
    private final Collection<IArmorBehavior> requiredBehaviors;
    private final Collection<IArmorBehavior> incompatibleBehaviors;

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> attachedBehaviors, Collection<IArmorBehavior> requiredBehaviors,
        Collection<IArmorBehavior> incompatibleBehaviors) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = requiredBehaviors;
        this.incompatibleBehaviors = incompatibleBehaviors;
    }

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> attachedBehaviors) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = Collections.emptyList();
        this.incompatibleBehaviors = Collections.emptyList();
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
}
