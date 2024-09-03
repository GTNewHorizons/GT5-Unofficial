package gtPlusPlus.xmod.bartworks;

import net.minecraft.item.ItemStack;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.OrePrefixes;

public class BWUtils {

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, short werkstoffID, int amount) {
        Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get(werkstoffID);
        if (werkstoff == null) return null;
        return WerkstoffLoader.getCorrespondingItemStackUnsafe(orePrefixes, werkstoff, amount);
    }
}
