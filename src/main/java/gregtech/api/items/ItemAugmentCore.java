package gregtech.api.items;

import java.util.Collection;

import gregtech.api.items.armor.behaviors.IArmorBehavior;

public class ItemAugmentCore extends ItemAugmentBase {

    public ItemAugmentCore(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> behaviors, int coreid) {
        super(aUnlocalized, aEnglish, aEnglishTooltip, behaviors, coreid);
    }

    @Override
    public boolean isSimpleAugment() {
        return false;
    }
}
