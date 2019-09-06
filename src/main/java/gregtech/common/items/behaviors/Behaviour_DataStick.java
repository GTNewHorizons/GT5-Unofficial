package gregtech.common.items.behaviors;

import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import java.util.List;

public class Behaviour_DataStick
        extends Behaviour_None {
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        String tString = GT_Utility.ItemNBT.getBookTitle(aStack);
        if (GT_Utility.isStringValid(tString)) {
            aList.add(tString);
        }
        tString = GT_Utility.ItemNBT.getBookAuthor(aStack);
        if (GT_Utility.isStringValid(tString)) {
            aList.add("by " + tString);
        }
        short tMapID = GT_Utility.ItemNBT.getMapID(aStack);
        if (tMapID >= 0) {
            aList.add("Map ID: " + tMapID);
        }
        tString = GT_Utility.ItemNBT.getPunchCardData(aStack);
        if (GT_Utility.isStringValid(tString)) {
            aList.add("Punch Card Data");
            int i = 0;int j = tString.length();
            for (; i < j; i += 64) {
                aList.add(tString.substring(i, Math.min(i + 64, j)));
            }
        }
        short sTier = GT_Utility.ItemNBT.getNBT(aStack).getShort("rocket_tier");
        if (sTier > 0 && sTier<100) {
        	 aList.add("Rocket Schematic Tier: " + sTier);
        }else if (sTier>=100) {
        	switch (sTier) {
        	case 100: {
        		aList.add("Moonbuggy Schematic");
        		break;
        	}
        	case 101: {
        		aList.add("Cargo-Rocket Schematic");
        		break;
        	}
        	case 102: {
        		aList.add("Astro-Miner Schematic");
        		break;
        	}
        	}
        }
       
        return aList;
    }
}
