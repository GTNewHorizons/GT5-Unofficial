package gtPlusPlus.xmod.bartworks;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.material.MU;

public class BWUtils {

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, short werkstoffID, int amount) {
        Material material = Materials2WerkstoffIndex.get(werkstoffID);
        if (material == null) return null;
        return MU.stack(orePrefixes, material, amount);
    }
}
