package gtPlusPlus.xmod.bartworks;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.OrePrefixes;

public class BW_Utils {

    public static ArrayList<ItemStack> getAll(int aStackSize) {
        ArrayList<ItemStack> aItems = new ArrayList<>();
        aItems.add(BW_NonMeta_MaterialItems.TiberiumCell_1.get(aStackSize));
        aItems.add(BW_NonMeta_MaterialItems.TiberiumCell_2.get(aStackSize));
        aItems.add(BW_NonMeta_MaterialItems.TiberiumCell_4.get(aStackSize));
        aItems.add(BW_NonMeta_MaterialItems.TheCoreCell.get(aStackSize));
        return aItems;
    }

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, short werkstoffID, int amount) {
        Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get(werkstoffID);
        if (werkstoff == null) return null;
        return WerkstoffLoader.getCorrespondingItemStackUnsafe(orePrefixes, werkstoff, amount);
    }
}
