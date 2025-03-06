package gregtech.api.items;

import gregtech.api.items.armor.behaviors.IArmorBehavior;

import java.util.Collection;

public class ItemAugmentFrame extends ItemAugmentBase {

    private int frameid;

    public ItemAugmentFrame(String aUnlocalized, String aEnglish, String aEnglishTooltip, Collection<IArmorBehavior> behaviors, int frameid) {
        super(aUnlocalized, aEnglish, aEnglishTooltip, behaviors);
        this.frameid = frameid;
    }

    public int getFrameid() {
        return frameid;
    }
}
