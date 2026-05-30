package gregtech.common.items;

import gregtech.api.GregTechAPI;
import gregtech.api.items.GTGenericItem;

public class ItemRubberTreeTap extends GTGenericItem {

    public ItemRubberTreeTap(String unlocalized, String english, String tooltip, int maxUses) {
        super(unlocalized, english, tooltip);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        setMaxStackSize(1);
        setMaxDamage(maxUses);
        setNoRepair();
    }
}
