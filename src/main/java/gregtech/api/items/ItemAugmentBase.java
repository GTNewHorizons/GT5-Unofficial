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

    private final int id;

    /*
     * ##
     * #
     * ##
     */
    public String[] size = new String[] { "##", "#~", "##" };

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> attachedBehaviors, Collection<IArmorBehavior> requiredBehaviors,
        Collection<IArmorBehavior> incompatibleBehaviors, int id) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.id = id;
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = requiredBehaviors;
        this.incompatibleBehaviors = incompatibleBehaviors;
    }

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> attachedBehaviors, int id) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.id = id;
        this.attachedBehaviors = attachedBehaviors;
        this.requiredBehaviors = Collections.emptyList();
        this.incompatibleBehaviors = Collections.emptyList();
    }

    public boolean isSimpleAugment() {
        return true;
    }

    public int getId() {
        return this.id;
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
