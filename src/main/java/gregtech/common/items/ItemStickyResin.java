package gregtech.common.items;

import gregtech.api.items.GTGenericItem;

public class ItemStickyResin extends GTGenericItem {

    public ItemStickyResin(final String aUnlocalized, final String aEnglish, final String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setCreativeTab(gregtech.api.GregTechAPI.TAB_GREGTECH_MATERIALS);
        setMaxStackSize(64);
    }
}
