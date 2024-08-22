package gregtech.common.items;

import gregtech.api.items.ItemGeneric;

public class GT_TierDrone extends ItemGeneric {

    private final int level;

    public GT_TierDrone(String aUnlocalized, String aEnglish, String aEnglishTooltip, int level) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.level = level;
        this.setMaxStackSize(64);
    }

    public int getLevel() {
        return level;
    }
}
