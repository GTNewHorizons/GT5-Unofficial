package gregtech.api.items;

import java.util.Collection;

import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorLoader;

public class ItemAugmentCore extends ItemAugmentBase {

    private int coreid;

    public ItemAugmentCore(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> behaviors, int coreid) {
        super(aUnlocalized, aEnglish, aEnglishTooltip, MechArmorLoader.AllMechArmor, behaviors);
        this.coreid = coreid;
    }

    public int getCoreid() {
        return coreid;
    }
}
