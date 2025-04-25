package gregtech.api.items;

import java.util.Collection;
import java.util.Collections;

import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorLoader;

public class ItemAugmentFrame extends ItemAugmentAbstract {

    private int frameid;

    public ItemAugmentFrame(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> behaviors, int frameid) {
        super(
            aUnlocalized,
            aEnglish,
            aEnglishTooltip,
            MechArmorLoader.AllMechArmor,
            behaviors,
            Collections.emptyList(),
            Collections.emptyList(),
            0);
        this.frameid = frameid;
    }

    public int getFrameid() {
        return frameid;
    }
}
