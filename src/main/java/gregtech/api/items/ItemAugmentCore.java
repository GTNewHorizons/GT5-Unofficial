package gregtech.api.items;

import gregtech.api.items.armor.behaviors.IArmorBehavior;

import java.util.Collection;

public class ItemAugmentCore extends ItemAugmentBase {

    private int coreid;

    public ItemAugmentCore(String aUnlocalized, String aEnglish, String aEnglishTooltip, Collection<IArmorBehavior> behaviors, int coreid) {
        super(aUnlocalized, aEnglish, aEnglishTooltip, behaviors);
        this.coreid = coreid;
    }

    public int getCoreid() {
        return coreid;
    }
}
