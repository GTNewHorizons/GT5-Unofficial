package gtPlusPlus.api.helpers;

import java.util.HashMap;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.multi.SpecialMultiBehaviour;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.util.SpecialBehaviourTooltipHandler;

public class GregtechPlusPlus_API {

    public static class Multiblock_API {

        private static final HashMap<String, SpecialMultiBehaviour> mSpecialBehaviourItemMap = new HashMap<String, SpecialMultiBehaviour>();

        /**
         * Register a special behaviour for GT++ Multis to listen use.
         * 
         * @param aBehaviour - An Object which has extended {@link SpecialMultiBehaviour}'s base implementation.
         * @return - Did this behaviour register properly?
         */
        public static boolean registerSpecialMultiBehaviour(SpecialMultiBehaviour aBehaviour) {
            if (aBehaviour.getTriggerItem() == null || aBehaviour.getTriggerItemTooltip() == null
                    || aBehaviour.getTriggerItemTooltip().length() <= 0) {
                Logger.INFO(
                        "Failed to attach custom multiblock logic to "
                                + ItemUtils.getItemName(aBehaviour.getTriggerItem()));
                return false;
            }
            mSpecialBehaviourItemMap.put("UniqueKey_" + aBehaviour.hashCode(), aBehaviour);
            SpecialBehaviourTooltipHandler
                    .addTooltipForItem(aBehaviour.getTriggerItem(), aBehaviour.getTriggerItemTooltip());
            Logger.INFO("Attached custom multiblock logic to " + ItemUtils.getItemName(aBehaviour.getTriggerItem()));
            return true;
        }

        public static final HashMap<String, SpecialMultiBehaviour> getSpecialBehaviourItemMap() {
            return mSpecialBehaviourItemMap;
        }
    }
}
