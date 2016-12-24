package com.detrav.items.tools;

import com.detrav.items.behaviours.BehaviourDetravToolSmartPlunger;
import gregtech.api.items.GT_MetaGenerated_Tool;

/**
 * Created by Detrav on 16.12.2016.
 */
public class DetravToolSmartPlunger64 extends DetravToolSmartPlunger {
    public float getMaxDurabilityMultiplier() {
        return 2F;
    }

    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        aItem.addItemBehavior(aID, new BehaviourDetravToolSmartPlunger(getToolDamagePerDropConversion(), 64000));
    }
}