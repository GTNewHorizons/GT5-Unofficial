package gregtech.common.items;

import gregtech.api.items.GT_Generic_Item;

public class GT_TierDrone extends GT_Generic_Item {

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
