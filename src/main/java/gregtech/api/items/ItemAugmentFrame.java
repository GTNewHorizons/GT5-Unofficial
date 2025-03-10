package gregtech.api.items;

import java.util.Collection;

import gregtech.api.items.armor.behaviors.IArmorBehavior;

public class ItemAugmentFrame extends ItemAugmentBase {

    public ItemAugmentFrame(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> behaviors, int id, String[] size) {
        super(aUnlocalized, aEnglish, aEnglishTooltip, behaviors, id);
        this.size = size;
    }

    @Override
    public boolean isSimpleAugment() {
        return false;
    }
}
