package gregtech.api.items;

import java.util.Collection;
import java.util.Collections;

import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorLoader;

public class ItemAugmentCore extends ItemAugmentAbstract {

    private int coreid;

    public ItemAugmentCore(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> behaviors, int coreid) {
        super(
            aUnlocalized,
            aEnglish,
            aEnglishTooltip,
            MechArmorLoader.AllMechArmor,
            behaviors,
            Collections.emptyList(),
            Collections.emptyList(),
            0);
        this.coreid = coreid;
    }

    public int getCoreid() {
        return coreid;
    }
}
