package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;

public class BehaviourSwitchMode extends BehaviourNone {

    public BehaviourSwitchMode() {}

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        super.getAdditionalToolTips(aItem, aList, aStack);
        if ((aItem instanceof MetaGeneratedTool itemTool)) {
            final int maxMode = itemTool.getToolMaxMode(aStack);
            if (maxMode > 1) {
                aList.add(StatCollector.translateToLocal("gt.behaviour.switch_mode.tooltip"));
            }
        }

        return aList;
    }

}
