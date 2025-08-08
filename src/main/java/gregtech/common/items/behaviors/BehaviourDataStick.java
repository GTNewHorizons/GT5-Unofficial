package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;

public class BehaviourDataStick extends BehaviourNone {

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        String tString = GTUtility.ItemNBT.getBookTitle(aStack);
        if (GTUtility.isStringValid(tString)) {
            aList.add(tString);
        }
        tString = GTUtility.ItemNBT.getBookAuthor(aStack);
        if (GTUtility.isStringValid(tString)) {
            aList.add("by " + tString);
        }
        short tMapID = GTUtility.ItemNBT.getMapID(aStack);
        if (tMapID >= 0) {
            aList.add("Map ID: " + tMapID);
        }
        tString = GTUtility.ItemNBT.getPunchCardData(aStack);
        if (GTUtility.isStringValid(tString)) {
            aList.add("Punch Card Data");
            int i = 0;
            int j = tString.length();
            for (; i < j; i += 64) {
                aList.add(tString.substring(i, Math.min(i + 64, j)));
            }
        }
        short sTier = GTUtility.ItemNBT.getNBT(aStack)
            .getShort("rocket_tier");
        if (sTier > 0 && sTier < 100) {
            aList.add("Rocket Schematic Tier: " + sTier);
        } else if (sTier >= 100) {
            switch (sTier) {
                case 100 -> aList.add("Moonbuggy Schematic");
                case 101 -> aList.add("Cargo-Rocket Schematic");
                case 102 -> aList.add("Astro-Miner Schematic");
            }
        }
        long lastUpdate = GTUtility.ItemNBT.getNBT(aStack)
            .getLong("lastUpdate");
        if (lastUpdate != 0) aList.add(String.format("Last update at: %tc", lastUpdate));

        return aList;
    }
}
