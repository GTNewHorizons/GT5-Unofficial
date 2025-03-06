package gregtech.api.items;

import gregtech.api.items.armor.behaviors.IArmorBehavior;

import java.util.Collection;
import java.util.Collections;

public class ItemAugmentBase extends GTGenericItem {

    private Collection<IArmorBehavior> attachedBehaviors;

    public ItemAugmentBase(String aUnlocalized, String aEnglish, String aEnglishTooltip, Collection<IArmorBehavior> behaviors) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        attachedBehaviors = behaviors;
    }

    public Collection<IArmorBehavior> getAttachedBehaviors() {
        return attachedBehaviors;
    }
}
