package gregtech.api.items;

import java.util.Collection;

import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorLoader;

public class ItemAugmentFrame extends ItemAugmentBase {

    private int frameid;

    public ItemAugmentFrame(String aUnlocalized, String aEnglish, String aEnglishTooltip,
        Collection<IArmorBehavior> behaviors, int frameid) {
        super(aUnlocalized, aEnglish, aEnglishTooltip, MechArmorLoader.AllMechArmor, behaviors);
        this.frameid = frameid;
    }

    public int getFrameid() {
        return frameid;
    }
}
