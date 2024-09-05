package gregtech.common.items;

import gregtech.api.items.GTGenericItem;

public class ItemTierDrone extends GTGenericItem {

    private final int level;

    public ItemTierDrone(String aUnlocalized, String aEnglish, String aEnglishTooltip, int level) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.level = level;
        this.setMaxStackSize(64);
    }

    public int getLevel() {
        return level;
    }
}
